package com.xmniao.xmn.core.businessman.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xmniao.xmn.core.base.*;
import com.xmniao.xmn.core.billmanagerment.dao.AllBillDao;
import com.xmniao.xmn.core.billmanagerment.entity.Bill;
import com.xmniao.xmn.core.business_cooperation.dao.JointDao;
import com.xmniao.xmn.core.business_cooperation.entity.TJoint;
import com.xmniao.xmn.core.business_cooperation.entity.TJointSaasLedger;
import com.xmniao.xmn.core.business_cooperation.service.JointSaasLedgerService;
import com.xmniao.xmn.core.business_cooperation.util.PartnerConstants;
import com.xmniao.xmn.core.businessman.constant.SaasConstant;
import com.xmniao.xmn.core.businessman.dao.*;
import com.xmniao.xmn.core.businessman.entity.*;
import com.xmniao.xmn.core.businessman.util.SellerConstants;
import com.xmniao.xmn.core.common.dao.AreaDao;
import com.xmniao.xmn.core.common.entity.TArea;
import com.xmniao.xmn.core.common.entity.TBank;
import com.xmniao.xmn.core.common.entity.TBusiness;
import com.xmniao.xmn.core.common.entity.TTrade;
import com.xmniao.xmn.core.common.service.BusinessService;
import com.xmniao.xmn.core.common.service.TBankService;
import com.xmniao.xmn.core.common.service.TradeService;
import com.xmniao.xmn.core.exception.ApplicationException;
import com.xmniao.xmn.core.http.entity.InterfacRequest;
import com.xmniao.xmn.core.http.entity.PhpHttpPageable;
import com.xmniao.xmn.core.http.util.AppMessageUtil;
import com.xmniao.xmn.core.marketingmanagement.entity.TActivity;
import com.xmniao.xmn.core.marketingmanagement.service.BargainProductService;
import com.xmniao.xmn.core.marketingmanagement.service.TActivityService;
import com.xmniao.xmn.core.reward_dividends.dao.BursEarningsRelationDao;
import com.xmniao.xmn.core.system_settings.entity.TUser;
import com.xmniao.xmn.core.thrift.client.proxy.ThriftClientProxy;
import com.xmniao.xmn.core.thrift.exception.FailureException;
import com.xmniao.xmn.core.thrift.service.ledgerService.ResponseSplitMap;
import com.xmniao.xmn.core.thrift.service.ledgerService.SplitService;
import com.xmniao.xmn.core.thrift.service.liveService.LiveOrderService;
import com.xmniao.xmn.core.thrift.service.liveService.ResponseData;
import com.xmniao.xmn.core.thrift.service.synthesizeService.MentionAccountService;
import com.xmniao.xmn.core.thrift.service.synthesizeService.SynthesizeService;
import com.xmniao.xmn.core.util.*;
import com.xmniao.xmn.core.util.StringUtils;
import com.xmniao.xmn.core.xmermanagerment.dao.BXmerDao;
import com.xmniao.xmn.core.xmermanagerment.dao.TSaasOrderDao;
import com.xmniao.xmn.core.xmermanagerment.dao.TSaasSoldOrderDao;
import com.xmniao.xmn.core.xmermanagerment.entity.*;
import com.xmniao.xmn.core.xmnburs.dao.BursDao;

import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.lang3.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.thrift.TException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.beans.PropertyDescriptor;
import java.math.BigDecimal;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 项目名称：XmnWeb
 * <p>
 * 类名称：SellerService
 * <p>
 * 类描述： 商家(商户)
 * <p>
 * 创建人： zhou'sheng
 * <p>
 * 创建时间：2014年11月11日15时22分21秒
 * <p>
 * Copyright (c) 广东寻蜜鸟网络技术有限公司
 */
@Service
public class SellerService extends BaseService<TSeller> {

    private static final String PROPERTIES_FILE_NAME = "conf_config.properties";
    private static final String PROPERTIES__NAME = "isAutomatic";

    static {
        Properties props = PropertiesUtil
                .getCustomProperties(PROPERTIES_FILE_NAME);
        String isopen = props.getProperty(PROPERTIES__NAME);
        Boolean.valueOf(isopen).booleanValue();
    }

    @Resource(name = "mentionAccountServiceClient")
    private ThriftClientProxy mentionAccountServiceClient;
    @Resource(name = "liveOrderServiceClient")
    private ThriftClientProxy liveOrderServiceClient;
    @Resource(name = "sellerServiceClient")
    private ThriftClientProxy sellerServiceClient;
    private String saasLedgerKey = PropertiesUtil.readValue("redis.queue.saasledger");//saas分账队列KEY

    private String sellerRandomNum = PropertiesUtil.readValue("redis.queue.sellerRandom");//商家随机数key

    @Autowired
    private SellerCommonService sellerCommonService;

    @Autowired
    private BargainProductService bargainProductService;
    /**
     * 商家dao
     */
    @Autowired
    private SellerDao sellerDao;
    // 折扣设置记录表dao
    @Autowired
    private AgioRecordDao agioRecordDao;

    @Autowired
    private SellerAgioDao sellerAgioDao;
    @Autowired
    private SellerDetailedDao sellerDetailedDao;

    /**
     * 账号信息dao
     */
    @Autowired
    private SellerAccountDao sellerAccountDao;
    /**
     * 推广设置表dao
     */
    @Autowired
    private ExtensionSetDao extensionSetDao;
    @Autowired
    private SellerAccountService sellerAccountService;

    @Autowired
    private SellerDetailedService sellerDetailedService;

    @Autowired
    private SellerApplyService sellerApplyService;

    @Autowired
    private SellerLandmarkService sellerLandmarkService;

    // 折扣service
    @Autowired
    private SellerAgioService sellerAgioService;

    // 行业
    @Autowired
    private TradeService tradeService;

    @Autowired
    private TBankService tBankService;// 银行信息获取

    /*
     * @Autowired private AreaService areaService;
     */

    // 商圈
    @Autowired
    private BusinessService businessService;

    // 图标
    @Autowired
    private SellerPicService sellerPicService;
    @Autowired
    private SellerPicDao sellerPicDao;

    // 营销活动
    @Autowired
    private TActivityService activityService;

    /**
     * 添加活动
     */
    @Autowired
    private SellerMarketingService sellerMarketingService;

    @Resource(name = "synthesizeServiceClient")
    private ThriftClientProxy synthesizeServiceClient;

    @Resource(name = "splitServiceClient")
    private ThriftClientProxy splitServiceClient;

    @Autowired
    private SellerLandmarkDao sellerLandmarkDao;

    /**
     * 商家贷申请dao
     */
    @Autowired
    private SellerApplyDao sellerApplyDao;

    /**
     * 合作商dao
     */
    @Autowired
    private JointDao jointDao;

    @Autowired
    private AreaDao areaDao;

    @Autowired
    private MongoBaseService mongoBaseService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private TSaasSoldOrderDao saasSoldOrderDao;

    @Autowired
    private BXmerDao xmerDao;

    @Autowired
    private BursDao bursDao;

    @Autowired
    private TSaasOrderDao saasOrderDao;

    @Autowired
    private AllBillDao allBillDao;
    @Autowired
    private JointSaasLedgerService jointSaasLedgerService;
    private String addwaiterRegister = "user/waiterRegister.html";// 服务员用户增加接口
    private String url = null;
    @Resource(name = "sellerCollectionname")
    private String collectionname;

    public SellerService() {
        this.url = PropertiesUtil.readValue("http.user.url");
        this.url = PropertiesUtil.readValue("http.user.url");
    }

    @Override
    protected BaseDao<TSeller> getBaseDao() {
        return sellerDao;
    }

    /**
     * 是否需要添加或更新mongoDB操作
     */
    private boolean enableUpdate(MSeller mSeller) {
        if (mSeller != null
                && mSeller.getIs_virtual().equals("0")    //不是虚拟商家
                && mSeller.getStatus() == 3        //已签约商家
                && mSeller.getIsonline() == 1) {    //已上线商家
            return true;
        }
        return false;
    }

    public void InsertOrUpdateMongo(int sellerId) {
        InsertOrUpdateMongo(sellerId, false);
    }

    /**
     * 添加或者更新mongodb中的商家信息
     *
     * @param sellerId
     */
    @Transactional
    public void InsertOrUpdateMongo(int sellerId, boolean online) {
//        try {
        MSeller m = sellerDao.getMSellerInfo(sellerId);

        this.log.debug("修改后的状态：" + m.getStatus());
        if (m != null && enableUpdate(m)) {
            this.log.debug("添加或修改状态为已签约商家详情后同步到MongDB-->");
            if (online) {
                //先恢复MongoDB数据
                ResponseData data = dealSellerAnalysisInfo(sellerId, true);
                if (data == null || data.getState() != 0) {
                    this.log.debug("获取商家浏览消费统计失败");
                } else {
                    m.setConsumption(Integer.parseInt(data.getResultMap().get("consumption")));
                    m.setViews(Integer.parseInt(data.getResultMap().get("views")));
                }

                Map<String, String> paraMap = new HashMap<String, String>();
                paraMap.put("sellerid", sellerId + "");
                paraMap.put("type", "0");
                ResponseData responseData = this.getSellerLiveCountInfo(paraMap);
                if (responseData == null || responseData.getState() != 0) {
                    this.log.debug("获取商家直播信息统计失败");
                } else {
                    m.setWeights(Integer.parseInt(responseData.getResultMap().get("weights")));
                    m.setIs_live(Integer.parseInt(responseData.getResultMap().get("hasLive")));
                    m.setIs_fans_coupon(Integer.parseInt(responseData.getResultMap().get("hasFansCoupon")));
                    m.setIs_advance(Integer.parseInt(responseData.getResultMap().get("hasAdvance")));
                }
                paraMap.put("type", "3");
                responseData = this.getSellerAnalysisInfo(paraMap);
                if (responseData == null || responseData.getState() != 0) {
                    this.log.debug("获取商家收藏统计失败");
                } else {
                    m.setSaved(Integer.parseInt(responseData.getResultMap().get("saved")));
                }
            }

            long sid = (long) sellerId;
            MSellerLandmark landmark = sellerLandmarkService
                    .getMSellerLandmarkByid(sid);
            if (landmark == null) {
                landmark = new MSellerLandmark();
            }
            m.setCoordinate(landmark);
            m.setPic_logo(JSONObject.toJSONString(
                    sellerPicService.getMSellerLogo(sid), JsonUtil.vfilter));
            m.setPic_cover(JSONObject.toJSONString(
                    sellerPicService.getMSellerCover(sid), JsonUtil.vfilter));

            List<MSellerPic> pics = sellerPicService.getMSellerPics(sid);
            if (pics != null && !pics.isEmpty()) {
                m.setPic_pics(JSONObject.toJSONString(pics, JsonUtil.vfilter));
            }
            Criteria criteria = Criteria.where("sellerid").is(sellerId);
            MSeller tm = mongoBaseService.findOne(collectionname, criteria, MSeller.class);
            if (tm != null) {
                m.set_id(tm.get_id());
            }

            this.log.info("Add seller: [sellerid = " + sellerId
                    + "] to MongoDB!");

            //查询redis 商家随机数
            if (online) {
                Integer sellerRandom = 100;
                String randomStr = (String) redisTemplate.opsForHash().get(sellerRandomNum, m.getSellerid() + "");
                if (randomStr != null && org.apache.commons.lang.StringUtils.isNotBlank(randomStr)) {
                    sellerRandom = Integer.parseInt(randomStr);
                } else {
                    sellerRandom = (int) (Math.random() * 400) + 101;//200-500随机数
                    redisTemplate.opsForHash().put(sellerRandomNum, m.getSellerid() + "", sellerRandom + "");
                }
//                Map<Object, Object> sellerMap = redisTemplate.opsForHash().entries(sellerRandomNum);
//                if(sellerMap == null){
//                    sellerMap = new HashMap<>();
//                }
//                if(sellerMap.containsKey(m.getSellerid())){//判断redis是否存在商家随机数，不存在创建
//                    sellerRandom = (Integer) sellerMap.get(m.getSellerid());
//                }else{
//                    sellerRandom = (int)(Math.random()*400)+101;//200-500随机数
//                    redisTemplate.opsForHash().put(sellerRandomNum,m.getSellerid()+"",sellerRandom+"");
//                }

                m.setSeller_random_num(sellerRandom);
                m.setSeller_random_num_consumption(sellerRandom + m.getConsumption());
            }

            if (online) {
                this.log.debug("同步的数据：" + m);
                mongoBaseService.insertOrUpdate(collectionname, m);
            } else {
                Map<String, Object> outMap = this.turnBeanToMap(m);
                this.removeCountProperty(outMap);
                this.log.debug("同步的数据：" + outMap);
                int result = mongoBaseService.updateOne(collectionname, criteria, outMap);
                log.debug("共影响了" + result + "条记录");
            }

            this.log.debug("添加或修改状态为已签约商家详情后同步到MongDB<--");
        }
//        } catch (Exception e) {
//            this.log.error("靠，原来是没调用mongodb",e);
//        }
    }

    /**
     * 更新mongodb中的商家信息
     * 自动填充更新时间udate
     *
     * @param sellerId
     */
    public Map<String, Object> turnBeanToMap(Object obj) {

        Map<String, Object> outMap = new HashMap<String, Object>(0);
        try {
            PropertyUtilsBean propertyUtilsBean = new PropertyUtilsBean();
            PropertyDescriptor[] descriptors = propertyUtilsBean.getPropertyDescriptors(obj);
            for (int i = 0; i < descriptors.length; i++) {
                String name = descriptors[i].getName();
                if (!"class".equals(name)) {
                    if (propertyUtilsBean.getNestedProperty(obj, name) != null) {
                        outMap.put(name, propertyUtilsBean.getNestedProperty(obj, name));
                    }//过滤有NULL值的字段
                }
            }
        } catch (Exception e) {
            log.error("Bean转换成Map<String,Object>异常", e);
        }
        return outMap;
    }

    /**
     * 更新mongodb中的商家信息
     * 自动填充更新时间udate
     *
     * @param sellerId
     */
    public void removeCountProperty(Map<String, Object> map) {

        if (map != null) {
            map.remove("is_live");
            map.remove("is_advance");
            map.remove("is_fans_coupon");
            map.remove("views");
            map.remove("consumption");
            map.remove("saved");
            map.remove("weights");
            map.remove("_id");
        }
    }

    /**
     * 更新mongodb中的商家信息
     * 自动填充更新时间udate
     *
     * @param sellerId
     */
    @Transactional
    public void updateMongo(int sellerId, Map<String, Object> updateMap) {

        //MSeller mSeller = mongoBaseService.findOne("sellerid",sellerId+"",MSeller.class);
        if (updateMap == null || updateMap.size() == 0) {
            return;
        }
        updateMap.put("udate", DateUtil.getNow(DateUtil.Y_M_D_HMS));
        this.log.debug("商家【" + sellerId + "】需在MongoDB中更新的数据：" + JSON.toJSONString(updateMap));
        Criteria criteria = Criteria.where("sellerid").is(sellerId);
        mongoBaseService.updateOne(collectionname, criteria, updateMap);
    }

    /**
     * 将商家从MongoDB中删除
     *
     * @param sellerId
     */
    @Transactional
    public void deleteSellerFromMongo(int sellerId) {
        this.log.info("Delete seller:[sellerId = " + sellerId
                + "] from MongoDB!");
        Criteria criteria = Criteria.where("sellerid").is(sellerId);
        mongoBaseService.delete(collectionname, new Query(criteria));
    }

    /**
     * 获取商家列表信息
     *
     * @param seller
     * @return
     */
    public Pageable<TSeller> getSellerInfoList(TSeller seller) {
        Pageable<TSeller> sellerInfoList = new Pageable<TSeller>(seller);
        // 商家列表内容
        List<TSeller> sellerList = sellerDao.getSellerList(seller);

        /**
         * add by lifeng 20160607 17:14
         *  加载并封装寻蜜客姓名
         */
        for (TSeller tSeller : sellerList) {
            if (tSeller.getUid() != null && !tSeller.getUid().equals("")) {
                String xmerName = bursDao.selectUrsName(tSeller.getUid());
                tSeller.setXmerName(xmerName == null ? "" : xmerName);
            }

        }

        sellerInfoList.setContent(sellerList);
        // 总条数
        sellerInfoList.setTotal(getSellerCount(seller));
        return sellerInfoList;
    }

    /**
     * 修改物料发送状态
     *
     * @param seller
     * @param seller
     */
/*    public void changeMaterielStatus(String sellerid,String uid){
        try {
            sellerDao.changeMaterielStatus(sellerid,uid);
        } catch (Exception e) {
            log.error("修改物料发送状态失败",e);
        }
    }*/
    public Long getSellerCount(TSeller seller) {
        return sellerDao.sellerCount(seller);
    }

    /**
     * 获取商家列表，不查询总条数
     *
     * @param seller
     * @return
     */
    public Pageable<TSeller> getSellerInfo(TSeller seller) {
        Pageable<TSeller> sellerInfoList = new Pageable<TSeller>(seller);
        // 商家列表内容
        sellerInfoList.setContent(sellerDao.getSellerList(seller));
        return sellerInfoList;
    }

    /**
     * 获取商家列表
     *
     * @param seller
     * @return
     */
    public List<TSeller> getSellerList(TSeller seller) {
        return sellerDao.exportSellerList(seller);
    }

    /**
     * 下拉框查寻所有总店
     *
     * @param seller
     * @return
     */
    public Pageable<TSeller> getFatherSeller(TSeller seller) {
        this.log.debug("取得连锁店总店->");
        Pageable<TSeller> fatherSeller = new Pageable<TSeller>(seller);
        // 查询已签约的连锁店商家
        seller.setStatus(SellerConstants.SELLER_STATUS_APPROVE);
        // 商家列表内容
        fatherSeller.setContent(sellerDao.getFatherSeller(seller));
        this.log.debug("取得连锁店总店<-");
        return fatherSeller;
    }

    /**
     * 下拉框查寻所有总店
     *
     * @param seller
     * @return
     */
    public Pageable<TSeller> getSellerIdAndSellerName(TSeller seller) {
        Pageable<TSeller> fatherSeller = new Pageable<TSeller>(seller);
        // 查询已签约的连锁店商家
        seller.setStatus(SellerConstants.SELLER_STATUS_APPROVE);
        // 商家列表内容
        fatherSeller.setContent(sellerDao.getSellerIdAndSellerName(seller));
        return fatherSeller;
    }

    /**
     * 查询已上线的签约商家
     *
     * @param seller
     * @return
     */
    public Pageable<TSeller> getSellerIdAndName(TSeller seller) {
        Pageable<TSeller> fatherSeller = new Pageable<TSeller>(seller);
        // 查询已签约的连锁店商家
        seller.setStatus(SellerConstants.SELLER_STATUS_APPROVE);
        // 商家列表内容
        fatherSeller.setContent(sellerDao.getSellerIdAndName(seller));
        return fatherSeller;
    }

    /**
     * 查询某商家的所有服务员
     */
    public List<TSeller> getWaiterList(int sellerid) {
        List<TSeller> waiterList = new ArrayList<>();
        waiterList = (List<TSeller>) sellerDao.getWaiterList(sellerid);
        return waiterList;
    }

    /**
     * 优化后的批量更改状态. 以下各处公用此代码 1、商家查看页面审核 2、商家待审核查看页面审核 3、待审核列表页面
     *
     * @param objects
     * @param sellerInfo
     * @return
     * @throws Exception
     * @author zhang'zhiwen
     * @date 2015年8月21日 上午11:45:20
     */
    @Transactional
    public Map<String, Object> batchUpdateSellerStatusOptimized(String[] objects,
                                                                TSeller sellerInfo) throws Exception {
        Map<String, Object> res = new HashMap<String, Object>();
        /* 修改商家状态 */
        int num = 0;
        Integer status = sellerInfo.getStatus();
        Assert.notNull(sellerInfo, "传入的修改信息为null");
        Assert.notEmpty(objects, "商家编号为空");
        Assert.notNull(status, "修改状态为空");

        Date date = new Date();
        TSeller persistentSeller = null;
        for (String sellerIdStr : objects) {
            persistentSeller = getObject(new Long(Integer.parseInt(sellerIdStr)));

            /* 审批通过或恢复合作 */
            if (status == SellerConstants.SELLER_STATUS_APPROVE) {
                num += sellerApproveOrResume(persistentSeller, date);
            }
            /* 审核不通过 */
            if (status == SellerConstants.SELLER_STATUS_DISAPPROVE) {
                if (sellerInfo.getExamineinfo() != null) {
                    persistentSeller
                            .setExamineinfo(sellerInfo.getExamineinfo());
                }
                num += sellerDisapprove(persistentSeller, date);
            }
            /* 暂停合作 */
            if (status == SellerConstants.SELLER_STATUS_PAUSE) {
                num += sellerPause(persistentSeller, date);
            }
        }
        res.put("num", num);
        return res;
    }

    /**
     * @param persistentSeller 从数据库中查出的商家
     * @param sellerInfo       接口的请求参数
     * @return
     * @throws Exception
     */
    @Transactional
    public Map<String, Object> updateSellerStatusOptimized(TSeller persistentSeller, TSeller sellerInfo) throws Exception {
        Map<String, Object> res = new HashMap<String, Object>();
        /* 修改商家状态 */
        int newstatus = sellerInfo.getStatus();
        
        /* 审批通过或恢复合作 */
        if (newstatus == SellerConstants.SELLER_STATUS_APPROVE) {
            sellerApproveOrResume(persistentSeller, sellerInfo.getUdate());
        }

        /* 审核不通过 */
        if (newstatus == SellerConstants.SELLER_STATUS_DISAPPROVE) {
            if (sellerInfo.getExamineinfo() != null) {
                persistentSeller.setExamineinfo(sellerInfo.getExamineinfo());
            }
            sellerDisapprove(persistentSeller, sellerInfo.getUdate());
        }

        /* 暂停合作 */
        if (newstatus == SellerConstants.SELLER_STATUS_PAUSE) {
            sellerPause(persistentSeller, sellerInfo.getUdate());
        }

        /* 注销合作 */
        if (newstatus == SellerConstants.SELLER_STATUS_RECOVER) {
            sellerRecover(persistentSeller, sellerInfo.getUdate());
        }

        return res;
    }

    /**
     * 商家状态修改时推送消息到APP账户
     *
     * @param sellerId
     * @param pushMessageParam
     * @throws PushMessageFailException
     * @author zhang'zhiwen
     * @date 2015年8月21日 上午11:43:55
     */
    public void pushMessageWhenStatusChange(Integer sellerId,
                                            Map<String, String> pushMessageParam) throws ApplicationException {
        Map<String, String> param = new HashMap<String, String>();
        param.put("uid", pushMessageParam.get("uid"));
        param.put("usertype", "4");
        param.put("type", "3");
        param.put("title", pushMessageParam.get("title"));
        param.put("content", pushMessageParam.get("content"));
        param.put("action", pushMessageParam.get("action"));
        param.put("contenttype", "1");
        param.put("remind", "0");
        // 推送消息
        try {
            this.log.debug("消息推送开始");
            AppMessageUtil.pushMessageToApp(param);
        } catch (Exception e) {
            this.log.error("消息推送异常:" + e);
            throw new ApplicationException("消息推送异常", e);
        }
    }

    /**
     * 更新一级行业、级行业、商圈的个数
     *
     * @param category
     * @param genre
     * @param zoneid
     * @param status
     */
    @Transactional
    public void updateCategoryCount(TSeller seller, Integer status) {
        categoryUpdate(seller, status);// 审批通过 类别更新
        updateBusiness(seller, "商圈", status);// 审批通过商圈数+1
    }

    public void categoryUpdate(TSeller seller, Integer status) {
        String category = seller.getCategory();
        String genre = seller.getGenre();
        Integer sellerId = seller.getSellerid();
        updateCategory(category, sellerId, "一级类别", status);// 审批通过一级类别+1
        updateCategory(genre, sellerId, "二级类别", status);// 审批通过二级类别+1
    }

    private void updateCategory(String id, Integer sellerId, String handelName,
                                Integer status) {
        if (StringUtils.hasLength(id)) {
            TTrade trade = tradeService.getObject(new Long(id));
            if (null != trade) {
                setTradeNumber(trade, status);
                tradeService.update(trade);
            }
        }
    }

    public void setTradeNumber(TTrade trade, Integer status) {
        if (status == SellerConstants.STATUS_PASS) {
            trade.setNumber(this.setNumberAdd(trade.getNumber()));
        } else if (status == SellerConstants.STATUS_NOTPASS) {
            trade.setNumber(this.setNumberSub(trade.getNumber()));
        }
    }

    /**
     * @param paramMap
     * @param seller
     * @author dong'jietao
     * @date 2015年6月17日 下午5:56:31
     * @TODO 增加钱包，接口调用前参数组装
     */
    public void putParamsMap(Map<String, String> paramMap, TSeller seller) {
        /*
         * String phoneid = seller.getPhoneid(); String password = ""; if
         * (StringUtils.hasLength(phoneid)) { password = phoneid
         * .substring(phoneid.length() - 6, phoneid.length()); }
         */
        paramMap.put("uId", String.valueOf(seller.getSellerid()));
        paramMap.put("userType",
                String.valueOf(SellerConstants.SELLER_USERTTYPE_S));
        paramMap.put("password", "");// 密码默认设置为空
        paramMap.put("phoneNumber", String.valueOf(seller.getAccount()));
        paramMap.put("sellerName", String.valueOf(seller.getSellername()));
    }

    /**
     * 更新行业与商圈
     *
     * @param seller      对应编号
     * @param seller 业务处理对象
     */
    @SuppressWarnings("rawtypes")
    private void updateBusiness(TSeller seller, String handelName,
                                Integer status) {
        Integer sellerId = seller.getSellerid();
        String id = new String(seller.getZoneid() == null ? "" : seller
                .getZoneid().toString());
        if (StringUtils.hasLength(id)) {
            TBusiness business = businessService.getObject(new Long(id));
            if (null != business) {
                setBusinessNumber(business, status);
                businessService.update(business);
            }
        }
    }

    public void setBusinessNumber(TBusiness business, Integer status) {
        if (status == SellerConstants.STATUS_PASS) {
            business.setNumber(this.setNumberAdd(business.getNumber()));
        } else if (status == SellerConstants.STATUS_NOTPASS) {
            business.setNumber(this.setNumberSub(business.getNumber()));
        }
    }

    private Integer setNumberAdd(Integer number) {
        return (null != number) ? ++number : 0;
    }

    private Integer setNumberSub(Integer number) {
        return (null != number) && (number > 0) ? --number : 0;
    }

    /**
     * 商家添加默认帐号
     *
     * @param persistentSeller 商家编号
     * @param persistentSeller  商家手机号
     * @return 默认账户的帐号
     */
    @Transactional
    private void addDefaultAccount(TSeller persistentSeller) throws Exception {
        Integer sellerid = new Integer(persistentSeller.getSellerid());
        String phoneId = persistentSeller.getPhoneid();

        Assert.notNull(sellerid, "商家编号不能为空！");
        Assert.hasLength(phoneId, "商家的手机号不为null且长度大于0");

        TSellerAccount sellerAccount = new TSellerAccount();
        sellerAccount.setSdate(new Date());
        sellerAccount.setSellerid(sellerid);
        sellerAccount.setType(new Integer(1));
        // 校验商家手机号是否已被用作商家帐号
        Long accountNum = sellerAccountService.checkAccount(phoneId);
        // 密码默认为手机号后六位
        String pwd = phoneId.substring(phoneId.length() - 6, phoneId.length());
        /**
         * 如果已被使用则商家帐号为：商家编号+商家手机号后六位,否则直接商家手机号作为商家帐号
         */
        String ac = phoneId;
//        if (accountNum > SellerConstants.COMMON_PARAM_Z) {	//新版SAAS端支持同一账号选择登陆多个家户
//            ac = sellerid + pwd;
//        }
        sellerAccount.setAccount(ac);
        sellerAccount.setPassword(NMD5.Encode(pwd));
        sellerAccount.setPhone(phoneId);
        this.log.debug("添加帐号 参数：account=" + ac);
        // 更新状态的同时添加一条默认的账号
        sellerAccountDao.add(sellerAccount);
        persistentSeller.setPassword(pwd);
        persistentSeller.setAccount(ac);
        // throw new ApplicationException("aaaaaaaaa");
    }

    /**
     * 根据商家编号查询商家与关联的其他信息.
     */
    public void findSellerInfo(Integer sellerid, ModelAndView model) {
        try {
            Assert.notNull(sellerid, "修改商家时，编号不能为空！");
            TSeller seller = getObject(sellerid.longValue());
            Assert.notNull(seller, "编号为：" + sellerid + "的商家不存在！！");
            /* 获取商家行业信息 */
            precessSellerInfoForView(seller);
            //2016年3月30日 Edit By ChenBo
            //seller.setTraderRefs(sellerDao.getTraderRefsBySellerId(sellerid));
            /* 根据商家id查询商家详细信息 */
            TSellerDetailed sellerDetailed = sellerDetailedService
                    .getObject(sellerid.longValue());

            if (null != seller && seller.getFlatAgio() != null) {
                seller.setFlatAgio(getDoubleMultiply100Value(seller.getFlatAgio()));
            }
            if (null != seller && seller.getAgioAgioNum() != null) {
                seller.setAgioAgioNum(getDoubleMultiply100Value(seller.getAgioAgioNum()));
            }
            /* 查询商家的直播分账设置信息 */
            TLiveSellerLedger liveLedger = sellerDao.getSellerLiveLeger(sellerid);
            if (liveLedger != null) {
                liveLedger.setLedgerRatio(getDoubleMultiply100Value(liveLedger.getLedgerRatio()));
                seller.setLiveLedger(liveLedger);
                seller.setLiveLedgerOperating(1);
            } else {
                seller.setLiveLedgerOperating(0);
            }
            
            /* 根据商家id查询商家经纬度信息 */
            TSellerLandmark sellerLandmark = sellerLandmarkService
                    .getSellerLandmarkByid(sellerid.longValue());

            this.log.debug("取得商家常规折扣->");
            TSellerAgio sellerAgio = sellerAgioDao.getUsingCommonAgion(sellerid
                    .longValue());
            this.log.debug("取得商家常规折扣<-");
            if (sellerAgio != null) {
                // 编辑初始化时给折扣赋值
                sellerAgio.setBaseagio(getDoubleMultiply100Value(sellerAgio
                        .getBaseagio()));
                sellerAgio.setIncome(getDoubleMultiply100Value(sellerAgio
                        .getIncome()));
                sellerAgio.setSledger(getDoubleMultiply100Value(sellerAgio
                        .getSledger()));
                sellerAgio.setYledger(getDoubleMultiply100Value(sellerAgio
                        .getYledger()));
                sellerAgio.setPledger(getDoubleMultiply100Value(sellerAgio
                        .getPledger()));
                sellerAgio.setFlatAgio(getDoubleMultiply100Value(sellerAgio
                        .getFlatAgio()));
            }
            TSellerPic logo = sellerPicDao.getSellerLogo(sellerid.longValue());
            if (null != logo) {
                seller.setUrl(logo.getPicurl());
            }

            // 商家图片
            TSellerPic pic = sellerPicDao.getSellerPic(sellerid.longValue());
            if (null != pic) {
                seller.setPicUrl(pic.getPicurl());
            }

            // 商家环境图片
            TSellerPic ts = new TSellerPic();
            ts.setSellerid(sellerid);
            ts.setIslogo(SellerConstants.PIC_TYPE_ENV);
            this.log.debug("取得商家环境图片->");
            List<TSellerPic> sellerPics = sellerPicService.getList(ts);
            this.log.debug("取得商家环境图片<-");
            /* 根据商家id查询联盟店信息 */
            TSeller allian = sellerDao.getAllianceRelation(sellerid.longValue());
            if (null != allian) {
                seller.setAllianceId(allian.getAllianceId());
            }
            ExtensionSet es = new ExtensionSet();
            es.setSellerId(sellerid);
            this.log.debug("取得推广设置信息->");
            List<ExtensionSet> extensionSetList = extensionSetDao.getList(es);
            this.log.debug("取得推广设置信息<-");
            Map<String, ExtensionSet> exMap = new HashMap<String, ExtensionSet>();
            for (ExtensionSet extension : extensionSetList) {
                exMap.put(getExtensionTypeString(extension.getExtensionType()),
                        extension);
            }
            SellerDetailedForm detailed = new SellerDetailedForm();
            detailed.setSellerDetailed(sellerDetailed);
            detailed.setExtensionSets(exMap);
            detailed.setSellerPics(sellerPics);
            /* model.addObject("sellerAccount", sellerAccount); */
            model.addObject("detailed", detailed);
            model.addObject("selleridList", seller);
            model.addObject("sellerLandmarkList", sellerLandmark);
            model.addObject("sellerAgio", sellerAgio);

            log.info(seller.getTraderRefs());
        } catch (Exception e) {
            this.log.info("查询商家信息失败。", e);
        }
    }

    /**
     * 处理修改查看商家时，从数据库中取得的数据转化为相关的格式（如：百分数显示）
     *
     * @param seller
     */
    private void precessSellerInfoForView(TSeller seller) {
        seller.setDebit(getDoubleMultiply100Value(seller.getDebit()));
    }

    /**
     * 取得推广设置信息的key用于jsp页面显示.
     *
     * @param type
     * @return
     */
    private String getExtensionTypeString(Integer type) {
        String result;
        switch (type) {// 1：摇一摇，2：订单推广，3：列表推广
            case 1:
                result = "yaoyiyao";// 摇一摇
                break;
            case 2:
                result = "orderPromotion";// 订单推广
                break;
            case 3:
                result = "listPromotion";// 列表推广
                break;
            default:
                result = null;
                break;
        }
        return result;
    }

    /**
     * 更新商家详细信息
     *
     * @param request
     * @return
     */
    @Transactional
    public Map<String, String> updateSellerDetailed(
            SellerDetailedForm sellerDetailedForm, HttpServletRequest request) {
        Map<String, String> emap = null;
        TSellerDetailed sellerDetailed = sellerDetailedForm.getSellerDetailed();
        Integer sellerid = sellerDetailed.getSellerid();
        TUser user = ResultUtil.getCurrentUser(request);
        try {
            this.log.debug("添加或修改商家详情-->");
            addOrUpdateSellerDetail(sellerDetailed, request);
            this.log.debug("添加或修改商家详情<--");

            this.log.debug("添加或修改商家推广信息->");
            emap = addOrUpdateSellerDetailExtensionSets(sellerDetailedForm,
                    user, sellerid);
            this.log.debug("添加或修改商家推广信息<-");

            this.log.debug("添加或修改商家环境图片-->");
            addOrUpdateSellerDetailSellerEvPic(
                    sellerDetailedForm.getSellerPics(), sellerid);
            this.log.debug("添加或修改商家环境图片<--");

            TSeller tSeller = getObject(sellerid.longValue());
            InsertOrUpdateMongo(sellerid);

            updateStatusWhenUpdateSeller(sellerid);

        } catch (HttpHostConnectException e) {
            e.printStackTrace();
        } catch (ConnectTimeoutException e) {
            e.printStackTrace();
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApplicationException("更新上商家异常！", e);
        }
        return emap;
    }

    /**
     * 添加或修改商家详情.
     *
     * @param sellerDetailed
     * @throws Exception
     * @throws ClientProtocolException
     * @throws SocketTimeoutException
     * @throws ConnectTimeoutException
     * @throws HttpHostConnectException
     */
    private void addOrUpdateSellerDetail(TSellerDetailed sellerDetailed,
                                         HttpServletRequest req) throws HttpHostConnectException,
            ConnectTimeoutException, SocketTimeoutException,
            ClientProtocolException, Exception {
        /* 更新商家详情时，当商家提现限额为空时，设置默认值 */
        if (sellerDetailed.getAmountLimit() == null) {
            sellerDetailed.setAmountLimit(50000D);
        }
        TSellerDetailed temp = sellerDetailedService.getObject(sellerDetailed
                .getSellerid().longValue());

        if (temp == null) {
            sellerDetailedService.add(sellerDetailed);
            /* 记录日志 */
            String[] loggerInfo = {"商家编号",
                    sellerDetailed.getSellerid().toString(), "添加商家详情", "添加"};
            fireLoginEvent(loggerInfo);
        } else {
            sellerDetailedService.update(sellerDetailed);
            /* 记录日志 */
            String[] loggerInfo = {"商家编号",
                    sellerDetailed.getSellerid().toString(), "更新商家详情", "更新"};
            fireLoginEvent(loggerInfo);
        }
        if (sellerDetailed.getIsPay() == 1) {
            this.log.debug("添加商家服务员用户");
            addWaiterRegister(req, sellerDetailed.getSellerid());// 增加服务员用户
        }

    }

    /**
     * 添加或修改推广设置信息.
     *
     * @param sellerDetailedForm
     * @param user
     * @return 返回id
     */
    private Map<String, String> addOrUpdateSellerDetailExtensionSets(
            SellerDetailedForm sellerDetailedForm, TUser user, Integer sellerid) {
        Map<String, String> emap = new HashMap<String, String>(7);
        int iemap = 0;
        for (String key : sellerDetailedForm.getExtensionSets().keySet()) {
            ExtensionSet extensionSet = sellerDetailedForm.getExtensionSets()
                    .get(key);
            if (extensionSet.getId() != null) {/* 修改 */
                extensionSet.setDateUpdated(new Date());
                extensionSet.setUpdator(user.getUsername());
                extensionSetDao.update(extensionSet);
                emap.put(String.valueOf(iemap),
                        String.valueOf(extensionSet.getId()));
            } else {/* 添加 */
                extensionSet.setSellerId(sellerid);
                extensionSet.setCreator(user.getUsername());
                extensionSet.setDateCreated(new Date());
                extensionSet.setDateUpdated(new Date());
                extensionSet.setUpdator(user.getUsername());
                extensionSetDao.addReturnId(extensionSet);
                emap.put(String.valueOf(iemap),
                        String.valueOf(extensionSet.getId()));
            }
            iemap++;
        }
        if (!emap.containsKey("4")) {// 填充占位作用
            emap.put("4", "");
        }
        return emap;
    }

    /**
     * 添加或修改商家环境图片.
     *
     * @param sellerPics
     * @param sellerId
     */
    @Transactional
    private void addOrUpdateSellerDetailSellerEvPic(
            List<TSellerPic> sellerPics, Integer sellerId) {
        if (sellerPics.size() > 0) {
            /* 取得存在的环境图片的 */
            StringBuilder picIds = new StringBuilder();
            for (TSellerPic sellerPic : sellerPics) {
                sellerPic.setSellerid(sellerId);
                sellerPic.setIslogo(SellerConstants.PIC_TYPE_ENV);
                Integer sellerPicId = sellerPic.getPicid();
                String picurl = sellerPic.getPicurl();
                if (sellerPicId != null) {/* 修改商家详情环境图片 */
                    TSellerPic sellerPicPersistent = sellerPicDao
                            .getObject(sellerPicId.longValue());
                    picIds.append(sellerPic.getPicid());
                    picIds.append(",");
                    if (!sellerPic.getPicurl().equals(
                            sellerPicPersistent.getPicurl())
                            || !sellerPic.getBewrite().equals(
                            sellerPicPersistent.getBewrite())) {
                        sellerPicDao.update(sellerPic);
                    }
                } else if (sellerPicId == null && null != picurl) {/* 添加商家详情环境图片 */
                    sellerPicDao.addReturnId(sellerPic);
                    picIds.append(sellerPic.getPicid());
                    picIds.append(",");
                }
            }

            if (picIds.length() > 0) {
                picIds.deleteCharAt(picIds.length() - 1);
                String existSellerPicIds = new String(picIds);
                TSellerPic picidsAndSellerId = new TSellerPic();
                picidsAndSellerId.setSellerid(sellerId);
                picidsAndSellerId.setPicids(existSellerPicIds);
                sellerPicDao.deleteEnviByPicidNotIn(picidsAndSellerId);
            }
        }
    }

    /**
     * 添加服务员
     */
    public void addWaiterRegister(HttpServletRequest req, Integer sellerid)
            throws HttpHostConnectException, ConnectTimeoutException,
            SocketTimeoutException, ClientProtocolException, Exception {
        PWaiterRegisterRequestAdd request = null;
        TSeller seller = new TSeller();
        seller.setSellerid(sellerid);
        TSellerAccount tSellerAccount = null;
        List<TSeller> tSellers = sellerDao.gettSellerAndSellerAccount(seller);
        if (tSellers.size() < 1) {
            return;
        }
        for (TSeller ts : tSellers) {
            if (ts.getUid() == null) {
                request = new PWaiterRegisterRequestAdd();
                request.setUname(ts.getAccount());// 用户登录名
                request.setNname(ts.getNname() == null ? "" : ts.getNname());// 用户昵称
                request.setPassword(ts.getPassword());// 用户密码
                request.setRegtype(String
                        .valueOf(SellerConstants.SELLER_REGTYPE));// 注册来源
                request.setRegcity(Integer.parseInt(ts.getCity()));// 注册时所在区域
                request.setRegarea(Integer.parseInt(ts.getArea()));
                request.setRegzoneid(ts.getZoneid());
                request.setPhone(ts.getPhone());
                PhpHttpPageable<PWaiterRegisterResponseAdd> response = waiterRegister(
                        req, request);
                if (response.getStatus()) {// 更新员工绑定的会员id
                    if (String.valueOf(response.getData()).matches("\\d+")) {// 返回如果是id
                        // 就更新
                        tSellerAccount = new TSellerAccount();
                        tSellerAccount.setAid(ts.getAid());
                        tSellerAccount.setUid(ts.getUid());
                        tSellerAccount.setOldpassword(ts.getPassword());
                        tSellerAccount.setPassword(ts.getPassword());
                        sellerAccountService.update(tSellerAccount);
                        String[] sellerDetailedInfo = {"商家服务员绑定的会员id",
                                tSellerAccount.getUid().toString(), "添加服务员用户",
                                "添加"};
                        sellerDetailedService
                                .fireLoginEvent(sellerDetailedInfo);
                    } else {// 更新
                        PWaiterRegisterResponseAdd pWaiterRegisterResponseAdd = JSON
                                .parseObject(
                                        String.valueOf(response.getData()),
                                        PWaiterRegisterResponseAdd.class);
                        tSellerAccount = new TSellerAccount();
                        tSellerAccount.setAid(ts.getAid());
                        tSellerAccount.setUid(pWaiterRegisterResponseAdd
                                .getUid());
                        tSellerAccount.setOldpassword(ts.getPassword());
                        tSellerAccount.setPassword(ts.getPassword());
                        sellerAccountService.update(tSellerAccount);
                    }

                }
            }
        }

    }

    /**
     * 添加服务员用户
     */
    public PhpHttpPageable<PWaiterRegisterResponseAdd> waiterRegister(
            HttpServletRequest req, PWaiterRegisterRequestAdd request)
            throws HttpHostConnectException, ConnectTimeoutException,
            SocketTimeoutException, ClientProtocolException, Exception {
        String ip = StringUtils.getIpAddr(req);
        request.setIp(ip);
        // md5加密并且前六位和后六位互换
        String pw = request.getPassword();
        String md5BeforeSix = pw.substring(0, 6);
        String md5InSix = pw.substring(6, 26);
        String md5AfterSix = pw.substring(26, 32);
        String md5Nmber = md5AfterSix + md5InSix + md5BeforeSix;
        request.setPassword(md5Nmber);
        PhpHttpPageable<PWaiterRegisterResponseAdd> response = handel(
                addwaiterRegister, request);
        setPageInfo(response, request);
        return response;
    }

    /**
     * 请求
     *
     * @param requestUrl
     * @param request
     * @return
     * @throws HttpHostConnectException
     * @throws ConnectTimeoutException
     * @throws SocketTimeoutException
     * @throws ClientProtocolException
     * @throws Exception
     */
    private PhpHttpPageable<PWaiterRegisterResponseAdd> handel(
            String requestUrl, Object request) throws HttpHostConnectException,
            ConnectTimeoutException, SocketTimeoutException,
            ClientProtocolException, Exception {
        return HttpUtil.getInstance().phpPost(url + "/" + requestUrl, request);

    }

    /**
     * 分页信息设置
     *
     * @param response
     * @param request
     */
    private void setPageInfo(
            PhpHttpPageable<PWaiterRegisterResponseAdd> response,
            InterfacRequest request) {
        if (null != response && null != request) {
            response.setPage(request.getPage());
            response.setPageSize(request.getPageSize());
        }
    }

    /**
     * 更新商家经纬度.
     *
     * @param sellerLandmark
     * @return
     */
    @Transactional
    public int updateSellerLandmark(TSellerLandmark sellerLandmark) {
        //设置经纬度的sellerType为0，代表商家
        sellerLandmark.setSellerType(0);
        int num = 0;
        try {
            bargainProductService.updateSellerLandmark(sellerLandmark);
            TSellerLandmark sellerId = new TSellerLandmark();
            sellerId.setSellerid(sellerLandmark.getSellerid());
            Long count = sellerLandmarkService.count(sellerId);
            if (count > 0) {
                if (sellerLandmark.getLid() != null || sellerLandmark.getSellerid() != null) {
                    num = sellerLandmarkService.update(sellerLandmark);
                }
                if (sellerLandmark.getLid() != null) {
                    num = sellerLandmarkService.update(sellerLandmark);
                    String[] sellerLandmarkInfo = {"商家编号",
                            sellerLandmark.getSellerid().toString(), "更新商家经纬度",
                            "更新"};
                    sellerLandmarkService.fireLoginEvent(sellerLandmarkInfo);
                }
            } else {
                sellerLandmarkService.add(sellerLandmark);
                String[] sellerLandmarkInfo = {"商家编号",
                        sellerLandmark.getSellerid().toString(), "添加商家经纬度", "添加"};
                sellerLandmarkService.fireLoginEvent(sellerLandmarkInfo);
                num = 1;
            }
            //TSeller s = getObject(sellerLandmark.getSellerid().longValue());
            InsertOrUpdateMongo(sellerLandmark.getSellerid());
            updateStatusWhenUpdateSeller(sellerLandmark.getSellerid());
        } catch (Exception e) {
            this.log.error("靠！原来是更新商家经纬度出异常了", e);
            throw new ApplicationException("经纬度更新失败", e);
        }
        return num;
    }

    /**
     * 删除商家
     *
     * @param objects
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public int deleteSeller(Object[] objects) {
        int row = 0;
        if (null != objects && objects.length > 0) {
            // 删除商家表id
            row = delete(objects);

            // 删除详情
            sellerDetailedService.delete(objects);

            // 删除账号
            sellerAccountDao.deleteAccountInfoBySellerid(objects);

            // 删除商家申请信息
            sellerApplyDao.deleteApplyBySellerid(objects);
        }
        return row;
    }

    /**
     * 获取分店列表信息
     *
     * @param seller
     * @return
     */
    public Pageable<TSeller> getSplitShopList(TSeller seller) {
        Pageable<TSeller> sellerInfoList = new Pageable<TSeller>(seller);
        // 商家分店列表
        sellerInfoList.setContent(sellerDao.getsplitShopList(seller));
        // 总条数
        sellerInfoList.setTotal(sellerDao.splitShopCount(seller));
        return sellerInfoList;
    }

    /**
     * 根据商户区域id查询对应的合作商信息
     */
    public TJoint getJointInfoByArea(String area) {
        List<TJoint> joints = jointDao.getJointInfoByArea(area);
        if (joints != null && joints.size() == 1) {
            return joints.get(0);
        }
        return null;
    }

    /**
     * 更新商家申账号信息 针对添加单个帐号
     *
     * @param infoList
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public int addAndUpdateAccount(TSellerAccount[] infoList) {
        int row = 0;
        if (null != infoList) {
            // 默认将商家手机号作为商家帐号手机号
            if (null != infoList[0].getSellerid()) {
                String phoneid = sellerDao.getObject(
                        infoList[0].getSellerid().longValue()).getPhoneid();
                infoList[0].setPhone(phoneid);
            }
            // 如果帐号id不为null则表示添加
            if (null != infoList[0].getAid()) {
                if (null != infoList[0].getPassword()) {
                    // 编辑初始化时候默认显示000000，如果前台穿过来的为此值则不不更新密码
                    if (infoList[0].getOldpassword().equals(
                            infoList[0].getPassword())
                            || infoList[0].getPassword().equals("000000")) {
                        infoList[0].setPassword(null);
                    } else {
                        infoList[0].setPassword(NMD5.Encode(infoList[0]
                                .getPassword()));// md5加密
                    }
                }
                sellerAccountDao.update(infoList[0]);

                String[] sellerAccountInfo = {"商家编号",
                        infoList[0].getSellerid().toString(), "更新帐号", "更新"};
                fireLoginEvent(sellerAccountInfo);
                row++;
            } else {
                infoList[0].setPassword(NMD5.Encode(infoList[0].getPassword()));// md5加密
                infoList[0].setType(1);// 账号类型1=商家管理账号|2=收银员账号|3=普通店员账号
                infoList[0].setSdate(new Date());
                sellerAccountDao.add(infoList[0]);
                String[] sellerAccountInfo = {"商家编号",
                        infoList[0].getSellerid().toString(), "添加帐号", "添加"};
                fireLoginEvent(sellerAccountInfo);
            }
        }
        return row;
    }

    /**
     * 添加或者更新商家信息(优化)
     *
     * @param request
     * @param sellerForm
     */
    @Transactional
    public Map<String, Object> addAndUpdateSellerOptimize(TSeller sellerForm, HttpServletRequest request) {
        Map<String, Object> resultMap;
        String operator = null;
        try {
            if (isAdd(sellerForm)) {
                resultMap = addSeller(sellerForm, request);
                operator = "添加";
            } else {
                resultMap = updateSeller(sellerForm, request);
                operator = "修改";
            }

        } catch (Exception e) {
            throw new ApplicationException(operator + "商家", e,
                    new Object[]{sellerForm}, new String[]{"商家",
                    sellerForm.getSellername(), operator + "商家",
                    operator});
        }
        return resultMap;
    }

    public boolean isAdd(TSeller sellerForm) {
        boolean isAdd = false;
        if (null == sellerForm.getSellerid()) {
            isAdd = true;
        }
        return isAdd;
    }

    /**
     * 添加商家.
     *
     * @param sellerForm
     * @param request
     * @return
     */
    private Map<String, Object> addSeller(TSeller sellerForm, HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Date date = new Date();
        /* 添加商家 */
        TSeller addSellerModel = getSellerAddModel(sellerForm, date);
        Long sellerid = addSellerReturnId(addSellerModel).longValue();
        /* 添加商家折扣 */
        sellerAgioService.add(getSellerCommonAgioModel(sellerForm, date,
                sellerid));
        /* 添加商家logo */
        sellerPicService.add(getSellerLogoAddOrUpdate(sellerForm, date,
                sellerid));
        /* 添加商家图片 */
        sellerPicService
                .add(getSellerPicAddOrUpdate(sellerForm, date, sellerid));
        /* 添加商家联盟店 */
        AllianceRelation allianceRelation = getAllianceRelationAddOrUpdatemodel(
                sellerForm, sellerid);
        if (null != allianceRelation) {
            sellerDao.addAllianceRelation(allianceRelation);
        }

        if (sellerForm.getLiveLedgerOperating() == 1) {//是否设置直播分账
            sellerForm.getLiveLedger().setSellerid(addSellerModel.getSellerid());
            sellerForm.getLiveLedger().setLedgerRatio(getDoubleDivide100Value(sellerForm.getLiveLedger().getLedgerRatio()));
            updateOrAddSellerLiveLedger(sellerForm.getLiveLedger());
        }
        /* 添加商家行业 */
        //2016年3月30日 Edit By ChenBo
        //addTSellerTraderRef(sellerForm, Integer.valueOf(sellerid.toString()), request);
        
        /* 记录日志 */
        String[] loggerInfo = {"商家名称", sellerForm.getSellername(), "新增"};
        fireLoginEvent(loggerInfo);
        resultMap.put("sellerid", sellerid);
        resultMap.put("liveLedgerId", sellerForm.getLiveLedger().getId());
        return resultMap;
    }

    /**
     * 商家添加行业
     *
     * @param sellerForm
     * @param sellerid
     * @param request
     */
    private void addTSellerTraderRef(TSeller sellerForm, Integer sellerid, HttpServletRequest request) {
        List<TSellerTraderRef> list = sellerForm.getTraderRefs();
        if (!list.isEmpty()) {
            Date date = new Date();
            String userName = ResultUtil.getCurrentUser(request).getUsername();
            TSellerTraderRef tSellerTraderRef = new TSellerTraderRef();
            tSellerTraderRef.setSellerid(sellerid);
            tSellerTraderRef.setCreator(userName);
            tSellerTraderRef.setDateCreated(date);
            tSellerTraderRef.setUpdator(userName);
            tSellerTraderRef.setDateUpdated(date);
            int size = list.size();
            for (int i = 0; i < size; i++) {
                TSellerTraderRef sellerTraderRef = list.get(i);
                String category = sellerTraderRef.getCategory();
                String genre = sellerTraderRef.getGenre();
                if (null != category
                        && !category.trim().equals("")
                        && null != genre
                        && !genre.trim().equals("")) {
                    tSellerTraderRef.setCategory(category);
                    tSellerTraderRef.setCategoryName(tradeService.getObject(Long.valueOf(category)).getTradename());
                    tSellerTraderRef.setGenre(genre);
                    tSellerTraderRef.setGenreName(tradeService.getObject(Long.valueOf(genre)).getTradename());

                    sellerDao.addTSellerTraderRef(tSellerTraderRef);
                }
            }
        }
    }

    /**
     * 更新商家行业
     *
     * @param sellerForm
     * @param sellerid
     * @param request
     */
    private void updateTSellerTraderRef(TSeller sellerForm, Integer sellerid, HttpServletRequest request) {
        //先删除该商家对应的所以区域，然后进行添加
        sellerDao.deleteTSellerTraderRefBySellerid(sellerid);
        addTSellerTraderRef(sellerForm, sellerid, request);
    }

    /**
     * 添加商家并返回商家自动增长的id.
     *
     * @param seller
     * @return
     */
    @Transactional
    public Integer addSellerReturnId(TSeller seller) {
        this.log.debug("添加一个商家时，会提交那些数据，SQL如下：");
        addReturnId(seller);
        this.log.debug("添加商家结束。");
        return seller.getSellerid();
    }

    /**
     * 修改商家.
     *
     * @param sellerForm
     * @param request
     * @return
     */
    private Map<String, Object> updateSeller(TSeller sellerForm, HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Date date = new Date();
        Long sellerid = sellerForm.getSellerid().longValue();
        /* 更新商家信息 */
        update(getSellerUpdateModel(sellerForm, date));
        /* 是否需要更新商家的状态 */
        updateStatusWhenUpdateSeller(sellerForm.getSellerid());
        /* 更新折扣 */
        updateSellerAgio(sellerForm, sellerid);

        /* 更新logo */
        updateSellerLogo(sellerForm, sellerid, date);

        /* 更新图片 */
        updateSellerPic(sellerForm, sellerid, date);

        /* 更新或添加关联的联盟商 */
        addOrUpdateSellerAllianceRelation(sellerForm, sellerid, date);

        TSeller s = getObject(sellerForm.getSellerid().longValue());
        InsertOrUpdateMongo(sellerForm.getSellerid());
        /* 更新商家行业 */
        log.info(sellerForm.getTraderRefs());

        if (sellerForm.getLiveLedgerOperating() == 1) {//是否设置直播分账
            sellerForm.getLiveLedger().setSellerid(sellerForm.getSellerid());
            sellerForm.getLiveLedger().setLedgerRatio(getDoubleDivide100Value(sellerForm.getLiveLedger().getLedgerRatio()));
            updateOrAddSellerLiveLedger(sellerForm.getLiveLedger());
        } else {
            sellerDao.deleteSellerLiveLedger(sellerForm.getSellerid());
            if (sellerForm.getLiveLedger() != null) {
                sellerForm.getLiveLedger().setId(null);
            }
        }

        //2016年3月30日 Edit By ChenBo
        //updateTSellerTraderRef(sellerForm, Integer.valueOf(sellerid.toString()), request);
    
        /* 记录日志 */
        String[] loggerInfo = {"商家编号", sellerForm.getSellerid().toString(),
                "商家更新", "更新"};
        fireLoginEvent(loggerInfo);
        resultMap.put("sellerid", sellerid);
        resultMap.put("liveLedgerId", sellerForm.getLiveLedger().getId());
        return resultMap;
    }

    /**
     * 修改商家常规折扣.
     *
     * @param sellerForm
     * @param sellerid
     */
    private void updateSellerAgio(TSeller sellerForm, Long sellerid) {
        Date date = new Date();
        TSellerAgio sellerAgio = getSellerCommonAgioModel(sellerForm, date,
                sellerid.longValue());
        TSellerAgio usingCommonSellerAgio = sellerAgioService
                .getUsingCommonAgion(sellerid.longValue());
        if (usingCommonSellerAgio != null) {
            /* 折扣未改变时，不更新折扣 */
            if (sellerAgio.getBaseagio().doubleValue() != usingCommonSellerAgio.getBaseagio().doubleValue() ||
                    usingCommonSellerAgio.getIncome() != sellerAgio.getIncome() ||
                    usingCommonSellerAgio.getSledger() != sellerAgio.getSledger() ||
                    usingCommonSellerAgio.getYledger() != sellerAgio.getYledger() ||
                    usingCommonSellerAgio.getPledger() != sellerAgio.getPledger()) {
                sellerAgio.setAid(usingCommonSellerAgio.getAid());
                sellerAgio.setSdate(date);
                sellerAgioService.update(sellerAgio);
                this.insertAgioRecord(sellerAgio);
            }
        } else {
            sellerAgioService.add(sellerAgio);
        }
    }

    public void insertAgioRecord(TSellerAgio sellerAgio) {
        TAgioRecord tAgioRecord = new TAgioRecord();
        this.puttAgioRecordParams(tAgioRecord, sellerAgio);// 参数装载
        agioRecordDao.add(tAgioRecord);
    }

    public void puttAgioRecordParams(TAgioRecord tAgioRecord,
                                     TSellerAgio sellerAgio) {
        tAgioRecord.setAid(sellerAgio.getAid());
        tAgioRecord.setSellerid(sellerAgio.getSellerid());
        tAgioRecord.setOperation(sellerAgio.getOperation());
        tAgioRecord.setUid(sellerAgio.getUid());
        tAgioRecord.setBaseagio(sellerAgio.getBaseagio());
        tAgioRecord.setIncome(sellerAgio.getIncome());
        tAgioRecord.setSledger(sellerAgio.getSledger());
        tAgioRecord.setYledger(sellerAgio.getYledger());
        tAgioRecord.setPledger(sellerAgio.getPledger());
        tAgioRecord.setStdate(sellerAgio.getStdate());
        tAgioRecord.setEndate(sellerAgio.getEndate());
        tAgioRecord.setSdate(sellerAgio.getSdate());
        tAgioRecord.setExcdate(sellerAgio.getExcdate());
        tAgioRecord.setRemarks(sellerAgio.getRemarks());
        tAgioRecord.setStatus(sellerAgio.getStatus());
        tAgioRecord.setType(sellerAgio.getType());
    }

    /**
     * 修改商家LOGO图片.
     *
     * @param sellerForm
     * @param sellerid
     */
    private void updateSellerLogo(TSeller sellerForm, Long sellerid,
                                  Date operatDate) {
        TSellerPic logoPersistent = sellerPicService.getSellerLogo(sellerid);
        if (logoPersistent != null) {
            /* 商家的logo图片改变了就更新，未改变就不更新 */
            if (!sellerForm.getUrl().equals(logoPersistent.getPicurl())) {
                logoPersistent.setPicurl(sellerForm.getUrl());
                logoPersistent.setSdate(operatDate);
                sellerPicService.update(logoPersistent);
            }
        } else {
            sellerPicService.add(getSellerLogoAddOrUpdate(sellerForm,
                    new Date(), sellerid));
        }
    }

    private void updateOrAddSellerLiveLedger(TLiveSellerLedger liveLedger) {
        if (liveLedger.getId() != null) {
            //update
            sellerDao.updateSellerLiveLedger(liveLedger);
        } else {
            //insert
            sellerDao.addSellerLiveLedger(liveLedger);
        }

    }

    /**
     * 修改商家图片.
     *
     * @param sellerForm
     * @param sellerid
     * @param operatDate
     */
    private void updateSellerPic(TSeller sellerForm, Long sellerid,
                                 Date operatDate) {
        TSellerPic picPersistent = sellerPicService.getSellerPic(sellerid);
        if (picPersistent != null) {
            /* 商家图片改变了就更新，未改变就不更新 */
            if (!sellerForm.getPicUrl().equals(picPersistent.getPicurl())) {
                picPersistent.setPicurl(sellerForm.getPicUrl());
                picPersistent.setSdate(operatDate);
                sellerPicService.update(picPersistent);
            }
        } else {
            sellerPicService.add(getSellerPicAddOrUpdate(sellerForm,
                    new Date(), sellerid));
        }
    }

    /**
     * 添加或修改商家联盟店.
     *
     * @param sellerForm
     * @param sellerid
     * @param operatDate
     */
    private void addOrUpdateSellerAllianceRelation(TSeller sellerForm,
                                                   Long sellerid, Date operatDate) {
        AllianceRelation allianceRelation = new AllianceRelation();
        allianceRelation.setSellerId(sellerid.longValue());
        TSeller allianceRelationPersistent = sellerDao
                .getAllianceRelation(sellerid.longValue());
        if (allianceRelationPersistent != null) {
            /* 商家的联盟商改变了就更新，未改变就不更新 */
            TSeller updateAllianceRelationModel = new TSeller();
            updateAllianceRelationModel.setSellerid(sellerid.intValue());
            updateAllianceRelationModel.setAllianceId(sellerForm
                    .getAllianceId());
            sellerDao.updateAllianceRelation(updateAllianceRelationModel);
        } else {
            allianceRelation = getAllianceRelationAddOrUpdatemodel(sellerForm,
                    sellerid);
            if (allianceRelation != null) {
                sellerDao.addAllianceRelation(allianceRelation);
            }
        }
    }

    private AllianceRelation getAllianceRelationAddOrUpdatemodel(
            TSeller sellerForm, Long sellerid) {
        AllianceRelation allianceRelation = null;
        if (null != sellerForm.getAllianceId()) {
            allianceRelation = new AllianceRelation();
            allianceRelation.setAllianceId(sellerForm.getAllianceId()
                    .longValue());
            allianceRelation.setSellerId(sellerid);
        }
        return allianceRelation;
    }

    /**
     * 取得商家图片的添加或修改信息.
     *
     * @param sellerForm
     * @param date
     * @return
     */
    public TSellerPic getSellerLogoAddOrUpdate(TSeller sellerForm, Date date,
                                               Long sellerid) {
        TSellerPic logo = new TSellerPic();
        logo.setIslogo(SellerConstants.PIC_TYPE_LOGO);
        logo.setPicurl(sellerForm.getUrl());
        logo.setSdate(date);
        logo.setSellerid(sellerid.intValue());
        return logo;
    }

    public TSellerPic getSellerPicAddOrUpdate(TSeller sellerForm, Date date,
                                              Long sellerid) {
        TSellerPic sellerPic = new TSellerPic();
        sellerPic.setIslogo(SellerConstants.PIC_TYPE_PIC);
        sellerPic.setPicurl(sellerForm.getPicUrl());
        sellerPic.setSdate(date);
        sellerPic.setSellerid(sellerid.intValue());
        return sellerPic;
    }

    /**
     * 包装处理添加商家时所需要的数据.
     *
     * @param sellerForm
     * @param date
     * @return
     */
    private TSeller getSellerAddModel(TSeller sellerForm, Date date) {
        TSeller addModel = new TSeller();
        BeanUtils.copyProperties(sellerForm, addModel);
        addModel.setAgioTime(date);
        addModel.setUdate(date);
        
        /* 平台所属商家，无需签约，直接进入待审核状态  */
        /*if(sellerForm.getUid()==null){
            addModel.setStatus(SellerConstants.SELLER_STATUS_AUDIT);
        }else{
            addModel.setStatus(SellerConstants.SELLER_STATUS_PROAPPROVE);
        }*/
        //添加商家，直接进入待审核状态，add by lifeng 20160729 18:57:20
        addModel.setStatus(SellerConstants.SELLER_STATUS_AUDIT);

        setAddSellerDataSource(addModel);
        setAddSellerDefaultInof(addModel);
//        setSellerTypeNameAndTradeName(addModel, Long.parseLong(sellerForm.getCategory()), Long.parseLong(sellerForm.getGenre()));
        setSellerFatherInfo(addModel, sellerForm.getFatherid());
        setSellerDoublePeropertyDivide100(addModel);
        setSellerYledger(addModel);
        setSellerSdate(addModel);
        //设置二级分类编号串
        setGenreIds(sellerForm.getTraderRefs(), addModel);
        return addModel;
    }

    /**
     * 设置二级分类编号串，APP专用需要同步到mongdb，各编号以“,”隔开
     *
     * @param traderRefs
     * @param seller
     */
    private void setGenreIds(List<TSellerTraderRef> traderRefs, TSeller seller) {
//        if(!traderRefs.isEmpty()){
//            int size = traderRefs.size();
//            StringBuilder builder = new StringBuilder();
//            for(int i = 0; i < size; i++){
//                builder.append(traderRefs.get(i).getGenre()).append(",");
//            }
//            String result = builder.toString();
//            seller.setGenreIds(result.substring(0, result.lastIndexOf(",")));
//        }
    }

    /**
     * 设置添加商家的数据来源,XMN各平台.
     *
     * @param seller
     */
    private void setAddSellerDataSource(TSeller seller) {
        seller.setDataSource(SystemConstants.PLATFORM_TYPE_XMNWEB);
    }

    /**
     * 包装处理更新商家时所需要的数据
     *
     * @param sellerForm
     * @param date
     * @return
     */
    private TSeller getSellerUpdateModel(TSeller sellerForm, Date date) {
        TSeller updateModel = new TSeller();
        BeanUtils.copyProperties(sellerForm, updateModel);
        updateModel.setAgioTime(date);
        updateModel.setUdate(date);
        //setSellerTypeNameAndTradeName(updateModel, Long.parseLong(sellerForm.getCategory()), Long.parseLong(sellerForm.getGenre()));
        setSellerFatherInfo(updateModel, sellerForm.getFatherid());
        setSellerDoublePeropertyDivide100(updateModel);
        setSellerYledger(updateModel);
        setSellerSdate(updateModel);
        //设置二级分类编号串
        setGenreIds(sellerForm.getTraderRefs(), updateModel);
        return updateModel;
    }

    /**
     * 设置添加商家时，所需要设置的默认信息.
     *
     * @param seller
     */
    private void setAddSellerDefaultInof(TSeller seller) {

        seller.setIsonline(SellerConstants.SELLER_NO_ISONLINE);
        seller.setDataSource(SellerConstants.SELLER_SOURCE);
        seller.setAgioStatus(SellerConstants.SELLER_AGIO_STATUS_OPEN);

        seller.setAgioType(SellerConstants.SELLER_AGIO_TYPE_COMMON);
    }

    /**
     * 设置商家的经营行业（包括一级行业和二级行业）.
     *
     * @param seller
     * @param category
     * @param genre
     */
    private void setSellerTypeNameAndTradeName(TSeller seller, Long category,
                                               Long genre) {
        if (category != null) {
            TTrade tradecategory = tradeService.getObject(category);
            if (tradecategory != null) {
                seller.setTypename(tradecategory.getTradename());
            }
        }
        if (genre != null) {
            TTrade tradecategory = tradeService.getObject(genre);
            if (tradecategory != null) {
                seller.setTradename(tradecategory.getTradename());
            }
        }
    }

    private void setSellerDoublePeropertyDivide100(TSeller seller) {
        seller.setBaseagio(getDoubleDivide100Value(seller.getBaseagio()));
        seller.setIncome(getDoubleDivide100Value(seller.getIncome()));
        seller.setSledger(getDoubleDivide100Value(seller.getSledger()));
        seller.setDebit(getDoubleDivide100Value(seller.getDebit()));
        seller.setFlatAgio(getDoubleDivide100Value(seller.getFlatAgio()));
        seller.setPledger(getDoubleDivide100Value(seller.getPledger()));
        seller.setRatio(getDoubleDivide100Value(seller.getRatio()));
        if (seller.getAgioAgioNum() != null) {
            seller.setAgioAgioNum(getDoubleDivide100Value(seller.getAgioAgioNum()));
        }
    }

    /**
     * 设置商家的总店信息（维护冗余字段：fatherid和fathername）.
     *
     * @param seller
     * @param fatherid
     */
    private void setSellerFatherInfo(TSeller seller, Integer fatherid) {
        if (fatherid == null) {
            seller.setFatherid(0);
            seller.setLssellername("");
        } else {
            this.log.debug("取得总店->");
            TSeller father = sellerDao.getObject(new Long(fatherid));
            if (null != father) {
                String lssellername = father.getSellername();
                seller.setLssellername(lssellername);
            }
        }
    }

    /**
     * 设置商家用户分账比例 (商家用户分账比例=折扣中用户分账占比+折扣中平台补贴占比
     * ，即：t_seller.yledger=t_seller_agio.yledger+t_seller_agio. flat_agio).
     *
     * @param seller
     */
    private void setSellerYledger(TSeller seller) {
        double sellerYledger = seller.getYledger() == null ? 0.0 : seller.getYledger()
                + (null == seller.getFlatAgio() ? 0.0 : seller.getFlatAgio());
        seller.setYledger(getDoubleDivide100Value(sellerYledger));
    }

    /**
     * 设置商家的营业时间.
     *
     * @param seller
     */
    private void setSellerSdate(TSeller seller) {
        seller.setSdate(seller.getSdate1() + "-" + seller.getSdate2());
    }

    /**
     * 修改商家基本信息、经纬度信息和详细信息时，是否需要修改商家状态.审核不通过时，修改以上信息需要修改商家状态为审核中
     *
     * @param sellerId
     */
    private void updateStatusWhenUpdateSeller(final int sellerId) {
        TSeller persistentSeller = getObject((long) sellerId);
        if (persistentSeller != null) {
            if (SellerConstants.SELLER_STATUS_DISAPPROVE == persistentSeller
                    .getStatus()) {
                this.log.debug("商家的状态为未通过时，修改商家信息要将商家状态改为审核中");
                TSeller seller = new TSeller();
                seller.setSellerid(sellerId);
                seller.setStatus(SellerConstants.SELLER_STATUS_AUDIT);
                update(seller);
            }
        }
    }

    /**
     * 获取商家常规折扣所需要的信息.
     *
     * @param sellerForm
     * @return
     */
    private TSellerAgio getSellerCommonAgioModel(TSeller sellerForm, Date date,
                                                 Long sellerid) {
        TSellerAgio sellerCommonAgio = new TSellerAgio();
        sellerCommonAgio.setSellerid(sellerid.intValue());
        sellerCommonAgio.setBaseagio(getDoubleDivide100Value(sellerForm
                .getBaseagio()));
        sellerCommonAgio.setIncome(getDoubleDivide100Value(sellerForm
                .getIncome()));
        sellerCommonAgio.setSledger(getDoubleDivide100Value(sellerForm
                .getSledger()));
        sellerCommonAgio.setYledger(getDoubleDivide100Value(sellerForm
                .getYledger()));
        sellerCommonAgio.setPledger(getDoubleDivide100Value(sellerForm
                .getPledger()));
        if (null != sellerForm.getRatio()) {
            sellerCommonAgio.setRatio(getDoubleDivide100Value(sellerForm
                    .getFlatAgio()));
        }
        if (null != sellerForm.getFlatAgio()) {
            sellerCommonAgio.setFlatAgio(getDoubleDivide100Value(sellerForm
                    .getFlatAgio()));
        }
        sellerCommonAgio.setType(SellerConstants.SELLER_AGIO_TYPE_COMMON);
        sellerCommonAgio.setStatus(SellerConstants.SELLER_AGIO_STATUS_OPEN);
        sellerCommonAgio.setOperation(SellerConstants.AGIO_RECORD_OPER_SY);
        return sellerCommonAgio;
    }

    /**
     * double除100
     *
     * @param value
     * @return
     */
    private Double getDoubleDivide100Value(Double value) {
        if (null != value) {
            return new BigDecimal(value).divide(new BigDecimal(100))
                    .doubleValue();
        }
        return 0.0;
    }

    /**
     * double乘100
     *
     * @param value
     * @return
     */
    private Double getDoubleMultiply100Value(Double value) {
        Double res = null;
        if (value == null) return null;
        try {
            res = new BigDecimal(Double.toString(value)).multiply(
                    new BigDecimal(100)).doubleValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 商家从表修改后，更新商家主表t_seller中的字段“修改时间（udate）
     *
     * @param seller
     */
    public void updateSellerDateTime(Integer sellerid) {
        TSeller seller = new TSeller();
        seller.setSellerid(sellerid);
        seller.setUdate(new Date());
        super.update(seller);
    }

    /**
     * 更新平台补贴
     *
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public int updateFlatAgio(TSeller seller) {
        int row = 0;
        if (null != seller && seller.getSellerid() != null) {
            seller.setUdate(new Date());
            // 添加平台补贴非空校验
            if (null != seller.getFlatAgio()) {
                seller.setFlatAgio(new BigDecimal(seller.getFlatAgio()
                        .toString()).divide(new BigDecimal(100)).doubleValue());
            } else {
                // 如果为空则默认为0.0
                seller.setFlatAgio(0.0);
            }
            if (null != seller.getSellerid()) {
                row = super.update(seller);
                String[] flatAgioInfo = {"商家编号",
                        seller.getSellerid().toString(), "更新平台补贴", "更新"};
                sellerPicService.fireLoginEvent(flatAgioInfo);
            }
        }
        return row;

    }

    /**
     * 设置商家的更新时间和操作时间为当前时间.
     *
     * @param seller
     */
    private void setSellerOpDateAndUDate(TSeller seller) {
        Date now = new Date();
        seller.setUdate(now);
        seller.setDateOperate(now);
    }

    /**
     * 批量上线
     *
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean beachOnLine(Object[] objects, TSeller seller) {
        boolean num = false;
        String name = null;
        if (null != objects) {
            seller.setArray(objects);
            try {
                setSellerOpDateAndUDate(seller);
                Integer count = sellerDao.beachOnLine(seller);
                num = (count != null && count > 0);
                // 0：未上线 1：已上线 2:预上线 3：已下线
                if (seller.getIsonline() == SellerConstants.SELLER_ONLINE) {
                    name = "上线";
                    for (Object obj : objects) {
                        InsertOrUpdateMongo(Integer.parseInt(((String) obj)), true);
                    }
                } else if (seller.getIsonline() == SellerConstants.SELLER_PREPARE_ONLINE) {
                    name = "预上线";
                    for (Object obj : objects) {
                        InsertOrUpdateMongo(Integer.parseInt(((String) obj)), true);
                    }
                } else if (seller.getIsonline() == SellerConstants.SELLER_NOT_ONLINE) {
                    name = "下线";
                    this.log.debug("下线商家时，直接将商家从MongDB中删除。。。");
                    for (Object obj : objects) {
                        Integer sellerId = Integer.parseInt(((String) obj));
                        deleteSellerFromMongo(sellerId);
                        dealSellerAnalysisInfo(sellerId, false);
                    }
                }
            } catch (Exception e) {
                log.error("商家" + name + "异常", e);
            } finally {
                String[] s = {"商家编号", seller.getIds(), name, name};
                fireLoginEvent(s, num ? 1 : 0);
            }
        }
        return num;
    }

    /**
     * 方法描述：批量修改商家   公开商户，参与分红，付费商家  状态
     * 创建人： chenJie <br/>
     * 创建时间：2016年12月29日上午11:06:09 <br/>
     *
     * @param seller
     */
    @Transactional
    public void statusOption(TSeller seller) {
        seller.setUdate(new Date());
        Object[] objects = StringUtils.paresToArray(seller.getIds(), ",");
        seller.setArray(objects);
        //批量修改商家状态
        Integer result = sellerDao.statusOption(seller);
        if (result <= 0) {
            log.error("批量修改商家状态失败" + seller.getIds());
            throw new RuntimeException();
        }
        //更新MongDB数据
        Map<String, Object> updateMap = new HashMap<>();
        updateMap.put("udate", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < objects.length; i++) {
            list.add(Integer.valueOf(objects[i].toString()));
        }
        Criteria criteria = Criteria.where("sellerid").in(list);

        //公开商户
        if (seller.getIsPublic() != null) {
            updateMap.put("is_public", seller.getIsPublic());
        }
        //付费商家
        if (seller.getIsPaid() != null) {
            updateMap.put("is_paid", seller.getIsPaid());
        }
        //参与分红
        if (seller.getJoinDividend() != null) {
            updateMap.put("join_dividend", seller.getJoinDividend());
        }
        log.info(seller.getIds() + "同步mongoDB数据");
        int updateOne = mongoBaseService.updateAll(collectionname, criteria, updateMap);
    }


    /**
     * 批量关联子店(清空原有的子店，添加新添加的子店)
     *
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public int batchRelationShop(TSeller seller) {
        int num = 0;
        if (null != seller && seller.getIds() != null) {
            Object[] objects = seller.getIds().split(",");
            if (null != objects) {
                seller.setArray(objects);
                setSellerOpDateAndUDate(seller);
                batchUpdateSellerDetailIsChain(seller, 1);
                sellerDao.deleteRelationShop(seller.getSellerid().longValue());//清空连锁店对应的所有子店信息
                num = sellerDao.beachRelationShop(seller);
            }
        }
        return num;
    }

    /**
     * 批量更新商家详情的isChain字段（包括原来的商家详情和现在的商家详情）
     *
     * @param sellerId
     * @param isChain
     */
    @Transactional
    private void batchUpdateSellerDetailIsChain(TSeller seller, Integer isChain) {
        List<TSeller> sellers = sellerDao.findSellerByFatherid(seller);
        StringBuilder sb = new StringBuilder();

        for (TSeller seller1 : sellers) {
            sb.append(seller1.getSellerid() + ",");
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
            TSellerDetailed sellerDetailedIsChain0 = new TSellerDetailed();
            sellerDetailedIsChain0.setSellerIds(new String(sb));
            sellerDetailedIsChain0.setIsChain(0);
            sellerDetailedDao.batchUpdateIsChain(sellerDetailedIsChain0);//将原有的商家改为0
        }

        TSellerDetailed sellerDetailed = new TSellerDetailed();
        sellerDetailed.setSellerIds(seller.getIds());
        sellerDetailed.setIsChain(isChain);
        this.log.info("修改商家是否是连锁店子店");
        sellerDetailedDao.batchUpdateIsChain(sellerDetailed);//将现在的商家改为1
    }

    public List<TSeller> getSellerMsg(TSeller seller) {
        return sellerDao.getSellerMsg(seller);
    }

    public Long getSellerMsgCount(TSeller seller) {
        return sellerDao.getSellerMsgCount(seller);
    }

    /**
     * 商家提现 根据手机号码查询指定商检编号
     *
     * @param PhoneId
     * @return
     */
    public String getPhoneIdBySellerId(String PhoneId) {
        return sellerDao.getPhoneIdBySellerId(PhoneId);
    }

    /**
     * @param seller
     * @return
     * @author zhangzhiwen
     */
    public void getSellerAndSellerAgio(TSeller seller, ModelAndView modelAndView) {
        seller.setArray(seller.getIds().split(","));
        List<TSeller> sellers = sellerDao.getSellerAndSellerAgio(seller);
        for (TSeller tempSeller : sellers) {
            tempSeller.setYledger(new BigDecimal(tempSeller.getYledger()
                    .toString()).multiply(new BigDecimal(100)).doubleValue());
            tempSeller.setFlatAgio(new BigDecimal(tempSeller.getFlatAgio()
                    .toString()).multiply(new BigDecimal(100)).doubleValue());
        }
        modelAndView.addObject("sellers", sellers);
    }

    /**
     * 批量更新商家设置常规平台补贴折扣
     *
     * @param seller
     */
    public void batchUpdateSellerDetailedFlatAgio(TSeller seller) {
        // StringBuffer ids = new StringBuffer();
        for (TSeller tempSeller : seller.getSellerList()) {
            try {
                // 将百分数转化为小数
                tempSeller.setFlatAgio(new BigDecimal(tempSeller.getFlatAgio()
                        .toString()).divide(new BigDecimal(100)).doubleValue());
                // 更新TSellerAgio
                TSellerAgio sellerAgio = sellerAgioDao
                        .getUsingCommonAgion(tempSeller.getSellerid()
                                .longValue());
                TSellerAgio tempSellerAgio = new TSellerAgio();
                tempSellerAgio.setAid(sellerAgio.getAid());
                tempSellerAgio.setFlatAgio(tempSeller.getFlatAgio());
                sellerAgioDao.update(tempSellerAgio);
                // 更新TSeller中的FlatAgio,使PHP可以使用
                sellerDao.update(tempSeller);
                String[] s = {"商家编号", tempSeller.getSellerid().toString(),
                        "设置平台补贴折扣为：" + tempSeller.getFlatAgio(), "设置平台补贴折扣"};
                this.fireLoginEvent(s, PartnerConstants.FIRELOGIN_NUMA);
            } catch (Exception e) {
                String[] s = {"商家编号", tempSeller.getSellerid().toString(),
                        "设置平台补贴折扣为：" + tempSeller.getFlatAgio(), "设置平台补贴折扣"};
                this.fireLoginEvent(s, PartnerConstants.FIRELOGIN_NUMB);
                throw e;
            }
        }
    }

    /**
     * @param sellerid
     * @return
     * @author dong'jietao
     * @date 2015年4月23日 下午2:28:14
     * @TODO 商家是否参与消费补贴或教育活动
     */
    public boolean getAllow(Integer sellerid) {// 商家是否参与消费补贴或教育活动
        boolean flag = true;
        TSellerMarketing sellerMarketing = new TSellerMarketing();
        sellerMarketing.setSellerid(sellerid);
        TActivity educationActivity = sellerMarketingService
                .getTActivity(SellerConstants.ACTIVITY_TYPE_J);// 教育活动
        sellerMarketing.setAid(educationActivity.getAid());
        List<TSellerMarketing> lTM = sellerMarketingService
                .getList(sellerMarketing);
        if (lTM.size() > SellerConstants.COMMON_PARAM_Z) {
            flag = false;
        }
        TActivity subsidyActivity = sellerMarketingService
                .getTActivity(SellerConstants.ACTIVITY_TYPE_X);// 消费补贴活动
        sellerMarketing.setAid(subsidyActivity.getAid());
        lTM = sellerMarketingService.getList(sellerMarketing);
        if (lTM.size() > SellerConstants.COMMON_PARAM_Z) {
            flag = false;
        }
        return flag;
    }

    /**
     * @param sellerid
     * @param newGread
     * @return
     * @author dong'jietao
     * @date 2015年4月23日 下午2:28:33
     * @TODO 商家等级升降判断逻辑
     */
    public boolean getGrade(Integer sellerid, Integer newGread) {// 商家等级升降判断逻辑
        TSellerDetailed sellerDetailed = new TSellerDetailed();
        sellerDetailed.setSellerid(sellerid);
        List<TSellerDetailed> lTDs = sellerDetailedService
                .getList(sellerDetailed);
        if (lTDs.size() == SellerConstants.COMMON_PARAM_Z) {
            return false;
        }
        Integer oldGread = lTDs.get(0).getSellerGrade();
        return newGread > oldGread;
    }

    /**
     * @param pageable
     * @param seller
     * @author dong'jietao
     * @date 2015年5月29日 下午4:09:50
     * @TODO 子店list数据组装
     */
    public void putSubShopPageable(Pageable<TSeller> pageable, TSeller seller) {
        pageable.setContent(sellerDao.getSellerList(seller));
        pageable.setTotal(sellerDao.sellerCount(seller));
    }


    /**
     * @param pageable
     * @param seller
     * @author
     * @date 2015年5月29日 下午4:09:50
     * @TODO 子店list数据组装
     */
    public void putSubShopPageableByAlliance(Pageable<TSeller> pageable, AllianceShop allianceShop) {
        pageable.setContent(sellerDao.getSellerListByAlliance(allianceShop));
        pageable.setTotal(sellerDao.getSellerListByAllianceCount(allianceShop));
    }


    public List<TSeller> getSelect(TSeller seller) {
        return sellerDao.getSelect(seller);
    }
    
    
    /*    *//**
     * 权限联动
     *
     * @param rid
     * @param auId
     * @return
     */
    /*
     * public String getLd(Long rid,Long auId){ return areaService.getLd(rid,
     * auId); }
     */

    /**
     * 商家是否锁定提现功能，并同步到mongdb中
     *
     * @param seller
     * @author zhang'zhiwen
     * @date 2015年8月3日 下午2:31:51
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public Map<String, String> updateSellerIsLock(TSeller seller) {
        Map<String, String> resultMap = null;
        try {
            this.update(seller);
            resultMap = updateSellerLock(seller);
            this.log.debug("商家是否锁定提现功能，并同步到mongdb中");
            Map<String, Object> mongoMap = new HashMap<String, Object>();
            mongoMap.put("islock", seller.getIsLock() + "");
            updateMongo(seller.getSellerid(), mongoMap);
            //InsertOrUpdateMongo(seller.getSellerid());
        } catch (Exception e) {
            throw new ApplicationException("商家更新锁定异常", e,
                    new Object[]{seller},
                    new String[]{"用户编号", seller.getSellerid().toString(),
                            "商家更新锁定", "商家更新锁定"});
        }
        return resultMap;
    }

    private Map<String, String> updateSellerLock(TSeller seller) {
        Map<String, String> resultMap = null;
        try {
            log.debug("商家更新锁定开始，用户ID：" + seller.getSellerid());
            SynthesizeService.Client client = (SynthesizeService.Client) (synthesizeServiceClient
                    .getClient());
            resultMap = client.setLockWallet(setParameter(seller));
            log.debug("商家更新锁定结束，返回值：" + resultMap.get("code") + "，描述："
                    + resultMap.get("msg"));
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApplicationException("商家更新锁定异常", e,
                    new Object[]{seller});
        } finally {
            synthesizeServiceClient.returnCon();
        }
        return resultMap;
    }

    private Map<String, String> getWalletInfo(int uId, int typeId) {
        Map<String, String> resultMap = null;
        try {
            log.debug("商家获取钱包信息开始，uId：" + uId + ",typeId:" + typeId);
            SynthesizeService.Client client = (SynthesizeService.Client) (synthesizeServiceClient
                    .getClient());
            resultMap = client.getWalletBalance(String.valueOf(uId), typeId);
            log.debug("商家获取钱包信息结束，返回值：" + resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApplicationException("商家获取钱包信息异常", e,
                    new Object[]{uId, typeId});
        } finally {
            synthesizeServiceClient.returnCon();
        }
        return resultMap;
    }

    private Map<String, String> getStatisticsWithdrawalsMoney(int uId, int userType) {
        Map<String, String> resultMap = null;
        try {
            log.debug("商家获取提现统计信息开始，uId：" + uId + ",userType:" + userType);
            SynthesizeService.Client client = (SynthesizeService.Client) (synthesizeServiceClient
                    .getClient());
            Map<String, String> map = new HashMap<String, String>();
            map.put("uId", String.valueOf(uId));
            map.put("userType", String.valueOf(userType));
            resultMap = client.statisticsWithdrawalsMoney(map);
            log.debug("商家获取提现统计结束，返回值：" + resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApplicationException("商家获取提现统计信息异常", e,
                    new Object[]{uId, userType});
        } finally {
            synthesizeServiceClient.returnCon();
        }
        return resultMap;
    }

    private Map<String, String> setParameter(TSeller seller) {
        Map<String, String> paramMap = new HashMap<>();
        String uId = String.valueOf(seller.getSellerid());
        paramMap.put("uId", uId);
        paramMap.put("userType", "2"); // 商家
        paramMap.put("phoneNumber", uId);
        if (SellerConstants.SELLER_IS_LOCK == seller.getIsLock()) { // 0:解锁
            paramMap.put("status", "1");
        } else { // 1:锁定
            paramMap.put("status", "2");
        }
        return paramMap;
    }

    /**
     * @param card
     * @return
     * @author dong'jietao
     * @date 2015年4月30日 下午2:13:36
     * @TODO 通过接口获取银行卡list
     */
    public Pageable<Card> getCardList(Card card) {
        Pageable<Card> sellerInfoList = new Pageable<Card>(card);
        List<Card> cardList = new ArrayList<Card>();
        MentionAccountService.Client client = (MentionAccountService.Client) (mentionAccountServiceClient
                .getClient());
        try {
            List<Map<String, String>> cardslist = client.getMentionAccount(
                    String.valueOf(card.sellerid), 2);
            for (Map m : cardslist) {
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
        sellerInfoList
                .setTotal(Long.parseLong(String.valueOf(cardList.size())));
        return sellerInfoList;
    }

    /**
     * @param map
     * @return
     * @author dong'jietao
     * @date 2015年4月30日 下午5:04:14
     * @TODO 从返回的Map中组装参数
     */
    public Card getCardFromMap(Map<String, String> map) {
        Card card = new Card();
        card.setSellerid(Integer.parseInt(map.get("uId")));
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

    /**
     * @param card
     * @return 银行卡增加执行方法
     * @author wangzhimin
     * @date 2015/08/25 10:55:30
     */
    public Resultable addCard(Card card) {
        Resultable resultable = new Resultable();
        Map<String, String> paraMap = new HashMap<String, String>();
        this.assembleParm(card);
        this.addCardParamMap(paraMap, card);
        MentionAccountService.Client client = (MentionAccountService.Client) (mentionAccountServiceClient
                .getClient());
        try {
            Map<String, String> resultMap = client.addMentionAccount(paraMap);
            resultable.setMsg(resultMap.get("msg"));
            resultable.setSuccess(String.valueOf(
                    SellerConstants.ADD_CARD_STATUS).equals(
                    resultMap.get("state")) ? true : false);
            this.log.debug("商家绑定银行卡返回信息: " + resultMap.get("msg"));
        } catch (Exception e) {
            this.log.debug("商家绑定银行卡失败！");
            throw new ApplicationException("商家绑定银行卡异常", e,
                    new Object[]{card}, new String[]{"商家",
                    card.getSellername(), "添加银行卡", "添加"});
        } finally {
            mentionAccountServiceClient.returnCon();
        }
        return resultable;
    }

    /**
     * @param areaId
     * @return
     * @author dong'jietao
     * @date 2015年5月8日 下午2:11:10
     * @TODO 根据区域id 获取区域名称
     */
    public TArea getAreaName(Integer areaId) {
        TArea area = new TArea();
        area.setAreaId(areaId);
        List<TArea> list = areaDao.getLdAll(area);
        if (list.size() > 0) {
            area = list.get(0);
        }
        return area;
    }

    /**
     * @param paraMap
     * @param card
     * @author dong'jietao
     * @date 2015年4月30日 下午4:37:44
     * @TODO 调用接口签参数组装
     */
    public void addCardParamMap(Map<String, String> paraMap, Card card) {
        paraMap.put("uId", String.valueOf(card.getSellerid()));
        if (!"null".equals(card.getId()) && card.getId() != null) {
            paraMap.put("id", String.valueOf(card.getId()));
        }
        paraMap.put("type", String.valueOf(2));
        paraMap.put("account", String.valueOf(card.getCardId()));
        paraMap.put("cardType", String.valueOf(card.getCardType()));
        paraMap.put("userName", String.valueOf(card.getCardUserName()));
        paraMap.put("bankName", String.valueOf(card.getBankName()));
        paraMap.put("mobileId", String.valueOf(card.getCardPhone()));
        paraMap.put("isuse", String.valueOf(card.getCardPurpose()));
        paraMap.put("userType", String.valueOf(2));// 用户类型 1用户 2商家 3合作商
        paraMap.put("ispublic", String.valueOf(card.getIspublic()));
        paraMap.put("idtype", String.valueOf(card.getIdtype()));
        paraMap.put("identity", String.valueOf(card.getIdentity()));
        paraMap.put("bank", String.valueOf(card.getBank()));
        paraMap.put("abbrev", String.valueOf(card.getAbbrev()));
        paraMap.put("province", String.valueOf(card.getProvince()));
        paraMap.put("cityname", String.valueOf(card.getCityname()));
    }

    /**
     * @param card
     * @return
     * @author dong'jietao
     * @date 2015年5月5日 上午10:49:15
     * @TODO 银行卡信息修改
     */
    public Card getUpdateByCard(Card card) {
        TArea tarea = new TArea();
        MentionAccountService.Client client = (MentionAccountService.Client) (mentionAccountServiceClient
                .getClient());
        Map<String, String> resultMap = null;
        try {
            resultMap = client.getMentionAccountById(String.valueOf(card
                    .getId()));
        } catch (FailureException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (TException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            mentionAccountServiceClient.returnCon();
        }
        if (resultMap.size() > 0) {
            card = getCardFromMap(resultMap);
        } else {
            return null;
        }
        tarea.setTitle(card.province);
        tarea = this.getAreaNameByName(tarea);
        card.setProvinceId(tarea.getAreaId());// 根据省名获取省id
        tarea = new TArea();
        tarea.setTitle(card.getCityname());
        tarea.setPid(card.getProvinceId());
        card.setCitynameId(this.getAreaNameByName(tarea).getAreaId());// 根据省id和区域名字
        // 获得该区域id
        return card;
    }

    /**
     * @param area
     * @return
     * @author dong'jietao
     * @date 2015年5月8日 下午2:11:10
     * @TODO 根据区域 获取区域名称
     */
    public TArea getAreaNameByName(TArea area) {
        List<TArea> list = areaDao.getLdAll(area);
        if (list.size() > 0) {
            area = list.get(0);
        }
        return area;
    }

    /**
     * @param card
     * @return 银行卡信息修改
     * @author wangzhimin
     * @date 2015/08/25 16:55:30
     */
    public Resultable updateCard(Card card) {
        Resultable resultable = new Resultable();
        Map<String, String> paraMap = new HashMap<String, String>();
        this.assembleParm(card);
        this.addCardParamMap(paraMap, card);
        MentionAccountService.Client client = (MentionAccountService.Client) (mentionAccountServiceClient
                .getClient());
        try {
            Map<String, String> resultMap = client
                    .updateMentionAccount(paraMap);
            resultable.setMsg(resultMap.get("msg"));
            resultable.setSuccess(String.valueOf(
                    SellerConstants.UPDATE_CARD_STATUS).equals(
                    resultMap.get("state")) ? true : false);
            this.log.debug("商家修改银行卡返回信息: " + resultMap.get("msg"));
        } catch (Exception e) {
            this.log.debug("商家修改银行卡失败！");
            throw new ApplicationException("商家修改银行卡异常", e,
                    new Object[]{card}, new String[]{"商家",
                    card.getSellername(), "修改银行卡", "修改"});
        } finally {
            mentionAccountServiceClient.returnCon();
        }
        return resultable;
    }

    /**
     * 根据省、市的ID获取具体省、市的名称
     *
     * @param card
     */
    public void assembleParm(Card card) {
        card.setProvince(this.getAreaName(card.getProvinceId()).getTitle());
        card.setCityname(this.getAreaName(card.getCitynameId()).getTitle());
        TBank tbank = new TBank();
        tbank.setAbbrev(card.getBank());
        card.setBank(tBankService.getTBank(tbank).getBankname());
    }

    /**
     * 商家审核通过、恢复合作，业务流程如下： <br>
     * <ol>
     * <li>是否设置商家等级 <br>
     * 否：审核不通过 <br>
     * 是：进行步骤2 <br>
     * <li>添加默认账号 <br>
     * 成功：进行步骤3 <br>
     * 失败：审核不通过 <br>
     * <li>为商家添加钱包 <br>
     * 成功：进行步骤4 <br>
     * 失败：审核不通过，进行步骤8 <br>
     * <li>更新行业总数和商圈总数 <br>
     * 成功：进行步骤5 <br>
     * 失败：审核不通过 <br>
     * <li>发送手机短信息 <br>
     * 成功：进行步骤6 <br>
     * 失败：审核不通过 <br>
     * <li>推送消息到APP账户 <br>
     * 成功：进行步骤7 <br>
     * 失败：审核不通过 <br>
     * <li>修改商家状态 <br>
     * 成功：进行步骤8 <br>
     * 失败：审核不通过 <br>
     * <li>通知分账服务进行分账 <br>
     * 成功：返回成功结果 <br>
     * 失败：返回失败结果 <br>
     * <li>返回结果 <br>
     * 成功结果 <br>
     * 失败结果 <br>
     * </ol>
     */
//    @Transactional
    public int sellerApproveOrResume(TSeller persistentSeller, Date updateDate)
            throws Exception {
        this.log.debug(SellerConstants.SELLER_STATUS_PAUSE == persistentSeller
                .getStatus() ? "商家恢复合作开始---->" : "商家审核通 过开始---->");

        Assert.notNull(persistentSeller.getPhoneid(), "商家手机号不能为空！");

        int num = 0;
        /** 记录日志信息. */
        Integer sellerId = persistentSeller.getSellerid();
        int status = SellerConstants.SELLER_STATUS_APPROVE;// 修改成的状态:已签约
        String[] loggerInfo = getLoggerInfoBySellerStatus(status, persistentSeller.getStatus(), sellerId);

        try {
            /* 修改商家状态 */
            num = updateSellerStatus(persistentSeller, updateDate, status);

            TSellerAccount sa = new TSellerAccount();
            sa.setSellerid(sellerId);
            Long countAccount = sellerAccountDao.count(sa);
            if (countAccount == 0) {
                /* 1、如果是审核通过，商家就会添加默认账号、钱包 ;2.如果是恢复合作，则不添加默认账号、钱包 . */
                // 审核通过
                if (SellerConstants.SELLER_STATUS_AUDIT == persistentSeller.getStatus()) {
                    addDefaultAccount(persistentSeller);// 添加万默认账号后取得商家的帐号和帐号密码，供发短信使用
                    addWallet(persistentSeller);
                    /* 发送短信 */
                    sellerCommonService.sendShortMessage(getSellerSendShortParam(persistentSeller, status));
                }

                /* 审核通过、恢复合作一级行业、级行业、商圈的个数加一 */
                updateCategoryCount(persistentSeller, status);

                /* 商家状态修改时推送消息到APP账户 */
                pushMessage(persistentSeller, status);
            } else {
                log.info("该商家【" + sellerId + "】已有商家钱包账号");
            }

            
            /* 成功后记录日志 */
            fireLoginEvent(loggerInfo, 1);
            this.log.debug(SellerConstants.SELLER_STATUS_PAUSE == persistentSeller
                    .getStatus() ? "商家恢复合作开始<----" : "商家审核通 过开始<----");
        } catch (ApplicationException e) {
            throw new ApplicationException("审核商家出现异常", e, new Object[]{
                    persistentSeller, updateDate}, loggerInfo);
        }
        return num;
    }

    /**
     * 商家审核未通过，业务流程如下：
     * <ol>
     * <li>推送短信
     * <li>更新商家状态
     * </ol>
     *
     * @param persistentSeller
     * @return
     * @author zhang'zhiwen
     */
    @Transactional
    public int sellerDisapprove(TSeller persistentSeller, Date updateDate) {
        this.log.debug("商家审核不通过--->");
        int status = SellerConstants.SELLER_STATUS_DISAPPROVE;
        int num = 0;
        Integer sellerId = persistentSeller.getSellerid();

        /** 记录日志信息. */
        String[] loggerInfo = getLoggerInfoBySellerStatus(status,
                persistentSeller.getStatus(), sellerId);
        try {
            /* 修改商家状态 */
            num = updateSellerStatus(persistentSeller, updateDate, status);

            /* 同步MongoDB */
            fireLoginEvent(loggerInfo, 1);
            /* 推送消息 */
            pushMessage(persistentSeller, status);
        } catch (ApplicationException e) {/* 推送消息失败异常 */
            throw new ApplicationException("商家审核不通过出现异常", e, new Object[]{
                    persistentSeller, updateDate}, loggerInfo);
            // throw e;
        }
        this.log.debug("商家审核不通过<-------");
        return num;
    }

    /**
     * 商家暂停合作，业务流程如下：
     * <ol>
     * <li>推送短信
     * <li>更新一级行业、二级行业、商圈的个数
     * <li>更新商家状态
     * </ol>
     *
     * @param persistentSeller
     * @return
     * @author zhang'zhiwen
     */
    @Transactional
    public int sellerPause(TSeller persistentSeller, Date updateDate) {
        this.log.debug("商家暂停合作------>");
        int num = 0;
        int status = SellerConstants.SELLER_STATUS_PAUSE;
        Integer sellerId = persistentSeller.getSellerid();
        /** 记录日志信息. */
        String[] loggerInfo = getLoggerInfoBySellerStatus(status,
                persistentSeller.getStatus(), sellerId);
        try {
            /* 修改商家状态 */
            num = updateSellerStatus(persistentSeller, updateDate, status);

            /* 更新一级行业、二级行业、商圈的个数 减一 */
            updateCategoryCount(persistentSeller,
                    SellerConstants.SELLER_STATUS_DISAPPROVE);

            // 将商家从MongoDB中删除
            deleteSellerFromMongo(sellerId);

            /* 推送消息 */
            pushMessage(persistentSeller, status);

            this.log.debug("商家暂停合作<-----");
            fireLoginEvent(loggerInfo, 1);

        } catch (Exception e) {
            /* 更新一级行业、二级行业、商圈的个数记录日志 ，到Controller中处理 */
            throw new ApplicationException("商家审核不通过异常", e, new Object[]{
                    persistentSeller, updateDate}, loggerInfo);
        }
        return num;
    }


    /**
     * 商家注销合作，业务流程如下：
     * <ol>
     * <li>推送短信
     * <li>更新一级行业、二级行业、商圈的个数
     * <li>归还寻蜜客SAAS库存
     * <li>清空商家MongoDB
     * <li>更新商家状态
     * </ol>
     *
     * @param persistentSeller
     * @return
     * @author ChenBo
     */
    @Transactional
    public int sellerRecover(TSeller persistentSeller, Date updateDate) {
        this.log.debug("商家注销合作------>");
        int num = 0;
        int status = SellerConstants.SELLER_STATUS_RECOVER;
        Integer sellerId = persistentSeller.getSellerid();
        /** 记录日志信息. */
        String[] loggerInfo = getLoggerInfoBySellerStatus(status,
                persistentSeller.getStatus(), sellerId);
        try {
            /* 修改商家状态 */
            num = updateSellerStatus(persistentSeller, updateDate, status);

            /* 更新一级行业、二级行业、商圈的个数 减一 */
            updateCategoryCount(persistentSeller,
                    SellerConstants.SELLER_STATUS_DISAPPROVE);
            //删除MongoDB数据
            deleteSellerFromMongo(sellerId);
                        
            /* 下发短信 */
            sellerCommonService.sendShortMessage(getRecoverSendSellerShortParam(
                    persistentSeller));

            this.log.debug("商家注销合作<-----");
            fireLoginEvent(loggerInfo, 1);

        } catch (Exception e) {
            /* 更新一级行业、二级行业、商圈的个数记录日志 ，到Controller中处理 */
            throw new ApplicationException("商家注销异常", e, new Object[]{
                    persistentSeller, updateDate}, loggerInfo);
        }
        return num;
    }


    /**
     * 修改商家状态
     *
     * @param persistentSeller
     * @param updateDate
     * @param status           修改成的状态
     * @return
     * @author zhang'zhiwen
     * @date 2015年8月28日 上午11:34:24
     */
    public int updateSellerStatus(TSeller persistentSeller, Date updateDate, int status) {
        TSeller record = new TSeller();
        record.setSellerid(persistentSeller.getSellerid());
        record.setUdate(updateDate);
        record.setStatus(status);

        // 恢复合作：修改上线状态为已上线
        if (persistentSeller.getStatus() == SellerConstants.SELLER_STATUS_PAUSE) {
            this.log.debug("商家恢复合作上线状态改为：已上线");
            record.setIsonline(SellerConstants.SELLER_ONLINE);
        }

        // 暂停合作：修改
        if (status == SellerConstants.SELLER_STATUS_PAUSE) {
            record.setIsonline(SellerConstants.SELLER_NOT_ONLINE);// 修改商家上线状态为：已下线
            record.setExamineinfo("商家暂停合作");
        }

        // 审核不通过
        if (status == SellerConstants.SELLER_STATUS_DISAPPROVE) {
            record.setExamineinfo(persistentSeller.getExamineinfo());
        }

        // 已签约 时间更新
        if (status == SellerConstants.SELLER_STATUS_APPROVE) {
            record.setSigndate(updateDate);
        }

        // 注销下架
        if (status == SellerConstants.SELLER_STATUS_RECOVER) {
            record.setIsonline(SellerConstants.SELLER_NOT_ONLINE);// 修改商家上线状态为：已下线
            record.setExamineinfo("已注销下架");
        }

        return sellerDao.update(record);
    }

    /**
     * 审核商家操作时，推送消息会调用
     *
     * @param persistentSeller
     * @param status           修改成的状态
     * @author zhang'zhiwen
     * @date 2015年8月28日 下午1:52:45
     * @see #getSellerApprovePushMessageParam
     */
    private void pushMessage(TSeller persistentSeller, int status) {
        if (isNeedPushMessage(persistentSeller)) {// 判断是否需要推送消息
            String title = null;
            String content = null;
            if (status == SellerConstants.SELLER_STATUS_APPROVE) {// 审核通过、恢复合作
                if (persistentSeller.getStatus() == SellerConstants.SELLER_STATUS_AUDIT) {// 审核通过
                    title = "商家名为：" + persistentSeller.getSellername()
                            + "的商家审核通过";
                    content = "商家名为：" + persistentSeller.getSellername()
                            + "的商家审核在 " + DateUtil.chinaSmartFormat(new Date())
                            + " 通过审核";
                }
                if (persistentSeller.getStatus() == SellerConstants.SELLER_STATUS_PAUSE) {// 恢复合作
                    title = "商家名为：" + persistentSeller.getSellername()
                            + "的商家已恢复合作";
                    content = "商家名为：" + persistentSeller.getSellername()
                            + "的商家在 " + DateUtil.chinaSmartFormat(new Date())
                            + " 恢复合作";
                }
            }
            if (status == SellerConstants.SELLER_STATUS_PAUSE) {// 暂停合作
                title = "商家名为：" + persistentSeller.getSellername() + "的商家已暂停合作";
                content = "商家名为：" + persistentSeller.getSellername() + "的商家在 "
                        + DateUtil.chinaSmartFormat(new Date()) + "已暂停合作";
            }

            if (status == SellerConstants.SELLER_STATUS_DISAPPROVE) {// 审核不通过
                title = "商家名为：" + persistentSeller.getSellername() + "的商家审核不通过";
                content = "商家名为："
                        + persistentSeller.getSellername()
                        + "的商家在"
                        + DateUtil.chinaSmartFormat(new Date())
                        + "审核不通过，"
                        + "不通过原因："
                        + (persistentSeller.getExamineinfo() == null ? ""
                        : persistentSeller.getExamineinfo());
            }
            pushMessageWhenStatusChange(persistentSeller.getSellerid(),
                    getSellerPushMessageParam(persistentSeller, title, content));
        } else {
            String[] s = new String[]{"商家编号",
                    persistentSeller.getSellerid().toString(), "推送消息", "推送消息",
                    "没有业务员"};
            fireLoginEvent(s, 0);
        }
    }

    /**
     * 商家审核操作时，判断商家是否需要推送消息。 根据商家是否有业务员来判断
     *
     * @param persistentSeller
     * @return
     * @author zhang'zhiwen
     * @date 2015年8月28日 上午10:54:15
     */
    private boolean isNeedPushMessage(TSeller persistentSeller) {
        return persistentSeller.getStaffid() != null ? true : false;
    }

    /**
     * 商家审核操作时添加钱包功能
     *
     * @param persistentSeller
     * @throws Exception
     * @author zhang'zhiwen
     * @date 2015年8月26日 下午4:57:42
     */
    private void addWallet(TSeller persistentSeller) throws Exception {
        Map<String, String> paramMap = new HashMap<String, String>();
        putParamsMap(paramMap, persistentSeller);
        Map<String, String> result;
        result = sellerCommonService.addWalletMap(paramMap);
        if (String.valueOf(SellerConstants.ADD_WALLET_RESPONSE_STATE_SUCCESS)
                .equals(result.get("state"))) {// 添加钱包成功
            this.log.debug("商家添加钱包成功！");
            this.log.debug("添加钱包返回描述：" + result.get("msg"));
        } else if (String.valueOf(
                SellerConstants.ADD_WALLET_RESPONSE_STATE_FAILURE).equals(
                result.get("state"))) {

        } else {
            this.log.debug("添加钱包返回描述：" + "失败原因：" + result.get("msg"));
            throw new ApplicationException(result.get("msg"));// 添加钱包失败，整个操作都结束
        }
    }

    /**
     * 商家审核操作封装推送消息的参数
     *
     * @param persistentSeller
     * @param title
     * @param content
     * @return
     * @author zhang'zhiwen
     * @date 2015年8月28日 下午2:35:28
     */
    private Map<String, String> getSellerPushMessageParam(
            TSeller persistentSeller, String title, String content) {
        String uid = null, action = null;

        StringBuffer uidbuffer = new StringBuffer();
        uidbuffer.append("[");
        uidbuffer
                .append("{\"uid\":\"")
                .append(persistentSeller.getStaffid())
                .append("\",\"iostoken\":\"")
                .append(persistentSeller.getIostoken() == null ? ""
                        : persistentSeller.getIostoken()).append("\"}");
        uidbuffer.append("]");
        uid = new String(uidbuffer);

        StringBuffer actionBuffer = new StringBuffer();
        actionBuffer.append("{\"activity\":1,\"shopid\":\"")
                .append(persistentSeller.getSellerid())
                .append("\",\"shopname\":\"")
                .append(persistentSeller.getSellername()).append("\"}");
        action = new String(actionBuffer);

        Map<String, String> param = new HashMap<String, String>();
        param.put("uid", uid);
        param.put("title", title);
        param.put("content", content);
        param.put("action", action);
        return param;
    }

    /**
     * 商家审核操作时，封装发送手机短信的参数
     *
     * @param persistentSeller
     * @return
     * @author zhang'zhiwen
     * @date 2015年8月28日 下午2:37:03
     */
    private Map<String, Object> getSellerSendShortParam(
            TSeller persistentSeller, final int status) {

        String text = null;
        if (persistentSeller.getStatus() == 1) {// 审核通过
            text = "您已正式成为寻蜜鸟商家，登录账号："
                    + persistentSeller.getAccount()
                    + "，密码："
                    + persistentSeller.getPassword()
                    + "。请尽快登录设置折扣及新密码，店铺信息越详细越能吸引客户哦！如您未安装寻蜜鸟商家版，点击http://dls.xmniao.com";
        }
        if (persistentSeller.getStatus() == 5) {// 恢复合作
            text = "您已恢复为寻蜜鸟商家，登录账号："
                    + persistentSeller.getAccount()
                    + "。请尽快登录设置折扣，店铺信息越详细越能吸引客户哦！如您未安装寻蜜鸟商家版，点击http://dls.xmniao.com";
        }

        List<String> phones = new ArrayList<String>();
        phones.add(persistentSeller.getPhoneid());
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("appid", "998899");
        param.put("text", text);
        param.put("phones", phones);
        return param;
    }

    /**
     * 商家注销下时，组装短信通知内容
     *
     * @param persistentSeller
     * @return
     * @author ChenBo
     * @date 2015年8月28日 下午2:37:03
     */
    private Map<String, Object> getRecoverSendSellerShortParam(
            TSeller persistentSeller) {

        String text = null;
        String phone = null;
        text = "您的【"
                + persistentSeller.getSellername()
                + "】的账户已注销，成功感谢您一直以来的支持，如有问题请致电客服：400-7766-333";
        phone = persistentSeller.getPhoneid();

        List<String> phones = new ArrayList<String>();
        phones.add(phone);
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("appid", "998899");
        param.put("text", text);
        param.put("phones", phones);
        return param;
    }

    /**
     * 商家注销下时，组装下发寻蜜客短信通知内容
     *
     * @param persistentSeller
     * @return
     * @author ChenBo
     * @date 2015年8月28日 下午2:37:03
     */
    private Map<String, Object> getRecoverSendXmerShortParam(
            TSeller persistentSeller) {

        String text = null;
        String phone = null;

        text = "您的签约商户【" + persistentSeller.getSellername() + "】的账户已注销成功并退还一套软件到您的“账户-我的-待上架”中，请注意查收，如有问题请致电客服：400-7766-333";


        Map<String, Object> userMap = (Map<String, Object>) this.getUserInfoForRedis("USERID_" + persistentSeller.getUid());
        phone = userMap == null ? "" : (userMap.get("phone") == null ? "" : String.valueOf(userMap.get("phone")));
        if (phone.equals("")) {
            log.error("REDIS中没找到用户【" + persistentSeller.getUid() + "】的信息。");
            BXmer xmer = xmerDao.getXmer(persistentSeller.getUid());
            phone = xmer.getPhoneid() == null ? "" : xmer.getPhoneid();
        }

        List<String> phones = new ArrayList<String>();
        phones.add(phone);
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("appid", "998899");
        param.put("text", text);
        param.put("phones", phones);
        return param;
    }

    private Object getUserInfoForRedis(String userKey) {
        if (redisTemplate.hasKey(userKey)) {
            return redisTemplate.opsForHash().entries(userKey);
        }
        return null;
    }

    /**
     * 获取商家日志信息.
     *
     * @param status
     * @param originalStatus
     * @param sellerId
     * @return
     */
    private String[] getLoggerInfoBySellerStatus(final int status,
                                                 final int originalStatus, final Integer sellerId) {
        String[] loggerinfo;
        switch (status) {
            case SellerConstants.SELLER_STATUS_DISAPPROVE:
                loggerinfo = new String[]{"商家编号", sellerId.toString(), "审核不通过", "审核不通过"};
                break;
            case SellerConstants.SELLER_STATUS_APPROVE:
                if (originalStatus == SellerConstants.SELLER_STATUS_PAUSE) {
                    /*
                     * 暂停->已签约
                     */
                    /** 恢复合作 */
                    loggerinfo = new String[]{"商家编号", sellerId.toString(), "商家恢复合作", "商家恢复合作"};
                } else {/* 审核中、未通过->已签约 */
                    /** 审核通过 */
                    loggerinfo = new String[]{"商家编号", sellerId.toString(), "商家审核通过", "商家审核通过"};
                }
                break;
            case SellerConstants.SELLER_STATUS_PAUSE:
                loggerinfo = new String[]{"商家编号", sellerId.toString(), "商家暂停合作", "商家暂停合作"};
                break;
            case SellerConstants.SELLER_STATUS_RECOVER:
                loggerinfo = new String[]{"商家编号", sellerId.toString(), "商家注销合作", "商家注销合作"};
                break;
            default:
                loggerinfo = null;
                break;
        }
        return loggerinfo;
    }

    /**
     * 重置提现密码
     *
     * @param seller
     * @return
     */
    public Map<String, String> resetDepositPassW(TSeller seller) {
        Map<String, String> resultMap = null;
        try {
            log.debug("重置提现密码开始，用户ID：" + seller.getSellerid() + " 用户类型："
                    + seller.getUserType());
            SynthesizeService.Client client = (SynthesizeService.Client) (synthesizeServiceClient
                    .getClient());
            resultMap = client.setWPwd(String.valueOf(seller.getSellerid()),
                    String.valueOf(seller.getUserType()), ""); // 密码默认重置为 ""
            // (空字符串)
            log.debug("重置提现密码结束，返回值：" + resultMap.get("state") + "，描述："
                    + resultMap.get("msg"));
        } catch (Exception e) {
            throw new ApplicationException("重置提现密码异常", e,
                    new Object[]{seller},
                    new String[]{"用户编号", seller.getSellerid().toString(),
                            "重置提现密码", "重置提现密码"});
        } finally {
            synthesizeServiceClient.returnCon();
        }
        return resultMap;
    }

    /**
     * @param seller
     */
    public Pageable<TSeller> getBrandSubSeller(TSeller seller) {
        Pageable<TSeller> brandSubSellers = new Pageable<TSeller>(seller);
        brandSubSellers.setContent(sellerDao.getBrandSubSeller(seller));
        brandSubSellers.setTotal(sellerDao.countOfBrandSubSeller(seller));
        return brandSubSellers;

    }

    /**
     * 为商家添加品牌店
     *
     * @param seller
     */
    public void addBrandSellerForSeller(TSeller seller) {
        if (null != seller.getSellerid()) {
            sellerDao.addBrandSeller(seller);
        }
    }

    /**
     * 为商家删除品牌店
     *
     * @param seller
     */
    public void deleteBrandSellerForSeller(TSeller seller) {
        if (null != seller.getSellerid()) {
            sellerDao.deleteBrandSeller(seller);
        }
    }
    
    /* 通知分账 */
    /*
     {   
        "orderId" : "151001000021",     //订单Id
        "sellerId":"886",    //消费商家Id
        "sellerName":"味千拉面"    //消费商家名称
        "xmerId ": "121",    //签约寻蜜客Id 。。
        "xmerName":"签约达人"    //签约寻蜜客名称 。。
        "xmerBackMoney": "360.00",      //签约寻蜜客押金返还金额（存入押金）
        "xmerMoney": "0.00",    //签约寻蜜客获得提成（存入佣金）
        "oneLevelXmerId ": "234",    //上线寻蜜客Id
        "oneLevelXmerName":"签约达人1"    //上线寻蜜客名称
        "oneLevelXmerMoney": "36.00",    //上线寻蜜客获得提成（存入佣金）
        "twoLevelXmerId  ": "3345",     //上上线寻蜜客Id
        "twoLevelXmerName":"签约达人1"    //上上线寻蜜客名称
        "twoLevelXmerMoney": "36.00",    //上上线寻蜜客获得提成（存入佣金）
        "payType":"1000000",    //支付类型
        "payCode":"123456789456123456789",    //支付流水号
        "money":"360.00",    //支付金额
        "payId":"16052716180000001",    //支付ID
        "payment":"360.00",    //第三方支付金额
        "sellerIndustry":"餐饮",    //商户所在行业 。。
        "sellerArea":"36",    //商户所在区域
        "sellerAreaName":"36区"    //商户所在区域名称 。。
    } 
     */

    /**
     *
     * @param tSaasSoldOrder  SAAS套餐销售订单
     * @param tSeller         数据库中查询出的商户信息
     * @throws Exception
     */
    public void notifyLedgerService(TSaasSoldOrder tSaasSoldOrder, TSeller tSeller) throws Exception {
        log.info("设置SAAS签约订单进行分账，tSaasSoldOrder:" + tSaasSoldOrder + ",tSeller:" + tSeller);

        RSaasSoldOrderLedger ssol = new RSaasSoldOrderLedger();
        int xmerType = 1;
        
        /* 获取寻蜜客等级信息 */
        String relationChain = saasOrderDao.selectOrderRelations(tSaasSoldOrder.getSaasOrdersn());
        HashMap<String, Object> issuesMap = new HashMap<>();
        issuesMap.put("uid",tSeller.getUid());
        if(org.apache.commons.lang3.StringUtils.isNotBlank(relationChain)){
            String[] uids = relationChain.split(",");
            if (uids.length > 1) { // 有上上级
                issuesMap.put("oneLevelXmerId",Integer.valueOf(uids[uids.length-2]));// 上上级
                issuesMap.put("twoLevelXmerId",Integer.valueOf(uids[uids.length-1]));// 上级
            }else if (uids.length > 0){ // 只有上级
                issuesMap.put("twoLevelXmerId",Integer.valueOf(uids[0]));// 上级
            }

        }

//        if (issuesMap == null) {
//            log.error("不得了啦，这个寻蜜客解约跑啦。。。");
//        }

        //saasPackageDao.getSaasPackageInfo(tSaasSoldOrder.get);
        /* 获取该商家订单所属寻蜜客套餐信息 */
        TSaasOrder so = new TSaasOrder();
        so.setOrdersn(tSaasSoldOrder.getSaasOrdersn());
        TSaasOrder saasOrder = saasOrderDao.getSaasOrder(so);

        boolean isOneLevelXmer = issuesMap == null ? false : (issuesMap.get("oneLevelXmerId") == null ? false : true);
        boolean isTwoLevelXmer = issuesMap == null ? false : (issuesMap.get("twoLevelXmerId") == null ? false : true);
        boolean isSellerArea = tSeller.getJointid() == null ? false : true;
        boolean isReturn = true;
        double discount = saasOrder.getAgio().doubleValue();
        boolean isAgentscope = false;    //是否卖光了签约套数
        double purchaseDiscount = 1;    //合作商进购价

        TJoint joint = null;
        if (isSellerArea) {
            isSellerArea = false;
            TJoint selJoint = new TJoint();
            selJoint.setJointid(tSeller.getJointid());
            joint = jointDao.getJointInfo(selJoint);
            if (joint != null && joint.getStatus() == 1) {
                isAgentscope = joint.getStocknum() == 0 ? false : true;
                purchaseDiscount = joint.getSaasagio() == null ? 1 : joint.getSaasagio();
                isSellerArea = true;
            }
        }


        double ledgerAmount = 0;
        /* 
         * 获取商家签约分账金额计算结果  
         * */
        ledgerAmount = tSaasSoldOrder.getAmount().doubleValue();

        ResponseSplitMap responseSplitMap = null;

        SplitService.Client splitService = (SplitService.Client) splitServiceClient.getClient();
        log.info("请求计算saas签约订单的分账参数：" + tSaasSoldOrder.getAmount() + "," + isReturn + "," + xmerType + "," + discount + isSellerArea + "," + isAgentscope + "," + purchaseDiscount + "," + isOneLevelXmer + "," + isTwoLevelXmer);


        String date = new SimpleDateFormat("yyyy-MM-dd").format(saasOrder.getZdate());
        if (date.compareTo("2016-09-06") > 0) {
            log.info("新折扣分账接口");
            responseSplitMap = splitService.saasLedger(ledgerAmount,
                    isReturn, xmerType, discount, isSellerArea, isAgentscope, purchaseDiscount, isOneLevelXmer, isTwoLevelXmer);
        } else {
            log.info("旧折扣分账接口");
            responseSplitMap = splitService.saasLedger2(ledgerAmount,
                    isReturn, xmerType, discount, isSellerArea, isAgentscope, purchaseDiscount, isOneLevelXmer, isTwoLevelXmer);
        }

        if (responseSplitMap.getCode() != 1) {
            log.error("调用分账算法接口失败：" + responseSplitMap.getMsg());
            return;
        }
        if (tSaasSoldOrder.getPayType().equals(10000000)) {
            log.info("寻蜜客赠送订单，该寻蜜客将不参与分账");
            responseSplitMap.getData().put("xmerMoney", 0.0);
            responseSplitMap.getData().put("xmerBackMoney", 0.0);
        }
        if (issuesMap == null) {
            log.info("已解约寻蜜客下的商家上线，该寻蜜客所分账金额将平台");
            double xmerAmount = responseSplitMap.getData().get("xmerMoney");
            double xmniaoMoney = responseSplitMap.getData().get("xmniaoMoney");
            double xmniaoNewMoney = xmerAmount + xmniaoMoney;
            responseSplitMap.getData().put("xmniaoMoney", xmniaoNewMoney);
            responseSplitMap.getData().put("xmerMoney", 0.0);
        }
        /* 获取商家经营信息 */
        String areaId = tSeller.getArea();
        Map<String, String> areaMap = sellerDao.getSellerArea(areaId);
        String areaText = areaMap.get("area");
        String categoryText = tSeller.getHyText();


        Map<String, Object> nameMap = new HashMap<String, Object>();

        log.info("issuesMap:" + issuesMap);
        if (issuesMap != null) {
            Set<String> keys = issuesMap.keySet();
            Iterator<String> it = keys.iterator();
            while (it.hasNext()) {
                String key1 = it.next();
                if (issuesMap.get(key1) != null) {
                    String name = xmerDao.getXmerName((int) issuesMap.get(key1));
                    nameMap.put(key1 + "Name", name);
                }
            }
            issuesMap.putAll(nameMap);
            System.out.println("寻蜜客信息：" + issuesMap);
        }
        /* 填充分账信息 */
        ssol.setPayType(getStringValue(tSaasSoldOrder.getPayType()));
        ssol.setPayId(tSaasSoldOrder.getPayId());
        ssol.setPayCode(tSaasSoldOrder.getPayCode());
        ssol.setOrderId(tSaasSoldOrder.getOrdersn());//DateUtil.getNow("yyyyMMddHHmmss")/*tSaasSoldOrder.getOrdersn()*/);
        ssol.setMoney(getStringValue(tSaasSoldOrder.getAmount()));
        ssol.setPayment(getStringValue(tSaasSoldOrder.getAmount()));
        ssol.setSellerId(getStringValue(tSaasSoldOrder.getSellerid()));
        ssol.setSellerName(tSaasSoldOrder.getSellername());

        ssol.setSellerArea(areaId);
        ssol.setSellerAreaName(areaText);
        ssol.setSellerIndustry(categoryText);
        ssol.setXmerId(getStringValue(tSeller.getUid()));
        ssol.setXmerName(getStringValue(issuesMap == null ? null : issuesMap.get("uidName")));
        ssol.setOneLevelXmerId(getStringValue(issuesMap == null ? null : issuesMap.get("oneLevelXmerId")));
        ssol.setOneLevelXmerName(getStringValue(issuesMap == null ? null : issuesMap.get("oneLevelXmerIdName")));
        ssol.setTwoLevelXmerId(getStringValue(issuesMap == null ? null : issuesMap.get("twoLevelXmerId")));
        ssol.setTwoLevelXmerName(getStringValue(issuesMap == null ? null : issuesMap.get("twoLevelXmerIdName")));
        ssol.setSellerAreaId(getStringValue(tSeller.getJointid()));
        ssol.setXmerType(getStringValue(xmerType));
        ssol.setCompanyXmerName(getStringValue(issuesMap == null ? null : issuesMap.get("uidName")));

        ssol.setXmerMoney(getStringValue(responseSplitMap.getData().get("xmerMoney")));
        ssol.setOneLevelXmerMoney(getStringValue(responseSplitMap.getData().get("oneLevelXmerMoney")));
        ssol.setTwoLevelXmerMoney(getStringValue(responseSplitMap.getData().get("twoLevelXmerMoney")));
        ssol.setXmerBackMoney(getStringValue(responseSplitMap.getData().get("xmerBackMoney")));
        ssol.setSellerAreaMoney(getStringValue(responseSplitMap.getData().get("sellerAreaMoney")));
        ssol.setCompanyXmerMoney(getStringValue(responseSplitMap.getData().get("companyXmerMoney") == null ? 0 : responseSplitMap.getData().get("companyXmerMoney")));
        ssol.setXmniaoMoney(getStringValue(responseSplitMap.getData().get("xmniaoMoney")));

        TSaasSoldOrder tsso = new TSaasSoldOrder();
        tsso.setCommission(JSON.toJSONString(responseSplitMap.getData()));
        tsso.setOrdersn(tSaasSoldOrder.getOrdersn());
        tsso.setHstatus(10);
        tsso.setUdate(new Date());
        saasSoldOrderDao.updateSaasSoldOrder(tsso);

        log.info("Saas签约订单分账队列：" + saasLedgerKey);
        log.info("Saas签约订单分账处理：" + JSON.toJSONString(ssol));
        redisTemplate.opsForList().leftPush(saasLedgerKey, JSON.toJSONString(ssol));

        //经销商saas库存-1
        if (isSellerArea && joint != null && joint.getStocknum() > 0) {
            TJoint mdyJoint = new TJoint();
            mdyJoint.setJointid(tSeller.getJointid());
            int mdyCount = jointDao.modifyJonitSaasInfo(mdyJoint);
            if (mdyCount == 0) {
                log.error("合作商的套餐已卖光了。。。");
            }
        }

        /**
         * 经销商saas库存-1后，新增1条 t_joint_saas_ledger记录
         * add by lifeng
         */
        TJointSaasLedger tJointSaasLedger = new TJointSaasLedger();
        tJointSaasLedger.setJointid(tSeller.getJointid());
        tJointSaasLedger.setSellerid(tSeller.getSellerid());
        tJointSaasLedger.setXmerUid(tSaasSoldOrder.getUid());
        tJointSaasLedger.setSaasBid(tSaasSoldOrder.getId() + "");
        if (ssol.getSellerAreaMoney() != null) {
            tJointSaasLedger.setCommison(new BigDecimal(ssol.getSellerAreaMoney()));
        }
        jointSaasLedgerService.addJointSaasLedger(tJointSaasLedger);//保存一条记录
        //
    }

    /**
     * 方法描述：返还SAAS软件重新签约，再次分账，金额设置全为0，即不再次分账<br/>
     * 创建人： ChenBo <br/>
     * 创建时间：2016年10月17日下午6:06:08 <br/>
     *
     * @param tSaasSoldOrder
     * @param tSeller
     * @throws Exception
     */
    public void notifyHasLedgeredService(TSaasSoldOrder tSaasSoldOrder, TSeller tSeller) throws Exception {
        log.info("更新SAAS签约订单为已分账(修改归还SAAS软件进行再次签约),tSaasSoldOrder:" + tSaasSoldOrder + ",tSeller:" + tSeller);


        //saasPackageDao.getSaasPackageInfo(tSaasSoldOrder.get);
        /* 获取该商家订单所属寻蜜客套餐信息 */
        TSaasOrder so = new TSaasOrder();
        so.setOrdersn(tSaasSoldOrder.getSaasOrdersn());
        TSaasOrder saasOrder = saasOrderDao.getSaasOrder(so);

        int xmerType = 1;
        boolean isOneLevelXmer = true;
        boolean isTwoLevelXmer = true;
        boolean isSellerArea = true;
        boolean isReturn = true;
        double discount = saasOrder.getAgio().doubleValue();
        boolean isAgentscope = false;    //是否卖光了签约套数
        double purchaseDiscount = 1;    //合作商进购价
        double ledgerAmount = 0;
        /* 
         * 获取商家签约分账金额计算结果  
         * */
        ledgerAmount = tSaasSoldOrder.getAmount().doubleValue();

        ResponseSplitMap responseSplitMap = null;

        try {
            SplitService.Client splitService = (SplitService.Client) splitServiceClient.getClient();
            responseSplitMap = splitService.saasLedger(ledgerAmount,
                    isReturn, xmerType, discount, isSellerArea, isAgentscope, purchaseDiscount, isOneLevelXmer, isTwoLevelXmer);
            if (responseSplitMap.getCode() != 1) {
                log.error("调用分账算法接口失败：" + responseSplitMap.getMsg());
                throw new Exception();
            } else {
                for (Map.Entry<String, Double> entry : responseSplitMap.getData().entrySet()) {
                    entry.setValue(0D);
                }
            }
        } catch (Exception e) {
            log.error("设置分账信息异常", e);
            responseSplitMap = new ResponseSplitMap();
            responseSplitMap.setData(new HashMap<String, Double>());
        }

        TSaasSoldOrder tsso = new TSaasSoldOrder();
        tsso.setCommission(JSON.toJSONString(responseSplitMap.getData()));
        tsso.setOrdersn(tSaasSoldOrder.getOrdersn());
        tsso.setHstatus(9);
        tsso.setUdate(new Date());
        saasSoldOrderDao.updateSaasSoldOrder(tsso);
    }

    private String getStringValue(Object obj) {
        if (obj != null) {
            return String.valueOf(obj);
        }
        return null;
    }
    /**
     * 调用支付服务，返还该笔签约订单的所需返还的押金
     */
//    public void notifyReturnDepositService(Integer uid){
//        //调用支付服务，返还寻蜜客押金
//        BXmer xmer = xmerDao.getXmer(uid);
//        if(xmer.getStock()>0){
//            
//              Map<String, String> res = null;
//              Map<String,String> paramMap = new HashMap<String,String>();
//              TSaasOrder so=new TSaasOrder();
//              so.setUid(uid);
//              TSaasOrder saasOrder = saasOrderDao.getSaasOrder(so);
//              BigDecimal singleAmount = div(saasOrder.getAmount(),new BigDecimal(saasOrder.getNums()),2);
//              paramMap.put("uid", String.valueOf(xmer.getUid()));
//              paramMap.put("bid", String.valueOf(saasOrder.getOrdersn()));
//              paramMap.put("amount", String.valueOf(singleAmount));
//              
//              try {
//                SynthesizeService.Client client = (SynthesizeService.Client)(synthesizeServiceClient.getClient());//支付服务
//                res = client.xmerReturnDeposit(paramMap);
//                if(res.get("sataus").equals("1")){
//                    log.info("返还失败");
//                }
//              }catch (TException e) {
//                this.log.error("返还寻蜜客押金接口调用：", e);
//                throw new ApplicationException("返还寻蜜客押金接口调用：",e,new Object[]{paramMap});
//              }finally {
//                  synthesizeServiceClient.returnCon();
//              }
//        }
//    }
//    
//    private BigDecimal div(BigDecimal b1,BigDecimal b2,int lenght){
//        return b1.divide(b2,lenght,BigDecimal.ROUND_HALF_UP);
//    }

    /**
     * @Title: ledgerAndReturn
     * @Description:分账及更新寻蜜客套餐销售信息
     */
    public void ledgerAndReturn(TSeller persistentSeller, TSeller seller) {
        log.info("ledgerAndReturn:" + persistentSeller);
        try {
            if (seller.getStatus() == SellerConstants.SELLER_STATUS_APPROVE
                    || seller.getStatus() == SellerConstants.SELLER_STATUS_DISAPPROVE
                    || seller.getStatus() == SellerConstants.SELLER_STATUS_RECOVER) {

                // 商户没有寻蜜客
                if (persistentSeller.getUid() == null) {
                    log.info("该商家无所属寻蜜客，无需进行分账！");
                    return;
                }

                // 查询商家签约SAAS订单
                TSaasSoldOrder sso = new TSaasSoldOrder();
                sso.setSellerid(persistentSeller.getSellerid());
                TSaasSoldOrder saasSoldOrder = saasSoldOrderDao.getSaasSoldOrder(sso);

                // 商户不是签约商户
                if (saasSoldOrder == null) {
                    log.error("找不到该商家对应的签约订单！");
                    return;
                }


                // 审核通过
                if (seller.getStatus() == SellerConstants.SELLER_STATUS_APPROVE) {//审核通过

                    //只要商家下单了，哪怕没付款，寻蜜客即会扣库存
                    if (saasSoldOrder.getStatus() != null && saasSoldOrder.getStatus() != 1) {
                        log.error("该签约订单【" + saasSoldOrder.getOrdersn() + "】状态不是‘已支付’(" + saasSoldOrder.getStatus() + ")！");
                        return;
                    }
                    if (saasSoldOrder.getHstatus() != null && saasSoldOrder.getHstatus() != 0) {
                        log.error("该签约订单【" + saasSoldOrder.getOrdersn() + "】已进行过分账处理");
                        return;
                    }


                    // 只对寻蜜客签约的店铺分账
                    if (persistentSeller.getSaasType() == SaasConstant.SELLER_SAAS_TYPE_XMER) {

                        switch (saasSoldOrder.getSaasSource()){
                            // SAAS来源 0 正常库存
                            case 0:
                                /* 通知分账服务对该签约订单进行分账 */
                                notifyLedgerService(saasSoldOrder, persistentSeller);
                                break;
                            // SAAS来源 1 销售退回库存
                            case 1:
                                /* 将该签约订单修改为已分账 */
                                notifyHasLedgeredService(saasSoldOrder, persistentSeller);
                                break;
                        }

                    }


                }
                // 审核不通过
                else if (seller.getStatus() == SellerConstants.SELLER_STATUS_DISAPPROVE) {//审核不通过
                    // 已分帐的SAAS不再进行退还SAAS
                    if (saasSoldOrder.getHstatus() != null && saasSoldOrder.getHstatus() != 0) {
                        log.error("该签约订单【" + saasSoldOrder.getOrdersn() + "】已进行过分账处理");
                        return;
                    }

                    // 2017-06-05 审核不通过 不退还SAAA套餐
                    /* 重新更新寻蜜客销售库存信息 */
//                    modifyXmerStore(saasSoldOrder, persistentSeller, 0);
                }
                // 注销合作
                else if (seller.getStatus() == SellerConstants.SELLER_STATUS_RECOVER) {//注销合作
                    
                    /* 重新更新寻蜜客库存信息 */
                    modifyXmerStore(saasSoldOrder, persistentSeller, 1);
                    sellerCommonService.sendShortMessage(getRecoverSendXmerShortParam(
                            persistentSeller));
                }
            }
        } catch (Exception e) {
            log.error("更新商家签约信息异常", e);
        }
    }

    /**
     * @Title: modifyXmerStore
     * @param destroy   0:审核不通过 更新寻蜜客信息   1:注销更新寻蜜客信息
     * @Description:更新寻蜜客库存信息
     */
    public void modifyXmerStore(TSaasSoldOrder tSaasSoldOrder, TSeller tSeller, int destroy) {
        if (destroy == 0) {

            log.info("商家【" + tSeller.getSellerid() + "-" + tSeller.getSellername() + "】审核不通过，更新寻蜜客【" + tSeller.getUid() + "】软件销售套数");

            // 获取寻蜜客信息
            BXmer xmer = xmerDao.getXmerInfo(tSeller.getUid());

            // 获取 SAAS订单数据
            TSaasOrder so = new TSaasOrder();
            so.setOrdersn(tSaasSoldOrder.getSaasOrdersn());
            TSaasOrder saasOrder = saasOrderDao.getSaasOrder(so);

            // 封装更新参数
            Map<String, Object> uMap = new HashMap<String, Object>();
            uMap.put("uid", tSeller.getUid());
            if (tSaasSoldOrder.getSaasSource() == 0) {
                uMap.put("isAddStock", true);
            } else if (tSaasSoldOrder.getSaasSource() == 1) {
                uMap.put("isAddReturn", true);
            }
            uMap.put("edate", new Date());
            uMap.put("ordersn", tSaasSoldOrder.getSaasOrdersn());
            uMap.put("version", saasOrder.getVersion());

            // 更新
            int result = saasOrderDao.updateSaasOrderSaleInfo(uMap);
            if (result == 0) {
                log.error("版本不一致，给寻蜜客【" + tSeller.getUid() + "】返还SAAS软件失败。");
            }

            // 判断SAAS套餐是否为寻蜜客, 不是则不更新寻蜜客信息
            else if(tSeller.getSaasType() == SaasConstant.SELLER_SAAS_TYPE_XMER){
                // 获取寻蜜客当前销售数量 需更新的等级名称
                String levelName = null;
                List<BLevel> levelList = xmerDao.getXmerLevel();
                for (BLevel lev : levelList) {
                    if (xmer.getSoldNums() + 1 >= lev.getSignNum()) {
                        levelName = lev.getLevelName();
                    }
                }
                uMap.put("achievement", levelName);

                // 更新寻蜜客信息
                xmerDao.updateXmerSoldInfo(uMap);
            }


        } else if (destroy == 1) {
            log.info("商家【" + tSeller.getSellerid() + "-" + tSeller.getSellername() + "】注销，更新寻蜜客【" + tSeller.getUid() + "】软件归还套数");
            BXmer xmer = xmerDao.getXmerInfo(tSeller.getUid());

            TSaasOrder so = new TSaasOrder();
            so.setOrdersn(tSaasSoldOrder.getSaasOrdersn());
            TSaasOrder saasOrder = saasOrderDao.getSaasOrder(so);
            Map<String, Object> uMap = new HashMap<String, Object>();
            uMap.put("uid", tSeller.getUid());
            uMap.put("isAddReturn", true);
            uMap.put("edate", new Date());
            uMap.put("ordersn", tSaasSoldOrder.getSaasOrdersn());
            uMap.put("version", saasOrder.getVersion());

            int result = saasOrderDao.updateSaasOrderSaleInfo(uMap);
            if (result == 0) {
                log.error("版本不一致，给寻蜜客【" + tSeller.getUid() + "】返还SAAS软件失败。");
            } else {
                tSaasSoldOrder.setStatus(2);
                saasSoldOrderDao.updateSaasSoldOrder(tSaasSoldOrder);

                // 当Saas来源是寻蜜客的时候才更新寻蜜客等级
                if (tSeller.getSaasType() == SaasConstant.SELLER_SAAS_TYPE_XMER) {
                    // 获取寻蜜客当前销售量, 更新等级信息
                    String levelName = null;
                    List<BLevel> levelList = xmerDao.getXmerLevel();
                    for (BLevel lev : levelList) {
                        if (xmer.getSoldNums() - 1 >= lev.getSignNum()) {
                            levelName = lev.getLevelName();
                        }
                    }
                    uMap.put("achievement", levelName);
                    xmerDao.updateXmerSoldInfo(uMap);
                }
            }
        }
    }


    /**
     * 审核前检查商家信息完整性
     */
    public String prePending(TSeller persistentSeller, int status) {
        boolean flag = true;
        StringBuilder jointMsgStr = new StringBuilder();
        if (status == SellerConstants.SELLER_STATUS_RECOVER) {
            /**
             * 以下条件决定该商家允许注销
             * 1.有钱包余额
             * 2.有提现中的记录
             * 3.有分账中的订单
             * 4.有发放中的红包
             * 5.有发放中的优惠券
             * 6....
             */
            Map<String, String> walletMap = this.getWalletInfo(persistentSeller.getSellerid(), 2);
            if (walletMap == null) {
                flag = false;
                jointMsgStr.append("商家钱包查询余额失败\r\n");
            } else if (
                    (walletMap.get("commision") != null
                            && Double.valueOf(walletMap.get("commision")) > 0)
                            ||
                            (walletMap.get("seller_amount") != null
                                    && Double.valueOf(walletMap.get("seller_amount")) > 0)
                    ) {
                flag = false;
                jointMsgStr.append("商家钱包还有余额\r\n");
            }
            walletMap.clear();
            walletMap = this.getStatisticsWithdrawalsMoney(persistentSeller.getSellerid(), 2);
            if (walletMap == null
                    || walletMap.get("state") == null
                    || !walletMap.get("state").equals("0")) {
                flag = false;
                jointMsgStr.append("查询商家是否有提现中的记录失败\r\n");
            } else if (
                    (walletMap.get("naIncome") != null
                            && Double.valueOf(walletMap.get("naIncome")) > 0)
                            ||
                            (walletMap.get("naCommision") != null
                                    && Double.valueOf(walletMap.get("naCommision")) > 0)
                    ) {
                flag = false;
                jointMsgStr.append("商家还有处理中的提现记录\r\n");
            }

            Bill bill = new Bill();
            bill.setStatus(1);
            bill.setSellerid(persistentSeller.getSellerid());
            if (allBillDao.accountedBillCount(bill) > 0) {
                flag = false;
                jointMsgStr.append("商家还有未完成分账的订单\r\n");
            }
            return jointMsgStr.toString();
        }
        if (status != SellerConstants.SELLER_STATUS_APPROVE) {    //设置为非上线状态时，不检查
            return jointMsgStr.toString();
        }
        jointMsgStr.append("【" + persistentSeller.getSellerid() + "-" + persistentSeller.getSellername() + "】");

        /** 判断商家中是否绑定合作商，即persistentSeller中的jointid是否为空，如果为空则提示不允许上线 
         * add by lifeng 2016年7月2日19:34:20*/
//        if(persistentSeller.getJointid() == null){
//            flag = false;
//            jointMsgStr.append("商家未绑定合作商");
//        }


        if (persistentSeller.getZoneid() == null) {
            flag = false;
            jointMsgStr.append("所属商圈未设置");
        }
        if (persistentSeller.getArea() == null) {
            flag = false;
            jointMsgStr.append("所在区域未设置");
        }
        if (persistentSeller.getGenre() == null || persistentSeller.getGenre().equals("")) {
            flag = false;
            jointMsgStr.append("经营行业未设置");
        }
        /*if(persistentSeller.getAgreement() == null || persistentSeller.getAgreement().equals("")){
            flag = false;
            res.put("jointMsg", "商户合同未设置，不允许上线！");
        }*/

        TSellerLandmark sellerLandmark = sellerLandmarkService
                .getSellerLandmarkByid(persistentSeller.getSellerid().longValue());
        if (sellerLandmark == null) {
            flag = false;
            jointMsgStr.append("经纬度信息未设置");
        }

        TSellerDetailed sellerDetailed = sellerDetailedDao.getObject(persistentSeller.getSellerid().longValue());
        if (sellerDetailed == null) {
            flag = false;
            jointMsgStr.append("商家等级信息未设置");
        }
        //TSellerAgio sellerAgio = sellerAgioDao.getUsingCommonAgion(persistentSeller.getSellerid().longValue());
        if (persistentSeller.getBaseagio() == null) {
            flag = false;
            jointMsgStr.append("折扣未设置");
        }
        TSellerPic logo = sellerPicDao.getSellerLogo(persistentSeller.getSellerid().longValue());
        if (null == logo || logo.getPicurl() == null || logo.getPicurl().equals("")) {
            flag = false;
            jointMsgStr.append("商家LOGO未设置");
        }
        TSellerPic pic = sellerPicDao.getSellerPic(persistentSeller.getSellerid().longValue());
        if (null == pic || pic.getPicurl() == null || pic.getPicurl().equals("")) {
            flag = false;
            jointMsgStr.append("商家图片未设置");
        }
        // 2017-05-26 18:15:06 Joney 不判断平台占比
//        TSellerAgio sellerAgio = sellerAgioService.getUsingCommonAgion(persistentSeller.getSellerid().longValue());
//        if (sellerAgio != null) {
//            if (sellerAgio.getPledger() == null || sellerAgio.getPledger() < 0.0) {
//                flag = false;
//                jointMsgStr.append("平台占比未计算或为空值");
//            }
//        }
        if (!flag) {
            jointMsgStr.setLength(jointMsgStr.length() - 1);
            jointMsgStr.append("，审核不通过！\r\n");
            return jointMsgStr.toString();
        } else {
            return new String();
        }

    }

    /**
     * 方法描述：在此处添加方法描述 <br/>
     * 创建人：  huang'tao <br/>
     * 创建时间：2016-11-3上午11:13:54 <br/>
     *
     * @param sellerid
     * @return
     */
    public TSeller getSellerLandmarkInfoById(Long sellerid) {

        return sellerDao.getSellerLandmarkInfoById(sellerid);
    }

    /**
     * 获取商家直播统计接口
     *
     * @param request
     * @return
     * @throws FailureException
     * @throws TException
     */
    public ResponseData getSellerLiveCountInfo(Map<String, String> paraMap) {
        LiveOrderService.Client client = (LiveOrderService.Client) (liveOrderServiceClient.getClient());//业务服务
        ResponseData responseData = null;
        try {
            responseData = client.getSellerLiveCountInfo(paraMap);
        } catch (Exception e) {
            this.log.debug("获取商家直播统计信息失败");
        } finally {
            liveOrderServiceClient.returnCon();
        }
        return responseData;
    }

    /**
     * 获取商家浏览消费统计接口
     *
     * @param request
     * @return
     * @throws FailureException
     * @throws TException
     */
    public ResponseData getSellerAnalysisInfo(Map<String, String> paraMap) {
        com.xmniao.xmn.core.thrift.service.businessService.SellerService.Client client =
                (com.xmniao.xmn.core.thrift.service.businessService.SellerService.Client) (sellerServiceClient.getClient());//业务服务
        ResponseData responseData = null;
        try {
            responseData = client.getSellerAnalysisInfo(paraMap);
        } catch (Exception e) {
            this.log.debug("获取商家浏览、消费、统计失败", e);
        } finally {
            sellerServiceClient.returnCon();
        }
        return responseData;
    }

    /**
     * 商家上下面商家数据恢复与备份接口
     *
     * @param request
     * @return
     * @throws FailureException
     * @throws TException
     */
    public ResponseData dealSellerAnalysisInfo(int sellerid, boolean online) {
        com.xmniao.xmn.core.thrift.service.businessService.SellerService.Client client =
                (com.xmniao.xmn.core.thrift.service.businessService.SellerService.Client) (sellerServiceClient.getClient());//业务服务
        ResponseData responseData = null;
        try {
            Map<String, String> paraMap = new HashMap<String, String>();
            paraMap.put("sellerid", "" + sellerid);
            paraMap.put("operate", online ? "1" : "-1");
            responseData = client.dealSellerAnalysisInfo(paraMap);
        } catch (Exception e) {
            this.log.debug("恢复、备份商家浏览、消费、统计失败", e);
        } finally {
            sellerServiceClient.returnCon();
        }
        return responseData;
    }

    /**
     * 已上线商家初始化其新增字段数据
     *
     * @param request
     * @return
     * @throws FailureException
     * @throws TException
     */
    public ResponseData initOnlineSellerInfo() {
        com.xmniao.xmn.core.thrift.service.businessService.SellerService.Client client =
                (com.xmniao.xmn.core.thrift.service.businessService.SellerService.Client) (sellerServiceClient.getClient());//业务服务
        ResponseData responseData = null;
        try {
            Map<String, String> paraMap = new HashMap<String, String>();
            responseData = client.initSellerCountInfo(paraMap);
        } catch (Exception e) {
            this.log.debug("商家初始化其新增字段数据", e);
        } finally {
            sellerServiceClient.returnCon();
        }
        return responseData;
    }

    /**
     * 前提：初始化所有的用户行为MongoDB数据
     * 初始方法，重新更新MongoDB中的商家信息。
     */
    public void batchReloadOnlineSellerMongoDB() {
        log.info("初始化所有商家MongoDB数据");
        Query query = new Query();
        mongoBaseService.delete(collectionname, query);
        int pageNo = 0;
        int pageSize = 1000;
        TSeller reqSeller = new TSeller();
        reqSeller.setLimit(pageSize);
        List<TSeller> sellerList = null;
        do {
            sellerList = null;
            reqSeller.setStart(pageNo * pageSize);
            sellerList = sellerDao.getOnlineSellerList(reqSeller);
            pageNo++;
            for (TSeller seller : sellerList) {
                //InsertOrUpdateMongo(seller.getSellerid(), true);
            }
            log.info("上线商家拿到" + sellerList.size() + "条数据");
        } while (sellerList.size() >= pageSize);

    }

    /**
     * 方法描述：更新商家鸟币支付状态
     * 创建人： chenJie <br/>
     * 创建时间：2016年12月20日上午10:33:52 <br/>
     *
     * @param seller
     * @return
     */
    @Transactional
    public Boolean updateCoinStatus(TSeller seller) {
        Integer result = sellerDao.updateCoinStatus(seller);

        if (result == 1) {
            log.info("更新mongoDB数据");
            Criteria criteria = Criteria.where("sellerid").is(seller.getSellerid());
            Map<String, Object> updateMap = new HashMap<>();
            updateMap.put("live_coin_pay", seller.getLiveCoinPay());
            int updateOne = mongoBaseService.updateOne(collectionname, criteria, updateMap);
            if (updateOne == 1) {
                log.info("更新商家鸟币支付状态成功");
                return true;
            } else {
                log.info("mongoDB更新商家信息失败");
                throw new RuntimeException();
            }
        } else {
            log.info("更新商家鸟币支付状态失败");
            return false;
        }
    }

    /**
     * 
     * 方法描述：寻蜜客放弃店铺 <br/>
     * 创建人： Administrator <br/>
     * 创建时间：2017年8月8日下午5:31:09 <br/>
     * @param sellerId
     * @throws Exception 
     */
    @Transactional
    public void abandonSeller(Integer sellerid,Integer uid) throws Exception{
    	log.info("放弃【"+uid+"】签约商家【"+sellerid+"】");
    	TSeller seller = sellerDao.getObject((long)sellerid);
    	if(uid==null || sellerid==null || seller.getSaasType()==null){
    		log.error("放弃操作失败,商家ID或UID或saasType不能为null");
    		throw new Exception("放弃操作失败,商家ID或UID或saasType不能为null");
    	}
    	if(!seller.getUid().equals(uid)){
    		log.error("商家所属寻蜜客信息不一致，放弃操作失败");
    		throw new Exception("商家所属寻蜜客信息不一致，放弃操作失败");
    	}
    	
    	TSaasOrder saasOrder = saasOrderDao.getSellerBelongSassOrder(sellerid,uid);
    	if(saasOrder == null){
    		log.error("该寻蜜客SAAS订单或SAAS签约订单错误");
    		throw new Exception("该寻蜜客SAAS订单或SAAS签约订单错误");
    	}
    	TSeller uSeller = new TSeller();
    	uSeller.setSellerid(sellerid);
    	uSeller.setUid(null);
    	uSeller.setSaasType(null);
    	uSeller.setUdate(new Date());
    	int result = sellerDao.xmerAbandonSeller(uSeller);
    	log.info("解除商家寻蜜客关系:"+result);
    	TSaasSoldOrder saasSoldOrder = new TSaasSoldOrder();
    	saasSoldOrder.setSellerid(sellerid);
    	saasSoldOrder.setUid(uid);
    	result = saasSoldOrderDao.cancelOrder(saasSoldOrder);
    	log.info("取消商家寻蜜客订单:"+result);
    	
    	if(seller.getSaasType()==4){
    		//1.V客的主播签的店，放弃时，名额退给对应V客
    		//主播saasorder给予数-1,V客saasorder数+1
    		result = saasOrderDao.reduceAnchorSaasOrder(saasOrder.getOrdersn());
    		log.info("修改主播的寻蜜客订单:"+result);
    		TSaasOrder vOrder =  saasOrderDao.getVerSaasOrer(saasOrder.getFromUid());
    		log.info("主播所属V客的寻蜜客订单信息:"+JSONObject.toJSONString(vOrder));
    		 String sourceSaasOrder = vOrder.getOrdersn();
    		Map<String,Object> uMap = new HashMap<>();
	    	uMap.put("isAddStock", true);
	    	uMap.put("ordersn", sourceSaasOrder);
	    	uMap.put("version", vOrder.getVersion());
	    	result = saasOrderDao.updateSaasOrderSaleInfo(uMap);
	    	log.info("修改V客的寻蜜客订单:"+result);
    	}else{
	    	Map<String,Object> uMap = new HashMap<>();
	    	if(seller.getSaasType() ==1){
	    		uMap.put("isAddReturn", true);
	    	}else{
	    		uMap.put("isAddStock", true);
	    	}
	    	uMap.put("ordersn", saasOrder.getOrdersn());
	    	uMap.put("version", saasOrder.getVersion());
	    	result=saasOrderDao.updateSaasOrderSaleInfo(uMap);
	    	log.info("修改的寻蜜客订单:"+result);
    	}
    }
}
