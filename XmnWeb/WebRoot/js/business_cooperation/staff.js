var staffList;
$(document).ready(function() {
	staffList = $('#staffList').page({
		url : 'business_cooperation/staff/init/list.jhtml',
		success : success,
		pageBtnNum : 10,
		paramForm : 'searchForm'
	});

	$('#delete').click(function() {
		remove(staffList.getIds());
	});

});

/**
 * 构件表格
 * 
 * @param data
 * @param obj
 */
function success(data, obj) {
	var html = [];
	html.push('<table class="table table-hover table-bordered table-striped info" >');
	html.push('<caption style="background:#EED8D8; text-align:center; color:#703636; font-size:16px; line-height:40px;">合作商员工</caption>');
	html.push('<thead>');
	html.push('<tr>');
	html.push('<th style="width:20px;"><input type="checkbox" /></th>');
	html.push('<th style="width:80px;">操作</th>');
	html.push('<th style="width:100px;">staffid</th>');
	html.push('</tr>');
	html.push('</thead>');
	html.push('<tbody');
	for (var i = 0; i < data.content.length; i++) {
		html.push('<tr>');
		html.push('<th><input type="checkbox" val=' + data.content[i].staffid + '  /></th>');
		html.push('<td><a href="javascript:viod(); " data-type="ajax"   data-url="business_cooperation/staff/update/init.jhtml?staffid='+data.content[i].staffid+'"  data-toggle="modal" >修改</a>&nbsp;/&nbsp;<a href="javascript:remove('+data.content[i].staffid+')">删除</a></td>');
		html.push('<td>' + (undefined == data.content[i].staffid ? "-" : data.content[i].staffid) + '</td>');
		html.push('</tr>');
	}
	html.push('</tbody>');
	html.push('</table>');
	obj.find('div').eq(0).html(html.join(''));
}

/**
 * 删除
 */
function remove(staffid) {
	if(staffid==undefined || staffid==''){
		showSmReslutWindow(false, "至少选择一条记录");
		return ;
	}
	showSmConfirmWindow(function() {
		$.ajax({
			type : 'post',
			url : 'business_cooperation/staff/delete.jhtml' + '?t=' + Math.random(),
			data : 'staffid=' + staffid,
			dataType : 'json',
			beforeSend : function(XMLHttpRequest) {
				$('#prompt').show();
			},
			success : function(data) {
				$('#prompt').hide();

				if (data.success) {
					staffList.reload();
				}

				showSmReslutWindow(data.success, data.msg);
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				$('#prompt').hide();
			}
		});
	});
}
