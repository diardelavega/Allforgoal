package api.rest;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import api.functionality.CommonAdversariesHandler;
import api.functionality.CompIdToCountryCompCompID;
import api.functionality.MatchPredLineHandler;
import api.functionality.WeekMatchHandler;
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

		return wmh.redWeekMatches(compId, ccal.getCompetition(), ccal.getCountry());
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

	// ---------------------------------------

	@GET
	@Path("/mpl/{datstamp}/{nr}")
	public String matchPredictionLine(@PathParam("datstamp") String datstamp, @PathParam("nr") int nr)
			throws SQLException {
		log.info("-----------------------------------------\n");
		int allMatchesIn = 0;
		LocalDate ld;
		List<Integer> keyList;
		List<FullMatchLine> list_fml;// = new ArrayList<>();
		List<MPLPack> packlist;
		TestHelp.initAll();
		//
		// log.info("flag:{}",flag);
		// log.info("allMatchesIn: {}", allMatchesIn);
		// if (!flag) {
		// // init MPL map
		// MPLFill mplfill = new MPLFill();
		// mplfill.fakeFiller();
		// log.info("fillin MPL");
		// flag=true;
		// }
		// log.info("flag:{}",flag);
		// log.info("allMatchesIn: {}", allMatchesIn);
		//
		try {
			datstamp = "2016-10-05";
			ld = LocalDate.parse(datstamp);
		} catch (Exception e) {
			log.info(" received date string was not parsed correctly");
			return ("{msg:'" + ServiceMsg.DATE_ERR_PARSE + "'}");
		}
		if (TimeVariations.mapMPL.get(ld) == null) {
			log.info("no matches @ that date");
			return ("{msg:'" + ServiceMsg.DATE_NO_REC + "'}");
		}
		keyList = new ArrayList<>(TimeVariations.mapMPL.get(ld).keySet());
		nr++;// get the next set of matches
		log.info("nr: {}, keys.size:{} ", nr, keyList.size());
		if (nr >= keyList.size()) {
			log.info("msg:' + ServiceMsg.SERI_END ");
			return ("{msg:'" + ServiceMsg.SERI_END + "'}");
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
			return "{msg:" + ServiceMsg.UNFOUND_ID + "}";
		}
		CCAllStruct ccal = CountryCompetition.ccasList.get(ind);

		WeekMatchHandler wmh = new WeekMatchHandler();
		String csv = wmh.redWeekMatches(compId, ccal.getCompetition(), ccal.getCountry());
		int linesRead = wmh.getLinesRead();
		WeekMatchesCSV wmcsv = new WeekMatchesCSV(ccal.getCountry(), ccal.getCompetition(), compId, linesRead, csv);
		Gson gson = new Gson();
		String jo = gson.toJson(wmcsv);
		return jo;
	}

	private CCAllStruct ccalExtract(int cid) {
		return new CCAllStruct("Casiopea_" + cid, "TerraMAlgon_" + cid, cid, "link/code/ciu/pp3", 1, -1);
		// int ind = CountryCompetition.idToIdx.get(cid);
		// return CountryCompetition.ccasList.get(ind);
	}
}