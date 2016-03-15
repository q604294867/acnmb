package zhang.com.java.ac.adapter;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import zhang.com.java.ac.R;
import zhang.com.java.ac.bean.DBChuanInfo;

/**
 * Created by win0真垃圾 on 2016/3/11.
 */
public class CollectAdapter extends BaseAdapter {
    private ArrayList<DBChuanInfo> list;
    private Context context;
    public CollectAdapter(ArrayList<DBChuanInfo> list, Context context) {
        this.list=list;
        this.context=context;
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
            convertView = View.inflate(context, R.layout.item_collect, null);
            holder.id = (TextView) convertView.findViewById(R.id.tv_collect_id);
            holder.content= (TextView) convertView.findViewById(R.id.tv_collect_content);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
        DBChuanInfo chuanInfo = list.get(position);
        holder.id.setText("NO."+chuanInfo.id);
        holder.content.setText(Html.fromHtml(chuanInfo.content));
        return convertView;
    }
}
 class ViewHolder {
    public TextView id;
    public TextView content;
}