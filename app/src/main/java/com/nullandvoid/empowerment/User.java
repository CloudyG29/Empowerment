package com.nullandvoid.empowerment;
public class User {
    public String userid;
    public String name;
    public String surname;
    public String email;

    public User(String userid, String name, String surname, String email) {
        this.userid = userid;
        this.name = name;
        this.surname = surname;
        this.email = email;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
