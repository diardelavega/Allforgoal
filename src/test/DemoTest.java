package test;

import java.io.IOException;
import java.sql.SQLException;

import demo.Demo;
import strategyAction.Strategy;

public class DemoTest {

	public static void main(String[] args) throws SQLException, IOException {
		Strategy s = new Strategy();
		Demo.initCCAllStruct();
//		s.dailyPartTask();
		s.yesterdayRun();
		
//		score.getFinishedYesterday();
//		tmf.completeYesterday();
	}

}
