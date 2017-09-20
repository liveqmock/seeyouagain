/**
 * 
 */
package com.xmniao.xmn.core.live_anchor.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xmniao.xmn.core.base.BaseDao;
import com.xmniao.xmn.core.base.BaseService;
import com.xmniao.xmn.core.live_anchor.dao.TLiveSalaryDao;
import com.xmniao.xmn.core.live_anchor.dao.TLiveSalaryFailDao;
import com.xmniao.xmn.core.live_anchor.entity.TLiveSalary;
import com.xmniao.xmn.core.live_anchor.entity.TLiveSalaryFail;
import com.xmniao.xmn.core.thrift.client.proxy.ThriftClientProxy;
import com.xmniao.xmn.core.thrift.service.ResponseData;
import com.xmniao.xmn.core.xmnburs.entity.Burs;
import com.xmniao.xmn.core.xmnburs.service.BursService;

/**
 * 
 * 项目名称：XmnWeb
 * 
 * 类名称：LiveSalaryService
 * 
 * 类描述： 在此处添加类描述
 * 
 * 创建人： jianming
 * 
 * 创建时间：2017年4月5日 下午5:24:09 
 * 
 * Copyright (c) 广东寻蜜鸟网络技术有限公司 
 */

@Service
public class TLiveSalaryService extends BaseService<TLiveSalary> {
	
	@Autowired
	private TLiveSalaryDao liveSalaryDao;

	@Autowired
	private BursService bursService;
	
	@Autowired
	private TLiveSalaryFailDao liveSalaryFailDao;
	
	@Resource(name = "liveOrderServiceClient")
	private ThriftClientProxy liveOrderServiceClient;
	
	@Override
	protected BaseDao getBaseDao() {
		return liveSalaryDao;
	}

	/**
	 * 方法描述：加载列表
	 * 创建人： jianming <br/>
	 * 创建时间：2017年4月5日下午6:34:14 <br/>
	 * @param liveSalary
	 * @return
	 */
	public List<TLiveSalary> getLiveSalarys(TLiveSalary liveSalary) {
		if(StringUtils.isNotBlank(liveSalary.getPhone())){
			Burs burs = new Burs();
			burs.setPhone(liveSalary.getPhone());
			List<Burs> ursList = bursService.getUrsList(burs);
			if(ursList.isEmpty()){
				return new ArrayList<>();
			}
			List<Integer> uids=new ArrayList<>();
			for (Burs burs1 : ursList) {
				uids.add(burs1.getUid());
			}
			liveSalary.setUids(uids);
			
		}
		List<TLiveSalary> list = liveSalaryDao.getList(liveSalary);
		if(list.isEmpty()){
			return list;
		}
		List<Integer> uids=new ArrayList<>();
		for (TLiveSalary tLiveSalary : list) {
			uids.add(tLiveSalary.getUid());
		}
		List<Burs> urs = bursService.getUrsListAndLevelByUids(uids.toArray());
		one:for (TLiveSalary tLiveSalary : list) {
			for (Burs burs2 : urs) {
				if(tLiveSalary.getUid().equals(burs2.getUid())){
					tLiveSalary.setName(burs2.getNname());
					tLiveSalary.setPhone(burs2.getPhone());
					tLiveSalary.setAvatar(burs2.getAvatar());
					tLiveSalary.setLevelName(burs2.getLevelName());
					continue one;
				}
			}
		}
		
		return list;	
	}

	/**
	 * 方法描述：修改工资
	 * 创建人： jianming <br/>
	 * 创建时间：2017年4月6日下午2:06:18 <br/>
	 * @param liveSalary
	 * @return
	 */
	public int updateSalary(TLiveSalary liveSalary) {
		return liveSalaryDao.update(liveSalary);
	}

	/**
	 * 方法描述：确认工资
	 * 创建人： jianming <br/>
	 * 创建时间：2017年4月6日下午3:10:29 <br/>
	 * @param id
	 */
	public void confirmSalary(Integer id) {
		liveSalaryDao.confirmSalary(id);
	}

	/**
	 * 方法描述：获得失败列表
	 * 创建人： jianming <br/>
	 * 创建时间：2017年4月11日下午5:07:47 <br/>
	 * @return
	 */
	public List<TLiveSalaryFail> getFailList() {
		List<TLiveSalaryFail> list = liveSalaryFailDao.getFailList();
		List<Integer> uids=new ArrayList<>();
		for (TLiveSalaryFail tLiveSalary : list) {
			uids.add(tLiveSalary.getUid());
		}
		List<Burs> urs = bursService.getUrsListAndLevelByUids(uids.toArray());
		one:for (TLiveSalaryFail tLiveSalary : list) {
			for (Burs burs2 : urs) {
				if(tLiveSalary.getUid().equals(burs2.getUid())){
					tLiveSalary.setName(burs2.getNname());
					tLiveSalary.setPhone(burs2.getPhone());
					/*tLiveSalary.setAvatar(burs2.getAvatar());
					tLiveSalary.setLevelName(burs2.getLevelName());*/
					continue one;
				}
			}
		}
		return list;
	}

	/**
	 * 方法描述：重新计算主播统计失败的工资
	 * 创建人： jianming <br/>
	 * 创建时间：2017年4月12日下午4:16:54 <br/>
	 * @return
	 */
	public boolean salaryAgain() {
		Boolean state = true;
		List<TLiveSalaryFail> list = liveSalaryFailDao.getFailList();
		Map<String,String> map=new HashMap<>();
		for (TLiveSalaryFail tLiveSalaryFail : list) {
			map.put("anchorId",tLiveSalaryFail.getAnchorId()+"");
			map.put("updateTime",tLiveSalaryFail.getCountTime());
			try {
				 Map<String, String>  responseData =liveOrderServiceClient.doClient("uploadLiveSalary", map);
				if(!"0".equals(responseData.get("state"))){
					state=false;
				}
			} catch (Exception e) {
				log.info("调用计算主播工资接口出现异常 anchorId="+map.get("anchorId"),e);
			}
		}
		return state;
	}

	/**
	 * 方法描述：在此处添加方法描述 <br/>
	 * 创建人： jianming <br/>
	 * 创建时间：2017年7月6日下午6:05:56 <br/>
	 * @return
	 * @throws Exception 
	 */
	public Map<String, String> manualSalary() throws Exception {
		Map<String, String> map=new HashMap<>();
		map.put("type", "manual");
		return liveOrderServiceClient.doClient("uploadLiveSalary", map);
	}
	
	

}
