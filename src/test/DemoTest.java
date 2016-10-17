package test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mysql.fabric.xmlrpc.base.Array;

import demo.Demo;
import strategyAction.Strategy;

public class DemoTest {

	public static void main(String[] args) throws SQLException, IOException {
		// Strategy s = new Strategy();
		// Demo.initCCAllStruct();
		//// s.dailyPartTask();
		// s.yesterdayRun();

		// score.getFinishedYesterday();
		// tmf.completeYesterday();

		// String s="aaaaa [w] bbbbb/ccccc!ddddd#eee";
		// String s1=s.replace("[w]", "~");
		// String s2=s.replace("[w]", "~");
		// String s3=s.replaceFirst("[w] ", "~");
		// System.out.println(s1);
		// System.out.println(s2);
		// System.out.println(s3);

		// String[] sss= s.split("[ /!]");
		// for(String ss:sss)
		// System.out.println(ss);

		List<String> ls = new ArrayList<>();
		ls.add("one");
		ls.add("two");
		ls.add("tre");

		tt2(ls);
		System.out.println("ls modified");
		for(String s:ls){
			System.out.println(s);
		}

	}

	public static void tt2(List<String> ls) {
		List<String> lss=ls;
		for(String s:lss){
			System.out.println(s);
		}
		lss.remove(1);
		ls.remove(0);
		System.out.println("lss modified");
		for(String s:lss){
			System.out.println(s);
		}
		
	}
}
