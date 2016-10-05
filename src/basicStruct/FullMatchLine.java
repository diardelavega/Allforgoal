package basicStruct;

import java.sql.Date;

public class FullMatchLine extends MatchObj {

	// pred points
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

	public FullMatchLine() {
		super();
	}

	public FullMatchLine(MatchObj m) {
		this.mId = m.getmId();
		this.comId = m.getComId();
		this.t1 = m.getT1();
		this.t2 = m.getT2();
		this.matchTime = m.getMatchTime();
		this.dat = m.getDat();
		this._1 = m.get_1();
		this._x = m.get_x();
		this._2 = m.get_2();

		this._o = m.get_o();
		this._u = m.get_u();
		this.ht1 = m.getHt1();
		this.ht2 = m.getHt2();
		this.ft1 = m.getFt1();
		this.ft2 = m.getFt2();

	}

	public FullMatchLine(long mId, int comId, String t1, String t2, int ft1, int ft2, int ht1, int ht2, float _1,
			float _2, float _x, float _o, float _u, Date dat, String matchTime, float h1, float hx, float h2, float so,
			float su, float p1y, float p1n, float p2y, float p2n, int ht, int ft) {
		super();
		this.mId = mId;
		this.comId = comId;
		this.t1 = t1;
		this.t2 = t2;
		this.ft1 = ft1;
		this.ft2 = ft2;
		this.ht1 = ht1;
		this.ht2 = ht2;
		this._1 = _1;
		this._2 = _2;
		this._x = _x;
		this._o = _o;
		this._u = _u;
		this.dat = dat;
		this.matchTime = matchTime;
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

	public String shower() {
		// StringBuilder sb = new StringBuilder();
		String s = s = super.printMatch();
		s+=", ";
		s += h1 + ", ";
		s += hx + ", ";
		s += h2 + ", ";
		s += so + ", ";
		s += su + ", ";
		s += p1y + ", ";
		s += p1n + ", ";
		s += p2y + ", ";
		s += p2n + ", ";
		s += ht + ", ";
		s += "ft:"+ft + " ";
//		s = super.printMatch() + s;
		return s;
	}

}