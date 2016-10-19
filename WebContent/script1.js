
 // var t1={name:"Kapfenberger SV",winsIn:10,winsOut:9,drawsIn:1,drawsOut:4,losesIn:7,losesOut:1,form:[],adv:[]};
 // var t2={name:"St. Polten",   winsIn:8,winsOut:10,drawsIn:10,drawsOut:14,losesIn:9,losesOut:5,form:[],adv:[]};
	 
	// 	var matchLine={t1:"Kapfenberger SV", t2:"St. Polten",t1Score:3,t2Score:5,head1:30,headx:30,head2:30,over:60,under:40,
	// 					p1Y:80,p1N:20,p2Y:40,p2N:60,totHt:1.3,totFt:3.6}
						
function populateTeamForm(){
	//original finction to simulate form data
	t1.form=[];
	t2.form=[];
	t1.adv=[];
	t1.adv=[];
	
	t1.form[0]=0;
	t2.form[0]=0;
	t1.adv.push("team "+0+"\n"+0);
	t2.adv.push("team "+77+"\n"+0);
	
	for (var i=1;i<45;i++){
	var	x=Math.random() * (4 - -4) -4;
	var	y=t1.form[i-1]+x;
		// v ="<h1 >team</h1> "+i +"\n "+y;
	var	v =i +"\n "+y;
		t1.form.push(y);
		t1.adv.push(v);
		console.log(v);
		
		x=Math.random() * (4 - -4) -4;
		y=t2.form[i-1]+x;
		// v ="<span style='fontSize:15'>team</span> "+ (-i)+"\n "+y;
		v =-i+"\n "+y;
		t2.form.push(y);
		t2.adv.push(v);	
	}
	genFormData();
}
	
	
function adapt() {
	var trs = $("#tab1 tbody tr");
	for (var i = 0; i < trs.length; i++) {
		document.getElementById("t1Name"+i).innerHTML = matchLine.t1;
		document.getElementById("t2Name"+i).innerHTML = matchLine.t2;
		document.getElementById("t1Score"+i).innerHTML =matchLine.t1Score;
		document.getElementById("t2Score"+i).innerHTML =matchLine.t2Score;
		idttr(i+"head_1",matchLine.head1);
		idttr(i+"head_x",matchLine.headx);
		idttr(i+"head_2",matchLine.head2);
		idttr("over"+i,matchLine.over);
		idttr("under"+i,matchLine.under);
		idttr("p1Y"+i,matchLine.p1Y);
		idttr("p1N"+i,matchLine.p1N);
		idttr("p2Y"+i,matchLine.p2Y);
		idttr("p2N"+i,matchLine.p2N);
		idttr("totHt"+i,matchLine.totHt);
		idttr("totFt"+i,matchLine.totFt);
	}
}
		
function idttr(id,text){
	document.getElementById(id).innerHTML =text;
}




function matchPredLineTrBuilder(resp){
/* builds the prediction lines in tr element for the daily matches of a competition*/	
	var compId=resp.compId;			//123;  		// parameter variable
	var country=resp.country;		//'COUNTRY';  		// parameter variable
	var comp=resp.competition			//'COMPETITION';  		// parameter variable
	
	var competitionTable= document.createElement("table");
	$(competitionTable).attr('test_id','tab'+compId);
	$(competitionTable).attr('class',"columns");
	$(competitionTable).attr('border',1);

	var htd  =document.createElement("td");
	$(htd).html(country+" : "+comp);
	$(htd).attr('colspan',17);
	
	var htr  =document.createElement("tr");
	$(htr).attr('class','compHeader');
	$(htr).append(htd);
	
	//TODO FIX add a tr for the matchPredLineHeader (1,x,2,o,u,...)
	
	var tabHead =document.createElement("thead");
	$(tabHead).append(htr);
	$(competitionTable).append(tabHead);
			
	var tabBody = document.createElement("tbody");
//	$(competitionTable).append(tabBody);

	console.log("tr builder");
	var tdTxt;
	for(var i=0;i<resp.obj.length;i++)	{
		var idval=compId*1000+i;
		tdTxt="<tr data-toggle='collapse' data-target='#collapsePanel"+idval+"' id='rowmld"+idval+"' onclick= 'clickCatch("+idval+");' >"+
					"<td><span class='glyphicon glyphicon-triangle-bottom' data-toggle='collapse' data-target='#collapsePanel"+idval+"'> </td>"+
					"<td></td>  <td></td> <td></td> <td></td>  <td></td>"+
					"<td></td>  <td></td> <td></td> <td></td>  <td></td>"+
					"<td></td>  <td></td> "+
					"<td></td>  <td></td> "+
					"<td></td>  <td></td> "+
					"<td></td>"+
				"</tr>";
//		$("#tab1").children('tbody').append(tdTxt);							// the visible header
//		$("#tab1").children('tbody').append(collapsablePannelCreator(i)); 	// the collapsable part
		$(tabBody).append(tdTxt);
		$(tabBody).append(collapsablePannelCreator(idval));	
	}
	$(competitionTable).append(tabBody);
	$("#allCompetitions").append(competitionTable);
}



function collapsablePannelCreator(line_i){
	//build the colapsable pannel for the graphical and statistical data

	//the div that keeps all the data
	var collapseDiv = document.createElement('div');
	$(collapseDiv).attr("id","collapsePanel"+line_i);
	$(collapseDiv).attr("class","panel-collapse collapse");
	$(collapseDiv).attr("role","tabpanel");
	$(collapseDiv).attr("aria-labelledby","headingOne");
	// $(collapseDiv).attr("onclick","clickCatch("+line_i+")");// return index

	// the div that keeps the header, pills or tabs to choose from  & all the other data
	var panelDiv = document.createElement('div');
	$(panelDiv).attr("class","panel-body well");

	// the div with the ctual tables and graphical data
	var contentDiv = document.createElement('div');
	$(contentDiv).attr("class","tab-content");


	// pannel pilars
	var panHeadTxt= "<div> <ul class='nav nav-pills'>"+
						"<li class='active'><a data-toggle='pill' href='#wdl"+line_i+"'>Win/Draw/Lose</a></li>"+
					  	"<li ><a data-toggle='pill' href='#commonAdv"+line_i+"'>CommonAdv</a></li>"+
						"<li><a data-toggle='pill' href='#formMenu"+line_i+"'>FORM</a></li>"+
						"<li><a data-toggle='pill' href='#progress"+line_i+"'>Progres</a></li>"+
						"</ul> </div>"
	$(panelDiv).append(panHeadTxt);

	

	//win draw lose div with all the tables and elements underneath						
	var winDrawLoseDivTxt= "<div id='wdl"+line_i+"' draw_data='false'  class='tab-pane fade in active'>"+
								"<h3>Win/Draw/Lose</h3>"+
								"<table class='columns'  >"+
								  "<tr align='center'>"+
								  	"<td colspan='3'>"+
								  	"<input id='wdl_checkTable"+line_i+"' type='checkbox' checked='true' onclick='wdltgswitch("+line_i+")'/>"+
									"<label for='wdl_checkTable"+line_i+"' >  tables</label>&emsp;"+
									"<input id='wdl_checkGraph"+line_i+"' type='checkbox'  onclick='wdltgswitch("+line_i+")'/>"+
									"<label for='wdl_checkGraph"+line_i+"' >  graphic</label>"+
									 "</td>"+
								   "</tr>"+
								   "<tr id='wdl_gra_tr"+line_i+"'>"+
									  "<td><div id='totBar"+line_i+"' style='border: 1px solid #ccc'></div></td>"+
									  "<td><div id='operativeBar"+line_i+"' style='border: 1px solid #ccc'></div></td>"+
									  "<td><div id='allBar"+line_i+"' style='border: 1px solid #ccc'></div></td>"+
								 " </tr>"+
								  "<tr id='wdl_tab_tr"+line_i+"'>"+
									  "<td>"+
										"<table id='wdl_tot"+line_i+"' class='table table-bordered'>"+
											"<tr><td colspan='2'>Team1</td> <td colspan='3'>Team2</td> </tr>"+
											"<tr><td class='tab_txt'>tot. Wins</td><td>Team1 res</td><td colspan='2'>Team2 res</td><td class='tab_txt'>tot. Wins</td></tr>"+
											"<tr><td class='tab_txt'>tot. Draw</td><td>Team1 res</td><td colspan='2'>Team2 res</td><td class='tab_txt'>tot. Draw</td></tr>"+
											"<tr><td class='tab_txt'> tot. Lose</td><td>Team1 res</td><td colspan='2'>Team2 res</td><td class='tab_txt'>tot. Lose</td></tr>"+
										"</table>"+
									  "</td>"+
									  "<td>"+
										"<table id='wdl_op"+line_i+"' class='table table-bordered'>"+
											"<tr><td colspan='2'>Team1</td> <td colspan='3'>Team2</td> "+
											"<tr><td class='tab_txt'>Wins In</td><td>Team1 res</td><td colspan='2'>Team2 res</td><td class='tab_txt'>Wins Out</td></tr>"+
											"<tr><td class='tab_txt'>Draw In</td><td>Team1 res</td><td colspan='2'>Team2 res</td><td class='tab_txt'>Draw Out</td></tr>"+
											"<tr><td class='tab_txt'>Lose In</td><td>Team1 res</td><td colspan='2'>Team2 res</td><td class='tab_txt'>Lose Out</td></tr>"+
										"</table>"+
									  "</td>"+
									  "<td>"+
										"<table id='wdl_all"+line_i+"' class='table table-bordered'>"+
											"<tr><td colspan='2'>Team1</td> <td colspan='3'>Team2</td> <!-- <td></td> --></tr>"+
											"<tr><td class='tab_txt'>Wins In</td><td>Team1 res</td><td colspan='2'>Team2 res</td><td class='tab_txt'>Wins In</td></tr>"+
											"<tr><td class='tab_txt'>Wins Out</td><td>Team1 res</td><td colspan='2'>Team2 res</td><td class='tab_txt'>Wins Out</td></tr>"+
											"<tr><td class='tab_txt'>Draw In</td><td>Team1 res</td><td colspan='2'>Team2 res</td><td class='tab_txt'>Draw In</td></tr>"+
											"<tr><td class='tab_txt'>Draw Out</td><td>Team1 res</td><td colspan='2'>Team2 res</td><td class='tab_txt'>Draw Out</td></tr>"+
											"<tr><td class='tab_txt'>Lose In</td><td>Team1 res</td><td colspan='2'>Team2 res</td><td class='tab_txt'>Lose In</td></tr>"+
											"<tr><td class='tab_txt'>Lose Out</td><td>Team1 res</td><td colspan='2'>Team2 res</td><td class='tab_txt'>Lose Out</td></tr>"+
										"</table>"+
									  "</td>"+
								  "</tr>"+
								"</table>"+
							  "</div>"
	$(contentDiv).append(winDrawLoseDivTxt);
	
	//the div with the comon adversaries section
	var commonDivTxt=" <div id='commonAdv"+line_i+"' class='tab-pane fade '>"+
								"<h3>Common Adversaries</h3>"+
								"<table class='columns table table-bordered' id='common"+line_i+"'>"+
								"<tr><td>Home Team</td><td>Guests Team</td></tr>"+
								"</table>"+
							 " </div>";
	$(contentDiv).append(commonDivTxt);

	// the form div with the check buttons and graphical divs
	var formDivTxt="<div id='formMenu"+line_i+"' class='tab-pane fade'>"+
								"<h3>FORM</h3>"+
								"<input id='form_check"+line_i+"' type='checkbox' checked='true'  onclick='formswitch("+line_i+")'/>"+
								"<label  for='form_check"+line_i+"' >  form</label>&emsp;"+
								"<input id='atackForm_check"+line_i+"' type='checkbox' onclick='formswitch("+line_i+")'/>"+
								"<label for='atackForm_check"+line_i+"' >  attack</label> &emsp;"+
								"<input id='deffForm_check"+line_i+"' type='checkbox' onclick='formswitch("+line_i+")'/>"+
								"<label for='deffForm_check"+line_i+"' >  deffence</label>"+
								
								"<div id='form_chart"+line_i+"' style=' margin-bottom:10px'></div>"+
								"<div id='atackForm_chart"+line_i+"' style=' margin-bottom:10px'></div>"+
								"<div id='deffForm_chart"+line_i+"' style=' margin-bottom:10px'></div>"+
							  "</div>"
	$(contentDiv).append(formDivTxt);

	// the div with the graphical area for progress chart
	var progressDivTxt=" <div id='progress"+line_i+"' class='tab-pane fade'>"+
								"<h3>Progres</h3>"+
								"<div id='progress_chart"+line_i+"'></div>"+
							  "</div>"
	$(contentDiv).append(progressDivTxt);



	$(panelDiv).append(contentDiv);
	$(collapseDiv).append(panelDiv);

	var td = document.createElement('td');
	$(td).attr("colspan",17);

	$(td).append(collapseDiv);

	//the tr that keeps all the hidden stats data for the match
	var collapsablePanelTr = document.createElement('tr');
	$(collapsablePanelTr).append(td);

	return collapsablePanelTr;
}
	
	
function matchPredLineShowSim(){
/*enter the values of the predictions*/
	for(var i=0;i<allMAtchPredLine.length;i++)	{
		var tds=$('#rowmld'+i).children('td');
		tds[1].innerHTML=allMAtchPredLine[i].t1;
//		tds[2].innerHTML=allMAtchPredLine[i].t1Score;
//		tds[4].innerHTML=allMAtchPredLine[i].t2Score;
		tds[5].innerHTML=allMAtchPredLine[i].t2;
		tds[6].innerHTML=allMAtchPredLine[i].head1;
		tds[7].innerHTML=allMAtchPredLine[i].headx;
		tds[8].innerHTML=allMAtchPredLine[i].head2;
		tds[9].innerHTML=allMAtchPredLine[i].over;
		tds[10].innerHTML=allMAtchPredLine[i].under;

		tds[11].innerHTML=allMAtchPredLine[i].p1Y;
		tds[12].innerHTML=allMAtchPredLine[i].p1N;
		tds[13].innerHTML=allMAtchPredLine[i].p2Y;
		tds[14].innerHTML=allMAtchPredLine[i].p2N;
		tds[15].innerHTML=allMAtchPredLine[i].totHt;
		tds[16].innerHTML=allMAtchPredLine[i].totFt;
	}
	console.log("in show & "+allMAtchPredLine.length);
}	

function matchPredLineShow(resp){
	/*enter the values of the predictions*/
	var res=resp.obj;
	
	for(var i=0; i < res.length; i++)	{
		var j=resp.compId*100+i;
		var tds=$('#rowmld'+j).children('td');
		tds[1].innerHTML=res[i].t1;
//		tds[2].innerHTML=res[i].t1Score;
//		tds[4].innerHTML=res[i].t2Score;
		tds[5].innerHTML=res[i].t2;
		tds[6].innerHTML=res[i]._1;
		tds[7].innerHTML=res[i]._x;
		tds[8].innerHTML=res[i]._2;
		tds[9].innerHTML=res[i]._o
		tds[10].innerHTML=res[i]._u;

		tds[11].innerHTML=res[i].p1y;
		tds[12].innerHTML=res[i].p1n;
		tds[13].innerHTML=res[i].p2y;
		tds[14].innerHTML=res[i].p2n;
		tds[15].innerHTML=res[i].ht;
		tds[16].innerHTML=res[i].ft;
	}
	
	
}
	
	