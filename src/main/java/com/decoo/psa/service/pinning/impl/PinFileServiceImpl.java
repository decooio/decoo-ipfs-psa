package com.decoo.psa.service.pinning.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import com.decoo.psa.constants.IPFSConstants;
import com.decoo.psa.dao.CrustOrderDao;
import com.decoo.psa.dao.PinFileDao;
import com.decoo.psa.dao.PinJobsDao;
import com.decoo.psa.domain.*;
import com.decoo.psa.exception.CrustException;
import com.decoo.psa.exception.IPFSException;
import com.decoo.psa.service.crust.CrustService;
import com.decoo.psa.service.ipfs.IPFSService;
import com.decoo.psa.service.pinning.PinFileService;
import com.decoo.psa.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
@Slf4j
@Service
public class PinFileServiceImpl implements PinFileService {

    @Autowired
    private PinFileDao pinFileDao;

    @Autowired
    private PinJobsDao pinJobsDao;

    @Autowired
    private IPFSService ipfsService;

    @Autowired
    private CrustService crustService;

    @Autowired
    private CrustOrderDao crustOrderDao;

    @Transactional
    @Override
    public PinFile createBasePinJobAndPinFile(PinJobs jobs, PinFile pinFile, DecooMetadata decooMetadata) {
        Date date = new Date();
        if (pinFile != null && pinFile.getId() != null) {
            pinFileDao.updateById(pinFile.setQueueId(jobs.getId())
                    .setLastUpdateAt(date)
                    .setQueueId(jobs.getId())
                    .setState(PinFile.FileState.IN_PIN_QUEUE.getCode())
                    .setMetaData(decooMetadata));
        } else {
            pinFile = new PinFile()
                    .setFileName(jobs.getCid())
                    .setState(PinFile.FileState.IN_PIN_QUEUE.getCode())
                    .setLastUpdateAt(date)
                    .setCreateAt(date)
                    .setUserId(jobs.getUserId())
                    .setMetaData(decooMetadata)
                    .setIpfsPinHash(jobs.getCid())
                    .setQueueId(jobs.getId())
                    .setThirdParty(IPFSConstants.PinThirdParty.DECOO.getCode())
                    .setUuid(CommonUtils.getUUID());
            pinFileDao.insert(pinFile);
        }
        jobs.setDeleted(PinJobs.PinJobDeleted.UNDELETED.getCode());
        if (jobs.getId() == null) {
            pinJobsDao.insert(jobs.setPinFileId(pinFile.getId()));
        } else {
            pinJobsDao.updateById(jobs.setPinFileId(pinFile.getId()));
        }
        return pinFile;
    }

    @Transactional
    @Override
    public PinJobs createPinJobAndUpsertPinFileByPinFile(PinFile pinFile, PinJobs.PinJobStatus status, PinJobs.PinJobType pinJobType) {
        if (pinFile.getId() == null) {
            pinFileDao.insert(pinFile);
        }
        PinJobs pinJobs = pinJobsDao.selectOne(new LambdaQueryWrapper<PinJobs>().eq(PinJobs::getCid, pinFile.getIpfsPinHash()).eq(PinJobs::getUserId, pinFile.getUserId()));
        Date now = new Date();
        if (pinJobs == null) {
            pinJobs = new PinJobs().setCid(pinFile.getIpfsPinHash())
                    .setPinFileId(pinFile.getId())
                    .setJobStatus(status.getCode())
                    .setJobType(pinJobType.getCode())
                    .setUserId(pinFile.getUserId())
                    .setLastUpdateAt(now)
                    .setCreateAt(now)
                    .setDeleted(PinJobs.PinJobDeleted.UNDELETED.getCode())
                    .setUuid(CommonUtils.getUUID());
            pinJobsDao.insert(pinJobs);
        } else {
            pinJobsDao.updateById(pinJobs
                    .setLastUpdateAt(now)
                    .setJobStatus(status.getCode())
                    .setJobType(pinJobType.getCode())
                    .setDeleted(PinJobs.PinJobDeleted.UNDELETED.getCode()));
        }
        pinFileDao.updateById(pinFile.setQueueId(pinJobs.getId()));
        return pinJobs;
    }

    @Transactional
    @Override
    public void convertPinJobsToPinFile(PinJobs pinJobs) throws IPFSException {
        Date now = new Date();
        PinFile pinFile = pinFileDao.selectOne(new LambdaQueryWrapper<PinFile>().eq(PinFile::getIpfsPinHash, pinJobs.getCid()).eq(PinFile::getUserId, pinJobs.getUserId()));
        // get file stat after pinned
        IPFSFile fileStat = ipfsService.fileStat("/ipfs/" + pinJobs.getCid(), ipfsService.getRandomHostByPeers());
        pinFile.setPinAt(now)
                .setLastUpdateAt(now)
                .setCumulativeSize(fileStat.getSize())
                .setState(PinFile.FileState.IN_IPFS_CLUSTER.getCode());
        pinFileDao.updateById(pinFile);
        pinJobs.setLastUpdateAt(now)
                .setJobStatus(PinJobs.PinJobStatus.PINNED.getCode());
        pinJobsDao.updateById(pinJobs);
        CrustOrderResult crustOrderResult;
        try {
            crustOrderResult = crustService.orderByCidAndSize(fileStat.getCid(), fileStat.getSize());
        } catch (CrustException e) {
            log.error("order failed");
            throw new IPFSException("crust order failed");
        }
        Date date = new Date();
        CrustOrder crustOrder = new CrustOrder();
        BeanUtils.copyProperties(crustOrderResult, crustOrder);
        crustOrder.setCreateAt(date).setLastUpdateAt(date).setPeerHost(IPFSConstants.DEFAULT_HOST);
        crustOrderDao.insert(crustOrder);
        pinFileDao.batchUpdateState(Arrays.asList(pinFile), PinFile.FileState.IN_ORDER.getCode(), crustOrder.getId());
    }

    @Transactional
    @Override
    public void convertExpirePinJobs(PinJobs pinJobs) {
        PinFile pinFile = pinFileDao.selectOne(new LambdaQueryWrapper<PinFile>()
                .eq(PinFile::getIpfsPinHash, pinJobs.getCid()).eq(PinFile::getUserId, pinJobs.getUserId()));
        if (pinFile != null) {
            pinFileDao.updateById(pinFile.setState(PinFile.FileState.EXPIRED.getCode()));
        }
        pinJobs.setLastUpdateAt(new Date()).setJobStatus(PinJobs.PinJobStatus.EXPIRED.getCode());
        pinJobsDao.updateById(pinJobs);
    }
}
