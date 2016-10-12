package dbhandler;

/**
 * @author Administrator
 *
 *         to contain the data needed for the calculation of the description
 *         data for the specific match page. The attributes corresponds to the
 *         db fields of the competitions table
 */
public class MatchSpecificDBAtts {
	private String team;
	private int position;
	private int points;

	private int matchesIn;
	private int matchesOut;
	private int ftScoreIn;
	private int ftScoreOut;
	private int ftConcededIn;
	private int ftConcededOut;
	private int htScoreIn;
	private int htScoreOut;
	private int htConcededIn;
	private int htConcededOut;

	private float form;
	private float formAtack;
	private float formDefence;

	private int winsIn;
	private int drawsIn;
	private int losesIn;
	private int winsOut;
	private int drawsOut;
	private int losesOut;
	private int htGg;

	private float avgWinsCont;
	private float avgDrawCont;
	private float avgLoseCont;

	public String getTeam() {
		return team;
	}

	public void setTeam(String team) {
		this.team = team;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public int getMatchesIn() {
		return matchesIn;
	}

	public void setMatchesIn(int matchesIn) {
		this.matchesIn = matchesIn;
	}

	public int getMatchesOut() {
		return matchesOut;
	}

	public void setMatchesOut(int matchesOut) {
		this.matchesOut = matchesOut;
	}

	public int getFtScoreIn() {
		return ftScoreIn;
	}

	public void setFtScoreIn(int ftScoreIn) {
		this.ftScoreIn = ftScoreIn;
	}

	public int getFtScoreOut() {
		return ftScoreOut;
	}

	public void setFtScoreOut(int ftScoreOut) {
		this.ftScoreOut = ftScoreOut;
	}

	public int getFtConcededIn() {
		return ftConcededIn;
	}

	public void setFtConcededIn(int ftConcededIn) {
		this.ftConcededIn = ftConcededIn;
	}

	public int getFtConcededOut() {
		return ftConcededOut;
	}

	public void setFtConcededOut(int ftConcededOut) {
		this.ftConcededOut = ftConcededOut;
	}

	public int getHtScoreIn() {
		return htScoreIn;
	}

	public void setHtScoreIn(int htScoreIn) {
		this.htScoreIn = htScoreIn;
	}

	public int getHtScoreOut() {
		return htScoreOut;
	}

	public void setHtScoreOut(int htScoreOut) {
		this.htScoreOut = htScoreOut;
	}

	public int getHtConcededIn() {
		return htConcededIn;
	}

	public void setHtConcededIn(int htConcededIn) {
		this.htConcededIn = htConcededIn;
	}

	public int getHtConcededOut() {
		return htConcededOut;
	}

	public void setHtConcededOut(int htConcededOut) {
		this.htConcededOut = htConcededOut;
	}

	public float getForm() {
		return form;
	}

	public void setForm(float form) {
		this.form = form;
	}

	public float getFormAtack() {
		return formAtack;
	}

	public void setFormAtack(float formAtack) {
		this.formAtack = formAtack;
	}

	public float getFormDefence() {
		return formDefence;
	}

	public void setFormDefence(float formDefence) {
		this.formDefence = formDefence;
	}

	public int getWinsIn() {
		return winsIn;
	}

	public void setWinsIn(int winsIn) {
		this.winsIn = winsIn;
	}

	public int getDrawsIn() {
		return drawsIn;
	}

	public void setDrawsIn(int drawsIn) {
		this.drawsIn = drawsIn;
	}

	public int getLosesIn() {
		return losesIn;
	}

	public void setLosesIn(int losesIn) {
		this.losesIn = losesIn;
	}

	public int getWinsOut() {
		return winsOut;
	}

	public void setWinsOut(int winsOut) {
		this.winsOut = winsOut;
	}

	public int getDrawsOut() {
		return drawsOut;
	}

	public void setDrawsOut(int drawsOut) {
		this.drawsOut = drawsOut;
	}

	public int getLosesOut() {
		return losesOut;
	}

	public void setLosesOut(int losesOut) {
		this.losesOut = losesOut;
	}

	public int getHtGg() {
		return htGg;
	}

	public void setHtGg(int htGg) {
		this.htGg = htGg;
	}

	public int getFtGg() {
		return ftGg;
	}

	public void setFtGg(int ftGg) {
		this.ftGg = ftGg;
	}

	public float getAvgWinsCont() {
		return avgWinsCont;
	}

	public void setAvgWinsCont(float f) {
		this.avgWinsCont = f;
	}

	public float getAvgDrawCont() {
		return avgDrawCont;
	}

	public void setAvgDrawCont(float avgDrawCont) {
		this.avgDrawCont = avgDrawCont;
	}

	public float getAvgLoseCont() {
		return avgLoseCont;
	}

	public void setAvgLoseCont(float avgLoseCont) {
		this.avgLoseCont = avgLoseCont;
	}

	private int ftGg;

	// average win continuity (calc sum (continuous results -1)/matches)

}
