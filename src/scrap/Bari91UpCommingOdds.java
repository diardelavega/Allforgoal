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
public class Bari91UpCommingOdds {

	public static final Logger logger = LoggerFactory
			.getLogger(Bari91UpCommingOdds.class);
	// http://www.bari91.com/previews/2016-03-30
	public static final String bariUrl = "http://www.bari91.com/previews/";
	public static Map<String, String> bts = new HashMap<String, String>();
	public static Map<String, String> btp = new HashMap<>();// bari to punter
	public static List<MatchObj> remainings = new ArrayList<>();

	private String errorStatus = "OK"; // a simple way to report problems
	private Map<String, Integer> tempBariToCompId = new HashMap<>();
	private List<String> rejects = new ArrayList<>();// comps we don;t need
														// today
	private String country;
	private String compName;
	private int level;

	private boolean remainingFlag = false;

	private Gson gson = new Gson();
	private CountryCompetition cc = new CountryCompetition();
	private FileHandler fh = new FileHandler();

	public void initBari91UpCommingOdds() {

		try {
			// automatically fills the map
			fh.readBariToScorer();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// TODO int bari to punter terms
		fh.readBariToPunter();

		// int bari remaining matches
		fh.readBariRemaining();
	}

	public void scrapBariPage(LocalDate ld) throws IOException {
		String url = bariUrl + ld.toString();
		Document doc = null;
		try {
			logger.info("getting page : {}", url);
			doc = Jsoup.parse(new File(
					"C:/Users/Administrator/Desktop/bari91_27.html"), "UTF-8");

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
		// storeSched(doc);
		oddAdder(doc, ld);
	}

	private void oddAdder(Document doc, LocalDate ld) throws IOException {

		// first search if the remaining matches can be combined with
		// scorer matches
		for (MatchObj m : remainings) {
			if (Period.between(m.getDat().toLocalDate(), ld).getDays() > 1) {
				remainings.remove(m);
			} else {
				if (MatchGetter.schedNewMatches.get(m.getComId()) != null) {
					int kk = teamCombinationScorerBari(m.getT1(), m.getT2(),
							m.getComId());
					if (kk == StandartResponses.DEFAULT_NULL)
						continue;
					if (kk == StandartResponses.ALL_MATCHES_TAKEN) {
						// store remaining matches for next odds add loop
						remainings.add(m);
						continue;
					}
					MatchGetter.schedNewMatches.get(m.getComId()).get(kk)
							.set_1(m.get_1());
					MatchGetter.schedNewMatches.get(m.getComId()).get(kk)
							.set_2(m.get_2());
					MatchGetter.schedNewMatches.get(m.getComId()).get(kk)
							.set_x(m.get_x());
					MatchGetter.schedNewMatches.get(m.getComId()).get(kk)
							.set_o(m.get_o());
					MatchGetter.schedNewMatches.get(m.getComId()).get(kk)
							.set_u(m.get_u());
					MatchGetter.schedNewMatches.get(m.getComId()).get(kk)
							.setDat(Date.valueOf(ld));
				}// if compId;
					//
			}
		}

		Elements trs = doc.getElementsByClass("vrsta");
		for (Element tr : trs) {
			String cc = tr.getElementsByTag("td").get(1).text();
			if (skipComp(cc)) {
				continue;
			}

			countryCompLevel(cc);// get country, comp, level
			// logger.info("{} {} {}", country, compName, level);

			// TODO place here a country & level combination for bari91 country
			// names

			int compId = getCompId(cc);
			// if(comp)
			if (compId >= 0) {// valid comp id; search Scprer matches & add odds
				if (MatchGetter.schedNewMatches.get(compId) == null) {
					// there is no such competition matches @ scorer
					// TODO store remaining matches for next odds add loop**??
					continue;
				}

				String[] teams = tr.getElementsByTag("td").get(2).text()
						.replaceFirst("\u00A0", "").split(" - ");

				int k = teamCombinationScorerBari(teams[0], teams[1], compId);
				if (k == StandartResponses.DEFAULT_NULL)
					continue;
				if (k == StandartResponses.ALL_MATCHES_TAKEN) {
					// store remaining matches for next odds add loop
					remainingFlag = true;
				}

				// logger.info("{} {} {}", country, compName, level);
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

				if (remainingFlag) {
					MatchObj mobj = new MatchObj(-1, compId, teams[0],
							teams[1], 0, 0, 0, 0, _1, _2, _x, _o, _u,
							Date.valueOf(ld));
					if (!remainings.contains(mobj))
						remainings.add(mobj);
					remainingFlag = false;
				} else {

					MatchGetter.schedNewMatches.get(compId).get(k).set_1(_1);
					MatchGetter.schedNewMatches.get(compId).get(k).set_x(_x);
					MatchGetter.schedNewMatches.get(compId).get(k).set_2(_2);
					MatchGetter.schedNewMatches.get(compId).get(k).set_o(_o);
					MatchGetter.schedNewMatches.get(compId).get(k).set_u(_u);
				}
			}// if compId >=0
		}// for elements

//		for (String s : rejects) {
//			logger.info("RRRRRRRRRRRRRRR{}", s);
//		}

		fh.writeBariRemaining(remainings);
		for (MatchObj m : remainings) {
			logger.info("remainings Size ----{}", remainings.size());
			m.printMatch();
		}
	}

	public int teamCombinationScorerBari(String t1, String t2, int compId)
			throws IOException {
		/*
		 * for the match we found in bari we search the analog match (team1 &
		 * team2) from the list of scorer matches with the same compId
		 */

		int d1 = 0, d2 = 0, td = 1000, k = -1;
		int mind1 = 1000, mind2 = 1000;

		if (bts.get(t1) != null) {
			t1 = bts.get(t1);
		}
		if (bts.get(t2) != null) {
			t2 = bts.get(t2);
		}

		for (int i = 0; i < MatchGetter.schedNewMatches.get(compId).size(); i++) {
			// dismis previously combined matches -1 the taken ones
			if (MatchGetter.schedNewMatches.get(compId).get(i).getFt1() != -1) {

				// logger.info(" {} - {}",
				// MatchGetter.schedNewMatches.get(compId)
				// .get(i).getT1(), MatchGetter.schedNewMatches
				// .get(compId).get(i).getT2());
				// logger.info(" {} - {}", t1, t2);

				d1 = StringSimilarity.levenshteinDistance(
						MatchGetter.schedNewMatches.get(compId).get(i).getT1(),
						t1);
				d2 = StringSimilarity.levenshteinDistance(
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
			 * compId will be valid but whet it tries to combine the teams it
			 * will find none aviable.
			 */
			return StandartResponses.ALL_MATCHES_TAKEN;
		}

		if (k == -1) {
			// test debug val
			logger.info("UUUUUUU");
		}

		if (mind1 <= StandartResponses.TEAM_DIST) {
			if (mind2 <= StandartResponses.TEAM_DIST) {
				MatchGetter.schedNewMatches.get(compId).get(k).setFt1(-1);
				return k;
			} else {
				fh.apendBariToScorer(t2, MatchGetter.schedNewMatches
						.get(compId).get(k).getT2());
				MatchGetter.schedNewMatches.get(compId).get(k).setFt1(-1);
				return k;
			}
		} else if (mind2 <= StandartResponses.TEAM_DIST) {
			// t1 is already proved bigger than team_dist
			fh.apendBariToScorer(t1, MatchGetter.schedNewMatches.get(compId)
					.get(k).getT1());
			MatchGetter.schedNewMatches.get(compId).get(k).setFt1(-1);
			return k;
		} else {
			BariToScorerTuple btst = new BariToScorerTuple(t1,
					MatchGetter.schedNewMatches.get(compId).get(k).getT1());
			rejects.add(gson.toJson(btst) + " " + country + "_" + compName
					+ "_" + level);
			// logger.info("{}",gson.toJson(btst));
			btst = new BariToScorerTuple(t2, MatchGetter.schedNewMatches
					.get(compId).get(k).getT2());
			rejects.add(gson.toJson(btst) + " " + country + "_" + compName
					+ "_" + level);
			// logger.info("{}",gson.toJson(btst));
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
			// TODO show teams that didint make it
			logger.info("NO CUT COUNTRIES & COMPETITIONS  {}", ss);
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
				// logger.info("'{}'   length {}", ss[i], ss[i].length());
				if (ss[i].length() <= 1) {
					try {
						level = Integer.parseInt(ss[i]);
						compName = null;
						country = sb.toString();
						if (btp.get(country) != null) {
							country = btp.get(country);
						}
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
			if (btp.get(country) != null) {
				country = btp.get(country);
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
