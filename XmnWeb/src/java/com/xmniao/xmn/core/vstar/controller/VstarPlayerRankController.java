/**
 * 
 */
package com.xmniao.xmn.core.vstar.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.xmniao.xmn.core.base.BaseController;
import com.xmniao.xmn.core.base.Pageable;
import com.xmniao.xmn.core.base.Resultable;
import com.xmniao.xmn.core.coupon.controller.vo.CouponIssueSystemPushAddRequest;
import com.xmniao.xmn.core.coupon.entity.TCoupon;
import com.xmniao.xmn.core.coupon.service.SystemPushCouponService;
import com.xmniao.xmn.core.util.PageConstant;
import com.xmniao.xmn.core.vstar.constant.VstarConstant;
import com.xmniao.xmn.core.vstar.entity.TVstarPlayerInfo;
import com.xmniao.xmn.core.vstar.service.TVstarPlayerInfoService;

/**
 * 
 * 项目名称：XmnWeb_vstar
 * 
 * 类名称：VstarPlayerRankController
 * 
 * 类描述： 新时尚大赛选手排名Controller
 * 
 * 创建人：  huang'tao
 * 
 * 创建时间：2017-6-1 下午6:25:34 
 * 
 * Copyright (c) 广东寻蜜鸟网络技术有限公司 
 */
@Controller
@RequestMapping(value = "VstarPlayerRank/manage")
public class VstarPlayerRankController extends BaseController {
	
	
	/**
	 * 注入新时尚大赛选手服务
	 */
	@Autowired
	private TVstarPlayerInfoService vstarPlayerService;
	
	/**
	 * 注入系统推送发放优惠券服务
	 */
	@Autowired
	private SystemPushCouponService systemPushConponService;
	
	
	/**
	 * 
	 * 方法描述：跳转到管理页面 <br/>
	 * 创建人： huang'tao <br/>
	 * 创建时间：2016-9-28下午2:43:23 <br/>
	 * 
	 * @return
	 */
	@RequestMapping(value = "init")
	public ModelAndView init(ModelAndView model) {
		vstarPlayerService.setDefaultTime(model);
		model.setViewName("vstar/vstarPlayerRankManage");
		return model;
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
			List<TVstarPlayerInfo> list = vstarPlayerService.getRankListInfo(vstarPlayer);
			long count = vstarPlayerService.getRankCount(vstarPlayer);
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
			count = vstarPlayerService.getRankCount(vstarPlayer);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}
	
	/**
	 * 
	 * 方法描述：跳转到奖励发放页面<br/>
	 * 创建人：  huang'tao <br/>
	 * 创建时间：2017-8-2上午11:45:05 <br/>
	 * @return
	 */
	@RequestMapping(value="reward/rewardSend/init")
	public ModelAndView rewardSendInit(@RequestParam(value="userItems",required=false) String userItems){
		ModelAndView mv= new ModelAndView();
		mv.setViewName("vstar/vstarRewardSend");
		mv.addObject("userItems", userItems);
		this.log.info("userItems="+userItems);
		return mv;
	}
	
	/**
     * 统计优惠劵发放[发放奖励]张数
     * @param request
     * @return
     */
    @RequestMapping("reward/count-add")
    @ResponseBody
    public Resultable countSystemPushAdd(CouponIssueSystemPushAddRequest request){
        Resultable result = new Resultable();
        log.info("调用统计优惠劵发放[发放奖励]张数 接口, 参数="+request.toString());
        try {
        	Integer ctype = request.getCtype();
        	if(ctype !=null && ctype.intValue()==VstarConstant.REWARD_TYPE.REWARD_TYPE_6){
        		result.setSuccess(true);
        		return result;
        	}
            List<TCoupon> couponsList = request.convertCouponList();
            int count = 0;
            ArrayList<HashMap<String, String>> selectUser = systemPushConponService.analysingSelectUsers(request.getSelectUsers());
            for (TCoupon tCoupon : couponsList) {
                if (request.getUserType() == 1) {
                    count += tCoupon.getUseNum() * selectUser.size();
                } else if(request.getUserType() == 0) {
                    count += tCoupon.getUseNum() * systemPushConponService.countRankUsers(request.getLevelSelect(),selectUser);
                }
            }
            result.setData(count);
            result.setSuccess(true);
            result.setMsg("保存成功!");

        } catch (Exception e) {
            e.printStackTrace();
            result.setSuccess(false);
            result.setMsg("保存失败!");
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
		playerInfo.setLimit(PageConstant.PAGE_LIMIT_NO);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("list", vstarPlayerService.getRankListInfo(playerInfo));
		doExport(request, response, "vstar/vstarPlayerRankInfo.xls", params);

	}

}
