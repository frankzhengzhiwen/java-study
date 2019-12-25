package dao;

import app.FileMeta;
import util.DBUtil;
import util.Pinyin4jUtil;

import java.io.File;
import java.sql.*;
import java.util.List;
import java.util.Set;

public class FileSaveDAO {

    public static void save(List<FileMeta> metas) {
        Connection connection = null;
        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;
        ResultSet resultSet = null;
        try {
            try {
                // 获取数据库连接，开启事务
                connection = DBUtil.getConnection();
                connection.setAutoCommit(false);
                // 保存文件信息
                String sql = "insert into file_meta(name,path,is_directory,size,last_modified)" +
                        " values (?,?,?,?,?)";
                ps1 = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                sql = "insert into file_pinyin(value, file_meta_id) values (?, ?)";
                ps2 = connection.prepareStatement(sql);
                for(FileMeta meta : metas){
                    System.out.println("insert:"+meta.getPath()+ File.separator+meta.getName());
                    ps1.setString(1, meta.getName());
                    ps1.setString(2, meta.getPath());
                    ps1.setBoolean(3, meta.getDirectory());
                    ps1.setLong(4, meta.getSize());
                    ps1.setTimestamp(5, new Timestamp(meta.getLastModified()));
                    ps1.executeUpdate();

                    resultSet = ps1.getGeneratedKeys();
                    resultSet.next();
                    int id = resultSet.getInt(1);

                    // 根据文件名获取拼音，如果有汉字，则保存拼音信息
                    Set<String> pinyins = Pinyin4jUtil.get(meta.getName());
                    for(String value : pinyins){
                        ps2.setString(1, value);
                        ps2.setInt(2, id);
                        ps2.executeUpdate();
                    }
                }
                connection.commit();
            } finally {
                DBUtil.close(ps1, ps2);
                DBUtil.close(connection);
            }
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
                throw new RuntimeException("文件删除回滚失败", ex);
            }
            e.printStackTrace();
            throw new RuntimeException("文件删除出错", e);
        }
    }
}
