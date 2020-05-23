package com.learning.skilclasses.models;

public class Message {
    private String message;
    private String category;
    private String subcategory;
    private String user_id;
    private String sentat;
    private String image;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    public Message(String id,String message, String category, String subcategory, String user_id, String sentat, String image) {
        this.id = id;
        this.message = message;
        this.category = category;
        this.subcategory = subcategory;
        this.user_id = user_id;
        this.sentat = sentat;
        this.image = image;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getSentat() {
        return sentat;
    }

    public void setSentat(String sentat) {
        this.sentat = sentat;
    }

    public Message() {
    }
}
