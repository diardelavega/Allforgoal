package test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import strategyAction.TempMatchFunctions;
import structures.CountryCompetition;
import basicStruct.MatchObj;

import com.sun.java.swing.plaf.windows.WindowsTreeUI.CollapsedIcon;

import dbtry.Conn;
import demo.Demo;
import extra.StringSimilarity;

public class MainTry {
	{
		System.out.println("_static");
	}

	public static void log2() {
		System.out.println("AAAAAAAAAAAAAA");
	}

	public static void main(String[] args) throws SQLException {
		// Demo.initCCAllStruct();
		// MatchObj mobj = new MatchObj();
		// mobj.setT1("hua");
		// mobj.setT2("borxh");
		// mobj.setComId(3);
		//
		// TempMatchFunctions tmf= new TempMatchFunctions();
		// tmf.incomeTempMatchesList.add(mobj);
		// tmf.storeToTempMatchesDB();

	log(	StringSimilarity.levenshteinDistance("championship", "CHAMPIONSHIP")+"");
	log(	StringSimilarity.levPerWord("champ", "CHAMp")+"");
		// log(CountryCompetition.ccasList.get(5 - 1).getCompetition());
		// log(CountryCompetition.ccasList.get(5 - 1).getCompId() + "");

	}

	public static void log(String s) {
		System.out.println(s);
	}

	class A {
		// static int x=20;
	}

	public static void sortArray() {
		ArrayObj ao11 = new ArrayObj(1, 1, "one");
		ArrayObj ao12 = new ArrayObj(1, 2, "two");
		ArrayObj ao13 = new ArrayObj(1, 3, "three");
		ArrayObj ao14 = new ArrayObj(1, 4, "four");
		ArrayObj ao21 = new ArrayObj(2, 1, "five");
		ArrayObj ao22 = new ArrayObj(2, 2, "six");
		ArrayObj ao23 = new ArrayObj(2, 3, "seven");
		ArrayObj ao24 = new ArrayObj(2, 4, "eight");
		List<ArrayObj> list = new ArrayList<>();
		list.add(ao11);
		list.add(ao12);
		list.add(ao13);
		list.add(ao14);
		list.add(ao21);
		list.add(ao22);
		list.add(ao23);
		list.add(ao24);
		// print(list);

		// Collections.shuffle(list);
		Collections.sort(list);
		print(list);
	}

	public static void print(List<ArrayObj> list) {
		for (ArrayObj ob : list) {
			ob.print();
		}
	}

}
