package task;

import app.FileMeta;
import util.DBUtil;
import util.Pinyin4jUtil;
import util.Util;

import java.math.BigDecimal;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Set;

public class FileSave implements FileScanner.Callback {
    @Override
    public void execute(FileMeta meta) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            try {
                // 获取数据库连接，开启事务
                connection = DBUtil.getConnection();
                connection.setAutoCommit(false);
                // 保存文件信息
                String sql = "insert into file_meta(name,path,size,last_modified)" +
                        " values (?,?,?,?)";
                statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                statement.setString(1, meta.getName());
                statement.setString(2, meta.getPath());
                statement.setLong(3, meta.getSize());
                statement.setTimestamp(4, new Timestamp(meta.getLastModified()));
                statement.executeUpdate();
                resultSet = statement.getGeneratedKeys();
                resultSet.next();
                int id = resultSet.getInt(1);

                // 根据文件名获取拼音，如果有汉字，则保存拼音信息
                Set<String> pinyins = Pinyin4jUtil.get(meta.getName());
                sql = "insert into file_pinyin(value, file_meta_id) values (?, ?)";
                for(String value : pinyins){
                    statement = connection.prepareStatement(sql);
                    statement.setString(1, value);
                    statement.setInt(2, id);
                    statement.executeUpdate();
                }
                connection.commit();
            } finally {
                DBUtil.close(connection, statement, resultSet);
            }
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException("保存文件信息回滚失败", ex);
            }
            throw new RuntimeException("保存文件信息错误", e);
        }
    }
}
