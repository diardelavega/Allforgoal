package dbhandler;

public class BasicTableEntity extends HalfTimeExtend implements
		Comparable<BasicTableEntity> {

	private String team;
	private int teamid;

	private int points;
	private int matchesIn = 0;
	private int matchesOut = 0;
	private int ftScoreIn = 0;
	private int ftScoreOut = 0;
	private int ftConcededIn = 0;
	private int ftConcededOut = 0;

	private int p3MatchesIn = 0;
	private int p3MatchesOut = 0;
	private int p3FtScoreIn = 0;
	private int p3FtScoreOut = 0;
	private int p3FtConcededIn = 0;
	private int p3FtConcededOut = 0;

	private int ttMatchesIn = 0;
	private int ttMatchesOut = 0;
	private int ttFtScoreIn = 0;
	private int ttFtScoreOut = 0;
	private int ttFtConcededIn = 0;
	private int ttFtConcededOut = 0;

	private int p3UpMatchesIn = 0;
	private int p3UpMatchesOut = 0;
	private int p3UpFtScoreIn = 0;
	private int p3UpFtScoreOut = 0;
	private int p3UpFtConcededIn = 0;
	private int p3UpFtConcededOut = 0;

	private int p3DownMatchesIn = 0;
	private int p3DownMatchesOut = 0;
	private int p3DownFtScoreIn = 0;
	private int p3DownFtScoreOut = 0;
	private int p3DownFtConcededIn = 0;
	private int p3DownFtConcededOut = 0;

	private float form = 0;
	private float form1 = 0;
	private float form2 = 0;
	private float form3 = 0;
	private float form4 = 0;

	private float formAtack = 0;
	private float formDefence = 0;

	public String getTeam() {
		return team;
	}

	public int getTeamid() {
		return teamid;
	}

	public int getPoints() {
		return points;
	}

	public int getMatchesIn() {
		return matchesIn;
	}

	public int getMatchesOut() {
		return matchesOut;
	}

	public int getFtScoreIn() {
		return ftScoreIn;
	}

	public int getFtScoreOut() {
		return ftScoreOut;
	}

	public int getFtConcededIn() {
		return ftConcededIn;
	}

	public int getFtConcededOut() {
		return ftConcededOut;
	}

	public int getP3MatchesIn() {
		return p3MatchesIn;
	}

	public int getP3MatchesOut() {
		return p3MatchesOut;
	}

	public int getP3FtScoreIn() {
		return p3FtScoreIn;
	}

	public int getP3FtScoreOut() {
		return p3FtScoreOut;
	}

	public int getP3FtConcededIn() {
		return p3FtConcededIn;
	}

	public int getP3FtConcededOut() {
		return p3FtConcededOut;
	}

	public int getTtMatchesIn() {
		return ttMatchesIn;
	}

	public int getTtMatchesOut() {
		return ttMatchesOut;
	}

	public int getTtFtScoreIn() {
		return ttFtScoreIn;
	}

	public int getTtFtScoreOut() {
		return ttFtScoreOut;
	}

	public int getTtFtConcededIn() {
		return ttFtConcededIn;
	}

	public int getTtFtConcededOut() {
		return ttFtConcededOut;
	}

	public int getP3UpMatchesIn() {
		return p3UpMatchesIn;
	}

	public int getP3UpMatchesOut() {
		return p3UpMatchesOut;
	}

	public int getP3UpFtScoreIn() {
		return p3UpFtScoreIn;
	}

	public int getP3UpFtScoreOut() {
		return p3UpFtScoreOut;
	}

	public int getP3UpFtConcededIn() {
		return p3UpFtConcededIn;
	}

	public int getP3UpFtConcededOut() {
		return p3UpFtConcededOut;
	}

	public int getP3DownMatchesIn() {
		return p3DownMatchesIn;
	}

	public int getP3DownMatchesOut() {
		return p3DownMatchesOut;
	}

	public int getP3DownFtScoreIn() {
		return p3DownFtScoreIn;
	}

	public int getP3DownFtScoreOut() {
		return p3DownFtScoreOut;
	}

	public int getP3DownFtConcededIn() {
		return p3DownFtConcededIn;
	}

	public int getP3DownFtConcededOut() {
		return p3DownFtConcededOut;
	}

	public float getForm() {
		return form;
	}

	public float getForm1() {
		return form1;
	}

	public float getForm2() {
		return form2;
	}

	public float getForm3() {
		return form3;
	}

	public float getForm4() {
		return form4;
	}

	public float getFormAtack() {
		return formAtack;
	}

	public float getFormDefence() {
		return formDefence;
	}

	public void setTeam(String team) {
		this.team = team;
	}

	public void setTeamid(int teamid) {
		this.teamid = teamid;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public void setMatchesIn(int matchesIn) {
		this.matchesIn = matchesIn;
	}

	public void setMatchesOut(int matchesOut) {
		this.matchesOut = matchesOut;
	}

	public void setFtScoreIn(int ftScoreIn) {
		this.ftScoreIn = ftScoreIn;
	}

	public void setFtScoreOut(int ftScoreOut) {
		this.ftScoreOut = ftScoreOut;
	}

	public void setFtConcededIn(int ftConcededIn) {
		this.ftConcededIn = ftConcededIn;
	}

	public void setFtConcededOut(int ftConcededOut) {
		this.ftConcededOut = ftConcededOut;
	}

	public void setP3MatchesIn(int p3MatchesIn) {
		this.p3MatchesIn = p3MatchesIn;
	}

	public void setP3MatchesOut(int p3MatchesOut) {
		this.p3MatchesOut = p3MatchesOut;
	}

	public void setP3FtScoreIn(int p3FtScoreIn) {
		this.p3FtScoreIn = p3FtScoreIn;
	}

	public void setP3FtScoreOut(int p3FtScoreOut) {
		this.p3FtScoreOut = p3FtScoreOut;
	}

	public void setP3FtConcededIn(int p3FtConcededIn) {
		this.p3FtConcededIn = p3FtConcededIn;
	}

	public void setP3FtConcededOut(int p3FtConcededOut) {
		this.p3FtConcededOut = p3FtConcededOut;
	}

	public void setTtMatchesIn(int ttMatchesIn) {
		this.ttMatchesIn = ttMatchesIn;
	}

	public void setTtMatchesOut(int ttMatchesOut) {
		this.ttMatchesOut = ttMatchesOut;
	}

	public void setTtFtScoreIn(int ttFtScoreIn) {
		this.ttFtScoreIn = ttFtScoreIn;
	}

	public void setTtFtScoreOut(int ttFtScoreOut) {
		this.ttFtScoreOut = ttFtScoreOut;
	}

	public void setTtFtConcededIn(int ttFtConcededIn) {
		this.ttFtConcededIn = ttFtConcededIn;
	}

	public void setTtFtConcededOut(int ttFtConcededOut) {
		this.ttFtConcededOut = ttFtConcededOut;
	}

	public void setP3UpMatchesIn(int p3UpMatchesIn) {
		this.p3UpMatchesIn = p3UpMatchesIn;
	}

	public void setP3UpMatchesOut(int p3UpMatchesOut) {
		this.p3UpMatchesOut = p3UpMatchesOut;
	}

	public void setP3UpFtScoreIn(int p3UpFtScoreIn) {
		this.p3UpFtScoreIn = p3UpFtScoreIn;
	}

	public void setP3UpFtScoreOut(int p3UpFtScoreOut) {
		this.p3UpFtScoreOut = p3UpFtScoreOut;
	}

	public void setP3UpFtConcededIn(int p3UpFtConcededIn) {
		this.p3UpFtConcededIn = p3UpFtConcededIn;
	}

	public void setP3UpFtConcededOut(int p3UpFtConcededOut) {
		this.p3UpFtConcededOut = p3UpFtConcededOut;
	}

	public void setP3DownMatchesIn(int p3DownMatchesIn) {
		this.p3DownMatchesIn = p3DownMatchesIn;
	}

	public void setP3DownMatchesOut(int p3DownMatchesOut) {
		this.p3DownMatchesOut = p3DownMatchesOut;
	}

	public void setP3DownFtScoreIn(int p3DownFtScoreIn) {
		this.p3DownFtScoreIn = p3DownFtScoreIn;
	}

	public void setP3DownFtScoreOut(int p3DownFtScoreOut) {
		this.p3DownFtScoreOut = p3DownFtScoreOut;
	}

	public void setP3DownFtConcededIn(int p3DownFtConcededIn) {
		this.p3DownFtConcededIn = p3DownFtConcededIn;
	}

	public void setP3DownFtConcededOut(int p3DownFtConcededOut) {
		this.p3DownFtConcededOut = p3DownFtConcededOut;
	}

	public void setForm(float form) {
		this.form = form;
	}

	public void setForm1(float form1) {
		this.form1 = form1;
	}

	public void setForm2(float form2) {
		this.form2 = form2;
	}

	public void setForm3(float form3) {
		this.form3 = form3;
	}

	public void setForm4(float form4) {
		this.form4 = form4;
	}

	public void setFormAtack(float formAtack) {
		this.formAtack = formAtack;
	}

	public void setFormDefence(float formDefence) {
		this.formDefence = formDefence;
	}

	public void addPoints(int points) {
		this.points = points;
	}

	public void addMatchesIn() {
		this.matchesIn += 1;
	}

	public void addMatchesOut() {
		this.matchesOut += 1;
	}

	public void addFtScoreIn(int ftScoreIn) {
		this.ftScoreIn += ftScoreIn;
	}

	public void addFtScoreOut(int ftScoreOut) {
		this.ftScoreOut += ftScoreOut;
	}

	public void addFtConcededIn(int ftConcededIn) {
		this.ftConcededIn += ftConcededIn;
	}

	public void addFtConcededOut(int ftConcededOut) {
		this.ftConcededOut += ftConcededOut;
	}

	public void addP3MatchesIn() {
		this.p3MatchesIn += 1;
	}

	public void addP3MatchesOut() {
		this.p3MatchesOut += 1;
	}

	public void addP3FtScoreIn(int p3FtScoreIn) {
		this.p3FtScoreIn += p3FtScoreIn;
	}

	public void addP3FtScoreOut(int p3FtScoreOut) {
		this.p3FtScoreOut += p3FtScoreOut;
	}

	public void addP3FtConcededIn(int p3FtConcededIn) {
		this.p3FtConcededIn += p3FtConcededIn;
	}

	public void addP3FtConcededOut(int p3FtConcededOut) {
		this.p3FtConcededOut += p3FtConcededOut;
	}

	public void addTtMatchesIn() {
		this.ttMatchesIn += 1;
	}

	public void addTtMatchesOut() {
		this.ttMatchesOut += 1;
	}

	public void addTtFtScoreIn(int ttFtScoreIn) {
		this.ttFtScoreIn += ttFtScoreIn;
	}

	public void addTtFtScoreOut(int ttFtScoreOut) {
		this.ttFtScoreOut += ttFtScoreOut;
	}

	public void addTtFtConcededIn(int ttFtConcededIn) {
		this.ttFtConcededIn += ttFtConcededIn;
	}

	public void addTtFtConcededOut(int ttFtConcededOut) {
		this.ttFtConcededOut += ttFtConcededOut;
	}

	public void addP3UpMatchesIn() {
		this.p3UpMatchesIn += 1;
	}

	public void addP3UpMatchesOut() {
		this.p3UpMatchesOut += 1;
	}

	public void addP3UpFtScoreIn(int p3UpFtScoreIn) {
		this.p3UpFtScoreIn += p3UpFtScoreIn;
	}

	public void addP3UpFtScoreOut(int p3UpFtScoreOut) {
		this.p3UpFtScoreOut += p3UpFtScoreOut;
	}

	public void addP3UpFtConcededIn(int p3UpFtConcededIn) {
		this.p3UpFtConcededIn += p3UpFtConcededIn;
	}

	public void addP3UpFtConcededOut(int p3UpFtConcededOut) {
		this.p3UpFtConcededOut += p3UpFtConcededOut;
	}

	public void addP3DownMatchesIn() {
		this.p3DownMatchesIn += 1;
	}

	public void addP3DownMatchesOut() {
		this.p3DownMatchesOut += 1;
	}

	public void addP3DownFtScoreIn(int p3DownFtScoreIn) {
		this.p3DownFtScoreIn += p3DownFtScoreIn;
	}

	public void addP3DownFtScoreOut(int p3DownFtScoreOut) {
		this.p3DownFtScoreOut += p3DownFtScoreOut;
	}

	public void addP3DownFtConcededIn(int p3DownFtConcededIn) {
		this.p3DownFtConcededIn += p3DownFtConcededIn;
	}

	public void addP3DownFtConcededOut(int p3DownFtConcededOut) {
		this.p3DownFtConcededOut += p3DownFtConcededOut;
	}

	public void addForm(float form) {
		this.form += form;
	}

	public void addFormAtack(float formAtack) {
		this.formAtack += formAtack;
	}

	public void addFormDefence(float formDefence) {
		this.formDefence += formDefence;
	}

	public int goalScored() {
		return ftScoreIn + ftScoreOut;
	}

	public int goalConceded() {
		return ftConcededIn + ftConcededOut;
	}

	@Override
	public int compareTo(BasicTableEntity o) {
		int compare = o.getPoints();
		if (compare == this.getPoints()) {
			return (((o.getFtScoreIn() + o.getFtScoreOut()) - (o
					.getFtConcededIn() + o.ftConcededOut)) - ((this
					.getFtScoreIn() + this.getFtScoreOut()) - (this
					.getFtConcededIn() + this.ftConcededOut)));
		} else {
			return compare - this.getPoints();
		}
	}

	public String line () {
		StringBuilder sb = new StringBuilder();
		sb.append(team);
		sb.append(" , ");
		sb.append(points);
		sb.append(" , ");
		sb.append(matchesIn);
		sb.append(" , ");
		sb.append(matchesOut);
		sb.append(" , ");
		sb.append(htScoreIn);
		sb.append(" , ");
		sb.append(htScoreOut);
		sb.append(" , ");
		sb.append(htConcededIn);
		sb.append(" , ");
		sb.append(htConcededOut);
		sb.append(" , ");
		sb.append(ftScoreIn);
		sb.append(" , ");
		sb.append(ftScoreOut);
		sb.append(" , ");
		sb.append(ftConcededIn);
		sb.append(" , ");
		sb.append(ftConcededOut);
		sb.append(" , ");

		sb.append(p3MatchesIn);
		sb.append(" , ");
		sb.append(p3MatchesOut);
		sb.append(" , ");
		sb.append(p3HtScoreIn);
		sb.append(" , ");
		sb.append(p3HtScoreOut);
		sb.append(" , ");
		sb.append(p3HtConcededIn);
		sb.append(" , ");
		sb.append(p3HtConcededOut);
		sb.append(" , ");
		sb.append(p3FtScoreIn);
		sb.append(" , ");
		sb.append(p3FtScoreOut);
		sb.append(" , ");
		sb.append(p3FtConcededIn);
		sb.append(" , ");
		sb.append(p3FtConcededOut);
		sb.append(" , ");

		// matches against the top teams on the competition cielic(n/4)
		sb.append(ttMatchesIn);
		sb.append(" , ");
		sb.append(ttMatchesOut);
		sb.append(" , ");
		sb.append(ttHtScoreIn);
		sb.append(" , ");
		sb.append(ttHtScoreOut);
		sb.append(" , ");
		sb.append(ttHtConcededIn);
		sb.append(" , ");
		sb.append(ttHtConcededOut);
		sb.append(" , ");
		sb.append(ttFtScoreIn);
		sb.append(" , ");
		sb.append(ttFtScoreOut);
		sb.append(" , ");
		sb.append(ttFtConcededIn);
		sb.append(" , ");
		sb.append(ttFtConcededOut);
		sb.append(" , ");

		sb.append(p3UpMatchesIn);
		sb.append(" , ");
		sb.append(p3UpMatchesOut);
		sb.append(" , ");
		sb.append(p3UpHtScoreIn);
		sb.append(" , ");
		sb.append(p3UpHtScoreOut);
		sb.append(" , ");
		sb.append(p3UpHtConcededIn);
		sb.append(" , ");
		sb.append(p3UpHtConcededOut);
		sb.append(" , ");
		sb.append(p3UpFtScoreIn);
		sb.append(" , ");
		sb.append(p3UpFtScoreOut);
		sb.append(" , ");
		sb.append(p3UpFtConcededIn);
		sb.append(" , ");
		sb.append(p3UpFtConcededOut);
		sb.append(" , ");

		sb.append(p3DownMatchesIn);
		sb.append(" , ");
		sb.append(p3DownMatchesOut);
		sb.append(" , ");
		sb.append(p3DownHtScoreIn);
		sb.append(" , ");
		sb.append(p3DownHtScoreOut);
		sb.append(" , ");
		sb.append(p3DownHtConcededIn);
		sb.append(" , ");
		sb.append(p3DownHtConcededOut);
		sb.append(" , ");
		sb.append(p3DownFtScoreIn);
		sb.append(" , ");
		sb.append(p3DownFtScoreOut);
		sb.append(" , ");
		sb.append(p3DownFtConcededIn);
		sb.append(" , ");
		sb.append(p3DownFtConcededOut);
		sb.append(" , ");

		sb.append(form);
		sb.append(" , ");
		sb.append(form1);
		sb.append(" , ");
		sb.append(form2);
		sb.append(" , ");
		sb.append(form3);
		sb.append(" , ");
		sb.append(form4);
		sb.append(" , ");
		sb.append(formAtack);
		sb.append(" , ");
		sb.append(formDefence);

		return  sb.toString();
	}
}
