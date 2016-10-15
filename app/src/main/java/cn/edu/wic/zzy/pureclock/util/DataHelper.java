package cn.edu.wic.zzy.pureclock.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.database.sqlite.SQLiteOpenHelper;

public class DataHelper extends SQLiteOpenHelper {
	private static final String TAG = "util.DataHelper";
	private Context context;

	public DataHelper(Context context) {
		super(context, Application.DB_NAME, null, 1);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase database) {

		/**
		 * 因为使用的是只读数据库，且数据量比较大，所以采用事先做好数据库，然后在运行时将其复制到 到指定的目录的方式，
		 */
		Log.d(TAG, "开始导入");

		// 在新的线程中执行复制数据的操作
		new Thread(new Runnable() {
			@Override
			public void run() {
				new ImportDB(context).copyDatabase();
			}
		}).start();

	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {

	}

}
