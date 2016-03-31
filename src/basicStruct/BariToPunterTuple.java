package basicStruct;

public class BariToPunterTuple {
	private String bt;// bari term
	private String pt;// scorer term

	public BariToPunterTuple() {
		super();
		// TODO Auto-generated constructor stub
	}

	public BariToPunterTuple(String bt, String pt) {
		super();
		this.bt = bt;
		this.pt = pt;
	}

	public String getBt() {
		return bt;
	}

	public String getPt() {
		return pt;
	}

	public void setBt(String bt) {
		this.bt = bt;
	}

	public void setPt(String pt) {
		this.pt = pt;
	}

	public void printer(){
		System.out.println(bt+"-"+pt);
	}
}
