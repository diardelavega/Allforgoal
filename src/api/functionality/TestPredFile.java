package api.functionality;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import basicStruct.StrStrTuple;
import diskStore.AnalyticFileHandler;

public class TestPredFile implements CsvFileHandler {

	public static Logger log = LoggerFactory.getLogger(TestPredFile.class);
	
	public List<StrStrTuple> daylyAdversaries(int compId, String compName,
			String country) {
		/* collect and return the adversary teams playing today */
		CSVParser parser = parser(compId, compName, country);

		List<StrStrTuple> daylyMatchAdversaries = new ArrayList<>();
		StrStrTuple temp;
		for (CSVRecord record : parser) {
			temp = new StrStrTuple();
			log.info("{}  -  {}",record.get("t1"),record.get("t2"));
			temp.setS1(record.get("t1"));
			temp.setS2(record.get("t2"));
			daylyMatchAdversaries.add(temp);
		}
		return (daylyMatchAdversaries);
	}

	@Override
	public String fullCsv(int compId, String compName, String country) {
		CSVParser parser = parser(compId, compName, country);
		StringBuilder sb = new StringBuilder();

		for (CSVRecord record : parser) {
			sb.append(record.get("week") + "," + record.get("t1") + ","
					+ record.get("t1") + "," + (-1) + "," + (-1) + "," + (-1)
					+ "," + (-1) + "," + record.get("t1Form") + ","
					+ record.get("t2Form") + "," + record.get("t1Atack") + ","
					+ record.get("t2Atack") + "," + ","
					+ record.get("t1Defense") + "," + record.get("t2Defense")
					+ "," + record.get("t1AvgFtScore") + ","
					+ record.get("t2AvgFtScore") + "\n");
		}
		return sb.toString();
	}

	@Override
	public String reducedCsv(int compId, String compName, String country) {
		CSVParser parser = parser(compId, compName, country);
		StringBuilder sb = new StringBuilder();

		for (CSVRecord record : parser) {
			sb.append(record.get("week") + "," + record.get("t1") + ","
					+ record.get("t1") + "," + (-1) + "," + (-1) + ","
					+ record.get("t1Form") + "," + record.get("t2Form") + ","
					+ record.get("t1Atack") + "," + record.get("t2Atack") + ","
					+ "," + record.get("t1Defense") + ","
					+ record.get("t2Defense") + ","
					+ record.get("t1AvgFtScore") + ","
					+ record.get("t2AvgFtScore") + "\n");
		}
		return sb.toString();
	}

	private CSVParser parser(int compId, String compName, String country) {
		/* get the leatest (today or in future test pred files and reads it) */
		CSVFormat format = CSVFormat.RFC4180.withHeader();

		AnalyticFileHandler afh = new AnalyticFileHandler();
		CSVParser parser = null;
		try {
			// parser = new CSVParser(new FileReader(afh.getTrainFileName(89,
			// "J2_League", "Japan")), format);

			parser = new CSVParser(new FileReader(afh.getLeatestTestFileName(
					compId, compName, country)), format);
		} catch (IOException e) {
			log.warn("Parsing exception");
			e.printStackTrace();
		}
		log.info(parser.toString());
		return parser;
	}
}