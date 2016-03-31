package basicStruct;

public class BariToScorerTuple {
	private String bt;// bari term
	private String st;// scorer term

	public BariToScorerTuple() {
		super();
		// TODO Auto-generated constructor stub
	}

	public BariToScorerTuple(String bt, String st) {
		super();
		this.bt = bt;
		this.st = st;
	}

	public String getBt() {
		return bt;
	}

	public String getSt() {
		return st;
	}

	public void setBt(String bt) {
		this.bt = bt;
	}

	public void setSt(String st) {
		this.st = st;
	}

	public void printer(){
		System.out.println(bt+"-"+st);
	}
}
