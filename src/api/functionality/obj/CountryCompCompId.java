package api.functionality.obj;

public class CountryCompCompId {

	private String country;
	private String competition;
	private int compId;
	private Object obj;

	public CountryCompCompId() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CountryCompCompId(String country, String competition, int compId) {
		super();
		this.country = country;
		this.competition = competition;
		this.compId = compId;
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

	public void setCountry(String country) {
		this.country = country;
	}

	public void setCompetition(String competition) {
		this.competition = competition;
	}

	public void setCompId(int compId) {
		this.compId = compId;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}

}
