package com.example.administrator.phonehelper.widgets;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2016/12/5.
 */

public class MoveTextView extends TextView {
    private Handler handler = new Handler();
    int i;
    public MoveTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setTitleContent(final int index) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for ( i = 0; i < index; i++) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (index > i) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                setText(i+"");
                            }
                        });
                    }
                }
            }
        }).start();
    }

    public void setTitleContentByTimer(final int index){
         final Timer timer = new Timer();
        final TimerTask timerTask = new TimerTask()
        {
            @Override
            public void run() {
                for ( i = 0; i < 100; i++) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (i<=index) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                setText(i+"");

                            }
                        });
                    }else {
                       timer.cancel();
                    }
                }
            }
        };
        timer.schedule(timerTask,10,10);

    }
}
