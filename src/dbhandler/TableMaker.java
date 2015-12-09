package dbhandler;

/**
 * @author Administrator
 *
 *         the attributes required for teams without ht results. Creates table,
 *         creates the insert lines for every team that is going into the table,
 *         and an update function for the renewal of the different attributes of
 *         each team
 */
public class TableMaker {

	private String team;
	private int teamid;

	private int points;
	private int matchesIn;
	private int matchesOut;
	private int ftScoreIn;
	private int ftScoreOut;
	private int ftConcededIn;
	private int ftConcededOut;

	private int p3MatchesIn;
	private int p3MatchesOut;
	private int p3FtScoreIn;
	private int p3FtScoreOut;
	private int p3FtConcededIn;
	private int p3FtConcededOut;

	// matches against the top teams on the competition cielic(n/4)
	private int ttMatchesIn;
	private int ttMatchesOut;
	private int ttFtScoreIn;
	private int ttFtScoreOut;
	private int ttFtConcededIn;
	private int ttFtConcededOut;

	private int p3UpMatchesIn;
	private int p3UpMatchesOut;
	private int p3UpFtScoreIn;
	private int p3UpFtScoreOut;
	private int p3UpFtConcededIn;
	private int p3UpFtConcededOut;

	private int p3DownMatchesIn;
	private int p3DownMatchesOut;
	private int p3DownFtScoreIn;
	private int p3DownFtScoreOut;
	private int p3DownFtConcededIn;
	private int p3DownFtConcededOut;

	private float form;
	private float form1;
	private float form2;
	private float form3;
	private float form4;

	private float formAtack;
	private float formDefence;

	public String createTable(String tableName) {
		String create = "createTable " + tableName + " (";
		String attributes = " team varchar(25) not null unique,"
				+ "teamid int, points int not null, matchesin int not null, matchesout int not null, ftscorein int not null, ftscoreout int not null,"
				+ "ftconcedein int,		ftconcedeout int,"
				+ "p3_matchesin int,  p3_matchesout int,  p3_ftscorein int not null,  p3_ftscoreout int not null, p3_ftconcedein int, p3_ftconcedeout int,"
				+ " tt_matchesin int,  tt_matchesout int, tt_ftscorein int not null, tt_ftscoreout int not null, tt_ftconcedein int, tt_ftconcedeout int,"
				+ " p3up_matchesin int,  p3up_matchesout int, p3up_ftscorein int not null, p3up_ftscoreout int not null, p3up_ftconcedein int, p3up_ftconcedeout int,"
				+ "p3down_matchesin int,  p3down_matchesout int, p3down_ftscorein int not null, p3down_ftscoreout int not null, p3down_ftconcedein int, p3down_ftconcedeout int,"
				+ " form numeric(5,3) , form1 numeric(5,3) , form2 numeric(5,3), form3 numeric(5,3), form4 numeric(5,3), formAtack numeric(5,3), formDefence numeric(5,3)";
		return create + attributes + " );";
	}

	public String insertTableLine(String tableName) {
		String insertSd = "insert into " + tableName + " valuses (";

		StringBuilder sb = new StringBuilder();
		sb.append(team);
		sb.append(" , ");
		sb.append(points);
		sb.append(" , ");
		sb.append(matchesIn);
		sb.append(" , ");
		sb.append(matchesOut);
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

		return insertSd + sb.toString() + " );";
	}

	public String basicUpdateTable(String tableName) {
		String updateSd = "update  " + tableName + " set ";
		StringBuilder sb = new StringBuilder();
		sb.append("points = ");
		sb.append(points + ", ");
		sb.append("matchesin = ");
		sb.append(matchesIn + ", ");
		sb.append("matchesout = ");
		sb.append(matchesOut + ", ");
		sb.append("ftscorein = ");
		sb.append(ftScoreIn + ", ");
		sb.append("ftscoreout = ");
		sb.append(ftScoreOut + ", ");
		sb.append("ftconcedein = ");
		sb.append(ftConcededIn + ", ");
		sb.append("ftconcedeout = ");
		sb.append(ftConcededOut);

		String ret = updateSd + sb.toString() + " where team like '" + team
				+ "');";
		return ret;
	}

	public String ttUpdateTable(String tableName) {
		String updateSd = "update  " + tableName + " set ";
		StringBuilder sb = new StringBuilder();
		sb.append("tt_matchesin = ");
		sb.append(ttMatchesIn + ", ");
		sb.append("tt_matchesout = ");
		sb.append(ttMatchesOut + ", ");
		sb.append("tt_ftscorein = ");
		sb.append(ttFtScoreIn + ", ");
		sb.append("tt_ftscoreout = ");
		sb.append(ttFtScoreOut + ", ");
		sb.append("tt_ftconcedein = ");
		sb.append(ttFtConcededIn + ", ");
		sb.append("tt_ftconcedeout = ");
		sb.append(ttFtConcededOut);

		String ret = updateSd + sb.toString() + " where team like '" + team
				+ "');";
		return ret;
	}

	public String p3UpdateTable(String tableName) {
		String updateSd = "update  " + tableName + " set ";
		StringBuilder sb = new StringBuilder();
		sb.append("p3_matchesin = ");
		sb.append(p3MatchesIn + ", ");
		sb.append("p3_matchesout = ");
		sb.append(p3MatchesOut + ", ");
		sb.append("p3_ftscorein = ");
		sb.append(p3FtScoreIn + ", ");
		sb.append("p3_ftscoreout = ");
		sb.append(p3FtScoreOut + ", ");
		sb.append("p3_ftconcedein = ");
		sb.append(p3FtConcededIn + ", ");
		sb.append("p3_ftconcedeout = ");
		sb.append(p3FtConcededOut);

		String ret = updateSd + sb.toString() + " where team like '" + team
				+ "');";
		return ret;
	}

	public String p3UpUpdateTable(String tableName) {
		String updateSd = "update  " + tableName + " set ";
		StringBuilder sb = new StringBuilder();
		sb.append("p3up_matchesin = ");
		sb.append(p3UpMatchesIn + ", ");
		sb.append("p3up_matchesout = ");
		sb.append(p3UpMatchesOut + ", ");
		sb.append("p3up_ftscorein = ");
		sb.append(p3UpFtScoreIn + ", ");
		sb.append("p3up_ftscoreout = ");
		sb.append(p3UpFtScoreOut + ", ");
		sb.append("p3up_ftconcedein = ");
		sb.append(p3UpFtConcededIn + ", ");
		sb.append("p3up_ftconcedeout = ");
		sb.append(p3UpFtConcededOut);

		String ret = updateSd + sb.toString() + " where team like '" + team
				+ "');";
		return ret;
	}

	public String p3DownUpdateTable(String tableName) {
		String updateSd = "update  " + tableName + " set ";
		StringBuilder sb = new StringBuilder();
		sb.append("p3down_matchesin = ");
		sb.append(p3DownMatchesIn + ", ");
		sb.append("p3down_matchesout = ");
		sb.append(p3DownMatchesOut + ", ");
		sb.append("p3down_ftscorein = ");
		sb.append(p3DownFtScoreIn + ", ");
		sb.append("p3down_ftscoreout = ");
		sb.append(p3DownFtScoreOut + ", ");
		sb.append("p3down_ftconcedein = ");
		sb.append(p3DownFtConcededIn + ", ");
		sb.append("p3down_ftconcedeout = ");
		sb.append(p3DownFtConcededOut);

		String ret = updateSd + sb.toString() + " where team like '" + team
				+ "');";
		return ret;
	}

	public String formUpdate(String tableName) {
		String updateSd = "update  " + tableName + " set ";
		StringBuilder sb = new StringBuilder();

		sb.append("form  = ");
		sb.append(form + ", ");
		sb.append("form1 = ");
		sb.append(form1 + ", ");
		sb.append("form2 = ");
		sb.append(form2 + ", ");
		sb.append("form3 = ");
		sb.append(form3 + ", ");
		sb.append("form4 = ");
		sb.append(form4 + ", ");
		sb.append("formAtack = ");
		sb.append(formAtack + ", ");
		sb.append("formDefence = ");
		sb.append(formDefence);

		String ret = updateSd + sb.toString() + " where team like '" + team
				+ "');";
		return ret;
	}

	public String getTeam() {
		return team;
	}

	public void setTeam(String team) {
		this.team = team;
	}

	public int getTeamid() {
		return teamid;
	}

	public void setTeamid(int teamid) {
		this.teamid = teamid;
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

	public int getP3MatchesIn() {
		return p3MatchesIn;
	}

	public void setP3MatchesIn(int p3MatchesIn) {
		this.p3MatchesIn = p3MatchesIn;
	}

	public int getP3MatchesOut() {
		return p3MatchesOut;
	}

	public void setP3MatchesOut(int p3MatchesOut) {
		this.p3MatchesOut = p3MatchesOut;
	}

	public int getP3FtScoreIn() {
		return p3FtScoreIn;
	}

	public void setP3FtScoreIn(int p3FtScoreIn) {
		this.p3FtScoreIn = p3FtScoreIn;
	}

	public int getP3FtScoreOut() {
		return p3FtScoreOut;
	}

	public void setP3FtScoreOut(int p3FtScoreOut) {
		this.p3FtScoreOut = p3FtScoreOut;
	}

	public int getP3FtConcededIn() {
		return p3FtConcededIn;
	}

	public void setP3FtConcededIn(int p3FtConcededIn) {
		this.p3FtConcededIn = p3FtConcededIn;
	}

	public int getP3FtConcededOut() {
		return p3FtConcededOut;
	}

	public void setP3FtConcededOut(int p3FtConcededOut) {
		this.p3FtConcededOut = p3FtConcededOut;
	}

	public int getTtMatchesIn() {
		return ttMatchesIn;
	}

	public void setTtMatchesIn(int ttMatchesIn) {
		this.ttMatchesIn = ttMatchesIn;
	}

	public int getTtMatchesOut() {
		return ttMatchesOut;
	}

	public void setTtMatchesOut(int ttMatchesOut) {
		this.ttMatchesOut = ttMatchesOut;
	}

	public int getTtFtScoreIn() {
		return ttFtScoreIn;
	}

	public void setTtFtScoreIn(int ttFtScoreIn) {
		this.ttFtScoreIn = ttFtScoreIn;
	}

	public int getTtFtScoreOut() {
		return ttFtScoreOut;
	}

	public void setTtFtScoreOut(int ttFtScoreOut) {
		this.ttFtScoreOut = ttFtScoreOut;
	}

	public int getTtFtConcededIn() {
		return ttFtConcededIn;
	}

	public void setTtFtConcededIn(int ttFtConcededIn) {
		this.ttFtConcededIn = ttFtConcededIn;
	}

	public int getTtFtConcededOut() {
		return ttFtConcededOut;
	}

	public void setTtFtConcededOut(int ttFtConcededOut) {
		this.ttFtConcededOut = ttFtConcededOut;
	}

	public int getP3UpMatchesIn() {
		return p3UpMatchesIn;
	}

	public void setP3UpMatchesIn(int p3UpMatchesIn) {
		this.p3UpMatchesIn = p3UpMatchesIn;
	}

	public int getP3UpMatchesOut() {
		return p3UpMatchesOut;
	}

	public void setP3UpMatchesOut(int p3UpMatchesOut) {
		this.p3UpMatchesOut = p3UpMatchesOut;
	}

	public int getP3UpFtScoreIn() {
		return p3UpFtScoreIn;
	}

	public void setP3UpFtScoreIn(int p3UpFtScoreIn) {
		this.p3UpFtScoreIn = p3UpFtScoreIn;
	}

	public int getP3UpFtScoreOut() {
		return p3UpFtScoreOut;
	}

	public void setP3UpFtScoreOut(int p3UpFtScoreOut) {
		this.p3UpFtScoreOut = p3UpFtScoreOut;
	}

	public int getP3UpFtConcededIn() {
		return p3UpFtConcededIn;
	}

	public void setP3UpFtConcededIn(int p3UpFtConcededIn) {
		this.p3UpFtConcededIn = p3UpFtConcededIn;
	}

	public int getP3UpFtConcededOut() {
		return p3UpFtConcededOut;
	}

	public void setP3UpFtConcededOut(int p3UpFtConcededOut) {
		this.p3UpFtConcededOut = p3UpFtConcededOut;
	}

	public int getP3DownMatchesIn() {
		return p3DownMatchesIn;
	}

	public void setP3DownMatchesIn(int p3DownMatchesIn) {
		this.p3DownMatchesIn = p3DownMatchesIn;
	}

	public int getP3DownMatchesOut() {
		return p3DownMatchesOut;
	}

	public void setP3DownMatchesOut(int p3DownMatchesOut) {
		this.p3DownMatchesOut = p3DownMatchesOut;
	}

	public int getP3DownFtScoreIn() {
		return p3DownFtScoreIn;
	}

	public void setP3DownFtScoreIn(int p3DownFtScoreIn) {
		this.p3DownFtScoreIn = p3DownFtScoreIn;
	}

	public int getP3DownFtScoreOut() {
		return p3DownFtScoreOut;
	}

	public void setP3DownFtScoreOut(int p3DownFtScoreOut) {
		this.p3DownFtScoreOut = p3DownFtScoreOut;
	}

	public int getP3DownFtConcededIn() {
		return p3DownFtConcededIn;
	}

	public void setP3DownFtConcededIn(int p3DownFtConcededIn) {
		this.p3DownFtConcededIn = p3DownFtConcededIn;
	}

	public int getP3DownFtConcededOut() {
		return p3DownFtConcededOut;
	}

	public void setP3DownFtConcededOut(int p3DownFtConcededOut) {
		this.p3DownFtConcededOut = p3DownFtConcededOut;
	}

	public float getForm() {
		return form;
	}

	public void setForm(float form) {
		this.form = form;
	}

	public float getForm1() {
		return form1;
	}

	public void setForm1(float form1) {
		this.form1 = form1;
	}

	public float getForm2() {
		return form2;
	}

	public void setForm2(float form2) {
		this.form2 = form2;
	}

	public float getForm3() {
		return form3;
	}

	public void setForm3(float form3) {
		this.form3 = form3;
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

	// public void addHtScoreIn(int htScoreIn) {
	// this.htScoreIn += htScoreIn;
	// }
	//
	// public void addHtScoreOut(int htScoreOut) {
	// this.htScoreOut += htScoreOut;
	// }
	//
	// public void addHtConcededIn(int htConcededIn) {
	// this.htConcededIn += htConcededIn;
	// }
	//
	// public void addHtConcededOut(int htConcededOut) {
	// this.htConcededOut += htConcededOut;
	// }

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
		this.p3MatchesOut +=1;
	}

//	public void addP3HtScoreIn(int p3HtScoreIn) {
//		this.p3HtScoreIn += p3HtScoreIn;
//	}
//
//	public void addP3HtScoreOut(int p3HtScoreOut) {
//		this.p3HtScoreOut += p3HtScoreOut;
//	}
//
//	public void addP3HtConcededIn(int p3HtConcededIn) {
//		this.p3HtConcededIn += p3HtConcededIn;
//	}
//
//	public void addP3HtConcededOut(int p3HtConcededOut) {
//		this.p3HtConcededOut += p3HtConcededOut;
//	}

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
		this.ttMatchesOut +=1;
	}

//	public void addTtHtScoreIn(int ttHtScoreIn) {
//		this.ttHtScoreIn += ttHtScoreIn;
//	}
//
//	public void addTtHtScoreOut(int ttHtScoreOut) {
//		this.ttHtScoreOut += ttHtScoreOut;
//	}
//
//	public void addTtHtConcededIn(int ttHtConcededIn) {
//		this.ttHtConcededIn += ttHtConcededIn;
//	}
//
//	public void addTtHtConcededOut(int ttHtConcededOut) {
//		this.ttHtConcededOut += ttHtConcededOut;
//	}

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

//	public void addP3UpHtScoreIn(int p3UpHtScoreIn) {
//		this.p3UpHtScoreIn += p3UpHtScoreIn;
//	}
//
//	public void addP3UpHtScoreOut(int p3UpHtScoreOut) {
//		this.p3UpHtScoreOut += p3UpHtScoreOut;
//	}
//
//	public void addP3UpHtConcededIn(int p3UpHtConcededIn) {
//		this.p3UpHtConcededIn += p3UpHtConcededIn;
//	}
//
//	public void addP3UpHtConcededOut(int p3UpHtConcededOut) {
//		this.p3UpHtConcededOut += p3UpHtConcededOut;
//	}

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
		this.p3DownMatchesOut +=1;
	}

//	public void addP3DownHtScoreIn(int p3DownHtScoreIn) {
//		this.p3DownHtScoreIn += p3DownHtScoreIn;
//	}
//
//	public void addP3DownHtScoreOut(int p3DownHtScoreOut) {
//		this.p3DownHtScoreOut += p3DownHtScoreOut;
//	}
//
//	public void addP3DownHtConcededIn(int p3DownHtConcededIn) {
//		this.p3DownHtConcededIn += p3DownHtConcededIn;
//	}
//
//	public void addP3DownHtConcededOut(int p3DownHtConcededOut) {
//		this.p3DownHtConcededOut += p3DownHtConcededOut;
//	}

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
		this.form = form;
	}

	public void addForm1(float form1) {
		this.form1 = form1;
	}

	public void addForm2(float form2) {
		this.form2 = form2;
	}

	public void addForm3(float form3) {
		this.form3 = form3;
	}

	public void addForm4(float form4) {
		this.form4 = form4;
	}

	public void addFormAtack(float formAtack) {
		this.formAtack = formAtack;
	}

	public void addFormDefence(float formDefence) {
		this.formDefence = formDefence;
	}

	
	
}
