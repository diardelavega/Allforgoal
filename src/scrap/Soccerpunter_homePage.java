package scrap;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import basicStruct.CCAllStruct;
import basicStruct.CompIdLinkSoccerPlunter;
import basicStruct.CountryCompObj;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.Commons;

import structures.CountryCompetition;
import extra.NameCleaner;

/**
 * @author Administrator
 *
 *         this class grabs data from the soccerprunter web page and initialises
 *         the countries, competitions, competition ids and competition links.
 *         It is intended to work only once in the beginning of the grabbing
 *         (ideally)
 */
public class Soccerpunter_homePage {

	private String link = "http://www.soccerpunter.com";
	private List<String> competitions = new ArrayList<>();
	private NameCleaner nc = new NameCleaner();

	public void goGetCompetitions() throws IOException {
		// got tho the home address of the site ang get the link for all the
		// competitions;

		Document doc = Jsoup.connect(link).userAgent("Mozilla").get();
		// File ssfile = new File("C:/Users/Administrator/Desktop/ss.html");
		// Document doc = Jsoup.parse(ssfile, "UTF-8");

		Element linkStructure = doc.getElementsByTag("select").get(4);
		// System.out.println(linkStructure);
		Elements opts = linkStructure.getElementsByTag("option");

		int i = 0;
		String s, txt;
		// CountryCompObj ccobj;// =new CountryCompObj();
		// CompIdLinkSoccerPlunter cid;// =new CompIdLinkSoccerPlunter ();
		CCAllStruct ccas;

		for (Element o : opts) {
			// System.out.println(o.attr("value"));
			s = o.attr("value").toString();
			txt = o.text();
			if (s.length() == 0) {
				continue;
			}
			if (s.contains("Cup") || s.contains("Coppa") || s.contains("Copa")
					|| s.contains("off") || s.contains("Trophy")
					|| s.contains("Coupe") || s.contains("World")
					|| s.contains("Africa") || s.contains("America")
					|| s.contains("Europe") || s.contains("Asia")
					|| s.endsWith("2015")) {
				continue;
			} else {
				String[] temp = s.split("/");
				int c = 1;// figure out how many words does the country have
				for (int j = 0; j < temp[2].length(); j++)
					if (temp[2].charAt(j) == '-')
						c++;
				temp[2] = temp[2].replaceAll("-", " ");
				String ret = getComp(c, txt);

				// ccobj = new CountryCompObj();
				// cid = new CompIdLinkSoccerPlunter();
				ccas = new CCAllStruct();
				i++;

				ccas.setCompId(i);
				ccas.setCountry(temp[2]);
				ccas.setCompetition(ret);
				ccas.setCompLink(s);

				// ccobj.setId(i);
				// ccobj.setCountry(temp[2]);
				// ccobj.setCompetition(ret);
				//
				// cid.setCompId(i);
				// cid.setCompLink(s);
				// CountryCompetition.compList.add(ccobj);
				// CountryCompetition.compLinkList.add(cid);
				CountryCompetition.ccasList.add(ccas);
				// System.out.println(i + " " + s);
			}
		}
		try {
			CountryCompetition cc = new CountryCompetition();
			cc.storeCCAllStruct();
			// cc.storeCountryComp();
			// cc.storeCompIdLink();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public String getComp(int c, String strComp) {
		StringBuilder sb = new StringBuilder();
		String[] s = nc.convertNonAscii(strComp).split(" ");
		for (int i = c; i < s.length; i++) {
			sb.append(s[i]);
			if (i < s.length) {
				sb.append(" ");
			}
		}
		return sb.toString();
	}

}
