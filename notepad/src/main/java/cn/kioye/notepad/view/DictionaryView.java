package cn.kioye.notepad.view;

import cn.kioye.notepad.utils.Preferences;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class DictionaryView {
    private Stage thisStage;

    public void setStage(Stage thisStage) {
        this.thisStage = thisStage;
    }


    @FXML
    private ColorPicker fontColorPicker;
    @FXML
    private ColorPicker fontBgColorPicker;
    @FXML
    private TextField fontSizeText;
    @FXML
    private TextField fontOpacityText;
    @FXML
    private Slider fontSizeSlider;
    @FXML
    private Slider fontOpacitySlider;
    @FXML
    private Text fontPattern;
    @FXML
    private Pane fontPane;


    /**
     * 初始化一些组件
     */
    @FXML
    private void initialize() {
        System.out.println("初始化窗口 DictionaryView ");

        // 初始化
        fontColorPicker.valueProperty().addListener((ObservableValue<? extends Color> observable, Color oldValue, Color newValue) -> {
            fontPattern.setFill(fontColorPicker.getValue());
        });
        fontBgColorPicker.valueProperty().addListener((ObservableValue<? extends Color> observable, Color oldValue, Color newValue) -> {
            fontPane.setStyle("-fx-background-color:" + newValue.toString().replace("0x", "#") + "; -fx-border-color: #456456;");
        });
        fontSizeSlider.valueProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            fontSizeText.setText(newValue.toString());
            fontPattern.setFont(Font.font(newValue.doubleValue()));
        });
        fontOpacitySlider.valueProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            fontOpacityText.setText(newValue.toString());
            fontPattern.setOpacity(newValue.doubleValue());
        });
        onLoad();
        System.out.println("初始化窗口 DictionaryView 完成");
    }

    private void onLoad() {
        java.util.prefs.Preferences preferences = Preferences.getPreferences();
        fontColorPicker.setValue(Color.valueOf(preferences.get("fontColor", "#000000")));
        fontBgColorPicker.setValue(Color.valueOf(preferences.get("fontBgColor", "#ffffff")));
        fontSizeSlider.setValue(preferences.getDouble("fontSize", 12));
        fontOpacitySlider.setValue(preferences.getDouble("fontOpacity", 1));
    }

    private void onSave() {
        java.util.prefs.Preferences preferences = Preferences.getPreferences();
        preferences.put("fontColor", fontColorPicker.getValue().toString().replace("0x", "#"));
        preferences.put("fontBgColor", fontBgColorPicker.getValue().toString().replace("0x", "#"));
        preferences.putDouble("fontSize", fontSizeSlider.getValue());
        preferences.putDouble("fontOpacity", fontOpacitySlider.getValue());
    }

    @FXML
    private void onSave(Event event) {
        Object source = event.getSource();
        if (source instanceof Button) {
            onSave();
            ViewFactory.alert(thisStage, "操作提示", "操作成功！").setOnCloseRequest(e -> {
                onCancel(event);
            });
        }
    }

    @FXML
    private void onCancel(Event event) {
        Event.fireEvent(thisStage, new WindowEvent(thisStage, WindowEvent.WINDOW_CLOSE_REQUEST));
    }

    public Stage getStage() {
        return thisStage;
    }
}
