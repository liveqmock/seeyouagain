var imgRoot = $("#fastfdsHttp").val();
var videos = new Array();
var addUrl = "VstarContent/manage/main/edit/init/addMain.jhtml";
var fileArr = new Array();
var videos = new Array();
// 清除ckEditor实例
if (CKEDITOR.instances['content']) {
	CKEDITOR.instances['content'].destroy(true);
}
// 初始化富文本编辑器
$("#content").ckeditor({

});


$("#coverImgImg").uploadImg({
	urlId : "coverImg",
	showImg : $('#coverImg').val()
});



var  options={
		deleteActionOnDone:function(file, doRemoveFile){
			debugger
			var key=false;
			$.ajax({
				url : "VstarContent/manage/fileUpload/deleteId.jhtml",
				type : "post",
				dataType : "json",
				async:false, 
				data : {
					id : file.contentId
				},
				success : function(data) {
					debugger
					if(data.success){
						fileArr.remove(file.contentId);
						key= true;
					}else{
						key= false;
					}
				}
			});
			return key;
		},
		deleteConfirm:'确认删除文件?此操作不需要保存,立即生效!',
		filters : {
			// 只允许上传图片或图标（.ico）
			mime_types : [
				{
					title : '文档',
					extensions : 'pdf,ppt,doc,xls'
				},
				{
					title : '文件',
					extensions : 'zip,rar,doc'
				}
			],
			// 不允许上传重复文件
			prevent_duplicates : true,
		},
		responseHandler: function(responseObject, file) {
	        if(responseObject.response.indexOf('ok')) {
	        	var result=JSON.parse(responseObject.response);
	        	file.contentId=result.id;
	        	fileArr.push(result.id);
	        }else{
	        	 return '上传失败。服务器返回了一个错误：' + responseObject.response;
	        }
	    }
		
	}



function loadVideo(data) {
	$.ajax({
		url : "VstarContent/manage/main/edit/init/getvideosByIds.jhtml",
		type : "post",
		dataType : "json",
		data : {
			ids : videos.toString()
		},
		success : function(result) {
			if (result.success) {
				$("#videoList").html("");
				var content = '';
				var num = 0;
				//加载统计区间表单数据
				$.each(result.data, function(i, item) {
					content += "<tr id='" + item.id + "'>"
						+ "       <td>" + item.title + "</td>"
						+ "       <td>" + item.description + "</td>"
						+ "       <td>" + item.videoUrl + "</td>"
						+ "       <td><img src='"+imgRoot+item.videoImg+"' style='height:50px' ></td>"
						+ '       <td><a  data-type="ajax" data-url="VstarContent/manage/init/video.jhtml?id=' + item.id + '" data-toggle="modal">修改</a>&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:;" onclick="delectvideo(' + item.id + ')" >删除</a></td>';
					+"</tr>" ;
					if (item.totalvideos) {
						num += item.totalvideos;
					}
				})
				$("#videoList").html(content);
			} else {
				showSmReslutWindow(result.success, result.msg);
			}
		}
	});
}

function delectvideo(id) {
	$.ajax({
		url : "VstarContent/manage/main/edit/init/deleteVideo.jhtml",
		type : "post",
		dataType : "json",
		data : {
			id : id
		},
		success : function(result) {
			if (result.success) {
				$("#" + id).remove();
				videos.remove(id);
			} else {
				showSmReslutWindow(result.success, result.msg);
			}
		}
	});
}


Array.prototype.indexOf = function(val) {
	for (var i = 0; i < this.length; i++) {
		if (this[i] == val) return i;
	}
	return -1;
};

Array.prototype.remove = function(val) {
	var index = this.indexOf(val);
	if (index > -1) {
		this.splice(index, 1);
	}
};

validate("editFrom", {
	rules : {
		title : {
			required : true
		},
		viceTitle : {
			required : true
		},
		sortVal : {
			required : true
		}
	},
	messages : {
	}
}, save);

function save() {
	if (!$("#coverImg").val()) {
		showWarningWindow("warning", "请选择封面图片!", 9999);
		return;
	}
	if (!videos.length) {
		showWarningWindow("warning", "请添加教学视频!", 9999);
		return;
	}
	if (!fileArr.length) {
			showWarningWindow("warning", "请添加教学附件!", 9999);
			return;
	}
	
	var data = $('#editFrom').serializeArray();
	data = jsonFromt(data);
	data.fileIds = fileArr.toString();
	data.videoIds = videos.toString();
	data.contentText = $("#content").val();
	if(!data.contentText){
		showWarningWindow("warning", "请输入教学内容!", 9999);
		return;
	}
	$.ajax({
		type : 'post',
		url : addUrl,
		data : data,
		dataType : 'json',
		beforeSend : function(XMLHttpRequest) {
			//$('#prompt').show();
		},
		success : function(data) {
			$('#prompt').hide();
			if (data.success) {
				window.location.href = "VstarContent/manage/init.jhtml";
			} else {
				showSmReslutWindow(data.success, data.msg);
			}
		},
		error : function() {
			window.messager.warning(data.msg);
		}
	});
	
	
}


