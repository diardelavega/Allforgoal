package basicStruct;

import java.sql.Date;
import java.util.List;

public class FullMatchLine {
	private long mId;
	private int comId;
	private String t1;
	private String t2;

	// match obj data
	private int ft1;
	private int ft2;
	private int ht1 = -1;
	private int ht2 = -1;
	private double _1;
	private double _2;
	private double _x;
	private double _o;
	private double _u;
	private Date dat;
	private String matchTime;

	// pred points
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

	public FullMatchLine(long mId, int comId, String t1, String t2, int ft1,
			int ft2, int ht1, int ht2, double _1, double _2, double _x,
			double _o, double _u, Date dat, String matchTime, String h1,
			String hx, String h2, String so, String su, String p1y, String p1n,
			String p2y, String p2n, String ht, String ft) {
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

	public long getmId() {
		return mId;
	}

	public int getComId() {
		return comId;
	}

	public String getT1() {
		return t1;
	}

	public String getT2() {
		return t2;
	}

	public int getFt1() {
		return ft1;
	}

	public int getFt2() {
		return ft2;
	}

	public int getHt1() {
		return ht1;
	}

	public int getHt2() {
		return ht2;
	}

	public double get_1() {
		return _1;
	}

	public double get_2() {
		return _2;
	}

	public double get_x() {
		return _x;
	}

	public double get_o() {
		return _o;
	}

	public double get_u() {
		return _u;
	}

	public Date getDat() {
		return dat;
	}

	public String getMatchTime() {
		return matchTime;
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

	public void setmId(long mId) {
		this.mId = mId;
	}

	public void setComId(int comId) {
		this.comId = comId;
	}

	public void setT1(String t1) {
		this.t1 = t1;
	}

	public void setT2(String t2) {
		this.t2 = t2;
	}

	public void setFt1(int ft1) {
		this.ft1 = ft1;
	}

	public void setFt2(int ft2) {
		this.ft2 = ft2;
	}

	public void setHt1(int ht1) {
		this.ht1 = ht1;
	}

	public void setHt2(int ht2) {
		this.ht2 = ht2;
	}

	public void set_1(double _1) {
		this._1 = _1;
	}

	public void set_2(double _2) {
		this._2 = _2;
	}

	public void set_x(double _x) {
		this._x = _x;
	}

	public void set_o(double _o) {
		this._o = _o;
	}

	public void set_u(double _u) {
		this._u = _u;
	}

	public void setDat(Date dat) {
		this.dat = dat;
	}

	public void setMatchTime(String matchTime) {
		this.matchTime = matchTime;
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

}
