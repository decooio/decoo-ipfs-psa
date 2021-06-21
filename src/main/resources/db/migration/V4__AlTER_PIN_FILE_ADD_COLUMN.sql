ALTER TABLE `pin_file`
ADD COLUMN `file_name` text NULL AFTER `id`,
ADD COLUMN `folder_pin_hash` varchar(64) NULL COMMENT 'folder pin hash' AFTER `ipfs_pin_hash`,
ADD COLUMN `pin_at` timestamp NULL COMMENT 'pin pinDate' AFTER `last_update_at`,
MODIFY COLUMN `meta_data` json NULL COMMENT 'ipfs metadata' AFTER `uuid`;
ALTER TABLE `user`
ADD COLUMN `third_party` int NOT NULL DEFAULT 0 COMMENT '0:decoo, 1:cow' AFTER `role`;