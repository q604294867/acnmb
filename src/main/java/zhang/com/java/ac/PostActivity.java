package zhang.com.java.ac;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import zhang.com.java.ac.CookieUtils.GetCookie;
import zhang.com.java.ac.utils.StringUtils;

public class PostActivity extends AppCompatActivity {
    private SharedPreferences sp;
    private SharedPreferences.Editor edit;
    private EditText et;
    private String imgPath="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("回复");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_left_dark_x24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        sp = getSharedPreferences("cookieInfo",this.MODE_PRIVATE);

        et = (EditText) findViewById(R.id.et_post);
        boolean flag = getIntent().getBooleanExtra("isHaveID",false);
        if (flag) {
            String id =">>No."+ getIntent().getStringExtra("id");
            et.setText(id);
            et.setSelection(id.length());
        }
    }
    public void post(View v) {
        String cookieName = sp.getString("cookieName", "");
        String cookieValue = sp.getString("cookieValue", "");
        if (TextUtils.isEmpty(cookieName)) {
            AlertDialog.Builder builder =new AlertDialog.Builder(this);
            AlertDialog dialog = builder.setView(R.layout.dialog_get_cookie).show();
            GetCookie.getCookieFromNet(this, dialog);

        } else {
            String content = et.getText().toString();
            Intent intent =new Intent();
            intent.putExtra("replyContent",content);
            intent.putExtra("imgPath",imgPath);
            setResult(RESULT_OK, intent);
            finish();
        }
    }
    public void openImage(View v) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode!=RESULT_OK) {
            return;
        }
        if (data!=null) {
            imgPath = StringUtils.getImageAbsolutePath(this, data.getData());
            Toast.makeText(PostActivity.this, "已选中一张图片", Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
