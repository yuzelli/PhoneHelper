package com.example.administrator.phonehelper.activitys;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.phonehelper.R;
import com.example.administrator.phonehelper.base.BaseActivity;
import com.example.administrator.phonehelper.widgets.CirleRocket;


public class MainActivity extends BaseActivity implements View.OnClickListener {
    private ImageButton btn_head_setting; //标题右侧图标
    private ImageButton btn_head_left;//标题左侧图标
    private LinearLayout ll_rocket;//手机加载item
    private LinearLayout ll_softmgr;//软件管理item
    private LinearLayout ll_phonemgr;//手机检测item
    private LinearLayout ll_telmgr;//通信大全item
    private LinearLayout ll_filemgr;//文件管理item
    private LinearLayout ll_sdclean;//垃圾清理item

    private TextView tv_label_head_title;//标题文字

    private long exitTime = 0;//记得点击返回键的时间

    private CirleRocket circle_rocket;//自定义View
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }
    private void initView() {
        btn_head_setting = (ImageButton) this.findViewById(R.id.btn_head_setting);
        btn_head_left = (ImageButton) this.findViewById(R.id.btn_head_left);
        ll_rocket = (LinearLayout) this.findViewById(R.id.ll_rocket);
        ll_softmgr = (LinearLayout) this.findViewById(R.id.ll_softmgr);
        ll_phonemgr = (LinearLayout) this.findViewById(R.id.ll_phonemgr);
        ll_telmgr = (LinearLayout) this.findViewById(R.id.ll_telmgr);
        ll_filemgr = (LinearLayout) this.findViewById(R.id.ll_filemgr);
        ll_sdclean = (LinearLayout) this.findViewById(R.id.ll_sdclean);
        circle_rocket = (CirleRocket) this.findViewById(R.id.circle_rocket);
        context = this;

        tv_label_head_title = (TextView) this.findViewById(R.id.tv_label_head_title);
        tv_label_head_title.setText("主页面");

        btn_head_setting.setOnClickListener(this);
        btn_head_left.setOnClickListener(this);
        ll_rocket.setOnClickListener(this);
        ll_softmgr.setOnClickListener(this);
        ll_phonemgr.setOnClickListener(this);
        ll_telmgr.setOnClickListener(this);
        ll_filemgr.setOnClickListener(this);
        ll_sdclean.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.btn_head_setting:
                intent.setClass(MainActivity.this, SettingActivity.class);
                break;
            case R.id.btn_head_left:

                break;
            case R.id.ll_rocket:
                intent.setClass(MainActivity.this, RocketActivity.class);
                break;
            case R.id.ll_softmgr:
                intent.setClass(MainActivity.this, SoftManagerActivity.class);
                break;
            case R.id.ll_phonemgr:
                intent.setClass(MainActivity.this, PhoneManagerActivity.class);
                break;
            case R.id.ll_telmgr:
                intent.setClass(MainActivity.this, TellManagerActivity.class);
                break;
            case R.id.ll_filemgr:
                intent.setClass(MainActivity.this, FileManagerActivity.class);
                break;
            case R.id.ll_sdclean:
                intent.setClass(MainActivity.this, RubbishActivity.class);
                break;
            default:
                break;
        }
        startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if((System.currentTimeMillis()-exitTime) > 2000){
                Toast.makeText(context,"再按一次退出程序",Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
