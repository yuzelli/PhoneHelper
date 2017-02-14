package com.example.administrator.phonehelper.activitys;


import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.phonehelper.R;
import com.example.administrator.phonehelper.base.BaseActivity;
import com.example.administrator.phonehelper.beans.ProgressInfo;
import com.example.administrator.phonehelper.utils.CleanUtil;
import com.example.administrator.phonehelper.utils.CommonAdapter;
import com.example.administrator.phonehelper.utils.GetPhoneSystemInfo;
import com.example.administrator.phonehelper.utils.MyAppManager;
import com.example.administrator.phonehelper.utils.ViewHolder;
import com.example.administrator.phonehelper.widgets.RocketProgress;
import com.example.administrator.phonehelper.widgets.TopBar;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 手机加速页面
 */
public class RocketActivity extends BaseActivity implements AdapterView.OnItemClickListener{
    private TopBar top_header;
    private ListView lv_rocket_progress;
    private Button btn_showWhat;   //显示用户还是全部进程
    private ProgressBar pr_loadingGetProgress; //等待指示器
    private static final int GETPROGRESS= 0;

    private TextView tv_brand;  //品牌
    private TextView tv_model;  //型号
    private TextView tv_rocket_message;  //内存使用情况
    private RocketProgress rocket_progress;   //内存使用进度条
    private Button btn_killProgress;  //一键加速

    private List<ProgressInfo> progressInfos;
    private CommonAdapter<ProgressInfo> adapter;
    private ActivityManager mActivityManager;


    private Context context;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case GETPROGRESS:
                    initListView();
                    pr_loadingGetProgress.setVisibility(View.GONE);
                    break;
            }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rocket);
        context = this;
        initView();
        new GetRunProgress().start();
        initHeadView();
    }


    private void initView() {
        top_header = (TopBar) this.findViewById(R.id.top_header);
        btn_showWhat = (Button) this.findViewById(R.id.btn_showWhat);
        pr_loadingGetProgress = (ProgressBar) this.findViewById(R.id.pr_loadingGetProgress);
        tv_brand = (TextView) this.findViewById(R.id.tv_brand);
        tv_model = (TextView) this.findViewById(R.id.tv_model);
        tv_rocket_message = (TextView) this.findViewById(R.id.tv_rocket_message);
        rocket_progress = (RocketProgress) this.findViewById(R.id.rocket_progress);
        btn_killProgress = (Button) this.findViewById(R.id.btn_killProgress);
        mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        lv_rocket_progress = (ListView) this.findViewById(R.id.lv_rocket_progress);
        top_header.setOnTopBarClickListener(new TopBar.topbarClickListener() {
            @Override
            public void leftClick() {
                finish();
            }

            @Override
            public void rightClick() {

            }
        });

        btn_killProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for(ProgressInfo info:progressInfos) {
                    if (info.isCheck()){
                        mActivityManager.killBackgroundProcesses(info.getPackageName());
                    }
                }
                initHeadView();
                new GetRunProgress().start();
            }
        });

    }


    private void initHeadView() {
        tv_brand.setText(Build.BRAND);
        tv_model.setText(android.os.Build.MODEL);
        GetPhoneSystemInfo getInfo = new GetPhoneSystemInfo(this);
        CleanUtil cleanUtil = new CleanUtil();
        double allSize = (double)((int)(cleanUtil.getAllMemory()/1024./1024.*100.))/100;
        tv_rocket_message.setText("已使用/全部："+(int)cleanUtil.getUesdMemory(this)/1024+"/"+allSize+"G");
        rocket_progress.autoNum((int)(cleanUtil.getUesdMemory(this)/1024*100)/(int)(allSize*1024));
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        String  f =Environment.getExternalStorageDirectory().getAbsolutePath();
        f =f+File.separator+ File.separator+"Android"+File.separator+"data"+progressInfos.get(i).getPackageName();
        Toast.makeText(this,"this--->"+f,Toast.LENGTH_SHORT).show();
    }

    private class GetRunProgress extends Thread{

        @Override
        public void run() {
            progressInfos =  MyAppManager.getMyAppManager(context).getAllAppInfo();
            super.run();
            //扫描完毕发送消息
            Message msg = new Message();
            msg.what = GETPROGRESS;
            handler.sendMessage(msg);
        }
    }

    private void initListView() {
        pr_loadingGetProgress.setVisibility(View.VISIBLE);

        adapter = new CommonAdapter<ProgressInfo>(this,progressInfos , R.layout.item_application_list) {
            @Override
            public void convert(ViewHolder helper, final ProgressInfo item) {
                helper.setText(R.id.tv_appName, item.getAppName());
                helper.setText(R.id.tv_appPath,item.getProgressMemory()+"M");
                ImageView imgView = helper.getView(R.id.img_appICON);
                imgView.setImageDrawable(item.getAppIcon());
                final CheckBox checkBox = helper.getView(R.id.cBox_isDelete);
                checkBox.setChecked(item.isCheck());
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        item.setCheck(b);
                        Toast.makeText(context,item.isCheck()+"",Toast.LENGTH_SHORT).show();
                    }
                });


            }
        };
        lv_rocket_progress.setAdapter(adapter);
        lv_rocket_progress.setOnItemClickListener(this);
    }


}

































//    private List<ProgressInfo> queryAllRunningAppInfo() {
//        pm = this.getPackageManager();
//        // 查询所有已经安装的应用程序
//        List<ApplicationInfo> listAppcations = pm.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
//        Collections.sort(listAppcations, new ApplicationInfo.DisplayNameComparator(pm));// 排序
//        // 保存所有正在运行的包名 以及它所在的进程信息
//        Map<String, ActivityManager.RunningAppProcessInfo> pgkProcessAppMap = new HashMap<String, ActivityManager.RunningAppProcessInfo>();
//         mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
//        // 通过调用ActivityManager的getRunningAppProcesses()方法获得系统里所有正在运行的进程
//        List<ActivityManager.RunningAppProcessInfo> appProcessList = mActivityManager
//                .getRunningAppProcesses();
//        for (ActivityManager.RunningAppProcessInfo appProcess : appProcessList) {
//            int pid = appProcess.pid; // pid
//            String processName = appProcess.processName; // 进程名
//          //  Log.i(TAG, "processName: " + processName + "  pid: " + pid);
//            String[] pkgNameList = appProcess.pkgList; // 获得运行在该进程里的所有应用程序包
//            // 输出所有应用程序的包名
//            for (int i = 0; i < pkgNameList.length; i++) {
//                String pkgName = pkgNameList[i];
//             //   Log.i(TAG, "packageName " + pkgName + " at index " + i+ " in process " + pid);
//                // 加入至map对象里
//                pgkProcessAppMap.put(pkgName, appProcess);
//            }
//        }
//        // 保存所有正在运行的应用程序信息
//        List<ProgressInfo> runningAppInfos = new ArrayList<>(); // 保存过滤查到的AppInfo
//        for (ApplicationInfo app : listAppcations) {
//            // 如果该包名存在 则构造一个RunningAppInfo对象
//            if (pgkProcessAppMap.containsKey(app.packageName)) {
//                // 获得该packageName的 pid 和 processName
//                int pid = pgkProcessAppMap.get(app.packageName).pid;
//                String processName = pgkProcessAppMap.get(app.packageName).processName;
//                if (btn_showWhat.getText().equals("显示全部进程")){
//                    if ((app.flags&ApplicationInfo.FLAG_SYSTEM) == 0){
//                        runningAppInfos.add(getAppInfo(app, pid, processName));
//                    }
//                }else {
//                    runningAppInfos.add(getAppInfo(app, pid, processName));
//                }
//            }
//        }
//
//        return runningAppInfos;
//
//    }
//
//    private ProgressInfo getAppInfo(ApplicationInfo app, int pid, String processName) {
//        ProgressInfo info = new ProgressInfo();
//        info.setPackageName(app.packageName);
//        info.setAppIcon(app.loadIcon(pm));
//        info.setProgressName((String) app.loadLabel(pm));
//        // 获得该进程占用的内存
//        int[] myMempid = new int[] { pid };
//        // 此MemoryInfo位于android.os.Debug.MemoryInfo包中，用来统计进程的内存信息
//        Debug.MemoryInfo[] memoryInfo = mActivityManager
//                .getProcessMemoryInfo(myMempid);
//        // 获取进程占内存用信息 kb单位
//        int memSize = memoryInfo[0].dalvikPrivateDirty;
//        info.setProgressMemory((int)memSize/1024);
//        //判断是不是system进程；   ||(app.flags & ApplicationInfo.FLAG_SYSTEM) == 0
//        if ((app.flags&ApplicationInfo.FLAG_SYSTEM) == 0){
//            info.setIsSystem(false);
//        }else {
//            info.setIsSystem(true);
//        }
//        return info;
//    }