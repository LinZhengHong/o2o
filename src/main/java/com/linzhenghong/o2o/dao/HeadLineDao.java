package com.linzhenghong.o2o.dao;

import com.linzhenghong.o2o.entity.HeadLine;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author LinZhenHong
 */
@Repository
public interface HeadLineDao {

    /**
     * 根据传入的查询条件（头条名查询头条）
     * @param headLineCondition
     * @return
     */
    List<HeadLine> queryHeadLine(@Param("headLineCondition") HeadLine headLineCondition);
}
