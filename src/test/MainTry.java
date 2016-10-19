package test;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import strategyAction.Strategy;
import structures.CountryCompetition;
import structures.ReducedPredictionTestFile;
import dbtry.Conn;
import diskStore.AnalyticFileHandler;
import extra.Unilang;

public class MainTry {

	public static void main(String[] args) throws SQLException, IOException {
		 initCCAllStruct();
		 Strategy strategy = new Strategy();
//		 strategy.periodic();
		 
		 

		 strategy.yesterdayRun();
//		 strategy.tryTask();
//		 strategy.dateChangePartTask();
		// Soccerpunter_homePage sch= new Soccerpunter_homePage();
		// sch.goGetCompetitions();
		// SoccerPunterOdds spo = new SoccerPunterOdds();
		// spo.getDailyOdds(LocalDate.now());

		// strategy.periodic();
		 
//		 for(key:ul.)
		 
	}

	public static void initCCAllStruct() throws SQLException, IOException {
		/*
		 * read from the DB the competitions data and keep them in the java
		 * Competition* structures. IDX orientation is based on the countries
		 * alphabetical order. (countries that have been added later are in the
		 * ena. with the last competition id, but in case of alphabetical order
		 * they will not be last so the competition order is different from
		 * compId order)
		 */
		CountryCompetition cp = new CountryCompetition();
		Conn conn = new Conn();
		conn.open();
		cp.readCCAllStruct(conn.getConn());
		if (cp.ccasList.size() > 0) {
			System.out.println("Country competition structure is ready");
		} else {
			System.out.println("Country competition "
					+ "structure not initialized corectly");
		}
		System.out.println("ccall size ------------------: "
				+ cp.ccasList.size());

		for (int i = 0; i < CountryCompetition.ccasList.size(); i++) {
			CountryCompetition.idToIdx.put(CountryCompetition.ccasList.get(i)
					.getCompId(), i);
		}

		cp.readsdStruct(conn.getConn());
		System.out.println("ccall size ------------------: "
				+ cp.sdsList.size());
		if (cp.sdsList.size() > 0) {
			System.out.println("Country competition "
					+ "Scorer Data structure is ready");
		} else {
			System.out.println("Country competition scorer data structure "
					+ "not initialized corectly");
		}
		cp.readAllowedComps();
		System.out.println("Allowed comp size= "+cp.allowedcomps);
		cp.readNotAllowedComps();
		System.out.println("NOT Allowed comp size= "+cp.notAllowedcomps);
		
		Unilang ul = new Unilang();
		ul.init();
		conn.close();
	}

	public static void log(String s) {
		System.out.println(s);
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
		// changeList(list);
		// Collections.shuffle(list);
		Collections.sort(list);
		print(list);
	}

	public static void changeList(List<ArrayObj> list) {
		list.get(0).setAtt1(100);
	}

	public static void print(List<ArrayObj> list) {
		for (ArrayObj ob : list) {
			ob.print();
		}
	}

	public static void dater() {
		// LocalDate ld = LocalDate.now();
		// LocalDate ld2 = LocalDate.now().plusDays(3);
		//
		// log(Period.between(ld, ld2).getDays() + "");
		// log(ChronoUnit.DAYS.between(ld2, ld)+"");

		for (int i = 0; i < CountryCompetition.sdsList.size(); i++) {
			log(CountryCompetition.sdsList.get(i).getCompId() + " " + i);
		}
	}

	public static void printer() {
		ReducedPredictionTestFile rpf = new ReducedPredictionTestFile("tq",
				"t2", "1", "o", "Y", "N", "10", "30");
		List <ReducedPredictionTestFile> tl =  new ArrayList<ReducedPredictionTestFile>();
		tl .add(rpf);
		tl .add(rpf);
		File file = new File("C:/ff1/hua.csv");
		AnalyticFileHandler afh = new AnalyticFileHandler();
		try {
			afh.rewriteTestFile(file, tl);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
