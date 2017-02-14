package com.example.administrator.phonehelper.activitys;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.phonehelper.R;
import com.example.administrator.phonehelper.beans.CacheMemoryInfo;
import com.example.administrator.phonehelper.beans.RubbishInfo;
import com.example.administrator.phonehelper.dao.DataBaseOpenHelper;
import com.example.administrator.phonehelper.receiver.BatteryReceiver;
import com.example.administrator.phonehelper.utils.CommonAdapter;
import com.example.administrator.phonehelper.utils.FileUtils;
import com.example.administrator.phonehelper.utils.MyAppManager;
import com.example.administrator.phonehelper.utils.ViewHolder;
import com.example.administrator.phonehelper.widgets.TopBar;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RubbishActivity extends Activity {
    private TopBar top_header;
    private TextView tv_allRubbish;
    private TextView tv_checkingapp;
    private ListView lv_rubbishList;
    private ProgressBar pr_appcachebar;
    private Button btn_cleanRubbish;
    private CheckBox cb_selectAll;

    private List<RubbishInfo> rubbishInfoList;

    private CommonAdapter<RubbishInfo> adapter;

    private long sumSize;
    private static final int GETDATA = 1005;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case GETDATA:
                    tv_allRubbish.setText(FileUtils.getFileSizeString(sumSize));
                    initListView();
                    pr_appcachebar.setVisibility(View.GONE);
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rubbish);
        assignView();
        initView();

       new GetDataByDB().start();

    }
    private class GetDataByDB extends Thread{
        @Override
        public void run() {
            getDataFromDB();
            super.run();
            //扫描完毕发送消息

            Message msg = new Message();
            msg.what = GETDATA;
            handler.sendMessage(msg);
        }

    }

    private void assignView() {
        top_header = (TopBar) this.findViewById(R.id.top_header);
        tv_allRubbish = (TextView) this.findViewById(R.id.tv_allRubbish);
        tv_checkingapp = (TextView) this.findViewById(R.id.tv_checkingapp);
        lv_rubbishList = (ListView) this.findViewById(R.id.lv_rubbishList);
        pr_appcachebar = (ProgressBar) this.findViewById(R.id.pr_appcachebar);
        btn_cleanRubbish = (Button) this.findViewById(R.id.btn_cleanRubbish);
        cb_selectAll = (CheckBox) this.findViewById(R.id.cb_selectAll);
    }
    private void initView() {
        top_header.setOnTopBarClickListener(new TopBar.topbarClickListener() {
            @Override
            public void leftClick() {
                finish();
            }

            @Override
            public void rightClick() {

            }
        });
        cb_selectAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                SelectAll(isChecked);
            }
        });
        btn_cleanRubbish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 清理
                /**
                 * 1、获取最新list 2、遍历list 3、如果是”选中“状态，则清理
                 */

                for (RubbishInfo clearInfo : rubbishInfoList) {
                    if (clearInfo.isCheck()) {
                        String path = clearInfo.getPath();
                        File file = new File(Environment
                                .getExternalStorageDirectory(), path);
                        FileUtils.deleteFile(file);
                    }
                }
                new GetDataByDB().start();
            }
        });

    }


//    加载listView的数据
    private void initListView() {

        adapter = new CommonAdapter<RubbishInfo>(this,rubbishInfoList,R.layout.item_rubbish_list) {
            @Override
            public void convert(ViewHolder helper, final RubbishInfo item) {
                final CheckBox cBox_isDelete = helper.getView(R.id.cBox_isDelete);
                cBox_isDelete.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                       item.setIsCheck(b);
                    }
                });
                cBox_isDelete.setChecked(item.isCheck());

                ImageView img = helper.getView(R.id.img_appICON);
                img.setImageDrawable(item.getAppIcon());
                helper.setText(R.id.tv_appName,item.getAppName());
                helper.setText(R.id.tv_appRubbishSize,FileUtils.getFileSizeString(item.getSize())+"");

            }
        };
        lv_rubbishList.setAdapter(adapter);
    }
    //从数据库中获取数据
    private void getDataFromDB(){
        List<RubbishInfo> list = new ArrayList<>();
        // 获取管理对象，因为数据库需要通过管理对象才能够获取
        DataBaseOpenHelper mg = DataBaseOpenHelper.getManager();
// 通过管理对象获取数据库
        SQLiteDatabase db1 = mg.getDatabase("clearpath.db");
// 对数据库进行操作
        Cursor cursor =  db1.rawQuery("select * from softdetail", null);
        if (cursor.moveToFirst()){
            do {
                RubbishInfo info = new RubbishInfo();
                String path = cursor.getString(4);
                // 完整的应用路径 storage/sdcard/a.txt
                File f = new File(Environment
                        .getExternalStorageDirectory(), path);
                if (f.exists()){
                    // 填充集合
                    // 应用的中文名称
                    String name = cursor.getString(1);
                    info.setAppName(name);
                    // 应用的apk名称--》包名
                    String apkName =cursor.getString(3);
                    info.setApkName(apkName);
                    // 应用的路径
                    info.setPath(path);
                    // 应用的图标---PackageManager
                    try {
                        Drawable d = getPackageManager()
                                .getApplicationIcon(apkName);
                        info.setAppIcon(d);
                    } catch (PackageManager.NameNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        // 假如无法找到指定的应用，则显示默认图标
                        Drawable drawable = getResources().getDrawable(
                                R.mipmap.icon_png);
                        info.setAppIcon(drawable);
                    }
                    // 文件大小
                    long size = FileUtils.getFileSize(f);
                    info.setSize(size);
                    list.add(info);
                    // 每一个符合要求的应用都执行字节数叠加
                    sumSize += size;
                    // 回归主线程

                }

            }while (cursor.moveToNext());
        }
        cursor.close();
       // db1.close();
        rubbishInfoList = list;
    }
    // 实现全选或反选的方法
    public void SelectAll(boolean b) {
        // 2、遍历
        for (RubbishInfo clearInfo : rubbishInfoList) {
            clearInfo.setIsCheck(b);
        }
        Toast.makeText(this,"-->sss",Toast.LENGTH_SHORT).show();
        initListView();
    }
}
