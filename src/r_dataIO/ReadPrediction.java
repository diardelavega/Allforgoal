package r_dataIO;

<<<<<<< HEAD
=======
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

>>>>>>> d89d2e0d5a3a27c888cf0d3be343a13bef471191
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
<<<<<<< HEAD

=======
	public static final Logger log = LoggerFactory
			.getLogger(ReadPrediction.class);

	// C:/BastData/WeekPredPoints/"country"/Eliteserien__112__Pred__2016-07-29
	private Map<Integer, List<BaseMatchLinePred>> dayMatchLinePred;

	public void prediction(List<Integer> compIds) throws IOException {
		/*
		 * create a map of <comp_Id, List<BaseMatchLinePred>> do be served to
		 * the client side
		 */
		String test = "test";
		String pred = "pred";
		CSVParser p;
		CCAllStruct ccs;
		dayMatchLinePred = new HashMap<>();
		// read the test file of that compid and get the team names
		for (int id : compIds) {
			int idx = CountryCompetition.idToIdx.get(id);
			ccs = CountryCompetition.ccasList.get(idx);
			p = parser(ccs.getCompId(), ccs.getCompetition(), ccs.getCountry(),
					test);
			List<BaseMatchLinePred> mlplist = new ArrayList<BaseMatchLinePred>();
			for (CSVRecord record : p) {
				BaseMatchLinePred mlp = new BaseMatchLinePred();
				mlp.setT1(record.get("t1"));
				mlp.setT2(record.get("t2"));
				mlplist.add(mlp);
			}
			p = parser(ccs.getCompId(), ccs.getCompetition(), ccs.getCountry(),
					pred);

			/*
			 * since the data table is switched rows/columns -> every record is
			 * all the points of every match for a particular atrribute such as
			 * _1, _x, _2 ... untill the two last rows which contain the points
			 * for totHt & totFt;
			 */
			CSVRecord rec1 = p.getRecords().get(0);
			CSVRecord recx = p.getRecords().get(1);
			CSVRecord rec2 = p.getRecords().get(2);
			CSVRecord reco = p.getRecords().get(3);
			CSVRecord recu = p.getRecords().get(4);
			CSVRecord recp1y = p.getRecords().get(5);
			CSVRecord recp1n = p.getRecords().get(6);
			CSVRecord recp2y = p.getRecords().get(7);
			CSVRecord recp2n = p.getRecords().get(8);
			CSVRecord rech = p.getRecords().get(9);
			CSVRecord recf = p.getRecords().get(10);

			for (int i = 0; i < mlplist.size(); i++) { // head 1
				mlplist.get(i).set_1(rec1.get(i));
			}
			for (int i = 0; i < mlplist.size(); i++) {// head x
				mlplist.get(i).set_x(recx.get(i));
			}
			for (int i = 0; i < mlplist.size(); i++) {// head 2
				mlplist.get(i).set_2(rec2.get(i));
			}
			for (int i = 0; i < mlplist.size(); i++) {// score o
				mlplist.get(i).set_o(reco.get(i));
			}
			for (int i = 0; i < mlplist.size(); i++) {// score u
				mlplist.get(i).set_u(recu.get(i));
			}
			for (int i = 0; i < mlplist.size(); i++) {// ht +1
				mlplist.get(i).setP1y(recp1y.get(i));
			}
			for (int i = 0; i < mlplist.size(); i++) {// ht -1
				mlplist.get(i).setP1n(recp1n.get(i));
			}
			for (int i = 0; i < mlplist.size(); i++) {// ht +2
				mlplist.get(i).setP2y(recp2y.get(i));
			}
			for (int i = 0; i < mlplist.size(); i++) {// ht -2
				mlplist.get(i).setP2n(recp2n.get(i));
			}
			for (int i = 0; i < mlplist.size(); i++) {// ht goals
				mlplist.get(i).setHt(rech.get(i));
			}
			for (int i = 0; i < mlplist.size(); i++) {// ft goals
				mlplist.get(i).setFt(recf.get(i));
			}

			dayMatchLinePred.put(id, mlplist);
		}// comp_id fors

	}

	private CSVParser parser(int compId, String compName, String country,
			String kind) {
		/* get the leatest (today or in future pred files and reads it) */
		CSVFormat format = CSVFormat.RFC4180;

		AnalyticFileHandler afh = new AnalyticFileHandler();
		CSVParser parser = null;
		try {
			// parser = new CSVParser(new FileReader(afh.getTrainFileName(89,
			// "J2_League", "Japan")), format);
			switch (kind) {
			case "pred":
				parser = new CSVParser(new FileReader(
						afh.getLeatestPredictionFileName(compId, compName,
								country)), format);
				break;
			case "test":
				parser = new CSVParser(new FileReader(
						afh.getLeatestTestFileName(compId, compName, country)),
						format);
				break;

			default:
				log.warn("No kind od file was switched");
				break;
			}

		} catch (IOException e) {
			log.warn("Parsing exception");
			e.printStackTrace();
		}
		log.info(parser.toString());
		return parser;
	}

	public List<String> getDominant(int compId) {
		/* to return the preveiling attribute in head,score,p1,p2,ht,ft */
		if (dayMatchLinePred == null) {
			log.warn("Not initiated prediction proces");
			return null;
		}
		if (dayMatchLinePred.size() == 0) {
			log.warn("No predictions found");
			return null;
		}
		if (dayMatchLinePred.get(compId) == null) {
			log.warn("No predictions found today for that competition");
			return null;
		}
		// else
		List<String> dominList = new ArrayList<String>();
		StringBuilder sb;
		for (BaseMatchLinePred mlp : dayMatchLinePred.get(compId)) {
			sb = new StringBuilder();
			float _1 = Float.parseFloat(mlp.get_1());
			float _x = Float.parseFloat(mlp.get_x());
			float _2 = Float.parseFloat(mlp.get_2());
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

			float _o = Float.parseFloat(mlp.get_o());
			float _u = Float.parseFloat(mlp.get_u());
			if (_o >= _u)
				sb.append("o,");
			else
				sb.append("u,");

			float _p1y = Float.parseFloat(mlp.getP1y());
			float _p1n = Float.parseFloat(mlp.getP1n());
			if (_p1y >= _p1n)
				sb.append("_p1y,");
			else
				sb.append("_p1n,");

			float _p2y = Float.parseFloat(mlp.getP2y());
			float _p2n = Float.parseFloat(mlp.getP2n());
			if (_p2y >= _p2n)
				sb.append("_p2y,");
			else
				sb.append("_p2n,");

			dominList.add(sb.toString());
		}
		return dominList;

	}
>>>>>>> d89d2e0d5a3a27c888cf0d3be343a13bef471191
}
