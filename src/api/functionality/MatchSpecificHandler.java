package api.functionality;

import java.sql.ResultSet;
import java.time.LocalDate;

import com.mysql.jdbc.Statement;

import api.functionality.obj.MatchSpecificDBAtts;
import api.functionality.obj.MatchSpecificLine;
import basicStruct.CCAllStruct;
import dbhandler.BasicTableEntity;
import dbtry.Conn;
import structures.CountryCompetition;

public class MatchSpecificHandler {

	private MatchSpecificLine msl= new MatchSpecificLine();

	public String getweekSpecificData(CCAllStruct ccal, LocalDate ld, String t1, String t2) {

		// get data from files
		WeekMatchHandler wmh = new WeekMatchHandler();
		wmh.setMsl(msl);
		String ret = wmh.fullWeekMatches(ccal.getCompId(), ccal.getCompetition(), ccal.getCountry(), ld, t1, t2);
		if(ret==null)
			return null;
		msl = wmh.getMsl();
		
		
		

		// get data from db

		// do some calculations maybe

		// compete the obj to send to client side

		return null;
	}

	public void readMatchSpecificAtts(String competition,String country,String t1,String t2){
		Conn conn = new Conn();
		conn.open();
		String sql ="SELECET team,point,matchesin,matchesout,htscorein,htscoreout";
		ResultSet rs=  conn.getConn().createStatement().executeQuery(sql);
		MatchSpecificDBAtts tm ;
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

			tm.setAvgWinCont(rs.getInt("avgWinCont"));
			tm.setAvgDrawCont(rs.getInt("avgDrawCont"));
			tm.setAvgLoseCont(rs.getInt("avgLoseCont"));

			tm.setWinsIn(rs.getInt("winsIn"));
			tm.setWinsOut(rs.getInt("winsOut"));
			tm.setDrawsIn(rs.getInt("drawsIn"));
			tm.setDrawsOut(rs.getInt("drawsOut"));
			tm.setLosesIn(rs.getInt("losesIn"));
			tm.setLosesOut(rs.getInt("losesOut"));
			tm.setFtGg(rs.getInt("ftgg"));
			tm.setHtGg(rs.getInt("htgg"));

			classificationPos.add(tm);
		}
	}
	
	
}
