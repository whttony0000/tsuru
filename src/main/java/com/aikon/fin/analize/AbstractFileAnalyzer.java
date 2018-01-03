package com.aikon.fin.analize;

import com.aikon.fin.dao.CurrentBalanceExtendMapper;
import com.aikon.fin.dao.SqlSessionFactoryUtil;
import com.aikon.fin.entity.CurrentBalance;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;

import java.util.regex.Pattern;

/**
 * @author haitao.wang
 */
@Slf4j
public abstract class AbstractFileAnalyzer implements Analyzer {

    boolean ifSaveCurrentBalance;

    static final Pattern LINE_PATTERN = Pattern.compile("(\\t)?(\\d+\\.?\\d+,)+(\\d+\\.?\\d+)$");

    public AbstractFileAnalyzer(boolean ifSaveCurrentBalance) {
        this.ifSaveCurrentBalance = ifSaveCurrentBalance;
    }

    public String preHandleLine(String line) {
        return line.replaceAll("[\"]", "");
    }

    public boolean checkLine(String line) {
        return LINE_PATTERN.matcher(line).matches();
    }

    void saveCurrentBalance(CurrentBalance currentBalance) {
        if (!this.ifSaveCurrentBalance) {
            return;
        }
        SqlSession sqlSession = SqlSessionFactoryUtil.openSession();
        try {

            CurrentBalanceExtendMapper currentBalanceExtendMapper = sqlSession.getMapper(CurrentBalanceExtendMapper.class);
            currentBalanceExtendMapper.insertSelective(currentBalance);
            sqlSession.commit();
        } finally {
            sqlSession.close();
        }
    }


}
