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
function clickCatch(idc){
	console.log(idc);
	//split the idc into compid and idx; var[0]-> comp id, var[1]-> idx
	var nrvals=compIdxSplitter(idc);
	
	//check if localstorage date is todays date
	var dat =localStorage.getItem("date");
	var tdate = new Date().getDate();
	
	if(tdate === null || tdate != dat){
		localStorage.clear();
		localStorage.setItem("date",tdate);
		redWeekMatchesAjaxCall(idc);
	}
	else{// check if storage has data for the selected compId
//		console.log("in mainPlay clickCatch ");
//		console.log("local storage : "+localStorage.getItem(nrvals[0]));
		if(localStorage.getItem(nrvals[0])===null  ){
			console.log("to call ajax");
			redWeekMatchesAjaxCall(idc);
		}else{
			console.log("handle response");
			redWeekHandle(idc,localStorage.getItem(nrvals[0]));
		}
	}
	
	
	// call the form data ajax and get the data
	
	
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

function redWeekHandle(cid,data){//complex id ; compId *100 + idx
	//tasks to be executed after the ajax call has returned data  
	//store to localStorage;		?? do it in ajax
	console.log(cid);
	
	//parse with papa parser
	papaparse(data);//parse  the data into a array   //-> ret compRedData
	
	//gather from displayed fields the two teams names
	var tds =$("#rowmld"+cid).children("td");
	var homeTeam = $(tds[1]).text();
	var awayTeam = $(tds[5]).text();
	
	//proces funcs
//	redFormDataExtraction(cid,homeTeam,awayTeam);
	redFormDataExtraction(cid,homeTeam,awayTeam);
	
	// draw common adversaries
	redCommonAdversaries(cid,homeTeam,awayTeam);
	
	//draw functions
	drawFormChart(cid,homeTeam,awayTeam);
//	$('#form_chart'+cid).hide();
	drawAtackChart(cid,homeTeam,awayTeam);
	$('#atackForm_chart'+cid).hide();
	drawDeffChart(cid,homeTeam,awayTeam);
	$('#deffForm_chart'+cid).hide();
//
	drawProgressChart(cid,homeTeam,awayTeam);
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
var compRedData={};		// keep the reduced data of a specific competition
function papaparse(csv){
	
	Papa.parse(csv, {
		  //header: true,
		  dynamicTyping: true,
		  complete: function(results) {
			  compRedData = results.data;
		  }
		});
	
	
}

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

