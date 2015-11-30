package scrap;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

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

	public void m2(String ajax) throws IOException {
		// JsonReader reader = new JsonReader(
		// new InputStreamReader(new FileInputStream(
		// "C:/Users/Administrator/Desktop/tjson.json")));

		URL url = new URL(ajax);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Accept", "application/json");

		if (conn.getResponseCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : "
					+ conn.getResponseCode());
		}

		JsonParser jp = new JsonParser();

		JsonObject jobj = (JsonObject) jp
				.parse(new InputStreamReader(conn.getInputStream(), "UTF-8"))
//		System.out.println(jobj);
				.getAsJsonObject().get("rows").getAsJsonObject().get("2.5")
				.getAsJsonObject().get("bm").getAsJsonObject();

		float over = jobj.get("1").getAsJsonObject().get("Over")
				.getAsJsonObject().get("odds").getAsFloat();
		float under = jobj.get("1").getAsJsonObject().get("Under")
				.getAsJsonObject().get("odds").getAsFloat();

		System.out.println(over + " " + under);

//		reader.close();
	}

	public void headResults(String ajaxUrl) throws IOException {
		// URL url = new URL(ajaxUrl);
		// HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		// conn.setRequestMethod("GET");
		// conn.setRequestProperty("Accept", "application/json");
		//
		// if (conn.getResponseCode() != 200) {
		// throw new RuntimeException("Failed : HTTP error code : "
		// + conn.getResponseCode());
		// }

		// JsonReader reader = new JsonReader(new InputStreamReader(
		// conn.getInputStream(), "UTF-8"));

		JsonReader reader = new JsonReader(
				new InputStreamReader(new FileInputStream(
						"C:/Users/Administrator/Desktop/tjson.json")));
		JsonParser jp = new JsonParser();

		JsonElement jobj = jp.parse(reader);

		reader.beginObject();
		String token;
		while (reader.hasNext()) {
			if (reader.nextName().equals("type")) {
				System.out.println("i know its 47 so ...  " + reader.nextInt());
			}
			if (reader.nextName().equals("rows")) {
				reader.beginObject();
				while (reader.hasNext())

					System.out.println("AAAAAAA " + reader.peek());
				// while (reader.hasNext()) {
				// // reader.beginObject();
				// if (reader.nextName().equals("2.5")) {
				// reader.beginObject();
				// if (reader.nextName().equals("bm")) {
				// reader.beginObject();
				// while (reader.hasNext()) {
				// if (reader.nextName().equals("1")) {
				// System.out.println("--  "
				// + reader.nextName());
				// } else if (reader.nextName().equals("2")) {
				// System.out.println("--  "
				// + reader.nextName());
				// } else if (reader.nextName().equals("3")) {
				// System.out.println("--  "
				// + reader.nextName());
				// }
				// }//
				// reader.endObject();
				// }// if bm
				// reader.endObject();
				// }// if 2.5
				// }// while
				reader.endObject();
			}// if
				// }// else
		}
		reader.endObject();

		// BufferedReader br = new BufferedReader(isr);

		// String inputLine;
		// while ((inputLine = br.readLine()) != null)
		// System.out.println(inputLine);
		// br.close();
		// isr.close();

		// TypeToken<List<MyJsonClass>> token = new
		// TypeToken<List<MyJsonClass>>(){};
		// List<MyJsonClass> list = new Gson().fromJson(isr, token.getType());

		// for (MyJsonClass mjc : list)
		// {
		// System.out.println(mjc.symbol + " : " + mjc.latest_trade);
		// }
		// System.out.println(jdoc);
	}
}
