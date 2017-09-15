package com.xmniao.xmn.core.sellerPackage.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xmniao.xmn.core.common.ResponseCode;
import com.xmniao.xmn.core.market.common.Response;
import com.xmniao.xmn.core.sellerPackage.request.RecomboRequest;
import com.xmniao.xmn.core.sellerPackage.service.ComboService;

import net.sf.oval.ConstraintViolation;
import net.sf.oval.Validator;

/**
 * 
* @projectName: xmnService 
* @ClassName: RecomboController    
* @Description:推荐套餐controller   
* @author: liuzhihao   
* @date: 2017年2月20日 下午3:58:08
 */
@Controller
@RequestMapping("/combo")
public class RecomboController {

	//注入数据验证service
	@Autowired
	private Validator validator;
	
	//注入套餐service
	@Autowired
	private ComboService comboService; 
	
	@RequestMapping(value="/recommend",method=RequestMethod.POST,produces={"application/json;charset=UTF-8"})
	@ResponseBody
	public Object recommendCombo(RecomboRequest recomboRequest){
		//验证数据
		List<ConstraintViolation> result = validator.validate(recomboRequest);
		if(result != null && !result.isEmpty()){
			String message ="";
			for(ConstraintViolation vo : result){
                message+=vo.getMessage()+",";
            }
			
			return new Response(ResponseCode.DATAERR, message.substring(0, message.length()-1));
		}
		return comboService.getRecommendCombo(recomboRequest);
	}
}
