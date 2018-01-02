package com.aikon.fin.dao.base;

import com.aikon.fin.entity.CurrentBalance;
import com.aikon.fin.entity.CurrentBalanceExample;

import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CurrentBalanceMapper {
    int countByExample(CurrentBalanceExample example);

    int deleteByExample(CurrentBalanceExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(CurrentBalance record);

    int insertSelective(CurrentBalance record);

    List<CurrentBalance> selectByExample(CurrentBalanceExample example);

    CurrentBalance selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") CurrentBalance record, @Param("example") CurrentBalanceExample example);

    int updateByExample(@Param("record") CurrentBalance record, @Param("example") CurrentBalanceExample example);

    int updateByPrimaryKeySelective(CurrentBalance record);

    int updateByPrimaryKey(CurrentBalance record);
}