<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html class="screen-desktop-wide device-desktop" lang="zh-CN">
<head>
<base href="<%=basePath%>">
<title>V客充值奖励管理列表</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">

<link href="<%=path%>/resources/web/css/view.css" rel="stylesheet">   
<link href="<%=path%>/resources/zui/css/zui.css" rel="stylesheet">
<link href="<%=path%>/resources/zui/lib/chosen/chosen.css" rel="stylesheet">
<link href="<%=path%>/resources/zui/lib/datetimepicker/datetimepicker.min.css" rel="stylesheet">
<style type="text/css">
body {
	margin: 0;
	font-size: 12px;
	font-family: 'Microsoft Yahei', '微软雅黑';
	overflow-y: auto;
	padding: 10px 30px;
}

.data table.info tr td {
	text-align: center;
	font-size: 14px
}

.data table,info {
	word-break: break-all;
	word-wrap: break-word;
}

.data table.info tr th {
	text-align: center;
	font-size: 14px;
	line-height: 15px;
}

.data table.info tr td a {
	color: #CC3333;
}

.data table.info tr td a:hover {
	color: #CC3333;
	text-decoration: underline;
}
.submit{text-align: left;}
</style>

</head>
<body
	style="overflow-x: auto;overflow-y: auto;min-width: 1024px;background:#EEE"
	class="doc-views with-navbar">
	<div id="prompt" style="position: fixed; height: 100%; width: 100%; z-index: 9; background: url(resources/page/loading.gif) #FFF no-repeat center; filter: alpha(opacity =  50); opacity: 0.5; display: none;"></div>
	<input type="hidden" id="path" value="<%=path%>" />
	<div class="panel">
		<div class="panel-body">
			<form class="form-horizontal" role="form" id="searchFromId">
				<table style="width: 100%;">
					<tbody>
						<tr>
							<td style="width: 6%;"><h5>&nbsp;&nbsp;用户编号:</h5></td>
							<td style="width: 10% !important;"><input type="text"
								class="form-control" value="" name="uid" style="width: 70%"></td>
								
							<td style="width: 6%;"><h5>&nbsp;&nbsp;手机号码:</h5></td>
							<td style="width: 10% !important;"><input type="text"
								class="form-control" value="" name="phone" style="width: 70%"></td>
								
							<td style="width: 6%;"><h5>&nbsp;&nbsp;用户昵称:</h5></td>
							<td style="width: 15% !important;"><input type="text"
								class="form-control" value="" name="nickname" style="width: 80%"></td>
										
							<!--<td style="width: 6%;"><h5>&nbsp;&nbsp;返还时间:</h5></td>
							<td style="width: 20% !important;">
							    <input type="text" class="form-control form-datetime" name ="useStartTime" placeholder="yyyy-MM-dd hh:mm" style="width:40%;float:left"  readonly="readonly">
								<label style="float: left;">&nbsp;&nbsp;至&nbsp;&nbsp;</label>
								<input type="text" class="form-control form-datetime" name ="useEndTime"  placeholder="yyyy-MM-dd hh:mm" style="width:40%;float:left"  readonly="readonly">	</td> -->										
						</tr>
						<tr>
							<td style="width: 6%;"><h5>&nbsp;&nbsp;会员等级：</h5></td>
							<td style="width: 20%;"><select class="form-control" name="ledgerLevel" id="ledgerLevel" style="width: 60%;"> 
							</select></td>
							<td style="width: 6%;"><h5>&nbsp;&nbsp;返还模式：</h5></td>
							<td style="width: 20%;"><select class="form-control"
								name="projectName" style="width: 70%;">
									<option value="">--请选择--</option>
									<option value="A">A模式</option>
									<option value="B">B模式</option>
							</select></td>							
							<td colspan="2" style="text-align: right;">
								<div class="submit">
									<input class="submit radius-3" type="button" value="查询全部" data-bus='query' style="width: 43%;" /> 
									<input class="reset radius-3" type="reset" value="重置全部" data-bus='reset' style="width: 43%;"/>
								</div>
							</td>
						</tr>

					</tbody>
				</table>
			</form>
		</div>
	</div>
	<div class="panel panel-default">
		<div class="panel-body data">
			<div class="btn-group" style="margin-bottom: 5px;">
				<!-- -->
				<c:if test="${null!=btnAu['rechargeReward/manage/edit'] && btnAu['rechargeReward/manage/edit']}">
					<a type="button" class="btn btn-primary" id="addRechargeRewardBto"
						href="rechargeReward/manage/add/init.jhtml"><span
						class="icon-plus"></span>&nbsp;返还配置</a>
				</c:if>
			</div>
			<div class="btn-group" style="margin-bottom: 5px;"></div>
			<div id="rechargeRewardInfoList"></div>
		</div>
	</div>
	
	<!-- modal 窗口部分开始 -->
	<div class="modal fade" id="rewardRecordListModal">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span><span class="sr-only">关闭</span>
					</button>
					<h4 class="modal-title">返还记录</h4>
				</div>
				<!-- style="height: 220px;" -->
				<div class="modal-body example" >
					<table class="table table-bordered" class="propertyTable">
						<thead>
							<tr class="text-center">
								<th class="text-center"><div class="header">返还日期</div></th>
								<th class="text-center"><div class="header">领取时间</div></th>
								<th class="text-center"><div class="header">返还状态</div></th>
							</tr>
						</thead>
						<tbody id="recordListInfoTB">
						</tbody>
					</table>
				</div>

			</div>
		</div>
	</div>
	
	
	<script type="text/json" id="permissions">
		{xg:'${ btnAu['liveLevel/manage/update'] }'}
    </script>
    
    
	<script type="text/javascript">   contextPath = '${pageContext.request.contextPath}' </script>
	<jsp:include page="../common.jsp"></jsp:include>
	<script src="<%=path%>/resources/zui/lib/chosen/chosen.js"></script>
	<script src="<%=path%>/resources/zui/lib/datetimepicker/datetimepicker.min.js"></script>
	<script src="<%=path%>/resources/page/page.js"></script>
	<script src="<%=path%>/ux/js/ld2.js"></script>
	
	<script src="<%=path%>/ux/js/scrollTablel.js"></script>
	<script src="<%=path%>/js/reward_dividends/rechargeRewardManage.js?v=1.0.5"></script>
	
</body>
</html>
