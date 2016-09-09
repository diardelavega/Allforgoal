package r_dataIO;

import java.io.IOException;
import java.sql.SQLException;

import demo.Demo;

public class RDemo {

	public static void main(String[] args) throws SQLException, IOException {
		Demo.initCCAllStruct();
		RHandler rh = new RHandler();
//		rh.testRcall();
		
		rh.predictOne(157);

	}

}
