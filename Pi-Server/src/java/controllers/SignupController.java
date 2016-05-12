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
 * @author Yang
 * 
 * User register API
 */
public class SignupController extends HttpServlet {
    private DatabaseIO db;
    
    public SignupController() {
        db = new DatabaseIO();
        db.connect();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String name = request.getParameter("name");
        String phone = request.getParameter("phone");
        String avator = request.getParameter("avator");
        
        String queryStr;
        PreparedStatement ps;
        ResultSet rs;
        
        try {
            queryStr = "SELECT * from piServer.users WHERE username=?;";           
            ps = db.getConn().prepareStatement(queryStr);
            ps.setString(1, username);
            rs = ps.executeQuery();
            
            if(rs.next()){
                PrintWriter out = response.getWriter();
                JSONObject obj = new JSONObject();
                response.setContentType("application/json");
                response.setStatus(403);
                out.print(obj);
                out.flush();
            }else{
                queryStr = "INSERT INTO piServer.users VALUES(?,?,?,?,?,?);";
                ps = db.getConn().prepareStatement(queryStr);
                ps.setInt(1, 0);
                ps.setString(2, username);
                ps.setString(3, password);
                ps.setString(4, name);
                ps.setString(5, phone);
                ps.setString(6, avator);
                ps.executeUpdate();
                
                request.getSession().setAttribute("user", username);
                
                PrintWriter out = response.getWriter();
                JSONObject obj = new JSONObject();
                response.setContentType("application/json");
                response.setStatus(201);
                
                obj.put("name", name);
                obj.put("username", username);
                obj.put("phone", phone);
                obj.put("avator", avator);
                out.print(obj);
                out.flush();
            }
        } catch (SQLException ex) {
            
            Logger.getLogger(SignupController.class.getName()).log(Level.SEVERE, null, ex);
        }    
    }
}
