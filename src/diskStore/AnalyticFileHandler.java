package diskStore;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Date;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import structures.CountryCompetition;
import basicStruct.CCAllStruct;
import basicStruct.MatchObj;

import com.google.gson.Gson;

public class AnalyticFileHandler {
	public static Logger log = LoggerFactory
			.getLogger(AnalyticFileHandler.class);
	// Gson gson = new Gson();
	private File bastFileFolder = new File("C:/BastData");
	private File predDataFolder = new File(bastFileFolder + "/Pred/Data");
	private File predTestFolder = new File(bastFileFolder + "/Pred/Test");
	private File imageFolder = new File(bastFileFolder + "/Img");
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
		bw = new BufferedWriter(new FileWriter(getTrainFileName(compId,
				compName, country), true));

	}

	public void openTestOutput(int compId, String compName, String country,
			LocalDate date) {
		/*
		 * from the compId get country and competition and create a country
		 * folder with competition_compId name for the prediction datafile
		 */

		try {
			bw = new BufferedWriter(new FileWriter(getTestFileName(compId,
					compName, country, date), true));
		} catch (IOException e) {
			// TODO Auto-generated catch block
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

	public File getTestFileName(int compId, String compName, String country,
			LocalDate dat) {
		// create the folder file and a new test file of format
		// folder/CompName_compId_Test_2016-10-10

		// CCAllStruct cc = CountryCompetition.ccasList.get(compId - 1);
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

	public File getLeatestTestFileName(int compId, String compName,
			String country) {
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

		}// for
		log.info(ldl.last().toString());
		StringBuilder sb = new StringBuilder();
		sb.append(File.separator);
		sb.append(compName);
		sb.append(wordSeparator);
		sb.append(compId);
		sb.append(wordSeparator);
		sb.append("Test");
		sb.append(wordSeparator);
		sb.append(ldl.last().toString());

		return (new File(cFolder + sb.toString()));
	}

	public File getTrainFileName(int compId, String compName, String country) {
		// create the folder file and a new test file of format
		// folder/CompName_compId_Data

		// CCAllStruct cc = CountryCompetition.ccasList.get(compId - 1);
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

		return tFile;
	}

	// --------------------------return Txt

	public String getImageFileName(int compId, String compName, String country) {
		/* see if image file exists and has data in it. Return its path or null */

		// CCAllStruct cc = CountryCompetition.ccasList.get(compId - 1);
		File cFolder = new File(imageFolder + "/" + country);
		if (!cFolder.exists()) {
			cFolder.mkdirs();
			return null;
		}
		StringBuilder sb = new StringBuilder();
		sb.append(File.separator);
		sb.append(compName);
		sb.append(wordSeparator);
		sb.append(compId);
		// sb.append(wordSeparator);
		sb.append(".RData");

		File tFile = new File(cFolder + sb.toString());
		if (tFile.exists() && tFile.length() > 10) {
			return (cFolder + sb.toString());
		}
		return null;
	}

	public boolean isTestFile(int compId, String compName, String country,
			LocalDate dat) {
		File cFolder = new File(predDataFolder + "/" + country);
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
		File cFolder = new File(predDataFolder + "/" + country);
		if (!cFolder.exists()) {
			return 0;
		}
		if (cFolder.list().length <= 0) {
			return 0;
		}

		String[] temp;
		for (int i = 0; i < cFolder.list().length; i++) {
			temp = cFolder.list()[i].split(wordSeparator);
			if (temp[0].equals(File.separator + compName)
					&& temp[1].equals(compId) && temp[2].equals("Test")) {
				try {
					LocalDate ld = LocalDate.parse(temp[3],
							DateTimeFormatter.ofPattern("yyyy-MM-dd"));
					return (int) ChronoUnit.DAYS.between(ld, dat);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		return 0;
	}
}
