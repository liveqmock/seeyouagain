/**
 * 
 */
package com.xmniao.xmn.core.manor.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xmniao.xmn.core.base.BaseDao;
import com.xmniao.xmn.core.base.BaseService;
import com.xmniao.xmn.core.base.Pageable;
import com.xmniao.xmn.core.manor.dao.ManorInfoDao;
import com.xmniao.xmn.core.manor.dao.ManorMarketTransDao;
import com.xmniao.xmn.core.manor.entity.TManorMarketTrans;
import com.xmniao.xmn.core.xmnburs.dao.BursDao;
import com.xmniao.xmn.core.xmnburs.entity.Burs;
import com.xmniao.xmn.core.xmnburs.service.BursService;


/*市集管理*/

@Service
public class ManorMarketTransService extends BaseService<TManorMarketTrans> {

	
	/**
	 * 注入市集管理服务
	 */
	@Autowired
	private ManorMarketTransDao manorMarketTransDao;
	
	@Autowired
	private ManorInfoDao manorInfoDao;
	
	@Autowired
	private BursDao bursDao;
	
	/**
	 * 寻蜜鸟用户Service
	 */
	@Autowired
	private BursService bursService;
	

	@SuppressWarnings("rawtypes")
	@Override
	protected BaseDao getBaseDao() {
		// TODO Auto-generated method stub
		return manorMarketTransDao;
	}

	
	public Pageable<TManorMarketTrans> getManorMarketTransList(TManorMarketTrans manorMarketTrans) {
		Pageable<TManorMarketTrans> manorFlowerRelationInfoList = new Pageable<TManorMarketTrans>(manorMarketTrans);
		
		List<TManorMarketTrans> manorMarketTransList = this.searchManorMarketTransList(manorMarketTrans) ;
		Long count = this.countManorMarketTrans(manorMarketTrans);

		manorFlowerRelationInfoList.setContent(manorMarketTransList);
		manorFlowerRelationInfoList.setTotal(count);
		
	    return manorFlowerRelationInfoList;
	}

	
	
	public List<TManorMarketTrans> searchManorMarketTransList(TManorMarketTrans manorMarketTrans) {
		List<TManorMarketTrans> manorMarketTransList = new ArrayList<TManorMarketTrans>();
		
		
		//通过会员昵称查询
		if (manorMarketTrans.getNname() != null && !"".equals(manorMarketTrans.getNname()) ) {
			Burs burs = new Burs();
			String nickname = manorMarketTrans.getNname();
			burs.setNname(nickname);
			List<Burs> bursList = bursService.getUrsList(burs);
			if (bursList != null && bursList.size() > 0) {
				Integer uid = bursList.get(0).getUid();
				manorMarketTrans.setUid(uid);
			}
		}
		manorMarketTransList = manorMarketTransDao.getManorMarketTransList(manorMarketTrans);
		List<Integer> uids = new ArrayList<Integer>();
		for (TManorMarketTrans manorMarketTransRecord : manorMarketTransList) {
			if (manorMarketTransRecord.getUid() != null)
				uids.add(manorMarketTransRecord.getUid());
		}
		
		

		//设置用户名称 电话信息
		if (uids.size() > 0){
//			List<Burs> bursList = bursDao.getUrsListByUids(uids.toArray());
		    List<Map<String, String>> manorMemberLevelNameList = manorInfoDao.getManorMemberLevelNameList(uids.toArray());
			for (TManorMarketTrans levelEarningRecord: manorMarketTransList){
				for (Map<String, String> object : manorMemberLevelNameList) {
					if (levelEarningRecord.getUid().equals(object.get("uid")) ){
						levelEarningRecord.setLevelName(object.get("levelName"));
					}
				}
			}
		}
		
		return manorMarketTransList;
	}
	
	
	/**
	 * 方法描述：统计市集管理 <br/>
	 * 创建人： Administrator <br/>
	 * 创建时间：2017年7月7日上午11:00:36 <br/>
	 * @param manorMarketTrans
	 * @return
	 */
	public Long countManorMarketTrans(TManorMarketTrans manorMarketTrans) {
		return  manorMarketTransDao.countManorMarket(manorMarketTrans);
	}
	
	
	public int saveUpdateActivity(TManorMarketTrans manorMarketTrans) throws Exception{
		manorMarketTrans.setUpdateDate(new Date());
		return manorMarketTransDao.updateByPrimaryKeySelective(manorMarketTrans);
	}
	
	
}
