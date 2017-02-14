package com.example.administrator.phonehelper.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.phonehelper.R;

/**
 * Created by Administrator on 2016/11/23.
 */
public class TopBar extends RelativeLayout {
    private Button btn_right;
    private ImageView btn_left;
    private TextView tv_title;

    private int leftTextColor;
    private Drawable leftBackground;
    private String leftText;

    private int rightTextColor;
    private Drawable rightBackground;
    private String rightText;

    private float titleTextSize;
    private int titleTextColor;
    private String toptitle;

    //定义三个布局参数
    private LayoutParams leftParams, rightParams, titleParams;

    //定义一个事件接口
    public interface topbarClickListener {
        public void leftClick();

        public void rightClick();
    }

    //创建接口对象
    public topbarClickListener listener;

    //创建为事件接口赋值的方法
    public void setOnTopBarClickListener(topbarClickListener listener) {
        this.listener = listener;
    }

    //构造方法，初始化成员
    public TopBar(Context context, AttributeSet attrs) {
        super(context, attrs);

//将XML中定义的自定义属性映射到attrs中。
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.Topbar);

//从ta结构中获取数据，类似一种key,value结构，通过R.styleable.Topbar_属性名获取
        leftTextColor = ta.getColor(R.styleable.Topbar_leftTextColor, 0);
        leftBackground = ta.getDrawable(R.styleable.Topbar_leftBackground);
        leftText = ta.getString(R.styleable.Topbar_leftText);

        rightTextColor = ta.getColor(R.styleable.Topbar_rightTextColor, 0);
        rightBackground = ta.getDrawable(R.styleable.Topbar_rightBackground);
        rightText = ta.getString(R.styleable.Topbar_rightText);

        titleTextSize = ta.getDimension(R.styleable.Topbar_titleTextSize, 0);
        titleTextColor = ta.getColor(R.styleable.Topbar_titleTextColor, 0);
        toptitle = ta.getString(R.styleable.Topbar_toptitle);

//进行垃圾回收
        ta.recycle();

//初始化控件
        btn_left = new ImageView(context);
        btn_right = new Button(context);
        tv_title = new TextView(context);

//设置控件的值
        //btn_left.setTextColor(leftTextColor); //设置文字颜色
        btn_left.setBackground(leftBackground); //设置背景
        btn_left.setScaleType(ImageView.ScaleType.CENTER);

        // btn_left.setText(leftText); //设置文本

        btn_right.setTextColor(rightTextColor); //设置文字颜色
        btn_right.setBackground(rightBackground); //设置背景
        btn_right.setText(rightText); //设置文本

        tv_title.setTextColor(titleTextColor); //设置字体颜色
        tv_title.setTextSize(titleTextSize); //设置字体大小
        tv_title.setText(toptitle); //设置文本
        tv_title.setGravity(Gravity.CENTER); //居中显示

        setBackgroundColor(ContextCompat.getColor(context, R.color.burlywood)); //设置View的背景颜色

//设置布局属性的width和height
        leftParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//设置对齐方式为父容器的左侧
        leftParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, TRUE);
        leftParams.addRule(RelativeLayout.CENTER_VERTICAL, TRUE);

//将左边按钮添加到视图中，并设置布局属性
        addView(btn_left, leftParams);

//设置布局属性的width和height
        rightParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//设置对齐方式为父容器的右侧
        rightParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, TRUE);
//将右边按钮添加到视图中，并设置布局属性
        addView(btn_right, rightParams);

//设置布局属性的width和height
        titleParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
//设置对齐方式为居中对齐
        titleParams.addRule(RelativeLayout.CENTER_IN_PARENT, TRUE);
//将中间TextView添加到视图中，并设置布局属性
        addView(tv_title, titleParams);

//添加左侧按钮的Click事件
        btn_left.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.leftClick();
            }
        });

//添加右侧按钮的Click事件
        btn_right.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.rightClick();
            }
        });
    }

    /**
     * 设置左边按钮是否隐藏，true隐藏， false消失
     *
     * @param flag
     */
    public void setLeftButtonIsVisiable(boolean flag) {
        if (flag) {
            btn_left.setVisibility(View.VISIBLE);
        } else {
            btn_left.setVisibility(View.GONE);
        }
    }

    /**
     * 设置右边按钮是否隐藏，true隐藏， false消失
     *
     * @param flag
     */
    public void setRightButtonIsVisiable(boolean flag) {
        if (flag) {
            btn_right.setVisibility(View.VISIBLE);
        } else {
            btn_right.setVisibility(View.GONE);
        }
    }

    public void setTitleName(String titleName) {
        tv_title.setText(titleName);
    }
}
