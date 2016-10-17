package test;

import java.io.IOException;
import java.sql.SQLException;

import demo.Demo;
import strategyAction.Strategy;

public class DemoTest {

	public static void main(String[] args) throws SQLException, IOException {
//		Strategy s = new Strategy();
//		Demo.initCCAllStruct();
////		s.dailyPartTask();
//		s.yesterdayRun();
		
//		score.getFinishedYesterday();
//		tmf.completeYesterday();
		
		String s="aaaaa [w] bbbbb/ccccc!ddddd#eee";
		String s1=s.replace("[w]", "~");
		String s2=s.replace("[w]", "~");
		String s3=s.replaceFirst("[w] ", "~");
		System.out.println(s1);
		System.out.println(s2);
		System.out.println(s3);
		
//		String[] sss= s.split("[ /!]");
//		for(String ss:sss)
//		System.out.println(ss);
		
	}

}
