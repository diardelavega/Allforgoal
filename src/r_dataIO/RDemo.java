package r_dataIO;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import demo.Demo;
import diskStore.AnalyticFileHandler;
import extra.AttsKind;

public class RDemo {
	public static final Logger log = LoggerFactory.getLogger(RDemo.class);

	public static void main(String[] args) throws SQLException, IOException {
		Demo.initCCAllStruct();
//		RHandler rh = new RHandler();
//		rh.predictOne(112);
		// rh.testRcall();

		// rh.predictOne(157);
		 simDtf_Create();
//		sim_readPrediction();
	}

	public static void testReadPred() {
		// get a nonconventional csv file with parser
		File file = new File("C:/ff1/fil");

		CSVFormat format = CSVFormat.RFC4180;

		CSVParser parser = null;
		try {
			parser = new CSVParser(new FileReader(file), format);

			// System.out.println(parser .getCurrentLineNumber());
			// System.out.println(parser .getRecordNumber());
			// System.out.println(parser .getRecords().size());
			// System.out.println(parser .getRecordNumber());
			System.out.println("----------------------");
			for (CSVRecord rec : parser.getRecords()) {
				System.out.println(parser.getCurrentLineNumber());
				if (rec.get(0).startsWith("#")) {
					System.out.println("header " + rec.get(0).split("#")[1]);
				} else {
					System.out.println("rec " + rec.get(1) + " " + rec.get(2)
							+ " " + rec.get(3));
					System.out.println(parser.getRecords().get(3));
				}
			}
			// print results
		} catch (Exception e) {
		}
	}

	public static void simDtf_Create() {
		String trpath = "C:/BastData/Pred/Data/Norway/Eliteserien__112__Data";
//		List<String> tempList = new ArrayList<String>();
//		tempList.add(trpath);
		List<Integer> tempList = new ArrayList<Integer>();
		tempList.add(112);
		tempList.add(164);
		tempList.add(165);
		tempList.add(157);
		tempList.add(46);
		tempList.add(4);
		RHandler rh = new RHandler();
		rh.Rcall_DTF(tempList, AttsKind.hs);
	}

	public static void sim_readPrediction() {
		ReadPrediction rp = new ReadPrediction();
		List<Integer> prefileid = new ArrayList<Integer>();
		prefileid.add(112);
		try {
			rp.prediction(prefileid);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (Integer key : rp.getMatchLinePred().keySet())
			for (int i = 0; i < rp.getMatchLinePred().get(key).size(); i++) {
				log.info("{}", rp.getMatchLinePred().get(key).get(i).liner());
			}
	}
}
