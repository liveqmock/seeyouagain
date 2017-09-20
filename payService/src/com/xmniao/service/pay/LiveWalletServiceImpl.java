package com.xmniao.service.pay;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TMultiplexedProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.xmniao.common.MapUtil;
import com.xmniao.common.ParamUtil;
import com.xmniao.common.RedisService;
import com.xmniao.common.ValidateUtil;
import com.xmniao.common.XmnConstants;
import com.xmniao.dao.LiveWalletMapper;
import com.xmniao.dao.WalletMapper;
import com.xmniao.dao.WalletRecordMapper;
import com.xmniao.dao.proxy.LiveWalletMapperProxy;
import com.xmniao.entity.LiveWallet;
import com.xmniao.entity.WalletExpansionRecord;
import com.xmniao.enums.WalletStatusCodeEnum;
import com.xmniao.exception.CustomException;
import com.xmniao.exception.LiveWalletZbalanceLockException;
import com.xmniao.exception.ParamBlankException;
import com.xmniao.exception.SignNotMatchException;
import com.xmniao.service.CommonService;
import com.xmniao.service.WalletExService;
import com.xmniao.thrift.busine.UserService;
import com.xmniao.thrift.ledger.FailureException;
import com.xmniao.thrift.ledger.LiveWalletService;
import com.xmniao.thrift.ledger.ResponseData;
import com.xmniao.thrift.ledger.ResponseList;
import com.xmniao.thrift.ledger.ResponsePageList;
import com.xmniao.thrift.ledger.ResponseSubList;
import com.xmniao.thrift.ledger.TopList;
import com.xmniao.thrift.ledger.WalletRecord;

/**
 * 项目名称：payService
 *
 * 类名称：LiveWalletServiceImpl
 *
 * 类描述：主播钱包服务类
 *
 * 创建人： chenJie
 *
 * 创建时间：2016年8月15日下午3:20:35
 *
 * Copyright (c) 广东寻蜜鸟网络技术有限公司
 */
@Service("LiveWalletServiceImpl")
public class LiveWalletServiceImpl implements LiveWalletService.Iface {

	private final Logger log = Logger.getLogger(LiveWalletServiceImpl.class);

	@Autowired
	private LiveWalletMapper liveWalletMapper;

	@Autowired
	private CommonService commonService;

	@Autowired
	private WalletMapper walletMapper;

	@Autowired
	private WalletRecordMapper walletRecordMapper;

	@Resource(name = "BUSINESS_IP_NUMBER")
	private String ipNumbertBusine;

	@Resource(name = "IP_PORT_BUSINE")
	private int ipPortBusine;

	@Autowired
	private SynthesizeServiceImpl syServiceImpl;

	@Autowired
	private WalletExService walletExService;

	@Resource(name = "WalletExpansionServiceImpl")
	private WalletExpansionServiceImpl walletExpansionServiceImpl;

	@Resource(name = "timeUnit")
	private Integer timeUnit;// 单位时间

	@Resource(name = "birdEggLimit")
	private BigDecimal birdEggLimit;// 单位时间主播加鸟币限额

	@Autowired
	private com.xmniao.service.LiveWalletAccess liveWalletAccess;
    
    @Autowired
	private RedisService redisService;

	private static Integer pageNo = 1;// 默认页码

	private static Integer pageSize = 20;// 默认页大小

	private static BigDecimal param_1000 = new BigDecimal(1000);// 1000

	/**
	 * 给用户或主播添加钱包
	 */
	@Override
	@Transactional(rollbackFor = { FailureException.class, Exception.class })
	public ResponseData addLiveWallet(String uid) throws FailureException, TException {
		log.info("添加直播钱包: 用户id: " + uid);
		ResponseData responseData = new ResponseData();
		Map<String, String> resultMap = new HashMap<>();

		try {
			// 验证参数
			if (StringUtils.isBlank(uid)) {
				log.error("用户id为空");
				responseData.setState(2);
				responseData.setMsg("用户参数不能为空");
				return responseData;
			}
			Map<String, String> map = new HashMap<>();
			map.put("uid", uid);
			// 验证该用户是否存在直播钱包
			Map<String, Object> liveWallet = liveWalletMapper.getLiveWallet(map);
			if (liveWallet != null) {
				log.info("用户" + uid + "已存在直播钱包");
				responseData.setState(0);
				responseData.setMsg("创建成功！");
				resultMap.put("id", liveWallet.get("id") + "");
				responseData.setResultMap(resultMap);
				return responseData;
			}

			// 钱包签名
			Map<String, String> signMap = new HashMap<>();
			signMap.put("uid", uid);
			signMap.put("balance", "0");
			signMap.put("commision", "0");
			signMap.put("zbalance", "0");
			signMap.put("sellerCoin", "0");
			String sign = commonService.LiveWalletSign(signMap);
			// 创建钱包
			Map<String, Object> walletMap = new HashMap<>();
			walletMap.put("uid", uid);
			walletMap.put("status", 1);
			walletMap.put("sign", sign);
			walletMap.put("signType", 1);
			walletMap.put("createTime", getFormatTime());

			liveWalletMapper.addLiveWallet(walletMap);

			log.info("用户：" + uid + "创建钱包成功");
			responseData.setState(0);
			resultMap.put("id", walletMap.get("id") + "");
			responseData.setMsg("创建钱包成功");
			responseData.setResultMap(resultMap);
			return responseData;
		} catch (Exception e) {
			log.error("用户" + uid + "添加钱包失败");
			throw new FailureException(1, "添加钱包失败");
		}
	}

	/**
	 * 解除或锁定钱包
	 */
	@Override
	@Transactional(rollbackFor = { FailureException.class, Exception.class })
	public ResponseData changeWalletState(Map<String, String> walletMap) throws FailureException, TException {
		log.info("修改直播钱包状态: 参数" + walletMap);
		ResponseData responseData = new ResponseData();

		try {
			// 验证参数id与uid不可同时为空，state不能为空
			if ((StringUtils.isBlank(walletMap.get("id")) || StringUtils.isBlank(walletMap.get("uid")))
					&& StringUtils.isBlank(walletMap.get("status"))) {
				log.error("更改钱包状态失败，参数不能为空");
				responseData.setState(2);
				responseData.setMsg("解除或锁定钱包失败，参数不能为空");
				return responseData;
			}

			Map<String, Object> liveWallet = liveWalletMapper.getLiveWallet(walletMap);
			if (liveWallet == null) {
				log.error(walletMap + "没有对应直播钱包");
				responseData.setState(1);
				responseData.setMsg("没有对应直播钱包");
				return responseData;
			}
			walletMap.put("sign", liveWallet.get("sign") + "");
			// 更新钱包状态
			int result = liveWalletMapper.updateWalletState(walletMap);
			if (1 != result) {
				log.error("更改钱包状态失败");
				throw new FailureException(1, "更改钱包状态失败,系统异常");
			}

			log.info("更改钱包状态成功");
			responseData.setState(0);
			responseData.setMsg("解除或锁定钱包成功");
			return responseData;
		} catch (Exception e) {
			log.error("更改钱包状态失败", e);
			throw new FailureException(1, "更改钱包状态失败");
		}
	}

	/**
	 * 查询钱包信息
	 */
	@Override
	public ResponseData getLiveWallet(Map<String, String> walletMap) throws FailureException, TException {
		log.info("获取直播钱包信息: " + walletMap);
		ResponseData responseData = new ResponseData();
		try {
			
			// 验证参数
			if (StringUtils.isNotBlank(walletMap.get("uid")) || StringUtils.isNotBlank(walletMap.get("id"))) {
				ObjectMapper mapper = new ObjectMapper();
				String uid=walletMap.get("uid");
				try {
					if("1".equals(walletMap.get("queryType"))&&LiveWalletMapperProxy.LIVE_REDIS_KEY&&StringUtils.isNotBlank(uid)){
						String string = redisService.getString(RedisService.LIVE_WALLET_REDIS_KEY+uid);
						if(StringUtils.isNotBlank(string)){
							Map readValue = mapper.readValue(string, Map.class);
							return ParamUtil.success("操作成功", readValue);
						}
					}
				} catch (Exception e) {
					log.error("获取redis数据异常",e);
				}
				
				// 查询钱包信息
				Map<String, Object> liveWallet = liveWalletMapper.getLiveWallet(walletMap);

				if (liveWallet == null) {
					if (StringUtils.isNotBlank(walletMap.get("checked"))) {
						// 没有钱包,新增
						addLiveWallet(walletMap.get("uid"));
						liveWallet = liveWalletMapper.getLiveWallet(walletMap);
					} else {
						log.error(walletMap + "没有对应直播钱包");
						responseData.setState(1);
						responseData.setMsg("没有对应直播钱包");
						return responseData;
					}
				}
				liveWallet.put("availableBalance", LiveWallet.getAvailableBalanceByMapWallet(liveWallet));
				Integer id = (Integer) liveWallet.get("id");
				if (id != null) {
					liveWallet.put("zbalanceLock", liveWalletAccess.hasZbalanceLock(id).toString());
				}
				log.info("获取钱包数据成功");
				responseData.setState(0);
				responseData.setMsg("获取钱包数据成功");
				Map<String, String> result = MapUtil.Object2String(liveWallet);
				responseData.setResultMap(result);
				try {
					if("1".equals(walletMap.get("queryType"))&&LiveWalletMapperProxy.LIVE_REDIS_KEY){
						redisService.getStringRedisTemplate().opsForValue().set(RedisService.LIVE_WALLET_REDIS_KEY+uid, mapper.writeValueAsString(result),60*30,TimeUnit.SECONDS);
					}
				} catch (Exception e) {
					log.error("set redis出现异常",e);
				}
				return responseData;
			} else {
				log.error("uid和id不能同时为空");
				responseData.setState(2);
				responseData.setMsg("uid和id不能同时为空");
				return responseData;
			}
		} catch (Exception e) {
			log.error("查询失败", e);
			responseData.setState(1);
			responseData.setMsg("查询钱包信息失败" + e);
			return responseData;
		}
	}

	/**
	 * 余额转出
	 */
	@Override
	@Transactional(rollbackFor = { FailureException.class, Exception.class })
	public ResponseData turnoutLiveWallet(Map<String, String> walletMap) throws FailureException, TException {
		log.info("鸟蛋转余额 turnoutLiveWallet:" + walletMap);
		ResponseData responseData = new ResponseData();
		try {
			String percent = walletMap.get("percent");// 分账比例
			// 验证参数 id 和uid不可同时为空，percent不可为空
			if ((StringUtils.isNotBlank(walletMap.get("id")) || StringUtils.isNotBlank(walletMap.get("uid")))
					&& StringUtils.isNotBlank(percent)) {

				Map<String, Object> liveWallet = liveWalletMapper.getLiveWallet(walletMap);
				// 验证该用户是否存在直播钱包
				if (liveWallet == null) {
					log.info("用户没有直播钱包" + walletMap);
					responseData.setState(1);
					responseData.setMsg("该用户没有直播钱包");
					return responseData;
				}
				// 验证签名
				Map<String, String> signMap = new HashMap<>();
				signMap.put("uid", liveWallet.get("uid") + "");
				signMap.put("balance", liveWallet.get("balance") + "");
				signMap.put("zbalance", liveWallet.get("zbalance") + "");
				signMap.put("commision", liveWallet.get("commision") + "");
				signMap.put("sellerCoin", liveWallet.get("sellerCoin") + "");
				String sign = commonService.LiveWalletSign(signMap);

				if (!sign.equals(liveWallet.get("sign"))) {
					log.error("签名验证失败");
					responseData.setState(1);
					responseData.setMsg("签名验证失败");
					return responseData;
				}

				int status = (int) liveWallet.get("status");
				// 判断钱包状态 1正常 2锁定 3注销
				if (1 != status) {
					log.error("钱包状态异常，该钱包已被锁定或注销");
					responseData.setState(1);
					responseData.setMsg("钱包状态异常，该钱包已被锁定或注销");
					return responseData;
				}
				liveWallet.put("description", "转出比例：" + percent);

				// 是否全部转出 鸟蛋/元=100/1
				if ("1".equals(walletMap.get("turnOutAll"))) {
					BigDecimal balance = (BigDecimal) liveWallet.get("balance");// 鸟蛋剩余数量
					BigDecimal money = turnEggToMoney(balance, "1000", percent);// 鸟蛋转成现金的可提现金额

					// 更新直播钱包数据，并添加记录
					updateLiveWallet(liveWallet, balance);

					// 更新寻蜜鸟用户钱包，并添加记录
					updateWallet(liveWallet.get("uid") + "", money.toString());

					// 转出部分金额
				} else if (StringUtils.isNotBlank(walletMap.get("money"))) {
					BigDecimal money = new BigDecimal(walletMap.get("money"));// 要转出的鸟蛋数量

					if (money.intValue() % 100 != 0) {
						log.error("鸟蛋必须为100的整数倍");
						responseData.setState(1);
						responseData.setMsg("鸟蛋数量异常，鸟蛋必须为100的整数倍");
						return responseData;
					}

					BigDecimal balance = (BigDecimal) liveWallet.get("balance");
					if (-1 == (balance.compareTo(money))) {// 鸟蛋余额不足
						log.error("鸟蛋余额不足");
						responseData.setState(1);
						responseData.setMsg("鸟蛋余额不足");
						return responseData;
					}
					BigDecimal cash = turnEggToMoney(money, "1000", walletMap.get("percent"));// 鸟蛋转成现金的可提现金额

					// 更新直播钱包数据，并添加记录
					updateLiveWallet(liveWallet, money);

					// 更新寻蜜鸟用户钱包，并添加记录
					updateWallet(liveWallet.get("uid") + "", cash.toString());

				} else {
					log.error("转出金额不可为空");
					responseData.setState(2);
					responseData.setMsg("转出金额不可为空");
					return responseData;
				}
			} else {
				log.error(" id 和uid不可同时为空，percent不可为空");
				responseData.setState(2);
				responseData.setMsg("id 和uid不可同时为空，percent不可为空");
				return responseData;
			}
		} catch (Exception e) {
			log.error("系统异常" + e);
			throw new FailureException(1, "更新失败");
		}
		log.info("鸟蛋转出成功");
		responseData.setState(0);
		responseData.setMsg("鸟蛋转出成");
		return responseData;
	}

	/**
	 * 更新钱包金额
	 */
	@Override
	public ResponseData updateWalletAmount(Map<String, String> walletMap) throws FailureException, TException {
		log.info("更新直播钱包金额 updateWalletAmount:" + walletMap);
		try {
			// 执行更新操作
			return updateLiveWallet2(walletMap);

		}catch (LiveWalletZbalanceLockException e) {
			log.error("[用户直播钱包鸟币余额被锁定不能扣除]" + walletMap,e);
			ResponseData fail = ParamUtil.fail("用户直播钱包鸟币余额被锁定不能扣除");
			Map<String, String> hashMap = new HashMap<>();
			hashMap.put("statusCode", WalletStatusCodeEnum.LIVE_ZBALANCE_LOCK_STATUS.getStatus());
			fail.setResultMap(hashMap);
			return fail;
		} catch (Exception e) {
			log.error("系统异常，更新钱包金额失败", e);
			return ParamUtil.error();
		}
	}

	/**
	 * 展示我的鸟蛋今日，累计，可提现数量
	 */
	@Override
	public ResponseData getBirdeggNums(Map<String, String> walletMap) throws FailureException, TException {
		log.info("getBirdeggNums:" + walletMap);
		ResponseData responseData = new ResponseData();
		Map<String, String> resultMap = new HashMap<>();
		try {
			if (StringUtils.isBlank(walletMap.get("id")) && StringUtils.isBlank("uid")) {
				log.error("uid和id不可同时为空");
				responseData.setState(2);
				responseData.setMsg("uid和id同时为空");
				return responseData;
			}
			Map<String, Object> liveWallet = liveWalletMapper.getLiveWallet(walletMap);
			// 鸟蛋余额
			BigDecimal balance = (BigDecimal) liveWallet.get("balance");
			// 鸟蛋转出总额
			BigDecimal turnoutEgg = (BigDecimal) liveWallet.get("turnEggOut");
			// 鸟蛋余额
			resultMap.put("balance", balance.toString());

			Map<String, String> map = new HashMap<>();
			map.put("day", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
			map.put("walletId", liveWallet.get("id") + "");

			Map<String, Object> eggMap = liveWalletMapper.countDayEgg(map);

			// 主播累计鸟蛋收入
			resultMap.put("totalEgg", liveWalletMapper.countTotalEgg(map).get("totalEgg").toString());
			// 今日鸟蛋收入
			resultMap.put("todayEgg", eggMap.get("eggMoney") + "");

			log.info("查询成功");
			responseData.setState(0);
			responseData.setMsg("查询成功");
			responseData.setResultMap(resultMap);
			return responseData;
		} catch (Exception e) {
			log.error("查询失败" + walletMap + e);
			responseData.setState(1);
			responseData.setMsg("系统异常查询失败");
			return responseData;
		}
	}

	/**
	 * 展示主播直播记录且能根据日期筛选
	 */
	@Override
	public WalletRecord birdEggDetail(Map<String, String> walletMap) throws FailureException, TException {
		log.info("birdEggDetail" + walletMap);
		WalletRecord walletRecord = new WalletRecord();
		try {
			if (StringUtils.isBlank(walletMap.get("id")) && StringUtils.isBlank(walletMap.get("uid"))) {
				log.error("uid和id不可同时为空");
				return walletRecord;
			}
			Map<String, Object> paramMap = new HashMap<>();
			// 如果页码为空设为默认值1
			if (StringUtils.isBlank(walletMap.get("pageNo"))) {
				paramMap.put("pageNo", pageNo);
			} else {
				paramMap.put("pageNo", Integer.valueOf(walletMap.get("pageNo")));
			}
			// 如果页大小为空设为默认值20
			if (StringUtils.isBlank(walletMap.get("pageSize"))) {
				paramMap.put("pageSize", pageSize);
			} else {
				paramMap.put("pageSize", Integer.valueOf(walletMap.get("pageSize")));
			}
			// 查询钱包信息
			Map<String, Object> liveWallet = liveWalletMapper.getLiveWallet(walletMap);
			if (liveWallet == null) {
				log.error("该用户没有直播钱包" + liveWallet);
				return walletRecord;
			}

			paramMap.put("walletId", liveWallet.get("id"));
			paramMap.put("sdate", walletMap.get("sdate"));
			paramMap.put("edate", walletMap.get("edate"));

			List<Map<String, Object>> recordList = liveWalletMapper.getWalletRecord(paramMap);

			List<Map<String, String>> list = new ArrayList<>();
			for (Map<String, Object> map : recordList) {
				list.add(MapUtil.Object2String(map));
			}

			return walletRecord.setWalletList(list);
		} catch (Exception e) {
			log.error("查询失败" + walletMap + e);
			return walletRecord;
		}
	}

	/**
	 * 查询三种排行 reype 1.平台鸟币消费排行 2.每场直播消费排行 3.用户对主播个人的消费排行
	 */
	@Override
	public WalletRecord birdCoinList(Map<String, String> walletMap) throws FailureException, TException {
		log.info("birdCoinList:" + walletMap);
		WalletRecord walletRecord = new WalletRecord();
		Map<String, Object> paramMap = new HashMap<>();
		List<Map<String, Object>> resultList = new ArrayList<>();
		try {
			// 如果页码为空设为默认值1
			if (StringUtils.isBlank(walletMap.get("pageNo"))) {
				paramMap.put("pageNo", pageNo);
			} else {
				paramMap.put("pageNo", Integer.valueOf(walletMap.get("pageNo")));
			}
			// 如果页大小为空设为默认值20
			if (StringUtils.isBlank(walletMap.get("pageSize"))) {
				paramMap.put("pageSize", pageSize);
			} else {
				paramMap.put("pageSize", Integer.valueOf(walletMap.get("pageSize")));
			}

			// 1.平台鸟币消费排行
			if ("1".equals(walletMap.get("rtype"))) {
				paramMap.put("uid", walletMap.get("uid"));
				resultList = liveWalletMapper.platformCoin(paramMap);
				// 2.每场直播消费排行
			} else if ("2".equals(walletMap.get("rtype"))) {
				if (StringUtils.isBlank(walletMap.get("recordId"))) {
					log.error("直播记录id为空");
					return walletRecord;
				}
				paramMap.put("recordId", walletMap.get("recordId"));
				resultList = liveWalletMapper.liveList(paramMap);
				// 3.主播个人的消费排行
			} else if ("3".equals(walletMap.get("rtype"))) {
				if (StringUtils.isBlank(walletMap.get("anchorId"))) {
					log.error("主播id为空");
					return walletRecord;
				}
				paramMap.put("anchorId", walletMap.get("anchorId"));
				resultList = liveWalletMapper.personList(paramMap);
			} else {
				log.error("rtype参数有误" + walletMap);
				return walletRecord;
			}

			/*
			 * if(("2".equals(walletMap.get("rtype")) ||
			 * "3".equals(walletMap.get("rtype"))) &&
			 * StringUtils.isNotBlank(walletMap.get("ledgerRatio"))){ Double
			 * ledgerRatio =Double.valueOf(walletMap.get("ledgerRatio"));
			 */
			BigDecimal ledger = StringUtils.isNotBlank(walletMap.get("ledgerRatio"))
					? new BigDecimal(walletMap.get("ledgerRatio")).multiply(BigDecimal.valueOf(10))
					: BigDecimal.valueOf(100);
			List<Map<String, Object>> leList = new ArrayList<>();
			for (Map<String, Object> map : resultList) {
				map.put("birdCoin", ledger.multiply(new BigDecimal(map.get("birdCoin").toString())));
				leList.add(map);
			}
			resultList = leList;
			/*
			 * }else{ log.error("rtype参数有误,分账比例为空" + walletMap); return
			 * walletRecord; }
			 */
			List<Map<String, String>> list = new ArrayList<>();
			for (Map<String, Object> map : resultList) {
				list.add(MapUtil.Object2String(map));
			}
			walletRecord.setWalletList(list);
			return walletRecord;
		} catch (Exception e) {
			log.error("系统异常" + e);
			return walletRecord;
		}
	}

	private String getFormatTime() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	}

	/**
	 *
	 * 方法描述：鸟蛋根据分账比例转为可提现金额 创建人： chenJie 创建时间：2016年8月16日下午3:32:20
	 *
	 * @param eggNums
	 *            鸟蛋数量
	 * @param percent1
	 *            鸟蛋和现金的兑换比例
	 * @param percent2
	 *            主播的分账比例
	 * @return 主播可提现的金额
	 */
	private BigDecimal turnEggToMoney(BigDecimal eggNums, String percent1, String percent2) {
		return eggNums.multiply(new BigDecimal(percent2)).divide(new BigDecimal(percent1), 2, BigDecimal.ROUND_HALF_UP);// eggNums*percent2/percent1
	}

	/**
	 *
	 * 方法描述：更新直播钱包数据，并添加记录 创建人： chenJie 创建时间：2016年8月16日下午9:25:50
	 *
	 * @param liveWallet
	 * @param balance
	 *            鸟蛋转出数量
	 * @throws FailureException
	 */
	public void updateLiveWallet(Map<String, Object> liveWallet, BigDecimal balance) throws FailureException {
		try {

			BigDecimal qbalance = (BigDecimal) liveWallet.get("balance");// 转出前鸟蛋余额

			BigDecimal hbalance = qbalance.subtract(balance);// 转出后鸟蛋余额
			BigDecimal turnEggOut = (BigDecimal) liveWallet.get("turnEggOut");

			Map<String, String> map = new HashMap<>();// 更新钱包的map
			Map<String, Object> recordMap = new HashMap<>();// 生成记录的map
			map.put("uid", liveWallet.get("uid") + "");
			map.put("balance", hbalance.toString());
			map.put("zbalance", liveWallet.get("zbalance") + "");
			map.put("commision", liveWallet.get("commision") + "");
			map.put("sellerCoin", liveWallet.get("sellerCoin") + "");
			// 生成新签名
			String newSign = commonService.LiveWalletSign(map);
			map.put("sign", newSign);
			map.put("oldSign", liveWallet.get("sign") + "");
			map.put("updateTime", getFormatTime());
			map.put("turnEggOut", turnEggOut.add(balance).toString());// 鸟蛋转出总量

			recordMap.put("walletId", liveWallet.get("id"));
			recordMap.put("rtype", 1);
			recordMap.put("eggMoney", balance);
			recordMap.put("qeggMoney", qbalance);
			recordMap.put("heggMoney", hbalance);
			recordMap.put("remarks", "");
			recordMap.put("description", liveWallet.get("description"));
			recordMap.put("createTime", getFormatTime());

			// 更新钱包数据
			int result = liveWalletMapper.updateWallet(map);

			if (1 != result) {
				log.error("更新钱包数据失败");
				throw new FailureException(1, "更新钱包数据失败");
			}
			// 插入钱包记录
			liveWalletMapper.insertWalletRecord(recordMap);
		} catch (Exception e) {
			log.error("更新直播钱包数据或插入记录失败");
			throw new FailureException(1, "更新钱包数据失败");
		}
	}

	/**
	 *
	 * 方法描述：更新寻蜜鸟钱包并添加记录
	 *
	 * 创建人： chenJie
	 *
	 * 创建时间：2016年8月16日下午8:14:04
	 *
	 * @param uid
	 *            用户id
	 * @param money
	 *            分账金额
	 * @throws FailureException
	 */
	public void updateWallet(String uid, String money) throws FailureException {

		BigDecimal balance = new BigDecimal(money);// 转账的金额

		Map<String, Object> map = new HashMap<>();
		map.put("uId", uid);
		map.put("typeId", 1);
		Map<String, Object> wallet = walletMapper.selectByuid(map);// 查询用户钱包信息

		if (wallet == null) {
			log.error("用户：" + uid + "没有寻蜜鸟钱包");
			throw new FailureException(1, "用户：" + uid + "没有寻蜜鸟钱包");
		}

		Map<String, Object> map2 = new HashMap<>();
		BigDecimal qcommision = (BigDecimal) wallet.get("commision");// 转账前钱包佣金余额
		BigDecimal hcommision = qcommision.add(balance);// 转账后钱包佣金余额
		map2.put("uid", wallet.get("uid"));
		map2.put("sellerid", wallet.get("sellerid"));
		map2.put("jointid", wallet.get("jointid"));
		map2.put("pwd", null == wallet.get("pwd") ? "" : wallet.get("pwd"));
		map2.put("amount", wallet.get("amount"));
		map2.put("balance", wallet.get("balance"));
		map2.put("zbalance", wallet.get("zbalance"));
		map2.put("integral", wallet.get("integral"));
		map2.put("commision", wallet.get("commision"));
		map2.put("sellerAmount", wallet.get("seller_amount"));
		String oldSign = commonService.walletSign(MapUtil.Object2String(map2));
		// 验证签名
		if (!oldSign.equals(wallet.get("sign"))) {
			log.error("钱包签名异常");
			throw new FailureException(1, "用户：" + uid + "签名数据异常");
		}
		map2.put("commision", hcommision.toString());
		// 生成新签名
		String sign = commonService.walletSign(MapUtil.Object2String(map2));
		map2.put("sign", sign);
		map2.put("lastDate", getFormatTime());
		map2.put("accountid", wallet.get("accountid"));
		map2.put("oldSign", oldSign);
		// 更新钱包数据
		int result = walletMapper.updateWalletMoneyFromLive(map2);
		if (result != 1) {
			throw new FailureException(1, "更新寻蜜鸟钱包失败");
		}

		// 会员钱包修改记录
		Map<String, Object> walletRecordMap = new HashMap<>();
		walletRecordMap.put("accountid", wallet.get("accountid"));
		walletRecordMap.put("rtype", XmnConstants.RTYPE_36);
		walletRecordMap.put("commision", balance);
		walletRecordMap.put("qcommision", qcommision);
		walletRecordMap.put("hcommision", hcommision);
		walletRecordMap.put("sdate", getFormatTime());
		// 添加会员钱包修改记录
		walletRecordMapper.addWalletRecord(walletRecordMap);
	}

	/**
	 * 批量更新直播钱包余额
	 */
	@Override
	@Transactional(rollbackFor = { FailureException.class, Exception.class })
	public ResponseData updateLiveWalletsForList(List<Map<String, String>> walletList)
			throws FailureException, TException {
		log.info("批量更新直播钱包余额updateLiveWalletsForList： " + walletList);

		ResponseData responseData = new ResponseData();

		try {
			if (walletList == null || walletList.size() == 0) {
				log.info("传入参数为空：" + walletList);
				responseData.setState(0);
				responseData.setMsg("更新成功");
				return responseData;
			}
			// 执行更新操作
			for (Map<String, String> map : walletList) {
				responseData = updateLiveWallet2(map);
				if (responseData.getState() != 0) {
					log.error("批量更新直播钱包失败：" + responseData.getMsg());
					return responseData;
				}
			}
			log.info("批量更新钱包余额成功");
			responseData.setState(0);
			responseData.setMsg("批量更新直播钱包成功");
			responseData.setResultMap(null);
			return responseData;
		} catch (Exception e) {
			log.error("批量更新失败：" + e);
			responseData.setState(1);
			responseData.setMsg("批量更新失败");
			return responseData;
		}
	}

	@Transactional(rollbackFor = { FailureException.class, Exception.class,RuntimeException.class })
	public ResponseData updateLiveWallet2(Map<String, String> walletMap) throws FailureException {
		log.info("updateLiveWallet2" + walletMap);

		ResponseData responseData = new ResponseData();
		Map<String, String> resultMap = new HashMap<>();
		// uid和id不可同时为空
		if (StringUtils.isBlank(walletMap.get("uid")) && StringUtils.isBlank(walletMap.get("id"))) {
			log.error("uid和id同时为空");
			responseData.setState(2);
			responseData.setMsg("uid和id同时为空");
			return responseData;
		}

		Map<String, Object> liveWallet = liveWalletMapper.getLiveWallet(walletMap);

		if (liveWallet == null) {
			log.error("用户没有直播钱包" + liveWallet);
			responseData.setState(2);
			responseData.setMsg("用户没有直播钱包");
			return responseData;
		}

		// 验证钱包状态
		int status = (int) liveWallet.get("status");
		if (1 != status) {
			log.error("该直播钱包已被锁定或注销" + liveWallet);
			responseData.setState(1);
			responseData.setMsg("该直播钱包" + liveWallet.get("id") + "已被锁定或注销");
			return responseData;
		}
		// 验证钱包签名
		if (!liveWalletAccess.verifySign(liveWallet)) {
			log.error("用户直播钱包签名异常" + liveWallet);
			responseData.setState(2);
			responseData.setMsg("用户直播钱包签名异常");
			return responseData;
		}

		// 注册送鸟豆
		if ("16".equals(walletMap.get("rtype"))) {
			Long countRegister = liveWalletMapper.countRegister(liveWallet.get("id").toString());
			if (countRegister > 0) {
				log.error("用户:" + walletMap.get("uid") + "已赠送注册鸟豆");
				responseData.setState(1);
				responseData.setMsg("用户:" + walletMap.get("uid") + "已赠送注册鸟豆");
				return responseData;
			}
		}
		// 验证订单
		if (StringUtils.isNotBlank(walletMap.get("remarks"))) {
			Long countRemarks = liveWalletMapper.countRemarks(walletMap.get("remarks"), liveWallet.get("id").toString(),
					walletMap.get("rtype"));
			if (countRemarks > 0) {
				log.info("订单" + walletMap.get("remarks") + "已更新成功成功，请勿重复提交");
				responseData.setState(0);
				responseData.setMsg("更新成功");
				return responseData;
			}
		}

		// rtype不可为空
		if (StringUtils.isNotBlank(walletMap.get("rtype"))) {

			Map<String, String> updateMap = new HashMap<>();// 更新钱包数据map
			Map<String, Object> recordMap = new HashMap<>();// 插入记录map

			Integer rtype = Integer.valueOf(walletMap.get("rtype"));// rtype
			BigDecimal qcommision = (BigDecimal) liveWallet.get("commision");// 钱包鸟豆余额
			BigDecimal qbalance = (BigDecimal) liveWallet.get("balance");// 鸟蛋余额
			BigDecimal qzbalance = (BigDecimal) liveWallet.get("zbalance");// 鸟币余额
			BigDecimal totalZbalance = (BigDecimal) liveWallet.get("cumulativeZbalance");// 累加鸟币

			updateMap.put("uid", liveWallet.get("uid") + "");
			updateMap.put("sellerCoin", liveWallet.get("sellerCoin") + "");// 商家鸟币

			recordMap.put("walletId", liveWallet.get("id"));
			recordMap.put("liveRecordId", walletMap.get("liveRecordId"));
			recordMap.put("anchorId", walletMap.get("anchorId"));
			recordMap.put("rtype", rtype);
			recordMap.put("remarks", walletMap.get("remarks") == null ? "" : walletMap.get("remarks"));
			recordMap.put("description", walletMap.get("description") == null ? "" : walletMap.get("description"));
			recordMap.put("createTime", getFormatTime());

			// 0 充值 1 转出 2 赠送礼物 3 发送私信 4 发送弹幕 ,5 主播礼物收入，6 主播私信收入 7 主播弹幕收入8购买粉丝券
			// 9主播卖出粉丝券分账收入 10用户使用粉丝券主播分账收入 11 发红包 12 收红包 13 红包余额退回
			/**
			 * 主播鸟蛋收支
			 */
			if (StringUtils.isNotBlank(walletMap.get("balance"))) {
				BigDecimal balance;
				if (rtype == XmnConstants.LIVE_RTYPE_5 || rtype == XmnConstants.LIVE_RTYPE_9
						|| rtype == XmnConstants.LIVE_RTYPE_10 || rtype == XmnConstants.LIVE_RTYPE_13
						|| rtype == XmnConstants.LIVE_RTYPE_11) {

					// 鸟蛋收入
					if (rtype == XmnConstants.LIVE_RTYPE_9 || rtype == XmnConstants.LIVE_RTYPE_10) {
						if (StringUtils.isNotBlank(walletMap.get("percent"))) {
							balance = getBalance(walletMap.get("balance"), walletMap.get("percent"));
						} else {
							log.error("参数异常，分账比例为空");
							throw new FailureException(1, "参数异常，分账比例为空");
						}
					} else {
						balance = new BigDecimal(walletMap.get("balance"));// 要操作的鸟蛋数量
					}

					/**
					 * 单位时间内主播获得鸟蛋不得超过限额
					 */
					// Map<String,String> tMap = new HashMap<>();
					// tMap.put("walletId",liveWallet.get("id").toString());
					// tMap.put("timeUnit",getDate((timeUnit-1)*-1));
					// tMap.put("date",new
					// SimpleDateFormat("yyyy-MM").format(new Date()));
					// BigDecimal unitTimeEgg =
					// liveWalletMapper.countUnitTimeEgg(tMap);
					// //判断是否超过限额
					// BigDecimal leftEgg = birdEggLimit.subtract(unitTimeEgg);
					// if(leftEgg.compareTo(BigDecimal.ZERO)>0){
					// if(leftEgg.compareTo(balance)<0){
					// log.info("本次更新超出主播单位时间鸟蛋收入额度，实际更新："+leftEgg+"
					// ,请求参数："+walletMap);
					// balance = leftEgg;
					// }
					// }else {
					// log.info("单位时间内主播鸟蛋收入已达限定额度，不再累加。参数："+walletMap);
					// responseData.setState(0);
					// responseData.setMsg("更新数据成功");
					// responseData.setResultMap(resultMap);
					// return responseData;
					// }

					// 鸟币/现金 = 100/1
					// balance = balance.multiply(new
					// BigDecimal(100)).setScale(0,BigDecimal.ROUND_HALF_UP);
					BigDecimal hbalance;

					// 鸟蛋支出
					if (rtype == XmnConstants.LIVE_RTYPE_11) {// 购买红包支出
						hbalance = qbalance.subtract(balance);
					} else {// 收入
						hbalance = qbalance.add(balance);
					}
					updateMap.put("balance", hbalance.toString());
					updateMap.put("zbalance", qzbalance.toString());
					updateMap.put("commision", qcommision.toString());
					updateMap.put("oldSign", liveWallet.get("sign") + "");
					// 生成新的签名
					String sign = commonService.LiveWalletSign(updateMap);
					updateMap.put("sign", sign);
					updateMap.put("updateTime", getFormatTime());
					// 更新钱包数据
					int result = liveWalletMapper.updateWallet(updateMap);
					if (result != 1) {
						log.error("更新钱包数据失败");
						throw new FailureException(1, "更新钱包数据失败");
					}

					recordMap.put("eggMoney", balance);
					recordMap.put("qeggMoney", qbalance);
					recordMap.put("heggMoney", hbalance);

					liveWalletMapper.insertWalletRecord(recordMap);// 插入更细记录

					resultMap.put("id", recordMap.get("id") + "");
					log.info("更新数据成功");
					responseData.setState(0);
					responseData.setMsg("更新数据成功");
					responseData.setResultMap(resultMap);
					return responseData;
				}

			}

			/**
			 * 鸟豆和鸟币收支
			 */
			BigDecimal commision;// 鸟豆金额
			BigDecimal zbalance;// 鸟币金额
			BigDecimal hcommision = new BigDecimal(0.00);
			BigDecimal hzbalance = new BigDecimal(0.00);

			// 打赏 消费鸟豆，赠送鸟币
			if (StringUtils.isNotBlank(walletMap.get("commision")) && (rtype == XmnConstants.LIVE_RTYPE_2
					|| rtype == XmnConstants.LIVE_RTYPE_3 || rtype == XmnConstants.LIVE_RTYPE_4)) {
				commision = new BigDecimal(walletMap.get("commision"));// 鸟豆金额

				BigDecimal turnCoinOut = (BigDecimal) liveWallet.get("turnCoinOut");
				turnCoinOut = turnCoinOut.add(commision);// 消费鸟豆总额
				if (-1 == (qcommision.compareTo(commision))) {// 验证钱包鸟豆余额是否不足
					log.error("鸟豆余额不足");
					responseData.setState(4);
					responseData.setMsg("鸟豆余额不足");
					return responseData;
				}

				if (rtype == XmnConstants.LIVE_RTYPE_2) {

					zbalance = new BigDecimal(walletMap.get("zbalance"));
					hzbalance = qzbalance.add(zbalance);
					totalZbalance = totalZbalance.add(zbalance);
					updateMap.put("totalZbalance", totalZbalance.compareTo(param_1000) == -1 ? totalZbalance.toString()
							: totalZbalance.subtract(param_1000).toString());// 累加鸟币
																				// 大于1000则清零
					recordMap.put("beansMoney", zbalance);
					recordMap.put("qbeansMoney", qzbalance);
					recordMap.put("hbeansMoney", hzbalance);
					recordMap.put("availableExchangeCoin", walletMap.get("availableCoin"));
					updateMap.put("availableExchangeCoin", walletMap.get("availableCoin"));

					// 更新商家专享鸟币（新增2017/3/9）
					if (walletMap.get("sellerCoin") != null) {
						BigDecimal sellerCoin = new BigDecimal(walletMap.get("sellerCoin"));// 商家专享鸟币
						BigDecimal qsellerCoin = new BigDecimal(liveWallet.get("sellerCoin").toString());// 更新前商家专享鸟币余额
						BigDecimal hsellerCoin = sellerCoin.add(qsellerCoin);// 更新后商家专享鸟币余额

						updateMap.put("sellerCoin", hsellerCoin.toString());

						recordMap.put("sellerCoin", sellerCoin.toString());
						recordMap.put("qsellerCoin", qsellerCoin.toString());
						recordMap.put("hsellerCoin", hsellerCoin.toString());
					}

				} else {
					hzbalance = qzbalance;
				}

				hcommision = qcommision.subtract(commision);

				updateMap.put("turnCoinOut", turnCoinOut.toString());// 消费鸟豆总量

				recordMap.put("coinMoney", commision);
				recordMap.put("qcoinMoney", qcommision);
				recordMap.put("hcoinMoney", hcommision);

				// 0充值鸟豆12领取主播红包
			} else if (StringUtils.isNotBlank(walletMap.get("commision")) && (rtype == XmnConstants.LIVE_RTYPE_0
					|| rtype == XmnConstants.LIVE_RTYPE_12 || rtype == XmnConstants.LIVE_RTYPE_16)) {

				commision = new BigDecimal(walletMap.get("commision"));// 鸟豆金额
				hcommision = qcommision.add(commision);
				hzbalance = qzbalance;

				recordMap.put("coinMoney", commision);
				recordMap.put("qcoinMoney", qcommision);
				recordMap.put("hcoinMoney", hcommision);

				// 鸟币，8购买粉丝券 14 推荐壕友奖励 15 消费买单
			} else if ((rtype == XmnConstants.LIVE_RTYPE_8 || rtype == XmnConstants.LIVE_RTYPE_15
					|| rtype == XmnConstants.LIVE_RTYPE_14) && StringUtils.isNotBlank(walletMap.get("zbalance"))) {
				zbalance = new BigDecimal(walletMap.get("zbalance"));

				// 8购买粉丝券 15 消费买单
				if (rtype != XmnConstants.LIVE_RTYPE_14) {

					/**
					 * 鸟币消费余额限制
					 */
					Object restrictive = liveWallet.get("restrictive");// 鸟币消费余额限制
					if ("002".equals(restrictive)) {// 开启限制
						BigDecimal limitBalance = new BigDecimal(liveWallet.get("limitBalance").toString());// 限额
						if (limitBalance.compareTo(BigDecimal.ZERO) > 0
								&& (qzbalance.subtract(zbalance)).compareTo(limitBalance) < 0) {// 超过限额
							log.error("本次鸟币消费超过限额:" + limitBalance + "鸟币余额：" + qzbalance + "，扣减鸟币失败" + walletMap);
							responseData.setState(1);
							responseData.setMsg("本次鸟币消费超过限额，扣减鸟币失败");
							return responseData;
						}
					}

					if (qzbalance.compareTo(zbalance) == -1) {
						log.error("鸟币余额不足");
						responseData.setState(1);
						responseData.setMsg("鸟币余额不足");
						return responseData;
					}
					hzbalance = qzbalance.subtract(zbalance);
					recordMap.put("usedExchangeCoin", walletMap.get("usedCoin"));
					updateMap.put("usedExchangeCoin", walletMap.get("usedCoin"));
				} else {// 推荐壕友
					hzbalance = qzbalance.add(zbalance);
					recordMap.put("availableExchangeCoin", walletMap.get("availableCoin"));
				}

				hcommision = qcommision;

				recordMap.put("beansMoney", zbalance);
				recordMap.put("qbeansMoney", qzbalance);
				recordMap.put("hbeansMoney", hzbalance);
			} else {
				log.error("参数有误:" + walletMap);
				responseData.setState(2);
				responseData.setMsg("参数有误" + walletMap);
				return responseData;
			}
			updateMap.put("commision", hcommision.toString());// 鸟豆
			updateMap.put("balance", qbalance.toString());// 鸟蛋
			updateMap.put("zbalance", hzbalance.toString());// 鸟币
			updateMap.put("oldSign", liveWallet.get("sign") + "");
			// 生成签名
			String sign = commonService.LiveWalletSign(updateMap);
			updateMap.put("sign", sign);
			updateMap.put("updateTime", getFormatTime());
			// 更新钱包数据
			int result = liveWalletMapper.updateWallet(updateMap);
			if (result != 1) {
				log.error("更新钱包数据失败");
				throw new FailureException(1, "更新钱包数据失败");
			}

			liveWalletMapper.insertWalletRecord(recordMap);// 插入更细记录

			resultMap.put("id", recordMap.get("id") + "");
			log.info("更新数据成功:" + walletMap);
			responseData.setState(0);
			responseData.setMsg("更新数据成功");
			responseData.setResultMap(resultMap);
			return responseData;

		} else {
			log.error("rtype不可为空");
			responseData.setState(2);
			responseData.setMsg("rtype不可为空");
			return responseData;
		}

	}

	/**
	 * 获取用户鸟豆消费明细
	 */
	@Override
	public WalletRecord getConsumerDetail(Map<String, String> walletMap) throws FailureException, TException {
		log.info("获取用户鸟豆消费明细getConsumerDetail" + walletMap);
		WalletRecord walletRecord = new WalletRecord();
		try {
			if (StringUtils.isBlank(walletMap.get("id")) && StringUtils.isBlank(walletMap.get("uid"))) {
				log.error("uid和id不可同时为空");
				throw new FailureException(1, "uid和id不可同时为空");
			}
			Map<String, Object> paramMap = new HashMap<>();
			// 如果页码为空设为默认值1
			if (StringUtils.isBlank(walletMap.get("pageNo"))) {
				paramMap.put("pageNo", pageNo);
			} else {
				paramMap.put("pageNo", Integer.valueOf(walletMap.get("pageNo")));
			}
			// 如果页大小为空设为默认值20
			if (StringUtils.isBlank(walletMap.get("pageSize"))) {
				paramMap.put("pageSize", pageSize);
			} else {
				paramMap.put("pageSize", Integer.valueOf(walletMap.get("pageSize")));
			}
			// 查询钱包信息
			Map<String, Object> liveWallet = liveWalletMapper.getLiveWallet(walletMap);
			if (liveWallet == null) {
				log.error("该用户没有直播钱包" + liveWallet);
				throw new FailureException(1, "该用户没有直播钱包");
			}

			paramMap.put("walletId", liveWallet.get("id"));
			paramMap.put("sdate", walletMap.get("sdate"));
			paramMap.put("edate", walletMap.get("edate"));

			List<Map<String, Object>> birdCoinList = liveWalletMapper.getBirdCoinList(paramMap);

			List<Map<String, String>> list = new ArrayList<>();
			for (Map<String, Object> map : birdCoinList) {
				list.add(MapUtil.Object2String(map));
			}

			log.info("查询鸟币消费明细成功");
			walletRecord.setWalletList(list);
			return walletRecord;
		} catch (Exception e) {
			log.error("查询鸟豆交易明细异常", e);
			throw new FailureException(1, "该用户没有直播钱包");
		}
	}

	/**
	 * 获取本周送出，本月送出，共送出鸟豆数量
	 */
	@Override
	public ResponseData getBirdBeans(Map<String, String> paraMap) throws FailureException, TException {
		log.info("获取本周送出，本月送出，共送出鸟豆数量getBirdBeans" + paraMap);
		ResponseData responseData = new ResponseData();
		Map<String, String> resultMap = new HashMap<>();
		try {
			if (StringUtils.isBlank(paraMap.get("id")) && StringUtils.isBlank(paraMap.get("uid"))) {
				log.error("uid和id不可同时为空");
				responseData.setState(2);
				responseData.setMsg("uid和id不可同时为空");
				return responseData;
			}
			paraMap.put("date", getFormatTime());
			// 获取直播钱包信息
			Map<String, Object> liveWallet = liveWalletMapper.getLiveWallet(paraMap);
			if (liveWallet == null) {
				log.error("用户没有直播钱包" + liveWallet);
				responseData.setState(2);
				responseData.setMsg("用户没有直播钱包");
				return responseData;
			}

			paraMap.put("walletId", liveWallet.get("id").toString());
			paraMap.put("date", getFormatTime());
			// 获取本周鸟豆消费总额
			Map<String, Object> weekBeans = liveWalletMapper.getWeekBeans(paraMap);
			// 获取本月鸟豆消费总额
			Map<String, Object> monthBeans = liveWalletMapper.getMonthBeans(paraMap);

			resultMap.put("weekBeans", weekBeans.get("weekBeans").toString());
			resultMap.put("monthBeans", monthBeans.get("monthBeans").toString());
			resultMap.put("todayBeans", liveWallet.get("turnCoinOut").toString());

			log.info("查询成功");
			responseData.setState(0);
			responseData.setMsg("查询成功");
			responseData.setResultMap(resultMap);
			return responseData;
		} catch (Exception e) {
			log.error("获取本周送出，本月送出，共送出鸟豆数量异常", e);
			responseData.setState(1);
			responseData.setMsg("获取本周送出，本月送出，共送出鸟豆数量异常");
			return responseData;
		}
	}

	/**
	 * 鸟豆消费明细列表
	 */
	@Override
	public ResponseList getBirdBeansList(Map<String, String> walletMap) throws FailureException, TException {
		log.info("鸟豆消费明细列表getBirdBeansList" + walletMap);
		ResponseList responseList = new ResponseList();
		ResponseData responseData = new ResponseData();
		try {
			// 验证参数
			if ((StringUtils.isBlank(walletMap.get("uid")) && StringUtils.isBlank(walletMap.get("id")))) {
				log.error("传入参数有误" + walletMap);
				throw new FailureException(1, "传入参数有误");
			}

			// 设置页码
			if (StringUtils.isBlank(walletMap.get("pageNo"))) {
				walletMap.put("pageNo", (pageNo - 1) + "");
			} else {
				walletMap.put("pageNo", (Integer.valueOf(walletMap.get("pageNo")) - 1) + "");
			}
			// 设置页大小
			if (StringUtils.isBlank(walletMap.get("pageSize"))) {
				walletMap.put("pageSize", pageSize.toString());
			}

			Map<String, Object> liveWallet = liveWalletMapper.getLiveWallet(walletMap);
			if (liveWallet == null) {
				log.error("用户:" + walletMap.get("uid") + "没有直播钱包");
				throw new FailureException(1, "用户没有直播钱包");
			}

			walletMap.put("walletId", liveWallet.get("id").toString());
			// 统计每日鸟豆消费总额
			List<Map<String, Object>> countBeansByDay = liveWalletMapper.countBeansByDay(walletMap);

			if (countBeansByDay == null || countBeansByDay.size() <= 0) {
				log.info("用户:" + walletMap.get("uid") + "没有鸟豆消费记录");
				responseData.setState(0);
				responseData.setMsg("该用户暂无鸟豆消费信息");
				responseList.setDataInfo(responseData);
				return responseList;
			}

			Map<String, String> paraMap = new HashMap<>();
			paraMap.put("walletId", liveWallet.get("id").toString());

			if (StringUtils.isNotBlank(walletMap.get("pageNo2"))) {
				paraMap.put("pageNo", (Integer.valueOf(walletMap.get("pageNo2")) - 1) + "");
				paraMap.put("pageSize", "20");
			} else {
				paraMap.put("pageNo", "0");
				paraMap.put("pageSize", "20");
			}
			List<ResponseSubList> dataList = new ArrayList<>();
			// 获取每日鸟豆消费列表
			for (Map<String, Object> map : countBeansByDay) {
				paraMap.put("date", map.get("countDate").toString());

				ResponseSubList responseSubList = new ResponseSubList();
				responseSubList.setCountNum(Integer.valueOf(map.get("countSum").toString()));// 鸟豆消费总额
				responseSubList.setCountSum(Double.valueOf(map.get("countNum").toString()));// 获赠鸟币总额
				responseSubList.setCountDate(map.get("countDate").toString());// 统计日期
				List<Map<String, String>> dayList = new ArrayList<>();
				List<Map<String, Object>> beansDetailByDay = liveWalletMapper.BeansDetailByDay(paraMap);
				for (Map<String, Object> map2 : beansDetailByDay) {
					dayList.add(MapUtil.Object2String(map2));
				}
				responseSubList.setSubList(dayList);
				dataList.add(responseSubList);
			}

			log.info("鸟豆消费明细列表成功");
			responseData.setState(0);
			responseData.setMsg("鸟豆消费明细列表成功");

			responseList.setDataInfo(responseData);
			responseList.setDataList(dataList);

			return responseList;

		} catch (Exception e) {
			log.error("获取鸟豆明细列表异常", e);
			throw new FailureException(1, "获取鸟豆明细列表异常");
		}
	}

	/**
	 * 鸟蛋收入明细
	 */
	@Override
	public ResponseSubList BirdeggIncomeList(Map<String, String> paraMap) throws FailureException, TException {
		log.info("鸟蛋收入明细:" + paraMap);
		ResponseSubList responseSubList = new ResponseSubList();
		try {
			// 验证参数
			if ((StringUtils.isBlank(paraMap.get("uid")) || StringUtils.isBlank(paraMap.get("id")))
					&& StringUtils.isBlank(paraMap.get("status"))) {
				log.error("传入参数有误" + paraMap);
				throw new FailureException(1, "传入参数有误");
			}

			// 设置页码
			if (StringUtils.isBlank(paraMap.get("pageNo"))) {
				paraMap.put("pageNo", (pageNo - 1) + "");
			} else {
				paraMap.put("pageNo", (Integer.valueOf(paraMap.get("pageNo")) - 1) + "");
			}
			// 设置页大小
			if (StringUtils.isBlank(paraMap.get("pageSize"))) {
				paraMap.put("pageSize", pageSize.toString());
			}

			Map<String, Object> liveWallet = liveWalletMapper.getLiveWallet(paraMap);
			if (liveWallet == null) {
				log.error("用户:" + paraMap.get("uid") + "没有直播钱包");
				throw new FailureException(1, "用户没有直播钱包");
			}

			paraMap.put("walletId", liveWallet.get("id") + "");

			// 统计鸟蛋收入总额
			Map<String, Object> totalIncome = liveWalletMapper.countIncomeBirdegg(paraMap);
			if (totalIncome == null || totalIncome.get("countSum") == null) {
				log.info("用户:" + paraMap.get("uid") + "暂无鸟蛋收入记录");
				responseSubList.setCountSum(0.00);
				return responseSubList;
			}

			List<Map<String, String>> subList = new ArrayList<>();
			List<Map<String, Object>> birdeggIncomeList = liveWalletMapper.BirdeggIncomeList(paraMap);
			for (Map<String, Object> map : birdeggIncomeList) {
				subList.add(MapUtil.Object2String(map));
			}
			log.info("查询鸟蛋收入明细成功");
			responseSubList.setCountSum(((BigDecimal) totalIncome.get("countSum")).doubleValue());
			responseSubList.setSubList(subList);
			return responseSubList;
		} catch (Exception e) {
			log.error("查询鸟蛋收入明细异常", e);
			throw new FailureException(1, "查询鸟蛋收入明细异常");
		}
	}

	/**
	 * 鸟蛋排行榜
	 */
	@Override
	public List<TopList> BirdeggTopList(Map<String, String> paraMap) throws FailureException, TException {
		log.info("主播鸟蛋排行榜");
		List<Integer> list = new ArrayList<>();
		List<TopList> resultList = new ArrayList<>();
		try {

			// 设置页码
			if (StringUtils.isBlank(paraMap.get("pageNo"))) {
				paraMap.put("pageNo", (pageNo - 1) + "");
			} else {
				paraMap.put("pageNo", (Integer.valueOf(paraMap.get("pageNo")) - 1) + "");
			}
			// 设置页大小
			if (StringUtils.isBlank(paraMap.get("pageSize"))) {
				paraMap.put("pageSize", pageSize.toString());
			}

			Map<String, Object> objectMap = MapUtil.String2Object(paraMap);

			if (StringUtils.isNotBlank(paraMap.get("uids"))) {
				objectMap.put("uids", Arrays.asList(paraMap.get("uids").split(",")));
			}

			// 获取主播收入排行榜
			List<Map<String, Object>> birdeggIncomeTop = liveWalletMapper.birdeggIncomeTop(objectMap);

			for (Map<String, Object> map : birdeggIncomeTop) {
				// 循环获取主播uid
				list.add((Integer) map.get("uid"));

			}
			// 调用业务服务获取主播anchor_id
			List<Integer> anchorList = TSocket(list);

			for (int i = 0; i < birdeggIncomeTop.size(); i++) {
				Map<String, String> resultMap = new HashMap<>();
				resultMap.put("uid", birdeggIncomeTop.get(i).get("uid") + "");
				resultMap.put("rowNo", birdeggIncomeTop.get(i).get("rowNo") + "");
				resultMap.put("birdeggCount", birdeggIncomeTop.get(i).get("birdeggCount") + "");

				// 获取每个主播贡献前三名
				List<Map<String, Object>> topThree = liveWalletMapper.topThree(anchorList.get(i));
				List<Map<String, String>> listMaps = new ArrayList<>();
				for (Map<String, Object> map : topThree) {
					listMaps.add(MapUtil.Object2String(map));
				}

				resultList.add(new TopList(resultMap, listMaps));
			}
			log.info("查询主播收入排行成功");
			return resultList;
		} catch (Exception e) {
			log.error("查询主播收入排行失败", e);
			throw new FailureException(1, "查询主播收入排行失败");
		}
	}

	/*
	 * 调用业务服务查询主播id
	 */
	public List<Integer> TSocket(List<Integer> list) throws FailureException, Exception {
		log.info("调用业务系统接口UserService查询主播id");

		List<Integer> result = null;
		TTransport transport = null;
		try {
			transport = new TSocket(ipNumbertBusine, ipPortBusine);
			TFramedTransport frame = new TFramedTransport(transport);
			// 设置传输协议为 TBinaryProtocol
			TProtocol protocol = new TBinaryProtocol(frame);
			TMultiplexedProtocol ManagerProtocol = new TMultiplexedProtocol(protocol, "UserService");
			UserService.Client client = new UserService.Client(ManagerProtocol);
			// 打开端口,开始调用
			transport.open();

			result = client.getAnchorId(list);
			log.info("主播id" + list);

		} catch (Exception e) {
			log.error("调用业务系统接口UserService查询主播id异常", e);
			throw new FailureException(1, "调用业务系统接口UserService查询主播id异常");
		} finally {
			transport.close();
		}
		return result;

	}

	/**
	 * 鸟蛋分账 传入值*分账比*100 取整
	 * 
	 * @param balance
	 * @param percent
	 * @return
	 */
	private BigDecimal getBalance(String balance, String percent) {
		return new BigDecimal(balance).multiply(new BigDecimal(percent))
				.multiply(new BigDecimal(1000).setScale(0, BigDecimal.ROUND_HALF_UP));
	}

	/**
	 * 鸟币收支记录
	 */
	@Override
	public List<Map<String, String>> birdCoinDetail(Map<String, String> paraMap) throws FailureException, TException {
		log.info("鸟币收支记录birdCoinDetail:" + paraMap);

		try {
			if (StringUtils.isBlank(paraMap.get("uid")) || StringUtils.isBlank(paraMap.get("type"))) {
				log.error("传入参数有误，uid和type不可为空");
				throw new FailureException(1, "传入参数有误，uid和type不可为空");
			}

			// 设置页码
			if (StringUtils.isBlank(paraMap.get("pageNo"))) {
				paraMap.put("pageNo", (pageNo - 1) + "");
			} else {
				paraMap.put("pageNo", (Integer.valueOf(paraMap.get("pageNo")) - 1) + "");
			}
			// 设置页大小
			if (StringUtils.isBlank(paraMap.get("pageSize"))) {
				paraMap.put("pageSize", pageSize.toString());
			}

			Map<String, Object> liveWallet = liveWalletMapper.getLiveWallet(paraMap);
			if (liveWallet == null) {
				log.error("用户:" + paraMap.get("uid") + "没有直播钱包");
				throw new FailureException(1, "用户没有直播钱包");
			}

			paraMap.put("walletId", liveWallet.get("id").toString());

			String type = paraMap.get("type");
			List<Map<String, String>> resList = new ArrayList<>();
			List<Map<String, Object>> birdDetail;
			if ("0".equals(type)) {// 查询鸟币收入
				birdDetail = liveWalletMapper.birdCoinInDetail(paraMap);
			} else if ("1".equals(type)) {// 查询支出
				birdDetail = liveWalletMapper.birdCoinOutDetail(paraMap);
			} else {
				log.error("type参数有误" + paraMap);
				throw new FailureException(1, "type参数有误");
			}

			for (Map<String, Object> map : birdDetail) {
				Map<String, String> map1 = MapUtil.Object2String(map);
				resList.add(map1);
			}
			log.info("查询鸟币收支记录成功");
			return resList;
		} catch (Exception e) {
			log.info("查询鸟币收支记录异常", e);
			throw new FailureException(1, "查询鸟币收支记录异常");
		}
	}

	/**
	 * 鸟币收入统计
	 */
	@Override
	public Map<String, String> countBirdCoin(Map<String, String> paraMap) throws FailureException, TException {
		log.info("鸟币收入统计:" + paraMap);
		Map<String, String> resultMap = new HashMap<>();
		try {
			if (StringUtils.isBlank(paraMap.get("uid"))) {
				log.error("传入参数有误，uid不可为空");
				throw new FailureException(1, "传入参数有误，uid不可为空");
			}

			Map<String, Object> liveWallet = liveWalletMapper.getLiveWallet(paraMap);
			if (liveWallet == null) {
				log.error("用户:" + paraMap.get("uid") + "没有直播钱包");
				throw new FailureException(1, "用户没有直播钱包");
			}
			// 鸟币余额
			resultMap.put("birdCoin", liveWallet.get("zbalance").toString());
			resultMap.put("sellerCoin", liveWallet.get("sellerCoin").toString());

			paraMap.put("walletId", liveWallet.get("id").toString());
			// 打赏
			paraMap.put("status", "2");
			Map<String, Object> count1 = liveWalletMapper.getcoinCount(paraMap);
			resultMap.put("birdCoin1", count1.get("total").toString());

			// 壕友
			paraMap.put("status", "14");
			Map<String, Object> count2 = liveWalletMapper.getcoinCount(paraMap);
			resultMap.put("birdCoin2", count2.get("total").toString());

			// 壕友
			paraMap.put("status", "19");
			Map<String, Object> count3 = liveWalletMapper.getcoinCount(paraMap);
			resultMap.put("birdCoin3", count3.get("total").toString());

			// 累计获得鸟币总额
			resultMap.put("total",
					new BigDecimal(count1.get("total").toString()).add(new BigDecimal(count2.get("total").toString()))
							.add(new BigDecimal(count3.get("total").toString())).toString());

			log.info("鸟币收入统计成功");
			return resultMap;
		} catch (Exception e) {
			log.info("鸟币收入统计异常", e);
			throw new FailureException(1, "鸟币收入统计异常");
		}
	}

	/**
	 * 获取间隔天数
	 * 
	 * @param num
	 */
	private static String getDate(int num) {
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, num);
		return new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
	}

	/**
	 * 直播钱包操作
	 */
	@Override
	public ResponseData liveWalletOption(Map<String, String> paraMap) throws FailureException, TException {
		log.info("更新直播钱包liveWalletOption：" + paraMap);
		try {
			ResponseData responseData = liveWalletAccess.liveWalletOption(paraMap);
			log.info("更新直播钱包成功");
			return responseData;
		} catch (LiveWalletZbalanceLockException e) {
			log.error("[用户直播钱包鸟币余额被锁定不能扣除]" + paraMap,e);
			ResponseData fail = ParamUtil.fail("用户直播钱包鸟币余额被锁定不能扣除");
			Map<String, String> hashMap = new HashMap<>();
			hashMap.put("statusCode", WalletStatusCodeEnum.LIVE_ZBALANCE_LOCK_STATUS.getStatus());
			fail.setResultMap(hashMap);
			return fail;
		} catch (Exception e) {
			log.error("更新直播钱包操作异常：", e);
			return ParamUtil.error();
		}

	}

	/**
	 * 获取用户所有收支明细
	 */
	@Override
	public List<Map<String, String>> getUserRecord(Map<String, String> paraMap) throws FailureException, TException {
		log.info("获取用户所有收支明细getUserRecord:" + paraMap);

		// 设置页大小
		if (paraMap.get("pageSize") == null) {
			paraMap.put("pageSize", "10");
		}

		// 设置页大小
		if (paraMap.get("cPage") == null || paraMap.get("cPage").equals("0")) {
			paraMap.put("cPage", "0");
		} else {
			paraMap.put("cPage", (Integer.parseInt(paraMap.get("cPage")) - 1) + "");
		}

		List<Map<String, String>> list = new ArrayList<>();
		if (StringUtils.isBlank(paraMap.get("uid"))) {
			log.error("uid为空");
			throw new FailureException(1, "uid为空");
		}

		// 查询明细
		List<Map<String, Object>> userRecords = liveWalletMapper.getUserRecords(paraMap);
		Long countUserRecord = liveWalletMapper.countUserRecord(paraMap);// 记录总条数
		for (Map<String, Object> map : userRecords) {
			map.put("total", countUserRecord);
			list.add(MapUtil.Object2String(map));
		}

		log.info("查询用户所有收支明细成功");
		return list;
	}

	/**
	 * 更新直播钱包和会员钱包
	 * 
	 * @param listParam
	 * @throws TException
	 * @throws FailureException
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { FailureException.class, Exception.class })
	public Boolean updateWallet(List<Map<String, String>> listParam) throws FailureException, TException {
		log.info("同时更新直播钱包和会员钱包：" + listParam);

		Boolean bool = true;
		for (Map<String, String> map : listParam) {
			if ("1".equals(map.get("ledgerType"))) {// 会员钱包

				Map<String, String> resMap = syServiceImpl.updateWalletAmount(map);
				if (!"0".equals(resMap.get("state"))) {
					log.info("会员钱包更新失败：" + resMap + "\r\n" + map);
					throw new FailureException(1, resMap.get("msg"));
				}
			} else if ("2".equals(map.get("ledgerType"))) {// 直播钱包
				ResponseData responseDate = liveWalletOption(map);
				if (responseDate.getState() != 0) {
					log.info("直播钱包更新失败：" + map);
					throw new FailureException(1, responseDate.getMsg());
				}
			} else {
				log.error("更新失败，参数类型有误");
				throw new FailureException(1, "参数类型有误");
			}
		}
		return bool;
	}

	/**
	 * 批量获取直播钱包信息
	 */
	@Override
	public Map<String, Map<String, String>> getLiveWalletMsg(List<String> paraList)
			throws FailureException, TException {
		log.info("批量获取直播钱包信息getLiveWalletMsg:" + paraList);

		Map<String, Map<String, String>> resultMap = new HashMap<>();

		for (String uid : paraList) {
			try {
				Map<String, String> walletMap = liveWalletMapper.getWalletMsgByUid(uid);
				Map<String, Object> getbeanCount = liveWalletMapper.getbeanCount(uid);
				walletMap.put("totalBean", getbeanCount.get("coinMoney").toString());
				resultMap.put(uid, walletMap);
			} catch (Exception e) {
				log.info("获取用户：" + uid + "钱包信息异常");
			}
		}

		log.info("批量获取直播钱包信息成功");
		return resultMap;
	}

	/**
	 * 更新直播钱包商家鸟币 到 公共鸟币（有多少扣多少）
	 * 
	 * @param paraMap
	 * @throws FailureException
	 */
	@Transactional(rollbackFor = { Exception.class, FailureException.class })
	public Boolean freeSellerCoinLimit(Map<String, Object> paraMap) throws FailureException {
		log.info("更新直播钱包商家鸟币freeSellerCoinLimit:" + paraMap);

		Map<String, String> walletMap = new HashMap<>();
		walletMap.put("uid", paraMap.get("uid").toString());

		BigDecimal quota = new BigDecimal(paraMap.get("quota").toString());// 用户储值卡余额

		Map<String, Object> liveWallet = liveWalletMapper.getLiveWallet(walletMap);

		Map<String, Object> recordMap = new HashMap<>();

		if (liveWallet != null) {

			// 验证钱包签名
			if (!liveWalletAccess.verifySign(liveWallet)) {
				log.error("用户直播钱包签名异常" + liveWallet);
				throw new FailureException(1, "用户：" + paraMap.get("uid") + "直播钱包签名异常");
			}

			BigDecimal sellerCoin = (BigDecimal) liveWallet.get("sellerCoin");// 钱包商家专享鸟币余额
			recordMap.put("walletId", liveWallet.get("id"));

			walletMap.put("balance", liveWallet.get("balance").toString());
			walletMap.put("commision", liveWallet.get("commision").toString());
			walletMap.put("oldSign", liveWallet.get("sign").toString());

			if (sellerCoin.compareTo(quota) > 0) {
				walletMap.put("sellerCoin", sellerCoin.subtract(quota).toString());
				walletMap.put("zbalance", quota.add((BigDecimal) liveWallet.get("zbalance")).toString());

				recordMap.put("beansMoney", quota);
				recordMap.put("qbeansMoney", liveWallet.get("zbalance"));
				recordMap.put("hbeansMoney", walletMap.get("zbalance"));

				recordMap.put("sellerCoin", quota.toString());
				recordMap.put("qsellerCoin", sellerCoin);
				recordMap.put("hsellerCoin", walletMap.get("sellerCoin"));
			} else {
				walletMap.put("sellerCoin", "0.00");
				walletMap.put("zbalance", sellerCoin.add((BigDecimal) liveWallet.get("zbalance")).toString());

				recordMap.put("beansMoney", sellerCoin.toString());
				recordMap.put("qbeansMoney", liveWallet.get("zbalance"));
				recordMap.put("hbeansMoney", walletMap.get("zbalance"));

				recordMap.put("sellerCoin", sellerCoin);
				recordMap.put("qsellerCoin", sellerCoin);
				recordMap.put("hsellerCoin", "0.00");
			}
			walletMap.put("sign", commonService.LiveWalletSign(walletMap));// 生成新签名
			walletMap.put("update_time", getFormatTime());

			recordMap.put("create_time", getFormatTime());
			recordMap.put("rtype", "20");
			recordMap.put("description", "商家释放储值卡");

			// 更新钱包数据
			int result = liveWalletMapper.updateWallet(walletMap);
			if (result != 1) {
				log.error("更新钱包数据失败");
				throw new FailureException(1, "更新钱包数据失败");
			}

			// 插入钱包更新记录
			liveWalletMapper.insertWalletRecord(recordMap);
		} else {
			log.error("该用户没有直播钱包");
			throw new FailureException(1, "更新钱包数据失败");
		}

		log.info("用户：" + paraMap.get("uid") + "转移商家专享鸟币成功");
		return true;
	}

	@Override
	@Transactional(rollbackFor = { RuntimeException.class, FailureException.class })
	public ResponseData cleanLiveWallet(Map<String, String> paramMap) throws TException {
		log.info("开始清除直播钱包 param=" + paramMap);
		Map<String, String> map = new HashMap<>();
		ResponseData responseData = new ResponseData();
		map.put("uid", paramMap.get("uid"));
		try {
			ParamUtil.checkMustParam(map);
			Map<String, Object> liveWallet = liveWalletMapper.getLiveWallet(map);
			if (liveWallet == null) {
				return ParamUtil.fail("直播钱包不存在");
			}
			map.put("rtype", String.valueOf(XmnConstants.LIVE_RTYPE_27));
			map.put("balance", liveWallet.get("balance").toString());
			map.put("option", "1");
			return liveWalletOption(map);
			/*
			 * checkSign(liveWallet); String sign = getCleanerSign(liveWallet);
			 * int result = liveWalletMapper.cleanWallet((Integer)
			 * liveWallet.get("uid"),sign); if(result>0){ HashMap<String,String>
			 * resultMap = new HashMap<>(); resultMap.put("id",
			 * liveWallet.get("id").toString()); Map<String, Object> recordMap =
			 * getCleanerRecord(liveWallet);
			 * liveWalletMapper.insertWalletRecord(recordMap);
			 * log.info("成功清空直播钱包"); return ParamUtil.success("清空成功",resultMap);
			 * }else{ return ParamUtil.error(); }
			 */
		} catch (ParamBlankException e) {
			log.info("调用清除钱包接口参数不正确 param=" + paramMap, e);
			responseData.setState(2);
			responseData.setMsg(e.getMessage());
			return responseData;
		} catch (Exception e) {
			log.info("调用清除钱包接口出现异常 param=" + paramMap, e);
			throw new FailureException();
		}

	}

	/**
	 *
	 * 方法描述：生成清空记录 创建人：jianming 创建时间：2017年4月5日 下午3:46:06
	 * 
	 * @param liveWallet
	 * @return
	 */
	private Map<String, Object> getCleanerRecord(Map<String, Object> liveWallet) {
		Map<String, Object> recordMap = new HashMap<>();
		recordMap.put("walletId", liveWallet.get("id"));
		recordMap.put("rtype", XmnConstants.LIVE_RTYPE_27);
		recordMap.put("eggMoney", liveWallet.get("balance"));
		recordMap.put("qeggMoney", liveWallet.get("balance"));
		recordMap.put("coinMoney", liveWallet.get("commision"));
		recordMap.put("qcoinMoney", liveWallet.get("commision"));
		recordMap.put("beansMoney", liveWallet.get("zbalance"));
		recordMap.put("qbeansMoney", liveWallet.get("zbalance"));
		recordMap.put("sellerCoin", liveWallet.get("sellerCoin"));
		recordMap.put("createTime", getFormatTime());
		return recordMap;
	}

	/**
	 *
	 * 方法描述：返回清空钱包后的签名 创建人：jianming 创建时间：2017年4月5日 下午3:03:44
	 * 
	 * @param liveWallet
	 * @return
	 */
	private String getCleanerSign(Map<String, Object> liveWallet) {
		HashMap<String, String> signMap = new HashMap<>();
		signMap.put("uid", liveWallet.get("uid").toString());
		signMap.put("balance", "0");
		signMap.put("commision", "0");
		signMap.put("zbalance", "0");
		signMap.put("sellerCoin", "0");
		return commonService.LiveWalletSign(signMap);
	}

	/**
	 *
	 * 方法描述：检查签名 创建人：jianming 创建时间：2017年4月5日 下午3:03:32
	 * 
	 * @param liveWallet
	 * @return
	 * @throws SignNotMatchException
	 */
	private String checkSign(Map<?, ?> liveWallet) throws SignNotMatchException {
		Map<String, String> signMap = new HashMap<>();
		signMap.put("uid", liveWallet.get("uid") + "");
		signMap.put("balance", liveWallet.get("balance") + "");
		signMap.put("zbalance", liveWallet.get("zbalance") + "");
		signMap.put("commision", liveWallet.get("commision") + "");
		signMap.put("sellerCoin", liveWallet.get("sellerCoin") + "");
		String sign = commonService.LiveWalletSign(signMap);
		if (!sign.equals(liveWallet.get("sign"))) {
			throw new SignNotMatchException();
		}
		return sign;
	}

	/**
	 *
	 */
	@Override
	public ResponseData updateExchangeCoinAmount(List<Map<String, String>> paraList)
			throws FailureException, TException {
		Map<String, String> resultMap = new HashMap<>();
		ResponseData responseData = new ResponseData(0, "成功", resultMap);
		int result = 0;
		for (Map<String, String> map : paraList) {
			try {
				result = liveWalletMapper.initExchangeCoinAmount(map);
				if (result == 0) {
					throw new Exception("找不到对应用户直播钱包");
				}
			} catch (Exception e) {
				for (Map.Entry<String, String> entry : map.entrySet()) {
					resultMap.put(map.get("uid") + "_" + entry.getKey(), entry.getValue());
				}
			}
		}
		return responseData;
	}

	@Transactional(rollbackFor = { Exception.class, FailureException.class })
	public Boolean updateWallet2(List<Map<String, String>> listParam) throws FailureException, TException {
		Boolean bool = true;
		for (Map<String, String> map : listParam) {
			if ("1".equals(map.get("ledgerType"))) {// 会员钱包
				Map<String, String> paramMap = new HashMap<>(); // 接口参数名转换
				paramMap.put("uid", map.get("uId"));
				paramMap.put("typeId", map.get("userType"));
				paramMap.put("type", map.get("rType"));
				paramMap.put("option", map.get("option"));
				paramMap.put("amount", map.get("commision"));
				paramMap.put("remark", map.get("remark"));
				paramMap.put("source", map.get("orderId"));
				List<WalletExpansionRecord> wallets = walletExService.getBySource(map.get("orderId"));
				if (!wallets.isEmpty()) {
					return bool;
				}
				Map<String, String> resMap = walletExpansionServiceImpl.updateWalletExpansion(paramMap);
				if (!"0".equals(resMap.get("state"))) {
					log.info("会员钱包更新失败：" + resMap + "\r\n" + map);
					throw new FailureException(1, resMap.get("msg"));
				}
			} else if ("2".equals(map.get("ledgerType"))) {// 直播钱包
				ResponseData responseDate = liveWalletOption(map);
				if (responseDate.getState() != 0) {
					log.info("直播钱包更新失败：" + map);
					throw new FailureException(1, responseDate.getMsg());
				}
			} else {
				log.error("更新失败，参数类型有误");
				throw new FailureException(1, "参数类型有误");
			}
		}
		return bool;
	}

	@Override
	public ResponseData updateWalletInternalChange(Map<String, String> walletMap) throws FailureException, TException {
		log.info("更新直播钱包金额转换 updateWalletInternalChange:" + walletMap);
		try {

			ResponseData responseData = new ResponseData();
			// uid和id不可同时为空
			if (StringUtils.isBlank(walletMap.get("uid")) && StringUtils.isBlank(walletMap.get("id"))) {
				log.error("uid和id同时为空");
				responseData.setState(2);
				responseData.setMsg("uid和id同时为空");
				return responseData;
			}

			Map<String, Object> liveWallet = liveWalletMapper.getLiveWallet(walletMap);

			if (liveWallet == null) {
				log.error("用户没有直播钱包" + liveWallet);
				responseData.setState(2);
				responseData.setMsg("用户没有直播钱包");
				return responseData;
			}

			// 验证钱包状态
			int status = (int) liveWallet.get("status");
			if (1 != status) {
				log.error("该直播钱包已被锁定或注销" + liveWallet);
				responseData.setState(1);
				responseData.setMsg("该直播钱包" + liveWallet.get("id") + "已被锁定或注销");
				return responseData;
			}
			// 验证钱包签名
			if (!liveWalletAccess.verifySign(liveWallet)) {
				log.error("用户直播钱包签名异常" + liveWallet);
				responseData.setState(2);
				responseData.setMsg("用户直播钱包签名异常");
				return responseData;
			}

			// rtype不可为空
			if (StringUtils.isNotBlank(walletMap.get("rtype"))) {
				// 验证订单
				if (StringUtils.isNotBlank(walletMap.get("remarks"))) {
					Long countRemarks = liveWalletMapper.countRemarks(walletMap.get("remarks"),
							liveWallet.get("id").toString(), walletMap.get("rtype"));
					if (countRemarks > 0) {
						log.info("订单" + walletMap.get("remarks") + "已更新成功成功，请勿重复提交");
						responseData.setState(0);
						responseData.setMsg("更新成功");
						return responseData;
					}
				}
				BigDecimal ucommision = walletMap.get("commision") == null ? BigDecimal.ZERO
						: new BigDecimal(walletMap.get("commision"));// 更新的钱包鸟豆余额
				BigDecimal ubalance = walletMap.get("balance") == null ? BigDecimal.ZERO
						: new BigDecimal(walletMap.get("balance"));// 更新的鸟蛋余额
				BigDecimal uzbalance = walletMap.get("zbalance") == null ? BigDecimal.ZERO
						: new BigDecimal(walletMap.get("zbalance"));// 更新的鸟币余额
				BigDecimal usellerCoin = walletMap.get("sellerCoin") == null ? BigDecimal.ZERO
						: new BigDecimal(walletMap.get("sellerCoin"));// 更新的商家鸟币

				Integer rtype = Integer.valueOf(walletMap.get("rtype"));// rtype
				BigDecimal qcommision = (BigDecimal) liveWallet.get("commision");// 钱包鸟豆余额
				BigDecimal qbalance = (BigDecimal) liveWallet.get("balance");// 鸟蛋余额
				BigDecimal qzbalance = (BigDecimal) liveWallet.get("zbalance");// 鸟币余额
				BigDecimal qsellerCoin = (BigDecimal) liveWallet.get("sellerCoin");// 商家
				BigDecimal limitBalance = (BigDecimal) liveWallet.get("limitBalance");// 限制鸟币
				boolean isLimitBalance = liveWallet.get("restrictive") == null ? false
						: liveWallet.get("restrictive").equals("001") ? false : true; // 是否限制使用鸟币

				BigDecimal hcommision = ucommision.add(qcommision);// 钱包鸟豆余额
				BigDecimal hbalance = ubalance.add(qbalance);// 鸟蛋余额
				BigDecimal hzbalance = uzbalance.add(qzbalance);// 鸟币余额
				BigDecimal hsellerCoin = usellerCoin.add(qsellerCoin);// 商家
				if ((ucommision.compareTo(BigDecimal.ZERO) < 0 && hcommision.compareTo(BigDecimal.ZERO) < 0)
						|| (ubalance.compareTo(BigDecimal.ZERO) < 0 && hbalance.compareTo(BigDecimal.ZERO) < 0)
						|| (uzbalance.compareTo(BigDecimal.ZERO) < 0 && hzbalance.compareTo(BigDecimal.ZERO) < 0)
						|| (usellerCoin.compareTo(BigDecimal.ZERO) < 0 && hsellerCoin.compareTo(BigDecimal.ZERO) < 0)) {
					log.info("扣减钱包余额时，余额不足");
					responseData.setState(1);
					responseData.setMsg("扣减钱包余额时，余额不足");
					return responseData;
				}
				if (isLimitBalance && uzbalance.compareTo(BigDecimal.ZERO) < 0
						&& hzbalance.compareTo(limitBalance) < 0) {
					log.info("扣减钱包鸟粉卡余额时，鸟币消费超过限额，扣减鸟币失败");
					responseData.setState(1);
					responseData.setMsg("鸟币消费超过限额，扣减鸟币失败");
					return responseData;
				}

				Map<String, String> updateMap = new HashMap<>();// 更新钱包数据map
				updateMap.put("uid", liveWallet.get("uid") + "");
				// if(ucommision.compareTo(BigDecimal.ZERO)!=0){
				updateMap.put("commision", hcommision + "");
				// }
				// if(ubalance.compareTo(BigDecimal.ZERO)!=0){
				updateMap.put("balance", hbalance + "");
				// }
				// if(uzbalance.compareTo(BigDecimal.ZERO)!=0){
				updateMap.put("zbalance", hzbalance + "");
				// }
				// if(usellerCoin.compareTo(BigDecimal.ZERO)!=0){
				updateMap.put("sellerCoin", hsellerCoin + "");
				// }
				updateMap.put("oldSign", liveWallet.get("sign") + "");

				// 生成新的签名
				String sign = commonService.LiveWalletSign(updateMap);
				updateMap.put("sign", sign);
				updateMap.put("updateTime", getFormatTime());
				int result = liveWalletMapper.updateWallet(updateMap);
				if (result != 1) {
					throw new Exception("修改钱包异常");
				}

				Map<String, Object> recordMap = new HashMap<>();// 插入记录map
				recordMap.put("walletId", liveWallet.get("id"));
				recordMap.put("rtype", rtype);
				recordMap.put("remarks", walletMap.get("remarks") == null ? "" : walletMap.get("remarks"));
				recordMap.put("description", walletMap.get("description") == null ? "" : walletMap.get("description"));
				recordMap.put("createTime", getFormatTime());
				recordMap.put("updateTime", getFormatTime());
				if (ucommision.compareTo(BigDecimal.ZERO) != 0) {
					recordMap.put("coinMoney", ucommision);
					recordMap.put("qcoinMoney", qcommision);
					recordMap.put("hcoinMoney", hcommision);
				}
				if (ubalance.compareTo(BigDecimal.ZERO) != 0) {
					recordMap.put("eggMoney", ubalance);
					recordMap.put("qeggMoney", qbalance);
					recordMap.put("heggMoney", hbalance);
				}
				if (uzbalance.compareTo(BigDecimal.ZERO) != 0) {
					recordMap.put("beansMoney", uzbalance);
					recordMap.put("qbeansMoney", qzbalance);
					recordMap.put("hbeansMoney", hzbalance);
				}
				if (usellerCoin.compareTo(BigDecimal.ZERO) != 0) {
					recordMap.put("sellerCoin", usellerCoin);
					recordMap.put("qsellerCoin", qsellerCoin);
					recordMap.put("hsellerCoin", hsellerCoin);
				}

				liveWalletMapper.insertWalletRecord(recordMap);// 插入更细记录
				Map<String, String> resultMap = new HashMap<>();
				resultMap.put("id", recordMap.get("id") + "");
				return new ResponseData(0, "成功", resultMap);
			} else {
				log.error("rtype不可为空");
				responseData.setState(2);
				responseData.setMsg("rtype不可为空");
				return responseData;
			}
		} catch (Exception e) {
			log.error("系统异常，更新钱包金额失败", e);
			throw new FailureException(1, "系统异常，更新钱包金额失败");
		}
	}

	/**
	 * V客兑换SAAS套餐扣除鸟币: 1.扣除鸟币 2.解除鸟币限制
	 *
	 * @param paraMap
	 *
	 */
	@Override
	public ResponseData exchangeSaas(Map<String, String> paraMap) throws FailureException, TException {
		ResponseData responseData = new ResponseData();
		log.info("调用Thrift接口[1.9.22  V客兑换SAAS套餐扣除鸟币 ], 参数:" + paraMap);

		Integer uid = null; // 用户uid
		BigDecimal deductZbalance = null; // 需要支付的鸟币
		String remarks = null; // 订单编号

		// 1. 校验参数
		try {

			// 判断参数是否为空
			if (ValidateUtil.validateNull(paraMap, "uid", "deductZbalance", "remarks")) {
				// 判断参数格式是否可以正确转换
				deductZbalance = new BigDecimal(paraMap.get("deductZbalance"));
				uid = Integer.valueOf(paraMap.get("uid"));
				remarks = paraMap.get("remarks");
			} else {
				log.info("参数不足, paraMap=" + paraMap);
				responseData.setState(2);
				responseData.setMsg("参数不足,paraMap=" + paraMap);
				return responseData;
			}

		} catch (Exception e) {
			log.error("校验参数失败!", e);
			responseData.setState(2);
			responseData.setMsg("参数错误! paraMap=" + paraMap);
			return responseData;
		}

		try {

			// 2. 获取用户钱包
			LiveWallet liveWallet = liveWalletAccess.getLiveWallet(uid);
			// 3. 兑换SAAS:扣除鸟币,解除鸟币限制
			liveWalletAccess.exchangeSaas(liveWallet, deductZbalance, remarks);

			// 4. 设置响应
			responseData.setState(0);
			responseData.setMsg("扣除鸟币成功");

		} catch (CustomException ex) {
			log.info(ex.getMessage());
			responseData.setMsg(ex.getMessage());
			responseData.setState(ex.getCode());
		} catch (Exception e) {
			responseData.setState(1);
			responseData.setMsg("支付系统出现异常!");
			log.error("调用Thrift接口[1.9.22  V客兑换SAAS套餐扣除鸟币], 出现异常!", e);
		}
		return responseData;
	}

	@Override
	public ResponseData updateLiveWalletLock(Map<String, String> param) throws FailureException, TException {
		log.info("[开始调用添加/解除直播钱包金额锁定接口]" + param);
		try {
			ParamUtil.checkMustParam(param, "type", "uid", "updateOption");
			if (StringUtils.isBlank(param.get("lockOption"))) {
				param.put("lockOption", "1");
			}
			Map<String, Object> walletMsgByUid = liveWalletMapper.getLiveWallet(param);

			if (walletMsgByUid == null) {
				return ParamUtil.fail("用户钱包不存在");
			}
			int i = liveWalletAccess.lockWallet(Integer.valueOf(param.get("updateOption")),
					Integer.valueOf(param.get("lockOption")), (Integer) walletMsgByUid.get("id"),
					Integer.valueOf(param.get("type")));
			return i > 0 ? ParamUtil.success("操作成功", new HashMap()) : ParamUtil.fail("操作失败");
		} catch (ParamBlankException e) {
			log.info("[开始调用添加/解除直播钱包金额锁定接口参数异常]" + e.getMessage());
			return ParamUtil.fail(e.getMessage());
		} catch (Exception e) {
			log.error(e);
			throw new FailureException();
		}
	}

	@Override
	public ResponsePageList getLiveWalletList(Map<String, String> walletMap) throws FailureException, TException {
		log.info("[开始调用批量查询直播钱包接口]" + walletMap);
		ResponsePageList responsePageList = new ResponsePageList();
		try {
			ParamUtil.checkMustParam(walletMap, "uids");
			List<LiveWallet> liveWallets = liveWalletMapper
					.getLiveWalletByUids(Arrays.asList(walletMap.get("uids").split(",")));
			List<Map<String, String>> list = new ArrayList<>();
			for (LiveWallet liveWallet : liveWallets) {
				Map<String, String> pojoToResultMap = ParamUtil.pojoToResultMap(liveWallet, "id", "uid", "status",
						"balance", "commision", "zbalance", "turnEggOut", "cumulativeZbalance", "turnCoinOut",
						"restrictive", "limitBalance", "sellerCoin", "availableExchangeCoin", "usedExchangeCoin",
						"zbalanceLock");
				pojoToResultMap.put("availableBalance",
						LiveWallet.getAvailableBalanceByMapWallet(liveWallet).toString());
				list.add(pojoToResultMap);
			}
			responsePageList.setDataInfo(ParamUtil.success("操作成功", null));
			responsePageList.setPageList(list);
			return responsePageList;
		} catch (ParamBlankException e) {
			log.info("[调用批量查询直播钱包接口参数异常]" + e.getMessage());
			responsePageList.setDataInfo(ParamUtil.fail(e.getMessage()));
			return responsePageList;
		}
	}

}