package r_dataIO;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import structures.CountryCompetition;
import sun.swing.StringUIClientPropertyKey;
import api.functionality.TestPredFile;
import api.functionality.obj.BaseMatchLinePred;
import basicStruct.CCAllStruct;
import diskStore.AnalyticFileHandler;


/**
 * @author Administrator
 *
 *         A class used to read the prediction points from the R prediction
 *         algorithms. it reads the points from a csv file where the columns
 *         represent the matches of the competition the day of the prediction
 *         and the lines represent the values as : **************************
 *         row1 - head1; row2 - headx, row3 - head2 **************************
 *         row4 - over, row5 - under, row6 - p1Y, row7 - p1N, ****************
 *         row8 - p2Y, row9 - p2N, row10 - totHt, row11 - totFt; *************
 *         Should contain functions to read, infere the most probable outcome
 *         vector for every match and somehow coordinate and facilitate the
 *         display or send data from the service class
 * 
 */
public class ReadPrediction {

	public static final Logger log = LoggerFactory
			.getLogger(ReadPrediction.class);

	// C:/BastData/WeekPredPoints/"country"/Eliteserien__112__Pred__2016-07-29
	private Map<Integer, List<BaseMatchLinePred>> matchLinePred = new HashMap<>();

	/**
	 * create a map of <comp_Id,List<BaseMatchLinePred>> do be served to the
	 * client side
	 */
	public void prediction(List<Integer> compIds) throws IOException {

		String test = "test";
		String pred = "pred";
		CSVParser p;
		CCAllStruct ccs;
		
		// read the test file of that compid and get the team names
		for (int cid : compIds) {
			int idx = CountryCompetition.idToIdx.get(cid);
			ccs = CountryCompetition.ccasList.get(idx);
			// read test file & get team names
			p = parser(ccs.getCompId(), ccs.getCompetition(), ccs.getCountry(), test);
			if (p == null)
				continue;

			List<BaseMatchLinePred> mlplist = new ArrayList<BaseMatchLinePred>();
			for (CSVRecord record : p) {
				BaseMatchLinePred mlp = new BaseMatchLinePred();
				mlp.setT1(record.get("t1"));
				mlp.setT2(record.get("t2"));
				mlplist.add(mlp);
			}
			// read r prediction file & assign outcomes to matches
			p = parser(ccs.getCompId(), ccs.getCompetition(), ccs.getCountry(), pred);
			if (p == null)
				continue;

			/*
			 * since the data table is switched rows/columns -> every record is
			 * all the points of every match for a particular atrribute such as
			 * _1, _x, _2 ... untill the twelve last rows which contain the
			 * points for 6-totHt & 6-totFt;
			 */
			List<CSVRecord> recs = p.getRecords();
			List<CSVRecord> smallrecslist;
			String header = "";
			CSVRecord rec1 = null, recx = null, rec2 = null, reco = null, recu = null, recY = null, recN = null, rec2Y = null, rec2N = null;
			List<Integer> htgoals = null;
			List<Integer> ftgoals = null;
			/*
			 * read the prediction file. Get the header and based on it fill the
			 * appropriate records.
			 */
			for (int j = 0; j < recs.size(); j++) {
				if (recs.get(j).get(0).startsWith("#")) {
					header = recs.get(j).get(0).split("#")[1];
					continue;
				}
				switch (header) {
				case "head":
					rec1 = recs.get(j);
					j++;
					recx = recs.get(j);
					j++;
					rec2 = recs.get(j);
					break;
				case "score":
					reco = recs.get(j);
					j++;
					recu = recs.get(j);
					break;
				case "p1":
					recY = recs.get(j);
					j++;
					recN = recs.get(j);
					break;
				case "p2":
					rec2Y = recs.get(j);
					j++;
					rec2N = recs.get(j);
					break;
				case "ht":
					smallrecslist = new ArrayList<>();
					for (int w = 0; w < 6; w++) {
						smallrecslist.add(recs.get(w));
					}
					htgoals = maxOfRecs(smallrecslist);
					break;
				case "ft":
					smallrecslist = new ArrayList<>();
					for (int w = 0; w < 6; w++) {
						smallrecslist.add(recs.get(w));
					}
					ftgoals = maxOfRecs(smallrecslist);
					break;
				default:
					log.warn("TDEFAULT in read prediction SWITCH; THIS SHULD NOT BE SHOWING ");
					break;
				}
				// }
			}
			// Fill the BaseMatchPredLine list with the valid records
			for (int k = 0; k < mlplist.size(); k++) { // head 1
				if (recY != null) {
					mlplist.get(k).setP1y(Float.parseFloat(recY.get(k)));
					mlplist.get(k).setP1n(Float.parseFloat(recN.get(k)));
				}
				if (rec2Y != null) {
					mlplist.get(k).setP2y(Float.parseFloat(rec2Y.get(k)));
					mlplist.get(k).setP2n(Float.parseFloat(rec2N.get(k)));
				}
				if (reco != null) {
					mlplist.get(k).setSo(Float.parseFloat(reco.get(k)));
					mlplist.get(k).setSu(Float.parseFloat(recu.get(k)));
				}
				if (rec1 != null) {
					mlplist.get(k).setH1(Float.parseFloat(rec1.get(k)));
					mlplist.get(k).setHx(Float.parseFloat(recx.get(k)));
					mlplist.get(k).setH2(Float.parseFloat(rec2.get(k)));
				}
				if (ftgoals != null) {
					mlplist.get(k).setFt(ftgoals.get(k));
				}
				if (htgoals != null) {
					mlplist.get(k).setHt(htgoals.get(k));
				}
			}

			matchLinePred.put(cid, mlplist);
		} // comp_id fors

	}

	private List<Integer> maxOfRecs(List<CSVRecord> smallrecslist) {
		// get the index of the max value for each of the matches

		List<Integer> preobGoals = new ArrayList<>();

		// iterate the words (csv words)of a record
		for (int j = 0; j < smallrecslist.get(0).size(); j++) {
			float max = Float.parseFloat(smallrecslist.get(0).get(j));
			int maxPos = 0;
			// iterate the lines of the array
			for (int i = 1; i < smallrecslist.size(); i++) {
				float curent = Float.parseFloat(smallrecslist.get(i).get(j));
				if (curent > max) {
					max = curent;
					maxPos = i;
				}
			}
			preobGoals.add(maxPos);
		}

		return preobGoals;
	}

	private CSVParser parser(int compId, String compName, String country,
			String kind) {
		/* get the leatest (today or in future pred files and reads it) */
		CSVFormat format = null;
		CSVFormat.RFC4180.withHeader();

		AnalyticFileHandler afh = new AnalyticFileHandler();
		CSVParser parser = null;
		try {
			// parser = new CSVParser(new FileReader(afh.getTrainFileName(89,
			// "J2_League", "Japan")), format);
			switch (kind) {
			case "pred":
				// prediction file doesnt have a header
				format = CSVFormat.RFC4180;
				File f = afh.getLeatestRPredictionFileName(compId, compName, country);
				if (f != null) {
					parser = new CSVParser(new FileReader(f), format);
				}else{
				log.warn("NO PREDICTION FILE  for {}, {}, {}", compId, compName, country);
				parser = null;
				}
				break;
			case "test":
				format = CSVFormat.RFC4180.withHeader();
				 f = afh.getLeatestTestFileName(compId, compName, country);
				if (f != null) {
					parser = new CSVParser(new FileReader(f), format);
				}else{
				log.warn("NO PREDICTION FILE  for {}, {}, {}", compId, compName, country);
				parser = null;
				}
				break;

			default:
				log.warn("No kind od file was switched");
				break;
			}

		} catch (IOException e) {
			log.warn("Parsing exception");
			e.printStackTrace();
		}
//		log.info(parser.toString());
		return parser;
	}

	public List<String> getDominant(int compId) {
		/* to return the prevailing attribute in head,score,p1,p2,ht,ft */
		if (matchLinePred == null) {
			log.warn("Not initiated prediction proces");
			return null;
		}
		if (matchLinePred.size() == 0) {
			log.warn("No predictions found");
			return null;
		}
		if (matchLinePred.get(compId) == null) {
			log.warn("No predictions found today for that competition");
			return null;
		}
		// else
		List<String> dominList = new ArrayList<String>();
		StringBuilder sb;
		for (BaseMatchLinePred mlp : matchLinePred.get(compId)) {
			sb = new StringBuilder();
			float _1 = mlp.getH1();
			float _x = mlp.getHx();
			float _2 = mlp.getH2();
			if (_1 >= _x) {
				if (_1 >= _2) {
					sb.append("1,");
				} else {
					sb.append("2,");
				}
			} else {
				if (_x >= _2) {
					sb.append("x,");
				} else {
					sb.append("2,");
				}
			}

			float _o = mlp.getSo();
			float _u = mlp.getSu();
			if (_o >= _u)
				sb.append("o,");
			else
				sb.append("u,");

			float _p1y = mlp.getP1y();
			float _p1n = mlp.getP1n();
			if (_p1y >= _p1n)
				sb.append("_p1y,");
			else
				sb.append("_p1n,");

			float _p2y = mlp.getP2y();
			float _p2n = mlp.getP2n();
			if (_p2y >= _p2n)
				sb.append("_p2y,");
			else
				sb.append("_p2n,");

			dominList.add(sb.toString());
		}
		return dominList;

	}


	public void htftMostPoint() {

	}

	public Map<Integer, List<BaseMatchLinePred>> getMatchLinePred() {
		return matchLinePred;
	}

	public void setDayMatchLinePred(
			Map<Integer, List<BaseMatchLinePred>> dayMatchLinePred) {
		this.matchLinePred = dayMatchLinePred;
	}


}
