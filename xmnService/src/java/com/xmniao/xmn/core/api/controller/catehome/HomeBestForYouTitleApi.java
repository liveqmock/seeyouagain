package com.xmniao.xmn.core.api.controller.catehome;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xmniao.xmn.core.base.BaseRequest;
import com.xmniao.xmn.core.base.BaseResponse;
import com.xmniao.xmn.core.base.BaseVControlInf;
import com.xmniao.xmn.core.catehome.service.NewXmnHomeService;
import com.xmniao.xmn.core.common.ResponseCode;
import com.xmniao.xmn.core.common.request.catehome.BestForYouTitleRequest;
import com.xmniao.xmn.core.common.request.catehome.CityRequest;
import com.xmniao.xmn.core.common.request.catehome.HomeCateCommetRequest;
import com.xmniao.xmn.core.common.request.catehome.HomeMustBuyRequest;

import net.sf.oval.ConstraintViolation;
import net.sf.oval.Validator;

/**
 * 
* @projectName: xmnService 
* @ClassName: XmnHomeApi    
* @Description: 寻蜜鸟首页controller   
* @author: liuzhihao   
* @date: 2016年11月9日 下午3:03:34
 */
@RequestMapping("/home")
@Controller
public class HomeBestForYouTitleApi implements BaseVControlInf{
	
	/**
	 * 注入寻蜜鸟首页service
	 */
	@Autowired
	private NewXmnHomeService xmnHomeService;
	
	@Autowired
	private Validator validator;
	
	/**
	 * 查询寻蜜鸟首页-banner
	 * @return
	 */
	@RequestMapping(value="/bestForyouTitle",method=RequestMethod.POST,produces={"application/json;charset=UTF-8"})
	@ResponseBody
	public Object getBestForYou(BestForYouTitleRequest request){
		//验证请求数据
		List<ConstraintViolation> requestParam = validator.validate(request);
		if(requestParam != null && !requestParam.isEmpty()){
			return new BaseResponse(ResponseCode.DATAERR,"提交的数据不正确!");
		}
		return versionControl(request.getApiversion(),request);

	}

	public Object versionOne(Object object){
		BestForYouTitleRequest request = (BestForYouTitleRequest) object;
		return xmnHomeService.getBestForYouTitle(request);
	}
	
	@Override
	public Object versionControl(int v, Object object) {
		switch(v){
			case 1:
				return versionOne(object);
			default :
				return new BaseResponse(ResponseCode.ERRORAPIV,"版本号不正确,请重新下载客户端");
		}
	}

}
