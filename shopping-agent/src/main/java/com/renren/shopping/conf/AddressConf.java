package com.renren.shopping.conf;

public class AddressConf {
	public static final String ADDRESSKEY = "address_info";

	public static final String ADDRESS_ID = "address_id";
	/** 收件人 */
	public static final String RECEIVER_NAME = "receiver_name";
	/** 详细信息 */
	public static final String ADDRESS_DETAIL = "address_detail";
	// 下面是detail中的详细字段 _code暂时没有用上
	public static final String LONGITUDE = "longitude";//经度
	public static final String LONGITUDE_CODE = "longitude_code";
	public static final String LATITUDE = "latitude";//纬度
	public static final String LATITUDE_CODE = "latitude_code";
	public static final String PROVINCE = "province";
	public static final String PROVINCE_CODE = "province_code";
	public static final String CITY = "city";
	public static final String CITY_CODE = "city_code";
	public static final String COUNTY = "county";
	public static final String COUNTY_CODE = "county_code";
	public static final String STREET = "street";
	public static final String CONTINENT = "continent";
	public static final String CONTINENT_CODE = "continent_code";
	public static final String COUNTRY = "country";
	public static final String COUNTRY_CODE = "country_code";
	/** 邮编 */
	public static final String ZIPCODE = "zipcode";
	/** 电话 */
	public static final String TEL = "tel";
	/** 是否为默认地址;0否;1是 */
	public static final String ADDRESS_IS_DEFAULT = "address_is_default";

}
