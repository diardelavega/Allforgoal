package strategyAction;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import basicStruct.MatchObj;
import scrap.XscoreUpComing;

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
	XscoreUpComing score = new XscoreUpComing();
	private int periode = 6; // 6 hours

	public void periodeTask() throws SQLException, IOException {
		/*
		 * set all the temp match scraping, transforming, calculating re-storing
		 * and re-writing.
		 */
		if (lastDatCheck == null) {
			// TODO scrap todays matches & tomorrows
			score.getScheduledToday();
			score.getScheduledTomorrow();
			tmf.corelatePunterXScorerTeams();
			tmf.storeToTempMatchesDB();
			score.clearLists();
		} else {
			if (lastDatCheck.isBefore(LocalDate.now())) {
				// is new day so get yesterdays results and tomorrows schedule
				score.getScheduledTomorrow();
				tmf.corelatePunterXScorerTeams();
				tmf.storeToTempMatchesDB();
				score.getFinishedYesterday();
				tmf.completeYesterday();
				lastDatCheck = LocalDate.now();
				score.clearLists();
			} else {
				// is still the same day get todays results
				score.getFinishedToday();
				tmf.completeToday();
				score.clearLists();
			}

			// TODO search db temp matches and get dates of matches
			tmf.openDBConn();
			List<String> dates = tmf.getTempDates();
			if (dates.size() == 0) {
				// if(db dates == empty)=> scrap todays & tomorrows matches
				score.getScheduledToday();
				score.getScheduledTomorrow();
				tmf.openDBConn();
				tmf.corelatePunterXScorerTeams();
				tmf.storeToTempMatchesDB();
				lastDatCheck = LocalDate.now();
			} else {

				// for (String d : dates) {
				// LocalDate mDat = LocalDate.parse(d,
				// DateTimeFormatter.ofPattern("yyyy-MM-dd"));
				// if(mDat.isBefore(LocalDate.now().plusDays(1))){
				// score.getFinishedOnDate(mDat);
				// tmf.complete(mDat);
				// }
				// score.getFinishedOnDate(mDat);
				// tmf.complete(mDat);
				//
				// }
			}

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
				LocalDate mDat = LocalDate.parse(d, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
				if (mDat.isBefore(LocalDate.now().minusDays(1))) {
					// if match is older than yesterday
					score.getFinishedOnDate(mDat);
					tmf.complete(mDat);
				}
			}// for

			if (tmf.readTempMatchesList.size() > 0) {
				for (MatchObj s : tmf.readTempMatchesList) {

				}
			}

		}
	}

}
