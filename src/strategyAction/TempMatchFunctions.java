package strategyAction;

import java.io.IOException;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import r_dataIO.ReadPrediction;
import structures.CountryCompetition;
import structures.TimeVariations;
import test.MatchGetter;
import basicStruct.CCAllStruct;
import basicStruct.FullMatchLine;
import basicStruct.FullMatchPredLineToSubStructs;
import basicStruct.MatchObj;
import calculate.MatchToTableRenewal;
import dbtry.Conn;
import extra.StandartResponses;
import extra.Status;
import extra.StringSimilarity;
import extra.Unilang;

public class TempMatchFunctions {

	public static final Logger logger = LoggerFactory
			.getLogger(TempMatchFunctions.class);
	public static List<Integer> skipDayCompIds = new ArrayList<>();

	private Unilang ul = new Unilang(); // Original_Line
	// public List<MatchObj> incomeTempMatchesListss = new ArrayList<>();
	public List<MatchObj> readTempMatchesList = new ArrayList<>();
	public List<FullMatchLine> readRecentMatchesList = new ArrayList<>();

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
		logger.info("Corelating");
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
		int chosenDbIdx1 = -1, chosenDbIdx2 = -1;
		for (Integer key : MatchGetter.schedNewMatches.keySet()) {
			for (int kk = 0; kk < MatchGetter.schedNewMatches.get(key).size(); kk++) {
				MatchObj m = MatchGetter.schedNewMatches.get(key).get(kk);
				// first search in unilang
				String t = ul.scoreTeamToCcas(m.getT1());
				if (t == null) {
					if (m.getComId() != compId) {
						// on new compId appears delete the previous comp teams
						dbTeams.clear();
						dbTeams = queryCompTeams(m.getComId());
						if (dbTeams == null) {// not available competition
							continue;
						}
						compId = m.getComId();
					}
					float minDist = 100;
					float curDist;
					for (int i = 0; i < dbTeams.size(); i++) {
						// iterate through all teams of that competition
						curDist = StringSimilarity.levenshteinDistance(
								m.getT1(), dbTeams.get(i));
						logger.info(
								"m.T1= '{}'    db.t= '{}'  curDist= {}  minDist= {}",
								m.getT1(), dbTeams.get(i), curDist, minDist);
						if (curDist > StandartResponses.TEAM_DIST)
							continue;
						if (curDist < minDist) {
							minDist = curDist;
							chosenDbIdx1 = i;
						}
					}// for dbteams

					/*
					 * find the most similar terms; add the to Unilang; delete
					 * them from dbTeamsList so to be more efficient in the next
					 * search. change the team names on the list
					 */
					if (chosenDbIdx1 >= 0) {// if found coreleted team
						logger.info("m.T1= '{}'    db.t= '{}' ", m.getT1(),
								dbTeams.get(chosenDbIdx1));
						ul.addTeam(dbTeams.get(chosenDbIdx1), m.getT1());
						MatchGetter.schedNewMatches.get(key).get(kk)
								.setT1(dbTeams.get(chosenDbIdx1));
						dbTeams.remove(chosenDbIdx1);
					} else {
//						logger.info("uncorelated --:  m.T1= '{}'", m.getT1());
					}
				} else {
					MatchGetter.schedNewMatches.get(key).get(kk).setT1(t);
				}
				// second team corelation search
				t = ul.scoreTeamToCcas(m.getT2());
				if (t == null) {
					float minDist = 100;
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
							chosenDbIdx2 = i;
						}
					}

					if (chosenDbIdx2 >= 0) {
						logger.info("m.T2= '{}'    db.t= '{}' ", m.getT2(),
								dbTeams.get(chosenDbIdx2));
						ul.addTeam(dbTeams.get(chosenDbIdx2), m.getT2());
						MatchGetter.schedNewMatches.get(key).get(kk)
								.setT2(dbTeams.get(chosenDbIdx2));
						dbTeams.remove(chosenDbIdx2);
					} else {
//						logger.info("uncorelated --:  m.T2= '{}'", m.getT2());
					}
				} else {
					MatchGetter.schedNewMatches.get(key).get(kk).setT2(t);
				}
			}
		}
	}

	public void complete(LocalDate d) throws SQLException {
		// TODO maybe to store in the temp matches the teams in scorer format
		// instead of punter so that to have faster acces
		// in case of comparison; without going through unilang conversion// ??
		// ??maybe they are kep in punter format for the oddsadders

		/*
		 * intended to be used during the periodical check for results of
		 * matches. Supposedly the readTempMatchesList is full of matches of a
		 * certain date ordered by home team for binary search. add results to
		 * the finished matches; store them to "regular" matches delete them
		 * from tempmatches db table. This func should be called after Xscore
		 * class functions have gathered the scores of the finished matches
		 */

		logger.info("--------------: COMPLETE");
		// fill from db the readTempMatchesList List<>, order by t1
		// matches in temp & recent db tabs is in the scorer syntax
		readFromTempMatches(d);
		if (readTempMatchesList.size() == 0) {
			logger.info("No temp matches in db");
			return;
		}

		/*
		 * will keep all the selected matches. Thean delete them from temp
		 * matches table & insert to regular matches dbtable
		 */
		List<MatchObj> matches = new ArrayList<MatchObj>();// all matches

		/*
		 * will contain at any time match(es) from the same competition, matches
		 * previously in the temp match db and test prediction file but now with
		 * the score results. They will be calculated and inserted in the
		 * competitions Train prediction file with all the matches data so far.
		 */
		// same comp matches of consecutive compids during search loops
		// used for test prediction file
		List<MatchObj> smallPredictionsList = new ArrayList<MatchObj>();
		// for (MatchObj m : XscoreUpComing.finNewMatches) { Original_Line
		int idx = -1;
		int prevCompId = -1;
		for (MatchObj m : MatchGetter.finNewMatches) {
			// String team = ul.scoreTeamToCcas(m.getT1()); Original_Line
			String team = ul.scoreTeamToCcas(m.getT1());
			if (team != null) {
				idx = binarySearch(readTempMatchesList, team);
				if (idx < 0) {
					logger.info("---Was not found t1 -{},  t2 {}    ",
							m.getT1(), m.getT2());
					continue;
				} else {
					if (prevCompId != m.getComId()) {
						prevCompId = m.getComId();
						if (smallPredictionsList.size() > 0) {
							predictionDataSet(smallPredictionsList);
							smallPredictionsList.clear();
						}
					}
					MatchObj mobj = readTempMatchesList.get(idx);
					mobj.setHt1(m.getHt1());
					mobj.setHt2(m.getHt2());
					mobj.setFt1(m.getFt1());
					mobj.setFt2(m.getFt2());

					smallPredictionsList.add(mobj);
					matches.add(mobj);
					readTempMatchesList.remove(idx);// for efficiency
				}
			} else {// if team not converted
				logger.info(
						"disply unconverted, Not found in temp matches {} - {}, __{}",
						m.getT1(), m.getT2(), m.getComId());
			}
		}// for

		deleteTempMatches(matches);// delete finished matches from tempdb
		insertMatches(matches);// ins finished matches from tempdb to matchesdb
		updateRecentScores(matches);// set score to recent matches
		synchronizeMPL_Map(matches,"score");
		logger.info("Competed standart Completion");

		if (readTempMatchesList.size() > 0) {
			/*
			 * it means that in the temporary matches table tempmatches there
			 * are still matches unfinished or not properly finished, errors
			 * canceled postponed etc; keep looping through the remaining
			 * matches to account for the remaining ones
			 */
			logger.info("--------------: LOOP TO find postponed");
			matches.clear();
			smallPredictionsList.clear();
			for (MatchObj m : MatchGetter.errorNewMatches) {
				String team = ul.scoreTeamToCcas(m.getT1());
				if (team != null) {
					idx = binarySearch(readTempMatchesList, team);
					if (idx < 0) {
						logger.info(
								"--- broken ones loop; Was not found t1 -{},  t2 {}    ",
								m.getT1(), m.getT2());
						continue;
					} else {
						// if (prevCompId != m.getComId()) {
						// prevCompId = m.getComId();
						// if (smallPredictionsList.size() > 0) {
						/*
						 * error matches will not be added to the prediction
						 * file
						 */// predictionDataSet(smallPredictionsList);
							// smallPredictionsList.clear();
						// }
						// }
						MatchObj mobj = readTempMatchesList.get(idx);
						/*
						 * since the matches were canceled we dont need to
						 * asigne scores to them
						 */
						// mobj.setHt1(m.getHt1());
						// mobj.setHt2(m.getHt2());
						// mobj.setFt1(m.getFt1());
						// mobj.setFt2(m.getFt2());
						mobj.setMatchTime("err");

						// smallPredictionsList.add(mobj);
						matches.add(mobj);
					}
				}
			}// for
				// delete cancelled matches from tempmatches table
			deleteTempMatches(matches);
			updateRecentError(matches);// set err to recent matches time
			synchronizeMPL_Map(matches,"error");
			logger.info("Competed Old Completion");
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

	// //////////////////////////////////////////
	// to write and read in the temp and recent matches db tables
	public void storeToTempMatchesDB() throws SQLException {
		// /*
		// * store the matches scheduled for today and tomorrow in the temporary
		// * database table
		// */
		String insert = "insert into tempmatches values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		storeToShortMatches(insert);
	}

	public void readFromTempMatches(LocalDate dat) throws SQLException {
		// intended to be used during periodic check for the finished matches
		logger.info("--------------: Read From TempMatches");
		Date date = Date.valueOf(dat);
		String sql = "SELECT * FROM  tempmatches  where dat ='" + date
				+ "' order by t1 ;";

		readFromShortMatches(sql, "tempmatches");

	}

	// //////////////////////////////////////////
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
		/* delete finished matches from the temp matches db */
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
			// TODO consider for match time element addition #ta3
			ps.addBatch();
			i++;
			if (i % 500 == 0) {
				ps.executeBatch();
				i = 0;
			}
		}
		ps.executeBatch();

	}

	private List<String> queryCompTeams(int compId) {
		/*
		 * get the team names for a certain competition
		 */

		int idx = CountryCompetition.idToIdx.get(compId);
		CCAllStruct cc = CountryCompetition.ccasList.get(idx);
		String tab_competition = (cc.getCompetition() + "$" + cc.getCountry());
		// else
		tab_competition = tab_competition.toLowerCase().replace(".", "")
				.replaceAll(" ", "_")
				+ "_fulltable";
		List<String> teamsList = new ArrayList<String>();
		ResultSet rs;
		try {

			rs = conn.getConn().createStatement()
					.executeQuery("SELECT team from " + tab_competition + " ;");

			while (rs.next()) {
				teamsList.add(rs.getString("team"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return teamsList;
	}

	// ********************Secondary/Common
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

	private void predictionDataSet(List<MatchObj> predictionsList) {
		/*
		 * add the concludet matches and the updated attributes corresponding to
		 * them to the Prediction training file
		 */
		MatchToTableRenewal mttr = new MatchToTableRenewal();
		try {
			mttr.calculate(predictionsList);
		} catch (SQLException | IOException e) {
			e.printStackTrace();
		}
	}

	private int binarySearch(List<MatchObj> list, String team) {
		int min = 0;
		int max = list.size() - 1;
		for (;;) {
			if (min > max) {
				return -1;
			}
			int mid = (max + min) / 2;

			if (list.get(mid).getT1().equals(team)) {
				return mid;
			} else if (list.get(mid).getT1().compareTo(team) > 0) {
				max = mid - 1;
			} else {
				min = mid + 1;
			}
		}
	}

	private void storeToShortMatches(String insertLine) throws SQLException {
		/*
		 * store the matches scheduled for today and tomorrow in the temporary
		 * database table. which one <temp> or <recent> is decided by the
		 * insertLine parameter
		 */
		logger.info("--------------: STORE To SHORT-MATCHS");
		// should come after the correlation from newFormat to punter

		openDBConn();
		// String insert =
		// "insert into tempmatches values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement ps = conn.getConn().prepareStatement(insertLine);
		int i = 0;
		// @ this point suppose xscore.shcedmatches is converted to punter teams
		// for (MatchObj mobj : XscoreUpComing.schedNewMatches) {
		for (Integer key : MatchGetter.schedNewMatches.keySet()) {
			for (MatchObj mobj : MatchGetter.schedNewMatches.get(key)) {// alt
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
				ps.setString(15, mobj.getMatchTime());
				// TODO consider for match time element addition #ta1
				ps.addBatch();
				i++;
				if (i % 500 == 0) {
					ps.executeBatch();
					i = 0;
				}
			}
		}
		ps.executeBatch();

		closeDBConn();

	}

	private void readFromShortMatches(String sql, String tab)
			throws SQLException {
		openDBConn();
		ResultSet rs = conn.getConn().createStatement().executeQuery(sql);

		List<MatchObj> mali = new ArrayList<>();
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
			mobj.setMatchTime(rs.getString(15));
			mali.add(mobj);
		}
		switch (tab) {
		case "tempmatches":
			readTempMatchesList.addAll(mali);
			logger.info("read-" + tab + "-MatchesList size ={}",
					readTempMatchesList.size());
			break;
		case "recentmatches":
			FullMatchPredLineToSubStructs fmplss = new FullMatchPredLineToSubStructs();
			readRecentMatchesList.addAll(fmplss.mobjToFullMatchLine(mali));
			logger.info("read-" + tab + "-MatchesList size ={}",
					readTempMatchesList.size());
			break;
		default:
			logger.warn("readfrom recent matches .. some error ocoured!");
			break;
		}
		closeDBConn();
	}

	public void readDaySkips() throws SQLException {
		/*
		 * create a list of competition ids with competitions that started
		 * yesterday but will finish today
		 */

		Date date = Date.valueOf(LocalDate.now().minusDays(1));
		openDBConn();
		conn.open();
		ResultSet rs = conn
				.getConn()
				.createStatement()
				.executeQuery(
						"SELECT compid FROM tempmatch WHERE dat = '" + date
								+ "'");
		while (rs.next()) {
			skipDayCompIds.add(rs.getInt(1));
		}

		closeDBConn();
	}

	// ********************RECENT MATCHES

	public void storeToRecentMatchesDB() throws SQLException {
		String insert = "insert into recentmatches values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		storeToShortMatches(insert);
	}

	public List<FullMatchLine> readInitialCompFromRecentMatches(LocalDate dat)
			throws SQLException {
		// read the data match obj atts, before the addition on the pred points
		Date date = Date.valueOf(dat);
		String sql = "SELECT * FROM  recentmatches  where dat ='" + date
				+ "' order by compid ;";
		readFromShortMatches(sql, "recentmatches");
		return readRecentMatchesList;
	}

	public List<FullMatchLine> readInitialTeamFromRecentMatches(LocalDate dat)
			throws SQLException {
		// read the data match obj atts, before the addition on the pred points
		Date date = Date.valueOf(dat);
		String sql = "SELECT * FROM  recentmatches  where dat ='" + date
				+ "' order by t1 ;";
		readFromShortMatches(sql, "recentmatches");
		return readRecentMatchesList;
	}

	public List<FullMatchLine> readFullFromRecentMatches(LocalDate dat)
			throws SQLException {
		// read the all the data of the db. The match obj atts and base red atts
		// so: teams odds, scores, pred points. After the update of the tabel.
		// use to read and fill the maps for the client side matches

		openDBConn();
		Date date = Date.valueOf(dat);
		ResultSet rs = conn
				.getConn()
				.createStatement()
				.executeQuery(
						"SELECT * FROM recentmatches where dat ='" + date
								+ "' order by t1 ;");

		List<FullMatchLine> fmli = new ArrayList<>();
		FullMatchLine f = null;
		while (rs.next()) {
			f = new FullMatchLine();
			f.setmId(rs.getLong(1));
			f.setComId(rs.getInt(2));
			f.setT1(rs.getString(3));
			f.setT2(rs.getString(4));
			f.setFt1(rs.getInt(5));
			f.setFt2(rs.getInt(6));
			f.setHt1(rs.getInt(7));
			f.setHt2(rs.getInt(8));
			f.set_1(rs.getFloat(9));
			f.set_x(rs.getFloat(10));
			f.set_2(rs.getFloat(11));
			f.set_o(rs.getFloat(12));
			f.set_u(rs.getFloat(13));
			f.setDat(rs.getDate(14));
			f.setMatchTime(rs.getString(15));
			f.setH1(rs.getFloat(16));
			f.setHx(rs.getFloat(17));
			f.setH2(rs.getFloat(18));
			f.setSo(rs.getFloat(19));
			f.setSu(rs.getFloat(20));
			f.setP1y(rs.getFloat(21));
			f.setP1n(rs.getFloat(22));
			f.setP2y(rs.getFloat(23));
			f.setP2n(rs.getFloat(24));
			f.setHt(rs.getInt(25));
			f.setFt(rs.getInt(26));
			fmli.add(f);
		}
		closeDBConn();
		return fmli;
	}

	public boolean deleteFromRecentMatches(LocalDate ld) throws SQLException {
		openDBConn();
		boolean b = conn
				.getConn()
				.createStatement()
				.execute(
						"DELETE FROM recentmatches where dat = ("
								+ Date.valueOf(ld.minusDays(3)) + ");");
		closeDBConn();
		return b;
	}

	public boolean deleteFromRecentMatches() throws SQLException {
		/*
		 * from recentmatches delete matches 3 days older than the suplied date
		 * (supposedly the dupplied date is today at 00:00 , when the date
		 * changes)
		 */
		openDBConn();
		boolean b = conn
				.getConn()
				.createStatement()
				.execute(
						"DELETE FROM recentmatches where dat = ("
								+ Date.valueOf(LocalDate.now().minusDays(3))
								+ ");");
		closeDBConn();
		return b;
	}

	public void addPredPoints(List<Integer> compsList) throws IOException,
			SQLException {
		addPredPoints(compsList, null);
	}

	public void addPredPoints(List<Integer> compsList, LocalDate ld)
			throws IOException, SQLException {
		/*
		 * add the prediction points, as generated and stored in files by R,
		 * from file to recent table
		 */
		if (ld == null) {
			ld = LocalDate.now();
		}

		FullMatchPredLineToSubStructs fmplss = new FullMatchPredLineToSubStructs();
		// read predictions from the pred files
		ReadPrediction rp = new ReadPrediction();
		rp.prediction(compsList);
		// read matches from the recent table
		readInitialTeamFromRecentMatches(ld);
		// TODO .. read fore more than just todays date

		// find the same matches from table and file
		for (Integer key : rp.getMatchLinePred().keySet()) {
			for (int i = 0; i < rp.getMatchLinePred().get(key).size(); i++) {
				int ind = binarySearch(
						fmplss.fullMatchPredLineToMatchObj(readRecentMatchesList),
						rp.getMatchLinePred().get(key).get(i).getT1());
				if (ind == -1) {
					// todo dysplay error and
					logger.info("unfound relation file & db {}", rp
							.getMatchLinePred().get(key).get(i).liner());
					continue;
				}
				// make sure both teams correspond
				if (rp.getMatchLinePred()
						.get(key)
						.get(i)
						.getT2()
						.equalsIgnoreCase(
								readRecentMatchesList.get(ind).getT2())) {
					FullMatchLine temp = readRecentMatchesList.get(ind);
					// remove the original fullmpl with mobj atts only
					readRecentMatchesList.remove(temp);
					// enhance the full match with base pred line atts
					temp = fmplss.baseMatchLinePredEnhanceFullLine(temp, rp
							.getMatchLinePred().get(key).get(i));
					// restore to the list
					readRecentMatchesList.add(temp);
				}
			}
		}
		// TODO update the recent matches table with the pred points
		updateRecentPredPoints();
		// fill a map of <compId,list<predLine>>

	}

	public void updateRecentPredPoints() throws SQLException {
		PreparedStatement preparedStatement = null;
		String updateTableSQL = "UPDATE recentmatches SET h1 = ?, hx = ?, h2 = ?, so = ?, p1y = ?, p1n = ?,"
				+ " p2y = ?, p2n = ?, ht = ?, ft = ?" + " WHERE mid =?";
		openDBConn();
		preparedStatement = conn.getConn().prepareStatement(updateTableSQL);
		for (FullMatchLine f : readRecentMatchesList) {
			preparedStatement.setFloat(1, f.getH1());
			preparedStatement.setFloat(2, f.getHx());
			preparedStatement.setFloat(3, f.getH2());
			preparedStatement.setFloat(4, f.getSo());
			preparedStatement.setFloat(5, f.getSu());
			preparedStatement.setFloat(6, f.getP1y());
			preparedStatement.setFloat(7, f.getP1n());
			preparedStatement.setFloat(8, f.getP2y());
			preparedStatement.setFloat(9, f.getP2n());
			preparedStatement.setInt(10, f.getHt());
			preparedStatement.setInt(11, f.getFt());
			preparedStatement.setLong(12, f.getmId());
			preparedStatement.addBatch();
		}
		// execute update SQL stetement
		preparedStatement.executeBatch();
		// preparedStatement.executeUpdate();
		closeDBConn();
	}

	private void updateRecentError(List<MatchObj> matches) throws SQLException {
		// write the error message to the matches that where not finished
		// normally
		PreparedStatement preparedStatement = null;

		String updateTableSQL = "UPDATE recentmatches SET tim = ?  WHERE mid =?";
		preparedStatement = conn.getConn().prepareStatement(updateTableSQL);
		openDBConn();
		for (MatchObj m : matches) {
			// if (m.getFt1() != -1) {// update only matches with results
			preparedStatement.setString(1, Status.ERROR);
			preparedStatement.setLong(2, m.getmId());
			preparedStatement.addBatch();
		}
		// }
		// execute update SQL stetement
		preparedStatement.executeBatch();
		// preparedStatement.executeUpdate();
		closeDBConn();

	}

	private void updateRecentScores(List<MatchObj> matches) throws SQLException {
		// update the score results of the recent matches table from temp
		// matches table, based on the idea that the mid of the matches is the
		// same in the temp and recent tables, since the matches inserted into
		// them are the same and simultaneous

		PreparedStatement preparedStatement = null;

		String updateTableSQL = "UPDATE recentmatches SET ft1 = ?,ft2 = ?,ht1 = ?,ht2 = ? "
				+ " WHERE mid =?";

		openDBConn();
		preparedStatement = conn.getConn().prepareStatement(updateTableSQL);
		for (MatchObj m : matches) {
			if (m.getFt1() != -1) {// update only matches with results

				preparedStatement.setInt(1, m.getFt1());
				preparedStatement.setInt(2, m.getFt2());
				preparedStatement.setInt(3, m.getHt1());
				preparedStatement.setInt(4, m.getHt2());
				preparedStatement.setLong(5, m.getmId());

				preparedStatement.addBatch();
			}
		}
		// execute update SQL stetement
		preparedStatement.executeBatch();
		// preparedStatement.executeUpdate();
		closeDBConn();

	}

	public void synchronizeMPL_Map(List<MatchObj> tempMatches, String updKind) {
		 /*find the match from tempMatches to MPL map & set its {score or error}
		 status*/
		LocalDate ld = LocalDate.now();

		for (int i = 0; i < tempMatches.size(); i++) {
			int cid = tempMatches.get(i).getComId();
			long mid = tempMatches.get(i).getmId();

			for (int j = 0; j < TimeVariations.mapMPL.get(ld).get(cid).size(); j++) {
				if (TimeVariations.mapMPL.get(ld).get(cid).get(j).getmId() == mid) {
					if (updKind.equals("score")) {
						//  update HT & FT SCORES
						TimeVariations.mapMPL.get(ld).get(cid).get(j).setHt1(tempMatches.get(i).getHt1());
						TimeVariations.mapMPL.get(ld).get(cid).get(j).setHt2(tempMatches.get(i).getHt2());
						TimeVariations.mapMPL.get(ld).get(cid).get(j).setFt1(tempMatches.get(i).getFt1());
						TimeVariations.mapMPL.get(ld).get(cid).get(j).setFt2(tempMatches.get(i).getFt2());
					}
					if (updKind.equals("error")) {
						// set match time with err
						TimeVariations.mapMPL.get(ld).get(cid).get(j).setMatchTime(Status.ERROR);
					}// if error
				}// if mid
			}// for j
		}// for i
	}
}
