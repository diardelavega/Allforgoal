/**
* The file contains the functions needed to gather the data, designe and show the charts
* in the appropriate divs in the collapsable panel of their match
*/

google.charts.load('current', {'packages':['line','corechart','bar']});
//google.charts.load('current', {'packages':['line','corechart','bar']});
// google.charts.setOnLoadCallback(drawChart);

/* draw and show all the column functions for the wdl data*/
function drawColumnChart(mpl_idx) {
/*to draw the chart firs the data must be collected from the tale that it is written
	so: from the data @ all wdl, gather the data, recreate obj1 & obj2; then draw the chart; */

	// var obj1,obj2;

	
	console.log(obj1+" "+obj2+" "+mpl_idx);
	 barOfTot(obj1,obj2,mpl_idx);
	 barOfOp(obj1,obj2,mpl_idx);
	 barOfAll(obj1,obj2,mpl_idx);
}


/* total wdl data chart*/
function barOfTot(obj1, obj2, idx){

	var data = google.visualization.arrayToDataTable([
	  ['category', obj1.name, obj2.name],
	  ['Wins', obj1.winIn + obj1.winOut, obj2.winIn + obj2.winOut],
	  //['Win Out', t1.winsOut, t2.winsOut],
	  ['Draws', obj1.drawIn + obj1.drawOut, obj2.drawIn + obj2.drawOut],
	  //['Draw Out', t1.drawsOut, t2.drawsOut],
	  ['Looses', obj1.loseIn + obj1.loseOut, obj2.loseIn + obj2.loseOut]
	  //['Loose Out', t1.losesOut, t2.losesOut]
	]);

	var options = {
		title: 'Total results of the teams ',
	   	chartArea: {
			left: '10%',bottom: '10%',
			right:'5%', top:'15%',
			//width: '90%', height: '90%',
			'backgroundColor': {
				 //'fill': '#BFC8D4',
				 'opacity': 10
			}
		},
		 
		legend: { position: 'top'},
		// backgroundColor:{fill:'#d7dde5'},
		height:350,
		width:300,
		 //bars: 'horizontal' // Required for Material Bar Charts.
	};

	//var chart = new google.charts.Bar(document.getElementById('#totBar'+idx));
	console.log('totBar'+idx);
	var chart = new google.visualization.ColumnChart(document.getElementById('totBar'+idx));
	chart.draw(data, options);
}

/*operative wdl data chart*/
function barOfOp(obj1, obj2, idx){
	//mld=todaysMatches[idx];

        var data = google.visualization.arrayToDataTable([
		  ['category', obj1.name, obj2.name],
          ['Win In vs Out', obj1.winIn, obj2.winOut],
		  //['Win Out', t1.winsOut, t2.winsOut],
		  ['Draw In vs Out', obj1.drawIn, obj2.drawOut],
		  //['Draw Out', t1.drawsOut, t2.drawsOut],
		  ['Loose In vs Out', obj1.loseIn, obj2.loseOut]
		  //['Loose Out', t1.losesOut, t2.losesOut]
        ]);

        var options = {
          /*chart: {
            // title: 'Teams Operative Results',
            // subtitle: 'Home Team In, Away Team Out Results',
          // },*/
		  title: 'Home Team results In vs Away Team results Out ',
		  
		  chartArea: {
				left: '10%',bottom: '10%',
				right:'5%', top:'15%',
				//width: '90%', height: '90%',
					'backgroundColor': {
					 //'fill': '#BFC8D4',
					 'opacity': 10
				}
			},
		  legend: { position: 'top'},
         bar: { groupWidth: '55%' },
		  // //isStacked: true,
		   height:350,
		  width:300
         };

        
	 	//var chart = new google.visualization.ColumnChart(chartId);
	 	console.log('operativeBar'+idx);
		 var chart = new google.visualization.ColumnChart(document.getElementById("operativeBar"+idx));
        // var chart = new google.charts.Bar(document.getElementById('operativeBar'));
        chart.draw(data, options);
}

/*all wdl data chart*/
function barOfAll(obj1,obj2,idx){
	//mld=todaysMatches[idx];
	
	var data = google.visualization.arrayToDataTable([
	  ['category', obj1.name, obj2.name],
	  ['Win In', obj1.winIn, obj2.winIn],
	  ['Win Out', obj1.winOut, obj2.winOut],
	  ['Draw In', obj1.drawIn, obj2.drawIn],
	  ['Draw Out', obj1.drawOut, obj2.drawOut],
	  ['Loose In', obj1.loseIn, obj2.loseIn],
	  ['Loose Out', obj1.loseOut, obj2.loseOut]
	]);

    var options = {
         /*material options
		  chart: {
          title: 'Teams Concrete Results',
          subtitle: 'Compared In vs Out',
         ,*/
		 title: 'In vs Out results of the teams ',
		 chartArea: {
			left: '10%',bottom: '10%',
			right:'5%', top:'15%',
			//width: '90%', height: '90%',
			'backgroundColor': {
			 //'fill': '#BFC8D4',
			 'opacity': 10
			}
		 },
		  //backgroundColor:{fill:'#d7dde5'},
			legend: { position: 'top'},
		 	height:350,
		 	width:400
          //	bars: 'horizontal' // Required for Material Bar Charts.
	};

	//var chart = new google.charts.Bar(document.getElementById('allBar'));

	console.log('allBar'+idx);
	var chart = new google.visualization.ColumnChart(document.getElementById('allBar'+idx));
	chart.draw(data, options);
}

function wdlGatherAndRecreate(mpl_idx){
	 var tds=$('wdl_all'+line_i).find('td');
}