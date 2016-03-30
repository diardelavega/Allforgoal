package scrap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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

import diskStore.FileHandler;
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
	public static Map<String, String> bts = new HashMap<String, String>();

	private String errorStatus = "OK"; // a simple way to report problems
	private Map<String, Integer> tempBariToCompId = new HashMap<>();
	private List<String> rejects = new ArrayList<>();// comps we don;t need
														// today

	String country;
	private String compName;
	private int level;
	private CountryCompetition cc = new CountryCompetition();
	private FileHandler fh = new FileHandler();

	public void initBari91UpCommingOdds() {

		try {
			// automatically fills the map
			fh.readBariToScorer();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void scrapBariPage(LocalDate ld) throws IOException {
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
//		storeSched(doc);
		oddAdder(doc);
	}

	private void oddAdder(Document doc) throws IOException {

		Elements trs = doc.getElementsByClass("vrsta");
		for (Element tr : trs) {
			String cc = tr.getElementsByTag("td").get(1).text();
			if (skipComp(cc)) {
				continue;
			}

			countryCompLevel(cc);// get country, comp, level
			logger.info("{} {} {}", country, compName, level);

			int compId = getCompId(cc);
			if (compId >= 0) {// valid comp id; search Scprer matches & add odds
				if (MatchGetter.schedNewMatches.get(compId) == null) {
					// there is no such competition matches @ scorer
					rejects.add(cc);
					logger.info("reject {} {} {}", country, compName, level);
					// TODO display all rejects
					continue;
				}

				String[] teams = tr.getElementsByTag("td").get(2).text().split(" - ");
				teams[0] = teams[0].replaceFirst("\u00A0", "");
				teams[1] = teams[1].replaceFirst("\u00A0", "");
				//TODO put here the baritoscore and see if booth teams ar in it then search
				// or put it in the teamcombination function

				int k = teamCombinationScorerBari(teams[0], teams[1], compId);
				if (k < 0)
					continue;

				logger.info("{} {} {}", country, compName, level);
				double _1 = 1, _x = 1, _2 = 1, _o = 1, _u = 1;

				if (tr.getElementsByTag("td").get(3).text() != " ") {
					try {
						_1 = Double.parseDouble(tr.getElementsByTag("td")
								.get(3).text());
					} catch (NumberFormatException e) {
					}
				}
				if (tr.getElementsByTag("td").get(4).text() != " ") {
					try {
						_x = Double.parseDouble(tr.getElementsByTag("td")
								.get(4).text());
					} catch (NumberFormatException e) {
					}
				}
				if (tr.getElementsByTag("td").get(5).text() != " ") {
					try {
						_2 = Double.parseDouble(tr.getElementsByTag("td")
								.get(5).text());
					} catch (NumberFormatException e) {
					}
				}
				if (tr.getElementsByTag("td").get(6).text() != " ") {
					try {
						_o = Double.parseDouble(tr.getElementsByTag("td")
								.get(6).text());
					} catch (NumberFormatException e) {
					}
				}
				if (tr.getElementsByTag("td").get(7).text() != " ") {
					try {
						_u = Double.parseDouble(tr.getElementsByTag("td")
								.get(7).text());
					} catch (NumberFormatException e) {
					}
				}

				MatchGetter.schedNewMatches.get(compId).get(k).set_1(_1);
				MatchGetter.schedNewMatches.get(compId).get(k).set_x(_x);
				MatchGetter.schedNewMatches.get(compId).get(k).set_2(_2);
				MatchGetter.schedNewMatches.get(compId).get(k).set_o(_o);
				MatchGetter.schedNewMatches.get(compId).get(k).set_u(_u);

			}// if compId >=0
		}// for elements
	}

	public int teamCombinationScorerBari(String t1, String t2, int compId)
			throws IOException {

		int d1 = 0, d2 = 0, td = 100, k = -1;
		int mind1 = 100, mind2 = 100;

		for (int i = 0; i < MatchGetter.schedNewMatches.get(compId).size(); i++) {
			logger.info(" {} - {}", MatchGetter.schedNewMatches.get(compId).get(i).getT1(), MatchGetter.schedNewMatches.get(compId)					.get(i).getT2());

			logger.info(" {} - {}", t1, t2);
			d1 = StringSimilarity.levenshteinDistance(MatchGetter.schedNewMatches.get(compId).get(i).getT1(), t1);
			d2 = StringSimilarity.levenshteinDistance(MatchGetter.schedNewMatches.get(compId).get(i).getT2(), t2);

			if (d1 + d2 < td) {
				k = i;
				td = d1 + d2;
				mind1 = d1;
				mind2 = d2;
			}
		}
		if (mind1 <= StandartResponses.TEAM_DIST) {
			if (mind2 <= StandartResponses.TEAM_DIST) {
				return k;
			} else {
				fh.apendBariToScorer(t2, MatchGetter.schedNewMatches
						.get(compId).get(k).getT2());
				return k;
			}
		} else if (mind2 <= StandartResponses.TEAM_DIST) {
			// t1 is already proved bigger than team_dist
			fh.apendBariToScorer(t1, MatchGetter.schedNewMatches.get(compId).get(k).getT1());
			return k;
		} else {
			return -1;
		}
	}

	private int getCompId(String ss) {
		/*
		 * search the country competition ccas and get the competition id not
		 * the index id. If already found before it should be stored in the MAP
		 * tempBariToCompId for a quick search.
		 */

		if (country == null) {
			return -1;
		}
		Integer compIdx = tempBariToCompId.get(ss);
		if (compIdx != null) {
			return compIdx;
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
			return compIdx;// -1
		}

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
			country = ss[0].split(" ")[0];
			compName = ss[1];
			level = -1;

		} else {
			ss = s.split(" ");
			StringBuilder sb = new StringBuilder();

			for (int i = 0; i < ss.length; i++) {
				logger.info("'{}'   length {}", ss[i],ss[i].length());
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

	// -----------------------
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
		} catch (IOException e) {
			// TODO Auto-generated catch block
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
