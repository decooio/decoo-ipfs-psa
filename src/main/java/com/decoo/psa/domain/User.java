package com.decoo.psa.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * (User)表实体类
 *
 * @author makejava
 * @since 2021-04-27 17:35:19
 */
@Data
@Accessors(chain = true)
@SuppressWarnings("serial")
public class User extends Model<User> {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    //nick name
    private String nickName;

    private String mobile;
    //email
    private String email;
    //password(encrypted)
    private String password;
    //create time
    private Date createAt;
    //last update time
    private Date lastUpdateAt;
    //0: valid, 1: invalid
    private Integer status;

    private Integer role;

    private Integer thirdParty;

    private Integer userType;

    public enum UserStatus {
        UNCONFIRMED(0),
        CONFIRMED(1);

        private Integer code;

        UserStatus(Integer code) {
            this.code = code;
        }

        public Integer getCode() {
            return this.code;
        }
    }

    public enum UserRole {

        USER(0, "user"),
        ADMIN(1, "admin"),
        SYSTEM(2, "system");

        private int code;
        private String msg;

        UserRole (int code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public Integer getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }

    }

}
