package com.example.administrator.phonehelper.beans;

/**
 * Created by Administrator on 2016/11/29.
 */
public class TellInfo {
    private String name;
    private String number;

    public TellInfo(String name, String number) {
        this.name = name;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
