package kr.co.mediex.myhealth.sample.models;

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
