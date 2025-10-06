import java.sql.*;

public class TestConnection {
    public static void main(String[] args) {
        try {
            System.out.println("Testing MySQL connection...");
            
            // Load driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver loaded successfully");
            
            // Test connection without database first
            Connection conn1 = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC", 
                "root", 
                "rootpassword"
            );
            System.out.println("Connected to MySQL server successfully");
            
            // Create database
            conn1.createStatement().execute("CREATE DATABASE IF NOT EXISTS crms_db");
            System.out.println("Database crms_db created/verified");
            conn1.close();
            
            // Test connection with database
            Connection conn2 = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/crms_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC", 
                "root", 
                "rootpassword"
            );
            System.out.println("Connected to crms_db successfully");
            conn2.close();
            
            System.out.println("All connection tests passed!");
            
        } catch (Exception e) {
            System.out.println("Connection failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}