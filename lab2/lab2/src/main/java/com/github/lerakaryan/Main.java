package com.github.lerakaryan;

import com.github.lerakaryan.dao.*;
import com.github.lerakaryan.entity.*;
import com.github.lerakaryan.config.*;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) {
        try (Connection conn = ClickHouseConfig.getConnection()) {
            System.out.println("Подключение установлено. Инициализация БД...");
            initDB(conn);
            fillDB(conn);

            ProductDAO productDAO = new ProductDAO(conn);
            AnalyticsDAO analyticsDAO = new AnalyticsDAO(conn);

            System.out.println("\n1. CRUD для Product");
            Product prod = new Product(99, 1, "Клавиатура");
            productDAO.create(prod);
            System.out.println("Product CREATE: " + productDAO.read(99).toString());

            productDAO.update(99, "Клавиатура RGB");
            System.out.println("Product UPDATE отправлен");

            System.out.println("Обновленный продукт сразу: " + productDAO.read(99).toString());
            Thread.sleep(2000);
            System.out.println("Обновленный продукт после паузы: " + productDAO.read(99).toString());

            productDAO.delete(99);
            System.out.println("Product DELETE отправлен");


            System.out.println("\n2. JOIN {");
            analyticsDAO.runInnerJoin();
            analyticsDAO.runLeftJoin();
            analyticsDAO.runRightJoin();
            analyticsDAO.runFullJoin();
            analyticsDAO.runCrossJoin();
            System.out.println("\n}");


            analyticsDAO.runComplexQuery();
            System.out.println();
            analyticsDAO.runReplacingMergeTree();
            System.out.println();
            analyticsDAO.runCollapsingMergeTree();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void initDB(Connection conn) throws Exception {
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS cart_items");
            stmt.execute("DROP TABLE IF EXISTS product_prices");
            stmt.execute("DROP TABLE IF EXISTS sales");
            stmt.execute("DROP TABLE IF EXISTS products");
            stmt.execute("DROP TABLE IF EXISTS categories");

            stmt.execute("CREATE TABLE categories (id UInt32, name String) ENGINE = MergeTree() ORDER BY id");
            stmt.execute("CREATE TABLE products (id UInt32, cat_id UInt32, name String) ENGINE = MergeTree() ORDER BY id");
            stmt.execute("CREATE TABLE sales (id UInt32, p_id UInt32, amount Decimal(10,2), sale_date Date) ENGINE = MergeTree() ORDER BY sale_date");
            stmt.execute("CREATE TABLE product_prices (p_id UInt32, price Decimal(10,2), version UInt64) ENGINE = ReplacingMergeTree(version) ORDER BY p_id");
            stmt.execute("CREATE TABLE cart_items (user_id UInt32, p_id UInt32, quantity UInt32, sign Int8) ENGINE = CollapsingMergeTree(sign) ORDER BY (user_id, p_id)");
        }
    }

    private static void fillDB(Connection conn) throws Exception {
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("INSERT INTO categories VALUES (1, 'Электроника'), (2, 'Книги'), (3, 'Одежда')");
            stmt.execute("INSERT INTO products VALUES (1, 1, 'Телефон'), (2, 1, 'Ноутбук'), (3, 2, 'Роман')");
            stmt.execute("INSERT INTO sales VALUES (1, 1, 45000.00, '2026-05-22'), (2, 2, 90000.00, '2026-05-23')");
        }
    }
}
