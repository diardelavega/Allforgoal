/**
 * conn estabilihed and working but cann not make it loop asking for the other
 * competitions
 */

function mlpCall(datstamp, seri) {
	console.log(seri)
	$.ajax({
		url : "http://localhost:8080/Bast/rest/services/mpl/" + datstamp + "/"
				+ seri,
		type : 'GET',
		dataType : 'json',
		// contentType: 'application/json; charset=utf-8',
		success : function(response) {
			console.log("in success");
			// $("#tit").html("Maracaibo!!");
			if (response.msg !== undefined || response.msg !== null) {
				console.log("exit msg");
				console.log(response);
				return;
			}

			$("#div1").append("</br>");
			$("#div1").append(response.competition);
			seri++;
			mlpCall(datstamp, seri);
			// matchPredLineTrBuilder(response);
			// matchPredLineShow(response);
			// wdlTableFiller(response);
		},
		error : function(error, stat, cod) {
			console.log(error);
			console.log(stat);
			console.log(cod);
		}
	});
}

;