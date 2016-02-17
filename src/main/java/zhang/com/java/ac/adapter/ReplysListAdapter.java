package zhang.com.java.ac.adapter;

import android.graphics.Color;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;

import java.util.ArrayList;

import zhang.com.java.ac.R;
import zhang.com.java.ac.ReplyActivity;
import zhang.com.java.ac.chuan.ReplysInfo;
import zhang.com.java.ac.utils.StringUtils;

/**
 * Created by win0真垃圾 on 2016/2/15.
 */
public class ReplysListAdapter extends BaseAdapter {
    private ArrayList<ReplysInfo.OneReply> list;
    private BitmapUtils utils ;
    private String upName;
    public ReplysListAdapter (ArrayList<ReplysInfo.OneReply> list,String upName) {
        this.list = list;
        this.upName =upName;
        utils=new BitmapUtils(ReplyActivity.activity);
        utils.configDefaultLoadFailedImage(R.drawable.image_failed);
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
        System.out.println("position"+position);
        ViewHolder holder;
        if(convertView ==null) {
            convertView =View.inflate(ReplyActivity.activity, R.layout.item_replys,null);
            holder = new ViewHolder();
            holder.tv_replys_content = (TextView) convertView.findViewById(R.id.tv_replys_content);
            holder.tv_replys_id= (TextView) convertView.findViewById(R.id.tv_replys_id);
            holder.tv_replys_now= (TextView) convertView.findViewById(R.id.tv_replys_now);
            holder.tv_replys_name= (TextView) convertView.findViewById(R.id.tv_replys_name);
            holder.iv_replys= (ImageView) convertView.findViewById(R.id.iv_replys);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        ReplysInfo.OneReply oneReply = list.get(position);

        if (oneReply.userid.equals(upName)) {
            holder.tv_replys_name.setTextColor(Color.parseColor("#006000"));
        }else if (oneReply.admin.equals("1")) {
            holder.tv_replys_name.setTextColor(Color.parseColor("#FF0000"));
        }else {
            holder.tv_replys_name.setTextColor(Color.parseColor("#9D9D9D"));
        }

        holder.tv_replys_name.setText(oneReply.userid);
        holder.tv_replys_now.setText(StringUtils.handlerDate(oneReply.now));
        holder.tv_replys_id.setText("NO."+oneReply.id);
        holder.tv_replys_content.setText(Html.fromHtml(oneReply.content));


        if (!oneReply.img.equals("")) {
            utils.configDefaultLoadingImage(R.drawable.image_loding);
            utils.display(holder.iv_replys, "http://cdn.ovear.info:8998/thumb/" + oneReply.img + oneReply.ext);
        } else {
            holder.iv_replys.setImageResource(R.drawable.image_null);
        }

        return convertView;
    }
    static  class ViewHolder {
        public TextView tv_replys_name;
        public TextView tv_replys_id;
        public TextView tv_replys_now;
        public TextView tv_replys_content;
        public ImageView iv_replys;
    }
}
