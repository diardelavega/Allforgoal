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

import calculate.MatchToTableRenewal;
import scrap.XscoreUpComing;
import structures.CountryCompetition;
import test.MatchGetter;
import dbtry.Conn;
import diskStore.AnalyticFileHandler;
import extra.StandartResponses;
import extra.StringSimilarity;
import extra.Unilang;
import basicStruct.CCAllStruct;
import basicStruct.MatchObj;
import basicStruct.ScorerDataStruct;

public class TempMatchFunctions {

	public static final Logger logger = LoggerFactory
			.getLogger(TempMatchFunctions.class);
	public static List<Integer> skipDayCompIds = new ArrayList<>();

	private Unilang ul = new Unilang(); // Original_Line
	// public List<MatchObj> incomeTempMatchesListss = new ArrayList<>();
	public List<MatchObj> readTempMatchesList = new ArrayList<>();
	public List<MatchObj> readRecentMatchesList = new ArrayList<>();

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
		int chosenDbIdx1 = -1, chosenDbIdx2 = -1;
		// for (Integer key : XscoreUpComing.schedNewMatches.keySet()) {
		// for (MatchObj m : XscoreUpComing.schedNewMatches.get(key)) {//
		// Original_Line
		for (Integer key : MatchGetter.schedNewMatches.keySet()) {
			for (int kk = 0; kk < MatchGetter.schedNewMatches.get(key).size(); kk++) {
				// for (MatchObj m : MatchGetter.schedNewMatches.get(key)) {
				MatchObj m = MatchGetter.schedNewMatches.get(key).get(kk);
				// first search in unilang
				String t = ul.scoreTeamToCcas(m.getT1());
				if (t == null) {
					if (m.getComId() != compId) {
						// if a new competition appears delete the previous
						// teams
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
						logger.info("uncorelated --:  m.T1= '{}'", m.getT1());
					}
				} else {
					MatchGetter.schedNewMatches.get(key).get(kk).setT1(t);
				}
				// second team corelation search
				t = ul.scoreTeamToCcas(m.getT2());
				if (t == null) {
					// if (m.getComId() != compId) {
					// dbTeams.clear();
					// dbTeams = queryCompTeams(m.getComId());
					// compId = m.getComId();
					// }
					float minDist = 100;
					// int chosenIdx = -1;
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
						logger.info("uncorelated --:  m.T2= '{}'", m.getT2());
					}
				} else {
					MatchGetter.schedNewMatches.get(key).get(kk).setT2(t);
				}
			}
		}
	}

	// //////////////////////////////////////////
	// to write and read in the temp and recent matches db tables
	public void storeToRecentMatchesDB() throws SQLException {
		String insert = "insert into recentmatches values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		storeToShortMatches(insert);
	}

	public List<MatchObj> readFromRecentMatches(LocalDate dat) throws SQLException {
		readFromShortMatches(dat, "recentmatches");
		return readRecentMatchesList;
	}

	public boolean deleteFromRecentMatches(LocalDate ld) throws SQLException {
		openDBConn();
		boolean b = conn
				.getConn()
				.createStatement()
				.execute(
						"DELETE FROM tempmatches where dat = ("
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
						"DELETE FROM tempmatches where dat = ("
								+ Date.valueOf(LocalDate.now().minusDays(3))
								+ ");");
		closeDBConn();
		return b;
	}

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
		readFromShortMatches(dat, "tempmatches");

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

	private void readFromShortMatches(LocalDate dat, String tab)
			throws SQLException {
		openDBConn();
		Date date = Date.valueOf(dat);
		ResultSet rs = conn
				.getConn()
				.createStatement()
				.executeQuery(
						"SELECT * FROM " + tab + " where dat ='" + date
								+ "' order by t1 ;");

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
			// TODO consider for match time element addition #ta2
			mali.add(mobj);
		}
		switch (tab) {
		case "tempmatches":
			readTempMatchesList.addAll(mali);
			logger.info("read-" + tab + "-MatchesList size ={}",
					readTempMatchesList.size());
			break;
		case "recentmatches":
			readRecentMatchesList.addAll(mali);
			logger.info("read-" + tab + "-MatchesList size ={}",
					readTempMatchesList.size());
			break;
		default:
			break;
		}
		closeDBConn();
	}

	// //////////////////////////////////////////
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
				idx = binarySearch(team, 0, readTempMatchesList.size() - 1);
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
		insertMatches(matches);// insertfinished matches from matches
		updateRecentScores(matches);// set score to recent matches
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
			// for (MatchObj m : XscoreUpComing.errorNewMatches) {
			for (MatchObj m : MatchGetter.errorNewMatches) {
				// String team = ul.scoreTeamToCcas(m.getT1()); Original_Line
				String team = ul.scoreTeamToCcas(m.getT1());
				if (team != null) {
					idx = binarySearch(team, 0, readTempMatchesList.size() - 1);
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
			logger.info("Competed Old Completion");
		}
	}

	private void updateRecentError(List<MatchObj> matches) throws SQLException {
		PreparedStatement preparedStatement = null;

		String updateTableSQL = "UPDATE recentmatches SET tim = 'err'  WHERE mid =?";

		openDBConn();
		for (MatchObj m : matches) {
			// if (m.getFt1() != -1) {// update only matches with results
			preparedStatement = conn.getConn().prepareStatement(updateTableSQL);
			preparedStatement.setLong(5, m.getmId());
			preparedStatement.addBatch();
		}
		// }
		// execute update SQL stetement
		preparedStatement.executeBatch();
		// preparedStatement.executeUpdate();
		closeDBConn();

	}

	private void updateRecentScores(List<MatchObj> matches) throws SQLException {
		// update the score results of the recent matches table
		// based on the idea that the mid of the matches is the same in the temp
		// and recent tables, since the matches inserted into them are
		// the same and simultaneous

		PreparedStatement preparedStatement = null;

		String updateTableSQL = "UPDATE recentmatches SET ft1 = ?,ft2 = ?,ht1 = ?,ht2 = ? "
				+ " WHERE mid =?";

		openDBConn();
		for (MatchObj m : matches) {
			if (m.getFt1() != -1) {// update only matches with results
				preparedStatement = conn.getConn().prepareStatement(
						updateTableSQL);

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

	private int binarySearch(String team, int min, int max) {
		try {
			// logger.info("t__{}  min_{}   max__{}", team, min, max);
			if (min > max) {
				return -1;
			}
			int mid = (max + min) / 2;
			// logger.warn("mid__{}", mid);
			if (readTempMatchesList.get(mid).getT1().equals(team)) {
				return mid;
			} else if (readTempMatchesList.get(mid).getT1().compareTo(team) > 0) {
				return binarySearch(team, min, mid - 1);
			} else {
				return binarySearch(team, mid + 1, max);
			}
		} catch (Exception e) {
			logger.info("t__{}  min_{}   max__{},  readTempMatchesList-:{}",
					team, min, max, readTempMatchesList.size());
			e.printStackTrace();
		}
		// return -1;
		return -1;
	}
	
	

	private List<String> queryCompTeams(int compId) {
		/*
		 * get the team names for a certain competition
		 */
		String tab_competition = compNameGetter(compId);
		if (tab_competition == null) {
			logger.warn("competition not found");
			return null;
		}
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

	private String compNameGetter(int compId) {

		int idx = CountryCompetition.idToIdx.get(compId);
		CCAllStruct cc = CountryCompetition.ccasList.get(idx);
		return cc.getCompetition();

//		for (int i = 0; i < CountryCompetition.ccasList.size(); i++) {
//			if (CountryCompetition.ccasList.get(i).getCompId() == compId) {
//				// country = CountryCompetition.ccasList.get(i).getCountry();
//				return CountryCompetition.ccasList.get(i).getCompetition();
//			}
//		}
//		return null;
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

	/*public void writeResultsToTestFile() throws SQLException {
		 in the test files created write the actual results 
		
		 * the data in the test file that is about to be writtend doesn't have
		 * to contain all the prediction file attributes just the prediction
		 * attributes id 1,x,2,o,u,1p,2p,ht,ft. *******************************
		 * The order in which the data is written in the file matters though
		 
		// the writing will be done at the yesterday comps
		// get all the tets files from yesterdayComps - the unfinished matches
		// of skipsday
		List<Integer> workingIds = CountryCompetition.yesterdayComps;
		workingIds.removeAll(TempMatchFunctions.skipDayCompIds);
		LocalDate yesterDat = LocalDate.now().minusDays(1);
		AnalyticFileHandler afh = new AnalyticFileHandler();
		
		
		//read yesterdays matches from the db
		readFromRecentMatches(yesterDat);
		for(int cid :workingIds){
			 CCAllStruct cc = CountryCompetition.ccasList.get(CountryCompetition.idToIdx.get(cid));
			 afh.getTestFileName(cid, cc.getCompetition(), cc.getCountry(), yesterDat);
		}
//		afh.getTestFileName(compId, compName, country, dat)
		// readr recent matches @ tempMAtchFunctions

		// get a list of testPathFiles (yesterday_comps-skipday)

		// for list of testfile paths
		// read test file1;
		// get t1 & t2 for each line
		// find t1 in recent by binary search
		// conferm by t2 check & compId check
		// write in the file a reduced test file {t1,t2,ht,sc,1p,2p,ht,ft}
		// (t1,t2)?? probably not necesary
		//write the line in the order that the matches were

	}*/
}
