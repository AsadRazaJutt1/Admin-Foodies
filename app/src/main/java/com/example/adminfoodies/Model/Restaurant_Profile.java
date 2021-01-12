package com.example.adminfoodies.Model;

public class Restaurant_Profile {
    private String restaurantName;
    private String restaurantPassword;
    private String restaurantPhone;
    private String restaurantAddress;
    private String restaurantId;
    private String restaurantTiming;
    private String closing;
    private String opening;

    public Restaurant_Profile() {
    }

    public Restaurant_Profile(String restaurantName, String restaurantPassword, String restaurantAddress, String restaurantId, String restaurantTiming) {
        this.restaurantName = restaurantName;
        this.restaurantPassword = restaurantPassword;
        this.restaurantAddress = restaurantAddress;
        this.restaurantId = restaurantId;
        this.restaurantTiming = restaurantTiming;

    }

    public Restaurant_Profile(String restaurantName, String restaurantPhone, String restaurantPassword, String restaurantAddress, String restaurantId, String restaurantTiming) {
        this.restaurantPhone = restaurantPhone;
        this.restaurantName = restaurantName;
        this.restaurantPassword = restaurantPassword;
        this.restaurantAddress = restaurantAddress;
        this.restaurantId = restaurantId;
        this.restaurantTiming = restaurantTiming;

    }

    public String getClosing() {
        return closing;
    }

    public void setClosing(String closing) {
        this.closing = closing;
    }

    public String getOpening() {
        return opening;
    }

    public void setOpening(String opening) {
        this.opening = opening;
    }

    public String getRestaurantTiming() {
        return restaurantTiming;
    }

    public void setRestaurantTiming(String restaurantTiming) {
        this.restaurantTiming = restaurantTiming;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getRestaurantAddress() {
        return restaurantAddress;
    }

    public void setRestaurantAddress(String restaurantAddress) {
        this.restaurantAddress = restaurantAddress;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getRestaurantPassword() {
        return restaurantPassword;
    }

    public void setRestaurantPassword(String restaurantPassword) {
        this.restaurantPassword = restaurantPassword;
    }

    public String getRestaurantPhone() {
        return restaurantPhone;
    }

    public void setRestaurantPhone(String restaurantPhone) {
        this.restaurantPhone = restaurantPhone;
    }


}
