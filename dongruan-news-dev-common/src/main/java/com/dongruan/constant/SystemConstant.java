package com.dongruan.constant;

/**
 * @author zhu
 * @date 2022/1/22 00:15:17
 * @description 常量类
 */
public class SystemConstant {

	/**
	 * redis 的常量
	 */
	public static final String REDIS_USER_TOKEN = "redis_user_token";
	public static final String REDIS_USER_INFO = "redis_user_info";
	public static final String REDIS_ADMIN_TOKEN = "redis_admin_token";

	/**
	 *
	 */
	public static final String REDIS_ALL_CATEGORY = "redis_all_category";
	public static final String REDIS_WRITER_FANS_COUNTS = "redis_writer_fans_counts";
	public static final String REDIS_MY_FOLLOW_COUNTS = "redis_my_follow_counts";
	public static final String REDIS_ARTICLE_ALREADY_READ = "redis_article_already_read";
	public static final String REDIS_ARTICLE_COMMENT_COUNTS = "redis_article_comment_counts";


	/**
	 *
	 */
	public static final Integer COOKIE_MONTH = 30 * 24 * 60 * 60;
	public static final Integer COOKIE_DELETE = 0;




	/**
	 *
	 */
	public static final Integer COMMON_START_PAGE = 1;
	public static final Integer COMMON_PAGE_SIZE = 10;


	/**
	 *
	 */
	public static final String MOBILE_SMSCODE = "mobile:smscode";

	public static final String USER_FACE0 = "http://122.152.205.72:88/group1/M00/00/05/CpoxxFw_8_qAIlFXAAAcIhVPdSg994.png";
	public static final String USER_FACE1 = "http://122.152.205.72:88/group1/M00/00/05/CpoxxF6ZUySASMbOAABBAXhjY0Y649.png";
	public static final String USER_FACE2 = "http://122.152.205.72:88/group1/M00/00/05/CpoxxF6ZUx6ANoEMAABTntpyjOo395.png";
	public static final String FAILED_IMAGE_URL = "https://dongruan-news-dev.oss-cn-guangzhou.aliyuncs.com/faild.jpeg";


	public static final String[] regions = {"北京", "天津", "上海", "重庆",
			"河北", "山西", "辽宁", "吉林", "黑龙江", "江苏", "浙江", "安徽", "福建", "江西", "山东",
			"河南", "湖北", "湖南", "广东", "海南", "四川", "贵州", "云南", "陕西", "甘肃", "青海", "台湾",
			"内蒙古", "广西", "西藏", "宁夏", "新疆",
			"香港", "澳门"};


	public static final String SERVICE_USER = "SERVICE-USER";
	public static final String SERVICE_ARTICLE = "SERVICE-ARTICLE";

}
