package calculate;

import structures.ReducedPredictionTestFile;
import basicStruct.MatchObj;
import basicStruct.MatchPredictionLine;
import extra.MatchOutcome;
import extra.Status;

public class OutcomeCalculator {

	public ReducedPredictionTestFile outcomeAsignment(MatchObj mobj) {
		ReducedPredictionTestFile redPf = new ReducedPredictionTestFile();
		redPf.setT1(mobj.getT1());
		redPf.setT2(mobj.getT2());
		if (!mobj.getMatchTime().equals(Status.ERROR)) {// correctly finished
			// head outcome 1X2
			if (mobj.getFt1() > mobj.getFt2()) {
				redPf.setHeadOutcome(MatchOutcome.home);
			} else if (mobj.getFt1() < mobj.getFt2()) {
				redPf.setHeadOutcome(MatchOutcome.away);
			} else {
				redPf.setHeadOutcome(MatchOutcome.deaw);
			}

			// over under outcome
			if (mobj.getFt1() + mobj.getFt2() >= 3) {
				redPf.setScoreOutcome(MatchOutcome.over);
			} else {
				redPf.setScoreOutcome(MatchOutcome.under);
			}

			// tot score
			redPf.setTotHtScore((mobj.getHt1() + mobj.getHt2()) + "");
			redPf.setTotFtScore((mobj.getFt1() + mobj.getFt2()) + "");

			try {// half time
				if (mobj.getHt1() + mobj.getHt2() >= 1) {
					redPf.setHt1pOutcome(MatchOutcome.yes);
					if (mobj.getHt1() + mobj.getHt2() >= 2) {
						redPf.setHt2pOutcome(MatchOutcome.yes);
					} else {
						redPf.setHt2pOutcome(MatchOutcome.no);
					}
				} else {
					redPf.setHt1pOutcome(MatchOutcome.no);
					redPf.setHt2pOutcome(MatchOutcome.no);
				}
			} catch (Exception e) {
				redPf.setHt1pOutcome(MatchOutcome.missing);
				redPf.setHt2pOutcome(MatchOutcome.missing);
			}
		} else {
			// for the erroneus matches attributes are missing
			redPf.setHt1pOutcome(MatchOutcome.missing);
			redPf.setHt2pOutcome(MatchOutcome.missing);
			redPf.setScoreOutcome(MatchOutcome.missing);
			redPf.setHeadOutcome(MatchOutcome.missing);
			redPf.setTotHtScore(MatchOutcome.missing);
			redPf.setTotFtScore(MatchOutcome.missing);

		}
	System.out.println( redPf.printRedPredLine());
		return redPf;
	}
}
