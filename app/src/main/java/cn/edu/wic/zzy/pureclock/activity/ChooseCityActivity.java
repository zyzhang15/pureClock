package cn.edu.wic.zzy.pureclock.activity;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import cn.edu.wic.zzy.pureclock.R;
import cn.edu.wic.zzy.pureclock.util.Application;
import cn.edu.wic.zzy.pureclock.util.DataHelper;

public class ChooseCityActivity extends Activity {
	private ListView listView;
	private ProgressBar progressBar;
	private TextView noLocationTips;
	private SQLiteDatabase database;
	private EditText inputCity;
	private ArrayAdapter<String> adapter;
	private DataHelper dataHelper;
	private ArrayList<String> citys;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_city);
		dataHelper = new DataHelper(this);
		database = dataHelper.getReadableDatabase();

		inputCity = (EditText) findViewById(R.id.inputcity);
		noLocationTips = (TextView) findViewById(R.id.no_location_tips);
		listView = (ListView) findViewById(R.id.list_view);
		progressBar = (ProgressBar) findViewById(R.id.progress_bar);
		citys = new ArrayList<String>();
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, citys);

		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// 传来的城市信息格式为："武汉,wuhan,430000",使用时仅需要"武汉"，所以剪切出"武汉"
				String city = citys.get(position).split(",")[0];
				Intent intent = new Intent();
				intent.putExtra(Application.city, city);
				setResult(RESULT_OK, intent);
				finish();
			}
		});

		inputCity.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
			}

			@Override
			public void afterTextChanged(Editable input) {
				citys.clear();
				if (input.length() >= 2) {
					new MyAsyncTask().execute(input.toString());
				}
				listView.setAdapter(adapter);
			}

		});
	}

	class MyAsyncTask extends AsyncTask<String, Integer, Boolean> {

		@Override
		protected void onPreExecute() {
			// 产生一个旋转进度条，表示正在查询数据
			noLocationTips.setVisibility(View.GONE);
			progressBar.setVisibility(View.VISIBLE);
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(String... inputWord) {
			boolean result = false;
			String input = inputWord[0];
			if (input.length() >= 2) {
				String table = Application.CityTable;// 查询的表名
				String[] columns = null;// 查询的列
				String selection = " CityName like ? or pinyin like ? or ZipCode like ?";// 查询条件
				String[] selectionArgs = { input + "%", input + "%", input + "%" };// 查询条件中的填充字符

				Cursor cursor = database.query(table, columns, selection, selectionArgs, null, null, null, null);

				if (cursor.moveToFirst()) {
					result = true;
					do {
						String cityName = cursor.getString(cursor.getColumnIndex("CityName"));
						String pingYing = cursor.getString(cursor.getColumnIndex("pinyin"));
						String zipCode = cursor.getString(cursor.getColumnIndex("ZipCode"));

						String item = cityName + "," + pingYing + "," + zipCode;
						citys.add(item);
					} while (cursor.moveToNext());
					cursor.close();
				}

			}
			return result;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			// 数据加载结束将进度条关闭
			if (true == result) {
				progressBar.setVisibility(View.INVISIBLE);
				listView.setAdapter(adapter);
			} else {
				progressBar.setVisibility(View.GONE);
				noLocationTips.setVisibility(View.VISIBLE);

			}

		}
	}

}
