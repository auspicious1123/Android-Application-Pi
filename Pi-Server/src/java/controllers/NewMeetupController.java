/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import org.json.simple.JSONObject;
import utils.DatabaseIO;

/**
 *
 * create a new Meetup
 */
public class NewMeetupController extends HttpServlet {

    private DatabaseIO db;

    public NewMeetupController() {
        db = new DatabaseIO();
        db.connect();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String name = request.getParameter("name");
        String content = request.getParameter("content");
        String category = request.getParameter("category");
        String address = request.getParameter("address");
        double latitude = Double.parseDouble(request.getParameter("latitude"));
        double longitude = Double.parseDouble(request.getParameter("longitude"));

        int status = Integer.parseInt(request.getParameter("status"));
        String startTime = request.getParameter("startTime");
        String endTime = request.getParameter("endTime");
        String avatar = request.getParameter("avatar");
        
        System.out.println("get Avatar:" + avatar);
        System.out.println(startTime);
        System.out.println(endTime);
        
        String queryStr;
        PreparedStatement ps;
        ResultSet rs;
        int count = 0;
        try {
            queryStr = "SELECT COUNT(*) FROM piServer.meetups";
            ps = db.getConn().prepareStatement(queryStr);
            rs = ps.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
            
            System.out.println(count);

            queryStr = "INSERT INTO piServer.meetups VALUES(?,?,?,?,?,?,?,?,?,?,?);";
            ps = db.getConn().prepareStatement(queryStr);
            
            ps.setInt(1, count + 1);
            ps.setString(2, name);
            ps.setString(3, avatar);
            ps.setString(4, content);
            ps.setString(5, category);
            ps.setString(6, address);
            ps.setDouble(7, latitude);
            ps.setDouble(8, longitude);
            ps.setInt(9, status);
            ps.setString(10, startTime);
            ps.setString(11, endTime);

            ps.executeUpdate();

            PrintWriter out = response.getWriter();
            JSONObject obj = new JSONObject();
            response.setContentType("application/json");
            response.setStatus(201);
            
            obj.put("id", count + 1);
            obj.put("name", name);
            obj.put("content", content);
            obj.put("avatar", avatar);
            obj.put("category", category);
            obj.put("address", address);
            obj.put("latitude", latitude);
            obj.put("longitude", longitude);
            obj.put("status", status);
            obj.put("startTime", startTime);
            obj.put("endTime", endTime);

            out.print(obj);
            out.flush();
        } catch (SQLException ex) {
            Logger.getLogger(NewMeetupController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
