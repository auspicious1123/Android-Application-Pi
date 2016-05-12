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
import javax.servlet.http.HttpSession;
import org.json.simple.JSONObject;
import utils.DatabaseIO;

/**
 *
 * @author Yang
 * 
 * Get user profile API
 */
public class UserController extends HttpServlet {

    private DatabaseIO db;

    public UserController() {
        db = new DatabaseIO();
        db.connect();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("user");

        if (username == null) {
            PrintWriter out = response.getWriter();
            JSONObject obj = new JSONObject();
            response.setContentType("application/json");
            response.setStatus(401);
            out.print(obj);
            out.flush();
        } else {
            String queryStr;
            PreparedStatement ps;
            ResultSet rs;
            try {
                queryStr = "SELECT * FROM piServer.users WHERE username=?;";
                ps = db.getConn().prepareStatement(queryStr);
                ps.setString(1, username);
                rs = ps.executeQuery();

                if (rs.next()) {
                    PrintWriter out = response.getWriter();
                    JSONObject obj = new JSONObject();
                    response.setContentType("application/json");
                    response.setStatus(201);
                    obj.put("username", rs.getString("name"));
                    obj.put("name", rs.getString("name"));
                    obj.put("usernkame", rs.getString("username"));
                    obj.put("phone", rs.getString("phone"));
                    obj.put("avator", rs.getString("avator"));
                    
                    out.print(obj);
                    out.flush();
                } else {
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
}
