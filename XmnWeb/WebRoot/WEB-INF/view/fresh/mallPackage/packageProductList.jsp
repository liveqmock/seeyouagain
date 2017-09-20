<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
  <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>


<form id="videoFrom" role="form" class="form-horizontal">
	<div class="form-group">
		<label class="col-md-4 control-label">所属分类：<span
			style="color:red;">*</span></label>
		<div class="col-md-7">
			<div class="container">
				<div class="dropdown">
					<select role="button" data-toggle="dropdown"
						class="btn form-control" data-target="#" name="classa">
						<option value="" style="display:none;">请选择</option>
						<option id="dLabel" value="" style="display:none;"></option>
					</select>
					<ul class="dropdown-menu multi-level" role="menu"
						aria-labelledby="dropdownMenu">
						<li><a href="javascript:;" id="" onclick="confirmType(this)">请选择</a></li>
						<c:forEach items="${freshTypes}" var="freshType">
							<li class="dropdown-submenu">` <a tabindex="-1"
								href="javascript:;" onclick="confirmType(this)"
								id="${freshType.id}">${freshType.name}</a> <c:if
									test="${fn:length(freshType.childs)>0}">
									<ul class="dropdown-menu">
										<c:forEach items="${freshType.childs}" var="type">
											<li><a tabindex="-1" href="javascript:;"
												onclick="confirmType(this)" id="${type.id}">${type.name}</a></li>
										</c:forEach>
									</ul>
								</c:if>
							</li>
						</c:forEach>
					</ul>
				</div>
			</div>
		</div>
	</div>
	<div class="form-group">
		<label class="col-md-4 control-label">商品：<span
			style="color:red;">*</span></label>
		<div class="col-md-7" id="chooseHtml">
			<select class="form-control" id="product" name="product" style="width:50%;" />
		</div>
	</div>
	<div class="form-group">
		<label class="col-md-4 control-label">规格：<span
			style="color:red;">*</span></label>
		<div class="col-md-7" id="chooseHtml">
			<select class="form-control" id="group"
								>
							</select>
		</div>
	</div>
		<div class="form-group">
		<label class="col-md-4 control-label">数量：<span
			style="color:red;">*</span></label>
		<div class="col-md-7" id="chooseHtml">
			<input class="form-control" type="number" name="number" id="number" />
		</div>
	</div>
	<div id="imgMsg">
		<div class="form-group">
			<label class="col-md-4 control-label">缩略图：<span
				style="color:red;">*</span></label>
			<div class="col-md-7" id="breviary"></div>
		</div>
		<div class="form-group">
			<label class="col-md-4 control-label">产品图: <span
				style="color: red;">*</span></label>
			<div class="col-sm-7" id="imgList"></div>
		</div>
	</div>
	<div class="form-group">
		<div class="text-center" style="padding:10px 0 10px 0;">
			<button type="submit" id="submitProduct" class="btn btn-success">
				<span class="icon-ok"></span> 保 存
			</button>
			&nbsp;&nbsp;
			<button type="reset" class="btn btn-default" data-dismiss="modal">
				<span class="icon-remove"></span> 取 消
			</button>
		</div>
	</div>
</form>

<script src="<%=path%>/js/fresh/mallPackage/packageProductList.js"></script>
<script type="text/javascript">
	function confirmType(item) {
		$("#dLabel").text($(item).text()).val($(item).attr("id")).attr("selected", "selected");
		$("#chooseHtml").html('');
		var select=$('<select class="form-control"  id="product" name="product"   />');
		select.appendTo($("#chooseHtml"));
		select.on("change",function(){
		productChange();
		});
		select.chosenObject({
				hideValue : "codeId",
				showValue : "pname",
				url : "fresh/activity/getProduct.jhtml?classa="+$(item).attr("id"),
				isChosen : true, //是否支持模糊查询
				isCode : true, //是否显示编号
				isHistorical : false, //是否使用历史已加载数据
				width : "100%"
			});
		
		
	}
</script>
</body>
