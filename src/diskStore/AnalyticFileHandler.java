package diskStore;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import strategyAction.TempMatchFunctions;
import structures.CountryCompetition;
import structures.ReducedPredictionTestFile;
import structures.TimeVariations;
import api.functionality.obj.StrStrTuple;
import basicStruct.CCAllStruct;
import basicStruct.FullMatchPredLineToSubStructs;
import basicStruct.MatchObj;
import calculate.OutcomeCalculator;
import extra.NameCleaner;

public class AnalyticFileHandler {
	public static Logger log = LoggerFactory
			.getLogger(AnalyticFileHandler.class);
	// Gson gson = new Gson();
	private File bastFileFolder = new File("C:/BastData");
	private File predDataFolder = new File(bastFileFolder + "/Pred/Data");
	private File predTestFolder = new File(bastFileFolder + "/Pred/Test");
	private File imageFolder = new File(bastFileFolder + "/DTF");
	private File dayPredFolder = new File(bastFileFolder + "/WeekPredPoints");
	// private File csvFile = new File(bastFileFolder + "/matches.csv");
	// private File mDataFile = new File(bastFileFolder + "/matchData.csv");
	private String wordSeparator = "__";

	private BufferedWriter bw = null;

	public AnalyticFileHandler() {
		super();
		if (!bastFileFolder.exists()) {
			bastFileFolder.mkdirs();
		}
		if (!predDataFolder.exists()) {
			predDataFolder.mkdirs();
		}
		if (!predTestFolder.exists()) {
			predTestFolder.mkdirs();
		}
	}

	public void openTrainOutput(int compId, String compName, String country)
			throws IOException {
		compName = NameCleaner.replacements(compName);
		country = NameCleaner.replacements(country);
		File f = createTrainFile(compId, compName, country);
		if (f == null) {
			log.warn("Train Pred file not found for {} {}", compName, country);
		} else {
			bw = new BufferedWriter(new FileWriter(f, true));
		}

	}

	public void openTestOutput(int compId, String compName, String country,
			LocalDate date) {
		/*
		 * from the compId get country and competition and create a country
		 * folder with competition_compId name for the prediction datafile
		 */
		compName = NameCleaner.replacements(compName);
		country = NameCleaner.replacements(country);

		try {
			File f = createTestFile(compId, compName, country, date);
			if (f == null) {
				log.warn("Test Pred file not found for {} {}", compName,
						country);
			} else {
				bw = new BufferedWriter(new FileWriter(f, true));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void openTestOutput(File file) {
		try {
			bw = new BufferedWriter(new FileWriter(file, true));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void closeOutput() throws IOException {
		if (bw != null) {
			bw.close();
		}
	}

	public void appendCsv(String line) throws IOException {
		bw.append(line + "\n");
	}

	public File createTestFile(int compId, String compName, String country,
			LocalDate dat) {
		compName = NameCleaner.replacements(compName);
		country = NameCleaner.replacements(country);
		File cFolder = new File(predTestFolder + "/" + country);
		if (!cFolder.exists()) {
			cFolder.mkdirs();
		}
		StringBuilder sb = new StringBuilder();
		sb.append(File.separator);
		sb.append(compName);
		sb.append(wordSeparator);
		sb.append(compId);
		sb.append(wordSeparator);
		sb.append("Test");
		sb.append(wordSeparator);
		sb.append(dat.toString());

		File tFile = new File(cFolder + sb.toString());
		return tFile;
	}

	public File getTestFileName(int compId, String compName, String country,
			LocalDate dat) {
		// create the folder file and a new test file of format
		// folder/CompName_compId_Test_2016-10-10

		// CCAllStruct cc = CountryCompetition.ccasList.get(compId - 1);
		compName = NameCleaner.replacements(compName);
		country = NameCleaner.replacements(country);
		File cFolder = new File(predTestFolder + "/" + country);
		if (!cFolder.exists()) {
			cFolder.mkdirs();
			return null;
		}
		StringBuilder sb = new StringBuilder();
		sb.append(File.separator);
		sb.append(compName);
		sb.append(wordSeparator);
		sb.append(compId);
		sb.append(wordSeparator);
		sb.append("Test");
		sb.append(wordSeparator);
		sb.append(dat.toString());

		File tFile = new File(cFolder + sb.toString());

		if (tFile.exists() && tFile.length() > 10)
			return tFile;
		else
			return null;
	}

	public File getLeatestTestFileName(int compId, String compName,
			String country) {
		// TODO replaces compName & country
		compName = NameCleaner.replacements(compName);
		country = NameCleaner.replacements(country);

		log.info("@ getLeatestTestFileName");
		File cFolder = new File(predTestFolder + "/" + country);
		if (!cFolder.exists()) {
			log.warn("afh Folder not found");
			return null;
		}
		SortedSet<LocalDate> ldl = new TreeSet<>();
		for (String fil : cFolder.list()) {
			String[] temp = fil.split(wordSeparator);
			if (!temp[0].equals(compName)) {
				// a country folder can have many competitions with different
				// names and dates
				continue;
			}
			try {
				LocalDate ld = LocalDate.parse(temp[3],
						DateTimeFormatter.ofPattern("yyyy-MM-dd"));
				ldl.add(ld);
			} catch (Exception e) {
				log.warn("afh Exception here");
				e.printStackTrace();
			}

		} // for
		// log.info(ldl.last().toString());
		StringBuilder sb = new StringBuilder();
		sb.append(File.separator);
		sb.append(compName);
		sb.append(wordSeparator);
		sb.append(compId);
		sb.append(wordSeparator);
		sb.append("Test");
		sb.append(wordSeparator);
		sb.append(ldl.last().toString());
		File tFile=new File(cFolder + sb.toString());
		if(tFile.exists() && tFile.length()>10){
			return tFile;
		}
		
		return null;
	}

	public File getLeatestRPredictionFileName(int compId, String compName,
			String country) {
		/*
		 * get all the predictions of a competition sort them bu the date in the
		 * file name and get the leatest one
		 */
		log.info("@ getLeatestPredictionFileName");

		compName = NameCleaner.replacements(compName);
		country = NameCleaner.replacements(country);
		File cFolder = new File(dayPredFolder + "/" + country);
		if (!cFolder.exists()) {
			log.warn("afh Folder not found");
			return null;
		}
		SortedSet<LocalDate> ldl = new TreeSet<>();
		for (String fil : cFolder.list()) {
			String[] temp = fil.split(wordSeparator);
			if (!temp[0].equals(compName)) {
				// a country folder can have many competitions with different
				// names and dates; so check the comp name first
				continue;
			}
			try {
				LocalDate ld = LocalDate.parse(temp[3],
						DateTimeFormatter.ofPattern("yyyy-MM-dd"));
				ldl.add(ld);
			} catch (Exception e) {
				log.warn("afh Exception here; un parsable date");
				e.printStackTrace();
			}

		} // for
			// rebuild the file path (~ = the folder)
		if (ldl.size() == 0) {
			return null;
		}
		// log.info(ldl.last().toString());
		StringBuilder sb = new StringBuilder();
		sb.append(File.separator);
		sb.append(compName);
		sb.append(wordSeparator);
		sb.append(compId);
		sb.append(wordSeparator);
		sb.append("Pred");
		sb.append(wordSeparator);
		sb.append(ldl.last().toString());

		return (new File(cFolder + sb.toString()));
	}

	public File createTrainFile(int compId, String compName, String country)
			throws IOException {
		compName = NameCleaner.replacements(compName);
		country = NameCleaner.replacements(country);
		File cFolder = new File(predDataFolder + "/" + country);
		if (!cFolder.exists()) {
			cFolder.mkdirs();
		}
		StringBuilder sb = new StringBuilder();
		sb.append(File.separator);
		sb.append(compName);
		sb.append(wordSeparator);
		sb.append(compId);
		sb.append(wordSeparator);
		sb.append("Data");

		File tFile = new File(cFolder + sb.toString());
		tFile.createNewFile();
		return tFile;
	}

	public File getTrainFileName(int compId, String compName, String country) {
		compName = NameCleaner.replacements(compName);
		country = NameCleaner.replacements(country);
		File cFolder = new File(predDataFolder + "/" + country);
		if (!cFolder.exists()) {
			cFolder.mkdirs();
			return null;
		}
		StringBuilder sb = new StringBuilder();
		sb.append(File.separator);
		sb.append(compName);
		sb.append(wordSeparator);
		sb.append(compId);
		sb.append(wordSeparator);
		sb.append("Data");

		// log.info("{}", (cFolder + sb.toString()));
		File tFile = new File(cFolder + sb.toString());
		if (tFile.exists() && tFile.length() > 10)
			return tFile;
		else
			return null;
	}

	public String getImageFolderName(int compId, String compName, String country) {

		/* see if image folder (country/competition__compId) exists and */
		compName = NameCleaner.replacements(compName);
		country = NameCleaner.replacements(country);
		File cFolder = new File(imageFolder + "/" + country);
		if (!cFolder.exists()) {
			// cFolder.mkdirs();
			return null;
		}
		StringBuilder sb = new StringBuilder();
		sb.append(File.separator);
		sb.append(compName);
		sb.append(wordSeparator);
		sb.append(compId);
		// sb.append(wordSeparator);
		// sb.append(".dtf.RData");xxxx
		// log.info("{}", (cFolder + sb.toString()));
		File tFolder = new File(cFolder + sb.toString());
		if (tFolder.exists())
			// if (tFolder.list().length > 0)
			return (cFolder + sb.toString());

		return null;
	}

	public boolean isTestFile(int compId, String compName, String country,
			LocalDate dat) {

		compName = NameCleaner.replacements(compName);
		country = NameCleaner.replacements(country);
		File cFolder = new File(predTestFolder + "/" + country);
		if (!cFolder.exists()) {
			return false;
		}
		StringBuilder sb = new StringBuilder();
		sb.append(File.separator);
		sb.append(compName);
		sb.append(wordSeparator);
		sb.append(compId);
		sb.append(wordSeparator);
		sb.append("Test");
		sb.append(wordSeparator);
		sb.append(dat.toString());

		File tFile = new File(cFolder + sb.toString());
		if (tFile.exists())
			if (tFile.length() > 10)
				return true;

		return false;
	}

	public int testFileDateDifference(int compId, String compName,
			String country, LocalDate dat) {
		/*
		 * if file not found or other iregularities return 0 else return the
		 * difference in days between the two files
		 */

		compName = NameCleaner.replacements(compName);
		country = NameCleaner.replacements(country);
		try {
			String[] temp = getLeatestTestFileName(compId, compName, country)
					.getAbsolutePath().split(wordSeparator);

			if (temp[0].equals(File.separator + compName)
					&& temp[1].equals(compId) && temp[2].equals("Test")) {

				LocalDate ld = LocalDate.parse(temp[3],
						DateTimeFormatter.ofPattern("yyyy-MM-dd"));
				return (int) ChronoUnit.DAYS.between(ld, dat);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return 0;
	}

	public int predFileDateDifference(int compId, String compName,
			String country, LocalDate ld) {
		/*
		 * if file not found or other iregularities return 0 else return the
		 * difference in days between the two files
		 */

		compName = NameCleaner.replacements(compName);
		country = NameCleaner.replacements(country);
		try {
			File f = getLeatestRPredictionFileName(compId, compName, country);
			if (f == null) {
				return -1;
			}
			String[] temp = f.getAbsolutePath().split(wordSeparator);

			if (temp[0].equals(File.separator + compName)
					&& temp[1].equals(compId) && temp[2].equals("Pred")) {

				LocalDate dat = LocalDate.parse(temp[3],
						DateTimeFormatter.ofPattern("yyyy-MM-dd"));
				return (int) ChronoUnit.DAYS.between(dat, ld);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return -1;
	}

	public boolean isPredFile(int compId, String compName, String country,
			LocalDate ld) {

		compName = NameCleaner.replacements(compName);
		country = NameCleaner.replacements(country);
		File cFolder = new File(dayPredFolder + "/" + country);
		if (!cFolder.exists()) {
			return false;
		}
		StringBuilder sb = new StringBuilder();
		sb.append(File.separator);
		sb.append(compName);
		sb.append(wordSeparator);
		sb.append(compId);
		sb.append(wordSeparator);
		sb.append("Pred");
		sb.append(wordSeparator);
		sb.append(ld.toString());

		File tFile = new File(cFolder + sb.toString());
		if (tFile.exists())
			if (tFile.length() > 10)
				return true;

		return false;
	}

	// ///////////Write Results Section
	public void writeResultsToTestFile() throws SQLException,
			FileNotFoundException, IOException {
		/* in the test files created write the actual results */
		/*
		 * the data in the test file that is about to be writtend doesn't have
		 * to contain all the prediction file attributes just the prediction
		 * attributes id 1,x,2,o,u,1p,2p,ht,ft. *******************************
		 * The order in which the data is written in the file matters though
		 */
		// the writing will be done at the yesterday comps
		// get all the tets files from yesterdayComps - the unfinished matches
		// of skipsday
		List<Integer> workingIds = TimeVariations.yesterdayComps;
		workingIds.removeAll(TempMatchFunctions.skipDayCompIds);
		LocalDate yesterDat = LocalDate.now().minusDays(1);
		TempMatchFunctions tmf = new TempMatchFunctions();

		// read yesterdays matches from the db
		FullMatchPredLineToSubStructs fmpss = new FullMatchPredLineToSubStructs();
		List<MatchObj> recentmatches = fmpss.fullMatchPredLineToMatchObj(tmf
				.readInitialTeamFromRecentMatches(yesterDat));

		for (int cid : workingIds) {
			CCAllStruct cc = CountryCompetition.ccasList
					.get(CountryCompetition.idToIdx.get(cid));
			File file = getTestFileName(cid, cc.getCompetition(),
					cc.getCountry(), yesterDat);
			if (file == null)
				continue;
			List<StrStrTuple> teamsList = readTestFileContent(file);
			if (teamsList == null)
				continue;

			List<ReducedPredictionTestFile> outcomesList = addOutcomes(
					teamsList, recentmatches);

			rewriteTestFile(file, outcomesList);
		}
	}

	public void rewriteTestFile(File file,
			List<ReducedPredictionTestFile> outcomesList) throws IOException {
		/*
		 * write to the test file the results needed for the reevaluation on the
		 * prediction points by the R system. The original predictionFile dat
		 * (73 attributes ...) will be wiped out
		 */
		FileWriter fwrite = new FileWriter(file);
		CSVFormat format = CSVFormat.RFC4180;
		// .withHeader(outcomesList.get(0) .csvHeader());
		CSVPrinter csvFilePrinter = new CSVPrinter(fwrite, format);
		List<String> matchRecord = null;
		fwrite.write(outcomesList.get(0).csvHeader() + "\n");
		for (ReducedPredictionTestFile rf : outcomesList) {
			matchRecord = new ArrayList<>();
			matchRecord.add(String.valueOf(rf.getT2()));
			matchRecord.add(String.valueOf(rf.getT1()));
			matchRecord.add(String.valueOf(rf.getHeadOutcome()));
			matchRecord.add(String.valueOf(rf.getScoreOutcome()));
			matchRecord.add(String.valueOf(rf.getHt1pOutcome()));
			matchRecord.add(String.valueOf(rf.getHt2pOutcome()));
			matchRecord.add(String.valueOf(rf.getTotHtScore()));
			matchRecord.add(String.valueOf(rf.getTotFtScore()));
			csvFilePrinter.printRecord(matchRecord);
		}

		System.out.println("CSV file was created successfully !!!");
		try {
			fwrite.flush();
			fwrite.close();
			csvFilePrinter.close();
		} catch (IOException e) {
			System.out
					.println("Error while flushing/closing fileWriter/csvPrinter !!!");
			e.printStackTrace();
		}
	}

	private List<ReducedPredictionTestFile> addOutcomes(
			List<StrStrTuple> teamsList, List<MatchObj> recentmatches) {
		OutcomeCalculator oc = new OutcomeCalculator();
		List<ReducedPredictionTestFile> redpfList = new ArrayList<ReducedPredictionTestFile>();

		for (int i = 0; i < teamsList.size(); i++) {
			String team = teamsList.get(i).getT1();
			int teamIdx = binarySearchRecent(recentmatches, team);
			if (teamIdx > -1) {
				if (teamsList.get(i).getT2()
						.equalsIgnoreCase(recentmatches.get(teamIdx).getT2())) {

					ReducedPredictionTestFile redpf = oc
							.outcomeAsignment(recentmatches.get(teamIdx));
					redpfList.add(redpf);

				} else {
					log.warn("T2 is not the expected one {} - {}", team,
							recentmatches.get(teamIdx).getT2());
				}
			} // if -1
		} // for
		return redpfList;

	}

	private List<StrStrTuple> readTestFileContent(File file)
			throws FileNotFoundException, IOException {
		/* read t1 and t2 that the test file contains */
		if (file == null)
			return null;
		CSVFormat format = CSVFormat.RFC4180;
		List<StrStrTuple> tuples = new ArrayList<StrStrTuple>();
		CSVParser parser = new CSVParser(new FileReader(file), format);
		for (CSVRecord record : parser) {
			StrStrTuple sst = new StrStrTuple();
			sst.setT1(record.get("t1"));
			sst.setT2(record.get("t2"));
			tuples.add(sst);
		}
		return tuples;
	}

	private int binarySearchRecent(List<MatchObj> list, String team) {
		int min = 0;
		int max = list.size() - 1;

		for (; min < max;) {
			int mid = (max + min) / 2;
			if (list.get(mid).getT1().equals(team)) {
				return mid;
			} else if (list.get(mid).getT1().compareTo(team) > 0) {
				max = mid - 1;
			} else {
				min = mid + 1;
			}
		}
		return -1;
	}

}
