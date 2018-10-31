package cn.kioye.notepad.view;

import cn.kioye.notepad.utils.FileUtil;
import cn.kioye.notepad.utils.Preferences;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.prefs.BackingStoreException;


public class MainView {
    private Stage primaryStage;

    public void setStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }


    @FXML
    private TabPane tabs;
    @FXML
    private AnchorPane rootPane;

    /**
     * 初始化一些组件
     */
    @FXML
    private void initialize() {
        System.out.println("初始化窗口 MainView ");

        // 初始化Prefrence
        //------
        tabs.getTabs().clear();
        // 延迟初始化

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(200), ev -> {
            showHelp();
        }));
        timeline.setCycleCount(1);
        timeline.play();

        String path = Preferences.getPath();

        // 初始化控件
        EventHandler<DragEvent> dragEvent = (DragEvent event) -> {
            if (event.getEventType().getName().equalsIgnoreCase("DRAG_OVER")) {
                event.acceptTransferModes(TransferMode.ANY);
            } else if (event.getEventType().getName().equalsIgnoreCase("DRAG_DROPPED")) {
                Dragboard dragboard = event.getDragboard();
                List<File> files = dragboard.getFiles();
                if (files == null) {
                    return;
                }
                openFiles(files);
            }
        };
        tabs.setOnDragOver(dragEvent);
        tabs.setOnDragDropped(dragEvent);

        tabs.getSelectionModel().selectedItemProperty().addListener(event -> {
            System.out.println(event);
            int selectedIndex = tabs.getSelectionModel().getSelectedIndex();
        });

        onLoad();
        updateLineNum();


        System.out.println("初始化窗口 MainView 完成");
    }

    private void showHelp() {
        addTab("Notepad 帮助", "欢迎使用Notepad（v1.0）\n\n" +
                "使用说明：\n\n" +
                "1. 打开.txt、.log等文本文件\n" +
                "\t 方法一：菜单栏->文件(F)->打开(O)->选择你要打开的文件(支持同时选择多个)\n" +
                "\t 方法二：在文件管理器下直接将文件拖进Notepad\n\n" +
                "2. 文本的编辑\n" +
                "\t 用户根据个人意向进行文本编辑\n\n" +
                "3. 字体样式设置\n" +
                "\t 菜单栏->格式(O)->字体样式(F)\n\n" +
                "4. 文本内容的查找、替换\n" +
                "\t 菜单栏->编辑(E)->查找(F)或替换(R)\n\n" +
                "以上操作或有相应快捷键，则可以直接使用快捷键。\n\n\n", null);
    }

    private void updateLineNum() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), ev -> {
            for (Tab tab : tabs.getTabs()) {
                VBox contentBox = (VBox) tab.getContent();
                TextArea content = (TextArea) contentBox.getChildren().get(0);
                HBox bottomBox = (HBox) contentBox.getChildren().get(contentBox.getChildren().size() - 1);
                Label contentInfo = (Label) bottomBox.getChildren().get(0);
                String text = content.getText();
                int length = 0;
                int line = 0;
                if (text != null && !text.trim().isEmpty()) {
                    length = text.length();
                    line = text.split("\\n").length;
                }
                contentInfo.setText("字符数：" + length + "  行数：" + line);
            }
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private Color fontColor;
    private Color fontBgColor;
    private double fontSize;
    private double fontOpacity;

    private void onLoad() {
        java.util.prefs.Preferences preferences = Preferences.getPreferences();
        fontColor = Color.valueOf(preferences.get("fontColor", "#000000"));
        fontBgColor = Color.valueOf(preferences.get("fontBgColor", "#ffffff"));
        fontSize = preferences.getDouble("fontSize", 12);
        fontOpacity = preferences.getDouble("fontOpacity", 1);
    }

    private static Map<Tab, File> tabMap = new ConcurrentHashMap<>();

    private void updateSingleContentWidth(Tab tab) {
        VBox contentBox = (VBox) tab.getContent();
        TextArea content = (TextArea) contentBox.getChildren().get(0);
        content.setPrefWidth(primaryStage.getWidth());
    }

    private void updateSingleContentSize(Tab tab) {
        updateSingleContentWidth(tab);
        updateSingleContentHeight(tab);
    }

    private void updateSingleContentHeight(Tab tab) {
        VBox contentBox = (VBox) tab.getContent();
        ObservableList<Node> children = contentBox.getChildren();
        TextArea content = (TextArea) children.get(0);
        double barHeight = 0;
        for (int i = 1; i < children.size(); i++) {
            Node node = children.get(i);
            if (node instanceof HBox) {
                HBox bar = (HBox) node;
                barHeight += bar.getPrefHeight();
            }
        }
        content.setPrefHeight(primaryStage.getHeight() - 85 - barHeight);// 85 33/66 20
    }

    private void updateContentSize(String name) {
        if (name.equals("width")) {
            tabs.setPrefWidth(primaryStage.getWidth());
            for (Tab tab : tabs.getTabs()) {
                updateSingleContentWidth(tab);
            }
        } else if (name.equals("height")) {
            tabs.setPrefHeight(primaryStage.getHeight() - 52);
            for (Tab tab : tabs.getTabs()) {
                updateSingleContentHeight(tab);
            }
        }
    }

    public void initWindowSize() {
        ChangeListener<Number> windowSizeChange = (ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            String name = StringUtils.substringBetween(observable.toString(), "name:", ",").trim();
            System.out.println(name);

            updateContentSize(name);
            System.out.println(observable);
        };
        primaryStage.widthProperty().addListener(windowSizeChange);
        primaryStage.heightProperty().addListener(windowSizeChange);
    }

    private static class InnerChangeListener {
        private ChangeListener<String> changeListener;

        public ChangeListener<String> getChangeListener() {
            return changeListener;
        }

        public void setChangeListener(ChangeListener<String> changeListener) {
            this.changeListener = changeListener;
        }
    }

    private void updateViewPattern(Tab tab) {

//        if (tab.getId()!=null&&!tab.getId().isEmpty())return;
        VBox contentBox = (VBox) tab.getContent();
        TextArea content = (TextArea) contentBox.getChildren().get(0);
        Region region = (Region) content.lookup(".content");
        if (region != null) {
            region.setBackground(new Background(new BackgroundFill(fontBgColor, CornerRadii.EMPTY, Insets.EMPTY)));
            tab.setId(System.currentTimeMillis() + "");
        }
//        region.setStyle("-fx-background-color: yellow");
//
        content.setFont(Font.font(fontSize));
        content.setOpacity(fontOpacity);
        //-fx-border-color: #456456;
        content.setStyle(" -fx-text-fill: " + fontColor.toString().replace("0x", "#") + ";");
    }

    public Tab addTab(String title, String contentStr, File file) {

        Tab tab = new Tab();
        HBox titleBar = new HBox();
        titleBar.setAlignment(Pos.CENTER);
        Label tabTitle = new Label(title);
        tabTitle.setPadding(new Insets(5, 5, 0, 0));
        Button closeTabBtn = new Button("X");
        closeTabBtn.setStyle("-fx-background-radius: 8;");
        closeTabBtn.setOnAction(event -> {
            String text = tabTitle.getText();
            if (text.substring(0, 1).equals("*")) {
                ButtonBar.ButtonData buttonData = ViewFactory.confirmDialog(primaryStage, "你是否想要保存已修改的文件:\n" + title + " ?", "如果你不保存，你的修改将不会生效！");
                switch (buttonData) {
                    case YES:
                        saveOrReSave("saveFile", tab, tabTitle);
                    case LEFT:

                        break;
                    case NO:
                        return;
                }
            }
            tabs.getTabs().remove(tab);
            tabMap.remove(tab);
        });
        closeTabBtn.setFont(Font.font(9));
        titleBar.getChildren().addAll(tabTitle, closeTabBtn);
        tab.setGraphic(titleBar);

        VBox contentBox = new VBox();
        TextArea content = new TextArea(contentStr);

        content.setWrapText(true);
        InnerChangeListener innerChangeListener = new InnerChangeListener();

        ChangeListener<String> changeListener = (ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            String text = tabTitle.getText();
            if (!text.substring(0, 1).equals("*"))
                tabTitle.setText("*" + tabTitle.getText());
            observable.removeListener(innerChangeListener.getChangeListener());
        };
        innerChangeListener.setChangeListener(changeListener);
        content.textProperty().addListener(changeListener);

        HBox bottomBox = new HBox();
        bottomBox.setPadding(new Insets(3, 0, 0, 8));

        Label lineNumLabel = new Label();
        lineNumLabel.setPrefWidth(200);
        Label lastSaveLabel = new Label();
        lastSaveLabel.setPrefWidth(210);
        lastSaveLabel.setAlignment(Pos.CENTER);
        if (file != null && file.exists()) {
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(file.lastModified());
            lastSaveLabel.setText("上次保存：" + sdf.format(cal.getTime()));
        }
        lastSaveLabel.setTextFill(Color.valueOf("#234423"));
        Label saveStatusLabel = new Label();
        saveStatusLabel.setPrefWidth(350);
        saveStatusLabel.setAlignment(Pos.CENTER_RIGHT);
        saveStatusLabel.setTextFill(Color.valueOf("#423143"));

        bottomBox.getChildren().addAll(lineNumLabel, lastSaveLabel, saveStatusLabel);
        contentBox.getChildren().addAll(content, bottomBox);
        tab.setContent(contentBox);
        // 延迟 初始化TextArea样式
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(200), ev -> {
            updateViewPattern(tab);
        }));
        timeline.setCycleCount(1);
        timeline.play();

        // 更新内容容器大小
        updateSingleContentSize(tab);
//        content.setPrefWidth(primaryStage.getWidth());
//        content.setPrefHeight(primaryStage.getHeight()-105);// 85 20

        tabs.getTabs().add(tab);
        if (file != null) {
            tabMap.put(tab, file);
        }
        tabs.getSelectionModel().select(tab);
        return tab;
    }

    private void openFiles(List<File> files){
        if (files != null) {
            for (File file : files) {
//                    if (!StringUtils.endsWithIgnoreCase(file.getName(), ".txt")) {
//                        continue;
//                    }
                try {
                    String content = FileUtil.readToStr(file);
                    String fileName = file.getName();
                    addTab(fileName, content, file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    /**
     * 顶部导航菜单栏
     */

    @FXML
    public void onTopMenu(Event event) {
        MenuItem menuItem = (MenuItem) event.getSource();
        System.out.println(menuItem);
        String id = menuItem.getId();

        switch (id) {
            // 文件(F)
            case "newFile":
                addTab("新建文本", null, null);
                break;
            case "openFile": {

                List<File> files = ViewFactory.openFilesChooser(primaryStage);
                openFiles(files);
            }
            break;
            case "saveFile":
            case "reSaveFile": {
                Tab currentTab = tabs.getSelectionModel().getSelectedItem();
                HBox titleBar = (HBox) currentTab.getGraphic();
                Label title = (Label) titleBar.getChildren().get(0);
                saveOrReSave(id, currentTab, title);
            }
            break;
            case "menuHide":
                primaryStage.setIconified(true);
                break;
            case "menuExit":
                primaryStage.close();
                break;
            // 编辑(E)
            case "menuCancel":

                break;
            case "menuShear":

                break;
            case "menuCopy":

                break;
            case "menuPaste":

                break;
            case "menuDel":

                break;
            case "menuSearch": {
                Tab selectedItem = tabs.getSelectionModel().getSelectedItem();
                VBox contentBox = (VBox) selectedItem.getContent();
                if (3 == contentBox.getChildren().size()) {
                    HBox bar = (HBox) contentBox.getChildren().get(1);
                    contentBox.getChildren().remove(1);
                    if (bar.getChildren().size() == 6) {
                        // 已经是搜索
                        break;
                    }
                }
                if (2 == contentBox.getChildren().size()) {
                    addSearchBar(selectedItem, false);
                }
            }

            break;
            case "menuSearchNext": {
                Tab selectedItem = tabs.getSelectionModel().getSelectedItem();
                VBox contentBox = (VBox) selectedItem.getContent();
                if (contentBox.getChildren().size() == 3) {
                    HBox bar = (HBox) contentBox.getChildren().get(1);
                    search(bar, contentBox, SearchType.search);
                } else {
                    ViewFactory.alert(primaryStage, "当前无可搜索内容！", "请先打开搜索，填入关键字再进行搜索！");
                }
            }
            break;
            case "menuReplace": {
                Tab selectedItem = tabs.getSelectionModel().getSelectedItem();
                VBox contentBox = (VBox) selectedItem.getContent();
                if (3 == contentBox.getChildren().size()) {
                    HBox bar = (HBox) contentBox.getChildren().get(1);
                    contentBox.getChildren().remove(1);
                    if (bar.getChildren().size() == 7) {
                        // 已经是替换
                        break;
                    }
                }
                if (2 == contentBox.getChildren().size()) {
                    addSearchBar(selectedItem, true);
                }
            }
            break;
            case "menuDrop":

                break;
            case "menuSelectAll":

                break;
            case "menuInsertDate": {
                Tab selectedItem = tabs.getSelectionModel().getSelectedItem();
                VBox contentBox = (VBox) selectedItem.getContent();
                TextArea content = (TextArea) contentBox.getChildren().get(0);
                content.replaceSelection(sdf.format(new Date()));
            }
            break;
            // 格式(O)
            case "autoLinefeed": {

                CheckMenuItem checkMenuItem = (CheckMenuItem) menuItem;

                for (Tab tab : tabs.getTabs()) {
                    VBox contentBox = (VBox) tab.getContent();
                    TextArea content = (TextArea) contentBox.getChildren().get(0);
                    content.setWrapText(checkMenuItem.isSelected());
                }
            }
            break;
            case "fontStyle": {
                DictionaryView dictionaryView = ViewFactory.showFontSetting(primaryStage);
                dictionaryView.getStage().setOnCloseRequest(e -> {
                    onLoad();
                    for (Tab tab : tabs.getTabs()) {
                        updateViewPattern(tab);
                    }
                });
            }
            break;

            case "menuClearTokenId":
                Preferences.getPreferences().remove("tokenId");
                break;
            case "menuLog":
                break;
            case "menuReset":
                try {
                    Preferences.getPreferences().clear();
                } catch (BackingStoreException e) {
                    e.printStackTrace();
                }
                break;
            case "menuCheckUpdate":
                break;
            case "menuHelp":
                showHelp();
                break;
            case "menuAbout":
                String about = "\t\t\t\t\t记事本\n\n"
                        + "\t\t\t\tNotepad software\n\n"
                        + "Copyright (c) 2018-20, Yanhong . All rights reserved.\n";
                ViewFactory.alert(primaryStage, "关于 Notepad:", about);
                break;

            default:
                break;
        }

    }

    private HBox addSearchBar(Tab tab, boolean isReplace) {
        HBox bar = new HBox();
        bar.setAlignment(Pos.CENTER_LEFT);
        bar.setStyle("-fx-border-color: gray;");
        bar.setPadding(new Insets(4, 8, 2, 8));
        ToggleButton Aa = new ToggleButton("Aa");
        Aa.setTextFill(Color.valueOf("#837373"));
        Aa.setStyle("-fx-border-insets: 10 10 10 10;");
        TextField searchText = new TextField();

        searchText.setPromptText("输入你要查找的内容");
        Button searchBtn = new Button("查找");
        Button searchLastBtn = new Button("查找上一个");
        Button searchAllBtn = new Button("查找全部");
        //-------
        TextField replaceText = new TextField();
        Button replaceBtn = new Button("替换");
        Button replaceAllBtn = new Button("替换全部");

        Button closeTabBtn = new Button("X");
        closeTabBtn.setStyle("-fx-background-radius: 8;");
        VBox contentBox = (VBox) tab.getContent();
        closeTabBtn.setFont(Font.font(9));
        Insets insets = new Insets(0, 6, 0, 0);
        HBox.setMargin(Aa, insets);
        HBox.setMargin(searchText, insets);
        HBox.setMargin(searchBtn, insets);

        if (isReplace) {
            searchText.setPrefWidth(243);
            replaceText.setPrefWidth(243);
            replaceText.setPromptText("输入你要替换的内容");
            HBox.setMargin(replaceBtn, insets);
            HBox.setMargin(replaceAllBtn, new Insets(0, 8, 0, 0));
            HBox.setMargin(replaceText, insets);
            bar.getChildren().addAll(Aa, searchText, replaceText, searchBtn, replaceBtn, replaceAllBtn, closeTabBtn);
        } else {
            searchText.setPrefWidth(451);
            HBox.setMargin(searchLastBtn, insets);
            HBox.setMargin(searchAllBtn, new Insets(0, 8, 0, 0));
            bar.getChildren().addAll(Aa, searchText, searchBtn, searchLastBtn, searchAllBtn, closeTabBtn);
        }
        EventHandler<ActionEvent> clickAction = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Object source = event.getSource();
                if (source == closeTabBtn) {
                    contentBox.getChildren().remove(bar);
                } else {
                    SearchType searchType = null;
                    if (source == searchBtn) {
                        searchType = SearchType.search;
                    } else if (source == searchLastBtn) {
                        searchType = SearchType.search_pre;
                    } else if (source == searchAllBtn) {
                        searchType = SearchType.search_all;
                    } else if (source == replaceBtn) {
                        searchType = SearchType.replace;
                    } else if (source == replaceAllBtn) {
                        searchType = SearchType.replace_all;
                    }
                    search(bar, contentBox, searchType);
                }
            }
        };
        searchBtn.setOnAction(clickAction);
        closeTabBtn.setOnAction(clickAction);
        if (isReplace) {
            replaceBtn.setOnAction(clickAction);
            replaceAllBtn.setOnAction(clickAction);
        } else {
            searchLastBtn.setOnAction(clickAction);
            searchAllBtn.setOnAction(clickAction);
        }
        searchText.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                search(bar, contentBox, SearchType.search);
            }
        });
        contentBox.getChildren().add(1, bar);
        // 修正窗体 高度
        updateSingleContentHeight(tab);
        searchText.requestFocus();
        return bar;
    }

    public enum SearchType {
        search, search_pre, search_all, replace_all, replace;
    }

    private boolean searchNext(TextArea content, String searchKey, String contentText) {
        int caretPosition = content.getSelection().getEnd();
//                        if (caretPosition == contentText.length()) caretPosition = 0;
        int index = contentText.indexOf(searchKey, caretPosition);
        if (index < caretPosition) {
            return false;
        } else {
//                                content.positionCaret(index + searchKey.length());
            content.selectRange(index + searchKey.length(), index);
            return true;
        }
    }

    private void search(HBox bar, VBox contentBox, SearchType searchType) {
        TextField searchText = (TextField) bar.getChildren().get(1);
        String searchKey = searchText.getText();
        if (searchKey != null && !searchKey.isEmpty()) {
            TextArea content = (TextArea) contentBox.getChildren().get(0);
            String contentText = content.getText();
            if (contentText != null && !contentText.isEmpty()) {
                switch (searchType) {
                    case search: {
                        if (!searchNext(content, searchKey, contentText)) {
                            ViewFactory.alert(primaryStage, "未找到匹配的关键字！", "请检查搜索的关键字是否正确！");
                        }
                    }
                    break;
                    case search_all: {
                        for (int i = 0; ; i++) {
                            if (!searchNext(content, searchKey, contentText)) {
                                if (i == 0) {
                                    ViewFactory.alert(primaryStage, "未找到匹配的关键字！", "请检查搜索的关键字是否正确！");
                                }
                                break;
                            }
                        }
                    }
                    break;
                    case search_pre: {
                        int caretPosition = content.getSelection().getStart();
//                        if (caretPosition == contentText.length()) caretPosition = 0;
                        int index = contentText.lastIndexOf(searchKey, caretPosition);
                        if (index < 0 || index > caretPosition) {
                            ViewFactory.alert(primaryStage, "未找到匹配的关键字！", "请检查搜索的关键字是否正确！");
                        } else {
//                                content.positionCaret(index + searchKey.length());
                            content.selectRange(index + searchKey.length(), index);
//                            content.sele
                        }
                    }
                    break;
                    case replace: {
                        if (content.getSelection().getLength() == 0 && !searchNext(content, searchKey, contentText)) {
                            ViewFactory.alert(primaryStage, "未找到匹配的关键字！", "请检查搜索的关键字是否正确！");
                            break;
                        }
                        TextField replaceText = (TextField) bar.getChildren().get(2);
                        String text = replaceText.getText();
                        if (text == null) text = "";
                        content.replaceSelection(text);
                    }

                    break;
                    case replace_all: {
                        for (int i = 0; ; i++) {
                            if (!searchNext(content, searchKey, contentText)) {
                                if (i == 0) {
                                    ViewFactory.alert(primaryStage, "未找到匹配的关键字！", "请检查搜索的关键字是否正确！");
                                }
                                break;
                            } else {
                                TextField replaceText = (TextField) bar.getChildren().get(2);
                                String text = replaceText.getText();
                                if (text == null) text = "";
                                content.replaceSelection(text);
                            }
                        }
                    }
                    break;
                    default:
                        return;
                }


            }
        }
    }

    public void saveOrReSave(String id, Tab currentTab, Label tabTitle) {
        if (id.equals("saveFile")) {
            File currentFile = tabMap.get(currentTab);
            if (currentFile != null) {
                saveContent(currentFile, currentTab);
                return;
            }
        }
        String text = tabTitle.getText();
        if (text.substring(0, 1).equals("*"))
            text = text.substring(1);

        File file = ViewFactory.saveFileChooser(primaryStage, text);
        if (file != null) {
            // file
            saveContent(file, currentTab);
            tabTitle.setText(file.getName());
            tabMap.put(currentTab, file);
        }
    }

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private void saveContent(File file, Tab currentTab) {
        VBox contentBox = (VBox) currentTab.getContent();
        TextArea content = (TextArea) contentBox.getChildren().get(0);
        try {
            FileUtil.saveOfStr(file, content.getText());
            HBox bottomBox = (HBox) contentBox.getChildren().get(contentBox.getChildren().size() - 1);

            Label lastSaveLabel = (Label) bottomBox.getChildren().get(1);
            Label saveStatusLabel = (Label) bottomBox.getChildren().get(2);
            lastSaveLabel.setText("上次保存：" + sdf.format(new Date()));
            saveStatusLabel.setText("保存成功：" + file.getAbsoluteFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onTest(Event event) {
//        String choosePath = ViewFactory.showDirectoryChooser(primaryStage);
//        System.out.println("you choose path:" + choosePath);


    }

    public void onCloseRequest() {

    }
}
