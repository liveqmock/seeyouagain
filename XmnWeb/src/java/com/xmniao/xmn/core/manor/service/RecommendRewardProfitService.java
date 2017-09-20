/**
 * 
 */
package com.xmniao.xmn.core.manor.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xmniao.xmn.core.base.BaseDao;
import com.xmniao.xmn.core.base.BaseService;
import com.xmniao.xmn.core.base.Pageable;
import com.xmniao.xmn.core.coupon.dao.CouponDetailDao;
import com.xmniao.xmn.core.coupon.entity.TCouponDetail;
import com.xmniao.xmn.core.manor.entity.TManorSunshineManage;
import com.xmniao.xmn.core.xmnburs.dao.BursDao;
import com.xmniao.xmn.core.xmnburs.entity.Burs;
import com.xmniao.xmn.core.xmnburs.service.BursService;


@Service
public class RecommendRewardProfitService extends BaseService<TManorSunshineManage> {

	
	@Autowired
	private BursDao bursDao;
	
	@Autowired
	private CouponDetailDao couponDetailDao;
	
	/**
	 * 寻蜜鸟用户Service
	 */
	@Autowired
	private BursService bursService;
	
	

	@SuppressWarnings("rawtypes")
	@Override
	protected BaseDao getBaseDao() {
		// TODO Auto-generated method stub
		return couponDetailDao;
	}

	
	public Pageable<TCouponDetail> getRecommendRewardProfitList(TCouponDetail tCouponDetail) {
		Pageable<TCouponDetail> couponDetailList = new Pageable<TCouponDetail>(tCouponDetail);
		//通过会员昵称查询
		if ((tCouponDetail.getNname() != null && !"".equals(tCouponDetail.getNname())) ) {
			Burs burs = new Burs();
			String nickname = tCouponDetail.getNname();
			burs.setNname(nickname);
			List<Burs> bursList = bursService.getUrsList(burs);
			if (bursList != null && bursList.size() > 0) {
				Integer uid = bursList.get(0).getUid();
				tCouponDetail.setUid(uid);
			}
		}
		couponDetailList.setContent(this.getRecommendRewardProfitDataList(tCouponDetail));
		couponDetailList.setTotal(couponDetailDao.count(tCouponDetail));
		
	    return couponDetailList;
	}
	
	
	public List<TCouponDetail> getRecommendRewardProfitDataList(TCouponDetail tCouponDetail) {
		List<TCouponDetail> couponDetailList=new ArrayList<TCouponDetail>();
		List<Object> uids=new ArrayList<Object>();
		
		//通过会员昵称查询
		if (tCouponDetail.getNname() != null && !"".equals(tCouponDetail.getNname()) ) {
			Burs burs = new Burs();
			String nickname = tCouponDetail.getNname();
			burs.setNname(nickname);
			List<Burs> bursList = bursService.getUrsList(burs);
			if (bursList != null && bursList.size() > 0) {
				Integer uid = bursList.get(0).getUid();
				tCouponDetail.setUid(uid);
			}
		}
		
		
		//通过会员昵称查询
		List<TCouponDetail> couponList = couponDetailDao.getList(tCouponDetail);
		if(couponList!=null && couponList.size()>0){
			for(TCouponDetail detail:couponList){
				uids.add(detail.getUid());
				uids.add(detail.getSourceUid());
			}
			List<Object> uidList = new ArrayList<>(new HashSet<>(uids));//去除重复UID
			List<Burs> ursList = bursService.getUrsListByUids(uidList.toArray());
			if(ursList!=null && ursList.size()>0){
				for(TCouponDetail detail:couponList){
					for(Burs urs:ursList){
						if(urs.getUid()!=null&&detail.getUid()!=null){
							if(urs.getUid().compareTo(detail.getUid())==0){
								detail.setNname(urs.getNname());
//								break;
							}
						}
						//提供的uid(庄园下级)
						if(urs.getUid()!=null&&detail.getSourceUid()!=null){
							if(urs.getUid().compareTo(detail.getSourceUid())==0){
								detail.setSourceName(urs.getNname());
								detail.setSourcePhone(urs.getPhone());
//								break;
							}
						}
					}
					
					couponDetailList.add(detail);
				}
			}
		}

		return couponDetailList;
	}
	
}
