
wdl_tot_txt='Total Results';//
wdl_op_txt='Home Team In vs Away Team Out';
wdl_all_txt='In & Out';

/**
 * create the box, on which the data from the mpl in the all matches page is
 * going to be displayed
 */
function mplBoxCreate() {
 	var mplResp ={
    "country": "Casiopea_0",
    "competition": "TerraMAlgon_0",
    "compId": 0,
    "serinr": 1,
    "lfml": [
      {
        "t1": "Sarpsborg 08",
        "t2": "Lillestrøm",
        "ft1": 1,
        "ft2": 9,
        "ht1": 1,
        "ht2": 3,
        "h1": 0.0,
        "hx": 0.0,
        "h2": 0.0,
        "so": 0.0,
        "su": 0.0,
        "dat": " Wed, 19/10/2016 ",
        "matchTime": "3:15"
      },
      {
        "t1": "Lillestrøm",
        "t2": "Molde",
        "ft1": 7,
        "ft2": 5,
        "ht1": 1,
        "ht2": 4,
        "h1": 0.0,
        "hx": 0.0,
        "h2": 0.0,
        "so": 0.0,
        "su": 0.0,
        "dat": " Wed, 19/10/2016 ",
        "matchTime": "23:19"
      },
      {
        "t1": "Brann",
        "t2": "Haugesund",
        "ft1": 2,
        "ft2": 0,
        "ht1": 3,
        "ht2": 3,
        "h1": 0.0,
        "hx": 0.0,
        "h2": 0.0,
        "so": 0.0,
        "su": 0.0,
        "dat": " Wed, 19/10/2016 ",
        "matchTime": "6:39"
      },
      {
        "t1": "Valerenga",
        "t2": "Odd",
        "ft1": 5,
        "ft2": 0,
        "ht1": 3,
        "ht2": 2,
        "h1": 0.0,
        "hx": 0.0,
        "h2": 0.0,
        "so": 0.0,
        "su": 0.0,
        "dat": " Wed, 19/10/2016 ",
        "matchTime": "17:51"
      },
      {
        "t1": "Tromsø",
        "t2": "Strømsgodset",
        "ft1": 9,
        "ft2": 3,
        "ht1": 3,
        "ht2": 0,
        "h1": 0.0,
        "hx": 0.0,
        "h2": 0.0,
        "so": 0.0,
        "su": 0.0,
        "dat": " Wed, 19/10/2016 ",
        "matchTime": "13:10"
      },
      {
        "t1": "Rosenborg",
        "t2": "Rosenborg",
        "ft1": 2,
        "ft2": 1,
        "ht1": 4,
        "ht2": 0,
        "h1": 0.0,
        "hx": 0.0,
        "h2": 0.0,
        "so": 0.0,
        "su": 0.0,
        "dat": " Wed, 19/10/2016 ",
        "matchTime": "19:53"
      }
    ]
  }
	
	var compId=mplResp.compId;
	var compName=mplResp.competition;
	var country=mplResp.country;
	
	var tab= document.createElement("table");
	$(tab).attr("border",1);
	$(tab).attr("class","mplBox");
	$(tab).attr("id","mplBox_"+compId);
	
	var thed= document.createElement("thead");
	var hedTxt="<tr><td style='padding-left:40px'></td><td colspan='5'>"+compName+"</td> <td colspan='10' style='padding-right:20px'>"+country+"</td></tr>";
	// $(thed).css("background-color", "#cbcdef")
	// $(thed).css("fontsize", "14pt")
	// $(thed).css("fontcolor", "#224488")
	$(thed).append(hedTxt);
	
	var tbodRow1="<tr id='rowmld"+i+"' class='mplheadrow' >"+"<td><span      class='chevron fa fa-fw'  > </td>"+  "<td>Home</td>  <td>FT</td> <td>HT</td> <td>HT</td>  <td>FT</td> <td>Away</td>"+ "<td> 1 </td>  <td>X</td> <td>2</td> <td>Over</td>  <td>Under</td></tr>"
	var tbod= document.createElement("tbody");
	$(tbod).append(tbodRow1);
	// $(tbod).find(td).css("padding","20px");
	
	$(tab).append(thed);
	$(tab).append(tbod);
	
	var tdTxt;
	for(var i=0;i<mplResp.lfml.length;i++)	{
		var idval=compId*1000+i;
		tdTxt="<tr  data-toggle='collapse' data-target='#collapsePanel"+idval+"' id='rowmld"+idval+"' onclick='chevrinCchange("+idval+"); clickCatch("+idval+"); ' >"
		+ "<td ><span   class='indicator glyphicon glyphicon-chevron-right  pull-left' > </td>"
		+ "<td>"+mplResp.lfml[i].t1+"</td>  <td>"+mplResp.lfml[i].ft1+"</td> <td>"+mplResp.lfml[i].ht1+"</td> <td>"+mplResp.lfml[i].ht2+"</td>  <td>"+mplResp.lfml[i].ft2+"</td> <td>"+mplResp.lfml[i].t2+"</td>"+  "<td> "+mplResp.lfml[i].h1+" </td>  <td>"+mplResp.lfml[i].hx+"</td> <td>"+mplResp.lfml[i].h2+"</td> <td>"+mplResp.lfml[i].so+"</td>  <td>"+mplResp.lfml[i].su+"</td></tr>";

		$(tbod).append(tdTxt);
		//console.log("idval "+idval);
		$(tbod).append(collapsablePannelCreator(idval));
	}
	$("#div1").append(tab);

	/*cal wdl extrapolate after the collapsable part is been created*/
	wdlExtrapolate(0,0);
}

/**change the chervon orienation*/
function chevrinCchange(id){
	var targ=$('#rowmld'+id+' td .indicator');
	targ.toggleClass('glyphicon-chevron-down glyphicon-chevron-right');

	// wdlExtrapolate(id);
}

/** create the extra data colapsable pannel for the mpl*/
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
								"<table class='outer_wdl_tab'  >"+
								  "<tr align='center'>"+
								  	"<td colspan='3'>"+
								  	"<input id='wdl_checkTable"+line_i+"' type='checkbox' checked='true' onclick='wdltgswitch("+line_i+")'/>"+
									"<label for='wdl_checkTable"+line_i+"' >  tables</label>&emsp;"+
									"<input id='wdl_checkGraph"+line_i+"' type='checkbox'  onclick='wdltgswitch("+line_i+")'/>"+
									"<label for='wdl_checkGraph"+line_i+"' >  graphic</label>"+
									 "</td>"+
								   "</tr>"+
								   "<tr id='wdl_gra_tr"+line_i+"'>"+
								   "<input type='hidden' value='NO'>"+// indicate wether the wdl is drawn or not
									  "<td><div id='totBar"+line_i+"' style='border: 1px solid #ccc'></div></td>"+
									  "<td><div id='operativeBar"+line_i+"' style='border: 1px solid #ccc'></div></td>"+
									  "<td><div id='allBar"+line_i+"' style='border: 1px solid #ccc'></div></td>"+
								 " </tr>"+
								  "<tr id='wdl_tab_tr"+line_i+"'>"+
									  "<td>"+
										"<table id='wdl_tot"+line_i+"' class='inner_wdl_tab'>"+
											"<tr><td colspan='2'>Team1</td> <td colspan='3'>Team2</td> </tr>"+
											"<tr><td class='tab_txt'>tot. Wins</td><td>Team1 res</td><td colspan='2'>Team2 res</td><td class='tab_txt'>tot. Wins</td></tr>"+
											"<tr><td class='tab_txt'>tot. Draw</td><td>Team1 res</td><td colspan='2'>Team2 res</td><td class='tab_txt'>tot. Draw</td></tr>"+
											"<tr><td class='tab_txt'> tot. Lose</td><td>Team1 res</td><td colspan='2'>Team2 res</td><td class='tab_txt'>tot. Lose</td></tr>"+
										"</table>"+
									  "</td>"+
									  "<td>"+
										"<table id='wdl_op"+line_i+"' class='inner_wdl_tab'>"+
											"<tr><td colspan='2'>Team1</td> <td colspan='3'>Team2</td> "+
											"<tr><td class='tab_txt'>Wins In</td><td>Team1 res</td><td colspan='2'>Team2 res</td><td class='tab_txt'>Wins Out</td></tr>"+
											"<tr><td class='tab_txt'>Draw In</td><td>Team1 res</td><td colspan='2'>Team2 res</td><td class='tab_txt'>Draw Out</td></tr>"+
											"<tr><td class='tab_txt'>Lose In</td><td>Team1 res</td><td colspan='2'>Team2 res</td><td class='tab_txt'>Lose Out</td></tr>"+
										"</table>"+
									  "</td>"+
									  "<td>"+
										"<table id='wdl_all"+line_i+"' class='inner_wdl_tab'>"+
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


//-----------wdl data disply
/** retrieve the data from json obj and dysplay it in the appropriate match prediction line*/
function wdlExtrapolate(wdlData, compId){
	compId=0;
	wdlData={
  "Haugesund": "7;4;4;3;1;5",
  "Sogndal": "4;3;6;4;2;5",
  "Start": "0;0;4;5;8;7",
  "Brann": "8;4;3;2;0;7",
  "Strømsgodset": "8;2;3;2;1;8",
  "Aalesund": "4;3;4;1;4;8",
  "Bodø / Glimt": "4;3;3;3;6;5",
  "Odd": "6;6;3;2;4;3",
  "Valerenga": "5;3;3;3;4;6",
  "Tromsø": "5;3;3;3;4;6",
  "Viking": "4;6;4;2;4;4",
  "Rosenborg": "12;6;0;5;0;1",
  "Sarpsborg 08": "6;4;3;3;3;5",
  "Stabæk": "3;3;1;5;7;5",
  "Molde": "7;4;3;2;2;6",
  "Lillestrøm": "4;1;3;5;5;6"
	}

	// get the data for all the teams of the competitions;
	// loop throught the mpl and fo every team found asigne the wdl data it corresponds to it;
	
	
	var mplBox = $('#mplBox_'+compId);
	var boxTrs = $(mplBox).find('>tbody >tr:odd');
	//var boxTrs = $(mplBox).children('tbody').find('[data-toggle]');//find('>tbody >tr:odd');
	for(var i=0;i<boxTrs.length;i++){
		var tempTeam=$(boxTrs.get(i)).children('td').get(1);
		var t1= $(tempTeam).text();
		var tempSplit = wdlData[t1].split(';');
		var t1obj={name:t1, winIn:tempSplit[0], winOut:tempSplit[1], drawIn:tempSplit[2], drawOut:tempSplit[3], loseIn:tempSplit[4], loseOut:tempSplit[5]};

		tempTeam=$(boxTrs.get(i)).children('td').get(6);
		var t2= $(tempTeam).text();
		tempSplit = wdlData[t2].split(';');
		var t2obj={name:t2, winIn:tempSplit[0], winOut:tempSplit[1], drawIn:tempSplit[2], drawOut:tempSplit[3], loseIn:tempSplit[4], loseOut:tempSplit[5]};
		

		// console.log(t1obj);
		// console.log(t2obj);

		fillWDLTr(t1obj,t2obj,compId,i);
		//drawColumnChart(t1obj,t2obj,compId*1000+i);
	}
}

function fillWDLTr(t1, t2,compId, i){
	var j=1000*compId+i;//mpl line var
	
	
	var tab=$('#wdl_tot'+j);
	var tds=$(tab).find('td');
	
	tds[0].innerHTML=t1.name;	
	// $(tds[0]).css('text-decoration', 'underline')
	tds[1].innerHTML=t2.name;
	// $(tds[1]).css('text-decoration', 'underline')

	tds[3].innerHTML= parseInt(t1.winIn) + parseInt(t1.winOut);
	// $(tds[3]).css("font-weight","Bold");
	tds[4].innerHTML=parseInt(t2.winIn) + parseInt(t2.winOut);
	// $(tds[4]).css("font-weight","Bold");
	tds[7].innerHTML=parseInt(t1.drawIn) + parseInt(t1.drawOut);
	// $(tds[7]).css("font-weight","Bold");
	tds[8].innerHTML=parseInt(t2.drawIn) + parseInt(t2.drawOut);
	// $(tds[8]).css("font-weight","Bold");
	tds[11].innerHTML=parseInt(t1.loseIn) + parseInt(t1.loseOut);
	// $(tds[11]).css("font-weight","Bold");
	tds[12].innerHTML=parseInt(t2.loseIn) + parseInt(t2.loseOut);
	// $(tds[12]).css("font-weight","Bold");

	var tr0=document.createElement('tr');
	var td0=document.createElement('td');
	$(td0).attr('colspan','5');
	td0.innerHTML= wdl_tot_txt; 
	// $(td0).css({fontSize: "14pt" //color: "red"});
	// $(td0).css("font-weight","Bold");
	$(tr0).append(td0);
	var f_td =$(tab).find('tr')[0];
	$(tr0).insertBefore(f_td);
	
	$(tab).height(300);
	$(tab).width(300);
	// $(tab).css("background-color", "#d7dde5");
	// $(tab).css({'border-right':'2px solid red'});	
	// $(tab).find('tr').css({'border-bottom':'2pt solid white'});
	// $(tab).find('tr:last').css({'border-bottom':'none'});
	
	
	tab=$('#wdl_op'+j);
	tds=$(tab).find('td');

	tds[0].innerHTML=t1.name;	
	// $(tds[0]).css('text-decoration', 'underline')
	tds[1].innerHTML=t2.name;
	// $(tds[1]).css('text-decoration', 'underline')

	tds[3].innerHTML=t1.winIn ;
	// $(tds[3]).css("font-weight","Bold");
	tds[4].innerHTML= t2.winOut;
	// $(tds[4]).css("font-weight","Bold");

	tds[7].innerHTML=t1.drawIn ;
	// $(tds[7]).css("font-weight","Bold");
	tds[8].innerHTML= t2.drawOut;
	// $(tds[8]).css("font-weight","Bold");
	tds[11].innerHTML=t1.loseIn;
	// $(tds[11]).css("font-weight","Bold");
	tds[12].innerHTML= t2.loseOut;
	// $(tds[12]).css("font-weight","Bold");

	var tr0=document.createElement('tr');
	var td0=document.createElement('td');
	$(td0).attr('colspan','5');
	td0.innerHTML= wdl_op_txt;
	// $(td0).css({fontSize: "14pt"});
	// $(td0).css("font-weight","Bold");
	$(tr0).append(td0);
	var f_td =$(tab).find('tr')[0];
	$(tr0).insertBefore(f_td);
	$(tab).height(300);
	$(tab).width(300);
	// $(tab).css("background-color", "#d7dde5");
	
	// $(tab).find('tr').css({'border-bottom':'2pt solid white'});
	// $(tab).find('tr:last').css({'border-bottom':'none'});
	// $(tab).css({'border-left' :'2px solid white'});
	// $(tab).css({'border-right':'2px solid white'});	
	//$(tab).css({'border':'2px solid white'});	

	
	
	tab=$('#wdl_all'+j);
	tds=$(tab).find('td');

	tds[0].innerHTML=t1.name;	
	// $(tds[0]).css('text-decoration', 'underline')
	tds[1].innerHTML=t2.name;
	// $(tds[1]).css('text-decoration', 'underline')

	tds[3].innerHTML=t1.winIn ;
	// $(tds[3]).css("font-weight","Bold");
	tds[4].innerHTML=t2.winIn ;
	// $(tds[4]).css("font-weight","Bold");

	tds[7].innerHTML= t1.winOut;
	// $(tds[7]).css("font-weight","Bold");
	tds[8].innerHTML= t2.winOut;
	// $(tds[8]).css("font-weight","Bold");

	tds[11].innerHTML=t1.drawIn ;
	// $(tds[11]).css("font-weight","Bold");
	tds[12].innerHTML=t2.drawIn ;
	// $(tds[12]).css("font-weight","Bold");

	tds[15].innerHTML= t1.drawOut;
	// $(tds[15]).css("font-weight","Bold");
	tds[16].innerHTML= t2.drawOut;
	// $(tds[16]).css("font-weight","Bold");


	tds[19].innerHTML=t1.loseIn;
	// $(tds[19]).css("font-weight","Bold");
	tds[20].innerHTML=t2.loseIn;
	// $(tds[20]).css("font-weight","Bold");

	tds[23].innerHTML= t1.loseOut;
	// $(tds[23]).css("font-weight","Bold");
	tds[24].innerHTML= t2.loseOut;
	// $(tds[24]).css("font-weight","Bold");


	
	var tr0=document.createElement('tr');
	var td0=document.createElement('td');
	$(td0).attr('colspan','5');
	td0.innerHTML=wdl_all_txt;
	// $(td0).css({fontSize: "15pt"//color: "red"});
	$(tr0).append(td0);
	var f_td =$(tab).find('tr')[0];
	$(tr0).insertBefore(f_td);
	$(tab).height(300);
	$(tab).width(400);
	// $(tab).css("background-color", "#d7dde5");
	//$(tab).css({'border-left':'2px solid red'});
	// $(tab).find('tr').css({'border-bottom':'2pt solid white'});
	// $(tab).find('tr:last').css({'border-bottom':'none'});
}


function  wdltgswitch(idx){
	//show hide the win draw lose table graph 
	console.log("wdltgswitch");

	if( $('#wdl_checkTable'+idx).is(':checked')){
		//$('#form_check'+idx).prop('checked',false);
		$('#wdl_tab_tr'+idx).show();	
	}
	else{
		//$('#form_check'+idx).prop('checked',true);
		$('#wdl_tab_tr'+idx).hide();
	}



	if( $('#wdl_checkGraph'+idx).is(':checked')){
		var isDrawn = $('#wdl_gra_tr'+idx).find('input').attr('value');
 
		if(isDrawn==="YES"){
			$('#wdl_gra_tr'+idx).show();	
		}
		else if(isDrawn==="NO"){
			$('#wdl_gra_tr'+idx).show();	
			$('#wdl_gra_tr'+idx).find('input').attr('value','YES');
			drawWinDrawLose(idx);
		}
		else if(isDrawn===undefined){
			console.log("this tr should not be opened");
		}
	}
	else{
		console.log("chart @ idx: "+idx+" id "+isDrawn);
		$('#wdl_gra_tr'+idx).hide();
	}
}

