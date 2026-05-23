package com.github.lerakaryan.dao;
import com.github.lerakaryan.config.DatabaseConfig;
import com.github.lerakaryan.entity.Message;
import java.sql.*;

public class MessageDAO {
    public void create(Message msg) throws SQLException {
        String sql = "INSERT INTO messages (user_id, content) VALUES (?, ?) RETURNING id";
        try (Connection conn = DatabaseConfig.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, msg.getUserId()); ps.setString(2, msg.getContent());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) msg.setId(rs.getInt(1));
        }
    }
    public Message findById(int id) throws SQLException {
        try (Connection conn = DatabaseConfig.getConnection(); PreparedStatement ps = conn.prepareStatement("SELECT * FROM messages WHERE id=?")) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return new Message(rs.getInt("id"), rs.getInt("user_id"), rs.getString("content"), rs.getTimestamp("created_at"));
        }
        return null;
    }
    public void delete(int id) throws SQLException {
        try (Connection conn = DatabaseConfig.getConnection(); PreparedStatement ps = conn.prepareStatement("DELETE FROM messages WHERE id=?")) {
            ps.setInt(1, id); ps.executeUpdate();
        }
    }

    public void update (int id, String text) throws SQLException {
        try (Connection conn = DatabaseConfig.getConnection(); PreparedStatement ps = conn.prepareStatement("UPDATE messages SET content=? WHERE id=?")) { //UPDATE users SET username=?, email=? WHERE id=?
            ps.setString(1, text); ps.setInt(2, id); ps.executeUpdate();
        }
    }
}