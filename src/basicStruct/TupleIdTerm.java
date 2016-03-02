package basicStruct;

/**
 * @author diego
 *
 *         Tuples to be used for ccas conversion from obj to file
 */
public class TupleIdTerm {
	private String s;
	private int i;

	public TupleIdTerm() {
		super();
		// TODO Auto-generated constructor stub
	}

	public TupleIdTerm(String s, int i) {
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
		System.out.println(s + " - " + i);
	}

}
