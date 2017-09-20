/**
 * 
 */
package com.xmniao.xmn.core.vstar.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.xmniao.xmn.core.base.BaseController;
import com.xmniao.xmn.core.base.Pageable;
import com.xmniao.xmn.core.base.Resultable;
import com.xmniao.xmn.core.businessman.service.SellerService;
import com.xmniao.xmn.core.util.PageConstant;
import com.xmniao.xmn.core.vstar.entity.TVstarPlayerInfo;
import com.xmniao.xmn.core.vstar.entity.TVstarSellerInfo;
import com.xmniao.xmn.core.vstar.service.TVstarPlayerInfoService;

/**
 * 
 * 项目名称：XmnWeb_vstar
 * 
 * 类名称：VstarPlayerInfoController
 * 
 * 类描述： 新时尚大赛选手Controller
 * 
 * 创建人：  huang'tao
 * 
 * 创建时间：2017-6-1 下午6:25:34 
 * 
 * Copyright (c) 广东寻蜜鸟网络技术有限公司 
 */
@Controller
@RequestMapping(value = "VstarPlayer/manage")
public class VstarPlayerInfoController extends BaseController {
	
	
	/**
	 * 注入新时尚大赛选手服务
	 */
	@Autowired
	private TVstarPlayerInfoService vstarPlayerService;
	
	/**
	 * 注入商家服务
	 */
	@Autowired
	private SellerService sellerService;
	
	
	/**
	 * 
	 * 方法描述：跳转到管理页面 <br/>
	 * 创建人： huang'tao <br/>
	 * 创建时间：2016-9-28下午2:43:23 <br/>
	 * 
	 * @return
	 */
	@RequestMapping(value = "init")
	public String init() {
		return "vstar/vstarPlayerManage";
	}
	
	/**
	 * 
	 * 方法描述：加载选手列表 <br/>
	 * 创建人： huang'tao <br/>
	 * 创建时间：2016-9-28下午2:43:23 <br/>
	 * 
	 * @return
	 */
	@RequestMapping(value = "init/list")
	@ResponseBody
	public Object initList(TVstarPlayerInfo vstarPlayer) {
		Pageable<TVstarPlayerInfo> pageable=new Pageable<TVstarPlayerInfo>(vstarPlayer);
		Object json =null;
		try {
			vstarPlayerService.setReferrerPhone(vstarPlayer);
			List<TVstarPlayerInfo> list = vstarPlayerService.getListInfo(vstarPlayer);
			long count = vstarPlayerService.count(vstarPlayer);
			pageable.setContent(list);
			pageable.setTotal(count);
			json = JSON.toJSON(pageable);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}
	
	/**
	 * 获取查询记录数
	 * 
	 * @author huang'tao
	 * @date 2015年8月12日 下午4:34:01
	 * @return
	 */
	@RequestMapping(value = "init/getCurrentSize")
	@ResponseBody
	public Object getCurrentSize(TVstarPlayerInfo vstarPlayer){
		long count=0l;
		try {
			count = vstarPlayerService.count(vstarPlayer);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}
	
	/**
	 * 
	 * 方法描述：跳转到签约商户列表页 <br/>
	 * 创建人：  huang'tao <br/>
	 * 创建时间：2017-4-1下午2:05:41 <br/>
	 * @return
	 */
	@RequestMapping(value="init/loadProductInfo/init")
	public String loadProductInfoInit(TVstarPlayerInfo playerInfo,Model model){
		model.addAttribute("playerInfo", playerInfo);
		return "vstar/vstarSellerManage";
	}
	
	/**
	 * 
	 * 方法描述：加载商品列表信息 <br/>
	 * 创建人：  huang'tao <br/>
	 * 创建时间：2017-4-1下午3:07:24 <br/>
	 * @param liveGift
	 * @return
	 */
	@RequestMapping(value="init/vstarSellerList")
	@ResponseBody
	public Object vstarSellerList(TVstarSellerInfo sellerInfo){
		Integer uid = sellerInfo.getUid();
		if(uid == null){
			return null;
		}
		Pageable<TVstarSellerInfo> pageable = new Pageable<TVstarSellerInfo>(sellerInfo);
		List<TVstarSellerInfo> sellerList = vstarPlayerService.getSellerListInfo(sellerInfo);
		long count = vstarPlayerService.getSellerCountByUid(sellerInfo);
		pageable.setContent(sellerList);
		pageable.setTotal(count);
		return pageable;
		
	}
	
	/**
	 * 
	 * 方法描述：放弃商家操作 <br/>
	 * 创建人：  huang'tao <br/>
	 * 创建时间：2017-4-1下午3:07:24 <br/>
	 * @param liveGift
	 * @return
	 */
	@RequestMapping(value="init/abandonSeller")
	@ResponseBody
	public Object abandonSeller(TVstarSellerInfo sellerInfo){
		Resultable result = new Resultable();
		try {
			Integer sellerid = sellerInfo.getSellerid();
			Integer uid = sellerInfo.getUid();
			sellerService.abandonSeller(sellerid, uid);
			result.setSuccess(true);
			result.setMsg("操作成功!");
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
			result.setMsg("操作失败!");
			this.log.error("放弃商家操作失败:"+e.getMessage());
		}
		return result;
	}
	
	/**
	 * 
	 * 方法描述：导出<br/>
	 * 创建人：  huang'tao <br/>
	 * 创建时间：2017-6-12下午6:08:14 <br/>
	 * @param playerInfo
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "export")
	public void export(TVstarPlayerInfo playerInfo, HttpServletRequest request,
			HttpServletResponse response) {
		playerInfo.setOrder(PageConstant.NOT_ORDER);
		playerInfo.setLimit(PageConstant.PAGE_LIMIT_NO);
		Map<String, Object> params = new HashMap<String, Object>();
		vstarPlayerService.setReferrerPhone(playerInfo);
		params.put("list", vstarPlayerService.getListInfo(playerInfo));
		doExport(request, response, "vstar/vstarPlayer.xls", params);

	}

}
