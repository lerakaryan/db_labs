package com.github.lerakaryan.entity;

public class Order {
    private String id;
    private String customerId;
    private String total;

    public Order(String id, String customerId, String total) {
        this.id = id;
        this.customerId = customerId;
        this.total = total;
    }

    public String getId() { return id; }
    public String getCustomerId() { return customerId; }
    public String getTotal() { return total; }

    @Override
    public String toString() {
        return "{ Заказ id: " + id + "; клиент: " + customerId + "; сумма: " + total + " }";
    }
}
