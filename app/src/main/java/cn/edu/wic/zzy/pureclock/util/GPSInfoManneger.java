package cn.edu.wic.zzy.pureclock.util;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

public class GPSInfoManneger implements LocationListener {
	private static final String TAG = "util.GPSInfoManneger";
	private String Longitude_Latitude;// 存储位置信息 (格式（不含引号）："Longitude,latitude")
	private GPSInfoMannegerCallBack gpsCallback;

	public GPSInfoManneger(GPSInfoMannegerCallBack callback) {
		this.gpsCallback = callback;
	}

	@Override
	public void onLocationChanged(Location location) {
		Log.d(TAG, "onLocationChanged");
		Longitude_Latitude = String.valueOf(location.getLongitude()) + "," + String.valueOf(location.getLatitude());
		gpsCallback.getCity(Longitude_Latitude);
		Log.d(TAG, "(经度,维度):" + String.valueOf(location.getLongitude()) + "," + String.valueOf(location.getLatitude()));// 经度
	}

	@Override
	public void onProviderDisabled(String arg0) {
		Log.d(TAG, "onProviderDisabled");
		Log.d(TAG, "onProviderDisabled"+arg0);
	}

	@Override
	public void onProviderEnabled(String arg0) {
		Log.d(TAG, "onProviderEnabled");
		Log.d(TAG, "onProviderEnabled"+arg0);
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		Log.d(TAG, "onStatusChanged");
	}

	/**
	 * 回调接口
	 * 
	 * @author z4463
	 *
	 */
	public interface GPSInfoMannegerCallBack {
		public void getCity(String Longitude_Latitude);
	}
}
