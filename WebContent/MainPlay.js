//organise the functions and actions that are needed for each match line tr
// the idea is: on click everything is calculated and reported, some of the graphs will be hidden to give the efect.
// eill be eliminated draw_flag variables an considered all true.


// suppose PreProcessing() has happened
// weekleyMatches is populated
// todayMatches is populated
// allMAtchPredLine is populated


// keep track of all selected data (in case a Tr is clicked thend functions are executed sothat the team data can be showed).
// "check" for simple click and "draw" for wdl drawed graphics so that it will not need redrawing
allIdxTrackArray= new Map();

var tout;
function newMainPlay(){
	console.log("on main");
	
	// if call is succesfull will return else log show
	matchPredLineAjaxCall();
	
}




//------------------------END new FUNCTIONS

var tempid;
function clickCatch(idx){
//	commonAdversaries(idx);
//
//	formDataExtraction(todaysMatches[idx]);
//
//		drawFormChart(idx);
//		drawAtackChart(idx);
//		$('#atackForm_chart'+idx).hide();
//		drawDeffChart(idx);
//		$('#deffForm_chart'+idx).hide();
//
//		drawProgressChart(idx);

	

//	var temptab= classFromtodaysMatches(idx);
//	wdlTableFiller(idx);
	
//	allIdxTrackArray.set(idx,"check");
//	console.log(" allTracker "+allIdxTrackArray.get(idx));
}



function drawWinDrawLoseSim(idx){
	// get the win draws and loses of the teams and draw graph
	var tempc= classFromtodaysMatches(idx);
	drawColumnChart(tempc[0], tempc[1],idx);
	// set universal clicked tr trackes to draw so that it shows and hides the
	// graphic div and does not re-draws it
	allIdxTrackArray.set(idx,"draw");
	console.log(" allTracker "+allIdxTrackArray.get(idx));
}

function drawWinDrawLose(t1,t2,idx){
	// get the win draws and loses of the teams and draw graph
//	var tempc= classFromtodaysMatches(idx);
//	drawColumnChart(t1, t2, idx);
	// set universal clicked tr trackes to draw so that it shows and hides the
	// graphic div and does not re-draws it
//	allIdxTrackArray.set(idx,"draw");
//	console.log(" allTracker "+allIdxTrackArray.get(idx));
}


function old_classFromtodaysMatches(idx){
	var clasObj1 = classificationTable.get(todaysMatches[idx].t1);
	var clasObj2 = classificationTable.get(todaysMatches[idx].t2);
	return([clasObj1,clasObj2]);	
}



// ------------Test------------
function testClassMap(){
	var map = new Map();
	map.set("a",1);
	map.set("b",2);
	map.set("c",3);
	map.set("d",4);


//	for(var ii of map.keys()){
//		console.log(ii);
//	}
}

