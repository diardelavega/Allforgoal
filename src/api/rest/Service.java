package api.rest;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import api.functionality.CommonAdversariesHandler;
import api.functionality.CompIdToCountryCompCompID;
import api.functionality.CompTableHandler;
import api.functionality.MatchPredLineHandler;
import api.functionality.WeekMatchHandler;
import api.functionality.WinDrawLoseHandler;
import api.functionality.obj.CountryCompCompId;
import api.functionality.obj.MPLPack;
import api.functionality.obj.WeekMatchesCSV;
import basicStruct.CCAllStruct;
import basicStruct.FullMatchLine;
import extra.ServiceMsg;
import extra.StandartResponses;
import structures.CountryCompetition;
import structures.TimeVariations;

import com.google.gson.Gson;

/**
 * @author Administrator
 *
 *         RESTFull API service
 */

@Path("/services")
public class Service {
	public static final Logger log = LoggerFactory.getLogger(Service.class);
	private static boolean flag = false;
	private CountryCompetition cc = new CountryCompetition();

	@GET
	@Path("/redweekly")
	public String reducedWeeklyMatches() {

		return null;
	}

	@GET
	@Path("/fullweekly")
	public String fullWeeklyMatches() {
		return null;
	}

	@GET
	@Path("/predwdl")
	public String competitionMatchPredLineWithWDL() throws SQLException {
		CountryCompCompId ccci = new CompIdToCountryCompCompID().search(112);
		// new CountryCompCompId("Sweden", "Superettan",
		// 164);

		MatchPredLineHandler mph = new MatchPredLineHandler();
		// mph.doer(164, "Superettan", "Sweden");
		mph.doer(ccci);
		Gson gson = new Gson();
		ccci.setObj(mph.getMatchPredLine());
		String jo = gson.toJson(ccci);
		return jo;
	}

	@GET
	@Path("/predwdl/{compid}")
	public String competitionMatchPredLineWithWDL(@PathParam("compid") int compId) throws SQLException {
		// from compId get country & competition
		CompIdToCountryCompCompID ctccci = new CompIdToCountryCompCompID();
		CountryCompCompId ccci = ctccci.search(compId);
		MatchPredLineHandler mph = new MatchPredLineHandler();
		mph.doer(ccci);
		Gson gson = new Gson();
		ccci.setObj(mph.getMatchPredLine());
		String jo = gson.toJson(ccci);
		return jo;
	}

	@GET
	@Path("/wdldata/{compid}")
	public String competitionWDL(@PathParam("compid") int compId) throws SQLException {
		/* get only the wdl data of this specific competition */
		CountryCompCompId ccci = new CompIdToCountryCompCompID().search(compId);
		MatchPredLineHandler mph = new MatchPredLineHandler();
		mph.wdlOnly(ccci);
		Gson gson = new Gson();
		ccci.setObj(mph.getMatchPredLine());
		String jo = gson.toJson(ccci);
		return jo;
	}

	@GET
	@Path("/redweekmatches/{compid}")
	public String weekMatchesRed(@PathParam("compid") int compId) throws SQLException {
		/*
		 * get the weekly data for a competition, including
		 * form,atack,score,defence,etc.
		 */
		int ind = CountryCompetition.idToIdx.get(compId);
		if (ind < 0) {
			log.warn("no competition found with that id");
			return "{msg:" + ServiceMsg.UNFOUND_ID + "}";
		}
		CCAllStruct ccal = CountryCompetition.ccasList.get(ind);

		WeekMatchHandler wmh = new WeekMatchHandler();

		return wmh.redWeekMatches(compId, ccal.getCompetition(), ccal.getCountry(),LocalDate.now());
	}

	@GET
	@Path("/redcommon/{compid}")
	public String commonAdversariesRed(@PathParam("compid") int compId) throws SQLException {
		/*
		 * Check to see if the static map has the data required
		 */
		if (CommonAdversariesHandler.commonAdv.get(compId) != null) {
			return CommonAdversariesHandler.commonAdv.get(compId);
		} else {
			// search and recalculate
		}
		return "hello";
	}

	// ---------------------------------------NEW-------------
	// --------------------All match page------------------------
	@GET
	@Path("/mpl/{datstamp}/{nr}")
	public String matchPredictionLine(@PathParam("datstamp") String datstamp, @PathParam("nr") int nr)
			throws SQLException {
		// return the mpl of the teams playing on the date-dat in "nr" order in
		// the MPL map
		// log.info("-----------------------------------------\n");
		int allMatchesIn = 0;
		LocalDate ld;
		List<Integer> keyList;
		List<FullMatchLine> list_fml;// = new ArrayList<>();
		List<MPLPack> packlist;
		TestHelp.initAll();
		//
		try {
			datstamp = "2016-10-05";
			ld = LocalDate.parse(datstamp);
		} catch (Exception e) {
			log.info(" received date string was not parsed correctly");
			return msgWriter(ServiceMsg.DATE_ERR_PARSE);
		}
		if (!TimeVariations.mapMPL.keySet().contains(ld)) {
			log.info("no matches @ that date");
			return msgWriter(ServiceMsg.DATE_NO_REC);
		}
		keyList = new ArrayList<>(TimeVariations.mapMPL.get(ld).keySet());
		nr++;// get the next set of matches
		log.info("nr: {}, keys.size:{} ", nr, keyList.size());
		if (nr >= keyList.size()) {
			log.info("msg:' + ServiceMsg.SERI_END ");
			return msgWriter(ServiceMsg.END_OF_DATA);
		}

		packlist = new ArrayList<>();
		do {
			list_fml = new ArrayList<>();
			list_fml.addAll(TimeVariations.mapMPL.get(ld).get(keyList.get(nr)));
			allMatchesIn += list_fml.size();
			log.info("allMatchesIn: {}", allMatchesIn);
			CCAllStruct ccdata = ccalExtract(list_fml.get(0).getComId());
			MPLPack pack = new MPLPack(ccdata.getCountry(), ccdata.getCompetition(), ccdata.getCompId(), nr, list_fml);
			packlist.add(pack);
			nr++;
		} while (allMatchesIn < StandartResponses.MPL_PACK_SIZE && nr < keyList.size());

		log.info("packlist : {}", packlist.size());
		Gson gson = new Gson();
		String jo = gson.toJson(packlist);
		return jo;
	}

	@GET
	@Path("/reducedweeksmatches/{compid}")
	public String reducedWeeksMatches(@PathParam("compid") int compId) throws SQLException {
		/*
		 * get the weekly data for a competition, including
		 * form,atack,score,defence,etc.
		 */
		int ind = CountryCompetition.idToIdx.get(compId);
		if (ind < 0) {
			log.warn("no competition found with that id");
			return msgWriter(ServiceMsg.UNFOUND_ID);
		}
		CCAllStruct ccal = CountryCompetition.ccasList.get(ind);

		WeekMatchHandler wmh = new WeekMatchHandler();
		String csv = wmh.redWeekMatches_TodTom(compId, ccal.getCompetition(), ccal.getCountry());
		int linesRead = wmh.getLinesRead();
		WeekMatchesCSV wmcsv = new WeekMatchesCSV(ccal.getCountry(), ccal.getCompetition(), compId, linesRead, csv);
		Gson gson = new Gson();
		String jo = gson.toJson(wmcsv);
		return jo;
	}

	@GET
	@Path("/onempl/{datstamp}/{cid}")
	public String compWinDrawLose(@PathParam("cid") int cid) {
		// return wdl for all the teams of the competition regardless of who is
		// playing today

		CCAllStruct ccal = ccalExtract(cid);
		if (ccal == null) {
			log.info("no matches @ that Competition Id");
			return msgWriter(ServiceMsg.UNFOUND_ID);
		}
		WinDrawLoseHandler wdlh = new WinDrawLoseHandler();
		try {
			Gson gson = new Gson();
			String jo = gson.toJson(wdlh.windrawloseDbGet(ccal.getCompetition(), ccal.getCountry()));
			return jo;
		} catch (SQLException e) {
			e.printStackTrace();
			return msgWriter(ServiceMsg.RETR_ERROR_DB);
		}
	}

	@GET
	@Path("/onempl/{datstamp}/{cid}")
	public String compDateWinDrawLose(@PathParam("datstamp") String datstamp, @PathParam("cid") int cid) {
		// to return wdl only for the teams playing today

		LocalDate ld;
		List<String> teams = null;
		TestHelp.initAll();
		//
		try {
			datstamp = "2016-10-05";
			ld = LocalDate.parse(datstamp);
		} catch (Exception e) {
			log.info(" received date string was not parsed correctly");
			return msgWriter(ServiceMsg.DATE_ERR_PARSE);
		}
		if (!TimeVariations.mapMPL.keySet().contains(ld)) {
			log.info("no matches @ that date");
			return msgWriter(ServiceMsg.DATE_NO_REC);
		}
		if (!TimeVariations.mapMPL.get(ld).keySet().contains(cid)) {
			log.info("no matches of that competition @ that date");
			return msgWriter(ServiceMsg.DATE_ID_NO_REC);
		}
		CCAllStruct ccal = ccalExtract(cid);
		if (ccal == null) {
			log.info("no matches @ that Competition Id");
			return msgWriter(ServiceMsg.UNFOUND_ID);
		}
		teams = new ArrayList<>();
		for (int i = 0; i < TimeVariations.mapMPL.get(ld).get(cid).size(); i++) {
			teams.add(TimeVariations.mapMPL.get(ld).get(cid).get(i).getT1());
			teams.add(TimeVariations.mapMPL.get(ld).get(cid).get(i).getT2());
		}
		if (teams.size() == 0) {
			log.info("no records found in the mplmap; This is wierd");
			return msgWriter(ServiceMsg.ERROR_ERROR);
		}

		WinDrawLoseHandler wdlh = new WinDrawLoseHandler();
		try {
			Gson gson = new Gson();
			Map<String, String> teamMap = wdlh.windrawloseDbGet(teams, ccal.getCompetition(), ccal.getCountry());
			if (teamMap == null) {
				return msgWriter(ServiceMsg.RETR_ERROR_DB);
			}
			String jo = gson.toJson(teamMap);
			return jo;
		} catch (SQLException e) {
			e.printStackTrace();
			return msgWriter(ServiceMsg.RETR_ERROR_DB);
		}
	}

	// --------------------Competition specific------------------------
	/*
	 * needed for the mpl in competition specific page (a lot of match
	 * prediction lines from the same competition). No date no seri. *** Just
	 * send one pack with all the data
	 */
	@GET
	@Path("/onempl/{datstamp}/{cid}")
	public String oneCompMatchPredictionLine(@PathParam("cid") int cid) {
		/*
		 * needed for the mpl in competition specific page (a lot of match
		 * prediction lines from the same competition). No date no seri. ***
		 * Just send one pack with all the data
		 */
		TestHelp.initAll();
		List<FullMatchLine> list_fml = new ArrayList<>();

		for (LocalDate dat : TimeVariations.mapMPL.keySet()) {
			if (dat.isAfter(LocalDate.now().minusDays(15)))// 2 weeks past
				if (TimeVariations.mapMPL.get(dat).containsKey(cid)) {
					list_fml.addAll(TimeVariations.mapMPL.get(dat).get(cid));
				}
		}
		if (list_fml.size() == 0) {
			log.info("there is no more date in the MPLmap");
			return msgWriter(ServiceMsg.END_OF_DATA);
		}

		CCAllStruct ccal = ccalExtract(cid);
		if (ccal == null) {
			log.info("no matches @ that Competition Id");
			return msgWriter(ServiceMsg.UNFOUND_ID);
		}
		MPLPack pack = new MPLPack(ccal.getCountry(), ccal.getCompetition(), ccal.getCompId(), 0, list_fml);

		Gson gson = new Gson();
		String jo = gson.toJson(pack);
		return jo;
	}

	/* get from db the data needed for the competition data table */
	@GET
	@Path("/compTableData/{cid}")
	public String compTableData(@PathParam("cid") int cid) {
		CCAllStruct ccal = ccalExtract(cid);
		if (ccal == null) {
			log.info("no matches @ that Competition Id");
			return msgWriter(ServiceMsg.UNFOUND_ID);
		}
		CompTableHandler cth = new CompTableHandler();
		try {
			Gson gson = new Gson();
			String jo = gson.toJson(cth.readCompTable(ccal.getCompetition(), ccal.getCountry()));
			log.info("size of: {}", jo.getBytes().length);
			return jo;
		} catch (SQLException e) {
			e.printStackTrace();
			log.info("no matches @ that Competition Id");
			return msgWriter(ServiceMsg.RETR_ERROR_DB);
		}
	}

	// -------------Match Specific Page ---------------------------
	@GET
	@Path("/matchSpecificData/{datstamp}/{compId}/{t1}/{t2}")
	String matchSpecificData(@PathParam("datstamp") String datstamp,@PathParam("t1") String t1, @PathParam("t2") String t2, @PathParam("compId") int compId) {
		/* find all the data needed regarding the two teams in hand */
		LocalDate ld ;
		try {
			datstamp = "2016-10-05";
			ld = LocalDate.parse(datstamp);
		} catch (Exception e) {
			log.info(" received date string was not parsed correctly");
			return msgWriter(ServiceMsg.DATE_ERR_PARSE);
		}
		if (!TimeVariations.mapMPL.keySet().contains(ld)) {
			log.info("no matches @ that date");
			return msgWriter(ServiceMsg.DATE_NO_REC);
		}
		int ind = CountryCompetition.idToIdx.get(compId);
		if (ind < 0) {
			log.warn("no competition found with that id");
			return msgWriter(ServiceMsg.UNFOUND_ID);
		}
		CCAllStruct ccal = CountryCompetition.ccasList.get(ind);
		
		return t1;
	}

	// ------------------------------------END OF NEW---------
	private CCAllStruct ccalExtract(int cid) {
		return new CCAllStruct("Casiopea_" + cid, "TerraMAlgon_" + cid, cid, "link/code/ciu/pp3", 1, -1);
		// int ind = CountryCompetition.idToIdx.get(cid);
		// return CountryCompetition.ccasList.get(ind);
	}

	private String msgWriter(String sub) {
		return ("{msg:'" + sub + "'}");
	}

}