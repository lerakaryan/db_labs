package com.github.lerakaryan.entity;

public class CartItem {
    private int userId;
    private int productId;
    private int quantity;
    private int sign;

    public CartItem() {}

    public CartItem(int userId, int productId, int quantity, int sign) {
        this.userId = userId;
        this.productId = productId;
        this.quantity = quantity;
        this.sign = sign;
    }

    public int getUserId() { return userId; }

    public int getProductId() { return productId; }

    public int getQuantity() { return quantity; }

    public int getSign() { return sign; }

    @Override
    public String toString() {
        return "CartItem{" +
                "user=" + userId +
                ", product=" + productId +
                ", qty=" + quantity +
                ", sign=" + sign +
                '}';
    }
}
