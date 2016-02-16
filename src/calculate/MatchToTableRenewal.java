package calculate;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.LoggerFactory;

import com.sun.media.jfxmedia.logging.Logger;

import structures.CompetitionTeamTable;
import structures.CountryCompetition;
import basicStruct.ClasifficationStruct;
import basicStruct.MatchObj;
import dbhandler.BasicTableEntity;
import dbhandler.FullTableMaker;
import dbhandler.TableMaker;

/**
 * @author Administrator
 *
 *         in this class we should get the competition table of a particular
 *         competition from the db, get the fields of the teams that are playing
 *         in a Match. The tablemaker classes for each team are recalculated
 *         taking in consideration the new results and are stored back in the db
 */
public class MatchToTableRenewal {

	public static final org.slf4j.Logger logger = LoggerFactory
			.getLogger(MatchToTableRenewal.class);

	private List<MatchObj> matchList;
	private int compId;
	private MatchObj mobj;
	private CompetitionTeamTable ctt;
	private BasicTableEntity t1, t2;
	private int N;
	private int posT1 = -1;
	private int posT2 = -1;
	private int match_counter = 0;

	Path path = Paths.get("C:/Users/Administrator/Desktop/matches.csv");
	List<String> matchesDF = new ArrayList<>();

	// vars to know which sets of db update to execute
	private boolean t1tt = false, t1p3 = false, t1p3up = false,
			t1p3down = false;
	private boolean t2tt = false, t2p3 = false, t2p3up = false,
			t2p3down = false;

	public MatchToTableRenewal(List<MatchObj> list, int compId2) {
		super();
		this.matchList = list;
		this.compId = compId2;
	}

	public MatchToTableRenewal() {
		super();
	}

	public void calculate() throws SQLException {
		t1 = null;
		t2 = null;

		init();// get the db table ready
		for (int i = 0; i < matchList.size(); i++) {
			mobj = matchList.get(i);
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

			if (t1.getMatchesIn() + t1.getMatchesOut() > 3
					|| t2.getMatchesIn() + t2.getMatchesOut() > 3) {
				fileIt();// write match to file

				// if teams matches > 3 then start calculating group attributes
				evalUpdateGroups();
				renew(true);
			} else {
				renew(false);
			}
			// -------------------------------------------
			// TODO test
			logger.info("t1 ={}    htscoreout={}  htconcedein={}",
					t1.getTeam(), t1.getHtScoreOut(), t1.getHtConcededIn());
			logger.info("t2 ={}    htscoreout={}  htconcedein={}",
					t2.getTeam(), t2.getHtScoreOut(), t2.getHtConcededIn());
			// -------------------------------------------

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

			// ctt.testPrint();
			// ctt.getClassificationPos().add(t2);
			match_counter++;
			if (match_counter >= 4) {
				// for every 4 matcher reorder teams in classification table
				match_counter = 0;
				ctt.orderClassificationTable();
			}
		}
		// at the end
		if (ctt.isTable()) {
			logger.info(
					"*******************************classification size ={}",
					ctt.getClassificationPos().size());
			ctt.updateTable();
		} else {
			ctt.insertTable();
		}

		mobj = new MatchObj();
		for (BasicTableEntity t : ctt.getClassificationPos()) {
			fileIt();
		}
		storeIt();
	}

	public void init() throws SQLException {
		/*
		 * in nit we check if the db table exists and then we read it else we
		 * create it. In any case we set a teamtable for a specific competition
		 * ready
		 */

		String compName = CountryCompetition.compList.get(compId - 1)
				.getCompetition();
		compName = compName.replaceAll(" ", "_");
		ctt = new CompetitionTeamTable(compName);

		ctt.existsDb();
		if (ctt.isTable()) {
			logger.info("----- IT IS TABLE!!!");
			ctt.tableReader();
			ctt.testPrint();
			N = ctt.getClassificationPos().size();
		} else {
			ctt.createFullTable();
			N = 0;
		}
	}

	private void renew(boolean flag4matches) {
		// calculate and change the appropriate values for the second team

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

		// CALCULATE FORM
		formCalc(flag4matches);
		// points are added in the end, after form is calculated
		if (mobj.getFt1() > mobj.getFt2()) {
			t1.addPoints(3);
		} else if (mobj.getFt1() < mobj.getFt2()) {
			t2.addPoints(3);
		} else {
			t1.addPoints(1);
			t2.addPoints(1);
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
		if ((mobj.getFt1() + mobj.getFt2()) == 0) {
			t1Atack = 0;
			t1Defence = 0;
			t2Atack = 0;
			t2Defence = 0;
		} else {
			t1Atack = (float) mobj.getFt1() / (mobj.getFt1() + mobj.getFt2());
			t1Defence = -(float) mobj.getFt2()
					/ (mobj.getFt1() + mobj.getFt2());
			t2Atack = (float) mobj.getFt2() / (mobj.getFt1() + mobj.getFt2());
			t2Defence = -(float) mobj.getFt1()
					/ (mobj.getFt1() + mobj.getFt2());
		}
		// classification points
		if (posPoint) { // if teams points are more than 3
			if (mobj.getFt1() > mobj.getFt2()) {
				if (t1.getPoints() == 0) {
					t1Form += (float) 1 / 2;
					t2Form -= (float) 1 / 2;
				} else {
					t1Form += (float) Math.abs(t1.getPoints() - t2.getPoints())
							/ t1.getPoints();
					t2Form -= (float) Math.abs(t1.getPoints() - t2.getPoints())
							/ t1.getPoints();
				}
			} else if (mobj.getFt1() < mobj.getFt2()) {
				if (t2.getPoints() == 0) {
					t1Form -= (float) 1 / 2;
					t2Form += (float) 1 / 2;
				} else {
					t1Form -= (float) Math.abs(t1.getPoints() - t2.getPoints())
							/ t2.getPoints();
					t2Form += (float) Math.abs(t1.getPoints() - t2.getPoints())
							/ t2.getPoints();
				}
			} else {// in case of draw
				if (t1.getPoints() > t2.getPoints()) {
					if (t2.getPoints() == 0) {
						t1Form -= (float) 1 / 4;
						t2Form += (float) 1 / 4;
					} else {
						t1Form -= (float) Math.abs(t1.getPoints()
								- t2.getPoints())
								/ t2.getPoints();
						t2Form += (float) Math.abs(t1.getPoints()
								- t2.getPoints())
								/ t2.getPoints();
					}
				} else if (t1.getPoints() < t2.getPoints()) {
					if (t1.getPoints() == 0) {
						t1Form += (float) 1 / 4;
						t2Form -= (float) 1 / 4;
					} else {
						t1Form += (float) Math.abs(t1.getPoints()
								- t2.getPoints())
								/ t1.getPoints();
						t2Form -= (float) Math.abs(t1.getPoints()
								- t2.getPoints())
								/ t1.getPoints();
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
		t1.addFormDefence(t1Defence);

		t2.setForm4(t2.getForm3());
		t2.setForm3(t2.getForm2());
		t2.setForm2(t2.getForm1());
		t2.setForm1(t2.getForm());
		t2.addForm(t2Form);
		t2.addFormAtack(t2Atack);
		t2.addFormDefence(t2Defence);

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
		 * to execute in the db
		 */
		if (N <= 12) {// less than 12 teams
			if (posT1 <= Math.floor(N / 3)) {
				t2tt = true;
			}
			if (posT2 <= Math.floor(N / 3)) {
				t1tt = true;
			}
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

}