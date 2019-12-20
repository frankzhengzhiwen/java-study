package util;

import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteDataSource;

import javax.sql.DataSource;
import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @ClassName DBUtil
 * @Description
 * @Author frank
 * @Date 2019/12/18 8:50 下午
 * @Version 1.0
 */
public class DBUtil {

    /**
     * 数据库文件名
     */
    private static final String DB_NAME = "mysearcher.db";

    /**
     * class文件编译路径：target
     */
    private static final String PATH;

    /**
     * jdbc数据库连接url
     */
    private static final String URL;

    /**
     * 数据库连接池
     */
    private static DataSource DATA_SOURCE;

    static{
        String classLoaderPath = DBUtil.class.getClassLoader().getResource("./").getPath();
        PATH = new File(classLoaderPath).getParent()+File.separator+DB_NAME;
        System.out.println("database path==="+PATH);
        URL = "jdbc:sqlite://" + PATH;
    }

    /**
     * 获取数据库连接池
     * @return
     */
    public static DataSource getDataSource(){
        if(DATA_SOURCE == null){
            synchronized (DBUtil.class){
                if(DATA_SOURCE == null){
                    SQLiteConfig config = new SQLiteConfig();
                    config.setDateStringFormat(Util.DATE_STRING_FORMAT);
                    DATA_SOURCE = new SQLiteDataSource(config);
                    ((SQLiteDataSource) DATA_SOURCE).setUrl(URL);
                }
            }
        }
        return DATA_SOURCE;
    }

    /**
     * 获取数据库连接
     * @return
     */
    public static Connection getConnection(){
        try {
            return getDataSource().getConnection();
        } catch (SQLException e) {
            throw new RuntimeException("数据库连接失败", e);
        }
    }

    /**
     * @see #close(Connection, Statement, ResultSet)
     */
    public static void close(Connection connection, Statement statement){
        close(connection, statement, null);
    }

    /**
     * 释放数据库资源
     * @param connection
     * @param statement
     * @param resultSet
     */
    public static void close(Connection connection, Statement statement, ResultSet resultSet){
        try {
            if(resultSet != null)
                resultSet.close();
            if(statement != null)
                statement.close();
            if(connection != null)
                connection.close();
        } catch (SQLException e) {
            throw new RuntimeException("释放数据库资源异常", e);
        }
    }
}
