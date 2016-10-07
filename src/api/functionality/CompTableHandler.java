package api.functionality;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import api.functionality.obj.CompTable;
import dbtry.Conn;

public class CompTableHandler {

	
	public List<CompTable> readCompTable(String compName, String country) throws SQLException{
		compName = compName.replaceAll(" ", "_").replace(".", "");
		country = country.replaceAll(" ", "_").replace(".", "");
		String tableName = compName + "$" + country;
		List<CompTable> ctl=null;
		String query = "SELECT * FROM " + tableName
				+ "_FullTable ORDER BY points DESC;";
		Conn conn = new Conn();
		conn.open();
		ResultSet rs = conn.getConn().createStatement().executeQuery(query);
		
		 CompTable tm;
		while (rs.next()) {
			tm = new CompTable();
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

			tm.setP3MatchesIn(rs.getInt("p3_matchesin"));
			tm.setP3MatchesOut(rs.getInt("p3_matchesout"));
			tm.setP3HtScoreIn(rs.getInt("p3_htscorein"));
			tm.setP3HtScoreOut(rs.getInt("p3_htscoreout"));
			tm.setP3HtConcededIn(rs.getInt("p3_htconcedein"));
			tm.setP3HtConcededOut(rs.getInt("p3_htconcedeout"));
			tm.setP3FtScoreOut(rs.getInt("p3_ftscoreout"));
			tm.setP3FtScoreIn(rs.getInt("p3_ftscorein"));
			tm.setP3FtConcededOut(rs.getInt("p3_ftconcedeout"));
			tm.setP3FtConcededIn(rs.getInt("p3_ftconcedein"));

			tm.setTtMatchesIn(rs.getInt("tt_matchesin"));
			tm.setTtMatchesOut(rs.getInt("tt_matchesout"));
			tm.setTtHtScoreIn(rs.getInt("tt_htscorein"));
			tm.setTtHtScoreOut(rs.getInt("tt_htscoreout"));
			tm.setTtHtConcededIn(rs.getInt("tt_htconcedein"));
			tm.setTtHtConcededOut(rs.getInt("tt_htconcedeout"));
			tm.setTtFtScoreOut(rs.getInt("tt_ftscoreout"));
			tm.setTtFtScoreIn(rs.getInt("tt_ftscorein"));
			tm.setTtFtConcededOut(rs.getInt("tt_ftconcedeout"));
			tm.setTtFtConcededIn(rs.getInt("tt_ftconcedein"));

			tm.setP3DownMatchesIn(rs.getInt("p3down_matchesin"));
			tm.setP3DownMatchesOut(rs.getInt("p3down_matchesout"));
			tm.setP3DownHtScoreIn(rs.getInt("p3down_htscorein"));
			tm.setP3DownHtScoreOut(rs.getInt("p3down_htscoreout"));
			tm.setP3DownHtConcededIn(rs.getInt("p3down_htconcedein"));
			tm.setP3DownHtConcededOut(rs.getInt("p3down_htconcedeout"));
			tm.setP3DownFtScoreOut(rs.getInt("p3down_ftscoreout"));
			tm.setP3DownFtScoreIn(rs.getInt("p3down_ftscorein"));
			tm.setP3DownFtConcededOut(rs.getInt("p3down_ftconcedeout"));
			tm.setP3DownFtConcededIn(rs.getInt("p3down_ftconcedein"));

			tm.setP3UpMatchesIn(rs.getInt("p3up_matchesin"));
			tm.setP3UpMatchesOut(rs.getInt("p3up_matchesout"));
			tm.setP3UpHtScoreIn(rs.getInt("p3up_htscorein"));
			tm.setP3UpHtScoreOut(rs.getInt("p3up_htscoreout"));
			tm.setP3UpHtConcededIn(rs.getInt("p3up_htconcedein"));
			tm.setP3UpHtConcededOut(rs.getInt("p3up_htconcedeout"));
			tm.setP3UpFtScoreOut(rs.getInt("p3up_ftscoreout"));
			tm.setP3UpFtScoreIn(rs.getInt("p3up_ftscorein"));
			tm.setP3UpFtConcededOut(rs.getInt("p3up_ftconcedeout"));
			tm.setP3UpFtConcededIn(rs.getInt("p3up_ftconcedein"));

			tm.setForm(rs.getFloat("form"));
			tm.setFormAtack(rs.getFloat("formAtack"));
			tm.setFormAtackIn(rs.getFloat("formAtackIn"));
			tm.setFormAtackOut(rs.getFloat("formAtackOut"));

			tm.setFormDefence(rs.getFloat("formDefence"));
			tm.setFormDefenceIn(rs.getFloat("formDefenceIn"));
			tm.setFormDefenceOut(rs.getFloat("formDefenceOut"));

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


			ctl.add(tm);
		}
		
		rs.close();
		conn.close();
		
		return ctl;
	}
}
