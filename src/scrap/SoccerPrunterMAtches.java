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

	public void matchGraber() {
		String matchResultUrl;
		for (int i = 0; i < CountryCompetition.compLinkList.size(); i++) {
			matchResultUrl = CountryCompetition.compLinkList.get(i)
					+ "/results";
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
		Document doc = Jsoup.connect(url).get();
		// get table with class= competitionRanking roundsResultDisplay
		Element resultTable = doc.getElementsByClass(
				"competitionRanking roundsResultDisplay").get(0);
		Elements trs = resultTable.getElementsByTag("tr");
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
			String t2, String ht, String oddUrl) {
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
		headAvgodds(oddUrl);
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

	public void headAvgodds(String url) {
		String matchId = url.split("_id=|&home")[1];
		String oddlink = "http://www.soccerpunter.com/livesoccerodds_ajx.php?match_id="
				+ matchId + "&typeId=69";
		
	}

}
