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
				range:"推荐排序必须1至999之间的整数"
			}
		}
	}, broadcastSave);
	
	
	//加载正在直播的主播通告
	initRecordId();
	
});




/**
 * 保存广播信息
 */
function broadcastSave() {
//	debugger;
	var url;
	var suffix = ".jhtml";
	url = "livePageHome/manage/addAnchorVideo" + suffix;
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
					var url = contextPath +'/livePageHome/manage/init.jhtml?operationType=3';
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
//	debugger;
	var result=true;
	var assignRoom = $("input[name='assignRoom']:checked").val();
	var nickname=$("#nickname").val();
	if(assignRoom=='1' && (nickname == null || nickname=="")){
		showWarningWindow("warning","请选择主播!",9999);
		rsult=false;
		return ;
	}
	
	return result;
}



//初始化主播房间
function initRecordId(){
//	debugger;
	var url="livePageHome/manage/initAnchorVideoId.jhtml";
	
	$("#id").chosenObject({
		hideValue : "id",
		showValue : "title",//输入查询值
		url : url,
		isChosen:true,//是否支持模糊查询
		isCode:true,//是否显示编号
		isHistorical:false,//是否使用历史已加载数据
		width:"100%"
	});
}

