package com.decoo.psa.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@Data
@Accessors(chain = true)
public class Api implements Serializable {
    private static final long serialVersionUID = -54691355112149377L;
    @TableId(type = IdType.AUTO)
    private Long id;

    private Integer apiType;
    /**
     * parent id
     */
    private Long pid;
    /**
     * api name
     */
    private String name;
    /**
     * api path
     */
    private String path;
    /**
     * order
     */
    private Integer order;
    /**
     * create at
     */
    private Date createAt;
    /**
     * last_update_at
     */
    private Date lastUpdateAt;

}
