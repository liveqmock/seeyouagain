<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="com.xmniao.xmn.core.util.FastfdsConstant"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html class="screen-desktop-wide device-desktop" lang="zh-CN">
  <head>
    <base href="<%=basePath%>">
    <title>生鲜列表</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0"> 
	<link href="<%=path%>/resources/web/css/view.css" rel="stylesheet">   
	<link href="<%=path%>/resources/zui/css/zui.css" rel="stylesheet"> 
	<link href="<%=path%>/resources/zui/lib/chosen/chosen.css" rel="stylesheet">
	<link href="<%=path%>/resources/web/css/jquery.validate.css" rel="stylesheet">
	<link href="<%=path%>/resources/zui/lib/datetimepicker/datetimepicker.min.css" rel="stylesheet">
	<style>
	.submit{float: left;}
	.btn-add{
	    background: #FF5C5C;
    	width: 160px;
		margin-right: 20px;
		border: 1px solid #FF5C5C;
		line-height: 20px;
		text-align: center;
		font-size:16px;
	}
	</style>
  </head>
  
  <body  style="overflow-x: hidden;overflow-y: auto;min-width: 1024px;background:#EEE" class="doc-views with-navbar">
  	<div id="prompt" style="position: fixed; height: 100%; width: 100%; z-index: 9; background: url(resources/page/loading.gif) #FFF no-repeat center; filter: alpha(opacity =  50); opacity: 0.5; display: none;"></div>
  	<input type="hidden" id="path" value="<%=path%>" />
  	<input type="hidden" id="checkbox" value="${btnAu['user_terminal/banner/delete']}" />
  	<input type="hidden" id="picSerUrl" value="<%=FastfdsConstant.FILE_UPLOAD_FASTDFS_HTTP%>" />
  	<div class="panel">
		<div class="panel-body">
			<form class="form-horizontal" role="form" method="get" id="searchForm">
				<table style="width:100%;">
					<tbody>
						<tr>
							<td style="width:5%;"><h5>导航图编号:</h5></td>
							<td style="width:18%;"><input type="text" class="form-control" id="id" name="id" style = "width:90%;"/></td>
							<td style="width:5%;"><h5>导航图位置:</h5></td>
							<td style="width:18%;">
							<select class="form-control" id="position" name="position" style = "width:90%;">
									<option value="">请选择</option>
									<option value="1">发现美食</option>
									<option value="2">积分商城</option>
									<option value="3">寻蜜客主页</option>
									<option value="4">直播首页</option>
									<option value="100">商家经营页</option>
									<option value="5">积分超市竞拍活动</option>
									<option value="6">积分超市夺宝活动</option>
									<option value="7">首页活动</option>
									<option value="8">充值界面</option>
									<option value="9">我的首页</option>
									<option value="10">脉宝云店</option>
									<option value="11">直播结束广告</option>
							</select>
							</td>
							<td style="width:5%;"><h5>展示风格:</h5></td>
							<td style="width:18%;">
							<select class="form-control" id="bannerStyle" name="bannerStyle" style = "width:90%;">
									<option value="">请选择</option>
									<option value="0">图片横排一格</option>
									<option value="1">图片横排两格</option>
									<option value="3">轮播图</option>
							</select>
							</td>
							<td style="width:5%;"><h5>上线状态:</h5></td>
							<td style="width:18%;">
								<select class="form-control" id="status" name="status" style = "width:90%;">
									<option value="">请选择</option>
									<option value="0">待上线</option>
									<option value="1">已上线</option>
									<option value="2">已下线</option>
								</select>
							</td>
						</tr>
						<tr>
							<td style="width:5%;"><h5>导航图描述:</h5></td>
							<td style="width:18%;"><input type="text" class="form-control" id="remarks" name="remarks" style = "width:90%;" placeholder=""/></td>
						  	<td style="width:5%;"><h5>投放时间:</h5></td>
						 	<td style="width:18%;">
							  <input type="text" class="form-control"  id="sdate" name="sdate" value="" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" style="width:42%;float:left"/>
							    <label style="float: left;">&nbsp;--&nbsp;</label>
							  <input type="text" class="form-control"  id="edate" name="edate" value="" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" style="width:42%;float:left"/>
							</td>	
							<td style="width:5%;"></td>
						 	<td style="width:18%;">
							</td>
							<td style="width:5%;"></td>
							<td style="width:18%;">
							</td>
						</tr>
						<tr>
							<td style="width:5%;"></td>
							<td style="width:18%;"></td>
						  	<td style="width:5%;"></td>
						 	<td style="width:18%;"></td>
						 	<td style="width:5%;"></td>
						 	<td style="width:18%;"></td>
							<td colspan="2" style="width:29%;">
								<div class="submit" style="text-align: left;width:100%;">
									<input class="submit radius-3" type="button"  value="查询全部"  data-bus = 'query' style="width:43%;flaot:left;margin:0 2%;"/>
									<input  class="reset radius-3" type="reset" value="重置全部"   data-bus = 'reset' style="width:43%;flaot:left;margin:0 2%;"/>
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
			</div>
			<div class="btn-group" style="margin-bottom: 5px;">
				<!-- 添加导航图按钮 -->
				<c:if test="${btnAu['user_terminal/banner/add']}">
					<a type="button" id="addBannerBto" class="btn btn-success"
						href="user_terminal/banner/add/init.jhtml"><span
						class="icon-plus"></span>&nbsp;添加</a>
				</c:if>
				<c:if test="${btnAu['user_terminal/banner/delete']}"><button type="button" class="btn btn-danger" id="delete"><span class="icon-remove"></span>&nbsp;删除</button>
				</c:if>
			</div>
			<div id="bannerList"></div>
		</div>
	</div>
     <!-- 操作结果提示层 -->
	<div class="modal fade" id="sm_reslut_window" data-position="100px">
		<div class="modal-dialog modal-sm">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span><span class="sr-only">关闭</span>
					</button>
					<h4 class="modal-title"></h4>
				</div>
				<div class="modal-body"></div>
			</div>
		</div>
	</div>
	<script type="text/json" id="permissions">{update:'${btnAu['user_terminal/banner/update']}',check:'${btnAu['user_terminal/banner/view']}'}</script>
	<jsp:include page="../../common.jsp"></jsp:include>
	<script src="<%=path%>/resources/zui/lib/chosen/chosen.js"></script>
	<script src="<%=path%>/js/user_terminal/banner/bannerlist.js"></script>
	<script src="<%=path%>/resources/zui/lib/datetimepicker/datetimepicker.min.js"></script>
	<script src="<%=path%>/ux/js/scrollTablel.js"></script>
	<!-- 引入时间插件 -->
   <script src="<%=path%>/resources/datapicker/WdatePicker.js"	type="text/javascript"></script>
  </body>
</html>
