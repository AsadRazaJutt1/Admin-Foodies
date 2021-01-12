package com.example.adminfoodies.Model;

public class Getter_Setter_Suggestions {
    String title,suggestionMsg,phone;

    public Getter_Setter_Suggestions() {
    }

    public Getter_Setter_Suggestions(String title, String suggestionMsg, String phone) {
        this.title = title;
        this.suggestionMsg = suggestionMsg;
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

    public String getSuggestionMsg() {
        return suggestionMsg;
    }

    public void setSuggestionMsg(String suggestionMsg) {
        this.suggestionMsg = suggestionMsg;
    }
}
