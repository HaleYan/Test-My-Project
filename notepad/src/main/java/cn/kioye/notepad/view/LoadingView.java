package cn.kioye.notepad.view;

import cn.kioye.notepad.utils.TaskWithLoading;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressIndicator;
import javafx.stage.Stage;

public class LoadingView {
    public interface WindowsStatus{
        public void Change(int status);
    }
    private Stage loadingStage;
    @FXML
    private ProgressIndicator loadingIndicator;
    private WindowsStatus windowsStatus;
    public void setWindowsStatus(WindowsStatus windowsStatus) {
        this.windowsStatus = windowsStatus;
    }
    private Task<?> task;
    @FXML
    public void cancelLoading() {
        loadingStage.close();
        upStatus(-1);
        if (task.isRunning()) {
            task.cancel();
        }
    }
    private void upStatus(int status) {
        if (windowsStatus!=null) {
            windowsStatus.Change(status);
        }
    }
    public void setStage(Stage loadingStage) {
        this.loadingStage=loadingStage;
    }
    public Stage getStage() {
        return loadingStage;
    }

    public void reset() {
        loadingIndicator.setProgress(-1F);
        windowsStatus=null;
    }
    public void start(final TaskWithLoading task) {
        this.task=task;
        new Thread(task).start();
    }

}
