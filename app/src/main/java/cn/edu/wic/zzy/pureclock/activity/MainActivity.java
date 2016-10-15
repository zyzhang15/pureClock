package cn.edu.wic.zzy.pureclock.activity;

import java.util.List;
import java.util.Map;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import cn.edu.wic.zzy.pureclock.R;
import cn.edu.wic.zzy.pureclock.http.JsonDataProvider;
import cn.edu.wic.zzy.pureclock.http.JsonDataProvider.JsonDataProviderCallBack;
import cn.edu.wic.zzy.pureclock.util.GPSInfoManneger;
import cn.edu.wic.zzy.pureclock.util.GPSInfoManneger.GPSInfoMannegerCallBack;
import cn.edu.wic.zzy.pureclock.util.Application;
import cn.edu.wic.zzy.pureclock.view.ClockView;

public class MainActivity extends Activity
        implements GPSInfoMannegerCallBack, View.OnLongClickListener, View.OnClickListener {
    private static final String USERINFO = "userInfo";
    private static final String USERINFO_CUSTOM_LOCATION = "userInfo.custom.location";
    private static final String USERINFO_CUSTOM_SHOWTIPS = "userInfo.custom.showtips";
    private static final String USERINFO_CUSTOM_WORD = "userInfo.word";
    private static final String USERINFO_PRESENT_LOCATION = "userInfo.present.location";
    private static final String TAG = "activity.MainActivity";
    private static final String DEFAULT_LOCATION = "北京";// 当程序第一次安装时使用的默认位置

    private String TIPS;// 打开应用时展示的的提示信息
    private TextView cityName;// 界面显示城市名的组件
    private TextView tempurature;// 界面显示温度的组件
    private TextView weather;// 界面显示温度的组件
    private ClockView clockView;// 时钟组件
    private View settingLayout;// 程序设置界面
    private TextView inputLocation;// 自定义城市
    private EditText inputWord;// 自定义标志句子
    private ImageButton buttonOK;// 设置中的确定按钮
    private CheckBox checkBoxShowTips;// 关闭或开启打开应用的提示
    private TextView aboutMe;// 打开“关于”界面的按钮文字
    private LocationManager locationManager;// 位置管理器
    private String presentCity;// 存储目前所在城市

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermissions();
        SharedPreferences pref = getSharedPreferences(USERINFO, MODE_PRIVATE);
        TIPS = this.getBaseContext().getString(R.string.tips);
        cityName = (TextView) findViewById(R.id.textCity);
        cityName.setText(pref.getString(USERINFO_CUSTOM_LOCATION, "--"));
        aboutMe = (TextView) findViewById(R.id.about_me);
        aboutMe.setText(Html.fromHtml("<u><b>" + this.getBaseContext().getString(R.string.about_me) + "</b></u>"));// 下划线
        tempurature = (TextView) findViewById(R.id.textTemperature);
        weather = (TextView) findViewById(R.id.textWeather);
        settingLayout = findViewById(R.id.setting_layout);
        settingLayout.setVisibility(View.GONE);// 默认情况下，设置界面为隐藏状态
        inputLocation = (TextView) findViewById(R.id.input_location);
        inputWord = (EditText) findViewById(R.id.input_word);
        checkBoxShowTips = (CheckBox) findViewById(R.id.show_tips);
        buttonOK = (ImageButton) findViewById(R.id.button_ok);
        clockView = (ClockView) findViewById(R.id.clock);
        String city = pref.getString(USERINFO_CUSTOM_LOCATION, presentCity != null ? presentCity : DEFAULT_LOCATION);
        Log.d(TAG, city);
        // 使用城市名称获取天气
        getWeather(city);
        // 控制是否显示提示
        showTips();
        buttonOK.setOnClickListener(this);
        clockView.setOnClickListener(this);
        clockView.setOnLongClickListener(this);
        inputLocation.setOnClickListener(this);

        Log.d(TAG, "位置字符的size:" + cityName.getTextSize());
    }

    private void showTips() {
        SharedPreferences pref = getSharedPreferences(USERINFO, MODE_PRIVATE);
        boolean ShowTips = pref.getBoolean(USERINFO_CUSTOM_SHOWTIPS, true);
        if (ShowTips) {
            Toast.makeText(this, TIPS, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 请求获取权限，并根据权限的授予情况开启相应功能
     */
    private static final int REQUEST_PERMISSION_LOCATION_CODE = 1;

    //get android system version :android.os.Build.VERSION.SDK_INT
    @TargetApi(23)
    private void requestPermissions() {
        // 运行时 权限是在android6.0（SDK23）以后才有，若当前的手机版本低于23则不能使用本特性，手机会自动给予所有权限
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            if (!(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_PERMISSION_LOCATION_CODE);
            } else {
                // 如果已经获取了权限，则直接使用定位功能
                getLocationManager();
            }
        } else {
            getLocationManager();
        }

    }

    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_PERMISSION_LOCATION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 如果同意位置权限请求,则开启定位功能
                    getLocationManager();
                }
                break;
        }

    }

    /**
     * 开启定位服务
     */
    private void getLocationManager() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // 先获取目前位置
        getPresentLocation(locationManager);
        // 然后添加一个监听，获取位置的变化// 每分钟或每1000米获取一次位置信息
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000 * 60, 1000,
                    new GPSInfoManneger(MainActivity.this));
        }

    }

    /**
     * 获取一个较精确的位置信息
     *
     * @param locationManager
     * @return
     */
    private void getPresentLocation(LocationManager locationManager) {
        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            for (String provider : providers) {
                Location l = locationManager.getLastKnownLocation(provider);
                if (l == null) {
                    continue;
                }
                if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                    bestLocation = l;
                }
            }
        }
        if (bestLocation != null) {
            String presentLoc = String.valueOf(bestLocation.getLongitude()) + ","
                    + String.valueOf(bestLocation.getLatitude());
            Log.d(TAG, "目前经纬度:" + bestLocation);
            getCity(presentLoc);
        } else {
            Log.d(TAG, "无法获取手机的经纬度信息。。。。。");
        }

    }

    /**
     * 根据手机经纬度查询所在城市
     */
    @Override
    public void getCity(String Longitude_Latitude) {
        new JsonDataProvider(new JsonDataProviderCallBack() {
            @Override
            public void callBack(Map<String, Object> map) {

                presentCity = (String) map.get(Application.city);
                SharedPreferences.Editor edit = getSharedPreferences(USERINFO, MODE_PRIVATE).edit();
                edit.putString(USERINFO_PRESENT_LOCATION, presentCity);
                edit.commit();
                Log.d(TAG, "位置： " + presentCity);
            }
        }, Application.CITY_NAME).execuse(Longitude_Latitude);
    }

    /**
     * 根据城市名信息获取天气信息
     *
     * @param city
     */
    public void getWeather(String city) {
        new JsonDataProvider(new JsonDataProviderCallBack() {
            @Override
            public void callBack(Map<String, Object> map) {
                boolean getWeatherNormal = (Boolean) map.get(Application.getWeatherNormal);
                String temperature = (String) map.get(Application.temperature);//
                String info = (String) map.get(Application.info);//
                String city = (String) map.get(Application.city);

                SharedPreferences.Editor edit = getSharedPreferences(USERINFO, MODE_PRIVATE).edit();
                if (getWeatherNormal == true) {
                    Log.d(TAG, "获取天气信息正常！");
                    getWeatherNormal = false;// 使用后将其恢复默认false
                    edit.putString(USERINFO_CUSTOM_LOCATION, city);
                    edit.commit();
                    cityName.setText(city);
                    tempurature.setText(temperature);
                    weather.setText(info);
                } else {
                    edit.putString(USERINFO_CUSTOM_LOCATION, cityName.getText().toString());
                    edit.commit();
                    Toast.makeText(MainActivity.this, "无法获取天气信息", Toast.LENGTH_SHORT).show();
                }

            }
        }, Application.WEATHER_INFO).execuse(city);
    }

    // 隐藏视图
    private void weatherInfoHide() {
        cityName.setVisibility(View.INVISIBLE);
        weather.setVisibility(View.INVISIBLE);
        tempurature.setVisibility(View.INVISIBLE);
    }

    // 显示视图
    private void weathInfoShow() {
        cityName.setVisibility(View.VISIBLE);
        weather.setVisibility(View.VISIBLE);
        tempurature.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (View.VISIBLE == settingLayout.getVisibility()) {
                settingLayout.setVisibility(View.GONE);
            } else {
                finish();
            }
        }
        return true;
    }

    public void openAboutMe(View view) {
        settingLayout.setVisibility(View.GONE);
        startActivity(new Intent(this, AboutMeActivity.class));
    }

    private void showSettingLayout() {
        SharedPreferences pref = getSharedPreferences(USERINFO, MODE_PRIVATE);
        if (presentCity != null) {
            inputLocation.setHint("你的位置：" + presentCity);
        }
        if (pref.getBoolean(USERINFO_CUSTOM_SHOWTIPS, true)) {
            checkBoxShowTips.setChecked(true);
        } else {
            checkBoxShowTips.setChecked(false);
        }
        settingLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.clock:
                if (View.VISIBLE == cityName.getVisibility()) {
                    weatherInfoHide();
                } else {
                    weathInfoShow();
                }
                break;
            case R.id.button_ok: {
                String inputlocation = "";
                String inputword = "";
                inputlocation = inputLocation.getText().toString();
                inputword = inputWord.getText().toString();
                clockView.YOUR_WORD = "".equals(inputword) ? clockView.YOUR_WORD : inputword;
                SharedPreferences.Editor edit = getSharedPreferences(USERINFO, MODE_PRIVATE).edit();
                edit.putString(USERINFO_CUSTOM_WORD, clockView.YOUR_WORD);

                if (!"".equals(inputlocation)) {
                    getWeather(inputlocation);

                }
                if (checkBoxShowTips.isChecked()) {
                    edit.putBoolean(USERINFO_CUSTOM_SHOWTIPS, true);
                } else {
                    edit.putBoolean(USERINFO_CUSTOM_SHOWTIPS, false);
                }
                edit.commit();
                // 当设置修改时，将界面文字显示出来，并关闭setting界面
                weathInfoShow();
                settingLayout.setVisibility(View.GONE);
            }
            break;
            case R.id.input_location: {
                // 开启一个新的activity，选择城市后返回
                startActivityForResult(new Intent(MainActivity.this, ChooseCityActivity.class),
                        Application.INTENT_FOR_CITY_NAME);
            }
            default:
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Application.INTENT_FOR_CITY_NAME:
                if (resultCode == RESULT_OK) {
                    inputLocation.setText(data.getStringExtra(Application.city));
                }
                break;

            default:
                break;
        }
    }

    @Override
    public boolean onLongClick(View view) {

        switch (view.getId()) {
            case R.id.clock: {
                showSettingLayout();
                return true;
            }
            default:
                return true;
        }
    }

}
