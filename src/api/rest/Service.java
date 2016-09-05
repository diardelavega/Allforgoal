package api.rest;

import java.sql.SQLException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import api.functionality.CommonAdversariesHandler;
import api.functionality.CompIdToCountryCompCompID;
import api.functionality.MatchPredLineHandler;
import api.functionality.WeekMatchHandler;
import api.functionality.obj.CountryCompCompId;

import com.google.gson.Gson;

/**
 * @author Administrator
 *
 *         RESTFull API service
 */

@Path("/services")
public class Service {

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
//				new CountryCompCompId("Sweden", "Superettan",
//				164);

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
	public String competitionMatchPredLineWithWDL(
			@PathParam("compid") int compId) throws SQLException {
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
	public String competitionWDL(@PathParam("compid") int compId)
			throws SQLException {
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
	public String weekMatchesRed(@PathParam("compid") int compId)
			throws SQLException {
		/*
		 * get the weekly data for a competition, including
		 * form,atack,score,defence,etc.
		 */
		WeekMatchHandler wmh = new WeekMatchHandler();
		// Gson gson = new Gson();
		return wmh.redWeekMatches(compId);
	}

	@GET
	@Path("/redcommon/{compid}")
	public String commonAdversariesRed(@PathParam("compid") int compId)
			throws SQLException {
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

}
