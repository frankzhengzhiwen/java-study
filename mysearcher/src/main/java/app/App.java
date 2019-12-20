package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @ClassName App
 * @Description
 * @Author frank
 * @Date 2019/12/18 5:58 下午
 * @Version 1.0
 */
public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("app.fxml"));
        primaryStage.setTitle("搜索神器");
        primaryStage.setScene(new Scene(root, 1000, 800));
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
