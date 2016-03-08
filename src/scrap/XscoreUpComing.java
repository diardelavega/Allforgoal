package scrap;

import java.io.File;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import extra.Status;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import extra.Unilang;
import structures.CountryCompetition;
import basicStruct.MatchObj;

/**
 * @author diego
 *
 *         it will serve to scrap from xscore/soccer the upcoming matches of the
 *         day. ***************************************************************
 *         Get all matches as are normally on the xScorer page and put them in
 *         the list; Before they are put in the DB TempMathes Table, transform
 *         them to the DB Punter matches format. During the correlation process
 *         the unilang maps should have been populated with the corresponding
 *         names of each country, competition and teams. After the first search
 *         then we can find each-other easy by using unilang Team Maps
 * 
 */

public class XscoreUpComing {
	public static final Logger logger = LoggerFactory
			.getLogger(XscoreUpComing.class);
	public static List<MatchObj> schedNewMatches = new ArrayList<>();
	public static List<MatchObj> finNewMatches = new ArrayList<>();
	public static List<MatchObj> errorNewMatches = new ArrayList<>();
	private final String mainUrl = "http://www.xscores.com/soccer/all-games/";// <-

	private void scrapMatchesDate(LocalDate dat, String _status) {
		// get matches of the date from xScored.soccer; collect only matches
		// based on the specified status. The matches are stored into their
		// respective arrays

		String url = allDateFormater(dat);
		Document doc = null;
		try {
			logger.info(url);
			// doc = Jsoup.connect(url )
			// doc = Jsoup.parse(new
			// File("C:/Users/diego/Desktop/Scores.html"),"UTF-8");
			doc = Jsoup.parse(new File(
					"C:/Users/Administrator/Desktop/Scores.html"), "UTF-8");
			// .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0)
			// Gecko/20100101 Firefox/23.0")
			// .maxBodySize(0).timeout(600000).get();
		} catch (Exception e) {
			logger.info("couldnf connect or parse the page");
			e.printStackTrace();
		}

		Element tab = doc.select("table table table").get(2);
		Elements mrows = tab.getElementsByTag("tr");
		MatchObj mobj;
		for (Element row : mrows) {
			if (row.hasAttr("class") && row.attr("class").contains("#")) {
				String[] clasVal = row.attr("class").split("#");
				logger.info("country {},   comp {}", clasVal[0], clasVal[4]);

				int compId = searchForCompId(clasVal[0], clasVal[4]);
				if (compId < 0) {
					continue;
				} else {
					Elements tds = row.getElementsByTag("td");
					String status = tds.get(1).text();
					String t1 = tds.get(5).text();
					String t2 = tds.get(9).text();
					mobj = new MatchObj();
					mobj.setT1(t1);
					mobj.setT2(t2);

					if (status == Status.SCHEDULED && status == _status) {
						mobj.setDat(Date.valueOf(dat));
						mobj.setComId(compId);
						schedNewMatches.add(mobj);
						continue;
					}

					if (_status == Status.FINISHED) {
						if (status == Status.ABANDONED
								|| status == Status.CANCELED
								|| status == Status.POSTPONED) {
							// find interrupted matches to delete them
							errorNewMatches.add(mobj);
						}
						if (status == Status.FINISHED) {
							String[] scores;
							// HT score - @ td_14
							if (tds.get(13).text().length() >= 3) {// score-score
								scores = tds.get(14).text().split("-");
								try {// just in case some error happens
									int ht1 = Integer.parseInt(scores[0]);
									int ht2 = Integer.parseInt(scores[1]);
									mobj.setHt1(ht1);
									mobj.setHt2(ht2);
								} catch (NumberFormatException e) {
									logger.warn(" SOMTHING WHENT WRONG WITH THE HT SCORE");
								}
							}
							if (tds.get(14).text().length() >= 3) {// FT @ td_15
								scores = tds.get(15).text().split("-");
								try {// just in case some error happens
									int ft1 = Integer.parseInt(scores[0]);
									int ft2 = Integer.parseInt(scores[1]);
									mobj.setFt1(ft1);
									mobj.setFt2(ft2);
								} catch (NumberFormatException e) {
									logger.warn(" SOMTHING WHENT WRONG WITH THE FT SCORE");
								}
							}
							finNewMatches.add(mobj);
						}// fin status
					}
				}
			}
		}

	}

	public void getFinishedOnDate(LocalDate dat) {
		// TODO search for matches of that date that are finished (status
		// 'Fin'); `8`8```*~*~ get cancelled & postponed matches
		scrapMatchesDate(dat, Status.FINISHED);

	}

	public void getScheduledOnDate(LocalDate dat) {
		// matches of that date that are scheduled(status Sched'); `*`* *
		scrapMatchesDate(dat, Status.SCHEDULED);
	}

	public void getScheduledToday() {
		getScheduledOnDate(LocalDate.now());
	}

	public void getScheduledTomorrow() {
		getScheduledOnDate(LocalDate.now().plusDays(1));
	}

	public void getFinishedToday() {
		getFinishedOnDate(LocalDate.now());
	}

	public void getFinishedYesterday() {
		getFinishedOnDate(LocalDate.now().minusDays(1));
	}

	// ----------------------------------

	public int searchForCompId(String country, String comp) {
		// search in allowed competitions map and CCAS
		CountryCompetition cc = null;
		try {
			cc = new CountryCompetition();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Integer searchCompId = cc.allowedcomps.get(comp);
		if (searchCompId != null) {
			return searchCompId;
		} else {

			Unilang ul = new Unilang();
			String newCountry = ul.scoreToCcas(country);
			String newComp = ul.scoreToCcas(comp);

			if (newCountry != null) {
				if (newComp != null) {// binarysearch newComp no levistein
					searchCompId = cc.searchCompBinary(newCountry, newComp,
							false);
					if (searchCompId > 0) {
						ul.addTerm(newCountry, country);
						ul.addTerm(newComp, comp);
						if (cc.ccasList.get(searchCompId).getDb() == 1) {
							cc.addAllowedComp(comp, searchCompId);
						}
					}
				} else {// bynarysearch with levistain for competition
					searchCompId = cc.searchCompBinary(newCountry, comp, true);
					if (searchCompId > 0) {
						ul.addTerm(newCountry, country);
						ul.addTerm(cc.ccasList.get(searchCompId)
								.getCompetition(), comp);
						if (cc.ccasList.get(searchCompId).getDb() == 1) {
							cc.addAllowedComp(comp, searchCompId);
						}
					}
				}
			} else {
				if (newComp != null) { // search usable (DB)newComp no levistein
					searchCompId = cc.searchUsableComp(newComp, false);
					if (searchCompId > 0) {
						// ul.addTerm(newCountry, country);
						ul.addTerm(newComp, comp);
						cc.addAllowedComp(comp, searchCompId);
					}
				} else {
					searchCompId = cc.searchUsableComp(comp, true);
					if (searchCompId > 0) {
						// ul.addTerm(newCountry, country);
						ul.addTerm(cc.ccasList.get(searchCompId)
								.getCompetition(), comp);
						cc.addAllowedComp(comp, searchCompId);
					}
				}
			}

			return searchCompId;
		}
		//
	}

	public void searchForTeam(String team, int compId) {
		// TODO search in unilang if the team is there
		// get teams from db and compare
		// This func probably is not needed here since we are going to use it
		// after the TempMatch<L> is filled with data
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

}
