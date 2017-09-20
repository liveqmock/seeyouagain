package com.xmniao.xmn.core.businessman.entity;

import java.io.Serializable;

import org.springframework.data.annotation.Id;

public class MSeller implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6992896986035518176L;
	@Id
	private String _id;
	private Integer sellerid;// 商家ID
	private String staffid="";// 员工ID
	private String jointid="";// 合作商ID
	private String sellername="";// 商家名称
	private Integer province;// 省编号
	private Integer city;// 市编号
	private Integer area;// 区编号
	private Integer zoneid;// 商圈编号
	private Integer category;// 经营类别
	private Integer genre;// 二级类别
	private String genreIds="";// 二级分类编号串，APP专用需要同步到mongdb，各编号以“,”隔开
	private String bewrite="";// 类别描述
	private String address="";// 商家地址
	private String tel="";// 联系电话
	private String fullname="";// 法人姓名
	private String identity="";// 法人身份证
	private String phoneid="";// 联系人手机
	private String orgid="";// 机构代码
	private String licenseid="";// 营业执照
	private String licenseurl="";// 执照电子版URL
	private String sdate="";// 营业时间
	private String sexplain="";// 使用说明
	private String svalidity="";// 有效期开始
	private String evalidity="";// 有效期结束
	private Integer status;// 默认0未验证|1审核中|2未通过|3已签约|4未签约
	private String examineinfo="";// 审批说明，审批失败原因
	private String signdate="";// 签约时间
	private String udate="";// 修改时间
	private String give="";// 默认0不是 1是
	private String fatherid="";// 默认为0 没有总部，否则此ID为对应总部商家ID
	private String remarks="";// 备注
	private String email="";// 法人EMAIL
	private String identityzurl="";// 身份证附件正面
	private String identityfurl="";// 身份证附件反面
	private String identitynurl;//手持身份证正面照
	private String licensefurl="";// 营业执照电子版反面URL
	private String agreement="";//商户协议URL
	private String lssellername="";//连锁店名称
	private String typename="";//经营类名称
	private String tradename="";//二级分类名称
	private String isprotocol="";//是否同意合作协议 0未同意    1已同意
	private String agio_type="";//折扣类型
	private String agio="";// 当前折扣，商家折扣
	private String yledger="";//返利
	private String agio_time="";//最后一次修改折扣时间
	private String entry="";//流水
	private String is_virtual="";//是否虚拟商户，默认为0 不是虚拟商户   1虚拟商户
	private String flat_agio="";//平台补贴折扣，此折扣 必须大于 用户占比。如  用户占比为0.12  此值必须大于0.12
	private String isforce="";//是否是美食星势力商户 0不是   1是
	private Integer isonline;// 0：未上线 1：已上线 2:预上线 3：已下线 默认为：0
	private String agio_agio="";// '0全单  1折上折'
	private String ismultiple="";// '默认0否   1是'
	private String data_source="";//数据来源  业务系统录入，1：合作商app录入，2：商家app录入，默认为 1';
	private String offlineReason="";//下线原因
	private String date_operate="";//操作时间（包括上线，下线，预上线）
	private String seller_grade="";//商家等级 1：A，2：B+，3：B，4：C+，5：C
	private String is_pay;//商家是否开启打赏功能，0未开启，1开启
	private String landmark="";//参考地标
	private String introduce="";//商家介绍
	private String dishes="";//推荐菜品
	private String rule="";//商家规则
	private String tips="";//商家提示
	private String iswifi="";//0 没有 1 免费提供
	private String isparking="";//0 没有  1 免费停车位  2 有偿停车位
	private double consume;//人均消费
	private String ssid="";//wifi ssid
	private String wifi_pwd="";//wifi 密码
	private String returnrmb="";//	返现描述
	private MSellerLandmark coordinate;//longitude:经度  latitude:纬度   格式{"longitude":102.71,"latitude":25.32}
	private String pic_logo="";//BSON 商家LOGO图  breviary:缩略图URL picurl:图片URL bewrite:图片描述  格式{"picurl":"42.png","breviary":null]
	private String pic_pics="";//BSON 商家环境图 breviary:缩略图URL picurl:图片URL bewrite:图片描述  格式[{"picurl":"42.png","breviary":null}]
	private String pic_cover="";//BSON 商家封面图  breviary:缩略图URL picurl:图片URL bewrite:图片描述  格式{"picurl":"42.png","breviary":null]
	private String areaname="";// 区名称
	private String business="";// 商圈标题
	private String commentnum="";//评论数量
	private String is_open_booking="";//
	private String islock="";
	private String ratio=""; //佣金补贴比例(商户补贴比例)，补给商户向蜜客额外的钱。以活动形式发放。补贴比例 0-100%
	private String isfees="";// 是否扣取支付手续费：
	private String debit="";//平台扣款比例
	private Double agioAgioNum;  //全单折扣
	private Integer label=0;  //商家标签
	private Integer order;  //订单基数
	private Integer uid;	//寻蜜客ID
	private String ledgerMode;//分账模式
	private Integer live_coin_pay=0;
	private Integer is_activity=0;//是否有正在进行的商家活动
	private Integer is_live;//是否有直播
	private Integer is_advance;//是否有预告
	private Integer is_fans_coupon;//是否有发售粉丝券
	private Integer views;//浏览量
	private Integer consumption;//消费量
	private Integer saved;//收藏量
	private Integer weights;//商家权重值
	private Integer version=0;
	
	private Integer is_public;//是否是公开商户
	private Integer is_paid;//是否是付费商家
	private Integer join_dividend;//是否参与平台分红
	
	private Double total_limit_turnover;//总交易限额
	private Double daily_limit_turnover;//日常交易限额
	private Double daily_limit_withdraw;//日常提现限额
	
	private Integer seller_random_num;//商家随机数
	private Integer seller_random_num_consumption;//商家随机消费数量
	
	public String getIs_open_booking() {
		return is_open_booking;
	}


	public void setIs_open_booking(String is_open_booking) {
		this.is_open_booking = is_open_booking;
	}


	public String get_id() {
		return _id;
	}


	public String getGenreIds() {
		return genreIds;
	}


	public void setGenreIds(String genreIds) {
		this.genreIds = genreIds;
	}


	public void set_id(String _id) {
		this._id = _id;
	}

	public String getStaffid() {
		return staffid;
	}


	public void setStaffid(String staffid) {
		this.staffid = staffid;
	}


	public String getJointid() {
		return jointid;
	}


	public void setJointid(String jointid) {
		this.jointid = jointid;
	}


	public String getSellername() {
		return sellername;
	}


	public void setSellername(String sellername) {
		this.sellername = sellername;
	}





	public Integer getIs_public() {
		return is_public;
	}


	public void setIs_public(Integer is_public) {
		this.is_public = is_public;
	}


	public Integer getIs_paid() {
		return is_paid;
	}


	public void setIs_paid(Integer is_paid) {
		this.is_paid = is_paid;
	}


	public Integer getJoin_dividend() {
		return join_dividend;
	}


	public void setJoin_dividend(Integer join_dividend) {
		this.join_dividend = join_dividend;
	}


	public Integer getSellerid() {
		return sellerid;
	}


	public void setSellerid(Integer sellerid) {
		this.sellerid = sellerid;
	}


	public Integer getProvince() {
		return province;
	}


	public void setProvince(Integer province) {
		this.province = province;
	}


	public Integer getCity() {
		return city;
	}


	public void setCity(Integer city) {
		this.city = city;
	}


	public Integer getArea() {
		return area;
	}


	public void setArea(Integer area) {
		this.area = area;
	}


	public Integer getZoneid() {
		return zoneid;
	}


	public void setZoneid(Integer zoneid) {
		this.zoneid = zoneid;
	}


	public Integer getCategory() {
		return category;
	}


	public void setCategory(Integer category) {
		this.category = category;
	}


	public Integer getGenre() {
		return genre;
	}


	public void setGenre(Integer genre) {
		this.genre = genre;
	}


	public void setStatus(Integer status) {
		this.status = status;
	}


	public void setIsonline(Integer isonline) {
		this.isonline = isonline;
	}


	public void setUid(Integer uid) {
		this.uid = uid;
	}


	public String getBewrite() {
		return bewrite;
	}


	public void setBewrite(String bewrite) {
		this.bewrite = bewrite;
	}


	public String getAddress() {
		return address;
	}


	public void setAddress(String address) {
		this.address = address;
	}


	public String getTel() {
		return tel;
	}


	public void setTel(String tel) {
		this.tel = tel;
	}


	public String getFullname() {
		return fullname;
	}


	public void setFullname(String fullname) {
		this.fullname = fullname;
	}


	public String getIdentity() {
		return identity;
	}


	public void setIdentity(String identity) {
		this.identity = identity;
	}


	public String getPhoneid() {
		return phoneid;
	}


	public void setPhoneid(String phoneid) {
		this.phoneid = phoneid;
	}


	public String getOrgid() {
		return orgid;
	}


	public void setOrgid(String orgid) {
		this.orgid = orgid;
	}


	public String getLicenseid() {
		return licenseid;
	}


	public void setLicenseid(String licenseid) {
		this.licenseid = licenseid;
	}


	public String getLicenseurl() {
		return licenseurl;
	}


	public void setLicenseurl(String licenseurl) {
		this.licenseurl = licenseurl;
	}


	public String getSdate() {
		return sdate;
	}


	public void setSdate(String sdate) {
		this.sdate = sdate;
	}


	public String getSexplain() {
		return sexplain;
	}


	public void setSexplain(String sexplain) {
		this.sexplain = sexplain;
	}


	public String getSvalidity() {
		return svalidity;
	}


	public void setSvalidity(String svalidity) {
		this.svalidity = svalidity;
	}


	public String getEvalidity() {
		return evalidity;
	}


	public void setEvalidity(String evalidity) {
		this.evalidity = evalidity;
	}

	public String getExamineinfo() {
		return examineinfo;
	}


	public void setExamineinfo(String examineinfo) {
		this.examineinfo = examineinfo;
	}


	public String getSigndate() {
		return signdate;
	}


	public void setSigndate(String signdate) {
		this.signdate = signdate;
	}


	public String getUdate() {
		return udate;
	}


	public void setUdate(String udate) {
		this.udate = udate;
	}


	public String getGive() {
		return give;
	}


	public void setGive(String give) {
		this.give = give;
	}


	public String getFatherid() {
		return fatherid;
	}


	public void setFatherid(String fatherid) {
		this.fatherid = fatherid;
	}


	public String getRemarks() {
		return remarks;
	}


	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getIdentityzurl() {
		return identityzurl;
	}


	public void setIdentityzurl(String identityzurl) {
		this.identityzurl = identityzurl;
	}


	public String getIdentityfurl() {
		return identityfurl;
	}


	public void setIdentityfurl(String identityfurl) {
		this.identityfurl = identityfurl;
	}

	public String getIdentitynurl() {
		return identitynurl;
	}


	public void setIdentitynurl(String identitynurl) {
		this.identitynurl = identitynurl;
	}

	public String getLicensefurl() {
		return licensefurl;
	}


	public void setLicensefurl(String licensefurl) {
		this.licensefurl = licensefurl;
	}


	public String getAgreement() {
		return agreement;
	}


	public void setAgreement(String agreement) {
		this.agreement = agreement;
	}


	public String getLssellername() {
		return lssellername;
	}


	public void setLssellername(String lssellername) {
		this.lssellername = lssellername;
	}


	public String getTypename() {
		return typename;
	}


	public void setTypename(String typename) {
		this.typename = typename;
	}


	public String getTradename() {
		return tradename;
	}


	public void setTradename(String tradename) {
		this.tradename = tradename;
	}


	public String getIsprotocol() {
		return isprotocol;
	}


	public void setIsprotocol(String isprotocol) {
		this.isprotocol = isprotocol;
	}






	public String getAgio_type() {
		return agio_type;
	}


	public void setAgio_type(String agio_type) {
		this.agio_type = agio_type;
	}


	public String getAgio() {
		return agio;
	}


	public void setAgio(String agio) {
		this.agio = agio;
	}


	public String getYledger() {
		return yledger;
	}


	public void setYledger(String yledger) {
		this.yledger = yledger;
	}


	public String getAgio_time() {
		return agio_time;
	}


	public void setAgio_time(String agio_time) {
		this.agio_time = agio_time;
	}


	public String getEntry() {
		return entry;
	}


	public void setEntry(String entry) {
		this.entry = entry;
	}


	public String getIs_virtual() {
		return is_virtual;
	}


	public void setIs_virtual(String is_virtual) {
		this.is_virtual = is_virtual;
	}


	public String getFlat_agio() {
		return flat_agio;
	}


	public void setFlat_agio(String flat_agio) {
		this.flat_agio = flat_agio;
	}


	public String getIsforce() {
		return isforce;
	}


	public void setIsforce(String isforce) {
		this.isforce = isforce;
	}


	public String getAgio_agio() {
		return agio_agio;
	}


	public void setAgio_agio(String agio_agio) {
		this.agio_agio = agio_agio;
	}


	public String getIsmultiple() {
		return ismultiple;
	}


	public void setIsmultiple(String ismultiple) {
		this.ismultiple = ismultiple;
	}


	public String getData_source() {
		return data_source;
	}


	public void setData_source(String data_source) {
		this.data_source = data_source;
	}


	public String getOfflineReason() {
		return offlineReason;
	}


	public void setOfflineReason(String offlineReason) {
		this.offlineReason = offlineReason;
	}


	public String getDate_operate() {
		return date_operate;
	}


	public void setDate_operate(String date_operate) {
		this.date_operate = date_operate;
	}


	public String getSeller_grade() {
		return seller_grade;
	}


	public void setSeller_grade(String seller_grade) {
		this.seller_grade = seller_grade;
	}


	public String getIs_pay() {
		return is_pay;
	}


	public void setIs_pay(String is_pay) {
		this.is_pay = is_pay;
	}


	public String getLandmark() {
		return landmark;
	}


	public void setLandmark(String landmark) {
		this.landmark = landmark;
	}


	public String getIntroduce() {
		return introduce;
	}


	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}


	public String getDishes() {
		return dishes;
	}


	public void setDishes(String dishes) {
		this.dishes = dishes;
	}


	public String getRule() {
		return rule;
	}


	public void setRule(String rule) {
		this.rule = rule;
	}


	public String getTips() {
		return tips;
	}


	public void setTips(String tips) {
		this.tips = tips;
	}


	public String getIswifi() {
		return iswifi;
	}


	public void setIswifi(String iswifi) {
		this.iswifi = iswifi;
	}


	public String getIsparking() {
		return isparking;
	}


	public void setIsparking(String isparking) {
		this.isparking = isparking;
	}


	public double getConsume() {
		return consume;
	}


	public void setConsume(double consume) {
		this.consume = consume;
	}


	public String getSsid() {
		return ssid;
	}


	public void setSsid(String ssid) {
		this.ssid = ssid;
	}


	public String getWifi_pwd() {
		return wifi_pwd;
	}


	public void setWifi_pwd(String wifi_pwd) {
		this.wifi_pwd = wifi_pwd;
	}


	public String getReturnrmb() {
		return returnrmb;
	}


	public void setReturnrmb(String returnrmb) {
		this.returnrmb = returnrmb;
	}


	public MSellerLandmark getCoordinate() {
		return coordinate;
	}


	public void setCoordinate(MSellerLandmark coordinate) {
		this.coordinate = coordinate;
	}


	public String getPic_logo() {
		return pic_logo;
	}


	public void setPic_logo(String pic_logo) {
		this.pic_logo = pic_logo;
	}


	public String getPic_pics() {
		return pic_pics;
	}


	public void setPic_pics(String pic_pics) {
		this.pic_pics = pic_pics;
	}


	public String getAreaname() {
		return areaname;
	}


	public void setAreaname(String areaname) {
		this.areaname = areaname;
	}


	public String getBusiness() {
		return business;
	}


	public void setBusiness(String business) {
		this.business = business;
	}


	public String getCommentnum() {
		return commentnum;
	}


	public void setCommentnum(String commentnum) {
		this.commentnum = commentnum;
	}



	public String getIslock() {
		return islock;
	}


	public void setIslock(String islock) {
		this.islock = islock;
	}


	public String getRatio() {
		return ratio;
	}


	public void setRatio(String ratio) {
		this.ratio = ratio;
	}


	public String getIsfees() {
		return isfees;
	}


	public void setIsfees(String isfees) {
		this.isfees = isfees;
	}


	public String getDebit() {
		return debit;
	}


	public void setDebit(String debit) {
		this.debit = debit;
	}


	public Double getAgioAgioNum() {
		return agioAgioNum;
	}


	public void setAgioAgioNum(Double agioAgioNum) {
		this.agioAgioNum = agioAgioNum;
	}


	public Integer getLabel() {
		return label;
	}


	public void setLabel(Integer label) {
		this.label = label;
	}


	public Integer getOrder() {
		return order;
	}


	public void setOrder(Integer order) {
		this.order = order;
	}


	public String getLedgerMode() {
		return ledgerMode;
	}


	public void setLedgerMode(String ledgerMode) {
		this.ledgerMode = ledgerMode;
	}

	public String getPic_cover() {
		return pic_cover;
	}


	public void setPic_cover(String pic_cover) {
		this.pic_cover = pic_cover;
	}


	public Integer getIs_live() {
		return is_live;
	}


	public void setIs_live(Integer is_live) {
		this.is_live = is_live;
	}


	public Integer getIs_advance() {
		return is_advance;
	}


	public void setIs_advance(Integer is_advance) {
		this.is_advance = is_advance;
	}


	public Integer getIs_fans_coupon() {
		return is_fans_coupon;
	}


	public void setIs_fans_coupon(Integer is_fans_coupon) {
		this.is_fans_coupon = is_fans_coupon;
	}


	public Integer getViews() {
		return views;
	}


	public void setViews(Integer views) {
		this.views = views;
	}


	public Integer getConsumption() {
		return consumption;
	}


	public void setConsumption(Integer consumption) {
		this.consumption = consumption;
	}


	public Integer getSaved() {
		return saved;
	}


	public void setSaved(Integer saved) {
		this.saved = saved;
	}


	public Integer getWeights() {
		return weights;
	}


	public void setWeights(Integer weights) {
		this.weights = weights;
	}


	public Integer getVersion() {
		return version;
	}


	public void setVersion(Integer version) {
		this.version = version;
	}


	public Integer getStatus() {
		return status;
	}


	public Integer getIsonline() {
		return isonline;
	}


	public Integer getUid() {
		return uid;
	}


	public Integer getLive_coin_pay() {
		return live_coin_pay;
	}


	public void setLive_coin_pay(Integer live_coin_pay) {
		this.live_coin_pay = live_coin_pay;
	}


	public Integer getIs_activity() {
		return is_activity;
	}


	public void setIs_activity(Integer is_activity) {
		this.is_activity = is_activity;
	}

	public Double getTotal_limit_turnover() {
		return total_limit_turnover;
	}


	public void setTotal_limit_turnover(Double total_limit_turnover) {
		this.total_limit_turnover = total_limit_turnover;
	}


	public Double getDaily_limit_turnover() {
		return daily_limit_turnover;
	}


	public void setDaily_limit_turnover(Double daily_limit_turnover) {
		this.daily_limit_turnover = daily_limit_turnover;
	}


	public Double getDaily_limit_withdraw() {
		return daily_limit_withdraw;
	}


	public void setDaily_limit_withdraw(Double daily_limit_withdraw) {
		this.daily_limit_withdraw = daily_limit_withdraw;
	}


	public Integer getSeller_random_num() {
		return seller_random_num;
	}


	public void setSeller_random_num(Integer seller_random_num) {
		this.seller_random_num = seller_random_num;
	}


	public Integer getSeller_random_num_consumption() {
		return seller_random_num_consumption;
	}


	public void setSeller_random_num_consumption(
			Integer seller_random_num_consumption) {
		this.seller_random_num_consumption = seller_random_num_consumption;
	}


	@Override
	public String toString() {
		return "MSeller [_id=" + _id + ", sellerid=" + sellerid + ", staffid="
				+ staffid + ", jointid=" + jointid + ", sellername="
				+ sellername + ", province=" + province + ", city=" + city
				+ ", area=" + area + ", zoneid=" + zoneid + ", category="
				+ category + ", genre=" + genre + ", genreIds=" + genreIds
				+ ", bewrite=" + bewrite + ", address=" + address + ", tel="
				+ tel + ", fullname=" + fullname + ", identity=" + identity
				+ ", phoneid=" + phoneid + ", orgid=" + orgid + ", licenseid="
				+ licenseid + ", licenseurl=" + licenseurl + ", sdate=" + sdate
				+ ", sexplain=" + sexplain + ", svalidity=" + svalidity
				+ ", evalidity=" + evalidity + ", status=" + status
				+ ", examineinfo=" + examineinfo + ", signdate=" + signdate
				+ ", udate=" + udate + ", give=" + give + ", fatherid="
				+ fatherid + ", remarks=" + remarks + ", email=" + email
				+ ", identityzurl=" + identityzurl + ", identityfurl="
				+ identityfurl + ", identitynurl=" + identitynurl
				+ ", licensefurl=" + licensefurl + ", agreement=" + agreement
				+ ", lssellername=" + lssellername + ", typename=" + typename
				+ ", tradename=" + tradename + ", isprotocol=" + isprotocol
				+ ", agio_type=" + agio_type + ", agio=" + agio + ", yledger="
				+ yledger + ", agio_time=" + agio_time + ", entry=" + entry
				+ ", is_virtual=" + is_virtual + ", flat_agio=" + flat_agio
				+ ", isforce=" + isforce + ", isonline=" + isonline
				+ ", agio_agio=" + agio_agio + ", ismultiple=" + ismultiple
				+ ", data_source=" + data_source + ", offlineReason="
				+ offlineReason + ", date_operate=" + date_operate
				+ ", seller_grade=" + seller_grade + ", is_pay=" + is_pay
				+ ", landmark=" + landmark + ", introduce=" + introduce
				+ ", dishes=" + dishes + ", rule=" + rule + ", tips=" + tips
				+ ", iswifi=" + iswifi + ", isparking=" + isparking
				+ ", consume=" + consume + ", ssid=" + ssid + ", wifi_pwd="
				+ wifi_pwd + ", returnrmb=" + returnrmb + ", coordinate="
				+ coordinate + ", pic_logo=" + pic_logo + ", pic_pics="
				+ pic_pics + ", pic_cover=" + pic_cover + ", areaname="
				+ areaname + ", business=" + business + ", commentnum="
				+ commentnum + ", is_open_booking=" + is_open_booking
				+ ", islock=" + islock + ", ratio=" + ratio + ", isfees="
				+ isfees + ", debit=" + debit + ", agioAgioNum=" + agioAgioNum
				+ ", label=" + label + ", order=" + order + ", uid=" + uid
				+ ", ledgerMode=" + ledgerMode + ", is_live=" + is_live
				+ ", is_advance=" + is_advance + ", is_fans_coupon="
				+ is_fans_coupon + ", views=" + views + ", consumption="
				+ consumption + ", saved=" + saved + ", weights=" + weights
				+ ", version=" + version + "]";
	}

}
