// 推荐消费红包 type=3
;(function(){
	
var isSuccess = 0;
router();



/*页面路由*/
function router(){
    var hash = window.location.hash;
    if(hash == '' || hash == '#step1'){
        $('#step1').show();
        $('#step2-1').hide();
        $('#step2-2').hide();
        $('#step3').hide();
    }else if(hash == '#step2-1'){
        $('#step1').hide();
        $('#step2-1').show();
        $('#step2-2').hide();
        $('#step3').hide();
        step2Init();
    }else if(hash == '#step2-2'){
        $('#step1').hide();
        $('#step2-1').hide();
        $('#step2-2').show();
        $('#step3').hide();
        step2Init();
    }else if(hash == '#step3-1' || hash == '#step3-2'){
        $('#step1').hide();
        $('#step2-1').hide();
        $('#step2-2').hide();
        $('#step3').show();
        step3Init(hash);
    }
    $('body').scrollTop(0);
}

function step2Init(){
    var totalMoney = parseFloat($('#totalmoney').val());
    $("#stp2-1-totalMoney").html('￥'+totalMoney);
    $("#stp2-2-totalMoney").html('￥'+totalMoney);
}

function step3Init(hash){
    /*投入金额*/
    var totalMoney = $('#totalmoney').val();
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

    $('#stp3-activityname').html(activityName);
    $('#stp3-totalmoney').html('￥'+parseFloat(totalMoney));
    $('#stp3-time').html( activityStartTime +'-'+ activityEndTime );
    $('#stp3-date').html( activityStartDate + ' 至 ' + activityEndDate);

    if(hash == '#step3-1'){
        /*最低金额*/
        var lowMoney = parseFloat($('#limit-lowmoney').val());
        /*最高金额*/
        var maxMoney = parseFloat($('#limit-maxmoney').val());
        var _html = '<div class="list-wrap">';
        _html += '<div class="list-item"><span class="item-input-wrap event-input">￥'+lowMoney+' - ￥'+maxMoney+'</span><span class="item-name">单个红包金额</span></div>';
        _html += '</div>';
        $('#limit-dynamic').html(_html);
    }else if(hash == '#step3-2'){
        /*红包金额*/
        var randMoney = parseFloat($('#randmoney').val());
        /*红包数量*/
        var randNum = parseInt($('#randrednum').val());

        var _html = '<div class="list-wrap">';
        _html += '<div class="list-item"><span class="item-input-wrap">￥'+randMoney+'</span><span class="item-name">单个红包金额</span></div>';
        _html += '</div>';
        _html += '<div class="list-wrap">';
        _html += '<div class="list-item"><span class="item-input-wrap">'+randNum+' 个红包</span><span class="item-name">红包个数</span></div>';
        _html += '</div>';
        
        $('#limit-dynamic').html(_html);
    }               
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


    $('#activity-name').bind('keyup',function(){
        checkStepOneStaus();
    });
    $('#totalmoney').bind('keyup',function(){
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
            if($('#suiji-radio').prop('checked')){
                window.location.hash = 'step2-1';
            }else{
                window.location.hash = 'step2-2';
            }
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

    /*step2-1提交*/
    $('#step21submit').bind('click',function(){
        /*最低金额*/
        var lowMoney = parseFloat($('#limit-lowmoney').val());
        /*最高金额*/
        var maxMoney = parseFloat($('#limit-maxmoney').val());
        if(isNaN(lowMoney) || lowMoney <= 0 ){
            Tips.show('请输入正确的最低金额');
        }else if(isNaN(maxMoney) || maxMoney <= 0){
            Tips.show('请输入正确的最高金额');
        }else if( parseFloat(lowMoney) >= parseFloat(maxMoney)){
            Tips.show('最低金额需低于最高金额');
        }else{
            window.location.hash = 'step3-1';
            router();
        }
    });

    /*step2-2提交*/
    $('#step22submit').bind('click',function(){
        /*红包金额*/
        var randMoney = parseFloat($('#randmoney').val());
        /*红包数量*/
        var randNum = parseInt($('#randrednum').val());
        if(isNaN(randMoney) || randMoney <= 0) {
          Tips.show('请输入正确的红包金额');
        }else if(randMoney >= 5000 ){
          Tips.show('红包金额需在5000以内');
        }else if(isNaN(randNum) || randNum < 0){
            Tips.show('请输入正确的红包数量');
        }else{
            window.location.hash = 'step3-2';
            router();
        }

    });

    
    $('#randmoney').bind('keyup',function(){
        var totalmoney = parseFloat($('#totalmoney').val());
        var randMoney = parseFloat($(this).val());
        if(!isNaN(randMoney) && randMoney){
            var randerNum = parseInt(totalmoney/randMoney);
            $('#randrednum').val(randerNum);
        }else{
            // Tips.show('请选择正确的金额');
        }
        
    });

    $('#randrednum').bind('keyup',function(){
        var totalmoney = parseFloat($('#totalmoney').val());
        var randNum = parseInt($(this).val());
        if(!isNaN(randNum) && randNum){
            var randerMoney = parseFloat(totalmoney/randNum).toFixed(2);
            $('#randmoney').val(randerMoney);
        }else{
            // Tips.show('请选择正确的个数');
        }
    });
    
    // $('#limit-lowmoney').bind('keyup',function(){
    //     var totalmoney = parseFloat($('#totalmoney').val());
    //     var maxmoney = parseInt($('#limit-maxmoney').val());
    //     if(!isNaN(maxmoney) && maxmoney){
    //     	if(parseInt($(this).val())>maxmoney){
    //     		Tips.show('最低金额不能大于最高金额');
    //     	}
    //         var money = parseFloat(maxmoney * 5).toFixed(2);
    //         if(money>totalmoney){
    //         	Tips.show('请确保红包最少能有5个');
    //         }
    //     }
    // });
    
    // $('#limit-maxmoney').bind('keyup',function(){
    //     var totalmoney = parseFloat($('#totalmoney').val());
    //     var lowmoney = parseFloat($('#limit-lowmoney').val());
    //     var maxmoney = parseInt($(this).val());
    //     if(!isNaN(maxmoney) && maxmoney){
    //     	if(lowmoney>maxmoney){
    //     		Tips.show('最低金额不能大于最高金额');
    //     	}
    //         var money = parseFloat(maxmoney * 5).toFixed(2);
    //         if(money>totalmoney){
    //            Tips.show('请确保红包最少能有5个');
    //         }
    //     }else{
    //         Tips.show('请输入正确的最高金额');
    //     }
    // });

    /*提交*/
    function subValidate(options){
        var hash = window.location.hash;
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
        
        /*金额类型*/
        var amountType= $('#suiji-radio').prop('checked') ? 0 : 1;

        /*step2-1提交*/
        /*最低金额*/
        var lowMoney = isNaN($('#limit-lowmoney').val()) ? null : $('#limit-lowmoney').val();
        /*最高金额*/
        var maxMoney = isNaN($('#limit-maxmoney').val()) ? null : $('#limit-maxmoney').val();

        /*step2-2提交*/
        /*红包金额*/
        var randMoney = isNaN($('#randmoney').val()) ? null : $('#randmoney').val();
        /*红包数量*/
        var randNum = isNaN($('#randrednum').val()) ? null : $('#randrednum').val();
        /*商户id*/
	    var sellerid=$('#sellerid').val();
	    /*红包类型*/
	    var redpacketType=$('#redpacketType').val();	

        /*数据校验*/
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
        }

        if(hash == '#step3-1'){
            if(isNaN(lowMoney) || lowMoney <= 0){
                Tips.show('请输入正确的最低金额');
                return false;
            }else if(isNaN(maxMoney) || maxMoney <= 0){
                Tips.show('请输入正确的最高金额');
                return false;
            }else if( parseFloat(lowMoney) >= parseFloat(maxMoney)){
                // Tips.show('请设置正确的金额范围');
              Tips.show('最低金额需低于最高金额');
                return false;
            }
        }
        if(hash == '#step3-2'){

            if(isNaN(randMoney) || randMoney <= 0) {
              Tips.show('请输入正确的红包金额');
              return false;
            }
            else if(randMoney >= 5000){
              Tips.show('金额不能高于5000');
              return false;
            }else if(isNaN(randNum) || randNum < 0){
                Tips.show('请输入正确的红包数量');
                return false;
            }
        }

        if(!submitFlag){
            Tips.show("请勿重复提交!");
            return false;
        }
        window.submitFlag = false;
        /*数据提交*/
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
            	"randomAmountMini" : lowMoney,
            	"randomAmountMax" : maxMoney,
            	"singleAmount" : randMoney,
            	"redpacketNumber" : randNum,
            	"amountType" : amountType
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
                //这里能调用ios事件
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
            //这里能调用ios事件
    		subValidate({
    			success: function(orderNo,sellerid,redpacketId,type){
    				document.location = "xmn://com.xmn.merchant/PaymentActivity?orderNo="+orderNo+"&sellerid="+sellerid+"&redpacketId="+redpacketId+"&type="+type;
    			}
    		});
    	}
    }
    
});
})();
