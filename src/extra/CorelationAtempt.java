package extra;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import test.MatchGetter;
import basicStruct.MatchObj;
import calculate.MatchToTableRenewal;

public class CorelationAtempt {
	public static Logger log = LoggerFactory.getLogger(CorelationAtempt.class);

	public List<MatchObj> corelatePunterXScorerTeams(String finerr,
			List<MatchObj> dbmatches) throws IOException {
		log.info("Corelating");

		List<MatchObj> corelatedTeamMAtches= new ArrayList<MatchObj>();
		List<MatchObj> smallForPredList= new ArrayList<MatchObj>();
		Map<Integer, List<MatchObj>> scrapmap = null;
		if (finerr.equals("fin")) {
			scrapmap = MatchGetter.finNewMatches;
			log.info("finNewMatches");
		} else if (finerr.equals("err")) {
			scrapmap = MatchGetter.errorNewMatches;
			log.info("errorNewMatches");
		}

		Unilang ul = new Unilang();

		// List<String> dbTeams = new ArrayList<>();
		// int compId = -1;
		int chosenDbIdx1 = -1, chosenDbIdx2 = -1;
		float dist1 = 1000, dist2 = 1000, dist = 0;
		boolean foundTeamFlag = false;
		int cid = -1, prevCid=-1;

		// TODO read from tempmatches ordered by compid
		for (MatchObj m : dbmatches) {
			cid = m.getComId();
			
			if(prevCid!=cid){
				prevCid=cid;
				if(smallForPredList.size()>0){
					addToPredTrainDataSet(smallForPredList);
					smallForPredList.clear();
				}
			}
			if (!scrapmap.containsKey(cid)) {
				log.warn("contained matches with cid:{}", cid);
				continue;
			}
			dist1 = 1000;
			dist2 = 1000;
			chosenDbIdx1 = -1;
			chosenDbIdx2 = -1;
			String t1 = null;
			for (int kk = 0; kk < scrapmap.get(cid).size(); kk++) {
				t1 = ul.scoreTeamToCcas(scrapmap.get(cid).get(kk).getT1());
				if (t1 == null) {
					foundTeamFlag = false;
				} else {// not null( found in unilang)
					foundTeamFlag = true;
				}
				if (!foundTeamFlag) {
					// for (int i = 0; i < scrapmap.get(cid).size(); i++) {
					dist = StringSimilarity.teamSimilarity(m.getT1(), scrapmap .get(cid).get(kk).getT1());
					log.info("{}   vs   {}", m.getT1(), scrapmap.get(cid).get(kk).getT1(), dist);
					if (dist1 > dist) {
						dist1 = dist;
						chosenDbIdx1 = kk;
					}
				}
			}//for kk
			String t2 = null;
			if (t1 != null || dist1 < StandartResponses.TEAM_DIST) {
				// we have found a team
				t2=ul.scoreTeamToCcas(scrapmap.get(cid).get(chosenDbIdx1).getT2());
				if(t2!=null){
					if(t2.equals(m.getT2())){
						log.info("HARD! Confirm T2 punter:{}, scorer:{}",m.getT2(),scrapmap.get(cid).get(chosenDbIdx1).getT2() );
					}
					else{
						log.warn("SOMETHING WHENT WRONG||| T1 punter:{}, scorer:{} --  T2 punter:{}, scorer:{}",m.getT1(),scrapmap.get(cid).get(chosenDbIdx1).getT1(),m.getT2(),scrapmap.get(cid).get(chosenDbIdx1).getT2() );
					}
				} else{//unstored t2
					dist2=StringSimilarity.teamSimilarity(m.getT2(), scrapmap.get(cid).get(chosenDbIdx1).getT2());
					if(dist2<StandartResponses.TEAM_DIST){
						log.info("Similarity Confirm T2 punter:{}, scorer:{}",m.getT2(),scrapmap.get(cid).get(chosenDbIdx1).getT2() );
					}
					else if(dist2<StandartResponses.RELATIVE_TEAM_DIST(scrapmap.get(cid).get(chosenDbIdx1).getT2())){
						log.info("RELATIVE Similarity T2 punter:{}, scorer:{}",m.getT2(),scrapmap.get(cid).get(chosenDbIdx1).getT2() );
						ul.addTeam(m.getT2(), scrapmap.get(cid).get(chosenDbIdx1).getT2());
					}
					else{
						log.warn("TOO FAR,  the T2 punter:{}, scorer:{}",m.getT2(),scrapmap.get(cid).get(chosenDbIdx1).getT2() );
						log.info("TO CHECK Added!!! punter:{}, scorer:{}",m.getT2(),scrapmap.get(cid).get(chosenDbIdx1).getT2() );
						ul.addTeam(m.getT2(), scrapmap.get(cid).get(chosenDbIdx1).getT2());
					}
				}
				MatchObj mobj = m;
				mobj.setHt1(scrapmap.get(cid).get(chosenDbIdx1).getHt1());
				mobj.setHt2(scrapmap.get(cid).get(chosenDbIdx1).getHt2());
				mobj.setFt1(scrapmap.get(cid).get(chosenDbIdx1).getFt1());
				mobj.setFt2(scrapmap.get(cid).get(chosenDbIdx1).getFt2());
				smallForPredList.add(mobj);
				corelatedTeamMAtches.add(mobj);
				scrapmap.get(cid).remove(chosenDbIdx1);

			} else {// if t1 not found
				// loop to find the t2 of the team and go on from there
				for (int kk = 0; kk < scrapmap.get(cid).size(); kk++) {
					 t2 = ul.scoreTeamToCcas(m.getT2());
					if (t2 == null) {
						foundTeamFlag = false;
					} else {
						foundTeamFlag = true;
					}
					if (!foundTeamFlag) {
						for (int i = 0; i < scrapmap.get(cid).size(); i++) {
							dist = StringSimilarity.teamSimilarity(m.getT2(), scrapmap.get(cid).get(i).getT2());
							log.info("{}   vs   {}", m.getT2(), scrapmap.get(cid) .get(i).getT2(), dist1);
							if (dist2 > dist) {
								dist2 = dist;
								chosenDbIdx2 = i;
							}
						}
					}
				}//for k
				//since we found nothig from t1, now corelate it based on t2 & match binding
				if (t2 != null || dist2 < StandartResponses.TEAM_DIST) {
					dist1=StringSimilarity.teamSimilarity(m.getT1(), scrapmap.get(cid).get(chosenDbIdx2).getT1());
					if(dist1<StandartResponses.TEAM_DIST){
						log.info("Similarity Confirm T1 punter:{}, scorer:{}",m.getT1(),scrapmap.get(cid).get(chosenDbIdx2).getT1() );
					}
					else if(dist1<StandartResponses.RELATIVE_TEAM_DIST(scrapmap.get(cid).get(chosenDbIdx2).getT1())){
						log.info("RELATIVE Similarity T1 punter:{}, scorer:{}",m.getT1(),scrapmap.get(cid).get(chosenDbIdx2).getT1() );
						ul.addTeam(m.getT1(), scrapmap.get(cid).get(chosenDbIdx2).getT1());
					}
					else{
						log.warn("TOO FAR,  the T1 punter:{}, scorer:{}",m.getT1(),scrapmap.get(cid).get(chosenDbIdx2).getT1() );
						log.info("TO CHECK Added!!! punter:{}, scorer:{}",m.getT1(),scrapmap.get(cid).get(chosenDbIdx2).getT1() );
						ul.addTeam(m.getT1(), scrapmap.get(cid).get(chosenDbIdx2).getT1());
					}
				}//if found t2
				MatchObj mobj = m;
				mobj.setHt1(scrapmap.get(cid).get(chosenDbIdx1).getHt1());
				mobj.setHt2(scrapmap.get(cid).get(chosenDbIdx1).getHt2());
				mobj.setFt1(scrapmap.get(cid).get(chosenDbIdx1).getFt1());
				mobj.setFt2(scrapmap.get(cid).get(chosenDbIdx1).getFt2());
				corelatedTeamMAtches.add(mobj);
				
				
			}// else t1 not found
		}// for m : dbmatches
		return corelatedTeamMAtches;
	}
	private void addToPredTrainDataSet(List<MatchObj> predictionsList) {
		/*
		 * add the concludet matches and the updated attributes corresponding to
		 * them to the Prediction training file
		 */
		MatchToTableRenewal mttr = new MatchToTableRenewal();
		try {
			mttr.calculate(predictionsList);
		} catch (SQLException | IOException e) {
			e.printStackTrace();
		}
	}
}
