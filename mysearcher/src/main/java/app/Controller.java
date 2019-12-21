package app;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import task.DBInit;
import task.FileSave;
import task.FileScanner;
import task.FileSearch;

import java.io.File;
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
    private GridPane rootPane;

    @FXML
    private TextField searchField;

    @FXML
    private TableView<FileMeta> fileTable;

    @FXML
    private Label srcDirectory;

    private Thread directoryChooseTask = null;

    public void initialize(URL location, ResourceBundle resources) {
        // 添加搜索框监听器，内容改变时执行监听事件
        searchField.textProperty().addListener(new ChangeListener<String>() {

            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                freshTable();
            }
        });
    }

    public void choose(Event event) {
        // 选择文件目录
        DirectoryChooser directoryChooser=new DirectoryChooser();
        Window window = (Stage) rootPane.getScene().getWindow();
        File file = directoryChooser.showDialog(window);
        // 获取选择的目录路径，并显示
        String path = file.getPath();
        String previousPath = srcDirectory.getText();
        if(path != null && !path.equals(previousPath)){
            srcDirectory.setText(path);
            // 如歌之前有选择文件目录，且还在执行扫描任务，则直接关闭（中断通知来关闭）
            if(directoryChooseTask != null)
                directoryChooseTask.interrupt();

            directoryChooseTask = new Thread(new Runnable() {
                @Override
                public void run() {
                    // 根据选择的目录执行数据库初始化任务，并开启文件扫描
                    FileScanner scanner = new FileScanner();
                    try {
                        DBInit.init();
                        scanner.scanWait(new File(path), new FileSave());
                        freshTable();
                    } catch (InterruptedException e) {
                        // 中断通知，由scanWait中的semaphore.acquire();或latch.await();抛出
                        System.out.println("ShutDown scan task in path: "+ path);
                        scanner.shutDownNow();
                    }
                }
            });
            directoryChooseTask.start();
        }
    }

    // 刷新表格数据
    private void freshTable(){
        ObservableList<FileMeta> metas = fileTable.getItems();
        metas.clear();
        List<FileMeta> searchMetas = FileSearch.search(searchField.getText());
        metas.addAll(searchMetas);
    }

}
