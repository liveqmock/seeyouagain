package com.xmniao.xmn.core.vstar.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.xmniao.xmn.core.base.BaseDao;
import com.xmniao.xmn.core.base.BaseService;
import com.xmniao.xmn.core.base.Resultable;
import com.xmniao.xmn.core.live_anchor.constant.LiveConstant;
import com.xmniao.xmn.core.live_anchor.dao.TLiveRecordDao;
import com.xmniao.xmn.core.live_anchor.entity.BLiveAnchorImage;
import com.xmniao.xmn.core.live_anchor.entity.BLiver;
import com.xmniao.xmn.core.live_anchor.service.BLiveAnchorImageService;
import com.xmniao.xmn.core.live_anchor.service.TLiveAnchorService;
import com.xmniao.xmn.core.thrift.client.proxy.ThriftClientProxy;
import com.xmniao.xmn.core.thrift.service.liveService.LiveWalletService;
import com.xmniao.xmn.core.util.DateUtil;
import com.xmniao.xmn.core.util.HttpUtil;
import com.xmniao.xmn.core.util.PageConstant;
import com.xmniao.xmn.core.util.PropertiesUtil;
import com.xmniao.xmn.core.util.handler.AreaHandler;
import com.xmniao.xmn.core.vstar.constant.VstarConstant;
import com.xmniao.xmn.core.vstar.dao.TVstarEnrollDao;
import com.xmniao.xmn.core.vstar.dao.TVstarEnrollImgDao;
import com.xmniao.xmn.core.vstar.dao.TVstarPlayerInfoDao;
import com.xmniao.xmn.core.vstar.dao.TVstarPlayerSettingDao;
import com.xmniao.xmn.core.vstar.entity.TVstarEnroll;
import com.xmniao.xmn.core.vstar.entity.TVstarEnrollImg;
import com.xmniao.xmn.core.vstar.entity.TVstarPlayerInfo;
import com.xmniao.xmn.core.vstar.entity.TVstarPlayerSetting;

/**
 * 
 * 
 * 项目名称：XmnWeb_vstar
 * 
 * 类名称：TVstarEnrollService
 * 
 * 类描述： 新时尚大赛报名Service
 * 
 * 创建人：  huang'tao
 * 
 * 创建时间：2017-6-1 下午1:52:27 
 * 
 * Copyright (c) 广东寻蜜鸟网络技术有限公司
 */
@Service
public class TVstarEnrollService extends BaseService<TVstarEnroll>{
	
	private final String USERID_="USERID_";//APP sessionToken前缀
	
	/**
	 * 注入新时尚大赛报名信息
	 */
	@Autowired
	private TVstarEnrollDao vstarEnrollDao;
	
	/**
	 * 注入新时尚大赛选手信息
	 */
	@Autowired
	private TVstarPlayerInfoDao vstarPlayerInfoDao;
	
	/**
	 * 注入新时尚大赛报名相册信息
	 */
	@Autowired
	private TVstarEnrollImgDao starEnrollImgDao;
	
	
	/**
	 * 注入新时尚大赛报名配置信息DAO
	 */
	@Autowired
	private TVstarPlayerSettingDao playerSettionDao;
	
	
	@Autowired
	private TLiveRecordDao liveRecordDao ;
	
	/**
	 * 注入主播相册服务
	 */
	@Autowired
	private BLiveAnchorImageService anchorImgService;
	
	/**
	 * 注入主播服务
	 */
	@Autowired
	private TLiveAnchorService anchorService;
	
	/**
	 * 注入redisTemplate
	 */
	@Autowired
	private StringRedisTemplate redisTemplate;
	

	@SuppressWarnings("rawtypes")
	@Override
	protected BaseDao getBaseDao() {
		return vstarEnrollDao;
	}
	
	/**
	 * 注入直播钱包服务
	 */
	@Resource(name = "liveWalletServiceServiceClient")
	private ThriftClientProxy liveWalletServiceServiceClient;

	/**
	 * 方法描述：获取报名信息 <br/>
	 * 创建人：  huang'tao <br/>
	 * 创建时间：2017-6-2上午10:51:43 <br/>
	 * @param vstarEnroll
	 * @return
	 */
	public List<TVstarEnroll> getListInfo(TVstarEnroll vstarEnroll) {
		List<TVstarEnroll> list = getList(vstarEnroll);
		convertAreaInfo(list);//转换区域信息
		loadPraiseInfo(list);//加载点赞信息
		loadEnrollImgInfo(list);//加载相册信息
		return list;
	}

	/**
	 * 方法描述：加载报名相册信息(个人风采照) <br/>
	 * 创建人：  huang'tao <br/>
	 * 创建时间：2017-6-3下午4:42:42 <br/>
	 * @param list
	 */
	private void loadEnrollImgInfo(List<TVstarEnroll> list) {
		List<Integer> enrollIds= new ArrayList<Integer>();
		
		for(TVstarEnroll enroll:list){
			Integer id = enroll.getId();
			enrollIds.add(id);
		}
		
		if(enrollIds != null && enrollIds.size()>0){
			TVstarEnrollImg enrollImg = new TVstarEnrollImg();
			enrollImg.setImgType(VstarConstant.ENROLL_IMG_TYPE.IMG_101);
			enrollImg.setEids(enrollIds);
			List<TVstarEnrollImg> imgList = starEnrollImgDao.getList(enrollImg);
			for(TVstarEnroll enroll:list){
				List<TVstarEnrollImg> enrollImgList = new ArrayList<TVstarEnrollImg>();
				for(TVstarEnrollImg imgInfo:imgList){
					Integer enrollId = imgInfo.getPid();
					Integer id = enroll.getId();
					if(id != null && enrollId != null && enrollId.intValue() == id.intValue()){
						enrollImgList.add(imgInfo);
					}
				}
				enroll.setEnrollImgList(enrollImgList);
				
			}
			
		}
	}

	/**
	 * 方法描述：加载点赞信息 <br/>
	 * 创建人：  huang'tao <br/>
	 * 创建时间：2017-6-2下午5:22:12 <br/>
	 * @param list
	 */
	private void loadPraiseInfo(List<TVstarEnroll> list) {
		List<Integer> enrollIds= new ArrayList<Integer>();
		
		for(TVstarEnroll enroll:list){
			Integer id = enroll.getId();
			enrollIds.add(id);
		}
		
		if(enrollIds != null && enrollIds.size()>0){
			TVstarPlayerInfo playerInfo= new TVstarPlayerInfo();
			playerInfo.setEnrollIds(enrollIds);
			playerInfo.setLimit(PageConstant.PAGE_LIMIT_NO);
			List<TVstarPlayerInfo> playerInfoList = vstarPlayerInfoDao.getList(playerInfo);
			for(TVstarEnroll enroll:list){
				for(TVstarPlayerInfo player:playerInfoList){
					Integer id = enroll.getId();
					Integer enrollId = player.getEnrollId();
					if(id!=null && enrollId!=null && id.intValue()==enrollId.intValue()){
						enroll.setLikeCount(player.getLikeCount());
						break;
					}
				}
			}
		}
		
	}

	/**
	 * 方法描述：将区域信息id转化为name <br/>
	 * 创建人：  huang'tao <br/>
	 * 创建时间：2017-6-2上午11:25:31 <br/>
	 * @param list
	 */
	private void convertAreaInfo(List<TVstarEnroll> list) {
		String province=null;
		String city=null;
		String area=null;
		for(TVstarEnroll enroll:list){
			StringBuffer areaText=new StringBuffer();
			Integer provinceId = enroll.getProvinceId();
			Integer cityId = enroll.getCityId();
			Integer areaId = enroll.getAreaId();
			if(provinceId!=null){
				province = AreaHandler.getAreaHandler().getAreaIdByTitle(provinceId);
			}
			if(cityId!=null){
				city = AreaHandler.getAreaHandler().getAreaIdByTitle(cityId);
			}
			if(areaId != null){
				area = AreaHandler.getAreaHandler().getAreaIdByTitle(areaId);
			}
			if(StringUtils.isNotBlank(province)){
				areaText.append(province);
			}
			
			if(StringUtils.isNotBlank(city)){
				areaText.append("-"+city);
			}
			
			if(StringUtils.isNotBlank(area)){
				areaText.append("-"+area);
			}
			enroll.setAreaText(areaText.toString());
		}
		
	}

	/**
	 * 方法描述：更新报名审核状态 <br/>
	 * 创建人：  huang'tao <br/>
	 * 创建时间：2017-6-6下午5:48:09 <br/>
	 * @param vstarEnroll
	 * @return
	 */
	public Integer updateInfo(TVstarEnroll vstarEnroll) {
		TVstarPlayerSetting playerSetting = playerSettionDao.getFirstObject();
		if(playerSetting!=null){
			Integer autoPassSecond = playerSetting.getAutoPassSecond();//是否自动通过试播审核 0 否 1 是
			if(autoPassSecond!=null && autoPassSecond.intValue()==1){
				Integer status = vstarEnroll.getStatus();
				if(status!=null && status.intValue()==VstarConstant.ENROLL_STATUS.STATUS_5){
//					vstarEnroll.setStatus(VstarConstant.ENROLL_STATUS.STATUS_7);
					vstarEnroll.setPlayStatus(VstarConstant.VSTAR_PLAY_STATUS.PLAY_STATUS_1);
					
				}
			}
		}
		
		
		Integer update = update(vstarEnroll);//更新报名状态信息
		
		/**
		 * 开通主播权限及初始化选手统计信息
		 */
//		Integer status = vstarEnroll.getStatus();
		Integer playStatus = vstarEnroll.getPlayStatus();
		if(playStatus!=null && playStatus.intValue()== VstarConstant.VSTAR_PLAY_STATUS.PLAY_STATUS_1){
			Integer id = vstarEnroll.getId();
			if(id!=null){
				autoGrantPermission(id.toString());//开通主播权限
				
				countPlayerInfo(id.toString());//初始化主播鸟蛋，直播时长等信息
				
				syncAnchorImgInfo(id.toString());//同步主播相册信息
			}
		}
		return update;
	}

	/**
	 * 方法描述：获取实名审核信息 <br/>
	 * 创建人：  huang'tao <br/>
	 * 创建时间：2017-6-8上午10:04:51 <br/>
	 * @param vstarEnroll
	 * @return
	 */
	public List<TVstarEnroll> getListRealInfo(TVstarEnroll vstarEnroll) {
		List<TVstarEnroll> list = getList(vstarEnroll);
		loadEnrollImgIdentityInfo(list,VstarConstant.ENROLL_IMG_TYPE.IMG_201);//加载身份证正面信息
		loadEnrollImgIdentityInfo(list,VstarConstant.ENROLL_IMG_TYPE.IMG_202);;//加载身份证反面信息
		loadEnrollImgIdentityInfo(list,VstarConstant.ENROLL_IMG_TYPE.IMG_203);;//加载手持身份证照片信息
		loadEnrollImgEnrollInfo(list,VstarConstant.ENROLL_IMG_TYPE.IMG_101);;//加载第一张报名照片信息
		return list;
	}

	/**
	 * 方法描述：加载第一张报名照片信息<br/>
	 * 创建人：  huang'tao <br/>
	 * 创建时间：2017-6-15下午3:44:41 <br/>
	 * @param list
	 * @param imgType
	 */
	private void loadEnrollImgEnrollInfo(List<TVstarEnroll> list, String imgType) {
		List<Integer> enrollIds= new ArrayList<Integer>();
		
		for(TVstarEnroll enroll:list){
			Integer id = enroll.getId();
			enrollIds.add(id);
		}
		
		if(enrollIds != null && enrollIds.size()>0 &&StringUtils.isNotBlank(imgType)){
			TVstarEnrollImg enrollImg = new TVstarEnrollImg();
			enrollImg.setImgType(imgType);
			enrollImg.setEids(enrollIds);
			List<TVstarEnrollImg> imgList = starEnrollImgDao.getList(enrollImg);
			for(TVstarEnroll enroll:list){
				for(TVstarEnrollImg imgInfo:imgList){
					Integer enrollId = imgInfo.getPid();
					Integer id = enroll.getId();
					if(id != null && enrollId != null && enrollId.intValue() == id.intValue()){
						if(VstarConstant.ENROLL_IMG_TYPE.IMG_101.equals(imgType)){
							enroll.setImgUrl101(imgInfo.getImgUrl());
						}
						break;
					}
				}
			}
			
		}
	}

	/**
	 * 方法描述：加载报名相册信息(身份证照片)<br/>
	 * 创建人：  huang'tao <br/>
	 * 创建时间：2017-6-8上午10:14:56 <br/>
	 * @param list
	 * @param imgType
	 */
	private void loadEnrollImgIdentityInfo(List<TVstarEnroll> list,
			String imgType) {

		List<Integer> enrollIds= new ArrayList<Integer>();
		
		for(TVstarEnroll enroll:list){
			Integer id = enroll.getId();
			enrollIds.add(id);
		}
		
		if(enrollIds != null && enrollIds.size()>0 &&StringUtils.isNotBlank(imgType)){
			TVstarEnrollImg enrollImg = new TVstarEnrollImg();
			enrollImg.setImgType(imgType);
			enrollImg.setEids(enrollIds);
			List<TVstarEnrollImg> imgList = starEnrollImgDao.getList(enrollImg);
			for(TVstarEnroll enroll:list){
				for(TVstarEnrollImg imgInfo:imgList){
					Integer enrollId = imgInfo.getPid();
					Integer id = enroll.getId();
					if(id != null && enrollId != null && enrollId.intValue() == id.intValue()){
						if(VstarConstant.ENROLL_IMG_TYPE.IMG_201.equals(imgType)){
							enroll.setImgUrl201(imgInfo.getImgUrl());
						}else if(VstarConstant.ENROLL_IMG_TYPE.IMG_202.equals(imgType)){
							enroll.setImgUrl202(imgInfo.getImgUrl());
						}else if(VstarConstant.ENROLL_IMG_TYPE.IMG_203.equals(imgType)){
							enroll.setImgUrl203(imgInfo.getImgUrl());
						}
						
						break;
					}
				}
			}
			
		}
	
		
	}

	/**
	 * 方法描述：批量更新实名审核状态 <br/>
	 * 创建人：  huang'tao <br/>
	 * 创建时间：2017-6-8上午11:58:06 <br/>
	 * @param ids
	 * @param status
	 * @return
	 */
	public Resultable updateRealNameBatch(String ids, String status) {
		Resultable result = new Resultable();
		String playStatus=null;
		Map<String,Object> map= new HashMap<String,Object>();
		try {
			if(StringUtils.isNotBlank(ids)){
				TVstarPlayerSetting playerSetting = playerSettionDao.getFirstObject();
				if(playerSetting!=null){
					Integer autoPassSecond = playerSetting.getAutoPassSecond();//是否自动通过试播审核 0 否 1 是
					if(autoPassSecond!=null && autoPassSecond.intValue()==1){
						if(status!=null && new Integer(status)==VstarConstant.ENROLL_STATUS.STATUS_5){
//							status=VstarConstant.ENROLL_STATUS.STATUS_7+"";
							playStatus=VstarConstant.VSTAR_PLAY_STATUS.PLAY_STATUS_1+"";
							
							autoGrantPermission(ids);//自动开通主播权限
							
							countPlayerInfo(ids);//初始化主播鸟蛋，直播时长等信息
							
							syncAnchorImgInfo(ids);//同步主播相册信息
						}
					}
				}
				
				map.put("ids", ids.split(","));
				map.put("status", status);
				map.put("playStatus", playStatus);
				vstarEnrollDao.updateBatch(map);
				
				result.setSuccess(true);
				result.setMsg("操作成功!");
			}
		} catch (Exception e) {
			result.setSuccess(false);
			result.setMsg("操作失败!");
			this.log.error("批量更新实名审核状态失败:ids="+ids, e);
			e.printStackTrace();
		}
		
		
		return result;
	}

	/**
	 * 方法描述：同步主播相册信息 <br/>
	 * 创建人：  huang'tao <br/>
	 * 创建时间：2017-6-20上午10:21:50 <br/>
	 * @param ids 报名表(t_vstar_enroll) id
	 */
	public void syncAnchorImgInfo(String ids) {
		if(StringUtils.isNotBlank(ids)){
			Map<String,Object> param=new HashMap<String,Object>();
			param.put("imgType", VstarConstant.ENROLL_IMG_TYPE.IMG_101);
			param.put("enrollIdList", ids.split(","));
			List<Map<String,Object>> imgList = starEnrollImgDao.getImgList(param);
			List<BLiveAnchorImage> imageList = new ArrayList<BLiveAnchorImage>();
			for(Map<String,Object> map:imgList){
				BLiveAnchorImage anchorImge= new BLiveAnchorImage();
				if(map.get("liverId") != null && map.get("imgUrl") != null){
					anchorImge.setAnchorId(new Integer(map.get("liverId").toString()));
					anchorImge.setAnchorImage(map.get("imgUrl").toString());
					anchorImge.setImageType(LiveConstant.IMAGETYPE_ANCHOR);
					anchorImge.setCreateTime(new Date());
					anchorImge.setStatus(1);
					imageList.add(anchorImge);
				}
			}
			
			if(imageList!=null && imageList.size()>0){
				anchorImgService.addBatch(imageList);
			}
		}
	}

	/**
	 * 方法描述：初始化主播鸟蛋，直播时长等信息 <br/>
	 * 创建人：  huang'tao <br/>
	 * 创建时间：2017-6-15下午3:02:42 <br/>
	 * @param ids 选手报名表id(t_vstar_enroll)
	 */
	private void countPlayerInfo(String ids) {
		if(StringUtils.isNotBlank(ids)){
			List<Integer> enrollIds=new ArrayList<Integer>();
			String[] idArray = ids.split(",");
			for(String id:idArray){
				enrollIds.add(new Integer(id));
			}
			TVstarPlayerInfo vstarPlayerInfo = new TVstarPlayerInfo();
			vstarPlayerInfo.setEnrollIds(enrollIds);
			List<TVstarPlayerInfo> list = vstarPlayerInfoDao.getList(vstarPlayerInfo);
			
			countLiveRecordInfo(list);//统计直播场次，时长信息
			
			countVstarEggCountInfo(list);//统计主播鸟蛋信息
			
			doUpdatePlayerInfo(list);//真正更新选手统计信息
			
		}
	}
	
	/**
	 * 方法描述：真正更新选手统计信息 <br/>
	 * 创建人：  huang'tao <br/>
	 * 创建时间：2017-6-10上午11:51:54 <br/>
	 * @param list
	 */
	private void doUpdatePlayerInfo(List<TVstarPlayerInfo> list) {
		try {
			for(TVstarPlayerInfo playerInfo:list){
				vstarPlayerInfoDao.update(playerInfo);
			}
		} catch (Exception e) {
			this.log.error("真正更新选手统计信息异常："+e);
			e.printStackTrace();
		}
	}
	
		 /**
		 * 方法描述：统计(预设值)选手鸟蛋信息 <br/>
		 * 创建人：  huang'tao <br/>
		 * 创建时间：2017-6-10下午5:00:23 <br/>
		 * @param list
		 */
		private void countVstarEggCountInfo(List<TVstarPlayerInfo> list) {
			List<String> uidList=new ArrayList<String>();
			try {
				for(TVstarPlayerInfo playerInfo:list){
					Integer uid = playerInfo.getUid();
					if(null != uid){
						uidList.add(uid.toString());
					}
				}
				
				if(uidList!=null && uidList.size()>0){
					LiveWalletService.Client client = (LiveWalletService.Client)liveWalletServiceServiceClient.getClient();
		            
		            Map<String, Map<String, String>> liveWalletMsg = client.getLiveWalletMsg(uidList);
		            
		            for(TVstarPlayerInfo playerInfo:list){
		            	Integer uid = playerInfo.getUid();
		            	if(uid!=null){
		            		Map<String, String> walletMsg = liveWalletMsg.get(uid.toString());//直播用户直播钱包信息
			            	if(walletMsg !=null && !walletMsg.isEmpty()){
			            		String balance = walletMsg.get("balance");//鸟蛋余额
			            		String balanceCoin = walletMsg.get("balanceCoin");//转出鸟蛋
			            		BigDecimal balanceEggs=new BigDecimal(balance);
			            		BigDecimal turnOutEggs=new BigDecimal(balanceCoin);
			            		BigDecimal eggCount = balanceEggs.add(turnOutEggs).setScale(0, BigDecimal.ROUND_UP);//打赏鸟蛋
			            		playerInfo.setEggCountInit(eggCount.intValue());
			            		playerInfo.setEggCount(eggCount.intValue());
			            	}
		            	}
		            }
		            
				}
			} catch (Exception e) {
				this.log.error("统计(预设值)选手鸟蛋信息异常："+e);
				e.printStackTrace();
			}finally{
				//释放连接
				liveWalletServiceServiceClient.returnCon();
			}
		}
		
	
	/**
	 * 方法描述：统计(预设值)直播场次，时长等信息 <br/>
	 * 创建人：  huang'tao <br/>
	 * 创建时间：2017-6-10上午11:31:38 <br/>
	 * @param list
	 */
	private void countLiveRecordInfo(List<TVstarPlayerInfo> list) {
		List<Integer> liverIdList=new ArrayList<Integer>();
		try {
			for(TVstarPlayerInfo playerInfo:list){
				Integer liverId = playerInfo.getLiverId();
				if(null != liverId){
					liverIdList.add(liverId);
				}
			}
			
			if(liverIdList!=null && liverIdList.size()>0){
				Map<String,Object> paraMap=new HashMap<String,Object>();
				paraMap.put("liverIds", liverIdList);
				List<Map<String,Object>> liveInfoList = liveRecordDao.getLiveInfoList(paraMap);
				for(TVstarPlayerInfo playerInfo:list){
					for(Map<String,Object> liveRecordMap:liveInfoList){
						Integer liverId = (Integer)liveRecordMap.get("liverId");//直播信息主播ID
						Integer liverId2 = playerInfo.getLiverId();//选手信息直播用户ID
						boolean isSame=liverId!=null && liverId2!=null && liverId.intValue()==liverId2.intValue();
						if(isSame){
							Integer liveCount =  new Integer(liveRecordMap.get("liveCount")==null?"0":liveRecordMap.get("liveCount").toString());//直播场次
							BigDecimal liveTimeCountDb =liveRecordMap.get("liveTimeCount")==null?new BigDecimal(0):(BigDecimal)liveRecordMap.get("liveTimeCount");
							Long liveTimeCount = liveTimeCountDb.longValue();//直播时长(单位分钟)
							playerInfo.setLiveCount(liveCount);
							playerInfo.setLiveTimeCount(liveTimeCount);
							playerInfo.setLiveCountInit(liveCount);
							playerInfo.setLiveTimeCountInit(liveTimeCount);
							break;
						}
					}
					
				}
			}
		} catch (Exception e) {
			this.log.error("统计(预设值)直播场次，时长等信息 异常："+e);
			e.printStackTrace();
		}
		
		
	}

	/**
	 * 方法描述：自动开通试播权限 <br/>
	 * 创建人：  huang'tao <br/>
	 * 创建时间：2017-6-8下午4:28:44 <br/>
	 * @param ids
	 */
	private void autoGrantPermission(String ids) {
		if(StringUtils.isNotBlank(ids)){
			TVstarEnroll vstarEnroll= new TVstarEnroll();
			List<String> idList=new ArrayList<String>();
			String[] idArray = ids.split(",");
			for(String id:idArray){
				idList.add(id);
			}
			vstarEnroll.setIdList(idList);
			vstarEnroll.setLimit(PageConstant.PAGE_LIMIT_NO);
			List<TVstarEnroll> enrollList = vstarEnrollDao.getList(vstarEnroll);
			
			List<Integer> uids= new ArrayList<Integer>();
			List<String> phones= new ArrayList<String>();
			if(enrollList!=null && enrollList.size()>0){
				for(TVstarEnroll enroll:enrollList){
					Integer uid = enroll.getUid();
					uids.add(uid);
					
					String phone = enroll.getPhone();
					if(StringUtils.isNotBlank(phone)){
						phones.add(phone);
					}
					
					//开通主播权限，删除用户sessionToken
//					redisTemplate.delete(USERID_+uid);
					
				}
				
				//TODO 校验主播房间号(分账比例,签约类型)为空的数据，并赋值
				validateAnchorRoomNum(uids);
				
				Map<String, Object> paramMap= new HashMap<String,Object>();
				paramMap.put("utype", "1");//用户类型
				paramMap.put("vstarStage", "1");//星食尚大赛入围阶段 0海选 1复赛 2决赛
				paramMap.put("uids", uids);//用户UID集合
				vstarEnrollDao.updateLiverVstarInfo(paramMap);//更新新时尚大赛报名选手直播用户信息
				
				sendMsg(phones);//发送短信提醒
			}
			
		}
	}

	/**
	 * 方法描述：校验房间号(分账比例，签约类型)为空的数据，并赋值 <br/>
	 * 创建人：  huang'tao <br/>
	 * 创建时间：2017-7-4下午3:14:45 <br/>
	 * @param uids
	 */
	private void validateAnchorRoomNum(List<Integer> uids) {
		// TODO Auto-generated method stub
		if(uids!=null && uids.size()>0){
			List<BLiver> anchorList = anchorService.selectLiversByUids(uids.toArray());
			boolean toUpdate=false;
			for(BLiver anchor:anchorList){
				String anchorRoomNo = anchor.getAnchorRoomNo();
				if(StringUtils.isBlank(anchorRoomNo)){
					String roomNo = anchorService.getAnchorRoomNo();//生成的房间号
					anchor.setAnchorRoomNo(roomNo);
					toUpdate=true;
				}
				
				BigDecimal ledgerRatio = anchor.getLedgerRatio();
				if(ledgerRatio==null){
					anchor.setLedgerRatio(new BigDecimal(LiveConstant.LEDGER_RATIO_DEFAULT));
					toUpdate=true;
				}
				
				Integer signType = anchor.getSignType();
				if(signType==null){
					anchor.setSignType(LiveConstant.SIGN_TYPE.SIGN_TYPE_4);
					anchor.setRootRole(0);//来源身份  1:普通兼职  2:工会合作 3:活动合作 4:其他（0：对应sign_type为签约或测试）
					toUpdate=true;
				}
				
				if(toUpdate){
					anchorService.updateByPrimaryKeySelective(anchor);
				}
			}
		}
	}

	/**
	 * 方法描述：发送开通试播权限短信提醒 <br/>
	 * 创建人：  huang'tao <br/>
	 * 创建时间：2017-6-19下午6:06:48 <br/>
	 * @param phones
	 */
	private void sendMsg(List<String> phones) {
		if(phones!=null && phones.size()>0){
			Map<String, Object> param = new HashMap<String, Object>();
			String url="";
			try {
				param.put("appid", "998899");			
				param.put("text", VstarConstant.MSG_CONTENT);
				param.put("phones", phones);
				url = PropertiesUtil.readValue("http.phone.url")+"/smsSend";
				this.log.info("发送开通试播权限短信提醒 ："+url+"，param" + param);
				HttpUtil.getInstance().postForString(url, param, true);
			} catch (Exception e) {
				e.printStackTrace();
				this.log.error("发送开通试播权限短信提醒,失败: "+url+"，param" + param, e);
			}
		}
	}

	/**
	 * 方法描述：获取城市报名人数排名
	 * 创建人： jianming <br/>
	 * 创建时间：2017年6月14日下午4:23:26 <br/>
	 * @return
	 */
	public List<Map<String, Object>> getCityRank() {
		return vstarEnrollDao.getCityRank();
	}

	/**
	 * 方法描述：按时间统计报名人数
	 * 创建人： jianming <br/>
	 * 创建时间：2017年6月14日下午5:20:47 <br/>
	 * @return
	 * @throws Exception 
	 */
	public List<Map<String,Object>> getCityRankByDate() throws Exception {
		List<Map<String,Object>> ranks =  vstarEnrollDao.getCityRankByDate(DateUtil.getSpecifiedDate(DateUtil.LAST_MONTY));
		return  recursionDateCount(ranks,DateUtil.formatStringToDate(DateUtil.getSpecifiedDate(DateUtil.LAST_MONTY), DateUtil.Y_M_D));
	}

	
	private List<Map<String, Object>> recursionDateCount(List<Map<String, Object>> data, Date date) throws Exception {
		List<Map<String, Object>> list = new ArrayList<>();
		int j=0;
		Map<String, Object> mapPojo = data.get(j);
		while(!DateUtil.isSameDate(DateUtil.getNearbyDate(date,-1),new Date())) {
			Map<String, Object> map=new HashMap<>();
			Date date2 = DateUtil.formatStringToDate((String)mapPojo.get("rankDate"),"");
			if(DateUtil.isSameDate(date,date2)){
				list.add(mapPojo);
				if(j<data.size()-1){
					mapPojo = data.get(++j);
				}
			}else{
				map.put("rankDate", DateUtil.formatDate(DateUtil.Y_M_D,date));
				map.put("count",0);
				map.put("defalt", 0);
				map.put("success", 0);
				map.put("activeCount", 0);
				list.add(map);
			}
			date=DateUtil.getNearbyDate(date,1);
		}
		return list;
	}
	
}