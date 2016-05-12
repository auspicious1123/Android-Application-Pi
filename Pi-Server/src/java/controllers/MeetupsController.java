package controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import utils.DatabaseIO;

/**
 *
 * @author Yang
 *
 * get Meetup list of certain category
 */
public class MeetupsController extends HttpServlet {

    private DatabaseIO db;

    public MeetupsController() {
        this.db = new DatabaseIO();
        this.db.connect();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String category = request.getParameter("category");
        double latitude = 40.43437661;
        double longitude = -79.93055258;

        if (category.equals("Nearby")) {
            latitude = Double.parseDouble(request.getParameter("latitude"));
            longitude = Double.parseDouble(request.getParameter("longitude"));
        }

        System.out.println(category);
        System.out.println("latitude = " + latitude + ", longitude = " + longitude);

        String queryStr;
        PreparedStatement ps;
        ResultSet rs;

        try {
            if (category.equals("All") || category.equals("Nearby")) {
                queryStr = "SELECT * FROM piServer.meetups;";
                ps = db.getConn().prepareStatement(queryStr);
            } else {
                queryStr = "SELECT * FROM piServer.meetups WHERE category = ?;";
                ps = db.getConn().prepareStatement(queryStr);
                ps.setString(1, category);
            }

            rs = ps.executeQuery();
            JSONArray jsonList = new JSONArray();

            while (rs.next()) {
                JSONObject obj = new JSONObject();

                System.out.println(rs.getString("avatar"));
                obj.put("id", rs.getInt("id"));
                obj.put("name", rs.getString("name"));
                obj.put("content", rs.getString("content"));
                obj.put("avatar", rs.getString("avatar"));
                obj.put("address", rs.getString("address"));
                obj.put("category", rs.getString("category"));
                obj.put("latitude", rs.getDouble("latitude"));
                obj.put("longitude", rs.getDouble("longitude"));
                obj.put("status", rs.getInt("status"));
                obj.put("startTime", rs.getString("start_time"));
                obj.put("endTime", rs.getString("end_time"));

                if (category.equals("Nearby")) {
                    double latA = latitude;
                    double longA = longitude;
                    double latB = rs.getDouble("latitude");
                    double longB = rs.getDouble("longitude");

                    double distance = GetDistance(latA, longA, latB, longB);
                    
                    if(distance < 5000){
                        jsonList.add(obj);
                    }
                } else {
                    jsonList.add(obj);
                }
            }

            JSONObject obj = new JSONObject();
            obj.put("meetups", jsonList);

            System.out.println(jsonList.size());
            PrintWriter out = response.getWriter();
            response.setContentType("application/json");
            response.setStatus(201);
            out.print(obj);
            out.flush();
        } catch (SQLException ex) {
            Logger.getLogger(SignupController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private final double EARTH_RADIUS = 6378.137;

    private double rad(double d) {
        return d * Math.PI / 180.0;
    }

    private double GetDistance(double lat1, double lng1, double lat2, double lng2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);

        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }
}
