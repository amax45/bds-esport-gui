package org.but.feec.esport.api;

import java.sql.Timestamp;
import java.util.ArrayList;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class AdminBasicView {

     private LongProperty admin_id = new SimpleLongProperty();
     private StringProperty nickname = new SimpleStringProperty();
     private StringProperty email = new SimpleStringProperty();
     private StringProperty given_name = new SimpleStringProperty();
     private StringProperty family_name = new SimpleStringProperty();
     private ArrayList<Timestamp> time_of_match;

     public AdminBasicView() {
          time_of_match = new ArrayList<>();

     }

     public Long getId() {
          return idProperty().get();
     }

     public Long getAdminId() {
     return admin_idProperty().get();
     }

     public void setAdminId(Long id) {
     this.admin_idProperty().setValue(id);
     }

     public String getNickname() {
     return nicknameProperty().get();
     }

     public void setNickname(String nickname) {
     this.nicknameProperty().set(nickname);
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

     public LongProperty idProperty() {
          return admin_id;
     }

     public void setFamilyName(String familyName) {
     this.familyNameProperty().setValue(familyName);
     }

     public ArrayList<Timestamp> getTime_of_match() {
          return time_of_match;
     }

     public void addTime_of_match(Timestamp time_of_match) {
          this.time_of_match.add(time_of_match) ;
     }

     public LongProperty admin_idProperty() {
     return admin_id;
     }

     public StringProperty nicknameProperty() {
     return nickname;
     }

     public StringProperty emailProperty() {
     return email;
     }

     public StringProperty givenNameProperty() {
     return given_name;
     }

     public StringProperty familyNameProperty() {
     return family_name;
     }

     }


