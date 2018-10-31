package cn.kioye.notepad.utils;

import cn.kioye.notepad.view.LoadingView;
import cn.kioye.notepad.view.ViewFactory;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;

/**
 * 异步网络请求，封装加载页面
 */
public class TaskWithLoading extends FileUtil {
    public interface MyTaskMethod{
        public void finishCallBack();
        public void execute();
    }
    private MyTaskMethod myTaskMethod;
    public static LoadingView showLoading = null;

    public TaskWithLoading(Stage stage, final MyTaskMethod myTaskMethod) {
        this.myTaskMethod=myTaskMethod;
        showLoading= ViewFactory.initLoading(stage);
        super.setOnRunning(new EventHandler<WorkerStateEvent>() {

            public void handle(WorkerStateEvent event) {
                showLoading.getStage().show();
            }
        });
        super.setOnSucceeded(new EventHandler<WorkerStateEvent>() {

            public void handle(WorkerStateEvent event) {
                myTaskMethod.finishCallBack();
                showLoading.cancelLoading();
            }
        });
    }
    public TaskWithLoading start() {
        showLoading.start(this);
        return this;
    }
    @Override
    protected Integer call() throws Exception {
        myTaskMethod.execute();
        return 1;
    }
}
