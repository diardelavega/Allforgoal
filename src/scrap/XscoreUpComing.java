package scrap;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import extra.Status;
import extra.StringSimilarity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.select.Evaluator.MatchesOwn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import extra.Unilang;
import structures.CountryCompetition;
import basicStruct.CCAllStruct;
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
	public static List<MatchObj> finNewMatches = new ArrayList<>();
	public static Map<Integer, List<MatchObj>> schedNewMatches = new HashMap<>();
	public static List<MatchObj> errorNewMatches = new ArrayList<>();
	private final String mainUrl = "http://www.xscores.com/soccer/all-games/";// <-
	private Unilang ul = new Unilang();

	private void scrapMatchesDate(LocalDate dat, String _status)
			throws IOException {
		// get matches of the date from xScored.soccer; collect only matches
		// based on the specified status. The matches are stored into their
		// respective arrays

		String url = allDateFormater(dat);
		Document doc = null;
		try {
			logger.info(url);
			// doc = Jsoup.parse(new File(
			// "C:/Users/Administrator/Desktop/Scores.html"), "UTF-8");
			doc = Jsoup
					.connect(url)
					.userAgent(
							"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0")
					.maxBodySize(0).timeout(600000).get();
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
				// logger.info("country {},   comp {}", clasVal[0], clasVal[4]);

				if (clasVal[0].contains("WORLD")
						|| clasVal[0].contains("AFRICA")
						|| clasVal[0].contains("AMERICA")
						|| clasVal[0].contains("EUROPE")
						|| clasVal[0].contains("ASIA")
						|| clasVal[4].contains("CUP")
						|| clasVal[4].contains("KUPA")
						|| clasVal[4].contains("COPPA")
						|| clasVal[4].contains("COPA")
						|| clasVal[4].contains("OFF")
						|| clasVal[4].contains("TROPHY")
						|| clasVal[4].contains("COUPE")
						|| clasVal[4].contains("EURO")) {
					continue;
				}

				int compId = searchForCompId(clasVal[0], clasVal[4]);
				if (compId < 0) {
					// TODO display un-found matches
					ul.appendUnfoundTerms(clasVal[0], clasVal[4]);
					continue;
				} else {
					// logger.info("comp-: {}   id-: {}    found_idx-: {}",
					// CountryCompetition.ccasList.get(compIdx)
					// .getCompetition(),
					// CountryCompetition.ccasList.get(compIdx) .getCompId(),
					// compIdx);
					Elements tds = row.getElementsByTag("td");
					String status = tds.get(1).text();
					String t1 = tds.get(5).text();
					String t2 = tds.get(9).text();
					mobj = new MatchObj();
					mobj.setT1(t1);
					mobj.setT2(t2);
					logger.info("t1-{}  t2-{}", t1, t2);

					if (_status.equals(Status.SCHEDULED)
							&& (status.equals(Status.SCHEDULED) || status
									.equals(Status.FTR))) {
						mobj.setDat(Date.valueOf(dat));
						mobj.setComId(compId);

						if (schedNewMatches.get(compId) == null) {
							List<MatchObj> mol = new ArrayList<>();
							mol.add(mobj);
							schedNewMatches.put(compId, mol);
						} else {
							schedNewMatches.get(compId).add(mobj);
						}

						logger.info("scheduled ---  t1-{}  t2-{}", t1, t2);
						continue;
					}

					if (_status == Status.FINISHED) {
						if (status.equals(Status.ABANDONED)
								|| status.equals(Status.CANCELED)
								|| status.equals(Status.POSTPONED)) {
							// find interrupted matches to delete them
							errorNewMatches.add(mobj);
							logger.info("ABANDONED ---  t1-{}  t2-{}", t1, t2);
						}
						if (status.equals(Status.FINISHED)) {
							String[] scores;
							// HT score - @ td_14
							if (tds.get(13).text().length() >= 3) {// score-score
								scores = tds.get(13).text().split("-");
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
								scores = tds.get(14).text().split("-");
								try {// just in case some error happens
									int ft1 = Integer.parseInt(scores[0]);
									int ft2 = Integer.parseInt(scores[1]);
									mobj.setFt1(ft1);
									mobj.setFt2(ft2);
								} catch (NumberFormatException e) {
									logger.warn(" SOMTHING WHENT WRONG WITH THE FT SCORE");
								}
							}
							logger.info(
									"FINISHED ---  t1-{} vs t2-{} ;; {} , {}  ",
									t1, t2, tds.get(13).text(), tds.get(14)
											.text());
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
		try {
			scrapMatchesDate(dat, Status.FINISHED);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void getScheduledOnDate(LocalDate dat) {
		// matches of that date that are scheduled(status Sched'); `*`* *
		try {
			scrapMatchesDate(dat, Status.SCHEDULED);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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

	public void clearLists() {
		schedNewMatches.clear();
		finNewMatches.clear();
		errorNewMatches.clear();
	}

	// ----------------------------------

	private int searchForCompId(String country, String comp) throws IOException {
		/*
		 * This function gets the index in the ccas structure for the
		 * competition we are searching and returns the id of that competition
		 */

		CountryCompetition cc = new CountryCompetition();
		/*
		 * cc allowed competitions should take a combination of name and
		 * competition because many competitions have the same name but allways
		 * a unique combination of name&competition
		 */
		Integer searchCompIdx = cc.allowedcomps.get(couComComb(country, comp));
		if (searchCompIdx != null) {
			return cc.ccasList.get(searchCompIdx).getCompId();
		} else {
			// search for country&comp in scorerDataStruct
			searchCompIdx = cc.scorerCompIdSearch(country, comp);
			if (searchCompIdx > -1) {
				if (cc.sdsList.get(searchCompIdx).getDb() == 1) {
					cc.allowedcomps.put(couComComb(country, comp),cc.sdsList
							.get(searchCompIdx).getCompId());
					return cc.sdsList.get(searchCompIdx).getCompId();
				}
			}
			return searchCompIdx;
		}

		// Unilang ul = new Unilang();
		// {
		// // String newCountry = ul.scoreToCcas(country);
		// // String newComp = ul.scoreToCcas(comp);
		//
		// // if (newCountry != null) {
		// // if (newComp != null) {// binarysearch newComp no levistein
		// // searchCompIdx = cc.searchCompBinary(newCountry,
		// // newComp, false);
		// // if (searchCompIdx >= 0) {
		// // // ul.addTerm(newCountry, country);
		// // // ul.addTerm(newComp, comp);
		// // if (cc.ccasList.get(searchCompIdx).getDb() == 1) {
		// // cc.addAllowedComp(comp, searchCompIdx + 1);
		// // } else {
		// // return -1;
		// // }
		// // }
		// // } else {// bynarysearch with levistain for competition
		// // searchCompIdx = cc.searchCompBinary(newCountry, comp,
		// // true);
		// // if (searchCompIdx >= 0) {
		// // // ul.addTerm(newCountry, country);
		// // ul.addTerm(cc.ccasList.get(searchCompIdx)
		// // .getCompetition(), comp);
		// // if (cc.ccasList.get(searchCompIdx).getDb() == 1) {
		// // cc.addAllowedComp(comp, searchCompIdx + 1);
		// // } else {
		// // return -1;
		// // }
		// // }
		// // }
		// // } else {
		// // if (newComp != null) { // search usable (DB)newComp no
		// // // levistein
		// // searchCompIdx = cc.searchUsableComp(newComp, false);
		// // if (searchCompIdx >= 0) {
		// // // in case the leagues have the same name but belong
		// // // to
		// // // different countries, check the country too
		// // if (StringSimilarity.levenshteinDistance(
		// // country,
		// // CountryCompetition.ccasList.get(
		// // searchCompIdx).getCountry()) > 3) {
		// // return -1;
		// // }
		// // ul.addTerm(
		// // CountryCompetition.ccasList.get(
		// // searchCompIdx).getCountry(),
		// // country);
		// // cc.addAllowedComp(comp, searchCompIdx + 1);
		// // }
		// // } else {
		// // searchCompIdx = cc.searchUsableComp(comp, true);
		// // if (searchCompIdx >= 0) {
		// // if (StringSimilarity.levenshteinDistance(
		// // country,
		// // CountryCompetition.ccasList.get(
		// // searchCompIdx).getCountry()) > 3) {
		// // return -1;
		// // }
		// // ul.addTerm(
		// // CountryCompetition.ccasList.get(
		// // searchCompIdx).getCountry(),
		// // country);
		// // ul.addTerm(cc.ccasList.get(searchCompIdx)
		// // .getCompetition(), comp);
		// // cc.addAllowedComp(comp, searchCompIdx + 1);
		// // }
		// // }
		// // }
		// }// empty

		//
	}

	private String couComComb(String country, String competition) {
		return country + "_" + competition;
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
