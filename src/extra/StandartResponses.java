package extra;

public class StandartResponses {
	public static String NOT_FOUND = "NOT_FOUND";
	
	public static int STD_DAYS_AGO = 15;// standard days ago

	public static float SMALL_LEV_DISTANCE = 1;
	public static float LEV_DISTANCE = 2;
	public static float DISMIS_WORD = 2;
	public static float TEAM_DIST = 3;

	public static int DEFAULT_NULL = -1;
	public static int ALL_MATCHES_TAKEN = -2;

	public static int SMALL_WORD = 3;
	public static int BIG_WORD = 4;

	/** max nr of total matches (even from different competition) in a pack */
	public static int MPL_PACK_SIZE = 10;

	public static float RELATIVE_TEAM_DIST(String team) {
		int len = team.length();
		if (len > 8 && len < 12) {
			return len / 3;
		}
		if (len > 12 && len < 18) {
			return len / 4;
		}
		if (len > 18 && len < 25) {
			return len / 5;
		}
		return TEAM_DIST;
	};
}
