ALTER TABLE `user`
MODIFY COLUMN `third_party` int NOT NULL DEFAULT 0 COMMENT 'oem.id' AFTER `role`,
ADD COLUMN `user_type` int NOT NULL DEFAULT 0 COMMENT '0: personal, 1: org' AFTER `third_party`;

CREATE TABLE `oem`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL,
  `desc` varchar(255) NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
);