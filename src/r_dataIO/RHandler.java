package r_dataIO;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.rosuda.JRI.Rengine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import structures.CountryCompetition;
import test.MatchGetter;
import basicStruct.CCAllStruct;
import diskStore.AnalyticFileHandler;

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
 *         c(data,dat,dat,) r vector format & java String).& in the
 *         CompletableFuture for runnable shuld go with runAsync instead of
 *         suplyAsync also i doesnt accepts exeptionally method??
 */

public class RHandler {
	public static Logger log = LoggerFactory.getLogger(RHandler.class);

	private List<String> foundImagePath;
	private List<String> predTrainPath;
	private List<String> predTestPath;
	private List<Integer> un_foundImagesCompIds;
	private AnalyticFileHandler afh = new AnalyticFileHandler();

	private void listFiller(CCAllStruct ccs, String ret) {
		/* handle the files (the found and the missing ones) */

		// add the image *.RData file path in the list
		foundImagePath.add(ret);

		// add (the image corresponding) train file path in the list
		File tempFile = afh.getTrainFileName(ccs.getCompId(),
				ccs.getCompetition(), ccs.getCountry());
		if (tempFile.exists() && tempFile.length() > 10) {
			predTrainPath.add(tempFile.getAbsolutePath());
			// Test file should exist because it should have been created
			// in the previous step
			predTestPath.add(afh.getLeatestTestFileName(ccs.getCompId(),
					ccs.getCompetition(), ccs.getCountry()).getAbsolutePath());
		} else {
			// if train file was not found remove the image found from the list
			foundImagePath.remove(ret);
		}
	}

	private String listToRvector(List<String> list) {
		StringBuilder sbf = new StringBuilder("c(");
		int i = 0;
		for (; i < list.size() - 1; i++) {
			sbf.append("'" + list.get(i) + "',");
		}
		sbf.append("'" + list.get(list.size() - 1) + "')");
		return sbf.toString();
	}

	public void predictAll() throws SQLException {
		/*
		 * read the scheduled matches competitions; search initially for the
		 * dtf_objs image file of that competition; for the found images get the
		 * train and test file path; with 3 lists of file paths transformed to R
		 * vecors call the predict function
		 */

		for (Integer key : MatchGetter.schedNewMatches.keySet()) {
			int idx = CountryCompetition.idToIdx.get(key);
			CCAllStruct ccs = CountryCompetition.ccasList.get(idx);
			String ret = afh.getImageFileName(ccs.getCompId(),
					ccs.getCompetition(), ccs.getCountry());
			if (ret != null) {
				listFiller(ccs, ret);
			} else {
				un_foundImagesCompIds.add(key);
			}
		}
		// after that the lists should be full
		Rcall_Pred();
	}

	public void predictSome(List<Integer> comp_Ids) {
		/* predict the competitions given by the list of compeyiyions ids */
		for (Integer key : comp_Ids) {
			int idx = CountryCompetition.idToIdx.get(key);
			CCAllStruct ccs = CountryCompetition.ccasList.get(idx);
			String ret = afh.getImageFileName(ccs.getCompId(),
					ccs.getCompetition(), ccs.getCountry());
			if (ret != null) {
				listFiller(ccs, ret);
			} else {
				un_foundImagesCompIds.add(key);
			}
		}
		// after that the lists should be full
		Rcall_Pred();
	}

	public void predictOne(int comp_Id) {
		/* predict the competitions given by the list of compeyiyions ids */
		int idx = CountryCompetition.idToIdx.get(comp_Id);
		CCAllStruct ccs = CountryCompetition.ccasList.get(idx);
		String ret = afh.getImageFileName(ccs.getCompId(),
				ccs.getCompetition(), ccs.getCountry());
		if (ret != null) {
			listFiller(ccs, ret);
		} else {
			un_foundImagesCompIds.add(comp_Id);
		}
		// after that the lists should be full
		Rcall_Pred();
	}

	public void reEvaluate(List<Integer> comp_Ids) {
		/*
		 * To be used after we ahve the actual data from the matches we
		 * predicted. To keep variating the points we have asigned to the
		 * predictive algorithms.
		 */

		for (Integer key : comp_Ids) {
			int idx = CountryCompetition.idToIdx.get(key);
			CCAllStruct ccs = CountryCompetition.ccasList.get(idx);
			String ret = afh.getImageFileName(ccs.getCompId(),
					ccs.getCompetition(), ccs.getCountry());
			if (ret != null) {
				listFiller(ccs, ret);
			} else {
				un_foundImagesCompIds.add(key);
			}
		}
		// after that the lists should be full
		Rcall_ReEvaluate();
	}

	public void Rcall_DTF(List<String> slist) {
		String trVec = listToRvector(slist);
		Runnable r = () -> {
			Rengine re = null;
			try {
				re = new Rengine(new String[] { "--no-save" }, false, null);
				re.eval(" source('C:/TotalPrediction/DTF_Create.R')");
				re.eval("runAll(" + trVec + ")");
				re.end();
			} catch (Exception e) {
				log.warn("SOMETHING WHENT WRONG");
				e.printStackTrace();
				{// test r func
					// double d = re.eval("test(100," + vecs + ")").asDouble();
					// log.info("the double isssss= {}", d);
				}
			} finally {
				re.end();
			}
		};

		CompletableFuture futureCount = CompletableFuture.runAsync(r)
		// .exceptionally(() -> log.warn("aaaa, {}"))
				.thenAccept(
						(c) -> log.info("succesfull R DTF completion  msg:{}",
								c));
	}

	public void Rcall_Pred() {
		String dftVec = listToRvector(foundImagePath);
		String trVec = listToRvector(predTrainPath);
		String tsVec = listToRvector(predTestPath);

		Runnable r = () -> {
			Rengine re = null;
			try {
				re = new Rengine(new String[] { "--no-save" }, false, null);
				re.eval(" source('C:/TotalPrediction/Predict.R')");
				re.eval("predictAll(" + dftVec + ", " + trVec + ", " + tsVec
						+ ")");
				re.end();
			} catch (Exception e) {
				log.warn("SOMETHING WHENT WRONG");
				e.printStackTrace();
			} finally {
				re.end();
			}
		};

		CompletableFuture futureCount = CompletableFuture
				.runAsync(r)
				.thenAccept(
						(c) -> log
								.info("succesfull R Prediction completion  msg:{}",
										c));
	}

	public void Rcall_ReEvaluate() {
		String dftVec = listToRvector(foundImagePath);
		String tsVec = listToRvector(predTestPath);

		Runnable r = () -> {
			Rengine re = null;
			try {
				re = new Rengine(new String[] { "--no-save" }, false, null);
				re.eval(" source('C:/TotalPrediction/ReEvaluation.R')");
				re.eval("reEvalAll(" + dftVec + ", " + tsVec + ")");
				re.end();
			} catch (Exception e) {
				log.warn("SOMETHING WHENT WRONG");
				e.printStackTrace();
			} finally {
				re.end();
			}
		};

		CompletableFuture futureCount = CompletableFuture.runAsync(r)
				.thenAccept(
						(c) -> log.info(
								"succesfull R reEvaluation completion  msg:{}",
								c));

	}

	public void handleUnfound() {
		/*
		 * for each of the unfound images for the competition ids () stored in
		 * un_found get the train file in the prediction folder & call R to
		 * initiate the DTF objects and execute a cros fold validation crfv for
		 * that cometitiom
		 */
		CCAllStruct ccs;
		List<String> slist = new ArrayList<String>();

		for (int i : un_foundImagesCompIds) {
			ccs = CountryCompetition.ccasList.get(CountryCompetition.idToIdx
					.get(i));
			File temptrFile = afh.getTrainFileName(ccs.getCompId(),
					ccs.getCompetition(), ccs.getCountry());
			if (temptrFile.exists() && temptrFile.length() > 10) {
				slist.add(temptrFile.getAbsolutePath());
			} else {
				log.warn("Comp Id {} has no Training Prediction file", i);
				un_foundImagesCompIds.remove(i);
			}
		}
		Rcall_DTF(slist);

		// call R to predict next match matchline pred data
		predictSome(un_foundImagesCompIds);
	}

	// ---------------------
	public void testRcall() {
		List<String> slist = new ArrayList<String>();
		slist.add("C:/BastData/Pred/Data/Norway/Eliteserien__112__Data");
		String vecs = listToRvector(slist);

		Runnable r = () -> {
			Rengine re = null;
			try {
				re = new Rengine(new String[] { "--no-save" }, false, null);
				re.eval(" source('C:/TotalPrediction/DTF_Create.R')");
				re.eval("runAll(" + vecs + ")");
				re.end();
			} catch (Exception e) {
				log.warn("SOMETHING WHENT WRONG");
				e.printStackTrace();
				{// test r func
					// double d = re.eval("test(100," + vecs + ")").asDouble();
					// log.info("the double isssss= {}", d);
				}
			} finally {
				re.end();
			}
		};

		CompletableFuture futureCount = CompletableFuture.runAsync(r)
		// .exceptionally(() -> log.warn("aaaa, {}"))
				.thenAccept(
						(c) -> log.info("succesfull R DTF completion  msg:{}",
								c));
	}
}
