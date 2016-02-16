package basicStruct;

public class CountryCompObj {

	private int id;
	private String country;
	private String competition;
	

	public CountryCompObj() {
	}

	public CountryCompObj(int id,String country, String competition) {
		super();
		this.country = country;
		this.competition = competition;
		this.id = id;
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
