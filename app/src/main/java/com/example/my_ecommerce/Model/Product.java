package com.example.my_ecommerce.Model;

public class Product {

    private String P_Name,P_Description,P_Price,P_Image,Category_Name,P_Id,P_Date,P_Time;

    public Product() {
    }

    public Product(String p_Name, String p_Description, String p_Price, String p_Image, String category_Name, String p_Id, String p_Date, String p_Time) {
        P_Name = p_Name;
        P_Description = p_Description;
        P_Price = p_Price;
        P_Image = p_Image;
        Category_Name = category_Name;
        P_Id = p_Id;
        P_Date = p_Date;
        P_Time = p_Time;
    }

    public String getP_Name() {
        return P_Name;
    }

    public void setP_Name(String p_Name) {
        P_Name = p_Name;
    }

    public String getP_Description() {
        return P_Description;
    }

    public void setP_Description(String p_Description) {
        P_Description = p_Description;
    }

    public String getP_Price() {
        return P_Price;
    }

    public void setP_Price(String p_Price) {
        P_Price = p_Price;
    }

    public String getP_Image() {
        return P_Image;
    }

    public void setP_Image(String p_Image) {
        P_Image = p_Image;
    }

    public String getCategory_Name() {
        return Category_Name;
    }

    public void setCategory_Name(String category_Name) {
        Category_Name = category_Name;
    }

    public String getP_Id() {
        return P_Id;
    }

    public void setP_Id(String p_Id) {
        P_Id = p_Id;
    }

    public String getP_Date() {
        return P_Date;
    }

    public void setP_Date(String p_Date) {
        P_Date = p_Date;
    }

    public String getP_Time() {
        return P_Time;
    }

    public void setP_Time(String p_Time) {
        P_Time = p_Time;
    }
}
