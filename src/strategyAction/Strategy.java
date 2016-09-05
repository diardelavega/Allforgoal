package strategyAction;

import java.io.IOException;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
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
import basicStruct.MatchObj;
import r_dataIO.RHandler;
import scrap.Bari91UpCommingOdds;
import scrap.OddsNStats;
import scrap.SoccerPunterOdds;
import scrap.XscoreUpComing;
import structures.CountryCompetition;
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
	private RHandler rh = new RHandler();

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

	public void task() throws SQLException, IOException {
		/*
		 * set all the temp match scraping, transforming, calculating re-storing
		 * and re-writing. All matches in hand are stored in the MatchGetter ||
		 * XscoreUpComing (scheduled or finished) matches-structures
		 */
		tmf.openDBConn();
		try {
			if (lastDatCheck == null) {
				lastDatCheck = LocalDate.now();
				score.getScheduledToday(); // a list of compIds playing
				score.getScheduledTomorrow();// today & tomorrow is created
				scheduledOddsAdderToday();
				scheduledOddsAdderTomorrow(lastDatCheck);
				tmf.corelatePunterXScorerTeams();
				storeToSmallDBsCondition(lastDatCheck);// store condition
				testPredFileMaker();// test file create
				score.clearLists();
				rh.predictSome(CountryCompetition.todayComps);
				// rh.predictSome(CountryCompetition.tomorrowComps);
				logger.info("NULL Last Ceck");
			} else {
				if (lastDatCheck.isBefore(LocalDate.now())) {
					lastDatCheck = LocalDate.now();
					score.getFinishedYesterday();
					tmf.completeYesterday();

					tmf.readDaySkips();
//					writeResultsToTestFile();
					rh.reEvaluate(CountryCompetition.yesterdayComps);

					CountryCompetition.yesterdayComps = CountryCompetition.todayComps;
					CountryCompetition.todayComps = CountryCompetition.tomorrowComps;
					CountryCompetition.tomorrowComps.clear();// to bee refilled

					// TODO write in the testfile the actual results of the
					// matches

					score.getScheduledTomorrow(); // tommorrowComps is updated
					scheduledOddsAdderTomorrow(lastDatCheck);
					tmf.corelatePunterXScorerTeams();
					storeToSmallDBs(); // store in temp and recent matches

					testPredFileMaker();// test file create
					rh.predictSome(CountryCompetition.tomorrowComps);

					score.clearLists();
					checkRemaining();
					tmf.deleteFromRecentMatches();
					logger.info("Last Ceck   BEFORE TODAY");
				} else {
					// is still the same day get todays results
					score.getFinishedToday();
					tmf.completeToday();
					score.clearLists();
					logger.info("Last Ceck   Finished  TODAY");
				}
			}
		} finally {
			tmf.closeDBConn();
		}
	}

	private void storeToSmallDBsCondition(LocalDate checkdat)
			throws SQLException, IOException {
		/*
		 * TODO a condition so that matches that have already been written
		 * inside will not be rewritten maybe get the leatest date from db and
		 * compare it to the today date
		 */
		Conn conn = new Conn();
		conn.open();
		LocalDate latestDate = null;
		ResultSet res = conn
				.getConn()
				.createStatement()
				.executeQuery(
						"SELECT dat from tempmatches order by dat desc limit 1;");
		if (res.next()) {
			latestDate = res.getDate(1).toLocalDate();
			if (!latestDate.isBefore(checkdat)) {
				// if leatest match in temp db is inserted before the curent
				// check date then store the info gathered. Avoid duplicates in
				// db and test file
				conn.close();
				return;
			}
		} else {
			testPredFileMaker();
			storeToSmallDBs();
		}

		conn.close();
	}

	public void storeToSmallDBs() throws SQLException {
		/* store to tempmatches and recentmatches */
		tmf.storeToTempMatchesDB();
		tmf.storeToRecentMatchesDB();

	}

	public void tryTask() throws SQLException {
		/* test method for strategu action performance */
		// tmf.openDBConn();
		try {

			// TODO integrate the test file and data file for prediction
			if (lastDatCheck == null) {
				lastDatCheck = LocalDate.now().plusDays(2);
				// first time ever to check for temp matches
				// scrap todays matches & tomorrows
				// score.getScheduledToday();
				score.getScheduledOnDate(lastDatCheck);
				tmf.corelatePunterXScorerTeams();
				// score.getFinishedOnDate(lastDatCheck);
				// score.getScheduledTomorrow();
				score.storeSched();
				// score.clearLists();
				// logger.info("sched size {}",score.schedNewMatches.size());
				// score.readSched();

				System.out.println("--------------------------\n\n\n");

				Bari91UpCommingOdds b91 = new Bari91UpCommingOdds();
				b91.scrapBariPage(lastDatCheck);

				System.out.println("---------------------------\n\n\n");

				//
				OddsNStats ons = new OddsNStats();
				ons.getOddsPage(lastDatCheck);
				//
				System.out.println("---------------------------\n\n\n");
				SoccerPunterOdds spo = new SoccerPunterOdds();
				spo.getDailyOdds(lastDatCheck);

				// tmf.storeToTempMatchesDB();
				score.clearLists();
				// lastDatCheck = LocalDate.now();
				// logger.info("NULL Last Ceck");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
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
		/* for all the new matches create a prediction file */

		// CREATE test prediction file for tomorrow group of matches of the same
		// competition and a test prediction file for the today group of matches
		// for
		// that same competition

		LocalDate tdy = LocalDate.now(), tom = LocalDate.now().plusDays(1);
		List<MatchObj> todayDate = null;//
		List<MatchObj> tomorrowDate = null;//

		MatchToTableRenewal mttr = new MatchToTableRenewal();
		// Key is the comp id not the index in the data structure!!!
		for (Integer key : MatchGetter.schedNewMatches.keySet()) {
			for (MatchObj m : MatchGetter.schedNewMatches.get(key)) {
				todayDate = new ArrayList<MatchObj>();
				tomorrowDate = new ArrayList<MatchObj>();
				if (m.getDat().toLocalDate().equals(tdy)) {
					todayDate.add(m);
				} else if (m.getDat().toLocalDate().equals(tom)) {
					tomorrowDate.add(m);
				}
			}
			if (todayDate != null) {
				if (todayDate.size() >= 1) {
					mttr.testPredFileCreate(todayDate, key, tdy);
				}
			}
			if (tomorrowDate != null) {
				if (tomorrowDate.size() >= 1) {
					mttr.testPredFileCreate(tomorrowDate, key, tom);
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

		int initialDelay = 0;
		int period = 5;
		executor.scheduleAtFixedRate(task, initialDelay, period, TimeUnit.HOURS);
	}

	
}
