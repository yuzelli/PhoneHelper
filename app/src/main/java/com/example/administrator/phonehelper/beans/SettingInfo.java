package com.example.administrator.phonehelper.beans;

/**
 * Created by Administrator on 2016/11/30.
 */
public class SettingInfo {
    private String name;
    private boolean showToggelButton;

    public SettingInfo(String name, boolean showToggelButton) {
        this.name = name;
        this.showToggelButton = showToggelButton;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isShowToggelButton() {
        return showToggelButton;
    }

    public void setShowToggelButton(boolean showToggelButton) {
        this.showToggelButton = showToggelButton;
    }
}
