<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>


<form id="editFrom" role="form" class="form-horizontal">
	<c:if test="${!empty groupLevel}">
		<input type="hidden" name="id" id="id" value="${catehomeActivity.id}">
	</c:if>
	<div class="form-group">
		<label class="col-md-4 control-label">位置：<span
			style="color:red;">*</span></label>
		<div class="col-md-7">
			<input name="location" value="1" type="radio"
				${catehomeActivity.location==1?"checked":""}><span
				style="font-size: 12px;">左1</span> &nbsp;&nbsp; <input
				name="location" value="2" type="radio"
				${catehomeActivity.location==2?"checked":""}><span
				style="font-size: 12px;">右1</span> &nbsp;&nbsp; <input
				name="location" value="3" type="radio"
				${catehomeActivity.location==3?"checked":""}><span
				style="font-size: 12px;">右2</span>
		</div>
	</div>
	<div class="form-group">
		<label class="col-md-4 control-label">标题：<span
			style="color:red;">*</span></label>
		<div class="col-md-7">
			<input class="form-control" id="title" name="title"
				style="width:98%;" />
		</div>
	</div>

	<div class="form-group">
		<label class="col-md-4 control-label">图片：<span
			style="color:red;">*</span></label>
		<div class="col-md-7">
			<input type="hidden" class="form-control" name="imageUrl"
				id="imageUrl" value="${catehomeActivity.imageUrl}">
			<div id="picUrlImg"></div>
		</div>
	</div>

	<div class="form-group">
		<label class="col-md-4 control-label">链接：<span
			style="color:red;">*</span></label>
		<div class="col-md-7">
			<input type="text" class="form-control" name="jumpUrl" id="jumpUrl"
				value="${catehomeActivity.jumpUrl}">
		</div>
	</div>

	<div class="form-group">
		<label class="col-md-4 control-label">排序：<span
			style="color:red;">*</span></label>
		<div class="col-md-7">
			<input type="number" class="form-control" name="sort" id="sort"
				min="0" value="${catehomeActivity.sort}">
		</div>
	</div>
	<div class="form-group">
		<label class="col-md-4 control-label">地区<span
			style="color:red;">*</span></label>
		<div class="col-md-7">
			<div class="input-group" id="activityLd" style="width:83%"
				<c:choose>
									    <c:when test="${!empty freshInfo.city}">
									    	initValue=" ${freshInfo.city}"
									    </c:when>  
									    <c:otherwise>  
									    	initValue=" ${freshInfo.province}"
									    </c:otherwise>
									 </c:choose>>
			</div>
		</div>
	</div>
	<div class="form-group">
		<label class="col-md-4 control-label">是否需要登陆：<span
			style="color:red;"></span></label>
		<div class="col-md-7">
			<select class="form-control" style="width:95%;" name="mustLogin" id="mustLogin">
				<option value="0"
					>否</option>
				<option value="1"
					>是</option>
			</select>
		</div>
	</div>
	<div class="form-group">
		<div class="text-center" style="padding:10px 0 10px 0;">
			<button type="submit" class="btn btn-success">
				<span class="icon-ok"></span> 保 存
			</button>
			&nbsp;&nbsp;
			<button type="reset" class="btn btn-default" data-dismiss="modal">
				<span class="icon-remove"></span> 取 消
			</button>
		</div>
	</div>
</form>
<script type="text/javascript"
	src="<%=path%>/js/businessman/cateActivityEdit.js">

</script>
