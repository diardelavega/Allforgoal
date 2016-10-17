package calculate;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.LoggerFactory;

import structures.CompetitionTeamTable;
import structures.CountryCompetition;
import structures.PredictionFile;
import basicStruct.MatchObj;
import dbhandler.BasicTableEntity;
import diskStore.AnalyticFileHandler;
import extra.ClassifiStatus;
import extra.MatchOutcome;
import extra.NameCleaner;
import extra.TeamStatus;

/**
 * @author Administrator
 *
 *         in this class we should get the competition table of a particular
 *         competition from the db, get the fields of the teams that are playing
 *         in a Match. The tablemaker classes for each team are recalculated
 *         taking in consideration the new results and are stored back in the db
 *         *****************************************************************
 *         Also calculate and store in a file atributes for prediction
 *         algorithms. This attributes of PredictionFile are calculated before
 *         the new match data is calculated
 */
public class MatchToTableRenewal {

	public static final org.slf4j.Logger logger = LoggerFactory
			.getLogger(MatchToTableRenewal.class);

	private PredictionFile pf;
	private AnalyticFileHandler afh = new AnalyticFileHandler();

	// private List<MatchObj> matchList;
	private int compId;
	private MatchObj mobj;
	private CompetitionTeamTable ctt;
	private BasicTableEntity t1, t2;
	private int N;
	private int posT1 = -1;
	private int posT2 = -1;
	private int match_counter = 0;
	private int totMatches = 0;

	private String compName;
	private String country;

	// test purposes, team table to file
	Path path = Paths.get("C:/Users/Administrator/Desktop/matches.csv");
	List<String> matchesDF = new ArrayList<>();

	private boolean t1tt = false, t1p3 = false, t1p3up = false,
			t1p3down = false;
	private boolean t2tt = false, t2p3 = false, t2p3up = false,
			t2p3down = false;

	public MatchToTableRenewal(int compId2) {
		super();
		// this.matchList = list;
		this.compId = compId2;
		compName = CountryCompetition.ccasList.get(
				CountryCompetition.idToIdx.get(compId)).getCompetition();

		country = CountryCompetition.ccasList.get(
				CountryCompetition.idToIdx.get(compId)).getCountry();

		compName = NameCleaner.replacements(compName);
		country = NameCleaner.replacements(country);
	}

	public MatchToTableRenewal() {
		super();
	}

	public void testPredFileCreate(List<MatchObj> ml, int comp_Id,
			LocalDate date) throws SQLException, IOException {
		/*
		 * get a list of matches to insert in the test prediction file. Use the
		 * country compName and comp id to determine the file that all will be
		 * written. For the tempMatches fill the attributes for the prediction
		 * file. In the afh when we open it we can decide if it is going to be
		 * for writing to TrainDataFile or to TestDataFile.
		 */
		compName = CountryCompetition.ccasList.get( CountryCompetition.idToIdx.get(comp_Id)).getCompetition();
		country = CountryCompetition.ccasList.get( CountryCompetition.idToIdx.get(comp_Id)).getCountry();
		compName = NameCleaner.replacements(compName);
		country = NameCleaner.replacements(country);

		// check if file exists
		if (afh.isTestFile(comp_Id, compName, country, date)) {
			return;
		}

		// check all existence in db and teamtable struct
		// init();
		ctt = new CompetitionTeamTable(compName, country);
		ctt.existsDb();
		if (!ctt.isTable()) {// if there is no table
			return;
		}
		N = ctt.getRowSize();
		if (N == 0) {// if the table has 0 rows
			return;
		}

		int week = ctt.getWeek()+1;
		

		// instanciate the pf class attribute
		try {
			ctt.tableReader();
			afh.openTestOutput(comp_Id, compName, country, date);
			 logger.info("---------------creating testPredFile{} {} {} {} week:{}", comp_Id, compName, country, date,week);

			pf = new PredictionFile();
			afh.appendCsv(pf.csvHeader());
			for (int i = 0; i < ml.size(); i++) {
				mobj = ml.get(i);
				if (testTeamDataPositions()) {
					pf = new PredictionFile();
					predictionFileAttributeAsignment(false);
					pf.setWeek(week);
					pf.setMatchTime(mobj.getMatchTime());
					afh.appendCsv(pf.liner());
				}
				logger.info("Table not initiated correctly ...............................\n ");
				logger.info("could not be found {}",mobj.printMatch());
			}
			afh.closeOutput();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean testTeamDataPositions() throws SQLException {
		/*
		 * gives value to the attributes of the teams (two teams of a
		 * match),needed for the testFile for the prediction operations. (i
		 * think)
		 */
		t1 = null;
		t2 = null;
		posT1 = -1;
		posT2 = -1;
		if (!getTablePosition()) {
			return false;
		}

		if (posT1 >= 0) {
			t1 = ctt.getClassificationPos().get(posT1);
		} else {
			logger.warn("Not found   team {}", mobj.getT1());
			return false;
		}
		if (posT2 >= 0) {
			t2 = ctt.getClassificationPos().get(posT2);
		} else {
			logger.warn("Not found  team {}", mobj.getT2());
			return false;
		}

		return true;
	}

	public void calculate(List<MatchObj> matchesList) throws SQLException,
			IOException {

		/*
		 * calculate the data and change the corresponding values for each of
		 * the two teams in a match. the new data is inserted in the train
		 * prediction file of that competition and also used to change the data
		 * in the DB for every team in the matches
		 */

		t1 = null;
		t2 = null;
		init();// get the db table ready
		if (N != 0) {
			BasicTableEntity tempT = ctt.getClassificationPos().get(0);
			int all1 = tempT.getMatchesIn() + tempT.getMatchesOut();
			tempT = ctt.getClassificationPos().get(4);
			int all2 = tempT.getMatchesIn() + tempT.getMatchesOut();
			totMatches = Math.max(all1, all2) * N / 2;
			tempT = null;
		}

		pf = new PredictionFile();
		AnalyticFileHandler afh = new AnalyticFileHandler();
		File f = afh.getTrainFileName(compId, compName, country);
		if (f == null) {
			// if file doesn't exists add a csv headder
			matchesDF.add(pf.csvHeader());
		}

		// if (!ctt.isTable()) {
		// matchesDF.add(pf.csvHeader());
		// }

		for (int i = matchesList.size() - 1; i >= 0; i--) {
			mobj = matchesList.get(i);
			posT1 = -1;
			posT2 = -1;
			getTablePosition();
			if (posT1 >= 0) {
				// if found on the classification table read them
				t1 = ctt.getClassificationPos().get(posT1);
			} else {
				// make a new one
				t1 = new BasicTableEntity();
				t1.setTeam(mobj.getT1());
			}
			if (posT2 >= 0) {
				t2 = ctt.getClassificationPos().get(posT2);
			} else {
				t2 = new BasicTableEntity();
				t2.setTeam(mobj.getT2());
			}

			// logger.info("{}", mobj.printMatch());

			if (t1.getMatchesIn() + t1.getMatchesOut() > 3
					|| t2.getMatchesIn() + t2.getMatchesOut() > 3) {

				// ********Execute all prediction file asignments*********
				/*
				 * add prediction file data to a list & in the end append the
				 * list of string in the file
				 */
				pf = new PredictionFile();
				predictionFileAttributeAsignment(true);// set up pred file data
				// nr of matches per nr of games (half of the teams)
				pf.setWeek((totMatches / (N / 2)) + 1);
				// afh.appendCsv(pf.liner());// put on file
				matchesDF.add(pf.liner());
				// -----------------------------------------

				// if teams matches > 3 then start calculating group attributes
				evalUpdateGroups();
				/*
				 * if mobj. has (ft1 || ft2)= null => next match do not
				 * "renew()" classification table. use this part of calculation
				 * method only to get new files for predict outcomes
				 */
				renew(true);
			} else {
				renew(false);
			}

			if (posT1 >= 0) {// if team exists replace with updated version
				ctt.getClassificationPos().remove(posT1);
				ctt.getClassificationPos().add(posT1, t1);
			} else {
				ctt.getClassificationPos().add(t1);
			}
			if (posT2 >= 0) {
				ctt.getClassificationPos().remove(posT2);
				ctt.getClassificationPos().add(posT2, t2);
			} else {
				ctt.getClassificationPos().add(t2);
			}

			totMatches++;
			match_counter++;
			if (match_counter >= 4) {
				// for every 4 matcher reorder teams in classification table
				match_counter = 0;
				orderClassificationTable();
			}
		}

		// at the end, after all the calcu;ations for all the matches in the
		// list
		orderClassificationTable();
		if (ctt.isTable()) {
			if (ctt.getRowSize() >= 1) {
				logger.info(
						"*******************************classification size ={}",
						ctt.getClassificationPos().size());
				ctt.updateTable();
			}
		} else {
			ctt.insertTable();
		}
		// write prediction data to file
		if (matchesDF.size() > 0) {
			// for less than 4 weeks do not write results in train file
			try {
				afh.openTrainOutput(compId, compName, country);
				for (String line : matchesDF) {
					afh.appendCsv(line);
				}
				afh.closeOutput();
			} catch (Exception e) {
				logger.warn("Something whent wrong with the train file creation");
			}
		}

	}

	public void init() throws SQLException {
		/*
		 * in int we check if the db table exists and then we read it else we
		 * create it. In any case we set a teamtable (classification)for a
		 * specific competition ready
		 */

		ctt = new CompetitionTeamTable(compName, country);

		ctt.existsDb();
		if (ctt.isTable()) {
			logger.info("----- {}, {}  IT IS TABLE!!!   size {}", compName,
					country, ctt.getRowSize());
			if (ctt.getRowSize() >= 1)
				ctt.tableReader();
			// ctt.testPrint();
			N = ctt.getClassificationPos().size();
		} else {
			ctt.createFullTable();
			N = 0;
		}
	}

	private void renew(boolean flag4matches) {
		/* teamtable data */

		t2.addMatchesOut();
		t2.addHtConcededOut(mobj.getHt1());
		t2.addHtScoreOut(mobj.getHt2());
		t2.addFtConcededOut(mobj.getFt1());
		t2.addFtScoreOut(mobj.getFt2());

		t1.addMatchesIn();
		t1.addHtConcededIn(mobj.getHt2());
		t1.addHtScoreIn(mobj.getHt1());
		t1.addFtConcededIn(mobj.getFt2());
		t1.addFtScoreIn(mobj.getFt1());

		// TOP TABLE
		if (t1tt) {
			t1.addTtMatchesIn();
			t1.addTtHtConcededIn(mobj.getHt2());
			t1.addTtHtScoreIn(mobj.getHt1());
			t1.addTtFtConcededIn(mobj.getFt2());
			t1.addTtFtScoreIn(mobj.getFt1());
		}
		if (t2tt) {
			t2.addTtMatchesOut();
			t2.addTtHtConcededOut(mobj.getHt1());
			t2.addTtHtScoreOut(mobj.getHt2());
			t2.addTtFtConcededOut(mobj.getFt1());
			t2.addTtFtScoreOut(mobj.getFt2());
		}
		// 3 POSITION NEAR
		if (t1p3) {
			t1.addP3MatchesIn();
			t1.addP3HtConcededIn(mobj.getHt2());
			t1.addP3HtScoreIn(mobj.getHt1());
			t1.addP3FtConcededIn(mobj.getFt2());
			t1.addP3FtScoreIn(mobj.getFt1());
		}
		if (t2p3) {
			t2.addP3MatchesOut();
			t2.addP3HtConcededOut(mobj.getHt1());
			t2.addP3HtScoreOut(mobj.getHt2());
			t2.addP3FtConcededOut(mobj.getFt1());
			t2.addP3FtScoreOut(mobj.getFt2());
		}
		// ABOVE MORE THAN 3 POSITIONS
		if (t1p3up) {
			t1.addP3UpMatchesIn();
			t1.addP3UpHtConcededIn(mobj.getHt2());
			t1.addP3UpHtScoreIn(mobj.getHt1());
			t1.addP3UpFtConcededIn(mobj.getFt2());
			t1.addP3UpFtScoreIn(mobj.getFt1());
		}
		if (t2p3up) {
			t2.addP3UpMatchesOut();
			t2.addP3UpHtConcededOut(mobj.getHt1());
			t2.addP3UpHtScoreOut(mobj.getHt2());
			t2.addP3UpFtConcededOut(mobj.getFt1());
			t2.addP3UpFtScoreOut(mobj.getFt2());
		}
		// BELOW MORE THAN 3 POSITIONS
		if (t1p3up) {
			t1.addP3DownMatchesIn();
			t1.addP3DownHtConcededIn(mobj.getHt2());
			t1.addP3DownHtScoreIn(mobj.getHt1());
			t1.addP3DownFtConcededIn(mobj.getFt2());
			t1.addP3DownFtScoreIn(mobj.getFt1());
		}
		if (t2p3up) {
			t2.addP3DownMatchesOut();
			t2.addP3DownHtConcededOut(mobj.getHt1());
			t2.addP3DownHtScoreOut(mobj.getHt2());
			t2.addP3DownFtConcededOut(mobj.getFt1());
			t2.addP3DownFtScoreOut(mobj.getFt2());
		}

		// calc outcome streams of(win, draw, lose)
		continuance();

		// CALCULATE FORM
		formCalc(flag4matches);
		// points are added in the end, after form is calculated
		if (mobj.getFt1() > mobj.getFt2()) {
			t1.addPoints(3);
			t1.addWinsIn();
			t2.addLosesOut();
		} else if (mobj.getFt1() < mobj.getFt2()) {
			t2.addPoints(3);
			t2.addWinsOut();
			t1.addLosesIn();
		} else {
			t1.addPoints(1);
			t2.addPoints(1);
			t1.addDrawsIn();
			t2.addDrawsOut();
		}

		if (mobj.getFt1() >= 1 && mobj.getFt2() >= 1) {
			t1.addFtGg();
			t2.addFtGg();
		}
		if (mobj.getHt1() >= 1 && mobj.getHt2() >= 1) {
			t1.addHtGg();
			t2.addHtGg();
		}

	}

	private void continuance() {
		/*
		 * teamtable data. Evaluate the continuance of a teams results. how long
		 * does it holds a winnig, drawing or loosing stream.
		 */
		float avgVal = 0;
		if (t1.getFtScoreIn() > t1.getFtConcededIn()) {
			// if home win and preciously home win
			if (t1.getPrevRes() == TeamStatus.WIN)
				t1.addContinuanceCounter();
			else {
				avgVal = (t1.getAvgWinCont() * t1.getWinStreams() + t1 .getContResCounter()) / (t1.getWinStreams() + 1);
				t1.setAvgWinCont(avgVal);
				t1.resetContinuanceCounter();
				t1.addWinStreams();
			}
			// curently awayteam lose -> update lose atts
			if (t2.getPrevRes() == TeamStatus.LOSE) {
				t2.addContinuanceCounter();
			} else {
				avgVal = (t2.getAvgLoseCont() * t2.getLoseStreams() + t2 .getContResCounter()) / (t2.getLoseStreams() + 1);
				t2.setAvgLoseCont(avgVal);
				t2.resetContinuanceCounter();
				t2.addLoseStreams();
			}
			t1.setPrevRes(TeamStatus.WIN);
			t2.setPrevRes(TeamStatus.LOSE);

		} else if (t1.getFtScoreIn() < t1.getFtConcededIn()) {
			// home lose && away win
			if (t1.getPrevRes() == TeamStatus.LOSE)
				t1.addContinuanceCounter();
			else {
				avgVal = (t1.getAvgLoseCont() * t1.getLoseStreams() + t1 .getContResCounter()) / (t1.getLoseStreams() + 1);
				t1.setAvgLoseCont(avgVal);
				t1.resetContinuanceCounter();
				t1.addLoseStreams();
			}

			// curently awayteam win -> update win atts
			if (t2.getPrevRes() == TeamStatus.WIN) {
				t2.addContinuanceCounter();
			} else {
				avgVal = (t2.getAvgWinCont() * t2.getWinStreams() + t2 .getContResCounter()) / (t2.getWinStreams() + 1);
				t2.setAvgLoseCont(avgVal);
				t2.resetContinuanceCounter();
				t2.addWinStreams();
			}
			t2.setPrevRes(TeamStatus.WIN);
			t1.setPrevRes(TeamStatus.LOSE);
		} else {// draw
			if (t1.getPrevRes() == TeamStatus.DRAW)
				t1.addContinuanceCounter();
			else {
				avgVal = (t1.getAvgDrawCont() * t1.getDrawStreams() + t1 .getContResCounter()) / (t1.getDrawStreams() + 1);
				t1.setAvgDrawCont(avgVal);
				t1.resetContinuanceCounter();
				t1.addDrawStreams();
			}

			if (t2.getPrevRes() == TeamStatus.DRAW) {
				t2.addContinuanceCounter();
			} else {
				avgVal = (t2.getAvgDrawCont() * t2.getDrawStreams() + t2 .getContResCounter()) / (t2.getDrawStreams() + 1);
				t2.setAvgDrawCont(avgVal);
				t2.resetContinuanceCounter();
				t2.addDrawStreams();
			}
			t2.setPrevRes(TeamStatus.DRAW);
			t1.setPrevRes(TeamStatus.DRAW);
		}

	}

	private void formCalc(boolean posPoint) {

		float t1Form = 0, t2Form = 0;
		float t1Atack, t1Defence;
		float t2Atack, t2Defence;

		// standard points
		if (mobj.getFt1() > mobj.getFt2()) {
			t1Form = 2;
			t2Form = -1;
		} else if (mobj.getFt1() < mobj.getFt2()) {
			t1Form = -1;
			t2Form = 2;
		} else {
			t1Form = 1;
			t2Form = 1;
		}
		// score points
		if ((mobj.getFt1() + mobj.getFt2()) == 0) { // avoid division/Zero
			t1Atack = 0;
			t1Defence = 0;
			t2Atack = 0;
			t2Defence = 0;
		} else {
			t1Atack = (float) mobj.getFt1() / (mobj.getFt1() + mobj.getFt2());
			t1Defence = -(float) mobj.getFt2() / (mobj.getFt1() + mobj.getFt2());
			t2Atack = (float) mobj.getFt2() / (mobj.getFt1() + mobj.getFt2());
			t2Defence = -(float) mobj.getFt1() / (mobj.getFt1() + mobj.getFt2());
		}

		// classification points
		if (posPoint) { // if teams points are more than 3
			if (mobj.getFt1() > mobj.getFt2()) {
				if (t1.getPoints() == 0) {
					t1Form += (float) 1 / 2;
					t2Form -= (float) 1 / 2;
				} else {
					t1Form += (float) Math.abs(t1.getPoints() - t2.getPoints()) / t1.getPoints();
					t2Form -= (float) Math.abs(t1.getPoints() - t2.getPoints()) / t1.getPoints();
				}
			} else if (mobj.getFt1() < mobj.getFt2()) {
				if (t2.getPoints() == 0) {
					t1Form -= (float) 1 / 2;
					t2Form += (float) 1 / 2;
				} else {
					t1Form -= (float) Math.abs(t1.getPoints() - t2.getPoints()) / t2.getPoints();
					t2Form += (float) Math.abs(t1.getPoints() - t2.getPoints()) / t2.getPoints();
				}
			} else {// in case of draw
				if (t1.getPoints() > t2.getPoints()) {
					if (t2.getPoints() == 0) {
						t1Form -= (float) 1 / 4;
						t2Form += (float) 1 / 4;
					} else {
						t1Form -= (float) Math.abs(t1.getPoints() - t2.getPoints()) / t2.getPoints();
						t2Form += (float) Math.abs(t1.getPoints() - t2.getPoints()) / t2.getPoints();
					}
				} else if (t1.getPoints() < t2.getPoints()) {
					if (t1.getPoints() == 0) {
						t1Form += (float) 1 / 4;
						t2Form -= (float) 1 / 4;
					} else {
						t1Form += (float) Math.abs(t1.getPoints() - t2.getPoints()) / t1.getPoints();
						t2Form -= (float) Math.abs(t1.getPoints() - t2.getPoints()) / t1.getPoints();
					}
				}
			}
		}
		t1Form = t1Form + t1Atack + t1Defence;
		t2Form = t2Form + t2Atack + t2Defence;

		t1.setForm4(t1.getForm3());
		t1.setForm3(t1.getForm2());
		t1.setForm2(t1.getForm1());
		t1.setForm1(t1.getForm());
		t1.addForm(t1Form);
		t1.addFormAtack(t1Atack);
		t1.addFormAtackIn(t1Atack);
		t1.addFormDefence(t1Defence);
		t1.addFormDefenceIn(t1Defence);

		t2.setForm4(t2.getForm3());
		t2.setForm3(t2.getForm2());
		t2.setForm2(t2.getForm1());
		t2.setForm1(t2.getForm());
		t2.addForm(t2Form);
		t2.addFormAtack(t2Atack);
		t2.addFormAtackOut(t2Atack);
		t2.addFormDefence(t2Defence);
		t2.addFormDefenceOut(t2Defence);

	}

	private boolean getTablePosition() {
		// get the classification position on the team table for the two teams
		// in hand
		boolean flag = false;
		// N = ctt.getClassificationPos().size();
		for (int i = 0; i < ctt.getClassificationPos().size(); i++) {
			if (mobj.getT1()
					.equals(ctt.getClassificationPos().get(i).getTeam())) {
				posT1 = i;
				flag = true;
			} else if (mobj.getT2().equals(
					ctt.getClassificationPos().get(i).getTeam())) {
				posT2 = i;
				flag = true;
			}
		}

		return flag;
	}

	private void evalUpdateGroups() {

		/*
		 * depending on the teams position on the classification table we can
		 * determine the different groups of attributes to calculate and updates
		 * to execute in the db ****************************************
		 * Furthermore we calculate some of the elements for the prediction file
		 */

		if (N <= 12) {// less than 12 teams
			if (posT1 <= Math.floor(N / 3)) {
				t2tt = true;
			}
			if (posT2 <= Math.floor(N / 3)) {
				t1tt = true;
			}
			// ------------prediction file elements

		} else {// more than 12 teams in table
			if (posT1 <= Math.ceil(N / 4)) {
				t2tt = true;
			}
			if (posT2 <= Math.ceil(N / 4)) {
				t1tt = true;
			}

		}

		if (Math.abs(posT1 - posT2) <= 3) {// 3 pos near each other
			t1p3 = true;
			t2p3 = true;
		} else {
			if (posT1 > posT2) {
				t1p3down = true;
				t2p3up = true;
			} else {
				t2p3down = true;
				t1p3up = true;
			}
		}
	}

	public void setMatch(MatchObj mobj) {
		this.mobj = mobj;
	}

	public MatchObj getMatch() {
		return mobj;
	}

	public void fileIt() {
		String line = mobj.printMatch() + t1.line() + t2.line();
		logger.info(
				" t1= {}   ht1 : {}     ht2 : {} t2={}     HtconcedeIn1={}    htConcedeOut1={}  HtconcedeIn2={}    htConcedeOut2={} ",
				mobj.getT1(), mobj.getHt1(), mobj.getHt2(), mobj.getT2(),
				t1.getHtConcededIn(), t1.getHtConcededOut(),
				t2.getHtConcededIn(), t2.getHtConcededOut());
		matchesDF.add(line);
	}

	public void storeIt() {
		try {
			Files.write(path, matchesDF);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Could not Save To FIle");
		}
	}

	private void predictionFileAttributeAsignment(boolean outcomes) {
		/*
		 * it sets the data for the each team in the PREDICTION file attributes
		 * for the train or test prediction file
		 */

		// outcomes is used to differentiate between train and test data
		// pf.setWeek(totMatches);
		BasicTableEntity Elem = ctt.getClassificationPos().get(posT1);
		pf.setT1(Elem.getTeam());
		pf.setT1Points(Elem.getPoints());
		pf.setT1Form(Elem.getForm());
		pf.setT1Form1Diff(Elem.getForm() - Elem.getForm1());
		pf.setT1Form2Diff(Elem.getForm1() - Elem.getForm2());
		pf.setT1Form3Diff(Elem.getForm2() - Elem.getForm3());
		pf.setT1Form4Diff(Elem.getForm3() - Elem.getForm4());
		pf.setT1Atack(Elem.getFormAtack());
		pf.setT1AtackIn(Elem.getFormAtackIn());
		pf.setT1AtackOut(Elem.getFormAtackOut());
		pf.setT1Defense(Elem.getFormDefence());
		pf.setT1DefenseIn(Elem.getFormDefenceIn());
		pf.setT1DefenseOut(Elem.getFormDefenceOut());

		float avg;
		if (Elem.getMatchesIn() > 0) {
			avg = (Elem.getHtScoreIn() + Elem.getHtConcededIn()) / Elem.getMatchesIn();
			pf.setT1AvgHtScoreIn(avg);
			avg = (Elem.getFtScoreIn() + Elem.getFtConcededIn()) / Elem.getMatchesIn();
			pf.setT1AvgFtScoreIn(avg);
		}
		if (Elem.getMatchesOut() > 0) {
			avg = (Elem.getHtScoreOut() + Elem.getHtConcededOut()) / Elem.getMatchesOut();
			pf.setT1AvgHtScoreOut(avg);
			avg = (Elem.getFtScoreOut() + Elem.getFtConcededOut()) / Elem.getMatchesOut();
			pf.setT1AvgFtScoreOut(avg);
		}
		if (Elem.getMatchesIn() + Elem.getMatchesOut() > 0) {
			pf.setT1AvgFtGgResult(Elem.getFtGg() / (Elem.getMatchesIn() + Elem.getMatchesOut()));
			pf.setT1AvgHtGgResult(Elem.getHtGg() / (Elem.getMatchesIn() + Elem.getMatchesOut()));
		}

		pf.setT1WinsIn(Elem.getWinsIn());
		pf.setT1WinsOut(Elem.getWinsOut());
		pf.setT1DrawsIn(Elem.getDrawsIn());
		pf.setT1DrawsOut(Elem.getDrawsOut());
		pf.setT1LosesIn(Elem.getLosesIn());
		pf.setT1LosesOut(Elem.getLosesOut());

		Elem = ctt.getClassificationPos().get(posT2);
		pf.setT2(Elem.getTeam());
		pf.setT2Points(Elem.getPoints());
		pf.setT2Form(Elem.getForm());
		pf.setT2Form1Diff(Elem.getForm() - Elem.getForm1());
		pf.setT2Form2Diff(Elem.getForm1() - Elem.getForm2());
		pf.setT2Form3Diff(Elem.getForm2() - Elem.getForm3());
		pf.setT2Form4Diff(Elem.getForm3() - Elem.getForm4());
		pf.setT2Atack(Elem.getFormAtack());
		pf.setT2AtackIn(Elem.getFormAtackIn());
		pf.setT2AtackOut(Elem.getFormAtackOut());
		pf.setT2Defense(Elem.getFormDefence());
		pf.setT2DefenseIn(Elem.getFormDefenceIn());
		pf.setT2DefenseOut(Elem.getFormDefenceOut());

		if (Elem.getMatchesIn() > 0) {
			avg = (Elem.getHtScoreIn() + Elem.getHtConcededIn()) / Elem.getMatchesIn();
			pf.setT2AvgHtScoreIn(avg);
			avg = (Elem.getFtScoreIn() + Elem.getFtConcededIn()) / Elem.getMatchesIn();
			pf.setT2AvgFtScoreIn(avg);
		}
		if (Elem.getMatchesOut() > 0) {
			avg = (Elem.getHtScoreOut() + Elem.getHtConcededOut()) / Elem.getMatchesOut();
			pf.setT2AvgHtScoreOut(avg);
			avg = (Elem.getFtScoreOut() + Elem.getFtConcededOut()) / Elem.getMatchesOut();
			pf.setT2AvgFtScoreOut(avg);
		}
		if (Elem.getMatchesIn() + Elem.getMatchesOut() > 0) {
			pf.setT2AvgFtGgResult(Elem.getFtGg() / (Elem.getMatchesIn() + Elem.getMatchesOut()));
			pf.setT2AvgHtGgResult(Elem.getHtGg() / (Elem.getMatchesIn() + Elem.getMatchesOut()));
		}

		pf.setT2WinsIn(Elem.getWinsIn());
		pf.setT2WinsOut(Elem.getWinsOut());
		pf.setT2DrawsIn(Elem.getDrawsIn());
		pf.setT2DrawsOut(Elem.getDrawsOut());
		pf.setT2LosesIn(Elem.getLosesIn());
		pf.setT2LosesOut(Elem.getLosesOut());

		classificationGroupsAsignment();// group position

		pf.setBet_1((float) mobj.get_1());
		pf.setBet_X((float) mobj.get_x());
		pf.setBet_2((float) mobj.get_2());
		pf.setBet_O((float) mobj.get_o());
		pf.setBet_U((float) mobj.get_u());

		if (outcomes) {
			outcomeAsignment();
		}
	}

	private void outcomeAsignment() {
		if (mobj != null) {

			pf.setMatchTime(MatchOutcome.missing);
			pf.setT1Ht(mobj.getHt1());
			pf.setT2Ht(mobj.getHt2());
			pf.setT1Ft(mobj.getFt1());
			pf.setT2Ft(mobj.getFt2());

			// head outcome 1X2
			if (mobj.getFt1() > mobj.getFt2()) {
				pf.setHeadOutcome(MatchOutcome.home);
			} else if (mobj.getFt1() < mobj.getFt2()) {
				pf.setHeadOutcome(MatchOutcome.away);
			} else {
				pf.setHeadOutcome(MatchOutcome.deaw);
			}

			// over under outcome
			if (mobj.getFt1() + mobj.getFt2() >= 3) {
				pf.setScoreOutcome(MatchOutcome.over);
			} else {
				pf.setScoreOutcome(MatchOutcome.under);
			}

			// tot score
			pf.setTotHtScore(mobj.getHt1() + mobj.getHt2());
			pf.setTotFtScore(mobj.getFt1() + mobj.getFt2());

			// Goal - Goal
			if (mobj.getFt1() >= 1 && mobj.getFt2() >= 1) {
				pf.setGgOutcome(MatchOutcome.yes);
			} else {
				pf.setGgOutcome(MatchOutcome.no);
			}

			try {// half time
				if (mobj.getHt1() + mobj.getHt2() >= 2) {
					pf.setHt1pOutcome(MatchOutcome.yes);
					pf.setHt2pOutcome(MatchOutcome.yes);
				} else if (mobj.getHt1() + mobj.getHt2() < 2
						&& mobj.getHt1() + mobj.getHt2() >= 1) {
					pf.setHt1pOutcome(MatchOutcome.yes);
					pf.setHt2pOutcome(MatchOutcome.no);
				} else {
					pf.setHt1pOutcome(MatchOutcome.no);
					pf.setHt2pOutcome(MatchOutcome.no);
				}
			} catch (Exception e) {
				pf.setHt1pOutcome(MatchOutcome.missing);
				pf.setHt2pOutcome(MatchOutcome.missing);
			}
		} else {
			// it should come here for the test pred file
			pf.setHt1pOutcome(MatchOutcome.missing);
			pf.setHt2pOutcome(MatchOutcome.missing);
			pf.setGgOutcome(MatchOutcome.missing);
			pf.setScoreOutcome(MatchOutcome.missing);
			pf.setHeadOutcome(MatchOutcome.missing);
		}
	}

	private void classificationGroupsAsignment() {
		if (N <= 12) {// less than 12 teams
			// ------------prediction file elements
			if (posT1 <= N / 3) {
				t2tt = true; // statistical data element *top of the table
				pf.setT1Classification(ClassifiStatus.ttable);
			} else if (posT1 > N / 3 && posT1 <= N / 2) {
				pf.setT1Classification(ClassifiStatus.mup);
			} else if (posT1 > N / 2 && posT1 <= N - 3) {
				pf.setT1Classification(ClassifiStatus.mdown);
			} else {
				pf.setT1Classification(ClassifiStatus.btable);
			}
			if (posT2 <= N / 3) {
				t1tt = true;// statistical data element
				pf.setT2Classification(ClassifiStatus.ttable);
			} else if (posT2 > N / 3 && posT2 <= N / 2) {
				pf.setT2Classification(ClassifiStatus.mup);
			} else if (posT2 > N / 2 && posT2 <= N - 3) {
				pf.setT2Classification(ClassifiStatus.mdown);
			} else {
				pf.setT2Classification(ClassifiStatus.btable);
			}

		} else {// more than 12 teams in table
			if (posT1 <= N / 4) {
				// t2tt = true;// statistical data element
				pf.setT1Classification(ClassifiStatus.ttable);
			} else if (posT1 > N / 4 && posT1 <= N / 2) {
				pf.setT1Classification(ClassifiStatus.mup);
			} else if (posT1 > N / 2 && posT1 <= 3 * N / 4) {
				pf.setT1Classification(ClassifiStatus.mdown);
			} else {
				pf.setT1Classification(ClassifiStatus.btable);
			}
			if (posT2 <= N / 4) {
				// t1tt = true;// statistical data element *top of the table
				pf.setT2Classification(ClassifiStatus.ttable);
			} else if (posT2 > N / 4 && posT2 <= N / 2) {
				pf.setT2Classification(ClassifiStatus.mup);
			} else if (posT2 > N / 2 && posT2 <= 3 * N / 4) {
				pf.setT2Classification(ClassifiStatus.mdown);
			} else {
				pf.setT2Classification(ClassifiStatus.btable);
			}
		}

	}

	public void orderClassificationTable() {
		ctt.orderClassificationTable();
	}

	public int getCompId() {
		return compId;
	}

	public String getCompName() {
		return compName;
	}

	public String getCountry() {
		return country;
	}

	public void setCompId(int compId) {
		this.compId = compId;
	}

	public void setCompName(String compName) {
		this.compName = NameCleaner.replacements(compName);
	}

	public void setCountry(String country) {
		this.country = NameCleaner.replacements(country);
	}

}
