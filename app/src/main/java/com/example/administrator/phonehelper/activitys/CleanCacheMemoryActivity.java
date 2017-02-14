package com.example.administrator.phonehelper.activitys;


import android.content.Intent;
import android.content.pm.IPackageDataObserver;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.os.StatFs;
import android.text.format.Formatter;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.administrator.phonehelper.R;
import com.example.administrator.phonehelper.base.BaseActivity;
import com.example.administrator.phonehelper.beans.CacheMemoryInfo;
import com.example.administrator.phonehelper.widgets.TopBar;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 垃圾清理
 */
public class CleanCacheMemoryActivity extends BaseActivity {
    private static final int SCANING = 0;
    private static final int ADD_APP_INFO = 1;
    private static final int SCAN_FINISH = 2;
    private static final int CLEAN_FINISH = 3;

    private TopBar top_header;
    private ProgressBar pr_appcachebar;//进度条
    private TextView tv_checkingapp;
    private TextView tv_allRubbish;
    private Button btn_cleanALL;//清理全部按钮
    private LinearLayout ll_container;//程序列表容器
    private long totalCacheSize = 0;//总缓存大小

    private PackageManager packageManager;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SCANING:
                    tv_checkingapp.setText("正在扫描：" + (String) msg.obj);
                    break;
                case ADD_APP_INFO:
                    View view = View.inflate(CleanCacheMemoryActivity.this, R.layout.item_sdclean_appinfo, null);
                    ImageView img_icon = (ImageView) view.findViewById(R.id.img_appICON);
                    TextView tv_packagename = (TextView) view.findViewById(R.id.tv_appName);
                    TextView tv_cachesize = (TextView) view.findViewById(R.id.tv_rubbish);
                    ImageView imgbn_delete = (ImageView) view.findViewById(R.id.imgbn_delete);
                    //赋值
                    final CacheMemoryInfo cacheInfo = (CacheMemoryInfo) msg.obj;
                    img_icon.setImageDrawable(cacheInfo.getAppIcon());
                    tv_packagename.setText(cacheInfo.getName());
                    tv_cachesize.setText("缓存大小:" + Formatter.formatFileSize(CleanCacheMemoryActivity.this, cacheInfo.getSize()));
                    //统计总缓存大小
                    totalCacheSize += cacheInfo.getSize();
                    tv_allRubbish.setText(Formatter.formatFileSize(getApplicationContext(),totalCacheSize));
                    //删除按钮点击事件
                    imgbn_delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                Method method = PackageManager.class.getMethod("deleteApplicationCacheFiles", String.class, IPackageStatsObserver.class);
                                method.invoke(packageManager, cacheInfo.getApkName(), new IPackageDataObserver.Stub() {

                                    @Override
                                    public void onRemoveCompleted(String packageName, boolean succeeded) throws RemoteException {

                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                                Intent intent = new Intent();
                                intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                                intent.addCategory(Intent.CATEGORY_DEFAULT);
                                intent.setData(Uri.parse("package:" + cacheInfo.getApkName()));
                                startActivity(intent);
                            }

                        }
                    });
                    ll_container.addView(view,0);
                    break;
                case SCAN_FINISH:
                    tv_checkingapp.setText("扫描完成！");
                    pr_appcachebar.setVisibility(View.GONE);
                    tv_allRubbish.setText(Formatter.formatFileSize(getApplicationContext(),totalCacheSize));
                    break;
                case CLEAN_FINISH:
                    ll_container.removeAllViews();
                    TextView tv_show_finish = new TextView(getApplicationContext());
                    tv_show_finish.setText("清理完成！");
                    tv_show_finish.setTextColor(Color.GRAY);
                    tv_show_finish.setTextSize(20);
                    ll_container.addView(tv_show_finish);
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clean_cache_menory);
        assignView();//
        initView();
    }


    private void assignView() {
        top_header = (TopBar) this.findViewById(R.id.top_header);
        pr_appcachebar = (ProgressBar) findViewById(R.id.pr_appcachebar);
        tv_checkingapp = (TextView) findViewById(R.id.tv_checkingapp);
        tv_allRubbish = (TextView) findViewById(R.id.tv_allRubbish);
        btn_cleanALL = (Button) findViewById(R.id.btn_cleanALL);
        ll_container = (LinearLayout) findViewById(R.id.ll_container);
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
        new GetAppCacheInfoThread().start();
        btn_cleanALL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Class[] arrayOfClass = new Class[2];
                    Class localClass2 = Long.TYPE;
                    arrayOfClass[0] = localClass2;
                    arrayOfClass[1] = IPackageDataObserver.class;
                    Method method = PackageManager.class.getMethod("freeStorageAndNotify", arrayOfClass);
                    method.invoke(packageManager, getEnvironmentSize() - 1L, new IPackageDataObserver.Stub() {

                        @Override
                        public void onRemoveCompleted(String packageName, boolean succeeded) throws RemoteException {
                            System.out.println("successed!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                            //发送消息清除缓存成功
                            Message msg = Message.obtain();
                            msg.what = CLEAN_FINISH;
                            handler.sendMessage(msg);
                        }
                    });
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private static long getEnvironmentSize()
    {
        File localFile = Environment.getDataDirectory();
        long l1;
        if (localFile == null)
            l1 = 0L;
        while (true)
        {

            String str = localFile.getPath();
            StatFs localStatFs = new StatFs(str);
            long l2 = localStatFs.getBlockSize();
            l1 = localStatFs.getBlockCount() * l2;
            return l1;
        }
    }

    private class GetAppCacheInfoThread extends Thread {
        @Override
        public void run() {
            packageManager = getPackageManager();
            List<PackageInfo> packageInfos = packageManager.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
            for (PackageInfo packageInfo : packageInfos) {
                String packageName = packageInfo.packageName;
                try {
                    Method method = PackageManager.class.getMethod("getPackageSizeInfo", String.class, IPackageStatsObserver.class);
                    method.invoke(packageManager, packageName, new MyIPackageStatsObserver());
                    //发送消息正在扫描的应用
                    Message msg = Message.obtain();
                    msg.what = SCANING;
                    msg.obj = packageInfo.applicationInfo.loadLabel(packageManager).toString();
                    handler.sendMessage(msg);
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            //扫描完毕发送消息
            Message msg = Message.obtain();
            msg.what = SCAN_FINISH;
            handler.sendMessage(msg);
        }
    }

    private class MyIPackageStatsObserver extends IPackageStatsObserver.Stub {

        @Override
        public void onGetStatsCompleted(PackageStats pStats, boolean succeeded) throws RemoteException {
            long cacheSize = pStats.cacheSize;
            long codeSize = pStats.codeSize;
            if (cacheSize > 0) {
                CacheMemoryInfo cacheInfo = new CacheMemoryInfo();
                //包名
                cacheInfo.setApkName(pStats.packageName);
                try {
                    //图标
                    cacheInfo.setAppIcon(packageManager.getApplicationIcon(pStats.packageName));
                    //名称
                    cacheInfo.setName(packageManager.getApplicationInfo(pStats.packageName, 0).loadLabel(packageManager).toString());
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                //缓存大小
                cacheInfo.setSize(cacheSize);
                //发送添加消息
                Message msg = Message.obtain();
                msg.what = ADD_APP_INFO;
                msg.obj = cacheInfo;
                handler.sendMessage(msg);
            }
        }
    }


}
