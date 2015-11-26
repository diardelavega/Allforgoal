package test;

import java.io.IOException;
import java.sql.SQLException;

import dbtry.Conn;
import dbtry.MatchQueries;
import srap.Soccerpunter_homePage;

public class Demo {

	public static void main(String[] args) throws IOException, SQLException {
		// TODO Auto-generated method stub
		 Soccerpunter_homePage sp = new Soccerpunter_homePage();
		 sp.goGetCompetitions();

//		foo(); // test db function
//		MatchQueries mq = new MatchQueries ();
//		mq.init();
	}

	public static void foo() {
		Conn c = new Conn();
		c.open();
		c.getData();
		c.close();
	}
}
