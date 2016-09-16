package api.functionality.obj;

public class BaseMatchLinePred {
	protected String t1;
	protected String t2;

	private int ht1;
	private int ht2;
	private int ft1;
	private int ft2;

	private String h1;
	private String hx;
	private String h2;
	private String so;
	private String su;

	private String p1y;
	private String p1n;

	private String p2y;
	private String p2n;

	private String ht;
	private String ft;

	public BaseMatchLinePred() {
		super();
	}

	public BaseMatchLinePred(String t1, String t2, String h1, String hx,
			String h2, String so, String su, String p1y, String p1n,
			String p2y, String p2n, String ht, String ft) {
		super();
		this.t1 = t1;
		this.t2 = t2;
		this.h1 = h1;
		this.hx = hx;
		this.h2 = h2;
		this.so = so;
		this.su = su;
		this.p1y = p1y;
		this.p1n = p1n;
		this.p2y = p2y;
		this.p2n = p2n;
		this.ht = ht;
		this.ft = ft;
	}

	public BaseMatchLinePred(String t1, String t2, int ht1, int ht2, int ft1,
			int ft2, String _1, String _x, String _2, String _o, String _u,
			String p1y, String p1n, String p2y, String p2n, String ht, String ft) {
		super();
		this.t1 = t1;
		this.t2 = t2;
		this.ht1 = ht1;
		this.ht2 = ht2;
		this.ft1 = ft1;
		this.ft2 = ft2;
		this.h1 = _1;
		this.hx = _x;
		this.h2 = _2;
		this.so = _o;
		this.su = _u;
		this.p1y = p1y;
		this.p1n = p1n;
		this.p2y = p2y;
		this.p2n = p2n;
		this.ht = ht;
		this.ft = ft;
	}

	public String getT1() {
		return t1;
	}

	public String getT2() {
		return t2;
	}

	public String getH1() {
		return h1;
	}

	public String getHx() {
		return hx;
	}

	public String getH2() {
		return h2;
	}

	public String getSo() {
		return so;
	}

	public String getSu() {
		return su;
	}

	public void setH1(String h1) {
		this.h1 = h1;
	}

	public void setHx(String hx) {
		this.hx = hx;
	}

	public void setH2(String h2) {
		this.h2 = h2;
	}

	public void setSo(String so) {
		this.so = so;
	}

	public void setSu(String su) {
		this.su = su;
	}

	public String getP1y() {
		return p1y;
	}

	public String getP1n() {
		return p1n;
	}

	public String getP2y() {
		return p2y;
	}

	public String getP2n() {
		return p2n;
	}

	public String getHt() {
		return ht;
	}

	public String getFt() {
		return ft;
	}

	public void setT1(String t1) {
		this.t1 = t1;
	}

	public void setT2(String t2) {
		this.t2 = t2;
	}

	public void setP1y(String p1y) {
		this.p1y = p1y;
	}

	public void setP1n(String p1n) {
		this.p1n = p1n;
	}

	public void setP2y(String p2y) {
		this.p2y = p2y;
	}

	public void setP2n(String p2n) {
		this.p2n = p2n;
	}

	public void setHt(String ht) {
		this.ht = ht;
	}

	public void setFt(String ft) {
		this.ft = ft;
	}

	public void matchPredLineShow() {
		StringBuilder sb = new StringBuilder();
		sb.append(t1);
		sb.append(" ");
		sb.append(t2);
		sb.append(" ");
		sb.append(h1);
		sb.append(" ");
		sb.append(hx);
		sb.append(" ");
		sb.append(h2);
		sb.append(" ");

		sb.append(so);
		sb.append(" ");
		sb.append(su);
		sb.append(" ");

		sb.append(p1y);
		sb.append(" ");
		sb.append(p1n);
		sb.append(" ");
		sb.append(p2y);
		sb.append(" ");
		sb.append(p2n);
		sb.append(" ");

		sb.append(ht);
		sb.append(" ");
		sb.append(ft);
		sb.append(" ");
	}

	public int getHt1() {
		return ht1;
	}

	public int getHt2() {
		return ht2;
	}

	public int getFt1() {
		return ft1;
	}

	public int getFt2() {
		return ft2;
	}

	public void setHt1(int ht1) {
		this.ht1 = ht1;
	}

	public void setHt2(int ht2) {
		this.ht2 = ht2;
	}

	public void setFt1(int ft1) {
		this.ft1 = ft1;
	}

	public void setFt2(int ft2) {
		this.ft2 = ft2;
	}

	public String liner() {
		String line = t1 + " " + ht1 + "  " + ht2 + " " + ft1 + " " + ft2 + " "
				+ t2 + " " + h1 + " " + hx + " " + h2 + " " + so + " " + su
				+ " " + p1y + " " + p1n + " " + p2y + " " + p2n + " " + ht
				+ " " + ft;
		return line;
	}

}
