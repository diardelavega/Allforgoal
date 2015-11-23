package srap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Soccerpunter_homePage {

	private String link="http://www.soccerpunter.com";
	private List <String> competitions = new ArrayList<>();
	
	public void goGetCompetitions() throws IOException{
		//got tho the home address of the site ang get the link for all the competitions;
	
		Document doc = Jsoup.connect(link).userAgent("Mozilla").get();
		
		Element linkStructure = doc.getElementById(id)
		Elements adlinks= 
		
	}
	
}
