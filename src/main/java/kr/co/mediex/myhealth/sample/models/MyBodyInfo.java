package kr.co.mediex.myhealth.sample.models;

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
