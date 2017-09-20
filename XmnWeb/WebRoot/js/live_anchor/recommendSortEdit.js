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
			sequenceNo : {
				required :true,
				digits:true,
				range:[1,999]
			}
		},
		messages:{
			sequenceNo:{
				required:"请输入推荐排序",
				digits:"推荐排序必须为正整数",
				range:"推荐排序必须为1至999之间的整数"
			}
		}
	}, broadcastSave);
	
});




/**
 * 保存广播信息
 */
function broadcastSave() {
//	debugger;
	var url;
	var suffix = ".jhtml";
	url = "livePageHome/manage/update" + suffix;
	var dataform = $("#" + formId).serializeArray();
	var jsonText = JSON.stringify({
		dataform : dataform
	});
	
	var result=validateCustomer();
	if(!result){//自定义校验不通过
		return ;
	}
	
	if (jsonTextInit != jsonText) {// 添加或者修改改了东西才会发送请求
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
					var operationType=getOperationType();
					var url = contextPath +'/livePageHome/manage/init.jhtml?operationType='+operationType;
					setTimeout(function() {
						location.href = url;
					}, 1000);
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
 * 将直播类型转化为tab选项卡序号
 */
function getOperationType(){
	var operationType=0;
	var zhiboTypeParam=$("#zhiboTypeParam").val();
	switch (zhiboTypeParam) {
	case '0':
		operationType=0;
		break;
	case '1':
		operationType=1;
		break;
	case '3':
		operationType=2;
		break;
	default:
		operationType=0;
		break;
	}
	return operationType;
}