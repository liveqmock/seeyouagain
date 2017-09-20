package com.xmniao.xmn.core.live_anchor.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.xmniao.xmn.core.base.BaseEntity;
import com.xmniao.xmn.core.util.NumberUtil;

public class BLiver extends BaseEntity {
    /**
	 * 
	 */
	private static final long serialVersionUID = 46849609848204876L;

	private Integer id;//主播 用户id

    private Integer uid;//寻蜜鸟 会员id
    
    private String uidStr;//11位uid的字符串,不足11位在uid前以"0"填充

    private Integer utype;//直播用户类型： 1 主播 2 普通用户

    private String anchorRoomNo;//主播房间编号
    
    private BigDecimal height;//主播身高
    
    private String heightStr;//主播身高

    private BigDecimal weight;//主播体重
    
    private String weightStr;//主播体重

    private String threeDimensional;//主播三围
    
    private String selfComment;//主播个人描述
    
    private BigDecimal ledgerRatio;////主播分账比例,0.75
    
    private String ledgerRatioSign;//主播分账比例,75%
    
    private BigDecimal saleCouponRatio;////粉丝券销售分账比例,0.75
    
    private String saleCouponRatioSign;//粉丝券销售分账比例,75%
    
    private BigDecimal useCouponRation;////粉丝券使用分账比例,0.75
    
    private String useCouponRationSign;//粉丝券使用分账比例,75%

    private Integer rankId;//等级ID

    private Integer rankNo;//等级

    private String achievement;//用户当前等级头衔名称

    private Integer currentExpe;//用户当前持有经验

    private Integer concernNums;//关注用户总数

    private Integer concernedNums;//被关注总数

    private Integer giveGiftsNums;//送出礼物总数

    private Integer givedGiftsNums;//接收礼物总数

    private Integer praisedNums;//被点赞总数

    private Float viewDuration;//观看直播总时长

    private Float liveDuration;//进行直播总时长(second)

    private String liveDurationStr;//执行直播总时长(n小时m分钟)
    
    private Boolean status;//用户状态 1启用   0停用
    
    private Integer sellerLookStatus;//商家是否可见： 1是   0否

    private Boolean msgStatus;//消息提醒状态  1开启   0未开启

//    private Boolean tlsSetStatus;//腾讯云资料设置状态 ： 1 已设置   0 未设置

    private Date createTime;//创建时间

    private Date updateTime;//更新时间
    
    private String bursPassword;//寻蜜鸟会员密码
    
    private String isBinding;//是否绑定寻蜜鸟会员信息
    
    private String role;//角色   0 无寻蜜鸟会员和直播观众信息  1 存在寻蜜鸟会员信息，不存在直播观众信息  2存在会员和直播观众信息
    
    private String changeToAnchor;//将直播普通用户设为主播 Y 是, N 否
    
    private BigDecimal liveMinute;//当月直播时长(分钟)
    
    private String liveTimeStr;//直播时长(格式： 20小时50分)
    
    private BigDecimal totalLiveMinute;//总直播时长(分钟)
    
    private String totalLiveTimeStr;//总直播时长
    
    private Integer playbackNum;//回放视频数
    
    private Integer recordNum;//当月通告记录数
    
    private Integer totalRecordNum;//总通告记录数
    
    private BigDecimal balance;//鸟蛋余额
    
    private BigDecimal birdEggTotal;//鸟蛋总额
    
    
  //================以下属性,存b_urs 或b_urs_info表===
    private String name;//主播真实姓名

    private String nickname;//主播用户昵称

    private String avatar;//头像
    
    private Integer sex;//性别
    
    private String password;///密码

    private String phone;//手机号码

    private String idcard;//身份证
    
    private String birthday;//出生日期

    private String weixin;//微信号

    private Integer age;//年龄
    
    private String showValue;
    
    private String filterVal;//当前会员的uid,用于排除等下级
    
    private String uidViewJunior;//点击会员列表查看下级,传入的uid,用于查询下级
    
	//================================================ 
    
    
    private Long imageCount;//主播相册照片数量
    
    private Integer androidNum;//生成的机器人数量
    
    private Integer isInside;//内部账号标志,默认 0 否，1 是
    
    private String groupId;//腾讯云群组号
    
    private String wechatPic;//微信群二维码图片
    
    private Integer sortVal;//主播推荐排序
    
    private String introduceMv;//主播介绍视频
    
    private String officeName;//主播来源
    
    private Integer enterpriseUid;//企业级uid
    
    private String enterpriseNname;//企业级昵称
    
    private String uidRelationChain;//会员关系链，规则:每一级用'',''隔开，包含自身，每一级均为11位uid的字符串，不足11位在uid前以''0''填充。如当前uid为112,其关系链为0000001000,0000002000,00000000112'

    private String uidRelationChainNname;//会员昵称关系链，多个以逗号分隔，顺序与uid_relation_chain保持一致
    
    private Integer uidRelationChainLevel;//会员在会员关系链中的级别，顶级为1，下级依次+1。如：若当前会员的上级为10级，则当前会员为11级

    private Long fansRankNo;//粉丝等级,级别越高,值越大

    private String fansRankName;//粉丝等级名称

    private Integer fansRankId;//粉丝等级 ID

    private String referrerType;//推荐人类型 ：001 企业推荐人；默认 002 普通推荐人
    
    private String referrerTypeStr;//推荐人类型字符串
    
    private String topLevel;//上上级nname
    
    private String topLevelUid;//上上级uid
    
    private String topLevelStr;//上上级,格式：[uid]nname
    
    private String superior;//上级nname
    
    private String superiorUid;//上级Uid
    
    private String superiorStr;//上级,格式：[uid]nname
    
    //企业级推荐人统计字段 start===============================
    
    private long juniors;//下线人数
    
    private BigDecimal juniorRecharge;//下线累计充值
    
    private BigDecimal juniorReward;//下线累计打赏
    
    private BigDecimal juniorConsume;//下线消费累计金额
    
    private String startTime;//统计开始时间
    
    private String endTime;//统计结束时间
    
    //企业级推荐人统计字段 end===============================
    
    //会员分账统计信息字段start======================
    private BigDecimal orderAmount;//充值总金额

    private BigDecimal comsumAmount;//总打赏鸟豆

    private BigDecimal comsumLedger;//总可返打赏面额

    private BigDecimal expectedPriviledgeLedger;//预计还可返的奖励面额

    private BigDecimal currentConsumeLedger;//当前已返打赏面额

    private BigDecimal currentDividendLedger;//当前已返分红红包金额

    private BigDecimal currentRecommendLedger;//当前已返推荐面额
    //会员分账统计信息字段end======================
    
    //直播钱包字段start===
    private String restrictive;
    
    private String restrictiveStr;
    
    private BigDecimal limitBalance;
    
    private BigDecimal commision;//鸟豆余额
    
    private BigDecimal zbalance;//鸟币余额
    //直播钱包字段end===
    
    private Integer operationType;//操作类型 registerCommon
    
    private String superiorIdChosen;//注册会员选择的上级id
    
    private List<Object> ids;//直播用户ids
    
    private Integer redPacketAuthority;//会员红包奖励权限 0 正常可获取 1 不可获取 2 可获取但不可入账
    
    private String redPacketAuthorityStr;//会员红包奖励权限 0 正常可获取 1 不可获取 2 可获取但不可入账

    private Long birthdayData;
    
    private Integer userPlatform;//会员平台 1 寻蜜鸟 2 中脉
    
    private Integer objectOriented;//会员类型 1 VIP 2 商家 3 直销
    
    private String objectOrientedVal;//会员类型VAL
    
    private Integer dividendsRole;//分红角色 1内购 2外购
    
    private String userDescription;//会员描述
    
    private Integer referrerSellerid;//推荐商家ID
    
    private BigDecimal juniorLimitRatio;//(VIP)红包下线充值限领比例
    
    private Integer signType;//主播签约类型：0 兼职主播，1 签约主播，2测试账号
    
    private Integer rootRole;//来源身份  1:普通兼职  2:工会合作 3:活动合作 4:其他
    
    private String anchorType;//主播类型
    
    private Integer levelId;//主播工资等级
    
    private String levelName;//主播工资等级名称
    
    private String picUrls;//相册字符串数据,多个相册已","分隔

    private String remark;//来源备注(当root_role兼职类型为4其他时生效)

    private Integer province;//省

    private Integer city;//市

    private Integer indirectUid;//V客间接上级，用于V客团队奖

    private Integer indirectId;//V客间接上级，直播会员id

    private String indirectNname;//V客业绩归属人
    
    /* 自定义字段区域*/
    private String performExperience;
    
    private String indirectUidAndName;//间接上级UID|name

    private String experienceResume;
    
    private String interests;
    
    private String styleLabel;
    
    private String styleLabelDesc;
    
    private List<BLiveAnchorImage> liveAnchorImageList;
    
    private Integer orderStatus;//可接单状态
    
    private String orderStatusStr;//接单权限
    
    private Integer verUid;//所属V客UID
    
    private String verPhone;//所属V客手机号
    
    private String verName;//所属V客名称
    
    private Integer realFansNum;//真实粉丝数量
    
    private Integer robotFansNum;//机器人粉丝数量
    
    private Integer totalFansNum;//显示总粉丝数
    
    
    
    
    /**
	 * @return the realFansNum
	 */
	public Integer getRealFansNum() {
		return realFansNum;
	}

	/**
	 * @param realFansNum the realFansNum to set
	 */
	public void setRealFansNum(Integer realFansNum) {
		this.realFansNum = realFansNum;
	}

	/**
	 * @return the robotFansNum
	 */
	public Integer getRobotFansNum() {
		return robotFansNum;
	}

	/**
	 * @param robotFansNum the robotFansNum to set
	 */
	public void setRobotFansNum(Integer robotFansNum) {
		this.robotFansNum = robotFansNum;
	}

	/**
	 * @return the totalFansNum
	 */
	public Integer getTotalFansNum() {
		int realFans=realFansNum == null?0:realFansNum;
		int robotFans = robotFansNum == null ? 0:robotFansNum;
		return totalFansNum = realFans + robotFans;
	}

	/**
	 * @param totalFansNum the totalFansNum to set
	 */
	public void setTotalFansNum(Integer totalFansNum) {
		this.totalFansNum = totalFansNum;
	}

	/**
	 * @return the verUid
	 */
	public Integer getVerUid() {
		return verUid;
	}

	/**
	 * @param verUid the verUid to set
	 */
	public void setVerUid(Integer verUid) {
		this.verUid = verUid;
	}

	/**
	 * @return the verPhone
	 */
	public String getVerPhone() {
		return verPhone;
	}

	/**
	 * @param verPhone the verPhone to set
	 */
	public void setVerPhone(String verPhone) {
		this.verPhone = verPhone;
	}

	/**
	 * @return the verName
	 */
	public String getVerName() {
		return verName;
	}

	/**
	 * @param verName the verName to set
	 */
	public void setVerName(String verName) {
		this.verName = verName;
	}

	/**
	 * @return the orderStatusStr
	 */
	public String getOrderStatusStr() {
		if(orderStatus==null){
			return null;
		}
		switch (orderStatus) {
		case 0:
			orderStatusStr="不可接单";
			break;
		case 1:
			orderStatusStr="可接单";
			break;

		default:
			orderStatusStr="不可接单";
			break;
		}
		
		return orderStatusStr;
	}

	/**
	 * @param orderStatusStr the orderStatusStr to set
	 */
	public void setOrderStatusStr(String orderStatusStr) {
		this.orderStatusStr = orderStatusStr;
	}

	/**
	 * @return the orderStatus
	 */
	public Integer getOrderStatus() {
		return orderStatus;
	}

	/**
	 * @param orderStatus the orderStatus to set
	 */
	public void setOrderStatus(Integer orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getPerformExperience() {
		return performExperience;
	}

	public void setPerformExperience(String performExperience) {
		this.performExperience = performExperience;
	}

	public String getExperienceResume() {
		return experienceResume;
	}

	public void setExperienceResume(String experienceResume) {
		this.experienceResume = experienceResume;
	}

	public String getInterests() {
		return interests;
	}

	public void setInterests(String interests) {
		this.interests = interests;
	}

	public String getStyleLabel() {
		return styleLabel;
	}

	public void setStyleLabel(String styleLabel) {
		this.styleLabel = styleLabel;
	}
	
	public String getStyleLabelDesc() {
		return styleLabelDesc;
	}

	public void setStyleLabelDesc(String styleLabelDesc) {
		this.styleLabelDesc = styleLabelDesc;
	}

	public List<BLiveAnchorImage> getLiveAnchorImageList() {
		return liveAnchorImageList;
	}

	public void setLiveAnchorImageList(List<BLiveAnchorImage> liveAnchorImageList) {
		this.liveAnchorImageList = liveAnchorImageList;
	}
	
	/* 自定义字段区域*/

    
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
    
    

    /**
	 * @return the uidStr
	 */
	public String getUidStr() {
		return uidStr;
	}

	/**
	 * @param uidStr the uidStr to set
	 */
	public void setUidStr(String uidStr) {
		this.uidStr = uidStr;
	}

	public Integer getUtype() {
        return utype;
    }

    public void setUtype(Integer utype) {
        this.utype = utype;
    }

    

	/**
	 * @return the anchorRoomNo
	 */
	public String getAnchorRoomNo() {
		return anchorRoomNo;
	}

	/**
	 * @param anchorRoomNo the anchorRoomNo to set
	 */
	public void setAnchorRoomNo(String anchorRoomNo) {
		this.anchorRoomNo = anchorRoomNo;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}


	/**
	 * @return the nickname
	 */
	public String getNickname() {
		return nickname;
	}

	/**
	 * @param nickname the nickname to set
	 */
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	/**
	 * @return the avatar
	 */
	public String getAvatar() {
		return avatar;
	}

	/**
	 * @param avatar the avatar to set
	 */
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	/**
	 * @return the sex
	 */
	public Integer getSex() {
		return sex;
	}

	/**
	 * @param sex the sex to set
	 */
	public void setSex(Integer sex) {
		this.sex = sex;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the idcard
	 */
	public String getIdcard() {
		return idcard;
	}

	/**
	 * @param idcard the idcard to set
	 */
	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}

	/**
	 * @return the birthday
	 */
	public String getBirthday() {
		return birthday;
	}

	/**
	 * @param birthday the birthday to set
	 */
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}


	/**
	 * @return the weixin
	 */
	public String getWeixin() {
		return weixin;
	}

	/**
	 * @param weixin the weixin to set
	 */
	public void setWeixin(String weixin) {
		this.weixin = weixin;
	}

	/**
	 * @return the age
	 */
	public Integer getAge() {
		return age;
	}

	/**
	 * @param age the age to set
	 */
	public void setAge(Integer age) {
		this.age = age;
	}
	
	
	

	/**
	 * @return the showValue
	 */
	public String getShowValue() {
		return showValue;
	}

	/**
	 * @param showValue the showValue to set
	 */
	public void setShowValue(String showValue) {
		this.showValue = showValue;
	}
	
	

	/**
	 * @return the filterVal
	 */
	public String getFilterVal() {
		return filterVal;
	}

	/**
	 * @param filterVal the filterVal to set
	 */
	public void setFilterVal(String filterVal) {
		this.filterVal = filterVal;
	}
	
	

	/**
	 * @return the uidViewJunior
	 */
	public String getUidViewJunior() {
		return uidViewJunior;
	}

	/**
	 * @param uidViewJunior the uidViewJunior to set
	 */
	public void setUidViewJunior(String uidViewJunior) {
		this.uidViewJunior = uidViewJunior;
	}

	public BigDecimal getHeight() {
        return height;
    }

    public void setHeight(BigDecimal height) {
        this.height = height;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }
    
    
    

    /**
	 * @return the heightStr
	 */
	public String getHeightStr() {
		return NumberUtil.getNumberFixedpoint(height, 0);
	}

	/**
	 * @param heightStr the heightStr to set
	 */
	public void setHeightStr(String heightStr) {
		this.heightStr = heightStr;
	}

	

	/**
	 * @return the weightStr
	 */
	public String getWeightStr() {
		return NumberUtil.getNumberFixedpoint(weight, 0);
	}

	/**
	 * @param weightStr the weightStr to set
	 */
	public void setWeightStr(String weightStr) {
		this.weightStr = weightStr;
	}

	/**
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * @param phone the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getThreeDimensional() {
        return threeDimensional;
    }

    public void setThreeDimensional(String threeDimensional) {
        this.threeDimensional = threeDimensional == null ? null : threeDimensional.trim();
    }

    public BigDecimal getLedgerRatio() {
        return ledgerRatio;
    }

    public void setLedgerRatio(BigDecimal ledgerRatio) {
        this.ledgerRatio = ledgerRatio;
    }

    public Integer getRankId() {
        return rankId;
    }

    public void setRankId(Integer rankId) {
        this.rankId = rankId;
    }

    public Integer getRankNo() {
        return rankNo;
    }

    public void setRankNo(Integer rankNo) {
        this.rankNo = rankNo;
    }

    public String getAchievement() {
        return achievement;
    }

    public void setAchievement(String achievement) {
        this.achievement = achievement == null ? null : achievement.trim();
    }

    public Integer getCurrentExpe() {
        return currentExpe;
    }

    public void setCurrentExpe(Integer currentExpe) {
        this.currentExpe = currentExpe;
    }

    public Integer getConcernNums() {
        return concernNums;
    }

    public void setConcernNums(Integer concernNums) {
        this.concernNums = concernNums;
    }

    public Integer getConcernedNums() {
        return concernedNums;
    }

    public void setConcernedNums(Integer concernedNums) {
        this.concernedNums = concernedNums;
    }

    public Integer getGiveGiftsNums() {
        return giveGiftsNums;
    }

    public void setGiveGiftsNums(Integer giveGiftsNums) {
        this.giveGiftsNums = giveGiftsNums;
    }

    public Integer getGivedGiftsNums() {
        return givedGiftsNums;
    }

    public void setGivedGiftsNums(Integer givedGiftsNums) {
        this.givedGiftsNums = givedGiftsNums;
    }

    public Integer getPraisedNums() {
        return praisedNums;
    }

    public void setPraisedNums(Integer praisedNums) {
        this.praisedNums = praisedNums;
    }

    public Float getViewDuration() {
        return viewDuration;
    }

    public void setViewDuration(Float viewDuration) {
        this.viewDuration = viewDuration;
    }

    public Float getLiveDuration() {
        return liveDuration;
    }

    public void setLiveDuration(Float liveDuration) {
        this.liveDuration = liveDuration;
    }
    
    

    /**
	 * @return the liveDurationStr
	 */
	public String getLiveDurationStr() {
		BigDecimal liveDurationSecond=new BigDecimal(liveDuration==null?0d:liveDuration.doubleValue());
		long hours=0;
		Integer minute=0;
		hours = liveDurationSecond.divideToIntegralValue(new BigDecimal(3600)).longValue();
//		minute = liveDurationSecond.subtract(new BigDecimal(hours*3600)).divide(new BigDecimal(60)).intValue();
		minute = liveDurationSecond.subtract(new BigDecimal(hours*3600)).divideToIntegralValue(new BigDecimal(60)).intValue();
		liveDurationStr=(hours==0?"":hours+"小时")+minute+"分钟";
		return liveDurationStr;
	}

	/**
	 * @param liveDurationStr the liveDurationStr to set
	 */
	public void setLiveDurationStr(String liveDurationStr) {
		this.liveDurationStr = liveDurationStr;
	}

	public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    
    /**
	 * @return the selfComment
	 */
	public String getSelfComment() {
		return selfComment;
	}

	/**
	 * @param selfComment the selfComment to set
	 */
	public void setSelfComment(String selfComment) {
		this.selfComment = selfComment;
	}

	/**
	 * @return the sellerLookStatus
	 */
	public Integer getSellerLookStatus() {
		return sellerLookStatus;
	}

	/**
	 * @param sellerLookStatus the sellerLookStatus to set
	 */
	public void setSellerLookStatus(Integer sellerLookStatus) {
		this.sellerLookStatus = sellerLookStatus;
	}

	public Boolean getMsgStatus() {
        return msgStatus;
    }

    public void setMsgStatus(Boolean msgStatus) {
        this.msgStatus = msgStatus;
    }


    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    
    

    /**
	 * @return the bursPassword
	 */
	public String getBursPassword() {
		return bursPassword;
	}

	/**
	 * @param bursPassword the bursPassword to set
	 */
	public void setBursPassword(String bursPassword) {
		this.bursPassword = bursPassword;
	}

	public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

	/**
	 * @return the ledgerRatioSign
	 */
	public String getLedgerRatioSign() {
		BigDecimal multiplicand=new BigDecimal(100);
		if(ledgerRatio!=null){
			ledgerRatioSign = NumberUtil.getNumberFixedpoint(ledgerRatio.multiply(multiplicand), 0)+"%";
		}
		return ledgerRatioSign;
	}

	/**
	 * @param ledgerRatioSign the ledgerRatioSign to set
	 */
	public void setLedgerRatioSign(String ledgerRatioSign) {
		this.ledgerRatioSign = ledgerRatioSign;
	}


	/**
	 * @return the isBinding
	 */
	public String getIsBinding() {
		return isBinding;
	}

	/**
	 * @param isBinding the isBinding to set
	 */
	public void setIsBinding(String isBinding) {
		this.isBinding = isBinding;
	}
	
	

	/**
	 * @return the role
	 */
	public String getRole() {
		return role;
	}

	/**
	 * @param role the role to set
	 */
	public void setRole(String role) {
		this.role = role;
	}

	
	/**
	 * @return the changeToAnchor
	 */
	public String getChangeToAnchor() {
		return changeToAnchor;
	}

	/**
	 * @param changeToAnchor the changeToAnchor to set
	 */
	public void setChangeToAnchor(String changeToAnchor) {
		this.changeToAnchor = changeToAnchor;
	}

	/**
	 * @return the liveMinute
	 */
	public BigDecimal getLiveMinute() {
		return liveMinute;
	}

	/**
	 * @param liveMinute the liveMinute to set
	 */
	public void setLiveMinute(BigDecimal liveMinute) {
		this.liveMinute = liveMinute;
	}

	/**
	 * @return the liveTimeStr
	 */
	public String getLiveTimeStr() {
		long hours=0;
		Integer minute=0;
		if(liveMinute!=null){
			hours = liveMinute.divideToIntegralValue(new BigDecimal(60)).longValue();
			minute = liveMinute.remainder(new BigDecimal(60)).intValue();
			liveTimeStr="本月："+(hours==0?"":hours+"小时")+minute+"分钟";
		}
		return liveTimeStr ;
	}

	/**
	 * @param liveTimeStr the liveTimeStr to set
	 */
	public void setLiveTimeStr(String liveTimeStr) {
		this.liveTimeStr = liveTimeStr;
	}
	
	

	/**
	 * @return the totalLiveMinute
	 */
	public BigDecimal getTotalLiveMinute() {
		return totalLiveMinute;
	}

	/**
	 * @param totalLiveMinute the totalLiveMinute to set
	 */
	public void setTotalLiveMinute(BigDecimal totalLiveMinute) {
		this.totalLiveMinute = totalLiveMinute;
	}

	
	/**
	 * @return the totalLiveTimeStr
	 */
	public String getTotalLiveTimeStr() {
		long hours=0;
		Integer minute=0;
		if(totalLiveMinute!=null){
			hours = totalLiveMinute.divideToIntegralValue(new BigDecimal(60)).longValue();
			minute = totalLiveMinute.remainder(new BigDecimal(60)).intValue();
			totalLiveTimeStr= (hours==0?"":hours+"小时")+minute+"分钟";
		}
		return totalLiveTimeStr;
	}

	/**
	 * @param totalLiveTimeStr the totalLiveTimeStr to set
	 */
	public void setTotalLiveTimeStr(String totalLiveTimeStr) {
		this.totalLiveTimeStr = totalLiveTimeStr;
	}

	/**
	 * @return the totalRecordNum
	 */
	public Integer getTotalRecordNum() {
		return totalRecordNum;
	}

	/**
	 * @param totalRecordNum the totalRecordNum to set
	 */
	public void setTotalRecordNum(Integer totalRecordNum) {
		this.totalRecordNum = totalRecordNum;
	}

	/**
	 * @return the playbackNum
	 */
	public Integer getPlaybackNum() {
		return playbackNum;
	}

	/**
	 * @param playbackNum the playbackNum to set
	 */
	public void setPlaybackNum(Integer playbackNum) {
		this.playbackNum = playbackNum;
	}

	/**
	 * @return the recordNum
	 */
	public Integer getRecordNum() {
		return recordNum;
	}

	/**
	 * @param recordNum the recordNum to set
	 */
	public void setRecordNum(Integer recordNum) {
		this.recordNum = recordNum;
	}

	
	
	/**
	 * @return the balance
	 */
	public BigDecimal getBalance() {
		return balance;
	}

	/**
	 * @param balance the balance to set
	 */
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	
	

	/**
	 * @return the birdEggTotal
	 */
	public BigDecimal getBirdEggTotal() {
		return birdEggTotal;
	}

	/**
	 * @param birdEggTotal the birdEggTotal to set
	 */
	public void setBirdEggTotal(BigDecimal birdEggTotal) {
		this.birdEggTotal = birdEggTotal;
	}

	/**
	 * @return the imageCount
	 */
	public Long getImageCount() {
		return imageCount;
	}

	/**
	 * @param imageCount the imageCount to set
	 */
	public void setImageCount(Long imageCount) {
		this.imageCount = imageCount;
	}
	
	

	/**
	 * @return the androidNum
	 */
	public Integer getAndroidNum() {
		return androidNum;
	}

	/**
	 * @param androidNum the androidNum to set
	 */
	public void setAndroidNum(Integer androidNum) {
		this.androidNum = androidNum;
	}

	
	/**
	 * @return the isInside
	 */
	public Integer getIsInside() {
		return isInside;
	}

	/**
	 * @param isInside the isInside to set
	 */
	public void setIsInside(Integer isInside) {
		this.isInside = isInside;
	}

	
	/**
	 * @return the groupId
	 */
	public String getGroupId() {
		return groupId;
	}

	/**
	 * @param groupId the groupId to set
	 */
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	
	

	/**
	 * @return the wechatPic
	 */
	public String getWechatPic() {
		return wechatPic;
	}

	/**
	 * @param wechatPic the wechatPic to set
	 */
	public void setWechatPic(String wechatPic) {
		this.wechatPic = wechatPic;
	}

	
	/**
	 * @return the saleCouponRatio
	 */
	public BigDecimal getSaleCouponRatio() {
		return saleCouponRatio;
	}

	/**
	 * @param saleCouponRatio the saleCouponRatio to set
	 */
	public void setSaleCouponRatio(BigDecimal saleCouponRatio) {
		this.saleCouponRatio = saleCouponRatio;
	}

	/**
	 * @return the saleCouponRatioSign
	 */
	public String getSaleCouponRatioSign() {
		BigDecimal multiplicand=new BigDecimal(100);
		if(saleCouponRatio!=null){
			saleCouponRatioSign = NumberUtil.getNumberFixedpoint(saleCouponRatio.multiply(multiplicand), 0)+"%";
		}
		return saleCouponRatioSign;
	}

	/**
	 * @param saleCouponRatioSign the saleCouponRatioSign to set
	 */
	public void setSaleCouponRatioSign(String saleCouponRatioSign) {
		this.saleCouponRatioSign = saleCouponRatioSign;
	}

	/**
	 * @return the useCouponRation
	 */
	public BigDecimal getUseCouponRation() {
		return useCouponRation;
	}

	/**
	 * @param useCouponRation the useCouponRation to set
	 */
	public void setUseCouponRation(BigDecimal useCouponRation) {
		this.useCouponRation = useCouponRation;
	}

	/**
	 * @return the useCouponRationSign
	 */
	public String getUseCouponRationSign() {
		BigDecimal multiplicand=new BigDecimal(100);
		if(useCouponRation!=null){
			useCouponRationSign = NumberUtil.getNumberFixedpoint(useCouponRation.multiply(multiplicand), 0)+"%";
		}
		return useCouponRationSign;
	}

	/**
	 * @param useCouponRationSign the useCouponRationSign to set
	 */
	public void setUseCouponRationSign(String useCouponRationSign) {
		this.useCouponRationSign = useCouponRationSign;
	}
	
	

	/**
	 * @return the sortVal
	 */
	public Integer getSortVal() {
		return sortVal;
	}

	/**
	 * @param sortVal the sortVal to set
	 */
	public void setSortVal(Integer sortVal) {
		this.sortVal = sortVal;
	}

	/**
	 * @return the introduceMv
	 */
	public String getIntroduceMv() {
		return introduceMv;
	}

	/**
	 * @param introduceMv the introduceMv to set
	 */
	public void setIntroduceMv(String introduceMv) {
		this.introduceMv = introduceMv;
	}
	

	/**
	 * @return the officeName
	 */
	public String getOfficeName() {
		return officeName;
	}

	/**
	 * @param officeName the officeName to set
	 */
	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}

	
	/**
	 * @return the enterpriseUid
	 */
	public Integer getEnterpriseUid() {
		return enterpriseUid;
	}

	/**
	 * @param enterpriseUid the enterpriseUid to set
	 */
	public void setEnterpriseUid(Integer enterpriseUid) {
		this.enterpriseUid = enterpriseUid;
	}

	
	/**
	 * @return the enterpriseNname
	 */
	public String getEnterpriseNname() {
		return enterpriseNname;
	}

	/**
	 * @param enterpriseNname the enterpriseNname to set
	 */
	public void setEnterpriseNname(String enterpriseNname) {
		this.enterpriseNname = enterpriseNname;
	}

	/**
	 * @return the uidRelationChainNname
	 */
	public String getUidRelationChainNname() {
		return uidRelationChainNname;
	}

	/**
	 * @param uidRelationChainNname the uidRelationChainNname to set
	 */
	public void setUidRelationChainNname(String uidRelationChainNname) {
		this.uidRelationChainNname = uidRelationChainNname;
	}
	
	

	/**
	 * @return the topLevel
	 */
	public String getTopLevel() {
		if(uidRelationChainNname==null){
			return null;
		}
		String[] nnameArray = uidRelationChainNname.split(",",-1);
		int length = nnameArray.length;
		if(length>2){
			topLevel=nnameArray[length-3];
		}
		return topLevel;
	}

	/**
	 * @param topLevel the topLevel to set
	 */
	public void setTopLevel(String topLevel) {
		this.topLevel = topLevel;
	}

	
	
	/**
	 * @return the topLevelUid
	 */
	public String getTopLevelUid() {
		if(uidRelationChain==null){
			return null;
		}
		String[] uidArray = uidRelationChain.split(",");
		int length = uidArray.length;
		if(length>2){
			topLevelUid=uidArray[length-3];
			String topLevelUidTemp = topLevelUid.replaceAll("^(0+)", "");//去掉uid开头拼接的0
			topLevelUid=topLevelUidTemp;
		}
		return topLevelUid;
	}

	/**
	 * @param topLevelUid the topLevelUid to set
	 */
	public void setTopLevelUid(String topLevelUid) {
		this.topLevelUid = topLevelUid;
	}

	
	
	/**
	 * @return the topLevelStr
	 */
	public String getTopLevelStr() {
		StringBuffer sb=new StringBuffer();
		if(objectOriented!=null && objectOriented!=null && uidRelationChainLevel!=null && objectOriented==2 && uidRelationChainLevel==2){
			String referrerSelleridTemp=referrerSellerid==null?"":referrerSellerid.toString();
			sb.append("[商家|").append(referrerSelleridTemp).append("]");
		}else{
			if(topLevelUid==null){
				return null;
			}
			sb.append("[用户|").append(topLevelUid).append("]");
		}
		
		topLevelStr=sb.toString();
		return topLevelStr;
	}

	/**
	 * @param topLevelStr the topLevelStr to set
	 */
	public void setTopLevelStr(String topLevelStr) {
		this.topLevelStr = topLevelStr;
	}

	/**
	 * @return the superior
	 */
	public String getSuperior() {
		if(uidRelationChainNname==null){
			return null;
		}
		String[] nnameArray = uidRelationChainNname.split(",",-1);
		int length = nnameArray.length;
		if(length>1){
			superior=nnameArray[length-2];
		}
		return superior;
	}

	/**
	 * @param superior the superior to set
	 */
	public void setSuperior(String superior) {
		this.superior = superior;
	}
	
	

	/**
	 * @return the superiorUid
	 */
	public String getSuperiorUid() {
		if(uidRelationChain==null){
			return null;
		}
		String[] uidArray = uidRelationChain.split(",");
		int length = uidArray.length;
		if(length>1){
			superiorUid=uidArray[length-2];
			String superiorUidTemp = superiorUid.replaceAll("^(0+)", "");//去掉开头拼接的0
			superiorUid=superiorUidTemp;
		}
		return superiorUid;
	}

	/**
	 * @param superiorUid the superiorUid to set
	 */
	public void setSuperiorUid(String superiorUid) {
		this.superiorUid = superiorUid;
	}

	
	
	/**
	 * @return the superiorStr
	 */
	public String getSuperiorStr() {
		StringBuffer sb=new StringBuffer();
		if(objectOriented!=null && objectOriented !=null && uidRelationChainLevel !=null && objectOriented==2 && uidRelationChainLevel==1){
			String superiorUidTemp=referrerSellerid==null?"":referrerSellerid.toString();
			sb.append("[商家|").append(superiorUidTemp).append("]");
		}else{
			if(superiorUid==null){
				return null;
			}
			String superiorUidTemp = superiorUid.replaceAll("^(0+)", "");//去掉开头拼接的0
			sb.append("[用户|").append(superiorUidTemp).append("]");
		}
		superiorStr=sb.toString();
		return superiorStr;
	}

	/**
	 * @param superiorStr the superiorStr to set
	 */
	public void setSuperiorStr(String superiorStr) {
		this.superiorStr = superiorStr;
	}

	/**
	 * @return the uidRelationChain
	 */
	public String getUidRelationChain() {
		return uidRelationChain;
	}

	/**
	 * @param uidRelationChain the uidRelationChain to set
	 */
	public void setUidRelationChain(String uidRelationChain) {
		this.uidRelationChain = uidRelationChain;
	}

	/**
	 * @return the uidRelationChainLevel
	 */
	public Integer getUidRelationChainLevel() {
		return uidRelationChainLevel;
	}

	/**
	 * @param uidRelationChainLevel the uidRelationChainLevel to set
	 */
	public void setUidRelationChainLevel(Integer uidRelationChainLevel) {
		this.uidRelationChainLevel = uidRelationChainLevel;
	}

	/**
	 * @return the fansRankNo
	 */
	public Long getFansRankNo() {
		return fansRankNo;
	}

	/**
	 * @param fansRankNo the fansRankNo to set
	 */
	public void setFansRankNo(Long fansRankNo) {
		this.fansRankNo = fansRankNo;
	}

	/**
	 * @return the fansRankName
	 */
	public String getFansRankName() {
		return fansRankName;
	}

	/**
	 * @param fansRankName the fansRankName to set
	 */
	public void setFansRankName(String fansRankName) {
		this.fansRankName = fansRankName;
	}

	/**
	 * @return the fansRankId
	 */
	public Integer getFansRankId() {
		return fansRankId;
	}

	/**
	 * @param fansRankId the fansRankId to set
	 */
	public void setFansRankId(Integer fansRankId) {
		this.fansRankId = fansRankId;
	}


	/**
	 * @return the currentConsumeLedger
	 */
	public BigDecimal getCurrentConsumeLedger() {
		return currentConsumeLedger;
	}

	/**
	 * @param currentConsumeLedger the currentConsumeLedger to set
	 */
	public void setCurrentConsumeLedger(BigDecimal currentConsumeLedger) {
		this.currentConsumeLedger = currentConsumeLedger;
	}


	/**
	 * @return the referrerType
	 */
	public String getReferrerType() {
		return referrerType;
	}

	/**
	 * @param referrerType the referrerType to set
	 */
	public void setReferrerType(String referrerType) {
		this.referrerType = referrerType;
	}
	
	

	/**
	 * @return the referrerTypeStr
	 */
	public String getReferrerTypeStr() {
		if(referrerType==null){
			return null;
		}
		switch (referrerType) {
		case "001":
			referrerTypeStr="企业推荐人";
			break;
		case "002":
			referrerTypeStr="普通推荐人";
			break;
		default:
			break;
		}
		
		return referrerTypeStr;
	}

	/**
	 * @param referrerTypeStr the referrerTypeStr to set
	 */
	public void setReferrerTypeStr(String referrerTypeStr) {
		this.referrerTypeStr = referrerTypeStr;
	}
	
	
	

	/**
	 * @return the juniors
	 */
	public long getJuniors() {
		return juniors;
	}

	/**
	 * @param juniors the juniors to set
	 */
	public void setJuniors(long juniors) {
		this.juniors = juniors;
	}

	/**
	 * @return the juniorRecharge
	 */
	public BigDecimal getJuniorRecharge() {
		return juniorRecharge;
	}

	/**
	 * @param juniorRecharge the juniorRecharge to set
	 */
	public void setJuniorRecharge(BigDecimal juniorRecharge) {
		this.juniorRecharge = juniorRecharge;
	}

	/**
	 * @return the juniorReward
	 */
	public BigDecimal getJuniorReward() {
		return juniorReward;
	}

	/**
	 * @param juniorReward the juniorReward to set
	 */
	public void setJuniorReward(BigDecimal juniorReward) {
		this.juniorReward = juniorReward;
	}

	/**
	 * @return the juniorConsume
	 */
	public BigDecimal getJuniorConsume() {
		return juniorConsume;
	}

	/**
	 * @param juniorConsume the juniorConsume to set
	 */
	public void setJuniorConsume(BigDecimal juniorConsume) {
		this.juniorConsume = juniorConsume;
	}
	
	

	/**
	 * @return the startTime
	 */
	public String getStartTime() {
		return startTime;
	}

	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	/**
	 * @return the endTime
	 */
	public String getEndTime() {
		return endTime;
	}

	/**
	 * @param endTime the endTime to set
	 */
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	
	
	/**
	 * @return the ids
	 */
	public List<Object> getIds() {
		return ids;
	}

	/**
	 * @param ids the ids to set
	 */
	public void setIds(List<Object> ids) {
		this.ids = ids;
	}
	
	

	/**
	 * @return the orderAmount
	 */
	public BigDecimal getOrderAmount() {
		return orderAmount;
	}

	/**
	 * @param orderAmount the orderAmount to set
	 */
	public void setOrderAmount(BigDecimal orderAmount) {
		this.orderAmount = orderAmount;
	}

	/**
	 * @return the comsumAmount
	 */
	public BigDecimal getComsumAmount() {
		return comsumAmount;
	}

	/**
	 * @param comsumAmount the comsumAmount to set
	 */
	public void setComsumAmount(BigDecimal comsumAmount) {
		this.comsumAmount = comsumAmount;
	}

	/**
	 * @return the comsumLedger
	 */
	public BigDecimal getComsumLedger() {
		return comsumLedger;
	}

	/**
	 * @param comsumLedger the comsumLedger to set
	 */
	public void setComsumLedger(BigDecimal comsumLedger) {
		this.comsumLedger = comsumLedger;
	}

	/**
	 * @return the expectedPriviledgeLedger
	 */
	public BigDecimal getExpectedPriviledgeLedger() {
		return expectedPriviledgeLedger;
	}

	/**
	 * @param expectedPriviledgeLedger the expectedPriviledgeLedger to set
	 */
	public void setExpectedPriviledgeLedger(BigDecimal expectedPriviledgeLedger) {
		this.expectedPriviledgeLedger = expectedPriviledgeLedger;
	}

	/**
	 * @return the currentDividendLedger
	 */
	public BigDecimal getCurrentDividendLedger() {
		return currentDividendLedger;
	}

	/**
	 * @param currentDividendLedger the currentDividendLedger to set
	 */
	public void setCurrentDividendLedger(BigDecimal currentDividendLedger) {
		this.currentDividendLedger = currentDividendLedger;
	}

	/**
	 * @return the currentRecommendLedger
	 */
	public BigDecimal getCurrentRecommendLedger() {
		return currentRecommendLedger;
	}

	/**
	 * @param currentRecommendLedger the currentRecommendLedger to set
	 */
	public void setCurrentRecommendLedger(BigDecimal currentRecommendLedger) {
		this.currentRecommendLedger = currentRecommendLedger;
	}
	
	

	/**
	 * @return the restrictive
	 */
	public String getRestrictive() {
		return restrictive;
	}

	/**
	 * @param restrictive the restrictive to set
	 */
	public void setRestrictive(String restrictive) {
		this.restrictive = restrictive;
	}
	
	

	/**
	 * @return the restrictiveStr
	 */
	public String getRestrictiveStr() {
		if(restrictive==null){
			return null;
		}
		switch (restrictive) {
		case "001":
			restrictiveStr="不限制";
			break;
		case "002":
			restrictiveStr="限制";
			break;
		default:
			break;
		}
		return restrictiveStr;
	}

	/**
	 * @param restrictiveStr the restrictiveStr to set
	 */
	public void setRestrictiveStr(String restrictiveStr) {
		this.restrictiveStr = restrictiveStr;
	}

	/**
	 * @return the limitBalance
	 */
	public BigDecimal getLimitBalance() {
		return limitBalance;
	}

	/**
	 * @param limitBalance the limitBalance to set
	 */
	public void setLimitBalance(BigDecimal limitBalance) {
		this.limitBalance = limitBalance;
	}
	
	

	/**
	 * @return the commision
	 */
	public BigDecimal getCommision() {
		return commision;
	}

	/**
	 * @param commision the commision to set
	 */
	public void setCommision(BigDecimal commision) {
		this.commision = commision;
	}

	/**
	 * @return the zbalance
	 */
	public BigDecimal getZbalance() {
		return zbalance;
	}

	/**
	 * @param zbalance the zbalance to set
	 */
	public void setZbalance(BigDecimal zbalance) {
		this.zbalance = zbalance;
	}

	/**
	 * @return the operationType
	 */
	public Integer getOperationType() {
		return operationType;
	}

	/**
	 * @param operationType the operationType to set
	 */
	public void setOperationType(Integer operationType) {
		this.operationType = operationType;
	}
	
	
	


	/**
	 * @return the superiorIdChosen
	 */
	public String getSuperiorIdChosen() {
		return superiorIdChosen;
	}

	/**
	 * @param superiorIdChosen the superiorIdChosen to set
	 */
	public void setSuperiorIdChosen(String superiorIdChosen) {
		this.superiorIdChosen = superiorIdChosen;
	}
	
	

	/**
	 * @return the redPacketAuthority
	 */
	public Integer getRedPacketAuthority() {
		return redPacketAuthority;
	}

	/**
	 * @param redPacketAuthority the redPacketAuthority to set
	 */
	public void setRedPacketAuthority(Integer redPacketAuthority) {
		this.redPacketAuthority = redPacketAuthority;
	}
	
	

	/**
	 * 0 正常可获取 1 不可获取 2 可获取但不可入账
	 * @return the redPacketAuthorityStr
	 */
	public String getRedPacketAuthorityStr() {
		if(redPacketAuthority==null){
			return null;
		}
		switch (redPacketAuthority) {
		case 0:
			redPacketAuthorityStr="正常可获取";
			break;
		case 1:
			redPacketAuthorityStr="不可获取";
			break;
		case 2:
			redPacketAuthorityStr="可获取但不可入账";
			break;

		default:
			break;
		}
		
		return redPacketAuthorityStr;
	}

	/**
	 * @param redPacketAuthorityStr the redPacketAuthorityStr to set
	 */
	public void setRedPacketAuthorityStr(String redPacketAuthorityStr) {
		this.redPacketAuthorityStr = redPacketAuthorityStr;
	}

	public Long getBirthdayData() {
		return birthdayData;
	}

	public void setBirthdayData(Long birthdayData) {
		this.birthdayData = birthdayData;
	}
	
	

	/**
	 * @return the userPlatform
	 */
	public Integer getUserPlatform() {
		return userPlatform;
	}

	/**
	 * @param userPlatform the userPlatform to set
	 */
	public void setUserPlatform(Integer userPlatform) {
		this.userPlatform = userPlatform;
	}

	/**
	 * @return the objectOriented
	 */
	public Integer getObjectOriented() {
		return objectOriented;
	}

	/**
	 * @param objectOriented the objectOriented to set
	 */
	public void setObjectOriented(Integer objectOriented) {
		this.objectOriented = objectOriented;
	}
	
	

	/**
	 * @return the objectOrientedVal
	 */
	public String getObjectOrientedVal() {
		return objectOrientedVal;
	}

	/**
	 * @param objectOrientedVal the objectOrientedVal to set
	 */
	public void setObjectOrientedVal(String objectOrientedVal) {
		this.objectOrientedVal = objectOrientedVal;
	}

	/**
	 * @return the dividendsRole
	 */
	public Integer getDividendsRole() {
		return dividendsRole;
	}

	/**
	 * @param dividendsRole the dividendsRole to set
	 */
	public void setDividendsRole(Integer dividendsRole) {
		this.dividendsRole = dividendsRole;
	}
	
	

	/**
	 * @return the userDescription
	 */
	public String getUserDescription() {
		return userDescription;
	}

	/**
	 * @param userDescription the userDescription to set
	 */
	public void setUserDescription(String userDescription) {
		this.userDescription = userDescription;
	}
	
	

	/**
	 * @return the juniorLimitRatio
	 */
	public BigDecimal getJuniorLimitRatio() {
		return juniorLimitRatio;
	}

	/**
	 * @param juniorLimitRatio the juniorLimitRatio to set
	 */
	public void setJuniorLimitRatio(BigDecimal juniorLimitRatio) {
		this.juniorLimitRatio = juniorLimitRatio;
	}
	
	

	/**
	 * 主播签约类型：0 兼职主播，1 签约主播，2测试账号
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
	 * 1:普通兼职  2:工会合作 3:活动合作 4:其他
	 * @return the rootRole
	 */
	public Integer getRootRole() {
		return rootRole;
	}
	
	

	/**
	 * @return the anchorType
	 */
	public String getAnchorType() {
		StringBuffer temp=new StringBuffer();
		
		if(signType==null){
			return null;
		}
		//主播签约类型：0 兼职主播，1 签约主播，2测试账号 ,4星食尚大赛报名主播
		switch (signType) {
		case 0:
			temp.append("兼职主播");
			break;
		case 1:
			temp.append("签约主播");
			break;
		case 2:
			temp.append("公司账号");
			break;
		case 4:
			temp.append("大赛主播");
			break;
		default:
			temp.append("兼职主播");
			break;
		}
		
		//1:普通兼职  2:工会合作 3:活动合作 4:其他
		if(rootRole!=null && signType==0){
			switch (rootRole) {
			case 1:
				temp.append("-普通兼职");
				break;
			case 2:
				temp.append("-工会合作");
				break;
			case 3:
				temp.append("-活动合作");
				break;
			case 4:
				temp.append("-其他");
				break;
			default:
				temp.append("-普通兼职");
				break;
			}
		}
		return anchorType=temp.toString();
	}

	/**
	 * @param anchorType the anchorType to set
	 */
	public void setAnchorType(String anchorType) {
		this.anchorType = anchorType;
	}

	/**
	 * @param rootRole the rootRole to set
	 */
	public void setRootRole(Integer rootRole) {
		this.rootRole = rootRole;
	}

	
	/**
	 * @return the levelId
	 */
	public Integer getLevelId() {
		return levelId;
	}

	/**
	 * @param levelId the levelId to set
	 */
	public void setLevelId(Integer levelId) {
		this.levelId = levelId;
	}
	
	

	/**
	 * @return the levelName
	 */
	public String getLevelName() {
		return levelName;
	}

	/**
	 * @param levelName the levelName to set
	 */
	public void setLevelName(String levelName) {
		this.levelName = levelName;
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
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * @return the province
	 */
	public Integer getProvince() {
		return province;
	}

	/**
	 * @param province the province to set
	 */
	public void setProvince(Integer province) {
		this.province = province;
	}

	/**
	 * @return the city
	 */
	public Integer getCity() {
		return city;
	}

	/**
	 * @param city the city to set
	 */
	public void setCity(Integer city) {
		this.city = city;
	}



	/**
	 * @return the referrerSellerid
	 */
	public Integer getReferrerSellerid() {
		return referrerSellerid;
	}

	/**
	 * @param referrerSellerid the referrerSellerid to set
	 */
	public void setReferrerSellerid(Integer referrerSellerid) {
		this.referrerSellerid = referrerSellerid;
	}

	/**
	 * @return the indirectUid
	 */
	public Integer getIndirectUid() {
		return indirectUid;
	}

	/**
	 * @param indirectUid the indirectUid to set
	 */
	public void setIndirectUid(Integer indirectUid) {
		this.indirectUid = indirectUid;
	}



	/**
	 * @return the indirectId
	 */
	public Integer getIndirectId() {
		return indirectId;
	}

	/**
	 * @param indirectId the indirectId to set
	 */
	public void setIndirectId(Integer indirectId) {
		this.indirectId = indirectId;
	}



	/**
	 * @return the indirectNname
	 */
	public String getIndirectNname() {
		return indirectNname;
	}

	/**
	 * @param indirectNname the indirectNname to set
	 */
	public void setIndirectNname(String indirectNname) {
		this.indirectNname = indirectNname;
	}
	
	/**
	 * @return the indirectUidAndName
	 */
	public String getIndirectUidAndName() {
		if(indirectUid !=null){
			indirectUidAndName = indirectUid.toString();
		}
		
		if(StringUtils.isNotBlank(indirectNname)){
			indirectUidAndName = indirectUid + "|" + indirectNname;
		}
		return indirectUidAndName;
	}

	/**
	 * @param indirectUidAndName the indirectUidAndName to set
	 */
	public void setIndirectUidAndName(String indirectUidAndName) {
		this.indirectUidAndName = indirectUidAndName;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "BLiver [id=" + id + ", uid=" + uid + ", uidStr=" + uidStr
				+ ", utype=" + utype + ", anchorRoomNo=" + anchorRoomNo
				+ ", height=" + height + ", heightStr=" + heightStr
				+ ", weight=" + weight + ", weightStr=" + weightStr
				+ ", threeDimensional=" + threeDimensional + ", selfComment="
				+ selfComment + ", ledgerRatio=" + ledgerRatio
				+ ", ledgerRatioSign=" + ledgerRatioSign + ", saleCouponRatio="
				+ saleCouponRatio + ", saleCouponRatioSign="
				+ saleCouponRatioSign + ", useCouponRation=" + useCouponRation
				+ ", useCouponRationSign=" + useCouponRationSign + ", rankId="
				+ rankId + ", rankNo=" + rankNo + ", achievement="
				+ achievement + ", currentExpe=" + currentExpe
				+ ", concernNums=" + concernNums + ", concernedNums="
				+ concernedNums + ", giveGiftsNums=" + giveGiftsNums
				+ ", givedGiftsNums=" + givedGiftsNums + ", praisedNums="
				+ praisedNums + ", viewDuration=" + viewDuration
				+ ", liveDuration=" + liveDuration + ", liveDurationStr="
				+ liveDurationStr + ", status=" + status
				+ ", sellerLookStatus=" + sellerLookStatus + ", msgStatus="
				+ msgStatus + ", createTime=" + createTime + ", updateTime="
				+ updateTime + ", bursPassword=" + bursPassword
				+ ", isBinding=" + isBinding + ", role=" + role
				+ ", changeToAnchor=" + changeToAnchor + ", liveMinute="
				+ liveMinute + ", liveTimeStr=" + liveTimeStr
				+ ", playbackNum=" + playbackNum + ", recordNum=" + recordNum
				+ ", balance=" + balance + ", birdEggTotal=" + birdEggTotal
				+ ", name=" + name + ", nickname=" + nickname + ", avatar="
				+ avatar + ", sex=" + sex + ", password=" + password
				+ ", phone=" + phone + ", idcard=" + idcard + ", birthday="
				+ birthday + ", weixin=" + weixin + ", age=" + age
				+ ", showValue=" + showValue + ", filterVal=" + filterVal
				+ ", uidViewJunior=" + uidViewJunior + ", imageCount="
				+ imageCount + ", androidNum=" + androidNum + ", isInside="
				+ isInside + ", groupId=" + groupId + ", wechatPic="
				+ wechatPic + ", sortVal=" + sortVal + ", introduceMv="
				+ introduceMv + ", officeName=" + officeName
				+ ", enterpriseUid=" + enterpriseUid + ", enterpriseNname="
				+ enterpriseNname + ", uidRelationChain=" + uidRelationChain
				+ ", uidRelationChainNname=" + uidRelationChainNname
				+ ", uidRelationChainLevel=" + uidRelationChainLevel
				+ ", fansRankNo=" + fansRankNo + ", fansRankName="
				+ fansRankName + ", fansRankId=" + fansRankId
				+ ", referrerType=" + referrerType + ", referrerTypeStr="
				+ referrerTypeStr + ", topLevel=" + topLevel + ", topLevelUid="
				+ topLevelUid + ", topLevelStr=" + topLevelStr + ", superior="
				+ superior + ", superiorUid=" + superiorUid + ", superiorStr="
				+ superiorStr + ", juniors=" + juniors + ", juniorRecharge="
				+ juniorRecharge + ", juniorReward=" + juniorReward
				+ ", juniorConsume=" + juniorConsume + ", startTime="
				+ startTime + ", endTime=" + endTime + ", orderAmount="
				+ orderAmount + ", comsumAmount=" + comsumAmount
				+ ", comsumLedger=" + comsumLedger
				+ ", expectedPriviledgeLedger=" + expectedPriviledgeLedger
				+ ", currentConsumeLedger=" + currentConsumeLedger
				+ ", currentDividendLedger=" + currentDividendLedger
				+ ", currentRecommendLedger=" + currentRecommendLedger
				+ ", restrictive=" + restrictive + ", restrictiveStr="
				+ restrictiveStr + ", limitBalance=" + limitBalance
				+ ", commision=" + commision + ", zbalance=" + zbalance
				+ ", operationType=" + operationType + ", superiorIdChosen="
				+ superiorIdChosen + ", ids=" + ids + ", redPacketAuthority="
				+ redPacketAuthority + ", redPacketAuthorityStr="
				+ redPacketAuthorityStr + ", birthdayData=" + birthdayData
				+ ", userPlatform=" + userPlatform + ", objectOriented="
				+ objectOriented + ", objectOrientedVal=" + objectOrientedVal
				+ ", dividendsRole=" + dividendsRole + ", userDescription="
				+ userDescription + ", referrerSellerid=" + referrerSellerid
				+ ", juniorLimitRatio=" + juniorLimitRatio + ", signType="
				+ signType + ", rootRole=" + rootRole + ", anchorType="
				+ anchorType + ", levelId=" + levelId + ", levelName="
				+ levelName + ", picUrls=" + picUrls + ", remark=" + remark
				+ ", province=" + province + ", city=" + city
				+ ", indirectUid=" + indirectUid + ", indirectId=" + indirectId
				+ ", indirectNname=" + indirectNname + ", performExperience="
				+ performExperience + ", experienceResume=" + experienceResume
				+ ", interests=" + interests + ", styleLabel=" + styleLabel
				+ ", styleLabelDesc=" + styleLabelDesc
				+ ", liveAnchorImageList=" + liveAnchorImageList + "]";
	}

	public String toStringSimple() {
		return "BLiver [id=" + id + ", uid=" + uid + ", uidStr=" + uidStr
				+ ", utype=" + utype + ", enterpriseUid=" + enterpriseUid
				+ ", enterpriseNname=" + enterpriseNname
				+ ", uidRelationChain=" + uidRelationChain
				+ ", uidRelationChainNname=" + uidRelationChainNname
				+ ", uidRelationChainLevel=" + uidRelationChainLevel
				+ ", userDescription=" + userDescription + "]";
	}
	
	
    
    
}