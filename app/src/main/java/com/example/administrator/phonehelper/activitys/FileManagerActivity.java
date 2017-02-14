package com.example.administrator.phonehelper.activitys;

import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.phonehelper.R;
import com.example.administrator.phonehelper.base.BaseActivity;
import com.example.administrator.phonehelper.beans.FileInfo;
import com.example.administrator.phonehelper.beans.FileManagerShow;
import com.example.administrator.phonehelper.constants.ConstantUtil;
import com.example.administrator.phonehelper.utils.CommonAdapter;
import com.example.administrator.phonehelper.utils.FileManagerUtils;
import com.example.administrator.phonehelper.utils.FileUtils;
import com.example.administrator.phonehelper.utils.GetPhoneSystemInfo;
import com.example.administrator.phonehelper.utils.ViewHolder;

import java.io.File;
import java.security.cert.Extension;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文件管理
 */
public class FileManagerActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private TextView tv_allFileSize;
    private ListView lv_fileContent;
    private CommonAdapter<FileManagerShow> adapter;
    private static List<FileManagerShow> showListData;
    /** 用来保存搜索到的文件列表文件实体对象 */
    private static HashMap<String, ArrayList<FileInfo>> fileListMap ;
    /** 用来保存搜索到的不同类型文件列表大小 */
    private static HashMap<String, Long> fileSizeMap ;
    private static final int GETFILEINFO = 1006;

    private static boolean isUpadateData = false;


    private String[] fileType = {"全部", "文档", "视频", "音频", "图像", "压缩包", "程序包"};
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GETFILEINFO:
                    for (FileManagerShow show :showListData ){
                        show.setFinish(true);
                    }
                    initView();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_manager);



        assginView();
        initView();
        if (fileSizeMap==null||fileSizeMap.get(FileManagerUtils.KEY_ANY)<=0){
           isUpadateData = true;
        }

        if (isUpadateData){
            FileManagerUtils.resetData();
            FileManagerUtils.setStopSearch(false);
            new GetFileSize().start();
        }else {
            getOldData();
        }
    }
//    获取已加载好的数据
    private void getOldData(){
        for (HashMap.Entry<String, Long> entry : fileSizeMap.entrySet()) {
            if (entry.getKey()==FileManagerUtils.KEY_ANY){
                showListData.get(0).setFileSize(entry.getValue());
            }
            if (entry.getKey()==FileManagerUtils.KEY_TXT){
                showListData.get(1).setFileSize(entry.getValue());
            }
            if (entry.getKey()==FileManagerUtils.KEY_VIDEO){
                showListData.get(2).setFileSize(entry.getValue());
            }
            if (entry.getKey()==FileManagerUtils.KEY_AUDIO){
                showListData.get(3).setFileSize(entry.getValue());
            }
            if (entry.getKey()==FileManagerUtils.KEY_IMAGE){
                showListData.get(4).setFileSize(entry.getValue());
            }
            if (entry.getKey()==FileManagerUtils.KEY_ZIP){
                showListData.get(5).setFileSize(entry.getValue());
            }
            if (entry.getKey()==FileManagerUtils.KEY_APK){
                showListData.get(6).setFileSize(entry.getValue());
            }
        }
        for (FileManagerShow show :showListData ){
            show.setFinish(true);
        }
        tv_allFileSize.setText(FileUtils.getFileSizeString(showListData.get(0).getFileSize()));
    }

    private void assginView() {
        tv_allFileSize = (TextView) this.findViewById(R.id.tv_allFileSize);
        lv_fileContent = (ListView) this.findViewById(R.id.lv_fileContent);
        initShowList();
    }

    private void initView() {

        adapter = new CommonAdapter<FileManagerShow>(this,showListData,R.layout.item_filemanager_list) {
            @Override
            public void convert(ViewHolder helper, FileManagerShow item) {
                helper.setText(R.id.tv_fileTypeName,item.getFileName());
                helper.setText(R.id.tv_fileTypeAllSize,FileUtils.getFileSizeString(item.getFileSize()));
                ProgressBar prob = helper.getView(R.id.prob_waiting);
                ImageView img_showContent  = helper.getView(R.id.img_showContent);
                if (item.isFinish()){
                    prob.setVisibility(View.GONE);
                    img_showContent.setVisibility(View.VISIBLE);
                }else {
                    prob.setVisibility(View.VISIBLE);
                    img_showContent.setVisibility(View.GONE);
                }
            }
        };

        lv_fileContent.setAdapter(adapter);
        lv_fileContent.setOnItemClickListener(this);
    }

    private void initShowList() {
        showListData = new ArrayList<>();
        for (int i = 0 ; i <fileType.length;i++){
            FileManagerShow show = new FileManagerShow();
            show.setFileName(fileType[i]);
            showListData.add(show);
        }
    }

    private class GetFileSize extends Thread {
        @Override
        public void run() {

           FileManagerUtils.fileSearchFromSDCard(new FileManagerUtils.FileSearchCallback() {
               @Override
               public void onSearchIng(String key) {
                   if (key==FileManagerUtils.KEY_ANY){
                       showListData.get(0).setFileSize(FileManagerUtils.getFileSizeMap().get(key));
                   }
                   if (key==FileManagerUtils.KEY_TXT){
                       showListData.get(1).setFileSize(FileManagerUtils.getFileSizeMap().get(key));
                   }
                   if (key==FileManagerUtils.KEY_VIDEO){
                       showListData.get(2).setFileSize(FileManagerUtils.getFileSizeMap().get(key));
                   }
                   if (key==FileManagerUtils.KEY_AUDIO){
                       showListData.get(4).setFileSize(FileManagerUtils.getFileSizeMap().get(key));
                   }
                   if (key==FileManagerUtils.KEY_IMAGE){
                       showListData.get(5).setFileSize(FileManagerUtils.getFileSizeMap().get(key));
                   }
                   if (key==FileManagerUtils.KEY_ZIP){
                       showListData.get(6).setFileSize(FileManagerUtils.getFileSizeMap().get(key));
                   }
                   if (key==FileManagerUtils.KEY_APK){
                       showListData.get(7).setFileSize(FileManagerUtils.getFileSizeMap().get(key));
                   }

                   runOnUiThread(new Runnable() {
                       @Override
                       public void run() {
                           adapter.notifyDataSetChanged();
                           tv_allFileSize.setText(FileUtils.getFileSizeString(showListData.get(0).getFileSize()));
                       }
                   });
               }

               @Override
               public void onSearchEnd(boolean isExceptionEnd, File targetFile) {
                   fileListMap = FileManagerUtils.getFileListMap();
                   fileSizeMap = FileManagerUtils.getFileSizeMap();
                   isUpadateData = false;
                   if (isExceptionEnd){
                       FileManagerUtils.resetData();
                       FileManagerUtils.setStopSearch(true);
                       isUpadateData = true;
                   }

                   Message msg = new Message();
                   msg.what = GETFILEINFO;
                   handler.sendMessage(msg);
                   FileManagerUtils.setStopSearch(true);
               }
           });

            super.run();

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode== ConstantUtil.START_FILELISTACTIVITY&&resultCode==ConstantUtil.FORSESULT_FILELISTACTIVITY){
            isUpadateData  = data.getBooleanExtra("isUpadate",false);
            Toast.makeText(this,data.getBooleanExtra("isUpadate",false)+"",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        switch (i){
            case 0:
                FileListActivity.actionStart(this,FileManagerUtils.KEY_ANY,fileListMap.get(FileManagerUtils.KEY_ANY));
                break;
            case 1:
                FileListActivity.actionStart(this,FileManagerUtils.KEY_TXT,fileListMap.get(FileManagerUtils.KEY_TXT));
                break;
            case 2:
                FileListActivity.actionStart(this,FileManagerUtils.KEY_VIDEO,fileListMap.get(FileManagerUtils.KEY_VIDEO));
                break;
            case 3:
                FileListActivity.actionStart(this,FileManagerUtils.KEY_AUDIO,fileListMap.get(FileManagerUtils.KEY_AUDIO));
                break;
            case 4:
                FileListActivity.actionStart(this,FileManagerUtils.KEY_IMAGE,fileListMap.get(FileManagerUtils.KEY_IMAGE));
                break;
            case 5:
                FileListActivity.actionStart(this,FileManagerUtils.KEY_ZIP,fileListMap.get(FileManagerUtils.KEY_ZIP));
                break;
            case 6:

                FileListActivity.actionStart(this,FileManagerUtils.KEY_APK,fileListMap.get(FileManagerUtils.KEY_APK));
                break;
            default:
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        FileManagerUtils.setStopSearch(true);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (isUpadateData==false){
            FileManagerUtils.resetData();
        }
        return super.onKeyDown(keyCode, event);
    }
}
