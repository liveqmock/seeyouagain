
$("input[type=text]").attr("autocomplete","off");

//限制输入数量
function sendNumOnkeyup(item,maxNum){
	var _this= $(item);
	_this.val(_this.val().replace(/\D/g,''));
	if(_this.val()>maxNum){
			_this.val(maxNum);
	}else if(_this.val()==''){}
	else if(_this.val()<=0){
		_this.val(1);
	}
	
}

$('#select-reward-box').bind('click',function(){
    var _this = this;
    var value = $(this).attr('value');
    new selector({
        options: [
            {text: '不重设',value: "0"},
            {text: '分享获得',value: "1"},
            {text: '每天重置',value: '2'},
            {text: '消费获得',value: '3'}
        ],
        initValue: value,
        success: function(res){
            console.log(res);
            $("#setCondition").val(res.value);
            $(_this).attr('value',res.value).html(res.text);
        }
    });
});



$('#activity-starttime').bind('click',function(){
    var _this = this;
    var endTime = $('#activity-endtime').attr('initTime');
    var startTime = $(_this).attr('initTime');
    var initTime;
    if(startTime) initTime = startTime;
    else initTime = endTime;
    new datePicker({
        initTime: initTime,
        successDestory: false,
        compareTime: endTime,
        operation: '<=',
        success: function(year,month,day,datePicker,compareStatus){
            if(!compareStatus) Tips.show('请选择正确的时间');
            else{
            	$("#beginDate").val(year + '-' + month + '-' + day);
                $(_this)
                .attr('initTime',year + '年' + month + '月' + day + '日')
                .html(month + '月' + day + '日');
                datePicker.destoryDatePicker();
            }
        }
    });
});

 $('#activity-endtime').bind('click',function(){
    var _this = this;
    var startTime = $('#activity-starttime').attr('initTime');
    var endTime = $(_this).attr('initTime');
    var initTime;
    if(endTime) initTime = endTime;
    else initTime = startTime;
    new datePicker({
        initTime: initTime,
        compareTime: startTime,
        operation: '>=',
        successDestory: false,
        success: function(year,month,day,datePicker,compareStatus){
            if(!compareStatus) Tips.show('请选择正确的时间');
            else{
            	$("#endDate").val(year + '-' + month + '-' + day);
                $(_this)
                .attr('initTime',year + '年' + month + '月' + day + '日')
                .html(month + '月' + day + '日');
                datePicker.destoryDatePicker();
            } 
        }
    });
});
 
 $('#activity-selstartTime').attr("initTime",$("#beginTime").val());
 $('#activity-selendTime').attr("initTime",$("#endTime").val());
 
 $('#activity-selstartTime').bind('click',function(){
     var _this = this;
     var startTime = $(_this).attr('initTime');
     var endTime = $('#activity-selendTime').attr('initTime');
     var initTime;
     if(startTime) initTime = startTime;
     else initTime = endTime;
     new timePicker({
         initTime: initTime,
         successDestory: false,
         compareTime: endTime,
         operation: '>=',
         success: function(hour,min,timepicker,compareStatus){
             // console.log(hour);
             if(!compareStatus) Tips.show('请选择正确的时间');
             else{
            	 $("#beginTime").val(hour+":"+min);
                 $(_this)
                 .attr('initTime',hour+":"+min)
                 .html(hour+":"+min);
                 timepicker.destoryTimePicker();
             }
             
         }
     });
 });

 $('#activity-selendTime').bind('click',function(){
     var _this = this;
     var endTime = $(_this).attr('initTime');
     var startTime = $('#activity-selstartTime').attr('initTime');
     var initTime;
     if(endTime) initTime = endTime;
     else initTime = startTime;
     new timePicker({
         initTime: initTime,
         successDestory: false,
         compareTime: startTime,
         operation: '<=',
         success: function(hour,min,timepicker,compareStatus){
             if(!compareStatus) Tips.show('请选择正确的时间');
             else{
            	 $("#endTime").val(hour+":"+min);
                 $(_this)
                 .attr('initTime',hour+":"+min)
                 .html(hour+":"+min);
                 timepicker.destoryTimePicker();
             }
             
         }
     });
 });
 
 function checkSum(minSum,maxSum){
		var awardState= $("#awardState[value='1']");
		if(!(awardState.length>=minSum&&awardState.length<=maxSum)){
			if(awardState.length<minSum){
				var t =minSum-awardState.length;
				$("#awardSubmit").text("提交(还缺"+t+"个)").attr("class","floor-item btn-gray");
			}else if(awardState.length>maxSum){
				$("#awardSubmit").text("提交("+awardState.length+")").attr("class","floor-item btn-blue");
				Tips.show('奖品数量不可超过'+maxSum+"个");
			}
			return false;
		}else{
			$("#awardSubmit").text("提交("+awardState.length+")").attr("class","floor-item btn-blue");
			return true;
		}
	}
 
 $("[name=setAward]").on("click",function(){
		var awardState = $(this).parent().find($("input[id='awardState']"));
		var endDate = $(this).parent().find("#couponEndDate").val()+" 23:59:00";
		var activityEndDate=$("#endDate").val()+" 23:59:00";
		if(new Date(Date.parse(endDate.replace(/-/g, "/"))).getTime()<new Date(Date.parse(activityEndDate.replace(/-/g, "/"))).getTime()){
			$(this).parent().find("#sendNum").val("");
			Tips.show("奖品结束时间必须大于活动时间");
			return;
		}
		if(awardState.val()==0){
			$(this).text("取消");
			$(this).attr("class","btn-item set-award");
			awardState.val(1);
			checkSum(minSum,maxSum);
		}else if(awardState.val()==1){
			$(this).text("设置为奖品");
			$(this).attr("class","btn-item set-item");
			$(this).parent().find("#sendNum").val("");
			awardState.val(0);
			checkSum(minSum,maxSum);
		}
	});
 
 
 
 $("#awardSubmit").on("click",function(){
	 	submitSetTime();
		var awardState= $("#awardState[value='1']");
		if(!checkSum(minSum, maxSum)){
			return;
		}
		var choose = false;
		$.each(awardState,function(i, item){
			if($(item).val()==1){
				var par = $(item).parent();
				if(!par.find("#sendNum").val()){
					Tips.show("请填写奖品数量");
					choose = true;
					return;					
				}
				par.find("#cid").attr("name","sellerCouponDetails["+i+"].cid");
				par.find("#sendNum").attr("name","sellerCouponDetails["+i+"].sendNum");
				par.find("#couponType").attr("name","sellerCouponDetails["+i+"].couponType");
				par.find("#couponName").attr("name","sellerCouponDetails["+i+"].couponName");
				par.find("#couponEndDate").attr("name","sellerCouponDetails["+i+"].couponEndDate");
			}
		});
		if(choose){return}
		$("#listawary").submit();
	});

 /*检查奖品结束时间是否在活动日期之后*/
 function checkAwardEndDate(){
 	var activityEndDate=$("#endDate").val()+" 23:59:00";
 	var awardEndDate=$("input[name*=couponEndDate]");
 	var eachKey=true;
 	$.each(awardEndDate,function(i,item){
 		var couponEndDate=$(item).val()+" 23:59:00";
 		if(new Date(Date.parse(couponEndDate.replace(/-/g, "/"))).getTime()<new Date(Date.parse(activityEndDate.replace(/-/g, "/"))).getTime()){
 			Tips.show("活动时间有更改,请重新选择奖品");
 			eachKey=false;
 			return ;
 		}
 	});
 	return eachKey;	
 }
 
 $("#awardProportion").on("click",function(){
		var val=$(this).val()
		if(val==''){
		}else if(val.lastIndexOf("%")>=0){
			$(this).val(val.substring(0,val.lastIndexOf("%")));
		}
	}).on("blur",function(){
		var val=$(this).val();
		if(val==''){
		}else if(val.lastIndexOf("%")<0){
			$(this).val(val+"%");
		}
		if(val!=''&&val.indexOf(".")==0){
			if(val>0){
				$(this).val("0"+$(this).val());
			}else{
				$(this).val("");
			}
			
		}
	}).on("keyup",function(){
		$(this).val($(this).val().replace(/[^\d.]+/g,''));
	});

 
/* $("#secKillAmount").on("click",function(){
		var val=$(this).val()
		if(val==''){
		}else if(val.indexOf("￥")>=0){
			$(this).val(val.substring(val.indexOf("￥")+1));
		}
		
	}).on("blur",function(){
		var val=$(this).val();
		if(val==''){
		}else if(val.lastIndexOf(".")<0){
			$(this).val(val+".00");
		}else if(val.length-1-val.lastIndexOf(".")==0){
			$(this).val(val+"00");
		}else if(val.length-1-val.lastIndexOf(".")>2){
			$(this).val(val.substring(0,val.indexOf(".")+3));
		}
		if(val!=''&&val.indexOf(".")==0){
			$(this).val("0"+$(this).val());
		}
		if(val!=''&&val.lastIndexOf("￥")<0){
			$(this).val("￥"+$(this).val());
		}
		
	}).on("keyup",function(){
		$(this).val($(this).val().replace(/[^\d.]+/g,''));
	});
*/

 var key = 1;
 function submitSetTime(){
	 key = 0;
	 window.setTimeout(function(){
			key=1;
	 },1000); 
 }
 
 function formatDate(date, fmt)
 {
     date = date == undefined ? new Date() : date;
     date = typeof date == 'number' ? new Date(date) : date;
     fmt = fmt || 'yyyy-MM-dd HH:mm:ss';
     var obj =
     {
         'y': date.getFullYear(), // 年份，注意必须用getFullYear
         'M': date.getMonth() + 1, // 月份，注意是从0-11
         'd': date.getDate(), // 日期
         'q': Math.floor((date.getMonth() + 3) / 3), // 季度
         'w': date.getDay(), // 星期，注意是0-6
         'H': date.getHours(), // 24小时制
         'h': date.getHours() % 12 == 0 ? 12 : date.getHours() % 12, // 12小时制
         'm': date.getMinutes(), // 分钟
         's': date.getSeconds(), // 秒
         'S': date.getMilliseconds() // 毫秒
     };
     var week = ['天', '一', '二', '三', '四', '五', '六'];
     for(var i in obj)
     {
         fmt = fmt.replace(new RegExp(i+'+', 'g'), function(m)
         {
             var val = obj[i] + '';
             if(i == 'w') return (m.length > 2 ? '星期' : '周') + week[val];
             for(var j = 0, len = val.length; j < m.length - len; j++) val = '0' + val;
             return m.length == 1 ? val : val.substring(val.length - m.length);
         });
     }
     return fmt;
 }
 
 function eachOnslide(obj,totalArr){ //表单数据组成数组
     $(obj).each(function(index,ele){
         var itemTitle = $(ele).find(".tit-desc").text();
         var itemValue = $(ele).find('.set-input input').val();
         var oOnslideItem = {};
         oOnslideItem['otitle'] =itemTitle;
         oOnslideItem['ovalue'] = itemValue;
         totalArr.push(oOnslideItem);
     })
 }
 
 function myPost(url,args){
	    var body = $(document.body),
	        form = $("<form method='post'></form>"),
	        input;
	    form.attr({"action":url});
	    $.each(args,function(key,value){
	        input = $("<input type='hidden'>");
	        input.attr({"name":key});
	        input.val(value);
	        form.append(input);
	    });

	    form.appendTo(document.body);
	    form.submit();
	    document.body.removeChild(form[0]);
	}
 