var commentList;
$(document).ready(function() {
	commentList = $('#commentList').page({
		url : 'vstarComment/init/list.jhtml',
		success : success,
		pageBtnNum : 10,
		pageSize : 15,
		paramForm : 'searchActivityForm'
	});

	inserTitle(' > 新食尚大赛 > <a href="vstarComment/init.jhtml" target="right">评论管理</a>', 'commentList', true);

});

/**
 * 构件表格
 * 
 * @param data
 * @param obj
 */
function success(data, obj) {
	var callbackParam = "isBackButton=true&callbackParam=" + getFormParam($("#searchActivityForm").serialize());
	obj.find('div').eq(0).scrollTablel({
		tableClass : "table-bcommented table-striped info",
		callbackParam : callbackParam,
		//数据
		data : data.content,
		//数据行
		cols : [ {
			title : "选手编号", // 标题
			name : "playerId",
			//sort : "up",
			width : 150, // 宽度
			type : "number", //数据类型					
		}, {
			title : "姓名", // 标题
			name : "playerName",
			width : 200, // 宽度
			type : "string", //数据类型
			customMethod : function(value, data) {
				return data.playerName+"("+data.playerPhone+")";
			}

		}, {
			title : "手机号码", // 标题
			name : "playerPhone",
			width : 150, // 宽度
			type : "string" //数据类型
		}
		, {
			title : "评论内容", // 标题
			name : "content",
			width : 500, // 宽度
			type : "string" //数据类型
		}
		, {
			title : "评论用户", // 标题
			name : "userName",
			width : 200, // 宽度
			type : "string", //数据类型
			customMethod : function(value, data) {
				if(data.userName||data.userPhone){
					return data.userName+"("+data.userPhone+")";
				}else{
					return '(未登录用户)';
				}
			}
		}
		, {
			title : "评论时间", // 标题
			name : "createTimeStr",
			width : 150, // 宽度
			type : "string" //数据类型
		}
		],
		//操作列
		handleCols : {
			title : "操作", // 标题
			queryPermission : [ "list","add","edit" ], // 不需要选择checkbox处理的权限
			width : 200, // 宽度
			// 当前列的中元素
			cols : [  
				{
					title : "链接", // 标题
					linkInfoName : "href",
					linkInfo : {
						href : "vstarComment/delete.jhtml", // url
						param : [ "id" ], // 参数
						permission : "edit" // 列权限
					},
					customMethod : function(value, data) {
							return "<a href='javascript:;' onclick='delete_fun("+data.id+")'  >删除</a>"
					}
				}
				]
		}
	}, permissions);

	
	
}


function delete_fun(id){
	showSmConfirmWindow(function() {
 		$.ajax({
 			type : 'post',
 			url : 'vstarComment/delete.jhtml',
 			data : {"id":id},
 			dataType : 'json',
 			beforeSend : function(XMLHttpRequest) {
 				$('#prompt').show();
 			},
 			success : function(data) {
 				$('#prompt').hide();

 				if (data.success) {
 					commentList.reset();
 				}

 				showSmReslutWindow(data.success, data.msg);
 			},
 			error : function(XMLHttpRequest, textStatus, errorThrown) {
 				$('#prompt').hide();
 			}
 		});
 	},'确定要删除吗?');
}






