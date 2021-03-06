var recordList;
var initListUrl = "VstarPlayer/manage/init/vstarSellerList.jhtml";
var imgRoot = $("#fastfdsHttp").val();
$(function() {
	
	//加载页面数据
	pageInit();
	

});

/**
 * 加载页面数据
 */
function pageInit(){
	var gid=$("#gid").val();
	recordList = $("#recordList").page({
		url : initListUrl,
		success : success,
		pageBtnNum : 10,
		paramForm : 'searchForm',
		param : {
			activityType : "5",
			gid:gid
		}
	});
}

function success(data, obj) {
	var formId = "searchForm", title = "商家列表", subtitle = "个商家";
	var captionInfo = '<caption style="background:#EED8D8; text-align:left; color:#703636; font-size:16px; line-height:40px;">&nbsp;&nbsp;'
			+ title
			+ '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font>共计【'
			+ data.total
			+ '】' + subtitle + '&nbsp;</font></caption>';
	var callbackParam = "isBackButton=true&callbackParam="
			+ getFormParam($("#" + formId).serialize());
	
	

	obj.find('div').eq(0).scrollTablel({
		identifier : "id",
		callbackParam : callbackParam,
		data : data.content,
		caption : captionInfo,
		// checkable : checkable,
		// 操作列
		handleCols : {
			title : "操作",// 标题
			queryPermission : [ "update","detail","delete" ],// 不需要选择checkbox处理的权限
			width : 150,// 宽度
			// 当前列的中元素
			cols : [{
				title : "放弃",// 标题
				linkInfoName : "href",
				linkInfo : {
					href : "VstarPlayer/manage/init/abandonSeller.jhtml",// url
					param : ["id"],// 参数
					permission : "update"// 列权限
				},
				customMethod : function(value, data){
						var result="";
						if(!data.status){
							result = "<a href='javascript:abandonSeller(\""+data.sellerid+"\",\""+data.uid+"\")'>" + "放弃" + "</a>";
						}
						return result;
			    }
			 }]
		},
		cols : [ {
			title : "商户名称",
			name : "sellername",
			type : "string",
			width : 220
		}, {
			title : "负责人",
			name : "fullname",
			type : "string",
			width : 110,
			customMethod:function(value,data){
				try {
					var fullInfo="";
					if(data.fullname){
						fullInfo=data.fullname;
					}
					
					if(data.phoneid){
						fullInfo+="<br/>("+data.phoneid+")";
					}
					return fullInfo;
				} catch (e) {
					console.log(e);
				}
			}
		},{
			title : "流水累计收益",
			name : "moneySum",
			type : "string",
			width : 180
		}, {
			title : "获得商户收益",
			name : "mikeAmountSum",
			type : "string",
			width : 180,
			customMethod:function(value,data){
				try {
					var mikeAmount="";
					if(value){
						mikeAmount="￥"+value;
					}
					return mikeAmount;
				} catch (e) {
					console.log(e);
				}
				
			}
		} ]
	}, permissions);
}
 

/**
 * 放弃操作
 */
 function abandonSeller(sellerid,uid){
	 showSmConfirmWindow(function (){
		 $.ajax({
			 url : "VstarPlayer/manage/init/abandonSeller.jhtml",
			 type : "post",
			 dataType : "json",
			 data:{sellerid:sellerid,uid:uid},
			 success : function(result) {
				 if (result.success) {
					 $('#prompt').hide();
					 $('#triggerModal').modal('hide');
					 showSmReslutWindow(result.success, result.msg);
					 setTimeout(function() {
						 window.location.href="VstarPlayer/manage/init.jhtml";
					 }, 3000);
				 } else {
					 window.messager.warning(result.msg);
				 }
			 }
		 });
	 },"确定要放弃吗？");
 }
 