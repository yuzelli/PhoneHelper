package com.example.administrator.phonehelper.activitys;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import com.example.administrator.phonehelper.R;
import com.example.administrator.phonehelper.base.BaseActivity;
import com.example.administrator.phonehelper.beans.PhoneManagerInfo;
import com.example.administrator.phonehelper.receiver.BatteryReceiver;
import com.example.administrator.phonehelper.utils.CommonAdapter;
import com.example.administrator.phonehelper.utils.GetPhoneSystemInfo;
import com.example.administrator.phonehelper.utils.ViewHolder;
import com.example.administrator.phonehelper.widgets.MoveProgressBar;
import com.example.administrator.phonehelper.widgets.MoveTextView;
import com.example.administrator.phonehelper.widgets.TopBar;

import java.util.ArrayList;
import java.util.List;

/**
 * 手机检测
 */
public class PhoneManagerActivity extends BaseActivity {
    private TopBar top_header;
    private MoveTextView tv_phoneElectric;
    private MoveProgressBar pb_moveProgress;
    private ListView lv_phoneMessage;
    private CommonAdapter<PhoneManagerInfo> adapter;
    private BatteryReceiver receiver;

    private int percent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_manager);


        initView();

    }

    private void initView() {
        top_header = (TopBar) this.findViewById(R.id.top_header);
        top_header.setOnTopBarClickListener(new TopBar.topbarClickListener() {
            @Override
            public void leftClick() {
                finish();
            }

            @Override
            public void rightClick() {

            }
        });
        tv_phoneElectric = (MoveTextView) this.findViewById(R.id.tv_phoneElectric);

        pb_moveProgress = (MoveProgressBar) this.findViewById(R.id.pb_moveProgress);

        lv_phoneMessage = (ListView) this.findViewById(R.id.lv_phoneMessage);

        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        receiver = new BatteryReceiver(tv_phoneElectric,pb_moveProgress);
        registerReceiver(receiver, filter);
        List<PhoneManagerInfo> list = new ArrayList<>();
        GetPhoneSystemInfo phoneSystemInfo = new GetPhoneSystemInfo(this);
        list = phoneSystemInfo.getDeviceInfo();
        adapter = new CommonAdapter<PhoneManagerInfo>(this,list,R.layout.item_phonemanager_message) {
            @Override
            public void convert(ViewHolder helper, PhoneManagerInfo item) {
                ImageView img = helper.getView(R.id.img_phoneInfo);
                img.setImageResource(item.getImgViewID());
                helper.setText(R.id.tv_firstName,item.getFirstName());
                helper.setText(R.id.tv_firstContent,item.getFirstContent());
                helper.setText(R.id.tv_nextName,item.getNextName());
                helper.setText(R.id.tv_nextContent,item.getNextContent());

            }
        };
        lv_phoneMessage.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
            super.onDestroy();
        //销毁广播
        unregisterReceiver(receiver);
    }
}
