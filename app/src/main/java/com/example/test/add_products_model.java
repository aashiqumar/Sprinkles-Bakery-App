package com.example.test;

public class add_products_model {
    private String category;
    private String title;
    private String price;
    private String uri;

    public add_products_model() {
    }

    public add_products_model(String category, String title, String price, String uri) {
        this.category = category;
        this.title = title;
        this.price = price;
        this.uri = uri;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String image) {
        this.category = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
