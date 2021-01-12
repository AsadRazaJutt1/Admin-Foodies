package com.example.adminfoodies.Model;

public class Food {
    private String Name, Image, Description, Price, MenuId, Discount,number;
    private String timing,id_food, cate_Id_Rest_Id, restaurantId, restaurant, city;
    private String closing;
    private String opening;
    public Food() {
    }


    public Food(String name, String image, String description, String price, String menuId, String discount) {
        Name = name;
        Image = image;
        Description = description;
        Price = price;
        MenuId = menuId;
        Discount = discount;
    }

    public Food(String name, String image, String description, String price, String menuId, String discount, String timing) {
        Name = name;
        Image = image;
        Description = description;
        Price = price;
        MenuId = menuId;
        Discount = discount;
        this.timing = timing;
    }

    public Food(String name, String description, String price, String menuId, String discount) {
        Name = name;
        Description = description;
        Price = price;
        MenuId = menuId;
        Discount = discount;
    }
    public Food(String name, String image, String description, String price, String menuId, String discount, String number, String timing, String restaurant) {
        Name = name;
        Image = image;
        Description = description;
        Price = price;
        MenuId = menuId;
        Discount = discount;
        this.number = number;
        this.timing = timing;
        this.restaurant = restaurant;
    }
    public Food(String name, String image, String description, String price, String menuId, String discount, String opening,String closing, String id_food, String cate_Id_Rest_Id, String restaurantId, String restaurant, String city) {
        Name = name;
        Image = image;
        Description = description;
        Price = price;
        MenuId = menuId;
        Discount = discount;
        this.opening = opening;
        this.closing = closing;
        this.timing = timing;
        this.id_food = id_food;
        this.cate_Id_Rest_Id = cate_Id_Rest_Id;
        this.restaurantId = restaurantId;
        this.restaurant = restaurant;
        this.city = city;
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

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getId_food() {
        return id_food;
    }

    public void setId_food(String id_food) {
        this.id_food = id_food;
    }

    public String getCate_Id_Rest_Id() {
        return cate_Id_Rest_Id;
    }

    public void setCate_Id_Rest_Id(String cate_Id_Rest_Id) {
        this.cate_Id_Rest_Id = cate_Id_Rest_Id;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(String restaurant) {
        this.restaurant = restaurant;
    }

    public String getTiming() {
        return timing;
    }

    public void setTiming(String timing) {
        this.timing = timing;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getMenuId() {
        return MenuId;
    }

    public void setMenuId(String menuId) {
        this.MenuId = menuId;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }
}
