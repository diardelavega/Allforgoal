package api.rest;

import java.sql.SQLException;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import com.google.gson.Gson;

import api.functionality.CompIdToCountryCompCompID;
import api.functionality.MatchPredLineHandler;
import api.functionality.WinDrawLoseHandler;
import api.functionality.obj.CountryCompCompId;
import api.functionality.obj.MatchPredWithWinDrawLose;

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
		CountryCompCompId ccci = new CountryCompCompId("Sweden", "Superettan",
				164);

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
	@Path("/weekmatchesred/{compid}")
	public String weekMatchesRed(@PathParam("compid") int compId)
			throws SQLException {
		/* get the weekly data for a competition, including form,atack,score,defence,etc. */
		CountryCompCompId ccci = new CompIdToCountryCompCompID().search(compId);
		MatchPredLineHandler mph = new MatchPredLineHandler();
		mph.wdlOnly(ccci);
		Gson gson = new Gson();
		ccci.setObj(mph.getMatchPredLine());
		String jo = gson.toJson(ccci);
		return jo;
	}
	
}
