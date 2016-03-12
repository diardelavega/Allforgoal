package test;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import basicStruct.MatchObj;
import extra.Status;

public class MatchGetter {
	public static Logger logger = LoggerFactory.getLogger(MatchGetter.class);

	public static List<MatchObj> schedNewMatches = new ArrayList<>();
	public static List<MatchObj> finNewMatches = new ArrayList<>();
	public static List<MatchObj> errorNewMatches = new ArrayList<>();
	private final String mainUrl = "http://www.xscores.com/soccer/all-games/";

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
						|| clasVal[0].contains("AFRICA")|| clasVal[0].contains("AMERICA")|| clasVal[0].contains("EUROPE")|| clasVal[0].contains("ASIA")|| clasVal[4].contains("CUP") || clasVal[4].contains("KUPA")
						|| clasVal[4].contains("COPPA")|| clasVal[4].contains("COPA")|| clasVal[4].contains("OFF")
						|| clasVal[4].contains("TROPHY")|| clasVal[4].contains("COUPE") || clasVal[4].contains("EURO")) {
					continue;
				}

//				int compIdx = searchForCompIdx(clasVal[0], clasVal[4]);
//				if (compIdx < 0) {
//					continue;
//				} else
				{
					int compIdx = 10101;
					Elements tds = row.getElementsByTag("td");
					String status = tds.get(1).text();
					String t1 = tds.get(5).text();
					String t2 = tds.get(9).text();
					mobj = new MatchObj();
					mobj.setT1(t1);
					mobj.setT2(t2);
					logger.info("t1-{}  t2-{}", t1, t2);

					if (_status.equals(Status.SCHEDULED)
							&& (status.equals(Status.SCHEDULED) || status .equals(Status.FTR))) {
						mobj.setDat(Date.valueOf(dat));
						mobj.setComId(compIdx + 1);
						schedNewMatches.add(mobj);
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

	public void clearLists(){
		schedNewMatches.clear();
		finNewMatches.clear();
		errorNewMatches.clear();
	}
	
}
