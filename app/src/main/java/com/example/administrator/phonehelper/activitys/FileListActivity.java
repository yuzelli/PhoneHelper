package com.example.administrator.phonehelper.activitys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.administrator.phonehelper.R;
import com.example.administrator.phonehelper.base.BaseActivity;
import com.example.administrator.phonehelper.beans.FileInfo;
import com.example.administrator.phonehelper.beans.FileListInfo;
import com.example.administrator.phonehelper.constants.ConstantUtil;
import com.example.administrator.phonehelper.utils.CommonAdapter;
import com.example.administrator.phonehelper.utils.FileManagerUtils;
import com.example.administrator.phonehelper.utils.FileOpenUtils;
import com.example.administrator.phonehelper.utils.FileUtils;
import com.example.administrator.phonehelper.utils.ViewHolder;
import com.example.administrator.phonehelper.widgets.TopBar;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FileListActivity extends BaseActivity implements AdapterView.OnItemClickListener{
    private TopBar top_header;
    private ListView lv_fileContentList;
    private CommonAdapter<FileInfo> adapter;
    private List<FileInfo> fileListData;

    private Button btn_clean;
    private static boolean isUpadate = false;
    private ImageLoader imageLoader ;
    private Intent intent;

//    private final static int GETFILEDATE = 1007;
//    private Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what) {
//                case GETFILEDATE:
//                    prob_waiting.setVisibility(View.GONE);
//                    initListView();
//                    break;
//            }
//        }
//    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_list);

    }

    @Override
    protected void onStart() {
        super.onStart();
        intent = getIntent();
        fileListData = (ArrayList<FileInfo>) intent.getSerializableExtra("fileInfos");
        initView();
        initListView();
    }

    private void initView() {
        btn_clean = (Button) this.findViewById(R.id.btn_clean);
        top_header = (TopBar) this.findViewById(R.id.top_header);
        lv_fileContentList = (ListView) this.findViewById(R.id.lv_fileContentList);

        top_header.setOnTopBarClickListener(new TopBar.topbarClickListener() {
            @Override
            public void leftClick() {
                intent.putExtra("isUpadate", isUpadate);
                //设置回传的意图p
                setResult(ConstantUtil.FORSESULT_FILELISTACTIVITY, intent);
                finish();
            }

            @Override
            public void rightClick() {

            }
        });

        btn_clean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (FileInfo info :fileListData )
                {
                    //删物理文件，清filemanger的集合
                    File file = info.getFile();
                    if (info.isSelect()){
                        if (file.isFile() && file.exists()) {
                            if(file.delete()){
                                isUpadate = true;
                                FileManagerUtils.removeFile(file.length(),info);
                            }
                        }
                    }
                }
                for (int i =0;i<fileListData.size();i++){
                    if (fileListData.get(i).isSelect()){
                        fileListData.remove(i);
                    }
                }

                adapter.notifyDataSetChanged();

            }
        });

    }

    //加载listView的内容
    private void initListView() {
        //显示图片的配置
        final DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showStubImage(R.mipmap.ic_launcher)
                .showImageForEmptyUri(R.mipmap.ic_launcher)
                .showImageOnFail(R.mipmap.ic_launcher)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .imageScaleType(ImageScaleType.NONE)
                .bitmapConfig(Bitmap.Config.RGB_565)//设置为RGB565比起默认的ARGB_8888要节省大量的内存
                .delayBeforeLoading(100)//载入图片前稍做延时可以提高整体滑动的流畅度
                .build();


        adapter = new CommonAdapter<FileInfo>(this,fileListData,R.layout.item_file_list) {
            @Override
            public void convert(ViewHolder helper, final FileInfo item) {
                helper.setText(R.id.tv_fileName,item.getFile().getName());
                long time = item.getFile().lastModified();//返回文件最后修改时间，是以个long型毫秒数
                String ctime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date(time));
                helper.setText(R.id.tv_fileCreateTime,ctime);
                helper.setText(R.id.tv_fileSize, FileUtils.getFileSizeString(item.getFile().length()));
                final CheckBox cBox_isDelete = helper.getView(R.id.cBox_isDelete);
                cBox_isDelete.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        item.setSelect(b);
                    }
                });
                cBox_isDelete.setChecked(item.isSelect());
                final ImageView img_fileIcon =helper.getView(R.id.img_fileIcon);
                String imgurl = item.getFile().getPath();
                imageLoader = ImageLoader.getInstance();
                imageLoader.displayImage(imgurl, new ImageViewAware(img_fileIcon), options, null, null);

            }
        };
        lv_fileContentList.setAdapter(adapter);
        lv_fileContentList.setOnItemClickListener(this);
        lv_fileContentList.setEmptyView(findViewById(R.id.img_emptyView));
        //listView猛滑动停止加载图片
       // lv_fileContentList.setOnScrollListener(new PauseOnScrollListener(imageLoader, true, true));
    }
//
//    //     获取数据的方法
//    private class GetFileList extends Thread {
//        @Override
//        public void run() {
//            super.run();
//
//            Message msg = new Message();
//            msg.what = GETFILEDATE;
//            handler.sendMessage(msg);
//        }
//    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
       Intent intent =  FileOpenUtils.openFile(fileListData.get(i).getFile().getPath());
        this.startActivity(intent);
    }

    //    跳转页面
    public static void actionStart(Activity context, String type, ArrayList<FileInfo> fileInfos) {
        Intent intent = new Intent(context, FileListActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("fileInfos",fileInfos);
        context.startActivityForResult(intent, ConstantUtil.START_FILELISTACTIVITY);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        intent.putExtra("isUpadate", isUpadate);
        //设置回传的意图p
        setResult(ConstantUtil.FORSESULT_FILELISTACTIVITY, intent);
        return super.onKeyDown(keyCode, event);

    }

}
