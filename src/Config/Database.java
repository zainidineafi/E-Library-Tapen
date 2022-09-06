/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Config;

import com.mysql.jdbc.Driver;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Illuminate
 */
public class Database {
    private static final String username = "root";
    private static final String password = "";
    private static final String database = "jdbc:mysql://localhost:3306/e_lite";
    private static Connection connection;
    
    public static Connection GetConnection() {
        try {
            DriverManager.registerDriver(new Driver());
            connection = DriverManager.getConnection(database, username, password);
        } catch (SQLException e) {
            System.out.println("Koneksi error : " + e.getMessage());
        }
        
        return connection;
    }
}
