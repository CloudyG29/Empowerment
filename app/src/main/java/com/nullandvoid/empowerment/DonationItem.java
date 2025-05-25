package com.nullandvoid.empowerment;

public class DonationItem {
    private String ItemName;
    private String Quantity;

    public DonationItem(String itemName, String quantity) {
        ItemName = itemName;
        Quantity = quantity;
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
}
