package com.example.inventorymanagementsystem;
import java.sql.Connection;
import java.sql.DriverManager;

public class database {
    public static Connection connectDB(){

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connect = DriverManager.getConnection("jdbc:mysql://localhost/inventory","root","admin");
            return  connect;
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
