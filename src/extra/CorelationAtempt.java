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

	/**
	 * corelates and updates the db matches with the finished matches from
	 * scraping scorer
	 */

	public List<MatchObj> corelatePunterXScorerTeams(String finerr, List<MatchObj> dbmatches) throws IOException {


		log.info("Corelating");

		List<MatchObj> corelatedTeamMAtches = new ArrayList<MatchObj>();
		List<MatchObj> smallForPredList = new ArrayList<MatchObj>();
		Map<Integer, List<MatchObj>> scrapmap = null;
		if (finerr.equals("fin")) {
			scrapmap = MatchGetter.finNewMatches;
			log.info("corelating from finNewMatches");
		} else if (finerr.equals("err")) {
			scrapmap = MatchGetter.errorNewMatches;
			log.info("corelating from  errorNewMatches");
		}

		Unilang ul = new Unilang();

		int chosenDbIdx1 = -1, chosenDbIdx2 = -1;
		float dist1 = 1000, dist2 = 1000, dist = 0;
//		boolean foundTeamFlag = false;
		boolean foundTeamFlag = false;
		int cid = -1, prevCid = -1;

		for (MatchObj m : dbmatches) {// for every matcg in the database
			cid = m.getComId();

			if (prevCid != cid) {
				// whenever the observation of a competition ends, store the list containing only its corelated matches and add them to their pred training file @ calculate class   
				prevCid = cid;
				if (smallForPredList.size() > 0) {
					addToPredTrainDataSet(smallForPredList);
					smallForPredList.clear();
				}
			}
			if (!scrapmap.containsKey(cid)) {
				log.warn(finerr+ " doesn't contains matches with cid:{}, as the db array does",cid);

				continue;
			}
			
			dist1 = 1000;
			dist2 = 1000;
			chosenDbIdx1 = -1;
			chosenDbIdx2 = -1;
			String t1 = null;
			String t2 = null;

			/* see if the match "m" is in unilang or in the list of scraped  matchesw with the same copId */
			for (int kk = 0; kk < scrapmap.get(cid).size(); kk++) {// check the list from scrap
				t1 = ul.scoreTeamToCcas(scrapmap.get(cid).get(kk).getT1());
				if (t1 != null) {
					if (t1.equals(m.getT1())) {
						chosenDbIdx1 = kk;
						break;
					}
				} else {
					dist = StringSimilarity.teamSimilarity(m.getT1(), scrapmap .get(cid).get(kk).getT1());
					log.info("**T1-considering* {}   vs   {}", m.getT1(), scrapmap.get(cid).get(kk).getT1(), dist);
					if (dist1 > dist) {
						dist1 = dist;
						chosenDbIdx1 = kk;// smallest dist from
											// list<scrap.get<cig>>
					}
				}
			}// for kk

			
			if (chosenDbIdx1 > -1) {
				// basically if t1 is found t2 will go in aswell, but we check to be thoural
				t2 = ul.scoreTeamToCcas(scrapmap.get(cid).get(chosenDbIdx1).getT2());
				if (t2 != null) {
					if (t2.equals(m.getT2())) {
						chosenDbIdx2 = chosenDbIdx1;
					}
				} else {
					dist = StringSimilarity.teamSimilarity(m.getT2(), scrapmap .get(cid).get(chosenDbIdx1).getT2());
					if (dist <= StandartResponses.TEAM_DIST) {
						log.info("#Similarity Distance Confirm T2 punter:{}, scorer:{}", m.getT2(), scrapmap.get(cid).get(chosenDbIdx1).getT2());
						chosenDbIdx2 = chosenDbIdx1;
					} else if (dist < StandartResponses.RELATIVE_TEAM_DIST(scrapmap.get(cid).get(chosenDbIdx1).getT2())) {
						// if t1 is found by distance that is a strong indicator for t2 of that index & db.t2 of match m
						// from db. so check if the disatane is smaller compared to the teams name
						chosenDbIdx2 = chosenDbIdx1;
						log.info( "-t1 found -> RELATIVE Similarity T2 punter:{}, scorer:{}", m.getT2(), scrapmap.get(cid).get(chosenDbIdx1).getT2());
						log.info("Adding them to unilang");
						ul.addTeam(m.getT2(), scrapmap.get(cid).get(chosenDbIdx1).getT2());

					} else {
						chosenDbIdx2 = chosenDbIdx1;
						log.warn( "!!!*** TOO FAR,  the T2 punter:{}, scorer:{}", m.getT2(), scrapmap.get(cid).get(chosenDbIdx1).getT2());
						log.info("!!!*** ADDDING REGARDLES.. TO BE  CHECKED UNILANG Added!!! punter:{}, scorer:{}");
						ul.addTeam(m.getT2(),scrapmap.get(cid).get(chosenDbIdx1).getT2());
					}
				}// else of t2 = null
			} 
//			else {
			if(chosenDbIdx1==-1){// if t1 was not related to any of the scrap teams 
				//we start and look to corelate T2 by looping the scrap list of that compId
				for (int kk = 0; kk < scrapmap.get(cid).size(); kk++) {// check the list from scrap
					t2 = ul.scoreTeamToCcas(scrapmap.get(cid).get(kk).getT2());
					if (t2 != null) {
						if (t2.equals(m.getT2())) {
							chosenDbIdx2 = kk;
							break;
						}
					} else {
						dist = StringSimilarity.teamSimilarity(m.getT2(), scrapmap .get(cid).get(kk).getT2());
						log.info("**T2-considering* {}   vs   {}", m.getT2(), scrapmap.get(cid).get(kk).getT2(), dist);
						if (dist2 > dist) {
							dist2 = dist;
							chosenDbIdx2 = kk;// smallest dist from list<scrap.get<cig>>
						}
					}
				}// for
				
				
				if (chosenDbIdx2 > -1) {
					//we found t2 check to relate t1, byt again we add it anyway 
					/*since we searched the ul for t1 in previous loop, skip it now*/
					dist = StringSimilarity.teamSimilarity(m.getT1(), scrapmap .get(cid).get(chosenDbIdx2).getT1());
					if (dist <= StandartResponses.TEAM_DIST) {
						log.info("#Similarity Distance Confirm T1 punter:{}, scorer:{}",m.getT1(), scrapmap .get(cid).get(chosenDbIdx2).getT1());
						chosenDbIdx1 = chosenDbIdx2;
					} else if (dist2 < StandartResponses.RELATIVE_TEAM_DIST(scrapmap.get(cid).get(chosenDbIdx1).getT2())) {
						chosenDbIdx1 = chosenDbIdx2;
						log.info( "-t2 found -> RELATIVE Similarity T1 punter:{}, scorer:{}",m.getT1(), scrapmap .get(cid).get(chosenDbIdx2).getT1());
						log.info("Adding them to unilang");
						ul.addTeam(m.getT1(), scrapmap .get(cid).get(chosenDbIdx2).getT1());
					} else {
						chosenDbIdx1 = chosenDbIdx2;
						log.warn( "!!!*** TOO FAR,  the T2 punter:{}, scorer:{}",m.getT1(), scrapmap .get(cid).get(chosenDbIdx2).getT1());
						log.info("!!!*** ADDDING REGARDLES.. TO BE  CHECKED UNILANG Added!!! punter:{}, scorer:{}");
						ul.addTeam(m.getT1(), scrapmap .get(cid).get(chosenDbIdx2).getT1());
					}
				} else {// if chosenDbIdx2 == -1 -> 
					//neather t1 or t2 from db match was corelated
					log.info("could not corelate the match {} {} {}", m.getT1(), m.getT2(), m.getComId());
					continue;
					
				}
			}
			// just for fun
			log.info("Should be the same ... idx1: {}, idx2: {}",chosenDbIdx1,chosenDbIdx2);
		
			MatchObj mobj = m;
			mobj.setHt1(scrapmap.get(cid).get(chosenDbIdx1).getHt1());
			mobj.setHt2(scrapmap.get(cid).get(chosenDbIdx1).getHt2());
			mobj.setFt1(scrapmap.get(cid).get(chosenDbIdx1).getFt1());
			mobj.setFt2(scrapmap.get(cid).get(chosenDbIdx1).getFt2());
			smallForPredList.add(mobj);
			corelatedTeamMAtches.add(mobj);
			scrapmap.get(cid).remove(chosenDbIdx1);

			
		}// for m : dbmatches

		return corelatedTeamMAtches;
	}

	private void addToPredTrainDataSet(List<MatchObj> predictionsList) {
		/*
		 * add the concludet matches and the updated attributes corresponding to
		 * them to the Prediction training file
		 */
		log.info("adding to train file cid:{}, size:{}", predictionsList.get(0).getComId(), predictionsList.size());
		MatchToTableRenewal mttr = new MatchToTableRenewal(predictionsList.get(0).getComId());

		try {
			mttr.calculate(predictionsList);
		} catch (SQLException | IOException e) {
			e.printStackTrace();
		}

	}

}
