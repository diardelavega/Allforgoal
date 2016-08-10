//Functional scripts

//============Storage Funcs
function testStorage() {
	console.log("on storage");
	try {
		console.log("trying");
		var storage = window.sessionStorage;
		var x = '__storage_test__';
		storage.setItem(x, x);
		console.log(storage.getItem(x));
		storage.removeItem(x);
		return true;
	} catch (e) {
		console.log("failing");
//		console.log(e);
		 return false;
	}
}

// ----------------------

// =========================WDL Funcs
function old_wdltgswitch(idx) {
	// show hide the win draw lose table graph
	console.log("wdltgswitch");

	if ($('#wdl_checkTable' + idx).is(':checked')) {
		// $('#form_check'+idx).prop('checked',false);
		$('#wdl_tab_tr' + idx).show();
	} else {
		// $('#form_check'+idx).prop('checked',true);
		$('#wdl_tab_tr' + idx).hide();
	}

	if ($('#wdl_checkGraph' + idx).is(':checked')) {
		// console.log(allIdxTrackArray.get(idx));

		if (allIdxTrackArray.get(idx) === "draw") {
			$('#wdl_gra_tr' + idx).show();
		} else if (allIdxTrackArray.get(idx) === "check") {
			$('#wdl_gra_tr' + idx).show();
			drawWinDrawLose(idx);
		} else if (allIdxTrackArray.get(idx) === undefined) {
			console.log("this tr should not be opened");
		}
	} else {
		console.log(allIdxTrackArray.get(idx));
		$('#wdl_gra_tr' + idx).hide();
	}
}

function wdltgswitch(idx) {
	// show hide the win draw lose table graph

	if ($('#wdl_checkTable' + idx).is(':checked')) {
		$('#wdl_tab_tr' + idx).show();
	} else {
		$('#wdl_tab_tr' + idx).hide();
	}

	if ($('#wdl_checkGraph' + idx).is(':checked')) {
		if ($('#wdl' + idx).attr('draw_data') == "true") {
			// check if data_draw is true-> graph is already drawn
			$('#wdl_gra_tr' + idx).show();
		} else {
			var t12 = gatherWDLTableData(idx); // gather data from the table
			$('#wdl_gra_tr' + idx).show(); // show
			drawColumnChart(t12[0], t12[1], idx);
			$('#wdl' + idx).attr('draw_data', true);
		}
	} else {
		$('#wdl_gra_tr' + idx).hide();
	}

}

function gatherWDLTableData(idx) {
	console.log("gatherWDLTableData");
	var tds = $('#wdl_all' + idx + '  td');

	// check if a td.text is valid number or not
	// if no, the call ajax and get the wdl for that competition

	var t1 = {
		name : tds[1].textContent,
		winIn : Number(tds[4].textContent),
		winOut : Number(tds[8].textContent),
		drawIn : Number(tds[12].textContent),
		drawOut : Number(tds[16].textContent),
		loseIn : Number(tds[20].textContent),
		loseOut : Number(tds[24].textContent)
	};

	var t2 = {
		name : tds[2].textContent,
		winIn : Number(tds[5].textContent),
		winOut : Number(tds[9].textContent),
		drawIn : Number(tds[13].textContent),
		drawOut : Number(tds[17].textContent),
		loseIn : Number(tds[21].textContent),
		loseOut : Number(tds[25].textContent)
	};
	return ([ t1, t2 ]);
}
// -----------------------------WDL END funcs

// =============================FORM Function
function formswitch(idx) {
	console.log("formswitch");

	if ($('#form_check' + idx).is(':checked')) {
		// $('#form_check'+idx).prop('checked',false);
		$('#form_chart' + idx).show();
	} else {
		// $('#form_check'+idx).prop('checked',true);
		$('#form_chart' + idx).hide();
	}

	if ($('#atackForm_check' + idx).is(':checked')) {
		// $('#atackForm_check'+idx).prop('checked',false);
		$('#atackForm_chart' + idx).show();
	} else {
		// $('#atackForm_check'+idx).prop('checked',true);
		$('#atackForm_chart' + idx).hide();
	}

	if ($('#deffForm_check' + idx).is(':checked')) {
		// $('#deffForm_check'+idx).prop('checked',false);
		$('#deffForm_chart' + idx).show();
	} else {
		// $('#deffForm_check'+idx).prop('checked',true);
		$('#deffForm_chart' + idx).hide();
	}
}

// -----------------------------FORM END funcs

// ==================Help func


function formtt(res1, res2, io, adv, weekVal, val, valDat){
	var color=colorCode(res1, res2);
	var txt;
	if(io==="In"){
		txt=" <div style='background-color:"+color+"'; width='100'> " +
				"<h4 > In "+ res1 +" - "+res2 +"  "+ adv+ " </h4>"+
				"<h4> week: "+ weekVal +",  "+ val+": "+numberFormat(valDat)+"</h4>"+
				"</div>";
	}
	else if (io==="Out"){
		txt=" <div style='background-color:"+color+"'; width='100'> " +
				"<h4>"+adv+" "+ res1 +" - "+res2 +" Out </h4>"+
				"<h4> week: "+ weekVal +",  "+ val+": "+numberFormat(valDat)+"</h4>"+
				"</div>";
	}
	return txt;
}

function progtt(res1, res2, io, adv){
	var color=colorCode(res1, res2);
	var txt;
	if(io==="In"){
		txt=" <div style='background-color:"+color+"'; width='100'> " +
				"<h4> In "+ res1 +" - "+res2 +"  "+ adv+ " </h4>"+
				"</div>";
	}
	else if (io==="Out"){
		txt=" <div style='background-color:"+color+"'; width='100'> " +
				"<h4>"+adv+" "+ res1 +" - "+res2 +" Out </h4>"+
				"</div>";
	}
	return txt;
}




function colorCode(myResult, advResult) {
	if (myResult > advResult) {
		return ("#81ea4c");
	} else if (myResult < advResult) {
		return ("#f66e24");
	} else if (myResult == advResult) {
		return ("#8aa3c5");
	} else {
		console.log("Something is wrong in colour code data");
		console.log("myResult = " + myResult + " &  advResult = " + advResult);
		return ("#4432b8");
		// return("transparent");
	}
}

function progressPoint(myResult, advResult) {
	if (myResult > advResult) {
		return (1);
	} else if (myResult < advResult) {
		return (-1);
	} else if (myResult == advResult) {
		return (0);
	} else {
		console.log("Something is wrong in colour code data");
		console.log("myResult = " + myResult + " &  advResult = " + advResult);
		// return("transparent");
	}
}

function numberFormat(val) {
	// var a=val.toFixed(3);
	return (Number(Math.round(val + 'e' + 3) + 'e-' + 3));
}

function compIdxSplitter(cid) {
	var compId = Number(Math.round(cid / 100));
	var idx = cid - (compId * 100);
	return ([ compId, idx ]);
}