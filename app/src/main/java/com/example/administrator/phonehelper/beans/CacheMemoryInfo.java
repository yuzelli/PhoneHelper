package com.example.administrator.phonehelper.beans;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/12/1.
 */
public class CacheMemoryInfo implements Serializable {
    private int id;
    private boolean cbisCheck;
    private String name;
    private String path;
    private String apkName;
    private long size;
    private Drawable appIcon;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isCbisCheck() {
        return cbisCheck;
    }

    public void setCbisCheck(boolean cbisCheck) {
        this.cbisCheck = cbisCheck;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getApkName() {
        return apkName;
    }

    public void setApkName(String apkName) {
        this.apkName = apkName;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public Drawable getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }
}
