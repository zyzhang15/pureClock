package cn.edu.wic.zzy.pureclock.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import cn.edu.wic.zzy.pureclock.R;

public class AboutMeActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setTitle("关于");
		setContentView(R.layout.activity_about_me);

	}

	public void back(View view) {
		
		finish();
	}
}
