package structures;

import extra.MatchOutcome;

public class ReducedPredictionTestFile {

	private String t1;
	private String t2;
	private String headOutcome = MatchOutcome.missing; // MatchOutcome val 1/x/2
	private String scoreOutcome = MatchOutcome.missing; // o/u
	private String ht1pOutcome = MatchOutcome.missing; // y,n, m-> missing
	private String ht2pOutcome = MatchOutcome.missing; // y,n, NA-> missing

	private String totHtScore = "-1";
	private String totFtScore = "-1";

	public ReducedPredictionTestFile() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ReducedPredictionTestFile(String t1, String t2, String headOutcome,
			String scoreOutcome, String ht1pOutcome, String ht2pOutcome,
			String totHtScore, String totFtScore) {
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

	public String getTotHtScore() {
		return totHtScore;
	}

	public String getTotFtScore() {
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

	public void setTotHtScore(String totHtScore) {
		this.totHtScore = totHtScore;
	}

	public void setTotFtScore(String totFtScore) {
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
	
	public String printRedPredLine() {
		StringBuilder sb = new StringBuilder();
		sb.append(t1);
		sb.append(", ");
		sb.append(t2);
		sb.append(", ");
		sb.append(headOutcome);
		sb.append(", ");
		sb.append(scoreOutcome);
		sb.append(", ");
		sb.append(ht1pOutcome);
		sb.append(", ");
		sb.append(ht2pOutcome);
		sb.append(", ");
		sb.append(totHtScore);
		sb.append(", ");
		sb.append(totFtScore);
		return sb.toString();
	}
}
