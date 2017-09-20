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
<title>V客收益统计</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<link href="<%=path%>/resources/web/css/view.css" rel="stylesheet">   
<link href="<%=path%>/resources/zui/css/zui.css" rel="stylesheet">
<link href="<%=path%>/resources/zui/lib/chosen/chosen.css" rel="stylesheet">
<link href="<%=path%>/resources/zui/lib/datetimepicker/datetimepicker.min.css" rel="stylesheet">
<link  href="<%=path%>/css/cloud_design/goodPage.css" rel="stylesheet"/> 

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
	<ul id="myTab" class="nav nav-tabs">
		<li class="active"><a href="#tab1" data-toggle="tab">签约商户收益统计</a></li>
		<li class=""><a href="#tab2" data-toggle="tab">推荐主播收益统计</a></li>
	</ul>
	<div class="tab-content">
		<div id="tab1" class="tab-pane in active">
			<div class="panel">
				<div class="panel-body">
					<form class="form-horizontal" role="form" id="searchFormBill">
					    <input type="hidden" name="type" value="1" />
						<div class="form-group">
							<table style="width:100%;">
								<tbody>
									<tr>
									    <td style="width:7%;"><h5>&nbsp;&nbsp;用户编号：</h5></td>
										<td style="width:18%;">
											<input type="text" class="form-control" name="vkeUid" value="" style = "width:65%;"/>
										</td>
										<td style="width:7%;"><h5>&nbsp;&nbsp;会员昵称：</h5></td>
										<td style="width:18%;">
											<input type="text" class="form-control" name="vkeNname" value="" style = "width:65%;"/>
										</td>
										<td style="width:7%;"><h5>&nbsp;&nbsp;会员手机号：</h5></td>
										<td style="width:18%;">
											<input type="text" class="form-control" name="vkePhone" value="" style = "width:68%;"/>
										</td>
									</tr>
									<tr>
										<td style="width: 6%;"><h5>&nbsp;&nbsp;统计时间：</h5></td>
				                        <td style="width:30% ;">
											<input type="text" name ="sdateStart" value="${param.saleStartTime}" placeholder="yyyy-MM-dd hh:mm" class="form-control form-datetime"style="width:32%;float:left" >
									        <label style="float: left;">&nbsp;&nbsp;-&nbsp;&nbsp;</label>
									        <input type="text" name ="sdateEnd" value="${param.sdateEnd}" placeholder="yyyy-MM-dd hh:mm" class="form-control form-datetime"style="width:32%;float:left">
										</td>
										<td></td><td></td>	
									    <td colspan="2" style="width:29%;">
											<div class="submit" style="text-align: left;">
												<input class="submit radius-3" type="button"  value="查询全部"  data-bus = 'query' style="width:43%;flaot:left;margin:0 2%;"/>
												<input  class="reset radius-3" type="reset" value="重置全部"   data-bus = 'reset' style="width:43%;flaot:left;margin:0 2%;"/>
											</div>
									    </td>
						            </tr>		
						            							
								</tbody>
							</table>
						</div>
					</form>
				</div>
			</div>
			
			<!-- 累计充值总额 begin -->
			<div class="panel panel-default">
				<div class="panel-body data">
					<table class="table table-hover table-bordered table-striped info" >
						<%-- <caption style="background:#EED8D8; text-align:center; color:#703636; font-size:16px; line-height:40px;">&nbsp;&nbsp;统计总数</caption> --%>
						<thead>
				 			<tr> 
							 <th style="width:130px;">统计时间</th> 
							 <th style="width:130px;">统计V客</th> 
							 <th style="width:130px;">统计商家</th> 
							 <th style="width:130px;">累计收益</th> 
				 			</tr> 
				 		</thead> 
				 		<tbody id="vkeProfitSellerTotal">
					    </tbody>
					</table> 
				</div>
			</div>
			
			<div class="panel panel-default">
			    <div class="panel-body data">
			    	<div class="btn-group" style="margin-bottom: 5px;">
						<c:if test="${btnAu['verProfitCount/manage/export']}">
								<button type="button" id="exportBill" class="btn btn-default" >
								<span class="icon-download-alt"></span>&nbsp;导出</button>
						</c:if>
					</div>
					<div id="billList" class="good-table-wrap"></div>
				</div>
	        </div>
		</div>
        <!--  tab2 -->
		<div id="tab2" class="tab-pane">
		    <div class="panel">
				<div class="panel-body">
					<form class="form-horizontal" role="form" id="searchFormGift">
					    <input type="hidden" name="type" value="2" />
						<div class="form-group">
							<table style="width:100%;">
								<tbody>
									<tr>
									    <td style="width:7%;"><h5>&nbsp;&nbsp;用户编号：</h5></td>
										<td style="width:18%;">
											<input type="text" class="form-control" name="vkeUid" value="" style = "width:65%;"/>
										</td>									
										<td style="width:7%;"><h5>&nbsp;&nbsp;会员昵称：</h5></td>
										<td style="width:18%;">
											<input type="text" class="form-control" name="vkeNname" value="" style = "width:65%;"/>
										</td>
										<td style="width:7%;"><h5>&nbsp;&nbsp;会员手机号：</h5></td>
										<td style="width:18%;">
											<input type="text" class="form-control" name="vkePhone" value="" style = "width:68%;"/>
										</td>
									</tr>
									<tr>
										<td style="width: 6%;"><h5>&nbsp;&nbsp;统计时间：</h5></td>
				                        <td style="width:30% ;">
											<input type="text" name ="sdateStart" value="${param.sdateStart}" placeholder="yyyy-MM-dd hh:mm" class="form-control form-datetime"style="width:32%;float:left" >
									        <label style="float: left;">&nbsp;&nbsp;-&nbsp;&nbsp;</label>
									        <input type="text" name ="sdateEnd" value="${param.sdateEnd}" placeholder="yyyy-MM-dd hh:mm" class="form-control form-datetime"style="width:32%;float:left">
										</td>
										<td></td><td></td>	
									    <td colspan="2" style="width:29%;">
											<div class="submit" style="text-align: left;">
												<input class="submit radius-3" type="button"  value="查询全部"  data-bus = 'query' style="width:43%;flaot:left;margin:0 2%;"/>
												<input  class="reset radius-3" type="reset" value="重置全部"   data-bus = 'reset' style="width:43%;flaot:left;margin:0 2%;"/>
											</div>
									    </td>
						            </tr>		
								</tbody>
							</table>
						</div>
					</form>
				</div>
			</div>
			<div class="panel panel-default">
				<div class="panel-body data">
					<table class="table table-hover table-bordered table-striped info" >
						<%-- <caption style="background:#EED8D8; text-align:center; color:#703636; font-size:16px; line-height:40px;">&nbsp;&nbsp;统计总数</caption> --%>
						<thead>
				 			<tr> 
							 <th style="width:130px;">统计时间</th> 
							 <th style="width:130px;">统计V客</th> 
							 <th style="width:130px;">统计收益</th> 
							 <th style="width:130px;">累计收益</th> 
				 			</tr> 
				 		</thead> 
				 		<tbody id="vkeProfitGiftTotal">
					    </tbody>
					</table> 
				</div>
			</div>
			
			<div class="panel panel-default">
			    <div class="panel-body data">
			    	<div class="btn-group" style="margin-bottom: 5px;">
						<c:if test="${btnAu['verProfitCount/manage/export']}">
								<button type="button" id="exportLive" class="btn btn-default" >
								<span class="icon-download-alt"></span>&nbsp;导出</button>
						</c:if>
					</div>
					<div id="giftList" class="good-table-wrap"></div>
				</div>
	        </div>
		</div>
	</div>

	<script type="text/json" id="permissions">{
		export:'${ btnAu['verProfitCount/manage/export']}'  }
	</script>
	
	<jsp:include page="../common.jsp"></jsp:include>
	<script src="<%=path%>/ux/js/ld2.js"></script>
	<script src="<%=path%>/ux/js/scrollTablel.js"></script>
	<script src="<%=path%>/resources/zui/lib/datetimepicker/datetimepicker.min.js"></script>

	<script src="<%=path%>/js/reward_dividends/verProfitCountManage.js?v=1.0.5"></script>
</body>
</html>
