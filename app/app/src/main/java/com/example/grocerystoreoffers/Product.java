package com.example.grocerystoreoffers;

//package com.example.grocerystoreoffers;

public class Product {
    private String id;
    private String image;
    private String name;
    private String price;
    private String store;

    public Product(String id, String image, String name, String price, String store) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.price = price;
        this.store = store;
    }

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }
}

