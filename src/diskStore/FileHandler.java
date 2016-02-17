package diskStore;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileHandler {

	private File bastFileFolder = new File("C:/BastData/");
	private File csvFile = new File(bastFileFolder + "matches.csv");
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

}
