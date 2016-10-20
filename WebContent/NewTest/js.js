/**
 * conn estabilihed and working but cann not make it loop asking for the other
 * competitions
 */
// http://snf-718978.vm.okeanos.grnet.gr:8080/Bast/NewTest/p1.html
var baseurl = "http://snf-718978.vm.okeanos.grnet.gr:";
var localhost = "http://localhost:";
var port = 8080;
var httpurl = localhost + port;

var jj=0;
/**
 * Match prediction line. call the rest api that returns the data for the
 * competitions of the date and the newxt serinumber that we give it. Serinumber
 * is cardinal number of the compstition in the map that holds them (so basicly
 * the next in line) ..........................................................
 * mpl call calls all the service untill it returns a exit msg.
 */
function mlpCall(datstamp, seri) {
	
	$.ajax({
		url : httpurl + "/Bast/rest/services/mpl/" + datstamp + "/" + seri,
//		beforeSend : function(request) {
//			request.setRequestHeader("Authorization", "Negotiate");
//		},
		type : 'GET',
//		crossDomain : true,
		dataType : 'json',
		success : function(response) {
			console.log(response);
			console.log("in success");
			if (response.hasOwnProperty('msg')) {
				console.log("exit msg");
				console.log(response.msg);
				return;
			}
			/*
			var i = 0;
			for (; i < response.length; i++) {
				$("#div1").append("coump: " + response[i].competition + " ");
				$("#div1").append("coumpId: " + response[i].compId + " ");
				$("#div1").append("serinr: " + response[i].serinr + " ");
				$("#div1").append( "i, lfml.length: " + i + ", " + response[i].lfml.length + " ");
				$("#div1").append("</br>");
				console.log(response[i].lfml.length);
				console.log(response[i].lfml[0]);
				for (var k = 0; k < response[i].lfml.length; k++) {
					$("#div1").append(JSON.stringify(response[i].lfml[k]));
					$("#div1").append("</br>");
				}
			}*/
			mplBoxCreate(response);
			/*$("#div1").append("</br>");
			i--;
			console.log("serinr-i :" + response[i].competition);
			seri = response[i].serinr;*/
			mlpCall(datstamp, seri);
			// matchPredLineTrBuilder(response);
			// matchPredLineShow(response);
			// wdlTableFiller(response);
		},
		error : function(error, stat, cod) {
			console.log(error);
			console.log(error.responseText);
			console.log(error.length);
			console.log(stat);
			console.log(cod);
		}
	});
};


/** call for the win draw lose data of the specific competition*/
function wdlCall(cid){
	compId=cid/1000;
$.ajax({
		url : httpurl + "/Bast/rest/services/compWinDrawLose/" + compId,
		type : 'GET',
		crossDomain : true,
		dataType : 'json',
		success : function(response) {
			console.log("in success");
			if (response.hasOwnProperty('msg')) {
				console.log("exit msg");
				console.log(response.msg);
				return;
			}
			
			wdlExtrapolate(response,compId);
		},
		error : function(error, stat, cod) {
			console.log(error);
			console.log(error.responseText);
			console.log(error.length);
			console.log(stat);
			console.log(cod);
		}
	});
}



/**
 * Call the service API that returns all the matches done so far in that specific competition 
 */
function compWeeksDataCall(cid) {
	console.log(seri)
	$.ajax({
		url : httpurl + "/Bast/rest/services/reducedweeksmatches/" + cid,
		type : 'GET',
		crossDomain : true,
		dataType : 'json',
		success : function(response) {
			console.log("in success");
			if (response.hasOwnProperty('msg')) {
				console.log("exit msg");
				console.log(response.msg);
				return;
			}
			
			
		},
		error : function(error, stat, cod) {
			console.log(error);
			console.log(error.responseText);
			console.log(error.length);
			console.log(stat);
			console.log(cod);
		}
	});
};
