package com.decoo.psa.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@Data
@Accessors(chain = true)
public class ApiKey implements Serializable {
    private static final long serialVersionUID = 976957830260367886L;
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * user.id
     */
    private Long userId;
    /**
     * key name
     */
    private String name;
    /**
     * unique key
     */
    private String apiKey;

    private String apiSecret;
    /**
     * jwt token
     */
    private String jwt;
    /**
     * 1:invalid, 0:valid
     */
    private Integer status;

    /**
     * create time
     */
    private Date createAt;
    /**
     * last update time
     */
    private Date lastUpdateAt;

    public enum KeyStatus {
        VALID(0),
        INVALID(1);

        private Integer code;

        KeyStatus(Integer code) {
            this.code = code;
        }

        public Integer getCode() {
            return code;
        }
    }

}
