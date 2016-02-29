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



}
