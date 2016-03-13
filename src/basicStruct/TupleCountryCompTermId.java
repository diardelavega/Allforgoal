package basicStruct;

public class TupleCountryCompTermId {
	private String country;
	private String competition;
	private int id;

	public TupleCountryCompTermId() {
		super();
		// TODO Auto-generated constructor stub
	}

	public TupleCountryCompTermId(String country, String competition, int id) {
		super();
		this.country = country;
		this.competition = competition;
		this.id = id;
	}

	public String getCountry() {
		return country;
	}

	public String getCompetition() {
		return competition;
	}

	public int getId() {
		return id;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public void setCompetition(String competition) {
		this.competition = competition;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void printer() {
		System.out.println(country + " " + competition + " " + id);
	}
}
