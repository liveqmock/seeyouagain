var enrollList1;//实名审核
var imgRoot = $("#fastfdsHttp").val();

$(document).ready(function() {
	inserTitle('> 新食尚大赛> <a href="VstarRealName/manage/init.jhtml" target="right">实名审核管理</a>','enrollConfirm',true);
	
	pageInit();
	
	//通过
	putaway();
	
	//拒绝
	removeOffshelf();
});

/**
 * 加载页面数据
 */
function pageInit(){
	enrollList1 = $('#enrollList1').page({
		url : 'VstarRealName/manage/init/list.jhtml',
		success : success,
		pageBtnNum : 10,//默认翻页按钮数量
		pageSize : 10,//每页条数
		paramForm : 'searchForm1',
		param :{}
	});
}


/**
 * 查询实名审核信息成功回调函数
 * @param data
 * @param obj
 */
function success(data, obj) {
	var formId = "searchForm1", title = "实名审核列表", subtitle = "条信息";
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
			queryPermission : [ "update","delete" ],// 不需要选择checkbox处理的权限
			width : 120,// 宽度
			// 当前列的中元素
			cols : [ 
				 {
					title : "拒绝",// 标题
					linkInfoName : "href",
					linkInfo : {
						href : "VstarRealName/manage/update.jhtml",// url
						param : ["id"],// 参数
						permission : "update"// 列权限
					},
					customMethod : function(value, data){
							var result="";
							if(data.status==4){
								result = "<a href='javascript:update("+data.id+",6"+")'>" + "拒绝" + "</a>";
							}
							return result;
				    }
				 }, {
						title : "通过",// 标题
						linkInfoName : "href",
						linkInfo : {
							href : "VstarRealName/manage/update/init.jhtml",
							param : [ "id" ],
							permission : "update"
						},
						customMethod : function(value, data){
							var result="";
							if(data.status==4){
								result = "<a href='javascript:update("+data.id+",5"+")'>" + "通过" + "</a>";
							}
							return result;
						}
					 } 
			]
		},
		cols : [ {
			title : "报名编号",
			name : "id",
			type : "string",
			width : 70
		},{
			title : "身份证正面",
			name : "imgUrl201",
			type : "string",
			width : 220,
			customMethod : function(value, data) {
//				debugger;
				var html='';
				html+='<div class="good-table-imgmodule">'
					+    '<img src="'+imgRoot+value+'"  class="" style=""/>'
		          
		            +    '<div class="good-table-img-price">'
		            +    '   <span>'+(data.identityName==undefined?"-":data.identityName)+'</span><span>身份证号：<em>'+ (data.identityNumber==undefined?"-":data.identityNumber) +'</em> </span>' 
		            +    '</div>'
		            +'</div>';
				return html;
			}
		},{
			title : "身份证反面",
			name : "imgUrl202",
			type : "string",
			width : 210,
			customMethod : function(value, data) {
				return '<img src="'+imgRoot+value + '"/>';
			}
		},{
			title : "持证照",
			name : "imgUrl203",
			type : "string",
			width : 210,
			customMethod : function(value, data) {
				return '<img src="'+imgRoot+value + '"/>';
			}
		},{
			title : "报名照片",
			name : "imgUrl101",
			type : "string",
			width : 210,
			customMethod : function(value, data) {
				return '<img src="'+imgRoot+value + '"/>';
			}
		},{
			title : "手机号码",
			name : "phone",
			type : "string",
			width : 90
		},{
			title : "状态",
			name : "statusText",
			type : "string",
			width : 100
		} ]
	}, permissions);
}

/**
 * 更新报名审核状态
 * @param id
 * @param status
 * @param telphones
 */
function update(id,status){
	 $.ajax({
        url : "VstarRealName/manage/update.jhtml",
        type : "post",
        dataType : "json",
        data:{"id":id,"status":status},
        success : function(result) {
       	 if (result.success) {
    			showSmReslutWindow(result.success, result.msg);
    			setTimeout(function() {
    				pageInit();
    			}, 1000);
    		} else {
    			window.messager.warning(result.msg);
    		}
        }
    });
}


/**
 * 批量通过
 */	
function putaway(){
	$("#putaway").click(function(){
		console.log(enrollList1.getIds());
		if(!enrollList1.getIds()){
			showWarningWindow("warning","请至少选择一条记录！");
			return;
		}
		if(!enrollList1.validateChose("status", "4", "非待审核数据不可执行此操作")){
			return;
		}
		var data = {ids:enrollList1.getIds(),status:'5'};
		updateBatch(data,"你确定要通过选中信息？");
	});
}

/**
 * 批量拒绝
 */	
function removeOffshelf(){
	$("#removeOffshelf").click(function(){
		console.log(enrollList1.getIds());
		if(!enrollList1.getIds()){
			showWarningWindow("warning","请至少选择一条记录！");
			return;
		}
		if(!enrollList1.validateChose("status", "4", "非待审核数据不可执行此操作")){
			return;
		}
		var data = {ids:enrollList1.getIds(),status:'6'};
		updateBatch(data,"你确定要拒绝选中信息？");
	});
}

/**
* 批量更新商品上架状态
* @param data
* @param title
*/
function updateBatch(data,title){
	showSmConfirmWindow(function() {
					$.ajax({
				        type: "POST",
				        url: "VstarRealName/manage/updateRealNameBatch.jhtml",
				        data: data,
				        dataType: "json",
				        success: function(result){
							showSmReslutWindow(result.success, result.msg);
							enrollList1.reload();
				         }
				    });
			},title);
}

