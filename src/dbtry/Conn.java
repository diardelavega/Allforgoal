package dbtry;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Conn {
	private Connection conn = null;

	public boolean open() {
		boolean flag = false;
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager
					.getConnection("jdbc:mysql://localhost:3306/bast?autoReconnect=true&useSSL=false", "root",
							"root");
//							"SanSalvador3#");
			flag = true;
		} catch (InstantiationException | IllegalAccessException
				| ClassNotFoundException e) {
			flag = false;
			e.printStackTrace();
		} catch (SQLException e) {
			flag = false;
			e.printStackTrace();
		}
		return flag;
	}

	public void foo() {

		if (conn == null) {
			System.out.println("NULL connection");
		} else {
			System.out.println("Connected");
			try {
				Statement st = conn.createStatement();
				ResultSet rs = st.executeQuery("SELECT * FROM aa");

				while (rs.next()) {
					System.out.println(rs.getString(1));
					System.out.println(rs.getString(2));
				}

				rs.close();
				st.close();

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void close() {
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Connection getConn() {
		return conn;
	}

	// public void setConn(Connection conn) {
	// this.conn = conn;
	// }

}
