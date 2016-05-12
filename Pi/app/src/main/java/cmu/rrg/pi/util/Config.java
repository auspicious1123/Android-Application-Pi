package cmu.rrg.pi.util;

import java.util.Date;

import org.json.JSONException;

import android.text.format.DateFormat;
import android.util.Log;

import cmu.rrg.pi.exception.AuthException;
import cmu.rrg.pi.util.auth.token.Mac;
import cmu.rrg.pi.util.auth.token.PutPolicy;

public class Config {
    public static String ACCESS_KEY = "D-I0UM5B_N4bB82K7KeikBB8OgE23dh8630qlWva";
    public static String SECRET_KEY = "1wOSvP1XmLDaM9jU3aWS0DVzUn5p1xvVLrE63sHU";
    // 指定上传所用的七牛空间名称
    public static String BUCKET_NAME = "pipicture";

    public static final String USER_AGENT = "qiniu android-sdk v7.1.0";
    public static final String UP_HOST = "http://up.qiniu.com";

    // get qiniu upload token
    public static String getToken() {
        String uptoken;
        Mac mac = new Mac(Config.ACCESS_KEY, Config.SECRET_KEY);
        PutPolicy putPolicy = new PutPolicy(Config.BUCKET_NAME);
        try {
            uptoken = putPolicy.token(mac);
            Log.e("qiniu uptoken:", uptoken);
            return uptoken;
        } catch (AuthException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getUploadFileName() {
        DateFormat dateformat = new DateFormat();
        String today = dateformat.format("yyyy-MM-dd hh:mm:ss", new Date()).toString();
        return today;
    }
}