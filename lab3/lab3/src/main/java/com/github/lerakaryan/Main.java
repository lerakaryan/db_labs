package com.github.lerakaryan;

import com.github.lerakaryan.config.RedisConfig;
import com.github.lerakaryan.dao.*;
import com.github.lerakaryan.entity.*;
import redis.clients.jedis.Jedis;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Jedis jedis = RedisConfig.getConnection();

        CustomerDao customerDao = new CustomerDao(jedis);
        ProductDao productDao = new ProductDao(jedis);
        OrderDao orderDao = new OrderDao(jedis);
        RedisQueriesDao queriesDao = new RedisQueriesDao(jedis);

        System.out.println("1. CRUD для Customer и Product");
        Customer customer = new Customer("1", "Валерия", "lera@mail.ru");
        customerDao.create(customer);
        System.out.println("Создание: " + customerDao.read("1"));
        customer = new Customer("1", "Лера", "lera@mail.ru");
        customerDao.update(customer);
        System.out.println("Редактирование: " + customerDao.read("1"));

        Product product = new Product("101", "Ноутбук", 150000.0);
        productDao.create(product);
        System.out.println(productDao.read("101"));
        System.out.println();

        System.out.println("5. CRUD через хэш-таблицы для Order");
        Order order = new Order("55", "1", "150000");
        orderDao.create(order);
        System.out.println(orderDao.read("55"));
        System.out.println();

        System.out.println("2. Списки");
        String historyKey = "history:1";
        jedis.lpush(historyKey, "Товар_101");
        jedis.lpush(historyKey, "Товар_102");
        jedis.ltrim(historyKey, 0, 4);
        System.out.println("История просмотров " + queriesDao.getHistory("1"));
        System.out.println();

        System.out.println("3. Множества");
        String catSmartphones = "cat:smartphones";
        String catApple = "cat:apple";
        jedis.del(catSmartphones, catApple);
        jedis.sadd(catSmartphones, "айфон15", "самсунг_с23", "пиксель8");
        jedis.sadd(catApple, "айфон15", "макбук", "айпад");
        System.out.println("Пересечение категорий Смартфоны и Apple " + queriesDao.getIntersection(catSmartphones, catApple));
        System.out.println("Разность категорий Смартфоны и Apple " + queriesDao.getDifference(catSmartphones, catApple));
        System.out.println();

        System.out.println("4. Упорядоченное множество");
        String salesRating = "sales_rating";
        jedis.del(salesRating);
        jedis.zadd(salesRating, 100, "айфон15");
        jedis.zadd(salesRating, 50, "самсунг_с23");
        jedis.zadd(salesRating, 150, "пиксель8");
        System.out.println("Рейтинг продаж " + queriesDao.getTopSales(salesRating));
        System.out.println();

        System.out.println("6. Очереди");
        queriesDao.subscribeChannel("новости");
        Thread.sleep(200);
        queriesDao.publishMessage("новости", "Новое поступление товара");
        Thread.sleep(200);

        jedis.close();
        System.exit(0);
    }
}
