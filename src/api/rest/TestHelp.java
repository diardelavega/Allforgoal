package api.rest;

import java.io.IOException;
import java.sql.SQLException;

import demo.Demo;

public class TestHelp {
	public static boolean initFlag = false;

	public static void initAll() {

//		log.info("flag:{}",flag);
//		 log.info("allMatchesIn: {}", allMatchesIn);
		if (!initFlag) {
			// init MPL map
			try {
				Demo.initCCAllStruct();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			MPLFill mplfill = new MPLFill();
			mplfill.fakeFiller();
//			log.info("fillin MPL");
			initFlag=true;
		}
//		log.info("flag:{}",flag);
//		 log.info("allMatchesIn: {}", allMatchesIn);
		
	}
}
