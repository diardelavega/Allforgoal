package api.functionality;

import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import api.functionality.obj.CountryCompCompId;
import basicStruct.CCAllStruct;
import structures.CountryCompetition;

/**
 * @author Administrator
 *
 *         A class that will get the data from training pred file of a specified
 *         competition. It will get the data as csv and send it to the client as
 *         text. there the data will be parsed by papas parser and adapted to
 *         the array form required by every competitions matches.**************
 *         The AllCompetitions page will get the reduced "red" weeklymatches
 *         whereas the MatchSpecific page will get the Full version of the data.
 *         Will try to add the created array to the browsers web memory(i don't
 *         remember what is called) for efficiency reasons
 */
public class WeekMatchHandler {
	public static Logger log = LoggerFactory.getLogger(WeekMatchHandler.class);
	private int linesRead = -1;

	public String redWeekMatches(int compId, String competition, String country) throws SQLException {

		TestPredFile tspf = new TestPredFile();
		String csvTxt = tspf.reducedCsv(compId, competition, country);
		linesRead = tspf.getLinesRead();
		
		TrainPredFile tprf = new TrainPredFile();
		String formData = tprf.reducedCsv(compId, competition, country);
		linesRead += tprf.getLinesRead();

		if (csvTxt == null)
			return formData;
		if (formData == null)
			return null;
		csvTxt += formData;
		return csvTxt;
	}

	public int getLinesRead() {
		return linesRead;
	}

	public void setLinesRead(int linesRead) {
		this.linesRead = linesRead;
	}

}
