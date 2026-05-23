package com.github.lerakaryan.dao;

import com.github.lerakaryan.entity.Product;
import redis.clients.jedis.Jedis;

public class ProductDao {
    private final Jedis jedis;

    public ProductDao(Jedis jedis) {
        this.jedis = jedis;
    }

    public boolean create(Product product) {
        String titleKey = "product:" + product.getId() + ":title";

        if (jedis.exists(titleKey)) {
            System.out.println("Ошибка: Товар с id " + product.getId() + " уже существует");
            return false;
        }

        jedis.set(titleKey, product.getTitle());
        jedis.set("product:" + product.getId() + ":price", String.valueOf(product.getPrice()));
        return true;
    }

    public Product read(String id) {
        String title = jedis.get("product:" + id + ":title");
        String price = jedis.get("product:" + id + ":price");
        if (title == null) return null;
        return new Product(id, title, Double.parseDouble(price));
    }

    public void update(Product product) {
        jedis.set("product:" + product.getId() + ":title", product.getTitle());
        jedis.set("product:" + product.getId() + ":price", String.valueOf(product.getPrice()));
    }

    public void delete(String id) {
        jedis.del("product:" + id + ":title", "product:" + id + ":price");
    }
}
