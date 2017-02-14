package com.example.administrator.phonehelper.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2016/12/6.
 */

public class RocketProgress extends ProgressBar {
    public RocketProgress(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void autoNum(final int num){
        final Timer timer =new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (getProgress()<=num){
                    setProgress(getProgress()+1);
                }else {
                    timer.cancel();
                }
            }
        };
        timer.schedule(task,30,30);
    }
}
