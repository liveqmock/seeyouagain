


$(function(){
	window.location.hash='#step1';
    router();



    /*页面路由*/
    function router(){
        var hash = window.location.hash;
        if(hash == '' || hash == '#step1'){
            $('#step1').show();
            $('#step2-1').hide();
            $('#step2-2').hide();
            $('#step3').hide();
            $('#getcoupon').hide();
            $('body').scrollTop(0);
        }else if(hash == '#step2-1'){
            $('#step1').hide();
            $('#step2-1').show();
            $('#step2-2').hide();
            $('#step3').hide();
            $('#getcoupon').hide();
            $('body').scrollTop(0);
            step21Init();
        }else if(hash == '#step2-2'){
            $('#step1').hide();
            $('#step2-1').hide();
            $('#step2-2').show();
            $('#getcoupon').hide();
            $('#step3').hide();
            $('body').scrollTop(0);
            step22Init();
        }else if(hash == '#getcoupon'){
            $('#step1').hide();
            $('#step2-2').hide();
            $('#step2-1').hide();
            $('#getcoupon').show();
            $('#step3').hide();
            $('body').scrollTop(0);
        }else if(hash == '#step3-1' || hash == '#step3-2'){
            $('#step1').hide();
            $('#step2-1').hide();
            $('#step2-2').hide();
            $('#step3').show();
            $('#getcoupon').hide();
            $('body').scrollTop(0);
            step3Init(hash);
        }
    }
    
    var switchStatus = true;
    var derateSwitchStatus = false;
    var isEverySwitch = new sassSwitch({
        ele: '#every-switch',
        open: true
    });
    var isLimitSwitch = new sassSwitch({
        ele: '#sass-switch'
    });
    
    $("#every-switch").bind('switchchange',function(){
    	switchStatus = isEverySwitch.getSwitchStatus();
    });
    $("#sass-switch").bind('switchchange',function(){
    	derateSwitchStatus = isLimitSwitch.getSwitchStatus();
    });
    
    function step21Init()
    {
    	var totalMoney = $('#totalmoney').val();
    	$('#normalconsum').val(parseFloat(totalMoney).toFixed(2));
    	
    }

    function step22Init()
    {
        $('body').scrollTop(0);
        var selList = $('#menberbag-underway .slide-item');
        var selName = '';
        selList.each(function(){
            if($(this).find('.cancel-item').length == 1){
                selName = $(this).find('.selitem-name').html();
                return false;
            }
        });

        $('#full-push-name').html(selName);
    }

    function step3Init(hash){
        /*抵用金额*/
        var totalMoney = $('#totalmoney').val();
        /*活动开始日期*/
        var activityStartDate = $('#activity-starttime').attr('initTime');
        /*活动结束日期*/
        var activityEndDate = $('#activity-endtime').attr('initTime');
        /*活动名称*/
        var activityName = $('#activity-name').val();

        

        var isMultistageOpen = isLimitSwitch.getSwitchStatus();
        var isEveryLimit = isEverySwitch.getSwitchStatus();

        var reductionText = '';
            
        if(isMultistageOpen){
            /*开启状态*/
            /*一级减免金额*/
            var levoneReduction = parseFloat($('#levonereduction').val());
            /*一级消费满金额*/
            var levoneConsum = parseFloat($('#levoneconsum').val());
            /*二级减免金额*/
            var levtwoReduction = parseFloat($('#levtworeduction').val());
            /*二级消费满金额*/
            var levtwoConsum = parseFloat($('#levtwoconsum').val());
            /*三级减免金额*/
            var levthreeReduction = parseFloat($('#levthreereduction').val());
            /*三级消费满金额*/
            var levthreeConsum = parseFloat($('#levthreeconsum').val());
            
            reductionText = '满'+levoneConsum+'减'+levoneReduction+',';
            reductionText += '满'+levtwoConsum+'减'+levtwoReduction+',';
            reductionText += '满'+levthreeConsum+'减'+levthreeReduction+'';

        }else{
            /*关闭状态*/
            /*减免金额*/
            var reductionMoney = parseFloat($('#normalreduction').val());
            /*消费 并 支付满*/
            var consumFullMoney = parseFloat($('#normalconsum').val());
            reductionText = '满'+consumFullMoney+'减'+reductionMoney+'元';
        }

        $('#stp3-activityname').html(activityName);
        // $('#stp3-totalmoney').html('￥'+parseFloat(totalMoney));
        $('#stp3-date').html( activityStartDate + '至' + activityEndDate);

        
       
        if(hash == '#step3-1'){
            var _html = '<div class="list-item">';
            _html += '<span class="item-input-wrap step3-content">'+reductionText+'</span>';
            _html += '<span class="item-name step3-title">满就减规则</span>';
            _html += '</div>';

            $('#everylimit').html( isEveryLimit ? '是':'否' );
        }else if(hash == '#step3-2'){
            var selList = $('#menberbag-underway .slide-item');
            var selName = '';
            selList.each(function(){
                if($(this).find('.cancel-item').length == 1){
                    selName = $(this).find('.selitem-name').html();
                    return false;
                }
            });

            var isfullGiveSwitch = fullGiveSwitch.getSwitchStatus();

            var  _html = '<div class="list-item">';
            _html += '<span class="item-input-wrap">￥'+parseFloat(totalMoney)+'</span>';
            _html += '<span class="item-name">消费并支付满</span>';
            _html += '</div></div>';
            _html += '<div class="list-wrap">';
            _html += '<div class="list-item">';
            _html += '<span class="item-input-wrap">'+selName+'1张</span>';
            _html += '<span class="item-name">赠送奖品</span>';
            _html += '</div>';

            $('#everylimit').html( isfullGiveSwitch ? '是':'否' );

        }
        
        $('#step3_init').addClass("list-wrap").html(_html);

        
    }
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


    $('#activity-name').bind('keyup',function(){
        checkStepOneStaus();
    });
    $('#totalmoney').bind('keyup',function(){
        checkStepOneStaus();
    });






    function checkStepOneStaus(){
        /*抵用金额*/
        var totalMoney = parseFloat($('#totalmoney').val());
        /*活动开始日期*/
        var activityStartDate = $('#activity-starttime').attr('initTime');
        /*活动结束日期*/
        var activityEndDate = $('#activity-endtime').attr('initTime');
        /*活动名称*/
        var activityName = $('#activity-name').val();

        if(!isNaN(totalMoney) && totalMoney > 0 && totalMoney <= 5000 && activityStartDate && activityEndDate && activityName != ''){
            $('#gesteptwo').removeClass('links-disabled');
        }else{
            $('#gesteptwo').addClass('links-disabled');
        }
    }


    /*下一步按钮*/
    $('#gesteptwo').bind('click',function(){
        /*抵用金额*/
        var totalMoney = parseFloat($('#totalmoney').val());
        /*活动开始日期*/
        var activityStartDate = $('#activity-starttime').attr('initTime');
        /*活动结束日期*/
        var activityEndDate = $('#activity-endtime').attr('initTime');
        /*活动名称*/
        var activityName = $('#activity-name').val();

        if(!$(this).hasClass('links-disabled')){
            if($('#manjjian-radio').prop('checked')){
                window.location.hash = 'step2-1';
            }else{
                window.location.hash = 'step2-2';
            }
            
            router();
        }else{


            if(isNaN(totalMoney) || totalMoney <= 0 || totalMoney > 5000){
                Tips.show('请正确填写满减金额');
            }else if(!activityStartDate){
                Tips.show('请选择活动开始日期');
            }else if(!activityEndDate){
                Tips.show('请选择活动结束日期');
            }else if(activityName == ''){
                Tips.show('请填写满减名称');
            }
        }
    });

    /*监听是否允许多级递减*/
    $('#sass-switch').bind('switchchange',function(){
        var isOpen = isLimitSwitch.getSwitchStatus();
        if(isOpen){
        	$("#levoneconsum").val($("#normalconsum").val());
            $('.morelevel').removeClass('list-hide');
            $('.levone').addClass('list-hide');
        }else{
            $('.morelevel').addClass('list-hide');
            $('.levone').removeClass('list-hide');
        }
    });

    /*step2-1提交*/
    $('#step2-1submit').bind('click',function(){
            if(step2Check()){
                window.location.hash = 'step3-1';
            }
    });

    /*step2-1检测*/
    function step2Check(){
        var isMultistageOpen = isLimitSwitch.getSwitchStatus();
        
        if(isMultistageOpen){
            /*开启状态*/
            /*一级减免金额*/
            var levoneReduction = parseFloat($('#levonereduction').val());
            /*一级消费满金额*/
            var levoneConsum = parseFloat($('#levoneconsum').val());
            /*二级减免金额*/
            var levtwoReduction = parseFloat($('#levtworeduction').val());
            /*二级消费满金额*/
            var levtwoConsum = parseFloat($('#levtwoconsum').val());
            /*三级减免金额*/
            var levthreeReduction = parseFloat($('#levthreereduction').val());
            /*三级消费满金额*/
            var levthreeConsum = parseFloat($('#levthreeconsum').val());

            if(isNaN(levoneReduction) || levoneReduction > 5000 || levoneReduction <= 0){
                Tips.show('请输入正确的一级减免金额');
                return false;
            }else if(isNaN(levoneConsum) || levoneConsum > 5000 || levoneConsum <= 0){
                Tips.show('请输入正确的一级消费金额');
                return false;
            }else if(isNaN(levtwoReduction) || levtwoReduction > 5000 || levtwoReduction <= 0){
                Tips.show('请输入正确的二级减免金额');
                return false;
            }else if(isNaN(levtwoConsum) || levtwoConsum > 5000 || levtwoConsum <= 0){
                Tips.show('请输入正确的二级消费金额');
                return false;
            }else if(isNaN(levthreeReduction) || levthreeReduction > 5000 || levthreeReduction <= 0){
                Tips.show('请输入正确的三级减免金额');
                return false;
            }else if(isNaN(levthreeConsum) || levthreeConsum > 5000 || levthreeConsum <= 0){
                Tips.show('请输入正确的三级消费金额');
                return false;
            }
        }else{
            /*关闭状态*/
            /*减免金额*/
            var reductionMoney = parseFloat($('#normalreduction').val());
        
            /*消费 并 支付满*/
            var consumFullMoney = parseFloat($('#normalconsum').val());
            if(parseFloat($("#normalreduction").val()) > parseFloat($("#normalconsum").val())){ Tips.show('减免金额不能大于消费金额');return false}
            if(isNaN(reductionMoney) || reductionMoney > 5000 || reductionMoney < 0){
                Tips.show('请输入5000元以内的减免金额');
                return false;
            }else if(isNaN(consumFullMoney) || consumFullMoney >5000 || consumFullMoney < 0){
                Tips.show('请输入5000元以内的消费金额');
                return false;
            }
        }

        return true;
    }

    /*领取条件*/
    $('.reset-radio-input').bind('change',function(){
        if($(this).attr('id') == 'xfmhd-radio') $('#fullconsum').show();
        else $('#fullconsum').hide();
    });
    
    
    /*赠品券页面翻页*/
    var isLoading = 0;
    /*总页数*/
    var pageCount = 5;
    /*当前页面*/
    var localPage = 1;
    window.onscroll = onScrollEvent;
    document.addEventListener('touchmove', onScrollEvent , false);
    function onScrollEvent(){
        var scrollTop = document.body.scrollTop || document.documentElement.scrollTop;
        var scrollHeight = document.body.scrollHeight || document.documentElement.scrollHeight;
        var clientHeight = document.body.clientHeight || document.documentElement.clientHeight;
        if(parseInt(scrollTop) > parseInt(scrollHeight) - parseInt(clientHeight) - 20 && window.location.hash == '#getcoupon' && isLoading == 0){
            if(pageCount > localPage){
                loadData();
            }else{
                isLoading = 1;
                $('#menberbag-underway').append('<div class="end">已经到底了!</div>');
            }
        }
    }

    function loadData(){
        isLoading = 1;
        $.ajax({
            type: "POST",
            url: "/api.php",
            data: {
                page: localPage
            },
            dataType: "json",
            success: function(data){

                var _html = '<div class="slide-item">';
                _html += '<div class="item-links" href="#">';
                _html += '<div class="item-head">';
                _html += '<span class="tit-desc"><i class="icon-wrap icon-zeng"></i><span class="selitem-name">赠品券名称3</span></span>';
                _html += '</div><div class="item-content">';
                _html += '<span class="content-size">';
                _html += '<b>98</b>';
                _html += '<em>领取个数</em> </span>';
                _html += '<span class="content-time">';
                _html += '<b>98</b>';
                _html += '<em>使用次数</em></span></div>';
                _html += '<div class="item-floor">';
                _html += '<span class="floor-limit">';
                _html += '<em class="limit-total">生成总数：100</em>';
                _html += '<em class="time-date">2016.09.06-2016.09.15</em>';
                _html += '</span>';
                _html += '<span class="floor-time"><em class="limit-sum">剩余数量：865</em></span></div>';
                _html += '<div class="set-btn-wrap align-right">';
                _html += '<a class="btn-item set-item" href="javascript:;">设置为奖品</a></div></div></div>';
                $('#menberbag-underway').append(_html);
                localPage++;
                isLoading = 0; 
            }
        });
    }

    $('body').on('click','#menberbag-underway .btn-item',function(){
        // if()
        if($(this).hasClass('cancel-item')){
            $(this).removeClass('cancel-item').addClass('set-item').html('设置为奖品');
        }else{
            $('#menberbag-underway .btn-item').removeClass('cancel-item').addClass('set-item').html('设置为奖品');
            $(this).removeClass('set-item').addClass('cancel-item').html('取消');
        }
    });

    $('#btn-submit-profit').bind('click',function(){
        var selLength = $('#menberbag-underway .cancel-item').length;
        if(selLength != 1){
            Tips.show('请选择满送奖品');
        }else{   
            window.location.hash = 'step2-2';
        }
    });

    $('#step2-2submit').bind('click',function(){
        var selLength = $('#menberbag-underway .cancel-item').length;
        if(selLength != 1){
            Tips.show('请选择满送奖品');
        }else{   
            window.location.hash = 'step3-2';
        }
    });
    
    var key = 1;
    function submitSetTime(){
   	 key = 0;
   	 window.setTimeout(function(){
   			key=1;
   	 },1000); 
    }
    /*提交*/
    $('#submitform').bind('click',function(){
    	if(!key){
        	return;
        }
    	submitSetTime();
        var hash = window.location.hash;
        /*抵用金额*/
        var totalMoney = parseFloat($('#totalmoney').val());
        /*活动开始日期*/
        var activityStartDate = $('#activity-starttime').attr('initTime');
        /*活动结束日期*/
        var activityEndDate = $('#activity-endtime').attr('initTime');
        /*活动名称*/
        var activityName = $('#activity-name').val();
        /*消费满*/
        var fullMoney = parseFloat($('#normalconsum').val());
       
        


        /*数据校验*/
        if(isNaN(totalMoney) || totalMoney <= 0){
            Tips.show('请正确填写满减金额');
            return;
        }else if(!activityStartDate){
            Tips.show('请选择活动开始日期');
            return;
        }else if(!activityEndDate){
            Tips.show('请选择活动结束日期');
            return;
        }else if(activityName == ''){
            Tips.show('请填写满减名称');
            return;
        }

        if(isNaN(fullMoney || fullMoney > 5000 || fullMoney <= 0)){
            Tips.show('请输入正确的消费满金额');
            return;
        }
        
        var obj = new Object();
        obj.name = activityName;
        obj.consumeAndPay = $('#normalconsum').val();
        obj.beginDate = activityStartDate;
        obj.endDate = activityEndDate;
        obj.limitNumber = (switchStatus?"1":"0");
        obj.isDerate = (derateSwitchStatus?"1":"0");
        if(derateSwitchStatus){//启用三级免减递减
        	obj.offsetAmount = "0.00";//设置普通减免金额为0.00
        	obj.derateLevel1Amount = $('#levonereduction').val();
        	obj.consumeAndPay1 = $('#levoneconsum').val();
        	obj.derateLevel2Amount = $('#levtworeduction').val();
        	obj.consumeAndPay2 = $('#levtwoconsum').val();
        	obj.derateLevel3Amount = $('#levthreereduction').val();
        	obj.consumeAndPay3 = $('#levthreeconsum').val();
        }else{
        	obj.offsetAmount = $('#normalreduction').val();
        	obj.consumeAndPay = $('#normalconsum').val();
        	obj.derateLevel1Amount = "0.00";
        	obj.consumeAndPay1 = "0.00";
        	obj.derateLevel2Amount = "0.00";
        	obj.consumeAndPay2 = "0.00";
        	obj.derateLevel3Amount = "0.00";
        	obj.consumeAndPay3 = "0.00";
        }
        /*数据提交*/
        $.ajax({
        	type:"post",
        	url: basePath + '/h5/coupon/save_fullfeduction',
        	data:obj,
        	dataType:"json",
        	success:function(data){
        		if(data.state==0){
        			window.location=data.result.url;
        		}else{
        			Tips.show('创建满减送活动失败！');
        		}
        	}
        });
    });

});



