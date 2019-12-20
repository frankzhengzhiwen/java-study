package app;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import task.FileSearch;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @ClassName Controller
 * @Description
 * @Author frank
 * @Date 2019/12/18 5:58 下午
 * @Version 1.0
 */
public class Controller implements Initializable {

    @FXML
    private TextField searchField;

    @FXML
    private TableView<FileMeta> fileTable;

    public void initialize(URL location, ResourceBundle resources) {
        // 添加搜索框监听器，内容改变时执行监听事件
        searchField.textProperty().addListener(new ChangeListener<String>() {

            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                ObservableList<FileMeta> metas = fileTable.getItems();
                metas.clear();
                List<FileMeta> searchMetas = FileSearch.search(searchField.getText());
                metas.addAll(searchMetas);
            }
        });
    }

}
