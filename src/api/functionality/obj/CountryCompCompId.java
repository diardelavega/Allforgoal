package api.functionality.obj;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CountryCompCompId {
	public static Logger log = LoggerFactory.getLogger(CountryCompCompId.class);

	private String country;
	private String competition;
	private int compId;
	private int serinr;
	private Object obj;

	public CountryCompCompId() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CountryCompCompId(String country, String competition, int compId, int serinr) {
		super();
		this.country = country;
		this.competition = competition;
		this.compId = compId;
		this.serinr = serinr;
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

	public int getSerinr() {
		return serinr;
	}

	public void setSerinr(int serinr) {
		this.serinr = serinr;
	}

	public void showLine() {
		log.info("{}   {}   {}", country, competition, compId);
	}
}
