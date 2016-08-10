/**
 * ajax functions
 */
function matchPredLineAjaxCall() {
	$.ajax({
		url : "http://localhost:8080/Bast/rest/services/predwdl",
		type : 'GET',
		dataType : 'json',
		// contentType: 'application/json; charset=utf-8',
		success : function(response) {
			console.log("in ajaxx");
			$("#tit").html("Maracaibo!!");
			
			matchPredLineTrBuilder(response);
			matchPredLineShow(response);
			wdlTableFiller(response);
		},
		error : function(error, stat, cod) {
			console.log(error);
			console.log(stat);
			console.log(cod);
		}
	});
}

function wdlAjaxCall(compId) {
	console.log("pushing " + compId);
	$.ajax({
		url : "http://localhost:8080/Bast/rest/services/wdldata/" + compId,
		type : 'GET',
		dataType : 'json',
		// contentType: 'application/json; charset=utf-8',
		success : function(response) {
			console.log("in ajaxx wdl");
			$("#tit").html("Baracura!!");//
			wdlTableFiller(response);
		},
		error : function(error, stat, cod) {
			console.log(error);
			console.log(stat);
			console.log(cod);
		}
	});
}

function redWeekMatchesAjaxCall(cId) {
/*its called by an onclick => comp id is compid * 100 + idx
 * */
	console.log("in ajaxx redWeekMatchesAjaxCall");
	var nrvars=compIdxSplitter(cId);
//	console.log("pushing " + nrvars[0]+ " "+nrvars[1]);
	$.ajax({
		url : "http://localhost:8080/Bast/rest/services/redweekmatches/" + nrvars[0],
		type : 'GET',
		dataType : 'text',
		// contentType: 'application/json; charset=utf-8',
		success : function(response) {
			console.log("in ajaxx redWeekMatchesAjaxCall  response --------> ");
			console.log(response);
			$("#tit").html("Bamboleio!!");//
			localStorage.setItem(nrvars[0],response )
			redWeekHandle(cId,response);
		},
		error : function(error, stat, cod) {
			console.log(error);
			console.log(stat);
			console.log(cod);
		}
	});
}

function redCommonAdvAjaxCall(compId) {
	console.log("pushing " + compId);
	$.ajax({
		url : "http://localhost:8080/Bast/rest/services/redcommon/" + compId,
		type : 'GET',
		dataType : 'text',
		// contentType: 'application/json; charset=utf-8',
		success : function(response) {
			console.log("in ajaxx wdl");
			$("#tit").html("Bolerossss!!");//
//			wdlTableFiller(response);
//			console.log(response);
			papaparse(response);
		},
		error : function(error, stat, cod) {
			console.log(error);
			console.log(stat);
			console.log(cod);
		}
	});
}