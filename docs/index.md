---
layout: default
---

MyHealth Android Library 연동
---

## Overview

`MyHealth`에 로그인하고 데이터를 보낼 수 있는 안드로이드 라이브러리입니다.
[MyHealth-0.0.1.arr](https://github.com/MediexcalExcellence/MyHealth-android-sample/raw/master/app/libs/myhealth-0.0.1.aar)에서 다운로드 받을 수 있습니다.

### 1. Setup

1. 다운받은 **MyHealth-0.0.1.arr**을 /{Project}/app/libs/에 넣어줍니다. 

2. > `app/build.gradle`
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

### 2. Example

#### 2.1. 서비스 정보 상수설정

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

#### 2.2. 로그인

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

#### 2.3. 로그인 성공

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

#### 2.4. 토큰 저장상태확인

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

#### 2.5. 유져 정보 가져오기

> SaveDataActivity.java

```java
private void updateUserInfo() {
	// 유저 정보를 가져옴.
	this.mMyHealthService.getUser(new Success<User>() {
		// 유저 정보를 서버에서 가져오기 성공
		@Override
		public void onSuccess(final User user) {
			...
		}
	}, new Error() {
		// 유저 정보를 서버에서 가져오기 실패
		@Override
		public void onError(Throwable throwable) {
			...
		}
	});
}
```

#### 2.6. 데이터 저장하기

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
MyBodyInfo myBodyInfo = new MyBodyInfo();
// 샘플 신체정보 아이디 지정
myBodyInfo.set...
...


mMyHealthService.saveData(myBodyInfo, new Success<Object>() {
	// 샘플 정보를 서버에서 저장하기 정공
	@Override
	public void onSuccess(Object o) {
		...
	}
}, new Error() {
	// 샘플 정보를 서버에서 저장하기 실패
	@Override
	public void onError(Throwable throwable) {
		...
	}
});
```

#### 2.7. 로그아웃

```java
mMyHealthService.logout();
```

### 3. 샘플 스크린샷

#### 3.1. Splash
![](docs/assets/img/screenshot_login.png)
#### 3.1. Login
![](docs/assets/img/screenshot_login2.png)
#### 3.1. Splash
![](docs/assets/img/screenshot_main.png)