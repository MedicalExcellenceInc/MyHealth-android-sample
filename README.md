MyHealth Android Library (For Private API)
---

### 목차

   - 안내
   - 설정
   - 로그인
   - 리소스
   - 업데이트 내역

# 안내

`MyHealth Private API`를 사용하는 Client가 측정데이터를 보다 쉽게 저장, 수정, 삭제, 조회할 수 있도록 도와주는 라이브러리입니다.
해당 라이브러리를 사용하기 위해서는 먼저 [MyHealth Developer Site (http://developer.my-health.co.kr)](http://developer.my-health.co.kr)에 접속하신 후 해당 페이지에 명시된 가이드라인을 참고하셔서 가입하십시오.
가입과 동시에 제공되는 **서비스 이름**, **서비스 비밀번호**, **리소스 이름**를 이용하면 라이브러리 사용이 가능합니다.

# 설정

필수사항은 안드로이드 *Jelly Bean*(4.1.x/SDK Version 16) 이상 , *자바6*(JDK Version1.6) 이상입니다.

1. [kr.co.mediex.myhealth-1.0.2.arr](https://github.com/MedicalExcellenceInc/myhealth-sample/raw/master/libs/kr.co.mediex.myhealth-1.0.2.aar) 혹은 
개별로 전달된 파일을 다운받습니다.

2. 라이브러리를 `app/lib`폴더에 넣은 후 `app/build.gradle`를 아래와같이 수정해줍니다.

```groovy
...
dependencies {
   compile(name: 'kr.co.mediex.myhealth-x.y.z', ext: 'aar')
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

서비스를 등록시 리소스도 함께 등록됩니다.

1. Resource 저장

```java
mMyHealthService.insertResource(MY_RESOURCE_NAME, myBodyInfo,
        new Success<Object>() {
            @Override
            public void onSuccess(Object o) {
                // 성공
            }
        }, errorHandler);
```

2. Resources 가져오기

```java
mMyHealthService.findResources(SampleApplication.MY_RESOURCE_NAME, "1",
        new Success<List<Map<String, Object>>>() {
            @Override
            public void onSuccess(List<Map<String, Object>> maps) {
                // 성공
            }
        }, errorHandler);
```

3. Resource 가져오기

```java
mMyHealthService.findResource(SampleApplication.MY_RESOURCE_NAME, "4",
        new Success<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> stringObjectMap) {
                alert(stringObjectMap.toString());
            }
        }, errorHandler);
```        

4. Resource 수정하기

```java
mMyHealthService.updateResource(SampleApplication.MY_RESOURCE_NAME, "3",
        myBodyInfo,
        new Success<Object>() {
            @Override
            public void onSuccess(Object o) {
                // 성공
            }
        }, errorHandler);
```

5. Resource 삭제하기

```java
mMyHealthService.deleteResource(SampleApplication.MY_RESOURCE_NAME, "2", 
        new Success<Object>() {
            @Override
            public void onSuccess(Object o) {
                // 성공
            }
        }, errorHandler);
```

# 업데이트 내역
