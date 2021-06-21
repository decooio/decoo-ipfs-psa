ALTER TABLE `user`
MODIFY COLUMN `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT 'email' AFTER `mobile`;