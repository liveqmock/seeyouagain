/**
 * 
 */
package com.xmniao.xmn.core.reward_dividends.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xmniao.xmn.core.base.BaseDao;
import com.xmniao.xmn.core.base.BaseService;
import com.xmniao.xmn.core.base.Pageable;
import com.xmniao.xmn.core.billmanagerment.dao.AllBillDao;
import com.xmniao.xmn.core.reward_dividends.dao.TLiveGivedGiftVkeDao;
import com.xmniao.xmn.core.reward_dividends.entity.TLiveGivedGiftVke;
import com.xmniao.xmn.core.thrift.client.proxy.ThriftClientProxy;
import com.xmniao.xmn.core.xmnburs.entity.Burs;
import com.xmniao.xmn.core.xmnburs.service.BursService;

@Service
public class VerProfitCountService extends BaseService<TLiveGivedGiftVke> {

	@Autowired
	private BursService bursService;
	
	@Autowired
	private AllBillDao allBillDao;
	
	@Autowired
	private TLiveGivedGiftVkeDao liveGivedGiftVkeDao;
	
	/**
	 * 注入扩展钱包服务
	 */
	@Resource(name = "walletExpansionServiceClient")
	private ThriftClientProxy walletExpansionServiceClient;
	
	@SuppressWarnings("rawtypes")
	@Override
	protected BaseDao getBaseDao() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * 方法描述：充值订单查询商家 <br/>
	 * 创建人： Administrator <br/>
	 * 创建时间：2017年8月8日下午5:37:54 <br/>
	 * @param liveGivedGiftVke
	 * @return
	 */
	public Pageable<TLiveGivedGiftVke> getSellerProfitInfoList(TLiveGivedGiftVke liveGivedGiftVke) {
		Pageable<TLiveGivedGiftVke> liveGivedGiftVkeInfoList = new Pageable<TLiveGivedGiftVke>(liveGivedGiftVke);
		Long totalNum = (long) 0;

		List<TLiveGivedGiftVke> liveGivedGiftVkeList = this.getSellerProfitDetailList(liveGivedGiftVke);
		if (liveGivedGiftVkeList != null) {
			totalNum = this.getSellerProfitVkeTotalNum(liveGivedGiftVke);
		}
		
		liveGivedGiftVkeInfoList.setContent(liveGivedGiftVkeList);
		liveGivedGiftVkeInfoList.setTotal(totalNum);
		
	    return liveGivedGiftVkeInfoList;
	}
	
	public List<TLiveGivedGiftVke> getSellerProfitDetailList(TLiveGivedGiftVke liveGivedGiftVke) {
		List<TLiveGivedGiftVke>  liveGivedGiftVkeList = new ArrayList<TLiveGivedGiftVke>();
		
		//通过电话号码/用户昵称查询
		if (StringUtils.isNotBlank(liveGivedGiftVke.getVkeNname()) || StringUtils.isNotBlank(liveGivedGiftVke.getVkePhone())  ) {
			Burs burs = new Burs();
			if (StringUtils.isNotBlank(liveGivedGiftVke.getVkePhone()) ) {
				String phone = liveGivedGiftVke.getVkePhone();
				burs.setPhone(phone);
			}
			if (StringUtils.isNotBlank(liveGivedGiftVke.getVkeNname()) ) {
				String nickname = liveGivedGiftVke.getVkeNname();
				burs.setNname(nickname);
			}		
			List<Burs> bursList = bursService.getUrsList(burs);
			if (bursList != null && bursList.size() > 0 ){
				liveGivedGiftVke.setVkeUid(bursList.get(0).getUid());
			}else{
				return liveGivedGiftVkeList;
			}
		}
		// 根据uid查询出对应的消费订单信息
		liveGivedGiftVkeList = allBillDao.getVerProfitCountSellerList(liveGivedGiftVke);  //业务库
		if (liveGivedGiftVkeList != null ){
			List<Integer> uids = new ArrayList<Integer>();
			for (TLiveGivedGiftVke bean : liveGivedGiftVkeList) {
				//V客uid
				if (bean.getVkeUid() != null )
					uids.add(bean.getVkeUid());
			}
			if (uids.size() > 0 ){
				// 根据uid查询出V客信息
				List<Burs> bursList = bursService.getVerUrsDetailInfoList(uids.toArray());  //用户库
				for (TLiveGivedGiftVke bean : liveGivedGiftVkeList) {
					if (bean.getCommission() != null && bean.getSaasChannel() != null) {  //已推荐的商家数据
						JSONObject json = JSON.parseObject(bean.getCommission());  
						if (bean.getSaasChannel().equals(3) ){//3V客SAAS签约 4主播(V客赠送)SAAS签约
							bean.setVkeRatio(new Double(10));
							if (json.getString("mike_amount") != null){
								bean.setProfit(new BigDecimal(json.getString("mike_amount")).setScale(4, BigDecimal.ROUND_HALF_UP));
							}
						}else{
							bean.setVkeRatio(new Double(5));
							if (json.getString("parent_mike_amount") != null){
								bean.setProfit(new BigDecimal(json.getString("parent_mike_amount")).setScale(4, BigDecimal.ROUND_HALF_UP));
							}
						}
					}
					for (Burs burs : bursList) { //V客uid
						if (bean.getVkeUid() != null && bean.getVkeUid().equals(burs.getUid()) ){
							bean.setVkeNname(burs.getNname());
							bean.setVkePhone(burs.getPhone());
							bean.setVkeRankname(burs.getLevelName());
						}
					}
				}
			}
		}
		return liveGivedGiftVkeList;
	}
	
	
	public Long getSellerProfitVkeTotalNum(TLiveGivedGiftVke liveGivedGiftVke){
		Long totalNum = new Long(0);
		//通过电话号码/用户昵称查询
		if (StringUtils.isNotBlank(liveGivedGiftVke.getVkeNname()) || StringUtils.isNotBlank(liveGivedGiftVke.getVkePhone())  ) {
			Burs burs = new Burs();
			if (StringUtils.isNotBlank(liveGivedGiftVke.getVkePhone()) ) {
				String phone = liveGivedGiftVke.getVkePhone();
				burs.setPhone(phone);
			}
			if (StringUtils.isNotBlank(liveGivedGiftVke.getVkeNname()) ) {
				String nickname = liveGivedGiftVke.getVkeNname();
				burs.setNname(nickname);
			}		
			List<Burs> bursList = bursService.getUrsList(burs);
			if (bursList != null && bursList.size() > 0 ){
				liveGivedGiftVke.setVkeUid(bursList.get(0).getUid());
			}else{
				return totalNum;
			}
		}
		totalNum = allBillDao.countVerProfitBill(liveGivedGiftVke);
		return totalNum;
	}
	
	/**
	 * 方法描述：统计V客获得主播礼物收益<br/>
	 * 创建人： Administrator <br/>
	 * 创建时间：2017年8月14日上午11:33:51 <br/>
	 * @param liveGivedGiftVke
	 * @return
	 */
	public Pageable<TLiveGivedGiftVke> getLiveGivedGiftVkeInfoList(TLiveGivedGiftVke liveGivedGiftVke) {
		Pageable<TLiveGivedGiftVke> liveGivedGiftVkeInfoList = new Pageable<TLiveGivedGiftVke>(liveGivedGiftVke);
		Long totalNum = (long) 0;
		List<TLiveGivedGiftVke>  liveGivedGiftVkeList = this.getLiveGivedGiftVkeDataList(liveGivedGiftVke);
		totalNum = this.getLiveGivedGiftVkeTotalNum(liveGivedGiftVke);

		liveGivedGiftVkeInfoList.setContent(liveGivedGiftVkeList);
		liveGivedGiftVkeInfoList.setTotal(totalNum);
		
	    return liveGivedGiftVkeInfoList;
	}
	
	public List<TLiveGivedGiftVke> getLiveGivedGiftVkeDataList(TLiveGivedGiftVke liveGivedGiftVke){
		// 根据uid查询出对应的消费订单信息
		List<TLiveGivedGiftVke>  liveGivedGiftVkeList = liveGivedGiftVkeDao.getVerProfitLiveGiftList(liveGivedGiftVke);
		/*for (TLiveGivedGiftVke bean: liveGivedGiftVkeList){
			BigDecimal profit = bean.getMoney().divide(new BigDecimal("1000"), 4, BigDecimal.ROUND_UP);
			double value  =   profit.multiply(new BigDecimal(bean.getVkeRatio())).setScale(4,BigDecimal.ROUND_UP).doubleValue();
			bean.setProfit(value);
		}*/
		
		return liveGivedGiftVkeList;
	}

	public Long getLiveGivedGiftVkeTotalNum(TLiveGivedGiftVke liveGivedGiftVke){
		Long totalNum = new Long(0);
		
		totalNum = liveGivedGiftVkeDao.countVerProfitLiveGift(liveGivedGiftVke);
		return totalNum;
	}
	
	/**
	 * 方法描述：统计店铺收益总数 <br/>
	 * 创建人： Administrator <br/>
	 * 创建时间：2017年9月7日上午11:16:25 <br/>
	 * @param liveGivedGiftVke
	 * @return
	 */
	public Map<String, Object> getVkeProfitSellerCount(TLiveGivedGiftVke liveGivedGiftVke){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		BigDecimal totalProfit = new BigDecimal(0);
		liveGivedGiftVke.setLimit(-1);
		List<TLiveGivedGiftVke> liveGivedGiftVkeList = this.getSellerProfitDetailList(liveGivedGiftVke);
	    for (TLiveGivedGiftVke bean: liveGivedGiftVkeList){
	    	totalProfit = totalProfit.add(bean.getProfit() == null ? new BigDecimal(0) : bean.getProfit());
	    }
	    resultMap.put("totalProfit", totalProfit);
	    
	    Map<String, Object> params = new HashMap<String, Object>();
		if (liveGivedGiftVke.getVkeUid() != null)
			params.put("vkeUid", liveGivedGiftVke.getVkeUid());
		if (liveGivedGiftVke.getSdateStart() != null){
			params.put("sdateStart", liveGivedGiftVke.getSdateStart());
			resultMap.put("sdateStart", liveGivedGiftVke.getSdateStart());
		}else{
			String dateString = "2017-01-01 00:00:01";
			resultMap.put("sdateStart", dateString);
		}
		if (liveGivedGiftVke.getSdateEnd() != null){
			params.put("sdateEnd", liveGivedGiftVke.getSdateEnd());
			resultMap.put("sdateEnd", liveGivedGiftVke.getSdateEnd());
		}else{	
			resultMap.put("sdateEnd", new SimpleDateFormat("yyyy-MM-dd HH:MM:ss").format(new Date()));
		}
			
		Integer vkeProfitVkeCount = allBillDao.getVkeProfitVkeCount(params);
		resultMap.put("vkeProfitVkeCount", vkeProfitVkeCount);
		Integer vkeProfitSellerCount = allBillDao.getVkeProfitSellerCount(params);
		resultMap.put("vkeProfitSellerCount", vkeProfitSellerCount);
		
		return resultMap;
	}
	
	
	/**
	 * 方法描述：统计主播打赏收益<br/>
	 * 创建人： Administrator <br/>
	 * 创建时间：2017年9月7日上午11:16:28 <br/>
	 * @param liveGivedGiftVke
	 * @return
	 */
	public Map<String, Object> getVkeProfitGiftCount(TLiveGivedGiftVke liveGivedGiftVke){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		BigDecimal totalProfit = new BigDecimal(0);
		liveGivedGiftVke.setLimit(-1);
		List<TLiveGivedGiftVke>  liveGivedGiftVkeList = this.getLiveGivedGiftVkeDataList(liveGivedGiftVke);
	    for (TLiveGivedGiftVke bean: liveGivedGiftVkeList){
	    	totalProfit = totalProfit.add(bean.getProfit() == null ? new BigDecimal(0) : bean.getProfit());
	    }
	    resultMap.put("totalProfit", totalProfit);
	    
	    Map<String, Object> params = new HashMap<String, Object>();
		if (liveGivedGiftVke.getVkeUid() != null)
			params.put("vkeUid", liveGivedGiftVke.getVkeUid());
		if (liveGivedGiftVke.getSdateStart() != null){
			params.put("sdateStart", liveGivedGiftVke.getSdateStart());
			resultMap.put("sdateStart", new SimpleDateFormat("yyyy-MM-dd HH:MM").format(liveGivedGiftVke.getSdateStart()));  //new SimpleDateFormat("yyyy-MM-dd HH:MM:ss")
		}else{
			String dateString = "2017-01-01 00:00";
			resultMap.put("sdateStart", dateString);
		}
		if (liveGivedGiftVke.getSdateEnd() != null){
			params.put("sdateEnd", liveGivedGiftVke.getSdateEnd());
			resultMap.put("sdateEnd", new SimpleDateFormat("yyyy-MM-dd HH:MM").format(liveGivedGiftVke.getSdateEnd()));
		}else{	
			resultMap.put("sdateEnd", new SimpleDateFormat("yyyy-MM-dd HH:MM").format(new Date()));
		}
			
		Integer vkeProfitVkeCount = liveGivedGiftVkeDao.getVkeProfitVkeCount(params);
		resultMap.put("vkeProfitVkeCount", vkeProfitVkeCount);
		resultMap.put("vkeProfitCount", liveGivedGiftVkeList.size());
		
		return resultMap;
	}
}
