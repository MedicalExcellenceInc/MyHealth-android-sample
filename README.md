MyHealth Android Library
---

### 목차

   - 안내
   - 설정
   - 로그인
   - 리소스
   - 업데이트 내역

# 안내

안드로이드 환경에서 사용자의 측정데이터를 `MyHealth`에 저장하고 공개된 정보를 가져올 수 있는 라이브러리입니다. 
라이브러이를 사용하기 위해선 **서비스이름**과 **키**를 사용하며 협의된 업체에게 발급해드리고 있습니다.

# 설정

필수사항은 안드로이드 *Jelly Bean*(4.1.x/SDK Version 16) 이상 , *자바6*(JDK Version1.6) 이상입니다.

1. [kr.co.mediex.myhealth-1.0.0.arr](https://github.com/MediexcalExcellence/myhealth-sample/raw/master/app/libs/kr.co.mediex.myhealth-1.0.0.arr) 혹은 
개별로 전달된 파일을 다운받습니다.

2. 라이브러리를 `app/lib`폴더에 넣은 후 `app/build.gradle`를 아래와같이 수정해줍니다.

```groovy
...
dependencies {
   compile(name: 'kr.co.mediex.myhealth-1.0.0', ext: 'aar')
   ...
}
...
repositories {
   ...
   flatDir {
       dirs 'libs'
   }
}
...
   ```

# 로그인

라이브러리에서 제공하는 Activity에서 로그인과 회원가입을 진행할 수 있습니다.
Activity가 종료되면 결과를 받을 수 있습니다.
   
1. Activity 호출
   
```java
...
final Intent intent = new Intent(getApplicationContext(), MyHealthWebActivity.class);
intent.putExtra(MyHealthActivity.SERVICE_NAME, MY_SERVICE_NAME);
intent.putExtra(MyHealthActivity.SERVICE_SECRET, MY_SERVICE_SECRET);
startActivityForResult(intent, REQUEST_CODE);
...
```

2. 결과반환

```java
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
 if (resultCode == MyHealthActivity.SUCCESS_CODE) {
    // ... 성공
 } else if (resultCode == MyHealthActivity.ERROR_CODE) {
    // ... 실패
 }
}
```

3. 성공코드가 반환되면 로그아웃 혹은 토큰만료, 앱데이터 삭제전까지 로그인이 유지되어 `kr.co.mediex.myhealth.v1.MyHealth`서비스를 사용할 수 있습니다.

4. 에러 메세지는 *data*에 `ERROR_MESSAGE_KEY`으로 반환됩니다.

5. MyHealth 회원정보 얻기

```java
this.mMyHealthService = MyHealthUtils.createService(getApplicationContext(),
                SampleApplication.MY_SERVICE_NAME,
                SampleApplication.MY_SERVICE_SECRET);

this.mMyHealthService.getCurrentUser(new Success<User>() {
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
            @Override
            public void onError(Throwable throwable) {
                showToast("에러가 발생하였습니다.");
                throwable.printStackTrace();
            }
        });
``` 

위 MyHealthUtils로 생성된 서비스는 회원로그인으로 생성된 토큰이 유효해야 사용가능합니다. 대부분의 서버와 통신하는 함수는 *interface*를 이용한 콜백과 `RxJava`에 **subscribe**을 사용할 수 있습니다.

# 리소스

서비스를 등록시 리소스도 함께 등록됩니다. 측정데이터를 저장하는 `Private resource`와 다른 서비스에서 측정되고 통일된 형식으로 반환하는 `Public resource`가 있습니다.

1. Private resource 저장

```java
mMyHealthService.insertResource(MY_RESOURCE_NAME, myBodyInfo,
        new Success<Object>() {
            @Override
            public void onSuccess(Object o) {
                // 성공
            }
        }, new Error() {
            @Override
            public void onError(Throwable throwable) {
                // 오류
                if (throwable instanceof SocketTimeoutException) {
                    throwable.printStackTrace();
                } else if (throwable instanceof HttpException) {
                    throwable.printStackTrace();
                } else {
                    throwable.printStackTrace();
                }
            }
        });
```

2. Public resource 불러오기

```java
mMyHealthService.findResources(PUBLIC_RESOURCE_NAME,
        new Success<Object>() {
            @Override
            public void onSuccess(Object o) {
                // 성공
            }
        }, new Error() {
            @Override
            public void onError(Throwable throwable) {
                // 오류
                if (throwable instanceof SocketTimeoutException) {
                    throwable.printStackTrace();
                } else if (throwable instanceof HttpException) {
                    throwable.printStackTrace();
                } else {
                    throwable.printStackTrace();
                }
            }
        });
```

# 업데이트 내역