package basicStruct;

import java.util.ArrayList;
import java.util.List;

public class ScorerDataStruct {
	private int compId;
	private String country;
	private String competition;
	private int db;

	public ScorerDataStruct() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ScorerDataStruct(int compId, String country, String competition,
			int db) {
		super();
		this.compId = compId;
		this.country = country;
		this.competition = competition;
		this.db = db;
	}

	public int getDb() {
		return db;
	}

	public void setDb(int db) {
		this.db = db;
	}

	public int getCompId() {
		return compId;
	}

	public String getCountry() {
		return country;
	}

	public String getCompetition() {
		return competition;
	}

	public void setCompId(int compId) {
		this.compId = compId;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public void setCompetition(String competition) {
		this.competition = competition;
	}

	public String[] altComps() {
		String[] altComps = competition.split("/");
		return altComps;
	}

	public void printer() {
		System.out.println(compId + " " + country + " " + competition+" "+db);
	}
}
