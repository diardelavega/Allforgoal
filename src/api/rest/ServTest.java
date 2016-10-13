package api.rest;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import api.functionality.CompTableHandler;
import api.functionality.MatchSpecificHandler;
import api.functionality.WeekMatchHandler;
import api.functionality.WinDrawLoseHandler;
import api.functionality.obj.MPLPack;
import api.functionality.obj.MatchSpecificObj;
import api.functionality.obj.WeekMatchesCSV;
import basicStruct.CCAllStruct;
import basicStruct.FullMatchLine;
import demo.Demo;
import extra.ServiceMsg;
import extra.StandartResponses;
import structures.CountryCompetition;
import structures.TimeVariations;

public class ServTest {
	public static final Logger log = LoggerFactory.getLogger(ServTest.class);

	public static void main(String[] args) throws SQLException, IOException {
		// TODO Auto-generated method stub
		// TimeVariations tv = new TimeVariations();
		// tv.initMPL();
		// log.info("{}",matchPredictionLine("2016-10-03",0));
		try {
			Demo.initCCAllStruct();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// TimeVariations tv = new TimeVariations();
		// tv.initMPL();
		// log.info("{}",matchPredictionLine("2016-10-03",0));
		// try {
		// Demo.initCCAllStruct();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		TestHelp.initAll();
//		MPLFill mplfill = new MPLFill();
//		mplfill.fakeFiller();
		int seri = -1;
		// matchPredictionLine("2016-10-12", seri);//OK

//		String ret = reducedWeeksMatches(112);//OK
		
//		compWinDrawLose(112);
//		oneCompMatchPredictionLine(4);
//		compTableData(112);
		matchSpecificData("2016-10-13","Sarpsborg 08","Start",112	);

		// String ret = matchPredictionLine("2016-10-12", seri);
		// log.info("{}",ret);

		// log.info(matchPredictionLine("2016-10-04", -1));

	}

	public static String matchPredictionLine(@PathParam("datstamp") String datstamp, @PathParam("nr") int nr)
			throws SQLException {
		LocalDate ld;
		try {
			ld = LocalDate.parse(datstamp);
		} catch (Exception e) {
			log.info(" received date string was not parsed correctly");
			return ("{msg:'" + ServiceMsg.DATE_ERR_PARSE + "'}");
		}
		if (TimeVariations.mapMPL.get(ld) == null) {
			log.info("no matches @ that date");
			return ("{msg:'" + ServiceMsg.DATE_NO_REC + "'}");
		}
		List<Integer> keyList = new ArrayList<>(TimeVariations.mapMPL.get(ld).keySet());
		nr++;// get the next set of matches
		if (nr >= keyList.size()) {
			return ("{msg:'" + ServiceMsg.END_OF_DATA + "'}");
		}

		List<FullMatchLine> list_fml;// = new ArrayList<>();
		List<MPLPack> packlist = new ArrayList<>();
		int allMatchesIn = 0;
		do {
			list_fml = new ArrayList<>();
			list_fml.addAll(TimeVariations.mapMPL.get(ld).get(keyList.get(nr)));
			allMatchesIn += list_fml.size();
			// log.info("{}", allMatchesIn);
			CCAllStruct ccdata = ccalExtract(list_fml.get(0).getComId());
			MPLPack pack = new MPLPack(ccdata.getCountry(), ccdata.getCompetition(), ccdata.getCompId(), nr, list_fml);
			packlist.add(pack);
			nr++;
		} while (allMatchesIn < 10 && nr < keyList.size());

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		// new Gson();
		String jo = gson.toJson(packlist);
		return jo;
	}

	public static String reducedWeeksMatches(@PathParam("compid") int compId) throws SQLException {
		/*
		 * get the weekly data for a competition, including
		 * form,atack,score,defence,etc.
		 */
		// int ind =compId;
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
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String jo = gson.toJson(wmcsv);

		// ----------------------------------------- Test outcome
		JsonParser jp = new JsonParser();
		JsonObject jjo = (JsonObject) jp.parse(jo);
		log.info("{}", jjo.get("csvTxt"));
		CSVFormat format = CSVFormat.RFC4180.withIgnoreHeaderCase();
		Reader red = new StringReader(jjo.get("csvTxt").getAsString());
		CSVParser parser = null;
		try {
			parser = new CSVParser(red, format);
		} catch (IOException e) {
			e.printStackTrace();
		}
		// (jjo.get("csvTxt"), format);
		for (CSVRecord record : parser) {
			log.info("{}", record.get(1));
			log.info("{} \n", record.get(2));

		}
		// -------------------------------

		return jo;
	}

	
	/**
	 * return wdl for all the teams of the competition regardless of who is
	 * playing today
	 */
	@GET
	@Path("/onempl/{datstamp}/{cid}")
	public static String compWinDrawLose(@PathParam("cid") int cid) {
		// return wdl for all the teams of the competition regardless of who is
		// playing today

		CCAllStruct ccal = ccalExtract(cid);
		if (ccal == null) {
			log.info("no matches @ that Competition Id");
			return msgWriter(ServiceMsg.UNFOUND_ID);
		}
		WinDrawLoseHandler wdlh = new WinDrawLoseHandler();
		try {
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String jo = gson.toJson(wdlh.windrawloseDbGet(ccal.getCompetition(), ccal.getCountry()));
			
			//---------------------------------
			JsonParser jp = new JsonParser();
			JsonObject jjo = (JsonObject) jp.parse(jo);
			log.info("{}", jjo.get("csvTxt"));
			//------------------------------------
			return jo;
		} catch (SQLException e) {
			e.printStackTrace();
			return msgWriter(ServiceMsg.RETR_ERROR_DB);
		}
	}
	
	
	/**
	 * needed for the mpl in competition specific page (a lot of match
	 * prediction lines from the same competition). No date no seri. *** Just
	 * send one pack with all the data
	 */
	@GET
	@Path("/onempl/{datstamp}/{cid}")
	public static String oneCompMatchPredictionLine(@PathParam("cid") int cid) {
		/*
		 * needed for the mpl in competition specific page (a lot of match
		 * prediction lines from the same competition). No date no seri. ***
		 * Just send one pack with all the data
		 */
		// TestHelp.initAll();
		/*
		 * Will return all the matches available for this specific competition
		 * since "STD_DAYS_AGO" and untill how many matches we have recorded in
		 * the future.(curently one day in the future)
		 */
		List<FullMatchLine> list_fml = new ArrayList<>();
		for (LocalDate dat : TimeVariations.mapMPL.keySet()) {
			if (dat.isAfter(LocalDate.now().minusDays(StandartResponses.STD_DAYS_AGO)))
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

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String jo = gson.toJson(pack);
		
		//---------------------------------
//		JsonParser jp = new JsonParser();
//		JsonObject jjo = (JsonObject) jp.parse(jo);
		log.info("{}", jo);
		//------------------------------------
		
		return jo;
	}

	
	/** get from db the data needed for the competition data table */
	@GET
	@Path("/compTableData/{cid}")
	public static String compTableData(@PathParam("cid") int cid) {
		CCAllStruct ccal = ccalExtract(cid);
		if (ccal == null) {
			log.info("no matches @ that Competition Id");
			return msgWriter(ServiceMsg.UNFOUND_ID);
		}
		CompTableHandler cth = new CompTableHandler();
		try {
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String jo = gson.toJson(cth.readCompTable(ccal.getCompetition(), ccal.getCountry()));
			log.info("size of: {}", jo.getBytes().length);
			log.info("{}", jo);
			return jo;
		} catch (SQLException e) {
			e.printStackTrace();
			log.info("no matches @ that Competition Id");
			return msgWriter(ServiceMsg.RETR_ERROR_DB);
		}
	}

	
	/**
	 * find all the data needed regarding the two teams in hand, to dysplay them
	 * in the single match specific page
	 */
	@GET
	@Path("/matchSpecificData/{datstamp}/{compId}/{t1}/{t2}")
	public static String matchSpecificData(@PathParam("datstamp") String datstamp, @PathParam("t1") String t1,
			@PathParam("t2") String t2, @PathParam("compId") int compId) {
		/* find all the data needed regarding the two teams in hand */
		LocalDate ld;
		try {
//			datstamp = "2016-10-05";
			ld = LocalDate.parse(datstamp);
		} catch (Exception e) {
			log.info(" received date string was not parsed correctly");
			return msgWriter(ServiceMsg.DATE_ERR_PARSE);
		}
		if (!TimeVariations.mapMPL.containsKey(ld)) {
			log.info("no matches @ that date");
			return msgWriter(ServiceMsg.DATE_NO_REC);
		}
		if (!TimeVariations.mapMPL.get(ld).containsKey(compId)) {
			log.info("no matches @ that date");
			return msgWriter(ServiceMsg.DATE_ID_NO_REC);
		}
		int ind = CountryCompetition.idToIdx.get(compId);
		if (ind < 0) {
			log.warn("no competition found with that id");
			return msgWriter(ServiceMsg.UNFOUND_ID);
		}
		CCAllStruct ccal = CountryCompetition.ccasList.get(ind);
		boolean teamflag = false;
		int idx = -1;
		for (int i = 0; i < TimeVariations.mapMPL.get(ld).get(compId).size(); i++) {
			if (TimeVariations.mapMPL.get(ld).get(compId).get(i).getT1().equals(t1)
					&& TimeVariations.mapMPL.get(ld).get(compId).get(i).getT2().equals(t2)) {
				idx = i;
				teamflag = true;
				break;
			}
		}
		if (!teamflag) {
			log.warn("no teams were not found in the map");
			return msgWriter(ServiceMsg.UNFOUND_ID);
		}
		MatchSpecificHandler msh = new MatchSpecificHandler();
		MatchSpecificObj singleMatchData = msh.getweekSpecificData(idx, ccal, ld, t1, t2);
		if (singleMatchData == null) {
			log.warn("CSV recuperation problems");
			return msgWriter(ServiceMsg.RETR_ERROR_CSV);
		}
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String jo = gson.toJson(singleMatchData);
		log.info("size of: {}", jo.getBytes().length);
		log.info("{}", jo);

		return jo;
	}

	
	
	private static String msgWriter(String sub) {
		return ("{msg:'" + sub + "'}");
	}

	private static CCAllStruct ccalExtract(int cid) {
		// cid+10 for practical purposes not real ones
		 int ind = CountryCompetition.idToIdx.get(cid );
		 return CountryCompetition.ccasList.get(ind);
		// CCAllStruct ccdata =
//		return new CCAllStruct("Casiopea_" + cid, "TerraMAlgon_" + cid, cid, "link/code/ciu/pp3", 1, -1);

	}
}
