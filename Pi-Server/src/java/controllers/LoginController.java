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
 * 
 * User login API
 */
public class LoginController extends HttpServlet {
    private DatabaseIO db;
    
    public LoginController() {
        db = new DatabaseIO();
        db.connect();
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
            
        System.out.println(username);
        System.out.println(password);
        
        String queryStr;
        PreparedStatement ps;
        ResultSet rs;
        try {
            queryStr = "SELECT * FROM piServer.users WHERE username=? and passwd=?;";
            ps = db.getConn().prepareStatement(queryStr);
            ps.setString(1, username);
            ps.setString(2, password);
            rs = ps.executeQuery();
            
            
            if(rs.next()){
                request.getSession().setAttribute("user", rs.getString("name"));
                
                PrintWriter out = response.getWriter();
                JSONObject obj = new JSONObject();
                response.setContentType("application/json");
                response.setStatus(201);
                obj.put("id", rs.getInt("id"));
                obj.put("name", rs.getString("name"));
                obj.put("username", rs.getString("username"));
                obj.put("password", rs.getString("passwd"));
                obj.put("phone", rs.getString("phone"));
                obj.put("avator", rs.getString("avator"));
                
                out.print(obj);
                out.flush();
            }else{
                PrintWriter out = response.getWriter();
                JSONObject obj = new JSONObject();
                response.setContentType("application/json");
                response.setStatus(401);
                
                out.print(obj);
                out.flush();
            }
        } catch (SQLException ex) {
            Logger.getLogger(SignupController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
