package com.github.lerakaryan.entity;

import java.math.BigDecimal;
import java.sql.Date;

public class Sale {
    private int id;
    private int productId;
    private BigDecimal amount;
    private Date date;

    public Sale() {}

    public Sale(int id, int productId, BigDecimal amount, Date date) {
        this.id = id;
        this.productId = productId;
        this.amount = amount;
        this.date = date;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }
}
