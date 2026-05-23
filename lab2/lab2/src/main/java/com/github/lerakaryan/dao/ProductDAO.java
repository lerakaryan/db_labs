package com.github.lerakaryan.dao;

import com.github.lerakaryan.entity.CartItem;
import com.github.lerakaryan.entity.Product;
import com.github.lerakaryan.entity.ProductPrice;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {
    private final Connection conn;

    public ProductDAO(Connection conn) {
        this.conn = conn;
    }

    public void create(Product product) throws SQLException {
        String sql = "INSERT INTO products (id, cat_id, name) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, product.getId());
            ps.setInt(2, product.getCatId());
            ps.setString(3, product.getName());
            ps.executeUpdate();
        }
    }

    public Product read(int id) throws SQLException {
        String sql = "SELECT id, cat_id, name FROM products WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Product(rs.getInt("id"), rs.getInt("cat_id"), rs.getString("name"));
                }
            }
        }
        return null;
    }

    public void update(int id, String newName) throws SQLException {
        String sql = "ALTER TABLE products UPDATE name = ? WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newName);
            ps.setInt(2, id);
            ps.execute();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "ALTER TABLE products DELETE WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.execute();
        }
    }

    public void createCartItem(CartItem ci) throws SQLException {
        String sql = "INSERT INTO cart_items (user_id, p_id, quantity, sign) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ci.getUserId());
            ps.setInt(2, ci.getProductId());
            ps.setInt(3, ci.getQuantity());
            ps.setInt(4, ci.getSign());
            ps.executeUpdate();
        }
    }

    public String readCartItems(int userId, int productId) throws SQLException {
        List<String> rows = new ArrayList<>();
        String sql = "SELECT user_id, p_id, quantity, sign FROM cart_items WHERE user_id = ? AND p_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, productId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rows.add(new CartItem(
                            rs.getInt(1),
                            rs.getInt(2),
                            rs.getInt(3),
                            rs.getInt(4)
                    ).toString());
                }
            }
        }
        return rows.toString();
    }

    public void createProductPrice(ProductPrice pp) throws SQLException {
        String sql = "INSERT INTO product_prices (p_id, price, version) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, pp.getProductId());
            ps.setBigDecimal(2, pp.getPrice());
            ps.setLong(3, pp.getVersion());
            ps.executeUpdate();
        }
    }
    public String readProductPrices(int productId) throws SQLException {
        List<String> versions = new ArrayList<>();
        String sql = "SELECT p_id, price, version FROM product_prices WHERE p_id = ? ORDER BY version ASC";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    versions.add(new ProductPrice(
                            rs.getInt(1),
                            rs.getBigDecimal(2),
                            rs.getLong(3)
                    ).toString());
                }
            }
        }
        return versions.toString();
    }
}
