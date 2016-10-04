package api.rest;

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
import com.google.gson.JsonObject;

import api.functionality.obj.MPLPack;
import basicStruct.CCAllStruct;
import basicStruct.FullMatchLine;
import extra.ServiceMsg;
import structures.CountryCompetition;
import structures.TimeVariations;

public class ServTest {
	public static final Logger log = LoggerFactory.getLogger(ServTest.class);

	public static void main(String[] args) throws SQLException {
		// TODO Auto-generated method stub
		
		String url="http://www.bari91.com/previews/2016-10-04";
		Document doc = null;
		try {
			log.info("getting page : {}", url);
			// doc = Jsoup.parse(new File(
			// "C:/Users/Administrator/Desktop/skedina/bari91_1.html"),
			// "UTF-8");

			doc = Jsoup
					.connect(url)
					.userAgent(
							"Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.101 Safari/537.36 OPR/40.0.2308.62")
					.maxBodySize(0).timeout(600000).get();
			log.info("Page aquired");
		} catch (Exception e) {
			e.printStackTrace();
//			errorStatus = "Faulty Connection";
//			logger.warn("---------:Connection not possible  {}", errorStatus);
//			return;
		}
		log.info("{}",doc);
		
//		try {
//			URL oracle = new URL("http://www.bari91.com/previews/2016-10-04");
////			BufferedReader in = new BufferedReader(new InputStreamReader(
////					oracle.openStream()));
//			
//			 URLConnection yc = oracle.openConnection();
//		        BufferedReader in = new BufferedReader(new InputStreamReader(
//		                                    yc.getInputStream()));
//
//			String inputLine;
//			while ((inputLine = in.readLine()) != null)
//				System.out.println(inputLine);
//			in.close();
//
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		// TimeVariations tv = new TimeVariations();
		// tv.initMPL();
		// log.info("{}",matchPredictionLine("2016-10-03",0));

		// log.info("{}", InetAddress.getLocalHost().getHostName());
		// log.info("--{}", System.getenv("COMPUTERNAME"));
		// System.out.println(Runtime.getRuntime().exec("hostname"));
	}

	public static String matchPredictionLine(
			@PathParam("datstamp") String datstamp, @PathParam("nr") int nr)
			throws SQLException {
		LocalDate ld;
		try {
			ld = LocalDate.parse(datstamp);
		} catch (Exception e) {
			log.info(" received date string was not parsed correctly");
			e.printStackTrace();

			return ("{msg:'" + ServiceMsg.DATE_ERR_PARSE + "'}");
			// {msg:'DATE NO RECORDS'}
		}

		if (TimeVariations.mapMPL.get(ld) == null) {
			log.info("no matches @ that date");
			// Gson gson = new Gson();
			// String jo = gson.toJson(ServiceMsg.DATE_ERR_PARSE);
			// return jo;
			return ("{msg:'" + ServiceMsg.DATE_NO_REC + "'}");
		}
		List<Integer> keyList = new ArrayList<>(TimeVariations.mapMPL.get(ld)
				.keySet());
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
			MPLPack pack = new MPLPack(ccdata.getCountry(),
					ccdata.getCompetition(), ccdata.getCompId(), nr, list_fml);
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
