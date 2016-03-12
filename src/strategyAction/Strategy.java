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
	private LocalDate lastDatCheck;
	private TempMatchFunctions tmf = new TempMatchFunctions();
	MatchGetter score = new MatchGetter();
	private int periode = 6; // 6 hours

	public void task() throws SQLException, IOException {
		/*
		 * set all the temp match scraping, transforming, calculating re-storing
		 * and re-writing.
		 */
		tmf.openDBConn();
		try {
			if (lastDatCheck == null) {
				// TODO scrap todays matches & tomorrows
				score.getScheduledToday();
				score.getScheduledTomorrow();
				// tmf.corelatePunterXScorerTeams();
				tmf.storeToTempMatchesDB();
				score.clearLists();
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
				} else {
					// is still the same day get todays results
					score.getFinishedToday();
					tmf.completeToday();
					score.clearLists();
				}

			}
		} finally {
			tmf.closeDBConn();
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
			// System.out.println(t.getI());
			try {
				task();
			} catch (Exception e) {
				e.printStackTrace();
			}
		};
		// System.out.println("Scheduling: " + System.nanoTime());

		int initialDelay = 0;
		int period = 6;
		executor.scheduleAtFixedRate(task, initialDelay, period, TimeUnit.HOURS);
	}
}
