package demo;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import dbtry.Conn;
import scrap.Soccerpunter_homePage;
import structures.CountryCompetition;
import test.MatchQueries;

public class Demo {

	public static void main(String[] args) throws IOException, SQLException {
		// TODO Auto-generated method stub
		// Soccerpunter_homePage sp = new Soccerpunter_homePage();
		// sp.goGetCompetitions();
		// sp.r

		initStructs();

		// foo(); // test db function
		// MatchQueries mq = new MatchQueries ();
		// mq.init();
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
