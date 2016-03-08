package demo;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.spi.LoggerFactoryBinder;

import com.google.gson.JsonSyntaxException;

import calculate.MatchToTableRenewal;
import basicStruct.CompIdLinkSoccerPlunter;
import basicStruct.CountryCompObj;
import dbtry.Conn;
import extra.Status;
import extra.StringSimilarity;
import extra.Unilang;
import scrap.AjaxGrabber;
import scrap.SoccerPrunterMAtches;
import scrap.Soccerpunter_homePage;
import scrap.XscoreUpComing;
import structures.CompetitionTeamTable;
import structures.CountryCompetition;
import structures.MatchesList;
import test.MatchQueries;
import test.Timestamps;

public class Demo {
	public static final Logger logger = LoggerFactory.getLogger(Demo.class);

	public static void main(String[] args) throws IOException, SQLException {
		// {// fill ccallstruct from site and store to db
		// Soccerpunter_homePage sp = new Soccerpunter_homePage();
		// sp.goGetCompetitions();
		// }

		 {
			 initCCAllStruct();
		//
//		 String link = CountryCompetition.ccasList.get(37).getCompLink();
//		 int compId = CountryCompetition.ccasList.get(37).getCompId();
//		 System.out.println(link);
		 }

		// {
		// {// TODO this section grabs the matches from the site
		// SoccerPrunterMAtches spm = new SoccerPrunterMAtches();
		// spm.matchGraber();
		// spm.competitionResultsGrabbers(link, compId);
		// // at this point matches garbed and put to list
		// }
		//
		// {// TODO this section reads and writes data to matches table
		// MatchesList ml = new MatchesList();
		// ml.insertMatches();
		// if (ml.readMatches.size() == 0) {
		// ml.readMatchesComp(compId);
		// }
		// }
		// // TODO store the list in db
		//
		// // TODO call MatchToTableRenewual and evaluate attributes
		// MatchToTableRenewal mttr;
		//
		// for (Integer key : MatchesList.readMatches.keySet()) {
		// mttr = new MatchToTableRenewal(
		// MatchesList.readMatches.get(key), key);
		// MatchToTableRenewal.fh.openOutput();
		// mttr.calculate();
		// MatchToTableRenewal.fh.closeOutput();
		// }
		// }

		 XscoreUpComing sc = new XscoreUpComing();
//		 sc.scrapMatchesDate(LocalDate.now(),Status.SCHEDULED);
		 sc.getScheduledToday();
		// sc.dateTodayFormat();
		// sc.dateTomorrowFormat();
		// sc.dateYesterdayFormat();

		// logger.info("{}", StringSimilarity.levenshteinDistance("MACEDONIa",
		// "FYR Macedonia"));
		// logger.info("{}","MACEDONIa".compareToIgnoreCase("FYR Macedonia"));
		// ajaxGrabber();

		 
		 
//		bar();
	}

	public static void ajaxGrabber() throws IOException {
		AjaxGrabber ag = new AjaxGrabber();
		String url = "http://www.soccerpunter.com/soccer-statistics/England/Premier-League-2015-2016/livesoccerodds?match_id=2043399&home=Manchester+City+FC&away=Tottenham+Hotspur+FC&date=2016-02-14+16%3A15%3A00";
		// String url =
		// "http://www.soccerpunter.com/soccer-statistics/Albania/Superliga-2015-2016/livesoccerodds?match_id=2074044&home=KF+Tirana&away=KS+Skënderbeu+Korçë&date=2015-11-30+13%3A00%3A00";
		String matchId = url.split("_id=|&home")[1];
		ag.f69(matchId);
		// ag.headResults("http://www.soccerpunter.com/livesoccerodds_ajx.php?match_id=2086384&typeId=69");
		// ag.f47("http://www.soccerpunter.com/livesoccerodds_ajx.php?match_id=2086384&typeId=47");
	}

	public static void initCCAllStruct() throws SQLException, JsonSyntaxException, IOException {
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
			System.out.println("Country competition structure not initialized corectly");
		}
		
		Unilang ul = new Unilang();
		ul.init();
		conn.close();
	}

	public static void foo() {
		LocalDate d1 = LocalDate.now();
		 LocalDate pd = LocalDate.of(2016, Month.JANUARY, 12);
		 
		 if(d1.isAfter(d1)){
			 logger.info("FFF UUUUUUU");
		 }
		 else{logger.info("NOOOOOOOOOOOOOOOOOOOO");}
	}

	public static void bar() {
//		Map<String, Integer> mm = new HashMap<>();
//		mm.put("A", 0);
//		mm.put("a", 1);
//		mm.put("b", 2);
//		mm.put("c", 3);
//		mm.put("e", 4);
//		mm.put("f", 5);
//
//		String c = null;
//		System.out.println(mm.get(c));
//		Integer k = mm.get("b");
//		if (k != null) {
//			System.out.println(k);
//		} else {
//			System.out.println("aaaaaaaaaaaaaaa");
//		}
		
		
//		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		LocalDate mDat = LocalDate.parse("01/03/2016",DateTimeFormatter.ofPattern("dd/MM/yyyy") );
		logger.info(mDat.toString());
	}
}
