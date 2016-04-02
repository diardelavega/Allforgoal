package test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import scrap.OddsNStats;
import scrap.OddsNStatsMatchOdd;
import scrap.Soccerpunter_homePage;
import scrap.XscoreUpComing;
import strategyAction.Strategy;
import strategyAction.TempMatchFunctions;
import structures.CountryCompetition;
import basicStruct.MatchObj;

import com.google.gson.JsonSyntaxException;
import com.sun.java.swing.plaf.windows.WindowsTreeUI.CollapsedIcon;
import com.sun.xml.ws.rx.util.FiberExecutor;

import dbtry.Conn;
import demo.Demo;
import extra.StringSimilarity;
import extra.Unilang;

public class MainTry {
	{
		System.out.println("_static");
	}

	public static void log2() {
		System.out.println("AAAAAAAAAAAAAA");
	}

	public static void main(String[] args) throws SQLException, IOException {
		initCCAllStruct();
		Strategy strategy = new Strategy();
		strategy.tryTask();

		// strategy .periodic();
		// corelator();
		// LocalDate ld =LocalDate.now();
		// log(ld.toString());
		// Soccerpunter_homePage sch = new Soccerpunter_homePage();
		// sch.goGetCompetitions();
		// filer();
		// System.out.println("Uganda".compareTo("Usa"));
		//

		// dater();
//		distancer() ;
//		odder();
	}

	public static void odder(){
//		OddsNStats odns=new OddsNStats();
//		odns.getOddsPage(LocalDate.now());
		
		OddsNStatsMatchOdd ood = new OddsNStatsMatchOdd("http://oddsandstats.com/as-latina-vs-us-avellino/e/5116387/");
		ood.pageGraber();
		log(ood.getOver()+"");
		log(ood.getUnder()+"");
	}
	 
	public static void distancer() {
//		String s1 ="LIGA II SERIE 1", s2="LIGA I";
		String s1 ="GERMANY  BUNDESLIGA WOMEN", s2="GERMANY  BUNDESLIGA";
		log(StringSimilarity.levenshteinDistance(s1, s2)+"");
	}
	
	public static void seter(){
//		HashSet<String> set = new HashSet<>();
		TreeSet<String> set = new TreeSet<String>();
		set.add("Amarija");
		set.add("bojano");
		set.add("tercerta");
		
//		set.g
		
	}

	public static void corelator() {
		/* write the corelated terms found and unfound */
		// XscoreUpComing score = new XscoreUpComing();
		MatchGetter score = new MatchGetter();
		score.getFinishedYesterday();
		// score
	}

	public static void initCCAllStruct() throws SQLException,
			JsonSyntaxException, IOException {
		/*
		 * read from the DB the competitions data and keep them in the java
		 * Competition* structures
		 */
		CountryCompetition cp = new CountryCompetition();
		Conn conn = new Conn();
		conn.open();
		cp.readCCAllStruct(conn.getConn());
		if (cp.ccasList.size() > 0) {
			System.out.println("Country competition structure is ready");
		} else {
			System.out
					.println("Country competition structure not initialized corectly");
		}
		cp.readsdStruct(conn.getConn());
		if (cp.sdsList.size() > 0) {
			System.out
					.println("Country competition Scorer Data structure is ready");
		} else {
			System.out
					.println("Country competition scorer data structure not initialized corectly");
		}
		cp.readAllowedComps();
		cp.readNotAllowedComps();

		Unilang ul = new Unilang();
		ul.init();
		conn.close();
	}

	public static void log(String s) {
		System.out.println(s);
	}

	public static void sortArray() {
		ArrayObj ao11 = new ArrayObj(1, 1, "one");
		ArrayObj ao12 = new ArrayObj(1, 2, "two");
		ArrayObj ao13 = new ArrayObj(1, 3, "three");
		ArrayObj ao14 = new ArrayObj(1, 4, "four");
		ArrayObj ao21 = new ArrayObj(2, 1, "five");
		ArrayObj ao22 = new ArrayObj(2, 2, "six");
		ArrayObj ao23 = new ArrayObj(2, 3, "seven");
		ArrayObj ao24 = new ArrayObj(2, 4, "eight");
		List<ArrayObj> list = new ArrayList<>();
		list.add(ao11);
		list.add(ao12);
		list.add(ao13);
		list.add(ao14);
		list.add(ao21);
		list.add(ao22);
		list.add(ao23);
		list.add(ao24);
		// print(list);

		// Collections.shuffle(list);
		Collections.sort(list);
		print(list);
	}

	public static void print(List<ArrayObj> list) {
		for (ArrayObj ob : list) {
			ob.print();
		}
	}

	public static void filer() throws IOException {
		String base = "C:/TestFileFolder";
		File fo = new File(base + "/MyFolder/c34");
		if (!fo.exists()) {
			fo.mkdirs();
		}

		Date date = new Date(15, 3, 2);

		File f = new File(fo + "/"
				+ /* LocalDate.now() */date.toString().toString());
		if (!f.exists()) {
			f.createNewFile();
		}
		BufferedWriter br = new BufferedWriter(new FileWriter(f, true));
		br.append("Marakaibo \n");
		br.append("Baila baracude \n");
		br.append("Si ma bailar nuda \n");
		br.append("Cha cah! \n");
		br.close();
	}

	public static void dater() {
		LocalDate ld = LocalDate.now();
		LocalDate ld2 = LocalDate.now().plusDays(3);

		log(Period.between(ld, ld2).getDays() + "");

	}

}
