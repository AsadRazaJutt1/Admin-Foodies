package com.example.adminfoodies.Model;

public class User {

    private String Name;
    private String Email;
    private String Password;
    private String Phone;
    private String isStaff="true";
    public User() {
    }

    public User(String name, String password) {
        Name = name;
        Password = password;
    }

    public User(String name, String isStaff, String password) {
        Name = name;
        this.isStaff= isStaff;
        Password = password;
    }

    public User(String name, String email, String password, String phone) {
        Name = name;
        Email = email;
        Password = password;
        Phone = phone;
    }

    public String getIsStaff() {
        return isStaff;
    }

    public void setIsStaff(String isStaff) {
        this.isStaff = isStaff;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
