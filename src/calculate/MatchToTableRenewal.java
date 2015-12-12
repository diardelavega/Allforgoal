package calculate;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

	private List<MatchObj> matchList;
	private int compId;
	private MatchObj mobj;
	private CompetitionTeamTable ctt = new CompetitionTeamTable();
	private BasicTableEntity t1, t2;
	private int N;
	private int posT1 = 0;
	private int posT2 = 0;
	private int match_counter = 0;

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
		// TODO init teamtable structures
		init();
		// MatchObj mobj;
		if (N == 0) {// table didn't exist
			for (int i = 0; i < matchList.size(); i++) {
				mobj = matchList.get(i);
//				if()
				BasicTableEntity t1 = new BasicTableEntity();
				BasicTableEntity t2 = new BasicTableEntity();
				t1.setTeam(mobj.getT1());
				t1.setTeam(mobj.getT2());
				renew(t1, t2);
				ctt.getClassificationPos().add(t1);
				ctt.getClassificationPos().add(t2);
				match_counter++;
			}
		} else {// N>0 -> table exists and has data

		}
	}

	public void init() throws SQLException {
		/*
		 * in nit we check if the db table exists and then we read it else we
		 * create it. In any case we set a teamtable for a specific competition
		 * ready
		 */
		String compName = CountryCompetition.compList.get(compId - 1)
				.getCompetition();
		ctt.existsDb(compName);
		if (ctt.isTable()) {
			ctt.tableReader(compName, mobj.getT1(), mobj.getT2());
			N = ctt.getClassificationPos().size();
		} else {
			ctt.createFullTable(compName);
			N = 0;
		}
	}

	public void recalculate() {
		/*
		 * get the team table object. Check if it is full or half table, also
		 * look and get the field of the first and second team to elaborate
		 */
		renew(t1, t2);
	}

	private void renew(BasicTableEntity t12, BasicTableEntity t22) {
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
		// formCalc();
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
			t1Atack = mobj.getFt1() / (mobj.getFt1() + mobj.getFt2());
			t1Defence = -mobj.getFt2() / (mobj.getFt1() + mobj.getFt2());
			t2Atack = mobj.getFt2() / (mobj.getFt1() + mobj.getFt2());
			t2Defence = -mobj.getFt1() / (mobj.getFt1() + mobj.getFt2());
		}
		// classification points
		if (posPoint) { // if teams points are more than 3
			if (mobj.getFt1() > mobj.getFt2()) {
				if (t1.getPoints() == 0) {
					t1Form += 1 / 2;
					t2Form -= 1 / 2;
				} else {
					t1Form += Math.abs(t1.getPoints() - t2.getPoints())
							/ t1.getPoints();
					t2Form -= Math.abs(t1.getPoints() - t2.getPoints())
							/ t1.getPoints();
				}
			} else if (mobj.getFt1() < mobj.getFt2()) {
				if (t2.getPoints() == 0) {
					t1Form -= 1 / 2;
					t2Form += 1 / 2;
				} else {
					t1Form -= Math.abs(t1.getPoints() - t2.getPoints())
							/ t2.getPoints();
					t2Form += Math.abs(t1.getPoints() - t2.getPoints())
							/ t2.getPoints();
				}
			} else {// in case of draw
				if (t1.getPoints() > t2.getPoints()) {
					if (t2.getPoints() == 0) {
						t1Form -= 1 / 4;
						t2Form += 1 / 4;
					} else {
						t1Form -= Math.abs(t1.getPoints() - t2.getPoints())
								/ t2.getPoints();
						t2Form += Math.abs(t1.getPoints() - t2.getPoints())
								/ t2.getPoints();
					}
				} else if (t1.getPoints() < t2.getPoints()) {
					if (t1.getPoints() == 0) {
						t1Form += 1 / 4;
						t2Form -= 1 / 4;
					} else {
						t1Form += Math.abs(t1.getPoints() - t2.getPoints())
								/ t1.getPoints();
						t2Form -= Math.abs(t1.getPoints() - t2.getPoints())
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

	private void getTablePosition() {
		// get the classification position on the team table for the two teams
		// in hand

		N = ctt.getClassificationPos().size();
		for (int i = 0; i < N; i++) {
			if (mobj.getT1()
					.equals(ctt.getClassificationPos().get(i).getTeam())) {
				posT1 = i;
			} else if (mobj.getT2().equals(
					ctt.getClassificationPos().get(i).getTeam())) {
				posT2 = i;
			}
		}
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

}
