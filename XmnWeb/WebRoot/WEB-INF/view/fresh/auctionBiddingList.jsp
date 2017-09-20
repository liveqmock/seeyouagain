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
<title>竞拍活动加价列表</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<link href="<%=path%>/resources/web/css/view.css" rel="stylesheet">
<link href="<%=path%>/resources/zui/css/zui.css" rel="stylesheet">
</head>

<body
	style="overflow-x: auto;overflow-y: auto;min-width: 1024px;background:#EEE"
	class="doc-views with-navbar">
	<div id="prompt"
		style="position: fixed; height: 100%; width: 100%; z-index: 9; background: url(resources/page/loading.gif) #FFF no-repeat center; filter: alpha(opacity =  50); opacity: 0.5; display: none;"></div>
	
			<form class="form-horizontal" role="form" id="searchForm">
				<input type="hidden" name="activityId" value="${ freshActivityAuction.id}">
			</form>
			
			<div class="panel panel-default">
				<div class="panel-body data">
					<div id="giftList"></div>
				</div>
			</div>
		
	<jsp:include page="../common.jsp"></jsp:include>
	<script src="<%=path%>/ux/js/scrollTablel.js"></script>
	<script src="<%=path%>/js/fresh/auctionBiddingList.js?v=1.0.4"></script>
</body>
</html>
