var formId = "editFrom";
var imgRoot = $("#fastfdsHttp").val();
var jsonTextInit;
var anchorImageChooser;
var dataCount = 0;//当前添加主播序号
var dataSize =10;//最多可添加主播数量
$(function() {
	var dataformInit = $("#" + formId).serializeArray();
	jsonTextInit = JSON.stringify({
		dataform : dataformInit
	});
	
	
	//初始化日期控件
	initDate();
	
	//初始化主播下拉框
	initAnchorId();
	
	//初始化商家下拉框
	initSellerid();
	
	//指定观众手机号码初始化
	telphonesInit();
	
	//直播类型初始化
	liveTopicInit();
	
	//初始化直播标签分类
	liveRecordClassifyIdInit();

	//初始化提供粉丝券设置
	initHaveCouponSet();
	
	//初始化粉丝券下拉框
	initLiveCoupon();
	
	//是否自定义分享描述
	initCustomShareSet();
	
	//是否配置机器人
	initRobotSet(0);
	
	//主播模块数量
	dataCount = $("#datas").find(".anchorModel").size()>0?$("#datas").find(".anchorModel").size():0;
	validate(formId, {
		rules : {
			zhiboTitle : {
				required : true
			},
			zhiboAddress : {
				required : true
			},
			sequenceNo  :{
				required :true,
				digits:true,
				range:[1,999]
			},
			zhiboPlaybackUrl :{
				required: true
			},
			telphones :{
				required:true,
				telRule :true
			},
			anchorRoomPassword :{
				passwordRule:true
			},
			haveCoupon :{
				required:true
			},
		},
		messages:{
			sequenceNo:{
				sequenceNo:"请输入直播推荐排序",
				digits:"排序必须为数字类型",
				range:"推荐排序须设定为1-999之间的整数"
			},
			zhiboPlaybackUrl :{
				required :"请填写回放地址"
			},
			telphones :{
				required: "请填写手机号码",
				telRule : "多个手机号以英文逗号分隔"
			},
			anchorRoomPassword :{
				passwordRule:"请填写6位数字密码"
			},
			haveCoupon :{
				required:"请选择是否绑定粉丝券"
			}
		}
	}, liveRecordSave);
	
	
});



/**
 * 保存通告信息
 */
function liveRecordSave() {
//	debugger;
	var url;
	var suffix = ".jhtml";
	var isAdd = $("#" + formId).find("input[name='id']").length == 0 ? true : false;
	if (isAdd) {// 添加操作
		url = "liveRecord/manage/addBatch" + suffix;
	} else {// 修改操作
		url = "liveRecord/manage/update" + suffix;
	}
	var dataform = $("#" + formId).serializeArray();
	var jsonText = JSON.stringify({
		dataform : dataform
	});
	
	//将标签，图片数组信息转化为字符串数据
	convertData();
	
	var result=validateCustomer(isAdd);
	if(!result){//自定义校验不通过
		return ;
	}
	
	if (isAdd || jsonTextInit != jsonText) {// 添加或者修改改了东西才会发送请求
		$.ajax({
			type : 'post',
			url : url,
			data : jsonFromt($('#' + formId).serializeArray()),
			dataType : 'json',
			beforeSend : function(XMLHttpRequest) {
				$('#prompt').show();
			},
			success : function(data) {
				$('#prompt').hide();
				$('#triggerModal').modal('hide');
				if (data.success) {
					var url = contextPath +'/liveRecord/manage/init.jhtml';
					setTimeout(function() {
						location.href = url;
					}, 1000);
				}
				showSmReslutWindow(data.success, data.msg);
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				$('#prompt').hide();
				$('#triggerModal').modal('hide');
			}
		});
	} else {
		$('#prompt').hide();
		$('#triggerModal').modal('hide');
		showSmReslutWindow(false, "没做任何修改！");
	}
}

/**
 * 提供粉丝券设置
 */
function initHaveCouponSet(){
//	debugger;
	var haveCoupon=$("input[name='haveCoupon']:checked").val();
	if(haveCoupon==undefined){
		haveCoupon=0;//未设置，默认不提供
		$("input[name='haveCoupon']")[1].checked=true;
	}

	if(haveCoupon=='0'||haveCoupon=='1'){
		if(haveCoupon=='1'){
			$(".on-off").css('display','block');
		}else{
			$(".on-off").css('display','none');
		}
	}
}

/**
 * 提供直播粉丝券改变触发
 */
$("input[name='haveCoupon']").change(function(){
//	debugger;
	var haveCoupon=$("input[name='haveCoupon']:checked").val();
	if(haveCoupon=='1'){
		$(".on-off").css('display','block');
	}else{
		$(".on-off").css('display','none');
	}
});

/**
 * 初始化直播券下拉框
 */
function initLiveCoupon(){
	$("#cid").chosenObject({
		hideValue : "cid",
		showValue : "cname",
		url : "liveRecord/manage/setAdvance/initLiveCoupon.jhtml",
		isChosen:true,//是否支持模糊查询
		isCode:true,//是否显示编号
		isHistorical:false,//是否使用历史已加载数据
		width:"100%"
	});
}

/**
 * 选择直播券后,修改对应信息
 */
$('body').on("click",'#cid_chosen .chosen-results li',function(){
//	debugger;
	loadAdvanceCouponInfo();
});

/**
 * 加载直播券信息
 */
function loadAdvanceCouponInfo(){
	var cid =  $("#cid").find("option:selected").val();
	$.ajax({
		type: "POST",
		url : "liveRecord/manage/setAdvance/getLiveCouponInfoById.jhtml?t=new Date()",
		dataType : "json",
		data: {"cid":cid},
		success : function(data){
			if(data != null){
				$("#cname").text(data.cname);//券名
				$("#couponSellerId").val(data.sellerid);//关联商户ID
				$("#couponSellerName").text(data.sellername==undefined?"":data.sellername);//关联商户
				$("#denomination").text(data.denomination);//设置价格
				$("#originalPrice").text(data.originalPrice);//原价
				$("#defaultMaxNum").text(data.defaultMaxNum);//粉丝券数量
				var useTimeZone="";
				if(data.startDateStr){
					useTimeZone+=data.startDateStr;
				}
				if(data.endDateStr){
					useTimeZone+=" 至 "+data.endDateStr;
				}
				$("#useTimeZone").text(useTimeZone);
				
				//初始化赠送预售抵用券情况
				initHaveFree(data.haveFree,data.voucherList);
			}
		}
	});
}

/**
 * 初始化赠送抵用券 
 */

function initHaveFree(haveFree,voucherList){
//	debugger;
	if(haveFree=='1'){//有抵用券
		$("input[name='haveFree']")[0].checked=true;
		//加载抵用券列表详情
		loadVouchers(voucherList);
		
	}else{//无抵用券
		$("input[name='haveFree']")[1].checked=true;
		$("#couponDiv").html("无");
	}
	
}

/**
 * 加载抵用券列表详情
 */
function loadVouchers(data){
	var content='';
	//加载抵用券列表
	 $.each(data, function (n, obj) {  
		 content +="<div class='input-group'>"
				 + "￥"+ obj.denomination +" ，满 " + obj.condition + " 元可用 "
				 + "</div>";
      });  
    $("#couponDiv").html(content);
}

//初始化默认主播下拉框
function initAnchorId(){
//	debugger;
	$("#anchorId").chosenObject({
		hideValue : "id",
		showValue : "nickname",
		url : "anchor/manage/initAnchorId.jhtml",
		isChosen:true,//是否支持模糊查询
		isCode:true,//是否显示编号
		isHistorical:false,//是否使用历史已加载数据
		width:"100%"
	});
}


//选择主播后，修改主播昵称隐藏域的值
$('body').on("click",'#anchorId_chosen .chosen-results li',function(){
//	debugger;
	var anchorId =  $("#anchorId").find("option:selected").val();
	$.ajax({
		type: "POST",
		url : "anchor/manage/getAnchorById.jhtml?t=new Date()",
		dataType : "json",
		data: {"id":anchorId},
		success : function(data){
			if(data != null){
				/*$("#uid").val(data.uid);*/
				$("#nname").val(data.nickname);
				$("#sex").val(data.sex);
				
			}
		}
	});
});


//初始化添加主播下拉框
function initAddAnchorId(index){
//	debugger;
	var currentId="anchorId"+index;
	$("#"+currentId).chosenObject({
		hideValue : "id",
		showValue : "nickname",
		url : "anchor/manage/initAnchorId.jhtml",
		isChosen:true,//是否支持模糊查询
		isCode:true,//是否显示编号
		isHistorical:false,//是否使用历史已加载数据
		width:"100%"
	});
	
}

/**
 * 绑定选择主播click事件
 * @param index
 */
function bindAnchorChange(index){
	$('body').on("click",'#anchorId'+index+'_chosen .chosen-results li',function(){
		var anchorId =  $("#anchorId"+index).find("option:selected").val();
		$.ajax({
			type: "POST",
			url : "anchor/manage/getAnchorById.jhtml?t=new Date()",
			dataType : "json",
			data: {"id":anchorId},
			success : function(data){
				if(data != null){
					console.log(data.nickname);
					$('input[name="anchorList['+index+'].nname"').val(data.nickname);
					$('input[name="anchorList['+index+'].sex"').val(data.sex);
					
				}
			}
		});
	});
}



//初始化商家下拉框
function initSellerid(){
	$("#sellerid").chosenObject({
		hideValue : "sellerid",
		showValue : "sellername",
		url : "businessman/seller/getSellerIdAndName.jhtml",
		isChosen:true,//是否支持模糊查询
		isCode:true,//是否显示编号
		isHistorical:false,//是否使用历史已加载数据
		width:"100%"
	});
}

//选择商家后，修改商家名称 
$('body').on("click",'#sellerid_chosen .chosen-results li',function(){
//	debugger;
	var sellerid =  $("#sellerid").find("option:selected").val();
	$.ajax({
		type: "POST",
		url : "businessman/seller/getSellerLandmarkInfoById.jhtml?t=new Date()",
		dataType : "json",
		data: {"sellerid":sellerid},
		success : function(data){
			if(data != null){
				$("#sellername").val(data.sellername);
				$("#sellerAlias").val(data.sellername);
				$("#longitude").val(data.longitude);
				$("#latitude").val(data.latitude);
				$("#zhiboAddress").val(data.address);
			}
		}
	});
	
});



/**
 * 绑定"是否指定观众"单击事件
 */
$("input[name='isAppoint']").on("change",function(){
	telphonesInit();
});

/**
 * 指定观众手机号码显示初始化
 */
function telphonesInit(){
	var isAppoint = $("input[name='isAppoint']:checked").val();
	if (isAppoint == 1) {
		$("#telphonesDiv").css("display","block");
	} else {
		$("#telphonesDiv").css("display","none");
		$("#telphones").val('');
	}
}

/**
 * 绑定"直播类型,1商家、2活动"change事件
 */
$("input[name='liveTopic']").on("change",function(){
	liveTopicInit();
});

/**
 * 商家别名 / 活动主题初始化
 * 
 */
function liveTopicInit(){
	var liveTopic = $("input[name='liveTopic']:checked").val();
	if (liveTopic == 1) {
		$("#sellerDiv").css("display","block");
		$("#sellerAliasLabel").text("商家别名：");
	} else {
		$("#sellerDiv").css("display","none");
		$("#sellerAliasLabel").text("活动主题：");
	}
}

/**
 * 自定义校验方法
 */
function validateCustomer(isAdd){
//	debugger;
	var result=true;
	var anchorId=$("#anchorId").val()||$("#anchorId").attr("initValue");
	if(anchorId == null || anchorId==""){
		showWarningWindow("warning","请选择主播!",9999);
		result=false;
		return ;
	}
	
	var planStartDate=$("#planStartDate").val();
	if(planStartDate == null || planStartDate==""){
		showWarningWindow("warning","请选择直播计划开始时间!",9999);
		result=false;
		return ;
	}
	
	var planEndDate=$("#planEndDate").val();
	if(planEndDate == null || planEndDate==""){
		showWarningWindow("warning","请选择直播计划结束时间!",9999);
		result=false;
		return ;
	}
	
	var haveCoupon=$("input[name='haveCoupon']:checked").val();
	var cid=$("#cid").val();
	if(haveCoupon==1){
		if(cid=='' || cid ==undefined){
			showWarningWindow('warning', "请选择粉丝券！");
			result=false;
			return;
		}
		
		var couponSellerId=$("#couponSellerId").val();
		if(couponSellerId==''||couponSellerId=='0' || couponSellerId==undefined){
			showWarningWindow('warning', "粉丝券关联商户为空");
			result=false;
			return;
		}
		
	}
	
	var liveTopic=$("input[name='liveTopic']:checked").val();
	if(liveTopic==1){
		var sellerid=$("#sellerid").val()||$("#sellerid").attr("initValue");
		if(sellerid == null || sellerid==""){
			showWarningWindow("warning","请选择商铺!",9999);
			result=false;
			return ;
		}
		
		var couponSellerId=$("#couponSellerId").val();
		if(couponSellerId!=sellerid && haveCoupon==1){
			showWarningWindow("warning","粉丝券关联商户与通告商铺不一致!",9999);
			result=false;
			return ;
		}
	}
	
	//校验直播封面
	var anchorIds=[];
	for(var i = 0;i < dataCount; i++) {
//		debugger;
		var zhiboCover=$('input[name="anchorList['+i+'].picUrls"]').val();
		var anchorId=$('select[name="anchorList['+i+'].anchorId"]').val();
		if(anchorId!=undefined && anchorId!=""){
			anchorIds.push(anchorId);
		}
		var message="请上传编号为"+anchorId+"的主播封面！";
		if(zhiboCover==''){
			showWarningWindow("warning",message,9999);
			result=false;
			return ;
		}
		
	}
	
	//重复主播校验
	var repeat=existRepeat(anchorIds);
	if(repeat){
		showWarningWindow("warning","不允许添加重复主播!",9999);
		result=false;
		return ;
	}
	
	//校验机器人设置输入值
	debugger;
	for(var i = 0;i < dataCount; i++) {
		var isRobotInit=$("input[name='anchorList["+i+"].isRobotInit']:checked").val();
		if(isRobotInit==1){
			var reg1 = new RegExp("^\\d+$");
			var robotSetMixNums=$("input[name='anchorList["+i+"].robotSetMixNums']").val();
			var robotSetMaxNums =$("input[name='anchorList["+i+"].robotSetMaxNums']").val();
			if(!(undefined ==robotSetMixNums || reg1.test(robotSetMixNums))){ 
				submitDataError("input[name='anchorList["+i+"].robotSetMixNums']","随机增加机器人最小值需为大于等于0的整数");
				result=false;
				return false;
			}
			var compare=parseFloat(robotSetMaxNums)>=parseFloat(robotSetMixNums);
			if(!(undefined ==robotSetMaxNums || (reg1.test(robotSetMaxNums)&&compare ))){ 
				submitDataError("input[name='anchorList["+i+"].robotSetMaxNums']","随机增加机器人最大值必须大于等于最小值");
				result=false;
				return false;
			}
			
			var robotMinNums=$("input[name='anchorList["+i+"].robotMinNums']").val();//初始机器人
			var robotMaxNums =$("input[name='anchorList["+i+"].robotMaxNums']").val();//最高上限
			
			if(!(undefined ==robotMinNums || reg1.test(robotMinNums))){ 
				submitDataError("input[name='anchorList["+i+"].robotMinNums']","初始机器人需为大于等于0的整数");
				result=false;
				return false;
			}
			var compare=parseFloat(robotMaxNums)>=parseFloat(robotMinNums);
			if(!(undefined ==robotMaxNums || (reg1.test(robotMaxNums)&&compare ))){ 
				submitDataError("input[name='anchorList["+i+"].robotMaxNums']","最高上限机器人必须大于等于初始机器人");
				result=false;
				return false;
			}
			
			var multiple =$("input[name='anchorList["+i+"].multiple']").val();//机器人显示倍数
			var rangeResult=multiple>=1 && multiple<=100000;
			if(!(reg1.test(multiple) && rangeResult )){ 
				submitDataError("input[name='anchorList["+i+"].multiple']","机器人显示倍数需为1-100000之间的整数");
				result=false;
				return false;
			}
		}
		
	}
	
	return result;
}

/**
 * 输出校验结果
 */
function printInfo(){
	convertData();
	var result=validateCustomer();
	console.log("校验结果:"+result);
	
}

/**
 * 数组存在重复元素校验
 * @param arr
 * @returns {Boolean}
 */
function existRepeat(arr){ 
    var hash = {}; 
    for(var i in arr) { 
        if(hash[arr[i]]) 
        return true; 
        hash[arr[i]] = true; 
    } 
    return false; 
}


//初始化日期控件
function initDate(){
	
	
	$('input[name="planStartDate"]').datetimepicker({
		weekStart : 0,
		todayBtn : 0,
		autoclose : 1,
		todayHighlight : 1,
		startView : 2,
		minView : 0,
		minuteStep :30,
		forceParse : 0,
		showMeridian : 1,
		format : 'yyyy-mm-dd hh:ii',
		startDate : new Date(),
		endDate: $("input[name='planEndDate']").val()
	}).on("changeDate",function() {
			$("input[name='planEndDate']").datetimepicker("setStartDate",$("input[name='planStartDate']").val());
		});
	
	$('input[name="planEndDate').datetimepicker({
		weekStart : 0,
		todayBtn : 0,
		autoclose : 1,
		todayHighlight : 1,
		startView : 2,
		minView : 0,
		minuteStep :30,
		forceParse : 0,
		showMeridian : 1,
		format : 'yyyy-mm-dd hh:ii',
		startDate: $("input[name='planStartDate']").val()
	}).on( "changeDate", function() {
				$("input[name='planStartDate']").datetimepicker("setEndDate", $("input[name='planEndDate']").val());
			});
	
};


/**
 * 手机号校验(只做长度校验)
 */
$.validator.addMethod("telRule", function(value, element) {
	var phone =  /^\d{11}(,\d{11})*$/;//多个手机号已逗号分隔
	return this.optional(element) || (phone.test(value));
}, "请输入正确的手机号！");

/**
 * 直播间密码校验(6为数字)
 */
$.validator.addMethod("passwordRule", function(value, element) {
	var password =  /^\d{6}$/;
	return this.optional(element) || (password.test(value));
}, "请填写6位数字密码！");



/**
 * 绑定单击添加主播模块事件
 */
$("#datas").on("click", ".icon-plus", function() {
	if(dataCount == dataSize-1){
		showWarningWindow("warning","最多可添加"+dataSize+"个主播!",9999);
		return;
	}
	if ($(this).parents(".plandiv").find(".anchorModel").size() < dataSize) {
		$(this).parents(".anchorModel").after( $(".dataTemp").html().replace(/Index/g, dataCount));
		//初始化添加主播下拉框组件
		initAddAnchorId(dataCount);
		
		//绑定选择主播点击事件
		bindAnchorChange(dataCount);
		
		//初始化添加主播模块分类标签组件
		initAddLiveRecordClassifyIdInit(dataCount);
		
		bindClassifyChange(dataCount);
		
		//初始化是否自定义分享描述组件
		initCustomSharesSet(dataCount);
		
		//绑定是否自定义分享描述change事件
		bindCustomShareChange(dataCount);
		
		//初始化是否设置机器人组件
		initRobotSet(dataCount);
		
		//绑定设置机器人change事件
		bindRobotInitChange(dataCount);
		
		dataCount++;
	}
});

/**
 * 绑定单击删除主播模块事件
 */
$("#datas").on("click",".icon-minus",function() {
		if ($(this).parents(".plandiv").find(".anchorModel").size() > 1) {
			$(this).parents(".anchorModel").remove();
		}
});




/**
 * 绑定"上传封面图"单击事件
 */

$("#datas").on("click",".upload-btn",function(){
	if($(this).next().find("li").length>=5){
		showWarningWindow("warning","最多添加5张图片!",9999);
		return false;
	}
	$("#datas .active").removeClass("active");
	$(this).next().addClass("active");
	var anchorId=$(this).parent().parent().siblings(".chosenAnchor").find("select option:selected").val();
	if(anchorId==undefined || anchorId==""){
		showWarningWindow("warning","请选择主播!",9999);
	}else{
		anchorImageChooser = new ModalTrigger({
 			title:'主播相册',
			type : 'ajax',
			width:'800px',
			position:'10px',//距顶部的偏移
			url : 'anchorBusiness/manage/anchorImage/anchorImageChooser.jhtml?id=' + anchorId,
			toggle : 'modal'
		});
		anchorImageChooser.show();
	}
});

//删除图片
$("#datas").on("click","em.delete-img",function(){
	$(this).parent().remove();
});


/**
 * 将标签，图片数组信息转化为字符串数据
 */
function convertData(){
	//拼装picUrl字符串
	var imgList=$("#datas .img-list");
	for(var i=0;i<imgList.length;i++){
		var picUrls=[];
		imgList.eq(i).find("img").each(function(index){
			var srcTemp=$(this).attr("src");
			srcTemp=srcTemp.replace(imgRoot,"");
			picUrls.push(srcTemp);
		});
		if(picUrls.length>0){
			imgList.eq(i).next().val(picUrls.join(";"));
		}
	}
	
	
	//拼装tagIds字符串
	var tagList=$("#datas .ids-box");
	for(var i=0;i<tagList.length;i++){
		var tagIds=[];
		tagList.eq(i).find("span").each(function(index){
			var srcTemp=$(this).text();
			tagIds.push(srcTemp);
		});
		if(tagIds.length>0){
			tagList.eq(i).next().val(tagIds.join(";"));
		}
	}
	
}


/**
 * 初始化直播标签分类
 */
function liveRecordClassifyIdInit(){
	$("#classifyId").chosenObject({
		hideValue : "id",
		showValue : "classifyName",
		url : "businessman/classify/liveRecordClassifyInit.jhtml",
		isChosen:true,//是否支持模糊查询
		isCode:true,//是否显示编号
		isHistorical:false,//是否使用历史已加载数据
		width:"40%"
	});
}

//选择分类后，初始化标签
$('body').on("click",'#classifyId_chosen .chosen-results li',function(){
	//初始化联动标签
	liveRecordTagInit();
	// 当原始select中的选项发生变化时通知chosen更新选项列表
	$('#tagId').trigger('chosen:updated');
});



/**
 * 初始化直播标签分类
 */
function initAddLiveRecordClassifyIdInit(index){
	var currentId="#classifyId"+index;
	$(currentId).chosenObject({
		hideValue : "id",
		showValue : "classifyName",
		url : "businessman/classify/liveRecordClassifyInit.jhtml",
		isChosen:true,//是否支持模糊查询
		isCode:true,//是否显示编号
		isHistorical:false,//是否使用历史已加载数据
		width:"40%"
	});
}

//选择分类后，初始化标签
function bindClassifyChange(index){

	$('body').on("click",'#classifyId'+index+'_chosen .chosen-results li',function(){
		//初始化联动标签
		addliveRecordTagInit(index);
		
		// 当原始select中的选项发生变化时通知chosen更新选项列表
		$('#tagId'+index).trigger('chosen:updated');
	});
}




/**
 * 初始化直播标签
 */
function liveRecordTagInit(){
	var classifyId=$("#classifyId").val();
	$("#tagId").chosenObject({
		hideValue : "id",
		showValue : "tagName",
		url : "businessman/classify/liveRecordTagInit.jhtml",
		isChosen:true,//是否支持模糊查询
		isCode:true,//是否显示编号
		filterVal:classifyId,////过滤的值 (classifyId=1)
		isHistorical:false,//是否使用历史已加载数据
		width:"40%"
	});
}

/**
 * 初始化直播标签
 */
function addliveRecordTagInit(index){
	var classifyId=$("#classifyId"+index).val();
	$("#tagId"+index).chosenObject({
		hideValue : "id",
		showValue : "tagName",
		url : "businessman/classify/liveRecordTagInit.jhtml",
		isChosen:true,//是否支持模糊查询
		isCode:true,//是否显示编号
		filterVal:classifyId,////过滤的值 (classifyId=1)
		isHistorical:false,//是否使用历史已加载数据
		width:"40%"
	});
}



/**
 * 绑定确认添加标签按钮click事件
 */

$("#datas").on("click",".addTagBtn",function(){
	
	var tagText=$(this).prev().find(".chosen-single span").text();
	var tagId=$(this).prev().prev().val();
	//储存id
	var tagIds=$(this).next().next();
	
	if(tagId){
		if(tagIds.find("span").length<2){
			var spans=tagIds.find("span");
			for(var i=0;i<spans.length;i++){
				if(tagId==spans.eq(i).text()){
					showWarningWindow("warning","该标签已添加!",9999);
					return false;
				}
			}
			tagIds.append("<span>"+tagId+"</span>");
			$(this).next().append("<span name='"+tagId+"'>"+tagText+"<em class='icon-remove remove-tag'></em></span>");
		}else{
			showWarningWindow("warning","最多添加2个标签!",9999);
		}
	}else{
		showWarningWindow("warning","请选择标签!",9999);
	}
	
});

//删除标签
$("#datas").on("click","em.remove-tag",function(){
	var tagId=$(this).parent().attr("name");
	var spans=$(this).parent().parent().next().find("span");
	for(var i=0;i<spans.length;i++){
		if(spans.eq(i).text()==tagId){
			spans.eq(i).remove();
		}
	}
	$(this).parent().remove();
});


/**
 * 是否自定义分享描述设置
 */
function initCustomShareSet(){
//	debugger;
	var customShare=$("input[name='anchorList[0].isCustomShare']:checked").val();
	if(customShare==undefined){
//		customShare=1;//未设置，默认不提供
		$("input[name='anchorList[0].isCustomShare']")[1].checked=true;
	}
}

/**
 * 绑定"是否自定义分享描述"单击事件
 */
$("input[name='anchorList[0].isCustomShare']").on("change",function(){
	customShareChange();
});

/**
 * 是否自定义分享描述初始化
 */
function customShareChange(){
	var isCustomShare = $("input[name='anchorList[0].isCustomShare']:checked").val();
	if (isCustomShare == 1) {
		$("[name='anchorList[0].customShareTitleInfo']").css("display","block");
		$("[name='anchorList[0].customShareDescInfo']").css("display","block");
	} else {
		$("[name='anchorList[0].customShareTitleInfo']").css("display","none");
		$("[name='anchorList[0].customShareDescInfo']").css("display","none");
		$("input[name='anchorList[0].customShareTitle']").val('');
		$("input[name='anchorList[0].customShareDesc']").val('');
	}
}

/**
 * 是否自定义分享描述设置
 */
function initCustomSharesSet(index){
//	debugger;
	var customShare=$("input[name='anchorList["+index+"].isCustomShare']:checked").val();
	if(customShare==undefined){
		$("input[name='anchorList["+index+"].isCustomShare']")[1].checked=true;
	}
}

/**
 * 绑定是否自定义分享描述click事件
 * @param index
 */
function bindCustomShareChange(index){
	$('body').on("change", "input[name='anchorList["+index+"].isCustomShare']",function(){
//		$("input[name='anchorList['+index+'].isCustomShare']").on("change",function(){		
//		 console.log(data.nickname);
		var isCustomShare = $("input[name='anchorList["+index+"].isCustomShare']:checked").val();
		if (isCustomShare == 1) {  //显示
			$("[name='anchorList["+index+"].customShareTitleInfo']").css("display","block");
			$("[name='anchorList["+index+"].customShareDescInfo']").css("display","block");
			
		} else { //隐藏
			console.log($("#anchorList["+index+"].customShareTitleInfo"));
			$("[name='anchorList["+index+"].customShareTitleInfo']").css("display","none");
			$("[name='anchorList["+index+"].customShareDescInfo']").css("display","none");
			
			$("input[name='anchorList["+index+"].customShareTitle]").val('');
			$("input[name='anchorList["+index+"].customShareDesc]").val('');
		}
	});
}

/**
 * 初始化机器人配置
 */
function initRobotSet(index){
//	debugger;
	var isRobotInit=$("input[name='anchorList["+index+"].isRobotInit']:checked").val();
	if(isRobotInit==undefined){
		$("input[name='anchorList["+index+"].isRobotInit']")[1].checked=true;
	}
}

/**
 * 绑定"是否配置机器人"单击事件
 */
$("input[name='anchorList[0].isRobotInit']").on("change",function(){
	robotConfChange(0);
});

/**
 * 绑定"是否配置机器人"click事件
 * @param index
 */
function bindRobotInitChange(index){
	$('body').on("change", "input[name='anchorList["+index+"].isRobotInit']",function(){
		robotConfChange(index);
	});
}

function robotConfChange(index){
	debugger;
	var isRobotInit = $("input[name='anchorList["+index+"].isRobotInit']:checked").val();
	if (isRobotInit == 1) {  //显示
		$("[name='anchorList["+index+"].robotRangeDiv']").css("display","block");
		$("[name='anchorList["+index+"].robotConfDiv']").css("display","block");
		$("[name='anchorList["+index+"].robotMultipleDiv']").css("display","block");
		
	} else { //隐藏
		$("[name='anchorList["+index+"].robotRangeDiv']").css("display","none");
		$("[name='anchorList["+index+"].robotConfDiv']").css("display","none");
		$("[name='anchorList["+index+"].robotMultipleDiv']").css("display","none");
		
		$("[name='anchorList["+index+"].robotSetMixNums']").val(0);//单次最少新增机器人数量
		$("[name='anchorList["+index+"].robotSetMaxNums']").val(0);//单次最少新增机器人数量
		$("[name='anchorList["+index+"].robotMinNums']").val(0);//初始机器人
		$("[name='anchorList["+index+"].robotMaxNums']").val(0);//最高上限机器人
		$("[name='anchorList["+index+"].multiple']").val(1);//显示倍数
	}
}


/**
 * 机器人设置失去焦点，校验方法
 * @param num
 */
function robotNumBlur(num){
//	var reg = new RegExp("^[1-9]\d*|0$");
	var reg = new RegExp("^\\d+$");
	if(!reg.test($(num).val())){
		submitDataError($(num),"需输入大于等于0的整数");
		return;
	}else{
	submitDatasuccess($(num));
	}
}
