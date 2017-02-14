package com.example.administrator.phonehelper.activitys;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.administrator.phonehelper.R;
import com.example.administrator.phonehelper.base.BaseActivity;
import com.example.administrator.phonehelper.beans.AppInfo;
import com.example.administrator.phonehelper.config.LogUtil;
import com.example.administrator.phonehelper.utils.CommonAdapter;
import com.example.administrator.phonehelper.utils.ViewHolder;
import com.example.administrator.phonehelper.widgets.TopBar;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.nostra13.universalimageloader.utils.L;

import java.util.ArrayList;
import java.util.List;

public class ApplicationListActivity extends BaseActivity implements View.OnClickListener {
    private static ListView lv_applicationList;
    private TopBar top_header;
    private static List<AppInfo> listData;
    private static CommonAdapter<AppInfo> adapter;
    private Button btn_deleteSelectApp;
    private static Context context;

    private static int dataType;  //类型分类
    private static String titleName;   //标题
    //    传过来的是那种选项（所有，系统应用，用户应用）
    private final static int ALL_APPLICATION = 0;
    private final static int SYSTEM_APPLICATION = 1;
    private final static int USER_APPLICATION = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application_list);
        Intent intent = getIntent();
        titleName = intent.getStringExtra("titleName");
        dataType = intent.getIntExtra("dataType", 0);

        initView();

    }

    private void initView() {
        context = getApplicationContext();
        top_header = (TopBar) this.findViewById(R.id.top_header);
        btn_deleteSelectApp = (Button) this.findViewById(R.id.btn_deleteSelectApp);
        btn_deleteSelectApp.setOnClickListener(this);
        if (dataType != 2) {
            btn_deleteSelectApp.setVisibility(View.GONE);
        }
        top_header.setTitleName(titleName);
        top_header.setOnTopBarClickListener(new TopBar.topbarClickListener() {
            @Override
            public void leftClick() {
                finish();
            }

            @Override
            public void rightClick() {

            }
        });

        lv_applicationList = (ListView) this.findViewById(R.id.lv_applicationList);
        initListView();
    }

    private static void initListView() {
        listData = getAllApps(context, dataType);
        adapter = new CommonAdapter<AppInfo>(context, listData, R.layout.item_application_list) {
            @Override
            public void convert(ViewHolder helper, final AppInfo item) {

                final CheckBox btn = helper.getView(R.id.cBox_isDelete);
                btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        item.setIsDelete(btn.isChecked());
                        Toast.makeText(context, item.isDelete() + "", Toast.LENGTH_SHORT).show();
                    }
                });
                btn.setChecked(item.isDelete());
                if (!titleName.equals("用户应用")) {
                    btn.setVisibility(View.GONE);
                }
                ImageView imgView = helper.getView(R.id.img_appICON);
                imgView.setImageDrawable(item.getAppICON());
                helper.setText(R.id.tv_appName, item.getAppName());
                helper.setText(R.id.tv_appVersion, item.getAppVersion());
                helper.setText(R.id.tv_appPath, item.getAppPath());
            }
        };
        lv_applicationList.setAdapter(adapter);
        lv_applicationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AppInfo info = listData.get(i);
                String appPackageName = info.getAppPath();
                try {

                    Intent intent = context.getPackageManager().getLaunchIntentForPackage(appPackageName);
                    if (intent != null && !appPackageName.equals("com.example.administrator.phonehelper")) {
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                } catch (Exception e) {
                    Toast.makeText(context, "没有安装", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    //    获取listView中显示的数据
    public static List<AppInfo> getAllApps(Context context, int type) {
        //获取手机中所有已安装的应用，并判断是否系统应用
        ArrayList<AppInfo> appList = new ArrayList<AppInfo>(); //用来存储获取的应用信息数据，手机上安装的应用数据都存在appList里
        List<PackageInfo> packages = context.getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < packages.size(); i++) {
            PackageInfo packageInfo = packages.get(i);
            AppInfo tmpInfo = new AppInfo();
            tmpInfo.setIsDelete(false);
            tmpInfo.setAppName(packageInfo.applicationInfo.loadLabel(context.getPackageManager()).toString());
            tmpInfo.setAppPath(packageInfo.packageName);
            String ver = packageInfo.versionName;
            if (ver.length() > 6) {
                ver = ver.substring(0, 5);
            }
            tmpInfo.setAppVersion(ver);
            tmpInfo.setAppICON(packageInfo.applicationInfo.loadIcon(context.getPackageManager()));

            switch (type) {
                case ALL_APPLICATION:
                    appList.add(tmpInfo);
                    break;
                case SYSTEM_APPLICATION:
                    if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                        //系统应用
                        appList.add(tmpInfo);
                    }
                    break;
                case USER_APPLICATION:
                    if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                        //非系统应用
                        appList.add(tmpInfo);
                    }
                    break;
                default:
                    break;
            }
        }
        return appList;
    }

    //跳转
    public static void actionStart(Context context, String titleName, int dataType) {
        Intent intent = new Intent(context, ApplicationListActivity.class);
        intent.putExtra("titleName", titleName);
        intent.putExtra("dataType", dataType);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_deleteSelectApp:
                doDelectSelectApp();
                break;
        }
    }

    public static void notifyData() {
        initListView();
    }

    private void doDelectSelectApp() {
        for (int i = 0; i < listData.size(); i++) {
            if (listData.get(i).isDelete()) {
                Uri uri = Uri.parse("package:" + listData.get(i).getAppPath());//获取删除包名的URI
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_DELETE);//设置我们要执行的卸载动作
                intent.setData(uri);//设置获取到的URI
                startActivity(intent);
            }
        }
    }


}





