MyHealth Android Library 연동
---

## Overview

`MyHealth`에 로그인하고 데이터를 보낼 수 있는 안드로이드 라이브러리입니다.
[MyHealth-0.0.1.arr](https://github.com/MediexcalExcellence/MyHealth-android-sample/raw/master/app/libs/myhealth-0.0.1.aar)를 다운받아서 사용합니다.
 `minSdkVersion`은 **16**(JellyBean)부터입니다. 권장 `targeSdk`는 **21**(Lollipop)이상입니다. 
 
서비스 비동기 처리는 Callback(Success, Error)과 함께 `RxJava` Observable도 지원합니다.

### Setup

1. 다운받은 **MyHealth-0.0.1.arr**을 /{프로젝트 폴더}/{모듈 폴더}/libs/에 넣어줍니다. 

2. > `build.gradle`
	```groovy
	...
	dependencies {
		compile(name: 'myhealth-0.0.1', ext: 'aar')
		...
	}
	...
	repositories {
		...
		flatDir {
			dirs 'libs'
		}
	}
	```

### Example

#### 서비스 사용 및 초기설정

> SampleApplication.java

```java
public class SampleApplication extends Application {

    // 서비스 이름
    public static String MY_SERVICE_NAME = "myhealth";
    // 서비스 생성 계정 비밀번호
    public static String MY_SERVICE_SECRET = "myhealthPassword1";
    // 리소스 이름
    public static String MY_RESOURCE_NAME = "bloodsugar";

}
```

#### 로그인

> SampleLoginActivity.java

```java
...
// view setup
this.mLoginButton.setOnClickListener(new View.OnClickListener() {
	@Override
	public void onClick(View v) {
		final Intent loginActivityIntent = new Intent(getApplicationContext(), LoginActivity.class);
		loginActivityIntent.putExtra(MyHealthActivity.INTENT_KEY_SERVICE_NAME, SampleApplication.MY_SERVICE_NAME);
		loginActivityIntent.putExtra(MyHealthActivity.INTENT_KEY_SERVICE_SECRET, SampleApplication.MY_SERVICE_SECRET);
		loginActivityIntent.putExtra(MyHealthActivity.INTENT_KEY_RESOURCE_NAME, SampleApplication.MY_RESOURCE_NAME);
		startActivityForResult(loginActivityIntent, 1);
	}
});
...
```

#### 로그인 성공

> SampleLoginActivity.java

```java
...
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	if (resultCode == MyHealthActivity.SUCCESS_CODE) {
		Intent saveDataActivityIntent = new Intent(getApplicationContext(), SampleSaveDataActivity.class);
		saveDataActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
		startActivity(saveDataActivityIntent);
	}
}
...
```

#### 토큰 저장상태확인

> SplashActivity.java

```java
...
ContextMyHealthService mMyHealthService = new ContextMyHealthService(getApplicationContext(),
 				SampleApplication.MY_SERVICE_NAME,
				SampleApplication.MY_SERVICE_SECRET,
				SampleApplication.MY_RESOURCE_NAME);

if (mMyHealthService.hasToken()) {
	// 저장된 토큰이 있음
	...
} else {
	// 저장된 토큰이 없음
	...
}
...
```

#### 유져 정보 가져오기

> SaveDataActivity.java

```java
private void updateUserInfo() {
	// 유저 정보를 가져옴.
	this.mMyHealthService.getUser(new Success<User>() {
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
```

#### 데이터 저장하기

> MyBodyInfo.java

```java
// 샘플 신체정보
public class MyBodyInfo {

    private Long id;
    // 키
    private Double height;
    // 뭄무게
    private Double weight;
    // 샘플 유저
    private MyUser myUser;

    public MyUser getMyUser() {
        return myUser;
    }

    public void setMyUser(MyUser myUser) {
        this.myUser = myUser;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }
}
```

> MyUser.java

```java
// 샘플 유저정보
public class MyUser {

    private Long id = null;
    // 유저이름
    private String name = null;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
```

> SampleSaveDataActivity.java

```java
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

mMyHealthService.saveData(myBodyInfo, new Success<Object>() {
	// 샘플 정보를 서버에서 저장하기 정공
	@Override
	public void onSuccess(Object o) {
		showToast("성공적으로 데이터를 전송하였습니다.");
	}
}, new Error() {
	// 샘플 정보를 서버에서 저장하기 실패
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
```

#### 로그아웃

```java
mMyHealthService.logout();
Intent splashActivityIntent = new Intent(getApplicationContext(), SplashActivity.class);
splashActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
startActivity(splashActivityIntent);
```
