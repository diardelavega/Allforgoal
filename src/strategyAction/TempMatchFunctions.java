package strategyAction;

import java.io.IOException;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import scrap.XscoreUpComing;
import structures.CountryCompetition;
import test.MatchGetter;
import dbtry.Conn;
import extra.StringSimilarity;
import extra.Unilang;
import basicStruct.CCAllStruct;
import basicStruct.MatchObj;

public class TempMatchFunctions {

	public static final Logger logger = LoggerFactory
			.getLogger(TempMatchFunctions.class);
	private Unilang ul = new Unilang(); // Original_Line
	public List<MatchObj> incomeTempMatchesLists = new ArrayList<>();
	public List<MatchObj> readTempMatchesList = new ArrayList<>();

	private Conn conn;

	public void readTodaysMatches() {
		try {
			readFromTempMatches(LocalDate.now());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void readYesterdaysMatches() {
		try {
			readFromTempMatches(LocalDate.now().minusDays(1));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void corelatePunterXScorerTeams() throws IOException {
		/*
		 * for every xScorer team name in the list find the analog Punter
		 * teamName, populate unilang Teams with it and convert the list team
		 * names. For every new Match search if it is previously stored in
		 * unilang; otherwise search in the database to get the teams from a
		 * competition with the specific compId. compare the new teams with
		 * every team of the db competition and consider analog the one with the
		 * smaller levenstain distance.
		 */

		List<String> dbTeams = new ArrayList<>();
		int compId = -1;

//		for (Integer key : XscoreUpComing.schedNewMatches.keySet()) {
//			for (MatchObj m : XscoreUpComing.schedNewMatches.get(key)) {// Original_Line
		for (Integer key : MatchGetter.schedNewMatches.keySet()) {
				 for (MatchObj m : MatchGetter.schedNewMatches.get(key)) {
				// first search in unilang
				String t = ul.scoreTeamToCcas(m.getT1());
				if (t == null) {
					if (m.getComId() != compId) {
						// if a new competition appears delete the previous
						// teams
						dbTeams.clear();
						dbTeams = queryCompTeams(m.getComId());
						compId = m.getComId();
					}
					float minDist = 100;
					int chosenDbIdx = -1;
					float curDist;
					for (int i = 0; i < dbTeams.size(); i++) {
						curDist = StringSimilarity.levenshteinDistance(m.getT1(), dbTeams.get(i));
						logger.info("m.T1= '{}'    db.t= '{}'  curDist= {}  minDist= {}",m.getT1(), dbTeams.get(i), curDist, minDist);
						if (curDist >= m.getT2().length())
							continue;
						if (curDist < minDist) {
							minDist = curDist;
							chosenDbIdx = i;
						}
					}
					// find the most similar terms; add the to Unilang; delete
					// them
					// from dbTeamsList so to be more efficient in the next
					// search.
					// change the team names on the list
					logger.info("m.T1= '{}'    db.t= '{}' ", m.getT1(),
							dbTeams.get(chosenDbIdx));
					ul.addTeam(dbTeams.get(chosenDbIdx), m.getT1());
					m.setT1(dbTeams.get(chosenDbIdx));
					dbTeams.remove(chosenDbIdx);
				} else {
					m.setT1(t);
				}

				t = ul.scoreTeamToCcas(m.getT2());
				if (t == null) {
					if (m.getComId() != compId) {
						dbTeams.clear();
						dbTeams = queryCompTeams(m.getComId());
						compId = m.getComId();
					}
					float minDist = 100;
					int chosenIdx = -1;
					float curDist;
					for (int i = 0; i < dbTeams.size(); i++) {
						curDist = StringSimilarity.levenshteinDistance(
								m.getT2(), dbTeams.get(i));
						logger.info(
								"m.T1= '{}'    db.t= '{}'  curDist= {}  minDist= {}",
								m.getT2(), dbTeams.get(i), curDist, minDist);
						if (curDist >= m.getT2().length())
							continue;
						if (curDist < minDist) {
							minDist = curDist;
							chosenIdx = i;
						}
					}
					logger.info("m.T1= '{}'    db.t= '{}' ", m.getT1(),dbTeams.get(chosenIdx));
					ul.addTeam(dbTeams.get(chosenIdx), m.getT2());
					m.setT2(dbTeams.get(chosenIdx));
					dbTeams.remove(chosenIdx);
				} else {
					m.setT2(t);
				}
			}
		}
		// incomeTempMatchesLists = ml;
	}

	public void storeToTempMatchesDB() throws SQLException {
		logger.info("--------------: STORE To TEMPMATCHS");
		// should come after the correlation from newFormat to punter

		openDBConn();
		String insert = "insert into tempmatches values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement ps = conn.getConn().prepareStatement(insert);
		int i = 0;
		// @ this point suppose xscore.shcedmatches is converted to punter teams
		// for (MatchObj mobj : XscoreUpComing.schedNewMatches) {
		for (Integer key : MatchGetter.schedNewMatches.keySet()) {
			for (MatchObj mobj : MatchGetter.schedNewMatches.get(key)) {// alt
																		// line

				// ps.setNull(1, java.sql.Types.INTEGER);
				ps.setLong(1, mobj.getmId());
				ps.setInt(2, mobj.getComId());
				ps.setString(3, mobj.getT1());
				ps.setString(4, mobj.getT2());
				ps.setInt(5, mobj.getFt1());
				ps.setInt(6, mobj.getFt2());
				ps.setInt(7, mobj.getHt1());
				ps.setInt(8, mobj.getHt2());
				ps.setDouble(9, mobj.get_1());
				ps.setDouble(10, mobj.get_x());
				ps.setDouble(11, mobj.get_2());
				ps.setDouble(12, mobj.get_o());
				ps.setDouble(13, mobj.get_u());
				ps.setDate(14, mobj.getDat());
				ps.addBatch();
				i++;
				if (i % 500 == 0) {
					ps.executeBatch();
					i = 0;
				}
			}
		}
		ps.executeBatch();

		// closeDBConn();
	}

	public void readFromTempMatches(LocalDate dat) throws SQLException {
		// intended to be used during periodic check for the finished matches
		logger.info("--------------: Read From TempMatches");

		openDBConn();
		Date date = Date.valueOf(dat);
		ResultSet rs = conn
				.getConn()
				.createStatement()
				.executeQuery(
						"SELECT * FROM tempmatches where dat ='" + date
								+ "' order by t1 ;");

		MatchObj mobj = null;
		while (rs.next()) {
			mobj = new MatchObj();
			mobj.setmId(rs.getLong(1));
			mobj.setComId(rs.getInt(2));
			mobj.setT1(rs.getString(3));
			mobj.setT2(rs.getString(4));
			mobj.setFt1(rs.getInt(5));
			mobj.setFt2(rs.getInt(6));
			mobj.setHt1(rs.getInt(7));
			mobj.setHt2(rs.getInt(8));
			mobj.set_1(rs.getFloat(9));
			mobj.set_x(rs.getFloat(10));
			mobj.set_2(rs.getFloat(11));
			mobj.set_o(rs.getFloat(12));
			mobj.set_u(rs.getFloat(13));
			mobj.setDat(rs.getDate(14));
			readTempMatchesList.add(mobj);
			logger.info("t1-: {}   t1-: {}", rs.getString(3), rs.getString(4));
		}
		logger.info("readTempMatchesList size ={}", readTempMatchesList.size());
	}

	public void complete(LocalDate d) throws SQLException {
		/*
		 * intended to be used during the periodical check for results of
		 * matches. Supposedly the readTempMatchesList is full of matches of a
		 * certain date ordered by home team for binary search. add results to
		 * the finished matches; store them to "regular" matches delete them
		 * from tempmatches db table. This func should be called after Xcore
		 * class functions have gathered the scores of the finished matches
		 */

		logger.info("--------------: COMPLETE");
		openDBConn();

		// fill from db readTempMatchesList List<>, order by t1
		readFromTempMatches(d);
		logger.info("readTempMatchesList size ={}", readTempMatchesList.size());

		List<MatchObj> matches = new ArrayList<MatchObj>();
		// for (MatchObj m : XscoreUpComing.finNewMatches) { Original_Line
		int idx = -1;
		for (MatchObj m : MatchGetter.finNewMatches) {
			// String team = ul.scoreTeamToCcas(m.getT1()); Original_Line
			String team = m.getT1();
			if (team != null) {
				if (readTempMatchesList.size() == 0) {
					break;
				}
				idx = binarySearch(team, 0, readTempMatchesList.size());
				if (idx < 0) {
					continue;
				} else {
					logger.info("t1 -{}     t1-{}", m.getT1(), m.getT2());
					MatchObj mobj = readTempMatchesList.get(idx);
					mobj.setHt1(m.getHt1());
					mobj.setHt2(m.getHt2());
					mobj.setFt1(m.getFt1());
					mobj.setFt2(m.getFt2());
					matches.add(mobj);
					readTempMatchesList.remove(idx);// for efficiency
					mobj.printMatch();
					logger.info("Competed");
				}
			}
		}// for

		deleteTempMatches(matches);// delete finished matches
		insertMatches(matches);

		if (readTempMatchesList.size() > 0) {
			logger.info("--------------: LOOP TO find postponed");
			/*
			 * keep looping through the remaining matches from the db read to
			 * delete the postponed or cancelled matches
			 */
			// for (MatchObj m : XscoreUpComing.errorNewMatches) {
			for (MatchObj m : MatchGetter.errorNewMatches) {
				// String team = ul.scoreTeamToCcas(m.getT1()); Original_Line
				String team = m.getT1();
				if (team != null) {
					if (readTempMatchesList.size() == 0) {
						break;
					}
					idx = binarySearch(team, 0, readTempMatchesList.size());
					if (idx < 0) {
						continue;
					} else {
						MatchObj mobj = readTempMatchesList.get(idx);
						matches.add(mobj);
						mobj.printMatch();
						logger.info("Errores");
					}
				}
			}// for

			deleteTempMatches(matches);// delete cancelled matches
		}
	}

	public void completeYesterday() throws SQLException {
		LocalDate dat = LocalDate.now().minusDays(1);
		complete(dat);
	}

	public void completeToday() throws SQLException {
		LocalDate dat = LocalDate.now();
		complete(dat);
	}

	public List<String> getTempDates() throws SQLException {
		// get different dates of matches in temp matches
		openDBConn();
		ResultSet rs = conn
				.getConn()
				.createStatement()
				.executeQuery(
						"SELECT dat FROM tempmatches GROUP BY dat ORDER BY dat asc ; ");
		List<String> dats = new ArrayList<String>();
		while (rs.next()) {
			dats.add(rs.getString(1));
		}
		closeDBConn();
		return dats;
	}

	public boolean deleteMatches(LocalDate dat) throws SQLException {
		boolean b = conn
				.getConn()
				.createStatement()
				.execute(
						"DELETE FROM tempmatches where dat like '"
								+ Date.valueOf(dat) + "');");
		return b;
	}

	private boolean deleteTempMatches(List<MatchObj> ml) throws SQLException {
		logger.info("--------------: DELETE FROM TempMatches");
		StringBuilder sb = new StringBuilder();
		for (MatchObj m : ml) {
			sb.append(m.getmId() + ", ");
		}
		sb.append("-1");
		boolean b = conn
				.getConn()
				.createStatement()
				.execute(
						"DELETE FROM tempmatches where mid in ("
								+ sb.toString() + ");");
		return b;
	}

	private void insertMatches(List<MatchObj> finMatches) throws SQLException {
		logger.info("--------------: INSERT to MATCHESS");
		String insert = "insert into matches values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement ps = conn.getConn().prepareStatement(insert);
		int i = 0;
		for (MatchObj mobj : finMatches) {
			ps.setNull(1, java.sql.Types.INTEGER);
			// ps.setLong(1, mobj.getmId());
			ps.setInt(2, mobj.getComId());
			ps.setString(3, mobj.getT1());
			ps.setString(4, mobj.getT2());
			ps.setInt(5, mobj.getFt1());
			ps.setInt(6, mobj.getFt2());
			ps.setInt(7, mobj.getHt1());
			ps.setInt(8, mobj.getHt2());
			ps.setDouble(9, mobj.get_1());
			ps.setDouble(10, mobj.get_x());
			ps.setDouble(11, mobj.get_2());
			ps.setDouble(12, mobj.get_o());
			ps.setDouble(13, mobj.get_u());
			ps.setDate(14, mobj.getDat());
			ps.addBatch();
			i++;
			if (i % 500 == 0) {
				ps.executeBatch();
				i = 0;
			}
		}
		ps.executeBatch();

	}

	private int binarySearch(String team, int min, int max) {
		try {
			logger.info("t__{}  min_{}   max__{}", team, min, max);
			if (min > max) {
				return -1;
			}
			int mid = (max + min) / 2;
			logger.warn("mid__{}", mid);
			if (readTempMatchesList.get(mid).getT1().equals(team)) {
				return mid;
			} else if (readTempMatchesList.get(mid).getT1().compareTo(team) > 0) {
				return binarySearch(team, min, mid - 1);
			} else {
				return binarySearch(team, mid + 1, max);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.info("t__{}  min_{}   max__{},  readTempMatchesList-:{}",
					team, min, max, readTempMatchesList.size());
			e.printStackTrace();
		}
		// return -1;
		return -1;
	}

	private List<String> queryCompTeams(int compId) {
		// sql get team names of a competition
		String tab = CountryCompetition.ccasList.get(compId - 1)
				.getCompetition();
		tab = tab.toLowerCase().replaceAll(" ", "_") + "fulltable";
		List<String> teamsList = new ArrayList<String>();
		ResultSet rs;
		try {
			rs = conn.getConn().createStatement()
					.executeQuery("SELECT team from " + tab + " ;");

			while (rs.next()) {
				teamsList.add(rs.getString("team"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return teamsList;
	}

	public void openDBConn() throws SQLException {
		if (conn == null) {
			conn = new Conn();
			conn.open();
		} else if (conn.getConn().isClosed()) {
			conn.open();
		}
	}

	public void closeDBConn() {
		if (conn != null) {
			conn.close();
		}
	}

}
