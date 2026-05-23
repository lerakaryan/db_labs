package com.github.lerakaryan.dao;

import com.github.lerakaryan.entity.Category;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {
    private final Connection conn;

    public CategoryDAO(Connection conn) {
        this.conn = conn;
    }

    public void create(Category category) throws SQLException {
        String sql = "INSERT INTO categories (id, name) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, category.getId());
            ps.setString(2, category.getName());
            ps.executeUpdate();
        }
    }

    public Category read(int id) throws SQLException {
        String sql = "SELECT id, name FROM categories WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Category(rs.getInt("id"), rs.getString("name"));
                }
            }
        }
        return null;
    }

    public void update(int id, String newName) throws SQLException {
        String sql = "ALTER TABLE categories UPDATE name = ? WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newName);
            ps.setInt(2, id);
            ps.execute();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "ALTER TABLE categories DELETE WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.execute();
        }
    }
}

