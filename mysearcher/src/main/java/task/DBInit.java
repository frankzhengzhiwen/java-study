package task;

import util.DBUtil;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Statement;

/**
 * @ClassName DBInit
 * @Description
 * @Author frank
 * @Date 2019/12/18 10:46 下午
 * @Version 1.0
 */
public class DBInit {
    /**
     * 获取数据库初始化执行语句
     * @return
     * @throws Exception
     */
    private static String[] getInitSQL() throws Exception {
        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader br = null;

        String line;
        StringBuilder sb = new StringBuilder();
        try {
            is = DBUtil.class.getClassLoader().getResourceAsStream("init.sql");
            isr = new InputStreamReader(is, "UTF-8");
            br = new BufferedReader(isr);
            while((line=br.readLine()) != null){
                int idx = line.indexOf("--");
                if(idx != -1)
                    line = line.substring(0, idx);
                sb.append(line);
            }
            return sb.toString().split(";");
        } finally {
            if(br != null)
                br.close();
            if(isr != null)
                isr.close();
            if(is != null)
                br.close();
        }
    }

    /**
     * 执行数据库初始化操作
     */
    public static void init(){
        Connection connection = null;
        Statement statement = null;
        try {
            try {
                connection = DBUtil.getConnection();
                statement = connection.createStatement();
                for(String sql : getInitSQL()){
                    System.out.println(sql);
                    statement.executeUpdate(sql);
                }
            } finally {
                DBUtil.close(connection, statement);
            }
        } catch (Exception e) {
            throw new RuntimeException("初始化数据库失败", e);
        }
    }
}
