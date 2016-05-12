package cmu.rrg.pi.model;

import android.content.Intent;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import cmu.rrg.pi.ui.LoginActitity;
import cmu.rrg.pi.ui.MeetupListActivity;
import cmu.rrg.pi.ui.RegisterActivity;

/**
 * Created by Yang on 4/30/16.
 */
public class UserADO {


    private static String urlhead = "http://128.237.212.2:8080/Pi-Server";

    //register the user to server
    public static int register(User user) {
        int statusCode = 0;
        try {
            URL url = new URL(urlhead + "/signup");
            String params;
            StringBuilder sBuilder = new StringBuilder();
            Log.i("user", user.getUsername());
            sBuilder.append("username=" + URLEncoder.encode(user.getUsername(), "UTF-8") + "&");
            sBuilder.append("password=" + URLEncoder.encode(user.getPassword(), "UTF-8") + "&");
            sBuilder.append("name=" + URLEncoder.encode(user.getName(), "UTF-8") + "&");
            sBuilder.append("phone=" + URLEncoder.encode(user.getPhoneNumber(), "UTF-8") + "&");
            sBuilder.append("avator=" + URLEncoder.encode("1", "UTF-8"));
            byte[] data = sBuilder.toString().getBytes("UTF-8");
            params = sBuilder.toString();


            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoOutput(true); //如果要输出，则必须加上此句
            OutputStream out = conn.getOutputStream();
            out.write(data);
            //System.out.println(conn.getResponseCode());
            statusCode = conn.getResponseCode();


        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusCode;
    }

    //login the user to server
    public static User login(final String username, final String passsword) {
        User user = null;
        try {
            URL url = new URL(urlhead + "/login");
            StringBuilder sBuilder = new StringBuilder();
            sBuilder.append("username=" + URLEncoder.encode(username, "UTF-8") + "&");
            sBuilder.append("password=" + URLEncoder.encode(passsword, "UTF-8"));
            byte[] data = sBuilder.toString().getBytes("UTF-8");

            //Log.i("getname", params);
            //String sr= HttpRequest.sendPost("http://128.237.207.55:8080/Pi-Server/login", params);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Accept", "application/json"); // 设置接收数据的格式
            conn.setDoOutput(true);
            conn.setDoOutput(true); //如果要输出，则必须加上此句
            OutputStream out = conn.getOutputStream();
            out.write(data);
            Log.i("statusCode", Integer.toString(conn.getResponseCode()));

            //System.out.println(conn.getResponseCode());
            if (conn.getResponseCode() == 201) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        conn.getInputStream()));
                String lines;
                StringBuffer sb = new StringBuffer("");
                while ((lines = reader.readLine()) != null) {
                    lines = new String(lines.getBytes(), "utf-8");
                    sb.append(lines);
                }
                //System.out.println(sb);

                JSONObject obj = new JSONObject(sb.toString());
                Log.i("username from json", obj.getString("username"));
                Log.i("name from json", obj.getString("name"));

                user = new User(obj.getInt("id"), obj.getString("username"),
                        obj.getString("name"), obj.getString("phone"), obj.getString("password"));
            } else {
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return user;
    }

    //get user from server with id
    public static User getWithId(Long id) {
        return null;
    }
}
