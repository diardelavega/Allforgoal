package calculate;

import java.util.ArrayList;
import java.util.List;

import basicStruct.MatchObj;

/**
 * @author diego
 *
 *         a class that specifies the general approach with tempMatches. get new
 *         matches, store them, update their results,restorethem in the normal
 *         matches table. create test files for prediction(arff & csv),
 *         calculate form, teamtable classification, update the prediction file
 *         data with the new data
 *
 */
public class TempMatchHandeling {

	
	private List<MatchObj> tempMatches= new ArrayList<>();
	
	public void getTempMatchesFromDb(){
		//TODO connect to db and read all matches from 
	}
	
}
