package zhang.com.java.ac.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;

import com.lidroid.xutils.BitmapUtils;

import java.util.ArrayList;

import zhang.com.java.ac.MainActivity;
import zhang.com.java.ac.R;
import zhang.com.java.ac.ReplyActivity;
import zhang.com.java.ac.chuan.ChuanInfo;
import zhang.com.java.ac.utils.StringUtils;
import zhang.com.java.ac.viewholder.ChuanInfoViewHolder;

/**
 * Created by win0真垃圾 on 2016/2/12.
 */
public class ChuanInfoAdapter extends RecyclerView.Adapter<ChuanInfoViewHolder> {
    private ArrayList<ChuanInfo.Res>  chuanInfoList = null;
    private Context context=null;
    private BitmapUtils utils = null;

    public ChuanInfoAdapter(ArrayList<ChuanInfo.Res> chuanInfoList,Context context) {
        this.context=context;
        this.chuanInfoList=chuanInfoList;
        utils =new BitmapUtils(MainActivity.mactivity);
        utils.configDefaultLoadFailedImage(R.drawable.image_failed);
    }

    @Override
    public ChuanInfoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_chuan, null);
        ChuanInfoViewHolder holder = new ChuanInfoViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ChuanInfoViewHolder holder, int position) {
        final ChuanInfo.Res info = chuanInfoList.get(position);
        holder.tv_chuan_replyCount.setText("回复:"+info.replyCount);
        holder.tv_chuan_userid.setText(info.userid);
        holder.tv_chuan_now.setText(StringUtils.handlerDate(info.now));
        holder.tv_chuan_content.setText(Html.fromHtml(info.content));
        holder.tv_chuan_content.setMaxLines(8);
        holder.rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.mactivity, ReplyActivity.class);
                intent.putExtra("id",info.id);
                MainActivity.mactivity.startActivity(intent);
            }
        });
        if (info.admin.equals("1")) {
            holder.tv_chuan_userid.setTextColor(Color.parseColor("#FF0000"));
        }else {
            holder.tv_chuan_userid.setTextColor(Color.parseColor("#9D9D9D"));
        }
        if (!info.img.equals("")) {
            utils.configDefaultLoadingImage(R.drawable.image_loding);
            utils.display(holder.iv_chuan_img, "http://cdn.ovear.info:8998/thumb/" + info.img + info.ext);
        }else {
            holder.iv_chuan_img.setImageResource(R.drawable.image_null);
        }


    }

    @Override
    public int getItemCount() {
        return chuanInfoList.size();
    }
}
