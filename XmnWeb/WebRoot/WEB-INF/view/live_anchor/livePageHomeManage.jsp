<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
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
<title>首页推荐管理</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<link href="<%=path%>/resources/web/css/view.css" rel="stylesheet">
<link href="<%=path%>/resources/zui/css/zui.css" rel="stylesheet">
<link href="<%=path%>/resources/zui/lib/datetimepicker/datetimepicker.min.css" rel="stylesheet"> 

<link href="<%=path%>/resources/zui/lib/chosen/chosen.css" rel="stylesheet">
<link href="<%=path%>/resources/webuploader/webuploader.css" rel="stylesheet">
<link href="<%=path%>/resources/upload/upload.css" rel="stylesheet">
</head>

<body
	style="overflow-x: auto;overflow-y: auto;min-width: 1024px;background:#EEE" class="doc-views with-navbar">
	<div id="prompt" style="position: fixed; height: 100%; width: 100%; z-index: 9; background: url(resources/page/loading.gif) #FFF no-repeat center; filter: alpha(opacity =  50); opacity: 0.5; display: none;"></div>
	<div class="col-md-2 column">
		<div class="panel ">
 			<!-- <div class="panel-heading">
				<h3 class="panel-title">直播首页管理</h3>
			</div> -->
			<div class="panel-body">
				<ul id="myTab" class="nav nav-stacked">
					<li class="active"><a href="#tab1" data-toggle="tab"><i class="icon icon-flag"></i>  精选直播</a></li>
					<li><a href="#tab2" data-toggle="tab"><i class="icon icon-hand-right"></i>  品质优选</a></li>
					<li><a href="#tab3" data-toggle="tab"><i class="icon icon-comment"></i>  专题栏目</a></li>
					<li><a href="#tab4" data-toggle="tab"><i class="icon icon-trophy"></i>  明星活动</a></li>
				</ul>
			</div>
		</div>
	</div>

	<div class="col-md-10 column">
		<div class="tab-content">
			<input type="hidden" id="operationType" name="operationType"
				value="${operationType }">
			<!-- 精选推荐 -->
			<div id="tab1" class="tab-pane in active">
				<div class="panel">
					<div class="panel-body">
						<ul id="choice" class="nav nav-tabs">
							<li class="active"><a href="#choice1" data-toggle="tab">直播推荐</a></li>
							<!-- <li class=""><a href="#choice2" data-toggle="tab">精选预告</a></li>
							<li class=""><a href="#choice3" data-toggle="tab">精彩视频推荐</a></li> -->
						</ul>
						<!-- ul开始 -->
						<div class="tab-content">
							<input type="hidden" id="myChoiceType" name="myChoiceType" value="${operationType }">
							<!-- 金牌推荐-->
							<div id="choice1" class="tab-pane in active">
								<div class="panel" style="display:none; ">
									<div class="panel-body">
										<form class="form-horizontal" role="form" id="underwaySearchForm">
											<!-- -1 初始 0 预告 1 正在直播  2暂停直播 3 回放  4历史通告 5结束直播 -->
											<input type="hidden" id="zhiboType" name="zhiboType"
												value="1"> <input type="hidden" id="recommended"
												name="recommended" value="1">
											<div class="form-group">
												<table style="width: 100%;">
													<tbody>
														<tr>
															<td style="width: 8%;"><h5>&nbsp;&nbsp;直播编号:</h5></td>
															<td style="width: 24%;"><input type="text"
																class="form-control" name="id" style="width: 80%;"></td>
															<td style="width: 8%;"><h5>&nbsp;&nbsp;主播昵称:</h5></td>
															<td style="width: 24%;"><input type="text"
																class="form-control" name="nname" style="width: 80%;"></td>
															<td style="width: 8%;"><h5>商家:</h5></td>
															<td style="width: 24%;"><input type="text"
																class="form-control" name="sellername"
																style="width: 80%;"></td>
														</tr>
														<tr>
															<td style="width: 8%;"><h5>&nbsp;&nbsp;直播日期:</h5></td>
															<td style="width: 24%;"><input type="text"
																class="form-control form_datetime" name="liveDate"
																style="width: 80%;"></td>
															<!-- <td style="width: 8%;"><h5></h5></td> -->
															<td colspan="1"></td>
															<td colspan="2">
																<div class="submit" style="text-align: left;">
																	<input class="submit radius-3" type="button"
																		value="查询全部" data-bus='query' style="width: 43%;" /> <input
																		class="reset radius-3" type="reset" value="重置全部"
																		data-bus='reset' style="width: 43%;" />
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
											<c:if test="${null!=btnAu['livePageHome/manage/add'] && btnAu['livePageHome/manage/add']}">
												<button type="button" class="btn btn-success"
													data-type="ajax" data-title="添加直播推荐"
													data-url="livePageHome/manage/add/init.jhtml?zhiboType=1"
													data-toggle="modal" data-width="auto">
													<span class="icon-plus"></span>&nbsp;添加推荐
												</button>
											</c:if>
										</div>
										<div id="underwayList"></div>
									</div>
								</div>
							</div>

							<!-- 精选预告-->
							<div id="choice2" class="tab-pane">
								<div class="panel" style="display:none; ">
									<div class="panel-body">
										<form class="form-horizontal" role="form" id="advanceSearchForm">
											<!-- 直播类型 -1 初始 0 预告 1 正在直播  2暂停直播 3 回放  4 无回放 -->
											<input type="hidden" id="zhiboType" name="zhiboType"
												value="0"> <input type="hidden" id="recommended" name="recommended" value="1">
											<div class="form-group">
												<table style="width: 100%;">
													<tbody>
														<tr>
															<td style="width: 8%;"><h5>&nbsp;&nbsp;直播编号:</h5></td>
															<td style="width: 24%;"><input type="text"
																class="form-control" name="id" style="width: 80%;"></td>
															<td style="width: 8%;"><h5>&nbsp;&nbsp;主播昵称:</h5></td>
															<td style="width: 24%;"><input type="text"
																class="form-control" name="nname" style="width: 80%;"></td>
															<td style="width: 8%;"><h5>商家:</h5></td>
															<td style="width: 24%;"><input type="text"
																class="form-control" name="sellername"
																style="width: 80%;"></td>
														</tr>
														<tr>
															<td style="width: 8%;"><h5>&nbsp;&nbsp;直播日期:</h5></td>
															<td style="width: 24%;"><input type="text"
																class="form-control form_datetime" name="liveDate"
																style="width: 80%;"></td>
															<!-- <td style="width: 8%;"><h5></h5></td> -->
															<td colspan="1"></td>
															<td colspan="2">
																<div class="submit" style="text-align: left;">
																	<input class="submit radius-3" type="button"
																		value="查询全部" data-bus='query' style="width: 43%;" /> <input
																		class="reset radius-3" type="reset" value="重置全部"
																		data-bus='reset' style="width: 43%;" />
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
											<c:if test="${null!=btnAu['livePageHome/manage/add'] && btnAu['livePageHome/manage/add']}">
												<button type="button" class="btn btn-success" data-type="ajax" data-title="添加预告推荐"
													data-url="livePageHome/manage/add/init.jhtml?zhiboType=0"
													data-toggle="modal" data-width="auto"> <span class="icon-plus"></span>&nbsp;添加推荐
												</button>
											</c:if>
										</div>
										<div id="advanceList"></div>
									</div>
								</div>
							</div>

							<!-- 精彩视频推荐-->
							<div id="choice3" class="tab-pane">
								<div class="panel" style="display:none; ">
									<div class="panel-body">
										<form class="form-horizontal" role="form" id="anchorVideoSearchForm">
											<!-- <input type="hidden" id="recommended" name="recommended" value="1"> -->
											<div class="form-group">
												<table style="width: 100%;">
													<tbody>
														<tr>
															<td style="width: 8%;"><h5>&nbsp;&nbsp;标题:</h5></td>
															<td style="width: 24%;"><input type="text"
																class="form-control" name="title" style="width: 80%;"></td>
															<td style="width: 8%;"><h5>&nbsp;&nbsp;主播昵称:</h5></td>
															<td style="width: 24%;"><input type="text"
																class="form-control" name="anchorName"
																style="width: 80%;"></td>
															<td colspan="1">
																<div class="submit" style="text-align: left;">
																	<input class="submit radius-3" type="button"
																		value="查询全部" data-bus='query' />
																    <input class="reset radius-3" type="reset" value="重置全部"
																		data-bus='reset' />
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
											<c:if test="${null!=btnAu['livePageHome/manage/add'] && btnAu['livePageHome/manage/add']}">
												<button type="button" class="btn btn-success" data-type="ajax" data-title="精彩视频推荐"
													data-url="livePageHome/manage/addAnchorVideo/init.jhtml"
													data-toggle="modal" data-width="auto"> <span class="icon-plus"></span>&nbsp;添加推荐
												</button>
											</c:if>
										</div>
										<div id="anchorVideoList"></div>
									</div>
								</div>
							</div>

						</div>
						<!--  ul结束 -->
					</div>
				</div>
			</div>

			<!-- 新人推荐 -->
			<div id="tab2" class="tab-pane">
				<div class="panel">
					<div class="panel-body">
						<ul id="freshman" class="nav nav-tabs">
						    <!-- <li class="active"><a href="#freshman1" data-toggle="tab">明星活动</a></li>
							<!-- <li class=""><a href="#freshman2" data-toggle="tab">新人报道</a></li>
							<li class=""><a href="#freshman3" data-toggle="tab">直播预告</a></li> -->
							<li class="active"><a href="#freshman4" data-toggle="tab">品质优选</a></li>
						</ul>

						<!--  ul结束 -->
						<div class="tab-content">
							<input type="hidden" id="freshmanType" name="freshmanType" value="${freshmanType }">
                            <!-- 新人推荐-->
							<div id="freshman1" class="tab-pane">
								<div class="panel" style="display:none; ">
									<div class="panel-body">
										<form class="form-horizontal" role="form" id="freshmanRecommendSearchForm">
											<div class="form-group">
												<table style="width: 100%;">
													<tbody>
														<tr>
															<td style="width: 8%;"><h5>&nbsp;&nbsp;标题:</h5></td>
															<td style="width: 24%;"><input type="text"
																class="form-control" name="title" style="width: 80%;"></td>
															<td colspan="2" style="width: 35%;">
																<div class="submit" style="text-align: left;">
																	<input class="submit radius-3" type="button" value="查询全部" data-bus='query' /> 
																	<input class="reset radius-3" type="reset" value="重置全部" data-bus='reset' />
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
										<div class="btn-group" style="margin-bottom: 5px;">
											<c:if test="${null!=btnAu['livePageHome/manage/add'] && btnAu['livePageHome/manage/add']}">
												<a type="button" style="float: left" id="addFreshmanRecommendbtn"
													class="btn btn-success" data-position="100px"
													data-width="auto" href="javascript:editDelicious(1);"><span
													class="icon-plus"></span>&nbsp;添加</a>
											</c:if>
										</div>
										<div id="freshmanRecommendLabelList"></div>
									</div>
								</div>
							</div>								
								
							<!-- 新人报道(直播推荐)-->
							<div id="freshman2" class="tab-pane">
								<div class="panel" style="display:none; ">
									<div class="panel-body">
										<form class="form-horizontal" role="form"
											id="liveRecommendSearchForm">
											<!-- -1 初始 0 预告 1 正在直播  2暂停直播 3 回放  4历史通告 5结束直播 -->
											<input type="hidden" id="zhiboType" name="zhiboType"
												value="1"> <input type="hidden" id="recommended" name="recommended" value="1">
											<div class="form-group">
												<table style="width: 100%;">
													<tbody>
														<tr>
															<td style="width: 8%;"><h5>&nbsp;&nbsp;直播编号:</h5></td>
															<td style="width: 24%;"><input type="text"
																class="form-control" name="id" style="width: 80%;"></td>
															<td style="width: 8%;"><h5>&nbsp;&nbsp;主播昵称:</h5></td>
															<td style="width: 24%;"><input type="text"
																class="form-control" name="nname" style="width: 80%;"></td>
															<td style="width: 8%;"><h5>商家:</h5></td>
															<td style="width: 30%;"><input type="text"
																class="form-control" name="sellername"
																style="width: 66%;"></td>
														</tr>
														<tr>
															<td style="width: 8%;"><h5>&nbsp;&nbsp;直播日期:</h5></td>
															<td style="width: 24%;"><input type="text"
																class="form-control form_datetime" name="liveDate"
																style="width: 80%;"></td>
															<!-- <td style="width: 8%;"><h5></h5></td> -->
															<td colspan="1"></td>
															<td colspan="2">
																<div class="submit" style="text-align: left;">
																	<input class="submit radius-3" type="button"
																		value="查询全部" data-bus='query' style="width: 43%;" /> <input
																		class="reset radius-3" type="reset" value="重置全部"
																		data-bus='reset' style="width: 43%;" />
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
											<c:if test="${null!=btnAu['livePageHome/manage/add'] && btnAu['livePageHome/manage/add']}">
												<a type="button" style="float: left" id="addlivebtn"
													class="btn btn-success" data-position="100px"
													data-rtype="2" data-width="auto"
													onclick="editFreshmanRecommend(2);"><span
													class="icon-plus"></span>&nbsp;添加</a>
												<!-- data-toggle="modal" data-data-target= ""  onclick="editFreshmanRecommend(2);"  href="javascript:;"-->
											</c:if>
										</div>
										<div id="liveRecommendList"></div>
									</div>
								</div>
							</div>

							<!-- 直播预告-->
							<div id="freshman3" class="tab-pane">
								<div class="panel" style="display:none; ">
									<div class="panel-body">
										<form class="form-horizontal" role="form"
											id="advanceRecommendSearchForm">
											<!-- 直播类型 -1 初始 0 预告 1 正在直播  2暂停直播 3 回放  4 无回放 -->
											<input type="hidden" id="zhiboType" name="zhiboType"
												value="0"> <input type="hidden" id="recommended"
												name="recommended" value="1">
											<div class="form-group">
												<table style="width: 100%;">
													<tbody>
														<tr>
															<td style="width: 8%;"><h5>&nbsp;&nbsp;直播编号:</h5></td>
															<td style="width: 24%;"><input type="text"
																class="form-control" name="id" style="width: 80%;"></td>
															<td style="width: 8%;"><h5>&nbsp;&nbsp;主播昵称:</h5></td>
															<td style="width: 24%;"><input type="text"
																class="form-control" name="nname" style="width: 80%;"></td>
															<td style="width: 8%;"><h5>商家:</h5></td>
															<td style="width: 30%;"><input type="text"
																class="form-control" name="sellername"
																style="width: 66%;"></td>
														</tr>
														<tr>
															<td style="width: 8%;"><h5>&nbsp;&nbsp;直播日期:</h5></td>
															<td style="width: 24%;"><input type="text"
																class="form-control form_datetime" name="liveDate"
																style="width: 80%;"></td>
															<td colspan="1"></td>
															<td colspan="2">
																<div class="submit" style="text-align: left;">
																	<input class="submit radius-3" type="button"
																		value="查询全部" data-bus='query' style="width: 43%;" /> <input
																		class="reset radius-3" type="reset" value="重置全部"
																		data-bus='reset' style="width: 43%;" />
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
											<c:if test="${null!=btnAu['livePageHome/manage/add'] && btnAu['livePageHome/manage/add']}">
												<a type="button" style="float: left" id="addAdvancebtn"
													class="btn btn-success" data-position="100px"
													data-width="auto" href="javascript:editFreshmanRecommend(3);"><span
													class="icon-plus"></span>&nbsp;添加</a>
											</c:if>
										</div>
										<div id="advanceRecommendList"></div>
									</div>
								</div>
							</div>

							<!-- 精彩视频推荐-->
							<div id="freshman4" class="tab-pane  in active">
								<div class="panel" style="display:none; ">
									<div class="panel-body">
										<form class="form-horizontal" role="form"
											id="anchorVideoRecommendSearchForm">
											<div class="form-group">
												<table style="width: 100%;">
													<tbody>
														<tr>
															<td style="width: 8%;"><h5>&nbsp;&nbsp;标题:</h5></td>
															<td style="width: 24%;"><input type="text"
																class="form-control" name="title" style="width: 80%;"></td>
															<td style="width: 8%;"><h5>&nbsp;&nbsp;主播昵称:</h5></td>
															<td style="width: 24%;"><input type="text"
																class="form-control" name="anchorName"
																style="width: 85%;"></td>
															<td colspan="2" style="width: 35%;">
																<div class="submit" style="text-align: left;">
																	<input class="submit radius-3" type="button"
																		value="查询全部" data-bus='query' /> <input
																		class="reset radius-3" type="reset" value="重置全部"
																		data-bus='reset' />
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
											<c:if test="${null!=btnAu['livePageHome/manage/add'] && btnAu['livePageHome/manage/add']}">
												<a type="button" style="float: left" id="addAnchorVideobtn"
													class="btn btn-success" data-position="100px"
													data-width="auto" href="javascript:editFreshmanRecommend(4);"><span
													class="icon-plus"></span>&nbsp;添加</a>
											</c:if>
										</div>
										<div id="anchorVideoRecommendList"></div>
									</div>
								</div>
							</div>
						</div>
						<!--  ul结束 -->

						<!-- modal 窗口部分开始 -->
						<div class="modal fade" id="editFreshmanModal">
							<div class="modal-dialog">
								<div class="modal-content">
									<div class="modal-header">
										<button type="button" class="close" data-dismiss="modal">
											<span aria-hidden="true">×</span><span class="sr-only">关闭</span>
										</button>
										<h4 class="modal-title">添加x推荐</h4>
									</div>
									<div class="modal-body example" style="height: 200px;">
										<form class="form-horizontal" id="freshmanRecommendForm">
											<input type="hidden" name="rtype" value="" />
											<input type="hidden" name="moduleType" value="" />
											<div class="form-group">
												<!-- <label for="exampleInputPassword5" class="col-sm-3">添加专题推荐：<em class="em-tips-font">*</em></label> -->
												<label class="col-md-4 control-label">主播昵称： <span
													style="color: red;">*</span></label>
												<div class="col-sm-8" id="col8">
													<select class="form-control" name="liveRecommend_choose"
														style="width: 60%; float: left"></select>
												</div>
											</div>
											<div class="form-group">
												<label class="col-md-4 control-label">排序：&nbsp;&nbsp;</label>
												<div class="col-lg-8 col-xs-8">
													<input type="number" class="form-control" id="homeSort"
														name="homeSort" style="width: 100px;">
												</div>
											</div>

											<div class="modal-footer">
												<button type="button" class="btn btn-default"
													data-dismiss="modal">关闭</button>
												<button type="button" class="btn btn-primary"
													id="editFreshmanRecommendSubmit">保存</button>
											</div>
										</form>
									</div>

								</div>
							</div>
						</div>

						<div class="modal fade" id="editFreshmanSortModal">
							<div class="modal-dialog">
								<div class="modal-content">
									<div class="modal-header">
										<button type="button" class="close" data-dismiss="modal">
											<span aria-hidden="true">×</span><span class="sr-only">关闭</span>
										</button>
										<h4 class="modal-title">排序:</h4>
									</div>
									<div class="modal-body example" style="height: 150px;">
										<form class="form-horizontal" id="specialSortForm">
											<div class="form-group">
												<label for="editSort_inp" class="col-sm-4 control-label">修改排序:
												</label>
												<div class="col-sm-8">
													<input type="number" class="form-control" id="editSort_inp"
														name="editSort_inp" style="width: 150px;">
												</div>
											</div>
											<div class="modal-footer">
												<button type="button" class="btn btn-default"
													data-dismiss="modal">关闭</button>
												<button type="button" class="btn btn-primary"
													id="editFreshmanRecommendSortSubmit">保存</button>
											</div>
										</form>
									</div>

								</div>
							</div>
						</div>
						<!-- modal 窗口部分结束 -->

					</div>
				</div>
			</div>

			<!-- 缤纷娱乐 -->
			<div id="tab3" class="tab-pane">
				<div class="panel">
					<div class="panel-body">
						<ul id="entertainment" class="nav nav-tabs">
							<li class="active"><a href="#entertainment1" data-toggle="tab">主播专区</a></li>
							<li class=""><a href="#entertainment2" data-toggle="tab">栏目专区</a></li>
							<!-- <li class=""><a href="#entertainment3" data-toggle="tab">热门回放</a></li> -->
						</ul>

						<!--  ul结束 -->
						<div class="tab-content">
							<!-- 抢先报名 -->
							<div id="entertainment1" class="tab-pane in active">
								<div class="panel" style="display:none; ">
									<div class="panel-body">
										<form class="form-horizontal" role="form" id="entertainmentEnlistSearchForm">
											<div class="form-group">
												<table style="width: 100%;">
													<tbody>
														<tr>
															<td style="width: 8%;"><h5>&nbsp;&nbsp;标题:</h5></td>
															<td style="width: 24%;"><input type="text" class="form-control" name="title" style="width: 80%;"></td>
															<td colspan="2" style="width: 35%;">
																<div class="submit" style="text-align: left;">
																	<input class="submit radius-3" type="button" value="查询全部" data-bus='query' /> 
																	<input class="reset radius-3" type="reset" value="重置全部" data-bus='reset' />
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
										<div class="btn-group" style="margin-bottom: 5px;">
											<c:if test="${null!=btnAu['livePageHome/manage/add'] && btnAu['livePageHome/manage/add']}">
												<a type="button" style="float: left" id="addEntertainmentEnlistbtn" class="btn btn-success"
													data-position="100px" data-width="auto" href="javascript:editEntertainment(1);"><span
													class="icon-plus"></span>&nbsp;添加</a>
											</c:if>
										</div>
										<div id="entertainmentEnlistList"></div>
									</div>
								</div>
							</div>
							<!-- 节目频道 -->
							<div id="entertainment2" class="tab-pane">
								<div class="panel" style="display:none; ">
									<div class="panel-body">
										<form class="form-horizontal" role="form" id="entertainmentProgramSearchForm">
											<div class="form-group">
												<table style="width: 100%;">
													<tbody>
														<tr>
															<td style="width: 8%;"><h5>&nbsp;&nbsp;标题:</h5></td>
															<td style="width: 24%;"><input type="text"
																class="form-control" name="title" style="width: 80%;"></td>
															<td colspan="2" style="width: 35%;">
																<div class="submit" style="text-align: left;">
																	<input class="submit radius-3" type="button" value="查询全部" data-bus='query' /> 
																	<input class="reset radius-3" type="reset" value="重置全部" data-bus='reset' />
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
										<div class="btn-group" style="margin-bottom: 5px;">
											<c:if
												test="${null!=btnAu['livePageHome/manage/add'] && btnAu['livePageHome/manage/add']}">
												<a type="button" style="float: left"
													id="addEntertainmentProgrambtn" class="btn btn-success"
													data-position="100px" data-width="auto"
													href="javascript:editEntertainment(2);"><span
													class="icon-plus"></span>&nbsp;添加</a>
											</c:if>
										</div>
										<div id="entertainmentProgramList"></div>
									</div>
								</div>
							</div>
							<!-- 热门回放-->
							<div id="entertainment3" class="tab-pane">
								<div class="panel" style="display:none; ">
									<div class="panel-body">
										<form class="form-horizontal" role="form" id="entertainmentHotPlaybackSearchForm">
											<!-- -1 初始 0 预告 1 正在直播  2暂停直播 3 回放  4历史通告 5结束直播 -->
											<input type="hidden" id="zhiboType" name="zhiboType"
												value="3"> <input type="hidden" id="recommended" name="recommended" value="1">
											<div class="form-group">
												<table style="width: 100%;">
													<tbody>
														<tr>
															<td style="width: 8%;"><h5>&nbsp;&nbsp;直播编号:</h5></td>
															<td style="width: 24%;"><input type="text"
																class="form-control" name="id" style="width: 85%;"></td>
															<td style="width: 8%;"><h5>&nbsp;&nbsp;主播昵称:</h5></td>
															<td style="width: 24%;"><input type="text"
																class="form-control" name="nname" style="width: 85%;"></td>
															<td style="width: 8%;"><h5>商家:</h5></td>
															<td style="width: 30%;"><input type="text"
																class="form-control" name="sellername"
																style="width: 66%;"></td>
														</tr>
														<tr>
															<td style="width: 8%;"><h5>&nbsp;&nbsp;直播日期:</h5></td>
															<td style="width: 24%;"><input type="text"
																class="form-control form_datetime" name="liveDate"
																style="width: 85%;"></td>
															<td colspan="1"></td>
															<td colspan="2">
																<div class="submit" style="text-align: left;">
																	<input class="submit radius-3" type="button"
																		value="查询全部" data-bus='query' style="width: 43%;" /> <input
																		class="reset radius-3" type="reset" value="重置全部"
																		data-bus='reset' style="width: 43%;" />
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
											<c:if test="${null!=btnAu['livePageHome/manage/add'] && btnAu['livePageHome/manage/add']}">
												<a type="button" style="float: left" id="addEntertainmentHotPlaybackbtn" class="btn btn-success"
													data-title="添加回放视频" data-position="100px" data-width="auto"
													href="javascript:editHotPlayback(2);"><span class="icon-plus"></span>&nbsp;添加</a>
											</c:if>
										</div>
										<div id="entertainmentHotPlaybackList"></div>
									</div>
								</div>
							</div>

						</div>
						<!--  ul结束 -->
						<!-- modal 窗口部分开始 -->
						<div class="modal fade" id="editEntertainmentModal">
							<div class="modal-dialog">
								<div class="modal-content">
									<div class="modal-header">
										<button type="button" class="close" data-dismiss="modal">
											<span aria-hidden="true">×</span><span class="sr-only">关闭</span>
										</button>
										<h4 class="modal-title">添加活动模块</h4>
									</div>
									<div class="modal-body example" style="height: 420px;">
										<form class="form-horizontal" id="freshmanRecommendForm">
											<input type="hidden" name="type" value="" />
											<input type="hidden" name="moduleType" value="" />
											<div class="form-group">
												<label class="col-md-4 control-label">标题： <span
													style="color: red;">*</span></label>
												<div class="col-sm-8">
													<input type="text" class="form-control" name="title"
														placeholder="标题" value="${liveLevelInfo.title}"
														style="width: 70%; float: left">
												</div>
											</div>
											<div class="form-group" id="subTitleInfo">
												<label class="col-md-4 control-label">副标题： <span
													style="color: red;">*</span></label>
												<div class="col-sm-8">
													<input type="text" class="form-control" id="subTitle"
														name="subTitle" placeholder="副标题"
														value="${params.subTitle}" style="width: 70%; float: left">
												</div>
											</div>
											<div class="form-group" id="activityTimeInfo">
												<label class="col-md-4 control-label">活动日期： <span
													style="color: red;">*</span></label>
												<div class="col-md-7">
													<input type="text" class="form-control"
														style="width: 180px; float: left" name="activityTime"
														value="<fmt:formatDate value="${params.activityTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
														placeholder="yyyy-MM-dd hh:mm" readonly="readonly">
												</div>
											</div>
											<div class="form-group">
												<label class="col-md-4  control-label">图片： <span
													style="color: red;">*</span></label>
												<div id='activityImg' ImgValidate="true"
													style="position: relative; left: 10px; float: left; margin-top: 10px;"></div>
												<input type="hidden" id="imageUrl" name="imageUrl"
													value="${params.imageUrl}" />
											</div>
											<div class="form-group">
												<label class="col-md-4 control-label">链接：&nbsp;&nbsp;</label>
												<div class="col-sm-8">
													<input type="text" class="form-control" id="url" name="url"
														placeholder="链接" value="${liveLevelInfo.floatPerformance}"
														style="width: 70%; float: left">
												</div>
											</div>
											<div class="form-group">
												<label class="col-md-4 control-label">排序：&nbsp;&nbsp;</label>
												<div class="col-lg-8 col-xs-8">
													<input type="number" class="form-control" id="homeSort"
														name="homeSort" style="width: 120px;">
												</div>
											</div>
											<div class="form-group" id="numberInfo">
												<label class="col-md-4 control-label">期数：&nbsp;&nbsp;</label>
												<div class="col-lg-8 col-xs-8">
													<input type="number" class="form-control" id="number"
														placeholder="期数" name="number" style="width: 120px;">
												</div>
											</div>

											<div class="modal-footer">
												<button type="button" class="btn btn-default"
													data-dismiss="modal">关闭</button>
												<button type="button" class="btn btn-primary"
													id="editEntertainmentSubmit">保存</button>
											</div>
										</form>
									</div>

								</div>
							</div>
						</div>

						<div class="modal fade" id="editEntertainmentSortModal">
							<div class="modal-dialog">
								<div class="modal-content">
									<div class="modal-header">
										<button type="button" class="close" data-dismiss="modal">
											<span aria-hidden="true">×</span><span class="sr-only">关闭</span>
										</button>
										<h4 class="modal-title">排序:</h4>
									</div>
									<div class="modal-body example" style="height: 150px;">
										<form class="form-horizontal" id="specialSortForm">
											<div class="form-group">
												<label for="editSort_inp" class="col-sm-4 control-label">修改排序:
												</label>
												<div class="col-sm-8">
													<input type="number" class="form-control"
														name="editSort_inp" style="width: 150px;">
												</div>
											</div>
											<div class="modal-footer">
												<button type="button" class="btn btn-default"
													data-dismiss="modal">关闭</button>
												<button type="button" class="btn btn-primary"
													id="editEntertainmentSortSubmit">保存</button>
											</div>
										</form>
									</div>

								</div>
							</div>
						</div>
						<!-- modal 窗口部分结束 -->


					</div>
				</div>
			</div>

			<!-- 美味撩味-->
			<div id="tab4" class="tab-pane">
				<div class="panel">
					<div class="panel-body">
						<ul id="delicious" class="nav nav-tabs">
							<!-- <li class="active"><a href="#delicious1" data-toggle="tab">美食撩味</a></li> -->
							<li class=""><a href="#delicious2" data-toggle="tab">明星活动</a></li>
						</ul>

						<!--  ul结束 -->
						<div class="tab-content">
							<input type="hidden" id="deliciousType" name="deliciousType" value="${delicious}">
							<!-- 美食撩味 -->
							<%-- <div id="delicious1" class="tab-pane">
								<div class="panel" style="display:none; ">
									<div class="panel-body">
										<form class="form-horizontal" role="form" id="deliciousRecommendSearchForm">
											<div class="form-group">
												<table style="width: 100%;">
													<tbody>
														<tr>
															<td style="width: 8%;"><h5>&nbsp;&nbsp;标题:</h5></td>
															<td style="width: 24%;"><input type="text"
																class="form-control" name="title" style="width: 80%;"></td>
															<td colspan="2" style="width: 35%;">
																<div class="submit" style="text-align: left;">
																	<input class="submit radius-3" type="button"
																		value="查询全部" data-bus='query' /> <input
																		class="reset radius-3" type="reset" value="重置全部"
																		data-bus='reset' />
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
										<div class="btn-group" style="margin-bottom: 5px;">
											<c:if
												test="${null!=btnAu['livePageHome/manage/add'] && btnAu['livePageHome/manage/add']}">
												<a type="button" style="float: left" id="addDelicioustn"
													class="btn btn-success" data-position="100px"
													data-width="auto" href="javascript:editDelicious(2);"><span
													class="icon-plus"></span>&nbsp;添加</a>
												<!-- data-toggle="modal" data-target="#editDeliciousModal" -->
											</c:if>
										</div>
										<div id="deliciousList"></div>
									</div>
								</div>
							</div> --%>
							<!-- 热门回放-->
							<div id="delicious2" class="tab-pane in active">
								<div class="panel" style="display:none; ">
									<div class="panel-body">
										<form class="form-horizontal" role="form"
											id="deliciousHotPlaybackSearchForm">
											<!-- -1 初始 0 预告 1 正在直播  2暂停直播 3 回放  4历史通告 5结束直播 -->
											<input type="hidden" id="zhiboType" name="zhiboType"
												value="3"> <input type="hidden" id="recommended" name="recommended" value="1">
											<div class="form-group">
												<table style="width: 100%;">
													<tbody>
														<tr>
															<td style="width: 8%;"><h5>&nbsp;&nbsp;直播编号:</h5></td>
															<td style="width: 24%;"><input type="text"
																class="form-control" name="id" style="width: 85%;"></td>
															<td style="width: 8%;"><h5>&nbsp;&nbsp;主播昵称:</h5></td>
															<td style="width: 24%;"><input type="text"
																class="form-control" name="nname" style="width: 85%;"></td>
															<td style="width: 8%;"><h5>商家:</h5></td>
															<td style="width: 30%;"><input type="text"
																class="form-control" name="sellername"
																style="width: 66%;"></td>
														</tr>
														<tr>
															<td style="width: 8%;"><h5>&nbsp;&nbsp;直播日期:</h5></td>
															<td style="width: 24%;"><input type="text"
																class="form-control form_datetime" name="liveDate"
																style="width: 85%;"></td>
															<td colspan="1"></td>
															<td colspan="2">
																<div class="submit" style="text-align: left;">
																	<input class="submit radius-3" type="button"
																		value="查询全部" data-bus='query' style="width: 43%;" />
																	<input class="reset radius-3" type="reset" value="重置全部"
																		data-bus='reset' style="width: 43%;" />
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
											<c:if test="${null!=btnAu['livePageHome/manage/add'] && btnAu['livePageHome/manage/add']}">
												<a type="button" style="float: left"
													id="addEntertainmentProgrambtn" class="btn btn-success"
													data-title="添加回放视频" data-position="100px" data-width="auto"
													href="javascript:editHotPlayback(3);"><span
													class="icon-plus"></span>&nbsp;添加</a>
											</c:if>
										</div>
										<div id="deliciousHotPlaybackList"></div>
									</div>
								</div>
							</div>

						</div>
						<!--  ul结束 -->

					</div>
				</div>
			</div>



			<!-- 公用　modal 窗口部分开始 -->
			<div class="modal fade" id="editHotPlaybackModal">
				<div class="modal-dialog">
					<div class="modal-content">
						<div class="modal-header">
							<button type="button" class="close" data-dismiss="modal">
								<span aria-hidden="true">×</span><span class="sr-only">关闭</span>
							</button>
							<h4 class="modal-title">添加回放通告</h4>
						</div>
						<div class="modal-body example" style="height: 200px;">
							<form class="form-horizontal" id="freshmanRecommendForm">
								<input type="hidden" name="rtype" value="" />
								<input type="hidden" name="moduleType" value="" />
								<div class="form-group">
									<label for="hotPlayback_choose" class="col-md-4 control-label">主播昵称：
										<span style="color: red;">*</span>
									</label>
									<div class="col-sm-8" id="hotPlaybackcs">
										<select class="form-control" name="hotPlayback_choose"
											style="width: 60%; float: left"></select>
									</div>
								</div>
								<div class="form-group">
									<label class="col-md-4 control-label">排序：&nbsp;&nbsp;</label>
									<div class="col-lg-8 col-xs-8">
										<input type="number" class="form-control" id="homeSort"
											name="homeSort" style="width: 100px;">
									</div>
								</div>

								<div class="modal-footer">
									<button type="button" class="btn btn-default"
										data-dismiss="modal">关闭</button>
									<button type="button" class="btn btn-primary"
										id="editHotPlaybackSubmit">保存</button>
								</div>
							</form>
						</div>

					</div>
				</div>
			</div>

			<div class="modal fade" id="editHotPlaybackSortModal">
				<div class="modal-dialog">
					<div class="modal-content">
						<div class="modal-header">
							<button type="button" class="close" data-dismiss="modal">
								<span aria-hidden="true">×</span><span class="sr-only">关闭</span>
							</button>
							<h4 class="modal-title">排序:</h4>
						</div>
						<div class="modal-body example" style="height: 150px;">
							<form class="form-horizontal" id="specialSortForm">
								<div class="form-group">
									<label for="editSort_inp" class="col-sm-4 control-label">修改排序:
									</label>
									<div class="col-sm-8">
										<input type="number" class="form-control" id="editSort_inp"
											name="editSort_inp" style="width: 150px;">
									</div>
								</div>
								<div class="modal-footer">
									<button type="button" class="btn btn-default"
										data-dismiss="modal">关闭</button>
									<button type="button" class="btn btn-primary"
										id="editHotPlaybackSortSubmit">保存</button>
								</div>
							</form>
						</div>

					</div>
				</div>
			</div>
			<!-- 公用 modal 窗口部分结束 -->


			<!-- modal 窗口部分开始 -->
			<div class="modal fade" id="editDeliciousModal">
				<div class="modal-dialog">
					<div class="modal-content">
						<div class="modal-header">
							<button type="button" class="close" data-dismiss="modal">
								<span aria-hidden="true">×</span><span class="sr-only">关闭</span>
							</button>
							<h4 class="modal-title">添加标签</h4>
						</div>
						<div class="modal-body example" style="height: 220px;">
							<form class="form-horizontal" id="freshmanRecommendForm">
								<input type="hidden" name="rtype" value="" />
								<div class="form-group">
									<label class="col-md-4 control-label">主题： <span
										style="color: red;">*</span></label>
									<div class="col-sm-8">
										<input type="text" class="form-control" name="title"
											placeholder="标题" value="美食撩味" style="width: 75%; float: left"
											readonly="readonly">
									</div>
								</div>
								<div class="form-group" id="liveRecordTagDiv">
									<label class="col-md-4 control-label">直播标签： <span
										style="color: red;">*</span></label>
									<div class="col-md-7">
										<select class="form-control" id="classifyId"
											style="width: 45%;"></select> <select class="form-control"
											id="tagId" style="width: 45%;">
										</select>
									</div>
								</div>

								<div class="form-group">
									<label class="col-md-4 control-label">排序：&nbsp;&nbsp;</label>
									<div class="col-lg-8 col-xs-8">
										<input type="number" class="form-control" name="homeSort"
											style="width: 100px;">
									</div>
								</div>

								<div class="modal-footer">
									<button type="button" class="btn btn-default"
										data-dismiss="modal">关闭</button>
									<button type="button" class="btn btn-primary"
										id="editDeliciousSubmit">保存</button>
								</div>
							</form>
						</div>

					</div>
				</div>
			</div>

			<div class="modal fade" id="editDeliciousSortModal">
				<div class="modal-dialog">
					<div class="modal-content">
						<div class="modal-header">
							<button type="button" class="close" data-dismiss="modal">
								<span aria-hidden="true">×</span><span class="sr-only">关闭</span>
							</button>
							<h4 class="modal-title">排序:</h4>
						</div>
						<div class="modal-body example" style="height: 150px;">
							<form class="form-horizontal" id="specialSortForm">
								<div class="form-group">
									<label for="editSort_inp" class="col-sm-4 control-label">修改排序:
									</label>
									<div class="col-sm-8">
										<input type="number" class="form-control" name="editSort_inp"
											style="width: 150px;">
									</div>
								</div>
								<div class="modal-footer">
									<button type="button" class="btn btn-default"
										data-dismiss="modal">关闭</button>
									<button type="button" class="btn btn-primary"
										id="editDeliciousSortSubmit">保存</button>
								</div>
							</form>
						</div>

					</div>
				</div>
			</div>
			<!-- modal 窗口部分结束 -->

		</div>

	</div>

	
	<script type="text/json" id="permissions">{
	  add:'${ btnAu['livePageHome/manage/add']}',
	  update:'${ btnAu['livePageHome/manage/update']}',
	  delete:'${ btnAu['livePageHome/manage/delete']}'
	}
	</script>
	<script type="text/javascript">
		contextPath = '${pageContext.request.contextPath}'
	</script>
	<jsp:include page="../common.jsp"></jsp:include>
	<script src="<%=path%>/resources/zui/lib/datetimepicker/datetimepicker.min.js"></script>
	<script src="<%=path%>/ux/js/scrollTablel.js"></script>
	
	<script src="<%=path%>/ux/js/searchChosen.js"></script>
	<script src="<%=path%>/resources/zui/lib/chosen/chosen.js"></script>
	<script src="<%=path%>/ux/js/ld2.js"></script>
	<script src="<%=path%>/resources/web/js/jquery.json-2.4.js"></script>
	
	<script src="<%=path%>/resources/webuploader/webuploader.js"></script>	
	<script src="<%=path%>/resources/upload/upload.js"></script>
	
	<script src="<%=path%>/js/live_anchor/livePageHomeManage.js?v=1.0.2"></script>
</body>
</html>
