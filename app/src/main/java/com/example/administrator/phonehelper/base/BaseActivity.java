package com.example.administrator.phonehelper.base;

import android.app.Activity;
import android.os.Bundle;

import com.example.administrator.phonehelper.R;
import com.example.administrator.phonehelper.dao.DataBaseOpenHelper;

public class BaseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
//        第一次使用数据库文件的时候把该文件夹拷贝到手机的/data/data/应用程序报名/database文件夹下，之后就直接从这个地方使用了。
        DataBaseOpenHelper.initManager(getApplication());
    }
}
