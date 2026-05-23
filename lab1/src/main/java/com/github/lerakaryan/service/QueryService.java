package com.github.lerakaryan.service;
import com.github.lerakaryan.config.DatabaseConfig;
import java.sql.*;

public class QueryService {
    public void executeLaboratoryQueries() throws SQLException {
        try (Connection conn = DatabaseConfig.getConnection(); Statement st = conn.createStatement()) {
            System.out.println("1. INNER JOIN (Юзеры имеющие сообщения)");
            printRs(st.executeQuery("SELECT u.username, m.content FROM users u INNER JOIN messages m ON u.id = m.user_id"));

            System.out.println("2. LEFT JOIN (Все юзеры и их сообщения)");
            printRs(st.executeQuery("SELECT u.username, m.content FROM users u LEFT JOIN messages m ON u.id = m.user_id"));

            System.out.println("3. RIGHT JOIN (Сообщения и лайки)");
            printRs(st.executeQuery("SELECT m.content, l.user_id FROM messages m RIGHT JOIN likes l ON m.id = l.message_id"));

            System.out.println("4. FULL OUTER JOIN (Все пользователи и все лайки)");
            printRs(st.executeQuery("SELECT u.username, l.id FROM users u FULL OUTER JOIN likes l ON u.id = l.user_id"));

            System.out.println("5. CROSS JOIN (Все сообщения кроме своих)");

            printRs(st.executeQuery("SELECT u.username, m.content " +
                    "FROM users u " +
                    "CROSS JOIN messages m " +
                    "WHERE u.id != m.user_id "));


            System.out.println("WHERE, GROUP BY, HAVING, ORDER BY (выводим юзеров у которых больше 0 сообщений (обязательно с текстом) и сортируем по количеству сообщений)");
            printRs(st.executeQuery("SELECT u.id, u.username, COUNT(*) " +
                    "FROM messages m " +
                    "JOIN users u ON m.user_id = u.id " +
                    "WHERE m.content IS NOT NULL " +
                    "GROUP BY u.id, u.username " +
                    "HAVING COUNT(*) > 0 " +
                    "ORDER BY 3 DESC"));

            System.out.println("WINDOW FUNCTION (Делит таблицу сообщений по юзерам, присваивает каждому сообщению юзера порядковый номер)");
            printRs(st.executeQuery("SELECT user_id, content, RANK() " +
                            " OVER(PARTITION BY user_id ORDER BY created_at) " +
                            "FROM messages"));

            System.out.println("RECURSIVE (Дерево друзей АЛисы)");
            int maxLevel = 3;
            String recursiveSql =
                    "WITH RECURSIVE f AS (" +
                            "  SELECT user_id2, 1 as level " +
                            "  FROM friends " +
                            "  WHERE user_id1 = 1 " +
                            "  UNION " +
                            "  SELECT fr.user_id2, f.level + 1 " +
                            "  FROM friends fr " +
                            "  JOIN f ON fr.user_id1 = f.user_id2 " +
                            "  WHERE f.level < " + maxLevel +
                            ") " +
                            "SELECT u.username, MIN(f.level) as min_dist " +
                            "FROM f " +
                            "JOIN users u ON f.user_id2 = u.id " +
                            "GROUP BY u.username " +
                            "ORDER BY min_dist, u.username";
            printRs(st.executeQuery(recursiveSql));
        }
    }

    private void printRs(ResultSet rs) throws SQLException {
        int cols = rs.getMetaData().getColumnCount();
        while (rs.next()) {
            for (int i = 1; i <= cols; i++) System.out.print(rs.getString(i) + " | ");
            System.out.println();
        }
        System.out.println();
    }
}