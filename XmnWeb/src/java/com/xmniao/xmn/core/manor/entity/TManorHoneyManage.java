package com.xmniao.xmn.core.manor.entity;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

public class TManorHoneyManage extends TManorUserInfo{
    /**
	 * 商店兑换管理-兑换管理
	 */
	private static final long serialVersionUID = 6573985205148336843L;

	private Integer id;

    private Double rate;

    private Integer sellPotLimit;

    private Integer potHoney;
    
    private String sunshineCouponJson;
    
    private List<TManorSunshineCoupon> manorSunshineCouponList;
    
    
    //　自定义字段
    private Integer uid;
    
    private Integer channel;  //收支渠道
    
    private BigDecimal num;  //兑换阳光数量
    
    private String outlay;  //兑换消耗鸟币/花蜜(滴)量
    
    private String income;  //获取花朵/鸟币数量
    
    private Date createTime;  //记录时间
    
    private String createTimeStr;  //记录时间
    
    private String channelDesc;  //收支渠道
    
    private List<TManorFlowerConvert> manorFlowerConvertList;  //黄金庄园花朵套餐信息
    
    public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public Integer getChannel() {
		return channel;
	}

	public void setChannel(Integer channel) {
		this.channel = channel;
	}

	public BigDecimal getNum() {
		return num;
	}

	public void setNum(BigDecimal num) {
		this.num = num;
	}


	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	public String getChannelDesc() {
		if (this.channel == null) {
			return "-";
		}
		if (channel == 34) {// 收支渠道
			return "兑换蜜罐获取鸟币";
		} else if (channel == 35) {
			return "兑换鸟币购买花朵";
		} else {
			return "";
		}
	}

	public void setChannelDesc(String channelDesc) {
		this.channelDesc = channelDesc;
	}

	public List<TManorFlowerConvert> getManorFlowerConvertList() {
		return manorFlowerConvertList;
	}

	public void setManorFlowerConvertList(
			List<TManorFlowerConvert> manorFlowerConvertList) {
		this.manorFlowerConvertList = manorFlowerConvertList;
	}
    
	public String getOutlay() {
		return outlay;
	}

	public void setOutlay(String outlay) {
		this.outlay = outlay;
	}

	public String getIncome() {
		return income;
	}

	public void setIncome(String income) {
		this.income = income;
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
	
    //　自定义字段
  


	public List<TManorSunshineCoupon> getManorSunshineCouponList() {
		return manorSunshineCouponList;
	}

	public void setManorSunshineCouponList(
			List<TManorSunshineCoupon> manorSunshineCouponList) {
		this.manorSunshineCouponList = manorSunshineCouponList;
	}

	public String getSunshineCouponJson() {
		return sunshineCouponJson;
	}

	public void setSunshineCouponJson(String sunshineCouponJson) {
		this.sunshineCouponJson = sunshineCouponJson;
	}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public Integer getSellPotLimit() {
        return sellPotLimit;
    }

    public void setSellPotLimit(Integer sellPotLimit) {
        this.sellPotLimit = sellPotLimit;
    }

    public Integer getPotHoney() {
        return potHoney;
    }

    public void setPotHoney(Integer potHoney) {
        this.potHoney = potHoney;
    }

	@Override
	public String toString() {
		return "TManorHoneyManage [id=" + id + ", rate=" + rate
				+ ", sellPotLimit=" + sellPotLimit + ", potHoney=" + potHoney
				+ ", sunshineCouponJson=" + sunshineCouponJson
				+ ", manorSunshineCouponList=" + manorSunshineCouponList
				+ ", uid=" + uid + ", channel=" + channel + ", num=" + num
				+ ", outlay=" + outlay + ", income=" + income + ", createTime="
				+ createTime + ", createTimeStr=" + createTimeStr
				+ ", channelDesc=" + channelDesc + ", manorFlowerConvertList="
				+ manorFlowerConvertList + "]";
	}

    
    
}