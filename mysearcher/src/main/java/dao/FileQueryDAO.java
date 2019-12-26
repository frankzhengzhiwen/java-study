package dao;

import app.FileMeta;
import util.DBUtil;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FileQueryDAO {

    public static List<FileMeta> search(String like) throws SQLException {
        List<FileMeta> r = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = DBUtil.getConnection();
            String sql = "select fm.name, fm.path, fm.is_directory, fm.size, fm.last_modified" +
                     " from file_meta fm" +
                     " left join file_pinyin fp on fm.id=fp.file_meta_id" +
                     " where fm.name like ? or fp.value like ?" +
                     " group by fm.id" +
                     " order by fm.path,fm.name";
            statement = connection.prepareStatement(sql);
            statement.setString(1, "%"+like+"%");
            statement.setString(2, "%"+like+"%");
            resultSet = statement.executeQuery();
            while(resultSet.next()){
                String name = resultSet.getString("name");
                String path = resultSet.getString("path");
                Boolean isDirectory = resultSet.getBoolean("is_directory");
                long size = resultSet.getLong("size");
                Timestamp lastModified = resultSet.getTimestamp("last_modified");
                FileMeta meta = new FileMeta(name, path, isDirectory, size, lastModified.getTime());
                r.add(meta);
            }
        }finally {
            DBUtil.close(connection,statement,resultSet);
        }
        return r;
    }

    public static List<FileMeta> query(File dir) throws SQLException {
        List<FileMeta> r = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = DBUtil.getConnection();
            String sql = "select id, name, path, is_directory, size, last_modified" +
                    " from file_meta" +
                    " where path=?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, dir.getPath());
            resultSet = statement.executeQuery();
            while(resultSet.next()){
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String path = resultSet.getString("path");
                Boolean isDirectory = resultSet.getBoolean("is_directory");
                long size = resultSet.getLong("size");
                Timestamp lastModified = resultSet.getTimestamp("last_modified");
                FileMeta meta = new FileMeta(name, path, isDirectory, size, lastModified.getTime());
                meta.setId(id);
                r.add(meta);
            }
        }finally {
            DBUtil.close(connection,statement,resultSet);
        }
        return r;
    }
}
