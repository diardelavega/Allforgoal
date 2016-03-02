package calculate;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import basicStruct.MatchObj;
import dbhandler.BasicTableEntity;
import dbtry.Conn;

/**
 * @author diego
 *
 *         a class that specifies the general approach with tempMatches. get new
 *         matches, store them, update their results,restorethem in the normal
 *         matches table. create test files for prediction(arff & csv),
 *         calculate form, teamtable classification, update the prediction file
 *         data with the new data
 *
 */
public class TempMatchHandeling {
	// REMAIN TO BE TESTED

	Conn conn = new Conn();
	private List<MatchObj> tempMatches = new ArrayList<>();

	public void getAllTempMatchesFromDb() throws SQLException {
		conn.open();
		ResultSet rs = conn.getConn().createStatement().executeQuery(" SELECT * FROM tempMatches ORDER BY dat ");
		mlistFill(rs);
		rs.close();
		conn.close();
	}

	public int getTempMatchesFromDb(LocalDate dat) throws SQLException {
		conn.open();
		ResultSet rs = conn.getConn().createStatement()
				.executeQuery(" SELECT * FROM tempMatches where dat ='" + dat + "');");
		int ret = rs.getInt(1);
		rs.close();
		conn.close();
		return ret;
	}

	public void sizeOfTempMatchesOnDate(LocalDate dat) throws SQLException {
		conn.open();
		ResultSet rs = conn.getConn().createStatement()
				.executeQuery(" SELECT count( *) FROM tempmatches where dat ='" + dat + "');");
		mlistFill(rs);
		rs.close();
		conn.close();
	}

	public void insertToTempMatches(List<MatchObj> mol) throws SQLException {
		String insert = "insert into tempmatches values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement ps = conn.getConn().prepareStatement(insert);
		int i = 0;
		for (MatchObj mobj : mol) {
			ps.setLong(1, mobj.getmId());
			ps.setInt(2, mobj.getComId());
			ps.setString(3, mobj.getT1());
			ps.setString(4, mobj.getT2());
			ps.setInt(5, mobj.getFt1());
			ps.setInt(6, mobj.getFt2());
			ps.setInt(7, mobj.getHt1());
			ps.setInt(8, mobj.getHt2());
			ps.setDouble(9, mobj.get_1());
			ps.setDouble(10, mobj.get_x());
			ps.setDouble(11, mobj.get_2());
			ps.setDouble(12, mobj.get_o());
			ps.setDouble(13, mobj.get_u());
			ps.setDate(14, mobj.getDat());
			ps.addBatch();
			i++;
			if (i % 200 == 0) {
				ps.executeBatch();
				i = 0;
			}
		}
		ps.executeBatch();
		ps.close();
		conn.close();

	}

	public void deleteTempMatch(LocalDate dat) throws SQLException {
		// TODO delete all records where dat = dat
		conn.open();
		ResultSet rs = conn.getConn().createStatement()
				.executeQuery(" DELETE FROM tempmatches where dat ='" + dat + "' );");
		mlistFill(rs);
		rs.close();
		conn.close();

	}

	private void mlistFill(ResultSet rs) throws SQLException {
		MatchObj mobj = null;
		List<MatchObj> ml = new ArrayList<>();
		while (rs.next()) {
			mobj = new MatchObj();
			mobj.setmId(rs.getLong(1));
			mobj.setComId(rs.getInt(2));
			mobj.setT1(rs.getString(3));
			mobj.setT2(rs.getString(4));
			mobj.setFt1(rs.getInt(5));
			mobj.setFt2(rs.getInt(6));
			mobj.setHt1(rs.getInt(7));
			mobj.setHt2(rs.getInt(8));
			mobj.set_1(rs.getFloat(9));
			mobj.set_x(rs.getFloat(10));
			mobj.set_2(rs.getFloat(11));
			mobj.set_o(rs.getFloat(12));
			mobj.set_u(rs.getFloat(13));
			mobj.setDat(rs.getDate(14));
			tempMatches.add(mobj);
		}
	}

	public List<MatchObj> getTempMatches() {
		return tempMatches;
	}

}
