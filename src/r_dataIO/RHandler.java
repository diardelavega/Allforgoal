package r_dataIO;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.rosuda.JRI.REXP;
import org.rosuda.JRI.Rengine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import basicStruct.CCAllStruct;
import api.functionality.CompIdToCountryCompCompID;
import api.functionality.obj.CountryCompCompId;
import diskStore.AnalyticFileHandler;
import structures.CountryCompetition;
import test.MatchGetter;

/**
 * @author Administrator
 *
 *         From the competitionId(s) of the matches about to be played gather
 *         the R data needed to make the prediction. ***********************
 *         What is needed is the paths of the .RData files that contain the DTF
 *         object the train file and the leatest Test file of that competition.
 *         All files must exist in order to make a prediction*****************
 *         From what i understand the data passed to R through JRI can only be
 *         of String format.(so the data from the list will be written as a
 *         c(data,dat,dat,))
 */

public class RHandler {
	public static Logger log = LoggerFactory.getLogger(RHandler.class);

	private List<String> foundImagePath;
	private List<String> predTrainPath;
	private List<String> predTestPath;
	private List<Integer> un_foundImagesCompIds;
	private AnalyticFileHandler afh = new AnalyticFileHandler();

	public void makePrediction() throws SQLException {
		// for all keys of the new Matches

		for (Integer key : MatchGetter.schedNewMatches.keySet()) {
			// CountryCompCompId ccci = new CompIdToCountryCompCompID()
			// .search(key);
			int idx = CountryCompetition.idToIdx.get(key);
			CCAllStruct ccs = CountryCompetition.ccasList.get(idx);
			// unefective ccci with db query
			// search for image ".Rdata" file with DTF ojbs
			String ret = afh.getImageFileName(ccs.getCompId(),
					ccs.getCompetition(), ccs.getCountry());
			if (ret != null) {
				listFiller(ccs, ret);
			} else {
				un_foundImagesCompIds.add(key);
			}
		}

		rVectorMaker();
	}

	public void listFiller(CCAllStruct ccs, String ret) {
		// add the image *.RData file path in the list
		foundImagePath.add(ret);

		// add (the image corresponding) train file path in the list
		File tempFile = afh.getTrainFileName(ccs.getCompId(),
				ccs.getCompetition(), ccs.getCountry());
		if (tempFile.exists() && tempFile.length() > 10) {
			predTrainPath.add(tempFile.getAbsolutePath());
			// Test file should exist becouse it should have been created
			// in the previous step
			predTestPath.add(afh.getLeatestTestFileName(ccs.getCompId(),
					ccs.getCompetition(), ccs.getCountry()).getAbsolutePath());
		} else {
			// if train file was not found remove the image found from the list
			foundImagePath.remove(ret);
		}
	}

	public void rVectorMaker() {
		/* use the found lists to create vectors to send to R */
		if (foundImagePath.size() != predTestPath.size()) {
			log.warn("Not equal found array sizes");
			return;
		}
		StringBuilder sbf = new StringBuilder("c(");
		StringBuilder sbtr = new StringBuilder("c(");
		StringBuilder sbts = new StringBuilder("c(");
		int i = 0;
		for (; i < foundImagePath.size() - 1; i++) {
			sbf.append("'" + foundImagePath.get(i) + "',");
			sbtr.append("'" + predTrainPath.get(i) + "',");
			sbts.append("'" + predTestPath.get(i) + "',");
		}
		// the last record of the vector should not contain "," comma at the end
		sbf.append("'" + foundImagePath.get(i + 1) + "'(");
		sbtr.append("'" + predTrainPath.get(i + 1) + "')");
		sbts.append("'" + predTestPath.get(i + 1) + "')");
	}
	
	public String listToRvector(List<String> list){
		StringBuilder sbf = new StringBuilder("c(");
		int i = 0;
		for (; i < list.size() - 1; i++) {
			sbf.append("'" + list.get(i) + "',");
		}
		sbf.append("'" + list.get(list.size() - 1) + "')");
		return sbf.toString();
	}

	public void testRcall() {
		Rengine re = new Rengine(new String[] { "--no-save" }, true, null);
		re.eval(" source('C:/TotalPrediction/DTF_Create.R')");
//		List<String> slist = new ArrayList<String>();
//		slist.add("C:/BastData/Pred/Data/Norway/Eliteserien__112__Data");
//		String vecs =listToRvector(slist);
//		re.eval("runAll("+vecs+")");

		int k =re.eval("test(8)").asInt();
		int kk =re.eval("max(8,3,6)").asInt();
		REXP ee = re.eval("max(8,3,6)");
		System.out.println(k);
		System.out.println(kk);
		System.out.println(ee);
		re.end();
	}

	public void handleUnfound() {
		/*
		 * for each of the unfound images for the competition ids () stored in
		 * un_found get the train file in the prediction folder & call R to
		 * initiate the DTF objects and execute a cros fold validation crfv for
		 * that cometitiom
		 */
		CCAllStruct ccs;
		for (int i : un_foundImagesCompIds) {
			ccs = CountryCompetition.ccasList.get(CountryCompetition.idToIdx
					.get(i));
			File tempFile = afh.getTrainFileName(ccs.getCompId(),
					ccs.getCompetition(), ccs.getCountry());
			if (tempFile.exists() && tempFile.length() > 10) {
				predTrainPath.add(tempFile.getAbsolutePath());
			} else {
				log.warn("Comp Id {} has no Training Prediction file", i);
				un_foundImagesCompIds.remove(i);
			}
		}
		// TODO call R files & funct to initiate the crfv for this competition
		// 1 - source DTF_create file;
		// 2- runAll<- function(trPaths)
		// 2.5- call runAll() with a vector "c()" of predTrainPath
		// 3: it will create datasets, DTF objects & store them in the DTF
		// folder

		// TODO call R to predict next match matchline pred data
	}

}

