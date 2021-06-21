package com.decoo.psa.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.decoo.psa.domain.PinFile;
import com.decoo.psa.domain.PinFileState;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * (PinFile)表数据库访问层
 *
 * @author makejava
 * @since 2021-05-07 15:37:34
 */
public interface PinFileDao extends BaseMapper<PinFile> {

    void batchUpdateState(@Param("list") List<PinFile> list, @Param("state") Integer state, @Param("orderId") Long orderId);

    List<PinFile> selectPinFilePageByUserIdStatesAndLastId(@Param("lastId") Long lastId, @Param("userId") Long userId, @Param("states") List<Integer> states, @Param("start") Integer start, @Param("pinSize") Integer size);

    Integer selectPinFileCountByUserIdAndLastId(@Param("lastId") Long lastId, @Param("userId") Long userId, @Param("states") List<Integer> states);

    List<PinFileState> selectPinFileByPinningService(@Param("cid") Set<String> cid, @Param("name") String name,
                                                     @Param("stateList") List<Integer> stateList,
                                                     @Param("before") Date before,
                                                     @Param("after") Date after,
                                                     @Param("limit") Integer limit,
                                                     @Param("meta") Map<String, String> meta,
                                                     @Param("uid") Long uid,
                                                     @Param("match") String match);

    Integer selectPinFileCountByPinningService(@Param("cid") Set<String> cid, @Param("name") String name,
                                               @Param("stateList") List<Integer> stateList,
                                               @Param("before") Date before,
                                               @Param("after") Date after,
                                               @Param("meta") Map<String, String> meta,
                                               @Param("uid") Long uid,
                                               @Param("match") String match);

    List<PinFile> selectPinFileByUserIdStatesAndTimeRange(@Param("uid") Long uid, @Param("startTime") Date startTime, @Param("endTime") Date endTime,
                                                          @Param("states") List<Integer> states,
                                                          @Param("sort") String sort, @Param("offset") Integer offset, @Param("limit") Integer limit);

    Long selectPinFileSizeSumByState(@Param("state") Integer state);

    List<PinFile> selectDistinctCidAndSizeByCode(@Param("state") Integer state, @Param("pageSize") Integer pageSize);

    List<PinFile> selectPinFilesByCidListAndState(@Param("cidList") List<String> cidList, @Param("state") Integer state);
}

