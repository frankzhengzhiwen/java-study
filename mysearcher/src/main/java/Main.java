import app.App;
import task.DBInit;
import task.FileScanner;
import task.FileSave;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @ClassName Main
 * @Description
 * @Author frank
 * @Date 2019/12/18 6:25 下午
 * @Version 1.0
 */
public class Main {
    public static void main(String[] args) throws InterruptedException {
//        DBInit.init();
//        FileScanner scanner = new FileScanner();
//        scanner.scanWait(new File("D:/Workspaces"), new FileSave());

        App.main(args);
    }
}
