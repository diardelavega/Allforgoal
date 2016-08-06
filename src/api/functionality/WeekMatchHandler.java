package api.functionality;

import java.sql.SQLException;

import api.functionality.obj.CountryCompCompId;

/**
 * @author Administrator
 *
 *         a clas that will get the data from training pred file of a specified
 *         competition. It will gwt the data as csv and send it to the client as
 *         text. there the data will be parsed by papas parser and adapted to
 *         the array form required by every competitions matches.**************
 *         The AllCompetitions page will get the reduced "red" weeklymatches
 *         whereas the MatchSpecific page will get the Full version of the data.
 *         Will try to add the created array to the browsers web memory(i don't
 *         remember what is called) for efficiency reasons
 */
public class WeekMatchHandler {

	public String redWeekMatches(int compId) throws SQLException {
		TestPredFile tspf = new TestPredFile();
		CountryCompCompId ccci = new CompIdToCountryCompCompID().search(compId);
		// get the test pred file first
		String csvTxt = tspf.reducedCsv(compId, ccci.getCompetition(),
				ccci.getCountry());
		// keep the week from lastRecWeek
		int lastRecWeek = tspf.getLastRecWeek();

		// get the train pred file after.
		// for records with week <= lastRecWeek-10
		// copy record in another csvtext holder
		TrainPredFile tprf = new TrainPredFile();
		tprf.setLastRecWeek(lastRecWeek);
		String formData = tprf.reducedCsv(ccci);
		
		csvTxt = tprf.getLast10() + csvTxt;

		// in the end we have the all weeks match csv text for the form data
		// and a last 10 weeks matches+ curent week matches for common
		// adversaries

		// store the comon adv csvtxt within a static map to have it ready on
		// the next request
		CommonAdversariesHandler.commonAdv.put(compId, csvTxt);

		return formData;
	}
}
