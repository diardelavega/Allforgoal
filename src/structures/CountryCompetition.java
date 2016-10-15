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

import basicStruct.CCAllStruct;
import basicStruct.ScorerDataStruct;

import com.google.gson.JsonSyntaxException;

import dbtry.Conn;
import diskStore.FileHandler;
import extra.StandartResponses;
import extra.StringSimilarity;

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
	public static Map<Integer, Integer> idToIdx = new HashMap<Integer, Integer>();
	public static List<CCAllStruct> ccasList = new ArrayList<>();
	public static List<ScorerDataStruct> sdsList = new ArrayList<>();
	public static Map<String, Integer> allowedcomps = new HashMap<>();
	public static List<String> notAllowedcomps = new ArrayList<>();
	FileHandler fh = new FileHandler();

//	// TODO implement lists of competition ids for yesterday, today & tomorrows
//	public static List<Integer> yesterdayComps = new ArrayList<>();
//	public static List<Integer> todayComps = new ArrayList<>();
//	public static List<Integer> tomorrowComps = new ArrayList<>();

	// --------------------------

	public CountryCompetition() {
		super();
	}

	public void readsdStruct(Connection conn) throws SQLException {
		Statement st = conn.createStatement();
		ResultSet rs = st
				.executeQuery("SELECT * FROM scoreDataStruct ORDER BY country ;");
		ScorerDataStruct sds;
		while (rs.next()) {
			sds = new ScorerDataStruct();
			sds.setCompId(rs.getInt("compid"));
			sds.setCountry(rs.getString("country"));
			sds.setCompetition(rs.getString("competition"));
			sds.setDb(rs.getInt("db"));
			sdsList.add(sds);
		}
		try {
			st.close();
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void readCCAllStruct(Connection conn) throws SQLException {
		// read from db and insert to list;
		// possible necessity of some ordering; COUNTRIES are already ordered
		Statement st = conn.createStatement();
		ResultSet rs = st
				.executeQuery("SELECT * FROM ccallstruct ORDER BY country;");
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
		// FileHandler fh = new FileHandler();
		fh.readAllowedCompetitions();
	}

	public void readNotAllowedComps() throws JsonSyntaxException, IOException {
		// FileHandler fh = new FileHandler();
		fh.readNotAllowedCompetitions();
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

		int idx = binarySearchCountry(country, 0, ccasList.size() - 1);
		if (idx < 0) {// country not found
			return idx;
		} else {// country found, search for competition
			idx = smallCompSearch(idx, country, compName, b);
			// allowedcomps.put(compName, idx);
			return idx;

		}
	}

	public int searchCompBinaryLevel(String country, int level) {
		int idx = binarySearchCountry(country, 0, ccasList.size() - 1);
		if (idx < 0)
			return idx;

		int i = idx;
		while (ccasList.get(i).getCountry().equalsIgnoreCase(country)) {
			if (ccasList.get(i).getLevel() == level
					&& ccasList.get(i).getDb() == 1) {
				return i;
			}
			i++;
		}
		i = idx;
		while (ccasList.get(i).getCountry().equalsIgnoreCase(country)) {
			if (ccasList.get(i).getLevel() == level
					&& ccasList.get(i).getDb() == 1) {
				return i;
			}
			i--;
		}
		return -1;
	}

	private int smallCompSearch(int initial, String country, String comp,
			boolean b) {
		int i = initial;
		if (b) {// with levistein
			while (ccasList.get(i).getCountry().equalsIgnoreCase(country)) {
				if (ccasList.get(i).getDb() == 1)
					if (StringSimilarity.levenshteinDistance(ccasList.get(i)
							.getCompetition(), comp) <= StandartResponses.LEV_DISTANCE) {
						return i;
					}
				i++;
				if (i >= ccasList.size()) {
					return -1;
				}
			}

			// do not search below idx 0
			if (initial == 0)
				return -1;

			i = initial;
			while (ccasList.get(i).getCountry().equalsIgnoreCase(country)) {
				if (ccasList.get(i).getDb() == 1)
					if (StringSimilarity.levenshteinDistance(ccasList.get(i)
							.getCompetition(), comp) <= StandartResponses.LEV_DISTANCE) {
						return i;
					}
				i--;
				if (i < 0) {
					return -1;
				}
			}
		} else {// no levestein
			while (ccasList.get(i).getCountry().equalsIgnoreCase(country)) {
				if (ccasList.get(i).getDb() == 1)
					if (ccasList.get(i).getCompetition().equalsIgnoreCase(comp)) {
						return i;
					}
				i++;
				if (i >= ccasList.size()) {
					return -1;
				}
			}
			// do not search below idx 0
			if (initial == 0)
				return -1;

			i = initial;
			while (ccasList.get(i).getCountry().equalsIgnoreCase(country)) {
				if (ccasList.get(i).getDb() == 1)
					if (ccasList.get(i).getCompetition().equalsIgnoreCase(comp)) {
						return i;
					}
				i--;
				if (i < 0) {
					return -1;
				}
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

		// logger.info("{} - {} ", ccasList.get(mid).getCountry().toLowerCase(),
		// country.toLowerCase());
		if (ccasList.get(mid).getCountry().equalsIgnoreCase(country)) {
			return mid;
		} else if (ccasList.get(mid).getCountry().toLowerCase()
				.compareTo(country.toLowerCase()) > 0) {
			return binarySearchCountry(country, min, mid - 1);
		} else {
			return binarySearchCountry(country, mid + 1, max);
		}
	}

	// ---------SCORER SEARCH------------------------
	public int scorerCompIdSearch(String country, String competition) {

		int idx = binaryScorerSearchCountry(country, 0, sdsList.size() - 1);
		if (idx < 0) {// country not found
			return idx;
		} else {// country found, search for competition
			idx = smallScorerCompidxSearch(idx, country, competition);
			// allowedcomps.put(compName, idx);
			return idx;
		}
	}

	public int scorerFullSearch(String country, String competition) {
		/*
		 * search the whole db table to find the competition. Since the Xscore
		 * has many competitions with the same name, and some competitions with
		 * alternative names wi use the country aswell to uniquely specify the
		 * competition. Also we use the levenstain distance for all the
		 * alternative competitions names a competition might have
		 */
		for (int i = 0; i < sdsList.size(); i++) {
			if (StringSimilarity.levenshteinDistance(sdsList.get(i)
					.getCountry(), country) <= StandartResponses.LEV_DISTANCE) {
				for (String s : sdsList.get(i).altComps()) {
					if (StringSimilarity.levenshteinDistance(s, competition) <= StandartResponses.LEV_DISTANCE) {

					}
				}

			}

		}
		return -1;
	}

	private int binaryScorerSearchCountry(String country, int min, int max) {
		/*
		 * the idx might bee a few positions away from compId because we renamed
		 * the republic korea to south korea keeping the compid similar to the
		 * soccer punter comp id for the analog competition
		 */
		if (min > max) {
			return -1;
		}
		int mid = (max + min) / 2;
		// logger.info("{} - {} ", sdsList.get(mid).getCountry(), country);
		if (sdsList.get(mid).getCountry().equalsIgnoreCase(country)) {
			return mid;
		} else if (sdsList.get(mid).getCountry().toUpperCase()
				.compareTo(country.toUpperCase()) > 0) {
			return binaryScorerSearchCountry(country, min, mid - 1);
		} else {
			return binaryScorerSearchCountry(country, mid + 1, max);
		}
	}

	private int smallScorerCompidxSearch(int initial, String country,
			String comp) {
		/* a small loop for all the competitions a country might have */
//		logger.info("{}", initial);
		int i = initial;
		// with levistein
		while (sdsList.get(i).getCountry().equalsIgnoreCase(country)) {
			if (sdsList.get(i).getDb() == 1)
				for (String s : sdsList.get(i).altComps()) {
					float d = StringSimilarity.levenshteinDistance(s, comp);
					// logger.info("----------------{} - {}, {}",s,comp,d);
					if (d <= StandartResponses.LEV_DISTANCE) {
						return i;
					}
				}
			i++;
			if (i >= sdsList.size()) {
				return -1;
			}
		}

		// if the idx was 0, it can not search in idx <0
		if (initial == 0)
			return -1;

		i = initial - 1;
		while (sdsList.get(i).getCountry().equalsIgnoreCase(country)) {
			if (sdsList.get(i).getDb() == 1)
				for (String s : sdsList.get(i).altComps()) {
					float d = StringSimilarity.levenshteinDistance(s, comp);
					// logger.info("---------------{} - {}, {}",s,comp,d);
					if (d <= StandartResponses.LEV_DISTANCE) {
						return i;
					}
				}
			i--;
			if (i < 0) {
				return -1;
			}
		}
		return -1;
	}

	// -----------------------------------------------------------
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
		float minDist = 100;// var to keep the min distance
		float dist = 0; // temporary distance var

		for (int i = 0; i < ccasList.size(); i++) {
			// if (ccasList.get(i).getDb() == 1) {//Original_Line
			if (b) {
				dist = StringSimilarity.levenshteinDistance(compName, ccasList
						.get(i).getCompetition());
				if (dist <= 2) {
					return i;
				} else {
					if (dist < minDist) {
						minDist = dist;
						minDistanceCompIdx = i;
					}
				} // else
			} else {
				if (compName.equalsIgnoreCase(ccasList.get(i).getCompetition())) {
					return i; // IDX
				}
			}
			// } // db ==1 //Original_Line
		}// for

		return minDistanceCompIdx;// IDX
	}

	public void addAllowedComp(String comp, int idx) {
		allowedcomps.put(comp, idx);

		try {
			fh.appendAllowedCompetitions(comp, idx);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void addNotAllowedComp(String comp) {
		notAllowedcomps.add(comp);

		try {
			fh.appendNotAllowedCompetitions(comp);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
