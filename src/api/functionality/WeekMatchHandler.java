package api.functionality;

import java.sql.SQLException;
import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import api.functionality.obj.CountryCompCompId;
import api.functionality.obj.MatchSpecificLine;
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
	private MatchSpecificLine msl;

	public String redWeekMatches(int compId, String competition, String country, LocalDate ld) {

		TestPredFile tspf = new TestPredFile();
		String csvTxt = tspf.reducedCsv(compId, competition, country, ld);
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

	public String redWeekMatches_TodTom(int compId, String competition, String country) {
		/*
		 * read the matches from test file of today & tomorrow so that to have
		 * the needed data even for tomorrow in the all matches page.
		 */

		TestPredFile tspf = new TestPredFile();
		String csvTxtTod = tspf.reducedCsv(compId, competition, country, LocalDate.now());
		linesRead = tspf.getLinesRead();

		String csvTxtTom = tspf.reducedCsv(compId, competition, country, LocalDate.now().plusDays(1));
		linesRead = tspf.getLinesRead();

		TrainPredFile csvTprf = new TrainPredFile();
		String formData = csvTprf.reducedCsv(compId, competition, country);
		linesRead += csvTprf.getLinesRead();

		if (formData == null)
			return null;
		if (csvTxtTom == null) {
			if (csvTxtTod == null) {
				return formData;
			} else {
				return (csvTxtTod + formData);
			}
		} else {// (csvTxtTom != null)
			if (csvTxtTod == null) {
				return (csvTxtTom + formData);
			} else {
				return (csvTxtTom + csvTxtTod + formData);
			}
		}

	}

	public String fullWeekMatches(int compId, String competition, String country, LocalDate ld, String t1, String t2) {
		TestPredFile tspf = new TestPredFile();
		String csvTxtTod = tspf.fullCsv(compId, competition, country, ld, t1, t2);
		linesRead = tspf.getLinesRead();

		TrainPredFile csvTprf = new TrainPredFile();
		String formData = csvTprf.fullCsv(compId, competition, country, t1, t2);
		linesRead += csvTprf.getLinesRead();
		msl.setT1Over_nr(csvTprf.getT1o());
		msl.setT1Under_nr(csvTprf.getT1u());
		msl.setT1GG_nr(csvTprf.getT1gg());
		msl.setT2Over_nr(csvTprf.getT2o());
		msl.setT2Under_nr(csvTprf.getT2u());
		msl.setT2GG_nr(csvTprf.getT2gg());

		if (formData == null)
			return null;
		if (csvTxtTod == null) {
			return (formData);
		} else {
			return (csvTxtTod + formData);
		}

	}

	public int getLinesRead() {
		return linesRead;
	}

	public void setLinesRead(int linesRead) {
		this.linesRead = linesRead;
	}

	public MatchSpecificLine getMsl() {
		return msl;
	}

	public void setMsl(MatchSpecificLine msl) {
		this.msl = msl;
	}

}
