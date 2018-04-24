package kr.co.mediex.myhealth.sample.views;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import kr.co.mediex.myhealth.sample.R;
import kr.co.mediex.myhealth.sample.SampleApplication;
import kr.co.mediex.myhealth.sample.models.MyBodyInfo;
import kr.co.mediex.myhealth.sample.models.MyUser;
import kr.co.mediex.myhealth.v1.MyHealth;
import kr.co.mediex.myhealth.v1.MyHealthUtils;
import kr.co.mediex.myhealth.v1.domain.User;
import kr.co.mediex.myhealth.v1.function.Error;
import kr.co.mediex.myhealth.v1.function.Success;
import retrofit2.HttpException;

import java.net.SocketTimeoutException;
import java.util.List;
import java.util.Map;

// 로그인 후 유저정보를 확인하거나 샘플 데이터를 보내볼 수 있음.
public class SampleSaveDataActivity extends AppCompatActivity implements View.OnClickListener {

    // 몸무게 입력 뷰
    private EditText mWeightEditText = null;
    // 키 입력 뷰
    private EditText mHeightEditText = null;

    // 유저 이름 뷰
    private TextView mUserNameTextView = null;
    // 유져 이메일 뷰
    private TextView mUserEmailTextView = null;

    // 저장 버튼
    private Button mSaveButton = null;
    // 로그아웃 버튼
    private Button mLogoutButton = null;

    // API를 사용할 수 있는 라이브러리
    private MyHealth mMyHealthService = null;

    // 클릭이벤트 통합처리
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.saveButton:
                this.saveData();    // 저장 관련 예제
//                this.updateData();    // 수정 관련 예제
//                this.deleteData();    // 삭제 관련 예제
//                this.findData();      // 상세 조회 관련 예제
//                this.findAllData();   // 페이지별 조회 관련 예제
                break;
            case R.id.logoutButton:
                this.logout();
                break;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_save);

        this.mMyHealthService = MyHealthUtils.createService(getApplicationContext(),
                SampleApplication.MY_SERVICE_NAME,
                SampleApplication.MY_SERVICE_SECRET);
        this.updateView();
        this.updateUserInfo();
    }

    // 전역뷰를 주입하고 뷰 Listener를 지정
    private void updateView() {
        this.mUserNameTextView = (TextView) findViewById(R.id.userNameTextView);
        this.mUserEmailTextView = (TextView) findViewById(R.id.userEmailTextView);

        this.mWeightEditText = (EditText) findViewById(R.id.weightEditText);
        this.mHeightEditText = (EditText) findViewById(R.id.heightEditText);

        this.mSaveButton = (Button) findViewById(R.id.saveButton);
        this.mSaveButton.setOnClickListener(this);

        this.mLogoutButton = (Button) findViewById(R.id.logoutButton);
        this.mLogoutButton.setOnClickListener(this);
    }

    private void updateUserInfo() {
        // 유저 정보를 가져옴.
        this.mMyHealthService.getCurrentUser(new Success<User>() {
            // 유저 정보를 서버에서 가져오기 성공
            @Override
            public void onSuccess(final User user) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mUserNameTextView.setText("이름 : " + user.getName());
                        mUserEmailTextView.setText("이메일 : " + user.getUsername());
                    }
                });
            }
        }, new Error() {
            // 유저 정보를 서버에서 가져오기 실패
            @Override
            public void onError(Throwable throwable) {
                showToast("에러가 발생하였습니다.");
                throwable.printStackTrace();
            }
        });
    }

    private void showToast(final String message) {
        // 백그라운드 작업 후 호출시 ui thread를 사용
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(SampleSaveDataActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void alert(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(SampleSaveDataActivity.this);
                builder.setCancelable(true)
                        .setMessage(message)
                        .setNegativeButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

    // myHealth 연동 관련 에러 핸들러
    private Error errorHandler = new Error() {
        @Override
        public void onError(Throwable throwable) {
            throwable.printStackTrace();
            if (throwable instanceof SocketTimeoutException) {
                showToast("인터넷연결상태를 확인해주세요.");
            } else if (throwable instanceof HttpException) {
                showToast("서버 에러");
            } else {
                throwable.printStackTrace();
                showToast("에러");
            }
        }
    };

    // 샘플 데이터를 저장해 볼 수 있습니다.
    private void saveData() {
        try {
            // TODO 데이터 유효성검사 필요!
            Double weight = Double.valueOf(this.mWeightEditText.getText().toString());
            Double height = Double.valueOf(this.mHeightEditText.getText().toString());

            // 서비스 사용자 정보
            MyUser myUser = new MyUser();
            // 샘플 유저 아이디를 지정
            myUser.setId((long) (Math.random() * 1000));
            myUser.setName("myUserName");

            MyBodyInfo myBodyInfo = new MyBodyInfo();
            // 샘플 신체정보 아이디 지정
            myBodyInfo.setId((long) (Math.random() * 1000));
            myBodyInfo.setWeight(weight);
            myBodyInfo.setHeight(height);
            myBodyInfo.setMyUser(myUser);

            mMyHealthService.insertResource(SampleApplication.MY_RESOURCE_NAME,
                myBodyInfo,
                    new Success<Object>() {
                        // 샘플 정보를 서버에서 저장하기 정공
                        @Override
                        public void onSuccess(Object o) {
                            showToast("성공적으로 데이터가 저장되었습니다.");
                        }
                    }, errorHandler);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void updateData() {
        try {
            // TODO 데이터 유효성검사 필요!
            Double weight = Double.valueOf(this.mWeightEditText.getText().toString());
            Double height = Double.valueOf(this.mHeightEditText.getText().toString());

            // 서비스 사용자 정보
            MyUser myUser = new MyUser();
            // 샘플 유저 아이디를 지정
            myUser.setId((long) (Math.random() * 1000));
            myUser.setName("myUserName");

            MyBodyInfo myBodyInfo = new MyBodyInfo();
            // 샘플 신체정보 아이디 지정
            myBodyInfo.setId((long) (Math.random() * 1000));
            myBodyInfo.setWeight(weight);
            myBodyInfo.setHeight(height);
            myBodyInfo.setMyUser(myUser);

            mMyHealthService.updateResource(SampleApplication.MY_RESOURCE_NAME, "3",
                    myBodyInfo,
                    new Success<Object>() {
                        // 샘플 정보를 서버에서 저장하기 정공
                        @Override
                        public void onSuccess(Object o) {
                            showToast("성공적으로 데이터가 수정되었습니다.");
                        }
                    }, errorHandler);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void deleteData() {
        try {
            mMyHealthService.deleteResource(SampleApplication.MY_RESOURCE_NAME, "2", new Success<Object>() {
                @Override
                public void onSuccess(Object o) {
                    showToast("성공적으로 데이터를 삭제하였습니다.");
                }
            }, errorHandler);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void findData() {
        try {
            mMyHealthService.findResource(SampleApplication.MY_RESOURCE_NAME, "4",
                    new Success<Map<String, Object>>() {
                        @Override
                        public void onSuccess(Map<String, Object> stringObjectMap) {
                            alert(stringObjectMap.toString());
                        }
                    }, errorHandler);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void findAllData() {
        try {
            mMyHealthService.findResources(SampleApplication.MY_RESOURCE_NAME, "1",
                    new Success<List<Map<String, Object>>>() {
                        @Override
                        public void onSuccess(List<Map<String, Object>> maps) {
                            alert(maps.toString());
                        }
                    }, errorHandler);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    // 라이브러리에 저장된 토큰값을 삭제하고 로그인 페이지로 넘어감
    private void logout() {
        mMyHealthService.logout();
        Intent splashActivityIntent = new Intent(getApplicationContext(), SplashActivity.class);
        splashActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(splashActivityIntent);
    }
}
