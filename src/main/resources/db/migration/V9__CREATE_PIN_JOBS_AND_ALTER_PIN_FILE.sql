CREATE TABLE `pin_jobs` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `uuid` varchar(64) COLLATE utf8mb4_general_ci NOT NULL COMMENT 'job uuid',
  `pin_file_id` bigint NOT NULL COMMENT 'pin file id',
  `user_id` bigint NOT NULL COMMENT 'user id',
  `cid` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'ipfs cid',
  `s3_url` text COLLATE utf8mb4_general_ci COMMENT 's3 url',
  `job_type` int NOT NULL DEFAULT '0' COMMENT '0: pin by file, 1: pin by cid, 2: pin by s3 url',
  `job_status` int NOT NULL DEFAULT '0' COMMENT '0: queued, 1: pinning, 2: pinned, 3: failed, 4: expired',
  `retry_times` int NOT NULL DEFAULT '0' COMMENT 'retry times for pin by cid',
  `create_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `deleted` int NOT NULL DEFAULT '0' COMMENT '1: deleted, 0: undeleted',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_pin_job_uuid` (`uuid`) USING BTREE,
  UNIQUE KEY `uniq_pin_job_cid_user_id` (`cid`,`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='pin jobs';

ALTER TABLE `pin_file`
ADD COLUMN `file_type` int NOT NULL DEFAULT 0 COMMENT 'file type 0: file, 1:folder' AFTER `folder_pin_hash`;
