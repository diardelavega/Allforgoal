package dbtry;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dbhandler.MatchHandler;
import dbhandler.MatchObj;

public class MatchQueries {

	Timestamp tst = new Timestamp(new Date().getTime());
	MatchObj mobj = new MatchObj (-1,1,"Juventus F.C.","Manchester City F.C.",1,0,1,0,2.10,3.20,3.0,2.0,1.7,tst);
	MatchObj mobj2 = new MatchObj (-1,1,"Shaktar Doneck F.C.","Real Madrid F.C.",3,4,0,1,2.10,3.20,1.8,1.6,1.7,tst);
	List<MatchObj> mlist = new ArrayList<>();
	mlist.add(mobj);
	
	MatchHandler mh = new MatchHandler ();
	
	//borusia munchengladbah
	
}
