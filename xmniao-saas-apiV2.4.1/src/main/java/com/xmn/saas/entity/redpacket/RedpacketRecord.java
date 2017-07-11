package com.xmn.saas.entity.redpacket;

import java.math.BigDecimal;
import java.util.Date;

public class RedpacketRecord {

	private Long id;

	private Integer sellerid;

	private Long redpacketId;

	private BigDecimal denomination;

	private Date recordTime;

	private Long userId;

	private String phone;

	private Integer isNewUser;

	private Integer isBinding;

	private Integer isShare;

	private Long secShareUid;
	
	private String orderNo;
	
	private Integer status;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getSellerid() {
		return sellerid;
	}

	public void setSellerid(Integer sellerid) {
		this.sellerid = sellerid;
	}

	public Long getRedpacketId() {
		return redpacketId;
	}

	public void setRedpacketId(Long redpacketId) {
		this.redpacketId = redpacketId;
	}

	public BigDecimal getDenomination() {
		return denomination;
	}

	public void setDenomination(BigDecimal denomination) {
		this.denomination = denomination;
	}

	public Date getRecordTime() {
		return recordTime;
	}

	public void setRecordTime(Date recordTime) {
		this.recordTime = recordTime;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Integer getIsNewUser() {
		return isNewUser;
	}

	public void setIsNewUser(Integer isNewUser) {
		this.isNewUser = isNewUser;
	}

	public Integer getIsBinding() {
		return isBinding;
	}

	public void setIsBinding(Integer isBinding) {
		this.isBinding = isBinding;
	}

	public Integer getIsShare() {
		return isShare;
	}

	public void setIsShare(Integer isShare) {
		this.isShare = isShare;
	}

	public Long getSecShareUid() {
		return secShareUid;
	}

	public void setSecShareUid(Long secShareUid) {
		this.secShareUid = secShareUid;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

}