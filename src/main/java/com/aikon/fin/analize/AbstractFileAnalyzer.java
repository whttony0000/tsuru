package com.aikon.fin.analize;

import com.aikon.fin.dao.CurrentBalanceExtendMapper;
import com.aikon.fin.entity.CurrentBalance;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import com.google.common.io.LineProcessor;
import com.aikon.fin.dao.SqlSessionFactoryUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author haitao.wang
 */
@Slf4j
public abstract class AbstractFileAnalyzer implements Analyzer {

     boolean ifSaveCurrentBalance;

    public AbstractFileAnalyzer(boolean ifSaveCurrentBalance) {
        this.ifSaveCurrentBalance = ifSaveCurrentBalance;
    }

    void saveCurrentBalance(CurrentBalance currentBalance) {
        if (!this.ifSaveCurrentBalance) {
            return;
        }
        SqlSession sqlSession = SqlSessionFactoryUtil.openSession();
        try{

            CurrentBalanceExtendMapper currentBalanceExtendMapper = sqlSession.getMapper(CurrentBalanceExtendMapper.class);
            currentBalanceExtendMapper.insertSelective(currentBalance);
            sqlSession.commit();
        } finally {
            sqlSession.close();
        }
    }


}
