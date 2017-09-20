package com.xmniao.xmn.core.user_terminal.entity;

import java.util.Date;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xmniao.xmn.core.base.BaseEntity;
import com.xmniao.xmn.core.util.Base64;
import com.xmniao.xmn.core.util.DateUtil;

/**
 *@ClassName:TBanner
 *@Description:导航图实体
 *@author hls
 *@date:2016年5月11日上午9:23:42
 */
public class TBanner extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8156397505120788351L;

	private Integer id;//导航图编号
	
	private Integer isEmphasis; //是否重点推荐
	
	private String position;//导航图位置信息，格式 1 2 3 4 
	private String positionStr;//导航图位置信息，(用于页面显示)
	
	private Integer bannerStyle;//展示风格。0图片横排一格，1图片横排两格
	private String bannerStyleStr;//展示风格。0图片横排一格，1图片横排两格(用于页面显示)
	
	private String objJson;//内容，json数组对象，由picurl，type，content，sort组成
	private String objJsonOpen;//内容，json数组对象，由picurl，type，content，sort组成（解密 objJson）
	
	private String picurl1;//图片1地址
	private String picurl2;//图片2地址
	
	private Integer type1; //图片1内容类型，1、套餐列表（原生界面）2、套餐详情（原生）3、外部连接（H5）4、商家详情页（原生）5、生鲜商品详情（原生）'
	private Integer type2; //图片2内容类型，1、套餐列表（原生界面）2、套餐详情（原生）3、外部连接（H5）4、商家详情页（原生）5、生鲜商品详情（原生）'
	private String typeStr1;//图片内容类型(用于页面显示)
	private String typeStr2;//图片内容类型(用于页面显示)
	
	private String content1;//图片1内容，1、套餐列表（原生界面）2、套餐详情（原生）3、外部连接（H5）4、商家详情页（原生）5、生鲜商品详情（原生）'
	private String content2;//图片2内容，1、套餐列表（原生界面）2、套餐详情（原生）3、外部连接（H5）4、商家详情页（原生）5、生鲜商品详情（原生）'
	
	private Integer sort1;//横排两格排序 图1
	private Integer sort2;//横排两格排序 图2
	
	private Integer status;//上线状态 0.待上线，1.已上线，2.已下线(用于接收sql查询出来的字段)
	private String statusStr;//上线状态 0.待上线，1.已上线，2.已下线(用于页面显示)
	
	private String remarks;//banner描述
	
	private Date createTime;//创建时间(用于接收sql查询出来的字段)
	private String createTimeStr;//创建时间(用于页面显示)
	
	private Date updateTime;//更新时间(用于接收sql查询出来的字段)
	private String updateTimeStr;//更新时间(用于页面显示)
	
	private Date sdate;//创建起始时间（用来作为查询起始时间）
	private Date edate;//创建结束时间（用来作为查询结束时间）
	
	private String ids;//批量删除id
	
	private Integer sort;//排序，越大，越优先展示,add by lifeng
	private Integer logRequired1;//是否需要登录1,默认为0，0:不需要登录，1:需要登录
	private Integer logRequired2;//是否需要登录2,默认为0,0:不需要登录，1:需要登录
	
	
	private String isAll;//全国 0 否 1 是
	private String province;//省
	private String city;//市
	private String provinceStr;//省
	private String cityStr;//是
	
	private Date beginTime; //开始时间
	private Date endTime;  //结束时间
	private String beginTimeStr; 
	private String endTimeStr;  
	
	
	public String getBeginTimeStr() {
		if(null==beginTime) return "-";
		beginTimeStr = DateUtil.smartFormat(beginTime);
		return beginTimeStr;
	}
	public void setBeginTimeStr(String beginTimeStr) {
		this.beginTimeStr = beginTimeStr;
	}
	public String getEndTimeStr() {
		if(null==endTime) return "-";
		endTimeStr = DateUtil.smartFormat(endTime);
		return endTimeStr;
	}
	public void setEndTimeStr(String endTimeStr) {
		this.endTimeStr = endTimeStr;
	}
	public Date getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public Integer getIsEmphasis() {
		return isEmphasis;
	}
	public void setIsEmphasis(Integer isEmphasis) {
		this.isEmphasis = isEmphasis;
	}
	public String getCityStr() {
		return cityStr;
	}
	public void setCityStr(String cityStr) {
		this.cityStr = cityStr;
	}
	//2016-7-4 改原来的位置信息为1 ，2，3形式
	public String getPositionStr() {
//		if("INDEX_001".equals(position)) return "首页上";
//		if("INDEX_002".equals(position)) return "首页中";
//		if("INDEX_003".equals(position)) return "首页下";
//		if("FRESH_001".equals(position)) return "生鲜页上";
//		if("FRESH_002".equals(position)) return "生鲜页中";
//		if("FRESH_003".equals(position)) return "生鲜页下";
		if("1".equals(position)) return "发现美食";
		if("2".equals(position)) return "积分商城";
		if("3".equals(position)) return "寻蜜客主页";
		if("4".equals(position)) return "直播首页";
		if("100".equals(position)) return "商家经营页";
		if("5".equals(position)) return "积分超市竞拍活动";
		if("6".equals(position)) return "积分超市夺宝活动";
		if("7".equals(position)) return "首页活动";
		if("8".equals(position)) return "充值界面";
		if("9".equals(position)) return "我的首页";
		if("10".equals(position)) return "脉宝云店";
		if("11".equals(position)) return "直播结束广告";
		return positionStr;
	}
	public String getProvinceStr() {
		return provinceStr;
	}
	public void setProvinceStr(String provinceStr) {
		this.provinceStr = provinceStr;
	}
	public void setPositionStr(String positionStr) {
		this.positionStr = positionStr;
	}
	public String getPicurl1() {
		return picurl1;
	}
	public void setPicurl1(String picurl1) {
		this.picurl1 = picurl1;
	}
	public String getPicurl2() {
		return picurl2;
	}
	public void setPicurl2(String picurl2) {
		this.picurl2 = picurl2;
	}
	public void setType1(Integer type1) {
		this.type1 = type1;
	}
	public String getIsAll() {
		return isAll;
	}
	public void setIsAll(String isAll) {
		this.isAll = isAll;
	}
	public Integer getType2() {
		return type2;
	}
	public void setType2(Integer type2) {
		this.type2 = type2;
	}
	public String getContent1() {
		return content1;
	}
	public void setContent1(String content1) {
		this.content1 = content1;
	}
	public String getContent2() {
		return content2;
	}
	public void setContent2(String content2) {
		this.content2 = content2;
	}
	public Integer getSort1() {
		return sort1;
	}
	public void setSort1(Integer sort1) {
		this.sort1 = sort1;
	}
	public Integer getSort2() {
		return sort2;
	}
	public void setSort2(Integer sort2) {
		this.sort2 = sort2;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
	public Date getSdate() {
		return sdate;
	}
	public void setSdate(Date sdate) {
		this.sdate = sdate;
	}
	public Date getEdate() {
		return edate;
	}
	public void setEdate(Date edate) {
		this.edate = edate;
	}
	public String getObjJsonOpen() {
		if(null != objJson && !"".equals(objJson)){
			objJsonOpen = Base64.getFromBase64(objJson);
		}
		return objJsonOpen;
	}
	public void setObjJsonOpen(String objJsonOpen) {
		this.objJsonOpen = objJsonOpen;
	}
	public String getBannerStyleStr() {
		if(bannerStyle == 0)return "图片横排一格";
		if(bannerStyle == 1)return "图片横排两格";
		if(bannerStyle == 2)return "轮播图";
		return bannerStyleStr;
	}
	public void setBannerStyleStr(String bannerStyleStr) {
		this.bannerStyleStr = bannerStyleStr;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public Integer getBannerStyle() {
		return bannerStyle;
	}
	public void setBannerStyle(Integer bannerStyle) {
		this.bannerStyle = bannerStyle;
	}
	public String getObjJson() {
		return objJson;
	}
	public void setObjJson(String objJson) {
		this.objJson = objJson;
		if(null != objJson && !"".equals(objJson)){
			JSONArray data = JSONArray.parseArray(Base64.getFromBase64(objJson));
			int bannerStyle = getBannerStyle();
			if(bannerStyle == 0){
			     JSONObject jobj =  (JSONObject) data.get(0);
			     setPicurl1(jobj.get("pic_url").toString());
			     setType1(Integer.parseInt(jobj.get("type").toString()));
			     setContent1(jobj.get("content").toString());
			     Object object1 = jobj.get("logRequired");
			     if(object1 !=null){
			    	 String logRequired  = object1.toString();
			    	 setLogRequired1(Integer.parseInt(logRequired==null||logRequired.equals("")?"0":logRequired));
			     }
			     Object sortObj = jobj.get("sort");
			     if(sortObj != null){
			    	 if(!sortObj.toString().equals("")) setSort1(Integer.parseInt(sortObj.toString()));
			     }
			}else if(bannerStyle == 1){
				JSONObject jobj1 =  (JSONObject) data.get(0);
				setPicurl1(jobj1.get("pic_url").toString());
				setType1(Integer.parseInt(jobj1.get("type").toString()));
				setContent1(jobj1.get("content").toString());
				Object object1 = jobj1.get("logRequired");
			    if(object1 !=null){
			    	String logRequired  = object1.toString();
			    	setLogRequired1(Integer.parseInt(logRequired==null||logRequired.equals("")?"0":logRequired));
			    }
			    Object sortObj1 = jobj1.get("sort");
			    if(sortObj1 != null){
			    	if(!sortObj1.toString().equals("")) setSort1(Integer.parseInt(sortObj1.toString()));
			    }
				JSONObject jobj2 =  (JSONObject) data.get(1);
				setPicurl2(jobj2.get("pic_url").toString());
				setType2(Integer.parseInt(jobj2.get("type").toString()));
				setContent2(jobj2.get("content").toString());
				Object object2 = jobj2.get("logRequired");
				if(object2 != null){
					String logRequired = object2.toString();				
					setLogRequired2(Integer.parseInt(logRequired==null||logRequired.equals("")?"0":logRequired));
				}
				Object sortObj2 = jobj2.get("sort");
			    if(sortObj2 != null){
			    	if(!sortObj2.toString().equals(""))setSort2(Integer.parseInt(sortObj2.toString()));
			    }
			}else{
				JSONObject jobj =  (JSONObject) data.get(0);
			    setPicurl1(jobj.get("pic_url").toString());
			    setType1(Integer.parseInt(jobj.get("type").toString()));
			    setContent1(jobj.get("content").toString());
			    Object object = jobj.get("logRequired");
			    if(object !=null){
			    	String logRequired  = object.toString();
			    	setLogRequired1(Integer.parseInt(logRequired==null||logRequired.equals("")?"0":logRequired));
			    }
			    Object sortObj = jobj.get("sort");
			    if(sortObj != null){
			    	if(!sortObj.toString().equals(""))setSort1(Integer.parseInt(sortObj.toString()));
			    }
			}
		}
	}
	public Integer getType1() {
		return type1;
	}
	public String getTypeStr1() {
		if(type1 == null) return"-";
		if(type1 == 1) return"套餐列表";
		if(type1 == 2) return"套餐详情";
		if(type1 == 3) return"外部连接";
		if(type1 == 4) return"商家详情页";
		if(type1 == 5) return"生鲜商品详情";
		if(type1 == 6) return"寻蜜客";
		return typeStr1;
	}
	public void setTypeStr1(String typeStr1) {
		this.typeStr1 = typeStr1;
	}
	
	public String getTypeStr2() {
		if(type2 == null) return"-";
		if(type2 == 1) return"套餐列表";
		if(type2 == 2) return"套餐详情";
		if(type2 == 3) return"外部连接";
		if(type2 == 4) return"商家详情页";
		if(type2 == 5) return"生鲜商品详情";
		if(type1 == 6) return"寻蜜客";
		return typeStr2;
	}
	public void setTypeStr2(String typeStr2) {
		this.typeStr2 = typeStr2;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getStatusStr() {
		if(null == status) return "-";
		if(status == 0) return "待上线";
		if(status == 1) return "已上线";
		if(status == 2) return "已下线";
		return statusStr;
	}
	public void setStatusStr(String statusStr) {
		this.statusStr = statusStr;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getCreateTimeStr() {
		if(null==createTime) return "-";
		createTimeStr = DateUtil.smartFormat(createTime);
		return createTimeStr;
	}
	public void setCreateTimeStr(String createTimeStr) {
		this.createTimeStr = createTimeStr;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getUpdateTimeStr() {
		if(null==updateTime) return "-";
		updateTimeStr = DateUtil.smartFormat(updateTime);
		return updateTimeStr;
	}
	public void setUpdateTimeStr(String updateTimeStr) {
		this.updateTimeStr = updateTimeStr;
	}
	public Integer getSort() {
		return sort;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	
	public Integer getLogRequired1() {
		return logRequired1;
	}
	public void setLogRequired1(Integer logRequired1) {
		this.logRequired1 = logRequired1;
	}
	public Integer getLogRequired2() {
		return logRequired2;
	}
	public void setLogRequired2(Integer logRequired2) {
		this.logRequired2 = logRequired2;
	}
	@Override
	public String toString() {
		return "TBanner [id=" + id + ", position=" + position
				+ ", positionStr=" + positionStr + ", bannerStyle="
				+ bannerStyle + ", bannerStyleStr=" + bannerStyleStr
				+ ", objJson=" + objJson + ", objJsonOpen=" + objJsonOpen
				+ ", picurl1=" + picurl1 + ", picurl2=" + picurl2 + ", type1="
				+ type1 + ", type2=" + type2 + ", typeStr1=" + typeStr1
				+ ", typeStr2=" + typeStr2 + ", content1=" + content1
				+ ", content2=" + content2 + ", sort1=" + sort1 + ", sort2="
				+ sort2 + ", status=" + status + ", statusStr=" + statusStr
				+ ", remarks=" + remarks + ", createTime=" + createTime
				+ ", createTimeStr=" + createTimeStr + ", updateTime="
				+ updateTime + ", updateTimeStr=" + updateTimeStr + ", sdate="
				+ sdate + ", edate=" + edate + ", ids=" + ids + ", sort="
				+ sort + ", logRequired1=" + logRequired1 + ", logRequired2="
				+ logRequired2 + ", isAll=" + isAll + ", province=" + province
				+ ", city=" + city + ", provinceStr=" + provinceStr
				+ ", cityStr=" + cityStr + "]";
	}
	

	
}
