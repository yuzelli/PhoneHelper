package com.example.administrator.phonehelper.activitys;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.example.administrator.phonehelper.R;
import com.example.administrator.phonehelper.base.BaseActivity;
import com.example.administrator.phonehelper.dao.DataBaseOpenHelper;
import com.example.administrator.phonehelper.utils.CommonAdapter;
import com.example.administrator.phonehelper.utils.ViewHolder;
import com.example.administrator.phonehelper.widgets.TopBar;

import java.util.ArrayList;
import java.util.List;

/**
 * 通信大全
 */
public class TellManagerActivity extends BaseActivity implements OnItemClickListener {
    private TopBar top_header;
    private List<String> phoneModelList;
    private GridView gv_phoneMode;
    private CommonAdapter<String> adapter;
    private DataBaseOpenHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tell_manager);
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

        gv_phoneMode  = (GridView) this.findViewById(R.id.gv_phoneMode);
        phoneModelList = new ArrayList<>();
        phoneModelList = getDataFromDB();

        adapter = new CommonAdapter<String>(this,phoneModelList,R.layout.item_tellmanager_phonemode) {
            @Override
            public void convert(ViewHolder helper, String item) {
                helper.setText(R.id.tv_content,item);
            }
        };
        gv_phoneMode.setAdapter(adapter);
        gv_phoneMode.setOnItemClickListener(this);
    }


    //从数据库中获取数据
    private List<String> getDataFromDB(){
        List<String> list = new ArrayList<>();
        // 获取管理对象，因为数据库需要通过管理对象才能够获取
        DataBaseOpenHelper mg = DataBaseOpenHelper.getManager();
// 通过管理对象获取数据库
        SQLiteDatabase db1 = mg.getDatabase("commonnum.db");
// 对数据库进行操作
        Cursor cursor =  db1.rawQuery("select * from classlist", null);
        if (cursor.moveToFirst()){
            do {
                String name = cursor.getString(cursor.getColumnIndex("name"));
                list.add(name);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
           StringBuffer buffer = new StringBuffer("table");
           buffer.append(i+1);
           DetailedTellNumber.actionStart(this, phoneModelList.get(i), buffer.toString());
    }
}
