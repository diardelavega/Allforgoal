package srap;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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
		String s;
		for (Element o : opts) {
			// System.out.println(o.attr("value"));
			s = o.attr("value").toString();
			if (s.contains("Cup") || s.contains("Copa") || s.contains("World")
					|| s.contains("Africa") || s.contains("America")
					|| s.contains("Europe") || s.contains("Asia")) {
				continue;
			} else {
				i++;
				System.out.println(i + " " + s);
			}
		}
		// Elements adlinks=

	}

}
