package cmu.rrg.pi.model;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import cmu.rrg.pi.service.LocationService;

/**
 * Created by Yang on 4/30/16.
 */
public class MeetupADO {
    private static String site = "http://128.237.212.2:8080/Pi-Server";

    //add the meetup to the server
    public static Meetup add(Meetup meetup){
        Meetup res = null;

        try {
            Log.i("url", site + "/newMeetup");
            URL url = new URL(site + "/newMeetup");

            String params;
            StringBuilder strBuilder = new StringBuilder();

            strBuilder.append("name=" + URLEncoder.encode(meetup.getName(), "UTF-8") + "&");
            strBuilder.append("content=" + URLEncoder.encode(meetup.getContent(), "UTF-8") + "&");
            strBuilder.append("category=" + URLEncoder.encode(meetup.getCategory(), "UTF-8") + "&");
            strBuilder.append("address=" + URLEncoder.encode(meetup.getAddress(), "UTF-8") + "&");
            strBuilder.append("latitude=" + URLEncoder.encode(Double.toString(meetup.getLatitude()),
                                "UTF-8") + "&");
            strBuilder.append("longitude=" + URLEncoder.encode(Double.toString(meetup.getLongitude()),
                                "UTF-8") + "&");
            strBuilder.append("status=" + URLEncoder.encode(Integer.toString(meetup.getStatus()),
                                "UTF-8") + "&");
            strBuilder.append("startTime=" + URLEncoder.encode(meetup.getStartTime(), "UTF-8") + "&");
            strBuilder.append("endTime=" + URLEncoder.encode(meetup.getEndTime(), "UTF-8") + "&");
            strBuilder.append("avatar=" + URLEncoder.encode(meetup.getAvatar(), "UTF-8"));

            byte[] data = strBuilder.toString().getBytes("UTF-8");
            params = strBuilder.toString();

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);
            conn.setDoOutput(true);
            OutputStream out = conn.getOutputStream();
            out.write(data);

            if(conn.getResponseCode() == 201){
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;
                StringBuffer sb = new StringBuffer("");
                while((line = reader.readLine()) != null){
                    line = new String(line.getBytes(), "utf-8");
                    sb.append(line);
                }

                JSONObject obj = new JSONObject(sb.toString());

                res = new Meetup(obj.getInt("id"), obj.getString("name"), obj.getString("content"),
                                        obj.getString("category"), obj.getString("address"), obj.getDouble("latitude"),
                                        obj.getDouble("longitude"), obj.getInt("status"),
                                        obj.getString("startTime"), obj.getString("endTime")
                                );
                res.setAvatar(obj.getString("avatar"));

                Log.i("Return avatar", res.getAvatar());
                Log.i("Return meetup", res.getName());
                return res;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return res;
    }

    //get certain meetup from the server with id
    public static Meetup get(int id){
        Meetup meetup = null;
        try{
            URL url = new URL(site + "/getMeetup?id=" + Integer.toString(id));

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);
            OutputStream out = conn.getOutputStream();
            String str = "get";
            out.write(str.getBytes());

            if(conn.getResponseCode() == 201){
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;
                StringBuffer sb = new StringBuffer("");
                while((line = reader.readLine()) != null){
                    line = new String(line.getBytes(), "utf-8");
                    sb.append(line);
                }

                JSONObject obj = new JSONObject(sb.toString());

                meetup = new Meetup(obj.getInt("id"), obj.getString("name"), obj.getString("content"),
                        obj.getString("category"), obj.getString("address"), obj.getDouble("latitude"),
                        obj.getDouble("longitude"), obj.getInt("status"),
                        obj.getString("startTime"), obj.getString("endTime")
                );
                meetup.setAvatar(obj.getString("avatar"));
            }
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return meetup;
    }

    public static ArrayList<Integer> getMembers(int id){
        ArrayList<Integer> members = new ArrayList<>();
        try{
            URL url = new URL(site + "/getMembers?meetupId=" + id);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            Log.i("Members", "sending");
            Log.i("Response Code", Integer.toString(conn.getResponseCode()));

            if(conn.getResponseCode() == 201){
                Log.i("Members ResponseCode", Integer.toString(conn.getResponseCode()));

                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        conn.getInputStream()
                ));

                String line;
                StringBuffer sb = new StringBuffer("");
                while((line = reader.readLine()) != null){
                    line = new String(line.getBytes(), "utf-8");
                    sb.append(line);
                }

                JSONObject obj = new JSONObject(sb.toString());
                JSONArray jsonList = (JSONArray) obj.get("members");
                for(int i = 0; i < jsonList.length(); i++){
                    JSONObject item = (JSONObject) jsonList.get(i);
                    int userId = item.getInt("userId");
                    members.add(userId);
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return members;
    }

    public static ArrayList<Integer> signupMeetup(int meetupId, int userId){
        ArrayList<Integer> members = new ArrayList<>();

        try {
            URL url = new URL(site + "/signupMeetup");

            StringBuilder strBuilder = new StringBuilder();

            strBuilder.append("meetupId=" + URLEncoder.encode(Integer.toString(meetupId), "UTF-8") + "&");
            strBuilder.append("userId=" + URLEncoder.encode(Integer.toString(userId), "UTF-8"));

            byte[] data = strBuilder.toString().getBytes("UTF-8");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);
            conn.setDoOutput(true);
            OutputStream out = conn.getOutputStream();
            out.write(data);

            Log.i("Sigup meetups ResponseCode", Integer.toString(conn.getResponseCode()));

            if(conn.getResponseCode() == 201){
                Log.i("Meetups ResponseCode", Integer.toString(conn.getResponseCode()));

                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        conn.getInputStream()
                ));

                String line;
                StringBuffer sb = new StringBuffer("");
                while((line = reader.readLine()) != null){
                    line = new String(line.getBytes(), "utf-8");
                    sb.append(line);
                }

                JSONObject obj = new JSONObject(sb.toString());
                JSONArray jsonList = (JSONArray) obj.get("members");
                for(int i = 0; i < jsonList.length(); i++){
                    JSONObject item = (JSONObject) jsonList.get(i);
                    members.add(item.getInt("userId"));
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return members;
    }

    //get the meetups from the server
    public static MeetupFactory getMeetups(String category, int pageNumber, int pageSize){
        return null;
    }

    public static MeetupFactory getMeetups(Context context, String category) {
        MeetupFactory meetupFactory = new MeetupFactory(category);
        try{
            URL url = new URL(site + "/meetups?category=" + category);
            if(category.equals("Nearby")) {
                Location loc = LocationService.getLocation(context);
                double latitude = 40.45360923;
                double longitude = -79.94405768;
                if(loc == null){
                    latitude = loc.getLatitude();
                    longitude = loc.getLongitude();
                }
                url = new URL(site + "/meetups?latitude=" + Double.toString(latitude) +
                        "&longitude=" + Double.toString(longitude) + "&category=" + category);
            }

            Log.i("meetupADO url", url.getPath());

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            Log.i("Meetups", "sending");
            Log.i("Response Code", Integer.toString(conn.getResponseCode()));

            if(conn.getResponseCode() == 201){
                Log.i("Meetups ResponseCode", Integer.toString(conn.getResponseCode()));

                BufferedReader reader = new BufferedReader(new InputStreamReader(
                    conn.getInputStream()
                ));

                String line;
                StringBuffer sb = new StringBuffer("");
                while((line = reader.readLine()) != null){
                    line = new String(line.getBytes(), "utf-8");
                    sb.append(line);
                }

                JSONObject obj = new JSONObject(sb.toString());
                JSONArray jsonList = (JSONArray) obj.get("meetups");
                for(int i = 0; i < jsonList.length(); i++){
                    JSONObject item = (JSONObject) jsonList.get(i);
                    Meetup meetup = new Meetup(item.getInt("id"), item.getString("name"),
                            item.getString("content"), item.getString("category"), item.getString("address"),
                            item.getDouble("latitude"), item.getDouble("longitude"),
                            item.getInt("status"),
                            item.getString("startTime"), item.getString("endTime"));
                    meetup.setAvatar(item.getString("avatar"));
                    meetupFactory.add(meetup);
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return meetupFactory;
    }


}
