package scrap;

import java.io.File;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OddsNStatsMatchOdd {

	public static final Logger logger = LoggerFactory
			.getLogger(OddsNStatsMatchOdd.class);

	private double over = 1;
	private double under = 1;
	private String matchOddUrl;

	private String errorStatus = "OK";

	public OddsNStatsMatchOdd(String matchOddUrl) {
		super();
		this.matchOddUrl = matchOddUrl;
	}

	public void pageGraber() {
		if (matchOddUrl == null) {
			errorStatus = "Null URL";
			return;
		}
		Document doc = null;
		try {
//			logger.info("getting page : {}", matchOddUrl);
//			doc = Jsoup.parse(new File(
//					"C:/Users/Administrator/Desktop/odds_0_1.html"), "UTF-8");
			 doc = Jsoup
			 .connect(matchOddUrl)
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

		Elements trs = doc.getElementsByClass("odds-holder").get(1)
				.select("table tbody tr");

		int j = 0, k = 0;
		for (int i = 0; i < trs.size(); i++) {
			if (trs.get(i).getElementsByTag("td").get(2)
					.attr("sorttable_customkey") != "") {
				try {
					over += Double.parseDouble(trs.get(i)
							.getElementsByTag("td").get(2)
							.attr("sorttable_customkey"));
					j++;
				} catch (NumberFormatException e) {
					errorStatus = "A non parsable convertion ocoured";
					e.printStackTrace();
				}
			}
			if (trs.get(i).getElementsByTag("td").get(6)
					.attr("sorttable_customkey") != "") {
				try {
					under += Double.parseDouble(trs.get(i)
							.getElementsByTag("td").get(6)
							.attr("sorttable_customkey"));
					k++;
				} catch (NumberFormatException e) {
					errorStatus = "A non parsable convertion ocoured";
					e.printStackTrace();
				}
			}
		}
		if (j > 0) {
			over--;
			over /= j;
		}
		if (k > 0) {
			under--;
			under /= k;
		}
	}

	public double getOver() {
		return over;
	}

	public double getUnder() {
		return under;
	}

	public String getMatchOddUrl() {
		return matchOddUrl;
	}

	public String getErrorStatus() {
		return errorStatus;
	}

	public void setOver(double over) {
		this.over = over;
	}

	public void setUnder(double under) {
		this.under = under;
	}

	public void setMatchOddUrl(String matchOddUrl) {
		this.matchOddUrl = matchOddUrl;
	}

	public void setErrorStatus(String errorStatus) {
		this.errorStatus = errorStatus;
	}

}
