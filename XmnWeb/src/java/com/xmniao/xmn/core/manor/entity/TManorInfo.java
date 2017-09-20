package com.xmniao.xmn.core.manor.entity;

import java.math.BigDecimal;
import java.util.Date;


/*庄园会员管理*/
//@Table(name="b_manor_info")
public class TManorInfo extends TManorUserInfo{    
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -1063742694530133041L;

	private Integer id;

    private Integer uid;
    
    private Integer superUid;

    private Integer manorStatus;

    private Integer manorLevel;

    private Date manorDeadline;

    private Integer buildStatus;

    private Date buildTime;
    
    private Integer purchasable;

    private Date createTime;

    private Date updateTime;
    
    /*自定义字段区域*/
    private String superName;//推荐人名称
    
    private Integer lowerLevelNumber;//下级
    
    private String uidRelationChain; //庄园会员关系链
    
    private Double sunNumber;  //阳光的总数量
    
    private Double repositoryNumber;  //仓库数量
    
    private Double nectarNumber;  //花蜜的总数量
    
    private Double nectarStoreNumber; //
    
    private BigDecimal converCoin;  //累计收益鸟币
    
    private String manorStatusDesc;
    
    private String purchasableDesc;
    
	public Double getSunNumber() {
		return sunNumber;
	}

	public void setSunNumber(Double sunNumber) {
		this.sunNumber = sunNumber;
	}

	public Double getRepositoryNumber() {
		return repositoryNumber;
	}

	public void setRepositoryNumber(Double repositoryNumber) {
		this.repositoryNumber = repositoryNumber;
	}

	public Double getNectarNumber() {
		return nectarNumber;
	}

	public void setNectarNumber(Double nectarNumber) {
		this.nectarNumber = nectarNumber;
	}

	public Double getNectarStoreNumber() {
		return nectarStoreNumber;
	}

	public void setNectarStoreNumber(Double nectarStoreNumber) {
		this.nectarStoreNumber = nectarStoreNumber;
	}
	
	public BigDecimal getConverCoin() {
		return converCoin;
	}

	public void setConverCoin(BigDecimal converCoin) {
		this.converCoin = converCoin;
	}

	public Integer getSuperUid() {
		return superUid;
	}

	public void setSuperUid(Integer superUid) {
		this.superUid = superUid;
	}

	public String getSuperName() {
		return superName;
	}

	public void setSuperName(String superName) {
		this.superName = superName;
	}

	public Integer getLowerLevelNumber() {
		return lowerLevelNumber;
	}

	public void setLowerLevelNumber(Integer lowerLevelNumber) {
		this.lowerLevelNumber = lowerLevelNumber;
	}

	public String getUidRelationChain() {
		return uidRelationChain;
	}

	public void setUidRelationChain(String uidRelationChain) {
		this.uidRelationChain = uidRelationChain;
	}
	
	public String getManorStatusDesc() {
		if (this.manorStatus == null) {
			return "-";
		}
		switch (manorStatus) {
		case 1:
			return "启用";
		case 2:
			return "停用";
		}
		
		return "";
	}

	public void setManorStatusDesc(String manorStatusDesc) {
		this.manorStatusDesc = manorStatusDesc;
	}
	

	public String getPurchasableDesc() {
		if (this.purchasable == null) {
			return "-";
		}
		switch (purchasable) {
		case 1:
			return "不具备资格";
		case 2:
			return "具备资格";
		}
		
		return "";
	}

	public void setPurchasableDesc(String purchasableDesc) {
		this.purchasableDesc = purchasableDesc;
	}
	

	 /*自定义字段区域*/



	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public Integer getManorStatus() {
		return manorStatus;
	}

	public void setManorStatus(Integer manorStatus) {
		this.manorStatus = manorStatus;
	}

	public Integer getManorLevel() {
		return manorLevel;
	}

	public void setManorLevel(Integer manorLevel) {
		this.manorLevel = manorLevel;
	}

	public Date getManorDeadline() {
		return manorDeadline;
	}

	public void setManorDeadline(Date manorDeadline) {
		this.manorDeadline = manorDeadline;
	}

	public Integer getBuildStatus() {
		return buildStatus;
	}

	public void setBuildStatus(Integer buildStatus) {
		this.buildStatus = buildStatus;
	}

	public Date getBuildTime() {
		return buildTime;
	}

	public void setBuildTime(Date buildTime) {
		this.buildTime = buildTime;
	}
	

	public Integer getPurchasable() {
		return purchasable;
	}

	public void setPurchasable(Integer purchasable) {
		this.purchasable = purchasable;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	@Override
	public String toString() {
		return "TManorInfo [id=" + id + ", uid=" + uid + ", superUid="
				+ superUid + ", manorStatus=" + manorStatus + ", manorLevel="
				+ manorLevel + ", manorDeadline=" + manorDeadline
				+ ", buildStatus=" + buildStatus + ", buildTime=" + buildTime
				+ ", purchasable=" + purchasable + ", createTime=" + createTime
				+ ", updateTime=" + updateTime + ", superName=" + superName
				+ ", lowerLevelNumber=" + lowerLevelNumber
				+ ", uidRelationChain=" + uidRelationChain + ", sunNumber="
				+ sunNumber + ", repositoryNumber=" + repositoryNumber
				+ ", nectarNumber=" + nectarNumber + ", nectarStoreNumber="
				+ nectarStoreNumber + ", converCoin=" + converCoin
				+ ", manorStatusDesc=" + manorStatusDesc + ", purchasableDesc="
				+ purchasableDesc + "]";
	}





}