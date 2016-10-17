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
import extra.CorelationAtempt;
import extra.NameCleaner;
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

	public void corelatePunterXScorerTeams() throws IOException, SQLException {
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
		/*
		 * find the most similar terms; add the to Unilang; delete them from
		 * dbTeamsList so to be more efficient in the next search. change the
		 * team names on the list
		 */

		List<String> dbTeams = new ArrayList<>();
		int compId = -1;
		int chosenDbIdx1 = -1, chosenDbIdx2 = -1;
		float dist1 = 1000, dist2 = 1000, dist = 0;
		boolean foundTeamFlag = false;

		for (int key : MatchGetter.schedNewMatches.keySet()) {
			for (int kk = 0; kk < MatchGetter.schedNewMatches.get(key).size(); kk++) {
				MatchObj m = MatchGetter.schedNewMatches.get(key).get(kk);
				logger.info("---Match :{} {}",m.getT1(),m.getT2());
				dist1 = 1000;
				dist2 = 1000;
				chosenDbIdx1 = -1;
				chosenDbIdx2 = -1;

				// first search in unilang
				String t1 = ul.scoreTeamToCcas(m.getT1());
				if (t1 == null) {
					foundTeamFlag = false;
					if (m.getComId() != compId) {// on new competition
						dbTeams.clear();
						dbTeams = queryCompTeams(m.getComId());
						if (dbTeams == null) {// not available competition
							continue;
						}
						compId = m.getComId();
					}
				} else {// not null( found in unilang)
					// MatchGetter.schedNewMatches.get(key).get(kk).setT1(t1);
					foundTeamFlag = true;
				}
				if (!foundTeamFlag) {
					for (int i = 0; i < dbTeams.size(); i++) {
						logger.info("T1 : {}   vs   {}", m.getT1(), dbTeams.get(i), dist);
						dist = StringSimilarity.teamSimilarity(m.getT1(), dbTeams.get(i));
						if (dist1 > dist) {
							dist1 = dist;
							chosenDbIdx1 = i;
						}
					}
				}

				// second team corelation search
				String t2 = ul.scoreTeamToCcas(m.getT2());
				if (t2 == null) {
					foundTeamFlag = false;
					if (m.getComId() != compId) {// on new competition
						dbTeams.clear();
						dbTeams = queryCompTeams(m.getComId());
						if (dbTeams == null) {// not available competition
							continue;
						}
						compId = m.getComId();
					}
				} else {
					// MatchGetter.schedNewMatches.get(key).get(kk).setT2(t2);
					foundTeamFlag = true;
				}
				if (!foundTeamFlag) {
					for (int i = 0; i < dbTeams.size(); i++) {
						dist = StringSimilarity.teamSimilarity(m.getT2(), dbTeams.get(i));
						logger.info("T2: {}   vs   {}", m.getT2(), dbTeams.get(i), dist);
						if (dist2 > dist) {
							dist2 = dist;
							chosenDbIdx2 = i;
						}
					}
				}

				if (t1 != null && t2 != null) {
					MatchGetter.schedNewMatches.get(key).get(kk) .setT1(t1);
					MatchGetter.schedNewMatches.get(key).get(kk) .setT2(t2);
					continue;
				}

				if (dist1 <= StandartResponses.TEAM_DIST && dist2 <= StandartResponses.TEAM_DIST) {
					MatchGetter.schedNewMatches.get(key).get(kk) .setT1(dbTeams.get(chosenDbIdx1));
					MatchGetter.schedNewMatches.get(key).get(kk) .setT2(dbTeams.get(chosenDbIdx2));
//					if(chosenDbIdx1 > -1)
//					dbTeams.remove(chosenDbIdx1);
//					if(chosenDbIdx2 > -1)
//					dbTeams.remove(chosenDbIdx2);
					continue;
				}

				// last chance of corelating the teams; based on the distance of
				// the other team
				if (dist1 > StandartResponses.TEAM_DIST && t1 == null) {
					if (t2 != null) {
						logger.info(
								"RELATING t2:{} {}~unilang; & by matchBind t1:{} {}  ", m.getT2(), t2, m.getT1(), dbTeams.get(chosenDbIdx1));
						MatchGetter.schedNewMatches.get(key).get(kk).setT2(t2);
						ul.addTeam(dbTeams.get(chosenDbIdx1), m.getT1());
						MatchGetter.schedNewMatches.get(key).get(kk) .setT1(dbTeams.get(chosenDbIdx1));
					} else if (dist2 <= StandartResponses.TEAM_DIST) {
						logger.info(
								"RELATING t2:{} {}; & by matchBind t1:{} {}  ", m.getT2(), dbTeams.get(chosenDbIdx2), m.getT1(), dbTeams.get(chosenDbIdx1));
						MatchGetter.schedNewMatches.get(key).get(kk) .setT2(dbTeams.get(chosenDbIdx2));
						ul.addTeam(dbTeams.get(chosenDbIdx1), m.getT1());
						MatchGetter.schedNewMatches.get(key).get(kk) .setT1(dbTeams.get(chosenDbIdx1));
					}
				}

				if (dist2 > StandartResponses.TEAM_DIST && t2 == null) {
					if (t1 != null) {
						logger.info(
								"RELATING t1:{} {}~unilang; & by matchBind t2:{} {}  ", m.getT1(), t1, m.getT2(), dbTeams.get(chosenDbIdx2));
						MatchGetter.schedNewMatches.get(key).get(kk).setT1(t1);
						ul.addTeam(dbTeams.get(chosenDbIdx2), m.getT2());
						MatchGetter.schedNewMatches.get(key).get(kk) .setT2(dbTeams.get(chosenDbIdx2));
					} else if (dist1 < StandartResponses.TEAM_DIST) {
						logger.info(
								"RELATING t1:{} {}; & by matchBind t2:{} {}  ", m.getT1(), dbTeams.get(chosenDbIdx1), m.getT2(), dbTeams.get(chosenDbIdx2));
						 MatchGetter.schedNewMatches.get(key).get(kk).setT1(dbTeams.get(chosenDbIdx1));
						ul.addTeam(dbTeams.get(chosenDbIdx2), m.getT2());
						MatchGetter.schedNewMatches.get(key).get(kk) .setT2(dbTeams.get(chosenDbIdx2));
					}
				}

			}
		}
	}

	// -----------------------------corelation end

	public void complete(LocalDate d) throws SQLException {
		// to store in the temp matches the teams in scorer format
		// instead of punter so that to have faster acces
		// in case of comparison; without going through unilang conversion// ??
		// ??maybe they are kep in punter format for the oddsadders

		// find which of the db matches is actually finished acording to scorer.
		// when found the matches are updated with the scores and restored in
		// the apropriate db table
		logger.info("--------------: COMPLETE");
		int tempMatchesSize = 0;
		// fill from db the readTempMatchesList List<>, order by compid
		// matches in temp & recent db tabs is in the scorer syntax
		readFromTempMatches(d);
		tempMatchesSize = readTempMatchesList.size();
		if (tempMatchesSize == 0) {
			logger.info("No temp matches in db");
			return;
		}

		/*
		 * will keep all the selected matches. Thean delete them from temp
		 * matches table & insert to regular matches dbtable
		 */
		List<MatchObj> foundMatches = new ArrayList<MatchObj>();// all matches

		/*
		 * will contain at any time match(es) from the same competition, matches
		 * previously in the temp match db and test prediction file but now with
		 * the score results. They will be calculated and inserted in the
		 * competitions Train prediction file with all the matches data so far.
		 */
		// same comp matches of consecutive compids during search loops
		// used for test prediction file

		// List<MatchObj> smallPredictionsList = new ArrayList<MatchObj>();

		CorelationAtempt ca = new CorelationAtempt();
		String finerr = "fin";
		try {
			foundMatches = ca .corelatePunterXScorerTeams(finerr, readTempMatchesList);
		} catch (IOException e) {
			e.printStackTrace();
		}

		openDBConn();
		if (foundMatches.size() > 0) {
			try {
				logger.info("found matches {} vs {}",foundMatches.get(0).getT1(),foundMatches.get(0).getT2());
				logger.info("found matches {} vs {}",foundMatches.get(1).getT1(),foundMatches.get(0).getT2());
				logger.info("found matches {} vs {}",foundMatches.get(2).getT1(),foundMatches.get(0).getT2());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			logger.warn("matches correlated size :{}", foundMatches.size());
			deleteTempMatches(foundMatches);// delete finished matches from tempdb
			insertMatches(foundMatches);// in finished matches from tempdb to matchesdb
			updateRecentScores(foundMatches);// set score to recent matches
			synchronizeMPL_Map(foundMatches, "score", d);// update MPL map
			logger.info("Competed standart Completion");
		}

		if (tempMatchesSize - foundMatches.size() > 0) {
			/*
			 * it means that in the temporary matches table tempmatches there
			 * are still matches unfinished or not properly finished, errors
			 * canceled postponed etc; keep looping through the remaining
			 * matches to account for the remaining ones
			 */
			logger.info("--------------: LOOP TO find postponed");

			foundMatches.clear();
			logger.info("errorNewMatches.size {}", MatchGetter.errorNewMatches.size());
			finerr = "err";
			try {
				foundMatches = ca.corelatePunterXScorerTeams(finerr, readTempMatchesList);
			} catch (IOException e) {
				e.printStackTrace();
			}
			deleteTempMatches(foundMatches);
			updateRecentError(foundMatches);// set err to recent matches time
			synchronizeMPL_Map(foundMatches, "error", d);
			logger.info("Competed Old Completion");
		}
		closeDBConn();// ------------------------------
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
		String sql = "SELECT * FROM  tempmatches  where dat ='" + date + "' order by compid ;";
		// original :: order by t1 ;
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
		openDBConn();
		boolean b = conn
				.getConn()
				.createStatement()
				.execute(
						"DELETE FROM tempmatches where mid in ("
								+ sb.toString() + ");");
		closeDBConn();
		return b;
	}

	private void insertMatches(List<MatchObj> finMatches) throws SQLException {
		logger.info("--------------: INSERT to MATCHESS");
		String insert = "insert into matches values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		openDBConn();
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
		ps.close();
		closeDBConn();
	}

	private List<String> queryCompTeams(int compId) throws SQLException {
		/*
		 * get the team names for a certain competition from the db
		 */

		int idx = CountryCompetition.idToIdx.get(compId);
		CCAllStruct cc = CountryCompetition.ccasList.get(idx);
		String tab_competition = (cc.getCompetition() + "$" + cc.getCountry());
		// else
		tab_competition = NameCleaner.replacements(tab_competition .toLowerCase()) + "_fulltable";
		List<String> teamsList = new ArrayList<String>();
		openDBConn();
		ResultSet rs;
		try {
			rs = conn.getConn().createStatement() .executeQuery("SELECT team from " + tab_competition + " ;");
			while (rs.next()) {
				teamsList.add(rs.getString("team"));
			}
		} catch (SQLException e) {
			logger.warn("{} not present  in db", tab_competition);
			e.printStackTrace();
		}
		closeDBConn();
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
		PreparedStatement ps = conn.getConn().prepareStatement(insertLine);
		int i = 0;
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
		ps.close();
		closeDBConn();

	}

	private void readFromShortMatches(String sql, String tab)
			throws SQLException {
		logger.info("sql: {}", sql);
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
					readRecentMatchesList.size());
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
		 * yesterday but will finish today.
		 * 
		 * After the completion of yesterday matches, they are deletet from the
		 * tempmatch db table so there only remain the unfinished ones which
		 * started yesterday but are not finished yet even though the date
		 * changed
		 */

		Date date = Date.valueOf(LocalDate.now().minusDays(1));
		openDBConn();
		conn.open();
		ResultSet rs = conn
				.getConn()
				.createStatement()
				.executeQuery(
						"SELECT compid FROM tempmatches WHERE dat = '" + date
								+ "'");
		while (rs.next()) {
			skipDayCompIds.add(rs.getInt(1));
		}

		closeDBConn();
	}

	// ********************RECENT MATCHES

	public void storeToRecentMatchesDB() throws SQLException {
		String insert = "insert into recentmatches (mid, compid, t1, t2, ft1, ft2, ht1, ht2,"
				+ " _1, _x, _2, _o, _u, dat, tim) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
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
		logger.info("adding prediction points ...");
		/*
		 * add the prediction points, as generated and stored in files by R,
		 * from file to recent table
		 */
		if (ld == null) {
			ld = LocalDate.now();
		}

		FullMatchPredLineToSubStructs fmplss = new FullMatchPredLineToSubStructs();

		ReadPrediction predfile = new ReadPrediction();
		predfile.prediction(compsList);// read predictions from the pred files
		readInitialTeamFromRecentMatches(ld);// read from recent db table & fill readRecentMatchesList
		List<FullMatchLine> smallList = new ArrayList<>();// to update db

		// find the same matches from db table and file
		for (int cid : predfile.getMatchLinePred().keySet()) {
			for (int i = 0; i < predfile.getMatchLinePred().get(cid).size(); i++) {
				int ind = binarySearch(
						fmplss.fullMatchPredLineToMatchObj(readRecentMatchesList),
						predfile.getMatchLinePred().get(cid).get(i).getT1());
				if (ind == -1) {
					// todo dysplay error and
					logger.info("unfound relation file & db {}", predfile .getMatchLinePred().get(cid).get(i).liner());
					continue;
				}
				// make sure both teams correspond
				if (predfile
						.getMatchLinePred()
						.get(cid)
						.get(i)
						.getT2()
						.equalsIgnoreCase(
								readRecentMatchesList.get(ind).getT2())) {
					FullMatchLine temp = readRecentMatchesList.get(ind);
					temp = fmplss.baseMatchLinePredEnhanceFullLine(temp, predfile .getMatchLinePred().get(cid).get(i));
					smallList.add(temp);
					// update MPL map with the pred points from
					enrichMPLmap(ld, cid, temp);
				}// if
			}// for every match
		}// for every competition

		try {
			// TODO enhance the recent matches table with the pred points
			updateRecentPredPoints(smallList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// fill a map of <compId,list<predLine>>

	}

	private void enrichMPLmap(LocalDate ld, int cid, FullMatchLine temp) {
		// find the element in the MPLmap list of that competition matches,
		// remove the original element, add the enhanced one
		if (TimeVariations.mapMPL.get(ld).size() == 0) {
			return;
		}
		for (FullMatchLine loopvar : TimeVariations.mapMPL.get(ld).get(cid)) {
			if (temp.getT1().equals(loopvar.getT1())) {
				if (temp.getT2().equals(loopvar.getT2())) {
					TimeVariations.mapMPL.get(ld).get(cid).remove(loopvar);
					TimeVariations.mapMPL.get(ld).get(cid).add(temp);
					return;
				} else {
					logger.warn("found similar t1 but nor t2 in : {},", cid);
				}
			}
		}

	}

	public void updateRecentPredPoints(List<FullMatchLine> smallList)
			throws SQLException {
		PreparedStatement preparedStatement = null;
		String updateTableSQL = "UPDATE recentmatches SET h1 = ?, hx = ?, h2 = ?, so = ?, su = ?, p1y = ?, p1n = ?,"
				+ " p2y = ?, p2n = ?, ht = ?, ft = ?" + " WHERE mid =?";
		openDBConn();
		preparedStatement = conn.getConn().prepareStatement(updateTableSQL);
		for (FullMatchLine f : smallList) {
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
		preparedStatement.close();
		closeDBConn();
	}

	private void updateRecentError(List<MatchObj> matches) throws SQLException {
		// write the error message to the matches that where not finished
		// normally
		PreparedStatement preparedStatement = null;

		String updateTableSQL = "UPDATE recentmatches SET tim = ?  WHERE mid =?";
		openDBConn();
		preparedStatement = conn.getConn().prepareStatement(updateTableSQL);
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
		preparedStatement.close();
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
		preparedStatement.close();
		closeDBConn();

	}

	public void synchronizeMPL_Map(List<MatchObj> tempMatches, String updKind,
			LocalDate ld) {
		/*
		 * find the match from tempMatches to MPL map & set its {score or error}
		 * status
		 */
		// LocalDate ld = LocalDate.now();
		if (TimeVariations.mapMPL.containsKey(ld)) {
			return;
		}
		for (int i = 0; i < tempMatches.size(); i++) {
			int cid = tempMatches.get(i).getComId();
			long mid = tempMatches.get(i).getmId();

			for (int j = 0; j < TimeVariations.mapMPL.get(ld).get(cid).size(); j++) {
				if (TimeVariations.mapMPL.get(ld).get(cid).get(j).getmId() == mid) {
					if (updKind.equals("score")) {
						// update HT & FT SCORES
						TimeVariations.mapMPL.get(ld).get(cid).get(j) .setHt1(tempMatches.get(i).getHt1());
						TimeVariations.mapMPL.get(ld).get(cid).get(j) .setHt2(tempMatches.get(i).getHt2());
						TimeVariations.mapMPL.get(ld).get(cid).get(j) .setFt1(tempMatches.get(i).getFt1());
						TimeVariations.mapMPL.get(ld).get(cid).get(j) .setFt2(tempMatches.get(i).getFt2());
					}
					if (updKind.equals("error")) {
						// set match time with err
						TimeVariations.mapMPL.get(ld).get(cid).get(j) .setMatchTime(Status.ERROR);
					}// if error
				}// if mid
			}// for j
		}// for i
	}
}
