var saasOrderList2;
$(document).ready(function() {
	saasOrderList2 = $('#saasOrderList2').page({
		url : 'xmer/manage/saasorder/init/list.jhtml',
		success : success2,
		pageBtnNum :10,
		pageSize:15,
		paramForm : 'saasOrderFormId2'
	});
	
	inserTitle(' > 寻蜜客管理 > <a href="xmer/manage/saasorder/init.jhtml" target="right">寻蜜客套餐订单</a>','saasOrderList2',true);
});

/**
 * 构件表格
 * 
 * @param data
 * @param obj
 */
function success2(data, obj) {
	var picTitle;
	var typeTitle;
	var contentTitle;
	var sortTiltle;
	var captionInfo = '<caption style="background:#EED8D8; text-align:left; color:#703636; font-size:16px; line-height:40px;"><font style="float:left;">&nbsp;&nbsp;寻蜜客套餐订单总数：'+data.total+'个&nbsp;&nbsp;</font></caption>';
	var callbackParam="isBackButton=true&callbackParam="+getFormParam($("#saasOrderFormId2").serialize());
	obj.find('div').eq(0).scrollTablel({
			checkable : false,
	    	identifier : "",
	    	tableClass :"table-bordered table-striped info",
	    	callbackParam : callbackParam,
	    	caption : captionInfo,
			//数据
			data:data.content, 
			//数据行
			cols:[{
					title : "订单号",// 标题
					name : "ordersn",
					width : 180,// 宽度
					type:"string"//数据类型
				},{
					title : "寻蜜客用户ID",// 标题
					name : "uid",
					width : 120,// 宽度
					type:"string"//数据类型
				},{
					title : "用户名",// 标题
					name : "xmerName",
					width : 120,// 宽度
					type:"string"//数据类型
				},{
					title : "手机号码",// 标题
					name : "xmerPhone",
					width : 120,// 宽度
					type:"number"//数据类型
				},{
					title : "SAAS数量",// 标题
					name : "nums",
					width : 120,// 宽度
					type:"number"//数据类型agio
				},{
					title : "订单金额",// 标题
					name : "amount",
					width : 150,// 宽度
					leng : 3,//显示长度
					type:"number"//数据类型
				},{
					title : "报单状态",// 标题
					name : "strStatus",
					width : 200,// 宽度
					type:"string"//数据类型
				},{
					title : "报单时间",// 标题
					name : "sdate",
					width : 180,// 宽度
					leng : 3,//显示长度
					type:"string"//数据类型
				},{
					title : "处理时间",// 标题sdate
					name : "zdate",
					//sort : "up",
					width : 180,// 宽度
					leng : 3,//显示长度
					type:"string"//数据类型
				},{
					title : "卖出数量",// 标题
					name : "soldNums",
					width : 150,// 宽度
					type:"number"//数据类型
				},{
					title : "套餐正常库存数量",// 标题
					name : "stock",
					width : 150,// 宽度
					leng : 8,//显示长度
					type:"int"//数据类型
				},{
					title : "套餐退还库存数量",// 标题
					name : "returnnums",
					width : 150,// 宽度
					leng : 8,//显示长度
					type:"int"//数据类型
				},{
					title : "EC会员编号",// 标题
					name : "mbEcno",
					width : 150,// 宽度
					leng : 8,//显示长度
					type:"string"//数据类型
				},{
					title : "EC报单编号",// 标题
					name : "mbTransNo",
					width : 150,// 宽度
					leng : 8,//显示长度
					type:"string"//数据类型		
				}]
	},permissions);
}
function substr(obj,length){
	if(obj.length > length){
		obj = obj.substring(0,length) +"...";
	}
	return obj;
}

$(function() {
	var today = new Date();
	var todaystr = addDate(today, 0);
	function addDate(date, days) {
		var d = new Date(date);
		d.setDate(d.getDate() + days);
		var month = d.getMonth() + 1;
		var day = d.getDate();
		if (month < 10) {
			month = "0" + month;
		}
		if (day < 10) {
			day = "0" + day;
		}
		var val = d.getFullYear() + "-" + month + "-" + day;
		return val;
	}
});
	
$("#export2").click(function(){
		var url = "xmer/manage/saasorder/export.jhtml";
		$form = $("#saasOrderFormId2").attr("action",url);
		$form[0].submit();
	});
