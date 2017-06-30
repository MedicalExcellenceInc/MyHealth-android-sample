package kr.co.mediex.myhealth.sample.views;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import kr.co.mediex.myhealth.ContextMyHealthService;
import kr.co.mediex.myhealth.function.Success;
import kr.co.mediex.myhealth.function.Error;
import kr.co.mediex.myhealth.sample.R;
import kr.co.mediex.myhealth.sample.SampleApplication;
import kr.co.mediex.myhealth.sample.models.MyBodyInfo;
import kr.co.mediex.myhealth.sample.models.MyUser;
import retrofit2.HttpException;

import java.net.SocketTimeoutException;

public class DataSendActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText weightEditText = null;
    private EditText heightEditText = null;
    private Button sendButton = null;

    private ContextMyHealthService myHealthService = null;
    private Long _id = 1L;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sendButton :
                this.dataSend();
                break;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_send);

        this.myHealthService = new ContextMyHealthService(getApplicationContext(), SampleApplication.MY_SERVICE_NAME, SampleApplication.MY_SERVICE_SECRET, SampleApplication.MY_RESOURCE_NAME);
        this.onCreateInjectView();
    }

    private void showToast(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(DataSendActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onCreateInjectView() {
        this.weightEditText = (EditText) findViewById(R.id.weightEditText);
        this.heightEditText = (EditText) findViewById(R.id.heightEditText);
        this.sendButton = (Button) findViewById(R.id.sendButton);
        this.sendButton.setOnClickListener(this);
    }

    private void dataSend() {
        try {
            Double weight = Double.valueOf(this.weightEditText.getText().toString());
            Double height = Double.valueOf(this.heightEditText.getText().toString());

            // 서비스 사용자 정보
            MyUser myUser = new MyUser();
            myUser.setId(_id++);
            myUser.setName("myUserName");

            MyBodyInfo myBodyInfo = new MyBodyInfo();
            myBodyInfo.setId(_id++);
            myBodyInfo.setWeight(weight);
            myBodyInfo.setHeight(height);
            myBodyInfo.setMyUser(myUser);

            myHealthService.save(myBodyInfo, new Success<Object>() {
                @Override
                public void onSuccess(Object o) {
                    showToast("성공적으로 데이터를 전송하였습니다.");
                }
            }, new Error() {
                @Override
                public void onError(Throwable throwable) {
                    if (throwable instanceof SocketTimeoutException) {
                        showToast("인터넷연결상태를 확인해주세요.");
                    } else if (throwable instanceof HttpException) {
                        showToast("서버 에러");
                    } else {
                        throwable.printStackTrace();
                        showToast("에러");
                    }
                }
            });
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
