package kr.co.mediex.myhealth.sample.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import kr.co.mediex.myhealth.sample.R;
import kr.co.mediex.myhealth.sample.SampleApplication;
import kr.co.mediex.myhealth.view.LoginActivity;
import kr.co.mediex.myhealth.view.MyHealthActivity;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Button loginButton = (Button) findViewById(R.id.loginButton);
		loginButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
				intent.putExtra(MyHealthActivity.INTENT_KEY_SERVICE_NAME, SampleApplication.MY_SERVICE_NAME);
				intent.putExtra(MyHealthActivity.INTENT_KEY_SERVICE_SECRET, SampleApplication.MY_SERVICE_SECRET);
				intent.putExtra(MyHealthActivity.INTENT_KEY_RESOURCE_NAME, SampleApplication.MY_RESOURCE_NAME);
				startActivityForResult(intent, -1);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == MyHealthActivity.SUCCESS_CODE) {
			Log.d("로그인", "성공");
		}
	}
}
