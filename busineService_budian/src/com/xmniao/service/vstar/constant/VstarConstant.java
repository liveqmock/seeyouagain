/**
 * 
 */
package com.xmniao.service.vstar.constant;

/**
 * 
 * 项目名称：XmnWeb_vstar
 * 
 * 类名称：VstarConstant
 * 
 * 类描述： 新时尚大赛常量实体类
 * 
 * 创建人：  huang'tao
 * 
 * 创建时间：2017-6-3 下午5:06:21 
 * 
 * Copyright (c) 广东寻蜜鸟网络技术有限公司 
 */

public class VstarConstant {

	/**
	 * 报名相册图片类型,101.个人风采照 ;201.身份证正面照 202.身份证反面照 203.手持身份证照
	 */
    public static class ENROLL_IMG_TYPE{
		/**
		 * 101.个人风采照 
		 */
		public static final String IMG_101="101";
		
		/**
		 * 201.身份证正面照
		 */
		public static final String IMG_201="201";
		
		/**
		 * 202.身份证反面照
		 */
		public static final String IMG_202="202";
		
		/**
		 * 203.手持身份证照
		 */
		public static final String IMG_203="203";
	}
    
    /**
	 * 参赛状态 1.已报名 2.报名审核通过  3.报名审核拒绝 4.实名认证待审核 5.实名认证通过 6.实名认证拒绝 7.试播审核通过 8.试播审核拒绝
	 */
    public static class ENROLL_STATUS{
		/**
		 * 1.已报名
		 */
		public static final int STATUS_1 = 1;
		
		/**
		 * 2.报名审核通过
		 */
		public static final int STATUS_2 = 2;
		
		
		/**
		 * 3.报名审核拒绝
		 */
		public static final int STATUS_3 = 3;
		
		/**
		 * 4.实名认证待审核
		 */
		public static final int STATUS_4 = 4;
		
		/**
		 * 5.实名认证通过
		 */
		public static final int STATUS_5 = 5;
		
		/**
		 * 6.实名认证拒绝
		 */
		public static final int STATUS_6 = 6;
		
		/**
		 * 7.试播审核通过
		 */
		public static final int STATUS_7 = 7;
		
		/**
		 * 8.试播审核拒绝
		 */
		public static final int STATUS_8 = 8;
		
	}
    
    /**
	 * 选手类型 0.海选选手(报名审核通过) 1.复赛选手(试播审核通过) 2.总决赛选手
	 */
    public static class PLAYER_TYPE{
		/**
		 * 0.海选选手(报名审核通过)
		 */
		public static final Integer TYPE_0=0;
		
		/**
		 * 1.复赛选手(试播审核通过)
		 */
		public static final Integer TYPE_1=1;
		
		/**
		 * 2.总决赛选手
		 */
		public static final Integer TYPE_2=2;
		
	}
    
    /**
	 * 领取状态，1领取成功，2领取失败
	 */
    public static class REWARD_RECEIVE_STATUS{
		/**
		 * 1领取成功
		 */
		public static final Integer SUCCESS=1;
		
		/**
		 * 2领取失败
		 */
		public static final Integer FAIL=2;
		
		
	}
}