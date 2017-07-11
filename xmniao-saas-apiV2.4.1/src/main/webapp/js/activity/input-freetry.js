/*页面路由*/
$(function(){
	
	router();	
	step1CheckStepOneStaus();
	
var switchStatus=$("#limitNumber").val();
function router(){
    var hash = window.location.hash;
    if(hash == '' || hash == '#step1'){
        $('#step1').show();
        $('#step2').hide();
        $('#step3').hide();
        $('body').scrollTop(0);
    }else if(hash == '#step2'){
        $('#step1').hide();
        $('#step2').show();
        $('#step3').hide();
        $('body').scrollTop(0);
    }else if(hash == '#step3'){
        $('#step1').hide();
        $('#step2').hide();
        $('#step3').show();
        $('body').scrollTop(0);
        step3Init();
    }
}




$("#step1").find("input[type='text']").bind('keyup',function(){
	step1CheckStepOneStaus();
 });

/**
 * 第一页表单验证
 */
function step1CheckStepOneStaus(){
	var awardProportion = $("#awardProportion").val();
	var beginDate = $("#beginDate").val();
	var endDate =$("#endDate").val();
	var name = $("#activityName").val();
	if(!(awardProportion&&(parseFloat(awardProportion)<=100&&parseFloat(awardProportion)>=0))){
		$("#step1Submit").addClass("links-disabled");
		return false;
	}else if(!(beginDate)){
		$("#step1Submit").addClass("links-disabled");
		return false;
	}else if(!(endDate)){
		$("#step1Submit").addClass("links-disabled");
		return false;
	}else if(!name){
		$("#step1Submit").addClass("links-disabled");
		return false;
	}
	$("#step1Submit").removeClass("links-disabled");
	return true;
}

function step1FormCheck(){
	var awardProportion = $("#awardProportion").val();
	var beginDate = $("#beginDate").val();
	var endDate =$("#endDate").val();
	var name = $("#activityName").val();
	if(!(awardProportion&&(parseFloat(awardProportion)<=100&&parseFloat(awardProportion)>=0))){
		Tips.show('请正确填写获得几率');
		$("#step1Submit").addClass("links-disabled");
		return false;
	}else if(!(beginDate)){
		Tips.show('请正确填写开始日期');
		$("#step1Submit").addClass("links-disabled");
		return false;
	}else if(!(endDate)){
		Tips.show('请正确填写结束日期');
		$("#step1Submit").addClass("links-disabled");
		return false;
	}else if(!name){
		Tips.show('请正确填写活动名称');
		$("#step1Submit").addClass("links-disabled");
		return false;
	}
	$("#step1Submit").removeClass("links-disabled");
	return true;
}

/**
 * 第二页表单验证
 */
function step2FormCheck(){
	var prizeDrawNumber = $("#prizeDrawNumber").val();
	var limitNumber = $("#limitNumber").val();
	var setCondition =$("#setCondition").val();
	if(!(awaryCount&&awaryCount>0)){
		Tips.show('请选择奖品');
		return false;
	}else if(!(prizeDrawNumber&&prizeDrawNumber>=0)){
		Tips.show('请正确填写每人可抽取次数');
		return false;
	}else if(!setCondition){
		Tips.show('请选择重设条件');
		return false;
	}else if(!limitNumber){
		Tips.show('请正确填写是否限制每人领取一次');
		return false;
	}
	return true;
}

function setPrizeDrawNumber(){
	if($("input[name=limitNumber][checked=checked]").val()==1){
		$("input[name=prizeDrawNumber]").val(1);
	}else{
		$("input[name=prizeDrawNumber]").val($("#prizeDrawNumber").val());
	}
}

$("#every-switch").bind('switchchange',function(){
	switchStatus = everySwitch.getSwitchStatus();
	setPrizeDrawNumber();
	$("#limitNumber").val(switchStatus?1:0);
	/*if(switchStatus){
		$("#prizeDrawNumber").attr("readonly","readonly");
		$("#list-ft-color").addClass("list-ft-color");
	}else{
		$("#prizeDrawNumber").removeAttr("readonly");
		$("#list-ft-color").removeClass("list-ft-color");
	}*/
});


$("#step1Submit").on("click",function(){
	$("#awardProportion").trigger("click");
	if(step1FormCheck()){
		window.location.hash = '#step2';
	}
});

$("#step2Submit").on("click",function(){
	if(step2FormCheck()){
		window.location.hash = '#step3';
	}
});

$("input[name=limitNumber]").on("click",function(){
	if($(this).val()==1){
		$("#item-content").attr("class","item-content");
	
	}
});

/**
 * 保存操作
 */
$("#submitButton").on("click",function(){
	if(!key){
    	return;
    }
	submitSetTime();
	if(step1FormCheck()&&step2FormCheck()&&checkAwardEndDate()){
		$("#formid").attr("action",path+"/h5/activity/freetry/save").submit();
	}
});

/*检查奖品结束时间是否在活动日期之后*/
function checkAwardEndDate(){
	var activityEndDate=$("#endDate").val()+" 23:59:00";
	var awardEndDate=$("input[name*=couponEndDate]");
	var eachKey=true;
	$.each(awardEndDate,function(i,item){
		var couponEndDate=$(item).val()+" 23:59:00";
		if(new Date(Date.parse(couponEndDate.replace(/-/g, "/"))).getTime()<new Date(Date.parse(activityEndDate.replace(/-/g, "/"))).getTime()){
			Tips.show("活动时间有更改,请重新选择奖品");
			eachKey=false;
			return ;
		}
	});
	return eachKey;	
}

/**
 * 第二页进入方法
 */
step2Init=function(){
	// TODO
}

/**
 * 第三页进入方法
 */
step3Init=function(){
	var awardProportion=$("#awardProportion").val();
	var beginDate=$("#beginDate").val();
	var endDate=$("#endDate").val();
	var activityName=$("#activityName").val();
	var prizeDrawNumber=$("#prizeDrawNumber").val();
	var setCondition=$("#setCondition").val();
	var limitNumber=$("#limitNumber").val();
	$("#step3name").text(activityName);
	$("#step3prizeDrawNumber").text(prizeDrawNumber);
	$("#step3limitNumber").text(limitNumber==1?limitNumber:"无限制");
	$("#step3awardProportion").text(awardProportion);
	var setConditionToString="";
	if(setCondition==0){
		setConditionToString="不重设";
	}else if(setCondition==1){
		setConditionToString="分享获得";
	}else if(setCondition==2){
	setConditionToString="每天重置";
	}else if(setCondition==3){
	setConditionToString="消费获得";
	}
	$("#step3setConditionToString").text(setConditionToString);
	$("#step3date").text(beginDate+" - "+endDate);
}

/**
 * 跳转奖品列表
 */
$("#list_award").on("click",function(){
	$("#formid").attr("action",path+"/h5/activity/freetry/list_award").submit();
});

window.onhashchange = router;




});