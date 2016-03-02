package extra;

import java.util.HashMap;
import java.util.Map;

/**
 * @author diego
 *
 *         A class that has to implement methods for finding a common language
 *         between the different web-site parsed counties, competitions and
 *         teams. Its main components will be maps of <id,term> : where id will
 *         be the same for the same entity, where-as the "term" will be the text
 *         of the entity in its different forms. *******************************
 *         To facilitate the searches we will have for xscore: MAP<term,id>,
 *         term = key & id=value.
 * 
 *
 */
public class Unilang {

	public static Map<Integer, String> ccasMap = new HashMap<>();
	public static Map<String, Integer> scoreMap = new HashMap<>();
	private int lastCassMapIdx;
	private int lastScoreMapIdx;

	public String scoreToccas(String term) {
		return ccasMap.get(scoreMap.get(term));
	}

	public String ccasToScore(String term) {
		if (ccasMap.containsValue(term)) {
			for (Integer key1 : ccasMap.keySet()) {
				if (ccasMap.get(key1).equals(term)) {
					// search in scoreMap now
					for (String key2 : scoreMap.keySet()) {
						if (scoreMap.get(key2).equals(key1)) {
							return key2;
						}
					} // for scoreMap
				}
			} // for ccasMap

		}
		return StandartResponses.NOT_FOUND;

	}

	public int ccasTermsId(String term) {
		if (ccasMap.containsValue(term)) {
			for (int key : ccasMap.keySet()) {
				if (ccasMap.get(key).equals(term)) {
					return key;
				}
			}
		}
		return -1;
	}

	public String ccasIdsTerm(int id) {
		return ccasMap.get(id);
	}

	public int scoreTermsId(String term) {
		return scoreMap.get(term);

	}

	public String scoreIdsTerm(int id) {
		if (scoreMap.containsValue(id)) {
			for (String key : scoreMap.keySet()) {
				if (scoreMap.get(key).equals(id)) {
					return key;
				}
			}
		}
		return "";
	}

	public void appendScoreTerms() {
		// TODO go to filehandler and append tuples from the last idx ()
		// last idx= map/size
	}

	public void appendCcasTerms() {
		// TODO go to filehandler and append tuples from the last idx ()
		// last idx= map/size
	}

	public void readScoreTerms() {
		// TODO go to file and read all the terms
		// last idx= map.size
	}

	public void readCcasTerms() {
	}

	public int addTerm(String ccasTerm,String scoreTerm) {
		/*
		 * as common id we use a random integer. to make it unique for booth
		 * maps we chose the biggest of the two map sizes
		 */

		int id = Math.max(ccasMap.size(), scoreMap.size());
		ccasMap.put(id, ccasTerm);
		scoreMap.put(scoreTerm, id);
		return id;

	}

}
