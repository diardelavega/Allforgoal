package api.functionality;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import api.functionality.obj.CountryCompCompId;
import diskStore.AnalyticFileHandler;

/**
 * @author Administrator
 * 
 *         reads the train csv file and gets the attributes it needs. With those
 *         forms another Csv~Text that cn be send to the client side.
 *
 */
public class TrainPredFile implements CsvFileHandler {

	public static final Logger log = LoggerFactory.getLogger(TrainPredFile.class);
	private int lastRecWeek;
	private int linesRead = 0;
	private String last10 = "";
	private int t1o = 0;
	private int t1u = 0;
	private int t1gg = 0;
	private int t2o = 0;
	private int t2u = 0;
	private int t2gg = 0;

	public String fullCsv(CountryCompCompId ccci) {
		return fullCsv(ccci.getCompId(), ccci.getCompetition(), ccci.getCountry());
	}

	public String fullCsv(int compId, String compName, String country, String homeTeam, String awayTeam) {
		/*
		 * TODO read the competition train .csv file. Get only the required
		 * information. store it in (a few posibilities: plain text, json, java
		 * list with jsp on client side etc.). Send the resu;t to the api
		 * function
		 */

		CSVParser parser = parser(compId, compName, country);
		StringBuilder sb = new StringBuilder();
		for (CSVRecord record : parser) {
			if (record.get("t1").equals(homeTeam) || record.get("t2").equals(homeTeam)
					|| record.get("t1").equals(awayTeam) || record.get("t2").equals(awayTeam)) {
				linesRead++;

				if (record.get("t1").equals(homeTeam) || record.get("t2").equals(homeTeam)) {// increment t1 atts 
					if (record.get("scoreOutcome").equals("O"))
						t1o++;
					else
						t1u++;
					if (record.get("scoreOutcome").equals("Y"))
						t1gg++;
				}
				if (record.get("t1").equals(awayTeam) || record.get("t2").equals(awayTeam)) {// increment t2 atts 
					if (record.get("scoreOutcome").equals("O"))
						t2o++;
					else
						t2u++;
					if (record.get("scoreOutcome").equals("Y"))
						t2gg++;
				}

				String line = record.get("week") + "," + record.get("t1") + "," + record.get("t2") + ","
						+ record.get("t1Ht") + "," + record.get("t2Ht") + "," + record.get("t1Ft") + ","
						+ record.get("t2Ft") + "," + record.get("t1Form") + "," + record.get("t2Form") + ","
						+ record.get("t1AtackIn") + "," + record.get("t1AtackOut") + "," + record.get("t2AtackIn") + ","
						+ record.get("t2AtackOut") + "," + record.get("t1DefenseIn") + "," + record.get("t1DefenseOut")
						+ "," + record.get("t2DefenseIn") + "," + record.get("t2DefenseOut") + "\n";
				sb.append(line);
				// commonAdvLines(line, record.get("week"));
			}
		}
		return sb.toString();
	}

	public String reducedCsv(CountryCompCompId ccci) {
		return reducedCsv(ccci.getCompId(), ccci.getCompetition(), ccci.getCountry());
	}

	@Override
	public String reducedCsv(int compId, String compName, String country) {
		CSVParser parser = parser(compId, compName, country);
		if (parser == null)
			return null;
		StringBuilder sb = new StringBuilder();
		for (CSVRecord record : parser) {
			linesRead++;
			String line = record.get("week") + "," + record.get("t1") + "," + record.get("t2") + ","
					+ record.get("t1Ft") + "," + record.get("t2Ft") + "," + record.get("t1Form") + ","
					+ record.get("t2Form") + "," + record.get("t1Atack") + "," + record.get("t2Atack") + ","
					+ record.get("t1Defense") + "," + record.get("t2Defense") + "\n";
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
			File f = afh.getTrainFileName(compId, compName, country);
			if (f != null)
				parser = new CSVParser(new FileReader(f), format);
			log.info("file {} {} not found", compName, country);
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

	public int getLinesRead() {
		return linesRead;
	}

	@Override
	public String reducedCsv(int compId, String compName, String country, LocalDate ld) {
		return null;
	}

	@Override
	public String fullCsv(int compId, String compName, String country) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String fullCsv(int compId, String compName, String country, LocalDate ld, String t1, String t2) {
		// TODO Auto-generated method stub
		return null;
	}

	public int getT1o() {
		return t1o;
	}

	public void setT1o(int t1o) {
		this.t1o = t1o;
	}

	public int getT1u() {
		return t1u;
	}

	public void setT1u(int t1u) {
		this.t1u = t1u;
	}

	public int getT1gg() {
		return t1gg;
	}

	public void setT1gg(int t1gg) {
		this.t1gg = t1gg;
	}

	public int getT2o() {
		return t2o;
	}

	public void setT2o(int t2o) {
		this.t2o = t2o;
	}

	public int getT2u() {
		return t2u;
	}

	public void setT2u(int t2u) {
		this.t2u = t2u;
	}

	public int getT2gg() {
		return t2gg;
	}

	public void setT2gg(int t2gg) {
		this.t2gg = t2gg;
	}

	public void setLinesRead(int linesRead) {
		this.linesRead = linesRead;
	}

	
}
