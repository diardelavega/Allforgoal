package structures;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import basicStruct.CompIdLinkSoccerPlunter;
import basicStruct.CountryCompObj;
import dbtry.Conn;

/**
 * @author Administrator
 * 
 *         a data structure intended to store a combination of countries and
 *         their competitions. It is also used to load the date into java
 *         structures whenever is requested.
 */

public class CountryCompetition {

	public static final String baseUrl = "http://www.soccerpunter.com";
	public static List<CountryCompObj> compList = new ArrayList<>();
	public static List<CompIdLinkSoccerPlunter> compLinkList = new ArrayList<>();

	public void readContryComp(Connection conn) throws SQLException {
		// read from db and insert to list;
		Statement st = conn.createStatement();
		// TODO possible necesity of some ordering
		ResultSet rs = st.executeQuery("SELECT * FROM countrycomp ;");
		CountryCompObj ccobj;
		while (rs.next()) {
			ccobj = new CountryCompObj();
			ccobj.setId(rs.getInt("compid"));
			ccobj.setCountry(rs.getString("country"));
			ccobj.setCompetition(rs.getString("competition"));
			compList.add(ccobj);
		}
		try {
			st.close();
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void storeCountryComp() throws SQLException {
		// write the comp country to the db
		Conn conn = new Conn();
		conn.open();
		String sql = "INSERT INTO countrycomp VALUES (?,?,?)";
		PreparedStatement ps = conn.getConn().prepareStatement(sql);

		for (CountryCompObj obj : compList) {
			ps.setInt(1, obj.getId());
			ps.setString(2, obj.getCountry());
			ps.setString(3, obj.getCompetition());
			ps.addBatch();
		}
		ps.executeBatch();
		ps.close();
		conn.close();

	}

	public void readCompIdLink(Connection conn) throws SQLException {
		Statement st = conn.createStatement();
		// TODO possible necesity of some ordering
		ResultSet rs = st.executeQuery("SELECT * FROM compidlink ;");
		CompIdLinkSoccerPlunter cid;
		while (rs.next()) {
			cid = new CompIdLinkSoccerPlunter();
			cid.setCompId(rs.getInt(1));
			cid.setCompLink(baseUrl + rs.getString(2));
			compLinkList.add(cid);
		}
		try {
			st.close();
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void storeCompIdLink() throws SQLException {
		// TODO store data todb
		Conn conn = new Conn();
		conn.open();
		String sql = "INSERT INTO compidlink VALUES (?,?)";
		PreparedStatement ps = conn.getConn().prepareStatement(sql);

		for (CompIdLinkSoccerPlunter obj : compLinkList) {
			ps.setInt(1, obj.getCompId());
			ps.setString(2, obj.getCompLink());
			ps.addBatch();
		}
		ps.executeBatch();
		ps.close();
		conn.close();
	}

}
