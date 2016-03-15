package zhang.com.java.ac.CookieUtils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.util.PreferencesCookieStore;

import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;


/**
 * Created by win0真垃圾 on 2016/3/5.
 */
public class GetCookie {

    public static void getCookieFromNet(final Context context, final AlertDialog dialog) {
        SharedPreferences sp =context.getSharedPreferences("cookieInfo",context.MODE_PRIVATE);
        final SharedPreferences.Editor edit = sp.edit();

        String cookieUrl="http://h.nimingban.com/Api/getCookie";
        final HttpUtils utils = new HttpUtils();
        utils.send(HttpRequest.HttpMethod.GET, cookieUrl, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (responseInfo.result.equals("error")) {
                    dialog.dismiss();
                    AlertDialog.Builder builder =new AlertDialog.Builder(context);
                    builder.setMessage("获取饼干失败").show();
                }else {
                    Toast.makeText(context, "获取饼干成功", Toast.LENGTH_SHORT).show();
                    DefaultHttpClient client = (DefaultHttpClient) utils.getHttpClient();
                    Cookie cookie = client.getCookieStore().getCookies().get(0);
                    edit.putString("cookieName",cookie.getName());
                    edit.putString("cookieValue",cookie.getValue());
                    edit.commit();
                    dialog.dismiss();
                }

            }

            @Override
            public void onFailure(HttpException e, String s) {
                System.out.println("获取cookie连接失败");
                System.out.println(e.getMessage());
            }
        });
    }
    public static PreferencesCookieStore getCookieFromSP(Context context) {
        SharedPreferences sp =context.getSharedPreferences("cookieInfo", context.MODE_PRIVATE);
        String cookieName = sp.getString("cookieName", "");
        String cookieValue = sp.getString("cookieValue", "");

        BasicClientCookie cookie = new BasicClientCookie(cookieName, cookieValue);
        cookie.setPath("/");
        cookie.setDomain("h.nimingban.com");
        cookie.setVersion(0);

        PreferencesCookieStore cookieStore = new PreferencesCookieStore(context);
        cookieStore.addCookie(cookie);
        return cookieStore;
    }
}
