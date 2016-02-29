package structures;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import basicStruct.CCAllStruct;
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
	public static List<CCAllStruct> ccasList = new ArrayList<>();
	// public static List<CountryCompObj> compList = new ArrayList<>();
	// public static List<CompIdLinkSoccerPlunter> compLinkList = new
	// ArrayList<>();

	public void readCCAllStruct(Connection conn) throws SQLException {
		// read from db and insert to list;
		// possible necessity of some ordering; COUNTRIES are already ordered
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery("SELECT * FROM ccallstruct ;");
		CCAllStruct ccas;
		while (rs.next()) {
			ccas = new CCAllStruct();
			ccas.setCompId(rs.getInt("compid"));
			ccas.setCountry(rs.getString("country"));
			ccas.setCompetition(rs.getString("competition"));
			ccas.setCompLink(rs.getString("link"));
			ccas.setDb(rs.getInt("db"));
			ccas.setDb(rs.getInt("_level"));
			ccasList.add(ccas);

		}
		try {
			st.close();
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void storeCCAllStruct() throws SQLException {
		// write the comp country to the db
		Conn conn = new Conn();
		conn.open();
		String sql = "INSERT INTO ccallstruct VALUES (?,?,?,?,?,?)";
		PreparedStatement ps = conn.getConn().prepareStatement(sql);

		for (CCAllStruct obj : ccasList) {
			ps.setInt(1, obj.getCompId());
			ps.setString(2, obj.getCountry());
			ps.setString(3, obj.getCompetition());
			ps.setString(4, obj.getCompLink());
			ps.setInt(5, obj.getDb());
			ps.setInt(6, obj.getLevel());
			ps.addBatch();
		}
		ps.executeBatch();
		ps.close();
		conn.close();
	}

	public int searchComp(String country, String compName) {
		/*
		 * search the competitions list and from the name return its id & index
		 * position. (use the fact that the countries are alphabetically ordered
		 * to make a faster search).
		 */

		// the first encountered instance of country might not be first orr last
		// but in the middle so we search from the middle up or down
		int initial = binarySearch(country, 0, ccasList.size());
		int i = initial;
		while (ccasList.get(i).getCountry().equals(country)) {
			if (ccasList.get(i).getCompetition().equals(country)) {
				return i;
			}
			initial++;
		}
		i = initial;
		while (ccasList.get(i).getCountry().equals(country)) {
			if (ccasList.get(i).getCompetition().equals(country)) {
				return i;
			}
			i--;
		}

		return 0;
	}

	// -------------------------------------ORIGINAL

	// public void readContryComp(Connection conn) throws SQLException {
	// // read from db and insert to list;
	// // possible necessity of some ordering; COUNTRIES are already ordered
	// Statement st = conn.createStatement();
	// ResultSet rs = st.executeQuery("SELECT * FROM countrycomp ;");
	// CountryCompObj ccobj;
	// while (rs.next()) {
	// ccobj = new CountryCompObj();
	// ccobj.setId(rs.getInt("compid"));
	// ccobj.setCountry(rs.getString("country"));
	// ccobj.setCompetition(rs.getString("competition"));
	// compList.add(ccobj);
	// }
	// try {
	// st.close();
	// rs.close();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }

	// public void storeCountryComp() throws SQLException {
	// // write the comp country to the db
	// Conn conn = new Conn();
	// conn.open();
	// String sql = "INSERT INTO countrycomp VALUES (?,?,?)";
	// PreparedStatement ps = conn.getConn().prepareStatement(sql);
	//
	// for (CountryCompObj obj : compList) {
	// ps.setInt(1, obj.getId());
	// ps.setString(2, obj.getCountry());
	// ps.setString(3, obj.getCompetition());
	// ps.addBatch();
	// }
	// ps.executeBatch();
	// ps.close();
	// conn.close();
	//
	// }

	// public void readCompIdLink(Connection conn) throws SQLException {
	// Statement st = conn.createStatement();
	// ResultSet rs = st.executeQuery("SELECT * FROM compidlink ;");
	// CompIdLinkSoccerPlunter cid;
	// while (rs.next()) {
	// cid = new CompIdLinkSoccerPlunter();
	// cid.setCompId(rs.getInt(1));
	// cid.setCompLink(baseUrl + rs.getString(2));
	// compLinkList.add(cid);
	// }
	// try {
	// st.close();
	// rs.close();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }

	// public void storeCompIdLink() throws SQLException {
	// Conn conn = new Conn();
	// conn.open();
	// String sql = "INSERT INTO compidlink VALUES (?,?)";
	// PreparedStatement ps = conn.getConn().prepareStatement(sql);
	//
	// for (CompIdLinkSoccerPlunter obj : compLinkList) {
	// ps.setInt(1, obj.getCompId());
	// ps.setString(2, obj.getCompLink());
	// ps.addBatch();
	// }
	// ps.executeBatch();
	// ps.close();
	// conn.close();
	// }

	// public int searchComp(String country, String compName) {
	// /*
	// * search the competitions list and from the name return its id & index
	// * position. (use the fact that the countries are alphabetically ordered
	// * to make a faster search).
	// */
	//
	// // the first encountered instance of country might not be first orr last
	// // but in the middle so we search from the middle up or down
	// int initial = binarySearch(country, 0, compList.size());
	// int i = initial;
	// while (compList.get(i).getCountry().equals(country)) {
	// if (compList.get(i).getCompetition().equals(country)) {
	// return i;
	// }
	// initial++;
	// }
	// i = initial;
	// while (compList.get(i).getCountry().equals(country)) {
	// if (compList.get(i).getCompetition().equals(country)) {
	// return i;
	// }
	// i--;
	// }
	//
	// return 0;
	// }

	private int binarySearch(String country, int min, int max) {
		if (min > max) {
			return -1;
		}
		int mid = (max + min) / 2;

		if (ccasList.get(mid).getCountry().equals(country)) {
			return mid;
		} else if (ccasList.get(mid).getCountry().compareTo(country) > 0) {
			return binarySearch(country, min, mid - 1);
		} else {
			return binarySearch(country, mid + 1, max);
		}
	}

	public int searchComp(String compName) {
		/*
		 * search the competitions list and from the name return its id & index
		 * position.
		 */
		for (int i = 0; i < ccasList.size(); i++) {
			if (compName.equals(ccasList.get(i).getCompetition())) {
				return ccasList.get(i).getCompId();
			}
		}
		return 0;
	}

	public int searchCompUsable(String compName) {
		/*
		 * searches to find a competition if it is db set, so it can be utilizet
		 * for processing
		 */
		for (int i = 0; i < ccasList.size(); i++) {
			if (ccasList.get(i).getDb() == 1) {
				if (compName.equals(ccasList.get(i).getCompetition())) {
					return ccasList.get(i).getCompId();
				}
			}
		}
		return 0;
	}

}
