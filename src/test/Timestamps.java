package test;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.Date;

public class Timestamps {

	public void ts(String date) throws ParseException {

		// Timestamp ts = Timestamp.valueOf(date);
		// System.out.println(ts);

		SimpleDateFormat datetimeFormatter1 = new SimpleDateFormat("dd/MM/yyyy");
		Date lFromDate1 = new Date(datetimeFormatter1.parse(date).getTime());
		System.out.println("gpsdate :" + lFromDate1);
		Timestamp fromTS1 = new Timestamp(lFromDate1.getTime());
		System.out.println(fromTS1);
	}

	public void spliter(String url) {
		url = url.split("_id=|&home")[1];
		System.out.println(url);

	}
}
