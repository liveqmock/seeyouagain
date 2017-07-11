// 分享引流红包 type=0
;(function(){
	
var isSuccess = 0;
router();
/*页面路由*/
function router(){
    var hash = window.location.hash;
    if(hash == '' || hash == '#step1'){
        $('#step1').show();
        $('#step2').hide();
        $('#step3').hide();
    }else if(hash == '#step2'){
        $('#step1').hide();
        $('#step2').show();
        $('#step3').hide();
        step2Init();
    }else if(hash == '#step3'){
        $('#step1').hide();
        $('#step2').hide();
        $('#step3').show();
        step3Init();
    }
    $('body').scrollTop(0);
}

function step2Init(){
    var totalMoney = parseFloat($('#totalmoney').val());
    $("#stp2-totalMoney").html('￥'+totalMoney);
}

function step3Init(){
    /*投入金额*/
    var totalMoney = parseFloat($('#totalmoney').val());
    /*活动开始日期*/
    var activityStartDate = $('#activity-starttime').attr('initTime');
    /*活动结束日期*/
    var activityEndDate = $('#activity-endtime').attr('initTime');
    /*活动开始时间*/
    var activityStartTime = $('#activity-selstartTime').attr('initTime');
    /*活动结束时间*/
    var activityEndTime = $('#activity-selendTime').attr('initTime');
    /*活动名称*/
    var activityName = $('#activity-name').val();
    /*新用户金额*/
    var newUserMoney = parseFloat($('#newusermoney').val());
    /*旧用户金额*/
    var oldUserMoney = parseFloat($('#oldusermoney').val());
    /*分享红包比例*/
    var percent = $('#select-reward-box').attr('value');

    $('#stp3-activityname').html(activityName);
    $('#stp3-totalmoney').html('￥'+totalMoney);
    $('#stp3-newusermoney').html('￥'+newUserMoney);
    $('#stp3-oldusermoney').html('￥'+oldUserMoney);
    $('#stp3-sharepecent').html(percent);
    $('#stp3-time').html( activityStartTime +'-'+ activityEndTime );
    $('#stp3-date').html( activityStartDate + ' 至 ' + activityEndDate);
}


$(function(){
    window.onhashchange = router;
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
                    $(_this)
                    .attr('initTime',year + '年' + month + '月' + day + '日')
                    .html(month + '月' + day + '日');
                    datePicker.destoryDatePicker();

                    checkStepOneStaus();
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
                    $(_this)
                    .attr('initTime',year + '年' + month + '月' + day + '日')
                    .html(month + '月' + day + '日');
                    datePicker.destoryDatePicker();
                    checkStepOneStaus();
                } 
            }
        });
    });

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
                    $(_this)
                    .attr('initTime',hour+":"+min)
                    .html(hour+":"+min);
                    timepicker.destoryTimePicker();
                    checkStepOneStaus();
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
                    $(_this)
                    .attr('initTime',hour+":"+min)
                    .html(hour+":"+min);
                    timepicker.destoryTimePicker();
                    checkStepOneStaus();
                }
                
            }
        });
    });


    $('#activity-name').keyup(function(){
        checkStepOneStaus();
    });
    
    $('#totalmoney').keyup(function(){
        checkStepOneStaus();
    });



    function checkStepOneStaus(){
        /*投入金额*/
        var totalMoney = parseFloat($('#totalmoney').val());
        /*活动开始日期*/
        var activityStartDate = $('#activity-starttime').attr('initTime');
        /*活动结束日期*/
        var activityEndDate = $('#activity-endtime').attr('initTime');
        /*活动开始时间*/
        var activityStartTime = $('#activity-selstartTime').attr('initTime');
        /*活动结束时间*/
        var activityEndTime = $('#activity-selendTime').attr('initTime');
        /*活动名称*/
        var activityName = $('#activity-name').val();

        if(!isNaN(totalMoney) && totalMoney > 0 && activityStartDate && activityEndDate && activityStartTime && activityEndTime && activityName != ''){
            $('#gesteptwo').removeClass('links-disabled');
        }else{
            $('#gesteptwo').addClass('links-disabled');
        }
    }
    /*下一步按钮*/
    $('#gesteptwo').bind('click',function(){
        if(!$(this).hasClass('links-disabled')){
            window.location.hash = 'step2';
            router();
        }else{
            /*投入金额*/
            var totalMoney = parseFloat($('#totalmoney').val());
            /*活动开始日期*/
            var activityStartDate = $('#activity-starttime').attr('initTime');
            /*活动结束日期*/
            var activityEndDate = $('#activity-endtime').attr('initTime');
            /*活动开始时间*/
            var activityStartTime = $('#activity-selstartTime').attr('initTime');
            /*活动结束时间*/
            var activityEndTime = $('#activity-selendTime').attr('initTime');
            /*活动名称*/
            var activityName = $('#activity-name').val();

            if(isNaN(totalMoney) || totalMoney <= 0){
                Tips.show('请正确填写投入金额');
            }else if(!activityStartDate){
                Tips.show('请选择活动开始日期');
            }else if(!activityEndDate){
                Tips.show('请选择活动结束日期');
            }else if(!activityStartTime){
                Tips.show('请选择活动开始时间');
            }else if(!activityEndTime){
                Tips.show('请选择活动结束时间');
            }else if(activityName == ''){
                Tips.show('请填写活动名称');
            }
        }
    });

    /*选择红包奖励比例*/
    $('#select-reward-box').bind('click',function(){
        var _this = this;
        var value = $(this).attr('value');
        new selector({
            options: [
                {text: '5%',value: '5%'},
                {text: '10%',value: '10%'},
                {text: '15%',value: '15%'},
                {text: '20%',value: '20%'}
            ],
            initValue: value,
            success: function(res){
                console.log(res);
                $(_this).attr('value',res.value).html(res.text);
                var totalmoney = parseFloat($('#totalmoney').val());
                var percent =res.value;
                var newusermoney = parseInt($('#newusermoney').val());
                var oldusermoney = parseInt($('#oldusermoney').val());
                if(!isNaN(newusermoney) && !isNaN(oldusermoney)){
                	//((新用户金额+新用户金额)*分享红包奖励比例) *  5 < 红包总金额 （确保每个红包活动最少能有5个或以上获得）
                	var percent=res.value.replace("%","") / 100;
                	var money=((newusermoney+oldusermoney) * percent) * 5;
                    if(money>totalmoney){
                       Tips.show('请确保红包最少能有5个');
                    }
                }else{
                       Tips.show('请输入正确的新旧用户金额');
                }
            }
        });
    });
    /*提交预览按钮事件*/
    $('#gostepthree').bind('click',function(){
        /*投入金额*/
        var totalMoney = parseFloat($('#totalmoney').val());
        /*新用户金额*/
        var newUserMoney = parseFloat($('#newusermoney').val());
        /*旧用户金额*/
        var oldUserMoney = parseFloat($('#oldusermoney').val());
        /*分享红包比例*/
        var percent = $('#select-reward-box').attr('value');
        if(isNaN(newUserMoney) || newUserMoney > 5000 || newUserMoney <= 0){
            Tips.show('请输入正确的新用户金额');
        }else if(isNaN(oldUserMoney) || oldUserMoney > 5000 || oldUserMoney <= 0){
            Tips.show('请输入正确的旧用户金额');
        }else if( (oldUserMoney + newUserMoney) * 3 >= totalMoney){
            Tips.show('需要投入￥'+((oldUserMoney + newUserMoney) * 3+1)+'，或减少用户领取金额');
            // Tips.show('请输入正确的金额');
        }else if(typeof(percent) == 'undefined' || percent == ''){
            Tips.show('请选择分享红包奖励比例');
        }else{
            window.location.hash = 'step3';
            router();
        }
    });
    
    /*提交按钮*/
    function subValidate(options){
	    /*投入金额*/
	    var totalMoney = parseFloat($('#totalmoney').val());
	    /*活动开始日期*/
	    var activityStartDate = $('#activity-starttime').attr('initTime');
	    /*活动结束日期*/
	    var activityEndDate = $('#activity-endtime').attr('initTime');
	    /*活动开始时间*/
	    var activityStartTime = $('#activity-selstartTime').attr('initTime');
	    /*活动结束时间*/
	    var activityEndTime = $('#activity-selendTime').attr('initTime');
	    /*活动名称*/
	    var activityName = $('#activity-name').val();
	    /*新用户金额*/
	    var newUserMoney = $('#newusermoney').val();
	    /*旧用户金额*/
	    var oldUserMoney = $('#oldusermoney').val();
	    /*分享红包比例*/
	    var percent = $('#select-reward-box').attr('value');
	    /*商户id*/
	    var sellerid=$('#sellerid').val();
	    /*红包类型*/
	    var redpacketType=$('#redpacketType').val();	
	    
	    if(isNaN(totalMoney) || totalMoney <= 0){
	        Tips.show('请正确填写投入金额');
	        return false;
	    }else if(!activityStartDate){
	        Tips.show('请选择活动开始日期');
	        return false;
	    }else if(!activityEndDate){
	        Tips.show('请选择活动结束日期');
	        return false;
	    }else if(!activityStartTime){
	        Tips.show('请选择活动开始时间');
	        return false;
	    }else if(!activityEndTime){
	        Tips.show('请选择活动结束时间');
	        return false;
	    }else if(activityName == ''){
	        Tips.show('请填写活动名称');
	        return false;
	    }else if(isNaN(newUserMoney) || newUserMoney > 5000 || newUserMoney <= 0){
	        Tips.show('请输入正确的新用户金额');
	        return false;
	    }else if(isNaN(oldUserMoney) || oldUserMoney > 5000 || oldUserMoney <= 0){
	        Tips.show('请输入正确的旧用户金额');
	        return false;
	    }else if(typeof(percent) == 'undefined' || percent == ''){
	        Tips.show('请选择分享红包奖励比例');
	        return false;
	    }else{
            if(!submitFlag){
                Tips.show("请勿重复提交!");
                return false;
            }
            window.submitFlag = false;
	        //请求
	        $.ajax({
	            type: "POST",
	            url: basePath+"/h5/redpacket/save",
	            data: {
	            	"sellerid" : sellerid,
	            	"redpacketType" : redpacketType,
	            	"totalAmount" : totalMoney,
	            	"beginDate" : activityStartDate,
	            	"endDate" : activityEndDate,
	            	"beginTime" : activityStartTime,
	            	"endTime" : activityEndTime,
	            	"redpacketName" : activityName,
	            	"newUserAmount" : newUserMoney,
	            	"oldUserAmount" :oldUserMoney,
	            	"shareAwardsProportion" : percent 
	            },
	            dataType: "json",
	            success: function(response){
	            	if(response.state==0){
	            		var orderNo=response.result.orderNo;
	        			var sellerid=response.result.sellerid;
	        			var redpacketId=response.result.redpacketId;
                  var type = response.result.type;
		            	isSuccess = 1;
		            	options.success(orderNo,sellerid,redpacketId,type);
		            	Tips.show(response.info);
	            	}else{
	            		Tips.show(response.info);
	            	}
	            }
	        }); 
	    }
    };
    
    //判断是否是ios系统
    function isIOS(){
    	var ua = navigator.userAgent.toLowerCase();	
    	if (/iphone|ipad|ipod/.test(ua)) {
    		return true;		
    	} else{
    		return false;	
    	}
    }

    /*这段代码是固定的，必须要放到js中*/
    function setupWebViewJavascriptBridge(callback) {
        if (window.WebViewJavascriptBridge) { 
        	  return callback(WebViewJavascriptBridge); 
        }
        if (window.WVJBCallbacks) {
        	 return window.WVJBCallbacks.push(callback); 
        }
        window.WVJBCallbacks = [callback];
        var WVJBIframe = document.createElement('iframe');
        WVJBIframe.style.display = 'none';
        WVJBIframe.src = 'wvjbscheme://__BRIDGE_LOADED__';
        document.documentElement.appendChild(WVJBIframe);
        setTimeout(function() { 
        	 document.documentElement.removeChild(WVJBIframe) 
        }, 0)
    };
  
    var submitform = document.getElementById('submitform');
    window.submitFlag = true;
    if(isIOS()){
    	/*与OC交互的所有JS方法都要放在此处注册，才能调用通过JS调用OC或者让OC调用这里的JS*/
    	setupWebViewJavascriptBridge(function(bridge) {
        	//这里能调用ios事件
        	submitform.onclick = function(e) {
        		subValidate({
        			success: function(orderNo,sellerid,redpacketId,type){
        				bridge.callHandler('objectCallback', {'orderNo': orderNo,"sellerid" : sellerid,"redpacketId":redpacketId,"type":type}, function(response) {
        	    			log(response)
        	    		});
        			}
        		});
        	}
        });
    }else{
    	submitform.onclick = function(e) {
    		subValidate({
    			success: function(orderNo,sellerid,redpacketId,type){
    				document.location = "xmn://com.xmn.merchant/PaymentActivity?orderNo="+orderNo+"&sellerid="+sellerid+"&redpacketId="+redpacketId+"&type="+type;
    				
    			}
    		});
    	}
    }
    
});

})();

