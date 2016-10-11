package api.functionality.obj;

/**
 * @author Administrator
 *
 *         a clase cleverly created to pass (send to client) easily some data
 *         with the basepred match line. (To avoid overhead)
 */

public class WinDrawLoseWithPred extends BaseMatchLinePred {
	private int t1wIn;
	private int t1wOut;
	private int t2wIn;
	private int t2wOut;

	private int t1dIn;
	private int t1dOut;
	private int t2dIn;
	private int t2dOut;

	private int t1lIn;
	private int t1lOut;
	private int t2lIn;
	private int t2lOut;

	public WinDrawLoseWithPred() {
		super();
	}

	public WinDrawLoseWithPred(int t1wIn,
			int t1wOut, int t2wIn, int t2wOut, int t1dIn, int t1dOut,
			int t2dIn, int t2dOut, int t1lIn, int t1lOut, int t2lIn, int t2lOut) {
		super();
		this.t1wIn = t1wIn;
		this.t1wOut = t1wOut;
		this.t2wIn = t2wIn;
		this.t2wOut = t2wOut;
		this.t1dIn = t1dIn;
		this.t1dOut = t1dOut;
		this.t2dIn = t2dIn;
		this.t2dOut = t2dOut;
		this.t1lIn = t1lIn;
		this.t1lOut = t1lOut;
		this.t2lIn = t2lIn;
		this.t2lOut = t2lOut;
	}

	public String getT1() {
		return t1;
	}

	public String getT2() {
		return t2;
	}

	public void setT1(String t1) {
		this.t1 = t1;
	}

	public void setT2(String t2) {
		this.t2 = t2;
	}

	public int getT1wIn() {
		return t1wIn;
	}

	public int getT1wOut() {
		return t1wOut;
	}

	public int getT2wIn() {
		return t2wIn;
	}

	public int getT2wOut() {
		return t2wOut;
	}

	public int getT1dIn() {
		return t1dIn;
	}

	public int getT1dOut() {
		return t1dOut;
	}

	public int getT2dIn() {
		return t2dIn;
	}

	public int getT2dOut() {
		return t2dOut;
	}

	public int getT1lIn() {
		return t1lIn;
	}

	public int getT1lOut() {
		return t1lOut;
	}

	public int getT2lIn() {
		return t2lIn;
	}

	public int getT2lOut() {
		return t2lOut;
	}

	public void setT1wIn(int t1wIn) {
		this.t1wIn = t1wIn;
	}

	public void setT1wOut(int t1wOut) {
		this.t1wOut = t1wOut;
	}

	public void setT2wIn(int t2wIn) {
		this.t2wIn = t2wIn;
	}

	public void setT2wOut(int t2wOut) {
		this.t2wOut = t2wOut;
	}

	public void setT1dIn(int t1dIn) {
		this.t1dIn = t1dIn;
	}

	public void setT1dOut(int t1dOut) {
		this.t1dOut = t1dOut;
	}

	public void setT2dIn(int t2dIn) {
		this.t2dIn = t2dIn;
	}

	public void setT2dOut(int t2dOut) {
		this.t2dOut = t2dOut;
	}

	public void setT1lIn(int t1lIn) {
		this.t1lIn = t1lIn;
	}

	public void setT1lOut(int t1lOut) {
		this.t1lOut = t1lOut;
	}

	public void setT2lIn(int t2lIn) {
		this.t2lIn = t2lIn;
	}

	public void setT2lOut(int t2lOut) {
		this.t2lOut = t2lOut;
	}

}
