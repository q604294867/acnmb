package zhang.com.java.ac;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.biao.pulltorefresh.OnRefreshListener;
import com.biao.pulltorefresh.PtrLayout;
import com.biao.pulltorefresh.header.DefaultRefreshView;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.rey.material.widget.ProgressView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;
import zhang.com.java.ac.CookieUtils.GetCookie;
import zhang.com.java.ac.DB.DB;
import zhang.com.java.ac.adapter.ReplysListAdapter;
import zhang.com.java.ac.bean.ReplysInfo;

public class ReplyActivity extends SwipeBackActivity implements AdapterView.OnItemLongClickListener {

    private ListView listView = null;
    private ReplysListAdapter listAdapter;
    private int mPage = 0;
    public static ReplyActivity activity =null;
    private PtrLayout ptrLayout = null;
    private int[] colors = new int[]{Color.RED, Color.GREEN, Color.BLUE};
    private SwipeBackLayout mSwipeBackLayout = null;
    private ArrayList<ReplysInfo.OneReply> lists =new ArrayList<ReplysInfo.OneReply>();
    private String upName = null;
    private ProgressView pb;
    private String[] dialogItem = {"复制内容","回复内容","举报内容"};
    private static final int REPLY=0;
    private static final int REPLY_ID=1;
    private boolean isFristEntry =true;
    private boolean flag = true;
    private ArrayList<ReplysInfo.OneReply> lastReplys;
    private Button btReload;

    Handler handler =new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            pb.stop();
            ReplysInfo replysInfo = (ReplysInfo) msg.obj;
            ptrLayout.onRefreshComplete();
            if (msg.what == 1) {
                int replyCount = replysInfo.replys.size();
                if (mPage == 1 && isFristEntry) {
                    isFristEntry=false;
                    ReplysInfo.OneReply PoContent = new ReplysInfo.OneReply();
                    PoContent.admin = replysInfo.admin;
                    PoContent.content = replysInfo.content;
                    PoContent.ext = replysInfo.ext;
                    PoContent.id = replysInfo.id;
                    PoContent.img = replysInfo.img;
                    PoContent.name = replysInfo.name;
                    PoContent.now = replysInfo.now;
                    PoContent.sage = replysInfo.sage;
                    PoContent.title = replysInfo.title;
                    PoContent.userid = replysInfo.userid;

                    lists.add(0, PoContent);
                    lists.addAll(replysInfo.replys);
                    listAdapter = new ReplysListAdapter(lists,upName);
                    listView.setAdapter(listAdapter);
                    if (replyCount<19) {
                        mPage--;
                        flag=false;
                    }
                }else {
                    if (replyCount < 19) {
                        mPage--;
                        if (flag) {
                            //第一次进入不足一页
                            lists.addAll(replysInfo.replys);
                            flag=false;
                        }else {
                            List<ReplysInfo.OneReply> newReplys = replysInfo.replys.subList(
                                    lastReplys.size(), replysInfo.replys.size());
                            lists.addAll(newReplys);
                        }

                    } else {
                        lists.addAll(replysInfo.replys);
                    }
                }
                listAdapter.notifyDataSetChanged();
                lastReplys=replysInfo.replys;
            } else if (msg.what==2) {
                mPage--;
                if (isFristEntry) {
                    btReload.setVisibility(View.VISIBLE);
                }else {
                    Toast.makeText(ReplyActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply);
        activity =this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.reply_toolbar);
        Intent intent = getIntent();
        final String id = intent.getStringExtra("id");
        toolbar.setTitle("NO." + id);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_left_dark_x24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btReload = (Button) findViewById(R.id.bt_reply);
        pb = (ProgressView) findViewById(R.id.pb_reply);
        pb.start();

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
        listView.setOnItemLongClickListener(this);

        showReplys(Integer.valueOf(id), mPage);

    }

    private  void showReplys(int no,int page) {

        mPage++;
        String path ="http://h.nimingban.com/Api/thread/id/"+no+"/page/"+mPage;
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.configCurrentHttpCacheExpiry(0);
        httpUtils.configDefaultHttpCacheExpiry(0);
        httpUtils.send(HttpRequest.HttpMethod.GET, path, new RequestCallBack<String>() {
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
                Message message = handler.obtainMessage(2);
                handler.sendMessage(message);
            }
        });
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ReplyActivity.this);
        final ReplysInfo.OneReply oneReply = lists.get(position);
        builder.setItems(dialogItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    clipboardManager.setText(oneReply.content.trim());
                    Toast.makeText(ReplyActivity.this, "复制成功", Toast.LENGTH_SHORT).show();
                } else if (which == 1) {
                    String id = oneReply.id;
                    Intent intent = new Intent(ReplyActivity.this, PostActivity.class);
                    intent.putExtra("id", id);
                    intent.putExtra("isHaveID", true);
                    startActivityForResult(intent, REPLY_ID);
                } else {
                }
            }
        }).show();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_reply, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id==R.id.action_reply) {
            Intent intent = new Intent(this, PostActivity.class);
            intent.putExtra("flag", false);
            startActivityForResult(intent, REPLY);
            return true;

        }else if (id==R.id.action_collection) {
            ReplysInfo.OneReply reply = lists.get(0);
            DB db = new DB(ReplyActivity.activity);
            boolean flag = db.add("collect", getIntent().getStringExtra("id"), reply.content);
            if (flag) {
                Toast.makeText(ReplyActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(ReplyActivity.this, "添加失败", Toast.LENGTH_SHORT).show();
            }

        }else if (id==R.id.action_open_in_browser) {
            Intent intent = new Intent("android.intent.action.VIEW");
            Uri uri = Uri.parse("http://h.nimingban.com/t/" + getIntent().getStringExtra("id"));
            intent.setData(uri);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String url="http://h.nimingban.com/Home/Forum/doReplyThread.html";
        if (resultCode==RESULT_OK) {
            final String content = data.getStringExtra("replyContent");
            final String replyID = getIntent().getStringExtra("id");
            String imgPath = data.getStringExtra("imgPath");

            HttpUtils utils = new HttpUtils();
            utils.configCookieStore(GetCookie.getCookieFromSP(this));

            RequestParams params = new RequestParams();

            params.addBodyParameter("content",content);
            params.addBodyParameter("resto",replyID);
            if (!imgPath.equals("")) {
                int index = imgPath.lastIndexOf(".");
                String type = imgPath.substring(index + 1);
                params.addBodyParameter("image",new File(imgPath),"image/"+type);
            }

            utils.send(HttpRequest.HttpMethod.POST, url,params, new RequestCallBack<String>() {
                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    String result = responseInfo.result;
                    ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                    if (result.contains("回复成功")) {
                        Toast.makeText(ReplyActivity.this, "回复成功", Toast.LENGTH_SHORT).show();
                        DB db = new DB(ReplyActivity.this);
                        db.add("postLog", replyID, content);
                    }else if (result.contains("含有非法词语")) {
                        Toast.makeText(ReplyActivity.this, "含有非法词语,回复内容以复制", Toast.LENGTH_SHORT).show();
                        clipboardManager.setText(content);
                    }else {
                        Toast.makeText(ReplyActivity.this, "未知错误,回复内容以复制", Toast.LENGTH_SHORT).show();
                        clipboardManager.setText(content);
                    }
                }
                @Override
                public void onFailure(HttpException e, String s) {
                    Toast.makeText(ReplyActivity.this, "回复失败,回复内容以存置草稿", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    public void clickLoad(View v) {
        btReload.setVisibility(View.GONE);
        showReplys(Integer.valueOf(getIntent().getStringExtra("id")), mPage);
    }
}
