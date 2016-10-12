package api.functionality;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import api.functionality.obj.MatchSpecificDescriptionData;
import api.functionality.obj.MatchSpecificObj;
import basicStruct.CCAllStruct;
import dbhandler.MatchSpecificDBAtts;
import dbtry.Conn;
import structures.TimeVariations;

public class MatchSpecificHandler {

	private MatchSpecificDescriptionData msl = new MatchSpecificDescriptionData();
	private int t1pos = -1, t2pos = -1;
	private MatchSpecificDBAtts[] msdb = new MatchSpecificDBAtts[2];
	// private MatchSpecificLine msl = new MatchSpecificLine();

	public MatchSpecificObj getweekSpecificData(int ind, CCAllStruct ccal, LocalDate ld, String t1, String t2) {

		MatchSpecificObj mso = new MatchSpecificObj();
		mso.setCompId(ccal.getCompId());

		WeekMatchHandler wmh = new WeekMatchHandler();
		wmh.setMsl(msl);
		mso.setCsvTxt(wmh.fullWeekMatches(ccal.getCompId(), ccal.getCompetition(), ccal.getCountry(), ld, t1, t2));
		if (mso.getCsvTxt() == null)
			return null;
		msl = wmh.getMsl();// suppose we have #, o,u,gg
		mso.setLinesRead(wmh.getLinesRead());
		// get data from db
		Conn conn = new Conn();
		conn.open();
		try {
			getPosition(ccal.getCompetition(), ccal.getCountry(), t1, t2);
			readMatchSpecificAtts(conn, ccal.getCompetition(), ccal.getCountry(), t1, t2);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		conn.close();

		msl.setFml(TimeVariations.mapMPL.get(ld).get(ccal.getCompId()).get(ind));
		assert (msdb[1].getFtGg() == msl.getT2GG_nr());
		assert (msdb[0].getFtGg() == msl.getT1GG_nr());

		// compete the obj to send to client side

		mso.setMsl(msl);

		return mso;
	}

	/**
	 * @param conn
	 * @param compName
	 * @param country
	 * @param t1
	 * @param t2
	 * @throws SQLException
	 */
	public void readMatchSpecificAtts(Conn conn, String compName, String country, String t1, String t2)
			throws SQLException {
		/* read from db and fill the atts we need */
		compName = compName.replaceAll(" ", "_").replace(".", "");
		country = country.replaceAll(" ", "_").replace(".", "");
		String tableName = compName + "$" + country;

		String sql = "SELECET team, point, matchesin, matchesout, htscorein, htscoreout, htconcedein, htconcedeout, "
				+ "ftscoreout,  ftscorein,ftconcedeout, ftconcedein, form, formAtack, formDefence, avgWinCont, "
				+ "avgDrawCont, avgLoseCont, winsIn, winsOut, drawsIn, drawsOut,  losesIn, losesOut, ftgg, htgg "
				+ "FROM  " + tableName + "_FullTable WHERE team in (" + t1 + ", " + t2 + ");";
		ResultSet rs = conn.getConn().createStatement().executeQuery(sql);
		MatchSpecificDBAtts[] msdb = new MatchSpecificDBAtts[2];
		MatchSpecificDBAtts tm;
		int i = 0;
		while (rs.next()) {
			tm = new MatchSpecificDBAtts();
			tm.setTeam(rs.getString("team"));
			tm.setPoints(rs.getInt("points"));

			tm.setMatchesIn(rs.getInt("matchesin"));
			tm.setMatchesOut(rs.getInt("matchesout"));
			tm.setHtScoreIn(rs.getInt("htscorein"));
			tm.setHtScoreOut(rs.getInt("htscoreout"));
			tm.setHtConcededIn(rs.getInt("htconcedein"));
			tm.setHtConcededOut(rs.getInt("htconcedeout"));
			tm.setFtScoreOut(rs.getInt("ftscoreout"));
			tm.setFtScoreIn(rs.getInt("ftscorein"));
			tm.setFtConcededOut(rs.getInt("ftconcedeout"));
			tm.setFtConcededIn(rs.getInt("ftconcedein"));

			tm.setForm(rs.getFloat("form"));
			tm.setFormAtack(rs.getFloat("formAtack"));
			tm.setFormDefence(rs.getFloat("formDefence"));

			tm.setAvgWinsCont(rs.getFloat("avgWinCont"));
			tm.setAvgDrawCont(rs.getFloat("avgDrawCont"));
			tm.setAvgLoseCont(rs.getFloat("avgLoseCont"));

			tm.setWinsIn(rs.getInt("winsIn"));
			tm.setWinsOut(rs.getInt("winsOut"));
			tm.setDrawsIn(rs.getInt("drawsIn"));
			tm.setDrawsOut(rs.getInt("drawsOut"));
			tm.setLosesIn(rs.getInt("losesIn"));
			tm.setLosesOut(rs.getInt("losesOut"));
			tm.setFtGg(rs.getInt("ftgg"));
			tm.setHtGg(rs.getInt("htgg"));

			msdb[i] = tm;
			i++;
		}
		rs.close();
		// calculate & assign the msl vars
		msl.setT1point(msdb[0].getPoints());
		msl.setT1position(t1pos);
		msl.setT1form(msdb[0].getForm());

		msl.setT1WinIn(msdb[0].getWinsIn());
		msl.setT1WinOut(msdb[0].getWinsOut());
		msl.setT1DrawIn(msdb[0].getDrawsIn());
		msl.setT1DrawOut(msdb[0].getDrawsOut());
		msl.setT1LoseIn(msdb[0].getLosesIn());
		msl.setT1LoseOut(msdb[0].getLosesOut());

		msl.setT1avgWinCont(msdb[0].getAvgWinsCont());
		msl.setT1avgDrawCont(msdb[0].getAvgDrawCont());
		msl.setT1avgLoseCont(msdb[0].getAvgLoseCont());

		msl.setT1avgHtScoreIn(msdb[0].getHtScoreIn() / msdb[0].getMatchesIn());
		msl.setT1avgHtScoreOut(msdb[0].getHtScoreOut() / msdb[0].getMatchesOut());
		msl.setT1avgHtConcedeIn(msdb[0].getHtConcededIn() / msdb[0].getMatchesIn());
		msl.setT1avgHtConcedeOut(msdb[0].getHtConcededOut() / msdb[0].getMatchesOut());
		msl.setT1avgFtScoreIn(msdb[0].getFtScoreIn() / msdb[0].getMatchesIn());
		msl.setT1avgFtScoreOut(msdb[0].getFtScoreOut() / msdb[0].getMatchesOut());
		msl.setT1avgFtConcedeIn(msdb[0].getFtConcededIn() / msdb[0].getMatchesIn());
		msl.setT1avgFtConcedeOut(msdb[0].getFtConcededOut() / msdb[0].getMatchesOut());

		// msl.setT1FtGG(msdb[0].getFtGg());
		msl.setT1HtGG(msdb[0].getHtGg());

		// -------------
		msl.setT2point(msdb[1].getPoints());
		msl.setT2position(t1pos);
		msl.setT2form(msdb[1].getForm());

		msl.setT2WinIn(msdb[1].getWinsIn());
		msl.setT2WinOut(msdb[1].getWinsOut());
		msl.setT2DrawIn(msdb[1].getDrawsIn());
		msl.setT2DrawOut(msdb[1].getDrawsOut());
		msl.setT2LoseIn(msdb[1].getLosesIn());
		msl.setT2LoseOut(msdb[1].getLosesOut());

		msl.setT2avgWinCont(msdb[1].getAvgWinsCont());
		msl.setT2avgDrawCont(msdb[1].getAvgDrawCont());
		msl.setT2avgLoseCont(msdb[1].getAvgLoseCont());

		msl.setT2avgHtScoreIn(msdb[1].getHtScoreIn() / msdb[1].getMatchesIn());
		msl.setT2avgHtScoreOut(msdb[1].getHtScoreOut() / msdb[1].getMatchesOut());
		msl.setT2avgHtConcedeIn(msdb[1].getHtConcededIn() / msdb[1].getMatchesIn());
		msl.setT2avgHtConcedeOut(msdb[1].getHtConcededOut() / msdb[1].getMatchesOut());
		msl.setT2avgFtScoreIn(msdb[1].getFtScoreIn() / msdb[1].getMatchesIn());
		msl.setT2avgFtScoreOut(msdb[1].getFtScoreOut() / msdb[1].getMatchesOut());
		msl.setT2avgFtConcedeIn(msdb[1].getFtConcededIn() / msdb[1].getMatchesIn());
		msl.setT2avgFtConcedeOut(msdb[1].getFtConcededOut() / msdb[1].getMatchesOut());

		// msl.setT2FtGG(msdb[1].getFtGg());
		msl.setT2HtGG(msdb[1].getHtGg());

	}

	public void getPosition(String compName, String country, String t1, String t2) throws SQLException {
		compName = compName.replaceAll(" ", "_").replace(".", "");
		country = country.replaceAll(" ", "_").replace(".", "");
		String tableName = compName + "$" + country;

		Conn conn = new Conn();
		conn.open();
		String sql = "SELECET team FROM  " + tableName + "_FullTable order by points desc";
		ResultSet rs = conn.getConn().createStatement().executeQuery(sql);
		int i = 1;
		while (rs.next()) {
			if (rs.getString("team").equals(t1))
				t1pos = i;
			if (rs.getString("team").equals(t2))
				t2pos = i;
			i++;
		}
		rs.close();

	}
}
