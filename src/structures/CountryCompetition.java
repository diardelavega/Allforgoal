package structures;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dbtry.Conn;
import extra.CompIdLinkSoccerPlunter;

/**
 * @author Administrator
 * 
 *         a data structure intended to store a combination of countries and
 *         their competitions
 */

public class CountryCompetition {

	public static final String baseUrl = "http://www.soccerpunter.com";
	private static List<CountryCompObj> compList;
	private static List<CompIdLinkSoccerPlunter> compLinkList;

	public void readContryComp() throws SQLException {
		// read from db and insert to list;
		Conn conn = new Conn();
		conn.open();
		Statement st = conn.getConn().createStatement();
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
			conn.close();
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

	public void readCompIdLink() {
		// TODO get data from db
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

	public static List<CountryCompObj> getCompList() {
		return compList;
	}

	public static void setCompList(List<CountryCompObj> compList) {
		CountryCompetition.compList = compList;
	}

	public static List<CompIdLinkSoccerPlunter> getCompLinkList() {
		return compLinkList;
	}

	public static void setCompLinkList(
			List<CompIdLinkSoccerPlunter> compLinkList) {
		CountryCompetition.compLinkList = compLinkList;
	}

}
