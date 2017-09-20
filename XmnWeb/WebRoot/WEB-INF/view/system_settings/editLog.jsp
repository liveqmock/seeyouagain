<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="zh-CN">
<head>
<base href="<%=basePath%>">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<script type="text/javascript">
	$(document).ready(function() {
		$('#ensure').click(function() {
			var url ;
			if($('#isType').val() ==  'add'){
				url = 'system_settings/log/add.jhtml' + '?t=' + Math.random();
			}else{
				url = 'system_settings/log/update.jhtml' + '?t=' + Math.random();
			}
			$.ajax({
				type : 'post',
				url : url,
				data : jsonFromt($('#editLogForm').serializeArray()),
				dataType : 'json',
				beforeSend : function(XMLHttpRequest) {
					$('#prompt').show();
				},
				success : function(data) {
					$('#prompt').hide();
					 $('#triggerModal').modal('hide');
					if (data.success) {
						if($('#isType').val() ==  'add'){
							logList.reset();
						}else{
							logList.reload();
						}
					}
					showSmReslutWindow(data.success, data.msg);
				},
				error : function(XMLHttpRequest, textStatus, errorThrown) {
					$('#prompt').hide();
					$('#triggerModal').modal('hide');
				}
			});
		});
	});
</script>
</head>

<body>
	<form class="form-horizontal" role="form" id="editLogForm">
		<input type="hidden"  name="userId" value="${log.logId}">
		<input type="hidden"   id = "isType" value="${isType}">
		<table width="100%">
			<tbody>
				<tr>
					<th style="width:70px;"><h5>&nbsp;&nbsp;logId</h5></th>	
					<th colspan="2"><input type="text" class="form-control"  name="logId"  value="${log.logId}"></th>
				</tr>
 					<tr>
	 					<th colspan="3" style="text-align: center;"> 
	 						<button type="button" class="btn btn-success" id="ensure"><span class="icon-ok"></span>  保  存  </button>&nbsp;&nbsp;
							<button type="reset" class="btn btn-default" data-dismiss="modal"><span class="icon-remove"></span>  取  消  </button>
	 					</th>
	 				</tr>
	 			</tbody>
	 		</table>
	 </form>
</body>
</html>
