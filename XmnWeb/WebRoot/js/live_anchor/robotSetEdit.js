var formId = "editFrom";
var imgRoot = $("#fastfdsHttp").val();
var jsonTextInit;
$(function() {
	var dataformInit = $("#" + formId).serializeArray();
	jsonTextInit = JSON.stringify({
		dataform : dataformInit
	});
	validate(formId, {
		rules : {
			robotMinNums : {
				required : true,
				digits : true,
				range : [0,10000]
			},
			robotSetMixNums : {
				required : true,
				digits : true,
				range : [0,10000]
			},
			robotSetMaxNums : {
				required : true,
				digits : true,
				range : [0,10000]
			},
			robotMaxNums : {
				required : true,
				digits : true,
				range : [0,100000]
			},
			multiple : {
				required : true,
				digits : true,
				range : [1,100000]
			}
		},
		messages:{
			robotMinNums:{
				required:"默认机器人不能为空",
				digits:"只能输入数字",
				range : "只能为0-10000之间的整数"
			},
			robotSetMixNums:{
				required:"匹配机器人最小值不能为空",
				digits:"只能输入数字",
				range : "只能为0-10000之间的整数"
			},
			robotSetMaxNums:{
				required:"匹配机器人最大值不能为空",
				digits:"只能输入数字",
				range : "只能为0-10000之间的整数"
			},
			robotMaxNums:{
				required:"机器人上限不能为空",
				digits:"只能输入数字",
				range : "只能为0-10000之间的整数"
			},
			multiple:{
				required:"机器人显示倍数不能为空",
				digits:"只能输入数字",
				range:"只能为1-100000之间的整数"
			}
		}
	}, robotSetSave);
	
	
});


/**
 * 保存机器人配置信息
 */
function robotSetSave() {
	var url;
	var suffix = ".jhtml";
	var isAdd = $("#" + formId).find("input[name='id']").length == 0 ? true : false;
	if (isAdd) {// 添加操作
		url = "liveVideo/manage/robotSet" + suffix;
	} else {// 修改操作
		url = "liveVideo/manage/robotSet" + suffix;
	}
	var dataform = $("#" + formId).serializeArray();
	var jsonText = JSON.stringify({
		dataform : dataform
	});
	
	var validateResult=customerValidate();
	if(!validateResult){
		return ;
	}
	
	if (isAdd || jsonTextInit != jsonText) {// 新增或修改数据才会发送请求
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
//		showSmReslutWindow(false, "没做任何修改！");
	}
}

/**
 * 自定义校验函数
 */
function customerValidate(){
	var result=true;
	var robotSetMixNums=$("#robotSetMixNums").val();
	var robotSetMaxNums=$("#robotSetMaxNums").val();
	if(parseInt(robotSetMaxNums)<parseInt(robotSetMixNums)){
		showWarningWindow("warning", "匹配机器人最大值不能小于最小值！",9999);
		result=false;
	}
	return result;
}
