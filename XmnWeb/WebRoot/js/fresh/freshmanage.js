var freshList;

$(document).ready(function() {
	freshList = $('#freshList').page({
		url : 'fresh/manage/list.jhtml',
		success : success,
		pageBtnNum :10,
		pageSize:15,
		page:page,
		paramForm : 'searchBillForm'
	});
	
	beachOnLine();
	
	beachDownLine();
	
	batchConfirmHotSale();
	
	/*$("#ptypeld").freshLd({
		showConfig : [ {name : "classa", tipTitle : "--&nbsp;类别&nbsp;--"}]
	});*/
	// 省市
/*	var ld = $("#ld").cityLd({
		isChosen : false
	});*/
	
	//初始化日期选择控件
	initDates();
	
	/*$('.form-datetime').datetimepicker({
		weekStart : 1,
		todayBtn : 1,
		autoclose : 1,
		todayHighlight : 1,
		startView : 2,
		forceParse : 0,
		minuteStep:1,
		showMeridian : 1,
		format : 'yyyy-mm-dd'
	});*/
	inserTitle(' > 产品管理 > <a href="fresh/manage.jhtml" target="right">所有产品</a>','allbillSpan',true);
	$("#export").click(function(){
		$form = $("#searchBillForm").attr("action","fresh/manage/export.jhtml");
		$form[0].submit();
	});
	
	
	$("input[data-bus=reset]").click(function(){
		$("#ld").find("select").trigger("chosen:updated");
	});	
	
	
});

/**
 * 构件表格
 * 
 * @param data
 * @param obj
 */
function success(data, obj) {
	page=data.page
	var captionInfo = '<caption style="background:#EED8D8; text-align:left; color:#703636; font-size:16px; line-height:40px;"><font style="float:left;">&nbsp;&nbsp;共计【'+data.total+'】种产品&nbsp;</font></caption>';
	var callbackParam="isBackButton=true&callbackParam="+getFormParam($("#searchBillForm").serialize());
	updateAddBtnHref(".btn-add",callbackParam);
	obj.find('div').eq(0).scrollTablel({
    	checkable :true,
    	identifier : "pid",
    	tableClass :"table-bordered table-striped info",
    	callbackParam : callbackParam,
    	caption : captionInfo,
		//数据
		data:data.content, 
		 //数据行
		cols:[{
			title : "排序",// 标题
			name : "sort",
			//sort : "up",
			width : 80,// 宽度
			leng : 3,//显示长度
			type:"number",//数据类型				
		},{
			title : "产品编号",// 标题
			name : "codeId",
			//sort : "up",
			width : 100,// 宽度
			leng : 3,//显示长度
			type:"number",//数据类型					
		},{
			title : " 产品名称",// 标题
			name : "pname",
			width : 200,// 宽度
			type:"string"//数据类型
				
		},{
			title : " 产品销售属性",// 标题
			name : "pname",
			type:"string",//数据类型
			isLink : true,
			link : {
				linkInfoName : "modal",
				linkInfo : {
					modal : {
						url : "fresh/manage/viewSaleProperty.jhtml",
						position : "200px",
						width : "auto",	
						title : "商品销售属性" 
					}
				},
				param : ["codeId"],
				permission : "init",
			},
			customMethod : function(value, data) {
				return $(value).html("销售属性");
			},
			width : 200// 宽度
		},{
			title : "产品标准号",// 标题
			name : "standard",
			//sort : "up",
			width : 120,// 宽度
			leng : 8,//显示长度
			type:"string"//数据类型
		},{
			title : "产品类型",// 标题
			name : "classaStr",
			width : 150,// 宽度
			type:"string"//数据类型
		},{
			title : "产品原价(元)",// 标题
			name : "price",
			width : 150,// 宽度
			type:"string"//数据类型
			
		},{
			title : "销售价(元)",// 标题
			name : "combinePrice",
			//sort : "up",
			width : 180,// 宽度
			type:"string"//数据类型
		},{
			title : "采购价(元)",// 标题
			name : "purchasePrice",
			//sort : "up",
			width : 180,// 宽度
			type:"string"//数据类型
		
		},{
			title : "产品净重(克)",// 标题
			name : "suttle",
			//sort : "up",
			width : 150,// 宽度
			type:"string"//数据类型
				
		},{
			title : "快递计重(kg)",// 标题
			name : "expWeight",
			//sort : "up",
			width : 150,// 宽度
			type:"string"//数据类型
				
		},{
			title : "快递模板",// 标题
			name : "expTitle",
			//sort : "up",
			width : 150,// 宽度
			type:"string"//数据类型
				
		},{
			title : "产品配料",// 标题
			name : "batching",
			//sort : "up",
			width : 160,// 宽度
			type:"string"//数据类型
			
		},{
			title : "保质期(天)",// 标题
			name : "quality",
			width : 150,// 宽度
			leng : 8,//显示长度
			type:"number"//数据类型
			
		},{
			title : "生产日期",// 标题
			name : "productionStr",
			//sort : "up",
			width : 160,// 宽度
			type:"string"//数据类型
			
		},{
			title : "品牌名称",// 标题
			name : "brandName",
			//sort : "up",
			width : 110,// 宽度
			leng : 8,//显示长度
			type:"string"//数据类型
			
		},{
			title : "产品状态",// 标题
			name : "pstatus",
			//sort : "up",
			width : 100,// 宽度
			type:"number",//数据类型
			customMethod : function(value, data) {
				if(data.pstatus==0){
					return "待上线";
				} 
				if(data.pstatus==1){
					return "已上线";
				} 
				if(data.pstatus==2){
					return "已售罄";
				}
				if(data.pstatus==3){
					return "已下线";
				}
				return "-";
			}
		},
		/*{
			title : "销售城市",// 标题
			name : "strSaleCity",
			//sort : "up",
			width : 250,// 宽度
			type:"string"//数据类型
		},*/
		{
			title : "产地",// 标题
			name : "place",
			//sort : "up",
			width : 110,// 宽度
			type:"string"//数据类型
			
		},{
			title : "库存总数(份)",// 标题
			name : "store",
			//sort : "up",
			width : 110,// 宽度
			type:"number"//数据类型
		},{
			title : "已销售量(份)",// 标题
			name : "sales",
			//sort : "up",
			width : 120,// 宽度
			type:"number"//数据类型
			
		},{
			title : "是否为精选",// 标题
			name : "choice",
			//sort : "up",
			width : 150,// 宽度
			type:"number",//数据类型
			customMethod : function(value, data) {
				if(data.choice==0){
					return "否";
				} 
				if(data.choice==1){
					return "是";
				} 
				return "-";
			}
		},{
			title : "生产厂名",// 标题
			name : "cname",
			//sort : "up",
			width : 80,// 宽度
			type:"string",//数据类型
		},{
			title : "供应商名称",// 标题
			name : "supplierName",
			//sort : "up",
			width : 150,// 宽度
			type:"string",//数据类型
		},{
			title : "供应商联系人",// 标题
			name : "contacts",
			width : 150,// 宽度
			type:"string",//数据类型
		},{
			title : "供应商联系电话",// 标题
			name : "supplierPhone",
			width : 120,// 宽度
			type:"string",//数据类型
		},{
			title : "供应商地址",// 标题
			name : "supplierAddress",
			width : 180,// 宽度
			type:"string",//数据类型
		}/*,
		{
				title : "用户手机",// 标题
				name : "phoneid",
				width : 50,// 宽度
				leng : 8,//显示长度
				type:"stirng",//数据类型
				isLink : true,// 表示当前列是否是超链接 true:是 false：不是
				// 只有当isLink为true时 才有效
				link : {
					linkInfoName : "href", // href , modal ,method
					linkInfo : {
						href : "billmanagerment/allbill/view/init", // 表示超链接调用的连接
					},
					param : ["bid"],// 参数
					permission : "detail"// 单列权限
				}
			}*/],
			//操作列
			handleCols : {
				title : "操作",// 标题
				queryPermission : ["detail","update"],// 不需要选择checkbox处理的权限
				width : 130,// 宽度
				// 当前列的中元素
				cols : [{
//					title : "查看",// 标题
//					linkInfoName : "modal",
//					linkInfo : {
//						modal : {
//							url : "fresh/manage/details.jhtml",// url
//							position:"60px",// 模态框显示位置
//							width:"800px"
//						},
//						param : ["codeId"],// 参数
//						permission : "detail"// 列权限
//					}
//				},
//				{
					title : "修改排序",// 标题
					linkInfoName : "href",
					linkInfo : {
						href : "fresh/hotsalemanage/updateSort.jhtml",// url
						param : ["pid"],// 参数
						permission : "init"// 列权限
					},
					customMethod : function(value, data){
						if(data.pid != null){
							var value1 = "<a href=\"javascript:updateSort('"+data.pid+"')\">" + "修改排序" + "</a>";
							return value1;
						}else{
							var value2 = '<a href="javascript:;" disabled="disabled" style="color:#CDCDCD;"></a>';
							return value2;
						}
					}
				},{
					title : "编辑",// 标题
					linkInfoName : "href",
					linkInfo : {
						href : "fresh/manage/updateView.jhtml",// url
						param : ["pid"],// 参数
						permission : "update"// 列权限
					},
					customMethod : function(value, data){
						return "<a href=\"javascript:updateViewCheck('"+data.codeId+"','"+data.pid+"')\">" + "编辑" + "</a>";
					}
				}] 
	}},permissions);
}

function updateViewCheck(codeId,pid){
	$.ajax({
		type : 'post',
		url : "fresh/manage/updateCheck.jhtml",
		data : {"codeId":codeId},
		dataType : 'json',
		beforeSend : function(XMLHttpRequest) {
			$('#prompt').show();
		},
		success : function(data) {
			$('#prompt').hide();
			if(Boolean(data.success)){
				var data = $('#searchBillForm').serializeArray();
				data = jsonFromt(data);
				data.page=page;
				data.pid=pid;
				console.log(data);
				myPost("fresh/manage/updateView.jhtml",data)
			}else{
				showSmReslutWindow(data.success, data.msg);
			}
		},
		error : function() {
			window.messager.warning(data.msg);
		}
	});
}

function substr(obj,length){
	if(obj.length > length){
		obj = obj.substring(0,length) +"...";
	}
	return obj;
}

// 时间对象的格式化;
Date.prototype.format = function(format){
	 var o = {
	  "M+" : this.getMonth()+1, //month
	  "d+" : this.getDate(), //day
	  "h+" : this.getHours(), //hour
	  "m+" : this.getMinutes(), //minute
	  "s+" : this.getSeconds(), //second
	  "q+" : Math.floor((this.getMonth()+3)/3), //quarter
	  "S" : this.getMilliseconds() //millisecond
	 };
	   
  	if(/(y+)/.test(format)) {
	  format = format.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));
	}
	  
	  for(var k in o) {
		  if(new RegExp("("+ k +")").test(format)) {
		 	 format = format.replace(RegExp.$1, RegExp.$1.length==1 ? o[k] : ("00"+ o[k]).substr((""+ o[k]).length));
		  }
	  }
	 return format;
};


/**
 * 导出无效产品
 */
$("#exportProductFailingBtn").click(function(){
	$("#exportProductFailingModal").modal();
	initImportSerial();
});


/**
 * datetimepicker 转化
 */
function initDates() {
	$("input.form-datetime").datetimepicker({
		autoclose : true,
		format : 'yyyy-mm-dd',
		todayBtn : true,
		minView:2
	});
}

//初始化方法
function initImportSerial(){
	$("#importserial_chosen span:first-child").html("--请选择--");
	$("#importserial").chosenObject({
		hideValue : "importserial",
		showValue : "rdateStr",
		url : "fresh/manage/initFailingSerial.jhtml",
		isChosen:true,//是否支持模糊查询
		isCode:true,//是否显示编号
		isHistorical:false,//是否使用历史已加载数据
		width:"100%"
	});
	// 当原始select中的选项发生变化时通知chosen更新选项列表
    $("#importserial").trigger('chosen:updated');
}

/**
 * 确认导出
 */
$("#exportconfirm").click(function(){
	$form = $("#exportform").attr("action","fresh/manage/exportFailingInfo.jhtml");
	var importserial=$("#importserial").val();
	if(importserial=="" || importserial ==undefined){
		showWarningWindow("warning","请选择序列号!");
		setTimeout(function(){
			$('#sm_reslut_window').modal('hide');
		}, 1500);
		return ;
	}
	$form[0].submit();
	$("#exportProductFailingModal").modal('hide');
});


/**
 * add by lifeng 20160721
 * 批量上线
 */	
function beachOnLine(){
	$("#beachOnLine").click(function(){
		console.log(freshList.getIds());
		if(!freshList.getIds()){
			showWarningWindow("warning","请至少选择一条记录！");
			return;
		}
		if(!freshList.validateChose("pstatus", "0,1,3", "产品已售罄不能上线")){
			return;
		}
		if(!freshList.validateChose("pstatus", "0,2,3", "商家已上线不能再次上线")){
			return;
		}
		var data = {ids:freshList.getIds(),pstatus:1};
		requestBeach(data,"你确定要上线选中产品？");
	});
}

/**
 * 批量下线
 */	
function beachDownLine(){
	 $("#beachDownLine").click(function(){
		    var flag=true;
			if(!freshList.getIds()){
				showWarningWindow("warning","请至少选择一条记录！");
				return;
			}
			if(!freshList.validateChose("pstatus", "1", "产品还未上线不能下线")){
				return;
			}
			var data = {ids:freshList.getIds(),pstatus:3};
			requestBeach(data,"你确定要下线选中产品？");
	 })
}

function batchConfirmHotSale(){
	$("#confirmHotSale").click(function(){
		if(!freshList.getIds()){
			showWarningWindow("warning","请至少选择一条记录！");
			return;
		}
		if(!freshList.validateChose("choice", "0", "该商品已是精选！")){
			return;
		}
		var data = {"ids":freshList.getIds(),"choice":1};
		requestBeach(data,"确定设为精选？");
	})
}

//发送请求
function requestBeach(data,title){
	showSmConfirmWindow(function() {
					$.ajax({
				        type: "POST",
				        url: "fresh/manage/batch.jhtml",
				        data: data,
				        dataType: "json",
				        success: function(result){
							showSmReslutWindow(result.success, result.msg);
							freshList.reload();
				         }
				    });
			},title);
}

//弹出修改热卖产品的排序模态框
function updateSort(pid){
	$("#updateSortModal").modal();
	$("#freshPid").val(pid);
}

/**
 * 确定修改精选排序
 */
$("#updateSortConfirm").click(function() {
	var sort = $("#sortId").val();
	var reg = /^\d+$/;
	if(!reg.test(sort)){
		submitDataError($("#sortId"),"请输入整数排序数值!");
		return false;
	}
	$.ajax({
		url : "fresh/manage/updateSort.jhtml",
		type : "post",
		dataType : "json",
		data:{"pid":$("#freshPid").val(),"sort":sort},
		success : function(data) {
				showSmReslutWindow(data.success, data.msg);
				freshList.reload();
				$("#sortId").val("");
				$("#sortId").attr("placeholder",'输入非负整数数值');
				$('#updateSortModal').modal('hide');
		}
	});
})

$("#addFresh_btn").on("click",function(){
	var data = $('#searchBillForm').serializeArray();
	data = jsonFromt(data);
	data.page=page;
	console.log(data);
	myPost("fresh/manage/addView.jhtml",data)
	//window.location.href="fresh/manage/addView.jhtml?isType=add&freshPage="+page;
});

