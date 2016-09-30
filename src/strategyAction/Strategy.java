package strategyAction;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysql.jdbc.Connection;

import calculate.MatchToTableRenewal;
import dbtry.Conn;
import diskStore.AnalyticFileHandler;
import diskStore.LastPeriodicDataGather;
import extra.AsyncType;
import extra.AttsKind;
import extra.PeriodicTimes;
import basicStruct.AsyncRequest;
import basicStruct.MatchObj;
import r_dataIO.RHandler;
import scrap.Bari91UpCommingOdds;
import scrap.OddsNStats;
import scrap.SoccerPunterOdds;
import scrap.XscoreUpComing;
import structures.CountryCompetition;
import structures.TimeVariations;
import test.MatchGetter;
import test.TestFile;

/**
 * @author Administrator
 *
 *         A class with function the specification and coordination of the
 *         different actions that have to go together for the well-function of
 *         the program
 */
public class Strategy {

	public static final Logger logger = LoggerFactory.getLogger(Strategy.class);
	private LocalDate lastDatCheck;// = LocalDate.now().minusDays(1);
	private TempMatchFunctions tmf = new TempMatchFunctions();
	private MatchGetter score = new MatchGetter();
	private TimeVariations tv = new TimeVariations();
	private LastPeriodicDataGather ldg = new LastPeriodicDataGather();

	// private XscoreUpComing score =new XscoreUpComing();
	// private int periode = 6; // 6 hours

	{
		/* strategic task explenation */
		/*
		 * the task specifies the series of actions to be complete in the
		 * everyday work of the program.the initial part <lastdatacheck == null>
		 * is for the forst time we start the programand obligate it to set the
		 * last date check to get the scheduled today and scheduled tomorrow
		 * matches, during each of those functions the competition ids for every
		 * competition gathered is inserted into a list of the today<> and
		 * tomorrow<>. These list of comp_ids will be used by the RHandeling
		 * class to generate, predict and reevaluate the predictions made.
		 * ---------------------------: a dificulty with the program is the fact
		 * that the reevaluation of the matches will be done by the R reading
		 * the testprdiction files, which are not updated with the results of
		 * the matches,(unimplemeted method & maybe unpractical)---------------
		 * 
		 * After the matches have been gathered we call methods to assigne odds
		 * to those matches. After the odds we corelate the matches by finding
		 * the equivalent word from Score to Punter so that the match from
		 * scorer are converted to punter and then inserted into 2 temprary db
		 * tables in punter format(which is the format of the words in
		 * everycometition fulltable in the db).
		 * 
		 * The temporary table stores in tempmatches_tab matches and moves them
		 * to matches_tab after they are finished. in the recentmatches_tab the
		 * matches remain there for a couple of days , they are updated with the
		 * scores once the matches are finished. the Recent tab is used in
		 * combination with the List of compIds (today<>, tomorrow<>) to display
		 * the result of the matches in the html pages. it is used to reduce the
		 * search that would otherwise have been done in the in the
		 * match_tab(which is way bigger). Then we try to predict the results
		 * for the matches we gathered so we call the R functions to handle it.
		 * 
		 * After the initial actions we procede to a phase of repetition, where
		 * the same actions are repeated over and over in the same manner except
		 * when the date changes. In that case we have a new set of matches for
		 * tomorrow and we have to generate the appropriate test prediction
		 * _iles. During the completion of a match @TempMatchFunction: Complete
		 * a call to <predictionDataset> calculates and puts the resulting
		 * prediction file line in the trainFile.
		 */
	}

	public void task() throws SQLException, IOException, ClassNotFoundException {
		/*
		 * set all the temp match scraping, transforming, calculating re-storing
		 * and re-writing. All matches in hand are stored in the MatchGetter ||
		 * XscoreUpComing (scheduled or finished) matches-structures
		 */
		tmf.openDBConn();
		try {
			if (lastDatCheck == null) {
				lastDatCheck = LocalDate.now();
				startPartTask();
			} else {
				if (lastDatCheck.isBefore(LocalDate.now())) {
					lastDatCheck = LocalDate.now();
					dateChangePartTask();

				} else {
					// is still the same day get todays results
					dailyPartTask();
				}
			}
		} finally {
			tmf.closeDBConn();
		}
	}

	public void startPartTask() throws ClassNotFoundException, IOException,
			SQLException {
		// in case os partial usage.. check the timestamp in the file
		if (ldg.hourlyFileFilledCheck()) {
			score.getScheduledToday();
			score.getScheduledTomorrow();
			// a list of compIds playing today & tomorrow is created
			scheduledOddsAdderToday();
			scheduledOddsAdderTomorrow(lastDatCheck);

			// scheduled matches is updated
			tmf.corelatePunterXScorerTeams();

			ldg.writeMatchStructs();
			ldg.writeMeta(lastDatCheck);
		} else {
			ldg.readMatchStructs();
		}
		storeToSmallDBsCondition(lastDatCheck);// store condition
		score.clearLists();

		tv.initMPL();
		schedulePredictionToday();
		schedulePredictionTomorrow();
		logger.info("Empty ... Last Ceck @ {}", LocalTime.now());
	}

	public void dateChangePartTask() throws SQLException,
			FileNotFoundException, IOException {
		tv.compsDateRotate();

		// if (!ldg.hourlyFileFilledCheck()) {
		score.getFinishedYesterday();
		tmf.completeYesterday();
		// at this point structs & db are updated

		tmf.readDaySkips();
		writeResultsToTest();// write to file for reeval
		scheduleReEvaluation(TimeVariations.yesterdayComps);

		// ----------------Tomorrow's actions reparation line

		score.getScheduledTomorrow();
		scheduledOddsAdderTomorrow(lastDatCheck);
		tmf.corelatePunterXScorerTeams();

		ldg.writeMatchStructs();
		ldg.writeMeta(lastDatCheck);

		storeToSmallDBs(); // store in temp and recent matches

		// testPredFileMaker();// test file create
		schedulePredictionTomorrow();

		score.clearLists();
		checkRemaining();
		tmf.deleteFromRecentMatches();
		logger.info("Last Ceck   BEFORE TODAY {}", LocalTime.now());
	}

	public void dailyPartTask() throws SQLException, IOException {
		/*
		 * completeToday(); : set score & error to the finished matches; deletes
		 * from temp & insert into matches; updates recentmatches & MPL
		 * map..............................................................
		 * finished matches can not be completed if they were not scheduled &
		 * stored in db
		 */
		score.getFinishedToday();
		if (score.finNewMatches.size() > 0) {
			tmf.completeToday();
		}

		ldg.writeMatchStructs();
		ldg.writeMeta(lastDatCheck);

		score.clearLists();
		logger.info("Last Ceck   Daily..  TODAY {}", LocalTime.now());
	}

	public void yesterdayRun() throws SQLException, FileNotFoundException,
			IOException {
		score.getFinishedYesterday();
		if (score.finNewMatches.size() > 0) {
			tmf.completeYesterday();
		}
		// at this point structs & db are updated

		tmf.readDaySkips();
		writeResultsToTest();// write to file for reeval
		scheduleReEvaluation(TimeVariations.yesterdayComps);
	}

	private void schedulePredictionToday() {
		/*
		 * if list of competitons is empty, dont send a request, because it will
		 * not have anything to do. Prediction points are added at the end of
		 * the exwcution,(after a response from the R functions har returned)
		 */
		logger.info("sched pred today  size: {}", TimeVariations.todayComps.size());
		if (TimeVariations.todayComps.size() > 0) {
			ReqScheduler rs = ReqScheduler.getInstance();
			rs.addReq(AsyncType.PRED, TimeVariations.todayComps, AttsKind.hs, LocalDate.now());
			rs.startReq();
		}
	}

	private void schedulePredictionTomorrow() {
		logger.info("sched pred tomorow  size: {}", TimeVariations.tomorrowComps.size());
		if (TimeVariations.tomorrowComps.size() > 0) {
			ReqScheduler rs = ReqScheduler.getInstance();
			rs.addReq(AsyncType.PRED, TimeVariations.tomorrowComps, AttsKind.hs, LocalDate.now().plusDays(1));
			rs.startReq();
		}
	}

	private void scheduleReEvaluation(List<Integer> list) {
		if (list.size() > 0) {
			ReqScheduler rs = ReqScheduler.getInstance();
			rs.addReq(AsyncType.RE_EVAL, list, AttsKind.hs, null);
			rs.startReq();
		}

	}

	private void storeToSmallDBsCondition(LocalDate checkdat)
			throws SQLException, IOException {
		logger.info("storing to small db");
		/*
		 * a condition so that matches that have already been written inside
		 * will not be rewritten maybe get the leatest date from db and compare
		 * it to the today date
		 */
		Conn conn = new Conn();
		conn.open();
		LocalDate latestRecDate = null;
		ResultSet res = conn
				.getConn()
				.createStatement()
				.executeQuery(
						"SELECT dat from tempmatches order by dat desc limit 1;");
		if (res.next()) {
			latestRecDate = res.getDate(1).toLocalDate();
			if (!latestRecDate.isBefore(checkdat)) {
				testPredFileMaker();// test file create
				conn.close();
				// return true;
			} else {
				// if leatest match in temp db is inserted before the curent
				// check date then store the info gathered. Avoid duplicates in
				// db and test file
				testPredFileMaker();
				storeToSmallDBs();
				// return false;
			}
		} else {
			// if leatest match in temp db is inserted before the curent
			// check date then store the info gathered. Avoid duplicates in
			// db and test file
			testPredFileMaker();
			storeToSmallDBs();
			// return false;
		}
		conn.close();
		// return false;
	}

	public void storeToSmallDBs() throws SQLException {
		/* store to tempmatches and recentmatches */
		logger.info("storing to temp and recent matches");
		tmf.storeToTempMatchesDB();
		tmf.storeToRecentMatchesDB();

	}

	public void tryTask() throws SQLException {
		/* test method for strategu action performance */
		// tmf.openDBConn();
		try {
			lastDatCheck = LocalDate.now();
			score.getScheduledToday();
			// score.getScheduledTomorrow();// today & tomorrow is created
			scheduledOddsAdderToday();
			// scheduledOddsAdderTomorrow(lastDatCheck);
			tmf.corelatePunterXScorerTeams();

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// tmf.closeDBConn();
		}
	}

	public void scheduledOddsAdderToday() {
		/*
		 * go to the specific websites and get the odds for the matches to
		 * analize.
		 */
		Bari91UpCommingOdds b91 = new Bari91UpCommingOdds();
		b91.scrapBariPage(lastDatCheck);

		// whent ofline
		// OddsNStats ons = new OddsNStats();
		// ons.getOddsPage(lastDatCheck);

		SoccerPunterOdds spo = new SoccerPunterOdds();
		spo.getDailyOdds(lastDatCheck);
	}

	public void scheduledOddsAdderTomorrow(LocalDate todate) {
		/*
		 * go to the specific websites and get the odds for the matches to
		 * analize.
		 */
		Bari91UpCommingOdds b91 = new Bari91UpCommingOdds();
		b91.scrapBariPage(todate.plusDays(1));

		// whent ofline
		// OddsNStats ons = new OddsNStats();
		// ons.getOddsPage(todate.plusDays(1));

		SoccerPunterOdds spo = new SoccerPunterOdds();
		spo.getDailyOdds(todate.plusDays(1));
	}

	public void testPredFileMaker() throws SQLException, IOException {
		logger.info(" --- : testPredFileMaker");
		/* for all the new matches create a prediction file */

		/*
		 * CREATE test prediction file for tomorrow group of matches of the same
		 * competition and a test prediction file for the today group of matches
		 * for that same competition
		 */

		LocalDate tdy = LocalDate.now(), tom = LocalDate.now().plusDays(1);
		List<MatchObj> todayDate = null;//
		List<MatchObj> tomorrowDate = null;//

		MatchToTableRenewal mttr = new MatchToTableRenewal();
		// Key is the comp id not the index in the data structure!!!
		for (int compId : MatchGetter.schedNewMatches.keySet()) {
			todayDate = new ArrayList<MatchObj>();
			tomorrowDate = new ArrayList<MatchObj>();
			for (MatchObj m : MatchGetter.schedNewMatches.get(compId)) {
				if (m.getDat().toLocalDate().equals(tdy)) {
					todayDate.add(m);
				} else if (m.getDat().toLocalDate().equals(tom)) {
					tomorrowDate.add(m);
				}
			}// for
			if (todayDate != null) {
				if (todayDate.size() >= 1) {
					mttr.testPredFileCreate(todayDate, compId, tdy);
				}
			}
			if (tomorrowDate != null) {
				if (tomorrowDate.size() >= 1) {
					mttr.testPredFileCreate(tomorrowDate, compId, tom);
				}
			}
		}
	}

	public void checkRemaining() throws SQLException, IOException {
		/*
		 * handle matches older than yesterday that haven't found a solution yet
		 * Remaining matches should be adequate to be used in the data
		 * prediction analisys file, so not very old.
		 */
		tmf.openDBConn();
		List<String> dates = tmf.getTempDates();
		if (dates.size() == 0) {
			logger.info("NO REMAINING MATCHES");
		} else {
			for (String d : dates) {
				// LocalDate.parse(d,DateTimeFormatter.ofPattern("yyyy-MM-dd"));
				LocalDate mDat = LocalDate.parse(d);
				if (mDat.isBefore(LocalDate.now().minusDays(1))) {
					// if match is older than yesterday
					score.getFinishedOnDate(mDat);
					// add results to matchgetter finished
					tmf.complete(mDat);
					// reads data from db temp matches
					// ?? incompatible
				}
			}// for

			if (tmf.readTempMatchesList.size() > 0) {
				TestFile tf = new TestFile();
				tf.inidRB();
				for (MatchObj m : tmf.readTempMatchesList) {
					tf.write(m);
				}
				tf.closeRB();
			}

		}
	}

	public void periodic() throws SQLException, IOException {
		ScheduledExecutorService executor = Executors.newScheduledThreadPool(3);
		// Task t = new Task();
		Runnable task = () -> {
			try {
				task();
			} catch (Exception e) {
				e.printStackTrace();
			}
		};
		// System.out.println("Scheduling: " + System.nanoTime());

		executor.scheduleAtFixedRate(task, PeriodicTimes.INIT_DELAY,
				PeriodicTimes.PERIOD, TimeUnit.HOURS);
	}

	public void writeResultsToTest() throws FileNotFoundException,
			SQLException, IOException {
		AnalyticFileHandler afh = new AnalyticFileHandler();
		afh.writeResultsToTestFile();
	}
}
