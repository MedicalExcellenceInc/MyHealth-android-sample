package kr.co.mediex.myhealth.sample.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import kr.co.mediex.myhealth.v1.MyHealth;
import kr.co.mediex.myhealth.sample.SampleApplication;
import kr.co.mediex.myhealth.v1.MyHealthUtils;
import kr.co.mediex.myhealth.v1.adapter.ContextOAuthTokenAdapter;

/**
 * 시작 activity
 */
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyHealth mMyHealthService = MyHealthUtils.createService(getApplicationContext(),
                SampleApplication.MY_SERVICE_NAME,
                SampleApplication.MY_SERVICE_SECRET,
                SampleApplication.MY_RESOURCE_NAME);
        // 토큰이 있는지 없는지 검사!
        if (mMyHealthService.hasToken()) {
            Intent sendDataActivityIntent = new Intent(getApplicationContext(), SampleSaveDataActivity.class);
            sendDataActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(sendDataActivityIntent);
        } else {
            Intent loginActivityIntent = new Intent(getApplicationContext(), SampleLoginActivity.class);
            loginActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(loginActivityIntent);
        }
    }

}