package com.decoo.psa.service.pinning.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.decoo.psa.constants.IPFSConstants;
import com.decoo.psa.dao.PinFileDao;
import com.decoo.psa.dao.PinJobsDao;
import com.decoo.psa.domain.*;
import com.decoo.psa.service.auth.AuthService;
import com.decoo.psa.service.ipfs.IPFSService;
import com.decoo.psa.service.pinning.PinFileService;
import com.decoo.psa.service.pinning.PinningService;

import com.decoo.psa.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.rmi.runtime.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
@Slf4j
@Service
public class PinningServiceImpl implements PinningService {

    @Autowired
    private IPFSService ipfsService;

    @Autowired
    private AuthService authService;

    @Autowired
    private PinFileDao pinFileDao;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private PinFileService pinFileService;

    @Autowired
    private PinJobsDao pinJobsDao;

    @Override
    public PinningResponse pinFileToIPFS(String fileName, InputStream inputStream, boolean wrapWithDirectory, IPFSConstants.CidVersion cidVersion, DecooMetadata decooMetadata) throws IOException {
        // upload file to ipfs
        IPFSFile ipfsFile = wrapWithDirectory ? ipfsService.addClusterInDefaultDir(fileName, inputStream, cidVersion)
                : ipfsService.addCluster(inputStream, cidVersion);
        Date date = new Date();
        Long uid = authService.uid();
        String cid = wrapWithDirectory ? ipfsFile.getFolderCid() : ipfsFile.getCid();
        Long fileSize = wrapWithDirectory ? ipfsFile.getFolderSize() : ipfsFile.getSize();
        PinFile.FileType fileType = wrapWithDirectory ? PinFile.FileType.FOLDER : PinFile.FileType.FILE;
        PinFile pinFile = pinFileDao.selectOne(new LambdaQueryWrapper<PinFile>().eq(PinFile::getUserId, uid)
                .eq(PinFile::getIpfsPinHash, cid));
        if (pinFile == null) {
            pinFile = new PinFile()
                    .setCreateAt(date)
                    .setFileType(fileType.getCode())
                    .setCumulativeSize(fileSize)
                    .setIpfsPinHash(ipfsFile.getCid())
                    .setThirdParty(IPFSConstants.PinThirdParty.DECOO.getCode())
                    .setUuid(CommonUtils.getUUID());
        }
        pinFile.setIpfsPinHash(cid)
                .setUserId(uid)
                .setPinAt(date)
                .setMetaData(decooMetadata)
                .setState(PinFile.FileState.IN_IPFS_CLUSTER.getCode())
                .setFileName(fileName)
                .setLastUpdateAt(date);
        PinJobs.PinJobStatus pinJobStatus = PinJobs.PinJobStatus.PINNED;
        PinJobs pinJobs = pinFileService.createPinJobAndUpsertPinFileByPinFile(pinFile, pinJobStatus, PinJobs.PinJobType.PIN_FILE);
        return new PinningResponse()
                .setPinHash(ipfsFile.getCid())
                .setPinDate(date)
                .setJobId(pinJobs.getUuid())
                .setJobStatus(pinJobStatus.getMsg())
                .setPinSize(ipfsFile.getSize());
    }

    @Override
    public PinningResponse pinByCid(PinningRequest pinningRequest) {
        Long uid = authService.uid();
        // find last one by id
        PinJobs pinJobs = pinJobsDao.selectOne(new LambdaQueryWrapper<PinJobs>()
                .eq(PinJobs::getCid, pinningRequest.getHashToPin())
                .eq(PinJobs::getUserId, uid));
        Date now = new Date();

        boolean needPin;
        if (pinJobs != null) {
            pinJobs.setLastUpdateAt(now)
                   .setDeleted(PinJobs.PinJobDeleted.UNDELETED.getCode());
            switch (PinJobs.PinJobStatus.getStatusByCode(pinJobs.getJobStatus())) {
                case PINNING:
                    pinJobsDao.updateById(pinJobs.setLastUpdateAt(now));
                    return new PinningResponse()
                            .setHashToPin(pinningRequest.getHashToPin())
                            .setJobId(pinJobs.getUuid())
                            .setJobStatus(PinJobs.PinJobStatus.PINNING.getMsg());
                case PINNED:
                    needPin = false;
                    break;
                default:
                    needPin = true;
                    pinJobs.setJobStatus(PinJobs.PinJobStatus.QUEUED.getCode())
                            .setLastUpdateAt(now).setRetryTimes(0);
                    break;
            }
        } else {
            pinJobs = new PinJobs()
                    .setUuid(CommonUtils.getUUID())
                    .setUserId(uid)
                    .setJobType(PinJobs.PinJobType.PIN_CID.getCode())
                    .setJobStatus(PinJobs.PinJobStatus.QUEUED.getCode());
            needPin = true;
        }
        PinFile pinFile = pinFileDao.selectOne(new LambdaQueryWrapper<PinFile>().eq(PinFile::getUserId, uid)
                .eq(PinFile::getIpfsPinHash, pinningRequest.getHashToPin()));
        pinFileService.createBasePinJobAndPinFile(pinJobs, pinFile, pinningRequest.getDecooMetadata());
        if (needPin) {
            rabbitTemplate.convertAndSend(IPFSConstants.IPFS_PIN_BY_CID_JOB, pinJobs);
        }
        return new PinningResponse()
                .setHashToPin(pinningRequest.getHashToPin())
                .setJobId(pinJobs.getUuid())
                .setJobStatus(PinJobs.PinJobStatus.getStatusByCode(pinJobs.getJobStatus()).getMsg());
    }

    @Override
    public PinStatus pinFromPinningService(Pin pin) {
        PinningRequest pinningRequest = new PinningRequest()
                .setHashToPin(pin.getCid())
                .setDecooMetadata(new DecooMetadata().setName(pin.getName())
                        .setKeyValues(pin.getMeta())
                        .setOrigins(pin.getOrigins()));
        Long uid = authService.uid();
        PinJobs pinJobs = pinJobsDao.selectOne(new LambdaQueryWrapper<PinJobs>()
                .eq(PinJobs::getCid, pinningRequest.getHashToPin())
                .eq(PinJobs::getUserId, uid));
        boolean needPin = pinJobs == null ? true : parsePinJobs(pinJobs);
        pinJobs = pinJobs == null ? convertPinJobs(pinningRequest, uid) : pinJobs;
        pinJobs.setDeleted(PinJobs.PinJobDeleted.UNDELETED.getCode())
                .setJobStatus(PinJobs.PinJobStatus.QUEUED.getCode());
        PinFile pinFile = pinFileDao.selectOne(new LambdaQueryWrapper<PinFile>().eq(PinFile::getUserId, uid)
                .eq(PinFile::getIpfsPinHash, pinningRequest.getHashToPin()));
        pinFile = pinFileService.createBasePinJobAndPinFile(pinJobs, pinFile, pinningRequest.getDecooMetadata());
        if (needPin) {
            rabbitTemplate.convertAndSend(IPFSConstants.IPFS_PIN_BY_CID_JOB, pinJobs);
        }
        return PinStatus.convertByPinFile(pinFile.setUuid(pinJobs.getUuid()), pinJobs.getJobStatus());
    }

    @Override
    public PinStatus getPinStatusByRequestId(String uuid) {
        Long uid = authService.uid();
        PinJobs pinJobs = pinJobsDao.selectOne(new LambdaQueryWrapper<PinJobs>().eq(PinJobs::getUuid, uuid).eq(PinJobs::getDeleted,
                PinJobs.PinJobDeleted.UNDELETED.getCode()));
        if (pinJobs != null) {
            PinFile pinFile = pinFileDao.selectOne(new LambdaQueryWrapper<PinFile>().eq(PinFile::getUserId, uid)
                    .eq(PinFile::getIpfsPinHash, pinJobs.getCid()).ne(PinFile::getState, PinFile.FileState.DELETED.getCode()));
            if (pinFile != null) {
                return PinStatus.convertByPinFile(pinFile.setUuid(pinJobs.getUuid()), pinJobs.getJobStatus());
            }
        }
        return null;
    }


    @Transactional
    @Override
    public void removeByRequestIdAndUid(String requestId, Long uid) {
        PinJobs pinJobs = pinJobsDao.selectOne(new LambdaQueryWrapper<PinJobs>().eq(PinJobs::getUuid, requestId));
        if (pinJobs != null) {
            pinJobs.setDeleted(PinJobs.PinJobDeleted.DELETED.getCode()).setLastUpdateAt(new Date());
            pinJobsDao.updateById(pinJobs);
            PinFile pinFile = pinFileDao.selectOne(new LambdaQueryWrapper<PinFile>()
                    .eq(PinFile::getIpfsPinHash, pinJobs.getCid())
                    .eq(PinFile::getUserId, uid));
            if (pinFile != null) {
                pinFile.setState(PinFile.FileState.DELETED.getCode());
                pinFileDao.updateById(pinFile);
            }
        }
    }

    private boolean parsePinJobs(PinJobs pinJobs) {
        Date now = new Date();
        boolean needPin = true;
        if (pinJobs != null) {
            switch (PinJobs.PinJobStatus.getStatusByCode(pinJobs.getJobStatus())) {
                case PINNING:
                    needPin = false;
                    break;
                case PINNED:
                    needPin = false;
                    break;
                default:
                    needPin = true;
                    pinJobs.setJobStatus(PinJobs.PinJobStatus.QUEUED.getCode())
                            .setLastUpdateAt(now).setRetryTimes(0);
                    break;
            }
        }
        return needPin;
    }

    private PinJobs convertPinJobs(PinningRequest pinningRequest, Long uid) {
        return new PinJobs()
                .setCid(pinningRequest.getHashToPin())
                .setUserId(uid)
                .setJobType(PinJobs.PinJobType.PIN_CID.getCode())
                .setJobStatus(PinJobs.PinJobStatus.QUEUED.getCode())
                .setUserId(uid)
                .setUuid(CommonUtils.getUUID());
    }
}
