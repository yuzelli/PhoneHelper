package com.example.administrator.phonehelper.beans;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/12/12.
 */

public class FileListInfo implements Serializable {

    private String fileName;
    private String creatFileDate;
    private Drawable fileImg;
    private long fileSize;
    private boolean isCheck;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getCreatFileDate() {
        return creatFileDate;
    }

    public void setCreatFileDate(String creatFileDate) {
        this.creatFileDate = creatFileDate;
    }

    public Drawable getFileImg() {
        return fileImg;
    }

    public void setFileImg(Drawable fileImg) {
        this.fileImg = fileImg;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }
}
