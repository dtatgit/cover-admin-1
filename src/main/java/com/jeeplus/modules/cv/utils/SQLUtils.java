package com.jeeplus.modules.cv.utils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.sys.utils.UserUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class SQLUtils {
    //根据提供的sql获取表名称
    public static String getTableBySql(String sql){
        if(StringUtils.isEmpty(sql)){
            return null;
        }
        String table=null;
        String regex="\\bfrom\\s*\\S*";
        Pattern p=Pattern.compile(regex);
        Matcher m=p.matcher(sql);
        if(m.find()){
            table=m.group().replace("from","");
            System.out.println(table.trim());
        }else{
            System.out.println("not found");
        }
        return table;
    }

    public static String handleSql(String sql,String parameter ,String extValue){
    String newSQL=null;
        if(StringUtils.isEmpty(sql)){
            return null;
        }
        StringBuffer sb=new StringBuffer(" where ");
        sb.append(parameter).append("='").append(extValue).append("'");
        sb.append(" and ");
        newSQL=sql.replaceFirst("where", sb.toString());
        return newSQL;
    }

    public static boolean isProject(String tableName){
        String[] tables = new String[]{"cover","biz_alarm"};
        String tableStr= StringUtils.join(tables,",");
        if(tableStr.contains(tableName)){
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        String sql2="select a,b,c from biz_alarm  where id=1 order by id desc";
        System.out.println("***************"+ getTableBySql(sql2));
        System.out.println("***************"+ isProject("biz_alarm"));
        System.out.println("***************"+ handleSql(sql2,"a.projectId","6665656675"));
    }
}
