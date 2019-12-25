package app;

import util.Util;

import java.io.File;
import java.util.Objects;

/**
 * @ClassName FileMeta
 * @Description
 * @Author frank
 * @Date 2019/12/18 7:24 下午
 * @Version 1.0
 */
public class FileMeta {

    private Integer id;

    private String name;

    private String path;

    private Boolean isDirectory;

    private Long size;

    private Long lastModified;

    private String sizeText;

    private String lastModifiedText;

    public FileMeta(String name, String path, Boolean isDirectory, Long size, Long lastModified) {
        this.name = name;
        this.path = path;
        this.isDirectory = isDirectory;
        this.size = size;
        this.sizeText = Util.formatSize(size);
        this.lastModified = lastModified;
        this.lastModifiedText = Util.formatDate(lastModified);
    }

    public FileMeta(File file){
        this(file.getName(), file.getParent(), file.isDirectory(), file.length(), file.lastModified());
    }

    @Override
    public String toString() {
        return "FileMeta{" +
                "name=" + name +
                ", path=" + path +
                ", isDirectory=" + isDirectory +
                "}\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileMeta meta = (FileMeta) o;
        return Objects.equals(name, meta.name) &&
                Objects.equals(path, meta.path) &&
                Objects.equals(isDirectory, meta.isDirectory);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, path, isDirectory);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Boolean getDirectory() {
        return isDirectory;
    }

    public void setDirectory(Boolean directory) {
        isDirectory = directory;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Long getLastModified() {
        return lastModified;
    }

    public void setLastModified(Long lastModified) {
        this.lastModified = lastModified;
    }

    public String getSizeText() {
        return sizeText;
    }

    public void setSizeText(String sizeText) {
        this.sizeText = sizeText;
    }

    public String getLastModifiedText() {
        return lastModifiedText;
    }

    public void setLastModifiedText(String lastModifiedText) {
        this.lastModifiedText = lastModifiedText;
    }
}
