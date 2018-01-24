package com.aikon.fin.plugin;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandlerRegistry;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

/**
 * 加密拦截器.
 *
 * @author haitao.wang
 */
@Intercepts({
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
})
public class EncodePlugin extends AbstractEnDecodeParseInterceptor {

    @Override
    EnDecodeEnum getEnDeCodeEnum() {
        return EnDecodeEnum.ENCODE;
    }

    @Override
    Class getToBeParsedClass(Object parameterObject, List<ResultMap> resultMaps) {
        return parameterObject.getClass();
    }

    @Override
    String parse(Object obj, EnDeCodePar enDeCodePar) throws IllegalAccessException, InstantiationException {
        return super.defaultParse(obj, enDeCodePar);
    }

    @Override
    Object execute(Configuration configuration, Object parameterObject, List<ParameterMapping> parameterMappings, BoundSql boundSql, List<ResultMap> resultMaps, Invocation invocation) throws InvocationTargetException, IllegalAccessException {
        Class clazz = parameterObject.getClass();
        Map<String, EnDeCodePar> parseFieldMap = super.generateParseFieldMap(clazz);

        if (parseFieldMap.isEmpty()) {
            return invocation.proceed();
        }
        TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
        if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {

        } else {
            MetaObject metaObject = configuration.newMetaObject(parameterObject);
            for (ParameterMapping parameterMapping : parameterMappings) {
                String propertyName = parameterMapping.getProperty();
                if (metaObject.hasGetter(propertyName)) {
                    Object obj = metaObject.getValue(propertyName);
                    EnDeCodePar enDeCodePar = parseFieldMap.get(propertyName);
                    if (enDeCodePar != null && obj != null) {
                        try {
                            metaObject.setValue(propertyName, parse(obj, enDeCodePar));
                        } catch (InstantiationException e) {
                            metaObject.setValue(propertyName, obj);
                            continue;
                        }
                        continue;
                    }
                    metaObject.setValue(propertyName, obj);
                } else if (boundSql.hasAdditionalParameter(propertyName)) {
                    Object obj = boundSql.getAdditionalParameter(propertyName);
                }
            }
        }
        return invocation.proceed();
    }


}
