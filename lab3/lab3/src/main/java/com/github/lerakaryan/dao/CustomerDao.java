package com.github.lerakaryan.dao;

import com.github.lerakaryan.entity.Customer;
import redis.clients.jedis.Jedis;

public class CustomerDao {
    private final Jedis jedis;

    public CustomerDao(Jedis jedis) {
        this.jedis = jedis;
    }

    public boolean create(Customer customer) {
        String nameKey = "customer:" + customer.getId() + ":name";

        if (jedis.exists(nameKey)) {
            System.out.println("Ошибка: Клиент с id " + customer.getId() + " уже существует");
            return false;
        }

        jedis.set(nameKey, customer.getName());
        jedis.set("customer:" + customer.getId() + ":email", customer.getEmail());
        return true;
    }

    public Customer read(String id) {
        String name = jedis.get("customer:" + id + ":name");
        String email = jedis.get("customer:" + id + ":email");
        if (name == null) return null;
        return new Customer(id, name, email);
    }

    public void update(Customer customer) {
        jedis.set("customer:" + customer.getId() + ":name", customer.getName());
        jedis.set("customer:" + customer.getId() + ":email", customer.getEmail());
    }

    public void delete(String id) {
        jedis.del("customer:" + id + ":name", "customer:" + id + ":email");
    }
}
