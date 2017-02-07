/**
 *
 * @author Deepak saraf
 */

package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import com.util.Utility;

public class DBUtil {

	private static Connection con = null;

	public static Connection getCon() {
		Properties p = Utility.readProperties();
		try {
			Class.forName(p.getProperty("DriverName"));
			con = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/hms", "root", "rat");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return con;
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		con.close();
	}
}
