package structures;

import extra.MatchOutcome;

public class ReducedPredictionTestFile {

	private String t1;
	private String t2;
	private String headOutcome = MatchOutcome.missing; // MatchOutcome val 1/x/2
	private String scoreOutcome = MatchOutcome.missing; // o/u
	private String ht1pOutcome = MatchOutcome.missing; // y,n, m-> missing
	private String ht2pOutcome = MatchOutcome.missing; // y,n, NA-> missing

	private int totHtScore = -1;
	private int totFtScore = -1;

	public ReducedPredictionTestFile() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ReducedPredictionTestFile(String t1, String t2, String headOutcome,
			String scoreOutcome, String ht1pOutcome, String ht2pOutcome,
			int totHtScore, int totFtScore) {
		super();
		this.t1 = t1;
		this.t2 = t2;
		this.headOutcome = headOutcome;
		this.scoreOutcome = scoreOutcome;
		this.ht1pOutcome = ht1pOutcome;
		this.ht2pOutcome = ht2pOutcome;
		this.totHtScore = totHtScore;
		this.totFtScore = totFtScore;
	}

	public String getHeadOutcome() {
		return headOutcome;
	}

	public String getScoreOutcome() {
		return scoreOutcome;
	}

	public String getHt1pOutcome() {
		return ht1pOutcome;
	}

	public String getHt2pOutcome() {
		return ht2pOutcome;
	}

	public int getTotHtScore() {
		return totHtScore;
	}

	public int getTotFtScore() {
		return totFtScore;
	}

	public void setHeadOutcome(String headOutcome) {
		this.headOutcome = headOutcome;
	}

	public void setScoreOutcome(String scoreOutcome) {
		this.scoreOutcome = scoreOutcome;
	}

	public void setHt1pOutcome(String ht1pOutcome) {
		this.ht1pOutcome = ht1pOutcome;
	}

	public void setHt2pOutcome(String ht2pOutcome) {
		this.ht2pOutcome = ht2pOutcome;
	}

	public void setTotHtScore(int totHtScore) {
		this.totHtScore = totHtScore;
	}

	public void setTotFtScore(int totFtScore) {
		this.totFtScore = totFtScore;
	}

	public String getT1() {
		return t1;
	}

	public String getT2() {
		return t2;
	}

	public void setT1(String t1) {
		this.t1 = t1;
	}

	public void setT2(String t2) {
		this.t2 = t2;
	}

	public String csvHeader() {
		StringBuilder sb = new StringBuilder();
		sb.append("t1,");
		sb.append("t2,");
		sb.append("headOutcome,");
		sb.append("scoreOutcome,");
		sb.append("ht1pOutcome,");
		sb.append("ht2pOutcome,");
		sb.append("totHtScore,");
		sb.append("totFtScore");
		return sb.toString();
	}
}
