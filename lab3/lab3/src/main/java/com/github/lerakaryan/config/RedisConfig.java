package com.github.lerakaryan.config;

import redis.clients.jedis.Jedis;

public class RedisConfig {
    private static final String HOST = "localhost";
    private static final int PORT = 6379;
    private static final String PASSWORD = "password123";

    public static Jedis getConnection() {
        Jedis jedis = new Jedis(HOST, PORT);
        jedis.auth(PASSWORD);
        return jedis;
    }
}
