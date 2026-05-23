package com.github.lerakaryan.entity;

import java.math.BigDecimal;

public class ProductPrice {
    private int productId;
    private BigDecimal price;
    private long version;

    public ProductPrice() {}

    public ProductPrice(int productId, BigDecimal price, long version) {
        this.productId = productId;
        this.price = price;
        this.version = version;
    }

    public int getProductId() { return productId; }

    public BigDecimal getPrice() { return price; }

    public long getVersion() { return version; }
    @Override
    public String toString() {
        return "ProductPrice{" +
                "id=" + productId +
                ", price=" + price +
                ", ver=" + version +
                '}';
    }
}
