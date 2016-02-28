package demo;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;

import calculate.MatchToTableRenewal;
import basicStruct.CompIdLinkSoccerPlunter;
import basicStruct.CountryCompObj;
import dbtry.Conn;
import scrap.AjaxGrabber;
import scrap.SoccerPrunterMAtches;
import scrap.Soccerpunter_homePage;
import structures.CompetitionTeamTable;
import structures.CountryCompetition;
import structures.MatchesList;
import test.MatchQueries;
import test.Timestamps;

public class Demo {

	public static void main(String[] args) throws IOException, SQLException {
		// TODO Auto-generated method stub
		// Soccerpunter_homePage sp = new Soccerpunter_homePage();
		// sp.goGetCompetitions();

		initStructs();

		String link = CountryCompetition.compLinkList.get(37).getCompLink();
		int compId = CountryCompetition.compList.get(37).getId();
		System.out.println(link);
		//
		{
			{// TODO this section grabs the matches from the site
				SoccerPrunterMAtches spm = new SoccerPrunterMAtches();
				spm.matchGraber();
				spm.competitionResultsGrabbers(link, compId);
				// at this point matches garbed and put to list
			}

			{// TODO this section reads and writes data to matches table
				MatchesList ml = new MatchesList();
				ml.insertMatches();
				if (ml.readMatches.size() == 0) {
					ml.readMatchesComp(compId);
				}
			}
			// TODO store the list in db

			// TODO call MatchToTableRenewual and evaluate attributes
			MatchToTableRenewal mttr;

			for (Integer key : MatchesList.readMatches.keySet()) {
				mttr = new MatchToTableRenewal(
						MatchesList.readMatches.get(key), key);
				MatchToTableRenewal.fh.openOutput();
				mttr.calculate();
				MatchToTableRenewal.fh.closeOutput();
			}
		}
		// ajaxGrabber();

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

	public static void initStructs() throws SQLException {
		/*
		 * read from the DB the competitions data and keep them in the java
		 * Competition* structures
		 */
		Conn conn = new Conn();
		conn.open();
		initCC(conn.getConn());
		initLinks(conn.getConn());
		conn.close();
	}

	public static void initCC(Connection conn) throws SQLException {
		CountryCompetition cp = new CountryCompetition();
		cp.readContryComp(conn);
		if (cp.compList.size() > 0) {
			System.out.println("Country competition structure is ready");
		} else {
			System.out
					.println("Country competition structure not initialized corectly");
		}
	}

	public static void initLinks(Connection conn) throws SQLException {
		CountryCompetition cp = new CountryCompetition();
		cp.readCompIdLink(conn);
		if (cp.compLinkList.size() > 0) {
			System.out.println("Competition link structure is ready");
			// System.out.println(cp.compLinkList.get(0).getCompLink());
		} else {
			System.out
					.println("Competition link structure is not initialized corectly");
		}
	}

	public static void foo() {
		Conn c = new Conn();
		c.open();
		c.getData();
		c.close();
	}
}
