package com.example.inventorymanagementsystem;

import java.util.Date;

public class customerData {

    private Integer customerId;
    private String type;
    private String brand;
    private String productName;
    private Integer quantity;
    private Double price;
    private Date date;

    //Watch put for the customerId != customer_id
    public customerData(Integer customerId,String type,String brand,String productName,Integer quantity,Double price,Date date){
        this.customerId = customerId;
        this.type = type;
        this.brand = brand;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.date = date;
    }
    public Integer getCustomerId(){
        return customerId;
    }
    public String getType(){
        return type;
    }
    public String getBrand(){
        return brand;
    }
    public String getProductName(){
        return productName;
    }
    public Integer getQuantity(){
        return  quantity;
    }
    public Double getPrice(){
        return price;
    }
    public Date getDate(){
        return date;
    }

}
