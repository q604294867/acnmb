package zhang.com.java.ac;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;
import uk.co.senab.photoview.PhotoViewAttacher;

public class ImageActivity extends SwipeBackActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        String url= "http://cdn.ovear.info:8998/image/" +intent.getStringExtra("url");
        ImageView imageView = (ImageView) findViewById(R.id.iv_big_image);
        final ProgressBar pb = (ProgressBar) findViewById(R.id.pb_image);
        BitmapUtils utils  = new BitmapUtils(this);
        utils.configDefaultLoadFailedImage(R.drawable.image_failed);

        utils.display(imageView, url, new BitmapLoadCallBack<ImageView>() {
            @Override
            public void onLoadCompleted(ImageView imageView, String s, Bitmap bitmap, BitmapDisplayConfig bitmapDisplayConfig, BitmapLoadFrom bitmapLoadFrom) {
                pb.setVisibility(View.GONE);
                imageView.setImageBitmap(bitmap);
            }

            @Override
            public void onLoadFailed(ImageView imageView, String s, Drawable drawable) {

            }
        });


        new PhotoViewAttacher(imageView);




    }

}
