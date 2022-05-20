
package Com.revature.app;
import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
	static Connection conn = null;
	protected ConnectionFactory() { }
	
	public static Connection getConnection() throws ClassNotFoundException, SQLException {
		String dri = "com.mysql.cj.jdbc.Driver";
		String url = "jdbc:mysql://localhost:3306/lms";
		String user = "root";
		String password = "root";
		
		if(conn == null) {
			Class.forName(dri);
			conn = DriverManager.getConnection(url, user, password);
			
		}
		return conn;
	}
}
