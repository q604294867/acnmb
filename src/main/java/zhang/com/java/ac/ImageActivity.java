package zhang.com.java.ac;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.rey.material.widget.ProgressView;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;
import uk.co.senab.photoview.PhotoViewAttacher;

public class ImageActivity extends SwipeBackActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_left_dark_x24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();
        final String url= "http://cdn.ovear.info:8998/image/" +intent.getStringExtra("url");
        final ImageView imageView = (ImageView) findViewById(R.id.iv_big_image);
        final ProgressView pb = (ProgressView) findViewById(R.id.pb_image);
        final Button btReload = (Button) findViewById(R.id.bt_image_reload);
        pb.start();


        loadImage(pb, imageView, btReload, url);
        /*
        BitmapUtils utils  = new BitmapUtils(this);
        utils.configDefaultLoadFailedImage(R.drawable.image_failed);

        utils.display(imageView, url, new BitmapLoadCallBack<ImageView>() {
            @Override
            public void onLoadCompleted(ImageView imageView, String s, Bitmap bitmap, BitmapDisplayConfig bitmapDisplayConfig, BitmapLoadFrom bitmapLoadFrom) {
                pb.stop();
                imageView.setImageBitmap(bitmap);
            }

            @Override
            public void onLoadFailed(ImageView imageView, String s, Drawable drawable) {
                imageView.setImageResource(R.drawable.image_failed);
                pb.stop();
                btReload.setVisibility(View.VISIBLE);
            }
        });
        */
        btReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pb.start();
                btReload.setVisibility(View.GONE);
                loadImage(pb,imageView,btReload,url);
            }
        });
    }

    private void loadImage(final ProgressView pb,ImageView imageView, final Button btReload,String url) {
        BitmapUtils utils  = new BitmapUtils(this);

        utils.display(imageView, url, new BitmapLoadCallBack<ImageView>() {
            @Override
            public void onLoadCompleted(ImageView imageView, String s, Bitmap bitmap, BitmapDisplayConfig bitmapDisplayConfig, BitmapLoadFrom bitmapLoadFrom) {
                pb.stop();
                imageView.setImageBitmap(bitmap);
            }

            @Override
            public void onLoadFailed(ImageView imageView, String s, Drawable drawable) {
                pb.stop();
                btReload.setVisibility(View.VISIBLE);
            }
        });
        new PhotoViewAttacher(imageView);
    }
}
