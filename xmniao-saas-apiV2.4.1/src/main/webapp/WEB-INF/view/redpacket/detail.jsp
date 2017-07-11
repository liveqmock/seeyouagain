<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>数据详细</title>
<meta name="renderer" content="webkit"> <meta name="fragment" content="!">
<meta name="format-detection" content="telephone=no">
<meta name="google" value="notranslate">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta http-equiv="Cache-Control" content="no-transform">
<meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
<%@ include file="/common/taglibs.jsp"%>
<link rel="stylesheet" href="${ctx}/css/normalize.css">
<link rel="stylesheet" href="${ctx}/css/common.css">
<link rel="stylesheet" href="${ctx}/css/swiper.css">
<link rel="stylesheet" href="${ctx}/css/component.css">
<link rel="stylesheet" href="${ctx}/css/marketing.css"/>
<script type="text/javascript" src="${ctx}/js/jquery.min.js"></script>
<script type="text/javascript" src="${ctx}/js/component.js"></script>
<script type="text/javascript">
	//页面加载函数
	$("document").ready(function(){
		   $("#stopId").click(function(){

               new dialog({
//                   title : "title",
                   buttonNum : 2,
                   content : "确认要终止吗?",
                   firstBtnText : "取消",
//                   contentText : "确认要终止吗?",
                   secondBtnText:"终止",
                   secondBtnEvent : function(){
                       $.ajax({
                           url: "${ctx}/h5/redpacket/stop",
                           type: "post",
                           data : {
                               "redpacketId" : "${requestScope.data.redpacketId}"
                           },
                           success:function(data){
                               var sellerid = ${requestScope.data.sellerid}
                               if(data.state==0){
                                   Tips.show(data.info);
                                   window.setTimeout("redirectRedpList()",2000);
                               }else{
                                   Tips.show(data.info);
                               }
                           }
                       })

                   }
               })

               <%-->

			   if (confirm("确定要终止吗?")){
					$.ajax({
		    			url: "${ctx}/h5/redpacket/stop",
		    			type: "post",
		    			data : {
		    				"redpacketId" : "${requestScope.data.redpacketId}"
		     			},
		    			success:function(data){
                            var sellerid = ${requestScope.data.sellerid}
		    				if(data.state==0){
		    					Tips.show(data.info);
		    					window.location.href="${ctx}/h5/redpacket/list?sellerid=${requestScope.data.sellerid}";
		    				}else{
		    					Tips.show(data.info);
		    				}
		    			}
		    		});
				}
				<--%>
		   })
	})
    function redirectRedpList(){
        window.location.href="${ctx}/h5/redpacket/list?sellerid=${requestScope.data.sellerid}";
    }

   //领取明细
  function listRecord(redpacketType,redpacketId){
	  window.location.href="${ctx}/h5/redpacket/list_record?redpacketType="+redpacketType+"&redpacketId="+redpacketId;
  };
  
  //红包明细
  function detailRedpacket(redpacketType,redpacketId){
	  window.location.href="${ctx}/h5/redpacket/detail_redpacket?redpacketType="+redpacketType+"&redpacketId="+redpacketId;
  }
</script>

</head>
<body class="padd-fill-tb bg-color-01">
    <div class="container-wrap">
        <div class="datadetails-info-module">
            <div class="datadetails-info-wrap">
                <div class="datadetails-ht">
                     <strong><c:choose>
							<c:when test="${!empty requestScope.data.stimulateConsume && requestScope.data.stimulateConsume > 0}">
							<em>￥</em>${requestScope.data.stimulateConsume}
							</c:when>
							<c:otherwise>
							    <em>￥0.00</em>
							</c:otherwise>
                          </c:choose></strong>
                    <span>刺激消费</span>
                </div>
                <div class="datadetails-hb con-col-3">
                    <span><strong>${requestScope.data.recordNumber}</strong><font>获取次数</font></span>
                    <span>
                       <strong>
                         <c:choose>
							<c:when test="${!empty requestScope.data.newuserNum}">
							   ${requestScope.data.newuserNum}
							</c:when>
							<c:otherwise>
							  0
							</c:otherwise>
                          </c:choose> 
                       </strong>
                       <font>新会员数</font>
                    </span>
                    <span>
                         <strong> 
	                          <c:choose>
								<c:when test="${!empty requestScope.data.views}">
									${requestScope.data.views}
								</c:when>
								<c:otherwise>
									0
								</c:otherwise>
							  </c:choose> 
                          </strong>
                          <font>曝光次数</font>
                    </span>
                </div>
            </div>
        </div>
        <div class="activity-module">
            <div class="activity-item">
                <c:choose>
					<c:when test="${requestScope.data.redpacketType eq 0}">
						<i class="icon-wrap icon-xiang"></i>
					</c:when>
					<c:when test="${requestScope.data.redpacketType eq 1}">
						<i class="icon-wrap icon-xian-3"></i>
					</c:when>
					<c:when test="${requestScope.data.redpacketType eq 2}">
						<i class="icon-wrap icon-jian"></i>
					</c:when>
					<c:when test="${requestScope.data.redpacketType eq 3}">
						<i class="icon-wrap icon-tui"></i>
					</c:when>
					<c:when test="${requestScope.data.redpacketType eq 4}">
						<i class="icon-wrap icon-chou"></i>
					</c:when>
					<c:otherwise>
					
					</c:otherwise>
                 </c:choose> 
                   
                 <c:set var="nowDate" value="<%=System.currentTimeMillis()%>"></c:set>
                 <c:choose>
                    <c:when test="${(requestScope.data.status eq 1 || requestScope.data.status eq 2 || requestScope.data.status eq 3)  && !empty requestScope.data.closeDate && nowDate - requestScope.data.closeDate.time < 0}">
						  活动已进行${requestScope.data.dayNumberIng}天，距结束${requestScope.data.dayNumberEnd}天~
					</c:when>
					<c:otherwise>
					        活动已结束
					</c:otherwise>
                 </c:choose> 
            </div>
        </div>
        <div class="cost-module">
            <div class="cost-item">
                <span><em>总投入费用：</em>￥${requestScope.data.totalAmount}</span><span>
                <em>剩余金额：</em>￥
                <c:choose> 
					<c:when test="${!empty requestScope.data.chargeBalance}">
						${requestScope.data.chargeBalance}
					</c:when>
					<c:otherwise>
					
					</c:otherwise>
		        </c:choose> 
                </span>
            </div>
        </div>
        <div class="list-default-module">
            <a class="list-item item-icon-right" href="javascript:void(0)" onclick="detailRedpacket(${requestScope.data.redpacketType},${requestScope.data.redpacketId})">红包明细 <i class="icon-wrap icon-arrow-right"></i></a>
            <a class="list-item item-icon-right" href="javascript:void(0)" onclick="listRecord(${requestScope.data.redpacketType},${requestScope.data.redpacketId})">领取明细 <i class="icon-wrap icon-arrow-right"></i></a>
        </div>
    </div>
   <div class="floor-module">
      <div class="floor-links-col-2">
          <%--<h5>${requestScope.data.redpacketId}</h5>--%>
              <c:choose>
                  <c:when test="${(data.status eq 1 || data.status eq 2 || data.status eq 3)  && !empty data.closeDate && (data.closeDate.time-nowDate)  > 0}">
                      <a href="javascript:;" class="floor-item btn-white" id="stopId">终止活动</a>
                      <a class="floor-item btn-blue"
                         href="${ctx}/api/v1/common/download_material?title=${title}&id=${data.redpacketId}&type=${type}">下载宣传物料</a>
                  </c:when>
                  <c:otherwise>
                      <%--<a  href="javascript:void(0);" class="floor-item btn-white" style="background-color:#d2d2d2;">终止活动</a>--%>
                  </c:otherwise>
              </c:choose>
      </div>
  </div>
</body>
</html>