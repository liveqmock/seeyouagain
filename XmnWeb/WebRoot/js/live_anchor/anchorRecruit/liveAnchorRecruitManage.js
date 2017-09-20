var anchorRecruitList;
var initListUrl = "liveAnchorRecruit/manage/init/list.jhtml";
var modalId;

$(function() {
	inserTitle(
			' > 直播频道管理 > <a href="liveAnchorRecruit/manage/init.jhtml" target="right">主播招募管理</a>',
			'userSpan', true);

	// 加载页面数据
	pageInit();
	
	/*审核通过*/
	 $("#liverPassedBtn").on("click", function(){
		 liverPassed();
	 });
	 
	 /*审核拒绝*/
	 $("#liverRefusedBtn").on("click", function(){
		 liverRefused();
	 });
});

/**
 * 加载页面数据
 */
function pageInit(){
	anchorRecruitList = $("#anchorRecruitInfoList").page({
		url : initListUrl,
		success : success,
		pageBtnNum : 10,
		paramForm : 'searchFromId',
		param : {
			rtype : "5"
		}
	});	
}

 /**
  * 
  * @param data
  * @param obj
  */
 function success(data, obj) {
 	var title = "主播列表", subtitle = "个主播";
 	var captionInfo = '<caption style="background:#EED8D8; text-align:left; color:#703636; font-size:16px; line-height:40px;">&nbsp;&nbsp;'
 			+ title+ '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font>共计【' + data.total + '】' + subtitle + '&nbsp;</font></caption>';
 	var callbackParam = "isBackButton=true&callbackParam="+ getFormParam($("#searchFromId").serialize());

 	obj.find('div').eq(0).scrollTablel({
 		identifier : "id",
 		callbackParam : callbackParam,
 		data : data.content,
 		caption : captionInfo,
 		// checkable : checkable,
 		// 操作列
 		handleCols : {
 			title : "操作",// 标题
 			queryPermission : [ "view","approve"],// 不需要选择checkbox处理的权限
 			width : 120,// 宽度
 			// 当前列的中元素
 			cols : [ //推荐状态, recommendStatus:1 审核中 2 审核通过 3 审核被拒
 				 {
 					title : "通过",// 标题
 					linkInfoName : "href",
 					linkInfo : {
// 						href : "liveAnchorRecruit/manage/delete.jhtml",// url
 						param : ["id"],// 参数
 						permission : "approve"// 列权限
 					},
 					customMethod : function(value, data){
 						var value = '';
 						var recommendStatus = data.recommendStatus;
 						if (recommendStatus == '1')
 						  value = "<a href='javascript: passed("+data.anchorUid+")'>" + "通过" + "</a>";
 				        return value;
 				    }
 				 },
 				 {
					title : "拒绝",// 标题
					linkInfoName : "href",
					linkInfo : {
//						href : "liveAnchorRecruit/manage/update/init.jhtml",
						param : [ "id" ],
						permission : "approve"
					},
				    customMethod : function(value, data){
				    	var value = '';
 						var recommendStatus = data.recommendStatus;
 						if (recommendStatus == '1')				    	
			                value = "<a href='javascript: refuse("+data.anchorUid+")'>" + "拒绝" + "</a>";
			            return value;
			        }
 				},
 				{
					title : "查看",// 标题
					linkInfoName : "href",
// 					linkInfoName : "modal",
					linkInfo : {
						href : "liveAnchorRecruit/manage/list/viewDetail.jhtml",
//						modal : {
//							url : "liveAnchorRecruit/manage/list/viewDetail.jhtml",// url
//							position:"60px",// 模态框显示位置
//							width:"800px"
//						},
						param : ["anchorUid"],// 参数
						permission : "view"
					},
				    customMethod : function(value, data){
				    	var value = '';
 						var recommendStatus = data.recommendStatus;
 						if (recommendStatus == '1')				    	
			                value = "<a href='javascript:anchorView("+data.anchorUid+")'>" + "查看" + "</a>";
			            return value;
			        }
 				}
 			]
 		},
 		cols : [ {
 			title : "状态",
 			name : "title",
 			type : "string",
 			width : 120,
            customMethod: function(value, data){
            	var result = "";
            	var recommendStatus = data.recommendStatus;
            	switch (parseInt(recommendStatus)) { //必须转成int型  推荐状态 1 审核中 2 审核通过 3 审核被拒
            	case 1:
            		result = "待审核";
            		break;
            	case 2:
            		result = "审核通过";
            		break;
            	case 3:
            		result = "审核被拒";
            		break;            		
            	default:
            		result = '-';
            		break;
            	};
            	return result;
	        }
 		},{
 			title : "来源渠道",
 			name : "topicTypeDesc",
 			type : "string",
 			width : 120,
            customMethod: function(value, data){
            	return "V客推荐";
	        }
 		},{
 			title : "主播类型",
 			name : "anchorName",
 			type : "string",
 			width : 100,
			customMethod: function(value, data){
				var title;
				var liveType = data.liveType;
				if (liveType == 1)
					title = "兼职主播-普通";
				else if (liveType == 2)
					title = "兼职主播-工会";				
				else if (liveType == 3)
					title = "兼职主播-活动";
				else 
					title = "兼职主播-普通";
				return title;
	        }
 		},{
 			title : "主播昵称",
 			name : "uname",
 			type : "string",
 			width : 100,
			customMethod: function(value, data){
				var sex = data.sex == 1 ? "男" : "女" ;
				var name = data.nickname == undefined ? "" : data.nickname;
				return name + "  [" + sex + "]";
	        }
 		},{
 			title : "主播手机",
 			name : "phone",
 			type : "string",
 			width : 150
 		},{
 			title : "V客编号",
 			name : "uid",
 			type : "string",
 			width : 100
 		},{
 			title : "V客手机",
 			name : "verPhone",
 			type : "string",
 			width : 100
 		},{
 			title : "V客名称",
 			name : "verNname",
 			type : "string",
 			width : 100
 		}]
 	}, permissions);
 }

 
	 function anchorView(uid){
		$("#viewAnchorRecruitModal :input").each(function () {  
		     $(this).val("");  
	    });  
		var url='liveAnchorRecruit/manage/list/viewDetail.jhtml?uid='+uid;
		$.ajax({
			type : 'post',
			url : url,
			data :[uid],
			dataType : 'json',
			success : viewSuccess,
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				$('#prompt').hide();
				$('#viewAnchorRecruitModal').modal('hide');
			}
		});
	}
 

   function viewSuccess(data) {
		var imgRoot = $("#fastfdsHttp").val();
		$("#uid").val(data.data.uid);
		$("#name").html(data.data.name);
		$("#sex").html(data.data.sex == 1 ? "男" : "女");
		
		$("#phone").html(data.data.phone);
		$("#birthDay").html(data.data.birthday);
		$("#performExperience").html(data.data.performExperience);
		$("#experienceResume").html(data.data.experienceResume);
		
		$("#interests").html(data.data.interests);
		$("#styleLabel").html(data.data.styleLabel);
		
		var html = [];
		if (data.data.liveAnchorImageList != null){
			for(var i=0; i<data.data.liveAnchorImageList.length; i++){
//				html.push("<img width='100px' height = '100px' src='"+imgRoot+data.data.liveAnchorImageList[i].anchorImage+"' />");
				html.push("<a href='"+imgRoot+data.data.liveAnchorImageList[i].anchorImage+"' rel='slide' class='fancybox' id ='picLink'> <img width='100px' height = '100px' src='"+imgRoot+data.data.liveAnchorImageList[i].anchorImage+"' /> </a>");
			    $("#liveAnchorImageUrl").html(html);
			}
		}
//		$("#viewAnchorRecruitModal").show();
		$('#viewAnchorRecruitModal').modal();
	
		// 点击关闭遮罩层
		$(".close-shade").on("click", function() {
			$(".shade-box,.shade-content").hide();
		});
  }
  
  function refuse(id){
     modalId = id;
     $("#refuseReason").val('');  //初始化
	 $('#refuseModal').modal();
  }
  
  /*审核拒绝通过*/
  $("#refuseSubmit").on("click",function(){
	    var status = 3;  //推荐状态 1 审核中 2 审核通过 3 审核被拒
		var refuseReason = $('#refuseReason').val();
		var url="liveAnchorRecruit/manage/approve/updateOptionState.jhtml";
		$.ajax({
			 url : url,
			 type : "post",
			 dataType : "json",
			 async: false,
			 data:{"anchorUid": modalId, "recommendStatus":status, "refuseReason":refuseReason},
			 success : function(data) {
				 if (data.success) {
					 $('#refuseModal').modal('hide');
					 setTimeout(function() {
						 anchorRecruitList.reload();
 					 }, 2000);
					 showSmReslutWindow(data.success,data.msg);
				 } else {
 					 window.messager.warning(result.msg);
 				 }
			 }
		});
	});
  
  function passed(id){
	    modalId = id;
		$('#passedModal').modal();
  }

  $("#passedSubmit").on("click",function(){
		var status = 2;  //推荐状态 1 审核中 2 审核通过 3 审核被拒
		var url="liveAnchorRecruit/manage/approve/updateOptionState.jhtml";
		$.ajax({
			 url : url,
			 type : "post",
			 dataType : "json",
			 async: false,
			 data:{"anchorUid": modalId, "recommendStatus":status},
			 success : function(data) {
				 if (data.success) {
					 $('#passedModal').modal('hide');
					 anchorRecruitList.reload();
				 } else {
 					 window.messager.warning(result.msg);
 				 }
				 showSmReslutWindow(data.success,data.msg);
			 }
		});
	});
  

  function liverPassed() {
	var uid = $("#uid").val();
	passed(uid);
  }

  function liverRefused() {
	var uid = $("#uid").val();
	refuse(uid);
 }
 