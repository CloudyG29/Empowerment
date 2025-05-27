package com.nullandvoid.empowerment;
import org.json.JSONArray;
public class MessageItem {
    private String Name;
    private String Surname;
    private String ItemName;
    private String Quantity;
    private String Email;

    public MessageItem(String name, String itemName, String quantity, String email, String surname) {
        Name = name;
        ItemName = itemName;
        Quantity = quantity;
        Email = email;
        Surname = surname;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getSurname() {
        return Surname;
    }

    public void setSurname(String surname) {
        Surname = surname;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }
}
