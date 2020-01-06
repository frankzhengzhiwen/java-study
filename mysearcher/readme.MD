# 文件搜索项目说明

## 项目目标

- 巩固多线程、JDBC、集合框架等技术，并在代码设计上有一定成长
- 满足文件搜索的需求

## 项目使用技术栈与平台

- 所用技术：JavaFX、多线程、SQLite、pinyin4j
- 平台与环境：Windows/Mac，IDEA，Maven

## 项目背景

各个操作系统下，都有提供文件搜索的功能，如windows中查找文件：

![](./assets/windows-search.png)



除系统自带的文件搜索功能外，也有大佬开发的很多搜索神器，比如[Everything](https://www.voidtools.com/zh-cn/)：

![](./assets/Everything.Search.Window.png)

Everything是基于NTFS文件系统的USN Journal（Update SequenceNumber Journal），是利用操作系统记录的文件操作日志来进行搜索，特点是效率高，速度快，但是具有一定的局限性，只能用于NTFS文件系统。

本项目使用实时的本地文件进行搜索，保存文件信息以便于提高搜索效率。

## 项目功能

- 指定搜索目录，显示目录中的所有文件、文件夹信息
- 使用多线程进行文件搜索操作，文件信息保存在数据库。如果已保存有的文件信息，执行本地目录与数据库文件信息比对操作，在更新到数据库。
- 可以根据文件名进行搜索
- 文件名包含中文时，支持汉语拼音的搜索（全拼或是首字母匹配）

## 项目演示

### 选择文件目录

扫描本地文件，保存文件信息并显示

![](./assets/mysearcher-look1.png)

### 根据文件名搜索

![](./assets/mysearcher-look2.png)

### 根据全拼搜索

![](./assets/mysearcher-look3.png)

### 根据拼音首字母搜索

![](./assets/mysearcher-look4.png)

## 系统流程

### 整体流程：

![](./assets/mysearcher-flow.png)

### 文件对比流程

![](./assets/mysearcher-filetask.png)

## 技术栈介绍

### SQLite介绍

- SQLite是一款轻量级的嵌入式内存数据库（嵌入在进程中，运行在内存中的数据库），使用 ANSI-C 编写的，并提供了简单和易于使用的 API。
- 一个完整的 SQLite 数据库是存储在一个单一的跨平台的磁盘文件。
- 非常小，是轻量级的，完全配置时小于 400KiB，省略可选功能配置时小于250KiB。
- 无需安装、配置及管理。
- 完全兼容 ACID 事务，允许从多个进程或线程安全访问。
- 支持多种开发语言，C, C++, PHP, Perl, Java, C#,Python, Ruby等
- 支持 SQL92（SQL2）标准的大多数查询语言的功能。

### Pinyin4j介绍

- 是一个Java的内库，提供对中文汉字到拼音的转换

- 存在多音字的情况，根据一个字符可以获取多个字符串

  ```java
  String[] pinyins = PinyinHelper.toHanyuPinyinStringArray('长');
  ```

  输出：

  [zhang3, chang2]

- 可以配置输出格式，包括大小写、是否带音调（默认带）、是否使用v（如绿的拼音lv）

### JavaFX介绍

- Java客户端UI库：JavaFX是一个强大的图形和多媒体处理工具包集合，它允许开发者来设计、创建、测试、调试和部署富客户端程序，并且和Java一样跨平台。
- 提供了丰富的客户端组件：面板、按钮、文本框、表格等，还提供了各类事件、动画效果支持。
- 提供了Web组件支持：HTML5的支持、CSS样式支持，JavaScript脚本支持。
- 还提供了[Scene Builder](http://www.oracle.com/technetwork/java/javase/downloads/javafxscenebuilder-info-2157684.html) 程序，来支持拖拽式界面开发。
- Java原有Swing、AWT客户端UI工具包，JavaFX 系统界面样式跟美观、系统架构对开发更友好、便捷

## 系统设计

### 基础设施搭建：数据库设计

+ 需要保存的文件基本信息有：**文件名称**、**路径**、**大小**、**上次修改时间**
+ 文件名包含中文时，要满足全拼和拼音首字母的搜索，所以还需要保存**全拼**和**拼音首字母**
+ 中文汉字可能存在多音字的情况，我们这里只实现简单的，取第一个拼音即可

### 基础设施搭建：JDBC工具类设计

+ 使用SQLite，需要先指定本地数据库文件路径，如果没有则创建该文件
+ 提供获取数据库连接的方法
+ 提供释放数据库资源的方法

### 基础设施搭建：数据库初始化任务

+ 读取本地写好的sql文件，再执行数据库初始化建表操作

### 基础设施搭建：拼音工具类

+ 需要提供获取文件名的全拼及拼音首字母的方法

### 基础设施搭建：JavaFX界面设计

+ 界面的搭建
+ 绑定选择文件目录事件，在每次事件发生时，调用该事件方法：启动目录扫描任务，并在完成后刷新表格
+ 绑定搜索框内容改变事件，在每次事件发生时，调用该事件方法：搜索文件信息，并刷新表格

### 基础设施搭建：多线程目录扫描任务设计

+ 入口是在选择文件目录事件发生后，根据选定的目录进行扫描
+ 扫描到目录（文件夹）时，执行目录下一级子文件的信息对比、更新操作
+ 如果目录下一级子文件是文件夹，则开启新的子任务处理这个子文件夹
+ 在所有任务都完成以后，刷新表格

### 业务功能开发：本地文件、数据库文件对比及更新

+ 本地有，数据库没有，插入该文件信息
+ 数据库有，本地没有，删除数据库中该文件信息。如果该文件是文件夹，还需要删除所有在该文件夹下的文件

### 业务功能开发：文件搜索

+ 根据搜索值，模糊匹配文件名，全拼或拼音首字母字段，任意匹配成功都可以获取文件信息

## 开发步骤

### 创建Maven项目

在pom.xml中引入依赖包，及配置编译版本：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>frank</groupId>
    <artifactId>mysearcher</artifactId>
    <version>1.0-SNAPSHOT</version>

    <dependencies>
        <dependency>
            <groupId>com.belerweb</groupId>
            <artifactId>pinyin4j</artifactId>
            <version>2.5.1</version>
        </dependency>
        <dependency>
            <groupId>org.xerial</groupId>
            <artifactId>sqlite-jdbc</artifactId>
            <version>3.28.0</version>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.8.1</version>
                <configuration>
                    <source>1.8</source>
                    <target>1.8</target>
                    <encoding>UTF-8</encoding>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-jar-plugin</artifactId>
                <version>3.1.0</version>
                <configuration>
                    <archive>
                        <manifest>
                            <mainClass>Main</mainClass> <!-- 指定入口类 -->
                            <addClasspath>true</addClasspath> <!-- 在jar的MF文件中生成classpath属性 -->
                            <classpathPrefix>lib/</classpathPrefix> <!-- classpath前缀,即依赖jar包的路径 -->
                        </manifest>
                    </archive>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-dependency-plugin</artifactId>
                <version>3.1.1</version>
                <executions>
                    <execution>
                        <id>copy</id>
                        <phase>package</phase>
                        <goals>
                            <goal>copy-dependencies</goal>
                        </goals>
                        <configuration>
                            <!-- 指定依赖包的输出路径，需与上方的classpathPrefix保持一致 -->
                            <outputDirectory>${project.build.directory}/lib</outputDirectory>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
    
</project>
```

### 设计数据库表

```sql
CREATE TABLE IF NOT EXISTS file_meta (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name VARCHAR(50) NOT NULL,
    path VARCHAR(1000) NOT NULL,
    is_directory BOOLEAN NOT NULL,
    pinyin VARCHAR(50) NOT NULL,
    pinyin_first VARCHAR(50) NOT NULL,
    size BIGINT NOT NULL,
    last_modified TIMESTAMP NOT NULL
);
```

### 设计数据库工具类，及表初始化操作

+ URL格式为：jdbc:sqlite://数据库本地文件路径，没有用户名密码。使用URL创建DataSource对象后，会以该路径文件作为数据库，没有则创建该文件。
+ 设计单例模式获取DataSource连接池对象。及通过DataSource获取数据库连接的方法。
+ 设计释放资源的方法：关闭连接Conncetion、关闭数据库操作对象Statement、关闭结果集对象ResultSet。
+ 初始化表：使用IO文件流进行sql文件读取，并执行。需要考虑多行sql代码的情况、sql注释的情况。

### 设计汉语拼音工具

+ 使用第三方库提供的汉字到拼音的转换，一个汉字转换出来可能有多个拼音。

  配置汉字的字符范围，及拼音的输出格式（小写、不带音调、包含V字符）

  ```java
  /**
   * 中文字符格式
   */
  private static final String CHINESE_PATTERN = "[\\u4E00-\\u9FA5]";
  
  /**
   * 拼音输出格式
   */
  private static final HanyuPinyinOutputFormat PINYIN_OUTPUT_FORMAT;
  
  static{
      PINYIN_OUTPUT_FORMAT = new HanyuPinyinOutputFormat();
  
      // 输出小写
      PINYIN_OUTPUT_FORMAT.setCaseType(HanyuPinyinCaseType.LOWERCASE);
      // 不带音调
      PINYIN_OUTPUT_FORMAT.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
      // 包含字符v
      PINYIN_OUTPUT_FORMAT.setVCharType(HanyuPinyinVCharType.WITH_V);
  }
  ```

  API有提供**单个字符**的拼音转换：

  ```java
  String[] temp = PinyinHelper.toHanyuPinyinStringArray(c, PINYIN_OUTPUT_FORMAT);
  ```

+ 多个字符组成的文件名，需要按照每个字符返回的字符串数组进行排列组合，我们只简单实现，每个字符取第一个返回的字符串作为拼音。拼音分全拼，和汉字首字母拼接的两种方式

### 设计界面及文件信息搜索

创建javafx界面文件

（1）Main.java

```java
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("app.fxml"));
        primaryStage.setTitle("文件搜索");
        primaryStage.setScene(new Scene(root, 1000, 800));
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
```

（2）app.fxml

```xml
<?xml version="1.0" encoding="UTF-8"?>

<?import javafx.geometry.*?>
<?import javafx.scene.layout.*?>
<?import javafx.scene.control.*?>
<?import javafx.scene.control.cell.*?>

<GridPane fx:id="rootPane" alignment="center" hgap="10" vgap="10" xmlns:fx="http://javafx.com/fxml/1" xmlns="http://javafx.com/javafx/8" fx:controller="app.Controller">
   <children>
   
       <Button onMouseClicked="#choose" prefWidth="90" text="选择目录" GridPane.columnIndex="0" GridPane.rowIndex="0" />
       <Label fx:id="srcDirectory">
         <GridPane.margin>
            <Insets left="100.0" />
         </GridPane.margin>
      </Label>
       <TextField fx:id="searchField" prefWidth="900" GridPane.columnIndex="0" GridPane.rowIndex="1" />
   
       <TableView fx:id="fileTable" prefHeight="700" prefWidth="900" GridPane.columnIndex="0" GridPane.columnSpan="2" GridPane.rowIndex="2">
           <columns>
               <TableColumn fx:id="nameColumn" prefWidth="220" text="名称">
                   <cellValueFactory>
                       <PropertyValueFactory property="name" />
                   </cellValueFactory>
               </TableColumn>
               <TableColumn prefWidth="400" text="路径">
                   <cellValueFactory>
                       <PropertyValueFactory property="path" />
                   </cellValueFactory>
               </TableColumn>
               <TableColumn fx:id="sizeColumn" prefWidth="120" text="大小">
                   <cellValueFactory>
                       <PropertyValueFactory property="sizeText" />
                   </cellValueFactory>
               </TableColumn>
               <TableColumn fx:id="lastModifiedColumn" prefWidth="160" text="修改时间">
                   <cellValueFactory>
                       <PropertyValueFactory property="lastModifiedText" />
                   </cellValueFactory>
               </TableColumn>
           </columns>
       </TableView>
   </children>
</GridPane>

```

（3）Controller

```java
public class Controller implements Initializable {

    @FXML
    private GridPane rootPane;

    @FXML
    private TextField searchField;

    @FXML
    private TableView<FileMeta> fileTable;

    @FXML
    private Label srcDirectory;

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
        Window window = rootPane.getScene().getWindow();
        File file = directoryChooser.showDialog(window);
        if(file == null)
            return;
        // 获取选择的目录路径，并显示
        String path = file.getPath();
        // TODO
    }

    // 刷新表格数据
    private void freshTable(){
        ObservableList<FileMeta> metas = fileTable.getItems();
        metas.clear();
        // TODO
    }

}
```

### 设计多线程扫描任务及文件信息保存

- 多线程执行大量任务，使用线程池来提高执行效率
- 为便于在线程中执行的任务有较好的扩展性，可以考虑使用接口回调的方式实现。传入时设定为文件信息保存的任务。
- 在线程执行时，待执行任务数+1， 执行完后，待执行任务数-1，开启子任务时，每个子任务都执行任务数+1操作，这样在最后可以判断出是否所有线程执行完毕。

## 项目总结

- 项目优点：使用多线程来进行文件遍历，提高了效率。
- 项目缺点：中文汉字多音字没有进行排列组合，只能支持多音字拼音的一种组合搜索。
- 项目扩展：我们还能将该项目写的更完善，可以往以下几个发展方向走：
  1. 多音字支持
  2. 将项目打包成exe安装文件

### 8. 常见项目面试题

- 你为什么要做这个项目?
- 你的项目有什么缺点?
- 你的项目参考了什么项目？
- 多线程做了什么事情，为什么使用多线程？如何保证线程安全？如何在所有任务执行完之前等待？
- 为什么使用SQLite？有什么优点？

### 9. 项目参考资料

- ### [*SQLite* Home Page](https://www.baidu.com/link?url=faQszQBrk50jxTgI_cjce_AEPHn9SMdzmUt3sV7P77_&wd=&eqid=d57fa9ea00045725000000025e05af22)

- ### **[Pinyin4j](https://github.com/belerweb/pinyin4j)**、http://pinyin4j.sourceforge.net/

- ### [JavaFX官网](https://www.oracle.com/technetwork/java/javase/documentation/javafx-docs-2159875.html)、[FX China](http://www.javafxchina.net/blog/docs/)、[JavaFX 教程](https://code.makery.ch/zh-cn/library/javafx-tutorial/)