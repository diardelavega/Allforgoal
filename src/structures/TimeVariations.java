package structures;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import strategyAction.TempMatchFunctions;
import api.functionality.obj.BaseMatchLinePred;
import basicStruct.FullMatchLine;

public class TimeVariations {
	public static final Logger log = LoggerFactory
			.getLogger(TimeVariations.class);
	// implement lists of competition ids for yesterday, today & tomorrows
	public static List<Integer> yesterdayComps = new ArrayList<>();
	public static List<Integer> todayComps = new ArrayList<>();
	public static List<Integer> tomorrowComps = new ArrayList<>();

	public static Map<LocalDate, Map<Integer, List<FullMatchLine>>> mapMPL = new HashMap<>();

	public void initMPL() throws SQLException {
		// read from recent matches yesterday, today, tomorrow
		LocalDate ld = LocalDate.now();

		// add today
		if (!mapMPL.containsKey(ld)) {
			addToMPLMap(ld);
		}
		// add yesterday
		if (!mapMPL.containsKey(ld.minusDays(1))) {
			addToMPLMap(ld.minusDays(1));
		}
		// add tomorrow
		if (!mapMPL.containsKey(ld.plusDays(1))) {
			addToMPLMap(ld.plusDays(1));
		}
	}

	public void addToMPLMap(LocalDate ld) throws SQLException {
		/* add the date specific matches from recent DB to MPL map */
		log.info("adding to MPLmap {}",ld);
		
		if (!mapMPL.containsKey(ld)) {
			TempMatchFunctions tmf = new TempMatchFunctions();
			Map<Integer, List<FullMatchLine>> tempMap = new HashMap<Integer, List<FullMatchLine>>();
			List<FullMatchLine> tempList = new ArrayList<FullMatchLine>();
			List<FullMatchLine> list = tmf.readFullFromRecentMatches(ld);
			
			for (int i = 0; i < list.size(); i++) {
				if(tempMap.keySet().contains(list.get(i).getComId())){
					//map has data for this competition
					tempMap.get(list.get(i).getComId()).add(list.get(i));
				}
				else{
					tempList = new ArrayList<FullMatchLine>();
					tempList.add(list.get(i));
					tempMap.put(list.get(i).getComId(),tempList);
				}
			}
			mapMPL.put(ld, tempMap);
			log.info(" in MPL map matches : {},  comps-{}",list.size(),mapMPL.size());
		}
	}

	public void threeDayMPLMap() {
		/* keep only today, yesterday & tomorrow and remove every other date */
		LocalDate ld = LocalDate.now();
		List<LocalDate> loclis = new ArrayList<LocalDate>();
		for (LocalDate key : mapMPL.keySet()) {
			if (key.isAfter(ld.plusDays(1)) || key.isBefore(ld.minusDays(1))) {
				// System.out.println(key + "    " + mapMPL.get(key));
				loclis.add(key);
			}
		}

		for (LocalDate k : loclis) {
			mapMPL.remove(k);
		}
	}

	public void countComps() {
		log.info("yesterday size : {}", yesterdayComps.size());
		if (yesterdayComps.size() > 0) {
			for (int itn : yesterdayComps) {
				System.out.print(itn + ", ");
			}
			System.out.println();
		}

		log.info("todayComps size : {}", todayComps.size());
		if (todayComps.size() > 0) {
			for (int itn : todayComps) {
				System.out.print(itn + ", ");
			}
			System.out.println();
		}

		log.info("tomorrowComps size : {}", tomorrowComps.size());
		if (tomorrowComps.size() > 0) {
			for (int itn : tomorrowComps) {
				System.out.print(itn + ", ");
			}
			System.out.println();
		}
	}

	public void compsDateRotate() {
		// when the daye changes, change the lists of the comps
		yesterdayComps = todayComps;
		todayComps = tomorrowComps;
		tomorrowComps.clear();// to bee refilled
		countComps();
	}
}