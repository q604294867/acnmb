package zhang.com.java.ac.adapter;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import zhang.com.java.ac.ImageActivity;
import zhang.com.java.ac.R;
import zhang.com.java.ac.ReplyActivity;
import zhang.com.java.ac.bean.ReplysInfo;
import zhang.com.java.ac.utils.DpPxSpTransformUtil;
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

        final ReplysInfo.OneReply oneReply = list.get(position);

        if (oneReply.admin.equals("1")) {
            holder.tv_replys_name.setTextColor(Color.parseColor("#FF0000"));
        }else if ( oneReply.userid.equals(upName)) {
            holder.tv_replys_name.setTextColor(Color.parseColor("#006000"));
        }else {
            holder.tv_replys_name.setTextColor(Color.parseColor("#9D9D9D"));
        }

        holder.tv_replys_name.setText(oneReply.userid);
        holder.tv_replys_now.setText(StringUtils.handlerDate(oneReply.now));
        holder.tv_replys_id.setText("NO."+oneReply.id);
      //  holder.tv_replys_content.setText(Html.fromHtml(oneReply.content));
        setSpan(holder.tv_replys_content,oneReply);


        if (!oneReply.img.equals("")) {
            holder.iv_replys.setPadding(0, DpPxSpTransformUtil.dip2px(10f),0,DpPxSpTransformUtil.dip2px(10f));
            utils.configDefaultLoadingImage(R.drawable.image_loding);
            utils.display(holder.iv_replys, "http://cdn.ovear.info:8998/thumb/" + oneReply.img + oneReply.ext);
        } else {
            holder.iv_replys.setPadding(0,0,0,0);
            holder.iv_replys.setImageResource(R.drawable.image_null);
        }
        holder.iv_replys.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReplyActivity.activity, ImageActivity.class);
                intent.putExtra("url",oneReply.img+oneReply.ext);
                ReplyActivity.activity.startActivity(intent);
            }
        });
        return convertView;
    }
    private void setSpan(TextView tv,ReplysInfo.OneReply info) {
        String handlerContent = StringUtils.handlerContent(info.content);
        Pattern pattern = Pattern.compile(">>No.[0-9]+");
        Matcher matcher = pattern.matcher(handlerContent);

        if (matcher.find()) {
            String contentId =handlerContent.substring(matcher.start()+5, matcher.end());
            tv.setMovementMethod(LinkMovementMethod.getInstance());
            tv.setHighlightColor(Color.WHITE);
            SpannableString spannableString = new SpannableString(handlerContent);
            spannableString.setSpan(new MySpan(info,contentId,list),matcher.start(),matcher.end(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            tv.setText(spannableString);
        }else {
            tv.setText(Html.fromHtml(info.content));
        }
    }
    class MySpan extends ClickableSpan {
        private ArrayList<ReplysInfo.OneReply> list;
        private ReplysInfo.OneReply info;
        private String contentId;
        private MySpan(ReplysInfo.OneReply info,String id,ArrayList<ReplysInfo.OneReply> list) {
            this.info=info;
            contentId=id;
            this.list=list;
        }

        @Override
        public void onClick(View widget) {
            ArrayList<String> idList = new ArrayList<>();
            for (int i=0;i<list.size();i++) {
                String id = list.get(i).id;
                idList.add(id);
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(ReplyActivity.activity);
            if (idList.contains(contentId)) {
                ReplysInfo.OneReply reply = list.get(idList.indexOf(contentId));
                View view = View.inflate(ReplyActivity.activity, R.layout.item_replys, null);
                TextView id = (TextView) view.findViewById(R.id.tv_replys_id);
                TextView name = (TextView) view.findViewById(R.id.tv_replys_name);
                TextView content = (TextView) view.findViewById(R.id.tv_replys_content);
                TextView now = (TextView) view.findViewById(R.id.tv_replys_now);
                id.setText(reply.id);
                name.setText(reply.userid);
                content.setText(Html.fromHtml(reply.content));
                now.setText(StringUtils.handlerDate(reply.now));
                builder.setView(view,0,0,0,0);
                builder.show();
            }else {
                builder.setMessage("不是本串内容").show();
            }
        }
    }

    static  class ViewHolder {
        public TextView tv_replys_name;
        public TextView tv_replys_id;
        public TextView tv_replys_now;
        public TextView tv_replys_content;
        public ImageView iv_replys;
    }
}
