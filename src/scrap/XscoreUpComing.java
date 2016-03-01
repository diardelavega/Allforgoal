package scrap;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import basicStruct.MatchObj;

/**
 * @author diego
 *
 *         it will serve to scrap from xscore/soccer the upcoming matches of the
 *         day
 *
 */
public class XscoreUpComing {
	public static final Logger logger = LoggerFactory.getLogger(XscoreUpComing.class);
	private List<MatchObj> tempNewMatches = new ArrayList<>();
	private final String mainUrl = "http://www.xscores.com/soccer/all-games/";// <-
																				// +
	// 28-02

	public void scrapMatches(String url) {
		/*
		 * TODO jsoup to site, get all matches if country ~= usable countries
		 * get it. FOR every new scoreCountryName 1) SEE IF COMPETITION WE GOT
		 * IS usable in db 2) if yes get match data
		 * country,competition,competitionId,t1,t2,dat 3)?? get bets for the
		 * match 4) store match in temp db matches table
		 */
	}

	public void getMatchesResults(String url) {
		/*
		 * TODO jsoup url and get finished matches add results to stored temp
		 * matches
		 * 
		 */
	}

	private String allDateFormater(LocalDate dat) {
		String url = mainUrl;
		String date = "";
		if (dat.getDayOfMonth() < 10) {
			date = "0" + dat.getDayOfMonth();
		} else {
			date = dat.getDayOfMonth() + "";
		}
		date = date + "-";
		if (dat.getMonthValue() < 10) {
			date = date + "0" + dat.getMonthValue();
		} else {
			date = date + dat.getMonthValue();
		}
		return mainUrl + date;

	}

	public void dateTomorrowFormat() {
		logger.info(allDateFormater(LocalDate.now().plusDays(1)));
	}

	public void dateTodayFormat() {
		logger.info(allDateFormater(LocalDate.now()));
	}

	public void dateYesterdayFormat() {
		logger.info(allDateFormater(LocalDate.now().minusDays(1)));
	}

	public List<MatchObj> getTempNewMatches() {
		return tempNewMatches;
	}

}
