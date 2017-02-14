package com.example.administrator.phonehelper.activitys;

import android.os.Debug;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.phonehelper.R;
import com.example.administrator.phonehelper.base.BaseActivity;
import com.example.administrator.phonehelper.utils.CleanUtil;
import com.example.administrator.phonehelper.utils.CommonAdapter;
import com.example.administrator.phonehelper.utils.FileUtils;
import com.example.administrator.phonehelper.utils.GetPhoneSystemInfo;
import com.example.administrator.phonehelper.utils.MemoryManager;
import com.example.administrator.phonehelper.utils.ViewHolder;
import com.example.administrator.phonehelper.widgets.CirleProgress;
import com.example.administrator.phonehelper.widgets.RocketProgress;
import com.example.administrator.phonehelper.widgets.TopBar;

import java.util.ArrayList;
import java.util.List;

/**
 * 软件管理
 */
public class SoftManagerActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private ListView lv_phoneMessage;
    private TextView tv_memory;
    private CommonAdapter<String> adapter;
    private RocketProgress soft_progress;
    private TopBar top_header;
    private CirleProgress cp_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soft_manager);
        initView();
        initwidgtsView();
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
        lv_phoneMessage = (ListView) this.findViewById(R.id.lv_softManager);
        adapter = new CommonAdapter<String>(this, getSoftManagerData(), R.layout.item_softmanager_list) {
            @Override
            public void convert(ViewHolder helper, String item) {
                helper.setText(R.id.tv_softSelect, item);
            }
        };
        lv_phoneMessage.setAdapter(adapter);
        lv_phoneMessage.setOnItemClickListener(this);

    }

    private void initwidgtsView() {
        soft_progress = (RocketProgress) this.findViewById(R.id.soft_progress);
        cp_view = (CirleProgress) this.findViewById(R.id.cp_view);
        tv_memory = (TextView) this.findViewById(R.id.tv_memory);
//        GetPhoneSystemInfo info = new GetPhoneSystemInfo(this);
//        String a = info.getRomTotalSize();//所有内存
//        String b = info.getRomAvailableSize();//已用内存
//        int index = a.indexOf("G");
//        int index1 = b.indexOf("M");
//        int aa = (int) (Double.valueOf(a.substring(0,index))*1024);
//        int bb =Integer.valueOf(b.substring(0,index1));
        long all = MemoryManager.getSelfSize();
        long noused = MemoryManager.getSelfAvailableSize();
        cp_view.setMemory((int) ((all-noused)/(double)all*360));
        soft_progress.autoNum((int) ((all-noused)/(double)all*100));
        tv_memory.setText("已使用/全部:"+ FileUtils.getFileSizeString(all-noused)+"/"+FileUtils.getFileSizeString(all));
    }
    private List<String> getSoftManagerData() {
        List<String> list = new ArrayList<>();
        list.add("所有应用");
        list.add("系统应用");
        list.add("用户应用");
        return list;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        switch (i) {
            case 0:
                ApplicationListActivity.actionStart(this, "所有应用", i);
                break;
            case 1:
                ApplicationListActivity.actionStart(this, "系统应用", i);
                break;
            case 2:
                ApplicationListActivity.actionStart(this, "用户应用", i);
                break;
        }


    }
}
