package com.example.administrator.phonehelper.beans;

/**
 * Created by Administrator on 2016/11/29.
 */
public class PhoneManagerInfo {
    private int imgViewID;
    private String firstName;
    private String firstContent;

    private String nextName;
    private String nextContent;

    public PhoneManagerInfo(int imgViewID, String firstName, String firstContent, String nextName, String nextContent) {
        this.imgViewID = imgViewID;
        this.firstName = firstName;
        this.firstContent = firstContent;
        this.nextName = nextName;
        this.nextContent = nextContent;
    }

    public int getImgViewID() {
        return imgViewID;
    }

    public void setImgViewID(int imgViewID) {
        this.imgViewID = imgViewID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstContent() {
        return firstContent;
    }

    public void setFirstContent(String firstContent) {
        this.firstContent = firstContent;
    }

    public String getNextName() {
        return nextName;
    }

    public void setNextName(String nextName) {
        this.nextName = nextName;
    }

    public String getNextContent() {
        return nextContent;
    }

    public void setNextContent(String nextContent) {
        this.nextContent = nextContent;
    }
}
