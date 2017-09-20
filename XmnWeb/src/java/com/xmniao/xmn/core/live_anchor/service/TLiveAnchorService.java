/**
 * 
 */
package com.xmniao.xmn.core.live_anchor.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;

import org.apache.thrift.TException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.xmniao.xmn.core.base.BaseDao;
import com.xmniao.xmn.core.base.BaseService;
import com.xmniao.xmn.core.base.Pageable;
import com.xmniao.xmn.core.base.ResultFile;
import com.xmniao.xmn.core.base.Resultable;
import com.xmniao.xmn.core.base.Import.PoiImport;
import com.xmniao.xmn.core.businessman.entity.Card;
import com.xmniao.xmn.core.businessman.util.SellerConstants;
import com.xmniao.xmn.core.common.dao.AreaDao;
import com.xmniao.xmn.core.common.entity.TArea;
import com.xmniao.xmn.core.common.entity.TBank;
import com.xmniao.xmn.core.common.service.TBankService;
import com.xmniao.xmn.core.common.service.TSequenceService;
import com.xmniao.xmn.core.exception.ApplicationException;
import com.xmniao.xmn.core.live_anchor.constant.LiveConstant;
import com.xmniao.xmn.core.live_anchor.dao.BLiveAnchorStartImageDao;
import com.xmniao.xmn.core.live_anchor.dao.BLiveFansRankDao;
import com.xmniao.xmn.core.live_anchor.dao.BLiverDao;
import com.xmniao.xmn.core.live_anchor.dao.TLiveRecordDao;
import com.xmniao.xmn.core.live_anchor.entity.AnchorRatioBean;
import com.xmniao.xmn.core.live_anchor.entity.BLiveAnchorImage;
import com.xmniao.xmn.core.live_anchor.entity.BLiveAnchorStartImage;
import com.xmniao.xmn.core.live_anchor.entity.BLiveFansRank;
import com.xmniao.xmn.core.live_anchor.entity.BLiver;
import com.xmniao.xmn.core.live_anchor.entity.FansConfigureRequest;
import com.xmniao.xmn.core.thrift.client.proxy.ThriftClientProxy;
import com.xmniao.xmn.core.thrift.service.liveService.FailureException;
import com.xmniao.xmn.core.thrift.service.liveService.ResponseData;
import com.xmniao.xmn.core.thrift.service.synthesizeService.MentionAccountService;
import com.xmniao.xmn.core.thrift.service.synthesizeService.SynthesizeService;
import com.xmniao.xmn.core.util.FastfdsConstant;
import com.xmniao.xmn.core.util.NMD5;
import com.xmniao.xmn.core.util.StringUtils;
import com.xmniao.xmn.core.xmnburs.dao.BursDao;
import com.xmniao.xmn.core.xmnburs.dao.BursInfoDao;
import com.xmniao.xmn.core.xmnburs.entity.Burs;
import com.xmniao.xmn.core.xmnburs.entity.BursInfo;
import com.xmniao.xmn.core.xmnburs.service.BursService;
import com.xmniao.xmn.core.xmnpay.entity.LiveWallet;
import com.xmniao.xmn.core.xmnpay.service.LiveWalletService;

/**
 * 项目名称：XmnWeb_LVB
 * 
 * 类名称：TLiveAnchorService
 * 
 * 类描述：主播Service
 * 
 * 创建人： huang'tao
 * 
 * 创建时间：2016-8-6下午3:23:50
 * 
 * Copyright (c) 广东寻蜜鸟网络技术有限公司
 */
@Service
public class TLiveAnchorService extends BaseService<BLiver> {

	private final Integer ANCHOR_NUMID = 100007;
	
	private final String B_URS="b_urs";//表名,用于缓存会员昵称
	
//	private final String B_URS_2="b_urs_2";//表名,用户缓存会员
	
	private final String PREFIX="$";//前缀
	
//	private final String USERID_="USERID_";//APP sessionToken前缀
	
	/**
	 * 注入直播关注Service
	 */
	@Autowired
	private TLiveFocusService liveFocusService;

	/**
	 * 注入主播(用户)服务
	 */
	@Autowired
	private BLiverDao liverDao;

	/**
	 * 注入直播服务
	 */
	@Autowired
	private TLiveRecordDao liveRecordDao;

	/**
	 * 注入寻蜜鸟用户服务
	 */
	@Autowired
	private BursDao bursDao;

	/**
	 * 注入寻蜜鸟用户详细信息服务
	 */
	@Autowired
	private BursInfoDao bursInfoDao;

	/**
	 * 注入序列号服务
	 */
	@Autowired
	private TSequenceService sequenceService;

	/**
	 * 寻蜜鸟用户Service
	 */
	@Autowired
	private BursService bursService;
	
	/**
	 * 注入会员钱包服务
	@Autowired
	private WalletService walletService;*/

	/**
	 * 注入直播钱包服务
	 */
	@Autowired
	private LiveWalletService liveWalletService;

	/**
	 * 注入主播相册服务
	 */
	@Autowired
	private BLiveAnchorImageService anchorImageService;

	/**
	 * 注入開始直播服務
	 */
	@Autowired
	private BLiveAnchorStartImageDao bLiveAnchorStartImageDao;

	
	/**
	 * 注入支付综合服务
	 */
	@Resource(name = "synthesizeServiceClient")
	private ThriftClientProxy synthesizeServiceClient;
	
	/**
	 * 注入直播钱包服务
	 */
	@Resource(name = "liveWalletServiceServiceClient")
	private ThriftClientProxy liveWalletServiceServiceClient;
	
	/**
	 * 注入粉丝级别服务
	 */
	@Autowired
	private BLiveFansRankDao fansRankDao;
	
	/**
	 * 注入直播会员服务
	 */
	@Autowired
	private BLiveMemberService liveMemberService;
	
	/**
	 * 注入银行卡服务
	 */
	@Resource(name = "mentionAccountServiceClient")
	private ThriftClientProxy mentionAccountServiceClient;
	
	@Autowired
	private TBankService tBankService;// 银行信息获取
	
	@Autowired
	private AreaDao areaDao;
	
	/**
	 * 注入redisTemplate
	 */
	@Autowired
	private StringRedisTemplate redisTemplate;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.xmniao.xmn.core.base.BaseService#getBaseDao()
	 */
	@SuppressWarnings("rawtypes")
	@Override
	protected BaseDao getBaseDao() {
		return liverDao;
	}

	/**
	 * 
	 * 方法描述：删除主播信息 创建人： huang'tao 创建时间：2016-8-6下午3:38:06
	 * 
	 * @param anchorId
	 * @return
	 */
	public int deleteByPrimaryKey(Integer anchorId) {
		return liverDao.deleteByPrimaryKey(anchorId);
	}
	
	
	/**
	 * 
	 * 方法描述：根据主播ID删除关注信息
	 * 创建人：  huang'tao
	 * 创建时间：2016-9-21上午9:51:42
	 * @param anchorId
	 * @return
	 */
	public int deleteFocusByAnchorId(Integer anchorId){
		return liverDao.deleteFocusByAnchorId(anchorId);
	}

	/**
	 * 
	 * 方法描述：添加主播信息 创建人： huang'tao 创建时间：2016-8-6下午3:40:18
	 * 
	 * @param record
	 * @return
	 */
	public int insertSelective(BLiver record) {
		return liverDao.insertSelective(record);
	}

	/**
	 * 
	 * 方法描述：查询主播信息 创建人： huang'tao 创建时间：2016-8-6下午3:43:58
	 * 
	 * @param anchorId
	 * @return
	 */
	public BLiver selectByPrimaryKey(Integer anchorId) {
		return liverDao.selectByPrimaryKey(anchorId);
	}
	
	/**
	 * 
	 * 方法描述：查询主播/观众信息
	 * 创建人：  huang'tao
	 * 创建时间：2016-9-19上午10:03:52
	 * @param liver
	 * @return
	 */
	public BLiver selectBLiver(BLiver liver){
		return liverDao.selectBLiver(liver);
	}

	/**
	 * 
	 * 方法描述：更新主播信息 创建人： huang'tao 创建时间：2016-8-6下午3:44:52
	 * 
	 * @param record
	 * @return
	 */
	public int updateByPrimaryKeySelective(BLiver record) {
		return liverDao.updateByPrimaryKeySelective(record);
	}
	
	/**
	 * 方法描述：修改真实姓名 <br/>
	 * 创建人： Administrator <br/>
	 * 创建时间：2017年8月2日下午4:09:41 <br/>
	 * @param liver
	 * @return
	 */
	public int updateBursInfoByUid(BLiver liver) {
		BursInfo bursInfo = new BursInfo();
		bursInfo.setUid(liver.getUid());
		bursInfo.setName(liver.getName());
		
		return bursInfoDao.updateBursInfo(bursInfo);
	}

	
	/**
	 * 
	 * 方法描述：获取主播用户基础信息列表 <br/>
	 * 创建人：  huang'tao <br/>
	 * 创建时间：2016-12-26上午11:18:10 <br/>
	 * @param liveAnchor
	 * @return
	 */
	public List<BLiver> getBaseInfoList(BLiver liveAnchor){
		return liverDao.getList(liveAnchor);
	}

	/**
	 * 
	 * 方法描述：获取主播列表 
	 * 创建人： huang'tao 
	 * 创建时间：2016-8-6下午3:44:52
	 * 
	 * @param record
	 * @return
	 */
	public List<BLiver> getList(BLiver liveAnchor) {
		List<BLiver> liveAnchorList = new ArrayList<BLiver>();
		
		try {
			List<BLiver> anchorList = liverDao.getList(liveAnchor);
			List<BLiver> liveInfoList = getLiveInfoByAnchor();
			List<BLiver> liveTotalInfoList = getLiveTotalInfoByAnchor();
			Object[] uids = getUidsFromAnchorList(anchorList);
			List<LiveWallet> walletList = liveWalletService.selectLiveWalletByUids(uids);
			
			Map<String,Object> param=new HashMap<String,Object>();
			param.put("uidList", uids);
			List<BLiver> realFansList = liverDao.getRealFansList(param);//主播真实粉丝数列表
			
			List<BLiver> robotFansList = liverDao.getRobotFansList(param);//主播机器人粉丝数列表

			if (anchorList != null && anchorList.size() > 0) {
				BLiveAnchorImage anchorImageParam = new BLiveAnchorImage();

				for (BLiver anchor : anchorList) {

					// 设置主播直播时长,回放数等信息
					if (liveInfoList != null && liveInfoList.size() > 0) {
						for (BLiver liveInfo : liveInfoList) {
							if (anchor.getId().compareTo(liveInfo.getId()) == 0) {
								anchor.setLiveMinute(liveInfo.getLiveMinute());// 当月直播时长
//								anchor.setPlaybackNum(liveInfo.getPlaybackNum());// 回放数
								anchor.setRecordNum(liveInfo.getRecordNum());//当月直播场次
								break;
							}
						}
					}
					
					// 设置主播直播总时长,回放数等信息
					if (liveTotalInfoList != null && liveTotalInfoList.size() > 0) {
						for (BLiver liveTotalInfo : liveTotalInfoList) {
							if (anchor.getId().compareTo(liveTotalInfo.getId()) == 0) {
								anchor.setTotalLiveMinute(liveTotalInfo.getLiveMinute());// 总直播时长
								anchor.setTotalRecordNum(liveTotalInfo.getRecordNum());//总直播场次
								break;
							}
						}
					}

					// 设置主播相册照片数量
					anchorImageParam.setAnchorId(anchor.getId());
					Long imageCount = anchorImageService.count(anchorImageParam);
					anchor.setImageCount(imageCount);

					// 设置主播鸟蛋余额
					if (walletList != null && walletList.size() > 0) {
						for (LiveWallet liveWallet : walletList) {
							if (anchor.getUid().compareTo(liveWallet.getUid()) == 0) {
								anchor.setBalance(liveWallet.getBalance());
								anchor.setBirdEggTotal(liveWallet.getEggsTotal());
							}
						}
					}
					
					// 设置主播真实粉丝数
					if (realFansList != null && realFansList.size() > 0) {
						for (BLiver realFansInfo : realFansList) {
							if (anchor.getUid().compareTo(realFansInfo.getUid()) == 0) {
								anchor.setRealFansNum(realFansInfo.getRealFansNum());
								break;
							}
						}
					}
					
					// 设置主播机器人粉丝数
					if (robotFansList != null && robotFansList.size() > 0) {
						for (BLiver robotFansInfo : robotFansList) {
							if (anchor.getUid().compareTo(robotFansInfo.getUid()) == 0) {
								anchor.setRobotFansNum(robotFansInfo.getRobotFansNum());
								break;
							}
						}
					}
					
					

					liveAnchorList.add(anchor);

				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			this.log.error("加载主播列表异常，异常信息为："+e.getMessage(), e);
		}

		return liveAnchorList;
	}

	/**
	 * 方法描述：获取主播用户id数组 
	 * 创建人： huang'tao 
	 * 创建时间：2016-8-22上午10:04:43
	 * 
	 * @param anchorList
	 * @return
	 */
	private Object[] getUidsFromAnchorList(List<BLiver> anchorList) {
		Object[] uids = null;
		StringBuilder sb = new StringBuilder();
		if (anchorList != null && anchorList.size() > 0) {
			for (BLiver anchor : anchorList) {
				sb.append(anchor.getUid()).append(",");
			}
		}
		String uidsStr = sb.toString();
		if (uidsStr.contains(",")) {
			uidsStr = uidsStr.substring(0, uidsStr.lastIndexOf(","));
		}
		uids = uidsStr.split(",");
		return uids;
	}

	/**
	 * 
	 * 方法描述：获取主播信息表记录数 
	 * 创建人： huang'tao 
	 * 创建时间：2016-8-6下午3:44:52
	 * 
	 * @param record
	 * @return
	 */
	public Long count(BLiver liveAnchor) {
		return liverDao.count(liveAnchor);
	}

	/**
	 * 
	 * 方法描述：获取分页列表 
	 * 创建人： huang'tao 
	 * 创建时间：2016-8-6下午5:02:22
	 * 
	 * @param liveAnchor
	 * @param pageable
	 */
	public void getListPage(BLiver liveAnchor, Pageable<BLiver> pageable) {
		pageable.setContent(this.getList(liveAnchor));
		pageable.setTotal(this.count(liveAnchor));
	}

	/**
	 * 
	 * 方法描述：根据主播分组统计直播时长等信息
	 *  创建人： huang'tao 
	 *  创建时间：2016-8-12下午4:19:38
	 * 
	 * @return
	 */
	List<BLiver> getLiveInfoByAnchor() {
		return liveRecordDao.getLiveInfoByAnchor();
	}
	
	/**
	 * 
	 * 方法描述：根据主播分组统计直播总时长等信息
	 *  创建人： huang'tao 
	 *  创建时间：2016-8-12下午4:19:38
	 * 
	 * @return
	 */
	List<BLiver> getLiveTotalInfoByAnchor() {
		return liveRecordDao.getLiveTotalInfoByAnchor();
	}

	/**
	 * 
	 * 方法描述：获取主播注册手机号关联的寻蜜鸟用户详情 创建人： huang'tao 创建时间：2016-8-13下午2:58:04
	 * 
	 * @param liver
	 * @return
	 */
	public BLiver getUrsDetailInfo(BLiver liver) {
		return bursDao.getUrsDetailInfo(liver);
	}

	/**
	 * 方法描述：在此处添加方法描述 创建人： huang'tao 创建时间：2016-8-15上午11:01:45
	 * 
	 * @param liveAnchor
	 */
	public int updateAnchorInfo(BLiver liveAnchor) {
		Integer province = liveAnchor.getProvince();
		Integer city = liveAnchor.getCity();
		if(province==null){
			liveAnchor.setProvince(-1);
		}
		if(city==null){
			liveAnchor.setCity(-1);
		}
		
		//将普通用户设为主播
		if("Y".equals(liveAnchor.getChangeToAnchor())){
			liveAnchor.setAnchorRoomNo(getAnchorRoomNo());
//			redisTemplate.delete(USERID_+liveAnchor.getUid());
		}
		int count = liverDao.updateByPrimaryKeySelective(liveAnchor);
		Burs burs = new Burs();
		BursInfo bursInfo = new BursInfo();

		burs.setUid(liveAnchor.getUid());
		burs.setNname(liveAnchor.getNickname());
		bursDao.updateUrs(burs);

		bursInfo.setUid(liveAnchor.getUid());
		bursInfo.setName(liveAnchor.getName());
		bursInfo.setAvatar(liveAnchor.getAvatar());
		bursInfo.setSex(liveAnchor.getSex());
		bursInfo.setBirthday(liveAnchor.getBirthday());
		bursInfo.setIdcard(liveAnchor.getIdcard());
		bursInfoDao.updateBursInfo(bursInfo);
		
		syncAnchorImage(liveAnchor);
		
		redisTemplate.opsForValue().set(B_URS+PREFIX+"changed", "true");
		
		return count;
	}

	/**
	 * 方法描述：同步主播相册信息 <br/>
	 * 创建人：  huang'tao <br/>
	 * 创建时间：2017-4-12下午4:32:41 <br/>
	 * @param liveAnchor
	 */
	private void syncAnchorImage(BLiver liveAnchor) {
		Integer anchorId = liveAnchor.getId();
		if(anchorId!=null){
			BLiveAnchorImage record=new BLiveAnchorImage();
			record.setAnchorId(anchorId);
			anchorImageService.deleteImagesByBean(record);
		}
		
		String picUrls = liveAnchor.getPicUrls();
		if(org.apache.commons.lang.StringUtils.isNotBlank(picUrls)){
			anchorImageService.addImageBatch(picUrls, LiveConstant.IMAGETYPE_ANCHOR,anchorId);
		}
	}

	/**
	 * 方法描述：添加主播信息,并同步用户信息 创建人： huang'tao 创建时间：2016-8-15下午1:57:16
	 * 
	 * @param liveAnchor
	 */
	public int saveLiverInfo(BLiver liveAnchor) {
		int count = 0;
		String password = "";
		Integer uid = -1;// 会员ID
		String isBinding = liveAnchor.getIsBinding();
		if ("N".equals(isBinding)) {
			Burs burs = new Burs();
			burs.setUname(liveAnchor.getPhone());// 会员登录账号
			burs.setNname(liveAnchor.getNickname());// 会员昵称
			//注册直播普通用户，默认密码为手机号后六位
			Integer operationType = liveAnchor.getOperationType();
			if(operationType!=null && LiveConstant.UTYPE_COMMON.compareTo(liveAnchor.getOperationType())==0){
				String phone = liveAnchor.getPhone();
				password=phone.substring(5);
			}else{
				password = liveAnchor.getPassword();
			}
//			burs.setPassword(NMD5.Encode(password));
			// md5加密并且前六位和后六位互换
			String md = NMD5.Encode(password);
			String md5BeforeSix = md.substring(0, 6);
			String md5InSix = md.substring(6, 26);
			String md5AfterSix = md.substring(26, 32);
			String md5Nmber = md5AfterSix + md5InSix + md5BeforeSix;
			burs.setPassword(md5Nmber);
			burs.setPhone(liveAnchor.getPhone());
			burs.setRegtime(new Date());

			Burs urs = bursService.addUrs(burs);
			BursInfo ursInfo = new BursInfo();
			uid = urs.getUid();
			ursInfo.setUid(uid);
			ursInfo.setPhone(liveAnchor.getPhone());
			ursInfo.setUname(liveAnchor.getPhone());
			ursInfo.setName(liveAnchor.getName());
			ursInfo.setAvatar(liveAnchor.getAvatar());
			ursInfo.setSex(liveAnchor.getSex());
			ursInfo.setBirthday(liveAnchor.getBirthday());
			ursInfo.setIdcard(liveAnchor.getIdcard());
			bursService.insertBursInfo(ursInfo);

			// 添加会员钱包
			/*Bwallet wallet = new Bwallet();
			wallet.setUid(uid);
			walletService.addWallet(wallet);*/
			
			SynthesizeService.Client client = (SynthesizeService.Client)(synthesizeServiceClient.getClient());
			Map<String,String> paramMap=new HashMap<String,String>();
	 		try {
	 			
	 			paramMap.put("uId", String.valueOf(urs.getUid()));
	 			paramMap.put("userType", "1");//用户类型 1用户 	2商家	3合作商
	 			
				log.info("添加寻蜜鸟用户钱包开始，uid：" + String.valueOf(urs.getUid()) + ",userType:1,password:" + paramMap.get("password") + ",name:" + urs.getUname());
				Map<String,String> resultMap=client.addWalletMap(paramMap);
				log.info("添加寻蜜鸟用户钱包结束，返回值：" + resultMap.get("state"));
				fireLoginEvent(new String[]{"寻蜜鸟用户编号",String.valueOf(urs.getUid()),"添加钱包","添加钱包"},1);
			} catch (Exception e) {
				log.error("寻蜜鸟会员失败", e);
				throw new ApplicationException("添加寻蜜鸟会员异常", e, new Object[]{urs});
			} finally{
				synthesizeServiceClient.returnCon();
			}
			
			
		}else{
			Burs bursParam=new Burs();
			bursParam.setPhone(liveAnchor.getPhone());
			Burs ursInfo = bursService.getUrs(bursParam);
			uid = ursInfo.getUid();
			password = liveAnchor.getBursPassword();
			
			syncBursInfo(liveAnchor);//同步会员信息
		}
		liveAnchor.setUid(uid);
		//注册普通会员
		Integer operationType = liveAnchor.getOperationType();
		if(operationType!=null && LiveConstant.UTYPE_COMMON.compareTo(liveAnchor.getOperationType())==0){
			liveAnchor.setUtype(LiveConstant.UTYPE_COMMON);
			syncSuperiorInfo(liveAnchor,true);//同步上级信息
		}else{
			liveAnchor.setUtype(1);// 1 主播 2 普通用户
			liveAnchor.setPassword(NMD5.Encode(password));
			liveAnchor.setAnchorRoomNo(getAnchorRoomNo());
			liveAnchor.setUidRelationChain(StringUtils.generateUidStr(liveAnchor.getUid()));
			liveAnchor.setUidRelationChainNname(liveAnchor.getNickname().replaceAll(",", ""));
			liveAnchor.setUidRelationChainLevel(1);
			//设置粉丝级别，默认最低级
			BLiveFansRank fansRank=new BLiveFansRank();
			List<BLiveFansRank> fansRankList = fansRankDao.getList(fansRank);
			if(fansRankList!=null && fansRankList.size()>0){
				BLiveFansRank fansRankBase = fansRankList.get(0);
				liveAnchor.setFansRankId(fansRankBase.getId().intValue());
				liveAnchor.setFansRankName(fansRankBase.getRankName());
				liveAnchor.setFansRankNo(fansRankBase.getRankNo().longValue());
			}
		}

		liveAnchor.setCreateTime(new Date());
		liveAnchor.setUpdateTime(new Date());
		count = insertSelective(liveAnchor);
		
		redisTemplate.opsForValue().set(B_URS+PREFIX+"changed", "true");
		
		//添加主播相册信息
		Integer anchorId = liveAnchor.getId();
		String picUrls = liveAnchor.getPicUrls();
		anchorImageService.addImageBatch(picUrls, LiveConstant.IMAGETYPE_ANCHOR, anchorId);
		
		//添加直播钱包
		com.xmniao.xmn.core.thrift.service.liveService.LiveWalletService.Client client = (com.xmniao.xmn.core.thrift.service.liveService.LiveWalletService.Client)(liveWalletServiceServiceClient.getClient());
 		try {
 			
			log.info("添加直播钱包开始，uid：" + String.valueOf(uid));
			ResponseData responseData = client.addLiveWallet(String.valueOf(uid));
			log.info("添加直播钱包结束，返回值：" + responseData.getState());
			fireLoginEvent(new String[]{"寻蜜鸟用户编号",String.valueOf(uid),"添加钱包","添加钱包"},1);
		} catch (Exception e) {
			log.error("添加直播钱包失败", e);
			throw new ApplicationException("添加直播钱包异常", e, new Object[]{uid});
		} finally{
			liveWalletServiceServiceClient.returnCon();
		}
		
		return count;
	}

	/**
	 * 方法描述：同步上级上级信息 <br/>
	 * 创建人：  huang'tao <br/>
	 * 创建时间：2017-1-8下午3:36:50 <br/>
	 * @param liveAnchor
	 * @param updateFansRank 
	 */
	private void syncSuperiorInfo(BLiver liveAnchor, boolean updateFansRank) {
		StringBuffer uidRelationChainCurrent=new StringBuffer();
//		StringBuffer uidRelationChainNnameCurrent=new StringBuffer();
		Integer uidRelationChainLevel=0;
		String superiorId = liveAnchor.getSuperiorIdChosen();
		if(org.apache.commons.lang.StringUtils.isNotBlank(superiorId)){
			BLiver liverRequest=new BLiver();
			Integer id=new Integer(superiorId);
			liverRequest.setId(id);
			BLiver superiorInfo = liverDao.selectBLiver(liverRequest);
			String referrerType = superiorInfo.getReferrerType();
			//推荐人类型 ：001 企业推荐人；默认 002 普通推荐人
			if("001".equals(referrerType)){
				liveAnchor.setEnterpriseUid(superiorInfo.getUid());
				liveAnchor.setEnterpriseNname(superiorInfo.getNickname());
			}else{
				/*liveAnchor.setEnterpriseUid(superiorInfo.getEnterpriseUid());
				liveAnchor.setEnterpriseNname(superiorInfo.getEnterpriseNname());*/
				//设置企业推荐人信息，取关系链中最近的企业级推荐人
				setEnterpriseInfo(liveAnchor,superiorInfo);
			}
			
			
			String uidRelationChain = superiorInfo.getUidRelationChain();
			//会员在会员关系链中的级别，顶级为1，下级依次+1
			uidRelationChainLevel = superiorInfo.getUidRelationChainLevel()==null?0:superiorInfo.getUidRelationChainLevel();
//			String uidRelationChainNname = superiorInfo.getUidRelationChainNname();
			if(uidRelationChain!=null){
				uidRelationChainCurrent.append(uidRelationChain).append(",").append(StringUtils.generateUidStr(liveAnchor.getUid()));
			}else{
				uidRelationChainCurrent.append(StringUtils.generateUidStr(liveAnchor.getUid()));
			}
			
			/*if(uidRelationChainNname!=null){
				uidRelationChainNnameCurrent.append(uidRelationChainNname).append(",").append(liveAnchor.getNickname().replaceAll(",", ""));
			}else{
				uidRelationChainNnameCurrent.append(liveAnchor.getNickname().replaceAll(",", ""));
			}*/
		}else{
			uidRelationChainCurrent.append(StringUtils.generateUidStr(liveAnchor.getUid()));
//			uidRelationChainNnameCurrent.append(liveAnchor.getNickname().replaceAll(",", ""));
		}
		
		liveAnchor.setUidRelationChain(uidRelationChainCurrent.toString());
//		liveAnchor.setUidRelationChainNname(uidRelationChainNnameCurrent.toString());
		liveAnchor.setUidRelationChainLevel(uidRelationChainLevel+1);
		
		if(updateFansRank){
			//设置粉丝级别，默认最低级
			BLiveFansRank fansRank=new BLiveFansRank();
			List<BLiveFansRank> fansRankList = fansRankDao.getList(fansRank);
			if(fansRankList!=null && fansRankList.size()>0){
				BLiveFansRank fansRankBase = fansRankList.get(0);
				liveAnchor.setFansRankId(fansRankBase.getId().intValue());
				liveAnchor.setFansRankName(fansRankBase.getRankName());
				liveAnchor.setFansRankNo(fansRankBase.getRankNo().longValue());
			}
		}
		
	}

	/**
	 * 方法描述：设置企业级推荐人相关信息 <br/>
	 * 创建人：  huang'tao <br/>
	 * 创建时间：2017-1-12上午9:25:03 <br/>
	 * @param liveAnchor
	 * @param superiorInfo
	 */
	private void setEnterpriseInfo(BLiver liveAnchor, BLiver superiorInfo) {
		String uidRelationChain = superiorInfo.getUidRelationChain();
		String[] uidRelationChainArray = uidRelationChain.split(",");
		List<String> uids=new ArrayList<String>();
		for(String uidStr:uidRelationChainArray){
			String uid=uidStr.replace("^(0+)", "");
			uids.add(uid);
		}
		
		if(uids!=null&&uids.size()>0){
			BLiver enterpriseInfo = liverDao.selectEnterpriseByUids(uids.toArray());
			if(enterpriseInfo!=null){
				liveAnchor.setEnterpriseUid(enterpriseInfo.getUid());
				liveAnchor.setEnterpriseNname(enterpriseInfo.getNickname());
			}
		}
	}

	/**
	 * 方法描述：同步会员信息
	 * 创建人：  huang'tao
	 * 创建时间：2016-9-23上午10:19:05
	 * @param liveAnchor
	 */
	private void syncBursInfo(BLiver liveAnchor) {
		Burs burs = new Burs();
		burs.setUid(liveAnchor.getUid());//会员ID
		burs.setNname(liveAnchor.getNickname());// 会员昵称
		bursDao.updateUrs(burs);
		
		BursInfo ursInfo = new BursInfo();
		ursInfo.setUid(liveAnchor.getUid());
		ursInfo.setPhone(liveAnchor.getPhone());
		ursInfo.setUname(liveAnchor.getPhone());
		ursInfo.setName(liveAnchor.getName());
		ursInfo.setAvatar(liveAnchor.getAvatar());
		ursInfo.setSex(liveAnchor.getSex());
		ursInfo.setBirthday(liveAnchor.getBirthday());
		ursInfo.setIdcard(liveAnchor.getIdcard());
		bursService.updateBursInfo(ursInfo);
		
	}

	/**
	 * 方法描述：获取自动生成的主播房间号。"遇4进1"，如：1554 ——> 1555
	 * 创建人：  huang'tao
	 * 创建时间：2016-9-18上午10:49:26
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public String getAnchorRoomNo() {
		Long currentSid = sequenceService.getAndUpdateSid(ANCHOR_NUMID);//当前系统序号
		Long nextSid=currentSid+1;
		String nextSidStr = nextSid.toString();
		if(nextSidStr.contains("4")){
			String sid=nextSidStr.replace("4", "5");
			Map<String,Object> param = new HashMap<String,Object>(); 
			param.put("sid", sid);
			param.put("numId", ANCHOR_NUMID);
			sequenceService.updateSpecifiedSid(param);
		}
		return currentSid.toString();
	}

	/**
	 * 
	 * 方法描述：根据用户uid数组获取主播信息 创建人： huang'tao 创建时间：2016-8-19下午3:18:23
	 * 
	 * @param uids
	 * @return
	 */
	public List<BLiver> selectLiversByUids(Object[] uids) {
		return liverDao.selectLiversByUids(uids);
	}

	/**
	 * 方法描述：在此处添加方法描述 创建人： Administrator 创建时间：2016年9月5日下午1:47:37
	 * 
	 * @param bLiveAnchorStartImage
	 * @param pageable
	 */
	public void getStartImageListPage(
			BLiveAnchorStartImage bLiveAnchorStartImage,
			Pageable<BLiveAnchorStartImage> pageable) {
		pageable.setContent(bLiveAnchorStartImageDao
				.getList(bLiveAnchorStartImage));
		pageable.setTotal(bLiveAnchorStartImageDao.count(bLiveAnchorStartImage));
	}

	/**
	 * 方法描述：添加 创建人： Administrator 创建时间：2016年9月5日下午7:24:21
	 * 
	 * @param bLiveAnchorStartImage
	 */
	public boolean add(BLiveAnchorStartImage bLiveAnchorStartImage) {
		if (hasPageImageType(bLiveAnchorStartImage
				.getImageType())) {
			return false;
			
		} else {
			bLiveAnchorStartImage.setCreateTime(new Date());
			bLiveAnchorStartImage.setStatus(1);
			bLiveAnchorStartImageDao.add(bLiveAnchorStartImage);
			return true;
		}
		
	}

	/**
	 * 方法描述：是否含有次图片类型 创建人： jianming 创建时间：2016年9月5日下午8:11:01
	 * 
	 * @param pageImageType
	 * @return
	 */
	public boolean hasPageImageType(Integer pageImageType) {
		return bLiveAnchorStartImageDao.hasPageImageType(pageImageType) > 0;
	}

	/**
	 * 方法描述：根据id查询 创建人： jianming 创建时间：2016年9月5日下午8:31:43
	 * 
	 * @param startImageId
	 * @return
	 */
	public Object selectAnchorStartImage(Integer startImageId) {
		 Object key = bLiveAnchorStartImageDao.selectByPrimaryKey(startImageId);
		 return key;
	}

	/**
	 * 方法描述：修改直播圖
	 * 创建人： jianming
	 * 创建时间：2016年9月5日下午9:17:57
	 * @param bLiveAnchorStartImage
	 */
	public boolean update(BLiveAnchorStartImage bLiveAnchorStartImage) {
		bLiveAnchorStartImage.setUpdateTime(new Date());
		if(hasPageImageType(bLiveAnchorStartImage.getImageType())){
			BLiveAnchorStartImage bLiveAnchorStartImage2=bLiveAnchorStartImageDao.selectByImageType(bLiveAnchorStartImage.getImageType());
			if(bLiveAnchorStartImage2.getId()==bLiveAnchorStartImage.getId()){
				bLiveAnchorStartImageDao.update(bLiveAnchorStartImage);
			}else{
				return false;
			}
		}else{
			bLiveAnchorStartImageDao.update(bLiveAnchorStartImage);
		}
		return true;
	}

	/**
	 * 方法描述：删除直播开始图
	 * 创建人： jianming
	 * 创建时间：2016年9月6日上午10:43:16
	 * @param bLiveAnchorStartImage
	 */
	public void delete(BLiveAnchorStartImage bLiveAnchorStartImage) {
		bLiveAnchorStartImageDao.deleteByPrimaryKey(bLiveAnchorStartImage);
		
	}
	
	/**
	 * 
	 * 方法描述：修改直播图状态
	 * 创建人： jiaming
	 * 创建时间：2016年9月7日上午9:06:46
	 * @param bLiveAnchorStartImage
	 */
	public void updateStartImageStatus(BLiveAnchorStartImage bLiveAnchorStartImage){
		bLiveAnchorStartImage.setUpdateTime(new Date());
		bLiveAnchorStartImage.setStatus(0);
		bLiveAnchorStartImageDao.updateStatus(bLiveAnchorStartImage);
	}

	/**
	 * 方法描述：根据直播会员id查询群组号
	 * 创建人： jianming
	 * 创建时间：2016年9月14日下午2:19:16
	 * @param anchorId
	 * @return
	 */
	public String getGroupId(Integer anchorId) {
		return liverDao.getGroupId(anchorId);
	}

	/**
	 * 方法描述：删除主播，同步更新用户关注主播信息
	 * 创建人：  huang'tao
	 * 创建时间：2016-9-21上午9:32:19
	 * @param id
	 */
	public void syncFocusInfo(Integer anchorId) {
		List<String> startIds = getLiverStartId(anchorId);
		deleteFocusByAnchorId(anchorId);
		if(startIds!=null && startIds.size()>0){
			updateConcernNums(startIds.toArray());
		}
	}
	
	/**
	 * 
	 * 方法描述：获取关注该主播的普通用户ID
	 * 创建人：  huang'tao
	 * 创建时间：2016-9-21上午10:22:56
	 * @param anchorId
	 * @return
	 */
	public  List<String> getLiverStartId(Integer anchorId){
		return liverDao.getLiverStartId(anchorId);
	}
	
	/**
	 * 
	 * 方法描述：更新用户关注主播数
	 * 创建人：  huang'tao
	 * 创建时间：2016-9-21上午10:54:52
	 * @param ids
	 * @return
	 */
	public int updateConcernNums(Object[] ids){
		return liverDao.updateConcernNums(ids);
	}

	/**
	 * 方法描述：同步主播的通告信息
	 * 创建人：  huang'tao
	 * 创建时间：2016-9-23下午2:48:36
	 * @param liveAnchor
	 */
	public void syncRecordInfo(BLiver liveAnchor) {
		liveRecordDao.updateRecordStatus(liveAnchor.getId());
	}
	
	/**
	 * 
	 * 方法描述：获取直播会员列表信息 <br/>
	 * 创建人：  huang'tao <br/>
	 * 创建时间：2017-1-4下午6:23:49 <br/>
	 * @param liveAnchor
	 * @return
	 */
	public List<BLiver> getLiveMemberList(BLiver liveAnchor){
		List<BLiver> liveMemberList = new ArrayList<BLiver>();
		try {
			/*ValueOperations<String, String> opsForValue = redisTemplate.opsForValue();
			String changedFlag = opsForValue.get(B_URS+PREFIX+"changed");
			if(null == changedFlag || "true".equals(changedFlag)){
				List<Burs> bursList = bursDao.getList(null);
				for(Burs bursInfo : bursList){
					String nname = bursInfo.getNname();
					if(nname!=null){
						opsForValue.set(B_URS+PREFIX+bursInfo.getUid(),nname );//缓存会员昵称
					}
					String phone = bursInfo.getPhone();
					if(phone!=null){
						opsForValue.set(B_URS_2+PREFIX+bursInfo.getUid(),phone );//缓存会员昵称
					}
				}
				//加载完会员信息后,将changed标志改为false
				opsForValue.set(B_URS+PREFIX+"changed", "false");
			}*/
			
			liveMemberList=liverDao.getLiveMemberList(liveAnchor);
			List<Integer> uids=new ArrayList<Integer>();
			if(liveMemberList!=null && liveMemberList.size()>0){
				for(BLiver liver:liveMemberList){
					uids.add(liver.getUid());
					/*String performanceUid=liver.getIndirectUid()==null?liver.getSuperiorUid():liver.getIndirectUid().toString();//业绩归属人UID
					if(org.apache.commons.lang.StringUtils.isNotBlank(performanceUid)){
						liver.setIndirectNname(opsForValue.get(B_URS+PREFIX+performanceUid));
					}*/
					
				}
				List<LiveWallet> liveWalletList = liveWalletService.selectLiveWalletByUids(uids.toArray());
				if(liveWalletList!=null && liveWalletList.size()>0){
					for(BLiver liver:liveMemberList){
						for(LiveWallet wallet:liveWalletList){
							
							if(liver.getUid().compareTo(wallet.getUid())==0){
								liver.setRestrictive(wallet.getRestrictive());
								liver.setLimitBalance(wallet.getLimitBalance());
								liver.setCommision(wallet.getCommision());
								liver.setZbalance(wallet.getZbalance());
								break;
							}
						}
					}
				}
				
				//加载会员活动收益等级信息
				loadEarningsRankInfo(liveMemberList);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			this.log.error("获取直播会员列表信息异常:"+e.getMessage());
		}
		return liveMemberList;
	}

	/**
	 * 方法描述：加载会员活动收益等级信息 <br/>
	 * 创建人：  huang'tao <br/>
	 * 创建时间：2017-4-21下午2:38:42 <br/>
	 * @param liveMemberList
	 */
	public void loadEarningsRankInfo(List<BLiver> liveMemberList) {
		if(liveMemberList!=null && liveMemberList.size()>0){
			List<Integer> uids=new ArrayList<Integer>();
			for(BLiver liver:liveMemberList){
				uids.add(liver.getUid());
			}
			List<Map<String, Object>> earningsRankList = bursService.getUrsEarningsRankByUids(uids.toArray());
			if(earningsRankList !=null && earningsRankList.size()>0){
				for(BLiver liver:liveMemberList){
					for(Map<String,Object> map:earningsRankList){
						if(map.get("uid")==null){
							continue;
						}
						Integer uid=new Integer(map.get("uid").toString());
						String rankName=(String) map.get("rank_name");
						String rankSource=(String) map.get("rank_source");
						StringBuffer userDescription=new StringBuffer();
						if(liver.getUid().compareTo(uid)==0){
							String currentDescription = liver.getUserDescription()==null ? "":liver.getUserDescription();
							userDescription.append(currentDescription);
							userDescription.append(rankSource).append("(").append(rankName).append(")").append(",");
							liver.setUserDescription(userDescription.toString());
						}
					}
					String userDescription = liver.getUserDescription();
					if(userDescription!=null && userDescription.contains(",")){
						String substring = userDescription.substring(0, userDescription.lastIndexOf(","));
						liver.setUserDescription(substring);
					}
				}
			}
		}
		
	}

	/**
	 * 方法描述：获取直播普通用户列表(包含分账统计信息) <br/>
	 * 创建人：  huang'tao <br/>
	 * 创建时间：2017-1-4下午6:16:54 <br/>
	 * @param liveAnchor
	 * @param pageable
	 */
	public void getMemberListPage(BLiver liveAnchor, Pageable<BLiver> pageable) {
		String uid = liveAnchor.getUidViewJunior();
		BigDecimal juniorAmount=new BigDecimal(0);
		if(org.apache.commons.lang.StringUtils.isNotBlank(uid)){
			juniorAmount = liveMemberService.getJuniorAmountByUid(uid);
		}
		Map<String, Object> titleInfo=new HashMap<String,Object>();
		titleInfo.put("juniorAmount", juniorAmount);
		pageable.setTitleInfo(titleInfo);
		pageable.setContent(getLiveMemberList(liveAnchor));
		pageable.setTotal(this.count(liveAnchor));
	}

	/**
	 * 方法描述：获取会员列表<br/>
	 * 1排除当前会员的等下级 <br/>
	 * 2要求与上级同会员平台和同会员类型（当前会员无类型，则类型不作要求）
	 * 创建人：  huang'tao <br/>
	 * 创建时间：2017-1-11下午4:41:03 <br/>
	 * @param liveAnchor
	 * @return
	 */
	public List<BLiver> getBaseInfoExcludeList(BLiver liveAnchor) {
		String filterVal = liveAnchor.getFilterVal();
		if(org.apache.commons.lang.StringUtils.isNotBlank(filterVal)){
			liveAnchor.setFilterVal(StringUtils.generateUidStr(new Integer(filterVal)));
			/*BLiver liverParam=new BLiver();
			liverParam.setUid(new Integer(filterVal));
			BLiver liver = liverDao.selectBLiver(liverParam);
			if(liver!=null){
				liveAnchor.setUserPlatform(liver.getUserPlatform());
				liveAnchor.setObjectOriented(liver.getObjectOriented());
			}*/
		}
		List<BLiver> list = liverDao.getList(liveAnchor);
		return list;
	}

	/**
	 * 方法描述：绑定上级信息 <br/>
	 * 0、查询等下级直播会员<br/>
	 * 1、更新关系链<br/>
	 * 2、更新关系链名称<br/>
	 * 3、更新会员在关系链中的等级(遍历0查询的所有等下级用户)<br/>
	 * 4、更新企业级uid(如果为null)<br/>
	 * 5、更新企业级nname<br/>
	 * 创建人：  huang'tao <br/>
	 * 创建时间：2017-1-11下午5:09:25 <br/>
	 * @param liveAnchor
	 */
	@SuppressWarnings("unused")
	public void bindSuperiorInfo(BLiver liveAnchor) {
		Integer uid = liveAnchor.getUid();//当前用户的会员uid
		String superiorIdChosen = liveAnchor.getSuperiorIdChosen();
		if(org.apache.commons.lang.StringUtils.isNotBlank(superiorIdChosen)){
			Integer superiorId=new Integer(superiorIdChosen);
			//待绑定的上级信息
			BLiver superiorInfo = liverDao.selectByPrimaryKey(superiorId);
		}
	}
	
	/**
	 * 
	 * 方法描述：根据uid统计下线人数 <br/>
	 * 创建人：  huang'tao <br/>
	 * 创建时间：2017-1-11下午8:29:40 <br/>
	 * @param uid
	 * @return
	 */
	public long countJuniorsByUid(Integer uid){
		long juniors=0l;
		BLiver liver = liverDao.countJuniorsByUid(uid);
		if(liver!=null){
			juniors = liver.getJuniors();
		}
		return juniors;
	}

	/**
	 * 方法描述：不存在上下级的用户添加上级 <br/>
	 * 创建人：  huang'tao <br/>
	 * 创建时间：2017-1-11下午9:24:10 <br/>
	 * @param liveAnchor
	 */
	public void addSuperiorInfo(BLiver liveAnchor) {
		syncSuperiorInfo(liveAnchor,false);
	}

	/**
	 * 方法描述：批量修改主播分成 <br/>
	 * 创建人：  huang'tao <br/>
	 * 创建时间：2017-1-13下午6:28:01 <br/>
	 * @param anchorRatioBean
	 * @return
	 */
	public Resultable updateBatchRatio(AnchorRatioBean anchorRatioBean) {
		Resultable result=new Resultable();
		String ids = anchorRatioBean.getIds();
		if(org.apache.commons.lang.StringUtils.isBlank(ids)){
			result.setSuccess(false);
			result.setMsg("获取主播信息失败!");
		}else{
			String[] idArray = ids.split(",");
			anchorRatioBean.setIdArray(idArray);
			liverDao.updateRatioBatchByIds(anchorRatioBean);
		}
		
		result.setSuccess(true);
		result.setMsg("操作成功!");
		return result;
	}
	
	/**
	 * 导入主播信息
	 * 创建人： ChenBo <br/>
	 * 创建时间：2017年1月22日上午11:39:49 <br/>
	 * @param multipartFile
	 * @return
	 */
	public ResultFile importAnchor(MultipartFile multipartFile,List<BLiver> faileList) {
		ResultFile rf=null;
		try {
			List<BLiver> productInfo = PoiImport.dataImport(multipartFile, BLiver.class);
			if(productInfo!=null&&productInfo.size()>0){
				if(productInfo.size()>1000){
					rf = new ResultFile(FastfdsConstant.FILE_UPLOAD_FASTDFS_FAILURE, String.format("导入数据不能超过一千条"));
					return rf;
				}
				Resultable resultable =new Resultable();
				bachSavaLiverInfo( productInfo,resultable,faileList);
				rf = new ResultFile(FastfdsConstant.FILE_UPLOAD_FASTDFS_SUCCESS, String.format(resultable.getMsg()));
			}else{
				rf = new ResultFile(FastfdsConstant.FILE_UPLOAD_FASTDFS_FAILURE, String.format("导入失败,模板错误或数据为空!"));
			}
			return rf;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("文件上传失败", e);
			throw new ApplicationException("批量导入信息异常",e);
		}finally {
//			fireLoginEvent( new String[]{"直播频道-主播管理-批量导入","导入操作","直播频道-主播管理-批量导入",""}, rf==null?0:rf.getStatus());
		}
	}
	
	/**
	 * 批量创建主播及相关信息
	 * 创建人： ChenBo <br/>
	 * 创建时间：2017年1月22日上午11:39:49 <br/>
	 * @param productInfo
	 * @param resultable
	 */
	private void bachSavaLiverInfo(List<BLiver> liverInfo, Resultable resultable,List<BLiver> faileLiverList) {
//			List<BLiver> faileLiverList = new ArrayList<BLiver>();
			int result=0;
			for(BLiver liver:liverInfo){
				try{
					result =validateProductInfo(liver);
					if(result!=0){
						int role = getBindingUrs(liver.getPhone());
						if(role==0){
							System.out.println("全新会员");
							liver.setIsBinding("N");
							result = saveLiverInfo(liver);
						}else if(role==1){
							System.out.println("半新会员");
							//已是寻蜜鸟会员
							liver.setIsBinding("Y");
							result = saveLiverInfo(liver);
						}else if(role==2){
							//已是直播会员
							result=0;
							System.out.println("已有会员");
						}
					}
				}catch(Exception e){
					result=0;
					log.error("添加失败直播会员/主播【"+liver.getPhone()+","+liver.getNickname()+"】失败",e);
				}
				if(result==0){
					faileLiverList.add(liver);
				}
			}
			if(faileLiverList.size()>0){	//失败记录
				//记录导入失败数据
				
				resultable.setMsg("本次共导入数据"+liverInfo.size()+"条, 其中失败"+faileLiverList.size()+"条！点击可下载");
			}else{
				resultable.setMsg("导入成功！");
			}
			
	}
	
	/**
	 * 
	 * 方法描述：验证主播信息 <br/>
	 * 创建人： ChenBo <br/>
	 * 创建时间：2017年1月22日上午11:39:49 <br/>
	 * @param liverInfo
	 * @param resultable
	 */
	private int validateProductInfo(BLiver liver) {
		if(org.apache.commons.lang.StringUtils.isBlank(liver.getPhone())
			|| liver.getNickname()==null
			|| liver.getName()==null
			|| liver.getSellerLookStatus()==null
			|| liver.getIsInside()==null
			|| liver.getSex()==null
			|| org.apache.commons.lang.StringUtils.isBlank(liver.getIdcard())
			|| org.apache.commons.lang.StringUtils.isBlank(liver.getWeixin())
			|| liver.getLedgerRatio()==null
			|| liver.getSaleCouponRatio()==null
			|| liver.getUseCouponRation()==null
//			|| org.apache.commons.lang.StringUtils.isBlank(liver.getIntroduceMv())
//			|| org.apache.commons.lang.StringUtils.isBlank(liver.getOfficeName())
			){
			return 0;
		}
		if(liver.getSortVal()==null){
			liver.setSortVal(0);
		}
		if(org.apache.commons.lang.StringUtils.isBlank(liver.getAvatar())){
			liver.setAvatar("");
		}
		if(org.apache.commons.lang.StringUtils.isBlank(liver.getWechatPic())){
			liver.setWechatPic("");;
		}
		if(liver.getBirthdayData()==null){
			liver.setBirthday("1990-01-01");;
		}else{
			liver.setBirthday(this.getParseDate(liver.getBirthdayData()));;
		}
		return 1;
	}
	
	/**
	 * 
	 * 方法描述：获取账号信息 <br/>
	 * 创建人： ChenBo <br/>
	 * 创建时间：2017年1月22日下午2:59:31 <br/>
	 * @param phone
	 * @return 0 无寻蜜鸟会员和直播观众信息  1 存在寻蜜鸟会员信息，不存在直播观众信息  2存在会员和直播观众信息
	 */
	private int getBindingUrs(String phone){
		BLiver liver=new BLiver();
		liver.setPhone(phone);
		BLiver liverInfo = this.getUrsDetailInfo(liver);
		
		if(liverInfo==null){
			return 0;
		}else{ 
			BLiver viewer = this.selectBLiver(liver);
			if(viewer == null){
				return 1;
			}else{
				return 2;
			}
		}
	}
	
	private String getParseDate(Long longDate){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String date = sdf.format(new Date(longDate));
		return date;
	}

	/**
	 * 方法描述：注销直播钱包 <br/>
	 * 创建人：  huang'tao <br/>
	 * 创建时间：2017-4-5下午4:45:04 <br/>
	 * @param uid
	 */
	public void updateLiveWallet(Integer uid) {
		if(uid != null){
			com.xmniao.xmn.core.thrift.service.liveService.LiveWalletService.Client client = (com.xmniao.xmn.core.thrift.service.liveService.LiveWalletService.Client)(liveWalletServiceServiceClient.getClient());
			Map<String, String> paraMap =new HashMap<String,String>();
			try {
				paraMap.put("uid", uid.toString());
				client.cleanLiveWallet(paraMap);
			} catch (FailureException e) {
				e.printStackTrace();
			} catch (TException e) {
				e.printStackTrace();
			}finally{
				liveWalletServiceServiceClient.returnCon();
			}
		}
	}
	
	/**
	 * 
	 * 方法描述：统计主播详情 <br/>
	 * 创建人：  huang'tao <br/>
	 * 创建时间：2017-4-13下午4:58:03 <br/>
	 * @return
	 */
	public Map<String,Object> countAnchor(){
		return liverDao.countAnchor();
	}

	/**
	 * 方法描述：添加主播银行卡信息 <br/>
	 * 创建人：  huang'tao <br/>
	 * 创建时间：2017-4-14下午2:33:26 <br/>
	 * @param card
	 * @return
	 */
	public Resultable addCard(Card card) {
		Resultable resultable = new Resultable();
		Map<String, String> paraMap = new HashMap<String, String>();
		assembleParm(card);
		addCardParamMap(paraMap, card);
		MentionAccountService.Client client = (MentionAccountService.Client) (mentionAccountServiceClient
				.getClient());
		try {
			Map<String, String> resultMap = client.addMentionAccount(paraMap);
			resultable.setMsg(resultMap.get("msg"));
			resultable.setSuccess(String.valueOf(
					SellerConstants.ADD_CARD_STATUS).equals(
					resultMap.get("state")) ? true : false);
			this.log.debug("绑定银行卡返回信息: " + resultMap.get("msg"));
		} catch (Exception e) {
			this.log.debug("绑定银行卡失败！");
			throw new ApplicationException("绑定银行卡异常", e,
					new Object[] { card }, new String[] { "主播",
							card.getNickname(), "添加银行卡", "添加" });
		} finally {
			mentionAccountServiceClient.returnCon();
		}
		return resultable;
	}

	/**
	 * 方法描述：组装接口参数 <br/>
	 * 创建人：  huang'tao <br/>
	 * 创建时间：2017-4-14下午2:48:08 <br/>
	 * @param paraMap
	 * @param card
	 */
	private void addCardParamMap(Map<String, String> paraMap, Card card) {
		paraMap.put("uId", card.getUid());
		if (!"null".equals(card.getId()) && card.getId() != null) {
			paraMap.put("id", String.valueOf(card.getId()));
		}
		paraMap.put("type", String.valueOf(2));
		paraMap.put("account", String.valueOf(card.getCardId()));
		paraMap.put("cardType", String.valueOf(card.getCardType()));
		paraMap.put("userName", String.valueOf(card.getCardUserName()));
		paraMap.put("bankName", String.valueOf(card.getBankName()));
		paraMap.put("mobileId", String.valueOf(card.getCardPhone()));
		paraMap.put("isuse", String.valueOf(0));//银行卡提现用途,0  对于用户和合作商此字无效 ,1 营业收入, 2 佣金
		paraMap.put("userType", String.valueOf(1));// 用户类型 1用户 2商家 3合作商
		paraMap.put("ispublic", String.valueOf(0));//0 对私  1对公
		paraMap.put("idtype", String.valueOf(card.getIdtype()));
		paraMap.put("identity", String.valueOf(card.getIdentity()));
		paraMap.put("bank", String.valueOf(card.getBank()));
		paraMap.put("abbrev", String.valueOf(card.getAbbrev()));
		paraMap.put("province", String.valueOf(card.getProvince()));
		paraMap.put("cityname", String.valueOf(card.getCityname()));
	}

	/**
	 * 方法描述：在此处添加方法描述 <br/>
	 * 创建人：  huang'tao <br/>
	 * 创建时间：2017-4-14下午2:48:06 <br/>
	 * @param card
	 */
	private void assembleParm(Card card) {
		card.setProvince(getAreaName(card.getProvinceId()).getTitle());
		card.setCityname(getAreaName(card.getCitynameId()).getTitle());
		TBank tbank = new TBank();
		tbank.setAbbrev(card.getBank());
		card.setBank(tBankService.getTBank(tbank).getBankname());
	}

	/**
	 * 方法描述：在此处添加方法描述 <br/>
	 * 创建人：  huang'tao <br/>
	 * 创建时间：2017-4-14下午3:25:11 <br/>
	 * @param provinceId
	 * @return
	 */
	private TArea getAreaName(Integer areaId) {
		TArea area = new TArea();
		area.setAreaId(areaId);
		List<TArea> list = areaDao.getLdAll(area);
		if (list.size() > 0) {
			area = list.get(0);
		}
		return area;
	}

	/**
	 * 方法描述：获取主播银行卡列表 <br/>
	 * 创建人：  huang'tao <br/>
	 * 创建时间：2017-4-14下午6:04:34 <br/>
	 * @param card
	 * @return
	 */
	public Pageable<Card> getCardList(Card card) {
		Pageable<Card> sellerInfoList = new Pageable<Card>(card);
		List<Card> cardList = new ArrayList<Card>();
		MentionAccountService.Client client = (MentionAccountService.Client) (mentionAccountServiceClient
				.getClient());
		try {
			List<Map<String, String>> cardslist = client.getMentionAccount(
					String.valueOf(card.getUid()), 1);
			for (Map<String,String> m : cardslist) {
				cardList.add(getCardFromMap(m));
			}
		} catch (Exception e) {
			e.printStackTrace();
			this.log.debug("获取数据的服务未开启或开启出现异常!");
			sellerInfoList.setTotal(0L);
			return sellerInfoList;
		} finally {
			mentionAccountServiceClient.returnCon();
		}
		sellerInfoList.setContent(cardList);
		sellerInfoList.setTotal(Long.parseLong(String.valueOf(cardList.size())));
		return sellerInfoList;
	}

	/**
	 * 方法描述：将Map转化为Card对象 <br/>
	 * 创建人：  huang'tao <br/>
	 * 创建时间：2017-4-14下午6:08:26 <br/>
	 * @param map
	 * @return
	 */
	private Card getCardFromMap(Map<String,String> map) {
		Card card = new Card();
		card.setUid(map.get("uId"));
		card.setId(map.get("id"));
		card.setCardId(String.valueOf(map.get("account")));
		card.setCardType(String.valueOf(map.get("cardtype")));
		card.setCardUserName(map.get("username"));
		card.setBankName(map.get("bankname"));
		card.setCardPhone(String.valueOf(map.get("mobileid")));
		card.setCardPurpose(String.valueOf(map.get("isuse")));
		card.setIspublic(String.valueOf(map.get("ispublic")));
		card.setIdtype(String.valueOf(map.get("idtype")));
		card.setIdentity(String.valueOf(map.get("identity")));
		card.setBank(String.valueOf(map.get("bank")));
		card.setAbbrev(String.valueOf(map.get("abbrev")));
		card.setProvince(String.valueOf(map.get("province")));
		card.setCityname(String.valueOf(map.get("cityname")));
		return card;
	}
	
	
	/*
	 * 获取主播关联的寻蜜鸟用户详情
	 */
	public List<BLiver> getUrsDetailInfoList(Object[] array){
		return bursDao.getUrsDetailInfoList(array);
	}
	
	
	/**
	 * 
	 * 方法描述：获取主播真实粉丝数列表 <br/>
	 * 创建人：  huang'tao <br/>
	 * 创建时间：2017-9-1下午2:42:37 <br/>
	 * @param param
	 * @return
	 */
	public List<BLiver> getRealFansList(Map<String,Object> param){
		return liverDao.getRealFansList(param);
	}
	
	/**
	 * 
	 * 方法描述：获取主播机器人粉丝数列表 <br/>
	 * 创建人：  huang'tao <br/>
	 * 创建时间：2017-9-1下午3:27:15 <br/>
	 * @param param
	 * @return
	 */
	public List<BLiver> getRobotFansList(Map<String,Object> param){
		return liverDao.getRobotFansList(param);
	}

	/**
	 * 方法描述：保存主播粉丝配置 <br/>
	 * 创建人：  huang'tao <br/>
	 * 创建时间：2017-9-4上午10:37:33 <br/>
	 * @param configureRequest
	 */
	public void saveFansConf(FansConfigureRequest configureRequest) {
		String ids = configureRequest.getIds();
		String uids = configureRequest.getUids();
		int fansMin = configureRequest.getFansMin().intValue();
		int fansMax = configureRequest.getFansMax().intValue();
		int sub= fansMax - fansMin;
		if(sub<=0){
			return ;
		}
		
		
		if(org.apache.commons.lang.StringUtils.isNotBlank(ids) 
				&& org.apache.commons.lang.StringUtils.isNotBlank(uids)){
			String[] idArray = ids.split(",");
			String[] uidArray = uids.split(",");
			int length = idArray.length;
			Random random = new Random();
			ExecutorService executor=Executors.newFixedThreadPool(10);
			
			for(int i=0;i<length;i++){
				String liverId = idArray[i];
				String liverUid = uidArray[i];
				
				int fansNum=fansMin+random.nextInt(sub)+1;//随机生成的粉丝数
				Map<String,Object> param= new HashMap<String,Object>();
				param.put("liverEndId", liverId);
				param.put("endUid", liverUid);
				param.put("fansNum", fansNum);
				
				FansConfigureThread fansConfigureThrea=new FansConfigureThread(liveFocusService,param);
				executor.execute(fansConfigureThrea);
				
			}
			
			
		}
	}
	
	
}
