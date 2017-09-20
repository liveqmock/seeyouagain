var ISTYPE;
ISTYPE = $("#isType").val();
$(document).ready(function() {
	if (ISTYPE == "add") {
		inserTitle(' >添加导航图','bannerList', false);
	}else{
		inserTitle(' >修改导航图','bannerList', false);
		//初始化修改数据
		updateInit();
		//修改时显示图片
		showPicORHide();
		
		//初始化导航图内容和直播标题显示
		initContentAndRecordId();
	}
    //验证提交添加或修改的数据
    initValidator();
	//初始化上传图片
	initPic();
	
	//初始化主播房间
	initRecordId();
	
	 //带2位小数字点
	 $.validator.addMethod("doublearea",function(value,element,params){
			var len = value.length;
			 if(len>12){
				 return false;
			 }else if( value >= 1000000000 || value < 0){
				 return false;
			 }else {
				 var indexOf = value.indexOf(".");
				 if(indexOf>0){
					 return false;
				 }
				 return true;
			 }
		 },"请填写整数,最大值为999999999");
	 
	 
	//初始化日期
	 $('#begin_time').datetimepicker({
	     format:'yyyy-mm-dd hh:ii',
	     startDate: new Date(),
	     language:  'zh-CN',
	     //weekStart: 1,
	     //todayBtn:  1,
	     autoclose: 1,
	     todayHighlight: 1,
	     startView: 2,
	     //minView:2,
	     forceParse: 0,
	     //showMeridian: 1
	 }).on("changeDate",function(ev){
	     var transferdate=transferDate($("#begin_time").val());//转时间日期
	     $('#end_time').datetimepicker('remove');
	     $('#end_time').datetimepicker({
	         format:'yyyy-mm-dd hh:ii',
	         language:  'zh-CN',
	         //minView:2,
	         autoclose: 1,
	         'startDate':transferdate
	     }).on("changeDate",function(ev){
	         var enddate=$("#end_time").val();
	         setEndTime(enddate);
	     });
	 });
	 $('#end_time').datetimepicker({
	     format:'yyyy-mm-dd hh:ii',
	     startDate: new Date(),
	     language:  'zh-CN',
	     minView:2,
	     autoclose: 1
	 }).on("changeDate",function(ev){
	     var enddate=$("#end_time").val();
	     setEndTime(enddate);
	 });
	 function setEndTime(enddate){
	     $('#begin_time').datetimepicker('remove');
	         $('#begin_time').datetimepicker({
	             format:'yyyy-mm-dd hh:ii',
	             language:  'zh-CN',
	             autoclose: 1,
	             todayHighlight: 1,
	             startView: 2,
	             forceParse: 0,
	             'endDate':transferDate(enddate)
	     });
	 }
	 //将时间字符串转为date
	 function transferDate(data){
	     var start_time=data;
	     var newTime= start_time.replace(/-/g,"-");
	     var transferdate = new Date(newTime);
	     return transferdate;
	 }
	 function transferTime(str){
	     var newstr=str.replace(/-/g,'-');
	     var newdate=new Date(newstr);
	     var time=newdate.getTime();
	     return time;
	 }
	 
	 
	 $("#position").trigger("change");

});

$(function() {
	var initAreaInfo = areaInfoHandler(function(areaInfo) {
		initAreaLd(areaInfo);
		var widths = getWidths();
		areaInfo.find(".chosen-container").each(function(i, v) {
			$(this).css("width", widths[i]);
		});
		return areaInfo.attr("initValue");
	});
	$("#specifyArea").on("click", {
		"initAreaInfo" : initAreaInfo
	}, function(event) {
		if ($(this).is(":checked")) {
			removeAreaInfo();
			createAreaInfo(event.data.initAreaInfo);
			initAreaLd($("#areaInfo"));
		}
	});
	$("#allArea").on("click", removeAreaInfo);
});

/**
 * 初始化上传图片
 */
function initPic() {
	//导航图
	$("#pic1").uploadImg({
		urlId : "picURL1",
		showImg : $('#picURL1').val()
	});
	$("#pic2").uploadImg({
		urlId : "picURL2",
		showImg : $('#picURL2').val()
	});
}
/**
* 触发取消
 */
  $("#cancelId").on("click", function() {
              muBack();
   });
/**
 * 取消方法
 */
function muBack() {
    var url = contextPath + '/user_terminal/banner/init.jhtml';
    location.href = url;
}

/**
 * 修改时数据初始化
 */
function updateInit(){
	//初始化位置信息
	 var positionInit = $("#position").attr("initValue");
     var p_html = '';
     if(positionInit == ""){
         p_html += '<option value="" selected="selected">请选择</option>';
     }else{
         p_html += '<option value="">请选择</option>';
     }
     if(positionInit == "1"){
         p_html +='<option value="1" selected="selected">发现美食</option>';
     }else{
         p_html +='<option value="1">发现美食</option>';
     }
     if(positionInit == "2"){
         p_html += '<option value="2" selected="selected">积分商城</option>';
     }else{
         p_html += '<option value="2">积分商城</option>';
     }
     if(positionInit == "3"){
         p_html += '<option value="3" selected="selected">寻蜜客主页</option>';
     }else{
         p_html += '<option value="3">寻蜜客主页</option>';
     }
     if(positionInit == "4"){
    	 p_html += '<option value="4" selected="selected">直播首页</option>';
     }else{
    	 p_html += '<option value="4">直播首页</option>';
     }
     if(positionInit == "100"){
         p_html += '<option value="100" selected="selected">商家经营页</option>';
     }else{
         p_html += '<option value="100">商家经营页</option>';
     }
     if(positionInit == "5"){
    	 p_html += '<option value="5" selected="selected">积分超市竞拍活动</option>';
     }else{
    	 p_html += '<option value="5">积分超市竞拍活动</option>';
     }
     if(positionInit == "6"){
    	 p_html += '<option value="6" selected="selected">积分超市夺宝活动</option>';
     }else{
    	 p_html += '<option value="6">积分超市夺宝活动</option>';
     }
     if(positionInit == "7"){
    	 p_html += '<option value="7" selected="selected">首页活动</option>';
     }else{
    	 p_html += '<option value="7">首页活动</option>';
     }
     if(positionInit == "8"){
    	 p_html += '<option value="8" selected="selected">充值界面</option>';
     }else{
    	 p_html += '<option value="8">充值界面</option>';
     }
     if(positionInit == "9"){
    	 p_html += '<option value="9" selected="selected">我的首页</option>';
     }else{
    	 p_html += '<option value="9">我的首页</option>';
     }
     if(positionInit == "10"){
    	 p_html += '<option value="10" selected="selected">脉宝云店</option>';
     }else{
    	 p_html += '<option value="10">脉宝云店</option>';
     }
     if(positionInit == "11"){
    	 p_html += '<option value="11" selected="selected">直播结束广告</option>';
     }else{
    	 p_html += '<option value="11">直播结束广告</option>';
     }
     
//     if(positionInit == "INDEX_001"){
//         p_html +='<option value="INDEX_001" selected="selected">首页上</option>';
//     }else{
//         p_html +='<option value="INDEX_001">首页上</option>';
//     }
//     if(positionInit == "INDEX_002"){
//         p_html += '<option value="INDEX_002" selected="selected">首页中</option>';
//     }else{
//         p_html += '<option value="INDEX_002">首页中</option>';
//     }
//     if(positionInit == "INDEX_003"){
//    	 p_html += '<option value="INDEX_003" selected="selected">首页下</option>';
//     }else{
//         p_html += '<option value="INDEX_003">首页下</option>';
//     }
//     if(positionInit == "FRESH_001"){
//    	 p_html += '<option value="FRESH_001" selected="selected">生鲜页上</option>';
//     }else{
//    	 p_html += '<option value="FRESH_001">生鲜页上</option>';
//     }
//     if(positionInit == "FRESH_002"){
//    	 p_html += '<option value="FRESH_002" selected="selected">生鲜页中</option>';
//     }else{
//    	 p_html += '<option value="FRESH_002">生鲜页中</option>';
//     }
//     if(positionInit == "FRESH_003"){
//    	 p_html += '<option value="FRESH_003" selected="selected">生鲜页下</option>';
//     }else{
//    	 p_html += '<option value="FRESH_003">生鲜页下</option>';
//     }
     $('#position').html(p_html);
     //初始化上线状态
     var statusInit = $("#status").attr("initValue");
     var s_html = '';
     if(statusInit == ""){
         s_html += '<option value="" selected="selected">请选择</option>';
     }else{
         s_html += '<option value="">请选择</option>';
     }
     if(statusInit == 0){
         s_html +='<option value="0" selected="selected">待上线</option>';
     }else{
         s_html +='<option value="0">待上线</option>';
     }
     if(statusInit == 1){
         s_html += '<option value="1" selected="selected">已上线</option>';
     }else{
         s_html += '<option value="1">已上线</option>';
     }
     if(statusInit == 2){
    	 s_html += '<option value="2" selected="selected">已下线</option>';
     }else{
         s_html += '<option value="2">已下线</option>';
     }
     $('#status').html(s_html);
     //初始化展示风格
     var bannerStyleInit = $("#bannerStyle").attr("initValue");
     var b_html = '';
     if(bannerStyleInit == ""){
         b_html += '<option value="" selected="selected">请选择</option>';
     }else{
         b_html += '<option value="">请选择</option>';
     }
     if(bannerStyleInit == 0){
         b_html +='<option value="0" selected="selected">图片横排一格</option>';
     }else{
         b_html +='<option value="0">图片横排一格</option>';
     }
     if(bannerStyleInit == 1){
         b_html +='<option value="1" selected="selected">图片横排两格</option>';
     }else {
    	 b_html +='<option value="1">图片横排两格</option>';
     }
     if(bannerStyleInit == 2){
         b_html +='<option value="2" selected="selected">轮播图</option>';
     }else{
    	 b_html +='<option value="2">轮播图</option>';
     }
     $('#bannerStyle').html(b_html);
     //初始化导航图1内容类型
     var type1Init = $("#type1").attr("initValue");
     var t1_html = '';
     if(type1Init == ""){
         t1_html += '<option value="" selected="selected">请选择</option>';
     }else{
         t1_html += '<option value="">请选择</option>';
     }
     if(type1Init == 1){
         t1_html +='<option disabled="disabled" value="1" selected="selected">套餐列表</option>';
     }else{
         t1_html +='<option disabled="disabled" value="1">套餐列表</option>';
     }
     if(type1Init == 2){
         t1_html +='<option disabled="disabled" value="2" selected="selected">套餐详情</option>';
     }else{
         t1_html +='<option disabled="disabled" value="2">套餐详情</option>';
     }
     if(type1Init == 3){
         t1_html +='<option value="3" selected="selected">外部链接</option>';
     }else{
         t1_html +='<option value="3">外部链接</option>';
     }
     if(type1Init == 4){
         t1_html +='<option value="4" selected="selected">商家详情页</option>';
     }else{
         t1_html +='<option value="4">商家详情页</option>';
     }
     if(type1Init == 5){
         t1_html +='<option value="5" selected="selected">积分商品详情</option>';
     }else{
         t1_html +='<option value="5">积分商品详情</option>';
     }
     if(type1Init == 6){
         t1_html +='<option value="6" selected="selected">寻蜜客主页</option>';
     }else{
         t1_html +='<option value="6">寻蜜客主页</option>';
     }
     if(type1Init == 7){
         t1_html +='<option value="7" selected="selected">积分超市主页</option>';
     }else{
         t1_html +='<option value="7">积分超市主页</option>';
     }
     if(type1Init == 8){
         t1_html +='<option value="8" selected="selected">直播列表</option>';
     }else{
         t1_html +='<option value="8">直播列表</option>';
     }
     if(type1Init == 9){
         t1_html +='<option value="9" selected="selected">直播间</option>';
     }else{
         t1_html +='<option value="9">直播间</option>';
     }
     if(type1Init == 10){
         t1_html +='<option value="10" selected="selected">预告详情</option>';
     }else{
         t1_html +='<option value="10">预告详情</option>';
     }
     if(type1Init == 11){
         t1_html +='<option value="11" selected="selected">不跳转</option>';
     }else{
         t1_html +='<option value="11">不跳转</option>';
     }
     $('#type1').html(t1_html);
     //初始化导航图2内容类型
     var type2Init = $("#type2").attr("initValue");
     var t2_html = '';
     if(type2Init == ""){
         t2_html += '<option value="" selected="selected">请选择</option>';
     }else{
         t2_html += '<option value="">请选择</option>';
     }
     if(type2Init == 1){
         t2_html +='<option disabled="disabled" value="1" selected="selected">套餐列表</option>';
     }else{
         t2_html +='<option disabled="disabled" value="1">套餐列表</option>';
     }
     if(type2Init == 2){
         t2_html +='<option disabled="disabled" value="2" selected="selected">套餐详情</option>';
     }else{
         t2_html +='<option disabled="disabled" value="2">套餐详情</option>';
     }
     if(type2Init == 3){
         t2_html +='<option value="3" selected="selected">外部链接</option>';
     }else{
         t2_html +='<option value="3">外部链接</option>';
     }
     if(type2Init == 4){
         t2_html +='<option value="4" selected="selected">商家详情页</option>';
     }else{
         t2_html +='<option value="4">商家详情页</option>';
     }
     if(type2Init == 5){
         t2_html +='<option value="5" selected="selected">积分商品详情</option>';
     }else{
         t2_html +='<option value="5">积分商品详情</option>';
     }
     if(type2Init == 6){
         t2_html +='<option value="6" selected="selected">寻蜜客主页</option>';
     }else{
         t2_html +='<option value="6">寻蜜客主页</option>';
     }
     if(type2Init == 7){
         t2_html +='<option value="7" selected="selected">积分超市主页</option>';
     }else{
         t2_html +='<option value="7">积分超市主页</option>';
     }if(type2Init == 8){
         t2_html +='<option value="8" selected="selected">直播列表</option>';
     }else{
         t2_html +='<option value="8">直播列表</option>';
     }
     if(type2Init == 9){
         t2_html +='<option value="9" selected="selected">直播间</option>';
     }else{
         t2_html +='<option value="9">直播间</option>';
     }
     if(type2Init == 10){
         t2_html +='<option value="10" selected="selected">预告详情</option>';
     }else{
         t2_html +='<option value="10">预告详情</option>';
     }if(type2Init == 11){
         t2_html +='<option value="11" selected="selected">不跳转</option>';
     }else{
         t2_html +='<option value="11">不跳转</option>';
     }
     $('#type2').html(t2_html);
}

/**
 * 展示风格如果是横排一格就显示一张图片，横排两格就显示两张图片
 */
$("#bannerStyle").on("change",function() {
	showPicORHide();
});

/**
 * 导航条位置
 */
$("#position").on("change",function(){
	var positionType=$(this).val();
	if(positionType==5||positionType==6){
		$("#bannerStyle").find("option[value!='2']").attr("disabled","disabled");
		$("#status > option[value!='1']").attr("disabled","disabled");
		showPicORHide(2);
	}else{
		$("#bannerStyle").find("option[value!='2']").removeAttr("disabled","disabled");
		$("#status > option[value!='1']").removeAttr("disabled","disabled");
	}
});

function showPicORHide(type){
	var opp = '<option value="">请选择</option>';
	opp += '<option disabled="disabled" value="1">套餐列表</option>';
	opp += '<option disabled="disabled" value="2">套餐详情</option>';
	opp += '<option value="3">外部链接</option>';
	opp += '<option value="4">商家详情页</option>';	
	opp += '<option value="5">积分商品详情</option>';
	opp += '<option value="6">寻蜜客主页</option>';
	opp += '<option value="7">积分超市主页</option>';
	opp += '<option value="8">直播列表</option>';
	var bannerStyle = type?type:$("#bannerStyle").val();
	bannerStyle = parseInt(bannerStyle);
	if(bannerStyle === 0){
		//$("#pic1Div select").empty().append(opp);
		$("#pic1Div").css("display",'block');
		$("#sort1").val(0);
		$("#sort1").attr("readonly", true);
		$("#pic2Div").css("display",'none');
	}else if(bannerStyle === 1){
		//$("#pic1Div select").empty().append(opp);
		$("#pic1Div").css("display",'block');
		$("#sort1").attr("readonly", false);
		$("#pic2Div").css("display",'block');
	}else if(bannerStyle === 2){
		$("#pic1Div").css("display",'block');
		//$("#pic1Div select").find("option[value='3']").attr("selected","selected");
		//$("#pic1Div select").empty().append('<option value="3">外部链接</option>');
		$("#sort1").attr("readonly", false);
		$("#pic2Div").css("display",'none');
	}else {
		$("#pic1Div select").empty().append(opp);
		$("#pic1Div").css("display",'none');
		$("#pic2Div").css("display",'none');
	}
}
/**
 * 导航图内容类型选中后提示导航图内容输入格式
 */
$("#type1").change(function(){
	var type1 = $("#type1").val();
	type1 = parseInt(type1);
	if(type1 === 1){
		$("#content1").attr("placeholder",'');
		$("#content1Tr").css("display",'table-row');
		$("#recordId1Tr").css("display",'none');
	}else if(type1 === 2){
		$("#content1").attr("placeholder",'');
		$("#content1Tr").css("display",'table-row');
		$("#recordId1Tr").css("display",'none');
	}else if(type1 === 3){
		$("#content1").attr("placeholder",'填写外部链接');
		$("#content1Tr").css("display",'table-row');
		$("#recordId1Tr").css("display",'none');
	}else if(type1 === 4){
		$("#content1").attr("placeholder",'填写商家编号');
		$("#content1Tr").css("display",'table-row');
		$("#recordId1Tr").css("display",'none');
	}else if(type1 === 5){
		$("#content1").attr("placeholder",'填写积分订单编号');
		$("#content1Tr").css("display",'table-row');
		$("#recordId1Tr").css("display",'none');
	}else if(type1 === 6){
		$("#content1").attr("placeholder",'');
		$("#content1Tr").css("display",'table-row');
		$("#recordId1Tr").css("display",'none');
	}else if(type1 === 7){
		$("#content1").attr("placeholder",'');
		$("#content1Tr").css("display",'table-row');
		$("#recordId1Tr").css("display",'none');
	}else if(type1 === 8){
		$("#content1").attr("placeholder",'');
		$("#content1Tr").css("display",'table-row');
		$("#recordId1Tr").css("display",'none');
	}else if(type1 === 9||type1 === 10){
		$("#content1").attr("placeholder",'');
		$("#content1Tr").css("display",'none');
		$("#recordId1Tr").css("display",'table-row');
	}else if(type1 === 11){
		$("#content1").attr("placeholder",'');
		$("#content1Tr").css("display",'table-row');
		$("#recordId1Tr").css("display",'none');
	}
});
$("#type2").change(function(){
	var type2 = $("#type2").val();
	type2 = parseInt(type2);
	if(type2 === 1){
		$("#content2").attr("placeholder",'');
	}else if(type2 === 2){
		$("#content1").attr("placeholder",'');
	}else if(type2 === 3){
		$("#content2").attr("placeholder",'填写外部链接');
	}else if(type2 === 4){
		$("#content2").attr("placeholder",'填写商家编号');
	}else if(type2 === 5){
		$("#content2").attr("placeholder",'填写积分订单编号');
	}else if(type2 === 6){
		$("#content2").attr("placeholder",'');
	}else if(type2 === 7){
		$("#content2").attr("placeholder",'');
	}else if(type2 === 8){
		$("#content2").attr("placeholder",'');
	}else if(type2 === 9 || type2== 10){
		$("#content2").attr("placeholder",'');
		$("#content2Tr").css("display",'none');
		$("#recordId2Tr").css("display",'table-row');
	}else if(type2 === 11){
		$("#content2").attr("placeholder",'');
		$("#content2Tr").css("display",'table-row');
		$("#recordId2Tr").css("display",'none');
	}
});
/**
 * 验证并且提交添加或修改的数据
 */
var formId = [ "editBannerForm" ];
function initValidator() {
    for (var i = 0; i < formId.length; i++) {
        validate(formId[i], valiinfo[formId[i]], formSubmit(formId[i]));
    }
}
/**
 * 验证方法
 */
var valiinfo = {
    "editBannerForm" : {
        rules : {
        	position : {
                required : true
            },
            status : {
                required : true
            },
            remarks : {
                required : true
            },
            bannerStyle : {
                required : true
            },
            sort : {
            	number : true,
            	doublearea : true
            },
            type1 : {
            	required : true
            },
            type2 : {
            	required : true
            },
            sort1 :{
            	required : true
            },
            sort2 :{
            	required : true
            }
        },
        messages : {
        	position : {
                required : "导航图位置信息不能为空！",
            },
            status : {
                required : "请选择上线状态！"
            },
            remarks : {
                required : "导航图描述不能为空！",
            },
            bannerStyle : {
                required : "请选择展示风格！"
            },
            sort : {
            	number : "排序必须为数字",
            	doublearea : "请填写整数,最大值为999999999"
            },
            type1 : {
            	required : "导航图内容类型不能为空"
            },
            type2 : {
            	required : "导航图内容类型不能为空"
            },
            sort1 :{
            	required : "排序不能为空"
            },
            sort2 :{
            	required : "排序不能为空"
            }
        }

    }
};

function getWidths() {
	return [ "50%", "50%"];
}
/**
 * 区域处理
 * 
 * @param {}
 *            fn
 */
function areaInfoHandler(fn) {
	var areaInfo = $("#areaInfo");
	if (areaInfo.length > 0) {
		return fn.call(this, areaInfo);
	}
	return null;
}

function removeAreaInfo() {
	var tr = $("#areaTable tr");
	if (tr.length == 2) {
		$(tr[1]).remove();
	}
}

function createAreaInfo(initAreaInfo) {
	var div = $("<div>").addClass("input-group").attr("id", "areaInfo").css({
		"width" : "80%",
		"float" : "left"
	});
	if (initAreaInfo) {
		div.attr("initValue", initAreaInfo);
	}
	var h5 = $("<h5>");
	var td = $("<td>").attr("colspan", 3).append(div, h5);
	var tr = $("<tr>").append(td);
	$("#areaTable tr:last").after(tr);
}

/**
 * 初始化联动信息
 * 
 * @param {}
 *            areaInfo
 */
function initAreaLd(areaInfo) {
	var widths = getWidths();
	areaInfo.areaLd({
		isChosen : true,
		isMultiple : false,// 区域是否支持多选（在isChosen为true时），
		separator : ",",
		showConfig : [ {
			name : "province",
			tipTitle : "--省--",
			width : widths[0]
		}, {
			name : "city",
			tipTitle : "--市--",
			width : widths[1]
		}]
	});
}

/**
 * Ajax 请求
 */
function formAjax() {
	var success = areaInfoHandler(function(areaInfo) {
		var selectAray = [ "province", "city" ];
		return checkSelect('#editAdvertisingForm', selectAray,
				true);
	});
	
	
	if (success == null || success) {
		var url;
		if ($('#isType').val() == 'add') {
			url = 'user_terminal/advertising/add.jhtml' + '?t=' + Math.random();
		} else {
			url = 'user_terminal/advertising/update.jhtml' + '?t='
					+ Math.random();
		}

		var data = jsonFromt($('#editAdvertisingForm').serializeArray());
		if (data) {
			data.area = areaInfoHandler(function(areaInfo) {
				var areas = $(areaInfo).find("select[name='area']").val();
				return areas ? areas.toString() : null;
			});
		}
		$.ajax({
			type : 'post',
			url : url,
			data : data,
			dataType : 'json',
			beforeSend : function(XMLHttpRequest) {
				$('#prompt').show();
			},
			success : function(data) {
				$('#prompt').hide();
				$('#triggerModal').modal('hide');
				if (data.success) {
					if ($('#isType').val() == 'add') {
						userAdvertisingList.reset();
					} else {
						userAdvertisingList.reload();
					}
				}
				showSmReslutWindow(data.success, data.msg);
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				$('#prompt').hide();
				$('#triggerModal').modal('hide');
			}
		});
	}
}
/**
 * 合作商业务员保存修改
 */
var UpdateSavaBanner = function() {
    var url;
    if (ISTYPE == "add") {
        url = "user_terminal/banner/add.jhtml";
    } else {
        url = "user_terminal/banner/update.jhtml";
    }

    if(!$("#begin_time").val()){
		showWarningWindow("warning","请输入开始时间!",9999);
		return;
	}
	if(!$("#end_time").val()){
		showWarningWindow("warning","请输入结束时间!",9999);
		return;
	}
    //直播间和预告详情，将recordId的值赋给content
    setRecordIdToContent();
    
    var data = $('#editBannerForm').serializeArray();
    //form转成json
    data = jsonFromt(data);
    if($("#isEmphasis:checked").length>0){
    	data.isEmphasis=1;
    }else{
    	data.isEmphasis=0;
    }
    //post提交请求
    $.post(url, data, function(result) {
        if (result.success) {
            showSmReslutWindow(result.success, result.msg);
            setTimeout(function() {
                muBack();
            }, 1000);
        } else {
            window.messager.warning(result.msg);
        }
    }, "json");
};
/**
 * 转换from表单为json数据格式
 */
function jsonFromt(data) {
    var json = {};
    for (var i = 0; i < data.length; i++) {
        json[data[i].name] = data[i].value;
    }
    return json;
}

/**
 * 提交表单方法
 */
var formHandle = {
        "editBannerForm" : UpdateSavaBanner
};
function formSubmit(form) {
    return formHandle[form];
}


//初始化主播房间
function initRecordId(){
	var initValue1=$("#recordId1").attr("initValue");
	var initValue2=$("#recordId2").attr("initValue");
	var reg = new RegExp("^\\d+$");
	if(!reg.test(initValue1)){
		$("#recordId1").attr("initValue","");
	}
	
	if(!reg.test(initValue2)){
		$("#recordId2").attr("initValue","");
	}
	
	$("#recordId1").chosenObject({
		hideValue : "id",
		showValue : "zhiboTitle",
		url : "liveRecord/manage/initRecordId.jhtml",
		isChosen:true,//是否支持模糊查询
		isCode:true,//是否显示编号
		isHistorical:false,//是否使用历史已加载数据
		width:"100%"
	});
	
	$("#recordId2").chosenObject({
		hideValue : "id",
		showValue : "zhiboTitle",
		url : "liveRecord/manage/initRecordId.jhtml",
		isChosen:true,//是否支持模糊查询
		isCode:true,//是否显示编号
		isHistorical:false,//是否使用历史已加载数据
		width:"100%"
	});
}

/**
 * 直播间和预告详情，将recordId的值赋给content
 */
function setRecordIdToContent(){
	var type1 = $("#type1").val();
	if(type1==9 ||type1==10){
		var recordId1=$("#recordId1").val();
		$("#content1").val(recordId1);
	}
	var type2 = $("#type2").val();
	if(type2==9||type2==10){
		var recordId2=$("#recordId2").val();
		$("#content2").val(recordId2);
	}
	
	
}

/**
 * 初始化导航图内容和直播标题显示情况
 */
function initContentAndRecordId(){
	var type1=$("#type1").val();
	if(type1==9 || type1==10){
		$("#content1Tr").css("display",'none');
		$("#recordId1Tr").css("display",'table-row');
	}
}


