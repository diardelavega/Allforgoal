package strategyAction;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import structures.CountryCompetition;
import dbtry.Conn;
import extra.StringSimilarity;
import extra.Unilang;
import basicStruct.CCAllStruct;
import basicStruct.MatchObj;

public class TempMatchFunctions {

	// private static final String ResultSet = null;
	private Unilang ul = new Unilang();
	public List<MatchObj> incomeTempMatchesList = new ArrayList<>();// temp
																	// matches
																	// list
	public List<MatchObj> readTempMatchesList = new ArrayList<>();// temp
																	// matches
																	// list
	private Conn conn;

	public void readTodaysMatches() {
		// TODO read matches of dat == todays Date
		// and put them to the tmList
	}

	public void readYesterdaysMatches() {
		// TODO read matches of dat == yesterdays Date
		// and put them to the tmList
	}

	public void corelatePunterXScorerTeams(List<MatchObj> ml) {
		/*
		 * for every xScorer team name in the list find the analog Punter
		 * teamName, populate unilang Teams with it and convert the list team
		 * names. For every new Match search if it is previously stored in
		 * unilang; otherwise search in the database to get the teams from a
		 * competition with the specific compId. compare the new teams with
		 * every team of the db competition and consider analog the one with the
		 * smaller levenstain distance.
		 */

		List<String> dbTeams = null;
		openDBConn();
		int compId = -1;

		for (MatchObj m : ml) {
			// first search in unilang
			String t = ul.scoreTeamToCcas(m.getT1());
			if (t == null) {
				if (m.getComId() != compId) {
					// if a new competition appears delete the previous teams
					dbTeams.clear();
					dbTeams = queryCompTeams(m.getComId());
					compId = m.getComId();
				}
				int minDist = 100;
				int chosenIdx = -1;
				int curDist;
				for (int i = 0; i < dbTeams.size(); i++) {
					curDist = StringSimilarity.levenshteinDistance(m.getT1(),
							dbTeams.get(i));
					if (curDist < minDist) {
						minDist = curDist;
						chosenIdx = i;
					}
				}
				// find the most similar terms; add the to Unilang; delete them
				// from dbTeamsList so to be more efficient in the next search.
				// change the team names on the list
				ul.addTerm(m.getT1(), dbTeams.get(chosenIdx));
				dbTeams.remove(chosenIdx);
				m.setT1(dbTeams.get(chosenIdx));
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
				int minDist = 100;
				int chosenIdx = -1;
				int curDist;
				for (int i = 0; i < dbTeams.size(); i++) {
					curDist = StringSimilarity.levenshteinDistance(m.getT2(),
							dbTeams.get(i));
					if (curDist < minDist) {
						minDist = curDist;
						chosenIdx = i;
					}
				}
				ul.addTerm(m.getT2(), dbTeams.get(chosenIdx));
				dbTeams.remove(chosenIdx);
				m.setT2(dbTeams.get(chosenIdx));
			} else {
				m.setT2(t);
			}
		}
		incomeTempMatchesList = ml;
		closeDBConn();
	}

	public void storeToTempMatchesDB() throws SQLException {
		// should come after the correlation from newFormat to punter
		// try {
		// insertMatches("tempmatches");
		// } catch (SQLException e) {
		// e.printStackTrace();
		// }
		//

		// openDBConn();
		String insert = "insert into tempmatches values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement ps = conn.getConn().prepareStatement(insert);
		int i = 0;
		for (MatchObj mobj : incomeTempMatchesList) {
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
		ps.executeBatch();

		// closeDBConn();
	}

	public void readFromTempMatches(LocalDate dat) throws SQLException {
		// intended to be used during periodic check for the finished matches
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
			// mobj.setmId(rs.getLong(1));
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
		}
	}

	public void complete(LocalDate dat) throws SQLException {
		/*
		 * intended to be used during the periodical check for results of
		 * matches. Supposedly the readTempMatchesKList is full of matches of a
		 * certain date ordered by home team for binary search. add results to
		 * the finished matches; store them to "regular" matches delete them
		 * from tempmatches db table
		 */
		readFromTempMatches(LocalDate.now());

		// keep the mid of the matches in the tempMatches db that will be
		// deleted
		StringBuilder sb = new StringBuilder();

		List<MatchObj> finMatches = new ArrayList<MatchObj>();
		for (MatchObj m : incomeTempMatchesList) {
			String team = ul.scoreTeamToCcas(m.getT1());
			if (team != null) {
				int idx = binarySearch(team, 0, readTempMatchesList.size());
				if (idx < 0) {
					continue;
				} else {
					MatchObj mobj = readTempMatchesList.get(idx);
					mobj.setHt1(m.getHt1());
					mobj.setHt2(m.getHt2());
					mobj.setFt1(m.getFt1());
					mobj.setFt2(m.getFt2());
					// {
					// mobj.set_1(m.get_1());
					// mobj.set_2(m.get_2());
					// mobj.set_x(m.get_x());
					// mobj.set_o(m.get_o());
					// mobj.set_u(m.get_u());
					// }
					finMatches.add(mobj);
					sb.append(mobj.getmId() + ", ");
				}
			}
		}// for
		sb.append("-1");

		conn.getConn()
				.createStatement()
				.execute(
						"DELETE FROM tempmatches where mid in ("
								+ sb.toString() + ");");
		insertMatches(finMatches);

	}

	private void insertMatches(List<MatchObj> finMatches) throws SQLException {
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
		if (min > max) {
			return -1;
		}
		int mid = (max + min) / 2;

		if (readTempMatchesList.get(mid).getT1().equals(team)) {
			return mid;
		} else if (readTempMatchesList.get(mid).getT1().compareTo(team) > 0) {
			return binarySearch(team, min, mid - 1);
		} else {
			return binarySearch(team, mid + 1, max);
		}
		// return -1;
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return teamsList;
	}

	public void openDBConn() {
		conn = new Conn();
		conn.open();
	}

	public void closeDBConn() {
		conn.close();
	}

}
