package api.functionality;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import structures.PredictionFile;
import api.functionality.obj.CountryCompCompId;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import diskStore.AnalyticFileHandler;

public class TestApachCsvParser {
	public static Logger log = LoggerFactory
			.getLogger(TestApachCsvParser.class);

	public static void main(String[] args) throws FileNotFoundException,
			IOException, SQLException {
		matchPredWithWDL();
	}

	public void parseNPrint() throws FileNotFoundException, IOException {
		CSVFormat format = CSVFormat.RFC4180.withHeader().withDelimiter(',');

		AnalyticFileHandler afh = new AnalyticFileHandler();
		CSVParser parser = new CSVParser(new FileReader(afh.getTrainFileName(
				89, "J2_League", "Japan")), format);

		List<PredictionFile> emps = new ArrayList<PredictionFile>();
		// StringBuilder sb = new StringBuilder();
		// sb.append(record.get("t1")+","+record.get("t2"));
		// System.out.println(sb);
		for (CSVRecord record : parser) {

			PredictionFile pf = new PredictionFile();
			pf.setT1(record.get("t1"));
			pf.setT2(record.get("t2"));

			emps.add(pf);
		}

		// close the parser
		parser.close();

		// System.out.println(emps);

		// CSV Write Example using CSVPrinter
		CSVPrinter printer = new CSVPrinter(System.out,
				format.withDelimiter(';'));
		System.out.println("********");
		printer.printRecord("ID", "Name", "Role", "Salary");
		for (PredictionFile emp : emps) {
			List<String> empData = new ArrayList<String>();
			empData.add(emp.getT1());
			empData.add(emp.getT2());
			printer.printRecord(empData);
		}
		// close the printer
		printer.close();
	}

	public static void simpleTxt() throws FileNotFoundException, IOException {
		CSVFormat format = CSVFormat.RFC4180.withHeader();

		AnalyticFileHandler afh = new AnalyticFileHandler();
		CSVParser parser = new CSVParser(new FileReader(afh.getTrainFileName(
				89, "J2_League", "Japan")), format);

		// List<PredictionFile> emps = new ArrayList<PredictionFile>();
		StringBuilder sb = new StringBuilder();

		for (CSVRecord record : parser) {
			sb.append(record.get("t1") + " " + record.get("t1Form") + " - "
					+ record.get("t2Form") + record.get("t2") + "\n");
		}
		System.out.println(sb);
		// close the parser
		parser.close();

	}

	public static void fileCounter() {
		AnalyticFileHandler afh = new AnalyticFileHandler();
		// File folde = afh.getLeatestTestFileName(93, "Supperttan", "Sweden");
		// SortedSet<LocalDate> ldl = new TreeSet<>();
		// for (String fil : folde.list()) {
		// String[] temp = fil.split("_");
		// try {
		// LocalDate ld = LocalDate.parse(temp[3],
		// DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		// ldl.add(ld);
		// // return (int) ChronoUnit.DAYS.between(ld, dat);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// System.out.println(fil);
		// }// for
		//
		// System.out.println(ldl.last().toString());
		System.out.println(afh.getLeatestTestFileName(93, "Supperttan",
				"Sweden"));

	}

	public static void matchPredWithWDL() throws SQLException {
		CountryCompCompId ccci = new CountryCompCompId("Sweden", "Superettan",
				164,0);
		ccci.showLine();
		MatchPredLineHandler mph = new MatchPredLineHandler();
		// mph.doer(164, "Superettan", "Sweden");
		mph.doer(ccci);

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		ccci.setObj(mph.getMatchPredLine());
		String jo = gson.toJson(ccci);
		// String jo = gson.toJson(jecci);
		// String jo = gson.toJson(mph.getMatchPredLine());
		log.info("{}", jo);
	}

	public static void wdlfuncs() throws SQLException {
		int compId = 112;
		CountryCompCompId ccci = new CompIdToCountryCompCompID().search(compId);
		ccci.showLine();
		MatchPredLineHandler mph = new MatchPredLineHandler();
		// mph.doer(164, "Superettan", "Sweden");
		mph.wdlOnly(ccci);

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		ccci.setObj(mph.getMatchPredLine());
		String jo = gson.toJson(ccci);
		// String jo = gson.toJson(jecci);
		// String jo = gson.toJson(mph.getMatchPredLine());
		log.info("{}", jo);
	}

	public static void weekmatch() throws SQLException {
		int compId = 112;
		CountryCompCompId ccci = new CompIdToCountryCompCompID().search(compId);
		ccci.showLine();
		WeekMatchHandler wmh = new WeekMatchHandler();
		wmh.redWeekMatches(compId);
//		System.out.println("FORM DATA");
//		log.info("{}", wmh.redWeekMatches(compId));

		log.info("-------:Now print common adversaries");
		if (CommonAdversariesHandler.commonAdv.get(compId) != null) {
			log.info("\n {}", CommonAdversariesHandler.commonAdv.get(compId));
		} else {
			log.warn("No common csvText found");
		}

	}

	public static void printparser() throws SQLException {
		int compId = 112;
		CountryCompCompId ccci = new CompIdToCountryCompCompID().search(compId);
//		TrainPredFile tpf = new TrainPredFile();
		TestPredFile tpf= new TestPredFile();
		log.info(tpf.reducedCsv(compId, ccci.getCompetition(), ccci.getCountry()));
	}
}
