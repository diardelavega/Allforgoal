package structures;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import basicStruct.ClasifficationStruct;
import dbhandler.BasicTableEntity;
import dbhandler.FullTableMaker;
import dbhandler.TableMaker;
import dbtry.Conn;

/**
 * @author Administrator
 *
 *         This class should contains the competitions team tables (points,
 *         wins, draws, losses, etc) It should read the data from the db, fill
 *         an array with full or half tablemaker AND update the db table with
 *         the new results.
 */
public class CompetitionTeamTable {
	final Logger logger = LoggerFactory.getLogger(CompetitionTeamTable.class);

	// private List<FullTableMaker> classificationFtm = new ArrayList<>();
	// private List<TableMaker> classificationTm = new ArrayList<>();
	private List<BasicTableEntity> classificationPos = new ArrayList<>();
	private boolean isFullTable = false;
	private boolean isTable = false;

	public void existsDb(String dbName) throws SQLException {
		/*
		 * search database to see if the table exists and if it has a particular
		 * field to see if it is full or half table
		 */
		Conn conn = new Conn();
		conn.open();
		DatabaseMetaData metadata = conn.getConn().getMetaData();
		ResultSet resultSet = metadata.getTables(null, null, dbName, null);
		if (resultSet.next()) {
			isTable = true;
			// logger.info(resultSet.getString("TABLE_NAME"));
			resultSet = metadata.getColumns(null, null, dbName, "htScoreIn");
			if (resultSet.next())
				isFullTable = true;
			// logger.info(resultSet.getString("COLUMN_NAME"));
		}
		conn.close();
	}

	public void tableReader(String compName, String t1, String t2)
			throws SQLException {
		// read all the table data and fill the list with the results
		String query;
		if (isFullTable) {
			query = "SELECT * FROM " + compName
					+ "FullTable ORDER BY points DESC;";
		} else {
			query = "SELECT * FROM " + compName + "Table ORDER BY points DESC;";
		}
		Conn conn = new Conn();
		conn.open();
		ResultSet rs = conn.getConn().createStatement().executeQuery(query);
		tableEntity(rs);
		rs.close();
		conn.close();
	}

	public void tableEntity(ResultSet rs) throws SQLException {
		/*
		 * create Java Entity from DB Relation and store it in the
		 * classification position list, daa is ordered by points
		 */
		// TableMaker tm = new TableMaker();
		BasicTableEntity tm = new BasicTableEntity();
		// if (classificationTm == null) {
		// classificationTm = new ArrayList<>();
		// }
		while (rs.next()) {
			tm.setPoints(rs.getInt("points"));
			tm.setMatchesIn(rs.getInt("matchesin"));
			tm.setMatchesOut(rs.getInt("matchesout"));
			tm.setFtScoreOut(rs.getInt("ftscoreout"));
			tm.setFtScoreIn(rs.getInt("ftscorein"));
			tm.setFtConcededOut(rs.getInt("ftconcedeout"));
			tm.setFtConcededIn(rs.getInt("ftconcedein"));

			tm.setP3MatchesIn(rs.getInt("p3_min"));
			tm.setP3MatchesOut(rs.getInt("p3_mout"));
			tm.setP3FtScoreOut(rs.getInt("p3_ftscoreout"));
			tm.setP3FtScoreIn(rs.getInt("p3_ftscoreint"));
			tm.setP3FtConcededOut(rs.getInt("p3_ftconcedeout"));
			tm.setP3FtConcededIn(rs.getInt("p3_ftconcedein"));

			tm.setTtMatchesIn(rs.getInt("tt_min"));
			tm.setTtMatchesOut(rs.getInt("tt_mout"));
			tm.setTtFtScoreOut(rs.getInt("tt_ftscoreout"));
			tm.setTtFtScoreIn(rs.getInt("tt_ftscoreint"));
			tm.setTtFtConcededOut(rs.getInt("tt_ftconcedeout"));
			tm.setTtFtConcededIn(rs.getInt("tt_ftconcedein"));

			tm.setP3DownMatchesIn(rs.getInt("p3down_min"));
			tm.setP3DownMatchesOut(rs.getInt("p3down_mout"));
			tm.setP3DownFtScoreOut(rs.getInt("p3down_ftscoreout"));
			tm.setP3DownFtScoreIn(rs.getInt("p3down_ftscoreint"));
			tm.setP3DownFtConcededOut(rs.getInt("p3down_ftconcedeout"));
			tm.setP3DownFtConcededIn(rs.getInt("p3down_ftconcedein"));

			tm.setP3UpMatchesIn(rs.getInt("p3up_min"));
			tm.setP3UpMatchesOut(rs.getInt("p3up_mout"));
			tm.setP3UpFtScoreOut(rs.getInt("p3up_ftscoreout"));
			tm.setP3UpFtScoreIn(rs.getInt("p3up_ftscoreint"));
			tm.setP3UpFtConcededOut(rs.getInt("p3up_ftconcedeout"));
			tm.setP3UpFtConcededIn(rs.getInt("p3up_ftconcedein"));

			tm.setForm(rs.getFloat("form"));
			tm.setForm1(rs.getFloat("form1"));
			tm.setForm2(rs.getFloat("form2"));
			tm.setForm3(rs.getFloat("form3"));

			if (isFullTable) {
				tm.setHtScoreIn(rs.getInt("htscorein"));
				tm.setHtScoreOut(rs.getInt("htscoreout"));
				tm.setHtConcededOut(rs.getInt("htconcedeout"));
				tm.setHtConcededIn(rs.getInt("htconcedein"));

				tm.setP3HtScoreOut(rs.getInt("p3_htscoreout"));
				tm.setP3HtScoreIn(rs.getInt("p3_htscoreint"));
				tm.setP3HtConcededOut(rs.getInt("p3_htconcedeout"));
				tm.setP3HtConcededIn(rs.getInt("p3_htconcedein"));

				tm.setTtHtScoreOut(rs.getInt("tt_htscoreout"));
				tm.setTtHtScoreIn(rs.getInt("tt_htscoreint"));
				tm.setTtHtConcededOut(rs.getInt("tt_htconcedeout"));
				tm.setTtHtConcededIn(rs.getInt("tt_htconcedein"));

				tm.setP3DownHtScoreOut(rs.getInt("p3down_htscoreout"));
				tm.setP3DownHtScoreIn(rs.getInt("p3down_htscoreint"));
				tm.setP3DownHtConcededOut(rs.getInt("p3down_htconcedeout"));
				tm.setP3DownHtConcededIn(rs.getInt("p3down_htconcedein"));

				tm.setP3UpHtScoreOut(rs.getInt("p3up_htscoreout"));
				tm.setP3UpHtScoreIn(rs.getInt("p3up_htscoreint"));
				tm.setP3UpHtConcededOut(rs.getInt("p3up_htconcedeout"));
				tm.setP3UpHtConcededIn(rs.getInt("p3up_htconcedein"));
			}

			classificationPos.add(tm);
		}
	}

	public boolean isFullTable() {
		return isFullTable;
	}

	public boolean isTable() {
		return isTable;
	}

	public void createFullTable(String tableName) throws SQLException {
		String create = "createTable Full" + tableName + " ( ";
		String attributes = " team varchar(25) not null unique, "
				+ "teamid int, points int not null, matchesin int not null, matchesout int not null, "
				+ " htscorein int, htscoreout int, htconcedein int, htconcedeout int, "
				+ " ftscorein int not null, ftscoreout int not null, ftconcedein int, ftconcedeout int, "
				+ "p3_matchesin int,  p3_matchesout int,  p3_htscorein int not null,  p3_htscoreout int not null, p3_htconcedein int, p3_htconcedeout int, "
				+ "p3_ftscorein int not null,  p3_ftscoreout int not null, p3_ftconcedein int, p3_ftconcedeout int, "
				+ " tt_matchesin int,  tt_matchesout int, tt_htscorein int not null, tt_htscoreout int not null, tt_htconcedein int, tt_htconcedeout int, "
				+ "tt_ftscorein int not null, tt_ftscoreout int not null, tt_ftconcedein int, tt_ftconcedeout int, "
				+ " p3up_matchesin int,  p3up_matchesout int, p3up_htscorein int not null, p3up_htscoreout int not null, p3up_htconcedein int, p3up_htconcedeout int, "
				+ "p3up_ftscorein int not null, p3up_ftscoreout int not null, p3up_ftconcedein int, p3up_ftconcedeout int, "
				+ "p3down_matchesin int,  p3down_matchesout int, p3down_htscorein int not null, p3down_htscoreout int not null, p3down_htconcedein int, p3down_htconcedeout int, "
				+ "p3down_ftscorein int not null, p3down_ftscoreout int not null, p3down_ftconcedein int, p3down_ftconcedeout int, "
				+ " form numeric(5,3) , form1 numeric(5,3) , form2 numeric(5,3), form3 numeric(5,3), form4 numeric(5,3), formAtack numeric(5,3), formDefence numeric(5,3) ";

		Conn conn = new Conn();
		conn.open();
		conn.getConn().createStatement().execute(create + attributes + " );");
		conn.close();

		// return create + attributes + " );";
	}

	public void orderClassificationTable() {
		// TODO sort the classificationPos based on the points of eachteam
	}

	public boolean hasTeamInClassification(String team) {
		for (int i = 0; i < classificationPos.size(); i++) {
			if (classificationPos.get(i).getTeam() == team) {
				return true;
			}
		}
		return false;
	}

	public List<BasicTableEntity> getClassificationPos() {
		return classificationPos;
	}

}
