	// to change the view with tables & rows for every competition or teams

//=========================== change win draw lose tab view
//function wdlTableFiller(t1, t2, idx){
function wdlTableFiller(res, company,compId){
	// fills the win draw lose table with data
	//company ids a var that shows weather the res obj came wdl alone or with pred line (with company)
	

//	var i=0;
//		if(company==true){
			for(var i=0; i < res.obj.length; i++)	{
			var j=res.compId*100+i;
			var obj=res.obj[i];
			var t1={name:obj.t1, winIn:obj.t1wIn, winOut:obj.t1wOut, drawIn:obj.t1dIn, drawOut:obj.t1dOut, loseIn:obj.t1lIn, loseOut:obj.t1lOut};
			var t2={name:obj.t2, winIn:obj.t2wIn, winOut:obj.t2wOut, drawIn:obj.t2dIn, drawOut:obj.t2dOut, loseIn:obj.t2lIn, loseOut:obj.t2lOut};
			fillWDLTr(t1, t2, j);
			}
//		}
		
//		else if(company==false){
//			for(var key in response){
//				var j=compId * 100 + i;
//				var obj=res.split[";"];
//					var t1={name:obj.t1, winIn:obj.t1wIn, winOut:obj.t1wOut, drawIn:obj.t1dIn, drawOut:obj.t1dOut, loseIn:obj.t1lIn, loseOut:obj.t1lOut};
//					var t2={name:obj.t2, winIn:obj.t2wIn, winOut:obj.t2wOut, drawIn:obj.t2dIn, drawOut:obj.t2dOut, loseIn:obj.t2lIn, loseOut:obj.t2lOut};
////            	console.log(key+"   ---   "+response[key]);
//					fillWDLTr(t1, t2, j);
//				i++;
//            }
//		} 
		
}

function fillWDLTr(t1, t2, j){
	
	var tab=$('#wdl_tot'+j);
	var tds=$(tab).find('td');
	
	tds[0].innerHTML=t1.name;	
	
	
		$(tds[0]).css('text-decoration', 'underline')
		tds[1].innerHTML=t2.name;
		$(tds[1]).css('text-decoration', 'underline')
	
		tds[3].innerHTML=t1.winIn + t1.winOut;
		$(tds[3]).css("font-weight","Bold");
		tds[4].innerHTML=t2.winIn + t2.winOut;
		$(tds[4]).css("font-weight","Bold");
	
		tds[7].innerHTML=t1.drawIn + t1.drawOut;
		$(tds[7]).css("font-weight","Bold");
		tds[8].innerHTML=t2.drawIn + t2.drawOut;
		$(tds[8]).css("font-weight","Bold");
		tds[11].innerHTML=t1.loseIn + t1.loseOut;
		$(tds[11]).css("font-weight","Bold");
		tds[12].innerHTML=t2.loseIn + t2.loseOut;
		$(tds[12]).css("font-weight","Bold");
	
		var tr0=document.createElement('tr');
		var td0=document.createElement('td');
		$(td0).attr('colspan','5');
		td0.innerHTML='Total Results';
		$(td0).css({
			fontSize: "14pt"
			//color: "red"
		});
		$(td0).css("font-weight","Bold");
		$(tr0).append(td0);
		var f_td =$(tab).find('tr')[0];
		$(tr0).insertBefore(f_td);
		
		$(tab).height(300);
		$(tab).width(300);
		$(tab).css("background-color", "#d7dde5");
		//$(tab).css({'border-right':'2px solid red'});	
		$(tab).find('tr').css({'border-bottom':'2pt solid white'});
		$(tab).find('tr:last').css({'border-bottom':'none'});
			
	
	
		tab=$('#wdl_op'+j);
		tds=$(tab).find('td');
	
		tds[0].innerHTML=t1.name;	
		$(tds[0]).css('text-decoration', 'underline')
		tds[1].innerHTML=t2.name;
		$(tds[1]).css('text-decoration', 'underline')
	
		tds[3].innerHTML=t1.winIn ;
		$(tds[3]).css("font-weight","Bold");
		tds[4].innerHTML= t2.winOut;
		$(tds[4]).css("font-weight","Bold");
	
		tds[7].innerHTML=t1.drawIn ;
		$(tds[7]).css("font-weight","Bold");
		tds[8].innerHTML= t2.drawOut;
		$(tds[8]).css("font-weight","Bold");
		tds[11].innerHTML=t1.loseIn;
		$(tds[11]).css("font-weight","Bold");
		tds[12].innerHTML= t2.loseOut;
		$(tds[12]).css("font-weight","Bold");
	
		var tr0=document.createElement('tr');
		var td0=document.createElement('td');
		$(td0).attr('colspan','5');
		td0.innerHTML='Home Team In vs Away Team Out';
		$(td0).css({
			fontSize: "14pt"
			//color: "red"
		});
		$(td0).css("font-weight","Bold");
		$(tr0).append(td0);
		var f_td =$(tab).find('tr')[0];
		$(tr0).insertBefore(f_td);
		$(tab).height(300);
		$(tab).width(300);
		$(tab).css("background-color", "#d7dde5");
		
		$(tab).find('tr').css({'border-bottom':'2pt solid white'});
		$(tab).find('tr:last').css({'border-bottom':'none'});
		$(tab).css({'border-left' :'2px solid white'});
		$(tab).css({'border-right':'2px solid white'});	
		//$(tab).css({'border':'2px solid white'});	
	
		
		
		tab=$('#wdl_all'+j);
		tds=$(tab).find('td');
	
		tds[0].innerHTML=t1.name;	
		$(tds[0]).css('text-decoration', 'underline')
		tds[1].innerHTML=t2.name;
		$(tds[1]).css('text-decoration', 'underline')
	
		tds[3].innerHTML=t1.winIn ;
		$(tds[3]).css("font-weight","Bold");
		tds[4].innerHTML=t2.winIn ;
		$(tds[4]).css("font-weight","Bold");
	
		tds[7].innerHTML= t1.winOut;
		$(tds[7]).css("font-weight","Bold");
		tds[8].innerHTML= t2.winOut;
		$(tds[8]).css("font-weight","Bold");
	
		tds[11].innerHTML=t1.drawIn ;
		$(tds[11]).css("font-weight","Bold");
		tds[12].innerHTML=t2.drawIn ;
		$(tds[12]).css("font-weight","Bold");
	
		tds[15].innerHTML= t1.drawOut;
		$(tds[15]).css("font-weight","Bold");
		tds[16].innerHTML= t2.drawOut;
		$(tds[16]).css("font-weight","Bold");
	
	
		tds[19].innerHTML=t1.loseIn;
		$(tds[19]).css("font-weight","Bold");
		tds[20].innerHTML=t2.loseIn;
		$(tds[20]).css("font-weight","Bold");
	
		tds[23].innerHTML= t1.loseOut;
		$(tds[23]).css("font-weight","Bold");
		tds[24].innerHTML= t2.loseOut;
		$(tds[24]).css("font-weight","Bold");
	
	
		
		var tr0=document.createElement('tr');
		var td0=document.createElement('td');
		$(td0).attr('colspan','5');
		td0.innerHTML='Teams In & Out results';
		$(td0).css({
			fontSize: "15pt"
			//color: "red"
		});
		$(tr0).append(td0);
		var f_td =$(tab).find('tr')[0];
		$(tr0).insertBefore(f_td);
		$(tab).height(300);
		$(tab).width(400);
		$(tab).css("background-color", "#d7dde5");
		//$(tab).css({'border-left':'2px solid red'});
		$(tab).find('tr').css({'border-bottom':'2pt solid white'});
		$(tab).find('tr:last').css({'border-bottom':'none'});
}


function matchHeaderFiller(){
	var trs= $(".matchLineHeader");
	for (var i = 0; i < trs.length; i++) {
		var tds = trs[i].children;
		//for (var j = 0; j < tds.length; j++) {
			//tds[j].innerHTML=matchLHrData[j];
		tds[1].innerHTML= "home team";
		tds[2].innerHTML= " ";
		tds[3].innerHTML= "  ";
		tds[4].innerHTML= " ";
		tds[5].innerHTML= "guest team ";
		tds[6].innerHTML= "home win";
		tds[7].innerHTML= "draw";
		tds[8].innerHTML= "away win";
		tds[9].innerHTML= "over";
		tds[10].innerHTML= "under";
		tds[11].innerHTML= "HT: 1 < tot. Score";
		tds[12].innerHTML= "HT: 1 > tot. Score";
		tds[13].innerHTML= "HT: 2 < tot. Score";
		tds[14].innerHTML= "HT: 2 > tot. Score";
		tds[15].innerHTML= "tot. HT Score";
		tds[16].innerHTML= "tot. FT Score";
		//}
	}	
}

//=============================	
	