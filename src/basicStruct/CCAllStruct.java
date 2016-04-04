package basicStruct;

public class CCAllStruct {

	private String country;
	private String competition;
	private int compId;
	private String compLink;
	private int db; // if we have it od db or not
	private int _level; // firstcategory, second category etc

	public CCAllStruct() {
	}

	public CCAllStruct(String country, String competition, int compId,
			String compLink, int db, int _level) {
		super();
		this.country = country;
		this.competition = competition;
		this.compId = compId;
		this.compLink = compLink;
		this.db = db;
		this._level = _level;
	}

	public String getCountry() {
		return country;
	}

	public String getCompetition() {
		return competition;
	}

	public int getCompId() {
		return compId;
	}

	public String getCompLink() {
		return compLink;
	}

	public int getDb() {
		return db;
	}

	public int getLevel() {
		return _level;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public void setCompetition(String competition) {
		this.competition = competition;
	}

	public void setCompId(int compId) {
		this.compId = compId;
	}

	public void setCompLink(String compLink) {
		this.compLink = compLink;
	}

	public void setDb(int db) {
		this.db = db;
	}

	public void setLevel(int _level) {
		this._level = _level;
	}

	public String printer(){
		StringBuilder sb = new StringBuilder();
		sb.append(compId);
		sb.append(",");
		sb.append(country);
		sb.append(",");
		sb.append(competition);
		sb.append(",");
		sb.append(compLink);
		sb.append(",");
		sb.append(db);
		sb.append(",");
		sb.append(_level);
		sb.append(",");
//		System.out.println(sb.toString());
		return sb.toString();
	}
}
