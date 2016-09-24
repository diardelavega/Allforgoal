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

import strategyAction.ReqScheduler;
import structures.CountryCompetition;
import test.MatchGetter;
import basicStruct.CCAllStruct;
import diskStore.AnalyticFileHandler;
import extra.AsyncType;
import extra.AttsKind;

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

	private void predListFiller(CCAllStruct ccs, String ret, LocalDate ld) {
		/*
		 * handle the files for the prediction if the image file has been found
		 * get the coresponding test and train files of that competition.
		 * 
		 * The prediction should be done only for the today matches not for
		 * tomorrows.Thats why the test file we require has the today date
		 */

		ret = ret.replace("\\", "/");
		// add the image *.dtf.RData file path in the list
		foundImagePath.add(ret);

		// add (the image corresponding) train file path in the list
		File tempFile = afh.getTrainFileName(ccs.getCompId(),
				ccs.getCompetition(), ccs.getCountry());
		if (tempFile != null) {
			File testfile = afh.getTestFileName(ccs.getCompId(),
					ccs.getCompetition(), ccs.getCountry(), ld);
			if (testfile != null) {
				predTestPath.add(testfile.getAbsolutePath().replace("\\", "/"));
				predTrainPath
						.add(tempFile.getAbsolutePath().replace("\\", "/"));
			} else {
				foundImagePath.remove(ret);
				predTrainPath.remove(tempFile.getAbsolutePath().replace("\\",
						"/"));
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
		log.info("{}", sbf.toString());
		return sbf.toString();

	}

	public void predictAll(LocalDate ld) throws SQLException {
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
				predListFiller(ccs, ret, ld);
			} else {
				un_foundImagesCompIds.add(key);
			}
		}
		// after that the lists should be full
		if (foundImagePath.size() > 0) {
			Rcall_Pred();
		} else if (un_foundImagesCompIds.size() > 0) {
			handleUnfound(ld);
		}
	}

	public void predictSome(List<Integer> comp_Ids, String attsKind, int seri,
			LocalDate ld) {
		log.info("Predicting some");
		/* predict the competitions given by the list of competition ids */
		for (Integer key : comp_Ids) {
			int idx = CountryCompetition.idToIdx.get(key);
			CCAllStruct ccs = CountryCompetition.ccasList.get(idx);
			//TODO check if already has a prediction
			String ret = afh.getImageFileName(ccs.getCompId(),
					ccs.getCompetition(), ccs.getCountry());
			if (ret != null) {
				predListFiller(ccs, ret, ld);
			} else {
				un_foundImagesCompIds.add(key);
			}
		}
		// after that the lists should be full
		if (foundImagePath.size() > 0) {
			Rcall_Pred(attsKind, seri);
		} else if (un_foundImagesCompIds.size() > 0) {
			handleUnfound(ld);
		}
	}

	public void predictOne(int comp_Id, LocalDate ld) {
		/* predict the competitions given by the list of compeyiyions ids */
		int idx = CountryCompetition.idToIdx.get(comp_Id);
		CCAllStruct ccs = CountryCompetition.ccasList.get(idx);
		String ret = afh.getImageFileName(ccs.getCompId(),
				ccs.getCompetition(), ccs.getCountry());

		if (ret != null) {
			ret = ret.replace("\\", "/");
			predListFiller(ccs, ret, ld);
		} else {
			un_foundImagesCompIds.add(comp_Id);
		}
		// after that the lists should be full
		if (foundImagePath.size() > 0) {
			Rcall_Pred();
		} else if (un_foundImagesCompIds.size() > 0) {
			handleUnfound(ld);
		}
	}

	public void reEvaluate(List<Integer> comp_Ids, int seri, LocalDate ld) {
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
				predListFiller(ccs, ret, ld);
			} else {
				un_foundImagesCompIds.add(key);
			}
		}
		// after that the lists should be full
		Rcall_ReEvaluate(seri);
	}

	public void Rcall_DTF(List<Integer> trlist) {
		// default dtf objects to be created are head and score
		String R_attKindVector = AttsKind.hs;
		Rcall_DTF(trlist, R_attKindVector, -1);
	}

	public void Rcall_DTF(List<Integer> trlist, String R_attKindVector) {
		// default dtf objects to be created are head and score
		Rcall_DTF(trlist, R_attKindVector, -1);
	}

	public void Rcall_DTF(List<Integer> comp_Ids, String R_attKindVector,
			int seri) {
		log.info("r cal dtf with {} ", R_attKindVector);
		List<String> trlist = new ArrayList<String>();
		for (Integer key : comp_Ids) {
			int idx = CountryCompetition.idToIdx.get(key);
			CCAllStruct ccs = CountryCompetition.ccasList.get(idx);
			String ret = afh.getImageFileName(ccs.getCompId(),
					ccs.getCompetition(), ccs.getCountry());
			if (ret != null) {
				trlist.add(ret);
			}
		}

		String trVec = listToRvector(trlist);
		Runnable r = () -> {
			log.info("START: {}", LocalDateTime.now());
			Rengine re = null;
			try {
				re = new Rengine(new String[] { "--no-save" }, false, null);
				re.eval(" source('C:/TotalPrediction/DTF_Create.R')");
				re.eval("runAll(" + trVec + "," + R_attKindVector + ")");
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

		CompletableFuture
				.runAsync(r)
				.thenAccept(
						(c) -> {
							log.info(
									"FINISH :{}  \n succesfull R DTF completion  msg:{}",
									LocalDateTime.now(), c);
							if (seri > -1) {
								ReqScheduler.getInstance().response(seri);
							}
						});
	}

	public void Rcall_Pred() {
		// deafault prediction without defined atts is h,s,ft
		String R_attKindVector = AttsKind.hs;
		Rcall_Pred(R_attKindVector, -1);
	}

	public void Rcall_Pred(int seri) {
		// deafault prediction without defined atts is h,s,ft
		String R_attKindVector = AttsKind.hs;
		Rcall_Pred(R_attKindVector, seri);
	}

	public void Rcall_Pred(String R_attKindVector, int seri) {
		log.info("r cal predict with {} ", R_attKindVector);

		String dftVec = listToRvector(foundImagePath);
		String trVec = listToRvector(predTrainPath);
		String tsVec = listToRvector(predTestPath);

		Runnable r = () -> {
			log.info("START: {}", LocalDateTime.now());
			Rengine re = null;
			try {
				re = new Rengine(new String[] { "--no-save" }, false, null);
				re.eval(" source('C:/TotalPrediction/Predict.R')");
				re.eval("predictAll(" + dftVec + ", " + trVec + ", " + tsVec
						+ ", " + R_attKindVector + ")");
			} catch (Exception e) {
				log.warn("SOMETHING WHENT WRONG");
				e.printStackTrace();
			} finally {
				re.end();
			}
		};

		// CompletableFuture futureCount = CompletableFuture
		CompletableFuture
				.runAsync(r)
				.thenAccept(
						(c) -> {
							log.info(
									"FINISH :{}  \n succesfull R DTF completion  msg:{}",
									LocalDateTime.now(), c);
							if (seri > -1) {
								ReqScheduler.getInstance().response(seri);
							}
							// else nothing
						});
	}

	public void Rcall_ReEvaluate(int seri) {
		log.info("r cal reevaluate with  ");
		/*
		 * reevaluate works on the bases that the test file prediction
		 * attributes are writen in the test file after the match.***********
		 * HANDLE this
		 */

		String dftVec = listToRvector(foundImagePath);
		String tsVec = listToRvector(predTestPath);

		log.info("r cal reevaluate with  {},  {}", dftVec, tsVec);

		Runnable r = () -> {
			log.info("START: {}", LocalDateTime.now());
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

		CompletableFuture
				.runAsync(r)
				.thenAccept(
						(c) -> {
							log.info(
									"FINISH :{}  \n succesfull R DTF completion  msg:{}",
									LocalDateTime.now(), c);
							if (seri > -1) {
								ReqScheduler.getInstance().response(seri);
							}
						});

	}

	public void handleUnfound(LocalDate ld) {
		log.info("handeling unfound comps");

		/*
		 * origina objective *********************************************** for
		 * each of the unfound images for the competition ids () stored in
		 * un_found get the train file in the prediction folder & call R to
		 * initiate the DTF objects and execute a cros fold validation crfv for
		 * that cometitiom.
		 * ******************************************************************
		 * curent Objective: for all the unfoun img, add a request in the line
		 * to create a dtf head and score file prediction
		 */
		// CCAllStruct ccs;
		// List<String> trlist = new ArrayList<String>();

		// for (int i : un_foundImagesCompIds) {
		// ccs = CountryCompetition.ccasList.get(CountryCompetition.idToIdx
		// .get(i));
		// File temptrFile = afh.getTrainFileName(ccs.getCompId(),
		// ccs.getCompetition(), ccs.getCountry());
		// if (temptrFile != null) {
		// log.info("{}", temptrFile.getAbsolutePath().replace("\\", "/"));
		// trlist.add(temptrFile.getAbsolutePath().replace("\\", "/"));
		// } else {
		// log.warn("Comp Id {} has no Training Prediction file", i);
		// un_foundImagesCompIds.remove(i);
		// }
		// }
		ReqScheduler.getInstance().addReq(AsyncType.DTF, un_foundImagesCompIds,
				AttsKind.hs, ld);
		ReqScheduler.getInstance().startReq();

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

	public void test2() {
		// Rengine re = null;
		// try {
		// re = new Rengine(new String[] { "--no-save" }, false, null);
		// re.eval(" source('C:/TotalPrediction/DTF_Create.R')");
		// // re.eval("runAll(" + vecs + ")");
		// double d = re.eval("test(100," + vecs + ")").asDouble();
		// log.info("the double isssss= {}", d);
		// re.end();
	}

}
