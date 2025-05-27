package com.nullandvoid.empowerment;

public class RequestItem {
    private String ItemName;
    private String Quantity;
    private String Bio;

    public RequestItem(String itemName, String quantity, String bio) {
        ItemName = itemName;
        Quantity = quantity;
        Bio = bio;
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

    public String getBio() {
        return Bio;
    }

    public void setBio(String bio) {
        Bio = bio;
    }
}
