package demo;

import java.io.IOException;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import scrap.SoccerPrunterMAtches;
import scrap.Soccerpunter_homePage;
import structures.CountryCompetition;
import structures.MatchesList;
import calculate.MatchToTableRenewal;

//import com.google.gson.JsonSyntaxException;

import dbtry.Conn;
import extra.Unilang;

public class Demo {
	public static final Logger logger = LoggerFactory.getLogger(Demo.class);

	public static void main(String[] args) throws IOException, SQLException {
		// renovateCCall();

		initCCAllStruct();
		funcGrab();
//		trainFileFromMatches();
	}

	public static void initCCAllStruct() throws SQLException, IOException {
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

	public static void funcGrab() throws IOException, SQLException {
		int compIdx = CountryCompetition.idToIdx.get(157);
		// String link = CountryCompetition.ccasList.get(111).getCompLink();
		// int compId = CountryCompetition.ccasList.get(111).getCompId();

		logger.info("{}", CountryCompetition.ccasList.get(compIdx).printer());
		// System.out.println(link);
		MatchesList ml = new MatchesList();

		// // ----------------TEST-----------
		SoccerPrunterMAtches spm = new SoccerPrunterMAtches();
		spm.competitionResultsGrabbers(compIdx);
//		 spm.remainingResultsGraber(compIdx);

		/*
		 * re calculate prediction file from dab matches without re grabing
		 * them. bets fixed
		 */

		// // -------------AFTER GRAB Calcualate & Put to Predfile
		MatchToTableRenewal mttr;
		for (Integer key : MatchesList.readMatches.keySet()) {
			mttr = new MatchToTableRenewal(key);
			mttr.calculate(MatchesList.readMatches.get(key));
		}
		ml.insertMatches();

	}

	public static void trainFileFromMatches() throws SQLException, IOException {
		int compId=  157;
		int compIdx = CountryCompetition.idToIdx.get(compId);
		//read from matches, get remaining data from punter
		MatchesList ml = new MatchesList();
		ml.readMatchesComp(compId);
		// recalculate and procede as normal
//		SoccerPrunterMAtches spm = new SoccerPrunterMAtches();
//		 spm.remainingResultsGraber(compIdx);
		 
		 MatchToTableRenewal mttr;
			for (Integer key : MatchesList.readMatches.keySet()) {
				mttr = new MatchToTableRenewal(key);
				mttr.calculate(MatchesList.readMatches.get(key));
			}
			ml.insertMatches();
	}
	
	public static void renovateCCall() throws IOException {
		Soccerpunter_homePage sph = new Soccerpunter_homePage();
		sph.goGetCompetitions();
		//tries to renovate all the data in the ccal struct
		/*
		 * in case this function is chosen try to keep in mind the update on the
		 * link(url addres specifically the year that the competition 2016-2017
		 * etc) and the coordination of the ccallstruct with the scoredatastruct
		 */
	}
}
