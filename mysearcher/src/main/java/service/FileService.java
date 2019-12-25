package service;

import app.FileMeta;
import dao.FileDeleteDAO;
import dao.FileQueryDAO;
import dao.FileSaveDAO;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件扫描回调类
 * 扫描的本地文件夹下一级子文件
 * 与数据库中的文件信息进行对比：
 * 1.本地有，数据库没有，执行插入操作
 * 2.本地没有，数据库有，执行删除操作
 */
public class FileService {

    public void process(File dir) throws SQLException {

        // 本地获取到的文件夹及下一级文件、文件夹
        List<FileMeta> scanned = new ArrayList<>();
        scanned.add(new FileMeta(dir));
        File[] subs = dir.listFiles();
        if(subs != null){
            for(File sub : subs)
                scanned.add(new FileMeta(sub));
        }

        // 数据库中保存的该文件夹下一级子文件
        List<FileMeta> saved = FileQueryDAO.query(dir);

        // 本地没有，数据库有，需要删除数据
        // 定义待删除的文件信息
        List<FileMeta> forDeletes = new ArrayList<>();
        for(FileMeta meta : saved){
            if(!scanned.contains(meta)){
                forDeletes.add(meta);
            }
        }
        FileDeleteDAO.delete(forDeletes);
        // 数据库没有，本地有，需要新增数据
        // 定义待插入的文件信息
        List<FileMeta> forInserts = new ArrayList<>();
        for(FileMeta meta : scanned){
            if(!saved.contains(meta)){
                forInserts.add(meta);
            }
        }
        FileSaveDAO.save(forInserts);
    }

    public static List<FileMeta> search(String like) throws SQLException {
        return FileQueryDAO.search(like);
    }
}
