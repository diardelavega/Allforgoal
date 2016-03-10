package diskStore;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import structures.CountryCompetition;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import extra.Unilang;
import basicStruct.MatchObj;
import basicStruct.TupleIdTerm;
import basicStruct.TupleTermId;

public class FileHandler {

	Gson gson = new Gson();
	private File bastFileFolder = new File("C:/BastData");
//	private File csvFile = new File(bastFileFolder + "/matches.csv");
//	private File mDataFile = new File(bastFileFolder + "/matchData.csv");

	private File dataFilesFolder = new File(bastFileFolder + "/data_files");
	private File unilangCcasTerms = new File(dataFilesFolder + "/ccasTerms");
	private File unilangScorerTerms = new File(dataFilesFolder + "/scorerTerms");
	private File unilangCcasTeams = new File(dataFilesFolder + "/ccasTeams");
	private File unilangScorerTeams = new File(dataFilesFolder + "/scorerTeams");
	private File allowedComps = new File(dataFilesFolder + "/allowedCompetitions");

	private BufferedWriter bw = null;

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
		if(!unilangCcasTerms.exists()){
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
		if(!unilangCcasTeams.exists()){
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
		if(!unilangScorerTerms.exists()){
			unilangScorerTerms.createNewFile();
			return;
		}
		BufferedReader br = new BufferedReader(new FileReader(unilangScorerTerms));
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
		if(!unilangScorerTeams.exists()){
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
//		format Map<xscoreCompName, ccasCompId>
		
		
		bw = new BufferedWriter(new FileWriter(allowedComps, true));
		TupleTermId ti = new TupleTermId(compName, compId);
		bw.write(gson.toJson(ti) + "\n");
		bw.close();
	}

	public void readAllowedCompetitions() throws JsonSyntaxException,
			IOException {
		if(!allowedComps.exists()){
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

}
