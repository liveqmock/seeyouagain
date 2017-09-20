package com.xmniao.xmn.core.xmermanagerment.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.xmniao.xmn.core.base.BaseDao;
import com.xmniao.xmn.core.util.proxy.annotation.DataSource;
import com.xmniao.xmn.core.xmermanagerment.entity.BLevel;
import com.xmniao.xmn.core.xmermanagerment.entity.BXmer;
import com.xmniao.xmn.core.xmermanagerment.entity.TSaasOrder;
import com.xmniao.xmn.core.xmermanagerment.entity.XmerSeller;
/**
 *@ClassName:BXmerDao
 *@Description:寻蜜客成员管理dao层
 *@author hls
 *@date:2016年5月25日上午11:15:23
 */
public interface BXmerDao extends BaseDao<BXmer> {
	/**
     * @Title:getXmerList
     * @Description:查询寻蜜客成员列表
     * @param bxmer
     * @return List<BXmer>
     * @throw
     */
	@DataSource("burs")
	public List<BXmer> getXmerList(BXmer bxmer);
	
	
	/**
	 * @Title:getXmerCount
	 * @Description:查询寻蜜客成员列表总条数
	 * @param bxmer
	 * @return Long
	 * @throw
	 */
	@DataSource("burs")
	public Long getXmerCount(BXmer bxmer);
	
	/**
	 * @Title:getClientSellCount
	 * @Description:查询商户端出售总数
	 * @return Long
	 * @throw
	 */
	@DataSource("slave")
	public Long getSellCount(@Param(value = "saasChannel") Integer saasChannel);
	
	/**
	 * @Title:getSellerCrrentCount
	 * @Description:查询平台店铺流水总额
	 * @return Long
	 * @throw
	 */
	@DataSource("slave")
	public Double getSellerCrrentCount(@Param(value = "saasChannel")Integer saasChannel);
	
	
	/**
	 * @Title:updateXmer
	 * @Description:修改蜜客成员(更新表b_xmer)
	 * @param banner void
	 * @throw
	 */
	@DataSource("burs")
	public void updateXmer(BXmer bxmer);
	/**
	 * @Title:updateUrsInfo
	 * @Description:修改蜜客成员(更新表b_urs_info)
	 * @param bxmer void
	 * @throw
	 */
	@DataSource("burs")
	public void updateUrsInfo(BXmer bxmer);
	/**
	 * @Title:getXmer
	 * @Description:根据寻蜜客编号查询导航图详情
	 * @param id
	 * @return BXmer
	 * @throw
	 */
	@DataSource("burs")
	public BXmer getXmer(Integer id);
	/**
	 * @Title:exportXmerList
	 * @Description:导出寻蜜客成员列表
	 * @param bxmer
	 * @return List<BXmer>
	 * @throw
	 */
	@DataSource("burs")
	public List<BXmer> exportXmerList(BXmer bxmer);
	/**
	 * @Title:getXmerPartnerList
	 * @Description:查询寻蜜客伙伴列表
	 * @param bxmer
	 * @return List<BXmer>
	 * @throw
	 */
	@DataSource("burs")
	public List<BXmer> getXmerPartnerList(BXmer bxmer);
	/**
	 * @Title:getXmerPartnerCount
	 * @Description:查询寻蜜客伙伴列表总条数
	 * @param bxmer
	 * @return Long
	 * @throw
	 */
	@DataSource("burs")
	public long getXmerPartnerCount(@Param("uid")Integer uid,@Param("objectOriented")Integer objectOriented);
	/**
	 * @Title:xmerSellerList
	 * @Description:查询寻蜜客商铺列表
	 * @param xmerseller
	 * @return List<XmerSeller>
	 * @throw
	 */
	@DataSource("slave")
	public List<XmerSeller> xmerSellerList(XmerSeller xmerseller);
	/**
	 * @Title:xmerSellerCount
	 * @Description:查询寻蜜客伙伴列表总条数
	 * @param xmerseller
	 * @return Long
	 * @throw
	 */
	@DataSource("slave")
	public Long xmerSellerCount(XmerSeller xmerseller);
	
	/**
     * @Title:getXmerIssues
     * @Description:获取当前寻蜜客关系
     * @param uid
     * @return Map<String,Object>
     * @throw
     */
	@DataSource("burs")
	public Map<String,Object> getXmerIssues(int uid);
	
	/**
     * @Title:getXmerIssues
     * @Description:获取当前寻蜜客用户名
     * @param uid
     * @return String
     * @throw
     */
	@DataSource("burs")
	public String getXmerName(int uid);
	
	/**
	 * 
	 * @Title: updateXmerSoldInfo 
	 * @Description:更新寻蜜客套餐销售信息
	 */
	@DataSource("burs")
	public int updateXmerSoldInfo(Map<String,Object> upadteMap);
	
	/**
	 * @Title:numsList
	 * @Description:根据寻蜜客id查询签约数量
	 * @param objects
	 * @return List<TSaasOrder>
	 * @throw
	 */
	@DataSource("slave")
	public List<TSaasOrder> numsList(Object[] objects);
	/**
	 * @Title:eightFoldNumsList
	 * @Description:根据寻蜜客id和套餐单价查询签约软件576套餐剩余套数
	 * @param objects
	 * @return List<TSaasOrder>
	 * @throw
	 */
	@DataSource("slave")
	public List<TSaasOrder> eightFoldNumsList(Object[] objects);
	/**
	 * @Title:sevenFoldNumsList
	 * @Description:根据寻蜜客id和套餐单价查询签约软件504套餐剩余套数
	 * @param objects
	 * @return List<TSaasOrder>
	 * @throw
	 */
	@DataSource("slave")
	public List<TSaasOrder> sevenFoldNumsList(Object[] objects);
	
	/**
	 * @Title:getList
	 * @Description:查询所有的寻蜜客的列表
	 * @param bxmer
	 * @return List<BXmer>
	 */
	@DataSource("burs")
	public List<BXmer> getList(BXmer bxmer);
	
	/**
	 * 获取所有的寻蜜客头衔信息
	 * @Title: getXmerLevel 
	 * @Description:
	 */
	@DataSource("burs")
	public List<BLevel> getXmerLevel();
	
	/**
	 * 获取寻蜜客信息
	 * @Title: getXmerInfo 
	 * @Description:
	 */
	@DataSource("burs")
	public BXmer getXmerInfo(Integer uid);
	
	/**
	 * 获取寻蜜客信息
	 * @Title: getXmerInfo 
	 * @Description:
	 */
	@DataSource("burs")
	public List<BXmer> getXmerBySaasOrderUid(List<Integer> xmerList);
	
	
	/**
	 * 方法描述：在此处添加方法描述 <br/>
	 * 创建时间：2017年5月5日下午3:39:51 <br/>
	 * @param params
	 * @return
	 */
	@DataSource("burs")
	public List<String> getUrsEarningsRelationByUid(Map<String, Object> params);
	
	/**
	 * 方法描述：在此处添加方法描述 <br/>
	 * 创建人： Administrator <br/>
	 * 创建时间：2017年5月5日下午6:10:57 <br/>
	 * @param bxmer
	 * @return
	 */
	@DataSource("burs")
	public List<BXmer> getNewXmerPartnerList(BXmer bxmer);
	
	/**
	 * @Title:numsList
	 * @Description:根据寻蜜客id查询签约数量
	 * @param objects
	 * @return List<TSaasOrder>
	 * @throw
	 */
	@DataSource("slave")
	public List<TSaasOrder> saasNumsList(@Param("objects") Object[] objects,@Param("saasChannel")Integer saasChannel);
}
