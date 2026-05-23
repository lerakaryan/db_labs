package com.github.lerakaryan.dao;

import com.github.lerakaryan.entity.Order;
import redis.clients.jedis.Jedis;
import java.util.HashMap;
import java.util.Map;

public class OrderDao {
    private final Jedis jedis;

    public OrderDao(Jedis jedis) {
        this.jedis = jedis;
    }

    public boolean create(Order order) {
        String key = "order_hash:" + order.getId();

        if (jedis.exists(key)) {
            System.out.println("Ошибка: Заказ с id " + order.getId() + " уже существует");
            return false;
        }

        Map<String, String> fields = new HashMap<>();
        fields.put("customerId", order.getCustomerId());
        fields.put("total", order.getTotal());
        jedis.hset(key, fields);
        return true;
    }

    public Order read(String id) {
        Map<String, String> fields = jedis.hgetAll("order_hash:" + id);
        if (fields.isEmpty()) return null;
        return new Order(id, fields.get("customerId"), fields.get("total"));
    }

    public void update(Order order) {
        Map<String, String> fields = new HashMap<>();
        fields.put("customerId", order.getCustomerId());
        fields.put("total", order.getTotal());
        jedis.hset("order_hash:" + order.getId(), fields);
    }

    public void delete(String id) {
        jedis.del("order_hash:" + id);
    }
}
