package extra;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonSyntaxException;

import diskStore.FileHandler;

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

	public static final Logger logger = LoggerFactory.getLogger(Unilang.class);

	public static Map<Integer, String> ccasMap = new HashMap<>();
	public static Map<String, Integer> scoreMap = new HashMap<>();

	public static Map<Integer, String> ccasTeamsMap = new HashMap<>();
	public static Map<String, Integer> scoreTeamsMap = new HashMap<>();

	private FileHandler fh = new FileHandler();

	// private static int lastCassMapIdx;
	// private static int lastScoreMapIdx;

	public String scoreToCcas(String term) {
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

	// ------------------------------------------
	public String scoreTeamToCcas(String term) {
		return ccasTeamsMap.get(scoreTeamsMap.get(term));
	}

	public String ccasTeamToScore(String term) {
		if (ccasTeamsMap.containsValue(term)) {
			for (Integer key1 : ccasTeamsMap.keySet()) {
				if (ccasTeamsMap.get(key1).equals(term)) {
					// search in scoreMap now
					for (String key2 : scoreTeamsMap.keySet()) {
						if (scoreTeamsMap.get(key2).equals(key1)) {
							return key2;
						}
					} // for scoreMap
				}
			} // for ccasMap

		}
		return StandartResponses.NOT_FOUND;
	}

	// ====================================
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

	// --------------------------------------------
	public int ccasTeamsId(String term) {
		if (ccasTeamsMap.containsValue(term)) {
			for (int key : ccasTeamsMap.keySet()) {
				if (ccasTeamsMap.get(key).equals(term)) {
					return key;
				}
			}
		}
		return -1;
	}

	public String ccasIdsTeam(int id) {
		return ccasTeamsMap.get(id);
	}

	public int scoreTeamsId(String term) {
		return scoreTeamsMap.get(term);

	}

	public String scoreIdsTeam(int id) {
		if (scoreTeamsMap.containsValue(id)) {
			for (String key : scoreTeamsMap.keySet()) {
				if (scoreTeamsMap.get(key).equals(id)) {
					return key;
				}
			}
		}
		return "";
	}

	// =====================================
	public void appendScoreTerms(String term, int id) throws IOException {
		logger.info("score    {} -   {}", id, term);
		scoreMap.put(term, id);
		fh.appendUnlangScorerTerm(term, id);
	}

	public void appendCcasTerms(int id, String term) throws IOException {
		logger.info("ccas    {} -   {}", id, term);
		ccasMap.put(id, term);
		fh.appendUnilangCcasTerm(id, term);
	}

	public void readScoreTerms() throws JsonSyntaxException, IOException {
		// TODO go to file and read all the terms
		// last idx= map.size

		fh.readUnilangAllScorerTerms();

	}

	public void readCcasTerms() throws JsonSyntaxException, IOException {
		fh.readUnilangAllCcasTerms();
	}

	// -------------------------------------
	public void appendScoreTeamTerms(String team, int id) throws IOException {
		scoreTeamsMap.put(team, id);
		fh.appendUnlangScorerTeam(team, id);
	}

	public void appendCcasTeamTerms(int id, String team) throws IOException {
		ccasTeamsMap.put(id, team);
		fh.appendUnilangCcasTeam(id, team);
	}

	public void readScoreTeamTerms() throws JsonSyntaxException, IOException {
		fh.readUnilangAllScorerTeams();
	}

	public void readCcasTeamTerms() throws JsonSyntaxException, IOException {
		fh.readUnilangAllCcasTeams();
	}

	public int addTerm(String ccasTerm, String scoreTerm) throws IOException {
		/*
		 * as common id we use a random integer. to make it unique for booth
		 * maps we chose the biggest of the two map sizes
		 */

		int id = Math.max(ccasMap.size(), scoreMap.size());
		appendCcasTerms(id, ccasTerm);
		appendScoreTerms(scoreTerm, id);
		return id;

	}

	public int addTeam(String ccasTeam, String scoreTeam) throws IOException {
		/*
		 * as common id we use a random integer. to make it unique for booth
		 * maps we chose the biggest of the two map sizes
		 */

		logger.info("ccasTeamsize-: {}  scorerTeamSize-: {}",
				ccasTeamsMap.size(), scoreTeamsMap.size());
		int id = Math.max(ccasTeamsMap.size(), scoreTeamsMap.size());
		appendCcasTeamTerms(id, ccasTeam);
		appendScoreTeamTerms(scoreTeam, id);
		// ccasTeamsMap.put(id, ccasTeam);
		// scoreTeamsMap.put(scoreTeam, id);
		return id;

	}

	public void init() throws JsonSyntaxException, IOException {
		// init unilang structures
		readCcasTeamTerms();
		readCcasTerms();
		readScoreTeamTerms();
		readScoreTerms();

	}

	public void appendUnfoundTerms(String country, String comp) throws IOException {
//		int id = ccasMap.size();
		fh.appendUnfoundTerms(country, comp,000);
	}

}
