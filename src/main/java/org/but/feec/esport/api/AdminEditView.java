package org.but.feec.esport.api;

public class AdminEditView {
    private Long id;
    private String email;
    private String givenName;
    private String nickname;
    private String familyName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) { this.familyName = familyName; }

    @Override
    public String toString() {
        return "AdminEditView{" +
                "email='" + email + '\'' +
                ", givenName='" + givenName + '\'' +
                ", nickname='" + nickname + '\'' +
                ", familyName='" + familyName + '\'' +
                '}';
    }
}
