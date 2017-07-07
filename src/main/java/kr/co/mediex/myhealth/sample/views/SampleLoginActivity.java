package kr.co.mediex.myhealth.sample.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import kr.co.mediex.myhealth.sample.R;
import kr.co.mediex.myhealth.sample.SampleApplication;
import kr.co.mediex.myhealth.v1.view.LoginActivity;
import kr.co.mediex.myhealth.v1.view.MyHealthActivity;

// 샘플 로그인 화면
public class SampleLoginActivity extends AppCompatActivity {

	// 로그인 버튼
	Button mLoginButton = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_main);
		this.updateView();
	}

	// 전역뷰를 주입하고 뷰 Listener를 지정
	private void updateView() {
		this.mLoginButton = (Button) findViewById(R.id.loginButton);
		this.mLoginButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final Intent loginActivityIntent = new Intent(getApplicationContext(), LoginActivity.class);
				// 라이브러리 Activity에 정보를 전달
				loginActivityIntent.putExtra(MyHealthActivity.INTENT_KEY_SERVICE_NAME, SampleApplication.MY_SERVICE_NAME);
				loginActivityIntent.putExtra(MyHealthActivity.INTENT_KEY_SERVICE_SECRET, SampleApplication.MY_SERVICE_SECRET);
				loginActivityIntent.putExtra(MyHealthActivity.INTENT_KEY_RESOURCE_NAME, SampleApplication.MY_RESOURCE_NAME);
				startActivityForResult(loginActivityIntent, 1);
			}
		});
	}

	// 라이브러리 로그인 Activity에서 로그인 성공하면 실행
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == MyHealthActivity.SUCCESS_CODE) {
			Intent saveDataActivityIntent = new Intent(getApplicationContext(), SampleSaveDataActivity.class);
			saveDataActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
			startActivity(saveDataActivityIntent);
		}
	}
}
