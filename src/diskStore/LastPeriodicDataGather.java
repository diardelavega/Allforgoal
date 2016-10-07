package diskStore;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import extra.PeriodicTimes;
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

	public void precheck() throws IOException {
		File folder = new File("C:/BastData/TempData");
		if (!folder.exists())
			folder.mkdirs();
		if (!meta.exists()) {
			meta.createNewFile();
			// meta.
		}
		if (!sch.exists()) {
			sch.createNewFile();
		}
		if (!fin.exists()) {
			fin.createNewFile();
		}
		if (!err.exists()) {
			err.createNewFile();
		}
		if (!odd.exists()) {
			odd.createNewFile();
		}
		if (!yes.exists()) {
			yes.createNewFile();
		}
		if (!tod.exists()) {
			tod.createNewFile();
		}
		if (!tom.exists()) {
			tom.createNewFile();
		}
	}

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
		MatchGetter.schedNewMatches = (Map<Integer, List<MatchObj>>) ois.readObject();
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
		MatchGetter.finNewMatches = (Map<Integer, List<MatchObj>>) ois.readObject();
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
		MatchGetter.errorNewMatches = (Map<Integer, List<MatchObj>>) ois.readObject();
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

	public void readLastYesterdayComp() throws IOException, ClassNotFoundException {
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

	public void readLasttomorrowComps() throws IOException, ClassNotFoundException {
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

	public boolean hourlyFileFilledCheck() throws ClassNotFoundException, IOException {
		// calc the difference between the last stored datetime and the current
		// one. Check if that differeence is smaller than the scheduled periodic
		// time for re-grub&update
		if (sch.exists() && sch.length() > 10) {
			long diff = 100;
			try {
				diff = ChronoUnit.HOURS.between(readMeta(), LocalDateTime.now());
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (diff > PeriodicTimes.PERIOD)
				return true;
			return false;
		}
		return true;
	}

	public boolean yesterdayFilledCheck() {
		// see if last scann was yesterday
		if (sch.exists() && sch.length() > 10) {
			long diff = 100;
			LocalDateTime ldt = LocalDateTime.now();
			try {
				diff = ChronoUnit.HOURS.between(readMeta(), ldt);
			} catch (Exception e) {
				e.printStackTrace();
			}
			// before today(hours of this day), after the start of yesterday
			if (diff < (24 + ldt.getHour()) && diff > ldt.getHour())
				return true;
		}
		return false;
	}

	public void writeMeta(LocalDate ld) throws IOException {
		LocalDateTime ldt = LocalDateTime.now();
		FileOutputStream fos = new FileOutputStream(meta);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeUTF(ldt.toString());
		oos.close();
	}

	public LocalDateTime readMeta() throws IOException, ClassNotFoundException {
		if (fileExzistence(meta)) {
			FileInputStream fis = new FileInputStream(meta);
			ObjectInputStream ois = new ObjectInputStream(fis);
			LocalDateTime ldt = (LocalDateTime) LocalDateTime.parse(ois.readUTF());
			ois.close();
			return ldt;
		}
		return null;
	}

	private boolean fileExzistence(File f) {
		if (f.exists())
			return true;
		return false;
	}

}
