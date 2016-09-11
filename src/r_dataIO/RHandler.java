package r_dataIO;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

	private List<String> foundImagePath = new ArrayList<String>();
	private List<String> predTrainPath = new ArrayList<String>();
	private List<String> predTestPath = new ArrayList<String>();
	private List<Integer> un_foundImagesCompIds = new ArrayList<>();
	private AnalyticFileHandler afh = new AnalyticFileHandler();

	private void predListFiller(CCAllStruct ccs, String ret) {
		/*
		 * handle the files for the prediction if the image file has been found
		 * get the coresponding test and train files of that competition.
		 * 
		 * The prediction should be done only for the today matches not for
		 * tomorrows.Thats why the test file we require has the today date
		 */

		// add the image *.dtf.RData file path in the list
		foundImagePath.add(ret);

		// add (the image corresponding) train file path in the list
		File tempFile = afh.getTrainFileName(ccs.getCompId(),
				ccs.getCompetition(), ccs.getCountry());
		if (tempFile != null) {
			File testfile = afh.getTestFileName(ccs.getCompId(),
					ccs.getCompetition(), ccs.getCountry(), LocalDate.now());
			if (testfile != null) {
				predTestPath.add(testfile.getAbsolutePath());
				predTrainPath.add(tempFile.getAbsolutePath());
			} else {
				foundImagePath.remove(ret);
				predTrainPath.remove(tempFile.getAbsolutePath());
				log.warn("test file for {}, {}, {}  ", ccs.getCompId(),
						ccs.getCompetition(), ccs.getCountry());
			}
		} else {
			// if train file was not found remove the image found from the list
			foundImagePath.remove(ret);
			log.warn("train file for {}, {}, {}  ", ccs.getCompId(),
					ccs.getCompetition(), ccs.getCountry());
		}
	}

	private String listToRvector(List<String> list) {
		/* transform a java list to an R vector syntax based (java string) */
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
				predListFiller(ccs, ret);
			} else {
				un_foundImagesCompIds.add(key);
			}
		}
		// after that the lists should be full
		if (foundImagePath.size() > 0) {
			Rcall_Pred();
		} else if (un_foundImagesCompIds.size() > 0) {
			handleUnfound();
		}
	}

	public void predictSome(List<Integer> comp_Ids) {
		/* predict the competitions given by the list of competition ids */
		for (Integer key : comp_Ids) {
			int idx = CountryCompetition.idToIdx.get(key);
			CCAllStruct ccs = CountryCompetition.ccasList.get(idx);
			String ret = afh.getImageFileName(ccs.getCompId(),
					ccs.getCompetition(), ccs.getCountry());
			if (ret != null) {
				predListFiller(ccs, ret);
			} else {
				un_foundImagesCompIds.add(key);
			}
		}
		// after that the lists should be full
		if (foundImagePath.size() > 0) {
			Rcall_Pred();
		} else if (un_foundImagesCompIds.size() > 0) {
			handleUnfound();
		}
	}

	public void predictOne(int comp_Id) {
		/* predict the competitions given by the list of compeyiyions ids */
		int idx = CountryCompetition.idToIdx.get(comp_Id);
		CCAllStruct ccs = CountryCompetition.ccasList.get(idx);
		String ret = afh.getImageFileName(ccs.getCompId(),
				ccs.getCompetition(), ccs.getCountry());
		if (ret != null) {
			predListFiller(ccs, ret);
		} else {
			un_foundImagesCompIds.add(comp_Id);
		}
		// after that the lists should be full
		if (foundImagePath.size() > 0) {
			Rcall_Pred();
		} else if (un_foundImagesCompIds.size() > 0) {
			handleUnfound();
		}
	}

	public void reEvaluate(List<Integer> comp_Ids) {
		/*
		 * To keep in mind that the reevaluation will be done for yesterday
		 * matches (maybe for the today matches) but the list filler only
		 * searches for test-pred matches with today-date
		 */

		/*
		 * To be used after we have the actual data from the matches we
		 * predicted. To keep variating/calibrating the points we have asigned
		 * to the predictive algorithms.
		 */

		for (Integer key : comp_Ids) {
			int idx = CountryCompetition.idToIdx.get(key);
			CCAllStruct ccs = CountryCompetition.ccasList.get(idx);
			String ret = afh.getImageFileName(ccs.getCompId(),
					ccs.getCompetition(), ccs.getCountry());
			if (ret != null) {
				predListFiller(ccs, ret);
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
			log.info("START: {}", LocalDateTime.now());
			Rengine re = null;
			try {
				re = new Rengine(new String[] { "--no-save" }, false, null);
				re.eval(" source('C:/TotalPrediction/DTF_Create.R')");
				re.eval("runAll(" + trVec + ")");
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

		CompletableFuture.runAsync(r).thenAccept(
				(c) -> log.info("FINISH :{}  \n succesfull R DTF completion  msg:{}",LocalDateTime.now(), c));
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
			} catch (Exception e) {
				log.warn("SOMETHING WHENT WRONG");
				e.printStackTrace();
			} finally {
				re.end();
			}
		};

		// CompletableFuture futureCount = CompletableFuture
		CompletableFuture.runAsync(r).thenAccept(
				(c) -> log
						.info("succesfull R Prediction completion  msg:{}", c));
	}

	public void Rcall_ReEvaluate() {
		/*
		 * TODO: reevaluate works on the bases that the test file prediction
		 * attributes are writen in the test file after the match.***********
		 * HANDLE this
		 */

		String dftVec = listToRvector(foundImagePath);
		String tsVec = listToRvector(predTestPath);

		Runnable r = () -> {
			Rengine re = null;
			try {
				re = new Rengine(new String[] { "--no-save" }, false, null);
				re.eval(" source('C:/TotalPrediction/ReEvaluation.R')");
				re.eval("reEvalAll(" + dftVec + ", " + tsVec + ")");
			} catch (Exception e) {
				log.warn("SOMETHING WHENT WRONG");
				e.printStackTrace();
			} finally {
				re.end();
			}
		};

		CompletableFuture.runAsync(r).thenAccept(
				(c) -> log.info("succesfull R reEvaluation completion  msg:{}",
						c));

	}

	public void handleUnfound() {

		// ///////TODO handle creae dtd
		/*
		 * for each of the unfound images for the competition ids () stored in
		 * un_found get the train file in the prediction folder & call R to
		 * initiate the DTF objects and execute a cros fold validation crfv for
		 * that cometitiom
		 */
		CCAllStruct ccs;
		List<String> trlist = new ArrayList<String>();

		for (int i : un_foundImagesCompIds) {
			ccs = CountryCompetition.ccasList.get(CountryCompetition.idToIdx
					.get(i));
			File temptrFile = afh.getTrainFileName(ccs.getCompId(),
					ccs.getCompetition(), ccs.getCountry());
			if (temptrFile != null) {
				try {
					log.info("{}",temptrFile.getCanonicalPath());
					log.info("{}",temptrFile.getName());
					log.info("{}",temptrFile.getParent());
					log.info("{}",temptrFile.getAbsolutePath().replace("\\", "/"));
					log.info("{}",temptrFile.getAbsolutePath());
					log.info("{}",temptrFile.getPath());
				} catch (IOException e) {
					e.printStackTrace();
				}
				trlist.add(temptrFile.getAbsolutePath().replace("\\", "/"));
			} else {
				log.warn("Comp Id {} has no Training Prediction file", i);
				un_foundImagesCompIds.remove(i);
			}
		}
		Rcall_DTF(trlist);

		// call R to predict next match matchline pred data
		// predictSome(un_foundImagesCompIds);
	}

	// ---------------------
	public void testRcall() {
		List<String> slist = new ArrayList<String>();
		slist.add("C:/BastData/Pred/Data/Norway/Eliteserien__112__Data");
		String vecs = listToRvector(slist);

		Runnable r = () -> {
			log.info("START: {}", LocalDateTime.now());
			Rengine re = null;
			try {
				re = new Rengine(new String[] { "--no-save" }, false, null);
				re.eval(" source('C:/TotalPrediction/DTF_Create.R')");
				// re.eval("runAll(" + vecs + ")");
				double d = re.eval("test(100," + vecs + ")").asDouble();
				log.info("the double isssss= {}", d);
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

		CompletableFuture.runAsync(r)
		// .exceptionally(() -> log.warn("aaaa, {}"))
				.thenAccept(
						(c) -> log.info(
								" {} succesfull R DTF completion  msg:{}",
								LocalDateTime.now(), c));
	}
	
	public void test2(){
//		Rengine re = null;
//		try {
//			re = new Rengine(new String[] { "--no-save" }, false, null);
//			re.eval(" source('C:/TotalPrediction/DTF_Create.R')");
////			re.eval("runAll(" + vecs + ")");
//			 double d = re.eval("test(100," + vecs + ")").asDouble();
//			 log.info("the double isssss= {}", d);
//			re.end();
	}
}
