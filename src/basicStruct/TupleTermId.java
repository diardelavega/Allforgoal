package basicStruct;

/**
 * @author diego
 *
 *         Tuples to be used for storing and reading xScorer data to and from
 *         obj to file
 */
public class TupleTermId {
	private String s;
	private int i;

	public TupleTermId() {
		super();
		// TODO Auto-generated constructor stub
	}

	public TupleTermId( String s,int i) {
		super();
		this.s = s;
		this.i = i;
	}

	public String getS() {
		return s;
	}

	public void setS(String s) {
		this.s = s;
	}

	public int getI() {
		return i;
	}

	public void setI(int i) {
		this.i = i;
	}

	public void printer() {
		System.out.println(i + " - " + s);
	}

}
