package org.but.feec.esport.api;

import java.sql.Timestamp;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


public class AdminDetailView {
    private LongProperty id = new SimpleLongProperty();
    private StringProperty email = new SimpleStringProperty();
    private StringProperty givenName = new SimpleStringProperty();
    private StringProperty familyName = new SimpleStringProperty();
    private StringProperty nickname = new SimpleStringProperty();
    private StringProperty  matchTime = new SimpleStringProperty();

    public String getMatchTime() {
        return matchTime.get();
    }

    public StringProperty matchTimeProperty() {
        return matchTime;
    }

    public void setMatchTime(String matchTime) {
        this.matchTime.set(matchTime);
    }

    public Long getId() {
        return idProperty().get();
    }

    public void setId(Long id) {
        this.idProperty().setValue(id);
    }

    public String getEmail() {
        return emailProperty().get();
    }

    public void setEmail(String email) {
        this.emailProperty().setValue(email);
    }

    public String getGivenName() {
        return givenNameProperty().get();
    }

    public void setGivenName(String givenName) {
        this.givenNameProperty().setValue(givenName);
    }

    public String getFamilyName() {
        return familyNameProperty().get();
    }

    public void setFamilyName(String familyName) {
        this.familyNameProperty().setValue(familyName);
    }

    public String getNickname() {
        return nicknameProperty().get();
    }

    public void setNickname(String nickname) {
        this.nicknameProperty().set(nickname);
    }


    public LongProperty idProperty() {
        return id;
    }

    public StringProperty emailProperty() {
        return email;
    }

    public StringProperty givenNameProperty() {
        return givenName;
    }

    public StringProperty familyNameProperty() {
        return familyName;
    }

    public StringProperty nicknameProperty() {
        return nickname;
    }

}
