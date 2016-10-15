package cn.edu.wic.zzy.pureclock.http;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import cn.edu.wic.zzy.pureclock.bean.CityInfoBean;
import cn.edu.wic.zzy.pureclock.util.Application;

public class JsonDataProvider {

    private String URL;
    private String KEY;
    private JsonDataProviderCallBack CallBack;// 网络请求结束后需要调用的接口
    private int whatData;

    public JsonDataProvider(JsonDataProviderCallBack CallBack, int whatData) {
        this.CallBack = CallBack;
        this.whatData = whatData;
        switch (whatData) {
            case Application.CITY_NAME:
                URL = Application.gecodingProviderURL;
                KEY = Application.gecodingProviderKEY;
                break;

            case Application.WEATHER_INFO:
                URL = Application.weatherProviderURL;
                KEY = Application.weatherProviderKEY;
                break;
        }
    }

    public void execuse(String parm) {
        new myAsyncTask().execute(parm);
    }

    class myAsyncTask extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... parms) {
            //////////////////////////////////////////////////////////////////////////////////////////
            List<NameValuePair> parm = new ArrayList<NameValuePair>();
            switch (whatData) {
                case Application.CITY_NAME:
                    parm.add(new BasicNameValuePair("location", parms[0]));
                    break;
                case Application.WEATHER_INFO:
                    parm.add(new BasicNameValuePair("cityname", parms[0]));
                    break;
            }
            parm.add(new BasicNameValuePair("key", KEY));

            HttpPost post = new HttpPost(URL);
            HttpResponse response = null;
            String jsonString = null;
            try {
                post.setEntity(new UrlEncodedFormEntity(parm, "UTF-8"));
                response = new DefaultHttpClient().execute(post);
                if (response.getStatusLine().getStatusCode() == 200) {
                    // 将一个响应流转化为String，再将String转化为Json数据
                    jsonString = EntityUtils.toString(response.getEntity());
                }
                ///////////////////////////////////////////////////////////////////////////////////////
            } catch (Exception e) {
                e.printStackTrace();
            }
            return jsonString;
        }

        @Override
        protected void onPostExecute(String jsonString) {
            super.onPostExecute(jsonString);
            switch (whatData) {

                case Application.CITY_NAME:
                    getCityInfo(jsonString);
                    break;

                case Application.WEATHER_INFO:
                    getWeatherInfo(jsonString);
                    break;

            }

        }

    }


    /**
     * 解析城市信息
     *
     * @param jsonString
     */
    public void getCityInfo(String jsonString) {
        Gson gson=new Gson();
        String type = (new TypeToken<CityInfoBean>() {
        }.getType()).toString();
        String city = "--";
        Log.i("TypeToken.getType()", type);
        CityInfoBean cityinfo=gson.fromJson(jsonString, new TypeToken<CityInfoBean>() {
        }.getType());

////////////////////////////////////////////////////////////////////////////////////////////////////


            if (cityinfo.getError_code()== 0) {
                String cit = cityinfo.getResult().getData().getRealtime().getCity_name();
                String province = jsonString.getJSONObject("regeocode").getJSONObject("addressComponent")
                        .getString("province") + "";
                if (!"[]".equals(city) && null != city) {
                    city = cit;
                } else if (!"[]".equals(province) && null != province) {
                    city = province;
                }
            }

////////////////////////////////////////////////////////////////////////////////////////////////////
        Map<String, Object> map = new HashMap<>();
        map.put(Application.city, city);
        CallBack.callBack(map);

    }

    /**
     * 解析天气信息
     *
     * @param jsonString
     */
    public void getWeatherInfo(String jsonString) {
        boolean getWeatherNormal = false;// 用于判断是否可以获取天气
        String temperature = "--";//
        String info = "--";//
        String city = "--";
        if (json != null) {
            if ("successed!".equals(json.getString("reason"))) {
                getWeatherNormal = true;// 如果获取天气数据成功
                temperature = json.getJSONObject("result").getJSONObject("data").getJSONObject("realtime")
                        .getJSONObject("weather").getString("temperature");
                info = json.getJSONObject("result").getJSONObject("data").getJSONObject("realtime")
                        .getJSONObject("weather").getString("info");
                city = json.getJSONObject("result").getJSONObject("data").getJSONObject("realtime")
                        .getString("city_name");

                temperature += "℃";
            }
        }
        Map<String, Object> map = new HashMap<>();
        map.put(Application.getWeatherNormal, getWeatherNormal);
        map.put(Application.city, city);
        map.put(Application.info, info);
        map.put(Application.temperature, temperature);
        CallBack.callBack(map);
    }

    /**
     * 回调接口
     *
     * @author z4463
     */
    public interface JsonDataProviderCallBack {
        public void callBack(Map<String, Object> map);
    }
}
