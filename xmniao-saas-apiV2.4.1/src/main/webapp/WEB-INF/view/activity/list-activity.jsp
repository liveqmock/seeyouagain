<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>营销活动</title>
<meta name="renderer" content="webkit">
<meta name="fragment" content="!">
<meta name="format-detection" content="telephone=no">
<meta name="google" value="notranslate">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta http-equiv="Cache-Control" content="no-transform">
<meta name="viewport"
	content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
<link rel="stylesheet" href="${ctx}/css/normalize.css">
<link rel="stylesheet" href="${ctx}/css/common.css">
<link rel="stylesheet" href="${ctx}/css/marketing.css" />
<style>
.end {
	font-size: 12px;
	text-align: center;
	padding: 2px 0;
	margin: 3px 0;
}
</style>
</head>

<body class="bg-color-01 padd-fill-tb">
	<div class="container-wrap container-wrap-padding">
		<div class="slide-tab-wrap slide-tab-fixed">
			<span class="active"><a href="javascript:;">进行中</a></span> <span><a
				href="javascript:;">已结束</a></span>
		</div>
		<div class="swiper-container" id="bag-list-module">
			<div class="swiper-wrapper">
				<div class="swiper-slide" id="menberbag-underway">
					<div class="content-slide-wrap" id="beingActivityList">
						<!-- 列表 -->
						<c:forEach items="${beingActivitys}" var='activity'>
							<c:if test="${activity.activityType==1}">
								<div class="slide-item">
									<a class="item-links" href="javascript:;" onclick="detailFreetry(this)">
										<input type="hidden" id="activityId" value="${activity.id}" />
										<input type="hidden" id="activityType" value="${activity.activityType}" />
										<div class="item-head">
											<span class="tit-desc"><i class="icon-wrap icon-chang"></i>${activity.name}</span>
										</div>
										<div class="item-content">
											<span class="content-size"> <b>${activity.joinNumber}</b>
												<em>参与人数</em>
											</span> <span class="content-time"> <b><fmt:formatNumber type="number" value="${activity.giveProportion*100}" pattern="0.00" maxFractionDigits="2"/>%</b>
												<em>获奖比例</em>
											</span>
										</div>
										<div class="item-floor">
											<span class="floor-limit"> <em class="limit-total">赠品总数：${activity.giveNumber}</em>
												<em class="limit-sum">使用数量：${activity.useNumber}</em>
											</span> <span class="floor-time"> <em class="time-date"><fmt:formatDate
														value='${activity.beginDate}' type="date"
														dateStyle="medium" />-<fmt:formatDate
														value='${activity.endDate}' type="date" dateStyle="medium" /></em>
												<div class="more-links">
													<i></i><i></i><i></i>
												</div>
											</span>
										</div>
									</a> <a class="icon-wrap share-links" id="${activity.id}" activityType="11" value="${activity.name}" href="javascript:;"></a>
								</div>
							</c:if>
							<c:if test="${activity.activityType==2}">
								<div class="slide-item">
									<a class="item-links" href="javascript:;" onclick="detailFreetry(this)">
										<input type="hidden" id="activityId" value="${activity.id}" />
										<input type="hidden" id="activityType" value="${activity.activityType}" />
										<div class="item-head">
											<span class="tit-desc"><i class="icon-wrap icon-chou"></i>${activity.name }</span>
										</div>
										<div class="item-content">
											<span class="content-size"> <b>${activity.joinNumber }</b>
												<em>参与人数</em>
											</span> <span class="content-time"> <b><fmt:formatNumber type="number" value="${activity.giveProportion*100}" pattern="0.00" maxFractionDigits="2"/>%</b>
												<em>获奖比例</em>
											</span>
										</div>
										<div class="item-floor">
											<span class="floor-limit"> <em class="limit-sum"><fmt:formatDate
														value='${activity.beginDate}' type="date"
														dateStyle="medium" />-<fmt:formatDate
														value='${activity.endDate}' type="date" dateStyle="medium" /></em>
											</span> <span class="floor-time">
												<div class="more-links">
													<i></i><i></i><i></i>
												</div>
											</span>
										</div>
									</a> <a class="icon-wrap share-links" id="${activity.id}" activityType="10" value="${activity.name}" href="javascript:;"></a>
								</div>
							</c:if>
							<c:if test="${activity.activityType==3}">
								<div class="slide-item">
									<a class="item-links" href="javascript:;" onclick="detailFreetry(this)">
										<input type="hidden" id="activityId" value="${activity.id}" />
										<input type="hidden" id="activityType" value="${activity.activityType}" />
										<div class="item-head">
											<span class="tit-desc"><i class="icon-wrap icon-xian-3"></i>${activity.name }</span>
										</div>
										<div class="item-content">
											<span class="content-size"> <b>${activity.joinNumber }</b>
												<em>参与人数</em>
											</span> <span class="content-time"> <b><fmt:formatNumber type="number" value="${activity.giveAwardCount}" pattern="0" /></b>
												<em>秒杀次数</em>
											</span>
										</div>
										<div class="item-floor">
											<span class="floor-limit"> <em class="limit-total">礼品数量：${activity.giveNumber}</em>
												<em class="limit-sum">使用数量：${activity.useNumber}</em>
											</span> <span class="floor-time"> <em class="time-date"><fmt:formatDate
														value='${activity.beginDate}' type="date"
														dateStyle="medium" />-<fmt:formatDate
														value='${activity.endDate}' type="date" dateStyle="medium" /></em>
												<div class="more-links">
													<i></i><i></i><i></i>
												</div>
											</span>
										</div>
									</a> <a class="icon-wrap share-links" id="${activity.id}" activityType="9" value="${activity.name}" href="javascript:;"></a>
								</div>
							</c:if>
							<c:if test="${activity.activityType==4}">
								<div class="slide-item">
									<a class="item-links" href="javascript:;" onclick="detailFreetry(this)">
										<input type="hidden" id="activityId" value="${activity.id}" />
										<input type="hidden" id="activityType" value="${activity.activityType}" />
										<div class="item-head">
											<span class="tit-desc"><i class="icon-direction-l icon-wrap icon-jiyou"></i>会员集赞</span>
										</div>
										<div class="item-content">
											<span class="content-size"> <b>${activity.joinNumber}</b>
												<em>参与人数</em>
											</span> <span class="content-time"> <b>${activity.countPoints}</b>
												<em>已集赞</em>
											</span>
										</div>
										<div class="item-floor">
											<span class="floor-limit">
												<em class="limit-sum">已兑换：${activity.conversionNumber}</em>
											</span> <span class="floor-time"> <em class="time-date"><fmt:formatDate
														value='${activity.beginDate}' type="date"
														dateStyle="medium" />-<fmt:formatDate
														value='${activity.endDate}' type="date" dateStyle="medium" /></em>
												<div class="more-links">
													<i></i><i></i><i></i>
												</div>
											</span>
										</div>
									</a> <a class="icon-wrap share-links" id="${activity.id}" activityType="12" value="集点活动" href="javascript:;"></a>
								</div>
							</c:if>
						</c:forEach>
						<c:if test="${empty beingActivitys}">
	                        <div class="emptydata">
									<i></i>
									<p>您当前没相关数据</p>
							</div>
					   </c:if>	
					</div>
				</div>
				<div class="swiper-slide" id="menberbag-end">
					<div class="content-slide-wrap" id="endActivityList">
						<c:forEach items="${endActivitys}" var='endActivity'>
							<c:if test="${endActivity.activityType==1}">
								<div class="slide-item">
									<a class="item-links" href="javascript:;" onclick="detailFreetry(this)">
										<input type="hidden" id="activityId" value="${endActivity.id}" />
										<input type="hidden" id="activityType" value="${endActivity.activityType}" />
										<div class="item-head">
											<span class="tit-desc"><i class="icon-wrap icon-chang-2"></i>${endActivity.name }</span>
										</div>
										<div class="item-content">
											<span class="content-size"> <b>${endActivity.joinNumber}</b>
												<em>参与人数</em>
											</span> <span class="content-time"> <b><fmt:formatNumber type="number" value="${endActivity.giveProportion*100}" pattern="0.00" maxFractionDigits="2"/>%</b>
												<em>获奖比例</em>
											</span>
										</div>
										<div class="item-floor">
											<span class="floor-limit"> <em class="limit-total">赠品总数：${endActivity.giveNumber }</em>
												<em class="limit-sum">使用数量：${endActivity.useNumber}</em>
											</span> <span class="floor-time"> <em class="time-date"><fmt:formatDate
														value='${endActivity.beginDate}' type="date"
														dateStyle="medium" />-<fmt:formatDate
														value='${endActivity.endDate}' type="date" dateStyle="medium" /></em>
												<div class="more-links">
													<i></i><i></i><i></i>
												</div>
											</span>
										</div>
									</a> <a class="icon-wrap share-links" href="javascript:;"></a>
								</div>
							</c:if>
							<c:if test="${endActivity.activityType==2}">
								<div class="slide-item">
									<a class="item-links" href="javascript:;" onclick="detailFreetry(this)">
										<input type="hidden" id="activityId" value="${endActivity.id}" />
										<input type="hidden" id="activityType" value="${endActivity.activityType}" />
										<div class="item-head">
											<span class="tit-desc"><i class="icon-wrap icon-chou-2"></i>${endActivity.name }</span>
										</div>
										<div class="item-content">
											<span class="content-size"> <b>${endActivity.joinNumber}</b>
												<em>参与人数</em>
											</span> <span class="content-time"> <b><fmt:formatNumber type="number" value="${endActivity.giveProportion*100}" pattern="0.00" maxFractionDigits="2"/>%</b>
												<em>获奖比例</em>
											</span>
										</div>
										<div class="item-floor">
											<span class="floor-limit"> <em class="limit-sum"><fmt:formatDate
														value='${endActivity.beginDate}' type="date"
														dateStyle="medium" />-<fmt:formatDate
														value='${endActivity.endDate}' type="date" dateStyle="medium" /></em>
											</span> <span class="floor-time">
												<div class="more-links">
													<i></i><i></i><i></i>
												</div>
											</span>
										</div>
									</a> <a class="icon-wrap share-links" href="javascript:;"></a>
								</div>
							</c:if>
							<c:if test="${endActivity.activityType==3}">
								<div class="slide-item">
									<a class="item-links" href="javascript:;" onclick="detailFreetry(this)">
										<input type="hidden" id="activityId" value="${endActivity.id}" />
										<input type="hidden" id="activityType" value="${endActivity.activityType}" />
										<div class="item-head">
											<span class="tit-desc"><i class="icon-wrap icon-xian-03"></i>${endActivity.name }</span>
										</div>
										<div class="item-content">
											<span class="content-size"> <b>${endActivity.joinNumber }</b>
												<em>参与人数</em>
											</span> <span class="content-time"> <b><fmt:formatNumber type="number" value="${endActivity.giveAwardCount}" pattern="0" /></b>
												<em>秒杀次数</em>
											</span>
										</div>
										<div class="item-floor">
											<span class="floor-limit"> <em class="limit-total">礼品数量：${endActivity.giveNumber}</em>
												<em class="limit-sum">使用数量：${endActivity.useNumber }</em>
											</span> <span class="floor-time"> <em class="time-date"><fmt:formatDate
														value='${endActivity.beginDate}' type="date"
														dateStyle="medium" />-<fmt:formatDate
														value='${endActivity.endDate}' type="date" dateStyle="medium" /></em>
												<div class="more-links">
													<i></i><i></i><i></i>
												</div>
											</span>
										</div>
									</a> <a class="icon-wrap share-links" href="javascript:;"></a>
								</div>
							</c:if>
							<c:if test="${endActivity.activityType==4}">
								<div class="slide-item">
									<a class="item-links" href="javascript:;" onclick="detailFreetry(this)">
										<input type="hidden" id="activityId" value="${endActivity.id}" />
										<input type="hidden" id="activityType" value="${endActivity.activityType}" />
										<div class="item-head">
											<span class="tit-desc"><i class="icon-direction-l icon-wrap icon-jiyou-02"></i>会员集赞</span>
										</div>
										<div class="item-content">
											<span class="content-size"> <b>${endActivity.joinNumber}</b>
												<em>参与人数</em>
											</span> <span class="content-time"> <b>${endActivity.countPoints}</b>
												<em>已集赞</em>
											</span>
										</div>
										<div class="item-floor">
											<span class="floor-limit">
												<em class="limit-sum">已兑换：${endActivity.conversionNumber}</em>
											</span> <span class="floor-time"> <em class="time-date"><fmt:formatDate
														value='${endActivity.beginDate}' type="date"
														dateStyle="medium" />-<fmt:formatDate
														value='${endActivity.endDate}' type="date" dateStyle="medium" /></em>
												<div class="more-links">
													<i></i><i></i><i></i>
												</div>
											</span>
										</div>
									</a> <a class="icon-wrap share-links"  href="javascript:;"></a>
								</div>
							</c:if>
						</c:forEach>
						<c:if test="${empty endActivitys}">
	                        <div class="emptydata">
									<i></i>
									<p>您当前没相关数据</p>
							</div>
					   </c:if>	
					</div>
				</div>
			</div>
		</div>

	</div>
	<div class="floor-module">
		<a class="floor-links" href="${ctx}/h5/activity/record/select_type">创建活动</a>
	</div>
</body>
<script type="text/javascript" src="${ctx}/js/jquery.min.js"></script>
<script type="text/javascript" src="${ctx}/js/activity/common.js"></script>
<script type="text/javascript" src="${ctx}/js/baseUtil.js"></script>
<script type="text/javascript">
var requestURL='${requestURL}';
$(function(){
        var tabIndex = '' ;
        $(".slide-tab-wrap span").each(function(index,ele){
            if ( $(this).hasClass("active" )) {
                tabIndex = $(this).index();
                $(".swiper-container .swiper-wrapper .swiper-slide").siblings().hide();
                $(".swiper-container .swiper-wrapper .swiper-slide").eq(tabIndex).show()
                return false;
            }
        })
        $(".slide-tab-wrap span").click(function(){
            var conIndex= $(this).index();
            $(this).addClass("active").siblings().removeClass("active");
            $(".swiper-container .swiper-wrapper .swiper-slide").siblings().hide();
            $(".swiper-container .swiper-wrapper .swiper-slide").eq(conIndex).show();
        })
    })
/*当前页数*/
var currentPage = 2;//已结束
/*内容状态,没有内容后状态为0*/
var currentPageStatus = 1;
/*每页显示条数*/
var pageSize = 5;


 	$(window).scroll(function() {
		 // 当滚动到最底部以上100像素时， 加载新内容
		 if ($(document).height() - $(this).scrollTop() - $(this).height() < 1) {
		 	if($(".active").find("a").html()=='已结束'){
		 		//进行中
		 		if(currentPageStatus){
		 			currentPageStatus=0;
		 			loadData();
		 			//$("#menberbag-underway").append("<div class="end">已经到底了</div>");
		 			//已到底最底部
		 		}
		 	}
		 }
  });
  
function loadData(){
	//分页触发方法
	var param={"pageSize":pageSize,"pageIndex":currentPage};
	$.get(requestURL,param,function(data,status){
		if(status=="success"&&data.state==0){
			$.each(data.result,function(i,item){
				var html='';
					if(item.activityType==1){
						//免尝
						  var giveProportion= item.giveProportion*100;
						   html+='<div class="slide-item"> ';
						   html+=' 	<a class="item-links" href="javascript:;" onclick="detailFreetry(this)"> ';
						   html+='  <input type="hidden" id="activityId" value="'+item.id+'" /> ';
						   html+=' <input type="hidden" id="activityType" value="'+item.activityType+'" /> ';
						   html+='  <div class="item-head"> ';
						   html+='  	<span class="tit-desc"><i class="icon-wrap icon-chang-2"></i>'+item.name+'</span> ';
						   html+='  </div> ';
						   html+='  <div class="item-content"> ';
						   html+='  	<span class="content-size"> <b>'+item.joinNumber+'</b> ';
						   html+='   <em>参与人数</em> ';
						   html+='  	</span> <span class="content-time"> <b>'+giveProportion.toFixed(2)+'%</b>';
						   html+='   <em>获奖比例</em> ';
						   html+='  	</span> ';
						   html+='  </div> ';
						   html+='  <div class="item-floor"> ';
						   html+='  	<span class="floor-limit"> <em class="limit-total">赠品总数：'+item.giveNumber +'</em> ';
						   html+='   <em class="limit-sum">使用数量：'+item.useNumber+'</em> ';
						   html+='  	</span> <span class="floor-time"> <em class="time-date">';
						   html+='   '+formatDate(new Date(item.beginDate),'yyyy-MM-dd')+'-'+formatDate(new Date(item.endDate),'yyyy-MM-dd')+'</em> ';
						   html+='   <div class="more-links"> ';
						   html+='   	<i></i><i></i><i></i> ';
						   html+='   </div> ';
						   html+='  	</span> ';
						   html+='  </div> ';
						   html+=' 	</a> <a class="icon-wrap share-links" href="javascript:;"></a> ';
						   html+=' </div> ';
					}else if(item.activityType==2){ 
							var giveProportion=item.giveProportion*100;
							html+='<div class="slide-item">';
							html+='<a class="item-links" href="javascript:;" onclick="detailFreetry(this)">';
							html+='<input type="hidden" id="activityId" value="'+item.id+'" /> ';
							html+='<input type="hidden" id="activityType" value="'+item.activityType+'" /> ';
							html+='<div class="item-head"> ';
							html+='<span class="tit-desc"><i class="icon-wrap icon-chou-2"></i>'+item.name +'</span> ';
							html+='</div>';
							html+='<div class="item-content">';
							html+='<span class="content-size"> <b>'+item.joinNumber+'</b>';
							html+='<em>参与人数</em> ';
							html+='</span> <span class="content-time"> <b>'+giveProportion.toFixed(2)+'%</b>';
							html+='<em>获奖比例</em> ';
							html+='</span> ';
							html+='</div>';
							html+='<div class="item-floor">';
							html+='<span class="floor-limit"> <em class="limit-sum">'+formatDate(new Date(item.beginDate),'yyyy-MM-dd')+'-'+formatDate(new Date(item.endDate),'yyyy-MM-dd');
							html+='</em> ';
							html+='</span> <span class="floor-time"> ';
							html+='<div class="more-links">';
							html+='<i></i><i></i><i></i> ';
							html+='</div>';
							html+='</span> ';
							html+='</div>';
							html+='</a> <a class="icon-wrap share-links" href="javascript:;"></a>';
							html+='</div> ';
						//大转盘                                                                                                                                                                            
					}else if(item.activityType==3){
						//秒杀
							html+='<div class="slide-item"> ';
							html+='<a class="item-links" href="javascript:;" onclick="detailFreetry(this)"> ';
							html+='<input type="hidden" id="activityId" value="'+item.id+'" /> ';
							html+='<input type="hidden" id="activityType" value="'+item.activityType+'" /> ';
							html+='<div class="item-head"> ';
							html+='<span class="tit-desc"><i class="icon-wrap icon-xian-2"></i>'+item.name +'</span> ';
							html+='</div> ';
							html+='<div class="item-content"> ';
							html+='<span class="content-size"> <b>'+item.joinNumber +'</b> ';
							html+='<em>参与人数</em> ';
							html+='</span> <span class="content-time"> <b>'+item.giveAwardCount+'</b>';
							html+='<em>秒杀次数</em> ';
							html+='</span> ';
							html+='</div> ';
							html+='<div class="item-floor"> ';
							html+='<span class="floor-limit"> <em class="limit-total">礼品数量：'+item.giveNumber+'</em> ';
							html+='<em class="limit-sum">使用数量：'+item.useNumber +'</em> ';
							html+='</span> <span class="floor-time"> <em class="time-date">'+formatDate(new Date(item.beginDate),'yyyy-MM-dd')+'-'+formatDate(new Date(item.endDate),'yyyy-MM-dd')+'</em> ';
							html+='<div class="more-links"> ';
							html+='<i></i><i></i><i></i> ';
							html+='</div> ';
							html+='</span> ';
							html+='</div> ';
							html+='</a> <a class="icon-wrap share-links" href="javascript:;"></a> ';
							html+='</div> ';
					}else if(item.activityType==4){
							html+='<div class="slide-item"> ';
						   html+=' 	<a class="item-links" href="javascript:;" onclick="detailFreetry(this)"> ';
						   html+='  <input type="hidden" id="activityId" value="'+item.id+'" /> ';
						   html+=' <input type="hidden" id="activityType" value="'+item.activityType+'" /> ';
						   html+='  <div class="item-head"> ';
						   html+='  	<span class="tit-desc"><i class="icon-direction-l icon-wrap icon-jiyou-02"></i>集点活动</span> ';
						   html+='  </div> ';
						   html+='  <div class="item-content"> ';
						   html+='  	<span class="content-size"> <b>'+item.joinNumber+'</b> ';
						   html+='   <em>参与人数</em> ';
						   html+='  	</span> <span class="content-time"> <b>'+item.countPoints+'</b>';
						   html+='   <em>已集赞</em> ';
						   html+='  	</span> ';
						   html+='  </div> ';
						   html+='  <div class="item-floor"> ';
						   html+='  	<span class="floor-limit">';
						   html+='   <em class="limit-sum">已兑换：'+item.conversionNumber+'</em> ';
						   html+='  	</span> <span class="floor-time"> <em class="time-date">';
						   html+='   '+formatDate(new Date(item.beginDate),'yyyy-MM-dd')+'-'+formatDate(new Date(item.endDate),'yyyy-MM-dd')+'</em> ';
						   html+='   <div class="more-links"> ';
						   html+='   	<i></i><i></i><i></i> ';
						   html+='   </div> ';
						   html+='  	</span> ';
						   html+='  </div> ';
						   html+=' 	</a> <a class="icon-wrap share-links" href="javascript:;"></a> ';
						   html+=' </div> ';
					}
					$("#endActivityList").append(html);
			});
			currentPage++;
			currentPageStatus=1;
			if(data.result.length==0){
				$("#endActivityList").append('<div class="end">已经到底了</div>');
				currentPageStatus=0;
			}
		}
	});
}  
	
	function detailFreetry(item){
		var id= $(item).find("#activityId").val();
		var activityType=$(item).find("#activityType").val();
		myPost('${ctx}/h5/activity/record/detail_init',{"activityId":id,"activityType":activityType});
	}
	
	/*调用分享接口*/
	$(".share-links").on("click",function(){
		var type=$(this).attr("activityType");
		share(this,type);
	});
	
	
</script>
</html>