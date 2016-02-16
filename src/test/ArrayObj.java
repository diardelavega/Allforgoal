package test;

public class ArrayObj implements Comparable<ArrayObj> {
	private int att1;
	private int att2;
	private String att3;

	// private char att4;

	public ArrayObj() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ArrayObj(int att1, int att2, String att3) {
		super();
		this.att1 = att1;
		this.att2 = att2;
		this.att3 = att3;
	}

	public int getAtt1() {
		return att1;
	}

	public int getAtt2() {
		return att2;
	}

	public void setAtt1(int att1) {
		this.att1 = att1;
	}

	public void setAtt2(int att2) {
		this.att2 = att2;
	}

	public String getAtt3() {
		return att3;
	}

	public void setAtt3(String att3) {
		this.att3 = att3;
	}

	public void print() {
		System.out.println(att1 + ", " + att2 + ", " + att3);
	}

	@Override
	public int compareTo(ArrayObj o) {
		int compare = o.getAtt2();
		if (compare == this.att2)
			return o.getAtt1() - this.att1;
		else {
			return compare - this.att2;
		}
	}

}
