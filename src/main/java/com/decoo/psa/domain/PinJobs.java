package com.decoo.psa.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@Data
@Accessors(chain = true)
public class PinJobs implements Serializable {
    private static final long serialVersionUID = -90309503944927670L;

    public enum PinJobStatus {
        QUEUED(0, "queued"),
        PINNING(1, "pinning"),
        PINNED(2, "pinned"),
        FAILED(3, "failed"),
        EXPIRED(4, "expired");

        private Integer code;
        private String msg;

        PinJobStatus(Integer code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public Integer getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }

        public static PinJobStatus getStatusByCode(Integer code) {
            for (PinJobStatus status:values()) {
                if (status.code.equals(code)) {
                    return status;
                }
            }
            return null;
        }
    }

    public enum PinJobType {
        PIN_FILE(0, "pin file"),
        PIN_CID(1, "pin by cid"),
        PIN_BY_S3(2, "pin by s3");

        private Integer code;
        private String msg;

        PinJobType(Integer code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public Integer getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }

    }

    public enum PinJobDeleted {
        UNDELETED(0, "undeleted"),
        DELETED(1, "deleted");

        private Integer code;
        private String msg;

        PinJobDeleted(Integer code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public Integer getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }

        public static PinJobDeleted getStatusByCode(Integer code) {
            for (PinJobDeleted status:values()) {
                if (status.code.equals(code)) {
                    return status;
                }
            }
            return null;
        }
    }

    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * job uuid
     */
    private String uuid;
    /**
     * pin file id
     */
    private Long pinFileId;
    /**
     * user id
     */
    private Long userId;
    /**
     * ipfs cid
     */
    private String cid;
    /**
     * s3 url
     */
    private String s3Url;
    /**
     * 0: pin by file, 1: pin by cid, 2: pin by s3 url
     */
    private Integer jobType;
    /**
     * 0: queued, 1: pinning, 2: pinned, 3: failed, 4: expired
     */
    private Integer jobStatus;
    /**
     * retry times for pin by cid
     */
    private Integer retryTimes;

    private Date createAt;

    private Date lastUpdateAt;
    /**
     * 1: deleted, 0: undeleted
     */
    private Integer deleted;

    public PinJob convertPinJob() {
        PinJobStatus status = PinJobStatus.getStatusByCode(jobStatus);
        status = status == null ? PinJobStatus.FAILED : status;
        return new PinJob().setId(uuid).setPinHash(cid).setStatus(status.msg);
    }
}
