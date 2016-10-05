package scrap;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import diskStore.FileHandler;
import extra.NameCleaner;
import extra.StandartResponses;
import extra.StringSimilarity;
import structures.CountryCompetition;
import test.MatchGetter;

public class SoccerPunterOdds {

	public static final Logger logger = LoggerFactory
			.getLogger(SoccerPunterOdds.class);
	public static Map<String, String> punterToScorerTeams = new HashMap<String, String>();

	private String errorStatus = "OK";
	private CountryCompetition cc = new CountryCompetition();
	private NameCleaner nc = new NameCleaner();
	private FileHandler fh = new FileHandler();

	// http://www.soccerpunter.com/soccer-statistics/matches_today?year=2016&month=04&day=07
	private final String baseUrl = "http://www.soccerpunter.com/soccer-statistics/matches_today";

	public void getDailyOdds(LocalDate ld) {
		String url = baseUrl + dateAdaptor(ld);
		Document doc = null;
		try {
			logger.info("getting page : {}", url);
			// doc = Jsoup.parse(new File(
			// "C:/Users/Administrator/Desktop/scorerOdd_3.htm"), "UTF-8");

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
		Elements trs = doc.getElementsByClass("competitionRanking").first()
				.select("tbody tr");
		if (trs.size() <= 1) {
			errorStatus = "Empty List";
			logger.warn("DIdnt get anything, Empty list");
			return;
		}

		// String country;
		// String competition;
		int compId = -1;
		String[] coucomp = null;
		for (int i = 0; i < trs.size(); i++) {
			if (trs.get(i).hasClass("seasonRow")) {
				coucomp = trs.get(i).getElementsByTag("td").first()
						.getElementsByTag("a").get(1).text().split(" - ");
				// logger.info("'{}' - '{}'", coucomp[0], coucomp[1]);
				coucomp[0] = nc.convertNonAscii(coucomp[0]);
				coucomp[0].replaceFirst("[N] ", "");
				coucomp[1] = nc.convertNonAscii(coucomp[1]);

				if (skipComp(coucomp[1]))
					continue;
				compId = getCompId(coucomp[0], coucomp[1]);

				if (existeceCheck(compId)) {
					continue;
				}
			} else {
				if (existeceCheck(compId)) {
					continue;
				}

				// TODO continue with existing competition data
				String t1 = nc.convertNonAscii(trs.get(i)
						.getElementsByTag("td").get(0).getElementsByTag("a")
						.first().text());
				String t2 = nc.convertNonAscii(trs.get(i)
						.getElementsByTag("td").get(2).getElementsByTag("a")
						.first().text());

				// TODO check if match is oddFree
				int k = teamCombinationScorerOdder(t1, t2, compId);
				// @ map with compId, @ the idx k of the list the match is it.
				if (k < 0) {
					continue;
				}

				String _1 = trs.get(i).getElementsByTag("td").get(3).text();
				String _X = trs.get(i).getElementsByTag("td").get(4).text();
				String _2 = trs.get(i).getElementsByTag("td").get(5).text();

				Elements a = trs.get(i).getElementsByTag("td").get(8)
						.getElementsByTag("a");
				String oddlink = a.attr("href");
				if (oddlink.length() < 3) {
					MatchGetter.schedNewMatches.get(compId).get(k).setFt1(0);
					continue;
				} else {
					oddlink = a.attr("href");

					String mid = a.attr("href").split("_id=|&home")[1];
					AjaxGrabber ag = new AjaxGrabber();
					if (_1.equals(" ") || _X.equals(" ") || _2.equals(" ")) {
						try {
							ag.f69(mid);
						} catch (IOException e) {
							e.printStackTrace();
						}

						if (ag.get_1() >= 1) {
							MatchGetter.schedNewMatches.get(compId).get(k)
									.set_1(ag.get_1());
						}
						if (ag.get_2() >= 1) {
							MatchGetter.schedNewMatches.get(compId).get(k)
									.set_2(ag.get_2());
						}
						if (ag.get_x() >= 1) {
							MatchGetter.schedNewMatches.get(compId).get(k)
									.set_x(ag.get_x());
						}
					} else {
						try {
							MatchGetter.schedNewMatches.get(compId).get(k)
									.set_1(Float.parseFloat(_1));
						} catch (Exception e) {
						}
						try {
							MatchGetter.schedNewMatches.get(compId).get(k)
									.set_x(Float.parseFloat(_X));
						} catch (Exception e) {
						}
						try {
							MatchGetter.schedNewMatches.get(compId).get(k)
									.set_2(Float.parseFloat(_2));
						} catch (Exception e) {
						}
					}
					try {
						ag.f47(mid);
					} catch (IOException e) {
						e.printStackTrace();
					}
					if (ag.getOver() >= 1) {
						MatchGetter.schedNewMatches.get(compId).get(k)
								.set_o(ag.getOver());
					}
					if (ag.getUnder() >= 1) {
						MatchGetter.schedNewMatches.get(compId).get(k)
								.set_u(ag.getUnder());
					}

				}
				logger.info("'{}' '{}' -{},{},{}- --- {}", t1, t2, _1, _X, _2,
						oddlink);
			}
		}

	}

	private boolean existeceCheck(int compId) {
		if (compId < 0)
			return true;
		if (MatchGetter.schedNewMatches.get(compId) == null) {
			return true;
		}
		if (MatchGetter.reviewedAndEmptyOdds.contains(compId))
			return true;
		return false;
	}

	private int teamCombinationScorerOdder(String t1, String t2, int compId) {
		/*
		 * for the match we found in bari we search the analog match (team1 &
		 * team2) from the list of scorer matches with the same compId
		 */

		float d1 = 0, d2 = 0, td = 1000;
		float mind1 = 1000, mind2 = 1000;
		int k = -1;
		// Loop through the scheduled matches of competition
		// TODO change it Xscorer vs MatchGetter
		boolean finishReviewingFlag = true;
		for (int i = 0; i < MatchGetter.schedNewMatches.get(compId).size(); i++) {
			if (MatchGetter.schedNewMatches.get(compId).get(i).getFt1() != -1) {
				finishReviewingFlag = false;
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
		if (finishReviewingFlag) {
			// if it has no pending matches
			MatchGetter.reviewedAndEmptyOdds.add(compId);
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
				// if t2 could not be adequately corelated, use the corelation
				// of the first team as a secure binder and add the second(the
				// uncorelated team) in the file so that it will be found next
				// time
				punterToScorerTeams.put(t2,
						MatchGetter.schedNewMatches.get(compId).get(k).getT2());
				fh.appendPunterToScorerTeams(t2, MatchGetter.schedNewMatches
						.get(compId).get(k).getT2());
				// fh.appendOdderToScorerTeams(t2, MatchGetter.schedNewMatches
				// .get(compId).get(k).getT2());
				MatchGetter.schedNewMatches.get(compId).get(k).setFt1(-1);
				return k;
			}
		} else if (mind2 <= StandartResponses.TEAM_DIST) {
			// t1 is already proved bigger than team_dist
			punterToScorerTeams.put(t1, MatchGetter.schedNewMatches.get(compId)
					.get(k).getT1());
			fh.appendPunterToScorerTeams(t1,
					MatchGetter.schedNewMatches.get(compId).get(k).getT1());
			// fh.appendOdderToScorerTeams(t1,
			// MatchGetter.schedNewMatches.get(compId).get(k).getT1());
			MatchGetter.schedNewMatches.get(compId).get(k).setFt1(-1);
			return k;
		} else {
			return -1;
		}

	}

	private int getCompId(String country, String competition) {
		int id = cc.searchCompBinary(country, competition, false);
		if (id < 0) {
			return -1;
		}
		return cc.ccasList.get(id).getCompId();
	}

	private String dateAdaptor(LocalDate ld) {
		StringBuilder sb = new StringBuilder();
		sb.append("?year=");
		sb.append(ld.getYear());
		sb.append("&month=");
		sb.append(ld.getMonthValue());
		sb.append("&day=");
		sb.append(ld.getDayOfMonth());
		return sb.toString();

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
