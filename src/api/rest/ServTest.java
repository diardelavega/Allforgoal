package api.rest;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.PathParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import api.functionality.obj.MPLPack;
import basicStruct.CCAllStruct;
import basicStruct.FullMatchLine;
import demo.Demo;
import extra.ServiceMsg;
import structures.CountryCompetition;
import structures.TimeVariations;

public class ServTest {
	public static final Logger log = LoggerFactory
			.getLogger(ServTest.class);
	public static void main(String[] args) throws SQLException {
		// TODO Auto-generated method stub
//		TimeVariations tv = new TimeVariations();
//		tv.initMPL();
//	log.info("{}",matchPredictionLine("2016-10-03",0));
		try {
			Demo.initCCAllStruct();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		MPLFill mplfill = new MPLFill();
		mplfill.fakeFiller();
//		mplfill.spiller();
//		mplfill .ranGen();
//		mplfill .rntest();
		log.info(matchPredictionLine("2016-10-04",0));
	}

	
	public static String matchPredictionLine(@PathParam("datstamp") String datstamp, @PathParam("nr") int nr)
			throws SQLException {
		LocalDate ld;
		try {
			ld = LocalDate.parse(datstamp);
		} catch (Exception e) {
			log.info(" received date string was not parsed correctly");
			e.printStackTrace();
			
			return ("{msg:'" + ServiceMsg.DATE_ERR_PARSE + "'}");
//			{msg:'DATE NO RECORDS'}
		}

		if(TimeVariations.mapMPL.get(ld)==null){
			log.info("no matches @ that date");
//			Gson gson = new Gson();
//			String jo = gson.toJson(ServiceMsg.DATE_ERR_PARSE);
//			return jo;
			return ("{msg:'" + ServiceMsg.DATE_NO_REC+ "'}");
		}
		List<Integer> keyList = new ArrayList<>(TimeVariations.mapMPL.get(ld).keySet());
		nr++;// get the next set of matches
		if (nr >= keyList.size()) {
			return ("{msg:'" + ServiceMsg.SERI_END + "'}");
		}

		List<FullMatchLine> list_fml;// = new ArrayList<>();
		List<MPLPack> packlist = new ArrayList<>();
		do {
			list_fml = new ArrayList<>();
			list_fml.addAll(TimeVariations.mapMPL.get(ld).get(keyList.get(nr)));
			CCAllStruct ccdata = ccalExtract(list_fml.get(0).getComId());
			MPLPack pack = new MPLPack(ccdata.getCountry(), ccdata.getCompetition(), ccdata.getCompId(), nr, list_fml);
			packlist.add(pack);
			nr++;
		} while (list_fml.size() < 10 || nr < keyList.size());

		Gson gson = new Gson();
		String jo = gson.toJson(packlist);
		return jo;
	}
	
	private static CCAllStruct ccalExtract(int cid) {
		int ind = CountryCompetition.idToIdx.get(cid);
		return CountryCompetition.ccasList.get(ind);
	}
}
