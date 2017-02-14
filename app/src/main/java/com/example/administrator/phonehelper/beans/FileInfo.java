package com.example.administrator.phonehelper.beans;

import java.io.File;
import java.io.Serializable;

/**
 * Created by Administrator on 2016/12/11.
 */

public class FileInfo implements Serializable{
    private File file;
    private String fileType;
    private String iconName;
    private String openType;
    private boolean isSelect;

    public FileInfo(File file, String iconName,String fileType, String openType) {
        this.file = file;
        this.fileType = fileType;
        this.iconName = iconName;
        this.openType = openType;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getIconName() {
        return iconName;
    }

    public void setIconName(String iconName) {
        this.iconName = iconName;
    }


    public String getOpenType() {
        return openType;
    }

    public void setOpenType(String openType) {
        this.openType = openType;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
