package cmu.rrg.pi.service;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Yang on 5/2/16.
 */
public class LocationService {
    static final String api = "http://maps.google.com/maps/api/geocode/json";

    public static String getAddressByGoodle(double latitude, double longitude){
        try {
            URL url = new URL(api + "?latlng=" + latitude + "," + longitude);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            Log.i("Members", "sending");
            Log.i("Response Code", Integer.toString(conn.getResponseCode()));

            if(conn.getResponseCode() == 200){
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        conn.getInputStream()
                ));

                String line;
                StringBuffer sb = new StringBuffer("");
                while((line = reader.readLine()) != null) {
                    line = new String(line.getBytes(), "utf-8");
                    sb.append(line);
                }

                JSONObject obj = new JSONObject(sb.toString());
                String address = obj.getJSONArray("results").getJSONObject(0).getString("formatted_address");
                return address;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    //Get the Location by GPS or WIFI
    public static Location getLocation(Context context) {
        Location loc = null;
        LocationManager locManager = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);

        try {
            loc = locManager
                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (loc == null) {
                loc = locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }

        } catch (SecurityException se) {
            se.printStackTrace();
        }

        return loc;
    }
}
