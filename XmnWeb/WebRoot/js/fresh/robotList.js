var robotList;
var initListUrl = "fresh/robot/init/list.jhtml";
var imgRoot = $("#fastfdsHttp").val();
$(function() {
	inserTitle(
			' > 积分超市管理 > <a href="fresh/robot/init.jhtml" target="right">机器人管理</a>',
			'userSpan', true);
	robotList = $("#robotList").page({
		url : initListUrl,
		success : success,
		pageBtnNum : 10,
		paramForm : 'searchForm',
	});
	
});

function success(data, obj) {
	var formId = "shareForm", title = "机器人列表", subtitle = "个机器人";
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
	    checkable : true,
		// 操作列
		handleCols : {
			title : "操作",// 标题
			queryPermission : [ "update","detail","delete" ],// 不需要选择checkbox处理的权限
			width : 150,// 宽度
			// 当前列的中元素
			cols : [ 
				 /*{
					title : "删除",// 标题
					linkInfoName : "href",
					linkInfo : {
						href : "liveAndroid/manage/delete.jhtml",// url
						param : ["id"],// 参数
						permission : "delete"// 列权限
					},
					customMethod : function(value, data){
				            var value = "<a href='javascript:confirmDelete(\""+data.id+"\")'>" + "删除" + "</a>";
				            return value;
				    }
				 },*/
		         {
					title : "修改",// 标题
					linkInfoName : "modal",
					linkInfo : {
						modal : {
							url : "fresh/robot/update/init.jhtml",
							position : "",
							width : "auto",
							title : "修改机器人"
						},
						param : [ "id" ],
						permission : "update"
					}
				} 
			]
		},
		cols : [ {
			title : "机器人编号",
			name : "id",
			type : "string",
			width : 150
		}, {
			title : "昵称",
			name : "uname",
			type : "string",
			width : 150
		}, {
			title : "头像",
			name : "avatar",
			type : "string",
			width : 150,
			customMethod : function(value, data) {
				return '<img style="width:50px;height:50px;" src="'+imgRoot+value + '"/>';
			}
		}, {
			title : "性别",
			name : "sex",
			type : "string",
			width : 150,
			customMethod : function(value, data) {
				var result;
				switch (value) {
				case 1:
					result = "男";
					break;
				default:
					result = "女";
					break;
				}
				return result;
			}
		} , {
			title : "关注",
			name : "concernNum",
			type : "string",
			width : 150
		}  , {
			title : "等级",
			name : "levelsId",
			type : "string",
			width : 150
		}  , {
			title : "直播动态",
			name : "conditionNum",
			type : "string",
			width : 150
		} , {
			title : "去过店铺总数",
			name : "storeNum",
			type : "string",
			width : 150
		} , {
			title : "粉丝总数",
			name : "fansNum",
			type : "string",
			width : 150
		} 
		]
	}, permissions);
}

/**
 * 删除操作
 */
 function confirmDelete(id){
	 showSmConfirmWindow(function (){
		 $.ajax({
			 url : "liveAndroid/manage/delete.jhtml",
			 type : "post",
			 dataType : "json",
			 data:'id=' + id,
			 success : function(result) {
				 if (result.success) {
					 showSmReslutWindow(result.success, result.msg);
					 setTimeout(function() {
						 robotList.reload();
					 }, 3000);
				 } else {
					 window.messager.warning(result.msg);
				 }
			 }
		 });
	 },"确定要删除吗？");
 }
 
 
 /**
  * 批量删除
  */
 $('#delete').click(function(){
 	var ids = robotList.getIds();
 	if(!ids){
 		showWarningWindow("warning","请至少选择一条记录！");
 		return;
 	}
 	showSmConfirmWindow(function() {
 		$.ajax({
 			type : 'post',
 			url : 'fresh/robot/delete.jhtml' + '?t=' + Math.random(),
 			data : 'ids=' + ids,
 			dataType : 'json',
 			beforeSend : function(XMLHttpRequest) {
 				$('#prompt').show();
 			},
 			success : function(data) {
 				$('#prompt').hide();

 				if (data.success) {
 					robotList.reset();
 				}

 				showSmReslutWindow(data.success, data.msg);
 			},
 			error : function(XMLHttpRequest, textStatus, errorThrown) {
 				$('#prompt').hide();
 			}
 		});
 	});
 });
 
 
 /**
  * 清除机器人
  */
 $('#deleteAll').click(function(){
 	showSmConfirmWindow(function() {
 		$.ajax({
 			type : 'post',
 			url : 'fresh/robot/deleteAll.jhtml' + '?t=' + Math.random(),
 			data : {},
 			dataType : 'json',
 			beforeSend : function(XMLHttpRequest) {
 				$('#prompt').show();
 			},
 			success : function(data) {
 				$('#prompt').hide();

 				if (data.success) {
 					robotList.reset();
 				}

 				showSmReslutWindow(data.success, data.msg);
 			},
 			error : function(XMLHttpRequest, textStatus, errorThrown) {
 				$('#prompt').hide();
 			}
 		});
 	},'确定删除所有机器人信息?');
 });
 
 
 /**
  * 清除机器人头像
  */
 $('#deleteImages').click(function(){
 	showSmConfirmWindow(function() {
 		$.ajax({
 			type : 'post',
 			url : 'liveAndroid/manage/deleteImages.jhtml' + '?t=' + Math.random(),
 			data : {},
 			dataType : 'json',
 			beforeSend : function(XMLHttpRequest) {
 				$('#prompt').show();
 			},
 			success : function(data) {
 				$('#prompt').hide();

 				if (data.success) {
 					robotList.reset();
 				}

 				showSmReslutWindow(data.success, data.msg);
 			},
 			error : function(XMLHttpRequest, textStatus, errorThrown) {
 				$('#prompt').hide();
 			}
 		});
 	});
 });