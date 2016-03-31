package extra;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StringSimilarity {

	public static final Logger logger = LoggerFactory
			.getLogger(StringSimilarity.class);

	public static int levPerWord(String a, String b) {
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

	public static int levenshteinDistance(String a, String b) {
		// izraeli teams area all macabi and hapoel ... return a list of
		// distances

		a = a.replaceAll("\u00A0", "").toLowerCase();
		b = b.replaceAll("\u00A0", "").toLowerCase();

		String[] aa = a.split(" ");
		String[] bb = b.split(" ");
		String[] cc = null;
		if (bb.length < aa.length) {
			// longest on top
			cc = aa;
			aa = bb;
			bb = cc;
		}

		int distance = 0, curdist = 0, totdist = 0;
		int k = 0;
		int simNumbers = 0;// in case they have numbers in comon
		Integer af = null, bf = null;

		/*
		 * for every word of aa compare it with all words from bb. If one of
		 * them curdist<2 -> equal; then dismis all distances else add them to
		 * the total. Set the found word to null so that it will not be counted
		 * twice.**************************************************************
		 * atention should be payed to nrs with digits s<= (dismis word length
		 * {2 letters}). If they are present they are stored and compared
		 */
		// boolean aflag = false, bflag = false;
		for (int i = 0; i < aa.length; i++) {
			// logger.info("'{}'",aa[i]);
			if (aa[i].length() <= StandartResponses.DISMIS_WORD) {
				continue;
			}
			for (int j = 0; j < bb.length; j++) {
				// logger.info("'{}'",bb[j]);
				if (bb[j] != null)
					if (bb[j].length() <= StandartResponses.DISMIS_WORD) {
						continue;
					} else {
						curdist = levPerWord(aa[i], bb[j]);
						if (curdist < StandartResponses.LEV_DISTANCE) {
							k++;
							bb[j] = null;
							distance = 0;
							// break;
						}
						distance += curdist;
					}
			}// for j
			totdist += distance;
		}

		for (int i = 0; i < aa.length; i++) {
			if (aa[i].length() <= StandartResponses.DISMIS_WORD) {
				af = dismisCheck(aa[i]);
			} else {
				continue;
			}
			for (int j = 0; j < bb.length; j++) {
				if (bb[j] != null)
					if (bb[j].length() <= StandartResponses.DISMIS_WORD) {
						bf = dismisCheck(bb[j]);
						try {
							if (af != null && null != bf) {
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
							// e.printStackTrace();
						}
					}
			}
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
		Integer af = null;
		s = s.replace(".", "");
		// logger.info("--------------:special {}",s);
		try {
			af = Integer.parseInt(s);
		} catch (Exception e) {
			// logger.warn("Not a valid string {}", s);
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
}
