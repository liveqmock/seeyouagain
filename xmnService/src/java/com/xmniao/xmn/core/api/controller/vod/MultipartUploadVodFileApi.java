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

import com.xmniao.xmn.core.base.BaseResponse;
import com.xmniao.xmn.core.base.BaseVControlInf;
import com.xmniao.xmn.core.common.ResponseCode;
import com.xmniao.xmn.core.common.request.vod.DescribeVodPlayInfoRequest;
import com.xmniao.xmn.core.common.request.vod.MultipartUploadVodFileRequest;
import com.xmniao.xmn.core.vod.service.LiveVodService;



/**
 * 
* 上传视频    
* 类名称：MultipartUploadVodFileApi   
* 类描述：   
* 创建人：xiaoxiong   
* 创建时间：2016年8月26日 上午9:05:25   
* 修改人：xiaoxiong   
* 修改时间：2016年8月26日 上午9:05:25   
* 修改备注：   
* @version    
*
 */
@Controller
@RequestMapping(value="/vod")
public class MultipartUploadVodFileApi implements BaseVControlInf{
	
	/**
	 * 日志
	 */
	private final Logger log = Logger.getLogger(MultipartUploadVodFileApi.class);
		
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
	
	@RequestMapping(value="/MultipartUploadVodFile",method=RequestMethod.GET,produces={"application/json;charset=UTF-8"})
	@ResponseBody
	public Object MultipartUploadVodFile(MultipartUploadVodFileRequest request){
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
		MultipartUploadVodFileRequest request = (MultipartUploadVodFileRequest) object;
		return LiveVodService.MultipartUploadVodFile(request);
	}

}
