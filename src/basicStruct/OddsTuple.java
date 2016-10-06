package basicStruct;

public class OddsTuple {
	private MatchObj m;
	private int level;

	public OddsTuple(MatchObj m, int level) {
		super();
		this.m = m;
		this.level = level;
	}

	public OddsTuple() {
		super();
	}

	public MatchObj getM() {
		return m;
	}

	public int getLevel() {
		return level;
	}

	public void setM(MatchObj m) {
		this.m = m;
	}

	public void setLevel(int level) {
		this.level = level;
	}

}
