package diskStore;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import structures.CountryCompetition;
import basicStruct.CCAllStruct;
import basicStruct.MatchObj;

import com.google.gson.Gson;

public class AnalyticFileHandler {
	Gson gson = new Gson();
	private File bastFileFolder = new File("C:/BastData");
	private File predDataFolder = new File(bastFileFolder + "/Pred/Data");
	private File predTestFolder = new File(bastFileFolder + "/Pred/Test");
	private File csvFile = new File(bastFileFolder + "/matches.csv");
	private File mDataFile = new File(bastFileFolder + "/matchData.csv");

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

	public void openTrainOutput(int compId,String compName,String country) throws IOException {
		bw = new BufferedWriter(new FileWriter(getTrainFileName(compId,compName,country), true));
		
	}

	public void openTestOutput(int compId, Date date) throws IOException {
		/*
		 * from the compId get country and competition and create a country
		 * folder with competition_compId name for the prediction datafile
		 */

		bw = new BufferedWriter(new FileWriter(getTestFileName(compId, date),
				true));
	}

	public void closeOutput() throws IOException {
		if (bw != null) {
			bw.close();
		}
	}

	public void appendCsv(String line) throws IOException {
		bw.append(line + "\n");
	}

	private File getTestFileName(int compId, Date dat) {
		// create the folder file and a new test file of format
		// folder/CompName_compId_Test_2016-10-10

		CCAllStruct cc = CountryCompetition.ccasList.get(compId - 1);
		File cFolder = new File(predTestFolder + "/" + cc.getCountry());
		if (!cFolder.exists()) {
			cFolder.mkdirs();
		}
		StringBuilder sb = new StringBuilder();
		sb.append(File.separator);
		sb.append(cc.getCompetition());
		sb.append("_");
		sb.append(cc.getCompId());
		sb.append("_");
		sb.append("Test");
		sb.append("_");
		sb.append(dat.toString());

		File tFile = new File(cFolder + sb.toString());

		return tFile;
	}

	private File getTrainFileName(int compId, String compName,String country) {
		// create the folder file and a new test file of format
		// folder/CompName_compId_Data

		CCAllStruct cc = CountryCompetition.ccasList.get(compId - 1);
		File cFolder = new File(predDataFolder + "/" + country);
		if (!cFolder.exists()) {
			cFolder.mkdirs();
		}
		StringBuilder sb = new StringBuilder();
		sb.append(File.separator);
		sb.append(compName);
		sb.append("_");
		sb.append(compId);
		sb.append("_");
		sb.append("Data");

		File tFile = new File(cFolder + sb.toString());

		return tFile;
	}
}
