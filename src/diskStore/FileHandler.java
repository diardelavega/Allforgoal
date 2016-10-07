package diskStore;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Document;

import scrap.Bari91UpCommingOdds;
import scrap.OddsNStats;
import structures.CountryCompetition;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import extra.Unilang;
import basicStruct.BariToPunterTuple;
import basicStruct.BariToScorerTuple;
import basicStruct.MatchObj;
import basicStruct.StrIntTuple;
import basicStruct.StrStrTuple;
import basicStruct.TupleCountryCompTermId;
import basicStruct.TupleIdTerm;
import basicStruct.TupleTermId;

public class FileHandler {

	Gson gson = new Gson();
	private File bastFileFolder = new File("C:/BastData");
	// private File csvFile = new File(bastFileFolder + "/matches.csv");
	// private File mDataFile = new File(bastFileFolder + "/matchData.csv");

	private File dataFilesFolder = new File(bastFileFolder + "/data_files");
	private File unilangCcasTerms = new File(dataFilesFolder + "/ccasTerms");
	private File unilangScorerTerms = new File(dataFilesFolder + "/scorerTerms");
	private File unilangCcasTeams = new File(dataFilesFolder + "/ccasTeams");
	private File unilangScorerTeams = new File(dataFilesFolder + "/scorerTeams");
	private File allowedComps = new File(dataFilesFolder
			+ "/allowedCompetitions");
	private File notAllowedComps = new File(dataFilesFolder
			+ "/notAllowedCompetitions");
	private File unfoundScoreTerms = new File(dataFilesFolder
			+ "/unfoundScoreTerms");

	
	private File bariToScorerTeams = new File(dataFilesFolder
			+ "/bariToScorerTeams");
	private File bariAllowedTerms = new File(dataFilesFolder
			+ "/bariAllowedTerms");
	private File bariNotAllowedTerms = new File(dataFilesFolder
			+ "/bariNotAllowedTerms");
	// private File bariRemainingMatches = new File(dataFilesFolder
	// + "/bariRemainingMatches");

	private File odderToScorerTeams = new File(dataFilesFolder
			+ "/odderToScorerTeams");
	private File odderAllowedComps = new File(dataFilesFolder
			+ "/odderAllowedComps");
	private File odderNotAllowedComps = new File(dataFilesFolder
			+ "/odderNotAllowedComps");

	private File punterToScorerTeams = new File(dataFilesFolder+"/punterToScorerTeams");
	
	private BufferedWriter bw = null;

	// ------------

	// ==================UNILANG=========================================
	/*
	 * data is stored as Tuple json object because it will be easier to
	 * understand, alter and add by user
	 */
	// ----------------CCAS TERMS (country competition)
	public void appendUnilangCcasAllMap(Map<Integer, String> map)
			throws IOException {
		bw = new BufferedWriter(new FileWriter(unilangCcasTerms, true));
		TupleIdTerm it;// = new TupleIdTerm();
		for (Integer key : map.keySet()) {
			it = new TupleIdTerm(key, map.get(key));
			bw.write(gson.toJson(it) + "\n");
		}
		bw.close();
	}

	public void appendUnilangCcasTerm(Integer i, String s) throws IOException {
		bw = new BufferedWriter(new FileWriter(unilangCcasTerms, true));
		TupleIdTerm it = new TupleIdTerm(i, s);
		bw.write(gson.toJson(it) + "\n");
		bw.close();
	}

	public void readUnilangAllCcasTerms() throws JsonSyntaxException,
			IOException {
		// read from file and put directly to Unilang map
		if (!unilangCcasTerms.exists()) {
			unilangCcasTerms.createNewFile();
			return;
		}
		BufferedReader br = new BufferedReader(new FileReader(unilangCcasTerms));
		String line;
		while ((line = br.readLine()) != null) {
			TupleIdTerm it = gson.fromJson(line, TupleIdTerm.class);
			Unilang.ccasMap.put(it.getI(), it.getS());
		}
		br.close();
	}

	// -------------------CCAS TEAMS
	public void appendUnilangCcasAllTeamsMap(Map<Integer, String> map)
			throws IOException {
		bw = new BufferedWriter(new FileWriter(unilangCcasTeams, true));
		TupleIdTerm it;// = new TupleIdTerm();
		for (Integer key : map.keySet()) {
			it = new TupleIdTerm(key, map.get(key));
			bw.write(gson.toJson(it) + "\n");
		}
		bw.close();
	}

	public void appendUnilangCcasTeam(Integer i, String s) throws IOException {
		bw = new BufferedWriter(new FileWriter(unilangCcasTeams, true));
		TupleIdTerm it = new TupleIdTerm(i, s);
		bw.write(gson.toJson(it) + "\n");
		bw.close();
	}

	public void readUnilangAllCcasTeams() throws JsonSyntaxException,
			IOException {
		// read from file and put directly to Unilang map
		if (!unilangCcasTeams.exists()) {
			unilangCcasTeams.createNewFile();
			return;
		}
		BufferedReader br = new BufferedReader(new FileReader(unilangCcasTeams));
		String line;
		while ((line = br.readLine()) != null) {
			TupleIdTerm it = gson.fromJson(line, TupleIdTerm.class);
			Unilang.ccasTeamsMap.put(it.getI(), it.getS());
		}
		br.close();
	}

	// -----------SCORER TERMS--------------
	public void appendUnilangScorerAllMap(Map<String, Integer> map)
			throws IOException {
		bw = new BufferedWriter(new FileWriter(unilangScorerTerms, true));
		TupleTermId ti;// = new TupleIdTerm();
		for (String key : map.keySet()) {
			ti = new TupleTermId(key, map.get(key));
			bw.write(gson.toJson(ti) + "\n");
		}
		bw.close();
	}

	public void appendUnlangScorerTerm(String s, Integer i) throws IOException {
		bw = new BufferedWriter(new FileWriter(unilangScorerTerms, true));
		TupleTermId ti = new TupleTermId(s, i);
		bw.write(gson.toJson(ti) + "\n");
		bw.close();
	}

	public void readUnilangAllScorerTerms() throws JsonSyntaxException,
			IOException {
		// read from file and put directly to Unilang map
		if (!unilangScorerTerms.exists()) {
			unilangScorerTerms.createNewFile();
			return;
		}
		BufferedReader br = new BufferedReader(new FileReader(
				unilangScorerTerms));
		String line;
		while ((line = br.readLine()) != null) {
			TupleTermId ti = gson.fromJson(line, TupleTermId.class);
			Unilang.scoreMap.put(ti.getS(), ti.getI());
		}
		br.close();
	}

	// -----------SCORER TEAMS--------------
	public void appendUnilangScorerTeamAllMap(Map<String, Integer> map)
			throws IOException {
		bw = new BufferedWriter(new FileWriter(unilangScorerTeams, true));
		TupleTermId ti;// = new TupleIdTerm();
		for (String key : map.keySet()) {
			ti = new TupleTermId(key, map.get(key));
			bw.write(gson.toJson(ti) + "\n");
		}
		bw.close();
	}

	public void appendUnlangScorerTeam(String s, Integer i) throws IOException {
		bw = new BufferedWriter(new FileWriter(unilangScorerTeams, true));
		TupleTermId ti = new TupleTermId(s, i);
		bw.write(gson.toJson(ti) + "\n");
		bw.close();
	}

	public void readUnilangAllScorerTeams() throws JsonSyntaxException,
			IOException {
		if (!unilangScorerTeams.exists()) {
			unilangScorerTeams.createNewFile();
			return;
		}
		BufferedReader br = new BufferedReader(new FileReader(
				unilangScorerTeams));
		String line;
		while ((line = br.readLine()) != null) {
			TupleTermId ti = gson.fromJson(line, TupleTermId.class);
			Unilang.scoreTeamsMap.put(ti.getS(), ti.getI());
		}
		br.close();
	}

	// --------------allowed competitions
	public void appendAllowedCompetitions(String compName, int compId)
			throws IOException {
		// the compName comes from xscorer and compId is from cca_structure
		// format Map<xscoreCompName, ccasCompId>

		bw = new BufferedWriter(new FileWriter(allowedComps, true));
		TupleTermId ti = new TupleTermId(compName, compId);
		bw.write(gson.toJson(ti) + "\n");
		bw.close();
	}

	public void readAllowedCompetitions() throws JsonSyntaxException,
			IOException {
		if (!allowedComps.exists()) {
			allowedComps.createNewFile();
			return;
		}
		BufferedReader br = new BufferedReader(new FileReader(allowedComps));
		String line;
		while ((line = br.readLine()) != null) {
			TupleTermId ti = gson.fromJson(line, TupleTermId.class);
			CountryCompetition.allowedcomps.put(ti.getS(), ti.getI());
		}
		br.close();
	}

	// --------not allowed ones
	public void readNotAllowedCompetitions() throws IOException {
		if (!notAllowedComps.exists()) {
			notAllowedComps.createNewFile();
			return;
		}
		BufferedReader br = new BufferedReader(new FileReader(notAllowedComps));
		String line;
		while ((line = br.readLine()) != null) {
			CountryCompetition.notAllowedcomps.add(line);
		}
		br.close();
	}

	public void appendNotAllowedCompetitions(String compName)
			throws IOException {
		// the compName comes from xscorer and compId is from cca_structure
		// format Map<xscoreCompName, ccasCompId>

		bw = new BufferedWriter(new FileWriter(notAllowedComps, true));
		bw.append(compName + "\n");
		bw.close();
	}

	// -------------
	public void appendUnfoundTerms(String cnt, String cmp, int i)
			throws IOException {
		bw = new BufferedWriter(new FileWriter(unfoundScoreTerms, true));
		TupleCountryCompTermId ccti = new TupleCountryCompTermId(cnt, cmp, i);
		bw.write(gson.toJson(ccti) + "\n");
		bw.close();
	}

	// ============================================================
	// -------------BariToScorer
	public void apendBariToScorer(String b, String s)  {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(
					bariToScorerTeams, true));
			BariToScorerTuple bts = new BariToScorerTuple(b, s);
			bw.write(gson.toJson(bts) + "\n");
			bw.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void readBariToScorer()  {
		try {
			if (!bariToScorerTeams.exists()) {
				bariToScorerTeams.createNewFile();
				return;
			}
			BufferedReader br = new BufferedReader(
					new FileReader(bariToScorerTeams));
			String line;
			while ((line = br.readLine()) != null) {
				BariToScorerTuple bts = gson
						.fromJson(line, BariToScorerTuple.class);
				Bari91UpCommingOdds.btsTeams.put(bts.getBt(), bts.getSt());
			}
			br.close();
		} catch (JsonSyntaxException | IOException e) {
			e.printStackTrace();
		}
	}

	// -------------BariToPunterAllowed
	public void apendAllowedBariToPunter(String b, int i) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(bariAllowedTerms,
				true));
		StrIntTuple sit = new StrIntTuple(b, i);
		bw.write(gson.toJson(sit) + "\n");
		bw.close();
	}

	public void readAllowedBariToPunter() {
		try {
			if (!bariAllowedTerms.exists()) {
				bariAllowedTerms.createNewFile();
				return;
			}
			BufferedReader br = new BufferedReader(new FileReader(
					bariAllowedTerms));
			String line;
			while ((line = br.readLine()) != null) {
				StrIntTuple sit = gson.fromJson(line, StrIntTuple.class);
				Bari91UpCommingOdds.btpAllowed.put(sit.getC(), sit.getI());
			}
			br.close();
		} catch (JsonSyntaxException | IOException e) {
			e.printStackTrace();
		}
	}

	// ---------------BariNotAllowed
	public void apendNotAllowedBariToPunter(String b) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(
				bariNotAllowedTerms, true));
		bw.write(gson.toJson(b) + "\n");
		bw.close();
	}

	public void readNotAllowedBariToPunter() {
		try {
			if (!bariNotAllowedTerms.exists()) {
				bariNotAllowedTerms.createNewFile();
				return;
			}
			BufferedReader br = new BufferedReader(new FileReader(
					bariNotAllowedTerms));
			String line;
			while ((line = br.readLine()) != null) {
				Bari91UpCommingOdds.btpNotAllowed.add(line);
			}
			br.close();
		} catch (JsonSyntaxException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// ---------BariRemainingMatches
	public void writeBariRemaining(List<MatchObj> rm)
			throws FileNotFoundException, IOException {
		if (!dataFilesFolder.exists()) {
			dataFilesFolder.mkdirs();
		}
		ObjectOutputStream oos = null;
		try {
			// oos = new ObjectOutputStream(new FileOutputStream(
			// bariRemainingMatches));
			// oos.writeObject(rm);
			oos.close();
		} catch (IOException e) {
			oos.close();
			e.printStackTrace();
		}
	}

	public void readBariRemaining() {
		ObjectInputStream ois = null;
		try {
			if (!dataFilesFolder.exists()) {
				dataFilesFolder.mkdirs();
			}
			// if (!bariRemainingMatches.exists()) {
			// bariRemainingMatches.createNewFile();
			// return;
			// }
			// ois = new ObjectInputStream(new FileInputStream(
			// bariRemainingMatches));
			// Bari91UpCommingOdds.remainings = (List<MatchObj>)
			// ois.readObject();
			ois.close();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	// ============================================
	// ---------------OdderTOScorer matches
	public void appendOdderToScorerTeams(String o, String s) {
		// odder-scorer
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(
					odderToScorerTeams, true));
			StrStrTuple sst = new StrStrTuple(o, s);
			bw.write(gson.toJson(sst) + "\n");
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void readOdderToScorerTeams() {
		try {
			if (!odderToScorerTeams.exists()) {
				odderToScorerTeams.createNewFile();
				return;
			}
			BufferedReader br = new BufferedReader(new FileReader(
					odderToScorerTeams));
			String line;
			while ((line = br.readLine()) != null) {
//				System.out.println(line);
				StrStrTuple sst = gson.fromJson(line, StrStrTuple.class);
				OddsNStats.oddsToScorer.put(sst.getS1(), sst.getS2());
			}
			br.close();
		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// ----------- odderAllowedComps
	public void apendOdderToPunterAllowedComps(String o, int i)
			throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(
				odderAllowedComps, true));
		// BariToPunterTuple btp = new BariToPunterTuple(b, p);
//		StrStrTuple sst = new StrStrTuple(o, p);
		StrIntTuple sit = new StrIntTuple(o,i);
		bw.write(gson.toJson(sit) + "\n");
		bw.close();
	}

	public void readOdderToPunterAllowedComps() {
		try {
			if (!odderAllowedComps.exists()) {
				odderAllowedComps.createNewFile();
				return;
			}
			BufferedReader br = new BufferedReader(new FileReader(
					odderAllowedComps));
			String line;
			while ((line = br.readLine()) != null) {
				StrIntTuple sit = gson.fromJson(line, StrIntTuple.class);
				OddsNStats.oddsToPunterAllowedComps.put(sit.getC(),sit.getI());
			}
			br.close();
		} catch (JsonSyntaxException | IOException e) {
			e.printStackTrace();
		}
	}

	// ----------- odderNotAllowedComps
	public void apendOdderToPunterNotAllowedComps(String t) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(
				odderNotAllowedComps, true));
		bw.write(gson.toJson(t) + "\n");
		bw.close();
	}

	public void readOdderToPunterNotAllowedComps() {
		try {
			if (!odderNotAllowedComps.exists()) {
				odderNotAllowedComps.createNewFile();
				return;
			}
			BufferedReader br = new BufferedReader(new FileReader(
					odderNotAllowedComps));
			String line;
			while ((line = br.readLine()) != null) {
				OddsNStats.oddsToPunterNotAllowedComps.add(line);
			}
			br.close();
		} catch (JsonSyntaxException | IOException e) {
			e.printStackTrace();
		}
	}
//=================Punter To scorer============================
public void appendPunterToScorerTeams(String p,String s){
	try {
		BufferedWriter bw = new BufferedWriter(new FileWriter(
				punterToScorerTeams, true));
		StrStrTuple sst = new StrStrTuple(p, s);
		bw.write(gson.toJson(sst) + "\n");
		bw.close();
	} catch (Exception e) {
		e.printStackTrace();
	}
}

public void readPunterToScorerTeams(){}
}
