/**
 * 
 */
package com.xmniao.xmn.core.fresh.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.xmniao.xmn.core.base.BaseDao;
import com.xmniao.xmn.core.base.BaseService;
import com.xmniao.xmn.core.fresh.dao.MallPackageProductDao;
import com.xmniao.xmn.core.fresh.entity.MallPackageProduct;
import com.xmniao.xmn.core.fresh.entity.ProductInfo;
import com.xmniao.xmn.core.fresh.entity.TSaleGroup;

/**
 * 
 * 项目名称：XmnWeb
 * 
 * 类名称：MallPackageProductService
 * 
 * 类描述： 在此处添加类描述
 * 
 * 创建人： jianming
 * 
 * 创建时间：2017年8月7日 下午2:55:59 
 * 
 * Copyright (c) 广东寻蜜鸟网络技术有限公司 
 */
@Service
public class MallPackageProductService extends BaseService<MallPackageProduct>{

	@Autowired
	private MallPackageProductDao mallPackageProductDao;
	
	@Autowired
	private FreshManageService freshManageService;
	
	@Override
	protected BaseDao getBaseDao() {
		return mallPackageProductDao;
	}

	/**
	 * 方法描述：在此处添加方法描述 <br/>
	 * 创建人： jianming <br/>
	 * 创建时间：2017年8月8日下午4:37:06 <br/>
	 * @param asList
	 * @return
	 */
	public List<MallPackageProduct> getByIds(List<String> ids) {
		List<Integer> intIds=new ArrayList<>();
		for (String string : ids) {
			intIds.add(Integer.valueOf(string));
		}
		List<MallPackageProduct> list = mallPackageProductDao.getByIds(intIds);
		setProductAndGroup(list);
		return list;
	}

	private void setProductAndGroup(List<MallPackageProduct> list) {
		for (MallPackageProduct mallPackageProduct : list) {
			ProductInfo productInfo = freshManageService.getByCodeId(mallPackageProduct.getCodeId());
			mallPackageProduct.setProductInfo(productInfo);
			if(StringUtils.isNotBlank(mallPackageProduct.getPvIds())){
				TSaleGroup groupsByPvid = freshManageService.getGroupsByPvid(mallPackageProduct.getCodeId(), mallPackageProduct.getPvIds());
				mallPackageProduct.setSaleGroup(groupsByPvid);
			}
		}
	}

	/**
	 * 方法描述：补充packageId
	 * 创建人： jianming <br/>
	 * 创建时间：2017年8月9日下午2:38:21 <br/>
	 * @param asList
	 * @param id
	 */
	public void updatePackageIdByIds(List<String> productIds, Long id) {
		mallPackageProductDao.updatePackageIdByIds(productIds,id);
	}

	/**
	 * 方法描述：在此处添加方法描述 <br/>
	 * 创建人： jianming <br/>
	 * 创建时间：2017年8月9日下午4:39:31 <br/>
	 * @param id
	 * @return
	 */
	public List<MallPackageProduct> getListByPackageId(Long id) {
		List<MallPackageProduct> listByPackageId = mallPackageProductDao.getListByPackageId(id);
		setProductAndGroup(listByPackageId);
		return listByPackageId;
	}

	/**
	 * 方法描述：检查是否有相同规格
	 * 创建人： jianming <br/>
	 * 创建时间：2017年8月14日下午6:01:47 <br/>
	 * @param mallPackageProduct
	 * @return
	 */
	public boolean hasPvId(MallPackageProduct mallPackageProduct) {
		return mallPackageProductDao.countPvIds(mallPackageProduct.getCodeId(),mallPackageProduct.getPvIds())>0;
	}
	
	
}
