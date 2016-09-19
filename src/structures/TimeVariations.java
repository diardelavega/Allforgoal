package structures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import api.functionality.obj.BaseMatchLinePred;
import basicStruct.FullMatchLine;

public class TimeVariations {
	// TODO implement lists of competition ids for yesterday, today & tomorrows
		public static List<Integer> yesterdayComps = new ArrayList<>();
		public static List<Integer> todayComps = new ArrayList<>();
		public static List<Integer> tomorrowComps = new ArrayList<>();
		
		// TODO implement lists of competition ids for yesterday, today & tomorrows
		public static  Map<Integer, List<FullMatchLine>> yestFullMatchLine= new HashMap<>();
		public static  Map<Integer, List<FullMatchLine>> todFullMatchLine= new HashMap<>();
		public static  Map<Integer, List<FullMatchLine>> tomFullMatchLine= new HashMap<>();
}
