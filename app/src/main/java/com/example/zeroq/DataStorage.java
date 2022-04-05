package com.example.zeroq;

public class DataStorage {
String name, Email,password;

    public DataStorage(String name, String Email, String password) {
        this.Email=Email;
        this.password=password;
        this.name=name;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
