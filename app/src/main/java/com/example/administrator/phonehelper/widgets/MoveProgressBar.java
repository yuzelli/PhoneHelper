package com.example.administrator.phonehelper.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ProgressBar;

/**
 * Created by Administrator on 2016/12/5.
 */

public class MoveProgressBar extends ProgressBar {
    public MoveProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    int i = 0;
    public void setProgressContent(final int index){
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (i = 0 ;i<=index;i++)
                {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    setProgress(i);
                }
            }
        }).start();
    }
}
