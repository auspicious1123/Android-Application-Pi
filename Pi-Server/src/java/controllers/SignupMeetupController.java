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
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import utils.DatabaseIO;

/**
 *
 * @author Yang
 */
public class SignupMeetupController extends HttpServlet {
    
    private DatabaseIO db;
    
    public SignupMeetupController(){
        this.db = new DatabaseIO();
        db.connect();
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int meetupId = Integer.parseInt(request.getParameter("meetupId"));
        int userId = Integer.parseInt(request.getParameter("userId"));
        
        System.out.println("Signup Meetup");
        System.out.println(meetupId);
        System.out.println(userId);
        
        String queryStr;
        PreparedStatement ps;
        ResultSet rs;
        try {
            queryStr = "SELECT * from piServer.users WHERE id=?;";           
            ps = db.getConn().prepareStatement(queryStr);
            ps.setInt(1, userId);
            rs = ps.executeQuery();
            
            if(!rs.next()){
                PrintWriter out = response.getWriter();
                JSONObject obj = new JSONObject();
                response.setContentType("application/json");
                response.setStatus(403);
                out.print(obj);
                out.flush();
            }else{
                queryStr = "SELECT * FROM piServer.meetups WHERE id=?";
                ps = db.getConn().prepareStatement(queryStr);
                ps.setInt(1, meetupId);

                rs = ps.executeQuery();

                if (!rs.next()) {
                    PrintWriter out = response.getWriter();
                    JSONObject obj = new JSONObject();
                    response.setContentType("application/json");
                    response.setStatus(403);
                    out.print(obj);
                    out.flush();
                } else {
                    queryStr = "SELECT * FROM piServer.members WHERE meetup_id=? AND user_id=?;";
                    ps = db.getConn().prepareStatement(queryStr);
                    ps.setInt(1, meetupId);
                    ps.setInt(2, userId);

                    rs = ps.executeQuery();

                    if (rs.next()) {
                        PrintWriter out = response.getWriter();
                        JSONObject obj = new JSONObject();
                        response.setContentType("application/json");
                        response.setStatus(403);
                        out.print(obj);
                        out.flush();
                    } else {
                        queryStr = "INSERT INTO piServer.members VALUES(?, ?, ?);";
                        ps = db.getConn().prepareStatement(queryStr);
                        ps.setInt(1, 0);
                        ps.setInt(2, meetupId);
                        ps.setInt(3, userId);

                        ps.executeUpdate();

                        queryStr = "SELECT * FROM piServer.members WHERE meetup_id=?";
                        ps = db.getConn().prepareStatement(queryStr);
                        ps.setInt(1, meetupId);
                        rs = ps.executeQuery();

                        JSONArray jsonList = new JSONArray();
                        while (rs.next()) {
                            JSONObject obj = new JSONObject();
                            obj.put("userId", rs.getInt("user_id"));
                            jsonList.add(obj);
                        }

                        JSONObject obj = new JSONObject();
                        obj.put("meetupId", meetupId);
                        obj.put("members", jsonList);

                        System.out.println(jsonList.size());
                        PrintWriter out = response.getWriter();
                        response.setContentType("application/json");
                        response.setStatus(201);
                        out.print(obj);
                        out.flush();
                    }
                }
            }
        } catch (SQLException ex) {
            
            Logger.getLogger(SignupController.class.getName()).log(Level.SEVERE, null, ex);
        }    
    }
}
