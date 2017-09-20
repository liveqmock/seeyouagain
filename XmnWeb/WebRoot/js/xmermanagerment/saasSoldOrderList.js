var saasSoldOrderList;
$(document).ready(function() {
	saasSoldOrderList = $('#saasSoldOrderList').page({
		url : 'xmer/manage/saassoldorder/init/list.jhtml',
		success : success,
		pageBtnNum :10,
		pageSize:15,
		paramForm : 'saasSoldOrderFormId'
	});
	
	inserTitle(' > 寻蜜客管理 > <a href="xmer/manage/saassoldorder/init.jhtml" target="right">商家签约订单</a>','saasSoldOrderList',true);
});

/**
 * 构件表格
 * 
 * @param data
 * @param obj
 */
function success(data, obj) {
	var picTitle;
	var typeTitle;
	var contentTitle;
	var sortTiltle;
	var captionInfo = '<caption style="background:#EED8D8; text-align:left; color:#703636; font-size:16px; line-height:40px;"><font style="float:left;">&nbsp;&nbsp;商家签约订单总数：'+data.total+'个</font></caption>';
	var callbackParam="isBackButton=true&callbackParam="+getFormParam($("#saasSoldOrderFormId").serialize());
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
				width : 200,// 宽度
				type:"string"//数据类型
				
			},{
				title : "商家编号",// 标题
				name : "sellerid",
				width : 150,// 宽度
				type:"string"//数据类型
				
			},{
				title : "商家名称",// 标题
				name : "sellername",
				width : 150,// 宽度
				type:"string",//数据类型
			},{
				title : "寻蜜客用户ID",// 标题
				name : "uid",
				width : 150,// 宽度
				type:"number",//数据类型
			},{
				title : "寻蜜客SAAS渠道",// 标题
				name : "saasChannelStr",
				width : 150,// 宽度
				type:"string",//数据类型
				
			},{
				title : "订单金额/元",// 标题
				name : "amount",
				width : 100,// 宽度
				leng : 3,//显示长度
				type:"number"//数据类型
			},{
				title : "订单状态",// 标题
				name : "strStatus",
				width : 200,// 宽度
				type:"string"//数据类型
			},{
				title : "SAAS来源",// 标题
				name : "strSaasSource",
				width : 200,// 宽度
				type:"string"//数据类型
			},{
				title : "订单后台状态",// 标题
				name : "hstatus",
				width : 200,// 宽度
				type:"string",//数据类型
				customMethod : function(value, data) {
					if(data.hstatus==0){
						return "待分账";
					} 
					if(data.hstatus==9){
						return "已分账";
					} 
					if(data.hstatus==10||data.hstatus==11){
						return "分账中";
					}
					return "-";
				}
			},{
				title : "SAAS返回押金金额",// 标题
				name : "xmerBackMoney",
				width : 150,// 宽度
				type:"string",//数据类型
			},{
				title : "分账后剩余金额",// 标题
				name : "xmniaoMoney",
				width : 150,// 宽度
				type:"string",//数据类型
			},{
				title : "寻蜜客分账金额",// 标题
				name : "xmerMoney",
				width : 150,// 宽度
				type:"string",//数据类型
			},{
				title : "一级寻蜜客分账金额",// 标题
				name : "oneLevelXmerMoney",
				width : 180,// 宽度
				type:"string",//数据类型
			},{
				title : "二级寻蜜客分账金额",// 标题
				name : "twoLevelXmerMoney",
				width : 180,// 宽度
				type:"string",//数据类型
			},{
				title : "经销商分账金额",// 标题
				name : "sellerAreaMoney",
				width : 150,// 宽度
				type:"string",//数据类型
			},{
				title : "支付时间",// 标题
				name : "zdate",
				width : 180,// 宽度
				leng : 3,//显示长度
				type:"string",//数据类型
			},{
				title : "支付方式",// 标题
				name : "payTypeText",
				width : 150,// 宽度
				leng : 8,//显示长度
				type:"string"//数据类型
			},{
				title : "最后修改时间",// 标题
				name : "udate",
				width : 180,// 宽度
				type:"string",//数据类型
			},{
				title : "第三方支付金额/元",// 标题
				name : "samount",
				width : 150,// 宽度
				type:"number",//数据类型
			},{
				title : "支付流水号",// 标题
				name : "payId",
				width : 150,// 宽度
				type:"string"//数据类型
				
			},{
				title : "第三方交易号",// 标题
				name : "payCode",
				width : 200,// 宽度
				type:"string"//数据类型
				
			},{
				title : "saas套餐订单编号",// 标题
				name : "saasOrdersn",
				width : 180,// 宽度
				type:"string"//数据类型
			}],
			//操作列
			handleCols : {
				title : "操作",// 标题
				queryPermission : ["refund"],// 不需要选择checkbox处理的权限
				width : 220,// 宽度
				// 当前列的中元素
				cols : [{
						title : "退款",// 标题
						linkInfoName : "href",
						linkInfo : {
							href : "xmer/manage/saassoldorder/refund.jhtml",// url
							param : ["id"],// 参数
							permission : "refund"// 列权限
						},
						customMethod : function(value, data){
							console.log(data.hstatus);
	                        if(data.hstatus==9){
	                            var value1 = "<a href='javascript:refund(\"" + data.id + "\",\"" + data.amount + "\",\"" + data.ordersn + "\")'>" + "退款" + "</a>";
	                            return value1;
	                        }else{
	                        	var value2 = '<a href="javascript:;" disabled="disabled" style="color:#CDCDCD;"></a>';
								return value2;
	                        }
	                    }
					}]
	}},permissions);
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
	
$("#export").click(function(){
	var url = "xmer/manage/saassoldorder/export.jhtml";
	$form = $("#saasSoldOrderFormId").attr("action",url);
	$form[0].submit();
});

/**
 * 退款操作
 */
 function refund(id,amount,ordersn){
	 showSmConfirmWindow(function (){
		 $.ajax({
	         url : "xmer/manage/saassoldorder/refund.jhtml",
	         type : "post",
	         dataType : "json",
	         data:'orderNumber=' + ordersn + '&money='+ amount + '&remark=""' + '&bid='+ id,
	         success : function(result) {
	        	 if (result.success) {
	     			showSmReslutWindow(result.success, result.msg);
	     			setTimeout(function() {
	     				saasSoldOrderList.reload();
	     			}, 3000);
	     		} else {
	     			window.messager.warning(result.msg);
	     		}
	         }
	     });
	 },"确定要退款吗？");
	 
 }
