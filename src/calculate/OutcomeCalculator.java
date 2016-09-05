package calculate;

import structures.ReducedPredictionTestFile;
import basicStruct.MatchObj;
import extra.MatchOutcome;

public class OutcomeCalculator {
	
	public ReducedPredictionTestFile outcomeAsignment(MatchObj mobj) {
		ReducedPredictionTestFile redPf = new ReducedPredictionTestFile();
		if (mobj != null) {
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
			redPf.setTotHtScore(mobj.getHt1() + mobj.getHt2());
			redPf.setTotFtScore(mobj.getFt1() + mobj.getFt2());

			try {// half time
				if (mobj.getHt1() + mobj.getHt2() >= 2) {
					redPf.setHt1pOutcome(MatchOutcome.yes);
					redPf.setHt2pOutcome(MatchOutcome.yes);
				} else if (mobj.getHt1() + mobj.getHt2() < 2
						&& mobj.getHt1() + mobj.getHt2() >= 1) {
					redPf.setHt1pOutcome(MatchOutcome.yes);
					redPf.setHt2pOutcome(MatchOutcome.no);
				} else {
					redPf.setHt1pOutcome(MatchOutcome.no);
					redPf.setHt2pOutcome(MatchOutcome.no);
				}
			} catch (Exception e) {
				redPf.setHt1pOutcome(MatchOutcome.missing);
				redPf.setHt2pOutcome(MatchOutcome.missing);
			}
		} else {
			// it should come here for the test pred file
			redPf.setHt1pOutcome(MatchOutcome.missing);
			redPf.setHt2pOutcome(MatchOutcome.missing);
			redPf.setScoreOutcome(MatchOutcome.missing);
			redPf.setHeadOutcome(MatchOutcome.missing);
		}
		return redPf;
	}
}
