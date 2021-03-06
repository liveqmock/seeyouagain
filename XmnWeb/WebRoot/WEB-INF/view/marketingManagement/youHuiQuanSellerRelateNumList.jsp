<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html class="screen-desktop-wide device-desktop" lang="zh-CN">
<head>
<base href="<%=basePath%>">
<title>商家待审核列表</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<link href="<%=path%>/resources/zui/css/zui.css" rel="stylesheet">
<link href="<%=path%>/resources/web/css/view.css" rel="stylesheet"> 
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
</style>

</head>
<body
	style="overflow-x: hidden;overflow-y: auto;min-width: 1024px;background:#EEE"
	class="doc-views with-navbar">
	<div id="prompt"
		style="position: fixed; height: 100%; width: 100%; z-index: 9; background: url(resources/page/loading.gif) #FFF no-repeat center; filter: alpha(opacity =  50); opacity: 0.5; display: none;"></div>
	<input type="hidden" id="path" value="<%=path%>" />
	<div class="panel">
		<div class="panel-body">
			<form class="form-horizontal" role="form" id="sellerPendingFromId">
			<input type="hidden" id="aid" name="aid" value="${aid}"/>
			<!-- 0不是连锁店 -->
			<input type="hidden" name="ismultiple" value="0"/>
				<table style="width:100%;">
					<tbody>
						<tr>
						    <td style="width:5%;"><h5>商家编号:</h5></td>
							<td style="width:25%;">
								<input type="text" class="form-control"  name="sellerid" value="${param.sellerid}" style="width:80%">
							</td>
							<td style="width:5%;"><h5>商家名称:</h5></td>
							<td style="width:25% !important;">
								<input type="text" class="form-control"  name="sellername"  value="${param.sellername}" style="width:80%">
							</td>
							</td>
							<td style="width:5%;"></td>
							<td style="width:25% !important;">	
							</td>
							
						</tr>
						<tr>
						    <td style="width:5%;"><h5>参与状态：</h5></td>
							<td style="width:25%;">
									<select class="form-control" tabindex="2" name ="isattend" style = "width:80%;">
									    <option value = "">--请选择--</option>
						                <option value = "0" >已参与</option> 
						                <option value = "1" >已暂停</option>
						             </select>
						   </td>
						    <td style="width:5%;"><h5>商家手机号:</h5></td>
							<td style="width:25% !important;">
								<input type="text" class="form-control"  name="phoneid"  value="${param.phoneid}" style="width:80%">
							</td>
							<td colspan="2" style="width:30%;">
								<div class="submit submit-sp"  style="text-align: left;">
									<input class="submit radius-3" type="button"  value="查询全部"  data-bus = 'query' style="width: 39%;"/>
									<input  class="reset radius-3" type="reset" value="重置全部"   data-bus = 'reset' style="width: 39%;"/>
								</div>
							</td>
						</tr>
					</tbody>
				</table>
				<c:if test="${!empty param.page}">
					<input type="hidden" value="${param.page}" name="page"/>
				</c:if>
			</form>
		</div>
	</div>
	<div class="panel panel-default">
		<div class="panel-body data">
			<div id="sellerInPendingfoDiv" request-init="${requestInit}"></div>
		</div>
	</div>
	<script type="text/javascript">   contextPath = '${pageContext.request.contextPath}' </script>
	<script type="text/json" id="permissions">
      {update:'${btnAu['businessman/sellerPending/update'] }',view:'${btnAu['businessman/sellerPending/getInit'] }',agio:'${btnAu['businessman/sellerPendingAgio/init'] }'
      ,account:'${btnAu['businessman/sellerPendingAccount/init'] }',updateIsattend:'${btnAu['marketingManagement/activityManagement/youhuiquan/initSellerRelateNum/updateSellerMarketingIsattend'] }'
      ,exitActivity:'${btnAu['marketingManagement/activityManagement/youhuiquan/initSellerRelateNum/youHuiQuanExitActivity'] }'}
    </script>
	<jsp:include page="../common.jsp"></jsp:include>
	<script src="<%=path%>/resources/zui/lib/chosen/chosen.js"></script>
	<script src="<%=path%>/resources/zui/lib/datetimepicker/datetimepicker.min.js"></script>
	<script src="<%=path%>/js/marketingmanagement/youHuiQuanSellerRelateNumList.js"></script>
	<script type="text/javascript">
	$(document).ready(function() {
		var vali=
			{
				rules:{
					sellerid:{
						digits:true,
						range:[1,2147483647]
					}
				},
				messages:{
					sellerid:{
						digits:"商家编号只能是数字",
						range:"最大值为2147483647"
					}
				}
			};
		validate("sellerPendingFromId",vali);
	});
	</script>
</body>
</html>
