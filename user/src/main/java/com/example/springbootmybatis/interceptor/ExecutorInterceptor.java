package com.example.springbootmybatis.interceptor;

import com.example.springbootmybatis.annotation.DataPermission;
import com.example.springbootmybatis.util.UserInfoUtils;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.util.TablesNamesFinder;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

@Component
@Intercepts({@Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
            @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})})
public class ExecutorInterceptor implements Interceptor {

    private static final Logger logger = LoggerFactory.getLogger(ExecutorInterceptor.class);

    private static  final int MAPPED_STATEMENT_INDEX = 0;
    private static final int PARAM_OBJ_INDEX = 1;
    private static final List<String> tableWithoutCreateBy = Arrays.asList(
            "MYBATIS.USER_ROLE",
            "MYBATIS.ROLE",
            "MYBATIS.ROLE_PERMISSION",
            "MYBATIS.PERMISSION"
    );

    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        Object[] args = invocation.getArgs();
        MappedStatement ms = (MappedStatement) args[0];
        String mId = ms.getId();
        Class<?> classType = Class.forName(mId.substring(0, ms.getId().lastIndexOf(".")));
        String methodName = mId.substring(mId.lastIndexOf(".") + 1);
        Object sqlCommandType = ms.getSqlCommandType();
        Object parameter = args[1];
        BoundSql boundSql = ms.getBoundSql((parameter));

        String sql = boundSql.getSql();

        System.out.println("sql:"+sql);

        for (Method method : classType.getDeclaredMethods()) {
//            if (method.getName().equals(methodName) && !method.isAnnotationPresent(DataPermission.class)) {
//                return invocation.proceed();
//            }
            if (method.isAnnotationPresent(DataPermission.class) && method.getName().equals(methodName)) {
                DataPermission dpAnnotation = method.getAnnotation(DataPermission.class);
                if (!dpAnnotation.enable()) {
                    return invocation.proceed();
                }
            }
        }

        List roleList = UserInfoUtils.getRole();
        System.out.println("Executor-roleList:"+roleList);
        if (Objects.nonNull(roleList) &&
        roleList.size()>0 &&
        roleList.contains("超级管理员")) {
            return invocation.proceed();
        }

        Statement statement = CCJSqlParserUtil.parse(sql);
        List<String> tableNames = new TablesNamesFinder().getTableList(statement);
        sql = sql.replaceAll("\r|\n", "").replaceAll(";", " ");

        if (Objects.equals(SqlCommandType.UPDATE, sqlCommandType)||
                Objects.equals(SqlCommandType.DELETE, sqlCommandType)){
            if (!tableWithoutCreateBy.contains(tableNames.get(0).toUpperCase())){
                Integer userId = UserInfoUtils.getUserId();
                if (sql.indexOf(" WHERE ") > 0 || sql.indexOf(" where ") > 0){
                    sql = sql + " AND createBy = " + userId;
                } else {
                    sql = sql + " WHERE createBy = " + userId;
                }
            }
        } else if (Objects.equals(SqlCommandType.SELECT, sqlCommandType)){

            for (String tableName : tableNames) {

                if (!tableWithoutCreateBy.contains(tableName.toUpperCase())){

                    Integer userId =  UserInfoUtils.getUserId();
                    if (sql.indexOf(" FROM " + tableName) > 0) {

                        sql = sql.replaceAll(" FROM " + tableName + " ", " FROM (SELECT * FROM " + tableName + " WHERE  createBy =" + userId + ")");
                    }
                    if (sql.indexOf(" from " + tableName) > 0) {

                        sql = sql.replaceAll(" from " + tableName + " ", " from (select * from " + tableName + " WHERE  createBy =" + userId + ")");
                    }
                    if (sql.indexOf(" JOIN " + tableName) > 0) {

                        sql = sql.replaceAll(" JOIN " + tableName + " ", " JOIN (SELECT * FROM " + tableName + " WHERE  createBy =" + userId + ")");
                    }
                    if (sql.indexOf(" join " + tableName) > 0) {

                        sql = sql.replaceAll(" join " + tableName + " ", " join (select * from " + tableName + " WHERE  createBy =" + userId + ") as cb");
                    }
                }
            }
        }
        System.out.println("newSql:"+sql);
        System.out.println("tableNames:"+tableNames);
        setCurrentSql(invocation, sql);
        return invocation.proceed();
    }


    private void setCurrentSql(Invocation invo, String sql) {
        MappedStatement mappedStatement = getMappedStatement(invo);
        Object[] args = invo.getArgs();
        Object paramObj = args[PARAM_OBJ_INDEX];
        BoundSql boundSql = mappedStatement.getBoundSql(paramObj);
        BoundSqlSource boundSqlSource = new BoundSqlSource(boundSql);
        MappedStatement newMappedStatement = copyFromMappedStatement(
                mappedStatement, boundSqlSource);
        MetaObject metaObject = MetaObject.forObject(newMappedStatement,
                new DefaultObjectFactory(), new DefaultObjectWrapperFactory(),
                new DefaultReflectorFactory());
        metaObject.setValue("sqlSource.boundSql.sql", sql);
        args[MAPPED_STATEMENT_INDEX] = newMappedStatement;
    }

    private MappedStatement getMappedStatement(Invocation invo) {
        Object[] args = invo.getArgs();
        Object mappedStatement = args[MAPPED_STATEMENT_INDEX];
        return (MappedStatement) mappedStatement;
    }

    private class BoundSqlSource implements SqlSource {

        private BoundSql boundSql;

        private BoundSqlSource(BoundSql boundSql) {
            this.boundSql = boundSql;
        }

        @Override
        public BoundSql getBoundSql(Object parameterObject) {
            return boundSql;
        }
    }

    private MappedStatement copyFromMappedStatement(MappedStatement ms, SqlSource newSqlSource) {
        MappedStatement.Builder builder = new MappedStatement.Builder(ms.getConfiguration(), ms.getId(), newSqlSource, ms.getSqlCommandType());
        builder.resource(ms.getResource());
        builder.fetchSize(ms.getFetchSize());
        builder.statementType(ms.getStatementType());
        builder.keyGenerator(ms.getKeyGenerator());
        if (ms.getKeyProperties() != null && ms.getKeyProperties().length != 0) {
            StringBuffer keyProperties = new StringBuffer();
            String[] arr$ = ms.getKeyProperties();
            int len$ = arr$.length;

            for (int i$ = 0; i$ < len$; ++i$) {
                String keyProperty = arr$[i$];
                keyProperties.append(keyProperty).append(",");
            }

            keyProperties.delete(keyProperties.length() - 1, keyProperties.length());
            builder.keyProperty(keyProperties.toString());
        }

        builder.timeout(ms.getTimeout());
        builder.parameterMap(ms.getParameterMap());
        builder.resultMaps(ms.getResultMaps());
        builder.resultSetType(ms.getResultSetType());
        builder.cache(ms.getCache());
        builder.flushCacheRequired(ms.isFlushCacheRequired());
        builder.useCache(ms.isUseCache());
        return builder.build();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target,this);
    }

    @Override
    public void setProperties(Properties properties) {
    }
}
