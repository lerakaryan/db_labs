package com.github.lerakaryan.dao;

import com.github.lerakaryan.entity.Sale;
import java.sql.*;

public class SaleDAO {
    private final Connection conn;

    public SaleDAO(Connection conn) {
        this.conn = conn;
    }

    public void create(Sale sale) throws SQLException {
        String sql = "INSERT INTO sales (id, p_id, amount, sale_date) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, sale.getId());
            ps.setInt(2, sale.getProductId());
            ps.setBigDecimal(3, sale.getAmount());
            ps.setDate(4, sale.getDate());
            ps.executeUpdate();
        }
    }

    public Sale read(int id) throws SQLException {
        String sql = "SELECT id, p_id, amount, sale_date FROM sales WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Sale(
                            rs.getInt("id"),
                            rs.getInt("p_id"),
                            rs.getBigDecimal("amount"),
                            rs.getDate("sale_date")
                    );
                }
            }
        }
        return null;
    }

    public void updateAmount(int id, double newAmount) throws SQLException {
        String sql = "ALTER TABLE sales UPDATE amount = ? WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, newAmount);
            ps.setInt(2, id);
            ps.execute();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "ALTER TABLE sales DELETE WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.execute();
        }
    }
}
