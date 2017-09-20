package com.xmniao.xmn.core.reward_dividends.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
import com.xmniao.xmn.core.base.BaseEntity;

public class TLivePrivilege extends BaseEntity{
    /**
	 * 
	 */
	private static final long serialVersionUID = -8338003372131424441L;

	private Integer id;

    private String orderNo;

    private Integer objectOriented;

    private Integer uid;

    private BigDecimal payment;

    private Date createTime;

    private Date updateTime;

    private BigDecimal totalLedgerAmount;

    private BigDecimal consumeLedger;

    private BigDecimal privilegeLedger;

    private BigDecimal currentConsumeLedger;

    private BigDecimal currentPrivilegeLedger;

    private BigDecimal currentDividendLedger;

    private BigDecimal currentDividendCoinLedger;

    private Integer ledgerLevel;

    private Integer debitcardId;

    private BigDecimal quota;

    private Integer version;

    private Integer hasRecomend;

    private Integer hasDividend;

    private Integer periodExcitation;

    private Integer curPeriodExcitation;
    
    private String excitationProject;
    
    /* 自定义区域*/
    
    private Integer rechargeId;
    
    private String nickname;//主播用户昵称
    
    private String name;//真实姓名

    private String phone;//手机号码
    
    private String rankName;//级别名称
    
    private String projectName;//奖励方案 A、B
    
    private Date startRefundTime;
    
    private Date endRefundTime;
    
    private Integer totalRecommendLive; //主播推荐总名额
    
    private String totalRecommendLiveStr; //主播推荐总名额
    
    private Integer recommendLive; //主播推荐名额
    
    private BigDecimal liveProfitAmount;  //获得主播收益
    
    private Integer totalRecommendSeller; //商户推荐总名额
    
    private String totalRecommendSellerStr; //商户推荐总名额
    
    private Integer recommendSeller; //商户推荐名额
    
    private BigDecimal sellerProfitAmount;  //获得商户收益
    
    private BigDecimal  totalRechargeAmount;  //累计充值总额
    
    private String excitationProjectStr;
    
    private String superiorStr;  //上级
    
    private String topLevelStr;  // 间接上级
    
    private String restrictive;
    
    private BigDecimal limitBalance;  //限制鸟币

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo == null ? null : orderNo.trim();
    }

    public Integer getObjectOriented() {
        return objectOriented;
    }

    public void setObjectOriented(Integer objectOriented) {
        this.objectOriented = objectOriented;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public BigDecimal getPayment() {
        return payment;
    }

    public void setPayment(BigDecimal payment) {
        this.payment = payment;
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

    public BigDecimal getTotalLedgerAmount() {
        return totalLedgerAmount;
    }

    public void setTotalLedgerAmount(BigDecimal totalLedgerAmount) {
        this.totalLedgerAmount = totalLedgerAmount;
    }

    public BigDecimal getConsumeLedger() {
        return consumeLedger;
    }

    public void setConsumeLedger(BigDecimal consumeLedger) {
        this.consumeLedger = consumeLedger;
    }

    public BigDecimal getPrivilegeLedger() {
        return privilegeLedger;
    }

    public void setPrivilegeLedger(BigDecimal privilegeLedger) {
        this.privilegeLedger = privilegeLedger;
    }

    public BigDecimal getCurrentConsumeLedger() {
        return currentConsumeLedger;
    }

    public void setCurrentConsumeLedger(BigDecimal currentConsumeLedger) {
        this.currentConsumeLedger = currentConsumeLedger;
    }

    public BigDecimal getCurrentPrivilegeLedger() {
        return currentPrivilegeLedger;
    }

    public void setCurrentPrivilegeLedger(BigDecimal currentPrivilegeLedger) {
        this.currentPrivilegeLedger = currentPrivilegeLedger;
    }

    public BigDecimal getCurrentDividendLedger() {
        return currentDividendLedger;
    }

    public void setCurrentDividendLedger(BigDecimal currentDividendLedger) {
        this.currentDividendLedger = currentDividendLedger;
    }

    public BigDecimal getCurrentDividendCoinLedger() {
        return currentDividendCoinLedger;
    }

    public void setCurrentDividendCoinLedger(BigDecimal currentDividendCoinLedger) {
        this.currentDividendCoinLedger = currentDividendCoinLedger;
    }

    public Integer getLedgerLevel() {
        return ledgerLevel;
    }

    public void setLedgerLevel(Integer ledgerLevel) {
        this.ledgerLevel = ledgerLevel;
    }

    public Integer getDebitcardId() {
        return debitcardId;
    }

    public void setDebitcardId(Integer debitcardId) {
        this.debitcardId = debitcardId;
    }

    public BigDecimal getQuota() {
        return quota;
    }

    public void setQuota(BigDecimal quota) {
        this.quota = quota;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Integer getHasRecomend() {
        return hasRecomend;
    }

    public void setHasRecomend(Integer hasRecomend) {
        this.hasRecomend = hasRecomend;
    }

    public Integer getHasDividend() {
        return hasDividend;
    }

    public void setHasDividend(Integer hasDividend) {
        this.hasDividend = hasDividend;
    }

    public Integer getPeriodExcitation() {
        return periodExcitation;
    }

    public void setPeriodExcitation(Integer periodExcitation) {
        this.periodExcitation = periodExcitation;
    }

    public Integer getCurPeriodExcitation() {
        return curPeriodExcitation;
    }

    public void setCurPeriodExcitation(Integer curPeriodExcitation) {
        this.curPeriodExcitation = curPeriodExcitation;
    }
    

	public String getExcitationProject() {
		return excitationProject;
	}

	public void setExcitationProject(String excitationProject) {
		this.excitationProject = excitationProject;
	}
	
	public Integer getRechargeId() {
		return rechargeId;
	}

	public void setRechargeId(Integer rechargeId) {
		this.rechargeId = rechargeId;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getRankName() {
		return rankName;
	}

	public void setRankName(String rankName) {
		this.rankName = rankName;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	@JSONField(format = "yyyy-MM-dd")
	public Date getStartRefundTime() {
		return startRefundTime;
	}

	public void setStartRefundTime(Date startRefundTime) {
		this.startRefundTime = startRefundTime;
	}

	@JSONField(format = "yyyy-MM-dd")
	// @JSONField(format = "yyyy-MM-dd HH:mm:ss")
	public Date getEndRefundTime() {
		return endRefundTime;
	}

	public void setEndRefundTime(Date endRefundTime) {
		this.endRefundTime = endRefundTime;
	}
	
	public Integer getTotalRecommendLive() {
		if (this.payment == null) {
			return 0;
		}
		
		if (payment.compareTo(new BigDecimal(9000))==0) {
			return 2;
		} else if (payment.compareTo(new BigDecimal(18000))==0) {
			return 5;
		} else if (payment.compareTo(new BigDecimal(36000))==0) {
			return 10;
		} else if (payment.compareTo(new BigDecimal(108000))==0) {
			return 30;			
		} else {
			return 0;
		}
	}

	public void setTotalRecommendLive(Integer totalRecommendLive) {
		this.totalRecommendLive = totalRecommendLive;
	}

	public Integer getRecommendLive() {
		return recommendLive;
	}

	public void setRecommendLive(Integer recommendLive) {
		this.recommendLive = recommendLive;
	}

	public BigDecimal getLiveProfitAmount() {  //获得主播收益
		return liveProfitAmount;
	}

	public void setLiveProfitAmount(BigDecimal liveProfitAmount) {
		this.liveProfitAmount = liveProfitAmount;
	}

	public Integer getTotalRecommendSeller() {
		if (this.payment == null) {
			return 0;
		}
		if (payment.compareTo(new BigDecimal(9000))==0) {
			return 10;
		} else if (payment.compareTo(new BigDecimal(18000))==0) {
			return 25;
		} else if (payment.compareTo(new BigDecimal(36000))==0) {
			return 50;
		} else if (payment.compareTo(new BigDecimal(108000))==0) {
			return 150;			
		} else {
			return 0;
		}
	}

	public void setTotalRecommendSeller(Integer totalRecommendSeller) {
		this.totalRecommendSeller = totalRecommendSeller;
	}
	
	public Integer getRecommendSeller() {
		return recommendSeller;
	}

	public void setRecommendSeller(Integer recommendSeller) {
		this.recommendSeller = recommendSeller;
	}

	public String getTotalRecommendLiveStr() {
		return totalRecommendLiveStr;
	}

	public void setTotalRecommendLiveStr(String totalRecommendLiveStr) {
		this.totalRecommendLiveStr = totalRecommendLiveStr;
	}
	

	public String getTotalRecommendSellerStr() {
		return totalRecommendSellerStr;
	}

	public void setTotalRecommendSellerStr(String totalRecommendSellerStr) {
		this.totalRecommendSellerStr = totalRecommendSellerStr;
	}


	public BigDecimal getSellerProfitAmount() {  //获得商户收益
		return sellerProfitAmount;
	}

	public void setSellerProfitAmount(BigDecimal sellerProfitAmount) {
		this.sellerProfitAmount = sellerProfitAmount;
	}

	public BigDecimal getTotalRechargeAmount() {
		return totalRechargeAmount;
	}

	public void setTotalRechargeAmount(BigDecimal totalRechargeAmount) {
		this.totalRechargeAmount = totalRechargeAmount;
	}

	public String getExcitationProjectStr() {
		if (this.excitationProject == null) {
			return "-";
		}
		if ("A".equals(excitationProject)) {// 收支渠道
			return "A模式";
		} else if ("B".equals(excitationProject)) {
			return "B模式";
		} else {
			return "-";
		}
	}

	public void setExcitationProjectStr(String excitationProjectStr) {
		this.excitationProjectStr = excitationProjectStr;
	}

	public String getSuperiorStr() {
		return superiorStr;
	}

	public void setSuperiorStr(String superiorStr) {
		this.superiorStr = superiorStr;
	}

	public String getTopLevelStr() {
		return topLevelStr;
	}

	public void setTopLevelStr(String topLevelStr) {
		this.topLevelStr = topLevelStr;
	}

	public String getRestrictive() {
		return restrictive;
	}

	public void setRestrictive(String restrictive) {
		this.restrictive = restrictive;
	}

	public BigDecimal getLimitBalance() {
		return limitBalance;
	}

	public void setLimitBalance(BigDecimal limitBalance) {
		this.limitBalance = limitBalance;
	}

	@Override
	public String toString() {
		return "TLivePrivilege [id=" + id + ", orderNo=" + orderNo
				+ ", objectOriented=" + objectOriented + ", uid=" + uid
				+ ", payment=" + payment + ", createTime=" + createTime
				+ ", updateTime=" + updateTime + ", totalLedgerAmount="
				+ totalLedgerAmount + ", consumeLedger=" + consumeLedger
				+ ", privilegeLedger=" + privilegeLedger
				+ ", currentConsumeLedger=" + currentConsumeLedger
				+ ", currentPrivilegeLedger=" + currentPrivilegeLedger
				+ ", currentDividendLedger=" + currentDividendLedger
				+ ", currentDividendCoinLedger=" + currentDividendCoinLedger
				+ ", ledgerLevel=" + ledgerLevel + ", debitcardId="
				+ debitcardId + ", quota=" + quota + ", version=" + version
				+ ", hasRecomend=" + hasRecomend + ", hasDividend="
				+ hasDividend + ", periodExcitation=" + periodExcitation
				+ ", curPeriodExcitation=" + curPeriodExcitation
				+ ", excitationProject=" + excitationProject + ", rechargeId="
				+ rechargeId + ", nickname=" + nickname + ", name=" + name
				+ ", phone=" + phone + ", rankName=" + rankName
				+ ", projectName=" + projectName + ", startRefundTime="
				+ startRefundTime + ", endRefundTime=" + endRefundTime
				+ ", totalRecommendLive=" + totalRecommendLive
				+ ", totalRecommendLiveStr=" + totalRecommendLiveStr
				+ ", recommendLive=" + recommendLive + ", liveProfitAmount="
				+ liveProfitAmount + ", totalRecommendSeller="
				+ totalRecommendSeller + ", totalRecommendSellerStr="
				+ totalRecommendSellerStr + ", recommendSeller="
				+ recommendSeller + ", sellerProfitAmount="
				+ sellerProfitAmount + ", totalRechargeAmount="
				+ totalRechargeAmount + ", excitationProjectStr="
				+ excitationProjectStr + ", superiorStr=" + superiorStr
				+ ", topLevelStr=" + topLevelStr + ", restrictive="
				+ restrictive + ", limitBalance=" + limitBalance + "]";
	}

    
}