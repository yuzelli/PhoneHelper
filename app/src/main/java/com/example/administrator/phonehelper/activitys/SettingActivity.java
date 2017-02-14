package com.example.administrator.phonehelper.activitys;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.administrator.phonehelper.R;
import com.example.administrator.phonehelper.base.BaseActivity;
import com.example.administrator.phonehelper.beans.SettingInfo;
import com.example.administrator.phonehelper.utils.CommonAdapter;
import com.example.administrator.phonehelper.utils.ViewHolder;

import java.util.ArrayList;
import java.util.List;

public class SettingActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private ListView lv_setting;
    private CommonAdapter<SettingInfo> adapter;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initView();
    }

    private void initView() {
        lv_setting = (ListView) this.findViewById(R.id.lv_setting);
        context = this;
        initListView();
    }

    private void initListView() {
        adapter = new CommonAdapter<SettingInfo>(this, getSettingData(), R.layout.item_setting_message) {
            @Override
            public void convert(ViewHolder helper, final SettingInfo item) {
                helper.setText(R.id.tv_contentSelect, item.getName());
                final ToggleButton toggleButton = helper.getView(R.id.togglebtn_select);
                ImageView img_arrows_right = helper.getView(R.id.img_arrows_right);
                if (item.isShowToggelButton()) {
                    toggleButton.setVisibility(View.VISIBLE);
                    img_arrows_right.setVisibility(View.GONE);
                } else {
                    toggleButton.setVisibility(View.GONE);
                    img_arrows_right.setVisibility(View.VISIBLE);
                }
                if(item.getName().equals("开机启动")){
                    toggleButton.setChecked(getSharedPreferences("phoneHelperShared", MODE_PRIVATE).getBoolean("isStartActivity", false));
                }
                if(item.getName().equals("通知图标")){
                    toggleButton.setChecked(getSharedPreferences("phoneHelperShared", MODE_PRIVATE).getBoolean("isSendNotification",false));
                }
                toggleButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (item.getName().equals("开机启动")) {
                            SharedPreferences.Editor editor = getSharedPreferences("phoneHelperShared", MODE_PRIVATE).edit();
                            if (toggleButton.isChecked()) {
                                editor.putBoolean("isStartActivity", true);
                            } else {
                                editor.putBoolean("isStartActivity", false);
                            }
                            editor.commit();
                        }
                        if (item.getName().equals("通知图标")) {
                            SharedPreferences.Editor editor = getSharedPreferences("phoneHelperShared", MODE_PRIVATE).edit();
                            if (toggleButton.isChecked()) {
                                editor.putBoolean("isSendNotification", true);
                            } else {
                                editor.putBoolean("isSendNotification", false);
                            }
                            editor.commit();
                        }
                    }
                });
            }
        };
        lv_setting.setAdapter(adapter);
        lv_setting.setOnItemClickListener(this);
    }

    /**
     * 为listView初始化数据，boolean是否显示togglebtn
     */
    private List<SettingInfo> getSettingData() {
        List<SettingInfo> list = new ArrayList<>();
        SettingInfo set;
        set = new SettingInfo("开机启动", true);
        list.add(set);
        set = new SettingInfo("通知图标", true);
        list.add(set);
        set = new SettingInfo("清理缓存", false);
        list.add(set);
        set = new SettingInfo("版本更新", false);
        list.add(set);
        set = new SettingInfo("关于我们", false);
        list.add(set);
        set = new SettingInfo("帮助说明", false);
        list.add(set);
        return list;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        switch (i) {
            case 2:

                Intent intent = new Intent(this,CleanCacheMemoryActivity.class);
                startActivity(intent);
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
            default:
                break;

        }
    }
}