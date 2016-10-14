package api.functionality.obj;

import java.util.List;

import basicStruct.FullMatchLine;
import basicStruct.MatchPredictionLine;
import extra.ServiceMsg;

/**
 * @author Administrator
 *To hold the match prediction line packets of data (packets from posibly more than one competiions)
 */
public class MPLPack {
//	private String msg = ServiceMsg.MPL_PACK;
	private String country;
	private String competition;
	private int compId;
	private int serinr;
	private List< MatchPredictionLine> lfml;

	public MPLPack(String country, String competition, int compId, int serinr, List< MatchPredictionLine> lfml) {
		super();
		this.country = country;
		this.competition = competition;
		this.compId = compId;
		this.serinr = serinr;
		this.lfml = lfml;
	}

	public MPLPack() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCompetition() {
		return competition;
	}

	public void setCompetition(String competition) {
		this.competition = competition;
	}

	public int getCompId() {
		return compId;
	}

	public void setCompId(int compId) {
		this.compId = compId;
	}

	public int getSerinr() {
		return serinr;
	}

	public void setSerinr(int serinr) {
		this.serinr = serinr;
	}

	public List< MatchPredictionLine> getLfml() {
		return lfml;
	}

	public void setLfml(List<MatchPredictionLine> lfml) {
		this.lfml = lfml;
	}

	
//public String getMsg() {
//		return msg;
//	}
//
//	public void setMsg(String msg) {
//		this.msg = msg;
//	}
//
	
}
