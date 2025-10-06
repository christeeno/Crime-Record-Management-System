package crms;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {

	private static Connection connect;
	private static boolean initialized = false;
	
	public static Connection getConnected()
	{
		// If already connected and connection is valid, return it
		try {
			if (connect != null && !connect.isClosed()) {
				return connect;
			}
		} catch(Exception e) {
			// Connection might be closed, continue to create new one
		}
		
		try {
			
			Class.forName("com.mysql.cj.jdbc.Driver");
			
			// First try to connect without specifying database to create it
			if (!initialized) {
				try {
					Connection tempConn = DriverManager.getConnection("jdbc:mysql://localhost:3306?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC", "root", "rootpassword");
					tempConn.createStatement().execute("CREATE DATABASE IF NOT EXISTS crms_db");
					tempConn.close();
					System.out.println("Database crms_db created/verified");
				} catch(Exception createEx) {
					System.out.println("Could not create database: " + createEx.getMessage());
				}
			}
			
			// Now connect to the crms_db database
			connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/crms_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC", "root", "rootpassword");
			
			// Create tables if they don't exist (only once)
			if (!initialized) {
				createTables();
				initialized = true;
			}
			
			System.out.println("Database connection established successfully!");
			
		}
		catch(Exception e)
		{
			System.out.println("Database not connected: "+e.getMessage());
			e.printStackTrace();
			connect = null;
		}
		
		return connect;
	}
	
	private static void createTables() {
		try {
			if (connect != null) {
				// Create officers table
				String createOfficersTable = "CREATE TABLE IF NOT EXISTS officers (" +
					"officerid VARCHAR(20) PRIMARY KEY," +
					"officername VARCHAR(100) NOT NULL," +
					"password VARCHAR(50) NOT NULL," +
					"designation VARCHAR(50) NOT NULL," +
					"station VARCHAR(100) NOT NULL," +
					"age INT NOT NULL," +
					"phno BIGINT NOT NULL" +
					")";
				
				connect.createStatement().execute(createOfficersTable);
				
				// Create FIR table
				String createFirTable = "CREATE TABLE IF NOT EXISTS fir (" +
					"firno VARCHAR(20) PRIMARY KEY," +
					"complainant_name VARCHAR(100) NOT NULL," +
					"complainant_address TEXT," +
					"complainant_phone BIGINT," +
					"incident_date DATE," +
					"incident_location VARCHAR(200)," +
					"incident_description TEXT," +
					"officer_id VARCHAR(20)," +
					"status VARCHAR(50) DEFAULT 'PENDING'," +
					"created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
					")";
				
				connect.createStatement().execute(createFirTable);
				
				// Create admin table
				String createAdminTable = "CREATE TABLE IF NOT EXISTS admin (" +
					"id INT AUTO_INCREMENT PRIMARY KEY," +
					"name VARCHAR(50) NOT NULL," +
					"password VARCHAR(50) NOT NULL" +
					")";
				
				connect.createStatement().execute(createAdminTable);
				
				// Insert sample admin if table is empty
				String checkAdmin = "SELECT COUNT(*) FROM admin";
				var adminRs = connect.createStatement().executeQuery(checkAdmin);
				adminRs.next();
				if (adminRs.getInt(1) == 0) {
					String insertAdmin = "INSERT INTO admin (name, password) VALUES ('admin', 'admin123')";
					connect.createStatement().execute(insertAdmin);
				}
				
				// Insert sample officers if table is empty
				String checkOfficers = "SELECT COUNT(*) FROM officers";
				var rs = connect.createStatement().executeQuery(checkOfficers);
				rs.next();
				if (rs.getInt(1) == 0) {
					String insertOfficers = "INSERT INTO officers VALUES " +
						"('ADM001', 'Admin Officer', 'admin123', 'Administrator', 'Central Station', 35, 9876543210)," +
						"('HIG001', 'John Smith', 'pass123', 'Inspector', 'North Station', 40, 9876543211)," +
						"('LOW001', 'Mike Wilson', 'pass789', 'Constable', 'East Station', 28, 9876543213)";
					connect.createStatement().execute(insertOfficers);
				}
				
				System.out.println("Database tables created successfully!");
			}
		} catch(Exception e) {
			System.out.println("Error creating tables: " + e.getMessage());
		}
	}
}
