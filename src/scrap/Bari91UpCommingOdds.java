package scrap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
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

import com.google.gson.Gson;

import diskStore.FileHandler;
import extra.StandartResponses;
import extra.StringSimilarity;
import basicStruct.BariToPunterTuple;
import basicStruct.BariToScorerTuple;
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
/**
 * @author Administrator
 *
 *         A class to scrap collect and assigne 1x2 and O/U odds to the matches
 *         that we got from scorer. Once we get a td element the country,
 *         competition and level(category) are extractet from it. with this 3
 *         attributes begins the search for the competition id. As a aid for a
 *         faster search or elimination of a match during the search the we use
 *         some structures that show the allowed and not allowed competitions.
 *         Also some similarities between teams that are hard to be combined.
 *         The search for the compId is done in the ccallstruct with the
 *         compNames from SoccerPunter. The search is done there because of the
 *         level attribute. ***************************************************
 *         The usage of remainig matches, matches that were present in bari but
 *         not scorer is depricates since the usage of the new odds page
 *         oodsandstats.********************************8***************** In
 *         the btsTeams we keep a combination of bari-scorer teams that are
 *         quite distant to be combined automatically******************** For
 *         analogy here we should use the countryCompetitionCombination method
 *         but since we not allways have a competition name we use directly the
 *         string we scraped
 * 
 */
public class Bari91UpCommingOdds {
	// public class Bari91UpCommingOdds {
	public static final Logger logger = LoggerFactory
			.getLogger(Bari91UpCommingOdds.class);
	// http://www.bari91.com/previews/2016-03-30
	public static final String bariUrl = "http://www.bari91.com/previews/";
	// bts bari to scorer : matches distant to be found
	public static Map<String, String> btsTeams = new HashMap<String, String>();
	// bari to punter: competitions hard to be found
	public static Map<String, Integer> btpAllowed = new HashMap<>();
	public static List<String> btpNotAllowed = new ArrayList<>();
	// remaining matches
	// public static List<MatchObj> remainings = new ArrayList<>();

	private String errorStatus = "OK"; // a simple way to report problems
	private Map<String, Integer> tempBariToCompId = new HashMap<>();
	private List<String> rejects = new ArrayList<>();// comps we don;t need
														// today
	private String country;
	private String compName;
	private int level;

	private boolean remainingFlag = false;

	// private Gson gson = new Gson();
	private CountryCompetition cc = new CountryCompetition();
	private FileHandler fh = new FileHandler();

	public void initBari91UpCommingOdds() {
		fh.readBariToScorer();
		fh.readAllowedBariToPunter();
		fh.readNotAllowedBariToPunter();
	}

	public void scrapBariPage(LocalDate ld) {
		initBari91UpCommingOdds();

		String url = bariUrl + ld.toString();
		Document doc = null;
		try {
			logger.info("getting page : {}", url);
			// doc = Jsoup.parse(new File(
			// "C:/Users/Administrator/Desktop/skedina/bari91_1.html"),
			// "UTF-8");

			doc = Jsoup
					.connect(url)
					.userAgent(
							"Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.101 Safari/537.36 OPR/40.0.2308.62")
					.maxBodySize(0).timeout(600000).get();
			logger.info("Page aquired");
		} catch (Exception e) {
			e.printStackTrace();
			errorStatus = "Faulty Connection";
			logger.warn("---------:Connection not possible  {}", errorStatus);
			return;
		}
logger.info("{}",doc);
//		Elements trs = doc.getElementsByClass("vrsta");
		logger.info("{}", doc);
		Element mid_tab = doc.getElementsByClass("middle-panel").get(0);
		Element div = mid_tab.getElementsByClass("padding-10").get(0);
		// Class("middle-panel").get(0);
		Elements trs = div.getElementsByTag("table tbody tr");
		// Class("vrsta");
		int compId = 1;
		for (Element tr : trs) {
			String ss = tr.getElementsByTag("td").get(1).text();
			if (skipComp(ss)) {
				continue;
			}

			ss = ss.replaceFirst("\u00A0", "");
			// logger.info("'{}'",ss);
			// --------------------Check if already stored-------
			if (tempBariToCompId.get(ss) == null) {
				if (btpNotAllowed.contains(ss) == true) {
					continue;
				}
				if (btpAllowed.get(ss) != null) {
					compId = btpAllowed.get(ss);
				} else {
					countryCompLevel(ss);// get country, comp, level
					compId = getCompId(ss);
				}
			} else {
				compId = tempBariToCompId.get(ss);
			}
			// -----------------------------

			if (compId >= 0) {// valid comp id; search Scprer matches & add odds
				if (MatchGetter.schedNewMatches.get(compId) == null) {
					// there is no such competition matches @ scorer, xscorer
					continue;
				}

				String[] teams = tr.getElementsByTag("td").get(2).text()
						.replaceFirst("\u00A0", "").split(" - ");

				int k = teamCombinationScorerBari(teams[0], teams[1], compId);
				if (k == StandartResponses.DEFAULT_NULL)
					continue;
				if (k == StandartResponses.ALL_MATCHES_TAKEN) {
					continue;
				}

				// logger.info("{} {} {}", country, compName, level);
				float _1 = 1, _x = 1, _2 = 1, _o = 1, _u = 1;

				if (tr.getElementsByTag("td").get(3).text() != " ") {
					try {
						_1 = Float.parseFloat(tr.getElementsByTag("td").get(3)
								.text());
					} catch (NumberFormatException e) {
					}
				}
				if (tr.getElementsByTag("td").get(4).text() != " ") {
					try {
						_x = Float.parseFloat(tr.getElementsByTag("td").get(4)
								.text());
					} catch (NumberFormatException e) {
					}
				}
				if (tr.getElementsByTag("td").get(5).text() != " ") {
					try {
						_2 = Float.parseFloat(tr.getElementsByTag("td").get(5)
								.text());
					} catch (NumberFormatException e) {
					}
				}
				if (tr.getElementsByTag("td").get(6).text() != " ") {
					try {
						_o = Float.parseFloat(tr.getElementsByTag("td").get(6)
								.text());
					} catch (NumberFormatException e) {
					}
				}
				if (tr.getElementsByTag("td").get(7).text() != " ") {
					try {
						_u = Float.parseFloat(tr.getElementsByTag("td").get(7)
								.text());
					} catch (NumberFormatException e) {
					}
				}

				MatchGetter.schedNewMatches.get(compId).get(k).set_1(_1);
				MatchGetter.schedNewMatches.get(compId).get(k).set_x(_x);
				MatchGetter.schedNewMatches.get(compId).get(k).set_2(_2);
				MatchGetter.schedNewMatches.get(compId).get(k).set_o(_o);
				MatchGetter.schedNewMatches.get(compId).get(k).set_u(_u);
				// }
			}// if compId >=0
		}// for elements
	}

	private int teamCombinationScorerBari(String t1, String t2, int compId) {
		/*
		 * for the match we found in bari we search the analog match (team1 &
		 * team2) from the list of matches from scorer with the same compId. The
		 * match which is closer to the curent match in hand is selected to have
		 * its odds altered with the scraped odds.
		 */

		// -------------------TEST
		// for (Integer key : MatchGetter.schedNewMatches.keySet())
		// Integer key = compId;
		// for (int i = 0; i < MatchGetter.schedNewMatches.get(key).size(); i++)
		// {
		// if (MatchGetter.schedNewMatches.get(key).get(i).getFt1() != -1) {
		// logger.info("{},  {} - {}",key,MatchGetter.schedNewMatches.get(compId).get(i).getT2(),MatchGetter.schedNewMatches.get(compId).get(i).getT1());
		// }
		// }
		// -------------

		if (btsTeams.get(t1) != null) {
			t1 = btsTeams.get(t1);
		}
		if (btsTeams.get(t2) != null) {
			t2 = btsTeams.get(t2);
		}

		float d1 = 0, d2 = 0, td = 1000;
		float mind1 = 1000, mind2 = 1000;
		int k = -1;

		for (int i = 0; i < MatchGetter.schedNewMatches.get(compId).size(); i++) {
			// dismis previously combined matches -1 the taken ones
			if (MatchGetter.schedNewMatches.get(compId).get(i).getFt1() != -1) {
				logger.info("{}  {}", MatchGetter.schedNewMatches.get(compId)
						.get(i).getT1(), MatchGetter.schedNewMatches
						.get(compId).get(i).getT2());
				d1 = StringSimilarity.teamSimilarity(
						MatchGetter.schedNewMatches.get(compId).get(i).getT1(),
						t1);
				d2 = StringSimilarity.teamSimilarity(
						MatchGetter.schedNewMatches.get(compId).get(i).getT2(),
						t2);
				if (d1 + d2 < td) {
					k = i;
					td = d1 + d2;
					mind1 = d1;
					mind2 = d2;
				}
			}
		}

		if (td == 1000) {
			/*
			 * in case bari has more matches than xscorer. It means that the
			 * compId will be valid but when it tries to combine the teams it
			 * will find none aviable and it will not change the td value.
			 */
			return StandartResponses.ALL_MATCHES_TAKEN;
		}

		/*
		 * after the closest match is been chosen, we check that it is whithin
		 * our chosen parameters. also we use the fact that have found one of
		 * the teams to store a combination of the other team in both versions;
		 * bari & scorer
		 */
		if (mind1 <= StandartResponses.TEAM_DIST) {
			if (mind2 <= StandartResponses.TEAM_DIST) {
				MatchGetter.schedNewMatches.get(compId).get(k).setFt1(-1);
				return k;
			} else {
				btsTeams.put(t2, MatchGetter.schedNewMatches.get(compId).get(k)
						.getT2());
				fh.apendBariToScorer(t2, MatchGetter.schedNewMatches
						.get(compId).get(k).getT2());
				MatchGetter.schedNewMatches.get(compId).get(k).setFt1(-1);
				return k;
			}
		} else if (mind2 <= StandartResponses.TEAM_DIST) {
			// t1 is already proved bigger than team_dist
			btsTeams.put(t1, MatchGetter.schedNewMatches.get(compId).get(k)
					.getT1());
			fh.apendBariToScorer(t1, MatchGetter.schedNewMatches.get(compId)
					.get(k).getT1());
			MatchGetter.schedNewMatches.get(compId).get(k).setFt1(-1);
			return k;
		} else {
			return -1;
		}
	}

	private int getCompId(String ss) {
		/*
		 * search the country competition ccas and get the competition id not
		 * the index id. If already found before it should be stored in the MAP
		 * tempBariToCompId for a quick search it is stored as it came from the
		 * bari page scrap.
		 */
		Integer compIdx;
		if (country == null) {
			return -1;
		}

		if (level != -1) {
			// binary search for country then partial search for level
			compIdx = cc.searchCompBinaryLevel(country, level);
		} else {
			compIdx = cc.searchCompBinary(country, compName, true);
		}
		if (compIdx >= 0) {
			tempBariToCompId.put(ss, cc.ccasList.get(compIdx).getCompId());
			return cc.ccasList.get(compIdx).getCompId();
		} else {

			// logger.info("unaccepted ---- '{}'", ss);
			return compIdx;// -1
		}

	}

	private void countryCompLevel(String s) {
		/*
		 * exctract the name of the country and copetition or country and
		 * division level
		 */
		// s = s.replaceFirst("\u00A0", "");
		String[] ss;
		if (s.contains(",")) {
			ss = s.split(", ");
			country = ss[0].split(" ")[0];
			compName = ss[1];
			level = -1;

		} else {
			ss = s.split(" ");
			StringBuilder sb = new StringBuilder();

			for (int i = 0; i < ss.length; i++) {
				// logger.info("'{}'   length {}", ss[i], ss[i].length());
				if (ss[i].length() <= 1) {
					try {
						level = Integer.parseInt(ss[i]);
						compName = null;
						country = sb.toString();
						return;
					} catch (NumberFormatException e) {
						e.printStackTrace();
					}
				} else {
					sb.append(ss[i]);
					if (i < ss.length - 2)
						sb.append(" ");
				}
			}
			country = sb.toString();
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

	// ////////////STORE & READ//////////////////
	public void storeSched(Document d) {
		File dir = new File("C:/m/");
		File doc = new File(dir + "/doc");
		if (!dir.exists()) {
			dir.mkdir();
		}
		try {
			ObjectOutputStream oos = new ObjectOutputStream(
					new FileOutputStream(doc));
			oos.writeObject(d);
			oos.close();
		} catch (IOException e) {
			// oos.close();
			e.printStackTrace();
		}
	}

	public void readSched() {
		File dir = new File("C:/m/");
		File doc = new File(dir + "/doc");
		Document d = null;
		try {
			if (!dir.exists()) {
				dir.mkdir();
			}
			if (!doc.exists()) {
				doc.createNewFile();
				logger.warn("Doc couldn't be read");
				return;
			}

			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(
					doc));
			d = (Document) ois.readObject();
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
