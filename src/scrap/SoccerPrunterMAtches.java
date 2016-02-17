package scrap;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import extra.NameCleaner;
import basicStruct.MatchObj;
import structures.CountryCompetition;
import structures.MatchesList;

/**
 * @author Administrator
 *
 *         Will use the links from the compidlink structure to go to each page
 *         and get the matches that where played until now. Alongside with the
 *         matches it will store the link for the odds of each match. ********
 *         1) for each competition link go get results. **********************
 *         2)for every developed match get odds link *************************
 *         3) transform link appropriately and get results for 1x2 & o/u *****
 *         i) 69 -> for 1x2 and 49 for o/u
 *         "http://www.soccerpunter.com/livesoccerodds_ajx.php?match_id=2120502
 *         &typeId=69"
 */
public class SoccerPrunterMAtches {

	// CountryCompetition.readCompIdLink
	private static final Logger logger = LoggerFactory
			.getLogger(SoccerPrunterMAtches.class);

	private String errorStatus = "OK"; // a simple way to report problems
	private List<MatchObj> matchlist = new ArrayList<>();
	private NameCleaner nc = new NameCleaner();

	public void matchGraber() {
		// for every competition link we have go get all results until now
		String matchResultUrl;
		for (int i = 0; i < CountryCompetition.compLinkList.size(); i++) {
			matchResultUrl = CountryCompetition.compLinkList.get(i)
					.getCompLink() + "/results";

			logger.info("link : {}", matchResultUrl);

			// get page
			// get matches with results
			// for every match
			// // // get odds link & match id
			// http://www.soccerpunter.com/livesoccerodds_ajx.php?match_id=2120515&typeId=69
			// (change match id and seq to get json results)
			// traverse json and get data from different bookies providers
			// return.
			// fill a list with matches

		}
	}

	public int competitionResultsGrabbers(String url, int compId)
			throws IOException {
		/*
		 * go to the page with the results table and gather the match data we
		 * want report error in case something goes wrong
		 */
		Document doc = null;
		try {
			logger.info(url + "/results");
			doc = Jsoup
					.connect(url + "/results")
					// doc = Jsoup.parse(new File(
					// "C:/Users/Administrator/Desktop/Albania.html"), "UTF-8");
					.userAgent(
							"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0")
					.maxBodySize(0).timeout(600000).get();
		} catch (Exception e) {
			e.printStackTrace();
			errorStatus = "Faulty Connection";
			logger.warn("---------:Connection not possible  {}", errorStatus);
			return -1;
		}

		Element resultTable = doc.getElementsByClass("roundsResultDisplay")
				.get(0);
		doc = null;
		if (resultTable == null) {
			logger.warn("---------:Element not found");
			errorStatus = "Unfound Element";
		} else {

			Elements trs = resultTable.getElementsByTag("tr");
			for (Element tr : trs) {
				if (tr.hasClass("even") || tr.hasClass("odd")) {
					Element scoretd = tr.children().get(3)
							.getElementsByTag("div").first();

					// logger.info("********** {}", scoretd);
					if (scoretd.hasClass("scoreW")
							|| scoretd.hasClass("scoreL")
							|| scoretd.hasClass("scoreD")) {
						Elements tds = tr.getElementsByTag("td");
						tds.get(2).select("span").remove();
						tds.get(4).select("span").remove();
						// String tm1=tds..text();
						// String tm2=tds.get(4).select("span").remove().text();

//						logger.info(tds.get(8).select("href").toString());
//						logger.info(tds.get(8).attr("href"));
//						logger.info(tds.get(8).getElementsByTag("a")
//								.attr("href"));
//
//						logger.info(tds.get(8).getElementsByAttribute("href")
//								.toString());

						adapto(tds.get(0).text(), tds.get(1).text(),/* tm1 */
								tds.get(2).text(), tds.get(3).text(), /* tm2 */
								tds.get(4).text(), tds.get(5).text(), tds
										.get(8).getElementsByTag("a").attr("href"), compId);
					}
				}
			}// for
		}
		logger.info("STATUS is {}", errorStatus);
		return 0;

	}

	private void adapto(String week, String date, String t1, String ft,
			String t2, String ht, String oddUrl, int compId) throws IOException {
		/*
		 * get all the data including the odds which should come from another 2
		 * url calls
		 */

		t1 = nc.convertNonAscii(t1.replaceFirst("\u00A0", ""));
		t2 = nc.convertNonAscii(t2.replaceFirst("\u00A0", ""));

		MatchObj match = new MatchObj();
		match.setT1(t1);
		match.setT2(t2);
		match.setComId(compId);
		match.setDat(ts(date));
		String[] temp = ft.split(" - ");
		match.setFt1(Integer.parseInt(temp[0]));
		match.setFt2(Integer.parseInt(temp[1]));
		if (!ht.equals("")) {
			// not all the matches have ht result records
			temp = ht.split("-");
			match.setHt1(Integer.parseInt(temp[0]));
			match.setHt2(Integer.parseInt(temp[1]));
		}
		if (!oddUrl.equals("")) {
			String matchId = oddUrl.split("_id=|&home")[1];
			AjaxGrabber ag = new AjaxGrabber();
			ag.f47(matchId);
			ag.f69(matchId);
			errorStatus = ag.errorStatus;
			if (errorStatus == "OK") {
				match.set_1(ag.get_1());
				match.set_2(ag.get_2());
				match.set_x(ag.get_x());
				match.set_o(ag.getOver());
				match.set_u(ag.getUnder());
			}
		}
		match.printMatch();
		// put the matches to the appropriate list structure
		if (MatchesList.readMatches.get(compId) == null) {
			MatchesList.readMatches.put(compId, new ArrayList<>());
			MatchesList.readMatches.get(compId).add(match);
		} else {
			MatchesList.readMatches.get(compId).add(match);
		}
		// logger.info("MAtches map  {}", MatchesList.readMatches.get(compId)
		// .size());

	}

	public Date ts(String date) {
		SimpleDateFormat datetimeFormatter1 = new SimpleDateFormat("dd/MM/yyyy");
		try {
			return new Date(datetimeFormatter1.parse(date).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	public String getErrorStatus() {
		return errorStatus;
	}

	public List<MatchObj> getMatchlist() {
		return matchlist;
	}

	public void setErrorStatus(String errorStatus) {
		this.errorStatus = errorStatus;
	}

	public void setMatchlist(List<MatchObj> matchlist) {
		this.matchlist = matchlist;
	}

}
