package zhang.com.java.ac;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;
import zhang.com.java.ac.DB.DB;
import zhang.com.java.ac.adapter.CollectAdapter;
import zhang.com.java.ac.bean.DBChuanInfo;

public class CollectActivity extends SwipeBackActivity implements DialogInterface.OnClickListener {
    private ArrayList list;
    private DB db;
    private int p;
    private CollectAdapter adapter;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                list.remove(p);
                adapter.notifyDataSetChanged();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collcet);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_left_dark_x24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ListView listView = (ListView) findViewById(R.id.lv_collect);
        db = new DB(this);
        list = db.query("collect");
        adapter = new CollectAdapter(list, this);
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CollectActivity.this);
                p = position;
                builder.setMessage("确定删除吗？");
                builder.setPositiveButton("确定", CollectActivity.this).show();
                return true;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DBChuanInfo chuanInfo = (DBChuanInfo) list.get(position);
                Intent intent = new Intent(CollectActivity.this, ReplyActivity.class);
                intent.putExtra("id",chuanInfo.id);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (which == dialog.BUTTON_POSITIVE) {
            DBChuanInfo chuanInfo = (DBChuanInfo) list.get(p);
            db.delete("collect", chuanInfo.id);
            Message message = handler.obtainMessage();
            message.what = 0;
            handler.sendMessage(message);
        } else if (which == dialog.BUTTON_NEGATIVE) {

        }
        dialog.dismiss();
    }

}


