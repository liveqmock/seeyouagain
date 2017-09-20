<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="zh-CN">
<head>
<base href="<%=basePath%>">
<title>系统公告</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<link href="<%=path%>/resources/web/css/view.css" rel="stylesheet">
<link href="<%=path%>/resources/zui/css/zui.css" rel="stylesheet">
<link href="<%=path%>/resources/zui/lib/datetimepicker/datetimepicker.min.css" rel="stylesheet"> 
</head>

<body
	style="overflow-x: auto;overflow-y: auto;min-width: 1024px;background:#EEE"
	class="doc-views with-navbar">
	<div id="prompt"
		style="position: fixed; height: 100%; width: 100%; z-index: 9; background: url(resources/page/loading.gif) #FFF no-repeat center; filter: alpha(opacity =  50); opacity: 0.5; display: none;"></div>
	<div class="panel">
		<div class="panel-body">
			<form class="form-horizontal" role="form" id="searchForm">
				<div class="form-group">
					<label class="col-md-1 control-label" >用户端类型：</label>
					<div class="col-md-3" style="width: 15%;">
						<select class="form-control" name="app">
							<option value="">--请选择--</option>
							<option value="1">商户端</option>
							<option value="2">用户端</option>
						</select>
					</div>
					<div class="submit">
						<input class="submit radius-3" type="button" value="查询全部"
							data-bus='query' /> <input type="reset" class="reset radius-3"
							value="重置全部" data-bus='reset' />
					</div>
				</div>

				<c:if test="${!empty param.page}">
					<input type="hidden" value="${param.page}" name="page" />
				</c:if>
			</form>
			
		</div>
	</div>
	
	<div class="panel panel-default">
		<div class="panel-body data">
			<div class="btn-group" style="margin-bottom: 5px;">
				<!-- 2016-12-17 -->
				<c:if test="${null!=btnAu['announcement/manage/add'] && btnAu['announcement/manage/add']}">
					<button type="button" class="btn btn-success" data-type="ajax"
						data-title="添加公告" data-url="announcement/manage/add/init.jhtml"
						data-toggle="modal" data-width="auto">
						<span class="icon-plus"></span>&nbsp;添加
					</button>
				</c:if>
				<c:if test="${ btnAu['announcement/manage/delete']}">
					<button type="button" class="btn btn-danger" id="delete">
						<span class="icon-remove"></span>&nbsp;删除
					</button>
				</c:if>
				<%-- <c:if test="${!empty btnAu['announcement/manage/update'] && btnAu['announcement/manage/update']}">
						<button type="button" class="btn btn-info" id="putaway" ><span class="icon-hand-up"></span>&nbsp;启用</button>
				</c:if>
				<c:if test="${!empty btnAu['announcement/manage/update'] && btnAu['announcement/manage/update']}">
					<button type="button" class="btn btn-warning" id="removeOffshelf" ><span class="icon-hand-down"></span>&nbsp;冻结</button>
				</c:if> --%>
			</div>
			<div id="recordList"></div>
		</div>
	</div>
	<script type="text/json" id="permissions">{
	  add:'${ btnAu['announcement/manage/add']}',
	  update:'${ btnAu['announcement/manage/update']}',
	  delete:'${ btnAu['announcement/manage/delete']}',
	}</script>
	<jsp:include page="../common.jsp"></jsp:include>
	<script src="<%=path%>/resources/zui/lib/datetimepicker/datetimepicker.min.js"></script>
	<script src="<%=path%>/ux/js/scrollTablel.js"></script>
	<script src="<%=path%>/js/dataDictionary/announcementManage.js"></script>
</body>
</html>
