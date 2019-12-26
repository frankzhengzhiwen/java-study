package dao;

import app.FileMeta;
import util.DBUtil;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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

                for(FileMeta meta : metas){
                    String sql = "delete from file_pinyin"
                            + " where exists ("
                            + " select * from file_meta" +
                            " where file_meta.id=file_pinyin.file_meta_id"
                            + " and file_meta.id=?"
                            + (meta.getDirectory() ?
                                " or (file_meta.path=? or file_meta.path like ?)"
                                : "")
                            + ")";
                    ps1 = connection.prepareStatement(sql);

                    sql = "delete from file_meta"
                            + " where id=?"
                            + (meta.getDirectory() ?
                            " or (path=? or path like ?)"
                            : "");
                    ps2 = connection.prepareStatement(sql);

                    ps1.setInt(1, meta.getId());
                    ps2.setInt(1, meta.getId());

                    String path = null;
                    if(meta.getDirectory()){
                        path = meta.getPath()+File.separator+meta.getName();
                        ps1.setString(2, path);
                        ps1.setString(3, path+ File.separator+"%");
                        ps2.setString(2, path);
                        ps2.setString(3, path+ File.separator+"%");
                    }
                    System.out.println("delete: id="+meta.getId()+(meta.getDirectory()?", or under "+path:""));
                    ps1.executeUpdate();
                    ps2.executeUpdate();
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
