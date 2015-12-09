package scrap;

import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import basicStruct.MatchObj;
import structures.CountryCompetition;

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

	public String errorStatus = "OK";

	public void matchGraber() {
		String matchResultUrl;
		for (int i = 0; i < CountryCompetition.compLinkList.size(); i++) {
			matchResultUrl = CountryCompetition.compLinkList.get(i)
					.getCompLink() + "/results";

			
			logger.info("link : {}",matchResultUrl);
			
			// TODO links that end with 2015 are finished competitions. (avoid!)
			// get page
			// get matches with results
			// for every match
			// // // get odds link & match id
			// http://www.soccerpunter.com/livesoccerodds_ajx.php?match_id=2120515&typeId=69
			// (change match id and seq to get json results)
			// traverse json and get data from different bookies providers
			// average and return.

		}
	}

	public void competitionResultsGrabbers(String url) throws IOException {
		Document doc = null;
		try {
			doc = Jsoup.connect(url).get();
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("---------:Connection not possible");
			errorStatus = "Faulty Connection";
		}
		// get table with class= competitionRanking roundsResultDisplay
		Element resultTable = doc.getElementsByClass(
				"competitionRanking roundsResultDisplay").get(0);
		Elements trs = resultTable.getElementsByTag("tr");
		if (resultTable == null) {
			logger.warn("---------:Element not found");
			errorStatus = "Unfound Element";
		}
		for (Element tr : trs) {
			if (tr.getElementsByClass("scoreW").get(0) != null) {
				Elements tds = tr.getElementsByTag("td");
				adapt(tds.get(0).text(), tds.get(1).text(), tds.get(2).text(),
						tds.get(3).text(), tds.get(4).text(),
						tds.get(5).text(), tds.get(8).text());
			}
		}
	}

	private void adapt(String week, String date, String t1, String ft,
			String t2, String ht, String oddUrl) throws IOException {
		// TODO Auto-generated method stub
		MatchObj match = new MatchObj();
		match.setT1(t1);
		match.setT2(t2);
		match.setDat(ts(date));
		String[] temp = ft.split(" - ");
		match.setFt1(Integer.parseInt(temp[0]));
		match.setFt2(Integer.parseInt(temp[1]));
		if (!ht.equals("")) {
			// not all the matches have ht result records
			temp = ht.split(" - ");
			match.setHt1(Integer.parseInt(temp[0]));
			match.setHt2(Integer.parseInt(temp[1]));
		}
		String matchId = oddUrl.split("_id=|&home")[1];
		AjaxGrabber ag = new AjaxGrabber();
		ag.f47(matchId);
		ag.f69(matchId);
		match.set_1(ag.get_1());
		match.set_2(ag.get_2());
		match.set_x(ag.get_x());
		match.set_o(ag.getOver());
		match.set_u(ag.getUnder());
		errorStatus = ag.errorStatus;
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

}
