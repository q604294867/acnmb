package zhang.com.java.ac.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import zhang.com.java.ac.R;

/**
 * Created by win0真垃圾 on 2016/2/14.
 */
public class ReplysInfoViewHolder extends RecyclerView.ViewHolder {
    public TextView tv_replys_name;
    public TextView tv_replys_id;
    public TextView tv_replys_now;
    public TextView tv_replys_content;
    public ImageView iv_replys;
    public ReplysInfoViewHolder(View itemView) {
        super(itemView);
        tv_replys_content = (TextView) itemView.findViewById(R.id.tv_replys_content);
        tv_replys_id= (TextView) itemView.findViewById(R.id.tv_replys_id);
        tv_replys_now= (TextView) itemView.findViewById(R.id.tv_replys_now);
        tv_replys_name= (TextView) itemView.findViewById(R.id.tv_replys_name);
        iv_replys= (ImageView) itemView.findViewById(R.id.iv_replys);
    }
}
