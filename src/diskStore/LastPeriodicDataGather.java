package diskStore;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import basicStruct.MatchObj;
import structures.TimeVariations;
import test.MatchGetter;

/**
 * @author Administrator
 *
 *         To be used for storing the last step of data scruped from the sites
 *         during periodic check.
 */
public class LastPeriodicDataGather {

	File sch = new File("C:/BastData/TempData/scheduled");
	File fin = new File("C:/BastData/TempData/finished");
	File err = new File("C:/BastData/TempData/errored");
	File odd = new File("C:/BastData/TempData/odded");

	File yes = new File("C:/BastData/TempData/yest");
	File tod = new File("C:/BastData/TempData/tod");
	File tom = new File("C:/BastData/TempData/tom");

	File meta = new File("C:/BastData/TempData/meta");

	public void writeLastSch() throws IOException {
		FileOutputStream fos = new FileOutputStream(sch);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(MatchGetter.schedNewMatches);
		oos.close();
	}

	public void readLastSche() throws IOException, ClassNotFoundException {
		FileInputStream fis = new FileInputStream(sch);
		ObjectInputStream ois = new ObjectInputStream(fis);
		MatchGetter.schedNewMatches.clear();
		MatchGetter.schedNewMatches = (Map<Integer, List<MatchObj>>) ois
				.readObject();
		ois.close();
	}

	public void writeLastFin() throws IOException {
		FileOutputStream fos = new FileOutputStream(fin);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(MatchGetter.finNewMatches);
		oos.close();
	}

	public void readLastFin() throws IOException, ClassNotFoundException {
		FileInputStream fis = new FileInputStream(fin);
		ObjectInputStream ois = new ObjectInputStream(fis);
		MatchGetter.finNewMatches.clear();
		MatchGetter.finNewMatches = (List<MatchObj>) ois.readObject();
		ois.close();
	}

	public void writeLastErr() throws IOException {
		FileOutputStream fos = new FileOutputStream(err);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(MatchGetter.errorNewMatches);
		oos.close();
	}

	public void readLastErr() throws IOException, ClassNotFoundException {
		FileInputStream fis = new FileInputStream(err);
		ObjectInputStream ois = new ObjectInputStream(fis);
		MatchGetter.errorNewMatches.clear();
		MatchGetter.errorNewMatches = (List<MatchObj>) ois.readObject();
		ois.close();
	}

	public void writeLastOdd() throws IOException {
		FileOutputStream fos = new FileOutputStream(odd);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(MatchGetter.reviewedAndEmptyOdds);
		oos.close();
	}

	public void readLastOdd() throws IOException, ClassNotFoundException {
		FileInputStream fis = new FileInputStream(odd);
		ObjectInputStream ois = new ObjectInputStream(fis);
		MatchGetter.reviewedAndEmptyOdds.clear();
		MatchGetter.reviewedAndEmptyOdds = (List<Integer>) ois.readObject();
		ois.close();
	}

	// ------------------------------------------------
	public void writeLastYesterdayComp() throws IOException {
		FileOutputStream fos = new FileOutputStream(yes);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(TimeVariations.yesterdayComps);
		oos.close();
	}

	public void readLastYesterdayComp() throws IOException,
			ClassNotFoundException {
		FileInputStream fis = new FileInputStream(yes);
		ObjectInputStream ois = new ObjectInputStream(fis);
		TimeVariations.yesterdayComps.clear();
		TimeVariations.yesterdayComps = (List<Integer>) ois.readObject();
		ois.close();
	}

	public void writeLastTodayComp() throws IOException {
		FileOutputStream fos = new FileOutputStream(tod);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(TimeVariations.todayComps);
		oos.close();
	}

	public void readLastTodayComp() throws IOException, ClassNotFoundException {
		FileInputStream fis = new FileInputStream(tod);
		ObjectInputStream ois = new ObjectInputStream(fis);
		TimeVariations.todayComps.clear();
		TimeVariations.todayComps = (List<Integer>) ois.readObject();
		ois.close();
	}

	public void writeLasttomorrowComp() throws IOException {
		FileOutputStream fos = new FileOutputStream(tom);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(TimeVariations.tomorrowComps);
		oos.close();
	}

	public void readLasttomorrowComps() throws IOException,
			ClassNotFoundException {
		FileInputStream fis = new FileInputStream(tom);
		ObjectInputStream ois = new ObjectInputStream(fis);
		TimeVariations.tomorrowComps.clear();
		TimeVariations.tomorrowComps = (List<Integer>) ois.readObject();
		ois.close();
	}

	// ------------------------------------------------

	public void readMatchStructs() throws ClassNotFoundException, IOException {
		readLastSche();
		readLastFin();
		readLastErr();
		readLastOdd();
		readLastYesterdayComp();
		readLastTodayComp();
		readLasttomorrowComps();
	}

	public void writeMatchStructs() throws IOException {
		writeLastSch();
		writeLastFin();
		writeLastErr();
		writeLastOdd();
		writeLastYesterdayComp();
		writeLastTodayComp();
		writeLasttomorrowComp();
	}

	public boolean fileFilledCheck() throws ClassNotFoundException, IOException {
		if (sch.exists() && sch.length() > 10) {
			if (!readMeta().isBefore(LocalDate.now()))
				return true;
		}
		return false;
	}

	public void writeMeta(LocalDate ld) throws IOException {
		FileOutputStream fos = new FileOutputStream(meta);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(ld);
		oos.close();
	}

	public LocalDate readMeta() throws IOException, ClassNotFoundException {
		FileInputStream fis = new FileInputStream(meta);
		ObjectInputStream ois = new ObjectInputStream(fis);
		LocalDate ld = (LocalDate) ois.readObject();
		ois.close();
		return ld;
	}

}
