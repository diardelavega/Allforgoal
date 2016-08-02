
// 
var weekStart=5;
var commonBackWeeksSearch=10;



//File get and assigne function
var alldata={};
function handleFileSelect(evt) {
	//chose csv file and parse with Papa
	var file = evt.target.files[0];
	Papa.parse(file, {
	  //header: true,
	  dynamicTyping: true,
	  complete: function(results) {
		//dta = results;
		alldata = results.data;
		//assig(results);
	  }
	});
}





//=======================================File Processing Funcs

var allMAtchPredLine=[];
var classificationTable= new Map();
var weeklyMatches=[];
var todaysMatches=[];
//---------------------------------------------------------------> united functions
function preProcess(){
	console.log("on preproces")
	// preproces the file data and at the end we should have ready as required, as expected from the server
	// continuing our tasks
	//classTablePopulation();
//	weekListPop();
//	todaysMatchesPop();
//	matchPredLinePop();
}


function matchPredLinePopSim(){
	console.log("on matchPredline");
	// populate an array simulating the predictions for every match of the dayly matches
	var matchLine={};
	for(var i=0;i<todaysMatches.length;i++)	{
		matchLine={t1:todaysMatches[i].t1, t2:todaysMatches[i].t2, t1Score:todaysMatches[i].t1res, t2Score:todaysMatches[i].t2res, 
				head1:30,headx:30,head2:30,over:60,under:40, p1Y:80,p1N:20,p2Y:40,p2N:60,totHt:1.3,totFt:3.6};
		allMAtchPredLine.push(matchLine);
		console.log(allMAtchPredLine.length);
	}
}

function matchPredLinePop(todaysMatches){
	console.log("on matchPredline");
	// populate an array simulating the predictions for every match of the dayly matches
	var matchLine={};
	for(var i=0;i<todaysMatches.length;i++)	{
		var mobj=todaysMatches[i];
		matchLine={t1:mobj.t1, t2:mobj.t2, t1Score:mobj.t1Ft, t2Score:mobj.t2Ft, 
				head1:mobj._1, headx:mobj._x, head2:mobj._2, over:mobj._o, under:mobj._u,
				p1Y:mobj.p1y, p1N:mobj.p1n, p2Y:mobj.p2y, p2N:mobj.p1n, 
				totHt:mobj.ht, totFt:mobj.ft};
		allMAtchPredLine.push(matchLine);
	}
}


function afterMatchPredLineAjax(){
//	matchPredLinePop(resp);
//	console.log("In the after part2 "+resp.lengh);
	matchPredLineTrBuilder();
	matchPredLineShowSim();
	console.log("In the after part3 "+allMAtchPredLine.lengh);
	for(var i=0;i<allMAtchPredLine.lengh;i++ ){
		wdlTableFiller(allMAtchPredLine[i],i);
	}
	
}


function classTablePopulation(){
	//populate classificaion table from the data of last week matches
	var teamobj={};
	for(var i= alldata.length-2; i>0; i--){
		if(stopPop(alldata[i][8])!= -1)
			break;
		teamobj={name:alldata[i][8],points:alldata[i][9],form:alldata[i][11],atackIn:alldata[i][17],atackOut:alldata[i][18],defIn:alldata[i][20],
			defOut:alldata[i][21], winIn:alldata[i][28], winOut:alldata[i][29], drawIn:alldata[i][30], drawOut:alldata[i][31], loseIn:alldata[i][32],
			loseOut:alldata[i][33], avgFtIn: alldata[i][24],avgFtOut:alldata[i][26]};
		// classTable.push(teamobj);
		classificationTable.set(alldata[i][8],teamobj);
	
		if(stopPop(alldata[i][34])!= -1)
			break;
		teamobj={name:alldata[i][34],points:alldata[i][35],form:alldata[i][37],atackIn:alldata[i][43],atackOut:alldata[i][44],defIn:alldata[i][46],
			defOut:alldata[i][48],winIn:alldata[i][54],winOut:alldata[i][55],drawIn:alldata[i][56],drawOut:alldata[i][57],loseIn:alldata[i][58],
			loseOut:alldata[i][59],avgFtIn: alldata[i][50],avgFtOut:alldata[i][51]};
		// classTable.push(teamobj);
		classificationTable.set(alldata[i][34],teamobj);
	}
	//tabDisplay();
}

function stopPop(name){

	/*for(var key of classificationTable.keys()){
		if(key===name){
			return(1);
		}
	}
	/*
	for(var i=1; i<classificationTable.size; i++){
		if(classTable[i].name===name){
			return(i);
		}
	}*/
	return(-1);
}


function weekListPopSim(){
	var res=0;
	var weekmatchobj={};
	for(var i=1; i< alldata.length-1;i++){
		res = resultSimulator(alldata[i][1],alldata[i][7]);
		
		weekmatchobj={week:alldata[i][0] ,t1:alldata[i][8] ,t2:alldata[i][34] ,t1res:res[0] ,t2res:res[1],
		t1form:alldata[i][11] ,t2form:alldata[i][37] ,t1atackIn:alldata[i][17],t1atackOut:alldata[i][18],t1defIn:alldata[i][20],t1defOut:alldata[i][21],
		t2atackIn:alldata[i][43],t2atackOut:alldata[i][44],t2defIn:alldata[i][46],t2defOut:alldata[i][47],
		t1AvgScoreIn:alldata[i][24], t1AvgScoreOut:alldata[i][25], t2AvgScoreIn:alldata[i][50],t2AvgScoreOut:alldata[i][51]}
		weeklyMatches.push(weekmatchobj);
	}
}

//TODO fix this function with data from the matchpred line 
function weekListPopRed(alldata){
	var res=0;
	var weekmatchobj={};
	for(var i=1; i< alldata.length-1;i++){
		res = resultSimulator(alldata[i][1],alldata[i][7]);
		
		weekmatchobj={week:alldata[i][0] ,t1:alldata[i][8] ,t2:alldata[i][34] ,t1res:res[0] ,t2res:res[1],
		t1form:alldata[i][11] ,t2form:alldata[i][37] ,t1atackIn:alldata[i][17],t1atackOut:alldata[i][18],t1defIn:alldata[i][20],t1defOut:alldata[i][21],
		t2atackIn:alldata[i][43],t2atackOut:alldata[i][44],t2defIn:alldata[i][46],t2defOut:alldata[i][47],
		t1AvgScoreIn:alldata[i][24], t1AvgScoreOut:alldata[i][25], t2AvgScoreIn:alldata[i][50],t2AvgScoreOut:alldata[i][51]}
		weeklyMatches.push(weekmatchobj);
	}
}


function todaysMatchesPop(){
	//get last week matches from weeklyMatches table
	console.log("before "+weeklyMatches.length);
	console.log(weeklyMatches[weeklyMatches.length-1].week);
	
	var weeknr =weeklyMatches[weeklyMatches.length-1].week;
	
	for(var i=weeklyMatches.length-1; i>0; i--){
		if(weeknr!=weeklyMatches[i].week){
			break;
		}
		else{
			todaysMatches.push(weeklyMatches.pop());
		}
	}
	console.log("after "+weeklyMatches.length);
}

////////////////////////////////////
/////////SPECIAL PROCESSING FUNCTIONS
/////////TO SHOW DATA

function commonAdversaries(idx){
	var t1spoting=[];
	var t2spoting=[];

	var team1=todaysMatches[idx].t1;
	var team2=todaysMatches[idx].t2;


	var tab= $("#common"+idx).find('td');
	tab[0].innerHTML=team1;
	$(tab[0]).css({'font-size':18});
	$(tab[0]).css("font-weight","Bold");
	$(tab[0]).css('text-decoration', 'underline')
	tab[1].innerHTML=team2;
	$(tab[1]).css({'font-size':18});
	$(tab[1]).css("font-weight","Bold");
	$(tab[1]).css('text-decoration', 'underline')
	
	//var ttArray=[];
	// suppose we have an array ttArray with the current weeks data as weeklyMatches format
	
	var currentWeek= todaysMatches[0].week;
	var advObj1={};
	var advObj2={};
	//search in tt (curent week matches)
	for(var i=todaysMatches.length-1;i>=0;i--){
		if(team1== todaysMatches[i].t1){
			advObj={datWeek:todaysMatches[i].week, team:team1,adv:todaysMatches[i].t2,pos:i,inOut:"In",tForm:todaysMatches[i].t1form,advForm:todaysMatches[i].t2form,
			      tAtack:(todaysMatches[i].t1atackIn +todaysMatches[i].t1atackOut),advAtak:(todaysMatches[i].t2atackIn +todaysMatches[i].t2atackOut),
				  tDef:(todaysMatches[i].t1defIn +todaysMatches[i].t1defOut), advDef:(todaysMatches[i].t2defIn +todaysMatches[i].t2defOut),
				  tAvgScore:(todaysMatches[i].t1AvgScoreIn+todaysMatches[i].t1AvgScoreOut), advAvgScore:(todaysMatches[i].t2AvgScoreIn + todaysMatches[i].t2AvgScoreOut)};
				  t1spoting[0]=advObj;
		}
		else if(team1== todaysMatches[i].t2){
			advObj={datWeek:todaysMatches[i].week, team:team2, adv:todaysMatches[i].t1, pos:i, inOut:"Out", tForm:todaysMatches[i].t2form, advForm:todaysMatches[i].t1form, 
				  tAtack:(todaysMatches[i].t2atackIn +todaysMatches[i].t2atackOut),advAtak:(todaysMatches[i].t1atackIn +todaysMatches[i].t1atackOut),
				  tDef:(todaysMatches[i].t2defIn +todaysMatches[i].t2defOut), advDef:(todaysMatches[i].t1defIn +todaysMatches[i].t1defOut),
				  tAvgScore:(todaysMatches[i].t2AvgScoreIn+todaysMatches[i].t2AvgScoreOut), advAvgScore:(todaysMatches[i].t1AvgScoreIn + todaysMatches[i].t1AvgScoreOut)};
				  t1spoting[0]=advObj;
		}
		
		if(team2== todaysMatches[i].t1){
			advObj={datWeek:todaysMatches[i].week, team:team1,adv:todaysMatches[i].t2,pos:i,inOut:"In",tForm:todaysMatches[i].t1form,advForm:todaysMatches[i].t2form,
			      tAtack:(todaysMatches[i].t1atackIn +todaysMatches[i].t1atackOut),advAtak:(todaysMatches[i].t2atackIn +todaysMatches[i].t2atackOut),
				  tDef:(todaysMatches[i].t1defIn +todaysMatches[i].t1defOut), advDef:(todaysMatches[i].t2defIn +todaysMatches[i].t2defOut),
				  tAvgScore:(todaysMatches[i].t1AvgScoreIn+todaysMatches[i].t1AvgScoreOut), advAvgScore:(todaysMatches[i].t2AvgScoreIn + todaysMatches[i].t2AvgScoreOut)
				 };
				  t2spoting[0]=advObj;
		}
		else if(team2== todaysMatches[i].t2){
			advObj={datWeek:todaysMatches[i].week, team:team2, adv:todaysMatches[i].t1, pos:i, inOut:"Out", tForm:todaysMatches[i].t2form, advForm:todaysMatches[i].t1form, 
				  tAtack:(todaysMatches[i].t2atackIn +todaysMatches[i].t2atackOut),advAtak:(todaysMatches[i].t1atackIn +todaysMatches[i].t1atackOut),
				  tDef:(todaysMatches[i].t2defIn +todaysMatches[i].t2defOut), advDef:(todaysMatches[i].t1defIn +todaysMatches[i].t1defOut),
				   tAvgScore:(todaysMatches[i].t2AvgScoreIn+todaysMatches[i].t2AvgScoreOut), advAvgScore:(todaysMatches[i].t1AvgScoreIn + todaysMatches[i].t1AvgScoreOut)};
				  t2spoting[0]=advObj;
		}
	}	
	
	//search in weeklyMatches (previous week matches)
	currentWeek=weeklyMatches[weeklyMatches.length-1].week
	
	for(var i=weeklyMatches.length-1;i>=0;i--){
		if(weeklyMatches[i].week + commonBackWeeksSearch < currentWeek){break;} // iterate 10 last weeks
		if(team1== weeklyMatches[i].t1){
			advObj1={datWeek:weeklyMatches[i].week, team:team1, adv:weeklyMatches[i].t2, pos:i, inOut:"In", tRes:weeklyMatches[i].t1res,advRes:weeklyMatches[i].t2res, tForm:weeklyMatches[i].t1form, advForm:weeklyMatches[i].t2form,
			      tAtack:(weeklyMatches[i].t1atackIn +weeklyMatches[i].t1atackOut), advAtak:(weeklyMatches[i].t2atackIn +weeklyMatches[i].t2atackOut),
				  tDef:(weeklyMatches[i].t1defIn +weeklyMatches[i].t1defOut), advDef:(weeklyMatches[i].t2defIn +weeklyMatches[i].t2defOut),
				  tAvgScore:(weeklyMatches[i].t1AvgScoreIn + weeklyMatches[i].t1AvgScoreOut), advAvgScore:(weeklyMatches[i].t2AvgScoreIn + weeklyMatches[i].t2AvgScoreOut)};
				  t1spoting.push(advObj1);
		}
		else if(team1== weeklyMatches[i].t2){
			advObj1={datWeek:weeklyMatches[i].week, team:team1, adv:weeklyMatches[i].t1, pos:i, inOut:"Out", tRes:weeklyMatches[i].t2res, advRes:weeklyMatches[i].t1res, tForm:weeklyMatches[i].t2form, advForm:weeklyMatches[i].t1form, 
				  tAtack:(weeklyMatches[i].t2atackIn +weeklyMatches[i].t2atackOut), advAtak:(weeklyMatches[i].t1atackIn +weeklyMatches[i].t1atackOut),
				  tDef:(weeklyMatches[i].t2defIn +weeklyMatches[i].t2defOut), advDef:(weeklyMatches[i].t1defIn +weeklyMatches[i].t1defOut),
				  tAvgScore:(weeklyMatches[i].t2AvgScoreIn + weeklyMatches[i].t2AvgScoreOut), advAvgScore:(weeklyMatches[i].t1AvgScoreIn + weeklyMatches[i].t1AvgScoreOut)};
				  t1spoting.push(advObj1);
		}
		
		if(team2== weeklyMatches[i].t1){
			advObj2={datWeek:weeklyMatches[i].week, team:team2, adv:weeklyMatches[i].t2, pos:i, inOut:"In", tRes:weeklyMatches[i].t1res,advRes:weeklyMatches[i].t2res, tForm:weeklyMatches[i].t1form, advForm:weeklyMatches[i].t2form,
			      tAtack:(weeklyMatches[i].t1atackIn +weeklyMatches[i].t1atackOut), advAtak:(weeklyMatches[i].t2atackIn +weeklyMatches[i].t2atackOut),
				  tDef:(weeklyMatches[i].t1defIn +weeklyMatches[i].t1defOut), advDef:(weeklyMatches[i].t2defIn +weeklyMatches[i].t2defOut),
				  tAvgScore:(weeklyMatches[i].t1AvgScoreIn + weeklyMatches[i].t1AvgScoreOut), advAvgScore:(weeklyMatches[i].t2AvgScoreIn + weeklyMatches[i].t2AvgScoreOut)};
				  t2spoting.push(advObj2);
		}
		else if(team2== weeklyMatches[i].t2){
			advObj2={datWeek:weeklyMatches[i].week, team:team2, adv:weeklyMatches[i].t1, pos:i, inOut:"Out", tRes:weeklyMatches[i].t2res,advRes:weeklyMatches[i].t1res, tForm:weeklyMatches[i].t2form, advForm:weeklyMatches[i].t1form, 
				  tAtack:(weeklyMatches[i].t2atackIn +weeklyMatches[i].t2atackOut), advAtak:(weeklyMatches[i].t1atackIn +weeklyMatches[i].t1atackOut),
				  tDef:(weeklyMatches[i].t2defIn +weeklyMatches[i].t2defOut), advDef:(weeklyMatches[i].t1defIn +weeklyMatches[i].t1defOut),
				  tAvgScore:(weeklyMatches[i].t2AvgScoreIn + weeklyMatches[i].t2AvgScoreOut), advAvgScore:(weeklyMatches[i].t1AvgScoreIn + weeklyMatches[i].t1AvgScoreOut)};
				  t2spoting.push(advObj2);
		}
	}	
	
	//---------------search first 3 common adv from t1spotiong then 2 from t2 spoting for tot of last 5 common adversaries
	// when a common adv is found we pair them so that we know which match in spot1 corresponds to spot2
	var count=0;
	for(var i=1;i<t1spoting.length;i++){// i & j =1 and not 0 because 0 has the current match data
		if(count>=3){break;}
		for(var j=1;j<t2spoting.length;j++){
			if(t2spoting[j].pair!==undefined){continue;}
			if(t1spoting[i].adv ==t2spoting[j].adv){//->  found a common Adversary
				t1spoting[i].pair=j;
				t2spoting[j].pair=i;
				count++;
			
				t1spoting[i].newForn =  t1spoting[i-1].tForm;
				t1spoting[i].newAtack = t1spoting[i-1].tAtack;
				t1spoting[i].newDef =   t1spoting[i-1].tDef;
				
				t2spoting[j].newForn =  t2spoting[j-1].tForm;
                t2spoting[j].newAtack = t2spoting[j-1].tAtack;
                t2spoting[j].newDef =  t2spoting[j-1].tDef;
			}
		}
	}
	
	count=0;
	for(var i=1;i<t2spoting.length;i++){
		if(count>=2){break;}
		if(t2spoting[i].pair!==undefined){continue;}
		for(var j=1;j<t1spoting.length;j++){
			if(t1spoting[j].pair!==undefined){continue;}
			
			if(t2spoting[i].adv ==t1spoting[j].adv){//->  found a common Adversary
				t2spoting[i].pair=j;
				t1spoting[j].pair=i;
				count++;
			
				t1spoting[j].newForn =  t1spoting[j-1].tForm;
				t1spoting[j].newAtack = t1spoting[j-1].tAtack;
				t1spoting[j].newDef =   t1spoting[j-1].tDef;
				
				t2spoting[i].newForn =  t2spoting[i-1].tForm;
                t2spoting[i].newAtack = t2spoting[i-1].tAtack;
                t2spoting[i].newDef =  t2spoting[i-1].tDef;
			}
		}
	}
		
		
	var directadv =[];
	//show their direct previous encounters
	for(var i=1;i<t1spoting.length;i++){
		if( t1spoting[i].adv==team2){
			directadv.push(t1spoting[i]);
			tobj={week:t1spoting[i].datWeek, tname:team1, adv:team2, io:t1spoting[i].inOut, t1Res:t1spoting[i].tRes, t2Res:t1spoting[i].advRes}
			commonAdvTabShower(idx, tobj, null);
		}
	}

	var tobj1;
	var tobj2;
	var pos2;
	for(var i=1;i<t1spoting.length;i++){
		if(t1spoting[i].pair!==undefined){
			tobj1={week:t1spoting[i].datWeek, tname:t1spoting[i].team, adv:t1spoting[i].adv, io:t1spoting[i].inOut, t1Res:t1spoting[i].tRes, t2Res:t1spoting[i].advRes}
			//console.log(tobj1);
			pos2=t1spoting[i].pair;
			tobj2={week:t2spoting[pos2].datWeek,tname:t2spoting[pos2].team, adv:t2spoting[pos2].adv, io:t2spoting[pos2].inOut, t1Res:t2spoting[pos2].tRes, t2Res:t2spoting[pos2].advRes}
			//console.log(tobj2);
			commonAdvTabShower(idx, tobj1, tobj2);
		}
	}
}


var formArray=[];
var atackArray=[];
var deffArray=[];
var progressArray=[];
function formDataExtraction(mld){
	console.log("formDataExtraction");
	//  from weeklyMatches get data for forms and progress chart data arrays.
	console.log(mld);
		
	//empty arrays from previous calculations	
	 formArray=[];
	 atackArray=[];
	 deffArray=[];
	 progressArray=[];
	
	//from weekley matches generate the array data for the graphs
	var t1data_row=[];
	var t2data_row=[];
	var t1Atack_row=[];
	var t2Atack_row=[];
	var t1Deff_row=[];
	var t2Deff_row=[];
	
	var t1Prog_row=[];
	var t1ProgVal=0;
	var t2Prog_row=[];
	var t2ProgVal=0;
	
	for(var i=0;i<weeklyMatches.length;i++){
		if(weeklyMatches[i].t1===mld.t1 ){
			color=colorCode(weeklyMatches[i].t1res,weeklyMatches[i].t2res);//</h5> <br> <h4>
			t1data_row.push([weeklyMatches[i].week, weeklyMatches[i].t1form, "<h4 style='background-color:"+color+"'> In "+ weeklyMatches[i].t1res+" - "+weeklyMatches[i].t2res+"  "+ weeklyMatches[i].t2+ " </h4>  <h4 style='background-color:"+color+"'>week: "+weeklyMatches[i].week+", form: "+numberFormat(weeklyMatches[i].t1form)+"</h4>"]);
			t1Atack_row.push([weeklyMatches[i].week, (weeklyMatches[i].t1atackIn + weeklyMatches[i].t1atackOut), "<h4 style='background-color:"+color+"'> In "+ weeklyMatches[i].t1res+" - "+weeklyMatches[i].t2res+"  "+ weeklyMatches[i].t2+ " </h4>  <h4 style='background-color:"+color+"'>week: "+weeklyMatches[i].week+", attack: "+numberFormat(weeklyMatches[i].t1atackIn + weeklyMatches[i].t1atackOut)+"</h4>"])
			t1Deff_row.push([weeklyMatches[i].week, (weeklyMatches[i].t1defIn + weeklyMatches[i].t1defOut), "<h4 style='background-color:"+color+"'> In "+ weeklyMatches[i].t1res+" - "+weeklyMatches[i].t2res+"  "+ weeklyMatches[i].t2+ "</h4>  <h4 style='background-color:"+color+"'>week: "+weeklyMatches[i].week+", def: "+numberFormat(weeklyMatches[i].t1defIn + weeklyMatches[i].t1defOut)+"</h4>"])
			
			 t1ProgVal+=progressPoint(weeklyMatches[i].t1res,weeklyMatches[i].t2res);
			 t1Prog_row.push([weeklyMatches[i].week, t1ProgVal, "<h4 style='background-color:"+color+"'> In "+ weeklyMatches[i].t1res+" - "+weeklyMatches[i].t2res+"  "+ weeklyMatches[i].t2+ " </h4>"]);
		
		}
		else if(weeklyMatches[i].t2===mld.t1){
			color=colorCode(weeklyMatches[i].t2res,weeklyMatches[i].t1res);//</h5>   <br> <h4>
			t1data_row.push([weeklyMatches[i].week, weeklyMatches[i].t2form, "<h4 style='background-color:"+color+"'> "+weeklyMatches[i].t1 +"  "+weeklyMatches[i].t1res+" - "+weeklyMatches[i].t2res+ " Out  </h4>  <h4 style='background-color:"+color+"'>week: "+weeklyMatches[i].week+", form: "+numberFormat(weeklyMatches[i].t2form)+"</h4>"]);
			t1Atack_row.push([weeklyMatches[i].week, (weeklyMatches[i].t2atackIn + weeklyMatches[i].t2atackOut), "<h4 style='background-color:"+color+"'>  "+ weeklyMatches[i].t1+"  "+ weeklyMatches[i].t1res+" - "+weeklyMatches[i].t2res+"  Out   </h4>  <h4 style='background-color:"+color+"'> week: "+weeklyMatches[i].week+", attack: "+numberFormat(weeklyMatches[i].t2atackIn + weeklyMatches[i].t2atackOut)+"</h4>"])
			t1Deff_row.push([weeklyMatches[i].week, (weeklyMatches[i].t2defIn + weeklyMatches[i].t2defOut), "<h4 style='background-color:"+color+"'>    "+ weeklyMatches[i].t1+"  "+ weeklyMatches[i].t1res+" - "+weeklyMatches[i].t2res+"  Out </h4>  <h4 style='background-color:"+color+"'> week: "+weeklyMatches[i].week+", def: "+numberFormat(weeklyMatches[i].t2defIn + weeklyMatches[i].t2defOut)+"</h4>"])
		
			 t1ProgVal+=progressPoint(weeklyMatches[i].t2res,weeklyMatches[i].t1res);
			 t1Prog_row.push([weeklyMatches[i].week, t1ProgVal, "<h4 style='background-color:"+color+"'>    "+ weeklyMatches[i].t1+"  "+ weeklyMatches[i].t1res+" - "+weeklyMatches[i].t2res+"  Out </h4>"]);
		}
		
		if(weeklyMatches[i].t2=== mld.t2 ){
			color=colorCode(weeklyMatches[i].t2res, weeklyMatches[i].t1res);
			t2data_row.push([weeklyMatches[i].week, weeklyMatches[i].t2form, "<h4 style='background-color:"+color+"'> "+weeklyMatches[i].t1 +"  "+weeklyMatches[i].t1res+" - "+weeklyMatches[i].t2res+ " Out  </h4>  <h4 style='background-color:"+color+"'>week: "+weeklyMatches[i].week+", form: "+numberFormat(weeklyMatches[i].t2form)+"</h4>"]);
			t2Atack_row.push([weeklyMatches[i].week, (weeklyMatches[i].t2atackIn + weeklyMatches[i].t2atackOut), "<h4 style='background-color:"+color+"'>  "+ weeklyMatches[i].t1+"  "+ weeklyMatches[i].t1res+" - "+weeklyMatches[i].t2res+"  Out  </h4>  <h4 style='background-color:"+color+"'> week: "+weeklyMatches[i].week+", attack: "+numberFormat(weeklyMatches[i].t2atackIn + weeklyMatches[i].t2atackOut)+"</h4>"])
			t2Deff_row.push([weeklyMatches[i].week, (weeklyMatches[i].t2defIn + weeklyMatches[i].t2defOut), "<h4 style='background-color:"+color+"'>    "+ weeklyMatches[i].t1+"  "+ weeklyMatches[i].t1res+" - "+weeklyMatches[i].t2res+"  Out  </h4>  <h4 style='background-color:"+color+"'> week: "+weeklyMatches[i].week+", def: "+numberFormat(weeklyMatches[i].t2defIn + weeklyMatches[i].t2defOut)+"</h4>"])
		
			 t2ProgVal+=progressPoint(weeklyMatches[i].t2res,weeklyMatches[i].t1res);
			t2Prog_row.push([weeklyMatches[i].week, t2ProgVal, "<h4 style='background-color:"+color+"'>    "+ weeklyMatches[i].t1+"  "+ weeklyMatches[i].t1res+" - "+weeklyMatches[i].t2res+"  Out </h4>"]);
		}
		else if(weeklyMatches[i].t1=== mld.t2 ){
			color=colorCode(weeklyMatches[i].t1res,weeklyMatches[i].t2res);
			t2data_row.push([weeklyMatches[i].week, weeklyMatches[i].t1form, "<h4 style='background-color:"+color+"'> In "+ weeklyMatches[i].t1res+" - "+weeklyMatches[i].t2res+"  "+ weeklyMatches[i].t2+ "</h4>  <h4 style='background-color:"+color+"'>week: "+weeklyMatches[i].week+", form: "+numberFormat(weeklyMatches[i].t1form)+"</h4>"]);
			t2Atack_row.push([weeklyMatches[i].week, (weeklyMatches[i].t1atackIn + weeklyMatches[i].t1atackOut), "<h4 style='background-color:"+color+"'> In "+ weeklyMatches[i].t1res+" - "+weeklyMatches[i].t2res+"  "+ weeklyMatches[i].t2+ " </h4>  <h4 style='background-color:"+color+"'> week: "+weeklyMatches[i].week+", attack:  "+numberFormat(weeklyMatches[i].t1atackIn + weeklyMatches[i].t1atackOut)+"</h4>"])
			t2Deff_row.push([weeklyMatches[i].week, (weeklyMatches[i].t1defIn + weeklyMatches[i].t1defOut), "<h4 style='background-color:"+color+"'> In "+ weeklyMatches[i].t1res+" - "+weeklyMatches[i].t2res+"  "+ weeklyMatches[i].t2+ "   </h4>  <h4 style='background-color:"+color+"'>week: "+weeklyMatches[i].week+", def: "+numberFormat(weeklyMatches[i].t1defIn + weeklyMatches[i].t1defOut)+"</h4>"])
		
			t2ProgVal+=progressPoint(weeklyMatches[i].t1res,weeklyMatches[i].t2res);
			t2Prog_row.push([weeklyMatches[i].week, t2ProgVal, "<h4 style='background-color:"+color+"'> In "+ weeklyMatches[i].t1res+" - "+weeklyMatches[i].t2res+"  "+ weeklyMatches[i].t2+ " </h4>"]);
		}
	}
	
	var t1i=0;
	var t2i=0;
	


	
	var sizeSize=false;
	if(t1data_row.length==t2data_row.length){
		sizeSize=true;
	}




	// so far the data is divided in two allays when the team plays in and out. We unite them to show the graph
	for(var i=weekStart; t1i<t1data_row.length || t2i<t2data_row.length;){

		if(t1data_row[t1i][0]==t2data_row[t2i][0]){
			formArray.push(    [t1data_row[t1i][0], t1data_row[t1i][1], t1data_row[t1i][2],t2data_row[t2i][1],  t2data_row[t2i][2]]);
			atackArray.push(   [t1Atack_row[t1i][0],t1Atack_row[t1i][1],t1Atack_row[t1i][2],t2Atack_row[t2i][1],t2Atack_row[t2i][2]]);
			deffArray.push(    [t1Deff_row[t1i][0], t1Deff_row[t1i][1], t1Deff_row[t1i][2],t2Deff_row[t2i][1],  t2Deff_row[t2i][2]]);
			progressArray.push([t1Prog_row[t1i][0], t1Prog_row[t1i][1], t1Prog_row[t1i][2],t2Prog_row[t2i][1],  t2Prog_row[t2i][2]]);
			t1i++;
			t2i++;
			i++;
		}
		else{ //in case of mising matches and wrong enumeration try to correct
			if(sizeSize){
				if(t1data_row[t1i][0]!=i){
					t1data_row[t1i][0]=i;
					t1Atack_row[t1i][0]=i;
					t1Deff_row[t1i][0]=i;
					t1Prog_row[t1i][0]=i;
					//i++;
				}
				if(t2data_row[t2i][0]!=i){
					t2data_row[t2i][0]=i;
					t2Atack_row[t2i][0]=i;
					t2Deff_row[t2i][0]=i;
					t2Prog_row[t2i][0]=i;
					//i++;
				}
				continue;
			}




			if(t1data_row[t1i][0]==t1data_row[t1i-1][0]){
				if(t1data_row[t1i+1][0]-t1data_row[t1i][0]>=2){ // if the week was counted wrongly as a previous week
					t1data_row[t1i][0] = t1data_row[t1i][0]+1;  // Math.round( (t1data_row[t1i][0]+t1data_row[t1i+1][0]) /2 ); // correct it and recalc
					t1Atack_row[t1i][0] = t1Atack_row[t1i][0]+1;
					t1Deff_row[t1i][0] = t1Deff_row[t1i][0]+1;
					t1Prog_row[t1i][0] = t1Prog_row[t1i][0]+1;

					console.log("aaaa "+t1data_row[t1i][0] +" == "+ t1data_row[t1i+1][0]);
					continue;
					console.log('This should not be showing');
				}
			}
			else if(t2data_row[t2i][0]==t2data_row[t2i-1][0]){
				if(t2data_row[t2i+1][0]-t2data_row[t2i][0]==2){ // if the week was counted wrongly as a previous week
					t2data_row[t2i][0] =  t2data_row[t1i][0]+1;//Math.round( (t2data_row[t2i][0]+t2data_row[t2i+1][0]) /2 ); // correct it and recalc
					t2Atack_row[t1i][0] = t2Atack_row[t1i][0]+1;
					t2Deff_row[t1i][0] = t2Deff_row[t1i][0]+1;
					t2Prog_row[t1i][0] = t2Prog_row[t1i][0]+1;
					console.log("aaaa "+t2data_row[t2i][0] +" == "+ t2data_row[t2i+1][0]);
					continue;
					console.log('This should not be showing');
				}
			}
			
			//in case of mising matches just skip over the missing matches and show only one of the teams for that missing week
			if(t1data_row[t1i][0]>t2data_row[t2i][0]){
				formArray.push ([t2data_row [t2i][0],,"",t2data_row [t2i][1],t2data_row [t2i][2]]);
				atackArray.push([t2Atack_row[t2i][0],,"",t2Atack_row[t2i][1],t2Atack_row[t2i][2]]);
				deffArray.push ([t2Deff_row [t2i][0],,"",t2Deff_row [t2i][1],t2Deff_row [t2i][2]]);
				progressArray.push([t2Prog_row[t2i][0],,"",t2Prog_row[t2i][1],t2Prog_row[t2i][2]]);
				t2i++;
				console.log(t1i +" < "+ t2i);
			}
			else if(t1data_row[t1i][0]<t2data_row[t2i][0]){
				formArray.push ([t1data_row [t1i][0],t1data_row [t1i][1],t1data_row [t1i][2],,""]);
				atackArray.push([t1Atack_row[t1i][0],t1Atack_row[t1i][1],t1Atack_row[t1i][2],,""]);
				deffArray.push ([t1Deff_row [t1i][0],t1Deff_row [t1i][1],t1Deff_row [t1i][2],,""]);
				progressArray.push([t1Prog_row[t1i][0],t1Prog_row[t1i][1],t1Prog_row[t1i][2],,""]);
				t1i++;
				console.log(t1i +" > "+ t2i);			
			}
		}
	}
	
}

//-----------------------------------------END OF Preprocessing
//=========================================

//------------Test the simulation

function resultSimulator(head,score){
	
	if(head=='X'){
		return([score/2,score/2]);
	}
	if(score==0){
		return([0,0]);
	}
	
	var x = Math.floor(Math.random() * (score + 1));
	var y=0;
		
	while(x==score/2){
		x = Math.floor(Math.random() * (score + 1));
	}
	y=score-x;
	//console.log('score: '+score + ", head: "+head+",  x-"+ x+ " y-"+ y);
	if(head==1){
		return([Math.max(x,y),Math.min(x,y)])
	}
	if(head==2){
		return([Math.min(x,y),Math.max(x,y)])
	}
}


function simSimTest(){
	for(var i=0;i<20;i++){
	a=Math.floor(Math.random() * (3 + 1));
	sc=Math.floor(Math.random() * (8 + 1));
	if(a==1){
		s.log(resultSimulator(1,sc));
	}
	if(a==2){
		console.log(resultSimulator("X",2*sc));
	}
	if(a==3){
		console.log(resultSimulator(2,sc));
	}
	}
}


var tempArray=[];
var tempArray2=[];
function tetToy(){
	var weeknr =weeklyMatches[weeklyMatches.length-1].week;
	for(var i=weeklyMatches.length-1; i>0;i-- ){
		
		console.log("in ");
		if(weeknr!=weeklyMatches[i].week){
			console.log(weeklyMatches[i].week);
			break;
		}
		else{
			//todaysMatches.push(weeklyMatches[i]);
			
			tempArray.push(weeklyMatches[i]);
			tempArray2.push(weeklyMatches.pop());
			
			
		}
		console.log(i);
	}
}
