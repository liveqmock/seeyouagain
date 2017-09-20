package com.xmniao.xmn.core.util;

public class SystemConstants {

	/** 寻蜜鸟平台类型，0：业务系统. */
	public static final int PLATFORM_TYPE_XMNWEB = 0;
	/** 寻蜜鸟平台类型，1：合作商APP. */
	public static final int PLATFORM_TYPE_XMNHZ = 1;
	/** 寻蜜鸟平台类型，2：商家APP. */
	public static final int PLATFORM_TYPE_XMNSH = 2;
	
	public static final String getPayTypeText(String paytype){
		if(paytype == null) return "";
		switch (paytype) {
		case "1000000":
			return "钱包支付";
		case "1000001":
			return "支付宝SDK支付";
		case "1000002":
			return "U付";
		case "1000003":
			return "微信SDK支付";
		case "1000004":
			return "AppStore支付";
		case "1000005":
			return "汇付天下";
		case "1000006":
			return "融宝支付";
		case "1000007":
			return "盛付通支付";
		case "1000008":
			return "快钱支付";
		case "1000009":
			return "通联支付";
		case "1000010":
			return "连连支付";
		case "1000011":
			return "优惠券支付";
		case "1000012":
			return "会员卡支付";
		case "1000013":
			return "微信公众号支付";
		case "1000014":
			return "支付宝(手机网页支付)";
		case "1000015":
//			return "鸟币支付";	
		case "1000020":
			return "鸟粉卡支付";
		case "1000016":
			return "支付宝条码支付(主扫)";
		case "1000017":
			return "微信刷卡支付(主扫)";
		case "1000018":
			return "支付宝扫码支付(反扫)";
		case "1000019":
			return "微信扫码支付(反扫)";
		case "1000021":
			return "线下转账";
		case "1000022":
			return "支付宝APP支付（鸟人科技）";
		case "1000023":
			return "支付宝WAP支付（鸟人科技）";
		case "1000024":
			return "微信APP支付（鸟人科技）";
		case "1000025":
			return "微信公众号支付（鸟人科技）";
		case "1000026":
			return "银联支付";
		case "1000027":
			return "专享卡支付";
		case "1000028":
			return "PC通联支付";
		case "10000000":
			return "免费赠送";
		case "10000001":
			return "EC/线下报单";
		default:
			return "";
		}
	}

}
