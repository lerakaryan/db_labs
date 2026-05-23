package com.github.lerakaryan.entity;

public class Product {
    private int id;
    private int catId;
    private String name;

    public Product() {}

    public Product(int id, int catId, String name) {
        this.id = id;
        this.catId = catId;
        this.name = name;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getCatId() { return catId; }
    public void setCatId(int catId) { this.catId = catId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    @Override
    public String toString() {
        return "Product{id=" + id + ", catId=" + catId + ", name='" + name + "'}";
    }

}
