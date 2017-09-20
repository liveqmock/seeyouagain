package com.xmniao.xmn.core.business_cooperation.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.xmniao.xmn.core.base.BaseController;
import com.xmniao.xmn.core.base.Pageable;
import com.xmniao.xmn.core.base.Resultable;
import com.xmniao.xmn.core.business_cooperation.entity.TJoint;
import com.xmniao.xmn.core.business_cooperation.service.JointService;
import com.xmniao.xmn.core.business_cooperation.util.PartnerConstants;
import com.xmniao.xmn.core.common.entity.TAppPush;
import com.xmniao.xmn.core.common.service.AppPushService;
import com.xmniao.xmn.core.user_terminal.util.UserConstants;
import com.xmniao.xmn.core.util.handler.annotation.RequestLogging;

/**
 * 
 * 项目名称：XmnWeb
 * 
 * 类名称：AppPushController
 * 
 * 类描述： APP推送信息
 * 
 * 创建人： zhou'sheng
 * 
 * 创建时间：2014年11月17日19时29分58秒
 * 
 * Copyright (c) 广东寻蜜鸟网络技术有限公司
 */
@RequestLogging(name="合作商消息推送管理")
@Controller("businessCooperationAppPushController")
@RequestMapping(value = "business_cooperation/appPush")
public class AppPushController extends BaseController {

	@Autowired
	private AppPushService appPushService;
	
	@Autowired
	private JointService jointService;

	/**
	 * 
	 * init(初始化列表页面)
	 * 
	 * @author：zhou'sheng
	 */
	@RequestMapping(value = "init")
	public String init(Model model) {
		model.addAttribute("client", PartnerConstants.CLIENT);
		return "common/appPushList";
	}

	/**
	 * 
	 * list(列表数据初始化)
	 * 
	 * @author：zhou'sheng
	 */
	@RequestMapping(value = "init/list")
	@ResponseBody
	public Object list(TAppPush appPush) {
		Pageable<TAppPush> pageable = new Pageable<TAppPush>(appPush);
		pageable.setContent(appPushService.getList(appPush));
		pageable.setTotal(appPushService.count(appPush));
		return pageable;
	}

	/**
	 * 导出数据
	 * @param appPush
	 * @param request
	 * @param response
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	@RequestMapping(value = "export")
	public void export(TAppPush appPush, HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, IOException {
		appPush.setLimit(-1);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("list", appPushService.getList(appPush));
		doExport(request, response, "common/appPush.xls", params);
	}
	
	/**
	 * 
	 * delete(删除)
	 * 
	 * @author：zhou'sheng
	 */
	@SuppressWarnings("finally")
	@RequestLogging(name="合作商消息推送删除")
	@RequestMapping(value = "/delete")
	@ResponseBody
	public Object delete(HttpServletRequest request, @RequestParam("tid") String tid) {
		Resultable resultable = null;
		try {
			Integer resultNum = appPushService.delete(tid.split(","));
			if (resultNum > 0) {
				this.log.info("删除成功");
				resultable = new Resultable(true, "操作成功");
				//写入日志记录表
				String[] s={"合作商消息推送编号",tid,"删除","删除"};
				appPushService.fireLoginEvent(s,PartnerConstants.FIRELOGIN_NUMA);
			}
		} catch (Exception e) {
			this.log.error("删除异常", e);
			resultable = new Resultable(false, "操作失败");
			//写入日志记录表
			String[] s={"合作商消息推送编号",tid,"删除","删除"};
			appPushService.fireLoginEvent(s,PartnerConstants.FIRELOGIN_NUMB);
		} finally {
			return resultable;
		}
	}

	/**
	 * 
	 * addInit(添加初始化)
	 * 
	 * @author：zhou'sheng
	 */
	@RequestMapping(value = "/add/init")
	public ModelAndView addInit() {
		ModelAndView modelAndView = new ModelAndView("common/editAppPush");
		modelAndView.addObject("isType", "add");
		return modelAndView;
	}

	/**
	 * 
	 * add(添加)
	 * 
	 * @author：zhou'sheng
	 */
	@SuppressWarnings("finally")
	@RequestLogging(name="合作商消息推送添加")
	@RequestMapping(value = "/add")
	@ResponseBody
	public Object add(TAppPush appPush) {
		Resultable resultable = null;
		try {
			appPushService.add(appPush);
			this.log.info("添加成功");
			resultable = new Resultable(true, "操作成功");
			//写入 日志记录表
			String word = appPush.getTitle();
			String str = "";
			if (word.length() <= PartnerConstants.WORD_LENGTH){
				str = word;
			}else{
				str = word.substring(PartnerConstants.RESULTNUM_INIT, PartnerConstants.WORD_LENGTH)+"...";
			}
			
			String[] s={"合作商消息推送",str,"新增"};
			appPushService.fireLoginEvent(s,PartnerConstants.FIRELOGIN_NUMA);
		} catch (Exception e) {
			this.log.error("添加异常", e);
			resultable = new Resultable(false, "操作失败");
			//写入 日志记录表
			String word = appPush.getTitle();
			String str = "";
			if (word.length() <= PartnerConstants.WORD_LENGTH){
				str = word;
			}else{
				str = word.substring(PartnerConstants.RESULTNUM_INIT, PartnerConstants.WORD_LENGTH)+"...";
			}
			
			String[] s={"合作商消息推送",str,"新增"};
			appPushService.fireLoginEvent(s,PartnerConstants.FIRELOGIN_NUMB);
		} finally {
			return resultable;
		}
	}

	/**
	 * 
	 * updateInit(修改初始化)
	 * 
	 * @author：zhou'sheng
	 */
	@RequestMapping(value = "/update/init")
	public ModelAndView updateInit(HttpServletRequest request, @RequestParam("tid") String tid) {
		ModelAndView modelAndView = new ModelAndView("common/editAppPush");
		//ModelAndView modelAndView = new ModelAndView("business_cooperation/editAppPush");
		modelAndView.addObject("isType", "update");
		try {
			TAppPush appPush = appPushService.getObject(new Long(tid));
			this.log.info(appPush);
			modelAndView.addObject("appPush", appPush);
		} catch (NumberFormatException e) {
			this.log.error("修改初始异常", e);
		} finally {
			return modelAndView;
		}
	}

	/**
	 * 
	 * update(修改)
	 * 
	 * @author：zhou'sheng
	 */
	@SuppressWarnings("finally")
	@RequestLogging(name="合作商消息推送修改")
	@RequestMapping(value = "/update")
	@ResponseBody
	public Object update(TAppPush  appPush) {
		Resultable resultable = null;
		try {
			appPushService.update(appPush);
			this.log.info("修改成功");
			resultable = new Resultable(true, "操作成功");
			//添加到日志记录表
			String[] s={"合作商消息推送",String.valueOf(appPush.getTid()),"修改","修改"};
			appPushService.fireLoginEvent(s,PartnerConstants.FIRELOGIN_NUMA);
		} catch (Exception e) {
			this.log.error("修改异常", e);
			resultable = new Resultable(false, "操作失败");
			//添加到日志记录表
			String[] s={"合作商消息推送",String.valueOf(appPush.getTid()),"修改","修改"};
			appPushService.fireLoginEvent(s,PartnerConstants.FIRELOGIN_NUMB);
		} finally {
			return resultable;
		}
	}
	
	/**
	 * 选择合作商页面
	 * @return
	 */
	@RequestMapping(value = "init/choseJoint/init")
	public String choseJointInit(){
		return "business_cooperation/choseJoint";
	}

	/**
	 * 选择合作商列表
	 * @param joint
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "init/choseJoint/list")
	public Object choseJointlist(TJoint joint) {
		Pageable<TJoint> pageable = new Pageable<TJoint>(joint);
		pageable.setContent(jointService.getList(joint));
		pageable.setTotal(jointService.count(joint));
		return pageable;
	}
}