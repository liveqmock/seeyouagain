/**
 * 
 */
package com.xmniao.xmn.core.manor.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xmniao.xmn.core.base.BaseDao;
import com.xmniao.xmn.core.base.BaseService;
import com.xmniao.xmn.core.base.Pageable;
import com.xmniao.xmn.core.coupon.dao.CouponDao;
import com.xmniao.xmn.core.manor.dao.ManorFlowerConvertDao;
import com.xmniao.xmn.core.manor.dao.ManorHoneyManageDao;
import com.xmniao.xmn.core.manor.dao.ManorInfoDao;
import com.xmniao.xmn.core.manor.dao.ManorSunshineCouponDao;
import com.xmniao.xmn.core.manor.entity.TManorFlowerConvert;
import com.xmniao.xmn.core.manor.entity.TManorHoneyManage;
import com.xmniao.xmn.core.thrift.client.proxy.ThriftClientProxy;
import com.xmniao.xmn.core.thrift.service.ResponsePageList;
import com.xmniao.xmn.core.xmnburs.dao.BursDao;
import com.xmniao.xmn.core.xmnburs.entity.Burs;
import com.xmniao.xmn.core.xmnburs.service.BursService;


@Service
public class ShopExchangeService extends BaseService<TManorHoneyManage> {

	/**
	 * 商店兑换管理服务
	 */
	@Autowired
	private ManorInfoDao manorInfoDao;
	
	@Autowired
	private ManorHoneyManageDao manorHoneyManageDao;
	
	@Autowired
	private ManorSunshineCouponDao manorSunshineCouponDao;
	
	@Autowired
	private ManorFlowerConvertDao manorFlowerConvertDao;
	
	@Autowired
	private BursDao bursDao;
	
	@Autowired
	private CouponDao couponDao;
	
	/**
	 * 寻蜜鸟用户Service
	 */
	@Autowired
	private BursService bursService;

	/**
	 * 注入获取庄园的服务
	 */
	@Resource(name = "manorRelatedServiceClient")
	private ThriftClientProxy manorRelatedServiceClient;	

	@SuppressWarnings("rawtypes")
	@Override
	protected BaseDao getBaseDao() {
		// TODO Auto-generated method stub
		return manorInfoDao;
	}

	private Long count = 0l;
	
	/**
	 * 方法描述：查询商店兑换管理数据 <br/>
	 * 创建人： Administrator <br/>
	 * 创建时间：2017年6月14日下午2:12:10 <br/>
	 * @param manorInfo
	 * @return
	 */
	public Pageable<TManorHoneyManage> getManorInfoInfoList(TManorHoneyManage manorHoneyManage) {
		Pageable<TManorHoneyManage> manorHoneyManageList = new Pageable<TManorHoneyManage>(manorHoneyManage);
		
		List<TManorHoneyManage> manorConvertRecordList = this.getManorSunshineRecordList(manorHoneyManage);
	
		manorHoneyManageList.setContent(manorConvertRecordList);
		manorHoneyManageList.setTotal(count);
		
	    return manorHoneyManageList;
	}
	
	
	public List<TManorHoneyManage> getManorSunshineRecordList(TManorHoneyManage manorHoneyManage){
		List<TManorHoneyManage> manorConvertRecordList = new ArrayList<TManorHoneyManage>();
		//通过会员昵称查询
		if ((manorHoneyManage.getNickname() != null && !"".equals(manorHoneyManage.getNickname())) 
				|| (manorHoneyManage.getPhone() != null && !"".equals(manorHoneyManage.getPhone()) ) ) {
			Burs burs = new Burs();
			if (manorHoneyManage.getNickname() != null){
				String nickname = manorHoneyManage.getNickname();
				burs.setNname(nickname);
			}
			if (manorHoneyManage.getPhone() != null){
				burs.setPhone(manorHoneyManage.getPhone());
			}
			List<Burs> bursList = bursService.getUrsList(burs);
			if (bursList != null && bursList.size() > 0) {
				Integer uid = bursList.get(0).getUid();
				manorHoneyManage.setUid(uid);
			}
		}
		
		try {
			//连接接口进行查询
			Map<String, String> params = new HashMap<>();
			if (manorHoneyManage.getUid() != null) {   //用户编号
				params.put("uid", manorHoneyManage.getUid().toString());
			}
			if (manorHoneyManage.getChannel() != null) {  //收支渠道, 1.购买花苗 2.购买施肥3.施肥
				params.put("channel", manorHoneyManage.getChannel().toString());
			}
			params.put("cPage", manorHoneyManage.getPage().toString());
			com.xmniao.xmn.core.thrift.service.manorService.ManorRelatedService.Client client = (com.xmniao.xmn.core.thrift.service.manorService.ManorRelatedService.Client) (manorRelatedServiceClient.getClient());
			log.info("查询用户庄园的阳光收益或支出流水开始");
			
			ResponsePageList response = client.getConvertRecord (params);
			if(response.getDataInfo().getState() != 0){
				log.error("调用修改指定收益类型提现手续费率失败");
				throw new RuntimeException("修改指定收益类型提现手续费率失败, 错误信息:"+ response.getDataInfo().getMsg());
			}
			
			log.info("查询用户庄园的阳光收益或支出流水结束，返回值：" + response.getDataInfo().getState());
		    //获取用户庄园的阳光收益或支出流水
			List<Map<String, String>> convertRecord = response.getPageList();
			
			count = new Long(response.getDataInfo().getResultMap().get("count"));
			manorConvertRecordList = this.getManorSunshineRecordFromMap(convertRecord);
			
		} catch (Exception e) {
			log.error("修改指定收益类型提现手续费率失败", e);
//			throw new ApplicationException("修改指定收益类型提现手续费率异常", e, new Object[] { liveDepositorsTaxes.getType() });
			
		} finally {
			manorRelatedServiceClient.returnCon();
		}
		
		return manorConvertRecordList;
	}
	
	public List<TManorHoneyManage> getManorSunshineRecordFromMap(List<Map<String, String>> convertRecordList) throws Exception{
		List<TManorHoneyManage> manorHoneyManageList = new ArrayList<TManorHoneyManage>();
		List<Integer> uids = new ArrayList<Integer>();
		for (Map<String, String> object : convertRecordList) {
			TManorHoneyManage bean = new TManorHoneyManage();
			Integer uid = new Integer(object.get("uid"));
			bean.setUid(uid);
			uids.add(uid);
			
			bean.setChannel(new Integer(object.get("channel")));
			if (object.get("createTime") != null){
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
				Date createTime = sdf.parse(object.get("createTime"));
				bean.setCreateTime(createTime);
			}
			/*BigDecimal num = new BigDecimal(object.get("outlay"));
			num = num.setScale(2, BigDecimal.ROUND_HALF_UP);  
			bean.setNum(num);*/

			String outlay = object.get("outlay");  // 兑换消耗鸟币/花蜜(滴)量
			bean.setOutlay(outlay);
			String income = object.get("income");  // 获取花朵/鸟币数量
			bean.setIncome(income);
			
			manorHoneyManageList.add(bean);
		}
		
		//设置用户名称 电话信息
		if (uids.size() > 0){
			List<Burs> bursList = bursDao.getUrsListByUids(uids.toArray());
			for (TManorHoneyManage manorSunshineInfo: manorHoneyManageList){
				for (Burs object : bursList) {
					if (manorSunshineInfo.getUid().equals(object.getUid()) ){
						manorSunshineInfo.setNickname(object.getNname());
						manorSunshineInfo.setPhone(object.getPhone());
					}
				}
			}
		}
		
		return manorHoneyManageList;
	}
	
	
	/**
	 * 方法描述：在此处添加方法描述 <br/>
	 * 创建人： Administrator <br/>
	 * 创建时间：2017年6月14日下午2:12:39 <br/>
	 * @param manorInfo
	 * @return
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor=Exception.class)
	public int saveUpdateActivity(TManorHoneyManage manorHoneyManage) throws Exception{
		int dataCount = 0;
		//花蜜兑换信息
		if (manorHoneyManage.getId() == null){
			dataCount = manorHoneyManageDao.insertSelective(manorHoneyManage);
		}else{
			dataCount = manorHoneyManageDao.updateByPrimaryKeySelective(manorHoneyManage);
		}
		
		//保存阳光兑换券信息
		this.saveManorSunshineCouponData(manorHoneyManage);

		return dataCount;
	}
	
	
	/**
	 * 方法描述：保存阳光兑换券信息 <br/>
	 * 创建人： Administrator <br/>
	 * 创建时间：2017年6月14日下午4:08:35 <br/>
	 * @param manorHoneyManage
	 * @throws Exception
	 */
	private void saveManorSunshineCouponData(TManorHoneyManage manorHoneyManage) throws Exception{
		int dataCount = 0;
		//删除所以兑换券信息
		manorFlowerConvertDao.deleteManorFlowerConvert(new TManorFlowerConvert());
		
		//阳光兑换券信息
		List<TManorFlowerConvert> manorFlowerConvertList = new ArrayList<TManorFlowerConvert>();
		
		String producutJsonJson = manorHoneyManage.getSunshineCouponJson();
		JSONObject fromObject = JSONObject.fromObject(producutJsonJson);
		JSONArray jsonArray = fromObject.getJSONArray("json");
		for (Object object : jsonArray) {
			TManorFlowerConvert manorFlowerConvert = new TManorFlowerConvert();
			//JSONObject -> bean
			TManorFlowerConvert bean = (TManorFlowerConvert) JSONObject.toBean(JSONObject.fromObject(object.toString()), TManorFlowerConvert.class);
			manorFlowerConvert.setCoin(bean.getCoin());
			manorFlowerConvert.setFlowers(bean.getFlowers());
			manorFlowerConvert.setLifeCycle(bean.getLifeCycle());
			manorFlowerConvert.setContent(bean.getContent());
			
			manorFlowerConvertList.add(manorFlowerConvert);
		}
		
		if (manorFlowerConvertList != null && manorFlowerConvertList.size() > 0)
			dataCount = manorFlowerConvertDao.insertManorFlowerConvertBatch(manorFlowerConvertList);
		
		log.info("保存阳光兑换优券信息, 共计：["+dataCount+"]条记录");
	}
	
	
	
	/**
	 * 方法描述：查询阳光兑换优券信息 <br/>
	 * 创建人： Administrator <br/>
	 * 创建时间：2017年6月14日下午2:42:12 <br/>
	 * @param manorSunshineCoupon
	 * @return
	 */
	/*public List<TManorSunshineCoupon> getManorSunshineCouponDataList(TManorSunshineCoupon manorSunshineCoupon) {
		List<TManorSunshineCoupon> manorSunshineCouponList = new ArrayList<TManorSunshineCoupon>();
		manorSunshineCouponList = manorSunshineCouponDao.getManorSunshineCouponList(manorSunshineCoupon);
		
		//每条记录需要转换成对应的json串；
		if (manorSunshineCouponList != null && manorSunshineCouponList.size() > 0) {
			for (TManorSunshineCoupon bean: manorSunshineCouponList){
                 //	把对象转换成JSONObject类型
//				    JSONObject map = JSONObject.fromObject(bean);
					ObjectMapper objectMapper = new ObjectMapper();
					try {
						String relationInfoJson = objectMapper.writeValueAsString(bean);
						bean.setSunshineCouponJson(relationInfoJson);
					} catch (JsonProcessingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}				
			}
		}

		return manorSunshineCouponList;
	}*/
	
	public List<TManorFlowerConvert> getManorFlowerConvertDataList(TManorFlowerConvert manorFlowerConvert) {
		List<TManorFlowerConvert> manorFlowerConvertList = manorFlowerConvertDao.getManorFlowerConvertList(manorFlowerConvert);
		
		//每条记录需要转换成对应的json串；
		if (manorFlowerConvertList != null && manorFlowerConvertList.size() > 0) {
			for (TManorFlowerConvert bean: manorFlowerConvertList){
                 //	把对象转换成JSONObject类型
//				    JSONObject map = JSONObject.fromObject(bean);
					ObjectMapper objectMapper = new ObjectMapper();
					try {
						String relationInfoJson = objectMapper.writeValueAsString(bean);
						bean.setProducutJson(relationInfoJson);
					} catch (JsonProcessingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}				
			}
		}

		return manorFlowerConvertList;
	}
	
	
	/**
	 * 方法描述：查询花蜜兑换信息 <br/>
	 * 创建人： Administrator <br/>
	 * 创建时间：2017年6月14日下午2:47:43 <br/>
	 * @param manorHoneyManage
	 * @return
	 */
	public TManorHoneyManage getManorHoneyManageData(TManorHoneyManage manorHoneyManage) {
		TManorHoneyManage manorHoneyManageData = manorHoneyManageDao.getManorHoneyManageData(manorHoneyManage);
		
		if (manorHoneyManageData != null){//查询阳光兑换优惠券信息
			List<TManorFlowerConvert> manorFlowerConvertList = this.getManorFlowerConvertDataList(new TManorFlowerConvert());
//			List<TCoupon> couponList = couponDao.getSelect(coupon); //获取优惠券信息
			manorHoneyManageData.setManorFlowerConvertList(manorFlowerConvertList);
		}
		
		return manorHoneyManageData;
	}
	
	
}
