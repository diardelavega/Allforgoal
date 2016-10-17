package api.rest;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import basicStruct.FullMatchLine;
import basicStruct.MatchObj;
import structures.TimeVariations;

/**
 * @author Administrator
 *
 *         HEPL CLASS TO fill with data simulating the match prediction line
 */
public class MPLFill {
	public static final Logger log = LoggerFactory.getLogger(MPLFill.class);
	private Random r = new Random();
	private boolean forFlag = true;
	// public List<FullMatchLine> fl = new ArrayList<>();
	// public List<MatchObj> ml= new ArrayList<>();

	private String[] silable = new String[] { "Aalesund", "Bodø / Glimt", "Brann", "Haugesund", "Lillestrøm", "Molde",
			"Odd", "Rosenborg", "Sarpsborg 08", "Sogndal", "Stabæk", "Start", "Strømsgodset", "Tromsø", "Valerenga",
			"Viking", };
	// { "lo", "bo", "ju", "ve", "ca", "li", "ve", "che", "la", "zio", "ro",
	// "ma",
	// "int", "er", "mi", "me", "ta", "ku", "zeo", "tako", "suli", "breg",
	// "whiskey", "tango", "foxtrot", "roky",
	// "sigma", "tera", "bravo", "union", "city", "new", "po", "rt", "ivo",
	// "Co", "ru", "re", "al", "ba", "atlet",
	// "ico", "se", "vi", "la" };

	public void fakeFiller() {
//		String t1, t2;
		FullMatchLine fml;
		MatchObj mobj;
		for (int n = 0; n < 100; n++) {// nr of matches
			// fml = null;
			String tim = timGen();
			LocalDate locd = datGen();
			if (forFlag) {
				locd = LocalDate.now().plusDays(1);
				mobj = new MatchObj(Long.parseLong(n + ""), 112, "Sarpsborg 08", "Start", ftGen(), ftGen(),
						htGen(), htGen(), oddGen(), oddGen(), oddGen(), oddGen(), oddGen(), LocalDate.now().plusDays(1), tim);
				forFlag=false;
			} else {
//				locd = datGen();
				mobj = new MatchObj(Long.parseLong(n + ""), cIdGen(), strGen(), strGen(), ftGen(), ftGen(), htGen(),
						htGen(), oddGen(), oddGen(), oddGen(), oddGen(), oddGen(), locd, tim);
			}

//			 log.info("localDate {}", locd);
			
			fml = new FullMatchLine(mobj);
			if (locd.isBefore(LocalDate.now()) || locd.isEqual(LocalDate.now())) {
				// fml = new FullMatchLine(mobj);
				fml.setH1(ppGen());
				fml.setHx(ppGen());
				fml.setH2(ppGen());
				fml.setSo(ppGen());
				fml.setSu(ppGen());
				fml.setP1n(ppGen());
				fml.setP1y(ppGen());
				fml.setP2n(ppGen());
				fml.setP2y(ppGen());
				fml.setHt(htGen());
				fml.setFt(ftGen());
			}
			// log.info("{}", fml.shower());

			if (TimeVariations.mapMPL.containsKey(fml.getLocalDat())) {
				if (TimeVariations.mapMPL.get(fml.getLocalDat()).containsKey(fml.getComId())) {
					TimeVariations.mapMPL.get(fml.getLocalDat()).get(fml.getComId()).add(fml);
				} else {
					List<FullMatchLine> fl = new ArrayList<>();
					fl.add(fml);
					TimeVariations.mapMPL.get(fml.getLocalDat()).put(fml.getComId(), fl);
				}
			} else {// doesnt contain the date
				Map<Integer, List<FullMatchLine>> fm = new HashMap<>();
				List<FullMatchLine> fl = new ArrayList<>();
				fl.add(fml);
				fm.put(fml.getComId(), fl);
				TimeVariations.mapMPL.put(fml.getLocalDat(), fm);
			}
		}
	}

	public void spiller() {
		// show what time mpl var has in it
		for (LocalDate datkey : TimeVariations.mapMPL.keySet()) {
			log.info("{}", datkey);
			for (int cidkey : TimeVariations.mapMPL.get(datkey).keySet()) {
				log.info("\t {}", cidkey);
				for (int i = 0; i < TimeVariations.mapMPL.get(datkey).get(cidkey).size(); i++) {
					log.info("\t\t {} ", TimeVariations.mapMPL.get(datkey).get(cidkey).get(i).shower());
				}
			}
		}
	}

	public String strGen() {
		// String str = "";
		// while (str.length() < 8) {
		// str += silable[r.nextInt(silable.length)];
		// }
		// return str;
		return silable[r.nextInt(silable.length)];
	}

	public int htGen() {
		return r.nextInt(5);
	}

	public int ftGen() {
		return r.nextInt(10);
	}

	public int cIdGen() {
		return r.nextInt(7);
	}

	public float oddGen() {
		return r.nextFloat() + r.nextInt(5) + 1;
	}

	public LocalDate datGen() {
		LocalDate ld;
		switch (r.nextInt(3)) {
		case 0:
			ld = LocalDate.now();
			break;
		case 1:
			ld = LocalDate.now().plusDays(1);
			break;
		case 2:
			ld = LocalDate.now().minusDays(1);
			break;
		default:
			ld = LocalDate.now();
			break;
		}
		return ld;
		// return Date.valueOf(ld);
	}

	public String timGen() {
		return (r.nextInt(23) + 1) + ":" + (r.nextInt(59) + 1);
	}

	public float ppGen() {
		return r.nextFloat() + r.nextInt(50);
	}

	public void rntest() {
		for (int i = 0; i < 10; i++) {
			log.warn("{}", timGen());
		}
	}

}
