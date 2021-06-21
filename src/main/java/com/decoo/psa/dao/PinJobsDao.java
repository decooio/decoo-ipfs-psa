package com.decoo.psa.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.decoo.psa.domain.PinJobs;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * pin jobs(PinJob)表数据库访问层
 *
 * @author makejava
 * @since 2021-06-10 13:38:55
 */
public interface PinJobsDao extends BaseMapper<PinJobs> {
    List<PinJobs> selectPinJobList(@Param("cid") String cid, @Param("sort") String sort, @Param("jobStatusCode") Integer jobStatusCode, @Param("offset") Integer offset, @Param("uid") Long uid, @Param("limit") Integer limit);
}

