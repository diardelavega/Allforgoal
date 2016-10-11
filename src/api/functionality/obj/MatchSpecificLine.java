package api.functionality.obj;

import basicStruct.FullMatchLine;

/**
 * @author Administrator
 *
 *         data for the match specific page
 */
public class MatchSpecificLine {
	// comp data, teams, odds, pred points, date time.
	private FullMatchLine fml = new FullMatchLine();

	private float t1WinIn; // db
	private float t1WinOut; // db
	private float t1DrawIn; // db
	private float t1DrawOut; // db
	private float t1LoseIn; // db
	private float t1LoseOut; // db
	private float t1avgWinCont; // db
	private float t1avgDrawCont;
	private float t1avgLoseCont;
	private float t1HtGG;// db gg/matches
	private float t1FtGG;
	private int t1Over_nr;// train
	private int t1Under_nr;// train
	private int t1GG_nr;// db

	private int t1point;
	private int t1position;
	private float t1form;
	private float t1avgHtScoreIn;// for match specific page
	private float t1avgHtScoreOut;// for match specific page
	private float t1avgHtConcedeIn;// for match specific page
	private float t1avgHtConcedeOut;// for match specific page
	private float t1avgFtScoreIn;// for match specific page
	private float t1avgFtScoreOut;// for match specific page
	private float t1avgFtConcedeIn;// for match specific page
	private float t1avgFtConcedeOut;// for match specific page

	private float t2WinIn; // db
	private float t2WinOut; // db
	private float t2DrawIn; // db
	private float t2DrawOut; // db
	private float t2LoseIn; // db
	private float t2LoseOut; // db
	private float t2avgWinCont;
	private float t2avgDrawCont;
	private float t2avgLoseCont;
	private float t2HtGG;
	private float t2FtGG;
	private int t2Over_nr;
	private int t2Under_nr;
	private int t2GG_nr;

	private int t2point;
	private int t2position;
	private float t2form;
	private float t2avgHtScoreIn;// for match specific page
	private float t2avgHtScoreOut;// for match specific page
	private float t2avgHtConcedeIn;// for match specific page
	private float t2avgHtConcedeOut;// for match specific page
	private float t2avgFtScoreIn;// for match specific page
	private float t2avgFtScoreOut;// for match specific page
	private float t2avgFtConcedeIn;// for match specific page
	private float t2avgFtConcedeOut;// for match specific page

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
		msl.t1HtGG = t1avgHtGG;
		msl.t1FtGG = t1avgFtGG;
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
		msl.t2HtGG = t2avgHtGG;
		msl.t2FtGG = t2avgFtGG;
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

	public void addTo(MatchSpecificLine msl, int t1Over_nr, int t1Under_nr, int t1gg_nr, int t2Over_nr, int t2Under_nr,
			int t2gg_nr) {
		msl.t1Over_nr = t1Over_nr;
		msl.t1Under_nr = t1Under_nr;
		msl.t1GG_nr = t1gg_nr;
		msl.t2Over_nr = t2Over_nr;
		msl.t2Under_nr = t2Under_nr;
		msl.t2GG_nr = t2gg_nr;
	}

	
	
	public int getT1position() {
		return t1position;
	}

	public void setT1position(int t1position) {
		this.t1position = t1position;
	}

	public int getT2position() {
		return t2position;
	}

	public void setT2position(int t2position) {
		this.t2position = t2position;
	}

	public float getT1form() {
		return t1form;
	}

	public void setT1form(float t1form) {
		this.t1form = t1form;
	}

	public float getT2form() {
		return t2form;
	}

	public void setT2form(float t2form) {
		this.t2form = t2form;
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

	public float getT1WinIn() {
		return t1WinIn;
	}

	public void setT1WinIn(float t1WinIn) {
		this.t1WinIn = t1WinIn;
	}

	public float getT1WinOut() {
		return t1WinOut;
	}

	public void setT1WinOut(float t1WinOut) {
		this.t1WinOut = t1WinOut;
	}

	public float getT1DrawIn() {
		return t1DrawIn;
	}

	public void setT1DrawIn(float t1DrawIn) {
		this.t1DrawIn = t1DrawIn;
	}

	public float getT1DrawOut() {
		return t1DrawOut;
	}

	public void setT1DrawOut(float t1DrawOut) {
		this.t1DrawOut = t1DrawOut;
	}

	public float getT1LoseIn() {
		return t1LoseIn;
	}

	public void setT1LoseIn(float t1LoseIn) {
		this.t1LoseIn = t1LoseIn;
	}

	public float getT1LoseOut() {
		return t1LoseOut;
	}

	public void setT1LoseOut(float t1LoseOut) {
		this.t1LoseOut = t1LoseOut;
	}

	public float getT2WinIn() {
		return t2WinIn;
	}

	public void setT2WinIn(float t2WinIn) {
		this.t2WinIn = t2WinIn;
	}

	public float getT2WinOut() {
		return t2WinOut;
	}

	public void setT2WinOut(float t2WinOut) {
		this.t2WinOut = t2WinOut;
	}

	public float getT2DrawIn() {
		return t2DrawIn;
	}

	public void setT2DrawIn(float t2DrawIn) {
		this.t2DrawIn = t2DrawIn;
	}

	public float getT2DrawOut() {
		return t2DrawOut;
	}

	public void setT2DrawOut(float t2DrawOut) {
		this.t2DrawOut = t2DrawOut;
	}

	public float getT2LoseIn() {
		return t2LoseIn;
	}

	public void setT2LoseIn(float t2LoseIn) {
		this.t2LoseIn = t2LoseIn;
	}

	public float getT2LoseOut() {
		return t2LoseOut;
	}

	public void setT2LoseOut(float t2LoseOut) {
		this.t2LoseOut = t2LoseOut;
	}

	public float getT1HtGG() {
		return t1HtGG;
	}

	public void setT1HtGG(float t1HtGG) {
		this.t1HtGG = t1HtGG;
	}

	public float getT1FtGG() {
		return t1FtGG;
	}

	public void setT1FtGG(float t1FtGG) {
		this.t1FtGG = t1FtGG;
	}

	public float getT2HtGG() {
		return t2HtGG;
	}

	public void setT2HtGG(float t2HtGG) {
		this.t2HtGG = t2HtGG;
	}

	public float getT2FtGG() {
		return t2FtGG;
	}

	public void setT2FtGG(float t2FtGG) {
		this.t2FtGG = t2FtGG;
	}

	
}
