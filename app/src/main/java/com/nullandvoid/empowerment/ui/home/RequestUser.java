package com.nullandvoid.empowerment.ui.home;

public class RequestUser {
    public String name;
    public String surname;
    public int quantity;
    public String biography;

    public RequestUser(String name, String surname, int quantity, String biography) {
        this.name = name;
        this.surname = surname;
        this.quantity = quantity;
        this.biography = biography;
    }
}
