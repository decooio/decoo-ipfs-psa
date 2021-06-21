CREATE TABLE `api_key` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL COMMENT 'user.id',
  `name` varchar(32) COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'default' COMMENT 'key name',
  `api_key` varchar(64) COLLATE utf8mb4_general_ci NOT NULL COMMENT 'unique key',
  `api_secret` varchar(64) COLLATE utf8mb4_general_ci NOT NULL,
  `jwt` text COLLATE utf8mb4_general_ci NOT NULL COMMENT 'jwt token',
  `status` int NOT NULL DEFAULT '0' COMMENT '1:invalid, 0:valid',
  `create_at` timestamp NOT NULL COMMENT 'create time',
  `last_update_at` timestamp NOT NULL COMMENT 'last update time',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_api_key_key` (`api_key`) USING BTREE COMMENT 'unique keys',
  KEY `idx_api_key_user` (`user_id`) USING BTREE COMMENT 'idx user_id for query'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='user keys';

CREATE TABLE `api` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `api_type` int NOT NULL DEFAULT '0' COMMENT 'api type 0: api,1: manage',
  `pid` bigint NOT NULL DEFAULT '-1' COMMENT 'parent id',
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'api name',
  `path` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'api path',
  `order` int NOT NULL COMMENT 'order',
  `create_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'create at',
  `last_update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'last_update_at',
  PRIMARY KEY (`id`),
  KEY `idx_api_pid` (`pid`) USING BTREE COMMENT 'idx for pid'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='api';

CREATE TABLE `api_key_rel` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `api_id` bigint NOT NULL,
  `key_id` bigint NOT NULL,
  `create_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_api_key_rel_api_key_id` (`key_id`,`api_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='api key rel';


