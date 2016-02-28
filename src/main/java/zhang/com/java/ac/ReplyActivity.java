package zhang.com.java.ac;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.biao.pulltorefresh.OnRefreshListener;
import com.biao.pulltorefresh.PtrLayout;
import com.biao.pulltorefresh.header.DefaultRefreshView;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;
import zhang.com.java.ac.adapter.ReplysListAdapter;
import zhang.com.java.ac.chuan.ReplysInfo;

public class ReplyActivity extends SwipeBackActivity {

    private ListView listView = null;
    private ReplysListAdapter listAdapter;
    private int mPage = 0;
    public static ReplyActivity activity =null;
    private PtrLayout ptrLayout = null;
    private int[] colors = new int[]{Color.RED, Color.GREEN, Color.BLUE};
    private SwipeBackLayout mSwipeBackLayout = null;
    private View emptyView =null;
    private ArrayList<ReplysInfo.OneReply> lists =new ArrayList<ReplysInfo.OneReply>();
    private String upName = null;
    private ProgressBar pb;

    Handler handler =new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            pb.setVisibility(View.GONE);
            ReplysInfo replysInfo = (ReplysInfo) msg.obj;
            ptrLayout.onRefreshComplete();
            if (msg.what == 1) {
                if (mPage == 1) {
                    ReplysInfo.OneReply oneReply = new ReplysInfo.OneReply();
                    oneReply.admin = replysInfo.admin;
                    oneReply.content = replysInfo.content;
                    oneReply.ext = replysInfo.ext;
                    oneReply.id = replysInfo.id;
                    oneReply.img = replysInfo.img;
                    oneReply.name = replysInfo.name;
                    oneReply.now = replysInfo.now;
                    oneReply.sage = replysInfo.sage;
                    oneReply.title = replysInfo.title;
                    oneReply.userid = replysInfo.userid;

                    lists.add(0, oneReply);
                    lists.addAll(replysInfo.replys);
                    listAdapter = new ReplysListAdapter(lists,upName);
                    System.out.println(upName);
                    listView.setAdapter(listAdapter);
                } else {
                    lists.addAll(replysInfo.replys);
                    listAdapter.notifyDataSetChanged();
                }
                System.out.println("mpage=" + mPage);
            } else if (msg.what==2) {
                listView.setEmptyView(emptyView);
            }
        }

    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply);
        activity =this;
        Intent intent = getIntent();
        final String id = intent.getStringExtra("id");

        pb = (ProgressBar) findViewById(R.id.pb_reply);

        mSwipeBackLayout = getSwipeBackLayout();
        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);

        ptrLayout = (PtrLayout) findViewById(R.id.reply_ptrRefresh);
        ptrLayout.setMode(PtrLayout.MODE_ONLY_CONTENT_NOT_MOVE);
        DefaultRefreshView footView = new DefaultRefreshView(ptrLayout.getContext());
        footView.setColorSchemeColors(colors);
        footView.setIsPullDown(false);
        ptrLayout.setFooterView(footView);

        ptrLayout.setOnPullUpRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                showReplys(Integer.valueOf(id), mPage);
            }
        });

        listView = (ListView) findViewById(R.id.test_lv);

        emptyView = findViewById(R.id.view_empty);


        showReplys(Integer.valueOf(id), mPage);

    }

    private  void showReplys(int no,int page) {

        mPage++;
        String path ="http://h.nimingban.com/Api/thread/id/"+no+"/page/"+mPage;
        HttpUtils httpUtils = new HttpUtils();
        HttpHandler<String> send = httpUtils.send(HttpRequest.HttpMethod.GET, path, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Gson gson = new Gson();
                ReplysInfo replysInfo = gson.fromJson(responseInfo.result, ReplysInfo.class);
                upName = replysInfo.userid;
                Message message = handler.obtainMessage(1, replysInfo);
                handler.sendMessage(message);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Message message =handler.obtainMessage(2);
                handler.sendMessage(message);
            }
        });
    }
}
