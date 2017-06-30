package kr.co.mediex.myhealth.sample.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.AppCompatActivity;
import kr.co.mediex.myhealth.ContextMyHealthService;
import kr.co.mediex.myhealth.sample.SampleApplication;

/**
 * 시작 activity
 */
public class SplashActivity extends AppCompatActivity {

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ContextMyHealthService mMyHealthService = new ContextMyHealthService(getApplicationContext(), SampleApplication.MY_SERVICE_NAME,
				SampleApplication.MY_SERVICE_SECRET,
				SampleApplication.MY_RESOURCE_NAME);
		// 토큰이 있는지 없는지 검사!
		if (mMyHealthService.hasToken()) {
			Intent sendDataActivityIntent = new Intent(getApplicationContext(), SampleSaveDataActivity.class);
			sendDataActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
			startActivity(sendDataActivityIntent);
		} else {
			Intent loginActivityIntent = new Intent(getApplicationContext(), SampleLoginActivity.class);
			loginActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
			startActivity(loginActivityIntent);
		}
	}

}