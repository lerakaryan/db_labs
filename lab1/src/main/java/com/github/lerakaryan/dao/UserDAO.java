package com.github.lerakaryan.dao;
import com.github.lerakaryan.config.DatabaseConfig;
import com.github.lerakaryan.entity.User;
import java.sql.*;

public class UserDAO {
    public void create(User user) throws SQLException {
        String sql = "INSERT INTO users (username, email) VALUES (?, ?) RETURNING id";
        try (Connection conn = DatabaseConfig.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getEmail());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) user.setId(rs.getInt(1));
        }
    }
    public User findById(int id) throws SQLException {
        try (Connection conn = DatabaseConfig.getConnection(); PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE id = ?")) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return new User(rs.getInt("id"), rs.getString("username"), rs.getString("email"));
        }
        return null;
    }
    public void update(User user) throws SQLException {
        try (Connection conn = DatabaseConfig.getConnection(); PreparedStatement ps = conn.prepareStatement("UPDATE users SET username=?, email=? WHERE id=?")) {
            ps.setString(1, user.getUsername()); ps.setString(2, user.getEmail()); ps.setInt(3, user.getId());
            ps.executeUpdate();
        }
    }
    public void delete(int id) throws SQLException {
        try (Connection conn = DatabaseConfig.getConnection(); PreparedStatement ps = conn.prepareStatement("DELETE FROM users WHERE id=?")) {
            ps.setInt(1, id); ps.executeUpdate();
        }
    }
    public void addFriend(int id1, int id2) throws SQLException {
        try (Connection conn = DatabaseConfig.getConnection(); PreparedStatement ps = conn.prepareStatement("INSERT INTO friends VALUES (?, ?)")) {
            ps.setInt(1, id1);
            ps.setInt(2, id2);
            ps.executeUpdate();
        }
    }
}
