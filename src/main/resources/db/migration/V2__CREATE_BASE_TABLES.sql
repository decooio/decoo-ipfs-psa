CREATE TABLE `pin_file` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `ipfs_pin_hash` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'ipfs pinHash',
  `third_party` int NOT NULL DEFAULT '0' COMMENT 'decoo: 0, cow:1',
  `order_id` bigint DEFAULT NULL COMMENT 'crust order id',
  `cos_url` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
  `uuid` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `meta_data` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT 'ipfs metadata',
  `cumulative_size` bigint DEFAULT NULL COMMENT 'cumulative pinSize in ipfs',
  `user_id` bigint NOT NULL DEFAULT '-1' COMMENT 'user.id',
  `create_at` timestamp NOT NULL COMMENT 'create pinDate',
  `state` int NOT NULL DEFAULT '0' COMMENT 'in cos:1, in ipfs cluster:2, in_order: 3, deleted: -1, expired:-2',
  `delete_time` timestamp NULL DEFAULT NULL COMMENT 'delete time or expire time',
  `last_update_at` timestamp NOT NULL COMMENT 'last update time',
  PRIMARY KEY (`id`),
  KEY `idx_pin_file_uuid` (`uuid`,`create_at`) USING BTREE COMMENT 'index for uuid to query',
  KEY `idx_pin_file_order_id` (`order_id`) USING BTREE COMMENT 'index for order to query',
  KEY `idx_pin_file_third_party` (`third_party`) USING BTREE COMMENT 'index for order to query'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE `crust_order` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `cid` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `peer_host` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'pin dir host',
  `file_size` bigint NOT NULL DEFAULT '-1',
  `status` int NOT NULL DEFAULT '0' COMMENT '0: pin on cluster, 1: pin in order',
  `expired_on` int NOT NULL DEFAULT '-1',
  `claimed_at` int NOT NULL DEFAULT '-1',
  `amount` bigint NOT NULL DEFAULT '-1',
  `reported_replica_count` int NOT NULL DEFAULT '0',
  `create_at` timestamp NOT NULL,
  `last_update_at` timestamp NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_crust_order_cid` (`cid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='crust order call back info';

CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nick_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'nick name',
  `mobile` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'mobile',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'email',
  `password` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'password(encrypted)',
  `create_at` timestamp NOT NULL COMMENT 'create time',
  `last_update_at` timestamp NOT NULL COMMENT 'last update time',
  `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '1: valid, 0: invalid',
  `role` int NOT NULL COMMENT '0: user, 1: admin, 2: system',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_user_email` (`email`) USING BTREE COMMENT 'unique for user.email to login'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;