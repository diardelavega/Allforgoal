package strategyAction;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import r_dataIO.RHandler;
import structures.TimeVariations;
import extra.AsyncType;
import basicStruct.AsyncRequest;

/**
 * @author Administrator
 *
 *         To be used for the competion of asyncronous tasks and their
 *         counterpart , a syncronous task (supposesed to be executed after the
 *         complition of the asynchronous one ) actualy waits and finds the data
 *         that needs to complete
 */
public class ReqScheduler {
	public static final Logger log = LoggerFactory
			.getLogger(ReqScheduler.class);
	private static ReqScheduler rqs;

	private List<AsyncRequest> que = new ArrayList<AsyncRequest>();
	private AsyncRequest reqInHand;
	private int serialNumber = 0;
	private RHandler rh = new RHandler();
	private String status = "idle";

	private ReqScheduler() {
	}

	public static ReqScheduler getInstance() {
		if (rqs == null) {
			rqs = new ReqScheduler();
		}
		log.info("---: size - {},  nr - {}", rqs.que.size(), rqs.serialNumber);
		return rqs;
	}

	public void addReq(String type, List<Integer> list, String attKind,
			LocalDate ld) {
		if (serialNumber == 100)
			serialNumber = 0;

		if (que.size() > 0) {
			serialNumber = que.get(que.size() - 1).getSerialCode() + 1;
		}
		log.info("adding req {} {} {}", attKind, ld, serialNumber);

		AsyncRequest ar = new AsyncRequest(type, list, attKind, serialNumber,
				ld);
		que.add(ar);
	}

	public void startReq() {
		log.info("status: " + status);
		if (status.equals("idle")) {
			if (que.size() > 0) {
				reqInHand = que.get(0);
				log.info("this req #" + reqInHand.getSerialCode()
						+ " is Running");
				runReq();
			} else {
				log.info("No REQUESTS in Line");
			}
		} else {
			// TODO waiting is not the request in hand, the req in hand is
			// running
			log.info("this req #" + serialNumber + " is Waiting");
		}
	}

	public void runReq() {
		log.info("run {}, #{}", reqInHand.getType(), reqInHand.getSerialCode());
		reqInHand.show();
		status = "running";
		switch (reqInHand.getType()) {
		case AsyncType.PRED:
			rh.predictSome(reqInHand.getList(), reqInHand.getAtts(),
					reqInHand.getSerialCode(), reqInHand.getLd());
			break;
		case AsyncType.DTF:
			rh.Rcall_DTF(reqInHand.getList(), reqInHand.getAtts(),
					reqInHand.getSerialCode());
			break;
		case AsyncType.RE_EVAL:
			rh.reEvaluate(reqInHand.getList(), reqInHand.getSerialCode(),
					reqInHand.getLd());
			break;
		case AsyncType.UP_PRE_POINT:
			// update the recent matches data with the prediction points
			// after the prediction request have been executed
			// TempMatchFunctions tmf = new TempMatchFunctions();
			// try {
			// tmf.readInitialTeamFromRecentMatches(dat)
			// tmf.updateRecentPredPoints();
			// } catch (SQLException e) {
			// e.printStackTrace();
			// }
			break;

		default:
			break;
		}
	}

	public void response(int k) {
		log.info("REQ response with code {}", k);
		// for (int i = 0; i < que.size(); i++) {
		// if (que.get(i).getSerialCode() == k) {
		// System.out.println(" SerialCode Found  ind: " + i);
		// }
		// }

		// if the serial code of the first req in line arrives => R has finished
		// execution, so remove the first req in line
		if (k == reqInHand.getSerialCode()) {
			que.remove(0);
			status = "idle";

			switch (reqInHand.getType()) {
			case AsyncType.PRED:
				TempMatchFunctions tmf = new TempMatchFunctions();
				try {
					tmf.addPredPoints(reqInHand.getList(), reqInHand.getLd());
					log.info("Prediction Point addition finished");
				} catch (IOException | SQLException e) {
					e.printStackTrace();
				}
				// ask the next task to run
				startReq();
				break;

			case AsyncType.RE_EVAL:
				// TODO Something elee
				log.info("Reevaluation of AccVal finished");
				startReq();
				break;

			case AsyncType.DTF:
				// TODO create a new request to predict the comps that just had
				// a dtf file created
				log.info("Stf creation finished");
				// addReq(AsyncType.PRED,reqInHand.getList(),
				// reqInHand.getAtts(), reqInHand.getLd());
				startReq();
				break;

			default:
				break;
			}
		}
	}

	public void ignoreFirstTask() {
		que.remove(0);
	}
}
