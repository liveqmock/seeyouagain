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
			title:{
				required:true,
				maxlength:200
			},
			videoUrl : {
				required : true,
				maxlength:200
			},
			sort:{
				required: true,
				digits:true,
				range:[1,99]
			}
		},
		messages:{
			sort:{
				required:"请输入排序值",
				digits:"请输入整数",
				range:"输入值必须为1-99之间的整数"
			}
		}
	}, anchorImageSave);
	
	//初始化主播下拉框
	initAnchorId();
	
	// 页面图片
	$("#coverUrlDiv").uploadImg({
		urlId : "coverUrl",
		showImg : $('#coverUrl').val()
	});
	
});




/**
 * 保存礼物信息
 */
function anchorImageSave() {
//	debugger;
	var url;
	var suffix = ".jhtml";
	var isAdd = $("#" + formId).find("input[name='id']").length == 0 ? true : false;
	if (isAdd) {// 添加操作
		url = "liveAnchorVideo/manage/add" + suffix;
	} else {// 修改操作
		url = "liveAnchorVideo/manage/update" + suffix;
	}
	var dataform = $("#" + formId).serializeArray();
	var jsonText = JSON.stringify({
		dataform : dataform
	});
	
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
					if (isAdd) {
						recordList.reset();
					} else {
						recordList.reload();
					}
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
	
	var coverUrl=$("#coverUrl").val();
	if(coverUrl == null || coverUrl==""){
		showWarningWindow("warning","请上传页面配图 !",9999);
		rsult=false;
		return ;
	}
	
	var anchorId=$("#anchorId").val();
	if(anchorId == null || anchorId==""){
		showWarningWindow("warning","请选择主播 !",9999);
		rsult=false;
		return ;
	}
	
	return result;
}


//初始化主播下拉框
function initAnchorId(){
//	debugger;
	$("#anchorId").chosenObject({
		hideValue : "id",
		showValue : "nickname",
		url : "anchor/manage/initAnchorId.jhtml",
		isChosen:true,//是否支持模糊查询
		isCode:true,//是否显示编号
		isHistorical:false,//是否使用历史已加载数据
		width:"100%"
	});
}


//选择主播后，修改主播昵称隐藏域的值
$('body').on("click",'#anchorId_chosen .chosen-results li',function(){
//	debugger;
	var anchorId =  $("#anchorId").find("option:selected").val();
	$.ajax({
		type: "POST",
		url : "anchor/manage/getAnchorById.jhtml?t=new Date()",
		dataType : "json",
		data: {"id":anchorId},
		success : function(data){
			if(data != null){
				$("#anchorName").val(data.nickname);
			}
		}
	});
});


