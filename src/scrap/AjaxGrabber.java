package scrap;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

/**
 * @author Administrator
 *
 *         This class will simulate the ajax call that the browser simulates to
 *         get the odds json data that is dynamically returned
 */

public class AjaxGrabber {

	static final Logger logger = LoggerFactory.getLogger(AjaxGrabber.class);
	private float over = 0;
	private float under = 0;
	private float _1 = 0;
	private float _2 = 0;
	private float _x = 0;
	private boolean flag = true;
	public String errorStatus = "OK";

	private final String ajaxUrl = "http://www.soccerpunter.com/livesoccerodds_ajx.php?match_id=";

	// 2086384&typeId=47";

	public boolean f47(String mid) throws IOException {
		/* this function call the o/u data link and collects the data */
		/**
		 * *** READ FROM FILE EXAMPLE ** * JsonReader reader = new JsonReader(
		 * new InputStreamReader(new FileInputStream(
		 * "C:/Users/Administrator/Desktop/tjson.json")));
		 * "http://www.soccerpunter.com/livesoccerodds_ajx.php?match_id=2086384&t
		 * y p e I d = 4 7
		 */
		URL url = new URL(ajaxUrl + mid + "&typeId=47");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Accept", "application/json");
		if (conn.getResponseCode() != 200) {
			errorStatus = "Faulty connection 47";
			throw new RuntimeException("Failed : HTTP error code : "
					+ conn.getResponseCode());
		}

		JsonParser jp = new JsonParser();
		// traverse json until 2.5 object
		JsonObject jobj = (JsonObject) jp
				.parse(new InputStreamReader(conn.getInputStream(), "UTF-8"))
				.getAsJsonObject().get("rows").getAsJsonObject().get("2.5")
				.getAsJsonObject().get("bm").getAsJsonObject();

		if (jobj == null) {
			// some matches don't have odds and they lack the json attributes
			logger.info("No odds for this match");
			errorStatus = "Not Found Json Object";
			flag = false;
			return flag;
		}

		try {// controls in case the values we require are missing
			int i = 0;
			if (jobj.has("1")) {// bookie 1
				over += jobj.get("1").getAsJsonObject().get("Over")
						.getAsJsonObject().get("odds").getAsFloat();
				under += jobj.get("1").getAsJsonObject().get("Under")
						.getAsJsonObject().get("odds").getAsFloat();
				i++;
			}
			if (jobj.has("2")) {// bookie 2
				over += jobj.get("2").getAsJsonObject().get("Over")
						.getAsJsonObject().get("odds").getAsFloat();
				under += jobj.get("2").getAsJsonObject().get("Under")
						.getAsJsonObject().get("odds").getAsFloat();
				i++;
			}
			if (jobj.has("3")) {// bookie 3
				over += jobj.get("3").getAsJsonObject().get("Over")
						.getAsJsonObject().get("odds").getAsFloat();
				under += jobj.get("3").getAsJsonObject().get("Under")
						.getAsJsonObject().get("odds").getAsFloat();
				i++;
			}
			// as value we get the average of the three bookies
			over /= i;
			under /= i;
		} catch (Exception e) {
			logger.info("No odds for this match");
			flag = false;
			errorStatus = "unconsistent odds";
			return flag;
		}

		logger.info("under is ={},  over is ={}", under, over);

		return flag;
	}

	public boolean f69(String mid) throws IOException {
		/* read the 1x2 odds from the json data */
		URL url = new URL(ajaxUrl + mid + "&typeId=69");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Accept", "application/json");
		if (conn.getResponseCode() != 200) {
			errorStatus = "Faulty connection 69";
			throw new RuntimeException("Failed : HTTP error code : "
					+ conn.getResponseCode());
		}

		JsonParser jp = new JsonParser();
		// traverse json until 1x2 results object
		JsonObject jobj = (JsonObject) jp
				.parse(new InputStreamReader(conn.getInputStream(), "UTF-8"))
				.getAsJsonObject().get("rows").getAsJsonObject().get("0")
				.getAsJsonObject().get("bm").getAsJsonObject();
		if (jobj == null) {
			// some matches don't have odds and they lack the json attributes
			logger.info("No odds for this match");
			flag = false;
			errorStatus = "Not found Json Object";
			return flag;
		}

		try {
			int i = 0;

			if (jobj.has("1")) {
				_1 = jobj.get("1").getAsJsonObject().get("1").getAsJsonObject()
						.get("odds").getAsFloat();
				_2 = jobj.get("1").getAsJsonObject().get("2").getAsJsonObject()
						.get("odds").getAsFloat();
				_x = jobj.get("1").getAsJsonObject().get("X").getAsJsonObject()
						.get("odds").getAsFloat();
				i++;
			}
			if (jobj.has("2")) {
				_1 += jobj.get("2").getAsJsonObject().get("1")
						.getAsJsonObject().get("odds").getAsFloat();
				_2 += jobj.get("2").getAsJsonObject().get("2")
						.getAsJsonObject().get("odds").getAsFloat();
				_x += jobj.get("2").getAsJsonObject().get("X")
						.getAsJsonObject().get("odds").getAsFloat();
				i++;
			}
			if (jobj.has("3")) {
				_1 += jobj.get("3").getAsJsonObject().get("1")
						.getAsJsonObject().get("odds").getAsFloat();
				_2 += jobj.get("3").getAsJsonObject().get("2")
						.getAsJsonObject().get("odds").getAsFloat();
				_x += jobj.get("3").getAsJsonObject().get("X")
						.getAsJsonObject().get("odds").getAsFloat();
				i++;
			}
			// as value we get the average of the three bookies
			_1 /= i;
			_x /= i;
			_2 /= i;

			logger.info("1 is = {}, x is ={}, 2 is ={} ", _1, _x, _2);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("No odds for this match");
			flag = false;
			errorStatus = "unconsistent odds";
			return flag;
		}

		return false;
	}

	public float getOver() {
		return over;
	}

	public float getUnder() {
		return under;
	}

	public float get_1() {
		return _1;
	}

	public float get_2() {
		return _2;
	}

	public float get_x() {
		return _x;
	}

}
