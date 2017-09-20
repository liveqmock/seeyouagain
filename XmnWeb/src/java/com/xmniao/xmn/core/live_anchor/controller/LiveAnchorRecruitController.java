package com.xmniao.xmn.core.live_anchor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xmniao.xmn.core.base.BaseController;
import com.xmniao.xmn.core.base.Pageable;
import com.xmniao.xmn.core.base.Resultable;
import com.xmniao.xmn.core.live_anchor.entity.BLiver;
import com.xmniao.xmn.core.live_anchor.entity.BVerAnchorRelation;
import com.xmniao.xmn.core.live_anchor.service.BLiveAnchorRecruitService;
import com.xmniao.xmn.core.util.handler.annotation.RequestLogging;

/**
 * 主播招募管理
 * @author Administrator
 *
 */
@RequestLogging(name="主播招募管理")
@Controller
@RequestMapping(value = "liveAnchorRecruit/manage")
public class LiveAnchorRecruitController extends BaseController {
	
	@Autowired
	private BLiveAnchorRecruitService verAnchorRelationService;
	
	
	@RequestMapping(value = "init")
	public String init() {
		return "live_anchor/anchorRecruit/liveAnchorRecruitManage";
	}
	
	
	@RequestMapping(value = "init/list")
	@ResponseBody
	public Object list(BVerAnchorRelation verAnchorRelation){
		Pageable<BVerAnchorRelation> pageable = new Pageable<BVerAnchorRelation>(verAnchorRelation);
		pageable = verAnchorRelationService.getVerAnchorRelationInfoList(verAnchorRelation);

		this.log.info("LiveAnchorRecruitController-->list pageable=" + pageable);
		return pageable;
	}
	

	@RequestMapping(value = { "update" })
	@ResponseBody
	public Resultable update(BVerAnchorRelation verAnchorRelation) {
		Resultable result = new Resultable();
		try {
			int count = 0;
			if (verAnchorRelation != null) {
				count = verAnchorRelationService.saveUpdateActivity(verAnchorRelation);
			}
			if (count > 0) {
				result.setMsg("更新数据成功!");
				result.setSuccess(true);
			} else {
				throw new Exception("更新数据出错");
			}
		} catch (Exception e) {
			result.setMsg("更新失败!");
			result.setSuccess(false);
			
			this.log.error(e.getMessage(), e);
		}
		
		return result;
	}
	
	@RequestMapping(value = "list/viewDetail")
	@ResponseBody
	public Object searchUrsDetailInfo(Integer uid) {
		Resultable resultable = null;
		try {
			BLiver liver = verAnchorRelationService.getUrsDetailInfoByUid(uid);
			resultable = new Resultable(true, "查询成功", liver);
			return resultable;
			
		} catch (Exception e) {
			log.error("查询个人信息失败", e);
			resultable = new Resultable(false, "查询个人信息失败");
			return resultable;
		}

	}
	
	/*@RequestMapping(value="list/viewDetail")
	public ModelAndView sellerPackageDetail(BVerAnchorRelation verAnchorRelation){
		ModelAndView modelAndView = new ModelAndView("live_anchor/anchorRecruit/liveAnchorRecruitDetail");
		
		try{
			Integer anchorUid  = verAnchorRelation.getAnchorUid();
			BLiver liver = verAnchorRelationService.getUrsDetailInfoByUid(anchorUid);
			modelAndView.addObject("liverInfo", liver);
			modelAndView.addObject("isType","view");
			
		}catch(Exception e){
			this.log.error("页面初始化异常", e);
		}
		
		return modelAndView;
	}*/
	

	@RequestMapping(value = { "approve/updateOptionState" })
	@ResponseBody
	public Resultable updateOptionState(BVerAnchorRelation verAnchorRelation) {
		Resultable result = new Resultable();
		try {
			int count = 0;
			if (verAnchorRelation != null) {
				count = verAnchorRelationService.updateOptionState(verAnchorRelation);
			}
			if (count > 0) {
				result.setMsg("更新数据成功!");
				result.setSuccess(true);
			} else {
				throw new Exception("更新数据出错");
			}
		} catch (Exception e) {
			result.setMsg("更新失败!" + e.getMessage());
			result.setSuccess(false);
			
			this.log.error(e.getMessage(), e);
		}
		
		return result;
	}
	
}
