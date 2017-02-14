package com.example.administrator.phonehelper.activitys;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;
import com.example.administrator.phonehelper.R;
import com.example.administrator.phonehelper.base.BaseActivity;
import com.example.administrator.phonehelper.beans.TellInfo;
import com.example.administrator.phonehelper.dao.DataBaseOpenHelper;
import com.example.administrator.phonehelper.utils.CommonAdapter;
import com.example.administrator.phonehelper.utils.ViewHolder;
import com.example.administrator.phonehelper.widgets.TopBar;

import java.util.ArrayList;
import java.util.List;

/**
 * 由TellManager跳过来，显示详细的电话号码选项
 */
public class DetailedTellNumber extends BaseActivity {
    private TopBar topBar;
    private String titleName; //topbar的title
    private String tableName; //数据库的表名
    private ListView lv_detailedTellNumberList;
    private List<TellInfo> tellInfos;
    private CommonAdapter<TellInfo> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_tel_number);
        initView();
    }

    private void initView() {
        Intent intent = getIntent();
        titleName = intent.getStringExtra("titleName");
        tableName = intent.getStringExtra("tableName");
        topBar = (TopBar) this.findViewById(R.id.top_header);
        topBar.setTitleName(titleName);
        topBar.setOnTopBarClickListener(new TopBar.topbarClickListener() {
            @Override
            public void leftClick() {
                finish();
            }

            @Override
            public void rightClick() {

            }
        });

        lv_detailedTellNumberList = (ListView) this.findViewById(R.id.lv_detailedTellNumberList);
        tellInfos = getDataFromDB();
        adapter = new CommonAdapter<TellInfo>(this,tellInfos,R.layout.item_detailed_tellinfo) {
            @Override
            public void convert(ViewHolder helper, TellInfo item) {
                helper.setText(R.id.tv_tellName,item.getName());
                helper.setText(R.id.tv_tellNumber,item.getNumber());
            }
        };
        lv_detailedTellNumberList.setAdapter(adapter);
    }
    //启动本activity
    public static void actionStart(Context context, String titleName, String tableName) {
        Intent intent = new Intent(context, DetailedTellNumber.class);
        intent.putExtra("tableName", tableName);
        intent.putExtra("titleName", titleName);
        context.startActivity(intent);
    }
    //从数据库中获取数据
    private List<TellInfo> getDataFromDB(){
        List<TellInfo> list = new ArrayList<>();
        // 获取管理对象，因为数据库需要通过管理对象才能够获取
        DataBaseOpenHelper mg = DataBaseOpenHelper.getManager();
// 通过管理对象获取数据库
        SQLiteDatabase db1 = mg.getDatabase("commonnum.db");
// 对数据库进行操作
        StringBuffer buffer = new StringBuffer("select * from ").append(tableName);
        Cursor cursor =  db1.rawQuery(buffer.toString(), null);
        if (cursor.moveToFirst()){
            do {
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String number = cursor.getString(cursor.getColumnIndex("number"));
                TellInfo info  = new TellInfo(name,number);
                list.add(info);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }
}
