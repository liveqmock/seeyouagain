package com.xmniao.xmn.core.xmer.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.xmniao.xmn.core.base.BaseResponse;
import com.xmniao.xmn.core.base.MongoBaseService;
import com.xmniao.xmn.core.base.SessionTokenService;
import com.xmniao.xmn.core.common.Constant;
import com.xmniao.xmn.core.common.MapResponse;
import com.xmniao.xmn.core.common.ResponseCode;
import com.xmniao.xmn.core.common.request.ModifySellerInfoRequest;
import com.xmniao.xmn.core.util.DateUtil;
import com.xmniao.xmn.core.util.SaasBidType;
import com.xmniao.xmn.core.xmer.dao.ModifySellerInfoDao;
import com.xmniao.xmn.core.xmer.dao.SaasOrderDao;
import com.xmniao.xmn.core.xmer.dao.SellerInfoDao;
import com.xmniao.xmn.core.xmer.dao.XmerDao;
import com.xmniao.xmn.core.xmer.entity.SaasSignOrder;
import com.xmniao.xmn.core.xmer.entity.XmerPic;

/**
 * 
 * 项目名称：xmnService 类名称：ModifySellerInfoService 类描述：商户信息service 创建人：liuzhihao
 * 创建时间：2016年5月24日 下午5:47:29
 * 
 * @version
 * 
 */
@Service
public class ModifySellerInfoService {
	
	//日志报错
	private final Logger log = Logger.getLogger(ModifySellerInfoService.class);

	// 注入修改dao
	@Autowired
	private ModifySellerInfoDao modifySellerInfoDao;

	// 注入商铺dao
	@Autowired
	private SellerInfoDao sellerInfoDao;

	// 注入订单dao
	@Autowired
	private SaasOrderDao saasOrderDao;
	
	/**
	 * 寻蜜客信息
	 */
	@Autowired
	private XmerDao xmerDao;
	
	@Autowired
	private SaasOrderService saasOrderService;
	// 注入缓存
	@Autowired
	private SessionTokenService sessionTokenService;

	// 注入mongoDB
	@Autowired
	private MongoBaseService mongoBaseService;

	// 注入上传文件根地址
	@Autowired
	private String fileUrl;

	// 全局
	private MapResponse response;

	
	/**
	 * 
	* @Title: ModifySellerInfoTwo
	* @Description: 版本二:店铺资料修改  版本一已删除
	* @return Object    返回类型
	* @author
	* @throws
	 */
	public Object ModifySellerInfoTwo(ModifySellerInfoRequest modifySellerInfoRequest) {
		try {
			Integer sellerid = modifySellerInfoRequest.getSellerid();
			Integer recodeid = modifySellerInfoDao.querySellerInfoRecord(sellerid);
			if(recodeid == null){
				Map<Object, Object> sellerInfoMap = modifySellerInfoDao.querySellerInfo(sellerid);
				Integer isonline =	Integer.parseInt(String.valueOf(sellerInfoMap.get("isonline")));
				Integer sellerStatus =	Integer.parseInt(String.valueOf(sellerInfoMap.get("status")));
				//商铺签约成功，且未下线
				if (sellerStatus == 6) {
					return new BaseResponse(ResponseCode.FAILURE, "商家已注销,请联系商户或联系客服！");
				}else if(sellerStatus==3&&isonline!=3){
					/*return 	modifyOnlineSellerInfoTwo(modifySellerInfoRequest);*/
					return new BaseResponse(ResponseCode.FAILURE, "不能修改已签约成功商户资料，请联系商户修改或联系客服！");
				}else{//商铺未上线时
					if (modifySellerInfoRequest.getType() == 0) {
						// 提交修改资料(修改草稿)
						editSellerInfoTwo(modifySellerInfoRequest);
						//修改商铺折扣表
						Map<Object,Object> agioMap = new HashMap<Object,Object>();
						agioMap.put("agio", modifySellerInfoRequest.getAgio());
						agioMap.put("sellerid", sellerid);
						try{
							//查询是否有设置商家折扣
							int count=sellerInfoDao.queryAgioCount(sellerid);
							if(count==0){
								sellerInfoDao.insertSellerAgio(agioMap);//插入折扣表
								sellerInfoDao.insertSellerAgioRecord(agioMap);//插入折扣记录表
								
							}else{
								modifySellerInfoDao.modifySellerAgio(agioMap);
								//修改商家折扣记录
								modifySellerInfoDao.modifyAgioReocrd(agioMap);
							}
						}catch(Exception e){
							e.printStackTrace();
							log.info("修改商铺折扣表异常");
						}
						// 编辑资料成功
						return new BaseResponse(ResponseCode.SUCCESS, "提交申请成功");
					}
					if (modifySellerInfoRequest.getType() == 1) {
						// 提交并审核(确定修改)
						if (editSellerInfoTwo(modifySellerInfoRequest) != 0) {
							//修改商铺折扣表
							Map<Object,Object> agioMap = new HashMap<Object,Object>();
							agioMap.put("agio", modifySellerInfoRequest.getAgio());
							agioMap.put("sellerid", sellerid);
							try{
								//查询是否有设置商家折扣
								int count=sellerInfoDao.queryAgioCount(sellerid);
								if(count==0){
									sellerInfoDao.insertSellerAgio(agioMap);//插入折扣表
									sellerInfoDao.insertSellerAgioRecord(agioMap);//插入折扣记录表
									
								}else{
									modifySellerInfoDao.modifySellerAgio(agioMap);
									//修改商家折扣记录
									modifySellerInfoDao.modifyAgioReocrd(agioMap);
								}
							}catch(Exception e){
								e.printStackTrace();
								log.info("修改商铺折扣表异常");
							}
							Map<Object, Object> result = new HashMap<>();
							//查询商家签约订单
							Map<Object, Object> map = modifySellerInfoDao.querySellerId(sellerid);
							String uid = sessionTokenService.getStringForValue(modifySellerInfoRequest.getSessiontoken()).toString();
							String orderId = "02" + SaasBidType.getBid();
							if (map == null) {		//如果没有订单，则重新创建订单、
								SaasSignOrder saasSignOrder = new SaasSignOrder();
								//查询寻蜜客最早购买的saas套餐的未使用完的订单信息
								Map<Object,Object> orderMap = saasOrderDao.queryOrderNums(Integer.valueOf(uid));
								if(orderMap==null||orderMap.size()==0){
									return new BaseResponse(ResponseCode.SAAS_STOCK_EMPTY, "SAAS套餐已使用完毕，请重新购买");
								}
								//签约店铺使用的套餐订单号
								saasSignOrder.setSaasOrdersn(orderMap.get("ordersn").toString());
								saasSignOrder.setId(orderId);
								saasSignOrder.setUid(uid);
								saasSignOrder.setAmount(Constant.SIGN_AMOUNT);
								saasSignOrder.setSellerid(String.valueOf(sellerid));
								saasSignOrder.setSellername(modifySellerInfoRequest.getSellername());
								saasSignOrder.setStatus(0);  //未支付还是未签约状态
								saasSignOrder.setZdate(DateUtil.format(DateUtil.now()));
								saasSignOrder.setCreateDate(DateUtil.format(DateUtil.now()));
								saasOrderDao.addSaasSignOrder(saasSignOrder);
								
								//更改saas购买过的套餐销售数量
								int soldnums = Integer.valueOf(orderMap.get("soldnums").toString())+1;//卖出订单数量(原有的基础上+1)
								int stock = Integer.valueOf(orderMap.get("stock").toString())-1;//库存数量(原有的基础上-1)
								int version=Integer.valueOf(orderMap.get("version").toString());//版本控制
								orderMap.put("soldnums", soldnums);
								orderMap.put("stock", stock);
								orderMap.put("version", version);//版本控制，防止并发
								int updateOrderFlag=saasOrderDao.updateOrderNums(orderMap);//根据订单id更新卖出数量和库存量
								if(updateOrderFlag==0){ //如果更新失败
									return new BaseResponse(ResponseCode.XMER_VERSION_ERROR,"提交审核出错，请刷新重新提交！");
								}
								xmerDao.addSignNum(Integer.valueOf(uid));	//新增签约数量
								Map<Object, Object> sellerinfoMap = new HashMap<>();
								sellerinfoMap.put("status", 4);	//已提交订单，未支付
								sellerinfoMap.put("sellerid", sellerid);
								//修改订单状态
								sellerInfoDao.modifySeller(sellerinfoMap);	
								//修改寻蜜客资料的头衔 
								Map<Object,Object> levelMap = new HashMap<Object,Object>();
								levelMap.put("uid", uid);
								levelMap.put("achievement", saasOrderService.getXmerLevelName(Integer.parseInt(uid)));
								saasOrderDao.modifyXmer(levelMap);//更新寻蜜客头衔
								response = new MapResponse(ResponseCode.SUCCESS, "提交成功");
								result.put("amount", saasSignOrder.getAmount());
								result.put("orderid", orderId);
								result.put("ordername", "寻蜜客签约商户");
								result.put("sellerid",sellerid);
								//判断订单是否支付完成
								Map<Object, Object> paramMap = new HashMap<>();
								paramMap.put("sellerid", sellerid);
								paramMap.put("uid", Integer.valueOf(uid));
								Integer status = saasOrderDao.querySaasOrder(paramMap);
								result.put("ispay", status);	//订单是否支付过  0 未支付    1 已支付
								response.setResponse(result);
								return response;
							}	
							//如果有订单，判断支付状态
							Integer status = Integer.valueOf(map.get("status").toString());
							switch (status) {
								case 0:// 未支付状态时   查看是否有这个店铺的订单，如果有订单则修改订单号 ，如果没有则新增订单
										// 修改订单号
									Map<Object, Object> updatemap = new HashMap<Object, Object>();
									updatemap.put("sellerid",sellerid);
									updatemap.put("orderid", orderId);
									updatemap.put("ordersn", map.get("ordersn"));
									modifySellerInfoDao.modifyOerderId(updatemap);
									// 更新成功后
									response = new MapResponse(ResponseCode.SUCCESS,"更新账单成功");
									result.put("orderid", orderId);
									result.put("amount", map.get("amount"));
									result.put("ordername", "寻蜜客签约商户");
									result.put("sellerid",sellerid);
									result.put("ispay", 0);	//订单是否支付过  0 未支付    1 已支付
									response.setResponse(result);
									return response;
								default:
									// 已支付状态，修改资料后，修改店铺状态
									editSellerInfoTwo(modifySellerInfoRequest);
									if(sellerStatus==2){		//如果是审核不通过状态下的商户，则查询扣除订单签约数量等数据
										Map<Object,Object> orderMap = saasOrderDao.queryOrderNums(Integer.valueOf(uid));
										if(orderMap==null||orderMap.size()==0){
											return new BaseResponse(ResponseCode.SAAS_STOCK_EMPTY, "SAAS套餐已使用完毕，请重新购买");
										}	
										int soldnums = Integer.valueOf(orderMap.get("soldnums").toString())+1;//卖出订单数量(原有的基础上+1)
										//优先扣除退款套数
										int returnnums = Integer.parseInt(orderMap.get("returnnums").toString());
										int stock = Integer.valueOf(orderMap.get("stock").toString())-1;//库存数量(原有的基础上-1)
										int version=Integer.valueOf(orderMap.get("version").toString());//版本控制
										//修改签约店铺套餐来源
										SaasSignOrder saasSignOrder = new SaasSignOrder();
										saasSignOrder.setId(map.get("ordersn").toString());
										if (returnnums == 0 && stock > 0) {
											stock = stock - 1;//库存数量(原有的基础上-1)
											//'SAAS来源 0 正常库存 1 销售退回库存'
											saasSignOrder.setSaasSource(0);
										}else if(returnnums > 0) {
											returnnums = returnnums - 1;
											//'SAAS来源 0 正常库存 1 销售退回库存'
											saasSignOrder.setSaasSource(1);
										}else {
											return new BaseResponse(ResponseCode.SAAS_STOCK_EMPTY, "SAAS套餐异常,请联系客服");
										}
										
										//更新签约店铺订单的来源
										saasOrderDao.updateSaasSoldOrder(saasSignOrder);
										
										orderMap.put("soldnums", soldnums);
										orderMap.put("stock", stock);
										orderMap.put("returnnums", returnnums);
										orderMap.put("version", version);//版本控制，防止并发
										int updateOrderFlag=saasOrderDao.updateOrderNums(orderMap);//根据订单id更新卖出数量和库存量
										if(updateOrderFlag==0){ //如果更新失败
											return new BaseResponse(ResponseCode.XMER_VERSION_ERROR,"提交审核出错，请刷新重新提交！");
										}
										xmerDao.addSignNum(Integer.valueOf(uid));	//新增签约数量
										//修改寻蜜客资料的头衔 
										Map<Object,Object> levelMap = new HashMap<Object,Object>();
										levelMap.put("uid", uid);
										levelMap.put("achievement", saasOrderService.getXmerLevelName(Integer.parseInt(uid)));
										saasOrderDao.modifyXmer(levelMap);//更新寻蜜客头衔
									}
									
									Map<Object, Object> statusMap = new HashMap<>();
									statusMap.put("status", 1);		//将商家状态改成审核中
									statusMap.put("sellerid", modifySellerInfoRequest.getSellerid());
									modifySellerInfoDao.modifySellerInfo(statusMap);
									response = new MapResponse(ResponseCode.SUCCESS,"修改成功");
									result.put("orderid", map.get("ordersn"));
									result.put("amount", map.get("amount"));
									result.put("ordername", "寻蜜客签约商户");
									result.put("sellerid",sellerid);
									result.put("ispay", 1);	//订单是否支付过  0 未支付    1 已支付
									response.setResponse(result);
									return response;
							}
						}
					}
					return new BaseResponse(ResponseCode.FAILURE, "提交申请失败");
				}
			}
			return new BaseResponse(ResponseCode.FAILURE, "你已提交了修改审核信息，还未审核通过，暂时不能提交");
		} catch (Exception e) {
			e.printStackTrace();
			return new BaseResponse(ResponseCode.FAILURE, "未知错误，请联系管理员");
		}
	}

	
	/**
	 * 
	* @Title: editSellerInfo
	* @Description: 版本二:编辑资料
	* @return int    返回类型
	* @author
	* @throws
	 */
	private int editSellerInfoTwo(ModifySellerInfoRequest modifySellerInfoRequest) {
		try {
			// 更新人均消费
			Map<Object, Object> consumermap = new HashMap<Object, Object>();
			consumermap.put("consume", modifySellerInfoRequest.getConsume());
			consumermap.put("sellerid", modifySellerInfoRequest.getSellerid());
			modifySellerInfoDao.modifySellerInfoToDetail(consumermap);
			// 更新商铺信息
			modifySellerInfoDao.modifySellerInfo(getUpdateMap(modifySellerInfoRequest));
			
			//更新商铺经纬度
			Map<Object, Object> paramMap = new HashMap<>();
			paramMap.put("sellerid", modifySellerInfoRequest.getSellerid());//经度
			paramMap.put("longitude", modifySellerInfoRequest.getLongitude());//经度
			paramMap.put("latitude", modifySellerInfoRequest.getLatitude());//纬度
			paramMap.put("sdate", DateUtil.format(new Date()));//纬度
			sellerInfoDao.updateSellerLandmark(paramMap);//更改商家经度和纬度
			
			// 更新环境图片
			modifyPic(modifySellerInfoRequest);
			// 更新封面图
			Map<Object, Object> covermap = new HashMap<Object, Object>();
			covermap.put("sellerid", modifySellerInfoRequest.getSellerid());
			covermap.put("islogo", 2);	//logo
			// 删除原有的封面图
			modifySellerInfoDao.deleteSellerPic(covermap);
			//插入新的封面图
			covermap.put("url",modifySellerInfoRequest.getCover().replace(fileUrl, ""));
			covermap.put("sdate", DateUtil.format(DateUtil.now()));
			modifySellerInfoDao.insertCoverPic(covermap);
			//修改商铺logo图
			covermap.clear();
			covermap.put("sellerid", modifySellerInfoRequest.getSellerid());
			covermap.put("islogo", 1);	//logo
			//删除原有的logo图
			modifySellerInfoDao.deleteSellerPic(covermap);
			//插入新的logo图
			covermap.put("url",modifySellerInfoRequest.getLogo().replace(fileUrl, ""));
			covermap.put("sdate", DateUtil.format(DateUtil.now(),"yyyy-MM-dd HH:mm:ss"));
			int result = modifySellerInfoDao.insertCoverPic(covermap);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}


	/**
	 * 
	 * @Title: getUpdateMap
	 * @Description: 初始化参数
	 * @return Map<Object,Object> 返回类型
	 * @throws
	 */
	private Map<Object, Object> getUpdateMap(ModifySellerInfoRequest modifySellerInfoRequest) {
		// 创建存数据map实例
		Map<Object, Object> map = new HashMap<Object, Object>();
		// 传入数据存储到map中
		map.put("sellerid", modifySellerInfoRequest.getSellerid());
		map.put("sellername", modifySellerInfoRequest.getSellername());
		map.put("province", modifySellerInfoRequest.getProvince());
		map.put("city", modifySellerInfoRequest.getCity());
		map.put("area", modifySellerInfoRequest.getArea());
		map.put("address", modifySellerInfoRequest.getAddress());
		map.put("category", modifySellerInfoRequest.getCategory().toString());
		map.put("genre", modifySellerInfoRequest.getGenre().toString());
		map.put("bewrite", modifySellerInfoRequest.getBewrite());
		map.put("tradename", modifySellerInfoRequest.getBewrite());//二级分类名称
		map.put("fullname", modifySellerInfoRequest.getFullname());
		map.put("phoneid", modifySellerInfoRequest.getPhoneid());
		map.put("tel", modifySellerInfoRequest.getTel());
		map.put("agio", modifySellerInfoRequest.getAgio());
		map.put("sdate", modifySellerInfoRequest.getSdate());
		map.put("identitynurl", modifySellerInfoRequest.getIdentitynurl().replace(fileUrl, ""));
		map.put("licenseurl",modifySellerInfoRequest.getLicenseurl().replace(fileUrl, ""));
		map.put("licensefurl", modifySellerInfoRequest.getLicensefurl().replace(fileUrl, ""));
		map.put("identityzurl", modifySellerInfoRequest.getIdentityzurl().replace(fileUrl, ""));
		map.put("identityfurl", modifySellerInfoRequest.getIdentityfurl().replace(fileUrl, ""));
		map.put("svalidity", modifySellerInfoRequest.getSvalidity());
		map.put("evalidity", modifySellerInfoRequest.getEvalidity());
		map.put("longitude", modifySellerInfoRequest.getLongitude());
		map.put("latitude", modifySellerInfoRequest.getLatitude());
		map.put("udate", DateUtil.now());
		//查询经营分类名称
		Map<Object,Object> tradeMap = new HashMap<Object,Object>();
		tradeMap.put("tid", modifySellerInfoRequest.getCategory());//一级id
		String typename = sellerInfoDao.queryTradeName(tradeMap);
		map.put("typename", typename);//经营分类名称
		return map;
	}
	// 修改图片
	private int modifyPic(ModifySellerInfoRequest modifySellerInfoRequest) {
		List<Map<Object, Object>> piclist = new ArrayList<Map<Object, Object>>();
		String sellerpic = modifySellerInfoRequest.getSellerpic();
		JSONArray sellerary = JSON.parseArray(sellerpic);
		for (int i = 0; i < sellerary.size(); i++) {
			// json格式的字符串转实体bean
			XmerPic xmerPic = JSON.parseObject(sellerary.getString(i),
					XmerPic.class);
			Map<Object, Object> map = new HashMap<Object, Object>();
			map.put("sellerid", modifySellerInfoRequest.getSellerid());
			map.put("url", xmerPic.getUrl().replace(fileUrl, ""));
			map.put("islogo", 0);// 环境图
			map.put("sdate", DateUtil.format(DateUtil.now(), "yyyy-MM-dd HH:mm:ss"));
			piclist.add(map);
		}
		// 删除该sellerid下的所有图片
		Map<Object, Object> deletemap = new HashMap<Object, Object>();
		deletemap.put("sellerid", modifySellerInfoRequest.getSellerid());
		deletemap.put("islogo", 0);
		modifySellerInfoDao.deleteSellerPic(deletemap);
		// 插入环境图
		return modifySellerInfoDao.insertSellerPic(piclist);
	}
	
	
	/**
	 * 
	* @Title: getPicList
	* @Description: 组装图片集合
	* @return List<Map<Object,Object>>
	 */
	public List<Map<Object,Object>> getPicList(ModifySellerInfoRequest modifySellerInfoRequest,Map<Object, Object> recordMap){
		//存图片map
		List<Map<Object,Object>> picList = new ArrayList<Map<Object,Object>>();
		String sellerPic = modifySellerInfoRequest.getSellerpic();
		JSONArray sellerary = JSON.parseArray(sellerPic);
		for (int i = 0; i < sellerary.size(); i++) {
			// json格式的字符串转实体bean
			XmerPic xmerPic = JSON.parseObject(sellerary.getString(i),XmerPic.class);
			Map<Object,Object> picMap = new HashMap<Object,Object>();
			picMap.put("sellerid", modifySellerInfoRequest.getSellerid());
			picMap.put("url", xmerPic.getUrl().replace(fileUrl, ""));
			picMap.put("type", 1);// 环境图
			picMap.put("said", recordMap.get("id"));
			picMap.put("udate", DateUtil.now());
			picList.add(picMap);
		}
		return picList;
	}
	

	/**
	 * 
	* @Title: modifySellerInfoTwo
	* @Description: 修改签约成功的店铺 版本二
	* @return Object
	 */
	public Object modifyOnlineSellerInfoTwo(ModifySellerInfoRequest modifySellerInfoRequest){
		try {
			Integer sellerid = modifySellerInfoRequest.getSellerid();
			modifySellerInfoRequest.setProvince(modifySellerInfoRequest.getProvince());//省编号
			modifySellerInfoRequest.setCity(modifySellerInfoRequest.getCity());//市编号
			modifySellerInfoRequest.setArea(modifySellerInfoRequest.getArea());//区域编号
			//初始化修改提交参数
			 Map<Object,Object> updateMap = getUpdateMap(modifySellerInfoRequest);
			 //插入一条修改记录
			 Map<Object,Object> recordMap = new HashMap<Object,Object>();
			 recordMap.put("sellerid", updateMap.get("sellerid"));
			 recordMap.put("sellername", updateMap.get("sellername"));
			 recordMap.put("logo", modifySellerInfoRequest.getLogo());//商铺头像logo
			 recordMap.put("province", updateMap.get("province"));
			 recordMap.put("city", updateMap.get("city"));
			 recordMap.put("area", updateMap.get("area"));
			 recordMap.put("address", updateMap.get("address"));
			 recordMap.put("sdate", DateUtil.format(DateUtil.now(), "yyyy-MM-dd HH:mm:ss"));//申请时间
			 recordMap.put("phone", updateMap.get("phoneid"));
			 recordMap.put("latitude", updateMap.get("latitude"));
			 recordMap.put("longitude", updateMap.get("longitude"));
			 try{
/*				 modifySellerInfoDao.insertSellerInfoRecord(recordMap);
*/					if(!isUpdateBaseInfo(recordMap,Integer.valueOf(modifySellerInfoRequest.getSellerid().toString()))){//如果基本信息有修改，添加一条信息修改记录
						//插入一条记录到修改记录表中（t_seller_apply）
						recordMap.put("source", 1);//商家基本信息
						recordMap.put("status", 0);//待审核
						//recordMap.put("sexplain", mdfSellerInfo.getSexplain());//商家说明
						modifySellerInfoDao.insertSellerInfoRecord(recordMap);//插入审核记录
					}
			 }catch(Exception e){
				 e.printStackTrace();
				 log.info("插入商铺修改信息记录异常");
			 }
			 //修改商铺人均消费(不需要审核即修改)
			 Map<Object, Object> consumerMap = new HashMap<Object, Object>();
			 consumerMap.put("consume", modifySellerInfoRequest.getConsume());
			 consumerMap.put("sellerid", sellerid);
			 try{
				 modifySellerInfoDao.modifySellerInfoToDetail(consumerMap);	//TODO MongoDB 修改
			 }catch(Exception e){
				 e.printStackTrace();
				 log.info("修改商铺人均消费信息异常");
			 }
			//修改商铺座机以及折扣信息
			Map<Object,Object> sellerMap = new HashMap<Object,Object>();
			sellerMap.put("tel", updateMap.get("tel"));
//			sellerMap.put("agio", updateMap.get("agio"));
			sellerMap.put("sdate", updateMap.get("sdate"));
			sellerMap.put("sellerid", sellerid);
			sellerMap.put("category", updateMap.get("category"));//一级类别id
			sellerMap.put("genre", updateMap.get("genre"));//二级类别id
			sellerMap.put("bewrite", updateMap.get("bewrite"));//二级类别描述
			sellerMap.put("tradename", updateMap.get("bewrite"));//二级类别名称
//			sellerMap.put("svalidity", updateMap.get("svalidity"));//有效期开始
//			sellerMap.put("evalidity", updateMap.get("evalidity"));//有效期结束
			sellerMap.put("identitynurl", updateMap.get("identitynurl"));//手持身份证照片URL
			sellerMap.put("licenseurl",updateMap.get("licenseurl"));//执照电子版URL
			sellerMap.put("licensefurl", updateMap.get("licensefurl"));//营业执照电子版反面URL
			sellerMap.put("identityzurl", updateMap.get("identityzurl"));//身份证附件正面
			sellerMap.put("identityfurl", updateMap.get("identityfurl"));//身份证附件反面
			sellerMap.put("typename", updateMap.get("typename"));//一级分类名称
			sellerMap.put("udate", DateUtil.now());
			try{
				modifySellerInfoDao.modifySellerInfo(sellerMap);	//TODO MongoDB 修改
			}catch(Exception e){
				e.printStackTrace();
				log.info("修改商铺信息异常");
			}
			//寻蜜客不能直接修改商家折扣
			/*//修改商铺折扣表
			Map<Object,Object> agioMap = new HashMap<Object,Object>();
			agioMap.put("agio", updateMap.get("agio"));
			agioMap.put("sellerid", sellerid);
			try{
				modifySellerInfoDao.modifySellerAgio(agioMap);
				//修改商家折扣记录
				modifySellerInfoDao.modifyAgioReocrd(agioMap);
			}catch(Exception e){
				e.printStackTrace();
				log.info("修改商铺折扣表异常");
			}*/
			
			//删除所有该sellerid下的审核图片
			Map<Object,Object> delMap = new HashMap<Object,Object>();
			delMap.put("sellerid", sellerid);
			delMap.put("type",1);
			try{
				modifySellerInfoDao.delSellerPicByType(delMap);
			}catch(Exception e){
				e.printStackTrace();
				log.info("删除图片审核表中的环境图异常");
			}
			//插入所有环境图片到图片审核表中
			try{
				modifySellerInfoDao.addSellerPicApply(getPicList(modifySellerInfoRequest, recordMap));
			}catch(Exception e){
				e.printStackTrace();
				log.info("插入图片到图片审核表中异常");
			}
			
			// 更新封面图
			Map<Object, Object> coverMap = new HashMap<Object, Object>();
			coverMap.put("sellerid", sellerid);
			coverMap.put("islogo", 2);	//logo
			// 删除原有的封面图
			try{
				modifySellerInfoDao.deleteSellerPic(coverMap);
			}catch(Exception e){
				e.printStackTrace();
				log.info("删除封面图异常");
			}
			//插入新的封面图
			coverMap.put("url",modifySellerInfoRequest.getCover().replace(fileUrl, ""));
			coverMap.put("sdate", DateUtil.format(DateUtil.now(),"yyyy-MM-dd HH:mm:ss"));
			try{
				modifySellerInfoDao.insertCoverPic(coverMap);	//TODO MongoDB 修改
			}catch(Exception e){
				e.printStackTrace();
				log.info("插入封面图异常");
			}
			//修改商铺logo图
			Map<Object, Object> logoMap = new HashMap<Object, Object>();
			logoMap.put("sellerid", sellerid);
			logoMap.put("islogo", 1);	//logo
			//删除原有的logo图
			try{
				modifySellerInfoDao.deleteSellerPic(logoMap);
			}catch(Exception e){
				e.printStackTrace();
				log.info("删除logo图异常");
			}
			//插入新的logo图
			logoMap.put("url",modifySellerInfoRequest.getLogo().replace(fileUrl, ""));
			logoMap.put("sdate", DateUtil.format(DateUtil.now(),"yyyy-MM-dd HH:mm:ss"));
			try{
				modifySellerInfoDao.insertCoverPic(logoMap);	//TODO MongoDB 修改
			}catch(Exception e){
				e.printStackTrace();
				log.info("插入logo图异常");
			}
			Map<Object, Object> result = new HashMap<>();
			response = new MapResponse(ResponseCode.SUCCESS, "提交申请成功");
			result.put("ispay", 1);	//订单是否支付过  0 未支付    1 已支付
			response.setResponse(result);
			return response;
		} catch (Exception e) {
			e.printStackTrace();
			return new BaseResponse(ResponseCode.FAILURE, "修改店铺资料失败，请联系客服");
		}
	}


	private boolean isUpdateBaseInfo(Map<Object, Object> recordMap,
			Integer valueOf) {
		// TODO Auto-generated method stub
		return false;
	}
}
