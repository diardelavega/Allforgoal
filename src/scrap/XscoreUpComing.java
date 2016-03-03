package scrap;

import java.io.File;
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
	private Map<String, Integer> allowedcomps = new HashMap<>();

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

	public void scrapMatchesDate(LocalDate dat) {
		String url = allDateFormater(dat);
		Document doc = null;
		try {
			logger.info(url);
			// doc = Jsoup.connect(url )
			doc = Jsoup.parse(new File("C:/Users/diego/Desktop/Scores.html"),
					"UTF-8");
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
				}
				else{
					
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

	public int searchForCompId(String country, String comp) {
		// search in allowed competitions map
		Integer searchCompId = allowedcomps.get(comp);
		if (searchCompId != null) {
			return searchCompId;
		} else {
			/*
			 * search in unilab for analog word in ccas comps ** TO BE THOUGHT
			 * {Unilang ul = new Unilang(); String countryName=
			 * ul.scoreToccas(country); String compName= ul.scoreToccas(comp); }
			 */

			// use advanced searching and comparing
			CountryCompetition cc = new CountryCompetition();
			searchCompId = cc.fullSearch(country, comp);
			if (searchCompId > 0) {
				allowedcomps.put(comp, searchCompId);// enrich allowed comp
				return searchCompId;
			}
		}
		return searchCompId;

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
