package com.renren.shopping.conf;

public class GoodConf {
	public static final String GOODKEY = "good_info";
	public static final String GOOD_ID = "good_id";
	public static final String GOOD_NAME = "good_name";
	public static final String GOOD_DESCRIPTION = "good_desc";
	public static final String GOODD_EFAULT_PHOTO_URL = "good_default_photo_url";
	public static final String GOOD_DEFAULT_MAIN_URL = "good_default_main_url";
	public static final String GOOD_DEFAULT_HEAD_URL = "good_default_head_url";
	public static final String GOOD_DEFAULT_TINY_URL = "good_default_tiny_url";
	/*** 商品图片信息 */
	public static final String GOODPHOTOS = "good_photos";
	public static final String GOODPHOTOID = "good_photo_id";
	public static final String GOOD_PHOTO_URL = "good_photo_url";
	public static final String GOOD_MAIN_URL = "good_main_url";
	public static final String GOOD_HEAD_URL = "good_head_url";
	public static final String GOOD_TINY_URL = "good_tiny_url";
	public static final String GOODPHOTODESCRIPTION = "good_photo_desc";
	/** 是否收藏商品信息 1收藏，0未收藏 */
	public static final String GOOD_IS_COLLECTIONED = "good_is_collection";

	/**************************************************/
	/** 收藏信息 */
	public static final String COLLECTIONKEY = "collection_info";
	public static final String COLLECTION_USER_ID = "collection_user_id";
	public static final String COLLECTION_PHOTO_ID = "collection_photo_id";
	public static final String COLLECTION_GOOD_ID = "collection_good_id";
	/** 收藏的商品对应的travel id */
	public static final String COLLECTION_TRAVEL_ID = "collection_travel_id";
	/** 收藏的商品对应的travel或者task id 是否被删除，0没有，1被删除 */
	public static final String COLLECTION_TRAVEL_DELETEFLAG = "collection_travel_deleteflag";
	/** 收藏人气 */
	public static final String COLLECTION_POPULARITY = "collection_popularity";
	/** 用户收藏页面，默认展示条数 */
	public static final int USER_COLLECTION_DEFAULT_NUM = 20;
}
