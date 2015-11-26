package test;

import java.io.IOException;

import dbtry.Conn;
import srap.Soccerpunter_homePage;

public class Demo {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		// Soccerpunter_homePage sp = new Soccerpunter_homePage();
		// sp.goGetCompetitions();

//		foo(); // test db function
	}

	public static void foo() {
		Conn c = new Conn();
		c.open();
		c.getData();
		c.close();
	}
}
