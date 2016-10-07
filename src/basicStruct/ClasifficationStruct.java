package basicStruct;

public class ClasifficationStruct {
	private String temaName;
	private int points;

	public ClasifficationStruct() {
		super();
	}

	public ClasifficationStruct(String temaName, int points) {
		super();
		this.temaName = temaName;
		this.points = points;
	}

	public String getTemaName() {
		return temaName;
	}

	public int getPoints() {
		return points;
	}

	public void setTemaName(String temaName) {
		this.temaName = temaName;
	}

	public void setPoints(int points) {
		this.points = points;
	}

}
