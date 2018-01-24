package com.aikon.fin.plugin;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * 解密拦截器.
 *
 * @author haitao.wang
 */
@Intercepts({
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class,
                RowBounds.class, ResultHandler.class})
})
public class DecodePlugin extends AbstractEnDecodeParseInterceptor {
    @Override
    EnDecodeEnum getEnDeCodeEnum() {
        return EnDecodeEnum.DECODE;
    }


    @Override
    Class getToBeParsedClass(Object parameterObject, List<ResultMap> resultMaps) {
        return resultMaps.get(0).getType();
    }

    @Override
    String parse(Object obj, EnDeCodePar enDeCodePar) throws IllegalAccessException, InstantiationException {
        return enDeCodePar.decoder().newInstance().decode(obj.toString());
    }

    @Override
    Object execute(Configuration configuration, Object parameterObject, List<ParameterMapping> parameterMappings, BoundSql boundSql, List<ResultMap> resultMaps, Invocation invocation) throws InvocationTargetException, IllegalAccessException {
        return defaultDecode(configuration, resultMaps, invocation);
    }

}
