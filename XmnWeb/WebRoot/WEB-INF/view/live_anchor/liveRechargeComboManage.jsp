<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="zh-CN">
<head>
<base href="<%=basePath%>">
<title>银行</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<link href="<%=path%>/resources/web/css/view.css" rel="stylesheet">
<link href="<%=path%>/resources/zui/css/zui.css" rel="stylesheet">
<link href="<%=path%>/resources/zui/lib/chosen/chosen.css"
	rel="stylesheet">
</head>

<body
	style="overflow-x: auto;overflow-y: auto;min-width: 1024px;background:#EEE"
	class="doc-views with-navbar">
	<div id="prompt"
		style="position: fixed; height: 100%; width: 100%; z-index: 9; background: url(resources/page/loading.gif) #FFF no-repeat center; filter: alpha(opacity =  50); opacity: 0.5; display: none;"></div>
	<div class="panel">
		<div class="panel-body">
			<input type="hidden" id="checkable"
				value="${ btnAu['liveRechargeCombo/manage/delete']}">
			<form class="form-horizontal" role="form" id="searchForm">
					<table style="width:100%;">
						<tbody>
						<tr>
							<td style="width:5%;"><h5>&nbsp;&nbsp;充值类型：</h5></td>
							<td style="width:25%;">
								<select class="form-control" name="rechargeType"
										style="width:85%;">
									<option value="">全部</option>
									<option value="1">苹果</option>
									<option value="2">安卓/微信</option>
									<option value="3">PC充值（鸟人VIP）</option>
									<option value="4">PC充值（鸟人GD）</option>
								</select>
							</td>
							
							<td style="width:5%;"><h5>&nbsp;&nbsp;APP类型：</h5></td>
							<td style="width:25%;">
								<select class="form-control"   name="comboSource" style="width: 80%">
									<option value="">--请选择--</option>
									<option value="0">寻蜜鸟</option>
									<option value="1">鸟人科技</option>
								</select>
							</td>
							
							
							<td style="width:5%;"><h5>&nbsp;&nbsp;绑定类型：</h5></td>
							<td style="width:30%;">
								<select class="form-control chosen-select"   multiple style="width: 66%">
									<option value="0">一般</option>
									<option value="1">VIP</option>
									<option value="2">商家</option>
									<option value="3">直销</option>
									<option value="4">营业厅</option>
									<option value="5">黄金庄园</option>
								</select>
								<input type="input" name="objectOriented" id="oriented"  style="display:none;"/>
							</td>
						</tr>

						<tr>
							<td style="width:5%;"><h5>&nbsp;&nbsp;有效状态：</h5></td>
							<td style="width:25%;">
								<select class="form-control"   name="status" style="width: 85%">
									<option value="">--请选择--</option>
									<option value="1">有效</option>
									<option value="2">无效</option>
								</select>
							</td>
							<td style="width:5%;"><h5></h5></td>
							<td style="width:25%;">
							</td>
							<td colspan="2" style="width:35%;">
								<div class="submit" style="text-align: left;">
									<input class="submit radius-3" type="button"  value="查询全部"  data-bus = 'query' />
									<input  class="reset radius-3" type="reset" value="重置全部"   data-bus = 'reset' />
								</div>
							</td>
						</tr>
						</tbody>
					</table>

				<c:if test="${!empty param.page}">
					<input type="hidden" value="${param.page}" name="page" />
				</c:if>
			</form>
		</div>
	</div>
	<div class="panel panel-default">
		<div class="panel-body data">
			<div class="btn-group" style="margin-bottom: 5px;">
				<c:if
					test="${null!=btnAu['liveRechargeCombo/manage/add/init'] && btnAu['liveRechargeCombo/manage/add/init']}">
					<button type="button" class="btn btn-success" data-type="ajax"
						data-title="添加充值金额"
						data-url="liveRechargeCombo/manage/add/init.jhtml"
						data-toggle="modal" data-width="auto">
						<span class="icon-plus"></span>&nbsp;添加
					</button>
				</c:if>
				<c:if test="${ btnAu['liveRechargeCombo/manage/delete']}">
					<button type="button" class="btn btn-danger" id="delete">
						<span class="icon-remove"></span>&nbsp;删除
					</button>
				</c:if>
			</div>
			<div id="comboList"></div>
		</div>
	</div>

	<script type="text/json" id="permissions">{
			update:'${ btnAu['liveRechargeCombo/manage/update/init']}',
			delete:'${ btnAu['liveRechargeCombo/manage/delete']}'
		}
	</script>
	<jsp:include page="../common.jsp"></jsp:include>
	<script src="<%=path%>/ux/js/scrollTablel.js"></script>
	<script src="<%=path%>/resources/zui/lib/chosen/chosen.js"></script>
	<script
		src="<%=path%>/js/live_anchor/liveRechargeComboManage.js?v=1.0.4"></script>
</body>
</html>
