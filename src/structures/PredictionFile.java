package structures;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PredictionFile {
	private static final Logger logger = LoggerFactory
			.getLogger(PredictionFile.class);
	private int week = 0;
	private String headOutcome; // MatchOutcome val 1/x/2
	private String scoreOutcome; // o/u
	// new
	private String ht1pOutcome; // y,n, m-> missing
	private String ht2pOutcome; // y,n, NA-> missing
	private String ggOutcome; // y,n
	private int totHtScore = 0;
	private int totFtScore = 0;

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
	private float t1Atack = 0;
	private float t1AtackIn = 0;
	private float t1AtackOut = 0;
	private float t1Defense = 0;
	private float t1DefenseIn = 0;
	private float t1DefenseOut = 0;
	// ---new
	private float t1AvgHtScoreIn = 0;
	private float t1AvgHtScoreOut = 0;
	private float t1AvgFtScoreIn = 0;
	private float t1AvgFtScoreOut = 0;
	private float t1AvgHtGgResult = 0;
	private float t1AvgFtGgResult = 0;
	private int t1WinsIn = 0;
	private int t1WinsOut = 0;
	private int t1DrawsIn = 0;
	private int t1DrawsOut = 0;
	private int t1LosesIn = 0;
	private int t1LosesOut = 0;

	private String t2;
	private int t2Points = 0;
	private String t2Classification;
	private float t2Form = 0;
	private float t2Form1Diff = 0;
	private float t2Form2Diff = 0;
	private float t2Form3Diff = 0;
	private float t2Form4Diff = 0;
	private float t2Atack = 0;
	private float t2AtackIn = 0;
	private float t2AtackOut = 0;
	private float t2Defense = 0;
	private float t2DefenseIn = 0;
	private float t2DefenseOut = 0;
	// ---new
	private float t2AvgHtScoreIn = 0;
	private float t2AvgHtScoreOut = 0;
	private float t2AvgFtScoreIn = 0;
	private float t2AvgFtScoreOut = 0;
	private float t2AvgHtGgResult = 0;
	private float t2AvgFtGgResult = 0;
	private int t2WinsIn = 0;
	private int t2WinsOut = 0;
	private int t2DrawsIn = 0;
	private int t2DrawsOut = 0;
	private int t2LosesIn = 0;
	private int t2LosesOut = 0;

	
	
	
	public int getTotHtScore() {
		return totHtScore;
	}

	public int getTotFtScore() {
		return totFtScore;
	}

	public void setTotHtScore(int totHtScore) {
		this.totHtScore = totHtScore;
	}

	public void setTotFtScore(int totFtScore) {
		this.totFtScore = totFtScore;
	}

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

	public float getT1Atack() {
		return t1Atack;
	}

	public float getT1AtackIn() {
		return t1AtackIn;
	}

	public float getT1AtackOut() {
		return t1AtackOut;
	}

	public float getT1Defense() {
		return t1Defense;
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

	public float getT2Atack() {
		return t2Atack;
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

	public String getGgOutcome() {
		return ggOutcome;
	}

	public int getT1WinsIn() {
		return t1WinsIn;
	}

	public int getT1WinsOut() {
		return t1WinsOut;
	}

	public int getT1DrawsIn() {
		return t1DrawsIn;
	}

	public int getT1DrawsOut() {
		return t1DrawsOut;
	}

	public int getT1LosesIn() {
		return t1LosesIn;
	}

	public int getT1LosesOut() {
		return t1LosesOut;
	}

	public int getT2WinsIn() {
		return t2WinsIn;
	}

	public int getT2WinsOut() {
		return t2WinsOut;
	}

	public int getT2DrawsIn() {
		return t2DrawsIn;
	}

	public int getT2DrawsOut() {
		return t2DrawsOut;
	}

	public int getT2LosesIn() {
		return t2LosesIn;
	}

	public int getT2LosesOut() {
		return t2LosesOut;
	}

	public void setT1WinsIn(int t1WinsIn) {
		this.t1WinsIn = t1WinsIn;
	}

	public void setT1WinsOut(int t1WinsOut) {
		this.t1WinsOut = t1WinsOut;
	}

	public void setT1DrawsIn(int t1DrawsIn) {
		this.t1DrawsIn = t1DrawsIn;
	}

	public void setT1DrawsOut(int t1DrawsOut) {
		this.t1DrawsOut = t1DrawsOut;
	}

	public void setT1LosesIn(int t1LosesIn) {
		this.t1LosesIn = t1LosesIn;
	}

	public void setT1LosesOut(int t1LosesOut) {
		this.t1LosesOut = t1LosesOut;
	}

	public void setT2WinsIn(int t2WinsIn) {
		this.t2WinsIn = t2WinsIn;
	}

	public void setT2WinsOut(int t2WinsOut) {
		this.t2WinsOut = t2WinsOut;
	}

	public void setT2DrawsIn(int t2DrawsIn) {
		this.t2DrawsIn = t2DrawsIn;
	}

	public void setT2DrawsOut(int t2DrawsOut) {
		this.t2DrawsOut = t2DrawsOut;
	}

	public void setT2LosesIn(int t2LosesIn) {
		this.t2LosesIn = t2LosesIn;
	}

	public void setT2LosesOut(int t2LosesOut) {
		this.t2LosesOut = t2LosesOut;
	}

	public void setGgOutcome(String ggOutcome) {
		this.ggOutcome = ggOutcome;
	}

	public String getHt1pOutcome() {
		return ht1pOutcome;
	}

	public String getHt2pOutcome() {
		return ht2pOutcome;
	}

	public float getT1AvgHtScoreIn() {
		return t1AvgHtScoreIn;
	}

	public float getT1AvgHtScoreOut() {
		return t1AvgHtScoreOut;
	}

	public float getT1AvgFtScoreIn() {
		return t1AvgFtScoreIn;
	}

	public float getT1AvgFtScoreOut() {
		return t1AvgFtScoreOut;
	}

	public float getT1AvgHtGgResult() {
		return t1AvgHtGgResult;
	}

	public float getT1AvgFtGgResult() {
		return t1AvgFtGgResult;
	}

	public float getT2AvgHtScoreIn() {
		return t2AvgHtScoreIn;
	}

	public float getT2AvgHtScoreOut() {
		return t2AvgHtScoreOut;
	}

	public float getT2AvgFtScoreIn() {
		return t2AvgFtScoreIn;
	}

	public float getT2AvgFtScoreOut() {
		return t2AvgFtScoreOut;
	}

	public float getT2AvgHtGgResult() {
		return t2AvgHtGgResult;
	}

	public float getT2AvgFtGgResult() {
		return t2AvgFtGgResult;
	}

	public void setHt1pOutcome(String ht1pOutcome) {
		this.ht1pOutcome = ht1pOutcome;
	}

	public void setHt2pOutcome(String ht2pOutcome) {
		this.ht2pOutcome = ht2pOutcome;
	}

	public void setT1AvgHtScoreIn(float t1AvgHtScoreIn) {
		this.t1AvgHtScoreIn = t1AvgHtScoreIn;
	}

	public void setT1AvgHtScoreOut(float t1AvgHtScoreOut) {
		this.t1AvgHtScoreOut = t1AvgHtScoreOut;
	}

	public void setT1AvgFtScoreIn(float t1AvgFtScoreIn) {
		this.t1AvgFtScoreIn = t1AvgFtScoreIn;
	}

	public void setT1AvgFtScoreOut(float t1AvgFtScoreOut) {
		this.t1AvgFtScoreOut = t1AvgFtScoreOut;
	}

	public void setT1AvgHtGgResult(float t1AvgHtGgResult) {
		this.t1AvgHtGgResult = t1AvgHtGgResult;
	}

	public void setT1AvgFtGgResult(float t1AvgFtGgResult) {
		this.t1AvgFtGgResult = t1AvgFtGgResult;
	}

	public void setT2AvgHtScoreIn(float t2AvgHtScoreIn) {
		this.t2AvgHtScoreIn = t2AvgHtScoreIn;
	}

	public void setT2AvgHtScoreOut(float t2AvgHtScoreOut) {
		this.t2AvgHtScoreOut = t2AvgHtScoreOut;
	}

	public void setT2AvgFtScoreIn(float t2AvgFtScoreIn) {
		this.t2AvgFtScoreIn = t2AvgFtScoreIn;
	}

	public void setT2AvgFtScoreOut(float t2AvgFtScoreOut) {
		this.t2AvgFtScoreOut = t2AvgFtScoreOut;
	}

	public void setT2AvgHtGgResult(float t2AvgHtGgResult) {
		this.t2AvgHtGgResult = t2AvgHtGgResult;
	}

	public void setT2AvgFtGgResult(float t2AvgFtGgResult) {
		this.t2AvgFtGgResult = t2AvgFtGgResult;
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

	public float getT2Defense() {
		return t2Defense;
	}

	public void setT1Atack(float t1Atack) {
		this.t1Atack = t1Atack;
	}

	public void setT1Defense(float t1Defense) {
		this.t1Defense = t1Defense;
	}

	public void setT2Atack(float t2Atack) {
		this.t2Atack = t2Atack;
	}

	public void setT2Defense(float t2Defense) {
		this.t2Defense = t2Defense;
	}

	public String liner() {
		// return a string to be stored in a file
		StringBuilder sb = new StringBuilder();
		sb.append(week);
		sb.append(",");
		sb.append(headOutcome);
		sb.append(",");
		sb.append(scoreOutcome);
		sb.append(",");
		sb.append(ht1pOutcome);
		sb.append(",");
		sb.append(ht2pOutcome);
		sb.append(",");
		sb.append(ggOutcome);
		sb.append(",");
		sb.append(totHtScore);
		sb.append(",");
		sb.append(totFtScore);
		sb.append(",");

		sb.append(t1);
		sb.append(",");
		sb.append(t1Points);
		sb.append(",");
		sb.append(t1Classification);
		sb.append(",");
		sb.append(t1Form);
		sb.append(",");
		sb.append(t1Form1Diff);
		sb.append(",");
		sb.append(t1Form2Diff);
		sb.append(",");
		sb.append(t1Form3Diff);
		sb.append(",");
		sb.append(t1Form4Diff);
		sb.append(",");
		sb.append(t1Atack);
		sb.append(",");
		sb.append(t1AtackIn);
		sb.append(",");
		sb.append(t1AtackOut);
		sb.append(",");
		sb.append(t1Defense);
		sb.append(",");
		sb.append(t1DefenseIn);
		sb.append(",");
		sb.append(t1DefenseOut);
		sb.append(",");
		sb.append(t1AvgHtScoreIn);// avg score ht in
		sb.append(",");
		sb.append(t1AvgHtScoreOut);
		sb.append(",");
		sb.append(t1AvgFtScoreIn);
		sb.append(",");
		sb.append(t1AvgFtScoreOut);
		sb.append(",");
		sb.append(t1AvgHtGgResult);// gg in ht /{per all matches}
		sb.append(",");
		sb.append(t1AvgFtGgResult);
		sb.append(",");
		sb.append(t1WinsIn);
		sb.append(",");
		sb.append(t1WinsOut);
		sb.append(",");
		sb.append(t1DrawsIn);
		sb.append(",");
		sb.append(t1DrawsOut);
		sb.append(",");
		sb.append(t1LosesIn);
		sb.append(",");
		sb.append(t1LosesOut);
		sb.append(",");

		sb.append(t2);
		sb.append(",");
		sb.append(t2Points);
		sb.append(",");
		sb.append(t2Classification);
		sb.append(",");
		sb.append(t2Form);
		sb.append(",");
		sb.append(t2Form1Diff);
		sb.append(",");
		sb.append(t2Form2Diff);
		sb.append(",");
		sb.append(t2Form3Diff);
		sb.append(",");
		sb.append(t2Form4Diff);
		sb.append(",");
		sb.append(t2Atack);
		sb.append(",");
		sb.append(t2AtackIn);
		sb.append(",");
		sb.append(t2AtackOut);
		sb.append(",");
		sb.append(t2Defense);
		sb.append(",");
		sb.append(t2DefenseIn);
		sb.append(",");
		sb.append(t2DefenseOut);
		sb.append(",");
		sb.append(t2AvgHtScoreIn);// avg score ht in
		sb.append(",");
		sb.append(t2AvgHtScoreOut);
		sb.append(",");
		sb.append(t2AvgFtScoreIn);
		sb.append(",");
		sb.append(t2AvgFtScoreOut);
		sb.append(",");
		sb.append(t2AvgHtGgResult);// gg in ht /{per all matches}
		sb.append(",");
		sb.append(t2AvgFtGgResult);
		sb.append(",");
		sb.append(t2WinsIn);
		sb.append(",");
		sb.append(t2WinsOut);
		sb.append(",");
		sb.append(t2DrawsIn);
		sb.append(",");
		sb.append(t2DrawsOut);
		sb.append(",");
		sb.append(t2LosesIn);
		sb.append(",");
		sb.append(t2LosesOut);
		sb.append(",");

		sb.append(bet_1);
		sb.append(",");
		sb.append(bet_X);
		sb.append(",");
		sb.append(bet_2);
		sb.append(",");
		sb.append(bet_O);
		sb.append(",");
		sb.append(bet_U);

		logger.info(sb.toString());

		return sb.toString();
	}

	public String csvHeader() {
		StringBuilder sb = new StringBuilder();
		sb.append("week,");
		sb.append("headOutcome,");
		sb.append("scoreOutcome,");
		sb.append("ht1pOutcome,");
		sb.append("ht2pOutcome,");
		sb.append("ggOutcome,");
		sb.append("totHtScore,");
		sb.append("totHtScore,");
		
		sb.append("t1,");
		sb.append("t1Points,");
		sb.append("t1Classification,");
		sb.append("t1Form,");
		sb.append("t1Form1Diff,");
		sb.append("t1Form2Diff,");
		sb.append("t1Form3Diff,");
		sb.append("t1Form4Diff,");
		sb.append("t1Atack,");
		sb.append("t1AtackIn,");
		sb.append("t1AtackOut,");
		sb.append("t1Defense,");
		sb.append("t1DefenseIn,");
		sb.append("t1DefenseOut,");
		sb.append("t1AvgHtScoreIn,");// avg score ht in
		sb.append("t1AvgHtScoreOut,");
		sb.append("t1AvgFtScoreIn,");
		sb.append("t1AvgFtScoreOut,");
		sb.append("t1AvgHtGgResult,");// gg in ht /{per all matches}
		sb.append("t1AvgFtGgResult,");
		sb.append("t1WinsIn,");
		sb.append("t1WinsOut,");
		sb.append("t1DrawsIn,");
		sb.append("t1DrawsOut,");
		sb.append("t1LosesIn,");
		sb.append("t1LosesOut,");

		sb.append("t2,");
		sb.append("t2Points,");
		sb.append("t2Classification,");
		sb.append("t2Form,");
		sb.append("t2Form1Diff,");
		sb.append("t2Form2Diff,");
		sb.append("t2Form3Diff,");
		sb.append("t2Form4Diff,");
		sb.append("t2Atack,");
		sb.append("t2AtackIn,");
		sb.append("t2AtackOut,");
		sb.append("t2Defense,");
		sb.append("t2DefenseIn,");
		sb.append("t2DefenseOut,");
		sb.append("t2AvgHtScoreIn,");// avg score ht in
		sb.append("t2AvgHtScoreOut,");
		sb.append("t2AvgFtScoreIn,");
		sb.append("t2AvgFtScoreOut,");
		sb.append("t2AvgHtGgResult,");// gg in ht /{per all matches}
		sb.append("t2AvgFtGgResult,");
		sb.append("t2WinsIn,");
		sb.append("t2WinsOut,");
		sb.append("t2DrawsIn,");
		sb.append("t2DrawsOut,");
		sb.append("t2LosesIn,");
		sb.append("t2LosesOut,");
		sb.append("bet_1,");
		sb.append("bet_X,");
		sb.append("bet_2,");
		sb.append("bet_O,");
		sb.append("bet_U");

		return sb.toString();
	}

	public String arffHeader() {
		StringBuilder sb = new StringBuilder();
		sb.append("week,");
		sb.append("headOutcome,");
		sb.append("scoreOutcome,");
		sb.append("ht1pOutcome,");
		sb.append("ht2pOutcome,");
		sb.append("ggOutcome,");
		sb.append("t1,");
		sb.append("t1Points,");
		sb.append("t1Classification,");
		sb.append("t1Form,");
		sb.append("t1Form1Diff,");
		sb.append("t1Form2Diff,");
		sb.append("t1Form3Diff,");
		sb.append("t1Form4Diff,");
		sb.append("t1Atack,");
		sb.append("t1AtackIn,");
		sb.append("t1AtackOut,");
		sb.append("t1Defense,");
		sb.append("t1DefenseIn,");
		sb.append("t1DefenseOut,");
		sb.append("t1AvgHtScoreIn,");// avg score ht in
		sb.append("t1AvgHtScoreOut,");
		sb.append("t1AvgFtScoreIn,");
		sb.append("t1AvgFtScoreOut,");
		sb.append("t1AvgHtGgResult,");// gg in ht /{per all matches}
		sb.append("t1AvgFtGgResult,");
		sb.append("t1WinsIn,");
		sb.append("t1WinsOut,");
		sb.append("t1DrawsIn,");
		sb.append("t1DrawsOut,");
		sb.append("t1LosesIn,");
		sb.append("t1LosesOut,");

		sb.append("t2,");
		sb.append("t2Points,");
		sb.append("t2Classification,");
		sb.append("t2Form,");
		sb.append("t2Form1Diff,");
		sb.append("t2Form2Diff,");
		sb.append("t2Form3Diff,");
		sb.append("t2Form4Diff,");
		sb.append("t2Atack,");
		sb.append("t2AtackIn,");
		sb.append("t2AtackOut,");
		sb.append("t2Defense,");
		sb.append("t2DefenseIn,");
		sb.append("t2DefenseOut,");
		sb.append("t2AvgHtScoreIn,");// avg score ht in
		sb.append("t2AvgHtScoreOut,");
		sb.append("t2AvgFtScoreIn,");
		sb.append("t2AvgFtScoreOut,");
		sb.append("t2AvgHtGgResult,");// gg in ht /{per all matches}
		sb.append("t2AvgFtGgResult,");
		sb.append("t2WinsIn,");
		sb.append("t2WinsOut,");
		sb.append("t2DrawsIn,");
		sb.append("t2DrawsOut,");
		sb.append("t2LosesIn,");
		sb.append("t2LosesOut,");
		sb.append("bet_1,");
		sb.append("bet_X,");
		sb.append("bet_2,");
		sb.append("bet_O,");
		sb.append("bet_U");

		return sb.toString();
	}
}
