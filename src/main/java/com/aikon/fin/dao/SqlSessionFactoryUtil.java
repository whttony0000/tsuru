package com.aikon.fin.dao;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author haitao.wang
 */
public class SqlSessionFactoryUtil {
    private static final Class CLASS_LOCK = SqlSessionFactoryUtil.class;

    private static SqlSessionFactory sqlSessionFactory = null;

    private SqlSessionFactoryUtil(){}

    public static SqlSessionFactory init() {
        String resource = "mybatis/mybatis-config.xml";
        InputStream inputStream = null;
        try {
            inputStream = Resources.getResourceAsStream(resource);
        } catch (IOException e) {
            e.printStackTrace();
        }
        synchronized (CLASS_LOCK) {
            if (sqlSessionFactory == null) {
                sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
            }

        }
        return sqlSessionFactory;
    }

    public static SqlSession openSession() {
        if (sqlSessionFactory == null) {
            init();
        }
        return sqlSessionFactory.openSession();
    }
}
