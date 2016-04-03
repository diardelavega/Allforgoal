package scrap;

import java.io.File;
import java.time.LocalDate;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.spi.LoggerFactoryBinder;

import extra.NameCleaner;
import structures.CountryCompetition;

public class SoccerPunterOdds {

	public static final Logger logger = LoggerFactory
			.getLogger(SoccerPunterOdds.class);

	private String errorStatus = "OK";
	private CountryCompetition cc = new CountryCompetition();
	private NameCleaner nc = new NameCleaner();
	
	// http://www.soccerpunter.com/soccer-statistics/matches_today?year=2016&month=04&day=07
	private final String baseUrl = "http://www.soccerpunter.com/soccer-statistics/matches_today";

	public void getDailyOdds(LocalDate ld) {
		String url = baseUrl + dateAdaptor(ld);
		Document doc = null;
		try {
			logger.info("getting page : {}", url);
			 doc = Jsoup.parse(new File(
			 "C:/Users/Administrator/Desktop/scorerOdd_3.htm"),
			 "UTF-8");

//			doc = Jsoup
//					.connect(url)
//					.userAgent(
//							"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0")
//					.maxBodySize(0).timeout(600000).get();
			logger.info("Page aquired");
		} catch (Exception e) {
			e.printStackTrace();
			errorStatus = "Faulty Connection";
			logger.warn("---------:Connection not possible  {}", errorStatus);
			return;
		}
		Elements trs = doc.getElementsByClass("competitionRanking").first().select("tbody tr");
		if (trs.size() <= 1) {
			errorStatus = "Empty List";
			logger.warn("DIdnt get anything, Empty list");
			return;
		}

		String country;
		String competition;
		int compId;
		String[] coucomp ;
		for (int i = 0; i < trs.size(); i++) {
			if (trs.get(i).hasClass("seasonRow")) {
				// TODO get new competition data
//				logger.info("{}",trs.get(i).getElementsByTag("td").first()
//						.getElementsByTag("a").get(1).text());
//				logger.info("{}",trs.get(i).getElementsByTag("td").text());
				coucomp = trs.get(i).getElementsByTag("td").first()
						.getElementsByTag("a").get(1).text().split(" - ");
				logger.info("'{}' - '{}'", coucomp[0], coucomp[1]);
				coucomp[0] =nc.convertNonAscii(coucomp[0]);
				coucomp[1] =nc.convertNonAscii(coucomp[1]);
				 compId=getCompId(coucomp[0],coucomp[1]);
			} else {
				// TODO continue with existing competition data
				String t1 = trs.get(i).getElementsByTag("td").get(0).getElementsByTag("a").first().text();
				t1= nc.convertNonAscii(t1);
				String t2 = trs.get(i).getElementsByTag("td").get(2).getElementsByTag("a").first().text();
				t2= nc.convertNonAscii(t2);
				
				String _1 = trs.get(i).getElementsByTag("td").get(3).text();
//						.getElementsByTag("a").first().getElementsByTag("div")
//						.first().text();
				String _X = trs.get(i).getElementsByTag("td").get(4).text();
//						.getElementsByTag("a").first().getElementsByTag("div")
//						.first().text();
				String _2 = trs.get(i).getElementsByTag("td").get(5).text();
//						.getElementsByTag("a").first().getElementsByTag("div")
//						.first().text();
				
				
				Node a = trs.get(i).getElementsByTag("td").get(8).childNode(0);
				String link = null;
				if( a!=null){
					link=a.attr("href");
				}
				logger.info("'{}' '{}' -{},{},{}- --- {}",t1,t2,_1,_X,_2,link);
//				link=trs.get(i).getElementsByTag("td").get(8)
//						.getElementsByTag("a").first().attr("href");
			}
		}

	}

	private int getCompId(String string, String string2) {
		// TODO Auto-generated method stub
		return 0;
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
}
