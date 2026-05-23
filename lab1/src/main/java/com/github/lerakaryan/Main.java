package com.github.lerakaryan;
import com.github.lerakaryan.config.DatabaseConfig;
import com.github.lerakaryan.dao.*;
import com.github.lerakaryan.entity.*;
import com.github.lerakaryan.service.QueryService;
import java.sql.*;

public class Main {
    public static void main(String[] args) {
        try {
//            initDatabase();
//            fillDB();
            QueryService queryService = new QueryService();
            UserDAO userDAO = new UserDAO();
            MessageDAO msgDAO = new MessageDAO();
            LikeDAO likeDAO = new LikeDAO();

//            User danile = new User("danile", "dan@mail.com");
//            userDAO.create(danile);

            queryService.executeLaboratoryQueries();

        } catch (Exception e) { e.printStackTrace(); }
    }

    private static void initDatabase() throws SQLException {
        try (Connection conn = DatabaseConfig.getConnection(); Statement st = conn.createStatement()) {
            st.execute("DROP TABLE IF EXISTS likes CASCADE; DROP TABLE IF EXISTS friends CASCADE; " +
                    "DROP TABLE IF EXISTS messages CASCADE; DROP TABLE IF EXISTS users CASCADE;");
            st.execute("CREATE TABLE users (id SERIAL PRIMARY KEY, username VARCHAR(50) UNIQUE, email VARCHAR(100) UNIQUE);");
            st.execute("CREATE TABLE messages (id SERIAL PRIMARY KEY, user_id INT REFERENCES users(id), content TEXT, created_at TIMESTAMP DEFAULT NOW());");
            st.execute("CREATE TABLE friends (user_id1 INT REFERENCES users(id), user_id2 INT REFERENCES users(id), PRIMARY KEY(user_id1, user_id2));");
            st.execute("CREATE TABLE likes (id SERIAL PRIMARY KEY, user_id INT REFERENCES users(id), message_id INT REFERENCES messages(id));");

        }
    }

    private static void fillDB() throws SQLException, InterruptedException {
        UserDAO userDAO = new UserDAO();
        MessageDAO msgDAO = new MessageDAO();
        LikeDAO likeDAO = new LikeDAO();

        User alice = new User("alice", "alice@mail.com");
        User bob = new User("bob", "bob@mail.com");
        User charlie = new User("charlie", "charlie@mail.com");
        User diana = new User("diana", "diana@mail.com");
        User silentUser = new User("silent_bob", "silent@mail.com");

        userDAO.create(alice);
        userDAO.create(bob);
        userDAO.create(charlie);
        userDAO.create(diana);
        userDAO.create(silentUser);

        Message m1 = new Message(alice.getId(), "Первое сообщение Алисы");
        Message m2 = new Message(alice.getId(), "Второе сообщение Алисы");
        Message m3 = new Message(bob.getId(), "Боб пишет");
        Message m4 = new Message(charlie.getId(), "От Чарли");
        Message m5 = new Message(alice.getId());
        Message m6 = new Message(bob.getId(), "Боб отвечает");


        msgDAO.create(m1);
        msgDAO.create(m2);
        msgDAO.create(m3);
        msgDAO.create(m4);
        msgDAO.create(m5);
        msgDAO.create(m6);

        likeDAO.create(new Like(bob.getId(), m1.getId()));
        likeDAO.create(new Like(charlie.getId(), m1.getId()));
        likeDAO.create(new Like(null, m1.getId()));
        likeDAO.create(new Like(alice.getId(), m3.getId()));
        likeDAO.create(new Like(null, m4.getId()));

        // цепочка: Alice -> Bob -> Charlie -> Diana
        userDAO.addFriend(alice.getId(), bob.getId());
        userDAO.addFriend(bob.getId(), charlie.getId());
        userDAO.addFriend(charlie.getId(), diana.getId());
        userDAO.addFriend(1, 5);
        userDAO.addFriend(5, 4);

        System.out.println("Данные успешно загружены!\n");
    }
}