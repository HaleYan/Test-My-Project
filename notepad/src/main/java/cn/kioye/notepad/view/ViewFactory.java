package cn.kioye.notepad.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.*;

import java.io.File;
import java.util.List;
import java.util.Optional;

public class ViewFactory extends LoadingView {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {

            FXMLLoader fxmlLoader = new FXMLLoader(ViewFactory.class.getClassLoader().getResource("MainView.fxml"));

            AnchorPane root = fxmlLoader.load();
            Scene scene = new Scene(root);
            primaryStage.setTitle("Notepad");
            primaryStage.setScene(scene);
//            primaryStage.setResizable(false);

            MainView mainView = fxmlLoader.getController();
//            primaryStage.setOnCloseRequest(event -> {
//                mainView.onCloseRequest();
//            });
            mainView.setStage(primaryStage);
            primaryStage.setMinWidth(750);
            primaryStage.setMinHeight(600);
            mainView.initWindowSize();
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static LoadingView loadingView = null;

    /**
     * 加载窗口
     */
    public static LoadingView initLoading(Stage stage) {
        try {
            if (loadingView == null || loadingView.getStage() == null) {
                Stage loadingStage = new Stage(StageStyle.TRANSPARENT);
                loadingStage.initModality(Modality.APPLICATION_MODAL);
                FXMLLoader fxmlLoader = new FXMLLoader(ViewFactory.class.getClassLoader().getResource("LoadingView.fxml"));

                AnchorPane root = fxmlLoader.load();
                Scene scene = new Scene(root);
                loadingStage.setTitle("加载中..");
                scene.setFill(null);
                loadingStage.setScene(scene);
                loadingStage.setResizable(false);
                loadingView = fxmlLoader.getController();
                loadingView.setStage(loadingStage);
                loadingView.getStage().initOwner(stage);
            }

            loadingView.reset();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return loadingView;
    }

    public static DictionaryView showFontSetting(Stage stage) {
        try {
            Stage settingStage = new Stage();
            settingStage.initModality(Modality.APPLICATION_MODAL);
            FXMLLoader fxmlLoader = new FXMLLoader(ViewFactory.class.getClassLoader().getResource("Dictionary.fxml"));

            AnchorPane root = fxmlLoader.load();
            Scene scene = new Scene(root);
            settingStage.setTitle("字体样式设置");
            settingStage.setScene(scene);
            settingStage.setResizable(false);

            DictionaryView dictionaryView = fxmlLoader.getController();
            dictionaryView.setStage(settingStage);
            settingStage.initOwner(stage);
            settingStage.show();
            return dictionaryView;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 弹窗消息
     */
    public static Alert alert(Stage stage, String p_header, String p_message) {
        Alert _alert = new Alert(Alert.AlertType.INFORMATION);
        _alert.setTitle("提示");
        _alert.setHeaderText(p_header);
        _alert.setContentText(p_message);
        _alert.initOwner(stage);
        _alert.show();
        return _alert;
    }

    /**
     * 弹出不保存退出确认对话框
     */
    public static ButtonBar.ButtonData confirmDialog(Stage stage, String p_header, String p_message) {
//        按钮部分可以使用预设的也可以像这样自己 new 一个
        Alert _alert = new Alert(Alert.AlertType.CONFIRMATION, p_message,
                new ButtonType("取消", ButtonBar.ButtonData.NO),
                new ButtonType("不保存", ButtonBar.ButtonData.LEFT),
                new ButtonType("保存", ButtonBar.ButtonData.YES)
        );
//        设置窗口的标题
        _alert.setTitle("确认");
        _alert.setHeaderText(p_header);
//        设置对话框的 icon 图标，参数是主窗口的 stage
        _alert.initOwner(stage);
//        showAndWait() 将在对话框消失以前不会执行之后的代码
        Optional<ButtonType> _buttonType = _alert.showAndWait();
//        根据点击结果返回
        return _buttonType.get().getButtonData();
    }

    public static File showDirectoryChooser(Stage stage) {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("选择文件夹");
        File chosenDir = chooser.showDialog(stage);
        if (chosenDir != null) {
            System.out.println("path:" + chosenDir.getAbsolutePath());
        } else {
            System.out.print("no directory chosen");
        }
        return chosenDir;
    }


    public static List<File> openFilesChooser(Stage stage) {
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("选择文件");
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
        );
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("文本文件", "*.txt", "*.log"),
                new FileChooser.ExtensionFilter("所有文件", "*.*")
        );
        return fileChooser.showOpenMultipleDialog(stage);
    }

    public static File saveFileChooser(Stage stage, String fileName) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("保存文件");
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
        );
        fileChooser.setInitialFileName(fileName);
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("文本文件", "*.txt", "*.log"),
                new FileChooser.ExtensionFilter("所有文件", "*.*")
        );
        return fileChooser.showSaveDialog(stage);
    }

//    private static Desktop desktop = Desktop.getDesktop();
//    public static void openFile(File file) {
//        try {
//            desktop.open(file);
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
//    }
}
