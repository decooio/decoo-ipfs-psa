ALTER TABLE `pin_file`
ADD COLUMN `queue_id` bigint NULL COMMENT 'queue id ' AFTER `pin_at`,
DROP INDEX `idx_pin_file_third_party`,
ADD INDEX `idx_pin_file_queue_id`(`queue_id`) USING BTREE COMMENT 'index for pin file queue to query',
ADD INDEX `idx_pin_file_user_id_state`(`user_id`, `state`, `create_at`) USING BTREE COMMENT 'index for user to query',
ADD INDEX `idx_pin_file_user_id_cid`(`user_id`, `ipfs_pin_hash`) USING BTREE COMMENT 'index for base query';