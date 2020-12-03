package com.jeeplus.core.persistence.interceptor;

import com.jeeplus.common.utils.Reflections;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.modules.cv.utils.SQLUtils;
import com.jeeplus.modules.sys.security.SystemAuthorizingRealm;
import com.jeeplus.modules.sys.utils.UserUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Properties;

@Intercepts({@Signature(type = Executor.class, method = "query",
        args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})})
public class ProjectInterceptor extends BaseInterceptor {
    private static final long serialVersionUID = 1L;
    //需要项目管理的表单
    String[] tables = new String[]{"biz_alarm","cover","cover_audit","cover_bell","cover_bell_alarm","cover_bell_operation","cover_bell_state",
            "cover_collect_statis","cover_history","cover_office_owner","cover_owner","cover_owner_confirm","cover_task_info","cover_task_process",
            "cover_work","cover_work_config","cover_work_operation","cover_work_operation_detail","cover_work_overtime",
            "flow_depart","flow_opt","flow_opt_result","flow_proc","flow_state","flow_user_org","flow_work_opt"};
    //部门模块不走项目过滤sys_office,sys_dict_type


    public boolean isProject(String tableName){
        String tableStr= StringUtils.join(tables,",");
        if(tableStr.contains(tableName)){
            return true;
        }
        return false;
    }
    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        final MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        Object parameter = invocation.getArgs()[1];
        BoundSql boundSql = mappedStatement.getBoundSql(parameter);
        //Object parameterObject = boundSql.getParameterObject();
        SystemAuthorizingRealm.Principal principal = UserUtils.getPrincipal();
        if(principal != null){
            String originalSql = boundSql.getSql().trim();//原始查询语句sql
            List<String> tableList=SQLUtils.getTableNames(originalSql);//提取原始sql中的表名称
            String table=tableList.get(0);
            if(StringUtils.isNotEmpty(table)&&isProject(table)){//判断该表是否需要项目权限过滤
                String projectId= UserUtils.getUser().getOffice().getProjectId();//获取当前登录用户的所属项目
                String newSql =SQLUtils.handleSql(originalSql, "a.project_id", projectId);//处理之后新的sql语句
                BoundSql newBoundSql = new BoundSql(mappedStatement.getConfiguration(), newSql, boundSql.getParameterMappings(), boundSql.getParameterObject());
                if (Reflections.getFieldValue(boundSql, "metaParameters") != null) {
                    MetaObject mo = (MetaObject) Reflections.getFieldValue(boundSql, "metaParameters");
                    Reflections.setFieldValue(newBoundSql, "metaParameters", mo);
                }
                MappedStatement newMs = copyFromMappedStatement(mappedStatement, new PaginationInterceptor.BoundSqlSqlSource(newBoundSql));
                invocation.getArgs()[0] = newMs;
            }
        }

        return invocation.proceed();
    }


    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        super.initProperties(properties);
    }

    private MappedStatement copyFromMappedStatement(MappedStatement ms,
                                                    SqlSource newSqlSource) {
        MappedStatement.Builder builder = new MappedStatement.Builder(ms.getConfiguration(),
                ms.getId(), newSqlSource, ms.getSqlCommandType());
        builder.resource(ms.getResource());
        builder.fetchSize(ms.getFetchSize());
        builder.statementType(ms.getStatementType());
        builder.keyGenerator(ms.getKeyGenerator());
        if (ms.getKeyProperties() != null) {
            for (String keyProperty : ms.getKeyProperties()) {
                builder.keyProperty(keyProperty);
            }
        }
        builder.timeout(ms.getTimeout());
        builder.parameterMap(ms.getParameterMap());
        builder.resultMaps(ms.getResultMaps());
        builder.cache(ms.getCache());
        return builder.build();
    }

//    public static class BoundSqlSqlSource implements SqlSource {
//        BoundSql boundSql;
//
//        public BoundSqlSqlSource(BoundSql boundSql) {
//            this.boundSql = boundSql;
//        }
//
//        public BoundSql getBoundSql(Object parameterObject) {
//            return boundSql;
//        }
//    }
}
