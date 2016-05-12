/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Yang
 */
public class DatabaseIO {
    private static final String URL = "jdbc:mysql://localhost:3306";
    private static final String USER_NAME = "root";
    private static final String PASSWORD = "123456";

    private Connection conn;
    
    public DatabaseIO(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DatabaseIO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void connect(){
        try {
            conn = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseIO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Connection getConn() {
        if(conn == null){
            try {
                conn = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseIO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return conn;
    }
    
    public void close(){
        try {
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseIO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
