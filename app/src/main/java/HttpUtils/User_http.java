package HttpUtils;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

import SQLite.MySQLiteImpl;
import utils.UrlPath;

/**
 * Created by zhengheming on 2016/1/22.
 */
public class User_http {
    AsyncHttpClient Client;
    String url;
    public User_http(String number) {
        Client = new AsyncHttpClient();
        url = UrlPath.ReceUserData + "?number="+number;
    }

    public void AsyncHttpService() {
        Client.get(url, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {

                String res = new String(bytes);
                try {
                    JSONObject jsonObject = new JSONObject(res);
                    JSONObject item = jsonObject.getJSONObject("User_data");
                    String userclassify = item.getString("userclassify");
                    String username = item.getString("username");
                    String usernumber = item.getString("usernumber");
                    String userportrait = item.getString("userportrait");
                    String userschool = item.getString("userschool");
                    String usersex = item.getString("usersex");
                    new MySQLiteImpl().addUserData(usernumber,username,userportrait,usersex,userclassify,userschool);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

            }
        });
    }
}
