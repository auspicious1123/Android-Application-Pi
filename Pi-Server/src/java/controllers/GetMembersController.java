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
 * 
 * get information of a meetup
 */
public class GetMembersController extends HttpServlet {
    private DatabaseIO db;
    
    public GetMembersController() {
        db = new DatabaseIO();
        db.connect();
    }
    
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int meetupId = Integer.parseInt(request.getParameter("meetupId"));
        
        String queryStr;
        PreparedStatement ps;
        ResultSet rs;
        try {
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
        } catch (SQLException ex) {
            Logger.getLogger(SignupController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
