package seatingarrangement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class conn {
    Connection c;
    Statement s;

    // Constructor
    conn() {
        try {
            
            Class.forName("com.mysql.cj.jdbc.Driver");

            
            c = DriverManager.getConnection("jdbc:mysql://localhost:3306/sms", "root", "mysql");

            
            s = c.createStatement();
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Connection failed.");
            e.printStackTrace();
        }
    }

   
    public void close() {
        try {
            if (s != null) s.close();
            if (c != null) c.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
