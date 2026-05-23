package com.github.lerakaryan.dao;
import com.github.lerakaryan.config.DatabaseConfig;
import com.github.lerakaryan.entity.Like;
import java.sql.*;

public class LikeDAO {
    public void create(Like like) throws SQLException {
        String sql = "INSERT INTO likes (user_id, message_id) VALUES (?, ?) RETURNING id";
        try (Connection conn = DatabaseConfig.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            if (like.getUserId() != null) ps.setInt(1, like.getUserId()); else ps.setNull(1, Types.INTEGER);
            ps.setInt(2, like.getMessageId());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) like.setId(rs.getInt(1));
        }
    }
    public void delete(int id) throws SQLException {
        try (Connection conn = DatabaseConfig.getConnection(); PreparedStatement ps = conn.prepareStatement("DELETE FROM likes WHERE id=?")) {
            ps.setInt(1, id); ps.executeUpdate();
        }
    }
}