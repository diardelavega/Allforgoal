package extra;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StringSimilarity {

	public static final Logger logger = LoggerFactory
			.getLogger(StringSimilarity.class);

	public static int levenshteinPerWord(String a, String b) {
		// a = a.toUpperCase();
		// b = b.toUpperCase();
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

	public static float levenshteinDistance(String a, String b) {
		// izraeli teams area all macabi and hapoel ... return a list of
		// distances

		// TODO separate the coum& country distance from the TEAMS distance

		a = a.replaceAll("\u00A0", "").toLowerCase();
		b = b.replaceAll("\u00A0", "").toLowerCase();

		String[] aa = a.split(" ");
		String[] bb = b.split(" ");

		int distance = 0, curdist = 0, totdist = 0;
		int k = 0;// true simmilar words counter
		int simNumbers = 0;// in case they have numbers(in the name ) in comon
		Integer af = null, bf = null;

		/*
		 * for every word of aa compare it with all words from bb. If one of
		 * them curdist<2 -> equal; then dismis all distances else add them to
		 * the total. Set the found word to null so that it will not be counted
		 * twice.**************************************************************
		 * atention should be payed to nrs with digits s<= (dismis word length
		 * {2 letters}). If they are present they are stored and compared
		 */
		for (int i = 0; i < aa.length; i++) {
			// logger.info("'{}'",aa[i]);
			if (aa[i].length() <= StandartResponses.DISMIS_WORD) {
				continue;
			}
			for (int j = 0; j < bb.length; j++) {
				// logger.info("'{}'",bb[j]);
				if (bb[j] != null) {
					if (bb[j].length() <= StandartResponses.DISMIS_WORD) {
						continue;
					} else {
						curdist = levenshteinPerWord(aa[i], bb[j]);
						if (curdist < StandartResponses.LEV_DISTANCE) {
							k++;
							bb[j] = null;
							aa[i] = null;
							// distance = 0;
							break;
						}
						distance += curdist;
					}
				}
			}// for j
			totdist += distance;
		}

		/*
		 * check the small parts of the words 2letters, if they describe a key
		 * factor uch as the category
		 */
		for (int i = 0; i < aa.length; i++) {
			if (aa[i] != null) {
				if (aa[i].length() <= StandartResponses.DISMIS_WORD) {
					af = dismisCheck(aa[i]);
				} else {
					continue;
				}
			}
			for (int j = 0; j < bb.length; j++) {
				if (bb[j] != null)
					if (bb[j].length() <= StandartResponses.DISMIS_WORD) {
						bf = dismisCheck(bb[j]);
						try {
							if (af != null && null != bf) {
								// if those small pieces ase simmilar => very
								// good else very bad; we can't be sure they
								// mean something quite different like category
								// 1 ve category 2
								// but that is what we assume
								if (af == bf) {
									bb[j] = null;
									aa[i] = null;
									simNumbers++;
									break;
								} else {
									simNumbers -= 2;
								}
							}
						} catch (Exception e) {
						}
					}
			}
		}

		// all the unrelated words increase the distance
		for (int i = 0; i < aa.length; i++) {
			if (aa[i] != null)
				totdist++;
		}
		for (int i = 0; i < bb.length; i++) {
			if (bb[i] != null)
				totdist++;
		}

		// make usage of similar or diverse numbers
		if (simNumbers < 0) {
			totdist += 20;
		}
		if (simNumbers > 0) {
			k += 1;
		}

		// total errors divided by sum of +3 letter words that are <2 simmilar
		if (k < 1) {
			return totdist + StandartResponses.LEV_DISTANCE;
		} else {
			return (totdist / (2 * (k + 1)));
		}
	}

	private static Integer dismisCheck(String s) {
		/* corelate numbers with letters or so i.e. 1 category - a gategory ... */
		Integer af = null;
		s = s.replace(".", "");
		// logger.info("--------------:special {}",s);
		try {
			af = Integer.parseInt(s);
		} catch (Exception e) {
			// logger.warn("Not a valid string {}", s);
		}
		if (s.contains("a")) {
			af = 1;
		}
		if (s.contains("b")) {
			af = 2;
		}
		if (s.contains("c")) {
			af = 3;
		}
		if (s.contains("i")) {
			af = 1;
		}
		if (s.contains("ii")) {
			af = 2;
		}
		if (s.contains("iii")) {
			af = 3;
		}
		if (s.equals("v")) {
			af = 5;
		}
		return af;
	}

	public static float teamSimilarity(String a, String b) {
		float smallMiss = 0;
		float smallHit = 0;
		float bigHit = 0;

		a = a.replaceAll("\u00A0", "").toLowerCase();
		b = b.replaceAll("\u00A0", "").toLowerCase();

		String[] aa = a.split(" ");
		String[] bb = b.split(" ");

		// to see if the next match will be after i>previ & j >prevj|| meaning
		// we have the order of the words aswell as the match
		int previ = 100, prevj = 100;
		int allALength = 0; // measure the length of all the words of aa
		int allBLength = 0;

		// if they start with small owrds supose AC,FC, BFC, AS, ...
		if (aa[0].length() <= StandartResponses.SMALL_WORD
				&& bb[0].length() <= StandartResponses.SMALL_WORD) {
			if (aa[0].equalsIgnoreCase(bb[0])) {
				smallHit++;
				allALength += aa[0].length();
				allBLength += bb[0].length();
				aa[0] = null;
				bb[0] = null;
				previ = 0;
				prevj = 0;
			} else {
				// if they both start with small word but is different, consider
				// it a miss
				smallMiss++;
			}
		}// if 0 smallword

		int tempDistance = 0;
//		int inLoopDistance = 0;
		int totalDistance = 0;
		boolean orderflag = false;
		

		// big word similar count
		for (int i = 0; i < aa.length; i++) {
			if (aa[i] == null) {
				continue;
			}
			allALength += aa[i].length();
			for (int j = 0; j < bb.length; j++) {
//				logger.info("{}  {}",aa[i],bb[j]);
				orderflag = false;
				if (bb[j] == null) {
					continue;
				}
					tempDistance = levenshteinPerWord(aa[i], bb[j]);
					if (tempDistance <= StandartResponses.LEV_DISTANCE) {
						if (i > previ && j > prevj) { // we have order
							orderflag = true;
						}
						if (aa[i].length() <= StandartResponses.SMALL_WORD || bb[j].length() <= StandartResponses.SMALL_WORD) {
							smallHit++;
							if (orderflag)
								smallHit += 1;// small words may be more common
						} else {// meaning is big word
							bigHit++;
							if (orderflag)// bonus points for order
								bigHit += 2;// big word is more important
						}
						allBLength += bb[j].length();
						aa[i] = null;
						bb[j] = null;
						prevj = j;
						previ = i;
						
						break;
					}// if lev distance
					totalDistance += tempDistance;
//				}// b != null
			}// for j
//			totalDistance += inLoopDistance;
		}// for i

		for (int j = 0; j < bb.length; j++) {
			if(bb[j]!=null)
			allBLength += bb[j].length();
		}

		float hits = (float)(bigHit * 2 + smallHit) / (aa.length + bb.length);
		float dist = (float)totalDistance / (allALength + allBLength) + smallMiss;
		return totalDistance + smallMiss - hits;
	}

	// ----------Alternatives
	public static double diceCoefficient(String s1, String s2) {
		Set<String> nx = new HashSet<String>();
		Set<String> ny = new HashSet<String>();

		for (int i = 0; i < s1.length() - 1; i++) {
			char x1 = s1.charAt(i);
			char x2 = s1.charAt(i + 1);
			String tmp = "" + x1 + x2;
			nx.add(tmp);
		}
		for (int j = 0; j < s2.length() - 1; j++) {
			char y1 = s2.charAt(j);
			char y2 = s2.charAt(j + 1);
			String tmp = "" + y1 + y2;
			ny.add(tmp);
		}

		Set<String> intersection = new HashSet<String>(nx);
		intersection.retainAll(ny);
		double totcombigrams = intersection.size();

		return (2 * totcombigrams) / (nx.size() + ny.size());
	}

	/**
	 * Here's an optimized version of the dice coefficient calculation. It takes
	 * advantage of the fact that a bigram of 2 chars can be stored in 1 int,
	 * and applies a matching algorithm of O(n*log(n)) instead of O(n*n).
	 * 
	 * <p>
	 * Note that, at the time of writing, this implementation differs from the
	 * other implementations on this page. Where the other algorithms
	 * incorrectly store the generated bigrams in a set (discarding duplicates),
	 * this implementation actually treats multiple occurrences of a bigram as
	 * unique. The correctness of this behavior is most easily seen when getting
	 * the similarity between "GG" and "GGGGGGGG", which should obviously not be
	 * 1.
	 * 
	 * @param s
	 *            The first string
	 * @param t
	 *            The second String
	 * @return The dice coefficient between the two input strings. Returns 0 if
	 *         one or both of the strings are {@code null}. Also returns 0 if
	 *         one or both of the strings contain less than 2 characters and are
	 *         not equal.
	 * @author Jelle Fresen
	 */
	public static double diceCoefficientOptimized(String s, String t) {
		// Verifying the input:
		if (s == null || t == null)
			return 0;
		// Quick check to catch identical objects:
		if (s == t)
			return 1;
		// avoid exception for single character searches
		if (s.length() < 2 || t.length() < 2)
			return 0;

		// Create the bigrams for string s:
		final int n = s.length() - 1;
		final int[] sPairs = new int[n];
		for (int i = 0; i <= n; i++)
			if (i == 0)
				sPairs[i] = s.charAt(i) << 16;
			else if (i == n)
				sPairs[i - 1] |= s.charAt(i);
			else
				sPairs[i] = (sPairs[i - 1] |= s.charAt(i)) << 16;

		// Create the bigrams for string t:
		final int m = t.length() - 1;
		final int[] tPairs = new int[m];
		for (int i = 0; i <= m; i++)
			if (i == 0)
				tPairs[i] = t.charAt(i) << 16;
			else if (i == m)
				tPairs[i - 1] |= t.charAt(i);
			else
				tPairs[i] = (tPairs[i - 1] |= t.charAt(i)) << 16;

		// Sort the bigram lists:
		Arrays.sort(sPairs);
		Arrays.sort(tPairs);

		// Count the matches:
		int matches = 0, i = 0, j = 0;
		while (i < n && j < m) {
			if (sPairs[i] == tPairs[j]) {
				matches += 2;
				i++;
				j++;
			} else if (sPairs[i] < tPairs[j])
				i++;
			else
				j++;
		}
		return (double) matches / (n + m);
	}

}
