<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>普通抽奖红包</title>
<meta name="renderer" content="webkit"> <meta name="fragment" content="!">
<meta name="format-detection" content="telephone=no">
<meta name="google" value="notranslate">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta http-equiv="Cache-Control" content="no-transform">
<meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
<%@ include file="/common/taglibs.jsp"%>
 <link rel="stylesheet" href="${ctx}/css/normalize.css">
 <link rel="stylesheet" href="${ctx}/css/common.css">
 <link rel="stylesheet" href="${ctx}/css/component.css">
 <link rel="stylesheet" href="${ctx}/css/marketing.css?v=2" />
</head>

<body class="padd-fill-tb bg-color-01">
    <input type="hidden" id="paramUrl" value="${requestScope.url}"/>
    <input type="hidden" id="sellerid" value="${requestScope.sellerid}"/>
    <input type="hidden" id="redpacketType" value="${requestScope.redpacketType}"/>
    <section id="step1" style="display: none;">
        <div class="container-wrap">
            <div class="activitysum-module">
                <div class="activitysum-wrap">
                    <div class="activitysum-name">预计总投入</div>
                    <div class="activitysum-input"><span><input type="text" class="amount" placeholder="输入投入金额" id="totalmoney"/></span></div>
                </div>
            </div>
            <div class="activeity-date-module">
                <div class="activeity-dived">活动时间</div>
                <div class="activeity-date-con">
                    <span class="aciveity-data-con aciveity-data-start">
                        <!-- <input type="text" placeholder="选择日期" id="activity-starttime" readonly value=""  -->
                        <a href="javascript:;" class="activity-time-select" id="activity-starttime" initTime="${requestScope.initTime}">
                            <c:choose>
                                <c:when test="${empty requestScope.currentDate }">
                                <i>选择日期</i>
                                </c:when>
                                <c:otherwise>
                                </c:otherwise>
                            </c:choose>
                            <p>${requestScope.currentDate}</p>
                        </a>
                        <i class="icon-wrap icon-arrow-right"></i>
                    </span>
                    <span class="aciveity-data-desc">至</span>
                    <span class="aciveity-data-con aciveity-data-end">
                        <!-- <input type="text" placeholder="选择日期" id="" readonly/> -->
                        <a href="javascript:;" class="activity-time-select" id="activity-endtime">
                            <i>选择日期</i>
                            <p></p>
                        </a>
                        <i class="icon-wrap icon-arrow-right"></i>
                    </span>
                </div>
            </div>
            <div class="activeity-date-module">
                <div class="activeity-dived">领取时段</div>
                <div class="activeity-date-con">
                    <span class="aciveity-data-con aciveity-data-start">
                        <!-- <input type="text" placeholder="选择时间" readonly id="activity-selstartTime" value="}" inittime=""/> -->
                        <a href="javascript:;" class="activity-time-select" id="activity-selstartTime" initTime="${requestScope.defaultStartTime}">
                            <c:choose>
                                <c:when test="${empty requestScope.defaultStartTime }">
                                <i>选择时间</i>
                                </c:when>
                                <c:otherwise>
                                </c:otherwise>
                            </c:choose>
                            <p>${requestScope.defaultStartTime}</p>
                        </a>
                        <i class="icon-wrap icon-arrow-right"></i>
                    </span>
                    <span class="aciveity-data-desc">至</span>
                    <span class="aciveity-data-con aciveity-data-end">
                        <!-- <input type="text" placeholder="选择时间" readonly id="activity-selendTime" value="${requestScope.defaultEndTime}" inittime="${requestScope.defaultEndTime}"/> -->
                        <a href="javascript:;" class="activity-time-select" id="activity-selendTime" initTime="${requestScope.defaultEndTime}">
                            <c:choose>
                                <c:when test="${empty requestScope.defaultEndTime }">
                                <i>选择时间</i>
                                </c:when>
                                <c:otherwise>
                                </c:otherwise>
                            </c:choose>
                            <p>${requestScope.defaultEndTime}</p>
                        </a>
                        <i class="icon-wrap icon-arrow-right"></i>
                    </span>
                </div>
            </div>
            <div class="ativety-data-name">
                <div class="activeity-dived">红包活动名称</div>
                <div class="activeity-input">
                    <input id="activity-name" type="text" placeholder="例如：新店开张，老板撒红包啦~" />
                </div>
            </div>
            
        </div>
        <div class="floor-module">
            <a class="floor-links links-disabled" href="javascript:;" id="gesteptwo">下一步</a>
        </div>
    </section>
    <section id="step2" style="display: none;">
        <div class="container-wrap">
            <div class="fill-list-module">
                <div class="list-divhead">预算与红包设置</div>
                <div class="list-wrap">
                    <div class="list-item"><span class="item-input-wrap" id="stp2-totalMoney"></span><span class="item-name">总预算</span></div>
                </div>
                <div class="list-wrap">
                    <div class="list-item"><span class="item-input-wrap event-input"><input name="sumName" type="number"  placeholder="最低金额" id="limit-lowmoney"/><em>-</em><input name="sumName" type="number" placeholder="最高金额" id="limit-maxmoney"/></span><span class="item-name">随机金额范围</span></div>
                </div>
            </div>
        </div>
        <div class="floor-module">
            <a class="floor-links links-type2" href="javascript:;" id="step21submit">提交浏览</a>
        </div>
    </section>
    
    <section id="step3" style="display: none;">
        <div class="container-wrap">
            <div class="fill-list-module resetfill-list">
                <div class="list-wrap">
                    <div class="list-item"><span class="item-input-wrap" id="stp3-activityname"></span><span class="item-name">红包名称</span></div>
                </div>
                <div class="list-wrap">
                    <div class="list-item"><span class="item-input-wrap" id="stp3-totalmoney"></span><span class="item-name">总预算</span></div>
                </div>
                <div id="limit-dynamic"></div>
                <div class="list-wrap">
                    <div class="list-item"><span class="item-input-wrap" id="stp3-time"></span><span class="item-name">领取时段</span></div>
                </div>
                <div class="list-wrap">
                    <div class="list-item"><span class="item-input-wrap" id="stp3-date"></span><span class="item-name">活动时间</span></div>
                </div>
            </div>
            <p class="fill-list-desc">通过给用户发红包刺激用户消费，最后返现回到商户手上；红包个数不定，由系统分配金额后来确定红包个数，红包个数保证至少5个。</p>
        </div>
        <div class="floor-module">
            <a class="floor-links links-type2" href="javascript:void(0);" id="submitform">提交</a>
        </div>
    </section>
    <script type="text/javascript" src="${ctx}/js/jquery.min.js"></script>
    <script type="text/javascript" src="${ctx}/js/component.js"></script>
    <script type="text/javascript" src="${ctx}/js/redpacket/common.js"></script>
    <script type="text/javascript" src="${ctx}/js/util.js"></script>
</body>

</html>
