package com.nullandvoid.empowerment.ui.home;

public class top_donators {
    public String name;
    public String surname;
    public int num_donations;
    public top_donators(String name, String surname, int num_donations) {
        this.name = name;
        this.surname = surname;
        this.num_donations = num_donations;
    }
    public String getName() { return name; }
    public String getSurname() { return surname; }
    public int getQuantity() { return num_donations; }
    }

