package basicStruct;

import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDate;

/**
 * @author Administrator
 *
 *         contains the specific data like odds, identifiers like competition,
 *         teams, date and time, related to a match
 */
public class MatchObj extends MatchPredictionLine implements Serializable {

	private static final long serialVersionUID = 1L;
	protected long mId;
	protected int comId;
	protected String t1;
	protected String t2;
	protected int ft1;// score
	protected int ft2;
	protected int ht1;
	protected int ht2;// score
	protected float _1 = 1;// odds
	protected float _2 = 1;
	protected float _x = 1;
	protected float _o = 1;
	protected float _u = 1;// odds
	protected Date dat;
	protected String matchTime;

	public MatchObj(long mId, int comId, String t1, String t2, int ft1, int ft2, int ht1, int ht2, float _1, float _2,
			float _x, float _o, float _u, Date dat) {
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

	public MatchObj(long mId, int comId, String t1, String t2, int ft1, int ft2, int ht1, int ht2, float _1, float _2,
			float _x, float _o, float _u, Date dat, String mtime) {
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
		this.matchTime = mtime;
	}

	public MatchObj(long mId, int comId, String t1, String t2, int ft1, int ft2, int ht1, int ht2, float _1, float _2,
			float _x, float _o, float _u, LocalDate date, String mtime) {
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
		this.dat = Date.valueOf(date);
		this.matchTime = mtime;
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

	public float get_1() {
		return _1;
	}

	public void set_1(float _1) {
		this._1 = _1;
	}

	public float get_2() {
		return _2;
	}

	public void set_2(float _2) {
		this._2 = _2;
	}

	public float get_x() {
		return _x;
	}

	public void set_x(float _x) {
		this._x = _x;
	}

	public float get_o() {
		return _o;
	}

	public void set_o(float _o) {
		this._o = _o;
	}

	public float get_u() {
		return _u;
	}

	public void set_u(float _u) {
		this._u = _u;
	}

	public Date getDat() {
		return dat;
	}

	public LocalDate getLocalDat() {
		return dat.toLocalDate();
	}

	public void setLocalDat(LocalDate dat) {
		this.dat = Date.valueOf(dat);
	}

	public void setDat(Date dat) {
		this.dat = dat;
	}

	public String getMatchTime() {
		return matchTime;
	}

	public void setMatchTime(String matchTime) {
		this.matchTime = matchTime;
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
		sb.append(mId);
		sb.append(",  ");
		sb.append(dat);
		sb.append(",  ");
		sb.append(matchTime);
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
		// sb.append(", ");
		// System.out.println(sb.toString());
		return sb.toString();

	}
}
