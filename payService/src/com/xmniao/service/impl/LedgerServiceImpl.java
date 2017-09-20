package com.xmniao.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.thrift.TException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.xmniao.common.MapUtil;
import com.xmniao.common.ParamUtil;
import com.xmniao.common.UpdateLedgerSystem;
import com.xmniao.common.UtilString;
import com.xmniao.common.XmnConstants;
import com.xmniao.dao.LedgerMapper;
import com.xmniao.dao.UpdateWalletBalanceMapper;
import com.xmniao.dao.WalletExpansionRecordMapper;
import com.xmniao.dao.WalletMapper;
import com.xmniao.dao.XmerWallerMapper;
import com.xmniao.entity.WalletExpansionRecord;
import com.xmniao.exception.ParamBlankException;
import com.xmniao.exception.RepetitionLedgerException;
import com.xmniao.service.CommonService;
import com.xmniao.service.LedgerService;
import com.xmniao.service.impl.LedgerServiceImpl.msgResult;
import com.xmniao.service.pay.SynthesizeServiceImpl;
import com.xmniao.service.pay.WalletExpansionServiceImpl;
import com.xmniao.service.pay.XmerWalletServiceImpl;
import com.xmniao.thrift.ledger.DataResponse;
import com.xmniao.thrift.ledger.FailureException;

/**
 * 分账监听
 * 
 * @author YangJing
 * 
 */
@Transactional
@Service("ledgerService")
public class LedgerServiceImpl implements LedgerService {

	/**
	 * 日志记录
	 */
	private final Logger log = Logger.getLogger(LedgerServiceImpl.class);

	@Autowired
	private LedgerMapper ledgerMapper;

	@Autowired
	private XmerWallerMapper xmerWalletMapper;

	@Autowired
	private UpdateWalletBalanceMapper updateWalletBalanceMapper;

	@Autowired
	private UpdateLedgerSystem updateLedgerSystem;

	@Autowired
	private CommonService commonService;

	@Autowired
	private XmerWalletServiceImpl xmerWalletService;

	@Autowired
	private SynthesizeServiceImpl synthesizeService;

	@Autowired
	private WalletExpansionServiceImpl walletExpansionServiceImpl;

	@Autowired
	private WalletMapper walletDao;

	@Autowired
	private WalletExpansionRecordMapper walletExpansionRecordMapper;

	@Transactional(rollbackFor = { FailureException.class,
			Exception.class }, isolation = Isolation.SERIALIZABLE, timeout = 100)
	@Override
	public Map<String, String> doLedger(Map<String, String> ledger) throws FailureException {

		log.info("分账开始--->");
		long sdate = System.currentTimeMillis();

		Map<String, String> map = new HashMap<String, String>();

		// String id = String.valueOf(ledger.get("id"));
		String orderId = ledger.get("orderId");
		String uId = ledger.get("uId");
		String uMoney = ledger.get("uMoney");
		String sId = ledger.get("sId");
		String sMoney = ledger.get("sMoney");
		String mId = ledger.get("mId");
		String mMoney = ledger.get("mMoney");
		String mType = ledger.get("mType");
		String brId = ledger.get("brId");
		String brMoney = ledger.get("brMoney");
		String crId = ledger.get("crId");
		String crMoney = ledger.get("crMoney");
		String remark = ledger.get("remark");
		String mExpenses = ledger.get("mExpenses");
		String brExpenses = ledger.get("brExpenses");
		String crExpenses = ledger.get("crExpenses");

		if (StringUtils.isEmpty(orderId) || StringUtils.isEmpty(uId) || StringUtils.isEmpty(uMoney)
				|| StringUtils.isEmpty(sId) || StringUtils.isEmpty(sMoney) || StringUtils.isEmpty(mId)
				|| StringUtils.isEmpty(mMoney) || StringUtils.isEmpty(mType) || StringUtils.isEmpty(brId)
				|| StringUtils.isEmpty(brMoney) || StringUtils.isEmpty(crId) || StringUtils.isEmpty(crMoney)
				|| StringUtils.isEmpty(remark) || StringUtils.isEmpty(mExpenses) || StringUtils.isEmpty(brExpenses)
				|| StringUtils.isEmpty(crExpenses)) {

			log.error("参数有空值:" + map);
			map.put("bid", orderId);
			map.put("code", "2");
			map.put("remark", "参数有空值");
			return map;
		}

		List<Map<String, String>> paramMap = new ArrayList<Map<String, String>>();

		if (!StringUtils.isEmpty(uId) && !uId.equals("0") && !StringUtils.isEmpty(uMoney)
				&& Double.valueOf(uMoney) != 0) {
			// 用户map
			Map<String, String> userMap = new HashMap<String, String>();
			userMap.put("uId", uId);
			userMap.put("userType", "1");
			userMap.put("balanceType", "2");
			userMap.put("balance", uMoney);
			userMap.put("orderId", orderId);
			userMap.put("ledgerType", "1");
			userMap.put("remark", remark);
			paramMap.add(userMap);
		}

		if (!StringUtils.isEmpty(sId) && !sId.equals("0") && !StringUtils.isEmpty(sMoney)
				&& Double.valueOf(sMoney) != 0) {

			// 商家map
			Map<String, String> sellerMap = new HashMap<String, String>();
			sellerMap.put("uId", sId);
			sellerMap.put("userType", "2");
			sellerMap.put("balanceType", "3");
			sellerMap.put("balance", sMoney);
			sellerMap.put("orderId", orderId);
			sellerMap.put("ledgerType", "2");
			sellerMap.put("remark", remark);
			paramMap.add(sellerMap);
		}

		if (!StringUtils.isEmpty(mId) && !mId.equals("0") && !StringUtils.isEmpty(mMoney)
				&& Double.valueOf(mMoney) != 0) {
			// 向蜜客map
			Map<String, String> mikeMap = new HashMap<String, String>();
			mikeMap.put("uId", mId);
			mikeMap.put("userType", mType);
			mikeMap.put("balanceType", "1");
			mikeMap.put("balance", mMoney);
			mikeMap.put("orderId", orderId);
			mikeMap.put("ledgerType", "3");
			mikeMap.put("remark", remark);
			mikeMap.put("expenses", mExpenses);
			paramMap.add(mikeMap);
		}

		if (!StringUtils.isEmpty(brId) && !brId.equals("0") && !StringUtils.isEmpty(brMoney)
				&& Double.valueOf(brMoney) != 0) {
			// 所属合作商map
			Map<String, String> brMap = new HashMap<String, String>();
			brMap.put("uId", brId);
			brMap.put("userType", "3");
			brMap.put("balanceType", "3");
			brMap.put("balance", brMoney);
			brMap.put("orderId", orderId);
			brMap.put("ledgerType", "4");
			brMap.put("remark", remark);
			brMap.put("expenses", brExpenses);
			paramMap.add(brMap);
		}

		if (!StringUtils.isEmpty(crId) && !crId.equals("0") && !StringUtils.isEmpty(crMoney)
				&& Double.valueOf(crMoney) != 0) {
			// 消费合作商map
			Map<String, String> crMap = new HashMap<String, String>();
			crMap.put("uId", crId);
			crMap.put("userType", "3");
			crMap.put("balanceType", "3");
			crMap.put("balance", crMoney);
			crMap.put("orderId", orderId);
			crMap.put("ledgerType", "5");
			crMap.put("remark", remark);
			crMap.put("expenses", crExpenses);
			paramMap.add(crMap);
		}

		// 分账处理
		map = ledger(paramMap);

		long edate = System.currentTimeMillis();

		long result = edate - sdate;

		log.info("分账运行时间：" + result + "ms");

		log.info("分账结束<---");

		return map;
	}

	private final BigDecimal ZERO = new BigDecimal("0.00");

	/*
	 * 消费订单新版分账 2016年6月21日
	 */
	@Transactional(rollbackFor = { FailureException.class,
			Exception.class,RuntimeException.class }, isolation = Isolation.SERIALIZABLE, timeout = 100)
	public Map<String, String> doXmnLedger(Map<String, String> ledger) throws FailureException {

		log.info("分账开始--->   参数:" + ledger);
		long sdate = System.currentTimeMillis();

		Map<String, String> map = new HashMap<String, String>();

		// String id = String.valueOf(ledger.get("id"));
		String orderId = ledger.get("orderId"); // 订单ID
		String uId = ledger.get("uId"); // 消费用户
		String uMoney = ledger.get("uMoney");
		String sId = ledger.get("sId"); // 消费商家
		String sMoney = ledger.get("sMoney");
		String mId = ledger.get("mId"); // 寻蜜客
		String mMoney = ledger.get("mMoney");
		String mType = ledger.get("mType"); // 寻蜜客类型
		String brId = ledger.get("brId"); // 经销商
		String brMoney = ledger.get("brMoney");
		String crId = ledger.get("crId"); // 消费所属合作商
		String crMoney = ledger.get("crMoney");
		String pmId = ledger.get("pmId"); // 上级寻蜜客
		String pmMoney = ledger.get("pmMoney");
		String tmId = ledger.get("tmId"); // 上上级寻蜜客
		String tmMoney = ledger.get("tmMoney");
		String ssId = ledger.get("ssId"); // 所属商家
		String ssMoney = ledger.get("ssMoney");

		String ptMoney = ledger.get("ptMoney"); // 平台金额

		String remark = ledger.get("remark");

		String saasChannel = ledger.get("saasChannel");

		if (UtilString.stringIsBlank(orderId)) {

			log.error("参数有空值:" + map);
			map.put("bid", orderId);
			map.put("code", "2");
			map.put("remark", "参数有空值");
			return map;
		}

		Integer type = null;
		Integer pmType = null;
		Integer recordType = null;
		if (org.apache.commons.lang.StringUtils.isNotBlank(saasChannel)) {
			switch (saasChannel) {
			case "1":
				type = XmnConstants.EX_RTYPE_7;
				pmType = XmnConstants.EX_RTYPE_7;
				break;
			case "2":
				type = XmnConstants.EX_RTYPE_9;
				pmType = XmnConstants.EX_RTYPE_9;
				break;
			case "3":
				type = XmnConstants.EX_RTYPE_8;
				pmType = XmnConstants.EX_RTYPE_8;
				break;
			case "4":
				type = XmnConstants.EX_RTYPE_7;
				pmType = XmnConstants.EX_RTYPE_8;
				recordType = XmnConstants.EX_RECORD_TYPE_10;
				break;
			default:
				log.error("saasChannel参数有误:" + map);
				map.put("bid", orderId);
				map.put("code", "2");
				map.put("remark", "saasChannel参数有误");
				return map;
			}

		}

		List<Map<String, String>> paramMapList = new ArrayList<Map<String, String>>();
		// 寻蜜客对象分账
		List<Map<String, String>> xmerList = new ArrayList<Map<String, String>>();

		// if (!UtilString.stringIsBlank(uId) && !uId.equals("0")
		// && !StringUtils.isEmpty(uMoney) && Double.valueOf(uMoney) != 0) {
		// // 用户map
		// Map<String, String> userMap = new HashMap<String, String>();
		// userMap.put("uId", uId);
		// userMap.put("userType", "1");
		// userMap.put("balanceType", "4");
		// userMap.put("balance", uMoney);
		// userMap.put("orderId", orderId);
		// userMap.put("ledgerType", "1");
		// userMap.put("remark", remark);
		// paramMapList.add(userMap);
		// }

		if (!StringUtils.isEmpty(sId) && !sId.equals("0") && !StringUtils.isEmpty(sMoney)
				&& Double.valueOf(sMoney) != 0) {

			// 消费商家map
			Map<String, String> sellerMap = new HashMap<String, String>();
			sellerMap.put("uId", sId);
			sellerMap.put("userType", "2");
			sellerMap.put("balanceType", "3");
			sellerMap.put("balance", sMoney);
			sellerMap.put("orderId", orderId);
			sellerMap.put("ledgerType", "2");
			sellerMap.put("remark", remark);
			paramMapList.add(sellerMap);
		}

		if (!StringUtils.isEmpty(ssId) && !ssId.equals("0") && !StringUtils.isEmpty(ssMoney)
				&& Double.valueOf(ssMoney) != 0) {

			// 所属商家map
			Map<String, String> sellerMap = new HashMap<String, String>();
			sellerMap.put("uId", ssId);
			sellerMap.put("userType", "2");
			sellerMap.put("balanceType", "1");
			sellerMap.put("balance", ssMoney);
			sellerMap.put("orderId", orderId);
			sellerMap.put("ledgerType", "3");
			sellerMap.put("remark", remark);
			paramMapList.add(sellerMap);
		}

		if (!StringUtils.isEmpty(brId) && !brId.equals("0") && !StringUtils.isEmpty(brMoney)
				&& Double.valueOf(brMoney) != 0) {
			// 所属合作商map
			Map<String, String> brMap = new HashMap<String, String>();
			brMap.put("uId", brId);
			brMap.put("userType", "3");
			brMap.put("balanceType", "3");
			brMap.put("balance", brMoney);
			brMap.put("orderId", orderId);
			brMap.put("ledgerType", "4");
			brMap.put("remark", remark);
			paramMapList.add(brMap);
		}

		if (!StringUtils.isEmpty(crId) && !crId.equals("0") && !StringUtils.isEmpty(crMoney)
				&& Double.valueOf(crMoney) != 0) {
			// 消费合作商map
			Map<String, String> crMap = new HashMap<String, String>();
			crMap.put("uId", crId);
			crMap.put("userType", "3");
			crMap.put("balanceType", "3");
			crMap.put("balance", crMoney);
			crMap.put("orderId", orderId);
			crMap.put("ledgerType", "5");
			crMap.put("remark", remark);
			paramMapList.add(crMap);
		}

		if (!StringUtils.isEmpty(mId) && !mId.equals("0") && !StringUtils.isEmpty(mMoney)
				&& Double.valueOf(mMoney) != 0) {
			// 寻蜜客map
			Map<String, String> mikeMap = new HashMap<>();
			mikeMap.put("uid", mId);
			mikeMap.put("typeId", "1");
			mikeMap.put("type", type + "");
			mikeMap.put("option", "0");
			mikeMap.put("amount", mMoney);
			mikeMap.put("source", orderId + "_6");
			mikeMap.put("remark", remark);
			xmerList.add(mikeMap);
		}

		if (!StringUtils.isEmpty(pmId) && !pmId.equals("0") && !StringUtils.isEmpty(pmMoney)
				&& Double.valueOf(pmMoney) != 0) {
			// 上级寻蜜客map
			Map<String, String> pmMap = new HashMap<String, String>();
			pmMap.put("uid", pmId);
			pmMap.put("typeId", "1");
			pmMap.put("type", pmType + "");
			pmMap.put("option", "0");
			pmMap.put("amount", pmMoney);
			pmMap.put("source", orderId + "_7");
			pmMap.put("remark", remark);
			if (recordType != null) {
				pmMap.put("recordType", recordType + "");
			}
			xmerList.add(pmMap);
		}

		if (!StringUtils.isEmpty(tmId) && !tmId.equals("0") && !StringUtils.isEmpty(tmMoney)
				&& Double.valueOf(tmMoney) != 0) {
			// 上上级寻蜜客map
			Map<String, String> tmMap = new HashMap<String, String>();
			tmMap.put("uid", tmId);
			tmMap.put("typeId", "1");
			tmMap.put("type", type + "");
			tmMap.put("option", "0");
			tmMap.put("amount", tmMoney);
			tmMap.put("source", orderId + "_8");
			tmMap.put("remark", remark);
			xmerList.add(tmMap);
		}

		if (!xmerList.isEmpty()) {
			Map<String, String> map2 = xmerList.get(0);
			 long countBySource = walletExpansionRecordMapper.countBySource(map2.get("source"));
			if (countBySource > 0) {
				log.info("该订单已分账");
				Map<String, String> resultMap = new HashMap<String, String>();
				resultMap.put("bid", orderId);
				resultMap.put("code", "0");
				resultMap.put("remark", "订单已分账");
				return resultMap;
			}

		}
		
		// 分账处理
		map = ledger(paramMapList);
		walletExpansionServiceImpl.updateBatch(xmerList);
		long edate = System.currentTimeMillis();

		long result = edate - sdate;

		log.info("分账运行时间：" + result + "ms");

		log.info("分账结束<---");

		return map;
	}

	@Transactional(rollbackFor = { FailureException.class,
			Exception.class,RuntimeException.class }, isolation = Isolation.SERIALIZABLE, timeout = 100)
	public Map<String, String> doSaasLedger(Map<String, String> ledger) throws FailureException {

		log.info("SAAS订单分账开始--->    参数:" + ledger);
		long sdate = System.currentTimeMillis();

		Map<String, String> map = new HashMap<String, String>();

		String ledgerText = org.apache.commons.lang.StringUtils.isNotBlank(ledger.get("ledgerText"))
				? ledger.get("ledgerText") : "pre_saas_ledger";
		String orderId = ledger.get("orderId"); // 订单号
		String xmerId = ledger.get("xmerId"); // uid
		String xmerBackMoney = ledger.get("xmerBackMoney"); // 分账金额
		String xmerMoney = ledger.get("xmerMoney");
		String oneLevelXmerId = ledger.get("oneLevelXmerId"); // 上上级uid
		String oneLevelXmerMoney = ledger.get("oneLevelXmerMoney"); // 上上级分账金额
		String twoLevelXmerId = ledger.get("twoLevelXmerId"); // 上线寻蜜客uid
		String twoLevelXmerMoney = ledger.get("twoLevelXmerMoney"); // 上线寻蜜客分账金额
		String sellerAreaId = ledger.get("sellerAreaId");
		String sellerAreaMoney = ledger.get("sellerAreaMoney");
		String remark = ledger.get("remark"); // 备注

		if (StringUtils.isEmpty(orderId) || StringUtils.isEmpty(xmerId)) {

			log.error("参数有空值:" + map);
			map.put("bid", orderId);
			map.put("code", "2");
			map.put("remark", "参数有空值");
			return map;
		}

		// 普通对象分账
		List<Map<String, String>> paramMapList = new ArrayList<>();
		// 寻蜜客对象分账
		List<Map<String, String>> xmerList = new ArrayList<>();

		/*
		 * 直接寻蜜客
		 */
		if (!StringUtils.isEmpty(xmerId) && !xmerId.equals("0")) {
			BigDecimal balance = new BigDecimal(0);
			if ((!StringUtils.isEmpty(xmerBackMoney) && Double.valueOf(xmerBackMoney) != 0)) {
				Map<String, String> userBackMap = new HashMap<>();
				userBackMap.put("uid", xmerId);
				userBackMap.put("typeId", "1");
				userBackMap.put("type", XmnConstants.EX_RTYPE_6 + "");
				userBackMap.put("option", "0");
				userBackMap.put("amount", xmerBackMoney);
				userBackMap.put("source", orderId + "_9");
				userBackMap.put("remark", remark);
				xmerList.add(userBackMap);
			}
			if ((!StringUtils.isEmpty(xmerMoney) && Double.valueOf(xmerMoney) != 0)) {
				Map<String, String> userMap = new HashMap<String, String>();
				userMap.put("uid", xmerId);
				userMap.put("typeId", "1");
				userMap.put("type", XmnConstants.EX_RTYPE_6 + "");
				userMap.put("option", "0");
				userMap.put("amount", xmerMoney);
				userMap.put("source", orderId + "_6");
				userMap.put("remark", remark);
				xmerList.add(userMap);
			}

		}

		/*
		 * 上线寻蜜客
		 */
		if (!StringUtils.isEmpty(twoLevelXmerId) && !twoLevelXmerId.equals("0")
				&& !StringUtils.isEmpty(twoLevelXmerMoney) && Double.valueOf(twoLevelXmerMoney) != 0) {
			Map<String, String> mikeMap = new HashMap<String, String>();
			mikeMap.put("uid", twoLevelXmerId);
			mikeMap.put("typeId", "1");
			mikeMap.put("type", XmnConstants.EX_RTYPE_6 + "");
			mikeMap.put("option", "0");
			mikeMap.put("amount", twoLevelXmerMoney);
			mikeMap.put("source", orderId + "_" + (isPreSaasLedger(ledgerText) ? "10" : "7"));
			mikeMap.put("remark", remark);
			xmerList.add(mikeMap);
		}

		/*
		 * 上上级寻蜜客
		 */
		if (!StringUtils.isEmpty(oneLevelXmerId) && !oneLevelXmerId.equals("0")
				&& !StringUtils.isEmpty(oneLevelXmerMoney) && Double.valueOf(oneLevelXmerMoney) != 0) {

			Map<String, String> sellerMap = new HashMap<String, String>();
			sellerMap.put("uid", oneLevelXmerId);
			sellerMap.put("typeId", "1");
			sellerMap.put("type", XmnConstants.EX_RTYPE_6 + "");
			sellerMap.put("option", "0");
			sellerMap.put("amount", oneLevelXmerMoney);
			sellerMap.put("source", orderId + "_" + (isPreSaasLedger(ledgerText) ? "11" : "8"));
			sellerMap.put("remark", remark);
			xmerList.add(sellerMap);
		}

		/*
		 * 合作商
		 */
		if (!StringUtils.isEmpty(sellerAreaId) && !sellerAreaId.equals("0") && !StringUtils.isEmpty(sellerAreaMoney)
				&& Double.valueOf(sellerAreaMoney) != 0) {
			// 所属合作商map
			Map<String, String> brMap = new HashMap<String, String>();
			brMap.put("uId", sellerAreaId);
			brMap.put("userType", "3");
			brMap.put("balanceType", "3");
			brMap.put("balance", sellerAreaMoney);
			brMap.put("orderId", orderId);
			brMap.put("ledgerType", "4");
			brMap.put("remark", remark);
			brMap.put("rtype", XmnConstants.RTYPE_24 + "");
			paramMapList.add(brMap);
		}

		if (!xmerList.isEmpty()) {
			Map<String, String> map2 = xmerList.get(0);
			 long countBySource = walletExpansionRecordMapper.countBySource(map2.get("source"));
			if (countBySource > 0) {
				log.info("该订单已分账");
				Map<String, String> resultMap = new HashMap<String, String>();
				resultMap.put("bid", orderId);
				resultMap.put("code", "0");
				resultMap.put("remark", "订单已分账");
				return resultMap;
			}

		}
		// 分账处理
		map = ledger(paramMapList);
		walletExpansionServiceImpl.updateBatch(xmerList);

		long edate = System.currentTimeMillis();

		long result = edate - sdate;

		log.info("分账运行时间：" + result + "ms");

		log.info("分账结束<---");

		return map;
	}

	/**
	 * 修改钱包余额（分账接口）事务处理
	 * 
	 * @param walletMap
	 * @throws Exception
	 */
	// @Transactional(rollbackFor = { FailureException.class, Exception.class },
	// isolation = Isolation.SERIALIZABLE, timeout = 100)
	@Override
	public Map<String, String> ledger(List<Map<String, String>> paramMapList) throws FailureException {

		Map<String, String> resultMap = new HashMap<String, String>();

		List<Map<String, Object>> amountList = new ArrayList<Map<String, Object>>();
		String orderId = "";
		Map<String, Integer> reMap = new HashMap<String, Integer>();

		log.info("本单的分账列表为：" + paramMapList);
		if (paramMapList.size() == 0) {
			resultMap.put("bid", orderId);
			resultMap.put("code", "0");
			resultMap.put("remark", "订单" + orderId + "分账成功");
			return resultMap;
		}
		for (int a = 0, len = paramMapList.size(); a < len; a++) {
			Map<String, Object> amountMap = new HashMap<String, Object>();
			Map<String, String> map = paramMapList.get(a);
			if ((map.get("userType") == null || ("0").equals(map.get("userType")) || map.get("userType").length() == 0)
					|| (map.get("uId") == null || ("0").equals(map.get("uId")) || map.get("uId").length() == 0)) {
				log.error("该用户不存在！map:" + map);
				resultMap.put("bid", orderId);
				resultMap.put("code", "2");
				resultMap.put("remark", "订单" + orderId + "中有用户不存在");
				return resultMap;
			}

			log.info("分账reMap:" + reMap);
			if (reMap != null && reMap.containsKey(map.get("uId") + "_" + map.get("userType"))) {
				amountMap = amountList.get(reMap.get(map.get("uId") + "_" + map.get("userType")));
				orderId = String.valueOf(map.get("orderId"));// 订单ID
				String balanceType = (String) map.get("balanceType");
				String accountId = String.valueOf(amountMap.get("accountid"));// 账户ID

				String money = map.get("balance"); // 分配的钱

				// 获取之前分配的金额
				double balance_d = Double.valueOf(String.valueOf(amountMap.get("balance_d"))); // 返利余额
				double commision_d = Double.valueOf(String.valueOf(amountMap.get("commision_d"))); // 佣金余额
				double seller_amount_d = Double.valueOf(String.valueOf(amountMap.get("seller_amount_d"))); // 营业余额
				double zbalance_d = Double.valueOf(String.valueOf(amountMap.get("zbalance_d"))); // 赠送金额

				double money_d = Double.valueOf(money); // 分配的钱

				int balanceType_d = Integer.valueOf(balanceType); // 金（余）额类型

				if (balanceType_d == 1) {
					commision_d = commision_d + money_d;

					amountMap
							.put("qcommision",
									balanceType.equals("1")
											? Double.valueOf(String.valueOf(amountMap.get("commision_d")))
													- Double.valueOf(String.valueOf(amountMap.get("commision")))
											: "0.00"); // 分账前佣金余额
					amountMap.put("commision",
							balanceType.equals("1")
									? money_d + Double.valueOf(String.valueOf(amountMap.get("commision")))
									: amountMap.get("commision"));// 佣金金额
					amountMap.put("hcommision", String.valueOf(commision_d));// 分账后佣余额
				} else if (balanceType_d == 2) {
					balance_d = balance_d + money_d;

					amountMap
							.put("qrebate",
									balanceType.equals("2")
											? Double.valueOf(String.valueOf(amountMap.get("balance")))
													- Double.valueOf(String.valueOf(amountMap.get("balance")))
											: "0.00"); // 分账前返利余额
					amountMap.put("balance",
							balanceType.equals("2") ? money_d + Double.valueOf(String.valueOf(amountMap.get("balance")))
									: amountMap.get("balance"));// 返利金额
					amountMap.put("hrebate", String.valueOf(balance_d)); // 分账后返利余额
				} else if (balanceType_d == 3) {
					seller_amount_d = seller_amount_d + money_d;

					amountMap.put("qincome",
							balanceType.equals("3") ? Double.valueOf(String.valueOf(amountMap.get("seller_amount_d")))
									- Double.valueOf(String.valueOf(amountMap.get("income"))) : "0.00"); // 分账前营业余额
					amountMap.put("income",
							balanceType.equals("3") ? money_d + Double.valueOf(String.valueOf(amountMap.get("income")))
									: amountMap.get("income"));// 营业收入
					amountMap.put("hincome", String.valueOf(seller_amount_d)); // 分账后营业余额
				} else if (balanceType_d == 4) {
					zbalance_d = zbalance_d + money_d;

					amountMap
							.put("qzbalance",
									balanceType.equals("4")
											? Double.valueOf(String.valueOf(amountMap.get("zbalance_d")))
													- Double.valueOf(String.valueOf(amountMap.get("zbalance")))
											: "0.00"); // 分账前赠送余额
					amountMap.put("zbalance",
							balanceType.equals("4")
									? money_d + Double.valueOf(String.valueOf(amountMap.get("zbalance")))
									: amountMap.get("zbalance")); // 赠送余额
					amountMap.put("hzbalance", String.valueOf(zbalance_d)); // 分账后赠送余额
				}

				// 2015年4月22日14:47:00添加
				// 组装手续费
				double expenses = Double.parseDouble(String.valueOf(amountMap.get("expenses")));
				expenses = Double.parseDouble(map.get("expenses") == null ? "0" : map.get("expenses")) + expenses;

				// 签名
				double userType = Double.valueOf(String.valueOf(map.get("userType"))); // 用户类型
				Map<String, String> signMap = new HashMap<String, String>();
				signMap.put("uid", userType == 1 ? map.get("uId") : "0");
				signMap.put("sellerid", userType == 2 ? map.get("uId") : "0");
				signMap.put("jointid", userType == 3 ? map.get("uId") : "0");
				signMap.put("amount", String.valueOf(amountMap.get("amount_d")));
				signMap.put("balance", String.valueOf(balance_d));
				signMap.put("commision", String.valueOf(commision_d));
				signMap.put("sellerAmount", String.valueOf(seller_amount_d));
				signMap.put("zbalance", String.valueOf(zbalance_d));
				signMap.put("integral", String.valueOf(amountMap.get("integral_d")));
				signMap.put("pwd", String.valueOf(amountMap.get("p_pwd")));
				String signStr = commonService.walletSign(signMap);// 获取新密钥

				amountMap.put("uId", Integer.parseInt(map.get("uId")));
				amountMap.put("userType", map.get("userType")); // 用户类型
				amountMap.put("balanceType", balanceType);
				amountMap.put("sign", signStr);
				amountMap.put("oldSign", amountMap.get("oldSign"));
				amountMap.put("commision_d", commision_d);
				amountMap.put("balance_d", balance_d);
				amountMap.put("seller_amount_d", seller_amount_d);
				amountMap.put("zbalance_d", zbalance_d);
				amountMap.put("accountid", accountId); // 钱包id

				amountMap.put("amount", "0.00");// 钱包金额
				amountMap.put("qamount", "0.00");// 充值前钱包余额
				amountMap.put("hamount", "0.00");// 充值后钱包余额
				amountMap.put("Integral", "0.00"); // 积分数
				amountMap.put("qIntegral", "0");// 写入积分前剩余积分
				amountMap.put("hIntegral", "0");// 写入积分后剩余积分
				amountMap.put("sdate", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));// 记录时间
				amountMap.put("rtype", amountMap.get("rtype")); // 记录类型
				amountMap.put("orderId", amountMap.get("orderId"));// 分账订单Id
				amountMap.put("ledgerType", amountMap.get("ledgerType"));// 分账用户类型
				amountMap.put("remark", map.get("remark"));// 描述

				// 2015年4月22日14:47:00添加

				amountMap.put("expenses", map.get("expenses") == null ? "0" : expenses);

				log.info("amountMap的数据" + amountMap);

				amountList.set(reMap.get(map.get("uId") + "_" + map.get("userType")), amountMap);

			} else {
				Map<String, String> result = new HashMap<String, String>();
				result = updateWalletBalanceMapper.getWalletByUserId(map);
				// 签名Map
				Map<String, String> signMap = new HashMap<String, String>();

				reMap.put(map.get("uId") + "_" + map.get("userType"), amountList.size());
				String accountId = "";// 账户ID
				orderId = String.valueOf(map.get("orderId"));// 订单ID
				String ledgerType = String.valueOf(map.get("ledgerType"));// 分账用户类型
				String balanceType = (String) map.get("balanceType");
				// String rtype="4".equals(balanceType.trim()) ? "3" : "0";
				String rtype = map.get("rtype");
				if (rtype == null) {
					rtype = "0";
				}
				if (result == null) {
					log.error("该用户不存在！map:" + map);
					log.error("订单" + orderId + "中有用户不存在");
					try {
						int code = synthesizeService.addWallet(map.get("uId"), map.get("userType"), "", "");
						if (code != 0) {
							throw new Exception("添加钱包失败");
						}
						result = updateWalletBalanceMapper.getWalletByUserId(map);
						System.out.println("新增钱包后：" + result);
					} catch (Exception e) {
						log.error("添加钱包失败", e);
						throw new FailureException(2, "订单" + orderId + "中有用户没有钱包");
					}

				}
				if (result == null) {
					log.error("该用户不存在且添加失败！map:" + map);
					log.error("订单" + orderId + "中有用户不存在");

					throw new FailureException(2, "订单" + orderId + "中有用户没有钱包且添加失败");
				} else {
					signMap.putAll(result);
					Map<String, Object> wrMap = new HashMap<String, Object>();
					accountId = String.valueOf(result.get("accountid"));// 账户ID
					wrMap.put("accountId", accountId);
					wrMap.put("orderId", orderId);
					wrMap.put("ledgerType", ledgerType);
					wrMap.put("rtype", rtype);
					int wrCount = updateWalletBalanceMapper.getWRCount(wrMap);
					log.info("分账订单重复数：" + wrCount);
					if (wrCount > 0) {
						log.info("重复分账订单");
						log.info("返回的参数：" + 0);
						throw new RepetitionLedgerException("重复分账:"+orderId);
					}
				}

				signMap.put("sellerAmount", String.valueOf(result.get("seller_amount")));
				signMap.put("integral", String.valueOf(result.get("integral")));
				signMap.put("pwd", String.valueOf(result.get("p_pwd")));

				// 旧密钥
				String getSign = commonService.walletSign(signMap);
				if (getSign.equals(signMap.get("sign")) == false) {
					log.error("订单" + orderId + "中有用户钱包密钥不一致");

					throw new FailureException(3, "订单" + orderId + "中有用户钱包密钥不一致");
				}
				String balance = String.valueOf(result.get("balance")); // 返利余额
				String commision = String.valueOf(result.get("commision")); // 佣金余额
				String seller_amount = String.valueOf(result.get("seller_amount")); // 营业余额
				String zbalance = String.valueOf(result.get("zbalance")); // 赠送余额
				String money = map.get("balance"); // 分配的钱

				// 获取旧钱包金额
				double balance_d = Double.valueOf(balance); // 返利余额
				double commision_d = Double.valueOf(commision); // 佣金余额
				double seller_amount_d = Double.valueOf(seller_amount); // 营业余额
				double zbalance_d = Double.valueOf(zbalance); // 赠送金额
				double money_d = Double.valueOf(money); // 分配的钱
				int balanceType_d = Integer.valueOf(balanceType); // 金（余）额类型

				// 组装新钱包金额
				if (balanceType_d == 1) {
					commision_d = commision_d + money_d;
				} else if (balanceType_d == 2) {
					balance_d = balance_d + money_d;
				} else if (balanceType_d == 3) {
					seller_amount_d = seller_amount_d + money_d;
				} else if (balanceType_d == 4) {
					zbalance_d = zbalance_d + money_d;
				}

				// 对新钱包余额数据签名
				signMap.put("balance", String.valueOf(balance_d));
				signMap.put("commision", String.valueOf(commision_d));
				signMap.put("sellerAmount", String.valueOf(seller_amount_d));
				signMap.put("zbalance", String.valueOf(zbalance_d));
				signMap.put("integral", String.valueOf(result.get("integral")));
				signMap.put("pwd", result.get("p_pwd"));
				String signStr = commonService.walletSign(signMap);

				amountMap.put("uId", Integer.parseInt(map.get("uId")));
				amountMap.put("p_pwd", result.get("p_pwd"));
				amountMap.put("userType", map.get("userType")); // 用户类型
				amountMap.put("sign", signStr);
				amountMap.put("oldSign", result.get("sign"));
				amountMap.put("commision_d", commision_d);
				amountMap.put("balance_d", balance_d);
				amountMap.put("seller_amount_d", seller_amount_d);
				amountMap.put("zbalance_d", zbalance_d);
				amountMap.put("integral_d", String.valueOf(result.get("integral")));
				amountMap.put("amount_d", String.valueOf(result.get("amount")));
				amountMap.put("balanceType", balanceType);
				amountMap.put("accountid", accountId); // 钱包id
				amountMap.put("balance", balanceType.equals("2") ? money : "0.00");// 返利金额
				amountMap.put("commision", balanceType.equals("1") ? money : "0.00");// 佣金金额
				amountMap.put("income", balanceType.equals("3") ? money : "0.00");// 营业收入
				amountMap.put("qrebate", balanceType.equals("2") ? balance : "0.00"); // 分账前返利余额
				amountMap.put("hrebate", balanceType.equals("2") ? String.valueOf(balance_d) : "0.00"); // 分账后返利余额
				amountMap.put("qcommision", balanceType.equals("1") ? commision : "0.00"); // 分账前佣金余额
				amountMap.put("hcommision", balanceType.equals("1") ? String.valueOf(commision_d) : "0.00");// 分账后佣余额
				amountMap.put("qincome", balanceType.equals("3") ? seller_amount : "0.00"); // 分账前营业余额
				amountMap.put("hincome", balanceType.equals("3") ? String.valueOf(seller_amount_d) : "0.00"); // 分账后营业余额
				amountMap.put("zbalance", balanceType.equals("4") ? money : "0.00"); // 赠送余额
				amountMap.put("qzbalance", balanceType.equals("4") ? zbalance : "0.00"); // 分账前赠送余额
				amountMap.put("hzbalance", balanceType.equals("4") ? String.valueOf(zbalance_d) : "0.00"); // 分账后赠送余额
				amountMap.put("amount", "0.00");// 钱包金额
				amountMap.put("qamount", "0.00");// 充值前钱包余额
				amountMap.put("hamount", "0.00");// 充值后钱包余额
				amountMap.put("Integral", "0.00"); // 积分数
				amountMap.put("qIntegral", "0");// 写入积分前剩余积分
				amountMap.put("hIntegral", "0");// 写入积分后剩余积分
				amountMap.put("sdate", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));// 记录时间
				amountMap.put("rtype", rtype); // 记录类型
				amountMap.put("orderId", orderId);// 分账订单Id
				amountMap.put("ledgerType", ledgerType);// 分账用户类型
				amountMap.put("remark", map.get("remark"));// 描述

				// 2015年4月22日14:47:00添加
				amountMap.put("expenses", map.get("expenses") == null ? "0" : map.get("expenses"));

				log.info("amountMap的数据" + amountMap);
				amountList.add(amountMap);
			}
		}

		int result1 = 0;
		for (Map<String, Object> ammountMap : amountList) {

			result1 = updateWalletBalanceMapper.updateBalanceType(ammountMap);
			if (result1 != 1) {
				log.error("修改钱包失败" + ammountMap);

				throw new FailureException(3, "修改钱包失败");
			}
		}
		if (result1 == 1) {
			result1 = updateWalletBalanceMapper.insertWR(amountList);
			if (result1 == amountList.size()) {
				resultMap.put("bid", orderId);
				resultMap.put("code", "0");
				resultMap.put("remark", "订单" + orderId + "分账成功");
				log.info("分账成功");
			} else {
				log.error("分账记录插入失败");

				throw new FailureException(3, "分账记录插入失败");
			}
			log.info("修改成功" + amountList.toString());
		} else {
			log.error("修改异常" + amountList.toString());

			throw new FailureException(3, "新增记录失败");

		}
		return resultMap;
	}

	/**
	 * 修改钱包余额（分账接口）事务处理
	 * 
	 * @param walletMap
	 * @throws Exception
	 */
	// @Transactional(rollbackFor = { FailureException.class, Exception.class },
	// isolation = Isolation.SERIALIZABLE, timeout = 100)
	public Map<String, String> xmerLedger(List<Map<String, String>> paramMapList) throws FailureException {
		log.info("参与分账的寻蜜客分账信息：" + paramMapList);

		Map<String, String> resultMap = new HashMap<String, String>();
		String orderId = null;
		String uId = null;
		if (paramMapList != null && paramMapList.size() > 0) {
			Map<String, String> rMap = paramMapList.get(0);
			String oldOrderId = rMap.get("orderId");
			if (rMap.get("rtype").equals(XmnConstants.XMER_RTYPE_2 + "")) {
				// SAAS签约订单将进行两次分账，一次支付立即分账，一次审核上线分账
				rMap.put("orderId", rMap.get("orderId") + "_" + rMap.get("ledgerType"));
			}
			int count = xmerWalletMapper.countWalletRecord(rMap);
			rMap.put("orderId", oldOrderId);
			if (count == 0) {
				for (Map<String, String> updateMap : paramMapList) {
					orderId = updateMap.get("orderId");
					uId = updateMap.get("uId");
					// 更新 佣金金额
					if (updateMap.get("balance") == null
							|| ZERO.compareTo(new BigDecimal(updateMap.get("balance"))) == 0) {
						continue;
					}

					Map<String, String> xmerWallet = xmerWalletMapper.selectXmerWallet(updateMap);
					if (xmerWallet == null) {
						log.error("找不到对应钱包，现给其新增寻蜜客钱包");
						Map<String, Object> sMap = new HashMap<String, Object>();
						sMap.put("uId", updateMap.get("uid"));
						sMap.put("typeId", 1);
						Map<String, Object> wMap = walletDao.selectByuid(sMap);
						if (wMap == null) {
							Map<String, String> amap = new HashMap<String, String>();
							amap.put("uId", updateMap.get("uid"));
							amap.put("userType", "1");
							try {
								Map<String, String> map = synthesizeService.addWalletMap(amap);
								if (!map.get("state").equals("0")) {
									throw new Exception();
								}
								wMap = walletDao.selectByuid(sMap);
							} catch (Exception e) {
								log.error("给寻蜜客的会员" + uId + "新增钱包异常", e);
								throw new FailureException(2, "寻蜜客的会员" + updateMap.get("uId") + "添加钱包失败");
							}
						}
						Map<String, String> newMap = new HashMap<String, String>();
						newMap.put("uid", updateMap.get("uid"));
						newMap.put("account", wMap.get("account") == null ? "" : String.valueOf(wMap.get("account")));
						newMap.put("uname", updateMap.get("remark"));
						try {
							xmerWalletService.addXmerWallet(newMap);
						} catch (Exception e) {
							log.error("给寻蜜客" + uId + "新增钱包异常", e);
							throw new FailureException(2, "寻蜜客" + updateMap.get("uId") + "添加钱包失败，系统异常");
						}
						xmerWallet = xmerWalletMapper.selectXmerWallet(updateMap);
					}
					if (!xmerWallet.get("status").equals("1")) {
						log.error("钱包:" + xmerWallet.get("id") + "状态异常");
						throw new FailureException(3, "订单" + updateMap.get("orderId") + "钱包状态异常");
					}
					if (!xmerWallet.get("sign").equals(commonService.XmerWalletSign(xmerWallet))) {
						log.error("钱包:" + xmerWallet.get("id") + "签名异常");
						throw new FailureException(3, "订单" + updateMap.get("orderId") + "钱包签名异常");
					}

					String oldSign = xmerWallet.get("sign");
					BigDecimal qprofit = new BigDecimal(xmerWallet.get("profit"));
					BigDecimal profit = new BigDecimal(updateMap.get("balance"));
					BigDecimal hprofit = qprofit.add(profit);

					xmerWallet.put("lastDate", UtilString.dateFormatNow());
					xmerWallet.put("profit", hprofit.toString());
					xmerWallet.put("sign", commonService.XmerWalletSign(xmerWallet));
					xmerWallet.put("oldSign", oldSign);
					int index = xmerWalletMapper.updateWalletById(xmerWallet);
					if (index == 0) {
						log.error("钱包:" + xmerWallet.get("id") + "数据已被修改，需重试");
						throw new FailureException(3, "订单" + updateMap.get("orderId") + "数据已被修改，需重试");
					}
					Map<String, String> recordMap = new HashMap<String, String>();
					recordMap.put("xid", xmerWallet.get("id"));
					recordMap.put("rtype", updateMap.get("rtype"));
					recordMap.put("profit", updateMap.get("balance"));
					recordMap.put("qprofit", qprofit.toString());
					recordMap.put("hprofit", xmerWallet.get("profit"));
					recordMap.put("sdate", xmerWallet.get("lastDate"));
					recordMap.put("remark", updateMap.get("orderId") + "_" + updateMap.get("ledgerType"));
					recordMap.put("description", updateMap.get("remark"));
					xmerWalletMapper.insertXmerWalletRecord(recordMap);

				}
				log.info("分账成功");
				resultMap.put("bid", orderId);
				resultMap.put("code", "0");
				resultMap.put("remark", "分账成功");
			}
		} else {
			log.info("该订单已分账");
			resultMap.put("bid", orderId);
			resultMap.put("code", "0");
			resultMap.put("remark", "分账成功");
		}
		return resultMap;

	}
	/**
	 * 钱包加密
	 * 
	 * @param signMap
	 * @return
	 */
	// public String sign(Map<String, String> signMap) {
	// StringBuffer sb = new StringBuffer();
	// sb.append(String.valueOf(signMap.get("uid")));
	// sb.append(String.valueOf(signMap.get("sellerid")));
	// sb.append(String.valueOf(signMap.get("jointid")));
	// sb.append(signMap.get("pwd") == null
	// || String.valueOf(signMap.get("pwd")).equals("null") ? ""
	// : String.valueOf(signMap.get("pwd")));
	// sb.append(String.format("%.2f",
	// Double.valueOf(String.valueOf(signMap.get("amount")))));
	// sb.append(String.format("%.2f",
	// Double.valueOf(String.valueOf(signMap.get("balance")))));
	// sb.append(String.format("%.2f",
	// Double.valueOf(String.valueOf(signMap.get("commision")))));
	// sb.append(String.format("%.2f",
	// Double.valueOf(String.valueOf(signMap.get("zbalance")))));
	// sb.append(String.valueOf((new
	// BigDecimal(signMap.get("Integral")).longValue())));
	// sb.append(String.format("%.2f",
	// Double.valueOf(String.valueOf(signMap.get("sellerAmount")))));
	//
	// log.info("钱包加密：" + sb.toString());
	//
	// return MD5.Encode(sb.toString());
	// }

	/**
	 * 积分商城订单分账明细 { "orderId":"", // 订单ID "dealType":"", //交易类型（1线上，2线下）
	 * "memberId":"", // 消费用户ID "memberBackMoney":"", // 消费用户返现 "sellerId":"",
	 * // 消费商家ID "sellerBackMoney":"", // 商家成本金额 "bSellerId":"", // 所属商家Id
	 * "bSellerMoney":"", // 所属商家提成 "sellerAreaId":"", //商户所在区域Id
	 * "sellerAreaMoney":"", //商户所在区域提成 "remark": "" //描述（消费商家名称，平台名称） }
	 */
	@Transactional(rollbackFor = { FailureException.class,
			Exception.class,RuntimeException.class }, isolation = Isolation.SERIALIZABLE, timeout = 100)
	@Override
	public Map<String, String> doMallLedger(Map<String, String> ledger) throws FailureException {

		log.info("积分商城/积分商品订单分账开始--->" + ledger);
		long sdate = System.currentTimeMillis();

		Map<String, String> map = new HashMap<String, String>();

		String orderId = ledger.get("orderId");
		String memberId = ledger.get("memberId");
		String memberBackMoney = ledger.get("memberBackMoney");
		String sellerId = ledger.get("sellerId");
		String sellerBackMoney = ledger.get("sellerBackMoney");
		String bSellerId = ledger.get("bSellerId");
		String bSellerMoney = ledger.get("bSellerMoney");
		String sellerAreaId = ledger.get("sellerAreaId");
		String sellerAreaMoney = ledger.get("sellerAreaMoney");
		String dealType = ledger.get("dealType");
		String remark = ledger.get("remark");
		String xmniaoRtype = XmnConstants.RTYPE_22 + "";
		if (dealType != null && dealType.equals("1")) {
			xmniaoRtype = XmnConstants.RTYPE_34 + "";
		}
		if (StringUtils.isEmpty(orderId)/* || StringUtils.isEmpty(sellerId) */) {

			log.error("参数有空值:" + map);
			map.put("bid", orderId);
			map.put("code", "2");
			map.put("remark", "参数有空值");
			return map;
		}

		// 普通对象分账
		List<Map<String, String>> paramMapList = new ArrayList<Map<String, String>>();
		// 寻蜜客对象分账
		List<Map<String, String>> xmerList = new ArrayList<Map<String, String>>();

		/*
		 * 商家
		 */
		if (!StringUtils.isEmpty(sellerId) && !sellerId.equals("0")
				&& (!StringUtils.isEmpty(sellerBackMoney) && Double.valueOf(sellerBackMoney) != 0)) {
			BigDecimal balance = new BigDecimal(sellerBackMoney).setScale(2);

			Map<String, String> userMap = new HashMap<String, String>();
			userMap.put("uId", sellerId);
			userMap.put("userType", "2");
			userMap.put("balanceType", "3");
			userMap.put("balance", balance.toString());
			userMap.put("orderId", orderId);
			userMap.put("ledgerType", "2");
			userMap.put("remark", remark);
			userMap.put("rtype", xmniaoRtype);
			paramMapList.add(userMap);
		}

		/*
		 * 用户
		 */
		if (!StringUtils.isEmpty(memberId) && !memberId.equals("0") && !StringUtils.isEmpty(memberBackMoney)
				&& Double.valueOf(memberBackMoney) != 0) {

			Map<String, String> sellerMap = new HashMap<String, String>();
			sellerMap.put("uId", memberId);
			sellerMap.put("userType", "1");
			sellerMap.put("balanceType", "1");
			sellerMap.put("balance", memberBackMoney);
			sellerMap.put("orderId", orderId);
			sellerMap.put("ledgerType", "1");
			sellerMap.put("remark", remark);
			sellerMap.put("rtype", xmniaoRtype);
			paramMapList.add(sellerMap);
		}

		/*
		 * 所属商家
		 */
		if (!StringUtils.isEmpty(bSellerId) && !bSellerId.equals("0") && !StringUtils.isEmpty(bSellerMoney)
				&& Double.valueOf(bSellerMoney) != 0) {
			Map<String, String> mikeMap = new HashMap<String, String>();
			mikeMap.put("uId", bSellerId);
			mikeMap.put("userType", "2");
			mikeMap.put("balanceType", "1");
			mikeMap.put("balance", bSellerMoney);
			mikeMap.put("orderId", orderId);
			mikeMap.put("ledgerType", "3");
			mikeMap.put("remark", remark);
			mikeMap.put("rtype", xmniaoRtype);
			paramMapList.add(mikeMap);
		}

		/*
		 * 合作商
		 */
		if (!StringUtils.isEmpty(sellerAreaId) && !sellerAreaId.equals("0") && !StringUtils.isEmpty(sellerAreaMoney)
				&& Double.valueOf(sellerAreaMoney) != 0) {
			// 所属合作商map
			Map<String, String> brMap = new HashMap<String, String>();
			brMap.put("uId", sellerAreaId);
			brMap.put("userType", "3");
			brMap.put("balanceType", "3");
			brMap.put("balance", sellerAreaMoney);
			brMap.put("orderId", orderId);
			brMap.put("ledgerType", "4");
			brMap.put("remark", remark);
			brMap.put("rtype", xmniaoRtype);
			paramMapList.add(brMap);
		}

		// 分账处理
		map = ledger(paramMapList);

		long edate = System.currentTimeMillis();

		long result = edate - sdate;

		log.info("分账运行时间：" + result + "ms");

		log.info("分账结束<---");

		return map;

	}

	private boolean isPreSaasLedger(String ledgerText) {
		return ledgerText.equals("pre_saas_ledger");
	}

	@Transactional
	@Override
	public Map<String, String> doPackageLeger(Map<String, String> paramMap) throws TException {
		log.info("[开始调用积分商城套餐分账方法]参数:"+paramMap);
		try {
			PackageParam packageParam =new PackageParam(paramMap);
			if(walletExpansionRecordMapper.countBySource(packageParam.getOrderId()+PackageParam.TWOLEVEL_ORDER_SUFFIX)>0){
				return msgResult.success(packageParam.getOrderId(),"分商城套餐分账 订单"+packageParam.getOrderId()+"  已分账" );
			}
			DataResponse leger = packageParam.leger();
			if(ParamUtil.SUCCESS_STATE!=leger.getState()){
				throw new FailureException(ParamUtil.ERROR_STATE, leger.getMsg());
			}
			return msgResult.success(packageParam.getOrderId(),"分商城套餐分账 订单"+packageParam.getOrderId()+"  分账成功" );
		} catch (ParamBlankException e) {
			log.info("[调用积分商城套餐分账参数有误]"+e.getMessage());
			return msgResult.fail(paramMap.get("orderId"), e.getMessage());
		}
	}

	public static class msgResult{
		/**
		 * 订单号
		 */
		String bid;
		/**
		 * 状态
		 */
		Integer code;
		/**
		 * 信息
		 */
		String remark;
		
		public final static Integer SUCCESS_STATE = 0;
		
		public final static Integer FAIL_STATE = 2;
		
		static Map<String,String> success(String bid,String remark){
			Map<String,String> resultMap=new HashMap<>();
			resultMap.put("bid", bid);
			resultMap.put("remark", remark);
			resultMap.put("code", SUCCESS_STATE.toString());
			return resultMap;
		}
		
		static Map<String,String> fail(String bid,String remark){
			Map<String,String> resultMap=new HashMap<>();
			resultMap.put("bid", bid);
			resultMap.put("remark", remark);
			resultMap.put("code", FAIL_STATE.toString());
			return resultMap;
		}
		
	}
	
	private class PackageParam {
		/**
		 * 订单号
		 */
		String orderId;
		
		/**
		 * 上级V客uid
		 */
		Integer twoLevelXmerId;
		
		/**
		 * 上级V客分账金额
		 */
		BigDecimal twoLevelXmerMoney;
		
		/**
		 * 上上级V客uid
		 */
		Integer oneLevelXmerId;
		
		/**
		 * 上上级V客分账金额
		 */
		BigDecimal oneLevelXmerMoney;
		
		String remark;
		
		/**
		 * 上上级订单后缀
		 */
		final static String ONELEVEL_ORDER_SUFFIX = "_1";
		
		/**
		 * 上级订单后缀
		 */
		final static String TWOLEVEL_ORDER_SUFFIX = "_2";
		
		boolean isFail = false;
		
		/**
		 * 上上级V客是否已分账
		 */
		private boolean oneLevelLegered = false;
		
		/**
		 * 上级V客是否已分账
		 */
		private boolean twoLevelLegered = false;
		
		private PackageParam(Map<String, String> paramMap) throws ParamBlankException{
			ParamUtil.checkMustParam(paramMap,"orderId","twoLevelXmerId","twoLevelXmerMoney");
			this.orderId=paramMap.get("orderId");
			this.twoLevelXmerId=Integer.valueOf(paramMap.get("twoLevelXmerId"));
			this.twoLevelXmerMoney=new BigDecimal(paramMap.get("twoLevelXmerMoney"));
			this.remark=paramMap.get("remark");
			if(org.apache.commons.lang.StringUtils.isNotBlank(paramMap.get("oneLevelXmerId"))){
				this.oneLevelXmerId=Integer.valueOf(paramMap.get("oneLevelXmerId"));
			}
			if(org.apache.commons.lang.StringUtils.isNotBlank(paramMap.get("oneLevelXmerMoney"))){
				this.oneLevelXmerMoney=new BigDecimal(paramMap.get("oneLevelXmerMoney"));
			}
		}

		public DataResponse leger() throws FailureException, TException {
			DataResponse legerTwoLevelXmer = legerTwoLevelXmer();
			if(isFail){
				return legerTwoLevelXmer;
			}
			if(oneLevelXmerId!=null&&oneLevelXmerMoney!=null){
				DataResponse legerOwoLevelXmer = legerOwoLevelXmer();
				if(isFail){
					return legerOwoLevelXmer;
				}
			}
			return legerTwoLevelXmer;
		}

		/**
		 * 
		 * 方法描述：给上级V客分账
		 * 创建人：jianming  
		 * 创建时间：2017年8月11日 下午3:54:08   
		 * @return
		 * @throws FailureException
		 * @throws TException
		 */
		public DataResponse legerTwoLevelXmer() throws FailureException, TException {
			log.info("开始给上级V客分账  uid:"+twoLevelXmerId+" 金额:"+twoLevelXmerMoney);
			if(twoLevelLegered){
				return ParamUtil.failDataResponse("上级V客已分账");
			}
			if(this.twoLevelXmerMoney.compareTo(BigDecimal.ZERO)<=0){
				return ParamUtil.failDataResponse("上级V客分账金额为0");
			}
			Map<String, String> twoLevelXmer = new HashMap<>();
			twoLevelXmer.put("uid", this.twoLevelXmerId.toString());
			twoLevelXmer.put("typeId", "1");
			twoLevelXmer.put("type", String.valueOf(XmnConstants.EX_RTYPE_1));
			twoLevelXmer.put("option", "0");
			twoLevelXmer.put("amount", this.twoLevelXmerMoney.toString());
			twoLevelXmer.put("remark", this.remark);
			twoLevelXmer.put("source", this.orderId+TWOLEVEL_ORDER_SUFFIX);
			Map<String,String> updateExpansionAmount = walletExpansionServiceImpl.updateWalletExpansion(twoLevelXmer);
			if(ParamUtil.SUCCESS_STATE.toString().equals(updateExpansionAmount.get("state"))){
				twoLevelLegered=true;
				return ParamUtil.successDataResponse(updateExpansionAmount.get("msg"));
			}else{
				isFail=true;
				return ParamUtil.failDataResponse(updateExpansionAmount.get("msg"));
			}
		}
		
		/**
		 * 
		 * 方法描述：给上上级V客分账
		 * 创建人：jianming  
		 * 创建时间：2017年8月11日 下午3:54:30   
		 * @return
		 * @throws FailureException
		 * @throws TException
		 */
		public DataResponse legerOwoLevelXmer() throws FailureException, TException {
			log.info("开始给上上级V客分账  uid:"+twoLevelXmerId+" 金额:"+twoLevelXmerMoney);
			if(oneLevelLegered){
				return ParamUtil.failDataResponse("上上级V客已分账");
			}
			if(this.oneLevelXmerMoney.compareTo(BigDecimal.ZERO)<=0){
				return ParamUtil.failDataResponse("上上级V客分账金额为0");
			}
			Map<String, String> oneLevelXmer = new HashMap<>();
			oneLevelXmer.put("uid", this.oneLevelXmerId.toString());
			oneLevelXmer.put("typeId", "1");
			oneLevelXmer.put("type", String.valueOf(XmnConstants.EX_RTYPE_1));
			oneLevelXmer.put("option", "0");
			oneLevelXmer.put("amount", this.oneLevelXmerMoney.toString());
			oneLevelXmer.put("remark", this.remark);
			oneLevelXmer.put("source", this.orderId+ONELEVEL_ORDER_SUFFIX);
			Map<String,String> updateExpansionAmount = walletExpansionServiceImpl.updateWalletExpansion(oneLevelXmer);
			if(ParamUtil.SUCCESS_STATE.toString().equals(updateExpansionAmount.get("state"))){
				oneLevelLegered=true;
				return ParamUtil.successDataResponse(updateExpansionAmount.get("msg"));
			}else{
				isFail=true;
				return ParamUtil.failDataResponse(updateExpansionAmount.get("msg"));
			}
		}

		
		public String getRemark() {
			return remark;
		}

		public void setRemark(String remark) {
			this.remark = remark;
		}

		public String getOrderId() {
			return orderId;
		}

		public void setOrderId(String orderId) {
			this.orderId = orderId;
		}

		public Integer getTwoLevelXmerId() {
			return twoLevelXmerId;
		}

		public void setTwoLevelXmerId(Integer twoLevelXmerId) {
			this.twoLevelXmerId = twoLevelXmerId;
		}

		public BigDecimal getTwoLevelXmerMoney() {
			return twoLevelXmerMoney;
		}

		public void setTwoLevelXmerMoney(BigDecimal twoLevelXmerMoney) {
			this.twoLevelXmerMoney = twoLevelXmerMoney;
		}

		public Integer getOneLevelXmerId() {
			return oneLevelXmerId;
		}

		public void setOneLevelXmerId(Integer oneLevelXmerId) {
			this.oneLevelXmerId = oneLevelXmerId;
		}

		public BigDecimal getOneLevelXmerMoney() {
			return oneLevelXmerMoney;
		}

		public void setOneLevelXmerMoney(BigDecimal oneLevelXmerMoney) {
			this.oneLevelXmerMoney = oneLevelXmerMoney;
		}
		
		
		
		
	}

}