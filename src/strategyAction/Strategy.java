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

import basicStruct.MatchObj;
import scrap.Bari91UpCommingOdds;
import scrap.XscoreUpComing;
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
	MatchGetter score = new MatchGetter();
	// XscoreUpComing score =new XscoreUpComing();
	private int periode = 6; // 6 hours

	public void task() throws SQLException, IOException {
		/*
		 * set all the temp match scraping, transforming, calculating re-storing
		 * and re-writing.
		 */
		tmf.openDBConn();
		try {

			// TODO integrate the test file and data file for prediction
			if (lastDatCheck == null) {
				// first time ever to check for temp matches
				// scrap todays matches & tomorrows
				score.getScheduledToday();
				score.getScheduledTomorrow();
				// tmf.corelatePunterXScorerTeams();
				tmf.storeToTempMatchesDB();
				score.clearLists();
				lastDatCheck = LocalDate.now();
				logger.info("NULL Last Ceck");
			} else {
				if (lastDatCheck.isBefore(LocalDate.now())) {
					// is new day so get yesterdays results and tomorrows
					// schedule
					score.getScheduledTomorrow();
					// tmf.corelatePunterXScorerTeams();
					tmf.storeToTempMatchesDB();
					score.getFinishedYesterday();
					tmf.completeYesterday();
					lastDatCheck = LocalDate.now();
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

	public void tryTask() throws SQLException {
		// tmf.openDBConn();
		try {

			// TODO integrate the test file and data file for prediction
			if (lastDatCheck == null) {
				// first time ever to check for temp matches
				// scrap todays matches & tomorrows
				 score.getScheduledToday();
				 score.storeSched();
//				 score.clearLists();
				// logger.info("sched size {}",score.schedNewMatches.size());
//				score.readSched();

//				for (Integer key : score.schedNewMatches.keySet()) {
//					System.out.println(key+"--------------->");
//					for (int i = 0; i < score.schedNewMatches.get(key).size(); i++)
//						score.schedNewMatches.get(key).get(i).printMatch();
//					System.out.println("-----------------------------\n");
//				}

				// score.getScheduledTomorrow();
				Bari91UpCommingOdds b91 = new Bari91UpCommingOdds();
				b91.initBari91UpCommingOdds();
				b91.scrapBariPage(LocalDate.now());
				// b91.scrapBariPage(LocalDate.now().plusDays(1));
				// tmf.corelatePunterXScorerTeams();

				System.out
						.println("------------------------------------\n\n\n");
				for (Integer key : score.schedNewMatches.keySet())
					for (int i = 0; i < score.schedNewMatches.get(key).size(); i++)
						score.schedNewMatches.get(key).get(i).printMatch();

				// tmf.storeToTempMatchesDB();
				score.clearLists();
//				lastDatCheck = LocalDate.now();
//				logger.info("NULL Last Ceck");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// tmf.closeDBConn();
		}
	}

	public void checkReamaining() throws SQLException, IOException {
		/* handle matches older than yesterday that haven't found a solution yet */
		tmf.openDBConn();
		List<String> dates = tmf.getTempDates();
		if (dates.size() == 0) {
			logger.info("NO REMAINING MATCHES");
		} else {
			for (String d : dates) {
				LocalDate mDat = LocalDate.parse(d,
						DateTimeFormatter.ofPattern("yyyy-MM-dd"));
				if (mDat.isBefore(LocalDate.now().minusDays(1))) {
					// if match is older than yesterday
					score.getFinishedOnDate(mDat);
					tmf.complete(mDat);
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
