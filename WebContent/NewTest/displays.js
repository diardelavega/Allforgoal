/**
 * create the box, on which the data from the mpl in the all matches page is
 * going to be displayed
 */
function mplBoxCreate(res) {
var tab= document.createElement("table");
var tdTxt;
for(var i=0;i<allMAtchPredLine.length;i++)	{

	tdTxt="<tr data-toggle='collapse' data-target='#collapsePanel"+i+"' id='rowmld"+i+"' onclick= 'clickCatch("+i+");' >"+
				"<td><span class='glyphicon glyphicon-triangle-bottom' data-toggle='collapse' data-target='#collapsePanel"+i+"'> </td>"+
				"<td>home team	</td>  <td>FT1</td> <td>HT1</td> <td>Ht2</td>  <td>FT2</td>"+
				"<td>home win	</td>  <td>draw</td> <td>away win	</td> <td>over</td>  <td>under</td>"+
				"<td></td>  <td></td> "+
				"<td>HT: 1 > tot. Score	</td>  <td>HT: 2 < tot. Score	</td> "+
				"<td>HT: 2 > tot. Score	</td>  <td>tot. HT Score	</td> "+
				"<td>tot. FT Score</td>"+
			"</tr>"


	$("#tab1").children('tbody').append(tdTxt);
	$("#tab1").children('tbody').append(collapsablePannelCreator(i));
}
}