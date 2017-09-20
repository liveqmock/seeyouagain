var anchorList;
var initListUrl = "recommendRewardProfit/manage/init/list.jhtml";
$(function() {
	inserTitle(
			' > 黄金庄园 > <a href="recommendRewardProfit/manage/init.jhtml" target="right">推荐奖励收益</a>',
			'userSpan', true);
	anchorList = $("#anchorList").page({
		url : initListUrl,
		success : success,
		pageBtnNum : 10,
		paramForm : 'searchForm',
		param : {
			getWay : "8"
		}
	});
	
	//导出
	$("#exportAnchor").click(function(){
		/*var size=getCurrentDataSize();
		if(size>1000){
			showWarningWindow("warning", "单次最多可导出1000条数据，请输入查询条件！",9999);
			return ;
		}*/
		
		var path="recommendRewardProfit/manage/export.jhtml";
		$form = $("#searchForm").attr("action",path);
		$form[0].submit();
	});	

});

function success(data, obj) {
	var formId = "searchForm";
	var title = "会员列表 ", subtitle = "个会员";
	var captionInfo = '<caption style="background:#EED8D8; text-align:left; color:#703636; font-size:16px; line-height:40px;">&nbsp;&nbsp;'
			+ title+ '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font>共计【'+ data.total+ '】' + subtitle + '&nbsp;</font></caption>';
	var callbackParam = "isBackButton=true&callbackParam="+ getFormParam($("#" + formId).serialize());

	obj.find('div').eq(0).scrollTablel({
		identifier : "id",
		callbackParam : callbackParam,
		data : data.content,
		caption : captionInfo,
		// checkable : checkable,
		cols : [ {
			title : "会员编号",
			name : "uid",
			type : "string",
			width : 100
		},{
			title : "会员昵称",
			name : "nname",
			type : "string",
			width : 120
		}, {
			title : "手机号码",
			name : "phone",
			type : "string",
			width : 120
		},{
			title : "获得优惠劵",
			name : "cname",
			type : "string",
			width : 100
		}, {
			title : "来源用户",
			name : "sourceName",
			type : "string",
			width : 100,
			customMethod:function(value,data){
				var value = (data.sourceName == undefined ? "" : data.sourceName) + ( data.sourcePhone == null || data.sourcePhone == undefined ? "" : " (" + data.sourcePhone +")" );
				return value;
			}
		}, {
			title : "获得时间",
			name : "getTime",
			type : "string",
			width : 80
		} ]
	}, permissions);
}

 
 /**
  * 获取当前查询记录数
  */
 /*function getCurrentDataSize(){
	 var formId = "searchForm";
	 var total=0;
	// 设置同步
    $.ajaxSetup({
        async: false
    });
	 
	 $.ajax({
		 url : "sunshineProfit/manage/init/getCurrentDataSize.jhtml",
		 type : "post",
		 dataType : "json",
		 data:jsonFromt($('#' + formId).serializeArray()),
		 success : function(result) {
			 total=result;
		 }
	 });
	 
	// 恢复异步
    $.ajaxSetup({
        async: true
    });
    
    return total;
 }*/
 

