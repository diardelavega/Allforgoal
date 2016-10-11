package api.functionality;

import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import api.functionality.obj.CountryCompCompId;
import api.functionality.CsvFileHandler;
import diskStore.AnalyticFileHandler;

/**
 * @author Administrator
 * 
 *         reads the train csv file and gets the attributes it needs. With those
 *         forms another Csv~Text that cn be send to the client side.
 *
 */
public class TrainPredFile implements CsvFileHandler {

	private int lastRecWeek;
	private String last10="";

	public String fullCsv(CountryCompCompId ccci) {
		return fullCsv(ccci.getCompId(), ccci.getCompetition(),
				ccci.getCountry());
	}

	public String fullCsv(int compId, String compName, String country) {
		/*
		 * TODO read the competition train .csv file. Get only the required
		 * information. store it in (a few posibilities: plain text, json, java
		 * list with jsp on client side etc.). Send the resu;t to the api
		 * function
		 */

		CSVParser parser = parser(compId, compName, country);
		StringBuilder sb = new StringBuilder();
		for (CSVRecord record : parser) {
			String line = record.get("week") + "," + record.get("t1") + ","
					+ record.get("t2") + "," + record.get("t1Ht") + ","
					+ record.get("t2Ht") + "," + record.get("t1Ft") + ","
					+ record.get("t2Ft") + "," + record.get("t1Form") + ","
					+ record.get("t2Form") + "," + record.get("t1AtackIn")
					+ "," + record.get("t1AtackOut") + ","
					+ record.get("t2AtackIn") + "," + record.get("t2AtackOut")
					+ "," + record.get("t1DefenseIn") + ","
					+ record.get("t1DefenseOut") + ","
					+ record.get("t2DefenseIn") + ","
					+ record.get("t2DefenseOut") + ","
					+ record.get("t1AvgFtScoreIn") + ","
					+ record.get("t1AvgFtScoreOut") + ","
					+ record.get("t2AvgFtScoreIn") + ","
					+ record.get("t2AvgFtScoreOut") + "\n";
			sb.append(line);
			commonAdvLines(line, record.get("week"));
		}
		return sb.toString();
	}

	public String reducedCsv(CountryCompCompId ccci) {
		return reducedCsv(ccci.getCompId(), ccci.getCompetition(),
				ccci.getCountry());
	}

	@Override
	public String reducedCsv(int compId, String compName, String country) {
		CSVParser parser = parser(compId, compName, country);
		StringBuilder sb = new StringBuilder();
		for (CSVRecord record : parser) {
			String line = record.get("week") + "," + record.get("t1") + ","
					+ record.get("t2") + "," + record.get("t1Ft") + ","
					+ record.get("t2Ft") + "," + record.get("t1Form") + ","
					+ record.get("t2Form") + "," + record.get("t1Atack") + ","
					+ record.get("t2Atack") + "," + record.get("t1Defense")
					+ "," + record.get("t2Defense") + "\n";
			sb.append(line);
			commonAdvLines(line, record.get("week"));
		}
		return sb.toString();
	}

	private void commonAdvLines(String line, String week) {
		int observedWeek = Integer.parseInt(week);
		if (lastRecWeek - observedWeek <= 10) {
			last10 += line;
		}
	}

	private CSVParser parser(int compId, String compName, String country) {
		CSVFormat format = CSVFormat.RFC4180.withHeader();

		AnalyticFileHandler afh = new AnalyticFileHandler();
		CSVParser parser = null;
		try {
			// parser = new CSVParser(new FileReader(afh.getTrainFileName(89,
			// "J2_League", "Japan")), format);

			parser = new CSVParser(new FileReader(afh.getTrainFileName(compId,
					compName, country)), format);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return parser;
	}

	public int getLastRecWeek() {
		return lastRecWeek;
	}

	public void setLastRecWeek(int lastRecWeek) {
		this.lastRecWeek = lastRecWeek;
	}

	public String getLast10() {
		return last10;
	}

	public void setLast10(String last10) {
		this.last10 = last10;
	}

	
}
