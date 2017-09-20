package com.xmniao.xmn.core.manor.entity;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

public class TManorLevelEarningRecord  extends TManorUserInfo{
    /**
	 * 花蜜收益管理
	 */
	private static final long serialVersionUID = -8305742317164109329L;

	private Long id;

    private Integer uid;

    private String phone;

    private Integer levelId;

    private String levelName;

    private Date yesterday;

    private Integer earningChanel;

    private Integer earningType;

    private Integer number;

    private Integer status;

    private Date createTime;

    private Date updateTime;

    private String description;
    
    private String statusDesc;
    
    private String createTimeStr;  //记录时间

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public Integer getLevelId() {
        return levelId;
    }

    public void setLevelId(Integer levelId) {
        this.levelId = levelId;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName == null ? null : levelName.trim();
    }

    public Date getYesterday() {
        return yesterday;
    }

    public void setYesterday(Date yesterday) {
        this.yesterday = yesterday;
    }

    public Integer getEarningChanel() {
        return earningChanel;
    }

    public void setEarningChanel(Integer earningChanel) {
        this.earningChanel = earningChanel;
    }

    public Integer getEarningType() {
        return earningType;
    }

    public void setEarningType(Integer earningType) {
        this.earningType = earningType;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

	public String getStatusDesc() {
		if (this.status == null) {
			return "-";
		}
		if (status != null) {
			if(status == 1){
				return "待入账"; //
			}else if(status == 2){
				return "已入账";//
			}else if(status == 3){
				return "入账失败"; //				
			}else if(status == 4){
				return "取消入账"; //
			}else{
				return "-";
			}
		}
		return "";
	}

	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}
	
	public String getCreateTimeStr() {
		if(createTime != null){
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(createTime);
		}else {
			return null;
		}
	}

	public void setCreateTimeStr(String createTimeStr) {
		this.createTimeStr = createTimeStr;
	}

	@Override
	public String toString() {
		return "TManorLevelEarningRecord [id=" + id + ", uid=" + uid
				+ ", phone=" + phone + ", levelId=" + levelId + ", levelName="
				+ levelName + ", yesterday=" + yesterday + ", earningChanel="
				+ earningChanel + ", earningType=" + earningType + ", number="
				+ number + ", status=" + status + ", createTime=" + createTime
				+ ", updateTime=" + updateTime + ", description=" + description
				+ ", statusDesc=" + statusDesc + ", createTimeStr="
				+ createTimeStr + "]";
	}

    
}