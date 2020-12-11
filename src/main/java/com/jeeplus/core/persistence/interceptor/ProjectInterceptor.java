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
            "cover_history","cover_office_owner","cover_owner","cover_owner_confirm","cover_task_info","cover_task_process","cover_table_field",
            "cover_work","cover_work_config","cover_work_operation","cover_work_operation_detail","cover_work_overtime",
            "flow_depart","flow_opt","flow_opt_result","flow_proc","flow_state","flow_user_org","flow_work_opt","sys_dict_value"};
    //部门模块不走项目过滤sys_office,sys_dict_type,cover_collect_statis
    String [] excludeMethod= new String[]{"checkFindList"};

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
        if(checkIsProject(mappedStatement.getId())){
            SystemAuthorizingRealm.Principal principal = UserUtils.getPrincipal();
            if(principal != null){
                String originalSql = boundSql.getSql().trim();//原始查询语句sql
                List<String> tableList=SQLUtils.getTableNames(originalSql);//提取原始sql中的表名称
                String table=tableList.get(0);
                if(StringUtils.isNotEmpty(table)&&isProject(table)){//判断该表是否需要项目权限过滤
                    String projectId= UserUtils.getProjectId();//获取当前登录用户的所属项目
//                    String newSql =originalSql;
//                    if(StringUtils.isNotEmpty(projectId)){//这样处理项目外的用户也有数据访问权限
//                        newSql =SQLUtils.handleSql(originalSql, "a.project_id", projectId);//处理之后新的sql语句
//                    }
                    if(StringUtils.isEmpty(projectId)&&table.equals("sys_dict_value")){//项目外用户进来可以看到所有的字典项
                        return invocation.proceed();
                    }
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

    public String getMethod(String methods){
        String methodName=null;
        if(StringUtils.isNotEmpty(methods)){
            String[]  methodArr=methods.split("\\.");
            methodName=methodArr[methodArr.length-1];
        }
        return methodName;
    }
    //校验是否需要进行项目过滤
    public Boolean checkIsProject(String methods){
        String excludeMethodStr= StringUtils.join(excludeMethod,",");
        Boolean flag=true;
        String method=getMethod(methods);
        if(StringUtils.isNotEmpty(method)){
            if(excludeMethodStr.contains(method)){
                flag= false;
            }
        }
        return flag;
    }


}
