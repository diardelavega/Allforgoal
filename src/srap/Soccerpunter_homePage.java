package srap;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.Commons;

import structures.CountryCompObj;
import structures.CountryCompetition;
import extra.CompIdLinkSoccerPlunter;
import extra.NameCleaner;

public class Soccerpunter_homePage {

	private String link = "http://www.soccerpunter.com";
	private List<String> competitions = new ArrayList<>();

	public void goGetCompetitions() throws IOException {
		// got tho the home address of the site ang get the link for all the
		// competitions;

		// Document doc = Jsoup.connect(link).userAgent("Mozilla").get();
		File ssfile = new File("C:/Users/Administrator/Desktop/ss.html");
		Document doc = Jsoup.parse(ssfile, "UTF-8");

		Element linkStructure = doc.getElementsByTag("select").get(4);
		// System.out.println(linkStructure);
		Elements opts = linkStructure.getElementsByTag("option");

		int i = 0;
		String s, txt;
		CountryCompObj ccobj;// =new CountryCompObj();
		CompIdLinkSoccerPlunter cid;// =new CompIdLinkSoccerPlunter ();
		CountryCompetition cc = new CountryCompetition();

		for (Element o : opts) {
			// System.out.println(o.attr("value"));
			s = o.attr("value").toString();
			txt = o.text();
			if (s.length() == 0) {
				continue;
			}
			if (s.contains("Cup") || s.contains("Coppa") || s.contains("Copa")
					|| s.contains("off") || s.contains("Coupe")
					|| s.contains("World") || s.contains("Africa")
					|| s.contains("America") || s.contains("Europe")
					|| s.contains("Asia")) {
				continue;
			} else {
				String[] temp = s.split("/");
				int c = 1;// figure out how many words does the country have
				for (int j = 0; j < temp[2].length(); j++)
					if (temp[2].charAt(j) == '-')
						c++;
				temp[2] = temp[2].replaceAll("-", " ");
				String ret = getComp(c, txt);

				ccobj = new CountryCompObj();
				cid = new CompIdLinkSoccerPlunter();
				i++;
				ccobj.setId(i);
				ccobj.setCountry(temp[2]);
				ccobj.setCompetition(ret);

				cid.setCompId(i);
				cid.setCompLink(s);
				cc.getCompList().add(ccobj);
				cc.getCompLinkList().add(cid);

				// System.out.println(i + " " + s);
			}
		}
		try {
			cc.storeCountryComp();
			cc.storeCompIdLink();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public String getComp(int c, String strComp) {
		NameCleaner nc = new NameCleaner();
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
