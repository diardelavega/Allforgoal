package scrap;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dbtry.Conn;
import diskStore.AnalyticFileHandler;
import diskStore.FileHandler;
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
//	AnalyticFileHandler afh;
	private String baseUrl ="http://www.soccerpunter.com";

	// ------------------MODULATE FUNCTIONS

	private Elements docConnectAndScrap(String url) {
		/* get page, get elements */
		Document doc = null;
		try {
			logger.info(baseUrl+url + "/results");
			doc = Jsoup
					.connect(baseUrl+url + "/results")
					// doc = Jsoup.parse(new File(
					// "C:/Users/Administrator/Desktop/Albania.html"), "UTF-8");
					.userAgent(
							"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0")
					.maxBodySize(0).timeout(600000).get();
		} catch (Exception e) {
			e.printStackTrace();
			errorStatus = "Faulty Connection";
			logger.warn("---------:Connection not possible  {}", errorStatus);
			return null;
		}

		Element resultTable = doc.getElementsByClass("roundsResultDisplay")
				.get(0);
		doc = null;// free scpace
		System.gc();// free scpace
		if (resultTable == null) {
			logger.warn("---------:Element not found");
			errorStatus = "Unfound Element";
			return null;
		} else {
			Elements trs = resultTable.getElementsByTag("tr");
			return trs;
		}

	}

	// -------------------------

	public int remainingResultsGraber(String url, int compId,
			LocalDate supplyDate, boolean partial) throws IOException {
		/*
		 * go to the page of the url; get matches list; get only matches with
		 * date greater than supplied date. this function is to be use in two
		 * ways. by getting all the matches dta up untill now AND also by taking
		 * a portion of matches(from a specific date up until now). The boolean
		 * partial variable specifies whether or not will be the second case. if
		 * it is the second case a valid date must also be provided to complete
		 * the required research factors
		 */

		Elements trs = docConnectAndScrap(url);// resultTable.getElementsByTag("tr");
		if (trs == null) {
			return -1;
		}
		// afh = new AnalyticFileHandler();// append matches to a json file
		// afh.opendataWrite();
		for (Element tr : trs) {
			if (tr.hasClass("even") || tr.hasClass("odd")) {
				Element scoretd = tr.children().get(3).getElementsByTag("div")
						.first();

				if (scoretd.hasClass("scoreW") || scoretd.hasClass("scoreL")
						|| scoretd.hasClass("scoreD")) {
					Elements tds = tr.getElementsByTag("td");
					tds.get(2).select("span").remove();
					tds.get(4).select("span").remove();

					if (partial) {// in case of partial match grub
						LocalDate matchDat = LocalDate.parse(tds.get(1).text(),
								DateTimeFormatter.ofPattern("dd/MM/yyyy"));
						if (matchDat.isEqual(supplyDate)) {
							// we already have the matches earlier than
							// supplied date
//							afh.closeOutput();
							return 0;
						}
					}

					adapto(tds.get(0).text(), tds.get(1).text(), /* tm1 */
							tds.get(2).text(), tds.get(3).text(), /* tm2 */
							tds.get(4).text(), tds.get(5).text(), tds.get(8)
									.getElementsByTag("a").attr("href"), compId);
				}
			}
		} // for
			// afh.closeOutput();

		return 0;
	}

	public int competitionResultsGrabbers(String url, int compId)
			throws IOException {
		/*
		 * go to the page with the results table and gather the match data we
		 * want report error in case something goes wrong
		 */
		Document doc = null;
		try {
			logger.info(baseUrl+url + "/results");
			doc = Jsoup
					.connect(baseUrl+url + "/results")
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
			// set file ready to be writen
//			afh = new AnalyticFileHandler();
//			afh.opendataWrite();

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

						adapto(tds.get(0).text(), tds.get(1).text(), /* tm1 */
								tds.get(2).text(), tds.get(3).text(), /* tm2 */
								tds.get(4).text(), tds.get(5).text(),
								tds.get(8).getElementsByTag("a").attr("href"),
								compId);
					}
				}
			} // for
//			afh.closeOutput();
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

		MatchObj match = new MatchObj();
		String[] temp = ft.split(" - ");
		try {// look out for posponed matches without int results
			match.setFt1(Integer.parseInt(temp[0]));
			match.setFt2(Integer.parseInt(temp[1]));
		} catch (Exception e) {
			logger.warn("POSTMONED MATCH {} vs {}  @ dat={}", t1, t2, date);
			return;
		}

		t1 = nc.convertNonAscii(t1.replaceFirst("\u00A0", ""));
		t2 = nc.convertNonAscii(t2.replaceFirst("\u00A0", ""));

		match.setT1(t1);
		match.setT2(t2);
		match.setComId(compId);
		match.setDat(ts(date));
		if (!ht.equals("")) {
			// not all the matches have ht result records
			temp = ht.split("-");
			match.setHt1(Integer.parseInt(temp[0]));
			match.setHt2(Integer.parseInt(temp[1]));
		} else {
			match.setHt1(-1);
			match.setHt2(-1);
		}
		if (!oddUrl.equals("")) {
			String matchId = oddUrl.split("_id=|&home")[1];
			AjaxGrabber ag = new AjaxGrabber();
			ag.f47(matchId);
			ag.f69(matchId);
			errorStatus = ag.errorStatus;
			logger.warn(errorStatus);
			if (errorStatus == "OK") {
				match.set_1(ag.get_1());
				match.set_2(ag.get_2());
				match.set_x(ag.get_x());
				if (ag.getOver() <= 0) {
					match.set_o(ag.getOver());
				}
				match.set_u(ag.getUnder());
			}
		}
		// match.printMatch();

		// append to file;
		// afh.appendMatchData(match);
		// put the matches to the appropriate list structure
		if (MatchesList.readMatches.get(compId) == null) {
			MatchesList.readMatches.put(compId, new ArrayList<>());
			MatchesList.readMatches.get(compId).add(match);
		} else {
			MatchesList.readMatches.get(compId).add(match);
		}
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

	public LocalDate getLatestMatchesDate(int compId) throws SQLException {
		Conn conn = new Conn();
		conn.open();
		ResultSet rs = conn
				.getConn()
				.createStatement()
				.executeQuery(
						"SELECT dat FROM matches WHERE compid = " + compId
								+ " GROUP BY dat ORDER BY dat desc LIMIT 1;");
//		String date = null;
		LocalDate ld = null;
		while (rs.next()) {
			ld = LocalDate.parse(rs.getString(1));
		}
		rs.close();
		conn.close();
		return ld;
	}
}
