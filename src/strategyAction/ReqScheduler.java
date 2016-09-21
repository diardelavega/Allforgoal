package strategyAction;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
	private static ReqScheduler rqs;

	private List<AsyncRequest> que = new ArrayList<AsyncRequest>();
	private AsyncRequest reqInHand;
	private int serialNumber = 0;
	private RHandler rh = new RHandler();

	private ReqScheduler() {
	}

	public static ReqScheduler getInstance() {
		if (rqs == null) {
			rqs = new ReqScheduler();
		}
		return rqs;
	}

	public void addReq(String type, List<Integer> list, String attKind) {
		serialNumber = que.get(que.size() - 1).getSerialCode() + 1;
		if (serialNumber == 100)
			serialNumber = 0;
		AsyncRequest ar = new AsyncRequest(type, list, attKind, serialNumber);
		que.add(ar);
	}

	public void startReq() {
		reqInHand = que.get(0);
		runReq();
	}

	public void runReq() {
		switch (reqInHand.getType()) {
		case AsyncType.PRED:
			rh.predictSome(reqInHand.getList(), reqInHand.getAtts(),
					reqInHand.getSerialCode());
			break;
		case AsyncType.DTF:

			break;
		case AsyncType.RE_EVAL:
			rh.reEvaluate(reqInHand.getList(), reqInHand.getSerialCode());
			break;
		case AsyncType.UP_PRE_POINT:
			// update the recent matches data with the prediction points
			// after the prediction request have been executed
//			TempMatchFunctions tmf = new TempMatchFunctions();
//			try {
//				tmf.readInitialTeamFromRecentMatches(dat)
//				tmf.updateRecentPredPoints();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
			break;

		default:
			break;
		}
	}

	public void response(int k) {
		if (k == reqInHand.getSerialCode()) {
			que.remove(0);

			switch (reqInHand.getType()) {
			case AsyncType.PRED:
				TempMatchFunctions tmf = new TempMatchFunctions();
				try {
					//TODO add pred point for today mathces predicted
					tmf.addPredPoints(TimeVariations.tomorrowComps);
				} catch (IOException | SQLException e) {
					e.printStackTrace();
				}
				break;

			case AsyncType.RE_EVAL:
				// TODO Something elee
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
