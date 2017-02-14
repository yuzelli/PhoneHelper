package com.example.administrator.phonehelper.beans;

import android.graphics.drawable.Drawable;

/**
 * Created by Administrator on 2016/11/30.
 */
public class AppInfo {
    private boolean isDelete;
    private String appName;
    private String appPath;
    private Drawable appICON;
    private String appVersion;


    public boolean isDelete() {
        return isDelete;
    }

    public void setIsDelete(boolean isDelete) {
        this.isDelete = isDelete;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppPath() {
        return appPath;
    }

    public void setAppPath(String appPath) {
        this.appPath = appPath;
    }

    public Drawable getAppICON() {
        return appICON;
    }

    public void setAppICON(Drawable appICON) {
        this.appICON = appICON;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }
}
