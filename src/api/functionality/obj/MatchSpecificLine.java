package api.functionality.obj;

import basicStruct.FullMatchLine;

/**
 * @author Administrator
 *
 *data for the match specific page
 */
public class MatchSpecificLine {
	// comp data, teams, odds, pred points, date time.
	private FullMatchLine fml = new FullMatchLine();

	private float t1avgWinCont = 0;
	private float t1avgDrawCont = 0;
	private float t1avgLoseCont = 0;
	private float t1avgHtGG = 0;
	private float t1avgFtGG = 0;
	private int t1Over_nr= 0;
	private int t1Under_nr= 0;
	private int t1GG_nr= 0;

	private int t1point = 0;
	private float t1avgHtScoreIn = 0;// for match specific page
	private float t1avgHtScoreOut = 0;// for match specific page
	private float t1avgHtConcedeIn = 0;// for match specific page
	private float t1avgHtConcedeOut = 0;// for match specific page
	private float t1avgFtScoreIn = 0;// for match specific page
	private float t1avgFtScoreOut = 0;// for match specific page
	private float t1avgFtConcedeIn = 0;// for match specific page
	private float t1avgFtConcedeOut = 0;// for match specific page

	private float t2avgWinCont = 0;
	private float t2avgDrawCont = 0;
	private float t2avgLoseCont = 0;
	private float t2avgHtGG = 0;
	private float t2avgFtGG = 0;
	private int t2Over_nr= 0;
	private int t2Under_nr= 0;
	private int t2GG_nr= 0;

	private int t2point = 0;
	private float t2avgHtScoreIn = 0;// for match specific page
	private float t2avgHtScoreOut = 0;// for match specific page
	private float t2avgHtConcedeIn = 0;// for match specific page
	private float t2avgHtConcedeOut = 0;// for match specific page
	private float t2avgFtScoreIn = 0;// for match specific page
	private float t2avgFtScoreOut = 0;// for match specific page
	private float t2avgFtConcedeIn = 0;// for match specific page
	private float t2avgFtConcedeOut = 0;// for match specific page

	public MatchSpecificLine() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void addTo_t1(MatchSpecificLine msl, float t1avgWinCont, float t1avgDrawCont, float t1avgLoseCont,
			float t1avgHtGG, float t1avgFtGG, int t1point, float t1avgHtScoreIn, float t1avgHtScoreOut,
			float t1avgHtConcedeIn, float t1avgHtConcedeOut, float t1avgFtScoreIn, float t1avgFtScoreOut,
			float t1avgFtConcedeIn, float t1avgFtConcedeOut) {
		msl.t1avgWinCont = t1avgWinCont;
		msl.t1avgDrawCont = t1avgDrawCont;
		msl.t1avgLoseCont = t1avgLoseCont;
		msl.t1avgHtGG = t1avgHtGG;
		msl.t1avgFtGG = t1avgFtGG;
		msl.t1point = t1point;
		msl.t1avgHtScoreIn = t1avgHtScoreIn;
		msl.t1avgHtScoreOut = t1avgHtScoreOut;
		msl.t1avgHtConcedeIn = t1avgHtConcedeIn;
		msl.t1avgHtConcedeOut = t1avgHtConcedeOut;
		msl.t1avgFtScoreIn = t1avgFtScoreIn;
		msl.t1avgFtScoreOut = t1avgFtScoreOut;
		msl.t1avgFtConcedeIn = t1avgFtConcedeIn;
		msl.t1avgFtConcedeOut = t1avgFtConcedeOut;
	}

	public void addTo_t2(MatchSpecificLine msl, float t2avgWinCont, float t2avgDrawCont, float t2avgLoseCont,
			float t2avgHtGG, float t2avgFtGG, int t2point, float t2avgHtScoreIn, float t2avgHtScoreOut,
			float t2avgHtConcedeIn, float t2avgHtConcedeOut, float t2avgFtScoreIn, float t2avgFtScoreOut,
			float t2avgFtConcedeIn, float t2avgFtConcedeOut) {
		msl.t2avgWinCont = t2avgWinCont;
		msl.t2avgDrawCont = t2avgDrawCont;
		msl.t2avgLoseCont = t2avgLoseCont;
		msl.t2avgHtGG = t2avgHtGG;
		msl.t2avgFtGG = t2avgFtGG;
		msl.t2point = t2point;
		msl.t2avgHtScoreIn = t2avgHtScoreIn;
		msl.t2avgHtScoreOut = t2avgHtScoreOut;
		msl.t2avgHtConcedeIn = t2avgHtConcedeIn;
		msl.t2avgHtConcedeOut = t2avgHtConcedeOut;
		msl.t2avgFtScoreIn = t2avgFtScoreIn;
		msl.t2avgFtScoreOut = t2avgFtScoreOut;
		msl.t2avgFtConcedeIn = t2avgFtConcedeIn;
		msl.t2avgFtConcedeOut = t2avgFtConcedeOut;
	}
		
	public void addTo( MatchSpecificLine msl,int t1Over_nr, int t1Under_nr, int t1gg_nr, int t2Over_nr, int t2Under_nr, int t2gg_nr) {
		msl.t1Over_nr = t1Over_nr;
		msl.t1Under_nr = t1Under_nr;
		msl.t1GG_nr = t1gg_nr;
		msl.t2Over_nr = t2Over_nr;
		msl.t2Under_nr = t2Under_nr;
		msl.t2GG_nr = t2gg_nr;
	}

	public int getT1point() {
		return t1point;
	}

	public void setT1point(int t1point) {
		this.t1point = t1point;
	}

	public float getT1avgHtScoreIn() {
		return t1avgHtScoreIn;
	}

	public void setT1avgHtScoreIn(float t1avgHtScoreIn) {
		this.t1avgHtScoreIn = t1avgHtScoreIn;
	}

	public float getT1avgHtScoreOut() {
		return t1avgHtScoreOut;
	}

	public void setT1avgHtScoreOut(float t1avgHtScoreOut) {
		this.t1avgHtScoreOut = t1avgHtScoreOut;
	}

	public float getT1avgHtConcedeIn() {
		return t1avgHtConcedeIn;
	}

	public void setT1avgHtConcedeIn(float t1avgHtConcedeIn) {
		this.t1avgHtConcedeIn = t1avgHtConcedeIn;
	}

	public float getT1avgHtConcedeOut() {
		return t1avgHtConcedeOut;
	}

	public void setT1avgHtConcedeOut(float t1avgHtConcedeOut) {
		this.t1avgHtConcedeOut = t1avgHtConcedeOut;
	}

	public float getT1avgFtScoreIn() {
		return t1avgFtScoreIn;
	}

	public void setT1avgFtScoreIn(float t1avgFtScoreIn) {
		this.t1avgFtScoreIn = t1avgFtScoreIn;
	}

	public float getT1avgFtScoreOut() {
		return t1avgFtScoreOut;
	}

	public void setT1avgFtScoreOut(float t1avgFtScoreOut) {
		this.t1avgFtScoreOut = t1avgFtScoreOut;
	}

	public float getT1avgFtConcedeIn() {
		return t1avgFtConcedeIn;
	}

	public void setT1avgFtConcedeIn(float t1avgFtConcedeIn) {
		this.t1avgFtConcedeIn = t1avgFtConcedeIn;
	}

	public float getT1avgFtConcedeOut() {
		return t1avgFtConcedeOut;
	}

	public void setT1avgFtConcedeOut(float t1avgFtConcedeOut) {
		this.t1avgFtConcedeOut = t1avgFtConcedeOut;
	}

	public int getT2point() {
		return t2point;
	}

	public void setT2point(int t2point) {
		this.t2point = t2point;
	}

	public float getT2avgHtScoreIn() {
		return t2avgHtScoreIn;
	}

	public void setT2avgHtScoreIn(float t2avgHtScoreIn) {
		this.t2avgHtScoreIn = t2avgHtScoreIn;
	}

	public float getT2avgHtScoreOut() {
		return t2avgHtScoreOut;
	}

	public void setT2avgHtScoreOut(float t2avgHtScoreOut) {
		this.t2avgHtScoreOut = t2avgHtScoreOut;
	}

	public float getT2avgHtConcedeIn() {
		return t2avgHtConcedeIn;
	}

	public void setT2avgHtConcedeIn(float t2avgHtConcedeIn) {
		this.t2avgHtConcedeIn = t2avgHtConcedeIn;
	}

	public float getT2avgHtConcedeOut() {
		return t2avgHtConcedeOut;
	}

	public void setT2avgHtConcedeOut(float t2avgHtConcedeOut) {
		this.t2avgHtConcedeOut = t2avgHtConcedeOut;
	}

	public float getT2avgFtScoreIn() {
		return t2avgFtScoreIn;
	}

	public void setT2avgFtScoreIn(float t2avgFtScoreIn) {
		this.t2avgFtScoreIn = t2avgFtScoreIn;
	}

	public float getT2avgFtScoreOut() {
		return t2avgFtScoreOut;
	}

	public void setT2avgFtScoreOut(float t2avgFtScoreOut) {
		this.t2avgFtScoreOut = t2avgFtScoreOut;
	}

	public float getT2avgFtConcedeIn() {
		return t2avgFtConcedeIn;
	}

	public void setT2avgFtConcedeIn(float t2avgFtConcedeIn) {
		this.t2avgFtConcedeIn = t2avgFtConcedeIn;
	}

	public float getT2avgFtConcedeOut() {
		return t2avgFtConcedeOut;
	}

	public void setT2avgFtConcedeOut(float t2avgFtConcedeOut) {
		this.t2avgFtConcedeOut = t2avgFtConcedeOut;
	}

	public FullMatchLine getFml() {
		return fml;
	}

	public void setFml(FullMatchLine fml) {
		this.fml = fml;
	}

	public float getT1avgWinCont() {
		return t1avgWinCont;
	}

	public void setT1avgWinCont(float t1avgWinCont) {
		this.t1avgWinCont = t1avgWinCont;
	}

	public float getT1avgDrawCont() {
		return t1avgDrawCont;
	}

	public void setT1avgDrawCont(float t1avgDrawCont) {
		this.t1avgDrawCont = t1avgDrawCont;
	}

	public float getT1avgLoseCont() {
		return t1avgLoseCont;
	}

	public void setT1avgLoseCont(float t1avgLoseCont) {
		this.t1avgLoseCont = t1avgLoseCont;
	}

	public float getT1avgHtGG() {
		return t1avgHtGG;
	}

	public void setT1avgHtGG(float t1avgHtGG) {
		this.t1avgHtGG = t1avgHtGG;
	}

	public float getT1avgFtGG() {
		return t1avgFtGG;
	}

	public void setT1avgFtGG(float t1avgFtGG) {
		this.t1avgFtGG = t1avgFtGG;
	}

	public float getT2avgWinCont() {
		return t2avgWinCont;
	}

	public void setT2avgWinCont(float t2avgWinCont) {
		this.t2avgWinCont = t2avgWinCont;
	}

	public float getT2avgDrawCont() {
		return t2avgDrawCont;
	}

	public void setT2avgDrawCont(float t2avgDrawCont) {
		this.t2avgDrawCont = t2avgDrawCont;
	}

	public float getT2avgLoseCont() {
		return t2avgLoseCont;
	}

	public void setT2avgLoseCont(float t2avgLoseCont) {
		this.t2avgLoseCont = t2avgLoseCont;
	}

	public float getT2avgHtGG() {
		return t2avgHtGG;
	}

	public void setT2avgHtGG(float t2avgHtGG) {
		this.t2avgHtGG = t2avgHtGG;
	}

	public float getT2avgFtGG() {
		return t2avgFtGG;
	}

	public void setT2avgFtGG(float t2avgFtGG) {
		this.t2avgFtGG = t2avgFtGG;
	}

	public int getT1Over_nr() {
		return t1Over_nr;
	}

	public void setT1Over_nr(int t1Over_nr) {
		this.t1Over_nr = t1Over_nr;
	}

	public int getT1Under_nr() {
		return t1Under_nr;
	}

	public void setT1Under_nr(int t1Under_nr) {
		this.t1Under_nr = t1Under_nr;
	}

	public int getT1GG_nr() {
		return t1GG_nr;
	}

	public void setT1GG_nr(int t1gg_nr) {
		t1GG_nr = t1gg_nr;
	}

	public int getT2Over_nr() {
		return t2Over_nr;
	}

	public void setT2Over_nr(int t2Over_nr) {
		this.t2Over_nr = t2Over_nr;
	}

	public int getT2Under_nr() {
		return t2Under_nr;
	}

	public void setT2Under_nr(int t2Under_nr) {
		this.t2Under_nr = t2Under_nr;
	}

	public int getT2GG_nr() {
		return t2GG_nr;
	}

	public void setT2GG_nr(int t2gg_nr) {
		t2GG_nr = t2gg_nr;
	}

	
}
