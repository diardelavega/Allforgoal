package extra;

public class StringSimilarity {

	private static int levPerWord(String a, String b) {

		// i == 0
		int[] costs = new int[b.length() + 1];
		for (int j = 0; j < costs.length; j++)
			costs[j] = j;
		for (int i = 1; i <= a.length(); i++) {
			// j == 0; nw = lev(i - 1, j)
			costs[0] = i;
			int nw = i - 1;
			for (int j = 1; j <= b.length(); j++) {
				int cj = Math.min(1 + Math.min(costs[j], costs[j - 1]),
						a.charAt(i - 1) == b.charAt(j - 1) ? nw : nw + 1);
				nw = costs[j];
				costs[j] = cj;
			}
		}
		return costs[b.length()];
	}

	public static int levenshteinDistance(String a, String b) {
		// izraeli teams area all macabi and hapoel ... return a list of
		// distances

		a = a.toLowerCase();
		b = b.toLowerCase();

		String[] aa = a.split(" ");
		String[] bb = b.split(" ");
		int distance = 100;
		int i = 0, j = 0;

		for (String sa : aa) {
			if (sa.length() > 3) {
				i++;
				for (String sb : bb) {
					if (sb.length() > 3) {
						j++;
						distance += levPerWord(sa, sb);
					}
				}
			}
		}
		// total errors divided by sum of +3 letter words
		return distance / (i + j);
	}

	// an option for searching terms with more than one word
	// compare only the more near length word ??
	// cartesian compare of the terms ???

	// public static void main(String [] args) {
	// String [] data = { "kitten", "sitting", "saturday", "sunday",
	// "rosettacode", "raisethysword" };
	// for (int i = 0; i < data.length; i += 2)
	// System.out.println("distance(" + data[i] + ", " + data[i+1] + ") = " +
	// distance(data[i], data[i+1]));
	// }

}
