package api.functionality.obj;

import java.sql.Date;
import java.time.LocalDate;

import basicStruct.MatchPredictionLine;

public class RedMPL extends MatchPredictionLine{

	private String t1;
	private String t2;
	private int ft1;// score
	private int ft2;
	private int ht1;
	private int ht2;// score
	private float h1;
	private float hx;
	private float h2;
	private float so;
	private float su;
	private Date dat;
	private String matchTime;

	public RedMPL(String t1, String t2, int ft1, int ft2, int ht1, int ht2, float h1, float hx, float h2, float so,
			float su, Date dat, String matchTime) {
		super();
		this.t1 = t1;
		this.t2 = t2;
		this.ft1 = ft1;
		this.ft2 = ft2;
		this.ht1 = ht1;
		this.ht2 = ht2;
		this.h1 = h1;
		this.hx = hx;
		this.h2 = h2;
		this.so = so;
		this.su = su;
		this.dat = dat;
		this.matchTime = matchTime;
	}

	public RedMPL() {
		super();
		// TODO Auto-generated constructor stub
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

	public float getH1() {
		return h1;
	}

	public void setH1(float h1) {
		this.h1 = h1;
	}

	public float getHx() {
		return hx;
	}

	public void setHx(float hx) {
		this.hx = hx;
	}

	public float getH2() {
		return h2;
	}

	public void setH2(float h2) {
		this.h2 = h2;
	}

	public float getSo() {
		return so;
	}

	public void setSo(float so) {
		this.so = so;
	}

	public float getSu() {
		return su;
	}

	public void setSu(float su) {
		this.su = su;
	}

	public Date getDat() {
		return dat;
	}

	public void setDat(Date dat) {
		this.dat = dat;
	}

	public LocalDate getLocalDat() {
		return dat.toLocalDate();
	}

	public void setLocalDat(LocalDate dat) {
		this.dat = Date.valueOf(dat);
	}

	public String getMatchTime() {
		return matchTime;
	}

	public void setMatchTime(String matchTime) {
		this.matchTime = matchTime;
	}

}
