package com.example.adminfoodies.Model;

public class Getter_Setter_Compains {
    String title,orderNumber,complaintMsg,phone;

    public Getter_Setter_Compains() {
    }

    public Getter_Setter_Compains(String title, String complaintMsg, String phone) {
        this.title = title;

        this.complaintMsg = complaintMsg;
        this.phone=phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getComplaintMsg() {
        return complaintMsg;
    }

    public void setComplaintMsg(String complaintMsg) {
        this.complaintMsg = complaintMsg;
    }
}
