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
import basicStruct.MatchObj;
import basicStruct.ScorerDataStruct;
import dbtry.Conn;
import extra.Status;
import extra.StringSimilarity;
import extra.Unilang;
import scrap.AjaxGrabber;
import scrap.SoccerPrunterMAtches;
import scrap.Soccerpunter_homePage;
import scrap.XscoreUpComing;
import strategyAction.TempMatchFunctions;
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
		// CountryCompetition cc = new CountryCompetition();
		// cc.storeCCAllStruct();
		// }

		initCCAllStruct();
		// for (int i=0;i<cc.sdsList.size();i++){
		// System.out.print(i+"\t");
		// cc.sdsList.get(i).printer();
		// }
		//
		int compIdx = 9;
		// String link = CountryCompetition.ccasList.get(111).getCompLink();
		// int compId = CountryCompetition.ccasList.get(111).getCompId();

		logger.info("{}", CountryCompetition.ccasList.get(compIdx).printer());
		// System.out.println(link);
		MatchesList ml = new MatchesList();
		//
		// // ----------------TEST-----------
		SoccerPrunterMAtches spm = new SoccerPrunterMAtches();
//		spm.competitionResultsGrabbers(compIdx);
		 spm.remainingResultsGraber(compIdx);

		// ml.storeSched();
		// ml.readSched();
		/*
		 * re calculate prediction file from dab matches without re grabing
		 * them. bets fixed
		 */

		// for (Integer key : MatchesList.readMatches.keySet()) {
		// // mttr = new MatchToTableRenewal(key);
		// for(MatchObj m:MatchesList.readMatches.get(key))
		// // log(m.printMatch());
		// }
		// // -------------AFTER GRAB Calcualate & Put to Predfile
		// //TODO handle db tables with the same banes
		MatchToTableRenewal mttr;
		for (Integer key : MatchesList.readMatches.keySet()) {
			mttr = new MatchToTableRenewal(key);
			mttr.calculate( MatchesList.readMatches.get(key));
		}
		ml.insertMatches();

		// {
		// {// TODO this section grabs the matches from the site
		// SoccerPrunterMAtches spm = new SoccerPrunterMAtches();
		// // spm.matchGraber();
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
		// mttr = new MatchToTableRenewal( MatchesList.readMatches.get(key),
		// key);
		// MatchToTableRenewal.fh.openOutput();
		// mttr.calculate();
		// MatchToTableRenewal.fh.closeOutput();
		// }
		// }

		// XscoreUpComing sc = new XscoreUpComing();
		//
		// TempMatchFunctions tmf = new TempMatchFunctions();
		//
		// sc.getScheduledToday();
		// tmf.openDBConn();
		// tmf.corelatePunterXScorerTeams();
		// tmf.storeToTempMatchesDB();
		// {
		// when the periodic check for finished matches is on
		// sc.getFinishedToday();
		// tmf.complete(LocalDate.now());

		// }
		// tmf.closeDBConn();
		// sc.clearLists();

		// Unilang ul = new Unilang();
		// log(ul.scoreTeamsMap.size() + "");
		// log(ul.ccasTeamsMap.size() + "");
		// for(Integer key:ul.ccasTeamsMap.keySet()){
		// log(key+" "+ul.ccasTeamsMap.get(key));
		// }
		// log(ul.scoreTeamsId("BLACKBURN") + "");
		// log(ul.ccasIdsTeam(ul.scoreTeamsId("BLACKBURN")) + "");
		//
		// log(ul.scoreTeamToCcas("BLACKBURN"));

		// log(ul.ccasMap.size()+"");
		// log(ul.scoreMap.size()+"");
		// log("");
		// log(ul.scoreTermsId("ENGLAND")+"");
		// log(ul.scoreToCcas("ENGLAND")+"");

		// sc.dateTodayFormat();
		// sc.dateTomorrowFormat();
		// sc.dateYesterdayFormat();

		// logger.info("{}", StringSimilarity.levenshteinDistance("MACEDONIa",
		// "FYR Macedonia"));
		// logger.info("{}","MACEDONIa".compareToIgnoreCase("FYR Macedonia"));
		// ajaxGrabber();

		// bar();
		// printest();

	}

	private static void log(String s) {
		System.out.println(s);

	}

	public static void ajaxGrabber() throws IOException {
		AjaxGrabber ag = new AjaxGrabber();
		String url = "http://www.soccerpunter.com/soccer-statistics/England/Premier-League-2015-2016/livesoccerodds?match_id=2043423&home=West+Ham+United+FC&away=Sunderland+AFC&date=2016-02-27+12%3A45%3A00";
		// String url =
		// "http://www.soccerpunter.com/soccer-statistics/England/Premier-League-2015-2016/livesoccerodds?match_id=2043399&home=Manchester+City+FC&away=Tottenham+Hotspur+FC&date=2016-02-14+16%3A15%3A00";
		// String url =
		// "http://www.soccerpunter.com/soccer-statistics/Albania/Superliga-2015-2016/livesoccerodds?match_id=2074044&home=KF+Tirana&away=KS+Skënderbeu+Korçë&date=2015-11-30+13%3A00%3A00";
		String matchId = url.split("_id=|&home")[1];
		ag.f69(matchId);
		// ag.f47(matchId);
		// ag.headResults("http://www.soccerpunter.com/livesoccerodds_ajx.php?match_id=2086384&typeId=69");
		// ag.f47("http://www.soccerpunter.com/livesoccerodds_ajx.php?match_id=2086384&typeId=47");
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
		for (int i = 0; i < CountryCompetition.ccasList.size(); i++) {
			CountryCompetition.idToIdx.put(CountryCompetition.ccasList.get(i)
					.getCompId(), i);
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

		Unilang ul = new Unilang();
		ul.init();
		conn.close();
	}

	public static void printest() {
		String s = "aa sa. 1";
		log(s);
		log(s.replace(' ', '_'));
		log(s.replace(" ", "_"));
		log(s.replace(" ", "_"));
		log(s.replaceAll(" ", "_"));
		log(s.replaceFirst(" ", "_"));

	}

	public static void foo() {
		LocalDate d1 = LocalDate.now();
		LocalDate pd = LocalDate.of(2016, Month.JANUARY, 12);

		if (d1.isAfter(d1)) {
			logger.info("FFF UUUUUUU");
		} else {
			logger.info("NOOOOOOOOOOOOOOOOOOOO");
		}
	}

	public static void bar() {
		// Map<String, Integer> mm = new HashMap<>();
		// mm.put("A", 0);
		// mm.put("a", 1);
		// mm.put("b", 2);
		// // mm.put("c", 3);
		// // mm.put("e", 4);
		// // mm.put("f", 5);
		// log(mm.get("c") + "");
		// Map<String,I>

		//
		// String c = null;
		// System.out.println(mm.get(c));
		// Integer k = mm.get("b");
		// if (k != null) {
		// System.out.println(k);
		// } else {
		// System.out.println("aaaaaaaaaaaaaaa");
		// }

		// DateTimeFormatter formatter =
		// DateTimeFormatter.ofPattern("dd/MM/yyyy");
		// LocalDate mDat = LocalDate.parse("01/03/2016",
		// DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		// logger.info(mDat.toString());

		LocalDate mDat = LocalDate.parse("2016-01-12");
		logger.info(mDat.plusDays(2).toString());
	}
}
