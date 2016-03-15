package zhang.com.java.ac.adapter;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

import zhang.com.java.ac.R;
import zhang.com.java.ac.bean.DBReplyInfo;

/**
 * Created by win0真垃圾 on 2016/3/12.
 */
public class ReplyLogAdapter extends BaseAdapter {
    private ArrayList list;
    private Context context;
    public ReplyLogAdapter(ArrayList list,Context context) {
        this.list=list;
        this.context=context;
        Collections.reverse(list);
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView==null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_reply_log, null);
            holder.id = (TextView) convertView.findViewById(R.id.tv_log_id);
            holder.content= (TextView) convertView.findViewById(R.id.tv_log_content);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
        DBReplyInfo replyInfo =(DBReplyInfo) list.get(position);
        holder.id.setText("NO."+replyInfo.id);
        holder.content.setText(Html.fromHtml(replyInfo.content));
        return convertView;
    }
    static class ViewHolder {
        public TextView id;
        public TextView content;
    }
}
