package com.xmniao.xmn.core.businessman.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
import com.xmniao.xmn.core.base.BaseEntity;

public class TDebitcardOrder  extends BaseEntity  {
	
	private static final long serialVersionUID = -7280973614175692373L;

	private Integer id;

    private String orderNo;

    private Integer uid;

    private String uname;

    private String phone;

    private Integer debitcardId;

    private Integer sellerid;

    private Integer sellertype;

    private String sellername;

    private BigDecimal payCoin;

    private BigDecimal denomination;

    private Integer entrySellerid;

    private String entrySellername;

    private BigDecimal entrySelleragio;

    private Integer payState;

    private BigDecimal quota;

    private BigDecimal qQuota;

    private Integer ledgerLevel;

    private Integer quantity;

    private BigDecimal sellerIncome;

    private BigDecimal offsetSellerIncome;

    private Date createTime;

    private Date payTime;

    private String uidRelationChain;
    
    private Date startDay;
    
    private Date endDay;
    
    private BigDecimal redPacketLimit;

    private BigDecimal totalLimit;

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

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname == null ? null : uname.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public Integer getDebitcardId() {
        return debitcardId;
    }

    public void setDebitcardId(Integer debitcardId) {
        this.debitcardId = debitcardId;
    }

    public Integer getSellerid() {
        return sellerid;
    }

    public void setSellerid(Integer sellerid) {
        this.sellerid = sellerid;
    }

    public Integer getSellertype() {
        return sellertype;
    }

    public void setSellertype(Integer sellertype) {
        this.sellertype = sellertype;
    }

    public String getSellername() {
        return sellername;
    }

    public void setSellername(String sellername) {
        this.sellername = sellername == null ? null : sellername.trim();
    }

    public BigDecimal getPayCoin() {
        return payCoin;
    }

    public void setPayCoin(BigDecimal payCoin) {
        this.payCoin = payCoin;
    }

    public BigDecimal getDenomination() {
        return denomination;
    }

    public void setDenomination(BigDecimal denomination) {
        this.denomination = denomination;
    }

    public Integer getEntrySellerid() {
        return entrySellerid;
    }

    public void setEntrySellerid(Integer entrySellerid) {
        this.entrySellerid = entrySellerid;
    }

    public String getEntrySellername() {
        return entrySellername;
    }

    public void setEntrySellername(String entrySellername) {
        this.entrySellername = entrySellername == null ? null : entrySellername.trim();
    }

    public BigDecimal getEntrySelleragio() {
        return entrySelleragio;
    }

    public void setEntrySelleragio(BigDecimal entrySelleragio) {
        this.entrySelleragio = entrySelleragio;
    }

    public Integer getPayState() {
        return payState;
    }

    public void setPayState(Integer payState) {
        this.payState = payState;
    }

    public BigDecimal getQuota() {
        return quota;
    }

    public void setQuota(BigDecimal quota) {
        this.quota = quota;
    }

    public BigDecimal getqQuota() {
        return qQuota;
    }

    public void setqQuota(BigDecimal qQuota) {
        this.qQuota = qQuota;
    }

    public Integer getLedgerLevel() {
        return ledgerLevel;
    }

    public void setLedgerLevel(Integer ledgerLevel) {
        this.ledgerLevel = ledgerLevel;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getSellerIncome() {
        return sellerIncome;
    }

    public void setSellerIncome(BigDecimal sellerIncome) {
        this.sellerIncome = sellerIncome;
    }

    public BigDecimal getOffsetSellerIncome() {
        return offsetSellerIncome;
    }

    public void setOffsetSellerIncome(BigDecimal offsetSellerIncome) {
        this.offsetSellerIncome = offsetSellerIncome;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public String getUidRelationChain() {
        return uidRelationChain;
    }

    public void setUidRelationChain(String uidRelationChain) {
        this.uidRelationChain = uidRelationChain == null ? null : uidRelationChain.trim();
    }

	public Date getStartDay() {
		return startDay;
	}

	public void setStartDay(Date startDay) {
		this.startDay = startDay;
	}

	public Date getEndDay() {
		return endDay;
	}

	public void setEndDay(Date endDay) {
		this.endDay = endDay;
	}

	
	
	
	public BigDecimal getRedPacketLimit() {
		return redPacketLimit;
	}

	public void setRedPacketLimit(BigDecimal redPacketLimit) {
		this.redPacketLimit = redPacketLimit;
	}

	public BigDecimal getTotalLimit() {
		return totalLimit;
	}

	public void setTotalLimit(BigDecimal totalLimit) {
		this.totalLimit = totalLimit;
	}

	@Override
	public String toString() {
		return "TDebitcardOrder [id=" + id + ", orderNo=" + orderNo + ", uid="
				+ uid + ", uname=" + uname + ", phone=" + phone
				+ ", debitcardId=" + debitcardId + ", sellerid=" + sellerid
				+ ", sellertype=" + sellertype + ", sellername=" + sellername
				+ ", payCoin=" + payCoin + ", denomination=" + denomination
				+ ", entrySellerid=" + entrySellerid + ", entrySellername="
				+ entrySellername + ", entrySelleragio=" + entrySelleragio
				+ ", payState=" + payState + ", quota=" + quota + ", qQuota="
				+ qQuota + ", ledgerLevel=" + ledgerLevel + ", quantity="
				+ quantity + ", sellerIncome=" + sellerIncome
				+ ", offsetSellerIncome=" + offsetSellerIncome
				+ ", createTime=" + createTime + ", payTime=" + payTime
				+ ", uidRelationChain=" + uidRelationChain + ", startDay="
				+ startDay + ", endDay=" + endDay + "]";
	}
    
    
}