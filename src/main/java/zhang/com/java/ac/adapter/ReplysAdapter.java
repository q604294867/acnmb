package zhang.com.java.ac.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import zhang.com.java.ac.R;
import zhang.com.java.ac.chuan.ReplysInfo;
import zhang.com.java.ac.utils.StringUtils;
import zhang.com.java.ac.viewholder.ReplysInfoViewHolder;

/**
 * Created by win0真垃圾 on 2016/2/14.
 */
public class ReplysAdapter extends RecyclerView.Adapter<ReplysInfoViewHolder> {
    private ArrayList<ReplysInfo.OneReply> lists;
    public ReplysAdapter (ArrayList<ReplysInfo.OneReply> lists){
        this.lists=lists;
    }
    @Override
    public ReplysInfoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ReplysInfoViewHolder holder = new ReplysInfoViewHolder(View.inflate(parent.getContext(), R.layout.item_replys, null));
        return holder;
    }

    @Override
    public void onBindViewHolder(ReplysInfoViewHolder holder, int position) {
        ReplysInfo.OneReply oneReply = lists.get(position);
        holder.tv_replys_name.setText(oneReply.userid);
        holder.tv_replys_now.setText(StringUtils.handlerDate(oneReply.now));
        holder.tv_replys_id.setText(oneReply.id);
        holder.tv_replys_content.setText(Html.fromHtml(oneReply.content));
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }
}
