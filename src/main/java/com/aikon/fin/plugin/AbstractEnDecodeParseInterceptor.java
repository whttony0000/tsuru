package com.aikon.fin.plugin;

import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 加解密拦截.
 *
 * @author haitao.wang
 */
public abstract class AbstractEnDecodeParseInterceptor implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        Object param = null;
        if (invocation.getArgs().length > 1) {
            param = invocation.getArgs()[1];
        }
        BoundSql boundSql = mappedStatement.getBoundSql(param);
        Configuration configuration = mappedStatement.getConfiguration();
        List<ResultMap> resultMaps = mappedStatement.getResultMaps();
        Object parameterObject = boundSql.getParameterObject();
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        if (parameterMappings.size() == 0 || parameterObject == null) {
            return invocation.proceed();
        }

        EnDecodeEnum enDecodeEnum = getEnDeCodeEnum();
        if (MapperMethod.ParamMap.class.isInstance(parameterObject)) {
            MapperMethod.ParamMap paramMap = (MapperMethod.ParamMap) parameterObject;
            for (Object obj : paramMap.entrySet()) {
                Object val = ((Map.Entry) obj).getValue();
                Class paramClass = ((Map.Entry) obj).getValue().getClass();
                EnDeCodePar enDeCodePar = (EnDeCodePar) paramClass.getAnnotation(EnDeCodePar.class);
                if (enDeCodePar == null) {
                    continue;
                }
                while (paramClass != null) {
                    Field[] fields = paramClass.getDeclaredFields();
                    Method[] methods = paramClass.getDeclaredMethods();

                    if (fields.length == 0 || methods.length == 0) {
                        paramClass = paramClass.getSuperclass();
                        continue;
                    }
                    AccessibleObject.setAccessible(fields, true);
                    AccessibleObject.setAccessible(methods, true);
                    MetaObject metaObject = configuration.newMetaObject(val);
                    for (Field field : fields) {
                        EnDeCodePar parAnn = field.getAnnotation(EnDeCodePar.class);
                        if (parAnn == null) {
                            continue;
                        }
                        metaObject.setValue(field.getName(), this.defaultParse(val, enDeCodePar));
                    }
                    //todo example method
                    paramClass = paramClass.getSuperclass();
                }
            }
            if (EnDecodeEnum.ENCODE.equals(enDecodeEnum)) {
                return this.defaultDecode(configuration, resultMaps, invocation);
            }
        }
        return execute(configuration, parameterObject, parameterMappings, boundSql, resultMaps, invocation);
    }

    public Map<String, EnDeCodePar> generateParseFieldMap(Class clazz) {
        Map<String, EnDeCodePar> parseFieldMap = new HashMap<>();
        while (clazz != null) {
            Field[] fields = clazz.getDeclaredFields();

            if (fields.length == 0) {
                clazz = clazz.getSuperclass();
                continue;
            }
            AccessibleObject.setAccessible(fields, true);
            for (Field field : fields) {
                EnDeCodePar enDeCodePar = field.getAnnotation(EnDeCodePar.class);
                if (enDeCodePar == null) {
                    continue;
                }
                parseFieldMap.put(field.getName(), enDeCodePar);
            }
            clazz = clazz.getSuperclass();
        }
        return parseFieldMap;
    }

    abstract EnDecodeEnum getEnDeCodeEnum();

    abstract Class getToBeParsedClass(Object parameterObject, List<ResultMap> resultMaps);

    abstract String parse(Object obj, EnDeCodePar enDeCodePar) throws IllegalAccessException, InstantiationException;

    abstract Object execute(Configuration configuration, Object parameterObject, List<ParameterMapping> parameterMappings, BoundSql boundSql, List<ResultMap> resultMaps, Invocation invocation) throws InvocationTargetException, IllegalAccessException;

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }

    public String defaultParse(Object obj, EnDeCodePar enDeCodePar) throws IllegalAccessException, InstantiationException {
        return enDeCodePar.encoder().newInstance().encode(obj.toString());
    }

    public void proceed(Configuration configuration, Map<String, EnDeCodePar> parseFieldMap, Object val) {
        MetaObject metaObject = configuration.newMetaObject(val);
        for (String propertyName : parseFieldMap.keySet()) {
            if (metaObject.hasSetter(propertyName) && metaObject.getValue(propertyName) != null) {
                try {
                    metaObject.setValue(propertyName, parse(metaObject.getValue(propertyName), parseFieldMap.get(propertyName)));
                } catch (Exception e) {
                    continue;
                }
            }
        }
    }

    public Object defaultDecode(Configuration configuration, List<ResultMap> resultMaps, Invocation invocation) throws InvocationTargetException, IllegalAccessException {
        if (resultMaps == null || resultMaps.size() == 0) {
            return invocation.proceed();
        }
        Class clazz = resultMaps.get(0).getType();

        // 返回值为基本类型暂不做处理.
        if (String.class.isAssignableFrom(clazz) || Integer.class.isAssignableFrom(clazz) || Long.class.isAssignableFrom(clazz)) {
            return invocation.proceed();
        }

        Map<String, EnDeCodePar> parseFieldMap = this.generateParseFieldMap(clazz);


        if (parseFieldMap.isEmpty()) {
            return invocation.proceed();
        }
        Object val = invocation.proceed();
        if (val == null) {
            return val;
        }
        if (val instanceof Collection) {
            for (Object item : (Collection) val) {
                proceed(configuration, parseFieldMap, item);
            }
        } else {
            proceed(configuration, parseFieldMap, val);
        }
        return val;
    }
}
