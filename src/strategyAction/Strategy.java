package strategyAction;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import calculate.MatchToTableRenewal;
import basicStruct.MatchObj;
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

	// private XscoreUpComing score =new XscoreUpComing();
	// private int periode = 6; // 6 hours

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
				// first time ever to check for temp matches
				// scrap todays matches & tomorrows
				score.getScheduledToday();
				score.getScheduledTomorrow();
				// a list of compIds playing today & tomorrow is created
				scheduledOddsAdderToday();
				scheduledOddsAdderTomorrow(lastDatCheck);
				tmf.corelatePunterXScorerTeams();
				testPredFileMaker();
				storeToTempDB();
				
				score.clearLists();

				// TODO insert R calls Here
				logger.info("NULL Last Ceck");
			} else {
				if (lastDatCheck.isBefore(LocalDate.now())) {
					lastDatCheck = LocalDate.now();
					// is new day so get yesterdays results and tomorrows
					// schedule
					score.getScheduledTomorrow();
					scheduledOddsAdderTomorrow(lastDatCheck);
					tmf.corelatePunterXScorerTeams();
					testPredFileMaker();
					tmf.storeToTempMatchesDB();
					score.getFinishedYesterday();
					tmf.completeYesterday();

					score.clearLists();
					checkReamaining();
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

	public void storeToTempDB() throws SQLException{
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

	public void testPredFileMaker() {
		/* for all the new matches create a prediction file */
		MatchToTableRenewal mttr = new MatchToTableRenewal();
		// Key is the comp id not the index in the data structure!!!
		for (Integer key : MatchGetter.schedNewMatches.keySet()) {
			try {
				mttr.testPredFileCreate(MatchGetter.schedNewMatches.get(key),
						key, MatchGetter.schedNewMatches.get(key).get(0)
								.getDat().toLocalDate());
			} catch (SQLException | IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void checkReamaining() throws SQLException, IOException {
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
