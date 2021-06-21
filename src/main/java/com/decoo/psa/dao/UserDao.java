package com.decoo.psa.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.decoo.psa.domain.User;
import org.apache.ibatis.annotations.Param;

/**
 * (User)表数据库访问层
 *
 * @author makejava
 * @since 2021-04-27 17:35:19
 */
public interface UserDao extends BaseMapper<User> {

    void updateUserStateByUserId(@Param("userId") long userId, @Param("status") Integer status);

}
