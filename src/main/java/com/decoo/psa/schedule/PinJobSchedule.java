package com.decoo.psa.schedule;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.decoo.psa.constants.IPFSConstants;
import com.decoo.psa.dao.PinFileDao;
import com.decoo.psa.dao.PinJobsDao;
import com.decoo.psa.domain.PinFile;
import com.decoo.psa.domain.PinJobs;
import com.decoo.psa.exception.IPFSException;
import com.decoo.psa.service.ipfs.IPFSService;
import com.decoo.psa.service.pinning.PinFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class PinJobSchedule {

    @Autowired
    private PinJobsDao pinJobsDao;

    @Autowired
    private IPFSService ipfsService;

    @Autowired
    private PinFileService pinFileService;

    @Autowired
    private PinFileDao pinFileDao;

    @Scheduled(cron = "0 */2 * * * *")
    public void updatePinJobsStatus() {
        log.info("update pin queue status start");
        // update every 5 minutes max limit 1000
        IPage<PinJobs> page = pinJobsDao.selectPage(new Page<>(1,
                IPFSConstants.IPFS_JOBS_BATCH_UPDATE_SIZE, false), new LambdaQueryWrapper<PinJobs>().eq(PinJobs::getJobStatus,
                PinJobs.PinJobStatus.PINNING.getCode()).eq(PinJobs::getDeleted, PinJobs.PinJobDeleted.UNDELETED.getCode())
                .orderByAsc(PinJobs::getId));
        long total = page.getRecords() == null ? 0 : page.getRecords().size();
        if (total > 0l) {
            for (PinJobs pinJobs : page.getRecords()) {
                Date date = new Date();
                pinJobs.setLastUpdateAt(date);
                switch (ipfsService.getClusterPinStatus(pinJobs.getCid())) {
                    case PINNED:
                        try {
                            pinFileService.convertPinJobsToPinFile(pinJobs);
                        } catch (IPFSException e) {
                            log.error("convertPinQueueToPinFile failed: {}", pinJobs.getCid());
                        }
                        break;
                    case PINNING:
                        pinJobsDao.updateById(pinJobs);
                        break;
                    default:
                        pinJobsDao.updateById(pinJobs
                                .setJobStatus(PinJobs.PinJobStatus.FAILED.getCode())
                                .setLastUpdateAt(date));
                        break;
                }
                log.info("pinJob id: {}, state: {}", pinJobs.getId(), pinJobs.getJobStatus());
            }
        }
        log.info("update pin queue status finished");
    }

    @Scheduled(cron = "0 */5 * * * *")
    public void rePinFileInQueue() {
        log.info("re pin queue start");
        // update every 5 minutes max limit 100
        IPage<PinJobs> page = pinJobsDao.selectPage(new Page<>(1,
                IPFSConstants.IPFS_JOBS_BATCH_UPDATE_SIZE, false), new LambdaQueryWrapper<PinJobs>().eq(PinJobs::getJobStatus,
                PinJobs.PinJobStatus.FAILED.getCode()).eq(PinJobs::getDeleted, PinJobs.PinJobDeleted.UNDELETED.getCode())
                .orderByAsc(PinJobs::getId));
        long total = page.getRecords() == null ? 0 : page.getRecords().size();
        if (total > 0l) {
            for (PinJobs pinJobs : page.getRecords()) {
                Date date = new Date();
                switch (ipfsService.getClusterPinStatus(pinJobs.getCid())) {
                    case PINNED:
                        try {
                            pinFileService.convertPinJobsToPinFile(pinJobs);
                        } catch (IPFSException e) {
                            log.error("convertPinQueueToPinFile failed: {}", pinJobs.getCid());
                        }
                        break;
                    case PINNING:
                        pinJobs.setLastUpdateAt(date).setJobStatus(PinJobs.PinJobStatus.PINNING.getCode());
                        pinJobsDao.updateById(pinJobs);
                        log.info("rePinFile cid: {} is pinning", pinJobs.getCid());
                        break;
                    default:
                        if (pinJobs.getRetryTimes() > IPFSConstants.IPFS_PIN_FAILED_RETRY_COUNT) {
                            pinFileService.convertExpirePinJobs(pinJobs);
                        } else {
                            PinFile pinFile = pinFileDao.selectOne(new LambdaQueryWrapper<PinFile>()
                                    .eq(PinFile::getUserId, pinJobs.getUserId())
                                    .eq(PinFile::getIpfsPinHash, pinJobs.getCid()));
                            if (pinFile.getMetaData() != null && pinFile.getMetaData().getOrigins() != null
                                    && !pinFile.getMetaData().getOrigins().isEmpty()) {
                                ipfsService.swarmConnect(pinFile.getMetaData().getOrigins());
                            }
                            ipfsService.pinClusterByCid(pinJobs.getCid());
                            pinJobs.setRetryTimes(pinJobs.getRetryTimes() + 1)
                                    .setLastUpdateAt(date)
                                    .setJobStatus(PinJobs.PinJobStatus.PINNING.getCode());
                            pinJobsDao.updateById(pinJobs);
                        }
                        break;
                }
            }
        }
    }
}
