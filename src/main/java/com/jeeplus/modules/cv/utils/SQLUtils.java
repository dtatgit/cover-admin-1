package com.jeeplus.modules.cv.utils;
import com.jeeplus.common.utils.StringUtils;
import java.io.StringReader;
import java.util.List;

import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.replace.Replace;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.update.Update;
import net.sf.jsqlparser.util.TablesNamesFinder;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class SQLUtils {
    private static CCJSqlParserManager pm = new CCJSqlParserManager();
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
    public static List<String> getTableNames(String sql) throws Exception {
        List<String> tablenames = null;
        TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
        Statement statement = pm.parse(new StringReader(sql));
        if (statement instanceof Select) {
            tablenames = tablesNamesFinder.getTableList((Select) statement);
        } else if (statement instanceof Update) {
            return null;
        } else if (statement instanceof Delete) {
            return null;
        } else if (statement instanceof Replace) {
            return null;
        } else if (statement instanceof Insert) {
            return null;
        }
        return tablenames;
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
