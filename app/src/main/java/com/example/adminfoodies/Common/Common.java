package com.example.adminfoodies.Common;


import com.example.adminfoodies.Model.Request;
import com.example.adminfoodies.Model.User;

public class Common {

    public static User currentUser;
    public static final String UPDATE="Update";
    public static final String DELETE="Delete";
    public static Request currentRequest;

    public static String convertCodeToStatus(String status) {

        if (status.equals("0"))
            return "Placed";

        else if (status.equals("1"))
            return "On the Way";

        else
            return "Shipped";
    }
}
