package task;

import app.FileMeta;
import util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FileSearch {

    public static List<FileMeta> search(String like){
        List<FileMeta> r = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            try {
                connection = DBUtil.getConnection();
                String sql = "select fm.name, fm.path, fm.size, fm.last_modified" +
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
                    long size = resultSet.getLong("size");
                    Timestamp lastModified = resultSet.getTimestamp("last_modified");
                    FileMeta meta = new FileMeta(name, path, size, lastModified.getTime());
                    r.add(meta);
                }
            }finally {
                DBUtil.close(connection, statement, resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return r;
    }
}
