package app;

import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import util.Util;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * @ClassName FileMeta
 * @Description
 * @Author frank
 * @Date 2019/12/18 7:24 下午
 * @Version 1.0
 */
public class FileMeta {

    private SimpleStringProperty name;

    private SimpleStringProperty path;

    private Long size;

    private Long lastModified;

    private SimpleStringProperty sizeText;

    private SimpleStringProperty lastModifiedText;

    public FileMeta(String name, String path, Long size, Long lastModified) {
        this.name = new SimpleStringProperty(name);
        this.path = new SimpleStringProperty(path);
        this.size = size;
        this.sizeText = new SimpleStringProperty(Util.formatSize(size));
        this.lastModified = lastModified;
        this.lastModifiedText = new SimpleStringProperty(Util.formatDate(lastModified));
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getPath() {
        return path.get();
    }

    public SimpleStringProperty pathProperty() {
        return path;
    }

    public void setPath(String path) {
        this.path.set(path);
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
        return sizeText.get();
    }

    public SimpleStringProperty sizeTextProperty() {
        return sizeText;
    }

    public void setSizeText(String sizeText) {
        this.sizeText.set(sizeText);
    }

    public String getLastModifiedText() {
        return lastModifiedText.get();
    }

    public SimpleStringProperty lastModifiedTextProperty() {
        return lastModifiedText;
    }

    public void setLastModifiedText(String lastModifiedText) {
        this.lastModifiedText.set(lastModifiedText);
    }
}
