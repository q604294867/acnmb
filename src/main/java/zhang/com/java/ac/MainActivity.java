package zhang.com.java.ac;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.biao.pulltorefresh.OnRefreshListener;
import com.biao.pulltorefresh.PtrLayout;
import com.biao.pulltorefresh.header.DefaultRefreshView;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;

import zhang.com.java.ac.adapter.ChuanInfoAdapter;
import zhang.com.java.ac.bean.ChuanInfo;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==WHAT_SHOW_CHUAN) {
                swipe.setRefreshing(false);

                mlist = (ArrayList<ChuanInfo.Res>) msg.obj;
                chuanInfoAdapter =new ChuanInfoAdapter(mlist,getBaseContext());
                chuanRecycler.setAdapter(chuanInfoAdapter);
            }else if(msg.what==WHAT_LOAD_MORE) {
                mlist.addAll((ArrayList<ChuanInfo.Res>) msg.obj);
                chuanInfoAdapter.notifyDataSetChanged();

                ptrLayout.onRefreshComplete();
                isLoadMore=false;
            }else if (msg.what==WHAT_FAILED){
                if (mpage==1){
                    swipe.setRefreshing(false);
                    btReload.setVisibility(View.VISIBLE);
                }else {
                    mpage--;
                    Toast.makeText(MainActivity.this, "加载失败", Toast.LENGTH_LONG).show();
                }
            }
        }
    };
    public static final int WHAT_FAILED =0;
    public static final int WHAT_SHOW_CHUAN=1;
    public static final int WHAT_LOAD_MORE=2;
    private RecyclerView chuanRecycler = null;
    private SwipeRefreshLayout swipe = null;
    private PtrLayout ptrLayout = null;
    private int[] colors = new int[]{Color.RED, Color.GREEN, Color.BLUE};
    private int currentID;
    private int mpage=1;
    private boolean isLoadMore=false;
    private ArrayList<ChuanInfo.Res> mlist = null;
    private ChuanInfoAdapter chuanInfoAdapter = null;
    private Toolbar toolbar = null;
    public static MainActivity mactivity;
    private Button btReload ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mactivity =this ;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("综合版");
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        btReload= (Button) findViewById(R.id.bt_chuan_reload);


        swipe = (SwipeRefreshLayout) findViewById(R.id.swipe);
        swipe.setColorSchemeColors(colors);

        ptrLayout = (PtrLayout) findViewById(R.id.ptrRefresh);
        ptrLayout.setMode(PtrLayout.MODE_ONLY_CONTENT_NOT_MOVE);

        DefaultRefreshView footView = new DefaultRefreshView(ptrLayout.getContext());
        footView.setColorSchemeColors(colors);
        footView.setIsPullDown(false);

        ptrLayout.setFooterView(footView);

        chuanRecycler = (RecyclerView) findViewById(R.id.rv_chuan);
        chuanRecycler.setLayoutManager(new LinearLayoutManager(this));



        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshChuan(currentID, 1);
                mpage = 1;
                swipe.setRefreshing(true);
            }
        });

        ptrLayout.setOnPullUpRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadMoreChuan(currentID, mpage);
            }
        });
        
        showChuan(4, 1);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.aciton_refresh) {
            refreshChuan(currentID, 1);
            mpage = 1;
            swipe.setRefreshing(true);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_collcet) {
            startActivity(new Intent(this,CollectActivity.class));
        } else if (id == R.id.nav_rep_log) {
            startActivity(new Intent(this,RepLogActivity.class));
        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id==R.id.nav_zonghe) {
            showChuan(4, 1);
            toolbar.setTitle("综合版");

        } else if (id==R.id.nav_jishu) {
            showChuan(30, 1);
            toolbar.setTitle("技术讨论");
        } else if (id==R.id.nav_huanle) {
            showChuan(20,1);
            toolbar.setTitle("欢乐恶搞");
        } else if (id==R.id.nav_riji) {
            showChuan(89,1);
            toolbar.setTitle("日记");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void refreshChuan(int id, int page) {
        currentID=id;

        String path="http://h.nimingban.com/Api/showf/id/"+id+"/page/"+page;

        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, path, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                StringBuffer res = new StringBuffer(responseInfo.result);
                res.insert(0, "{\"res\":");
                res.insert(res.length(), "}");

                Gson gson = new Gson();
                ChuanInfo chuanInfo = gson.fromJson(res.toString(), ChuanInfo.class);
                ArrayList<ChuanInfo.Res> list = chuanInfo.res;
                Message message = null;
                if (isLoadMore == false) {
                    message = handler.obtainMessage(WHAT_SHOW_CHUAN, list);
                } else {
                    message = handler.obtainMessage(WHAT_LOAD_MORE, list);
                }
                handler.sendMessage(message);

            }

            @Override
            public void onFailure(HttpException e, String s) {
                Message message = handler.obtainMessage(WHAT_FAILED);
                handler.sendMessage(message);
            }
        });
    }

    private void showChuan(int id, int page){
        refreshChuan(id,page);
        swipe.setProgressViewOffset(false, 0,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24,
                        getResources().getDisplayMetrics()));

        swipe.setRefreshing(true);
    }
    private void loadMoreChuan(int id, int page){
        isLoadMore=true;
        mpage++;
        refreshChuan(id, mpage);
    }
    public void clickLoad(View v) {
        showChuan(currentID, 1);
        btReload.setVisibility(View.GONE);
    }

}
