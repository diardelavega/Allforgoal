package diskStore;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import basicStruct.MatchObj;

import com.google.gson.Gson;

public class AnalyticFileHandler {
	Gson gson = new Gson();
	private File bastFileFolder = new File("C:/BastData");
	private File csvFile = new File(bastFileFolder + "/matches.csv");
	private File mDataFile = new File(bastFileFolder + "/matchData.csv");
	
	private BufferedWriter bw = null;
	

	public void openOutput() throws IOException {
		bw = new BufferedWriter(new FileWriter(csvFile, true));
	}

	public void closeOutput() throws IOException {
		if (bw != null) {
			bw.close();
		}
	}

	public void appendCsv(String line) throws IOException {
		bw.write(line + "\n");
	}

	// -----------------------------------------
	public void opendataWrite() throws IOException {
		// inits the buffer
		bw = new BufferedWriter(new FileWriter(mDataFile, true));
	}

	public void appendMatchData(MatchObj match) throws IOException {
		match.printMatch();
		String line = gson.toJson(match);

		bw.write(line + "\n");
	}

	public List<MatchObj> readMatchData() throws IOException {
		
		BufferedReader br = new BufferedReader(new FileReader(mDataFile));
		String line;
		List<MatchObj> matchesList = new ArrayList<>();
		MatchObj match;
		while ((line = br.readLine()) != null) {
			match = gson.fromJson(line, MatchObj.class);
			matchesList.add(match);
		}
		return matchesList;
	}

}
