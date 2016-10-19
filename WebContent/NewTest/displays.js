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
	var thed= document.createElement("thead");
	
	var hedTxt="<tr><td style='padding-left:40px'></td><td colspan='5'>"+compName+"</td> <td colspan='10' style='padding-right:20px'>"+country+"</td></tr>";
	$(thed).css("background-color", "#cbcdef")
	$(thed).append(hedTxt);
	
	var tbodRow1="<tr id='rowmld"+i+"' class='mplheadrow' >"+"<td><span      class='chevron fa fa-fw'  > </td>"+  "<td>Home</td>  <td>FT</td> <td>HT</td> <td>HT</td>  <td>FT</td> <td>Away</td>"+ "<td> 1 </td>  <td>X</td> <td>2</td> <td>Over</td>  <td>Under</td></tr>"
	var tbod= document.createElement("tbody");
	$(tbod).append(tbodRow1);
	//$(tbod).find(td).css("padding","20px");
	
	$(tab).append(thed);
	$(tab).append(tbod);
	
	var tdTxt;
	for(var i=0;i<mplResp.lfml.length;i++)	{
		var idval=compId*1000+i;
		tdTxt="<tr  data-toggle='collapse' data-target='#collapsePanel"+idval+"' id='rowmld"+idval+"' onclick= 'clickCatch("+idval+");' >"
		+ "<td ><span   class='indicator glyphicon glyphicon-chevron-right  pull-left' >"
		+ "</td>"+ "<td>"+mplResp.lfml[i].t1+"</td>  <td>"+mplResp.lfml[i].ft1+"</td> <td>"+mplResp.lfml[i].ht1+"</td> <td>"+mplResp.lfml[i].ht2+"</td>  <td>"+mplResp.lfml[i].ft2+"</td> <td>"+mplResp.lfml[i].t2+"</td>"+  "<td> "+mplResp.lfml[i].h1+" </td>  <td>"+mplResp.lfml[i].hx+"</td> <td>"+mplResp.lfml[i].h2+"</td> <td>"+mplResp.lfml[i].so+"</td>  <td>"+mplResp.lfml[i].su+"</td></tr>";

		$(tbod).append(tdTxt);
		$(tbod).append(collapsablePannelCreator(idval));
	}
	$("#div1").append(tab);
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
	