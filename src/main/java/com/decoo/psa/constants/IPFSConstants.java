package com.decoo.psa.constants;

import com.decoo.psa.utils.CommonUtils;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class IPFSConstants {
    public static final String IPFS_PIN_BY_CID_JOB = "ipfs-pin-cid-job";

    public static final String IPFS_CLUSTER_HOST = CommonUtils.getenv("IPFS_CLUSTER_HOST", "http://127.0.0.1");

    public static final String IPFS_CLUSTER_AUTH_PASSWORD = CommonUtils.getenv("IPFS_CLUSTER_AUTH_PASSWORD", "decoo");

    public static final String IPFS_NODE_PORT = CommonUtils.getenv("IPFS_NODE_PORT", "5001");

    public static final String IPFS_URL_CLUSTER_PEERS = IPFS_CLUSTER_HOST + "/peers";

    public static final String IPFS_URL_CLUSTER_ADD = IPFS_CLUSTER_HOST + "/add?replication-min=2&replication-max=2&local=true";

    public static final String IPFS_URL_CLUSTER_UNPIN = IPFS_CLUSTER_HOST + "/pins/";

    public static final String IPFS_URL_CLUSTER_PIN = IPFS_CLUSTER_HOST + "/pins/";

    public static final String IPFS_URL_FILE_MKDIR = IPFS_CLUSTER_HOST + "/api/v0/files/mkdir";

    public static final String IPFS_URL_FILE_STAT = IPFS_CLUSTER_HOST + "/api/v0/files/stat";

    public static final String IPFS_URL_FILE_LS = IPFS_CLUSTER_HOST + "/api/v0/files/ls";

    public static final String IPFS_URL_FILE_CP = IPFS_CLUSTER_HOST + "/api/v0/files/cp";

    public static final String IPFS_URL_PIN_RM = IPFS_CLUSTER_HOST + "/api/v0/pin/rm";

    public static final String IPFS_URL_PIN_ADD = IPFS_CLUSTER_HOST + "/api/v0/pin/add";

    public static final String IPFS_URL_SWARM_CONNECT = IPFS_CLUSTER_HOST + "/api/v0/swarm/connect";

    public static final long IPFS_JOBS_BATCH_UPDATE_SIZE = 100l;

    public static final int IPFS_PIN_FAILED_RETRY_COUNT = 3;

    public static final Set<String> IPFS_DELEGATES = Arrays.asList("/ip4/127.0.0.1/tcp/4001/p2p/12D3KooWBoQZ9e6e2XuH7Fp86bLTXKXpNkVRUxgVMh86dMUcDutF").stream().collect(Collectors.toSet());

    public static final String CRUST_SEEDS = CommonUtils.getenv("CRUST_SEEDS", "CRUST_SEEDS");

    public static final String CRUST_ORDER_HOST = CommonUtils.getenv("CRUST_ORDER_URL", "http://127.0.0.1:3000");

    public static final String CRUST_URL_ORDER_STAT = CRUST_ORDER_HOST + "/order/";

    public static final String CRUST_URL_ORDER = CRUST_ORDER_HOST + "/order";

    public static final String DEFAULT_HOST = "http://127.0.0.1";

    public enum PinningServiceState {
        queued(1, "queued"),
        pinning(2, "pinning"),
        pinned(3, "pinned"),
        failed(4, "failed");

        private int code;
        private String msg;

        PinningServiceState (int code, String msg) {
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

    public enum CidVersion {
        CID0(0, "cid0"),
        CID1(1, "cid1");

        private int code;
        private String msg;

        CidVersion (int code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public Integer getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }

        public static CidVersion getCidByCode(Integer code) {
            for (CidVersion cidVersion: values()) {
                if (cidVersion.getCode().equals(code)) {
                    return cidVersion;
                }
            }
            return CidVersion.CID0;
        }
    }

    public enum ClusterPinStatus {
        PINNED(0, "pinned"),
        PINNING(1, "pinning"),
        UNPINNED(2, "unpinned"),
        PIN_ERROR(3, "pin_error");

        private int code;
        private String msg;

        ClusterPinStatus (int code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public Integer getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }

        public static ClusterPinStatus getCidByCode(Integer code) {
            for (ClusterPinStatus cidVersion: values()) {
                if (cidVersion.getCode().equals(code)) {
                    return cidVersion;
                }
            }
            return ClusterPinStatus.PIN_ERROR;
        }
    }

    public enum PinThirdParty {
        DECOO(0, "DECOO"),
        COW(1, "COW");

        private int code;
        private String msg;

        PinThirdParty (int code, String msg) {
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
}
