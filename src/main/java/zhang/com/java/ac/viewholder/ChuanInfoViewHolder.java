package zhang.com.java.ac.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import zhang.com.java.ac.R;

/**
 * Created by win0真垃圾 on 2016/2/12.
 */
public class ChuanInfoViewHolder extends RecyclerView.ViewHolder{
    public TextView tv_chuan_replyCount;
    public TextView tv_chuan_userid;
    public TextView tv_chuan_now;
    public TextView tv_chuan_content;
    public ImageView iv_chuan_img;
    public RelativeLayout rootLayout;

    public ChuanInfoViewHolder(View itemView) {
        super(itemView);
        tv_chuan_replyCount = (TextView) itemView.findViewById(R.id.tv_chuan_replyCount);
        tv_chuan_userid =(TextView)itemView.findViewById(R.id.tv_chuan_name);
        tv_chuan_now=(TextView)itemView.findViewById(R.id.tv_chuan_now);
        tv_chuan_content=(TextView)itemView.findViewById(R.id.tv_chuan_content);
        iv_chuan_img=(ImageView)itemView.findViewById(R.id.iv_chuan_img);
        rootLayout=(RelativeLayout)itemView.findViewById(R.id.rootLayout);

    }
}
