//To draw the charts needed
		
	
google.charts.load('current', {'packages':['line','corechart','bar']});
//google.charts.setOnLoadCallback(drawChart);
	
function genFormData() {
	//array = [];
	for (var i = 0; i < t1.form.length; i++) {
	  //var x = array.length+i;
		  /*y = Math.floor(Math.random() * (10 - (-10) + 1)) + (-10);
		  z = Math.floor(Math.random() * (10 - (-10) + 1)) + (-10);
		  k = 'hello my man the val is \n ';*/
		  if(i==10){
			  formArray.push([i, t1.form[i], "<h4>"+t1.adv[i]+"</h4>", , ""]);
		  }else{
	  formArray.push([i, t1.form[i], "<h4>"+t1.adv[i]+"</h4>", t2.form[i], "<h3>+t2.adv[i]]+</h3>"]);
		  }
	}
	$("#displayD").html(formArray[1]);
	//return array;
	drawFormChart();
	//drawAtackChart();
	//drawDeffChart();
}


function commonAdvTabShower(tid, t_1, t_2){
	//is called from common adversaries to display the direct matches and common adversaries of the two teams 
	console.log("in commonAdvTabShower");


	var tab= $("#common"+tid);
	var tr = document.createElement('tr');
	
	var td1 = document.createElement('td');
	$(td1).css("background-color",colorCode(t_1.t1Res, t_1.t2Res));
	
	var txt;
	if(t_1.io==='In'){
		txt='<span style="float:left">week: '+t_1.week+'</span> <b>'+t_1.tname+" &emsp;<big> "+t_1.t1Res+" - "+t_1.t2Res+" </big>&emsp; "+t_1.adv+'</b>';
	}else{
		txt='<span style="float:left">week: '+t_1.week+'</span> <b>'+t_1.adv +" &emsp; <big> "+t_1.t2Res+" - "+t_1.t1Res+" </big>&emsp; "+t_1.tname+'</b>';
	}
	td1.innerHTML=txt;
	$(tr).append(td1);
	
	if(t_2!==null){
		var td2 = document.createElement('td');
		$(td2).css("background-color",colorCode(t_2.t1Res, t_2.t2Res));
		
		if(t_2.io==='In'){
			txt='<span style="float:left">week: '+t_2.week+'</span> <b>'+t_2.tname+" &emsp; <big> "+t_2.t1Res+" - "+t_2.t2Res+" </big>&emsp; "+t_2.adv+'</b>';
		}else{
			txt='<span style="float:left">week: '+t_2.week+'</span> <b>'+t_2.adv +" &emsp; <big> "+t_2.t2Res+" - "+t_2.t1Res+" </big>&emsp; "+t_2.tname+'</b>';
		}
		td2.innerHTML=txt;
		$(tr).append(td2);
		
	}
	else{
		$(td1).attr('colspan',2);
	}
	
	$(tab).append(tr);	
}
	
//==================Help functions 

function formChartData(chId,progId){
	console.log("DDDAAATTTTAAAA");
	formDataExtraction(t1,t2);
	drawFormChart(t1,t2, chId);
	drawProgressChart(t1,t2, progId);

	//drawColumnChart();
}
//---------------------------end of help functions



//====================================Progress chart
function drawProgressChart(idx,homeTeam,awayTeam){

	var data = new google.visualization.DataTable();
	data.addColumn('number','week')
	data.addColumn('number', homeTeam);
	data.addColumn({type: 'string', role: 'tooltip', 'p': {'html': true}});
	data.addColumn('number',awayTeam);			
	data.addColumn({type: 'string', role: 'tooltip', 'p': {'html': true}});

	data.addRows(progressArray);

	var options = {
		chartArea: {
			left: '10%',bottom: '10%',
			right:'15%', top:'10%',
			//width: '90%', height: '90%',
			'backgroundColor': {
				'fill': '#BFC8D4',
				'opacity': 10
			 }
		 },
		lineWidth: 3,
		width: 900,
		height: 450,
		backgroundColor: '#d7dde5',
		title:  'Team RESULT progres throught the weeks',
		titleTextStyle: { color: '#5c5c5c', fontName: 'Montserrat', fontSize: '15' },
		tooltip: {isHtml: true},
		axes:{
			x:{
				0:{side:'top'}
			}
		},
		legend: { position: 'right',textStyle: {fontSize: 10} },
		tooltip: {isHtml: true},
		hAxis: {
			gridlines: { color:  'f2f2f2'},
			//gridlines: { count:  0},
			baselineColor: 'transparent',
			baselineColor: '#587dae',
			title: 'Weeks',
			titleTextStyle:{fontSize: 10}
		},
		vAxis: {
		 baselineColor: '#587dae',
			//gridlines: { count: 6 },
			gridlines: { color:  'f2f2f2'},
			title: 'Value',
			titleTextStyle:{fontSize: 10}
		},
		 //tooltip:{trigger:'none'},
		trendlines: {
			trigger:'none',
			 tooltip:false,
		  0: {type: 'linear', color: '#8986dd', opacity: .5,lineWidth:3, visibleInLegend: true, labelInLegend:' trend'},
		  1: {type: 'linear', color: '#d07b24', opacity: .5,lineWidth:3, visibleInLegend: true,labelInLegend:' trend'}
		},
		
		colors: [ 'blue', 'red', 'green', 'black','yellow', 'gray'],
		pointSize: 5,
		pointShape: 'square'
	};

	var chart = new google.visualization.LineChart(document.getElementById('progress_chart'+idx));
	//var chart = new google.charts.Line(document.getElementById('the_chart'));

	//google.visualization.events.addListener(chart, 'ready', animate);
	chart.draw(data, google.charts.Line.convertOptions(options));
}
//------------------------------------END OF Progress Charts

//==================Line chart for FORM		
function drawFormChart(idx,homeTeam,awayTeam) {

	var data = new google.visualization.DataTable();
	data.addColumn('number','week')
	data.addColumn('number', homeTeam);
	data.addColumn({type: 'string', role: 'tooltip', 'p': {'html': true}});
	data.addColumn('number', awayTeam);			
	data.addColumn({type: 'string', role: 'tooltip', 'p': {'html': true}});

	data.addRows(formArray);
	
	var options = {
		chartArea: {
			left: '10%',bottom: '10%',
			right:'15%', top:'10%',
			//width: '90%', height: '90%',
			'backgroundColor': {
				'fill': '#BFC8D4',
				'opacity': 10
			 }
		 },
		lineWidth: 5,
		width: 900,
		height: 450,
		backgroundColor: '#d7dde5',
		title:  'Team FORM progres throught the weeks',
		titleTextStyle: { color: '#5c5c5c', fontName: 'Montserrat', fontSize: '15' },
		tooltip: {isHtml: true},
		axes:{
			x:{
				0:{side:'top'}
			}
		},
		legend: { position: 'right',textStyle: {fontSize: 10} },
		tooltip: {isHtml: true},
		hAxis: {
			gridlines: { color:  'f2f2f2'},
			//gridlines: { count:  0},
			baselineColor: 'transparent',
			baselineColor: '#587dae',
			title: 'Weeks',
			titleTextStyle:{fontSize: 10}
		},
		vAxis: {
		 baselineColor: '#587dae',
			//gridlines: { count: 6 },
			gridlines: { color:  'f2f2f2'},
			title: 'Value',
			titleTextStyle:{fontSize: 10}
		},
		 //tooltip:{trigger:'none'},
		trendlines: {
			 tooltip:false,
		  0: {type: 'linear', color: '#8986dd', opacity: .5,lineWidth:3, visibleInLegend: true, labelInLegend:' trend'},
		  1: {type: 'linear', color: '#d07b24', opacity: .5,lineWidth:3, visibleInLegend: true,labelInLegend:' trend'}
		},
		
		colors: [ 'blue', 'red', 'green', 'black','yellow', 'gray'],
		//pointSize: 5,
		//pointShape: 'square'
	};

	//var chart = new google.visualization.LineChart(chartId);
	var chart = new google.visualization.LineChart(document.getElementById('form_chart'+idx));
	//var chart = new google.charts.Line(document.getElementById('the_chart'));

	//google.visualization.events.addListener(chart, 'ready', animate);
	chart.draw(data, google.charts.Line.convertOptions(options));
	//form_form_flag=true;
}
		

function drawAtackChart(idx,homeTeam,awayTeam) {

	var data = new google.visualization.DataTable();
	data.addColumn('number','week')
	data.addColumn('number', homeTeam);
	data.addColumn({type: 'string', role: 'tooltip', 'p': {'html': true}});
	data.addColumn('number',  awayTeam);			
	data.addColumn({type: 'string', role: 'tooltip', 'p': {'html': true}});

	data.addRows(atackArray);

	var options = {
	
		chartArea: {
			left: '10%',bottom: '10%',
			right:'15%', top:'10%',
			//width: '90%', height: '90%',
			'backgroundColor': {
				'fill': '#BFC8D4',
				'opacity': 10
			 }
		 },
		lineWidth: 3,
		width: 900,
		height: 450,
		backgroundColor: '#d7dde5',
		title:  'Team Atack FORM progres throught the weeks',
		titleTextStyle: { color: '#5c5c5c', fontName: 'Montserrat', fontSize: '15' },
		tooltip: {isHtml: true},
		axes:{
			x:{
				0:{side:'top'}
			}
		},
		legend: { position: 'right',textStyle: {fontSize: 10} },
		tooltip: {isHtml: true},
		hAxis: {
			gridlines: { color:  'f2f2f2'},
			//gridlines: { count:  0},
			baselineColor: 'transparent',
			baselineColor: '#587dae',
			title: 'Weeks',
			titleTextStyle:{fontSize: 10}
		},
		vAxis: {
		 baselineColor: '#587dae',
			//gridlines: { count: 6 },
			gridlines: { color:  'f2f2f2'},
			title: 'Value',
			titleTextStyle:{fontSize: 10}
		},
		colors: [ 'blue', 'red', 'green', 'black','yellow', 'gray'],
		pointSize: 7,
		pointShape: 'square'
	};

	var chart = new google.visualization.LineChart(document.getElementById('atackForm_chart'+idx));
	//var chart = new google.charts.Line(document.getElementById('atackForm_chart'+idx));

	//google.visualization.events.addListener(chart, 'ready', animate);
	chart.draw(data, google.charts.Line.convertOptions(options));
}		
  
  
function drawDeffChart(idx,homeTeam,awayTeam) {
	var data = new google.visualization.DataTable();
	data.addColumn('number','week')
	data.addColumn('number',homeTeam);
	data.addColumn({type: 'string', role: 'tooltip', 'p': {'html': true}});
	data.addColumn('number',  awayTeam);			
	data.addColumn({type: 'string', role: 'tooltip', 'p': {'html': true}});

	data.addRows(deffArray);

	var options = {
		chartArea: {
			left: '10%',bottom: '10%',
			right:'15%', top:'10%',
			//width: '90%', height: '90%',
			'backgroundColor': {
				'fill': '#BFC8D4',
				'opacity': 10
			 }
		 },
		lineWidth: 3,
		width: 900,
		height: 450,
		backgroundColor: '#d7dde5',
		title:  'Team Defence FORM progres throught the weeks',
		titleTextStyle: { color: '#5c5c5c', fontName: 'Montserrat', fontSize: '15' },
		tooltip: {isHtml: true},
		axes:{
			x:{
				0:{side:'top'}
			}
		},
		legend: { position: 'right',textStyle: {fontSize: 10} },
		tooltip: {isHtml: true},
		hAxis: {
			gridlines: { color:  'f2f2f2'},
			//gridlines: { count:  0},
			baselineColor: 'transparent',
			baselineColor: '#587dae',
			title: 'Weeks',
			titleTextStyle:{fontSize: 10}
		},
		vAxis: {
		 baselineColor: '#587dae',
			//gridlines: { count: 6 },
			gridlines: { color:  'f2f2f2'},
			title: 'Value',
			titleTextStyle:{fontSize: 10}
		},
		colors: [ 'blue', 'red', 'green', 'black','yellow', 'gray'],
		pointSize: 5,
		pointShape: 'square'
	};

	
	var chart = new google.visualization.LineChart(document.getElementById('deffForm_chart'+idx));
	//var chart = new google.charts.Line(document.getElementById('the_chart'));

	//google.visualization.events.addListener(chart, 'ready', animate);
	chart.draw(data, google.charts.Line.convertOptions(options));
}	
//===========================================END OF FORM 		  

//=========================================WIN DRAW LOSE 	
function drawColumnChart(obj1,obj2,idx) {
	var tdlist = $('#wdl_gra_tr div');//.find('td');
	//wdl_draw_flag	
	 barOfTot(obj1,obj2,idx);
	 barOfOp(obj1,obj2,idx);
	 barOfAll(obj1,obj2,idx);
}

function barOfTot(obj1, obj2, idx){
	//mld=todaysMatches[idx];

	var data = google.visualization.arrayToDataTable([
	  ['', obj1.name, obj2.name],
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
		height:350,
		width:300,
		 //bars: 'horizontal' // Required for Material Bar Charts.
	};

	//var chart = new google.charts.Bar(document.getElementById('#totBar'+idx));
	console.log('totBar'+idx);
	var chart = new google.visualization.ColumnChart(document.getElementById('totBar'+idx));
	chart.draw(data, options);
}

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

//------------------Deprecated---------------
{
	//Depricatted funcs
	function drawT1Pie(t1,t2){
	        var data = new  google.visualization.DataTable();
				data.addColumn('string', 'att');
				data.addColumn('number', 'percentage');
				data.addRows([
				 ['Wins',    t1.winsIn + t1.winsOut],
				 ['Draws',    t1.drawsIn + t1.drawsOut],
				 ['Loses',    t1.losesIn + t1.losesOut]
				]);

	        var options = {
				height:250,
				width:300,
				titleTextStyle: { color: '#5c5c5c',  fontSize: '15' },
				legend:'none',
				pieSliceText: 'label',
				pieSliceTextStyle:{color: '#ffffff',  fontSize: 12},
				
				chartArea:{left:20,top:20,width:'90%',height:'85%'},
				backgroundColor:{fill:'#d7dde5'},
	          title: t1.name,
			   slices: {  
				   0: {color: 'green' },
				   1: {color: '#5880ae' },
				   2: {color: '#c53909' },  
				},
			  //is3D: true,
			  pieHole: 0.4
	        };

	        var chart = new google.visualization.PieChart(document.getElementById('t1Pie'));
	        chart.draw(data, options);
	}

	function drawT2Pie(t1,t2){
	        var data = new  google.visualization.DataTable();
				data.addColumn('string', 'att');
				data.addColumn('number', 'percentage');
			 data.addRows([
			 ['Wins',     t2.winsIn + t2.winsOut],
			 ['Draws',    t2.drawsIn + t2.drawsOut],
			 ['Loses',    t2.losesIn + t2.losesOut]
	        ]);

	        var options = {
				//colors: ['#e0440e', '#e6693e', '#ec8f6e', '#f3b49f', '#f6c7b6'],
				height:250,
				width:300,
				titleTextStyle: { color: '#5c5c5c',  fontSize: '15' },
				legend:'none',
				pieSliceText: 'label',
				pieSliceTextStyle:{color: '#ffffff',  fontSize: 12},
				chartArea:{left:20,top:20,width:'90%',height:'85%'},
				backgroundColor:{fill:'#d7dde5'},
	          title: t2.name,
			  //is3D: true,
			   pieHole: 0.4,
			   slices: {  
				    0: {color: 'green' },
				   1: {color: '#5880ae' },
				   2: {color: '#c53909' },  
				},
	        };

	        var chart = new google.visualization.PieChart(document.getElementById('t2Pie'));

	        chart.draw(data, options);
	}	
}