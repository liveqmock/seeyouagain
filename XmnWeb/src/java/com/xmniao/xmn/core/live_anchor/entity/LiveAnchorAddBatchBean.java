/**
 * 
 */
package com.xmniao.xmn.core.live_anchor.entity;

import com.xmniao.xmn.core.base.BaseEntity;

/**
 * 
 * 项目名称：XmnWeb
 * 
 * 类名称：LiveAnchorAddBatchBean
 * 
 * 类描述： 批量添加通告主播Bean
 * 
 * 创建人：  huang'tao
 * 
 * 创建时间：2017-2-23 下午3:41:49 
 * 
 * Copyright (c) 广东寻蜜鸟网络技术有限公司 
 */

public class LiveAnchorAddBatchBean extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -813182089278149924L;
	
	private Integer anchorId;// 主播用户id
	
	private String nname;// 用户昵称
	
	private Integer sex;//主播性别
	
	private Integer signType;//主播签约类型：0 兼职主播，1 签约主播，2测试账号
	
	private Integer rootRole;//来源身份  1:普通兼职  2:工会合作 3:活动合作 4:其他
	
	private Integer sequenceNo;// 排序序号(通告推荐排序)
	
	private String picUrls;//主播相册地址,多个图片已";"分隔	
	
	private String tagIds;//标签ID,多个已";"分隔
	
	private Integer classifyId;//分类ID
	
	private Integer tagId;//标签ID
	
	private Integer isCustomShare; //是否自定义分享描述

	private String customShareTitle; //分享标题

	private String customShareDesc; //分享描述

	private Integer isRobotInit;//是否配置机器人
	
	private Integer robotSetMixNums;//单次最少新增机器人个数
	
	private Integer robotSetMaxNums;//单次最多新增机器人个数
	
	private Integer robotMinNums;//初始化机器人数量
	
	private Integer robotMaxNums;//最高上限机器人数量
	
	private Integer multiple;//机器人显示倍数

	/**
	 * @return the anchorId
	 */
	public Integer getAnchorId() {
		return anchorId;
	}

	/**
	 * @param anchorId the anchorId to set
	 */
	public void setAnchorId(Integer anchorId) {
		this.anchorId = anchorId;
	}

	/**
	 * @return the nname
	 */
	public String getNname() {
		return nname;
	}

	/**
	 * @param nname the nname to set
	 */
	public void setNname(String nname) {
		this.nname = nname;
	}

	/**
	 * @return the sex
	 */
	public Integer getSex() {
		return sex;
	}
	
	

	/**
	 * @return the signType
	 */
	public Integer getSignType() {
		return signType;
	}

	/**
	 * @param signType the signType to set
	 */
	public void setSignType(Integer signType) {
		this.signType = signType;
	}

	/**
	 * @return the rootRole
	 */
	public Integer getRootRole() {
		return rootRole;
	}

	/**
	 * @param rootRole the rootRole to set
	 */
	public void setRootRole(Integer rootRole) {
		this.rootRole = rootRole;
	}

	/**
	 * @param sex the sex to set
	 */
	public void setSex(Integer sex) {
		this.sex = sex;
	}

	/**
	 * @return the sequenceNo
	 */
	public Integer getSequenceNo() {
		return sequenceNo;
	}

	/**
	 * @param sequenceNo the sequenceNo to set
	 */
	public void setSequenceNo(Integer sequenceNo) {
		this.sequenceNo = sequenceNo;
	}

	/**
	 * @return the picUrls
	 */
	public String getPicUrls() {
		return picUrls;
	}

	/**
	 * @param picUrls the picUrls to set
	 */
	public void setPicUrls(String picUrls) {
		this.picUrls = picUrls;
	}
	
	

	/**
	 * @return the tagIds
	 */
	public String getTagIds() {
		return tagIds;
	}

	/**
	 * @param tagIds the tagIds to set
	 */
	public void setTagIds(String tagIds) {
		this.tagIds = tagIds;
	}

	/**
	 * @return the classifyId
	 */
	public Integer getClassifyId() {
		return classifyId;
	}

	/**
	 * @param classifyId the classifyId to set
	 */
	public void setClassifyId(Integer classifyId) {
		this.classifyId = classifyId;
	}

	/**
	 * @return the tagId
	 */
	public Integer getTagId() {
		return tagId;
	}

	/**
	 * @param tagId the tagId to set
	 */
	public void setTagId(Integer tagId) {
		this.tagId = tagId;
	}
	
	
	public Integer getIsCustomShare() {
		return isCustomShare;
	}

	public void setIsCustomShare(Integer isCustomShare) {
		this.isCustomShare = isCustomShare;
	}

	public String getCustomShareTitle() {
		return customShareTitle;
	}

	public void setCustomShareTitle(String customShareTitle) {
		this.customShareTitle = customShareTitle == null ? null
				: customShareTitle.trim();
	}

	public String getCustomShareDesc() {
		return customShareDesc;
	}

	public void setCustomShareDesc(String customShareDesc) {
		this.customShareDesc = customShareDesc == null ? null : customShareDesc
				.trim();
	}
	
	

	/**
	 * @return the isRobotInit
	 */
	public Integer getIsRobotInit() {
		return isRobotInit;
	}

	/**
	 * @param isRobotInit the isRobotInit to set
	 */
	public void setIsRobotInit(Integer isRobotInit) {
		this.isRobotInit = isRobotInit;
	}

	/**
	 * @return the robotSetMixNums
	 */
	public Integer getRobotSetMixNums() {
		return robotSetMixNums;
	}

	/**
	 * @param robotSetMixNums the robotSetMixNums to set
	 */
	public void setRobotSetMixNums(Integer robotSetMixNums) {
		this.robotSetMixNums = robotSetMixNums;
	}

	/**
	 * @return the robotSetMaxNums
	 */
	public Integer getRobotSetMaxNums() {
		return robotSetMaxNums;
	}

	/**
	 * @param robotSetMaxNums the robotSetMaxNums to set
	 */
	public void setRobotSetMaxNums(Integer robotSetMaxNums) {
		this.robotSetMaxNums = robotSetMaxNums;
	}

	/**
	 * @return the robotMinNums
	 */
	public Integer getRobotMinNums() {
		return robotMinNums;
	}

	/**
	 * @param robotMinNums the robotMinNums to set
	 */
	public void setRobotMinNums(Integer robotMinNums) {
		this.robotMinNums = robotMinNums;
	}

	/**
	 * @return the robotMaxNums
	 */
	public Integer getRobotMaxNums() {
		return robotMaxNums;
	}

	/**
	 * @param robotMaxNums the robotMaxNums to set
	 */
	public void setRobotMaxNums(Integer robotMaxNums) {
		this.robotMaxNums = robotMaxNums;
	}
	
	

	/**
	 * @return the multiple
	 */
	public Integer getMultiple() {
		return multiple;
	}

	/**
	 * @param multiple the multiple to set
	 */
	public void setMultiple(Integer multiple) {
		this.multiple = multiple;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "LiveAnchorAddBatchBean [anchorId=" + anchorId + ", nname="
				+ nname + ", sex=" + sex + ", signType=" + signType
				+ ", rootRole=" + rootRole + ", sequenceNo=" + sequenceNo
				+ ", picUrls=" + picUrls + ", tagIds=" + tagIds
				+ ", classifyId=" + classifyId + ", tagId=" + tagId
				+ ", isCustomShare=" + isCustomShare + ", customShareTitle="
				+ customShareTitle + ", customShareDesc=" + customShareDesc
				+ ", isRobotInit=" + isRobotInit + ", robotSetMixNums="
				+ robotSetMixNums + ", robotSetMaxNums=" + robotSetMaxNums
				+ ", robotMinNums=" + robotMinNums + ", robotMaxNums="
				+ robotMaxNums + "]";
	}

	

}
