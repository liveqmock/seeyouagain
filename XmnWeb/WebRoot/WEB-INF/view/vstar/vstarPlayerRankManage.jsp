<%@ page language="java" import="java.util.*,java.net.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    <title>选手排名统计</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0"> 
	<link href="<%=path%>/resources/web/css/view.css" rel="stylesheet">   
	<link href="<%=path%>/resources/zui/css/zui.css" rel="stylesheet">   
	<link href="<%=path%>/resources/zui/lib/chosen/chosen.css" rel="stylesheet">
	<link href="<%=path%>/resources/zui/lib/datetimepicker/datetimepicker.min.css" rel="stylesheet"> 
	<style type="text/css">
		#enrollList1 table td{
			text-overflow:initial;
		}
	</style>
  </head>
  
  <body  style="overflow-x: auto;overflow-y: auto;min-width: 1024px;background:#EEE" class="doc-views with-navbar">
	<div id="prompt" style="position: fixed; height: 100%; width: 100%; z-index: 9; background: url(resources/page/loading.gif) #FFF no-repeat center; filter: alpha(opacity =  50); opacity: 0.5; display: none;"></div>
	
	<div class="tab-content">
		<!-- 全部star -->
		<div id="tab1" class="tab-pane in active">
			<div class="panel">
					<div class="panel-body">
						<form class="form-horizontal" role="form" id="searchForm1">
						<!-- 选手类型 0.海选选手(报名审核通过) 1.复赛选手(试播审核通过) 2.总决赛选手 -->
						<input type="hidden"  name="playerType" value="">
							<div class="form-group">
									<table style="width:100%;">
										<tbody>
											<tr>
												<td style="width:5%;"><h5>&nbsp;&nbsp;选手编号：</h5></td>
												<td style="width:25%;"><input type="text" class="form-control"  name="id" style = "width:85%;"></td>
												<td style="width:5%;"><h5>&nbsp;&nbsp;选手名称：</h5></td>
												<td style="width:25%;"><input type="text" class="form-control"  name="nname" style = "width:85%;"></td>
												<td style="width:5%;"><h5>手机号码：</h5></td>
												<td style="width:30%;"><input type="text" class="form-control"  name="phone" style = "width:66%;"></td>
											</tr>
											<tr>
												<td style="width:5%;"><h5>&nbsp;&nbsp;区域：</h5></td>
												<td style="width:25%;">
													<div class="input-group" id="ld1"  style="width:88%">
													</div>
												</td>
												<td style="width:5%;"><h5>&nbsp;&nbsp;赛区：</h5></td>
												<td style="width:25%;">
													<select class="form-control" id="divisionId" name="divisionId" style = "width:85%;">
													</select> 
												</td>
												<td style="width:5%;"><h5>统计人数:</h5></td>
												<td style="width:30%;">
													<select class="form-control"  name="limitSize" style = "width:66%;">	
														<option value="10">前10名</option>
														<option value="50">前50名</option>
														<option value="100">前100名</option>
														<option value="500">前500名</option>
													</select> 
												</td>
											</tr>
											<tr>
												<td style="width:5%;"><h5>&nbsp;&nbsp;排名条件:</h5></td>
												<td style="width:30%;">
													<select class="form-control"  name="orderParam" style = "width:85%;">	
														<option value="2">按鸟蛋降序</option>
														<option value="1">按粉丝降序</option>
													</select> 
												</td>
												<td style="width:5%;"><h5>&nbsp;&nbsp;统计时间:</td>
												<td style="width:25%;">
													<input type="text" id="startTime" name="startTime" value="${startTime }" placeholder="yyyy-MM-dd" 
														class="form-control form_datetime" style="width:40%;float:left" readonly="readonly">
													<label style="width:6%;float: left;">&nbsp;--&nbsp;</label>
													<input type="text" id="endTime" name="endTime" value="${endTime }" placeholder="yyyy-MM-dd" 
														class="form-control form_datetime" style="width:40%;float:left" readonly="readonly">
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
						<c:if test="${btnAu['VstarPlayerRank/manage/export']}">
								<button type="button" id="export" class="btn btn-default" >
								<span class="icon-download-alt"></span>&nbsp;导出</button>
						</c:if>
						<c:if test="${btnAu['VstarPlayerRank/manage/reward']}">
								<a type="button" class="btn btn-success"  href="javascript:void(0);" onclick="rewardSendBefore()">
									<span class="icon-plus"></span>&nbsp;发放奖励
								</a>
						</c:if>
					</div>
					<div id="enrollList1"></div>
				</div>
			</div>
		</div>
		<!-- 全部end -->
		
	</div>
	
	
	<script type="text/json" id="permissions">
		{update:'${ btnAu['VstarEnroll/manage/update']}'}
    </script>
	<jsp:include page="../common.jsp"></jsp:include>
	<script src="<%=path%>/resources/zui/lib/chosen/chosen.js"></script>
	<script src="<%=path%>/ux/js/ld2.js"></script>
	<script src="<%=path%>/ux/js/scrollTablel.js"></script>
	<script src="<%=path%>/resources/zui/lib/datetimepicker/datetimepicker.min.js"></script>
	<script src="<%=path%>/js/vstar/vstarPlayerRankManage.js?v=1.0.2"></script>
  </body>
</html>