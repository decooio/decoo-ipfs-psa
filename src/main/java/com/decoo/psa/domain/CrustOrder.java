package com.decoo.psa.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * crust order call back info(CrustOrder)表实体类
 *
 * @author makejava
 * @since 2021-04-27 17:35:18
 */
@Data
@SuppressWarnings("serial")
@Accessors(chain = true)
public class CrustOrder extends Model<CrustOrder> {
    @TableId(type = IdType.AUTO)
    private Long id;
    //account.id
    private String cid;

    private Long fileSize;

    private Integer status;

    private Integer expiredOn;

    private Integer claimedAt;

    private Integer reportedReplicaCount;

    private Long amount;

    private Date createAt;

    private Date lastUpdateAt;

    private String peerHost;

}
