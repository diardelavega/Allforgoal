package structures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Administrator
 * 
 *         a data structure intended to store a combination of countries and
 *         their competitions
 */
public class CountryCompetition {

	private static Map<String, ArrayList<String>> cc = new HashMap<>();

	public static Map<String, ArrayList<String>> getCc() {
		return cc;
	}

	public static void setCc(Map<String, ArrayList<String>> cc) {
		CountryCompetition.cc = cc;
	}

}
