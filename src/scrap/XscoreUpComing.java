package scrap;

import java.io.File;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
 *         day
 *
 */
public class XscoreUpComing {
	public static final Logger logger = LoggerFactory
			.getLogger(XscoreUpComing.class);
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
		 */
	}

	public void scrapMatchesDate(LocalDate dat) throws SQLException {
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
		for (Element row : mrows) {
			if (row.hasAttr("class") && row.attr("class").contains("#")) {
				String[] clasVal = row.attr("class").split("#");
				logger.info("country {},   comp {}", clasVal[0], clasVal[4]);

				int compId = searchForCompId(clasVal[0], clasVal[4]);
				if (compId < 0) {
					continue;
				} else {
					Elements tds = row.getElementsByTag("td");
					// for(Element td:tds){
					String status = tds.get(1).text();
					String t1 = tds.get(5).text();
					String t2 = tds.get(9).text();

					if (status == "Fin") {
						String[] scores;
						// HT score - @ td_14
						if (tds.get(13).text().length() == 3) {// score-score
							scores = tds.get(14).text().split("-");
							try {// just in case some error happens
								int ht1 = Integer.parseInt(scores[0]);
								int ht2 = Integer.parseInt(scores[1]);
							} catch (NumberFormatException e) {
								logger.warn(" SOMTHING WHENT WRONG WITH THE HT SCORE");
							}
						}
						if (tds.get(14).text().length() == 3) {// FT @ td_15
							scores = tds.get(15).text().split("-");
							try {// just in case some error happens
								int ft1 = Integer.parseInt(scores[0]);
								int ft2 = Integer.parseInt(scores[1]);
							} catch (NumberFormatException e) {
								logger.warn(" SOMTHING WHENT WRONG WITH THE FT SCORE");
							}
						}

					}
				}
				// TODO search and get competition id;=>
				/*
				 * 1)keep a list of allowed competitions & fast check to see if
				 * the comp we want (in scorer format writing) is there. * * ***
				 * 2) search on uniLang for the term (country & competition); 3)
				 * search in the accstruct for the compid {if search -> -1 skip}
				 */
				// get teams; create a new tempMatchesList; store to db
				// tempMatches table

			}
		}

	}

	public int searchForCompId(String country, String comp) throws SQLException {
		// search in allowed competitions map
		CountryCompetition cc = new CountryCompetition();
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
				if (newComp != null) {
					// search usable (DB)newComp no levistein
				} else {
					// search usable with levistain for competition
				}
			}

			// transform country from all upperCase to first letter uppercase
			// country = country.substring(0, 1).toUpperCase()
			// + country.substring(1).toLowerCase();

			// searchCompId = cc.fullSearch(country, comp);
			// if (searchCompId > 0) {
			// cc.allowedcomps.put(comp, searchCompId);// enrich allowed comp
			// return searchCompId;
			// }
			return searchCompId;
		}
		//
	}

	public void searchForTeam(String team, int compId) {
		// TODO search in unilang if the team is there
		// get teams from db and compare
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
