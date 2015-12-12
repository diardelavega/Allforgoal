package structures;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dbtry.Conn;
import basicStruct.MatchObj;

/**
 * @author Administrator
 *
 *         A structure to keep matches. Matches can be held before their teams
 *         attributes are calculated. Matches can also be held after a request
 *         for some matches from the db. ************************************
 *         Basic STRUCTURE holds matches-competition ************************
 *         Can Query the db & Store at the db
 *
 */
public class MatchesList {

	public static Map<Integer, List<MatchObj>> readMatches =new HashMap<>();
	public static List<MatchObj> toStoreMatches=new ArrayList<>();
	private Conn conn = new Conn();

	public void readMatchesCompTeamDateFromTo(int compId, String teamName,
			Date date1, Date date2) throws SQLException {
		conn.open();
		boolean flag = false;
		StringBuilder sb = new StringBuilder();
		String query = "SELECT * FROM matches WHERE ";
		sb.append(query);
		if (compId > 0) {
			sb.append(" compId = " + compId);
			flag = true;
		}
		if (teamName != "") {
			if (flag)
				sb.append(" AND ");
			sb.append("( t1 OR t2 = '" + compId + "')");
			flag = true;
		}
		if (date1 != null) {
			if (flag)
				sb.append(" AND ");
			sb.append(" dat >= '" + date1 + "'");
			flag = true;
		}
		if (date2 != null) {
			if (flag)
				sb.append(" AND ");
			sb.append(" dat <= '" + date2 + "'");
			flag = true;
		}
		sb.append(" ORDER BY compid ");
		ResultSet rs = conn.getConn().createStatement()
				.executeQuery(sb.toString());
		listFill(rs);
		rs.close();
		conn.close();
	}

	public void insertMatches(List<MatchObj> mlist) throws SQLException {

		conn.open();

		String insert = "insert into matches values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement ps = conn.getConn().prepareStatement(insert);

		for (MatchObj mobj : mlist) {
			ps.setNull(1, mobj.getComId());
			ps.setString(2, mobj.getT1());
			ps.setString(3, mobj.getT2());
			ps.setInt(4, mobj.getFt1());
			ps.setInt(5, mobj.getFt2());
			ps.setInt(6, mobj.getHt1());
			ps.setInt(7, mobj.getHt2());
			ps.setDouble(8, mobj.get_1());
			ps.setDouble(9, mobj.get_x());
			ps.setDouble(10, mobj.get_2());
			ps.setDouble(11, mobj.get_o());
			ps.setDouble(12, mobj.get_u());
			ps.setDate(13, mobj.getDat());
			ps.addBatch();
		}
		ps.executeBatch();
		ps.close();
		conn.close();

	}

	public void listFill(ResultSet rs) throws SQLException {
		/*
		 * get the result set with the data for the matches and put them to a
		 * map
		 */
		MatchObj mobj = null;
		List<MatchObj> ml = new ArrayList<>();
		readMatches = new HashMap<>();
		while (rs.next()) {
			if (mobj.getComId() != rs.getInt(2)) {
				// if the new match is of a different competition, then store
				// the current list of matches
				readMatches.put(mobj.getComId(), ml);
			}
			mobj = new MatchObj();
			mobj.setmId(rs.getLong(1));
			mobj.setComId(rs.getInt(2));
			mobj.setT1(rs.getString(3));
			mobj.setT2(rs.getString(4));
			mobj.setFt1(rs.getInt(5));
			mobj.setFt2(rs.getInt(6));
			mobj.setHt1(rs.getInt(7));
			mobj.setHt1(rs.getInt(8));
			mobj.set_1(rs.getFloat(9));
			mobj.set_x(rs.getFloat(10));
			mobj.set_2(rs.getFloat(11));
			mobj.set_o(rs.getFloat(12));
			mobj.set_u(rs.getFloat(13));
			mobj.setDat(rs.getDate(14));
			ml.add(mobj);
		}
		readMatches.put(mobj.getComId(), ml);
	}


	public void readMatchesComp(int compId) throws SQLException {
		readMatchesCompTeamDateFromTo(compId, "", null, null);
	}

	public void readMatchesCompTeam(int compId, String teamName)
			throws SQLException {
		readMatchesCompTeamDateFromTo(compId, teamName, null, null);
	}

	public void readMatchesTeam(String teamName) throws SQLException {
		readMatchesCompTeamDateFromTo(0, teamName, null, null);
	}

	public void readMatchesTeamDateFrom(String teamName, Date date)
			throws SQLException {
		readMatchesCompTeamDateFromTo(0, teamName, date, null);
	}

	public void readMatchesTeamDateTo(String teamName, Date date)
			throws SQLException {
		readMatchesCompTeamDateFromTo(0, teamName, null, date);
	}
}
