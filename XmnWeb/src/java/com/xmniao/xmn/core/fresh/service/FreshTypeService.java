/**
 * 
 */
package com.xmniao.xmn.core.fresh.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xmniao.xmn.core.base.BaseDao;
import com.xmniao.xmn.core.base.BaseService;
import com.xmniao.xmn.core.fresh.dao.FreshTypeDao;
import com.xmniao.xmn.core.fresh.entity.FreshType;

/**
 * 
 * 项目名称：XmnWeb1
 * 
 * 类名称：FreshTypeService
 * 
 * 类描述： 在此处添加类描述
 * 
 * 创建人： jianming
 * 
 * 创建时间：2016年12月20日 上午11:30:37 
 * 
 * Copyright (c) 广东寻蜜鸟网络技术有限公司 
 */
@Service
public class FreshTypeService extends BaseService<FreshType>{
	
	@Autowired
	private FreshTypeDao freshTypeDao;
	
	/* (non-Javadoc)
	 * @see com.xmniao.xmn.core.base.BaseService#getBaseDao()
	 */
	@Override
	protected BaseDao getBaseDao() {
		return freshTypeDao;
	}

	/**
	 * 方法描述：获取全部分类
	 * 创建人： jianming <br/>
	 * 创建时间：2016年12月20日上午11:36:04 <br/>
	 * @param freshType 
	 * @param freshType
	 * @return
	 */
	public List<FreshType> getALLByPage(FreshType freshType) {
		 List<FreshType> father = freshTypeDao.getALLFather(freshType);
		 List<FreshType> all=new ArrayList<FreshType>();
		 for (FreshType freshType2 : father) {
			all.add(freshType2);
			List<FreshType> childs=freshTypeDao.findChild(freshType2.getId());
			all.addAll(childs);
		}
		 return all;
	}

	/**
	 * 方法描述：统计父级数量
	 * 创建人： jianming <br/>
	 * 创建时间：2016年12月20日下午2:48:28 <br/>
	 * @param freshType
	 * @return
	 */
	public Long getFatherTotal() {
		return freshTypeDao.selectFatherTotal();
	}

	/**
	 * 方法描述：获取所有父级目录
	 * 创建人： jianming <br/>
	 * 创建时间：2016年12月21日上午11:41:44 <br/>
	 * @param freshType 
	 * @return
	 */
	public List<FreshType> getALLFather(FreshType freshType) {
		return freshTypeDao.getALLFather(freshType);
	}

	/**
	 * 方法描述：在此处添加方法描述 <br/>
	 * 创建人： jianming <br/>
	 * 创建时间：2016年12月21日下午6:18:20 <br/>
	 * @param id
	 */
	public void delete(Integer id) {
		freshTypeDao.delete(id);
	}


	/**
	 * 方法描述：批量删除
	 * 创建人： jianming <br/>
	 * 创建时间：2016年12月22日上午9:52:42 <br/>
	 * @param ids
	 * @param childId 
	 * @return
	 */
	public int deleteMine(String parentId, String childId) {
		return freshTypeDao.deleteMine(parentId,childId);
	}

	/**
	 * 方法描述：查询父级包含所有下级
	 * 创建人： jianming <br/>
	 * 创建时间：2016年12月26日上午10:27:46 <br/>
	 * @param freshType
	 * @return
	 */
	public List<FreshType> getFatherAndChilds(FreshType freshType) {
		 List<FreshType> father = freshTypeDao.getALLFather(freshType);
		 for (FreshType freshType2 : father) {
			List<FreshType> childs=freshTypeDao.findChild(freshType2.getId());
			freshType2.setChilds(childs);
		}
		 return father;
	}

	/**
	 * 方法描述：判断父分类是否包含品牌
	 * 创建人： jianming <br/>
	 * 创建时间：2017年1月11日下午3:32:07 <br/>
	 * @param parentId
	 * @return
	 */
	public boolean hasParentBrand(String parentId) {
		List<FreshType> freshTypes=freshTypeDao.hasParentBrand(parentId);
		if(freshTypes.size()>0){
			return true;
		}
		return false;
	}

	/**
	 * 方法描述：判断父分类是否包含商品
	 * 创建人： jianming <br/>
	 * 创建时间：2017年1月11日下午3:37:11 <br/>
	 * @param parentId
	 * @return
	 */
	public boolean hasParentProduct(String parentId) {
		List<FreshType> freshTypes=freshTypeDao.hasParentProduct(parentId);
		if(freshTypes.size()>0){
			return true;
		}
		return false;
	}

	/**
	 * 方法描述：判断子分类是否包含品牌
	 * 创建人： jianming <br/>
	 * 创建时间：2017年1月11日下午3:41:39 <br/>
	 * @param childId
	 * @return
	 */
	public boolean hasChildBrand(String childId) {
		List<FreshType> freshTypes=freshTypeDao.hasChildBrand(childId);
		if(freshTypes.size()>0){
			return true;
		}
		return false;
	}

	/**
	 * 方法描述：判断子分类是否包含商品
	 * 创建人： jianming <br/>
	 * 创建时间：2017年1月11日下午3:44:43 <br/>
	 * @param parentId
	 * @return
	 */
	public boolean hasChildProduct(String childId) {
		List<FreshType> freshTypes=freshTypeDao.hasChildProduct(childId);
		if(freshTypes.size()>0){
			return true;
		}
		return false;
	}

}
