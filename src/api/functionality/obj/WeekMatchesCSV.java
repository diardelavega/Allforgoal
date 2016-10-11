package api.functionality.obj;

public class WeekMatchesCSV {
	private String country;
	private String competition;
	private int compId;
	private int size;
	// private int serinr;
	private String csvTxt;

	public WeekMatchesCSV() {
		super();
		// TODO Auto-generated constructor stub
	}

	public WeekMatchesCSV(String country, String competition, int compId, int size, String csvTxt) {
		super();
		this.country = country;
		this.competition = competition;
		this.compId = compId;
		this.size = size;
		this.csvTxt = csvTxt;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCompetition() {
		return competition;
	}

	public void setCompetition(String competition) {
		this.competition = competition;
	}

	public int getCompId() {
		return compId;
	}

	public void setCompId(int compId) {
		this.compId = compId;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public String getCsvTxt() {
		return csvTxt;
	}

	public void setCsvTxt(String csvTxt) {
		this.csvTxt = csvTxt;
	}

}
