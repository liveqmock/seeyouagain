/**
 * 2016年10月24日 下午8:07:49
 */
package com.xmniao.xmn.core.api.controller.live;

import java.util.List;

import net.sf.oval.ConstraintViolation;
import net.sf.oval.Validator;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xmniao.xmn.core.base.BaseResponse;
import com.xmniao.xmn.core.base.BaseVControlInf;
import com.xmniao.xmn.core.common.ResponseCode;
import com.xmniao.xmn.core.common.request.live.CancelPreSellOrderRequest;
import com.xmniao.xmn.core.common.request.live.PresellOrderRequest;
import com.xmniao.xmn.core.live.service.PresellOrderService;

/**
 * @项目名称：xmnService
 * @类名称：AddPresellOrderApi
 * @类描述：
 * @创建人： yeyu
 * @创建时间 2016年10月24日 下午8:07:49
 * @version
 */
@Controller
public class AddPresellOrderApi implements BaseVControlInf {

		//日志
		private final Logger log = Logger.getLogger(AddPresellOrderApi.class);
		//注入validator
		@Autowired
		private Validator validator;
		@Autowired
		private PresellOrderService presellOrderService;
		/**
		 * 
		* @方法名称: payPresellOrder
		* @描述: 预售订单支付
		* @返回类型 Object
		* @创建时间 2016年10月24日
		* @param preSellOrderRequest
		* @return
		 */
		@RequestMapping(value="/live/payPresellOrder",method=RequestMethod.POST,produces={"application/json;charset=UTF-8"})
		@ResponseBody
		public Object payPresellOrder(PresellOrderRequest preSellOrderRequest){
			//日志
			log.info("preSellOrderRequest data:" + preSellOrderRequest.toString());
			List<ConstraintViolation> result = validator.validate(preSellOrderRequest);
			if(result.size() >0 && result != null){
				log.info("提交的数据有问题"+result.get(0).getMessage());
				return new BaseResponse(ResponseCode.DATAERR,"提交的数据有问题");
			}
			return versionControl(preSellOrderRequest.getApiversion(), preSellOrderRequest);
		}
		
		@Override
		public Object versionControl(int v, Object object) {
			switch(v){
			case 1:
				return versionControlOne(object);
				default :
					return new BaseResponse(ResponseCode.ERRORAPIV,"版本号不正确,请重新下载客户端");
			}
		}
		
		private Object versionControlOne(Object object) {
			PresellOrderRequest  preSellOrderRequest = (PresellOrderRequest) object;
			return presellOrderService.payPresellOrder(preSellOrderRequest);
		}

}
