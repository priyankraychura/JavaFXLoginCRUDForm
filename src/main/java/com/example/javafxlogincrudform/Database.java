package com.example.javafxlogincrudform;

import java.sql.Connection;
import java.sql.DriverManager;

public class Database {
    public static Connection connect(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            String url = "jdbc:mysql://localhost/javafxcrud";
            Connection connect = DriverManager.getConnection(url, "root", "");
            return connect;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
