package com.adaptwo.adap.firebasenotificationapp;

/**
 * Created by AkshayeJH on 04/01/18.
 */

public class Users extends UserId {

    String name, image, email;

    public Users(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Users(String name, String email, String image) {
        this.name = name;
        this.email= email;
        this.image = image;
    }
}
