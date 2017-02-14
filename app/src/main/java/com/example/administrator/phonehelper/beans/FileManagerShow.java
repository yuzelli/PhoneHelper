package com.example.administrator.phonehelper.beans;

/**
 * Created by Administrator on 2016/12/12.
 */

public class FileManagerShow  {
    private String fileName;
    private long fileSize;
    private boolean isFinish;

    public boolean isFinish() {
        return isFinish;
    }

    public void setFinish(boolean finish) {
        isFinish = finish;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }
}
