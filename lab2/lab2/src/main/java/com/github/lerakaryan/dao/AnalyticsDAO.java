package com.github.lerakaryan.dao;

import com.github.lerakaryan.entity.CartItem;
import com.github.lerakaryan.entity.ProductPrice;

import java.math.BigDecimal;
import java.sql.*;

public class AnalyticsDAO {
    private final Connection conn;

    public AnalyticsDAO(Connection conn) {
        this.conn = conn;
    }

    public void runInnerJoin() throws SQLException {
        System.out.println("\n1. INNER JOIN (Сопоставление товаров и категорий)");
        String sql = "SELECT p.name, c.name FROM products p INNER JOIN categories c ON p.cat_id = c.id";
        printQueryResults(sql);
    }

    public void runLeftJoin() throws SQLException {
        System.out.println("\n2. LEFT JOIN (Категории, даже пустые)");
        String sql = "SELECT c.name, p.name FROM categories c LEFT JOIN products p ON c.id = p.cat_id";
        printQueryResults(sql);
    }

    public void runRightJoin() throws SQLException {
        System.out.println("\n3. RIGHT JOIN (Продажи и товары)");
        String sql = "SELECT p.name, s.amount FROM products p RIGHT JOIN sales s ON p.id = s.p_id";
        printQueryResults(sql);
    }

    public void runFullJoin() throws SQLException {
        System.out.println("\n4. FULL JOIN (Объединение всех товаров и продаж)");
        String sql = "SELECT p.name, s.amount FROM products p FULL JOIN sales s ON p.id = s.p_id";
        printQueryResults(sql);
    }

    public void runCrossJoin() throws SQLException {
        System.out.println("\n5. CROSS JOIN (Декартово произведение, ограничено LIMIT)");
        String sql = "SELECT p.name, c.name FROM products p CROSS JOIN categories c LIMIT 5";
//        String sql = "SELECT p.name, c.name FROM products p CROSS JOIN categories c WHERE p.cat_id = c.id LIMIT 5\n";

        printQueryResults(sql);
    }

    public void runComplexQuery() throws SQLException {
        System.out.println("\n3. Сложный запрос (WHERE, GROUP BY, HAVING, ORDER BY, LIMIT, LIMIT BY)");
        // выводим первый товар каждой категории (кроме категорий без товаров)
        String sql = "SELECT cat_id, name, count() as cnt " +
                "FROM products " +
                "WHERE cat_id > 0 " +
                "GROUP BY cat_id, name " +
                "HAVING cnt >= 1 " +
                "ORDER BY cat_id ASC " +
                "LIMIT 1 BY cat_id " +
                "LIMIT 10";
        printQueryResults(sql);
    }

    public void runReplacingMergeTree() throws SQLException {
        System.out.println("4. ReplacingMergeTree (Актуальные цены)");
        ProductDAO productDAO = new ProductDAO(conn);
        int productId = 1;
        productDAO.createProductPrice(new ProductPrice(productId, new BigDecimal("100.00"), 1L));
        productDAO.createProductPrice(new ProductPrice(productId, new BigDecimal("125.50"), 2L));
        String sql = "SELECT p_id, price, version FROM product_prices ORDER BY p_id, version DESC LIMIT 1 BY p_id";
        System.out.println(productDAO.readProductPrices(productId));
        printQueryResults(sql);
    }

    public void runCollapsingMergeTree() throws SQLException {
        System.out.println("5. CollapsingMergeTree (Корзина)");
        ProductDAO productDAO = new ProductDAO(conn);
        int productId = 10;
        int userId = 101;
        productDAO.createCartItem(new CartItem(userId, productId, 5, 1));
        String sql = "SELECT p_id, sum(quantity * sign) as qty FROM cart_items GROUP BY p_id HAVING sum(sign) > 0";
        printQueryResults(sql);
        productDAO.createCartItem(new CartItem(userId, productId, 5, -1));
        System.out.println("Все записи: ");
        System.out.println(productDAO.readCartItems(userId, productId));
        sql = "SELECT p_id, sum(quantity * sign) as qty FROM cart_items GROUP BY p_id HAVING sum(sign) > 0";
        System.out.println("В корзине: ");
        printQueryResults(sql);
    }

    private void printQueryResults(String sql) throws SQLException {
        try (Statement setupStmt = conn.createStatement()) {
            setupStmt.execute("SET join_use_nulls = 1");
        }

        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            ResultSetMetaData meta = rs.getMetaData();
            int colCount = meta.getColumnCount();
            for (int i = 1; i <= colCount; i++) {
                System.out.print(meta.getColumnLabel(i) + "\t\t");
            }
            System.out.println();
            while (rs.next()) {
                for (int i = 1; i <= colCount; i++) {
                    String val = rs.getString(i);
                    if (rs.wasNull() || val == null) {
                        val = "NULL";
                    }

                    System.out.print(val + "\t\t");
                }
                System.out.println();
            }
        }
    }
}
