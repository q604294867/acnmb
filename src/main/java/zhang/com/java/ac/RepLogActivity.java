package zhang.com.java.ac;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;
import zhang.com.java.ac.DB.DB;
import zhang.com.java.ac.adapter.ReplyLogAdapter;
import zhang.com.java.ac.bean.DBReplyInfo;

public class RepLogActivity extends SwipeBackActivity {
    private ArrayList list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rep_log);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_left_dark_x24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ListView listView = (ListView) findViewById(R.id.lv_reply_log);
        DB db = new DB(this);
        list = db.query("postLog");
        ReplyLogAdapter adapter = new ReplyLogAdapter(list, this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DBReplyInfo replyInfo = (DBReplyInfo) list.get(position);
                Intent intent = new Intent(RepLogActivity.this, ReplyActivity.class);
                intent.putExtra("id",replyInfo.id);
                startActivity(intent);
            }
        });
    }
}
