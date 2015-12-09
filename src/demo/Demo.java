package demo;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;

import basicStruct.CountryCompObj;
import dbtry.Conn;
import scrap.AjaxGrabber;
import scrap.SoccerPrunterMAtches;
import scrap.Soccerpunter_homePage;
import structures.CompetitionTeamTable;
import structures.CountryCompetition;
import test.MatchQueries;
import test.Timestamps;

public class Demo {

	public static void main(String[] args) throws IOException, SQLException {
		// TODO Auto-generated method stub
		// Soccerpunter_homePage sp = new Soccerpunter_homePage();
		// sp.goGetCompetitions();
//		int i;

		System.out.println(0/30);
		// initStructs();
		// SoccerPrunterMAtches spm = new SoccerPrunterMAtches ();
		// spm.matchGraber();

		// CompetitionTeamTable ctt = new CompetitionTeamTable ();
		// ctt.existsDb("country");

		// for (int i=0;i<CountryCompetition.compList.size();i++){
		// CountryCompObj c= CountryCompetition.compList.get(i);
		// System.out.println(i+" "+c.getId()+" "+ c.getCompetition()+
		// " "+c.getCountry());
		// }

		// Timestamps ts = new Timestamps ();
		// ts.ts("12/01/1990");
		// ts.spliter("http://www.soccerpunter.com/soccer-statistics/Spain/Primera-División-2015-2016/livesoccerodds?match_id=2086384&home=Getafe+Club+de+Fútbol&away=Villarreal+Club+de+Fútbol&date=2015-11-29+11%3A00%3A00");

		// ajaxGrabber();
		// foo(); // test db function
		// MatchQueries mq = new MatchQueries ();
		// mq.init();
	}

	public static void ajaxGrabber() throws IOException {
		AjaxGrabber ag = new AjaxGrabber();
		String url = "http://www.soccerpunter.com/soccer-statistics/Albania/Superliga-2015-2016/livesoccerodds?match_id=2074044&home=KF+Tirana&away=KS+Skënderbeu+Korçë&date=2015-11-30+13%3A00%3A00";
		String matchId = url.split("_id=|&home")[1];
		ag.f69(matchId);
		// ag.headResults("http://www.soccerpunter.com/livesoccerodds_ajx.php?match_id=2086384&typeId=69");
		// ag.f47("http://www.soccerpunter.com/livesoccerodds_ajx.php?match_id=2086384&typeId=47");
	}

	public static void initStructs() throws SQLException {
		Conn conn = new Conn();

		conn.open();
		initCC(conn.getConn());
		initLinks(conn.getConn());
		// cp.readCompIdLink(conn.getConn());
		// cp.readContryComp(conn.getConn());
		conn.close();
		// if()

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
			System.out.println(cp.compLinkList.get(0).getCompLink());
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
