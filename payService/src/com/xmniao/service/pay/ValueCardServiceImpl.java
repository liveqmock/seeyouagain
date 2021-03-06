package com.xmniao.service.pay;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xmniao.common.MapUtil;
import com.xmniao.dao.ValueCardDao;
import com.xmniao.thrift.busine.UserService;
import com.xmniao.thrift.ledger.FailureException;
import com.xmniao.thrift.ledger.PageList;
import com.xmniao.thrift.ledger.ResponseData;
import com.xmniao.thrift.ledger.SubList;
import com.xmniao.thrift.ledger.ValueCardService;

@Service("ValueCardServiceImpl")
@Transactional
public class ValueCardServiceImpl implements ValueCardService.Iface {

	/**
	 * 初始化日志类
	 */
	private static Logger log = Logger.getLogger(ValueCardServiceImpl.class);

	@Autowired
	private ValueCardDao vCardDao;
	
	@Resource(name = "BUSINESS_IP_NUMBER")
	private String ipNumbertBusine;
	
	@Resource(name = "IP_PORT_BUSINE")
	private int ipPortBusine;
	
	@Autowired
	private LiveWalletServiceImpl liveWalletServiceImpl;
	
	private static final Integer page = 1;// 当前页码，默认为1

	private static final Integer pageSize = 10;// 页大小，默认为10
	/**
	 * 获取会员储值卡列表
	 */
	@Override
	public List<Map<String, String>> getValueCardMsg(Map<String, String> paraMap)
			throws FailureException, TException {

		log.info("获取会员储值卡列表getValueCardMsg:" + paraMap);
		List<Map<String, String>> resultList = new ArrayList<>();

		if (StringUtils.isBlank(paraMap.get("uid"))) {
			log.error("传入参数有误，用户id为空");
			throw new FailureException(2, "传入参数有误，用户id为空");
		}
		
		// 设置页码
		if (StringUtils.isBlank(paraMap.get("page"))) {
			paraMap.put("page", (page - 1) + "");
		} else {
			paraMap.put("page",
					(Integer.valueOf(paraMap.get("page")) - 1) + "");
		}
		// 设置页大小
		if (StringUtils.isBlank(paraMap.get("pageSize"))) {
			paraMap.put("pageSize", pageSize.toString());
		}
		
		Map<String, Object> cardNum = vCardDao.countCardNum(paraMap);
		
		List<Map<String, Object>> valueCard = vCardDao.getValueCard(paraMap);
		for (Map<String, Object> map : valueCard) {
			map.put("cardNum", cardNum.get("num").toString());
			resultList.add(MapUtil.Object2String(map));
		}
		log.info("获取会员储值卡列表成功");
		return resultList;
	}

	/**
	 * 更新会员储值卡
	 */
	@Override
	@Transactional(rollbackFor={Exception.class,FailureException.class})
	public ResponseData updateValueCard(Map<String, String> paraMap)
			throws FailureException, TException {
		
		log.info("更新会员储值卡updateValueCard:"+paraMap);
		ResponseData responseData = new ResponseData();
		paraMap.put("updateTime",getFormatDate());
		
		if(StringUtils.isBlank(paraMap.get("uid"))&&StringUtils.isBlank(paraMap.get("sellerid"))
				&&StringUtils.isBlank(paraMap.get("rtype"))&&StringUtils.isBlank(paraMap.get("quota"))){
			log.error("传入参数有误，部分参数为空");
			throw new FailureException(2,"传入参数有误，部分参数为空");
		}
		
		List<Map<String, Object>> valueCard;
		
		valueCard = vCardDao.getValueCard(paraMap);
		
		if(valueCard != null && valueCard.size()>1){
			log.error("数据异常,查询到同一商家，该会员有多张储值卡:"+paraMap);
			throw new FailureException(1,"数据异常，查询到同一商家，该会员有多张储值卡");
		}else if(valueCard == null || valueCard.size() == 0){
			try {
				log.info("会员对该商家没有储值卡，现对其添加"+paraMap);
				vCardDao.addValueCard(paraMap);
			} catch (Exception e) {
				log.error("新增会员卡失败",e);
				throw new FailureException(1,"该用户没有储值卡，并新增失败");
			}
			valueCard = vCardDao.getValueCard(paraMap);
		}
		
		BigDecimal cumulativeQuota = (BigDecimal)valueCard.get(0).get("cumulativeQuota");//累计额度
		BigDecimal quota = new BigDecimal(paraMap.get("quota"));//要更新的金额
		BigDecimal qquota = (BigDecimal)valueCard.get(0).get("quota");//更新前
		BigDecimal hquota;//更新后
		
		
		if("1".equals(paraMap.get("option"))){
			hquota=qquota.add(quota);
			cumulativeQuota = cumulativeQuota.add(quota);
		}else if("2".equals(paraMap.get("option"))){
			
			if(qquota.compareTo(quota)<0){
				log.error("储值卡额度不足，更新失败:"+paraMap);
				throw new FailureException(1,"储值卡额度不足，更新失败");
			}
			
			hquota = qquota.subtract(quota);
		}else {
			log.error("传入参数option错误："+paraMap);
			throw new FailureException(1,"传入参数option错误");
		}
		
		paraMap.put("hquota", hquota.toString());
		paraMap.put("cumulativeQuota",cumulativeQuota.toString());
		
		//更新储值卡
		Integer result = vCardDao.updateValueCard(paraMap);
		
		if(result !=1){
			log.error("更新储值卡失败");
			throw new FailureException(1,"更新储值卡失败");
		}
		
		paraMap.put("qquota", qquota.toString());
		paraMap.put("qid",valueCard.get(0).get("id").toString());
		
		Integer result2 = vCardDao.addValueCardRecord(paraMap);
		
		if(result2 != 1){
			log.error("插入更新储值卡记录失败");
			throw new FailureException(1,"插入更新储值卡记录失败");
		}
		
		log.info("更新储值卡成功");
		responseData.setState(0);
		responseData.setMsg("更新成功");
		return responseData;
	}

	/**
	 * 获取储值卡列表（业务后台）
	 */
	@Override
	public List<Map<String, String>> getValueCardList(
			Map<String, String> paraMap) throws FailureException,
			TException {
		log.info("获取储值卡列表（多个会员）getValueCardList:" + paraMap);

		List<Map<String, String>> resultList = new ArrayList<>();
				
				Map<String,Object> vmap = new HashMap<>();
				vmap.put("page",Integer.valueOf(paraMap.get("page")));
				vmap.put("limit",Integer.valueOf(paraMap.get("limit")));
				vmap.put("sellername",paraMap.get("sellername"));
				vmap.put("uid",paraMap.get("uid"));
				vmap.put("sellertype",paraMap.get("sellertype"));
				
				List<Map<String, Object>> valueCard = vCardDao
						.getCardList(vmap);
				for (Map<String, Object> map : valueCard) {
					try {
						resultList.add(MapUtil.Object2String(map));
					} catch (Exception e) {
						log.info("格式化异常："+map,e);
					}
				}
				
		return resultList;
	}
	
	
	
	/**
	 * 统计记录条数
	 */
	@Override
	public Map<String, String> countCardNums(Map<String, String> paraMap)
			throws FailureException, TException {
		log.info("获取记录条数countCardNums："+paraMap);
		
		Map<String,Object> vmap = new HashMap<>();
		vmap.put("sellername",paraMap.get("sellername"));
		vmap.put("uid",paraMap.get("uid"));
		vmap.put("sellertype",paraMap.get("sellertype"));
		
		Long count = vCardDao.countCard(vmap);
		Map<String,String> map = new HashMap<>();
		map.put("total",count.toString());
		return map;
	}
	
	/**
	 * 获取格式化的当前时间
	 * @return
	 */
	private String getFormatDate(){
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	}
	
	
	/**
	 * 获取商户累计充值，累计剩余，充值会员
	 * @param (sellerid,sellerid)
	 * @return map("sellerid","累计充值，累计剩余，充值会员")
	 */
	@Override
	public Map<String, String> getValueCardBalance(Map<String, String> paraMap)
			throws FailureException, TException {
		log.info("获取商户累计充值，累计剩余，充值会员人数getValueCardBalance:"+paraMap);
		Map<String,String> resultMap = new HashMap<>();
		String sellerids = paraMap.get("sellerid");
		
		String[] ids = sellerids.split(",");
		
		for (String sellerid : ids) {
			Map<String, Object> countBalance = vCardDao.countBalance(sellerid);
			StringBuilder sb = new StringBuilder();
			sb.append(countBalance.get("cumulativeQuota")+","+countBalance.get("quota")+","+countBalance.get("num"));
			resultMap.put(sellerid,sb.toString());
		}
		return resultMap;
	}
	
	/**
	 * 更新储值卡状态，并释放额度
	 */
	@Override
	@Transactional(rollbackFor={Exception.class,FailureException.class})
	public ResponseData updateCardStatus(Map<String, String> paraMap)
			throws FailureException, TException {
		log.info("更新储值卡状态updateCardStatus："+paraMap);
		ResponseData responseData = new ResponseData();
		
		if(StringUtils.isBlank(paraMap.get("sellerid"))){
			log.error("商家id为空");
			responseData.setState(1);
			responseData.setMsg("商家id为空");
			return responseData;
		}
		paraMap.put("isOverdue","1");//未过期
		paraMap.put("limit","-1");
		
		//查询未过期商家会员卡
		List<Map<String, Object>> cardList = vCardDao.getValueCard(paraMap);
		if(cardList == null || cardList.size()<=0){
			log.info("更新商家储值卡状态成功");
			responseData.setState(0);
			return responseData;
		}
		
		//更新商家会员卡状态
		Integer result = vCardDao.updateCardStatus(paraMap.get("sellerid"),paraMap.get("sellertype"));
		if(result != cardList.size()){
			log.error("更新商家会员卡状态失败");
			throw new FailureException(1,"更新失败");
		}
		
		//插入更新记录
		vCardDao.insertRecord(cardList);
		
		/**
		 * 将用户商家专享鸟币移至公共鸟币
		 */
		for (Map<String, Object> map : cardList) {
			liveWalletServiceImpl.freeSellerCoinLimit(map);
		}
		
		log.info("更新储值卡状态成功");
		responseData.setState(0);
		responseData.setMsg("更新成功");
		
		return responseData;
	}
	
	/**
	 * 获取储值卡收支记录
	 */
	@Override
	public List<Map<String, String>> getCardRecord(Map<String, String> paraMap)
			throws FailureException, TException {
		log.info("获取储值卡收支记录:getCardRecord"+paraMap);
		
		// 设置页码
		if (StringUtils.isBlank(paraMap.get("page"))) {
			paraMap.put("page", (page - 1) + "");
		} else {
			paraMap.put("page",
					(Integer.valueOf(paraMap.get("page")) - 1) + "");
		}
		// 设置页大小
		if (StringUtils.isBlank(paraMap.get("pageSize"))) {
			paraMap.put("pageSize", pageSize.toString());
		}
		
		List<Map<String, Object>> records = vCardDao.getRecords(paraMap);
		List<Map<String,String>> list = new ArrayList<>();
		for (Map<String, Object> map : records) {
			list.add(MapUtil.Object2String(map));
		}
		return list;
	}
	
	/**
	 * 获取储值卡充值消费详细记录
	 */
	@Override
	public PageList getValueCardRecord(Map<String, String> paraMap)
			throws FailureException, TException {
		log.info("获取储值卡充值消费详细记录getValueCardRecord:"+paraMap);
		PageList pageList = new PageList();
		
		if(StringUtils.isBlank(paraMap.get("sellerid"))){
			log.error("商家id为空");
			throw new FailureException(1,"商家id为空");
		}
		
		// 设置页码
		if (StringUtils.isBlank(paraMap.get("page"))) {
			paraMap.put("page", (page - 1) + "");
		} else {
			paraMap.put("page",
					(Integer.valueOf(paraMap.get("page")) - 1) + "");
		}
		// 设置页大小
		if (StringUtils.isBlank(paraMap.get("pageSize"))) {
			paraMap.put("pageSize", pageSize.toString());
		}
		
		String sellerid = paraMap.get("sellerid");//商家id
		
		/**
		 * 查询 收支总额，充值会员人数，记录总数
		 */
		Map<String, Object> sellerQuota = vCardDao.countQuotaByDate(paraMap);
		if(sellerQuota != null && Integer.valueOf(sellerQuota.get("sum")+"")<=0){
			log.info("商家该时间段内没有消费记录："+paraMap);
			return pageList;
		}else {
			pageList.setTotal(sellerQuota.get("total").toString());//收支总额
			pageList.setSum(sellerQuota.get("sum").toString());//总记录数
		}
		
		/**
		 * 查询某段日期内，每天总收支总额
		 */
		List<Map<String,Object>> recordGroupByDay = vCardDao.getRecordGroupByDay(paraMap);
			
		Map<String,String> businesMap = new HashMap<>();
		List<SubList> subList = new ArrayList<>();
		for (Map<String, Object> map : recordGroupByDay) {
			
			map.put("rtype",paraMap.get("rtype"));
			
			SubList sList = new SubList();
			List<Map<String,String>> list = new ArrayList<>();
			sList.setDealDate(map.get("dealDate")+"");
			sList.setSum(map.get("total").toString());
			List<Map<String, Object>> recordListByDay = vCardDao.getRecordListByDay(map);
			for (Map<String, Object> map2 : recordListByDay) {
				
				businesMap.put(map2.get("uid").toString(), map2.get("uid").toString());//获取所有用户id
				list.add(MapUtil.Object2String(map2));
				
			}
			sList.setDataList(list);
			subList.add(sList);
		}
		
		try {
			Map<String, String> userInfo = TSocket(businesMap);
			for (SubList subList2 : subList) {
				for (Map<String,String> map: subList2.getDataList()){
					if(userInfo.get(map.get("uid"))!=null){
						String[] str = userInfo.get(map.get("uid")).split(",");
						map.put("nname",str[0]);
						map.put("isvip",sellerid.equals(str[1])?"0":"1");
						map.put("avatar",str[2]);
						map.put("phone",str[3]);
						map.put("bandTime",str[4]);
						
						map.put("consumeTime",vCardDao.countConsumeTime(map).toString());//消费次数
					}
				}
			}
		} catch (Exception e) {
			log.info("调用业务系统查询用户信息异常",e);
		}
		pageList.setPageList(subList);
		
		log.info("获取储值卡充值消费详细记录成功");
		
		return pageList;
	}
	
	/*
	 * 调用业务服务查询用户信息
	 */
	public Map<String,String> TSocket(Map<String,String> paraMap) throws FailureException,Exception{
		log.info("调用业务系统接口用户信息");
		
		Map<String, String> userInfo = null;
		TTransport transport = null;
		try {
		    transport = new TSocket(ipNumbertBusine, ipPortBusine);
		    TFramedTransport frame = new TFramedTransport(transport);
		    // 设置传输协议为 TBinaryProtocol
		    TProtocol protocol = new TBinaryProtocol(frame);
		    TMultiplexedProtocol ManagerProtocol = new TMultiplexedProtocol(
			    protocol, "UserService");
		    UserService.Client client = new UserService.Client(ManagerProtocol);
		    // 打开端口,开始调用
		    transport.open();

		    userInfo = client.getUserInfo(paraMap);
		    
		} catch (Exception e) {
		    log.error("调用业务系统查询用户信息异常", e);
		    throw new FailureException(1, "调用业务系统查询用户信息异常");
		}finally {
		    transport.close();
		}
		return userInfo;
		
	}
	
	/**
	 * 获取商家充值会员列表
	 */
	@Override
	public SubList getUserList(Map<String, String> paraMap)
			throws FailureException, TException {
		log.info("获取商家充值会员列表getUserList"+paraMap);
		SubList subList = new SubList();
		if(StringUtils.isBlank(paraMap.get("sellerid"))){
			log.error("商家id为空");
			throw new FailureException(1,"商家id为空");
		}
		
		// 设置页码
		if (StringUtils.isBlank(paraMap.get("page"))) {
			paraMap.put("page", (page - 1) + "");
		} else {
			paraMap.put("page",
					(Integer.valueOf(paraMap.get("page")) - 1) + "");
		}
		// 设置页大小
		if (StringUtils.isBlank(paraMap.get("pageSize"))) {
			paraMap.put("pageSize", pageSize.toString());
		}
		
		/**
		 * 查询 收支总额，充值会员人数，记录总数
		 */
		Map<String, Object> sellerQuota = vCardDao.countBalance(paraMap.get("sellerid"));
		
		subList.setDealDate(sellerQuota.get("quota").toString());//剩余总额
		subList.setSum(sellerQuota.get("num").toString());//会员人数
		
		Integer num = Integer.valueOf(sellerQuota.get("num").toString());//会员人数
		String sellerid = paraMap.get("sellerid");
		
		Map<String,String> busMap = new HashMap<>();
		List<Map<String,String>> list = new ArrayList<>();
		if(num>0){
			List<Map<String, Object>> valueCard = vCardDao.getValueCard(paraMap);
			for (Map<String, Object> map : valueCard) {
				busMap.put(map.get("uid").toString(),map.get("uid").toString());
				list.add(MapUtil.Object2String(map));
			}
			
			try {
				Map<String, String> userInfo = TSocket(busMap);
					for (Map<String,String> map: list){
						if(userInfo.get(map.get("uid"))!=null){
							String[] str = userInfo.get(map.get("uid")).split(",");
							map.put("nname",str[0]);
							map.put("isvip",sellerid.equals(str[1])?"0":"1");
							map.put("avatar",str[2]);
						}
					}
			} catch (Exception e) {
				log.info("调用业务系统查询用户信息异常",e);
			}
		}else {
			log.info("商户："+paraMap.get("sellerid")+"没有充值会员");
		}
		
		subList.setDataList(list);
		
		log.info("获取商家充值会员列表成功");
		
		return subList;
	}
	
	/**
	 * 获取储值卡--会员详情
	 */
	@Override
	public Map<String, String> getUserDetail(Map<String, String> paraMap)
			throws FailureException, TException {
		log.info("获取储值卡--会员详情getUserDetail:"+paraMap);
		Map<String,String> resultMap = new HashMap<>();
		
		if(StringUtils.isBlank(paraMap.get("uid"))&&StringUtils.isBlank(paraMap.get("sellerid"))){
			log.error("uid或sellerid为空");
			throw new FailureException(1,"uid或sellerid为空");
		}
		
		//获取用户储值卡
		List<Map<String, Object>> valueCards = vCardDao.getValueCard(paraMap);
		if(valueCards == null || valueCards.size()<=0){
			log.info("用户在该商家没有储值卡："+paraMap);
			throw new FailureException(1,"用户在该商家没有储值卡");
		}
		
		Map<String, Object> vCard = valueCards.get(0);
		resultMap.put("quota",vCard.get("quota").toString());//剩余额度
		resultMap.put("totalQuota",vCard.get("cumulativeQuota").toString());//累计充值
		
		//调用业务服务获取用户 昵称、头像、手机号、关联商家 
		Map<String,String> busMap = new HashMap<String, String>();
		busMap.put(paraMap.get("uid"),paraMap.get("uid"));
		try {
			Map<String, String> userInfo = this.TSocket(busMap);
			if(userInfo !=null){
				String[] str = userInfo.get(paraMap.get("uid")).split(",");
				resultMap.put("nname",str[0]);//昵称
				resultMap.put("isvip",paraMap.get("sellerid").equals(str[1])?"0":"1");//是否是绑会员
				resultMap.put("avatar",str[2]);//头像
				resultMap.put("phone",str[3]);//用户手机
				resultMap.put("bandTime",str[4]);
			}
		} catch (Exception e) {
			log.info("调用业务服务查询用户信息失败：uid:"+paraMap.get("uid"),e);
		}
		
		Map<String, Object> userRecordInfo = vCardDao.getUserRecordInfo(paraMap);
		if(userRecordInfo != null){
			resultMap.put("firstTime", userRecordInfo.get("minTime")==null?"":userRecordInfo.get("minTime")+"");//首次消费时间
			resultMap.put("lastTime", userRecordInfo.get("maxTime")==null?"":userRecordInfo.get("maxTime")+"");//最近消费时间
			resultMap.put("times", userRecordInfo.get("sum")+"");//消费总次数
			resultMap.put("totalConsume", userRecordInfo.get("totalConsume")+"");//总消费金额
			resultMap.put("highConsume", userRecordInfo.get("maxQuota")==null?"0.00":userRecordInfo.get("maxQuota").toString());//最高消费
			resultMap.put("lowConsume", userRecordInfo.get("minQuota")==null?"0.00":userRecordInfo.get("minQuota").toString());//最低消费
			if(!"0".equals(resultMap.get("times"))){
				BigDecimal average = new BigDecimal(resultMap.get("totalConsume")).divide(new BigDecimal(resultMap.get("times")),2,BigDecimal.ROUND_HALF_UP);
				resultMap.put("average",average.toString());//平均消费
			}else{
				resultMap.put("average","0");//平均消费
			}
		}
		
		log.info("储值卡--会员详情查询成功");
		return resultMap;
	}
}
