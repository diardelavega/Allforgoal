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