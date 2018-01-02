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


    public <T> List<T> analyze(String path) {
        File file = new File(path);
        List<T> lineObjList = null;
        try {
            lineObjList = Files.readLines(file, Charsets.UTF_8, new LineProcessor<List<T>>() {
                List<T> lineObjCollection = Lists.newLinkedList();

                public boolean processLine(String line) {
                    if (StringUtils.isBlank(line)) {
                        return false;
                    }
                    T lineObj = handleLine(line);
                    if (lineObj == null) {
                        return true;
                    }
                    lineObjCollection.add(lineObj);
                    return true;
                }

                public List<T> getResult() {
                    return lineObjCollection;
                }
            });
        } catch (IOException e) {
            log.info("Error Processing Line {}",e);
        }
        return lineObjList;
    }

    void saveCurrentBalance(CurrentBalance currentBalance) {
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
