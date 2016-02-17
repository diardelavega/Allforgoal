package structures;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PredictionFile {
	private static final Logger logger = LoggerFactory
			.getLogger(PredictionFile.class);
	private int week = 0;
	private String headOutcome; // MatchOutcome val 1/x/2
	private String scoreOutcome; // o/u

	private float bet_1 = 0;
	private float bet_2 = 0;
	private float bet_X = 0;
	private float bet_O = 0;
	private float bet_U = 0;

	private String t1;
	private int t1Points = 0; // top, mid_up, mid_down, bot_table
	private String t1Classification;
	private float t1Form = 0;
	private float t1Form1Diff = 0;
	private float t1Form2Diff = 0;
	private float t1Form3Diff = 0;
	private float t1Form4Diff = 0;
	private float t1AtackIn = 0;
	private float t1AtackOut = 0;
	private float t1DefenseIn = 0;
	private float t1DefenseOut = 0;

	private String t2;
	private int t2Points = 0;
	private String t2Classification;
	private float t2Form = 0;
	private float t2Form1Diff = 0;
	private float t2Form2Diff = 0;
	private float t2Form3Diff = 0;
	private float t2Form4Diff = 0;
	private float t2AtackIn = 0;
	private float t2AtackOut = 0;
	private float t2DefenseIn = 0;
	private float t2DefenseOut = 0;

	public float getBet_1() {
		return bet_1;
	}

	public float getBet_2() {
		return bet_2;
	}

	public float getBet_X() {
		return bet_X;
	}

	public float getBet_O() {
		return bet_O;
	}

	public float getBet_U() {
		return bet_U;
	}

	public void setBet_1(float bet_1) {
		this.bet_1 = bet_1;
	}

	public void setBet_2(float bet_2) {
		this.bet_2 = bet_2;
	}

	public void setBet_X(float bet_X) {
		this.bet_X = bet_X;
	}

	public void setBet_O(float bet_O) {
		this.bet_O = bet_O;
	}

	public void setBet_U(float bet_U) {
		this.bet_U = bet_U;
	}

	public String getHeadOutcome() {
		return headOutcome;
	}

	public String getScoreOutcome() {
		return scoreOutcome;
	}

	public void setHeadOutcome(String headOutcome) {
		this.headOutcome = headOutcome;
	}

	public void setScoreOutcome(String scoreOutcome) {
		this.scoreOutcome = scoreOutcome;
	}

	public int getWeek() {
		return week;
	}

	
	
	public void setWeek(int week) {
		this.week = week;
	}

	public String getT1() {
		return t1;
	}

	public int getT1Points() {
		return t1Points;
	}

	public String getT1Classification() {
		return t1Classification;
	}

	public float getT1Form() {
		return t1Form;
	}

	public float getT1Form1Diff() {
		return t1Form1Diff;
	}

	public float getT1Form2Diff() {
		return t1Form2Diff;
	}

	public float getT1Form3Diff() {
		return t1Form3Diff;
	}

	public float getT1Form4Diff() {
		return t1Form4Diff;
	}

	public float getT1AtackIn() {
		return t1AtackIn;
	}

	public float getT1AtackOut() {
		return t1AtackOut;
	}

	public float getT1DefenseIn() {
		return t1DefenseIn;
	}

	public float getT1DefenseOut() {
		return t1DefenseOut;
	}

	public String getT2() {
		return t2;
	}

	public int getT2Points() {
		return t2Points;
	}

	public String getT2Classification() {
		return t2Classification;
	}

	public float getT2Form() {
		return t2Form;
	}

	public float getT2Form1Diff() {
		return t2Form1Diff;
	}

	public float getT2Form2Diff() {
		return t2Form2Diff;
	}

	public float getT2Form3Diff() {
		return t2Form3Diff;
	}

	public float getT2Form4Diff() {
		return t2Form4Diff;
	}

	public float getT2AtackIn() {
		return t2AtackIn;
	}

	public float getT2AtackOut() {
		return t2AtackOut;
	}

	public float getT2DefenseIn() {
		return t2DefenseIn;
	}

	public float getT2DefenseOut() {
		return t2DefenseOut;
	}

	public void upWeek() {
		this.week++;
	}

	public void setT1(String t1) {
		this.t1 = t1;
	}

	public void setT1Points(int t1Points) {
		this.t1Points = t1Points;
	}

	public void setT1Classification(String t1Classification) {
		this.t1Classification = t1Classification;
	}

	public void setT1Form(float t1Form) {
		this.t1Form = t1Form;
	}

	public void setT1Form1Diff(float t1Form1Diff) {
		this.t1Form1Diff = t1Form1Diff;
	}

	public void setT1Form2Diff(float t1Form2Diff) {
		this.t1Form2Diff = t1Form2Diff;
	}

	public void setT1Form3Diff(float t1Form3Diff) {
		this.t1Form3Diff = t1Form3Diff;
	}

	public void setT1Form4Diff(float t1Form4Diff) {
		this.t1Form4Diff = t1Form4Diff;
	}

	public void setT1AtackIn(float t1AtackIn) {
		this.t1AtackIn = t1AtackIn;
	}

	public void setT1AtackOut(float t1AtackOut) {
		this.t1AtackOut = t1AtackOut;
	}

	public void setT1DefenseIn(float t1DefenseIn) {
		this.t1DefenseIn = t1DefenseIn;
	}

	public void setT1DefenseOut(float t1DefenseOut) {
		this.t1DefenseOut = t1DefenseOut;
	}

	public void setT2(String t2) {
		this.t2 = t2;
	}

	public void setT2Points(int t2Points) {
		this.t2Points = t2Points;
	}

	public void setT2Classification(String t2Classification) {
		this.t2Classification = t2Classification;
	}

	public void setT2Form(float t2Form) {
		this.t2Form = t2Form;
	}

	public void setT2Form1Diff(float t2Form1Diff) {
		this.t2Form1Diff = t2Form1Diff;
	}

	public void setT2Form2Diff(float t2Form2Diff) {
		this.t2Form2Diff = t2Form2Diff;
	}

	public void setT2Form3Diff(float t2Form3Diff) {
		this.t2Form3Diff = t2Form3Diff;
	}

	public void setT2Form4Diff(float t2Form4Diff) {
		this.t2Form4Diff = t2Form4Diff;
	}

	public void setT2AtackIn(float t2AtackIn) {
		this.t2AtackIn = t2AtackIn;
	}

	public void setT2AtackOut(float t2AtackOut) {
		this.t2AtackOut = t2AtackOut;
	}

	public void setT2DefenseIn(float t2DefenseIn) {
		this.t2DefenseIn = t2DefenseIn;
	}

	public void setT2DefenseOut(float t2DefenseOut) {
		this.t2DefenseOut = t2DefenseOut;
	}

	public String liner() {
		// return a string to be stored in a file
		StringBuilder sb = new StringBuilder();
		sb.append(week);
		sb.append(" ,");
		sb.append(headOutcome);
		sb.append(" ,");
		sb.append(scoreOutcome);
		sb.append(" ,");

		sb.append(t1);
		sb.append(" ,");
		sb.append(t1Points);
		sb.append(" ,");
		sb.append(t1Classification);
		sb.append(" ,");
		sb.append(t1Form);
		sb.append(" ,");
		sb.append(t1Form1Diff);
		sb.append(" ,");
		sb.append(t1Form1Diff);
		sb.append(" ,");
		sb.append(t1Form2Diff);
		sb.append(" ,");
		sb.append(t1Form3Diff);
		sb.append(" ,");
		sb.append(t1Form4Diff);
		sb.append(" ,");
		sb.append(t1AtackIn);
		sb.append(" ,");
		sb.append(t1AtackOut);
		sb.append(" ,");
		sb.append(t1DefenseIn);
		sb.append(" ,");
		sb.append(t1DefenseOut);
		sb.append(" ,");

		sb.append(t2);
		sb.append(" ,");
		sb.append(t2Points);
		sb.append(" ,");
		sb.append(t2Classification);
		sb.append(" ,");
		sb.append(t2Form);
		sb.append(" ,");
		sb.append(t2Form1Diff);
		sb.append(" ,");
		sb.append(t2Form1Diff);
		sb.append(" ,");
		sb.append(t2Form2Diff);
		sb.append(" ,");
		sb.append(t2Form3Diff);
		sb.append(" ,");
		sb.append(t2Form4Diff);
		sb.append(" ,");
		sb.append(t2AtackIn);
		sb.append(" ,");
		sb.append(t2AtackOut);
		sb.append(" ,");
		sb.append(t2DefenseIn);
		sb.append(" ,");
		sb.append(t2DefenseOut);
		sb.append(" ,");

		sb.append(bet_1);
		sb.append(" ,");
		sb.append(bet_X);
		sb.append(" ,");
		sb.append(bet_2);
		sb.append(" ,");
		sb.append(bet_O);
		sb.append(" ,");
		sb.append(bet_U);

		logger.info(sb.toString());
		
		return sb.toString();
	}

}
