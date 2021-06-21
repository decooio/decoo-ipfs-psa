CREATE TABLE `pin_file_queue` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `cid` varchar(64) COLLATE utf8mb4_general_ci NOT NULL COMMENT 'pinHash',
  `user_id` bigint NOT NULL COMMENT 'user.id',
  `state` int NOT NULL COMMENT '0:pending, 1:success, -1:expired',
  `retry_times` int NOT NULL DEFAULT '0' COMMENT 'expire after more then retry limit',
  `create_at` timestamp NOT NULL,
  `last_update_at` timestamp NOT NULL,
  `pin_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_pin_file_queue_user_id_cid` (`user_id`,`cid`) USING BTREE,
  KEY `idx_pin_file_queue_state_user_id` (`state`,`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;