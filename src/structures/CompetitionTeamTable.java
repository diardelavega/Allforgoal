package structures;

import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
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
	private String tableName;
	private int rowSize;// rows in the db

	public CompetitionTeamTable(String tableName, String countryName) {
		super();
		//TODO set "this.tableName"  to a combo of country$tablename
		this.tableName = (tableName+"$"+countryName).replace(".", "");
	}

	public void existsDb() throws SQLException {
		/*
		 * search database to see if the table exists and if it has a particular
		 * field to see if it is full or half table
		 */
		Conn conn = new Conn();
		conn.open();
		DatabaseMetaData metadata = conn.getConn().getMetaData();
		ResultSet resultSet = metadata.getTables(null, null, tableName+ "_FullTable", null);
		if (resultSet.next()) {
			isTable = true;
			// resultSet = metadata.getColumns(null, null, dbName, "htScoreIn");
			//take nw of rows in the db
			resultSet=conn.getConn().createStatement().executeQuery("select count(*) from "+ tableName
					+ "_FullTable;");
			
			if (resultSet.next()) {
				rowSize=resultSet.getInt(1);
			}
		}
		
		
		resultSet.close();
		conn.close();
	}

	public void tableReader() throws SQLException {
		// read all the table data and fill the list with the results
		String query;
		// if (isFullTable) {
		query = "SELECT * FROM " + tableName
				+ "_FullTable ORDER BY points DESC;";
		Conn conn = new Conn();
		conn.open();
		ResultSet rs = conn.getConn().createStatement().executeQuery(query);
		 tableEntity(rs);
		rs.close();
		conn.close();
	}

	private void tableEntity(ResultSet rs) throws SQLException {
		/*
		 * create Java Entity from DB Relation and store it in the
		 * classification position list, daa is ordered by points
		 */
		// TableMaker tm = new TableMaker();
		BasicTableEntity tm;
		// if (classificationTm == null) {
		// classificationTm = new ArrayList<>();
		// }
		while (rs.next()) {
			tm = new BasicTableEntity();
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
			tm.setForm1(rs.getFloat("form1"));
			tm.setForm2(rs.getFloat("form2"));
			tm.setForm3(rs.getFloat("form3"));
			tm.setFormAtack(rs.getFloat("formAtack"));
			tm.setFormAtackIn(rs.getFloat("formAtackIn"));
			tm.setFormAtackOut(rs.getFloat("formAtackOut"));

			tm.setFormDefence(rs.getFloat("formDefence"));
			tm.setFormDefenceIn(rs.getFloat("formDefenceIn"));
			tm.setFormDefenceOut(rs.getFloat("formDefenceOut"));

			tm.setAvgWinCont(rs.getInt("winsIn"));
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

			// if (isFullTable) {
			// tm.setHtScoreIn(rs.getInt("htscorein"));
			// tm.setHtScoreOut(rs.getInt("htscoreout"));
			// tm.setHtConcededOut(rs.getInt("htconcedeout"));
			// tm.setHtConcededIn(rs.getInt("htconcedein"));
			//
			// tm.setP3HtScoreOut(rs.getInt("p3_htscoreout"));
			// tm.setP3HtScoreIn(rs.getInt("p3_htscorein"));
			// tm.setP3HtConcededOut(rs.getInt("p3_htconcedeout"));
			// tm.setP3HtConcededIn(rs.getInt("p3_htconcedein"));
			//
			// tm.setTtHtScoreOut(rs.getInt("tt_htscoreout"));
			// tm.setTtHtScoreIn(rs.getInt("tt_htscorein"));
			// tm.setTtHtConcededOut(rs.getInt("tt_htconcedeout"));
			// tm.setTtHtConcededIn(rs.getInt("tt_htconcedein"));
			//
			// tm.setP3DownHtScoreOut(rs.getInt("p3down_htscoreout"));
			// tm.setP3DownHtScoreIn(rs.getInt("p3down_htscorein"));
			// tm.setP3DownHtConcededOut(rs.getInt("p3down_htconcedeout"));
			// tm.setP3DownHtConcededIn(rs.getInt("p3down_htconcedein"));
			//
			// tm.setP3UpHtScoreOut(rs.getInt("p3up_htscoreout"));
			// tm.setP3UpHtScoreIn(rs.getInt("p3up_htscorein"));
			// tm.setP3UpHtConcededOut(rs.getInt("p3up_htconcedeout"));
			// tm.setP3UpHtConcededIn(rs.getInt("p3up_htconcedein"));
			// }

			classificationPos.add(tm);
		}
	}

	public void createFullTable() throws SQLException {
		//TODO get a combo of country& talename
		String create = "create table " + tableName + "_fullTable ( ";

		String attributes = " team varchar(25) not null unique, "
				+ "teamid int, points int not null, matchesin int not null, matchesout int not null, "
				+ " htscorein int, htscoreout int, htconcedein int, htconcedeout int, "
				+ " ftscorein int not null, ftscoreout int not null, ftconcedein int, ftconcedeout int, "
				+ " p3_matchesin int,  p3_matchesout int,  p3_htscorein int not null,  p3_htscoreout int not null, p3_htconcedein int, p3_htconcedeout int, "
				+ " p3_ftscorein int not null,  p3_ftscoreout int not null, p3_ftconcedein int, p3_ftconcedeout int, "
				+ " tt_matchesin int,  tt_matchesout int, tt_htscorein int not null, tt_htscoreout int not null, tt_htconcedein int, tt_htconcedeout int, "
				+ " tt_ftscorein int not null, tt_ftscoreout int not null, tt_ftconcedein int, tt_ftconcedeout int, "
				+ " p3up_matchesin int,  p3up_matchesout int, p3up_htscorein int not null, p3up_htscoreout int not null, p3up_htconcedein int, p3up_htconcedeout int, "
				+ " p3up_ftscorein int not null, p3up_ftscoreout int not null, p3up_ftconcedein int, p3up_ftconcedeout int, "
				+ " p3down_matchesin int,  p3down_matchesout int, p3down_htscorein int not null, p3down_htscoreout int not null, p3down_htconcedein int, p3down_htconcedeout int, "
				+ " p3down_ftscorein int not null, p3down_ftscoreout int not null, p3down_ftconcedein int, p3down_ftconcedeout int, "
				+ " form numeric(5,3) , form1 numeric(5,3) , form2 numeric(5,3) , form3 numeric(5,3) , form4 numeric(5,3) , formAtack numeric(5,3) , formAtackIn numeric(5,3) , formAtackOut numeric(5,3) , "
				+ " formDefence numeric(5,3) , formDefenceIn numeric(5,3) , formDefenceOut numeric(5,3),"
				+ " avgWinCont numeric(5,3), avgDrawCont numeric(5,3), avgLoseCont numeric(5,3), winsIn int(2), winsOut int(2),"
				+ " drawsIn int(2), drawsOut int(2), losesIn int(2), losesOut int(2), ftgg int(2), htgg int(2) ";

		logger.info(create);

		Conn conn = new Conn();
		conn.open();
		conn.getConn().createStatement().execute(create + attributes + " );");
		conn.close();

		// return create + attributes + " );";
	}

	public void insertTable() throws SQLException {
		String sql = "insert into " + tableName
				+ "_FullTable values (?,?,?,?,?,?,?,?,?,?,"
				+ "?,?,?,?,?,?,?,?,?,?," + "?,?,?,?,?,?,?,?,?,?,"
				+ "?,?,?,?,?,?,?,?,?,?," + "?,?,?,?,?,?,?,?,?,?,"
				+ "?,?,?,?,?,?,?,?,?,?," + "?,?,?,?,?,?,?,?,?,?," + "?,?,?,?,?);";

		// logger.info(sql);
		Conn conn = new Conn();
		conn.open();
		PreparedStatement ps = conn.getConn().prepareStatement(sql);
		int i = 0;
		for (BasicTableEntity o : classificationPos) {
			ps.setString(1, o.getTeam());
			ps.setInt(2, 0);
			ps.setInt(3, o.getPoints());

			ps.setInt(4, o.getMatchesIn());
			ps.setInt(5, o.getMatchesOut());
			ps.setInt(6, o.getHtScoreIn());
			ps.setInt(7, o.getHtScoreOut());
			ps.setInt(8, o.getHtConcededIn());
			ps.setInt(9, o.getHtConcededOut());
			ps.setInt(10, o.getFtScoreIn());
			ps.setInt(11, o.getFtScoreOut());
			ps.setInt(12, o.getFtConcededIn());
			ps.setInt(13, o.getFtConcededOut());

			ps.setInt(14, o.getP3MatchesIn());
			ps.setInt(15, o.getP3MatchesOut());
			ps.setInt(16, o.getP3HtScoreIn());
			ps.setInt(17, o.getP3HtScoreOut());
			ps.setInt(18, o.getP3HtConcededIn());
			ps.setInt(19, o.getP3HtConcededOut());
			ps.setInt(20, o.getP3FtScoreIn());
			ps.setInt(21, o.getP3FtScoreOut());
			ps.setInt(22, o.getP3FtConcededIn());
			ps.setInt(23, o.getP3FtConcededOut());

			ps.setInt(24, o.getTtMatchesIn());
			ps.setInt(25, o.getTtMatchesOut());
			ps.setInt(26, o.getTtHtScoreIn());
			ps.setInt(27, o.getTtHtScoreOut());
			ps.setInt(28, o.getTtHtConcededIn());
			ps.setInt(29, o.getTtHtConcededOut());
			ps.setInt(30, o.getTtFtScoreIn());
			ps.setInt(31, o.getTtFtScoreOut());
			ps.setInt(32, o.getTtFtConcededIn());
			ps.setInt(33, o.getTtFtConcededOut());

			ps.setInt(34, o.getP3UpMatchesIn());
			ps.setInt(35, o.getP3UpMatchesOut());
			ps.setInt(36, o.getP3UpHtScoreIn());
			ps.setInt(37, o.getP3UpHtScoreOut());
			ps.setInt(38, o.getP3UpHtConcededIn());
			ps.setInt(39, o.getP3UpHtConcededOut());
			ps.setInt(40, o.getP3UpFtScoreIn());
			ps.setInt(41, o.getP3UpFtScoreOut());
			ps.setInt(42, o.getP3UpFtConcededIn());
			ps.setInt(43, o.getP3UpFtConcededOut());

			ps.setInt(44, o.getP3DownMatchesIn());
			ps.setInt(45, o.getP3DownMatchesOut());
			ps.setInt(46, o.getP3DownHtScoreIn());
			ps.setInt(47, o.getP3DownHtScoreOut());
			ps.setInt(48, o.getP3DownHtConcededIn());
			ps.setInt(49, o.getP3DownHtConcededOut());
			ps.setInt(50, o.getP3DownFtScoreIn());
			ps.setInt(51, o.getP3DownFtScoreOut());
			ps.setInt(52, o.getP3DownFtConcededIn());
			ps.setInt(53, o.getP3DownFtConcededOut());

			ps.setFloat(54, o.getForm());
			ps.setFloat(55, o.getForm1());
			ps.setFloat(56, o.getForm2());
			ps.setFloat(57, o.getForm3());
			ps.setFloat(58, o.getForm4());

			ps.setFloat(59, o.getFormAtack());
			ps.setFloat(60, o.getFormAtackIn());
			ps.setFloat(61, o.getFormAtackOut());
			ps.setFloat(62, o.getFormDefence());
			ps.setFloat(63, o.getFormDefenceIn());
			ps.setFloat(64, o.getFormDefenceOut());

			ps.setFloat(65, o.getAvgWinCont());
			ps.setFloat(66, o.getAvgLoseCont());
			ps.setFloat(67, o.getAvgDrawCont());

			ps.setFloat(68, o.getWinsIn());
			ps.setFloat(69, o.getWinsOut());
			ps.setFloat(70, o.getDrawsIn());
			ps.setFloat(71, o.getDrawsOut());
			ps.setFloat(72, o.getLosesIn());
			ps.setFloat(73, o.getLosesOut());

			ps.setInt(74, o.getFtGg());
			ps.setInt(75, o.getHtGg());

			ps.addBatch();
			i++;
			if (i % 200 == 0) {
				ps.executeBatch();
			}

		}
		ps.executeBatch();
		ps.close();
		conn.close();

	}

	public void updateTable() throws SQLException {

		String sql = "update "
				+ tableName
				+ "_FullTable set points=?, matchesin=?, matchesout=?, htscorein = ?, htscoreout = ?, htconcedein = ?, "
				+ "htconcedeout = ?, ftscorein =?, ftscoreout =?, ftconcedein = ?, ftconcedeout = ?, "
				+ " p3_matchesin = ?,  p3_matchesout = ?,  p3_htscorein =?,  p3_htscoreout =?, p3_htconcedein = ?, p3_htconcedeout = ?, "
				+ " p3_ftscorein =?,  p3_ftscoreout =?, p3_ftconcedein = ?, p3_ftconcedeout = ?, "
				+ "  tt_matchesin = ?,  tt_matchesout = ?, tt_htscorein =?, tt_htscoreout =?, tt_htconcedein = ?, tt_htconcedeout = ?, "
				+ " tt_ftscorein =?, tt_ftscoreout =?, tt_ftconcedein = ?, tt_ftconcedeout = ?, "
				+ "  p3up_matchesin = ?,  p3up_matchesout = ?, p3up_htscorein =?, p3up_htscoreout =?, p3up_htconcedein = ?, p3up_htconcedeout = ?, "
				+ " p3up_ftscorein =?, p3up_ftscoreout =?, p3up_ftconcedein = ?, p3up_ftconcedeout = ?, "
				+ " p3down_matchesin = ?,  p3down_matchesout = ?, p3down_htscorein =?, p3down_htscoreout =?, p3down_htconcedein = ?, p3down_htconcedeout = ?, "
				+ " p3down_ftscorein =?, p3down_ftscoreout =?, p3down_ftconcedein = ?, p3down_ftconcedeout = ?, "
				+ "  form  = ? , form1  = ? , form2  = ?, form3  = ?, form4  = ?, formAtack  = ?, formAtackIn  = ?,formAtackOut  = ?, "
				+ "  formDefence  = ?, formDefenceIn  = ?, formDefenceOut  = ?, avgWinCont = ?, avgDrawCont = ?, avgLoseCont = ?, "
				+ " winsIn = ?, winsOut = ?, drawsIn = ?, drawsOut = ?, losesIn = ?, losesOut = ?, ftgg = ?, htgg = ? "
				+ "WHERE team=? ";

		Conn conn = new Conn();
		conn.open();
		PreparedStatement ps = conn.getConn().prepareStatement(sql);
		for (BasicTableEntity o : classificationPos) {
logger.info("{}",o.line());
			ps.setInt(1, o.getPoints());
			ps.setInt(2, o.getMatchesIn());
			ps.setInt(3, o.getMatchesOut());
			ps.setInt(4, o.getHtScoreIn());
			ps.setInt(5, o.getHtScoreOut());
			ps.setInt(6, o.getHtConcededIn());
			ps.setInt(7, o.getHtConcededOut());
			ps.setInt(8, o.getFtScoreIn());
			ps.setInt(9, o.getFtScoreOut());
			ps.setInt(10, o.getFtConcededIn());
			ps.setInt(11, o.getFtConcededOut());

			ps.setInt(12, o.getP3MatchesIn());
			ps.setInt(13, o.getP3MatchesOut());
			ps.setInt(14, o.getP3HtScoreIn());
			ps.setInt(15, o.getP3HtScoreOut());
			ps.setInt(16, o.getP3HtConcededIn());
			ps.setInt(17, o.getP3HtConcededOut());
			ps.setInt(18, o.getP3FtScoreIn());
			ps.setInt(19, o.getP3FtScoreOut());
			ps.setInt(20, o.getP3FtConcededIn());
			ps.setInt(21, o.getP3FtConcededOut());

			ps.setInt(22, o.getTtMatchesIn());
			ps.setInt(23, o.getTtMatchesOut());
			ps.setInt(24, o.getTtHtScoreIn());
			ps.setInt(25, o.getTtHtScoreOut());
			ps.setInt(26, o.getTtHtConcededIn());
			ps.setInt(27, o.getTtHtConcededOut());
			ps.setInt(28, o.getTtFtScoreIn());
			ps.setInt(29, o.getTtFtScoreOut());
			ps.setInt(30, o.getTtFtConcededIn());
			ps.setInt(31, o.getTtFtConcededOut());

			ps.setInt(32, o.getP3UpMatchesIn());
			ps.setInt(33, o.getP3UpMatchesOut());
			ps.setInt(34, o.getP3UpHtScoreIn());
			ps.setInt(35, o.getP3UpHtScoreOut());
			ps.setInt(36, o.getP3UpHtConcededIn());
			ps.setInt(37, o.getP3UpHtConcededOut());
			ps.setInt(38, o.getP3UpFtScoreIn());
			ps.setInt(39, o.getP3UpFtScoreOut());
			ps.setInt(40, o.getP3UpFtConcededIn());
			ps.setInt(41, o.getP3UpFtConcededOut());

			ps.setInt(42, o.getP3DownMatchesIn());
			ps.setInt(43, o.getP3DownMatchesOut());
			ps.setInt(44, o.getP3DownHtScoreIn());
			ps.setInt(45, o.getP3DownHtScoreOut());
			ps.setInt(46, o.getP3DownHtConcededIn());
			ps.setInt(47, o.getP3DownHtConcededOut());
			ps.setInt(48, o.getP3DownFtScoreIn());
			ps.setInt(49, o.getP3DownFtScoreOut());
			ps.setInt(50, o.getP3DownFtConcededIn());
			ps.setInt(51, o.getP3DownFtConcededOut());

			ps.setFloat(52, o.getForm());
			ps.setFloat(53, o.getForm1());
			ps.setFloat(54, o.getForm2());
			ps.setFloat(55, o.getForm3());
			ps.setFloat(56, o.getForm4());
			ps.setFloat(57, o.getFormAtack());
			ps.setFloat(58, o.getFormAtackIn());
			ps.setFloat(59, o.getFormAtackOut());
			ps.setFloat(60, o.getFormDefence());
			ps.setFloat(61, o.getFormDefenceIn());
			ps.setFloat(62, o.getFormDefenceOut());

			ps.setFloat(63, o.getAvgWinCont());
			ps.setFloat(64, o.getAvgLoseCont());
			ps.setFloat(65, o.getAvgDrawCont());

			ps.setFloat(66, o.getWinsIn());
			ps.setFloat(67, o.getWinsOut());
			ps.setFloat(68, o.getDrawsIn());
			ps.setFloat(69, o.getDrawsOut());
			ps.setFloat(70, o.getLosesIn());
			ps.setFloat(71, o.getLosesOut());

			ps.setInt(72, o.getFtGg());
			ps.setInt(73, o.getHtGg());
			
			ps.setString(74, o.getTeam());

			ps.addBatch();
//			ps.executeBatch();
		}
		ps.executeBatch();
		ps.close();
		conn.close();

	}

	public void orderClassificationTable() {
		//  sort the classificationPos based on the points of eachteam
		Collections.sort(classificationPos);
	}

	public List<BasicTableEntity> getClassificationPos() {
		return classificationPos;
	}

	public void testPrint() {
		for (int i = 0; i < classificationPos.size(); i++) {
			logger.info("points = {},  team = {},   goalavg = {}",
					classificationPos.get(i).getPoints(), classificationPos
							.get(i).getTeam(), classificationPos.get(i)
							.goalScored()
							- classificationPos.get(i).goalConceded());
		}
		logger.info("------------------------------------------------");
		logger.info("------------------------------------------------");
	}

	public boolean isFullTable() {
		return isFullTable;
	}

	public boolean isTable() {
		return isTable;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableNames(String tableName, String country) {
		this.tableName = (tableName+"$"+country).replace(".", "");
	}

	
	public int getRowSize() {
		return rowSize;
	}

	
	
}
