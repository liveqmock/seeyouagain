var formId = "editFrom";
var imgRoot = $("#fastfdsHttp").val();
var jsonTextInit;
$(function() {
	var dataformInit = $("#" + formId).serializeArray();
	jsonTextInit = JSON.stringify({
		dataform : dataformInit
	});
	
	//页面文本初始化
	contentInit();
	
	validate(formId, {
		rules : {
			
		},
		messages:{
			
		}
	}, save);
	
});




/**
 * 保存报名信息
 */
function save() {
//	debugger;
	var url;
	var suffix = ".jhtml";
	var isAdd = $("#" + formId).find("input[name='id']").length == 0 ? true : false;
	if (isAdd) {// 添加操作
		url = "VstarEnroll/manage/add" + suffix;
	} else {// 修改操作
		url = "VstarEnroll/manage/update" + suffix;
	}
	var dataform = $("#" + formId).serializeArray();
	var jsonText = JSON.stringify({
		dataform : dataform
	});
	
	converData();
	
	var result=validateCustomer();
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
					pageInit();
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
 * 自定义校验方法
 */
function validateCustomer(){
	var result=true;
	
	return result;
}

/**
 * 转换数据
 */
function converData(){
	var refusedTypeArray = new Array(); 
	$('input[name="refusedTypes"]:checked').each(function(){ 
		refusedTypeArray.push($(this).val()); 
    }); 
	var refusedType=refusedTypeArray.join(',');
	$("#refusedType").val(refusedType);
}

/**
 * 文本内容初始化
 */
function contentInit(){
	var confining =$("#confining").val();
	if('1'==confining){
		$("#title").html("是否限制选手显示");
		$("#reason").html("限制原因：<span style='color:red;'>*</span>");
	}
	
}
