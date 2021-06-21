package com.decoo.psa.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.decoo.psa.domain.ApiKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * user keys(Key)表数据库访问层
 *
 * @author makejava
 * @since 2021-05-19 13:31:26
 */
public interface ApiKeyDao extends BaseMapper<ApiKey> {

    List<ApiKey> selectApiKeyPageByMinIdAndUserId(@Param("minId") Long minId, @Param("userId") Long userId,
                                                  @Param("start") Integer start, @Param("pageSize") Integer pageSize);
}

