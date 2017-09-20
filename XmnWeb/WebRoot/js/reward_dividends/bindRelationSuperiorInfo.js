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
			referrerType : {
				required : true
			},
			limitBalance:{
				required:true,
				number:true,
				range:[0,1000000000]
			}
		},
		messages:{
			referrerType:{
				required:"请选择推荐人类型"
			},
			limitBalance:{
				required:"请输入鸟币限制最低余额",
				number:"请输入0-1000000000之间的数字",
				range:"请输入0-1000000000之间的数字"
			}
		}
	}, save);
	
	//加载上级信息
	loadSuperiorInfo();
	
	//初始化间接收益人下拉框
	indirectIdInit();
	
	//编辑控制
	editControl();
	
});




/**
 * 保存绑定上级信息
 */
function save() {
//	debugger;
	var url;
	var suffix = ".jhtml";
	url = "bursRelationChain/manage/bindSuperiorInfo" + suffix;
	var dataform = $("#" + formId).serializeArray();
	
	var jsonText = JSON.stringify({
		dataform : dataform
	});
	
	var result=validateCustomer();
	if(!result){//自定义校验不通过
		return ;
	}
	
	if (jsonTextInit != jsonText) {// 修改了东西才会发送请求
		$.ajax({
			type : 'post',
			url : url,
			data : jsonFromt($('#' + formId).serializeArray()),
			dataType : 'json',
			beforeSend : function(XMLHttpRequest) {
				$('#prompt').show();
				//提交后禁用提交按钮，防止重复提交表单
				$('#submitBtn').attr("disabled","disabled");
			},
			success : function(data) {
				$('#prompt').hide();
				$('#triggerModal').modal('hide');
				if (data.success) {
					anchorList.reload();
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
		showWarningWindow("warning", "没做任何修改！");
	}
}

/**
 * 自定义校验方法
 */
function validateCustomer(){
//	debugger;
	var result=true;
	
	var objectOriented = $("#objectOriented").val();//会员渠道来源 1.VIP 2.商家 3.直销 4.v客
	var uidRelationChainLevel=$("#uidRelationChainLevel").val();//会员层级
	if(objectOriented ==2 && uidRelationChainLevel==1){
		showWarningWindow("warning","商家一级会员不可绑定上级!",9999);
		result=false;
		return result;
	}
	
	var superiorIdChosen=$("#superiorIdChosen").val();//上级直播会员id
	if(superiorIdChosen==null || superiorIdChosen==''){
		showWarningWindow("warning","请选择上级!",9999);
		result=false;
		return result;
	}
	
	var indirectId=$("#indirectId").val();
	if(indirectId==null || indirectId==''){
		showWarningWindow("warning","请选业绩归属人!",9999);
		result=false;
		return result;
	}
	
	if(!indirectValidate()){
		result=false;
		return result;
	};
	
	return result;
}


/**
 * 加载上级信息
 * @param uid
 */
function loadSuperiorInfo(){
	var rechargeV=getRechargeInfo();//发生V客充值，不可修改上级
	
	console.log("rechargeV="+rechargeV);
	
	var superior=$("#superior").val();
	if(superior!="" && superior != undefined && rechargeV){
		$("#superiorDiv").css("display","block");
	}else{
		$("#superiorIdChosenDiv").css("display","block");
	}
	initLiverId();
};

/**
 * 获取充实信息
 * @returns {Boolean}
 */
function getRechargeInfo(){
	var result=false;
	var currentUid=$("#uid").val();//当前直播会员UID
	// 设置同步
    $.ajaxSetup({
        async: false
    });
	$.ajax({
		type: "POST",
		url : "liveMember/manage/bindSuperiorInfo/getRechargeInfo.jhtml?t=new Date()",
		dataType : "json",
		data: {"uid":currentUid},
		beforeSend : function(XMLHttpRequest) {
			
		},
		success : function(data){
			if(data>0){
				result=true;
			}
		}
	});
	// 恢复异步
    $.ajaxSetup({
        async: true
    });
	
	return result;
}


//初始化直播用户下拉框,排除等下级
function initLiverId(){
//	debugger;
	var currentUid=$("#uid").val();
	var objectOriented = $("#objectOriented").val();
	var filterVal = currentUid+"|"+objectOriented;
	$("#superiorIdChosen").chosenObject({
		hideValue : "id",
		showValue : "showValue",//输入查询值
		showType:"multiple",//选项显示形式
		showParams:["phone","nickname"],//showType为multiple时生效,phone|nickname
		url : "bursRelationChain/manage/initLiverExclude.jhtml",
		filterVal:filterVal,
		isChosen:true,//是否支持模糊查询
		isCode:false,//是否显示编号
		isHistorical:false,//是否使用历史已加载数据
		width:"100%"
	});
}

/**
 * 初始化间接收益人
 */
function indirectIdInit(){
	//initLiverId
	$("#indirectId").chosenObject({
		hideValue : "id",
		showValue : "showValue",//输入查询值
		showType:"multiple",//选项显示形式
		showParams:["phone","nickname"],//showType为multiple时生效,phone|nickname
		url : "anchor/manage/initLiverId.jhtml",
		isChosen:true,//是否支持模糊查询
		isCode:false,//是否显示编号
		isHistorical:false,//是否使用历史已加载数据
		width:"100%"
	});
	
}

//监听间接收益人按键事件
$("#indirectId_chosen").keydown( function(e) {
    var key = window.event?e.keyCode:e.which;
    if(key.toString() == "13"){
    	indirectValidate();
    }
});


$('body').on("click",'#indirectId_chosen .chosen-results li',function(){
	indirectValidate();
});

/**
 * 绑定间接收益人校验
 */
function indirectValidate(){
//	debugger;
	var result = true;
	var currentUid=$("#uid").val();//当前直播会员UID
	var superiorIdChosen=$("#superiorIdChosen").val();//上级直播会员id
	var indirectId =  $("#indirectId").val();//间接上级直播会员id
	var objectOriented=$("#objectOriented").val();//会员渠道来源 1.VIP 2.商家 3.直销 4.V客
	
	if(objectOriented!=4){
		return result;
	}
	
	// 设置同步
    $.ajaxSetup({
        async: false
    });
    
	$.ajax({
		type: "POST",
		url : "bursRelationChain/manage/bindSuperiorInfo/indirectValidate.jhtml?t=new Date()",
		dataType : "json",
		data: {"uid":currentUid,"superiorIdChosen":superiorIdChosen,"indirectId":indirectId,"objectOriented":objectOriented},
		beforeSend : function(XMLHttpRequest) {
			//禁用提交按钮，验证通过后启用提交
			$('#submitBtn').attr("disabled","disabled");
		},
		success : function(data){
			if(data.success){
				$('#submitBtn').attr("disabled",false);
			}else{
				result=data.success;
				showWarningWindow("warning",data.msg,9999);
			}
		}
	});
	
	// 恢复异步
    $.ajaxSetup({
        async: true
    });
    
    return result;
}

/**
 * 编辑控制
 */
function editControl(){
//	debugger;
	var objectOriented = $("#objectOriented").val();//会员渠道来源 1.VIP 2.商家 3.直销 4.v客
	var uidRelationChainLevel=$("#uidRelationChainLevel").val();//会员层级
	
	if(objectOriented == 4){
		$("#indirectUidDiv").css("display","block");
		$("#indirectTips").css("display","block");
	}
	
	if(objectOriented ==2 && uidRelationChainLevel==1){
		$("#superiorIdChosenDiv").css("display","none");
	}
}


