package scrap;

import java.io.File;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import structures.CountryCompetition;

public class OddsNStats {
	public static final Logger logger = LoggerFactory
			.getLogger(OddsNStats.class);
	// http://oddsandstats.com/odds_comparison.asp?spoid=1&modid=1&cur_dat=20160402
	public final String oddsUrl = "http://oddsandstats.com/odds_comparison.asp?spoid=1";

	private CountryCompetition cc = new CountryCompetition();
	private Map<String, Integer> tempOddsNStatsToCompId = new HashMap<>();

	private String errorStatus = "OK";
	private String country;
	private String competition;
	private int level = -1;

	public void getOddsPage(LocalDate ld) {
		String url = oddsUrl + urlFormater(ld);
		Document doc = null;
		try {
			logger.info("getting page : {}", url);
			doc = Jsoup.parse(new File(
					"C:/Users/Administrator/Desktop/odds_2.html"), "UTF-8");

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
		Element dataDiv = doc.getElementsByClass("odds-holder").first();
		Elements trs = dataDiv.select("table tbody tr");

		// storeSched(trs);
		oddAdder(trs, ld);
	}

	void oddAdder(Elements trs, LocalDate ld) {

		int compId;
		for (int i = 0; i < trs.size(); i++) {
			Element td = trs.get(i).getElementsByTag("td").get(1);
			if (!td.hasClass("slc")) {

				String title = td.getElementsByTag("a").get(0).attr("title");
				String span = td.getElementsByTag("a").get(0).getElementsByTag("span").get(0).text();
				attributeFiller(title, span);
				compId = getCompId(title);
			}
			logger.info("{},  '{}',  {}", country, competition, level);

			// TODO get teams

		}
	}

	private int getCompId(String title) {

		if (country == null) {
			return -1;
		}
		Integer id = tempOddsNStatsToCompId.get(title);
		if (id != null) {
			return id;
		}
		if (level > 1) {
			id = cc.searchCompBinaryLevel(country, level);
		} else {
			id = cc.searchCompBinary(country, competition, true);
		}
		if (id >= 0) {
			tempOddsNStatsToCompId.put(title, cc.ccasList.get(id).getCompId());
			return cc.ccasList.get(id).getCompId();
		} else {
			// show teams that didint make it
			logger.info("rejects {}", ccc());
			return id;
		}
	}

	// ------------------------HELPERS
	private void attributeFiller(String title, String span) {
		/* extract country, competition and posibly level of the matches in hand */

		String st[] = title.split(" ");
		if (inverter(st[0])) {
			String[] temp = title.substring(st[0].length() + 1, title.length())
					.split(" - ");
			competition = st[0] + " " + temp[0];
			country = temp[1];
			leveler(span);
			return;
		}
		if (splitHelper(st[0])) {
			country = st[0] + " " + st[1];
			for (int i = 2; i < st.length; i++) {
				competition += st[i];
				if (i < st.length - 1)
					competition += " ";
			}
			leveler(span);
			return;
		}

		country = st[0];
		competition = title.substring(country.length(), title.length());

	}

	private void leveler(String s) {
		s = s.replaceAll("[A-Z]", "");
		if (s.length() > 0) {
			level = Integer.parseInt(s);
		} else {
			level = -1;
		}
	}

	private boolean splitHelper(String s) {
		if (s.equals("Northen") || s.equals("Rep.") || s.equals("Czech")
				|| s.equals("Korea")) {
			return true;
		}
		return false;
	}

	private boolean inverter(String s) {
		if (s.equals("Lega") || s.equals("Primera")) {
			return true;
		}
		return false;
	}

	private String urlFormater(LocalDate ld) {
		StringBuilder sb = new StringBuilder();
		sb.append(ld.getYear());
		sb.append(ld.getMonthValue());
		sb.append(ld.getDayOfMonth());
		return "&modid=1&cur_dat=" + sb.toString();
	}

	private String ccc() {
		/* country competition combination */
		return country + "_" + competition;
	}
}
