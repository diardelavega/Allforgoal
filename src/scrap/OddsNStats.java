package scrap;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import diskStore.FileHandler;
import basicStruct.BariToScorerTuple;
import basicStruct.OddsTuple;
import extra.StandartResponses;
import extra.StringSimilarity;
import structures.CountryCompetition;
import test.MatchGetter;

public class OddsNStats {
	public static final Logger logger = LoggerFactory
			.getLogger(OddsNStats.class);
	// http://oddsandstats.com/odds_comparison.asp?spoid=1&modid=1&cur_dat=20160402
	public final String mainUrl = "http://oddsandstats.com/odds_comparison.asp?spoid=1";
	public final String oddUrl = "http://oddsandstats.com";

	/*
	 * structures to keep teams or the country competion cobination that are too
	 * distant to be combined
	 */
	// teams:{"bt":"Hapoel Ironi Kiryat Shmona","st":"HAP.IR.KIRYAT SHMONA"}
	public static Map<String, String> oddsToScorer = new HashMap<String, String>();
	// allowed :{"s":"SCOTLAND_SCOTTISH LEAGUE TWO","i":135}
	public static Map<String, Integer> oddsToPunterAllowedComps = new HashMap<String, Integer>();
	// not allowed :NETHERLANDS_EREDIVISIE WOMEN
	public static List<String> oddsToPunterNotAllowedComps = new ArrayList<String>();

	private Map<String, Integer> tempOddsNStatsToCompId = new HashMap<>();
	// keep the all matches reviewed competitions as to avoid them later
	private List<Integer> empAllReviewedComps = new ArrayList<>();

	private CountryCompetition cc = new CountryCompetition();
	private FileHandler fh = new FileHandler();

	private String errorStatus = "OK";
	private String country;
	private String competition;
	private int level = -1;

	private void initOdds() {
		fh.readOdderToPunterAllowedComps();
		fh.readOdderToPunterNotAllowedComps();
		fh.readOdderToScorerTeams();
	}

	public void getOddsPage(LocalDate ld) {
		initOdds();
		String url = mainUrl + urlFormater(ld);
		Document doc = null;
		try {
			// logger.info("getting page : {}", url);
			// doc = Jsoup.parse(new File(
			// "C:/Users/Administrator/Desktop/odds_1.html"), "UTF-8");

			doc = Jsoup
					.connect(url)
					.userAgent(
							"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0")
					.maxBodySize(0).timeout(600000).get();
			logger.info("Page aquired");
		} catch (Exception e) {
			e.printStackTrace();
			errorStatus = "Faulty Connection";
			logger.warn("---------:Connection not possible  {}", errorStatus);
			return;
		}
		Elements trs = doc.getElementsByClass("odds-holder").first()
				.select("table tbody tr");

		// storeSched(trs);
		oddAdder(trs, ld);
	}

	private void oddAdder(Elements trs, LocalDate ld) {

		int compId = -1;
		for (int i = 0; i < trs.size(); i++) {
			Element td = trs.get(i).getElementsByTag("td").get(1);
			if (!td.hasClass("slc")) {
				String title = td.getElementsByTag("a").get(0).attr("title");
				String span = td.getElementsByTag("a").get(0).getElementsByTag("span").get(0).text();

				if (skipComp(title))
					continue;

//				logger.info("match  {}",trs.get(i).getElementsByTag("td").get(2).text());
				attributeFiller(title, span);// country, comp, level creation
				compId = getCompId(title);
				
			}
			if (compId < 0) {
				continue;
			}
			// all matches of compId are filled with odds
			if (MatchGetter.reviewedAndEmptyOdds.contains(compId))
				continue;
//			logger.info("{},  '{}',  {}", country, competition, level);

			if (compId > -1) // wonted competition
				if (MatchGetter.schedNewMatches.get(compId) == null) {
					continue;// no matches of this competition
				}
			String[] teams = trs.get(i).getElementsByTag("td").get(2).text()
					.split(" - ");

			int k = teamCombinationScorerOdder(teams[0], teams[1], compId);
			// @ map with compId, @ the idx k of the list the match is it.
			if (k < 0) {
				continue;
			}

			// get odds 1x2 & go to link & get o/u ods
			double _1 = 1, _x = 1, _2 = 1, _o = 1, _u = 1;

			if (trs.get(i).getElementsByTag("td").get(7).text() != " ") {
				try {
					_1 = Double.parseDouble(trs.get(i).getElementsByTag("td")
							.get(7).text().replace(",", "."));
				} catch (NumberFormatException e) {
				}
			}
			if (trs.get(i).getElementsByTag("td").get(9).text() != " ") {
				try {
					_x = Double.parseDouble(trs.get(i).getElementsByTag("td")
							.get(9).text().replace(",", "."));
				} catch (NumberFormatException e) {
				}
			}
			if (trs.get(i).getElementsByTag("td").get(11).text() != " ") {
				try {
					_2 = Double.parseDouble(trs.get(i).getElementsByTag("td")
							.get(11).text().replace(",", "."));
				} catch (NumberFormatException e) {
				}
			}

			String oddLink = trs.get(i).getElementsByTag("td").get(14).getElementsByTag("a").get(0).attr("href");
			// go to the new Page and grab O/U odds
			OddsNStatsMatchOdd moos = new OddsNStatsMatchOdd(oddUrl + oddLink);
			moos.pageGraber();
			_o = moos.getOver();
			_u = moos.getUnder();
			
			MatchGetter.schedNewMatches.get(compId).get(k).set_1(_1);
			MatchGetter.schedNewMatches.get(compId).get(k).set_x(_x);
			MatchGetter.schedNewMatches.get(compId).get(k).set_2(_2);
			MatchGetter.schedNewMatches.get(compId).get(k).set_o(_o);
			MatchGetter.schedNewMatches.get(compId).get(k).set_u(_u);
		}
	}

	private int teamCombinationScorerOdder(String t1, String t2, int compId) {
		/*
		 * for the match we found in bari we search the analog match (team1 &
		 * team2) from the list of scorer matches with the same compId
		 */

		float d1 = 0, d2 = 0, td = 1000 ;
		float mind1 = 1000, mind2 = 1000;
		int k=-1;

		if (oddsToScorer.get(t1) != null) {
			t1 = oddsToScorer.get(t1);
		}
		if (oddsToScorer.get(t2) != null) {
			t2 = oddsToScorer.get(t2);
		}

		// Loop through the scheduled matches of competition
		// TODO change it Xscorer vs MatchGetter
		boolean finishReviewingFlag = true;
		for (int i = 0; i < MatchGetter.schedNewMatches.get(compId).size(); i++) {
			// dismis previously combined matches -1 the taken ones
			if (MatchGetter.schedNewMatches.get(compId).get(i).getFt1() != -1) {
				finishReviewingFlag = false;
				d1 = StringSimilarity.levenshteinDistance(MatchGetter.schedNewMatches.get(compId).get(i).getT1(),t1);
				d2 = StringSimilarity.levenshteinDistance(MatchGetter.schedNewMatches.get(compId).get(i).getT2(),t2);
				if (d1 + d2 < td) {
					k = i;
					td = d1 + d2;
					mind1 = d1;
					mind2 = d2;
				}
			}
		}
		if (finishReviewingFlag) {
			// if it has no pending matches
			MatchGetter.reviewedAndEmptyOdds.add(compId);
//			tempAllReviewedComps.add(compId);
			return -1;
		}

		if (td == 1000) {
			/*
			 * in case this site has more matches than xscorer. It means that
			 * the compId will be valid but when it tries to combine the teams
			 * it will find none aviable and it will not change the td value.
			 */
			return StandartResponses.ALL_MATCHES_TAKEN;
		}

		if (mind1 <= StandartResponses.TEAM_DIST) {
			if (mind2 <= StandartResponses.TEAM_DIST) {
				MatchGetter.schedNewMatches.get(compId).get(k).setFt1(-1);
				return k;
			} else {
				oddsToScorer.put(t2, MatchGetter.schedNewMatches.get(compId).get(k).getT2());
				fh.appendOdderToScorerTeams(t2, MatchGetter.schedNewMatches.get(compId).get(k).getT2());
				MatchGetter.schedNewMatches.get(compId).get(k).setFt1(-1);
				return k;
			}
		} else if (mind2 <= StandartResponses.TEAM_DIST) {
			// t1 is already proved bigger than team_dist
			oddsToScorer.put(t1, MatchGetter.schedNewMatches.get(compId).get(k).getT1());
			fh.appendOdderToScorerTeams(t1,MatchGetter.schedNewMatches.get(compId).get(k).getT1());
			MatchGetter.schedNewMatches.get(compId).get(k).setFt1(-1);
			return k;
		} else {
			logger.info("escaped {} {}",t1,t2);
			return -1;
		}
	}

	private int getCompId(String title) {
		if (country == null) {
			return -1;
		}
		Integer id;
		if (oddsToPunterNotAllowedComps.contains(ccc()) == true)
			return -1;
		if (tempOddsNStatsToCompId.get(title) != null) {
			id = tempOddsNStatsToCompId.get(title);
			return id;
		} else {
			if (oddsToPunterAllowedComps.get(ccc()) != null) {
				id = oddsToPunterAllowedComps.get(ccc());
				return id;
			}
		}

		if (level > 1) {
			id = cc.searchCompBinaryLevel(country, level);
		} else {
			id = cc.searchCompBinary(country, competition, true);
		}
		if (id >= 0) {
			tempOddsNStatsToCompId.put(title, cc.ccasList.get(id).getCompId());
			return cc.ccasList.get(id).getCompId();
		} else {
			// show teams that didint make it
//			logger.info("rejects '{}'", ccc());
			return id;
		}
	}

	// ------------------------HELPERS
	private void attributeFiller(String title, String span) {
		/* extract country, competition and posibly level of the matches in hand */

		String st[] = title.split(" ");
		if (inverter(st[0])) {
			String[] temp = title.substring(st[0].length() + 1, title.length())
					.split(" - ");
			competition = st[0] + " " + temp[0];
			country = temp[1];
			leveler(span);
			return;
		}
		if (splitHelper(st[0])) {
			country = st[0] + " " + st[1];
			competition = st[2];
			;
			for (int i = 3; i < st.length; i++) {
				competition += st[i];
				if (i < st.length - 1)
					competition += " ";
			}
			leveler(span);
			return;
		}
		if (st[0].equals("Bosnia")) {
			country = "Bosnia and Herzegovina";
			competition = "Premier Liga";
			level = -1;
		}

		country = st[0];
		competition = title.substring(country.length() + 1, title.length());
		leveler(span);

	}

	private void leveler(String s) {
		s = s.replaceAll("[A-Z]", "");
		if (s.length() > 0) {
			level = Integer.parseInt(s);
		} else {
			level = -1;
		}
	}

	private boolean splitHelper(String s) {
		if (s.equals("Northen") || s.equals("Rep.") || s.equals("Czech")
				|| s.equals("Korea")|| s.equals("Costa") ) {
			return true;
		}
		return false;
	}

	private boolean inverter(String s) {
		if (s.equals("Lega") || s.equals("Primera")) {
			return true;
		}
		return false;
	}

	private String urlFormater(LocalDate ld) {
		StringBuilder sb = new StringBuilder();
		sb.append(ld.getYear());
		sb.append(ld.getMonthValue());
		sb.append(ld.getDayOfMonth());
		return "&modid=1&cur_dat=" + sb.toString();
	}

	private String ccc() {
		/* country competition combination */
		return country + "_" + competition;
	}

	public String getErrorStatus() {
		return errorStatus;
	}

	public void setErrorStatus(String errorStatus) {
		this.errorStatus = errorStatus;
	}

	private boolean skipComp(String s) {
		if (s.contains("World")
				|| s.contains("Africa")
				|| s.contains("ACN")// africa cup of nations
				|| s.contains("America") || s.contains("Europe")
				|| s.contains("Europa") || s.contains("Asia")
				|| s.contains("Friendly") || s.contains("Cup")
				|| s.contains("Kup") || s.contains("Kupa")
				|| s.contains("Coppa") || s.contains("Copa")
				|| s.contains("Off") || s.contains("Trophy")
				|| s.contains("Coupe") || s.contains("Euro")
				|| s.contains("po")) {
			return true;
		}
		return false;
	}
}
