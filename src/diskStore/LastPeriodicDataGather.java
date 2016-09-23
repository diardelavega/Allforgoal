package diskStore;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Map;

import basicStruct.MatchObj;
import test.MatchGetter;

/**
 * @author Administrator
 *
 *         To be used for storing the last step of data scruped from the sites
 *         during periodic check.
 */
public class LastPeriodicDataGather {

	String tempStore = "C:/BastData/TempData/file";
	File file = new File(tempStore);

	public void writeLastScheduled() throws IOException {
		FileOutputStream fos = new FileOutputStream(file);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(MatchGetter.schedNewMatches);
		oos.close();
	}

	public void readLastScheduled() throws IOException, ClassNotFoundException {
		FileInputStream fis = new FileInputStream(file);
		ObjectInputStream ois = new ObjectInputStream(fis);
		MatchGetter.schedNewMatches.clear();
		MatchGetter.schedNewMatches = (Map<Integer, List<MatchObj>>) ois
				.readObject();
		ois.close();
	}

	public boolean fileFilledCheck() {
		if (file.exists() && file.length() > 10) {
			return true;
		}
		return false;
	}
}
