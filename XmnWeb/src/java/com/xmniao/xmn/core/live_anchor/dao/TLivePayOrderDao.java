package com.xmniao.xmn.core.live_anchor.dao;

import com.xmniao.xmn.core.base.BaseDao;
import com.xmniao.xmn.core.live_anchor.entity.TLivePayOrder;
import com.xmniao.xmn.core.util.proxy.annotation.DataSource;

import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 
 * 项目名称：XmnWeb
 * 
 * 类名称：TLivePayOrderDao
 * 
 * 类描述： 直播支付订单Dao
 * 
 * 创建人：  huang'tao
 * 
 * 创建时间：2016-9-1 下午4:10:03 
 * 
 * Copyright (c) 广东寻蜜鸟网络技术有限公司
 */
public interface TLivePayOrderDao extends BaseDao<TLivePayOrder>{
    @DataSource("joint")
    Double statLiverPromance(@Param("uids") ArrayList<Integer> uids);
}