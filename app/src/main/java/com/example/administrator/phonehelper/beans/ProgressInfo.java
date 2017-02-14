package com.example.administrator.phonehelper.beans;

import android.graphics.drawable.Drawable;
import android.util.StringBuilderPrinter;

/**
 * Created by Administrator on 2016/11/30.
 */
public class ProgressInfo {
    private Drawable appIcon;
    private String appName;

    private String progressName;
    private int progressMemory;
    private String packageName;

    private int ViersionCode;
    private String ViersionName;


    private boolean check;

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public Drawable getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getProgressName() {
        return progressName;
    }

    public void setProgressName(String progressName) {
        this.progressName = progressName;
    }

    public int getProgressMemory() {
        return progressMemory;
    }

    public void setProgressMemory(int progressMemory) {
        this.progressMemory = progressMemory;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public int getViersionCode() {
        return ViersionCode;
    }

    public void setViersionCode(int viersionCode) {
        ViersionCode = viersionCode;
    }

    public String getViersionName() {
        return ViersionName;
    }

    public void setViersionName(String viersionName) {
        ViersionName = viersionName;
    }
}
