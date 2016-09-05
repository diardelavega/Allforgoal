package api.functionality;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import structures.CountryCompetition;
import api.functionality.obj.CountryCompCompId;
import basicStruct.CCAllStruct;
import dbtry.Conn;

public class CompIdToCountryCompCompID {

	public static Logger log = LoggerFactory
			.getLogger(CompIdToCountryCompCompID.class);

	public CountryCompCompId search(int compId) throws SQLException {
		int idx = CountryCompetition.idToIdx.get(compId);
		CCAllStruct cc = CountryCompetition.ccasList.get(idx);
		return (new CountryCompCompId(cc.getCountry(), cc.getCompetition(),
				compId));

		/*
		 * Conn conn = new Conn(); conn.open();
		 * 
		 * String country = null, competiion = null; ResultSet rs = null; rs =
		 * conn .getConn() .createStatement() .executeQuery(
		 * "SELECT country, competition FROM ccallstruct WHERE compid = " +
		 * compId + ";");
		 * 
		 * if (rs.next()) { country = rs.getString("country"); competiion =
		 * rs.getString("competition"); log.info("{}  {}", country, competiion);
		 * } else { log.info("{}  {}", country, competiion);
		 * log.warn("Docs not found"); return null; }
		 * 
		 * rs.close(); conn.close();
		 * 
		 * return (new CountryCompCompId(country, competiion, compId));
		 */
	}

}
