package calculate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import basicStruct.MatchObj;

/**
 * @author diego
 *
 *         a class that handles the periodic routine of scraping, comparing,
 *         storing, re-evaluating, etc. that happens periodically for the
 *         specific matches.
 */
public class RutineExecution {

	private LocalDate curentDat = LocalDate.now();
	private LocalDate latestDat = null;

	private List<MatchObj> tempMatchList = new ArrayList<>();
	private List<MatchObj> errorMatchList = new ArrayList<>();

	public void rutine() {
		if (latestDat == null) {// first time
			/*
			 * TODO get todays matches & get tomorrows matches & store them to
			 * the db table tempMatches;
			 */
			latestDat = curentDat;
		} else {
			/*
			 * TODO 1)get from db tempMatches where dat=latestDat; **********
			 * 2)scrap laatestDatMatches form scorer; ************************
			 * 3) if(Fin)->{ add results, recalculate & evaluate & update etc}
			 * if(shed)->{match is to be played latter}; if(canceled ||
			 * postponed)->{ remove from temp & add to error list (and maybe
			 * append to file error file)} ********************************* ***
			 * 4) if(curentDat > latestDat)->{get tomorrow (curent+1) matches;
			 * if(latestDat Matches.size()==0){ latestDat=curentDat;}}
			 */ }
	}

	public List<MatchObj> getTempMatchList() {
		return tempMatchList;
	}

	public void setTempMatchList(List<MatchObj> tempMatchList) {
		this.tempMatchList = tempMatchList;
	}

	public List<MatchObj> getErrorMatchList() {
		return errorMatchList;
	}

	public void setErrorMatchList(List<MatchObj> errorMatchList) {
		this.errorMatchList = errorMatchList;
	}

	public LocalDate getLatestDat() {
		return latestDat;
	}

	
	
}
