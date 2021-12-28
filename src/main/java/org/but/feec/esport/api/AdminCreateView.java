package org.but.feec.esport.api;

import java.util.Arrays;

public class AdminCreateView {
    private String email;
    private String givenName;
    private String nickname;
    private String familyName;
    private char[] password;

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

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public char[] getPassword() {
        return password;
    }

    public void setPassword(char[] password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "AdminCreateView{" +
                "email='" + email + '\'' +
                ", givenName='" + givenName + '\'' +
                ", nickname='" + nickname + '\'' +
                ", familyName='" + familyName + '\'' +
                ", password=" + Arrays.toString(password) +
                '}';
    }
}
