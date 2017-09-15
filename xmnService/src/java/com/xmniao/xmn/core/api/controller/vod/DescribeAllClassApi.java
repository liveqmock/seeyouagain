package com.xmniao.xmn.core.api.controller.vod;

import java.util.List;

import net.sf.oval.ConstraintViolation;
import net.sf.oval.Validator;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xmniao.xmn.core.base.BaseRequest;
import com.xmniao.xmn.core.base.BaseResponse;
import com.xmniao.xmn.core.base.BaseVControlInf;
import com.xmniao.xmn.core.common.ResponseCode;
import com.xmniao.xmn.core.vod.service.LiveVodService;


/**
 * 
*      
* 类名称：DescribeAllClassApi   
* 类描述：   获取分类信息
* 创建人：xiaoxiong   
* 创建时间：2016年8月25日 上午10:31:19   
* 修改人：xiaoxiong   
* 修改时间：2016年8月25日 上午10:31:19   
* 修改备注：   
* @version    
*
 */
@Controller
@RequestMapping(value="/live/vod")
public class DescribeAllClassApi implements BaseVControlInf{
	
		/**
		 * 日志
		 */
		private final Logger log = Logger.getLogger(DescribeVodinfoApi.class);
			
		/**
		 * 注入service
		 */
		@Autowired
		private LiveVodService LiveVodService;
		
		/**
		 * 注入验证
		 */
		@Autowired
		private Validator validator;
		
		@RequestMapping(value="/DescribeAllClass",method=RequestMethod.GET,produces={"application/json;charset=UTF-8"})
		@ResponseBody
		public Object DescribeAllClass(BaseRequest request){
			//验证参数
			log.info("request data:"+request.toString());
			List<ConstraintViolation> result = validator.validate(request);
			if(result != null && result.size()>0){
				log.info("提交的数据有问题"+result.get(0).getMessage());
				return new BaseResponse(ResponseCode.DATAERR,result.get(0).getMessage());
			}
			return versionControl(request.getApiversion(), request);
			
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

		private Object versionOne(Object object) {
			BaseRequest request = (BaseRequest) object;
			return LiveVodService.DescribeAllClass(request);
		}

}