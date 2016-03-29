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

import extra.StandartResponses;
import extra.StringSimilarity;
import basicStruct.MatchObj;
import structures.CountryCompetition;
import test.MatchGetter;

/**
 * @author Administrator GO to bari91 website and with the specific date (today
 *         or future date) get the upcomming matches for all the competitions.
 *         It Should be so that matches without odds will get one "1" as their
 *         odd. ************************************************************* It
 *         is supposed that we already have a MAP <compId,LIST<MatchObj>> with
 *         matches from xscore. From that map we corelate the matches comparing
 *         the home and away team
 */
public class Bari91UpCommingOdds {

	public static final Logger logger = LoggerFactory
			.getLogger(Bari91UpCommingOdds.class);
	// http://www.bari91.com/previews/2016-03-30
	public static final String bariUrl = "http://www.bari91.com/previews/";

	private String errorStatus = "OK"; // a simple way to report problems
	private Map<String, Integer> tempBariToCompId = new HashMap<>();
	private List<String> rejects = new ArrayList<>();// comps we don;t need
														// today

	String country;
	private String compName;
	private int level;
	private CountryCompetition cc = new CountryCompetition();

	public void scrapBariPage(LocalDate ld) {
		String url = bariUrl + ld.toString();
		Document doc = null;
		try {
			doc = Jsoup.parse(new File(
					"C:/Users/Administrator/Desktop/bari91_27.html"), "UTF-8");
			logger.info("getting page : {}", url);
			// doc = Jsoup
			// .connect(url)
			// .userAgent(
			// "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0")
			// .maxBodySize(0).timeout(600000).get();
			logger.info("Page aquired");
		} catch (Exception e) {
			e.printStackTrace();
			errorStatus = "Faulty Connection";
			logger.warn("---------:Connection not possible  {}", errorStatus);
			return;
		}

		Elements trs = doc.getElementsByClass("vrsta");
		for (Element tr : trs) {
			String cc = tr.getElementsByTag("td").get(1).text();
			if (skipComp(cc)) {
				continue;
			}

			countryCompLevel(cc);// get country, comp, level
			int compId = getCompId(cc);

			if (compId >= 0) {// valid comp id; search Scprer matches & add odds
				if (MatchGetter.schedNewMatches.get(compId) == null) {
					// there is no such competition matches @ scorer
					rejects.add(cc);
					continue;
				}

				String[] teams = tr.getElementsByTag("td").get(2).text()
						.split(" - ");

				//TODO find the most somilar matches not the ones that are within the parameters
				for (int i = 0; i < MatchGetter.schedNewMatches.get(compId)
						.size(); i++) {
					if ((StringSimilarity.levenshteinDistance(
							MatchGetter.schedNewMatches.get(compId).get(i)
									.getT1(), teams[0]) <= StandartResponses.LEV_DISTANCE)
							&& (StringSimilarity.levenshteinDistance(
									MatchGetter.schedNewMatches.get(compId)
											.get(i).getT2(), teams[1]) <= StandartResponses.LEV_DISTANCE)) {
						logger.info("T2 Is found similar");

						float _1 = 1, _x = 1, _2 = 1, _o = 1, _u = 1;
						if (tr.getElementsByTag("td").get(3).text() != " ") {
							_1 = Float.parseFloat(tr.getElementsByTag("td")
									.get(3).text());
							MatchGetter.schedNewMatches.get(compId).get(i)
									.set_1(_1);
						}
						if (tr.getElementsByTag("td").get(4).text() != " ") {
							_x = Float.parseFloat(tr.getElementsByTag("td")
									.get(4).text());
							MatchGetter.schedNewMatches.get(compId).get(i)
									.set_1(_x);
						}
						if (tr.getElementsByTag("td").get(5).text() != " ") {
							_2 = Float.parseFloat(tr.getElementsByTag("td")
									.get(5).text());
							MatchGetter.schedNewMatches.get(compId).get(i)
									.set_1(_2);
						}
						if (tr.getElementsByTag("td").get(6).text() != " ") {
							_o = Float.parseFloat(tr.getElementsByTag("td")
									.get(6).text());
							MatchGetter.schedNewMatches.get(compId).get(i)
									.set_1(_o);
						}
						if (tr.getElementsByTag("td").get(7).text() != " ") {
							_u = Float.parseFloat(tr.getElementsByTag("td")
									.get(7).text());
							MatchGetter.schedNewMatches.get(compId).get(i)
									.set_1(_u);
						}

					}// if levinsteins
				}// for
			}// if compId >=0
		}// for elements
	}

	private int getCompId(String ss) {
		/*
		 * search the country competition ccas and get the competition id not
		 * the index id. If already found before it should be stored in the MAP
		 * tempBariToCompId for a quick search.
		 */
		Integer compId = tempBariToCompId.get(ss);
		if (compId != null) {
			return compId;
		}

		if (level != -1) {
			// binary search for country then partial search for level
			compId = cc.searchCompBinaryLevel(country, level);
		} else {
			compId = cc.searchCompBinary(country, compName, true);
		}
		if (compId >= 0) {
			tempBariToCompId.put(ss, compId + 1);
		}

		return compId + 1;
	}

	private void countryCompLevel(String s) {
		/*
		 * exctract the name of the country and copetition or country and
		 * division level
		 */
		s = s.replaceFirst("\u00A0", "");
		String[] ss;
		if (s.contains(",")) {
			ss = s.split(", ");
			country = ss[0];
			compName = ss[1];
			level = -1;

		} else {
			ss = s.split(" ");
			if (ss[1].length() > 1) {
				// country names is comprized by 2 words South Korea
				country = ss[0] + " " + ss[1];
			} else {
				country = ss[0];
				compName = null;
				try {
					// Slovenia 2 => "2" -> level
					level = Integer.parseInt(ss[1]);
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
			}

		}
	}

	private boolean skipComp(String s) {
		if (rejects.contains(s))
			return true;
		if (s.contains("World")
				|| s.contains("Africa")
				|| s.contains("ACN")// africa cup of nations
				|| s.contains("America") || s.contains("Europe")
				|| s.contains("Europa") || s.contains("Asia")
				|| s.contains("Friendly") || s.contains("Cup")
				|| s.contains("Kup") || s.contains("Kupa")
				|| s.contains("Coppa") || s.contains("Copa")
				|| s.contains("Off") || s.contains("Trophy")
				|| s.contains("Coupe") || s.contains("Euro")) {
			return true;
		}
		return false;
	}

	public String getErrorStatus() {
		return errorStatus;
	}

	public String getCompName() {
		return compName;
	}

	public String getCountry() {
		return country;
	}

	public int getLevel() {
		return level;
	}
}
