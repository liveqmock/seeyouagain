<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@page import="com.xmniao.xmn.core.util.FastfdsConstant"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>添加活动</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<link href="<%=path%>/resources/web/css/view.css" rel="stylesheet">
<link href="<%=path%>/resources/zui/css/zui.min-1.7.0.css"
	rel="stylesheet">
<link href="<%=path%>/resources/webuploader/webuploader.css"
	rel="stylesheet">
<link
	href="<%=path%>/resources/zui/lib/datetimepicker/datetimepicker.min.css"
	rel="stylesheet">
<link href="<%=path%>/resources/zui/lib/chosen/chosen.css"
	rel="stylesheet">
<link href="<%=path%>/resources/zui/lib/uploader/zui.uploader.min.css"
	rel="stylesheet">
<link href="<%=path%>/resources/upload/upload.css" rel="stylesheet">
<style type="text/css">
input::-webkit-outer-spin-button, input::-webkit-inner-spin-button {
	-webkit-appearance: none !important;
	margin: 0;
}

input[type="number"] {
	-moz-appearance: textfield;
}
</style>
<style type="text/css">
img.upload-btn{
	vertical-align: top;
}
.box span{
	display: inline-block;
    padding: 3px 10px;
    border: 1px solid;
    border-radius: 5px;
    position:relative;
    margin-right: 30px;
}
.box span em,#datas .img-list li em{
	position: absolute;
	right: -15px;
	top: 0;
	color: #c13535;
	cursor: pointer;
}
.img-list{
	display:inline-block;
}
#datas .img-list{
	font-size:0;
}
#datas .img-list li{
	position:relative;
	display:inline-block;
	margin-right:20px;
}
.img-list li img{
	width:100px;
	height:100px;
}
</style>
</head>
<body
	style="overflow-x: auto;overflow-y: auto;min-width: 1024px;background:#EEE"
	class="doc-views with-navbar">
	<input type="hidden" id="fastfdsHttp"
		value="<%=FastfdsConstant.FILE_UPLOAD_FASTDFS_HTTP%>" />
	<div id="prompt"
		style="position: fixed; height: 100%; width: 100%; z-index: 9; background: url(resources/page/loading.gif) #FFF no-repeat center; filter: alpha(opacity =  50); opacity: 0.5; display: none;"></div>
	<div id="main">
		<div class="panel panel-primary">
			<div class="panel-heading">添加远程教学</div>
			<div class="panel-body">
				<form id="editFrom" role="form"
					class="form-horizontal scrollbar-hover">
					<input type="hidden" value="${content.id}" name="id"
						id="fashionTicketId" />
					<div class="form-group">
						<label class="col-md-3 control-label">教学标题：<span
							style="color:red;">*</span></label>
						<div class="col-md-7" style="width:25%;">
							<input type="text" class="form-control" id="title" name="title"
								value="${content.title}" style="width:60%;float:left">
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-3 control-label">教学分类：<span
							style="color:red;">*</span></label>
						<div class="col-md-7" style="width:25%;">
							<select class="form-control" id="vstarDictId" name="vstarDictId">
								<c:forEach items="${dicts}" var="dict">
									<option value="${dict.id}"
										${content.vstarDictId==dict.id?'selected="selected"':''}>${dict.name}</option>
								</c:forEach>
							</select>
						</div>
					</div>

					<div class="form-group">
						<label class="col-md-3 control-label">选择主播：<span
							style="color:red;">*</span></label>
						<div class="col-md-7">
							<select class="form-control" id="anchorId" name="anchorId"
								initValue="${content.anchorId}"></select>
						</div>
					</div>

					<div class="form-group">
						<label class="col-md-3 control-label">封面图片：<span
							style="color:red;">*</span></label>
						<div class="col-md-7">
							<input type="hidden" class="form-control" id="coverImg"
								name="coverImg" value="${content.coverImg}">
							<div id="coverImgImg"></div>
						</div>
					</div>

					<div class="form-group">
						<label class="col-md-3 control-label">预告视频：<span
							style="color:red;">*</span></label>
						<div class="col-md-7" style="width:25%;">
							<input type="text" class="form-control" id="foreshowUrl"
								name="foreshowUrl" value="${content.foreshowUrl}"
								style="width:60%;float:left">
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-3 control-label">回放视频：<span
							style="color:red;">*</span></label>
						<div class="col-md-7" style="width:25%;">
							<input type="text" class="form-control" id="playbackUrl"
								name="playbackUrl" value="${content.playbackUrl}"
								style="width:60%;float:left">
						</div>
					</div>

					<div class="form-group">
						<label class="col-md-3 control-label">直播时间：<span
							style="color:red;">*</span></label>
						<div class="col-md-7">
							<input type="text" class="form-control" style="width:49%;"
								id="beginTime" name="beginTime" value="${content.beginTimeStr}"
								readonly="readonly"> <span>--</span> <input type="text"
								class="form-control" style="width:49%;" id="endTime"
								name="endTime" value="${content.endTimeStr}" readonly="readonly">
						</div>
					</div>
					
					<div id="datas">
					<div class="form-group" >
								<label class="col-md-3 control-label">上传封面：<span
									style="color: red;">*</span></label>
								<!-- 从主播相册选择照片后，拼接字符串 -->
								<div style="position: relative; margin-top: 10px;">
									<img src="<%=path%>/resources/upload/add.png"
										class="upload-btn" id="zhiboCoverDiv" style="width: 100px; height: 100px;" />
									<ul class="img-list">
										<c:forEach items="${content.anchorImageList}" var="img" >
										<li><img src="<%=FastfdsConstant.FILE_UPLOAD_FASTDFS_HTTP %>${img}" value='${img}' /><em class='icon-remove delete-img'></em></li>
										</c:forEach>
									</ul>
								</div>

							</div>
					</div>
					
					<div class="form-group">
						<label class="col-md-3 control-label">教学内容：<span
							style="color:red;">*</span></label>
						<div class="col-md-7">
							<div class="block-content collapse in">
								<textarea id="content" class="ckeditor" name="content" width="">${content.contentText}</textarea>
							</div>
						</div>
					</div>

				
					<div class="form-group">
						<label class="col-md-3 control-label">教学附件：<span
							style="color:red;">*</span></label>
						<div class="col-md-7">
							<div id='uploaderExample2' name="multipartFile" class="uploader"
								data-url="<%=path%>/vstarLiverContent/fileUpload.jhtml">
								<div class="uploader-message text-center">
									<div class="content"></div>
									<button type="button" class="close">×</button>
								</div>
								<div class="uploader-files file-list file-list-lg"
									data-drag-placeholder="请拖拽文件到此处">
								</div>
								<div class="uploader-actions">
									<div class="uploader-status pull-right text-muted"></div>
									<button type="button" class="btn btn-link uploader-btn-browse">
										<i class="icon icon-plus"></i> 选择文件
									</button>
									<button type="button" class="btn btn-link uploader-btn-start">
										<i class="icon icon-cloud-upload"></i> 开始上传
									</button>
								</div>
							</div>
						</div>
					</div>

					<div class="form-group">
						<label class="col-md-3 control-label">是否收费：<span
							style="color:red;">*</span></label>
						<div class="col-md-7">
							<input type="checkbox" id="is_pay"
								${content.zbalance!=null&&content.amount!=null?'checked=checked':''}>
						</div>
					</div>
					<div
						${content.zbalance!=null&&content.amount!=null?'':'style="display:none;"'}
						id="pay_model">
						<div class="form-group">
							<label class="col-md-3 control-label"></label>
							<div class="col-md-7">
								 <input
									type="number" min="0.01" class="form-control" placeholder="鸟币"
									name="zbalance" value="${content.zbalance}" style="width:50%" /> 
								 <input
									type="number" min="0.01" class="form-control" placeholder="现金"
									name="amount" value="${content.amount}" style="width:50%" /> 
							</div>
						</div>
					</div>

					<div class="form-group">
						<label class="col-md-3 control-label">排序：<span
							style="color:red;">*</span></label>
						<div class="col-md-7">
							<input type="number" class="form-control" id="sortVal"
								name="sortVal" min="0" value="${content.sortVal}"
								style="width:41%;float:left">
						</div>
					</div>
					<div class="form-group">
						<div class="text-center" style="padding:10px 0 10px 0;">
							<button type="submit" class="btn btn-success" >
								<span class="icon-ok"></span> 保 存
							</button>
							&nbsp;&nbsp; <a class="btn btn-warning"
								href="vstarLiverContent/init.jhtml"><span
								class="icon-remove"></span>&nbsp;取消</a>
						</div>
					</div>
			</div>
			</form>
		</div>
	</div>
	</div>
	<span id="contentKey"></span>
	<jsp:include page="../../common.zui-1.7.jsp"></jsp:include>
	<script src="<%=path%>/resources/zui/lib/uploader/zui.uploader.js"></script>
	<script src="<%=path%>/ux/js/ld2.js"></script>
	<script src="<%=path%>/ux/js/scrollTablel.js"></script>
	<script src="<%=path%>/resources/upload/upload.js"></script>
	<script src="<%=path%>/resources/zui/lib/chosen/chosen.js"></script>
	<script src="<%=path%>/resources/webuploader/webuploader.js"></script>
	<script src="<%=path%>/resources/web/js/jquery.json-2.4.js"></script>
	<script src="<%=path%>/resources/ckeditor_4.5.6/ckeditor.js"></script>
	<script src="<%=path%>/resources/ckeditor_4.5.6/config.js"></script>
	<script src="<%=path%>/resources/ckeditor_4.5.6/adapters/jquery.js"></script>
	<script src="<%=path%>/resources/web/js/jquery.json-2.4.js"></script>
	<script src="<%=path%>/resources/zui/lib/datetimepicker/datetimepicker.min.js"></script>
	<script
		src="<%=path%>/js/vstar/vstarLiverContent/liverContentRangeEdit.js?v=1.0.4"></script>
	<script type="text/javascript">
		$(function() {
			if ('${content.id}') {
				$.post("vstarLiverContent/init/getContentEditMsg.jhtml", {
					"id" : '${content.id}'
				}, function(data, status) {
					//获取所有附件id和所有视频id
					var staticFiles = new Array();
					if (status == 'success') {
						$.each(data.attachment, function(i, item) {
							fileArr.push(item.id);
							var file={
									"id":item.id,
								"contentId":item.id,
								"type":item.fileType,
								"name":item.fileName,
								"lastModifiedDate":item.updateTime,
								"size":item.fileSize
							}
							staticFiles.push(file);
						});
						console.log(staticFiles);
						options.staticFiles=staticFiles;
						$('#uploaderExample2').uploader(options);
					}
	
				});
			}else{
				$('#uploaderExample2').uploader(options);
			}
		});
	</script>
</body>
</html>
