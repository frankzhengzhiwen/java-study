package dao;

import app.FileMeta;
import util.DBUtil;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FileDeleteDAO {

    public static List<FileMeta> delete(List<FileMeta> metas) {
        List<FileMeta> r = new ArrayList<>();
        Connection connection = null;
        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;
        PreparedStatement ps3 = null;
        try {
            try {
                connection = DBUtil.getConnection();
                connection.setAutoCommit(false);
                String sql = "delete from file_pinyin" +
                        " where exists (" +
                        " select * from file_meta" +
                        " where file_meta.id=file_pinyin.file_meta_id" +
                        " and file_meta.id=?" +
                        " or (file_meta.path like ? and file_meta.is_directory=?)" +
                        ")";
                ps1 = connection.prepareStatement(sql);

                sql = "delete from file_meta" +
                        " where id=?" +
                        " or (path like ? and is_directory=?)";
                ps2 = connection.prepareStatement(sql);

                sql = "delete from file_meta" +
                        " where path=? or path like ?";
                ps3 = connection.prepareStatement(sql);

                for(FileMeta meta : metas){
                    ps1.setInt(1, meta.getId());
                    ps1.setString(2, meta.getPath()+"%");
                    ps1.setBoolean(3, true);
                    ps1.executeUpdate();
                    ps2.setInt(1, meta.getId());
                    ps2.setString(2, meta.getPath()+"%");
                    ps2.setBoolean(3, true);
                    ps2.executeUpdate();
                    if(meta.getDirectory()){
                        String path = meta.getPath()+File.separator+meta.getName();
                        ps3.setString(1, path);
                        ps3.setString(2, path+ File.separator+"%");
                        ps3.executeUpdate();
                    }
                }
                connection.commit();
            }finally {
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
        return r;
    }
}
