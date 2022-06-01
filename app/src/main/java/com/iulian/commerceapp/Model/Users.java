package com.iulian.commerceapp.Model;

public class Users {
    private String name, phone, password, image, adress;

    public Users()
    {
    }

    public Users(String name, String phone, String password, String image, String adress) {
        this.name = name;
        this.phone = phone;
        this.password = password;
        this.image = image;
        this.adress = adress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }
}
