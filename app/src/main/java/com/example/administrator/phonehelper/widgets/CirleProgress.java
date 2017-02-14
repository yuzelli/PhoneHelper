package com.example.administrator.phonehelper.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by Administrator on 2016/12/9.
 */

public class CirleProgress extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder holder;
    private RenderThread renderThread;
    private static boolean  isDraw = false;// 控制绘制的开关
    private static int angle=0;
    private int memory;


    public void setMemory(int memory){
        this.memory = memory;
    }

    public CirleProgress(Context context) {
        super(context);
        initView();
    }


    public CirleProgress(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }


    public CirleProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }


    private void initView() {
        holder = this.getHolder();
        // 给SurfaceView当前的持有者一个回调对象。
        holder.addCallback(this);
        renderThread = new RenderThread(holder);

    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        isDraw = true;
        if (!renderThread.isAlive()){
            renderThread.start();
        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        isDraw = false;
        renderThread.isOpean = false;
        renderThread.interrupt();
       // renderThread.stop();
        renderThread = null;

    }


    class RenderThread extends Thread {
        private SurfaceHolder holder;
        public boolean isOpean = true;

        public RenderThread(SurfaceHolder holder) {
            this.holder = holder;
        }

        @Override
        public void run() {
            while (isDraw) {
                drawUI();
                if(angle>memory){
                    isDraw = false;
                }
                angle++;
            }
            super.run();
        }

        private void drawUI() {
            Canvas canvas = holder.lockCanvas();
            try {
                drawCanvas(canvas);

            } catch (Exception e) {

            } finally {
                if (canvas != null) {
                    holder.unlockCanvasAndPost(canvas);
                }
            }

        }

        private void drawCanvas(Canvas canvas) {
            int height = canvas.getHeight();
            int weight = canvas.getWidth();
           // canvas.drawColor(Color.WHITE);
            Paint pArc2 = new Paint();
            pArc2.setAntiAlias(true);
            pArc2.setDither(true);
            pArc2.setColor(Color.BLUE);
            RectF rect2 = new RectF(weight/2-150-100,height/2-150,300+weight/2-150-100,height/2+150);
//          画背景
            canvas.drawArc(rect2,-90,angle+360-memory,true,pArc2);
            Paint pArc = new Paint();
            pArc.setAntiAlias(true);
            pArc.setDither(true);
            pArc.setColor(Color.YELLOW);
            RectF rect = new RectF(weight/2-150-100,height/2-150,300+weight/2-150-100,height/2+150);
//            画圆弧实现呢进度
            canvas.drawArc(rect,-90,angle,true,pArc);
//            画方块
            Rect roundRect = new Rect(weight-weight/4,height/2-20,weight-weight/4+40,height/2+20);
            canvas.drawRect(roundRect,pArc2);
//            画方块
            Rect roundRect2 = new Rect(weight-weight/4,height/2-20+40,weight-weight/4+40,height/2+20+40);
            canvas.drawRect(roundRect2,pArc);
            Paint paintText = new Paint();
            paintText.setColor(Color.YELLOW);
            paintText.setTextSize(26);
            canvas.drawText("剩余内存",weight-weight/4+50,height/2+15,paintText);
            canvas.drawText("已用内存",weight-weight/4+50,height/2+55,paintText);
        }
    }

}
