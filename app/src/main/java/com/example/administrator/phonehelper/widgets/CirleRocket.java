package com.example.administrator.phonehelper.widgets;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.example.administrator.phonehelper.R;
import com.example.administrator.phonehelper.beans.ProgressInfo;
import com.example.administrator.phonehelper.utils.CleanUtil;
import com.example.administrator.phonehelper.utils.GetPhoneSystemInfo;
import com.example.administrator.phonehelper.utils.MyAppManager;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2016/12/12.
 */

public class CirleRocket extends View {
    private Context context;
    private int radioArc;
    private int newSize;

    private List<ProgressInfo> progressInfos;
    private static final int GETPROGRESS = 0;
    private ActivityManager mActivityManager;
    private boolean isOpenTouch = true;
    private GetRunProgress thread;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GETPROGRESS:
                    CleanUtil cleanUtil = new CleanUtil();
                    double allSize = cleanUtil.getAllMemory();
                    double useSize = cleanUtil.getUesdMemory(context);
                    newSize = (int) (useSize / allSize * 100);
                    break;
            }
        }
    };

    public CirleRocket(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        CleanUtil cleanUtil = new CleanUtil();
        double allSize = cleanUtil.getAllMemory();
        double useSize = cleanUtil.getUesdMemory(context);
        radioArc = (int) (useSize / allSize * 100);
        mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int canvasWidth = canvas.getWidth();
        int canvasHeight = canvas.getHeight();


//        最外测圆边
        Paint paint1 = new Paint();
        paint1.setAntiAlias(true);
        paint1.setDither(true);
        paint1.setColor(ContextCompat.getColor(context, R.color.lightgrey));
        canvas.drawCircle(canvasWidth / 2, canvasHeight / 2, canvasWidth * 3 / 8, paint1);

        //        显示进度
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setColor(ContextCompat.getColor(context, R.color.greenyellow));
        RectF rectF = new RectF(canvasWidth / 2 - canvasWidth * 3 / 8 - 5, canvasHeight / 2 - canvasWidth * 3 / 8 - 5, canvasWidth / 2 + canvasWidth * 3 / 8 + 5, canvasHeight / 2 + canvasWidth * 3 / 8 + 5);

        canvas.drawArc(rectF, 270, radioArc * 360 / 100, true, paint);


//        渐变圆环
        Paint paint2 = new Paint();
        paint2.setAntiAlias(true);
        paint2.setDither(true);
        LinearGradient linearGradient = new LinearGradient(canvasWidth / 2, canvasHeight / 2 - canvasWidth * 3 / 8, canvasWidth / 2, canvasHeight / 2 + canvasWidth * 3 / 8, new int[]{Color.WHITE, ContextCompat.getColor(context, R.color.darkgray)}, null,
                Shader.TileMode.REPEAT
        );
        paint2.setShader(linearGradient);
//        paint2.setColor(ContextCompat.getColor(context, R.color.deepskyblue));
        canvas.drawCircle(canvasWidth / 2, canvasHeight / 2, canvasWidth * 3 / 8 - 5, paint2);

//        中心大园
        Paint paint3 = new Paint();
        paint3.setAntiAlias(true);
        paint3.setDither(true);
        paint3.setColor(ContextCompat.getColor(context, R.color.lightgrey));
        canvas.drawCircle(canvasWidth / 2, canvasHeight / 2, canvasWidth * 3 / 8 - 15, paint3);

//        文字
        Paint paint4 = new Paint();
        paint4.setAntiAlias(true);
        paint4.setDither(true);
        paint4.setTextSize(40);
        paint4.setColor(ContextCompat.getColor(context, R.color.black));
        canvas.drawText(radioArc + "%", canvasWidth / 2 - 30, canvasHeight / 2 + 20, paint4);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                if (isOpenTouch) {
                    thread = new GetRunProgress();
                    thread.start();
                    doRocketAction();
                    isOpenTouch = false;
                }

                break;
        }
        return true;
    }

    public void doRocketAction() {
        final int size = radioArc;
        final Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {

                for (int i = size; i >= 0; i--) {
                    radioArc = i;
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    postInvalidate();
                }

                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                timer.cancel();
                showNewProgress();

            }
        };
        timer.schedule(task, 30, 30);
    }


    private class GetRunProgress extends Thread {

        @Override
        public void run() {
            progressInfos = MyAppManager.getMyAppManager(context).getAllAppInfo();
            for (ProgressInfo info : progressInfos) {
                mActivityManager.killBackgroundProcesses(info.getPackageName());
            }
            super.run();
            //扫描完毕发送消息
            Message msg = new Message();
            msg.what = GETPROGRESS;
            handler.sendMessage(msg);
        }
    }

    private void showNewProgress() {
        new Thread(new Runnable() {
            @Override
            public void run() {
//                try {
//                    thread.join();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                for (int i = 0; i <= newSize; i++) {

                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    radioArc = i;
                    postInvalidate();
                }
                isOpenTouch = true;
            }
        }).start();
    }


}
