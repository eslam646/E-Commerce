package com.example.my_ecommerce.Model;

public class Cart {
    private String P_Id,P_Name,P_Price,P_Description,Quantity,Discount;

    public Cart() {
    }

    public Cart(String p_Id, String p_Name, String p_Price, String p_Description, String quantity, String discount) {
        P_Id = p_Id;
        P_Name = p_Name;
        P_Price = p_Price;
        P_Description = p_Description;
        Quantity = quantity;
        Discount = discount;
    }

    public String getP_Id() {
        return P_Id;
    }

    public void setP_Id(String p_Id) {
        P_Id = p_Id;
    }

    public String getP_Name() {
        return P_Name;
    }

    public void setP_Name(String p_Name) {
        P_Name = p_Name;
    }

    public String getP_Price() {
        return P_Price;
    }

    public void setP_Price(String p_Price) {
        P_Price = p_Price;
    }

    public String getP_Description() {
        return P_Description;
    }

    public void setP_Description(String p_Description) {
        P_Description = p_Description;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }
}
