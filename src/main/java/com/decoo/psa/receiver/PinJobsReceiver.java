package com.decoo.psa.receiver;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.decoo.psa.constants.IPFSConstants;
import com.decoo.psa.dao.PinFileDao;
import com.decoo.psa.dao.PinJobsDao;
import com.decoo.psa.domain.PinFile;
import com.decoo.psa.domain.PinJobs;
import com.decoo.psa.service.ipfs.IPFSService;
import com.decoo.psa.service.pinning.PinFileService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
@RabbitListener(queues = IPFSConstants.IPFS_PIN_BY_CID_JOB)
public class PinJobsReceiver {

    @Autowired
    private IPFSService ipfsService;

    @Autowired
    private PinFileService pinFileService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private PinJobsDao pinJobsDao;

    @Autowired
    private PinFileDao pinFileDao;

    @RabbitHandler
    public void receivePinFile(@Header(AmqpHeaders.CHANNEL) Channel channel, Message message, PinJobs pinJobs) {
        try {
            // ack first
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
            Date date = new Date();
            // get file ping stat
            IPFSConstants.ClusterPinStatus status = ipfsService.getClusterPinStatus(pinJobs.getCid());
            switch (status) {
                case PINNED:
                    log.info("receivePinFile cid: {} pinned success", pinJobs.getCid());
                    pinFileService.convertPinJobsToPinFile(pinJobs);
                    break;
                case PINNING:
                    log.info("receivePinFile cid: {} pinning", pinJobs.getCid());
                    break;
                default:
                    log.info("receivePinFile cid: {} status: {}", pinJobs.getCid(), status.getMsg());
                    PinFile pinFile = pinFileDao.selectOne(new LambdaQueryWrapper<PinFile>()
                            .eq(PinFile::getUserId, pinJobs.getUserId())
                            .eq(PinFile::getIpfsPinHash, pinJobs.getCid()));
                    if (pinFile.getMetaData() != null && pinFile.getMetaData().getOrigins() != null
                            && !pinFile.getMetaData().getOrigins().isEmpty()) {
                        ipfsService.swarmConnect(pinFile.getMetaData().getOrigins());
                    }
                    // pin file update queue status
                    ipfsService.pinClusterByCid(pinJobs.getCid());
                    pinJobsDao.updateById(pinJobs.setLastUpdateAt(date)
                            .setJobStatus(PinJobs.PinJobStatus.PINNING.getCode()));
                    break;
            }
        } catch (Throwable e) {
            log.error("pin file by cid err: {}, pinFileQueueId: {}", e.getMessage(), pinJobs.getId());
        }
    }
}
