package basicStruct;

import java.sql.Date;

public class MatchObj {

	private long mId;
	private int comId;
	private String t1;
	private String t2;
	private int ft1;
	private int ft2;
	private int ht1;
	private int ht2;
	private double _1;
	private double _2;
	private double _x;

	private double _o;
	private double _u;
	private Date dat;

	public MatchObj(long mId, int comId, String t1, String t2, int ft1,
			int ft2, int ht1, int ht2, double _1, double _2, double _x,
			double _o, double _u, Date dat) {
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
	}

	public MatchObj() {
	}

	public long getmId() {
		return mId;
	}

	public void setmId(long mId) {
		this.mId = mId;
	}

	public int getComId() {
		return comId;
	}

	public void setComId(int comId) {
		this.comId = comId;
	}

	public String getT1() {
		return t1;
	}

	public void setT1(String t1) {
		this.t1 = t1;
	}

	public String getT2() {
		return t2;
	}

	public void setT2(String t2) {
		this.t2 = t2;
	}

	public int getFt1() {
		return ft1;
	}

	public void setFt1(int ft1) {
		this.ft1 = ft1;
	}

	public int getFt2() {
		return ft2;
	}

	public void setFt2(int ft2) {
		this.ft2 = ft2;
	}

	public int getHt1() {
		return ht1;
	}

	public void setHt1(int ht1) {
		this.ht1 = ht1;
	}

	public int getHt2() {
		return ht2;
	}

	public void setHt2(int ht2) {
		this.ht2 = ht2;
	}

	public double get_1() {
		return _1;
	}

	public void set_1(double _1) {
		this._1 = _1;
	}

	public double get_2() {
		return _2;
	}

	public void set_2(double _2) {
		this._2 = _2;
	}

	public double get_x() {
		return _x;
	}

	public void set_x(double _x) {
		this._x = _x;
	}

	public double get_o() {
		return _o;
	}

	public void set_o(double _o) {
		this._o = _o;
	}

	public double get_u() {
		return _u;
	}

	public void set_u(double _u) {
		this._u = _u;
	}

	public Date getDat() {
		return dat;
	}

	public void setDat(Date dat) {
		this.dat = dat;
	}

	public boolean isFull() {
		try {
			if (ht1 >= 0)
				return true;
		} catch (Exception e) {
			return false;
		}
		return false;
	}

	public String printMatch() {
		StringBuilder sb = new StringBuilder();
		sb.append(dat);
		sb.append(",  ");
		sb.append(comId);
		sb.append(",  ");
		sb.append(t1);
		sb.append(",  ");
		sb.append(t2);
		sb.append(",  ");
		sb.append(ft1);
		sb.append(",  ");
		sb.append(ft2);
		sb.append(",  ");
		sb.append(ht1);
		sb.append(",  ");
		sb.append(ht2);
		sb.append(",  ");
		sb.append(_1);
		sb.append(",  ");
		sb.append(_2);
		sb.append(",  ");
		sb.append(_x);
		sb.append(",  ");
		sb.append(_o);
		sb.append(",  ");
		sb.append(_u);
//		sb.append(",  ");
		System.out.println(sb.toString());
		return sb.toString();

	}
}
