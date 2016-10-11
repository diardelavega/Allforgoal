package api.rest;

public class TestHelp {
	public static boolean initFlag = false;

	public static void initAll() {

//		log.info("flag:{}",flag);
//		 log.info("allMatchesIn: {}", allMatchesIn);
		if (!initFlag) {
			// init MPL map
			MPLFill mplfill = new MPLFill();
			mplfill.fakeFiller();
//			log.info("fillin MPL");
			initFlag=true;
		}
//		log.info("flag:{}",flag);
//		 log.info("allMatchesIn: {}", allMatchesIn);
		
	}
}
