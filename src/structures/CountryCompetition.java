package structures;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonSyntaxException;
import com.sun.org.apache.bcel.internal.generic.ALOAD;

import basicStruct.CCAllStruct;
import basicStruct.CompIdLinkSoccerPlunter;
import basicStruct.CountryCompObj;
import dbtry.Conn;
import demo.Demo;
import diskStore.FileHandler;
import extra.StandartResponses;
import extra.StringSimilarity;
import extra.Unilang;

/**
 * @author Administrator
 * 
 *         a data structure intended to store a combination of countries and
 *         their competitions. It is also used to load the date into java
 *         structures whenever is requested.
 */

public class CountryCompetition {

	public static final Logger logger = LoggerFactory
			.getLogger(CountryCompetition.class);

	public static final String baseUrl = "http://www.soccerpunter.com";
	public static List<CCAllStruct> ccasList = new ArrayList<>();
	public static Map<String, Integer> allowedcomps = new HashMap<>();

	public CountryCompetition() throws SQLException {
		super();
	}

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
			ccas.setCompLink(rs.getString("complink"));
			ccas.setDb(rs.getInt("db"));
			ccas.setLevel(rs.getInt("_level"));
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

	public void readAllowedComps() throws JsonSyntaxException, IOException {
FileHandler fh = new FileHandler();
fh.readAllowedCompetitions();
	}

	// ----------------------------------------
	public int searchCompBinary(String country, String compName, boolean b) {
		/*
		 * search the competitions list and from the name return its id & index
		 * position. (use the fact that the countries are alphabetically ordered
		 * to make a faster search).
		 * *********************************************** the first encountered
		 * instance of country might not be first or last for the competition we
		 * are looking for; it can be in the middle so we search from the middle
		 * up or down. Depending on b we search with levistein similarity or
		 * without it (with equality)
		 */

		int idx = binarySearchCountry(country, 0, ccasList.size());
		if (idx < 0) {// country not found
			return idx;
		} else {// country found, search for competition
			idx = smallCompSearch(idx, country, compName, b);
			// allowedcomps.put(compName, idx);
			return idx;

		}
	}

	private int smallCompSearch(int initial, String country, String comp,
			boolean b) {
		int i = initial;
		if (b) {// with levistein
			while (ccasList.get(i).getCountry().equals(country)) {
				if (StringSimilarity.levenshteinDistance(ccasList.get(i)
						.getCompetition(), comp) <= StandartResponses.LEV_DISTANCE) {
					return i;
				}
				i++;
			}
			i = initial;
			while (ccasList.get(i).getCountry().equals(country)) {
				if (StringSimilarity.levenshteinDistance(ccasList.get(i)
						.getCompetition(), comp) <= StandartResponses.LEV_DISTANCE) {
					return i;
				}
				i--;
			}
		} else {// no levestein
			while (ccasList.get(i).getCountry().equals(country)) {
				if (ccasList.get(i).getCompetition().equalsIgnoreCase(comp)) {
					return i;
				}
				i++;
			}
			i = initial;
			while (ccasList.get(i).getCountry().equals(country)) {
				if (ccasList.get(i).getCompetition().equalsIgnoreCase(comp)) {
					return i;
				}
				i--;
			}
		}
		return -1;
	}

	private int binarySearchCountry(String country, int min, int max) {
		/*
		 * use binary search to find an instance of the country we are looking
		 * for. Keep in mind the countries are alphabetically sorted and present
		 * more than once (one country many competitions)
		 */

		if (min > max) {
			return -1;
		}
		int mid = (max + min) / 2;

		if (ccasList.get(mid).getCountry().equals(country)) {
			return mid;
		} else if (ccasList.get(mid).getCountry().compareTo(country) > 0) {
			return binarySearchCountry(country, min, mid - 1);
		} else {
			return binarySearchCountry(country, mid + 1, max);
		}
	}

	// ---------------------------------
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

	public int searchUsableComp(String compName, boolean b) {
		/*
		 * searches to find a competition if it is db set, so it can be utilised
		 * for processing. Searching for competitions within the allowed
		 * Levestain distance (b is with or without levistani distance)
		 */
		int minDistanceCompIdx = -1;// the idx of the most similar term
		int minDist = 100;// var to keep the min distance
		int dist = 0; // temporary distance var

		for (int i = 0; i < ccasList.size(); i++) {
//			if (ccasList.get(i).getDb() == 1) {//Original_Line
				if (b) {
					dist = StringSimilarity.levenshteinDistance(compName, ccasList.get(i).getCompetition());
					if (dist <= 2) {
						return i;
					} else {
						if (dist < minDist) {
							minDist = dist;
							minDistanceCompIdx = i;
						}
					} // else
				} else {
					if (compName.equalsIgnoreCase(ccasList.get(i) .getCompetition())) {
						return i;
					}
				}
//			} // db ==1  //Original_Line
		}// for

		return minDistanceCompIdx;
	}

	public int fullSearch(String country, String compName) {
		// int retId = searchBinaryComp(country, compName);
		// if (retId > 0) {
		// if (ccasList.get(retId).getDb() == 1) {
		// return retId;
		// }
		// return -1;
		// } else {
		// return searchCompUsable(compName);
		// }
		return -1;
	}

	public void addAllowedComp(String comp, int idx) {
		allowedcomps.put(comp, idx);
		FileHandler fh= new FileHandler();
		try {
			fh.appendAllowedCompetitions(comp, idx);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
