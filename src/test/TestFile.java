package test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import basicStruct.MatchObj;
import com.google.gson.Gson;

public class TestFile {
	Gson gson = new Gson();
	private File bastFileFolder = new File("C:/BastData");
	private File errorMatches = new File(bastFileFolder + "/errorMatchesFile");

	private BufferedWriter bw;

	public void inidRB() throws IOException {
		if(!bastFileFolder.exists()){
			bastFileFolder.mkdir();
		}
		bw = new BufferedWriter(new FileWriter(errorMatches, true));
	}

	public void closeRB() throws IOException {
		if (bw != null) {
			bw.close();
		}
	}

	public void write(MatchObj m) throws IOException {
		bw.append(gson.toJson(m) + "\n");
	}

}
