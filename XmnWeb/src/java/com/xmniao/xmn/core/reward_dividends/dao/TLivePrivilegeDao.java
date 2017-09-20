package com.xmniao.xmn.core.reward_dividends.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xmniao.xmn.core.reward_dividends.entity.TLivePrivilege;
import com.xmniao.xmn.core.util.proxy.annotation.DataSource;

public interface TLivePrivilegeDao {
	@DataSource(value = "slave")
	int deleteByPrimaryKey(Integer id);

	@DataSource(value = "slave")
	int insert(TLivePrivilege record);

	@DataSource(value = "slave")
	int insertSelective(TLivePrivilege record);

	@DataSource(value = "slave")
	TLivePrivilege selectByPrimaryKey(Integer id);

	@DataSource(value = "slave")
	int updateByPrimaryKeySelective(TLivePrivilege record);

	@DataSource(value = "slave")
	int updateByPrimaryKey(TLivePrivilege record);

	@DataSource(value = "slave")
	List<TLivePrivilege> getLivePrivilegeList(TLivePrivilege record);

	@DataSource(value = "slave")
	Long countLivePrivilege(TLivePrivilege record);

	@DataSource(value = "slave")
	TLivePrivilege getLivePrivilegeByOrderNo(@Param("orderNo") String orderNo);

	@DataSource(value = "slave")
	List<TLivePrivilege> getRecommendMemberList(TLivePrivilege record);

	@DataSource(value = "slave")
	Long countRecommendMember(TLivePrivilege record);
	
	@DataSource(value = "slave")
	List<TLivePrivilege> getRecommendMemberListByUids(Object[] objects);
}