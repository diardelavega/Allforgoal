package demo;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;

import dbtry.Conn;
import scrap.AjaxGrabber;
import scrap.Soccerpunter_homePage;
import structures.CountryCompetition;
import test.MatchQueries;
import test.Timestamps;

public class Demo {

	public static void main(String[] args) throws IOException, SQLException {
		// TODO Auto-generated method stub
		// Soccerpunter_homePage sp = new Soccerpunter_homePage();
		// sp.goGetCompetitions();
		// sp.r

//		initStructs();
		
//		Timestamps ts = new Timestamps ();
		//			ts.ts("12/01/1990");
//		ts.spliter("http://www.soccerpunter.com/soccer-statistics/Spain/Primera-División-2015-2016/livesoccerodds?match_id=2086384&home=Getafe+Club+de+Fútbol&away=Villarreal+Club+de+Fútbol&date=2015-11-29+11%3A00%3A00");

		AjaxGrabber ag = new AjaxGrabber();
//		ag.headResults("http://www.soccerpunter.com/livesoccerodds_ajx.php?match_id=2086384&typeId=69");
		ag.m2("http://www.soccerpunter.com/livesoccerodds_ajx.php?match_id=2086384&typeId=47");
		// foo(); // test db function
//		 MatchQueries mq = new MatchQueries ();
//		 mq.init();
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
