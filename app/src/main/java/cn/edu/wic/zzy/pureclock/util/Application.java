package cn.edu.wic.zzy.pureclock.util;

import android.os.Environment;

public class Application extends android.app.Application {

	public static final String weatherProviderURL = "http://op.juhe.cn/onebox/weather/query";//请求天气信息的url
	public static final String weatherProviderKEY = "7e62c9443f63ca8a8ec3d05502ce9221";//请求天气信息的key
	public static final String gecodingProviderURL = "http://restapi.amap.com/v3/geocode/regeo";//请求位置解析的url
	public static final String gecodingProviderKEY = "ab7ee296d3298216e5dfd5034f9b5840";//请求位置解析的key
	public static final String getWeatherNormal = "getWeatherNormal";
	public static final String DB_NAME = "city.db";//数据库名
	public static final String DB_PATH = "/data" + Environment.getDataDirectory().getAbsolutePath()
			+ "/cn.edu.wic.zzy.pureclock/databases/";//数据库绝对路径
	public static final String CityTable = "public_zi_city";
	public static final String city = "city";
	public static final String info = "weatherinfo";
	public static final String temperature = "temperature";
	public static final int WEATHER_INFO = 1;
	public static final int CITY_NAME = 2;
	public static final int INTENT_FOR_CITY_NAME = 23333;

}
