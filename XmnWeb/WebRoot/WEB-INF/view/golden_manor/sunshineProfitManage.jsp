<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
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
<title>阳光收益管理</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<link href="<%=path%>/resources/web/css/view.css" rel="stylesheet">
<link href="<%=path%>/resources/zui/css/zui.css" rel="stylesheet">
<link  href="<%=path%>/css/cloud_design/goodPage.css" rel="stylesheet"/> 
</head>

<body
	style="overflow-x: auto;overflow-y: auto;min-width: 1024px;background:#EEE"
	class="doc-views with-navbar">
	<div id="prompt"
		style="position: fixed; height: 100%; width: 100%; z-index: 9; background: url(resources/page/loading.gif) #FFF no-repeat center; filter: alpha(opacity =  50); opacity: 0.5; display: none;"></div>
	<div class="panel">
		<div class="panel-body">
			<form class="form-horizontal" role="form" id="searchForm">
				<input type="hidden" id="uidViewJunior" name="uidViewJunior" value="${uidViewJunior}">
				<div class="form-group">
					<table style="width:100%;">
						<tbody>
							<tr>
								<td style="width:7%;"><h5>&nbsp;&nbsp;会员昵称：</h5></td>
								<td style="width:18%;">
									<input type="text" class="form-control" name="nickname" value="" style = "width:85%;"/>
								</td>
								<td style="width:7%;"><h5>&nbsp;&nbsp;会员手机号：</h5></td>
								<td style="width:18%;">
									<input type="text" class="form-control" name="phone" value="" style = "width:58%;"/>
								</td>
								<td style="width:7%;"><h5>类型：</h5></td>
								<td style="width:18%;">
										<select class="form-control" tabindex="2" name ="channel"  style = "width:75%;">
										    <option value = "">--全部--</option>
							                <option value = "17" ${param.status==1?"selected":""}>推荐奖励</option>
							                <option value = "10" ${param.status==2?"selected":""}>园友赠送</option>
							                <option value = "15" ${param.status==3?"selected":""}>仓库存储收益</option>
							             </select>
							   </td>
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

				<c:if test="${!empty param.page}">
					<input type="hidden" value="${param.page}" name="page" />
				</c:if>
			</form>
		</div>
	</div>
	<div class="panel panel-default">
		<div class="panel-body data">
			<div class="btn-group" style="margin-bottom: 5px;">
				<c:if test="${btnAu['liveMember/manage/export']}">
                      <a type="button" style="float: left" id="sunshineChannelBtn" class="btn btn-success"
							data-title="阳光渠道设置" data-position="100px" data-width="auto" href="javascript:sunshineChannelView();"><span class="icon-plus"></span>&nbsp;阳光渠道设置</a>
				</c:if>
				<c:if test="${btnAu['liveMember/manage/export']}">
					  <button type="button" id="storeProfitBtn" class="btn btn-success"  data-position="100px"
								data-width="auto" onclick="javascript:storeProfitView();"><span class="icon-edit"></span>&nbsp;储存收益设置</button>
				</c:if>			
				<c:if test="${btnAu['liveMember/manage/export']}">
					  <button type="button" id="exportAnchor" class="btn btn-default" ><span class="icon-download-alt"></span>&nbsp;导出</button>
				</c:if>

			</div>
			<div id="anchorList" class="good-table-wrap"></div>
		</div>
	</div>
	
	<!-- modal start -->
	<div class="modal fade" id="sunshineChannelModal">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span><span class="sr-only">关闭</span>
					</button>
					<h4 class="modal-title">阳光渠道管理</h4>
				</div>
				<div class="modal-body example" >
					<form class="form-horizontal" id="recommendSpecialForm">
						<input type="hidden" id="id" name="id">
						
						<div class="form-group">
							<label class="col-md-3 control-label">推荐奖励获得阳光： <span
								style="color: red;">*</span></label>
							<div class="col-md-6">
								<div class="input-group">
									<input type="text" name="recommendNumber" class="form-control"  /><span
										class="input-group-addon">束</span>
								</div>
							</div>
						</div>
						
						<!-- <div class="form-group">
						    <input name="isSpendEnergy" type="checkbox" /><label class="col-md-3 control-label" >直属下级消耗：&nbsp;&nbsp;</label>	
							<div class="col-md-6">
								<div class="input-group">
									 <input type="text" name="spendEnergyNumber"  class="form-control" readonly="true" /><span
										class="input-group-addon">能量邀请人获得推荐奖励</span>
								</div>
							</div>
						</div> -->
						<div class="form-group">
							<label class="col-md-3 control-label">园友赠送阳光： <span
								style="color: red;">*</span></label>
							<div class="col-md-6">
								<div class="input-group">
									<input type="text" name="handselNumber" class="form-control" /><span
										class="input-group-addon">束</span>
								</div>
							</div>
						</div>
						
						<div class="form-group">
							<div class="col-sm-offset-3 col-sm-6">
								<div class="alert alert-warning">
									<strong>提示：</strong> 推荐只有消耗能量的时候才能获得阳光奖励
								</div>
							</div>
						</div>
						<div class="modal-footer">
							<button type="button" class="btn btn-primary"
								id="editSunshineChannelSubmit">保存</button>
							<button type="button" class="btn btn-default"
								data-dismiss="modal">关闭</button>
						</div>
					</form>
				</div>
			</div>
		</div>
    </div> 
    
    <!-- 仓库储存收益 -->
    <div class="modal fade" id="storeProfitModal">
		<div class="modal-dialog" >
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span><span class="sr-only">关闭</span>
					</button>
					<h4 class="modal-title">仓库储存收益</h4>
				</div>
				<!-- style="height: 260px;" -->
				<div class="modal-body example" >
					<form class="form-horizontal" id="storeProfitForm">
						<!-- 公用开始 -->
                        <div class="form-group" id="dates">
						    <label class="col-md-1 control-label"></label>
							<div class="col-md-10">
								<div class="input-group">
									<span class="input-group-addon">仓库满:</span> <input type="number" name="baseNumber" placeholder="数量"  class="form-control form-date issueVolume" />
									<span class="input-group-addon">束, 可获得</span> <input type="text" name="profit" placeholder="比例(0-1)"  class="form-control form-date issueVolume" />
									<span class="input-group-addon">% 的收益</span><span class="input-group-addon "><i class="icon icon-plus" style="cursor: pointer" >添加</i></span>
								</div>
							</div>
						</div>

						<div class="form-group">
							<div class="col-sm-offset-1 col-sm-10">
								<table class="table table-bordered">
									<thead style="background-color: beige;">
										<tr class="text-center">
											<th class="text-center"><div class="header">仓库存放数量(阳光/束)</div></th>
											<th class="text-center"><div class="header">收益比例</div></th>
											<th class="text-center"><div class="header">预计每日收益(阳光/束)</div></th>
											<th class="text-center"><div class="header">操作</div></th>
										</tr>
									</thead>
			
									<tbody id="storeProfitTB">

									</tbody>
								</table>
							</div>
						</div>
						
						<!-- 结束 -->
						<div class="modal-footer">
							<button type="button" class="btn btn-primary" id="editStoreProfitSubmit">保存</button>
							<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
						</div>
					</form>
				</div>

			</div>
		</div>
    </div> 
    
	<!-- modal end -->
	
	<script type="text/json" id="permissions">{
		add:'${ btnAu['liveMember/manage/add']}'  }
	</script>
	
	<jsp:include page="../common.jsp"></jsp:include>
	<script src="<%=path%>/ux/js/ld2.js"></script>
	<script src="<%=path%>/ux/js/scrollTablel.js"></script>
	<script src="<%=path%>/resources/web/js/jquery.json-2.4.js"></script>

	<script src="<%=path%>/js/golden_manor/sunshineProfitManage.js?v=1.0.5"></script>
</body>
</html>
