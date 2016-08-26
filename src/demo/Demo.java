package demo;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
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

		initCCAllStruct();
		funcGrab();
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

	
	public static void funcGrab() throws IOException, SQLException{
		int compIdx = CountryCompetition.idToIdx.get(112) ;
		// String link = CountryCompetition.ccasList.get(111).getCompLink();
		// int compId = CountryCompetition.ccasList.get(111).getCompId();

		logger.info("{}", CountryCompetition.ccasList.get(compIdx).printer());
		// System.out.println(link);
		MatchesList ml = new MatchesList();
		
		
		// // ----------------TEST-----------
		SoccerPrunterMAtches spm = new SoccerPrunterMAtches();
		spm.competitionResultsGrabbers(compIdx);
		 spm.remainingResultsGraber(compIdx);

		/*
		 * re calculate prediction file from dab matches without re grabing
		 * them. bets fixed
		 */

		// // -------------AFTER GRAB Calcualate & Put to Predfile
		// //TODO handle db tables with the same banes
		MatchToTableRenewal mttr;
		for (Integer key : MatchesList.readMatches.keySet()) {
			mttr = new MatchToTableRenewal(key);
			mttr.calculate( MatchesList.readMatches.get(key));
		}
		ml.insertMatches();


	}
}
