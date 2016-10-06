package api.functionality.obj;

public class BaseMatchLinePred {
	protected String t1;
	protected String t2;

	private int ht1;
	private int ht2;
	private int ft1;
	private int ft2;

	private float h1;
	private float hx;
	private float h2;
	private float so;
	private float su;

	private float p1y;
	private float p1n;

	private float p2y;
	private float p2n;

	private int ht;
	private int ft;

	public BaseMatchLinePred() {
		super();
	}

	public BaseMatchLinePred(String t1, String t2, float h1, float hx,
			float h2, float so, float su, float p1y, float p1n,
			float p2y, float p2n, int ht, int ft) {
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
			int ft2, float _1, float _x, float _2, float _o, float _u,
			float p1y, float p1n, float p2y, float p2n, int ht, int ft) {
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

	public float getH1() {
		return h1;
	}

	public float getHx() {
		return hx;
	}

	public float getH2() {
		return h2;
	}

	public float getSo() {
		return so;
	}

	public float getSu() {
		return su;
	}

	public float getP1y() {
		return p1y;
	}

	public float getP1n() {
		return p1n;
	}

	public float getP2y() {
		return p2y;
	}

	public float getP2n() {
		return p2n;
	}

	public int getHt() {
		return ht;
	}

	public int getFt() {
		return ft;
	}

	public void setT1(String t1) {
		this.t1 = t1;
	}

	public void setT2(String t2) {
		this.t2 = t2;
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

	public void setH1(float h1) {
		this.h1 = h1;
	}

	public void setHx(float hx) {
		this.hx = hx;
	}

	public void setH2(float h2) {
		this.h2 = h2;
	}

	public void setSo(float so) {
		this.so = so;
	}

	public void setSu(float su) {
		this.su = su;
	}

	public void setP1y(float p1y) {
		this.p1y = p1y;
	}

	public void setP1n(float p1n) {
		this.p1n = p1n;
	}

	public void setP2y(float p2y) {
		this.p2y = p2y;
	}

	public void setP2n(float p2n) {
		this.p2n = p2n;
	}

	public void setHt(int ht) {
		this.ht = ht;
	}

	public void setFt(int ft) {
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

	
	public String liner() {
		String line = t1 + " " + ht1 + "  " + ht2 + " " + ft1 + " " + ft2 + " "
				+ t2 + " " + h1 + " " + hx + " " + h2 + " " + so + " " + su
				+ " " + p1y + " " + p1n + " " + p2y + " " + p2n + " " + ht
				+ " " + ft;
		return line;
	}

}
