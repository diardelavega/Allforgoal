package api.functionality;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import api.functionality.obj.CountryCompCompId;
import api.functionality.obj.WinDrawLoseWithPred;
import basicStruct.StrStrTuple;
import dbtry.Conn;

public class WinDrawLoseHandler {
	public static Logger log = LoggerFactory.getLogger(WinDrawLoseHandler.class);
	
	public void wdlOnly(CountryCompCompId ccci) throws SQLException{
		// get teams from the testpredfile
		TestPredFile tpf = new TestPredFile();
		List<StrStrTuple> advlist = tpf.daylyAdversaries(ccci.getCompId(), ccci.getCompetition(),
				ccci.getCountry());

		// get teams & wdl from db
		WinDrawLoseHandler wdl = new WinDrawLoseHandler();
		Map<String, String> wdll = wdl.windrawloseDbGet( ccci.getCompetition());

		for (StrStrTuple t : advlist) {
			/* for every dayly match get its corresponding wdl of both teams */

//			t.strstrshow();
			WinDrawLoseWithPred mld = new WinDrawLoseWithPred();
			mld.setT1(t.getS1());
			String[] temp = wdll.get(t.getS1()).split(";");
			mld.setT1wIn(Integer.parseInt(temp[0]));
			mld.setT1wOut(Integer.parseInt(temp[1]));
			mld.setT1dIn(Integer.parseInt(temp[2]));
			mld.setT1dOut(Integer.parseInt(temp[3]));
			mld.setT1lIn(Integer.parseInt(temp[4]));
			mld.setT1lOut(Integer.parseInt(temp[5]));

			mld.setT2(t.getS2());
			temp = wdll.get(t.getS2()).split(";");
			mld.setT2wIn(Integer.parseInt(temp[0]));
			mld.setT2wOut(Integer.parseInt(temp[1]));
			mld.setT2dIn(Integer.parseInt(temp[2]));
			mld.setT2dOut(Integer.parseInt(temp[3]));
			mld.setT2lIn(Integer.parseInt(temp[4]));
			mld.setT2lOut(Integer.parseInt(temp[5]));

//			matchPredLine.add(mld);
		}

	}
	
	public Map<String, String> windrawloseDbGet(String  compName)
			throws SQLException {
		/*
		 * get wdl data from the db table of a competition and tore in a map of
		 * <teamName,wIn;wout;din;dout;lin;lout>
		 */
		String query = "SELECT team, winsIn, winsOut, drawsIn,drawsOut,losesIn,losesOut FROM "
				+ compName + "_FullTable ORDER BY team ;";
		Conn conn = new Conn();
		conn.open();
		ResultSet rs = conn.getConn().createStatement().executeQuery(query);

		// <teamName,wIn;wout;din;dout;lin;lout>
		Map<String, String> ml = new HashMap<>();
		String temp;
		while (rs.next()) {
			StringBuilder sb = new StringBuilder();
			// sb.append(rs.getString(0));
			// sb.append(";");
			sb.append(rs.getString(2));
			sb.append(";");
			sb.append(rs.getString(3));
			sb.append(";");
			sb.append(rs.getString(4));
			sb.append(";");
			sb.append(rs.getString(5));
			sb.append(";");
			sb.append(rs.getString(6));
			sb.append(";");
			sb.append(rs.getString(7));
			log.info("{},  {}", rs.getString(1), sb.toString());
			ml.put(rs.getString(1), sb.toString());
		}

		rs.close();
		conn.close();
		return ml;
	}

}
