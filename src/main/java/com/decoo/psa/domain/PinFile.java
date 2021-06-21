package com.decoo.psa.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.decoo.psa.constants.IPFSConstants;
import com.decoo.psa.handler.PinFileMetaDataTypeHandler;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * (PinFile)实体类
 *
 * @author makejava
 * @since 2021-05-08 17:26:57
 */
@Data
@Accessors(chain = true)
@TableName(autoResultMap = true)
public class PinFile implements Serializable {
    private static final long serialVersionUID = 752357726583039766L;
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long orderId;

    private String fileName;

    private String folderPinHash;
    /**
     * ipfs pinHash
     */
    private String ipfsPinHash;

    private Integer thirdParty;

    private String cosUrl;

    private String uuid;
    /**
     * ipfs metadata
     */
    @TableField(typeHandler = PinFileMetaDataTypeHandler.class)
    private DecooMetadata metaData;
    /**
     * cumulative pinSize in ipfs
     */
    private Long cumulativeSize;
    /**
     * user.id
     */
    private Long userId;
    /**
     * create pinDate
     */
    private Date createAt;
    /**
     * 1: deleted, 2: expired, 0: undeleted
     */
    private Integer state;
    /**
     * delete time or expire time
     */
    private Date deleteTime;

    /**
     * last update time
     */
    private Date lastUpdateAt;

    private Date pinAt;

    private Long queueId;

    private Integer fileType;

    public enum FileType {
        FILE(0, "file"),
        FOLDER(1, "folder");

        private int code;
        private String msg;

        FileType (int code, String msg) {
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

    public enum FileState {
        IN_COS(1, "in_cos"),
        IN_IPFS_CLUSTER(2, "in_ipfs_cluster"),
        IN_ORDER(3, "in_order"),
        IN_PIN_QUEUE(4, "in_pin_queue"),
        DELETED(-1, "deleted"),
        EXPIRED(-2, "expired");

        private int code;
        private String msg;

        FileState (int code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public Integer getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }

        public static FileState getFileStateByCode(Integer code) {
            for (FileState state: values()) {
                if (state.getCode().equals(code)) {
                    return state;
                }
            }
            return null;
        }

        public IPFSConstants.PinningServiceState convertPinningServiceState(PinJobs.PinJobStatus pinStatus) {
            switch (this) {
                case IN_IPFS_CLUSTER:
                    return IPFSConstants.PinningServiceState.pinned;
                case IN_ORDER:
                    return IPFSConstants.PinningServiceState.pinned;
                case IN_PIN_QUEUE:
                    if (pinStatus == null) {
                        return IPFSConstants.PinningServiceState.queued;
                    }
                    switch (pinStatus) {
                        case QUEUED:
                            return IPFSConstants.PinningServiceState.queued;
                        case PINNING:
                            return IPFSConstants.PinningServiceState.pinning;
                        case PINNED:
                            return IPFSConstants.PinningServiceState.pinned;
                        default:
                            return IPFSConstants.PinningServiceState.failed;
                    }
                default:
                    return IPFSConstants.PinningServiceState.failed;
            }
        }
    }

    public PinningFile convertPinningFile() {
        PinningFile pinningFile = new PinningFile().setId(id).setPinDate(pinAt).setPinSize(cumulativeSize)
                .setPinHash(folderPinHash == null ? ipfsPinHash : folderPinHash);
        if (metaData != null) {
            pinningFile.setMetadata(metaData);
        }
        return pinningFile;
    }

}
