package com.example.administrator.phonehelper.activitys;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.phonehelper.R;
import com.example.administrator.phonehelper.adapter.GuideViewAdapter;
import com.example.administrator.phonehelper.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 引导页
 */
public class GuideActivity extends BaseActivity {
    private ViewPager vp_viewPage;
    // 图片
    private int[] imageView = { R.drawable.guide1, R.drawable.guide2,
            R.drawable.guide3, R.drawable.guide4};
    private List<View> list;
    // 底部小点的图片
    private LinearLayout ll_Point;
    //立即进入按钮
    private TextView tv_immediatelyEnter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        initview();
        initoper();
        addView();
        addPoint();
    }
    private void initview() {
        vp_viewPage = (ViewPager) findViewById(R.id.vp_viewPage);
        ll_Point = (LinearLayout) findViewById(R.id.ll_Point);
        tv_immediatelyEnter = (TextView) findViewById(R.id.tv_immediatelyEnter);
    }

    private void initoper() {
        // 进入按钮
        tv_immediatelyEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                SharedPreferences.Editor editor = getSharedPreferences("phoneHelperShared",MODE_PRIVATE).edit();
                editor.putBoolean("firstUse", false);
                editor.commit();
                Intent intent = new Intent(GuideActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        /**
         * 添加图片到view
         */

        // 2.监听当前显示的页面，将对应的小圆点设置为选中状态，其它设置为未选中状态
        vp_viewPage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                monitorPoint(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
               // UIUtils.log("arg0--" + arg0);
            }
        });

    }
    private void addView() {
        list = new ArrayList<View>();
        // 将imageview添加到view
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        for (int i = 0; i < imageView.length; i++) {
            ImageView iv = new ImageView(this);
            iv.setLayoutParams(params);
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            iv.setImageResource(imageView[i]);
            list.add(iv);
        }
        // 加入适配器
        vp_viewPage.setAdapter(new GuideViewAdapter(list));

    }
    /**
     * 添加小圆点
     */
    private void addPoint() {
        // 1.根据图片多少，添加多少小圆点
        for (int i = 0; i < imageView.length; i++) {
            LinearLayout.LayoutParams pointParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            if (i < 1) {
                pointParams.setMargins(0, 0, 0, 0);
            } else {
                pointParams.setMargins(10, 0, 0, 0);
            }
            ImageView iv = new ImageView(this);
            iv.setLayoutParams(pointParams);
            iv.setBackgroundResource(R.drawable.point_normal);
            ll_Point.addView(iv);
        }
        ll_Point.getChildAt(0).setBackgroundResource(R.drawable.point_select);

    }

    /**
     * 判断小圆点
     *
     * @param position
     */
    private void monitorPoint(int position) {
        for (int i = 0; i < imageView.length; i++) {
            if (i == position) {
                ll_Point.getChildAt(position).setBackgroundResource(
                        R.drawable.point_select);
            } else {
                ll_Point.getChildAt(i).setBackgroundResource(
                        R.drawable.point_normal);
            }
        }
        // 3.当滑动到最后一个添加按钮点击进入，
        if (position == imageView.length - 1) {
            tv_immediatelyEnter.setVisibility(View.VISIBLE);
        } else {
            tv_immediatelyEnter.setVisibility(View.GONE);
        }
    }

}
