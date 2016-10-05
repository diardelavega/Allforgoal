package api.rest;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.PathParam;

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

import api.functionality.obj.MPLPack;
import basicStruct.CCAllStruct;
import basicStruct.FullMatchLine;
import demo.Demo;
import extra.ServiceMsg;
import structures.CountryCompetition;
import structures.TimeVariations;

public class ServTest {
	public static final Logger log = LoggerFactory.getLogger(ServTest.class);

	public static void main(String[] args) throws SQLException {
		// TODO Auto-generated method stub
//		TimeVariations tv = new TimeVariations();
//		tv.initMPL();
//	log.info("{}",matchPredictionLine("2016-10-03",0));
		try {
			Demo.initCCAllStruct();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// TimeVariations tv = new TimeVariations();
		// tv.initMPL();
		// log.info("{}",matchPredictionLine("2016-10-03",0));
//		try {
//			Demo.initCCAllStruct();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		MPLFill mplfill = new MPLFill();
		mplfill.fakeFiller();
		int seri = -1;
		matchPredictionLine("2016-10-04", seri);

		// Gson gson = new GsonBuilder().setPrettyPrinting().create();
		// JsonParser jp = new JsonParser();
		//
		// // String ret = "{msg:'" + ServiceMsg.DATE_ERR_PARSE + "'}";
		// int seri=-1;
		// String ret = matchPredictionLine("2016-10-04", seri);
		// JsonObject jo ;
		// JsonArray jsonArray = null;
		// try {
		// jsonArray =jp.parse(ret).getAsJsonArray();
		// } catch (JsonSyntaxException e) {
		// e.printStackTrace();
		// jo = jp.parse(ret).getAsJsonObject();
		// }
		// log.info("-------------{}", gson.toJson(jsonArray));
		// String pj;
		// while ( !ret.substring(0, 6).contains("msg")) {
		// seri++;
		// ret = matchPredictionLine("2016-10-04", seri);
		// pj = gson.toJson(ret);
		// log.info(".......{}", pj);
		// }
		// log.info("-------------{}", ret);
		// pj = gson.toJson(ret);
		// log.info("{}", pj);

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
			return ("{msg:'" + ServiceMsg.SERI_END + "'}");
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

		Gson gson = new Gson();
		String jo = gson.toJson(packlist);
		return jo;
	}

	private static CCAllStruct ccalExtract(int cid) {
		// cid+10 for practical purposes not real ones
		// int ind = CountryCompetition.idToIdx.get(cid + 10);
		// return CountryCompetition.ccasList.get(ind);
		// CCAllStruct ccdata =
		return new CCAllStruct("Casiopea_" + cid, "TerraMAlgon_" + cid, cid, "link/code/ciu/pp3", 1, -1);

	}
}
