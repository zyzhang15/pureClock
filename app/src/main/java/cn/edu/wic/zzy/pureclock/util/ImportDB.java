package cn.edu.wic.zzy.pureclock.util;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import android.content.Context;
import android.util.Log;

public class ImportDB {
	private static final String TAG = "util.ImportDB";

	public static final String DB_PATH = Application.DB_PATH; // 数据库存放绝对路径
	public static final String DB_NAME = Application.DB_NAME; // 数据库文件名
	private Context context;

	public ImportDB(Context context) {
		this.context = context;
	}

	public void copyDatabase() {

		try {
			// 执行数据库导入
			InputStream is = this.context.getResources().getAssets().open(Application.DB_NAME); // 欲导入的数据库,存放在android的资源文件中
			String dbfile = DB_PATH + DB_NAME;
			FileOutputStream fos = new FileOutputStream(dbfile);
			byte[] buffer = new byte[1024];
			int length = 0;
			while ((length = is.read(buffer)) > 0) {
				fos.write(buffer, 0, length);
			}
			Log.d(TAG, "导入结束");
			// 关闭流
			fos.flush();
			fos.close();
			is.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
