package com.example.youcef.bijoux;

/**
 * Created by YouCef on 05/05/15.
 */
public interface TaskListener {
    public void onTaskStarted();
    public void onTaskFinished();
    public void onTaskSucceed();
    public void onTaskFailed();
}
