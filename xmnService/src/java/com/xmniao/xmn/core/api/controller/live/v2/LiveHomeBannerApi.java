package com.xmniao.xmn.core.api.controller.live.v2;

import com.xmniao.xmn.core.api.controller.live.AnchorAnnunciateListApi;
import com.xmniao.xmn.core.base.BaseRequest;
import com.xmniao.xmn.core.base.BaseResponse;
import com.xmniao.xmn.core.base.BaseVControlInf;
import com.xmniao.xmn.core.common.ResponseCode;
import com.xmniao.xmn.core.live.service.LiveHomeV2Service;
import net.sf.oval.ConstraintViolation;
import net.sf.oval.Validator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 直播首页 banner
 * Created by Administrator on 2017/5/16.
 */
@RequestMapping("/v2")
@Controller
public class LiveHomeBannerApi implements BaseVControlInf {


    private final Logger log = Logger.getLogger(LiveHomeBannerApi.class);

    @Autowired
    private Validator validator;

    @Autowired
    private LiveHomeV2Service liveHomeV2Service;

    /**
     * 缤纷娱乐，节目频道
     * @return
     */
    @RequestMapping(value = "/live/home/banner" ,method= RequestMethod.POST,produces={"application/json;charset=utf-8"})
    @ResponseBody
    public Object homeBannerList(BaseRequest request) {
        log.info("homeTvShowList data:" + request.toString());
        //验证
        List<ConstraintViolation> result = validator.validate(request);
        if(result != null && result.size()>0){
            log.info("提交的数据有问题"+result.get(0).getMessage());
            return new BaseResponse(ResponseCode.DATAERR,"提交的数据不正确!");
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

    public Object versionOne(Object obj){
        BaseRequest request = (BaseRequest)obj;
        return liveHomeV2Service.getBannerList(request);
    }
}