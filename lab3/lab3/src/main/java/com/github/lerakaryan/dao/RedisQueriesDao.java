package com.github.lerakaryan.dao;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;
import java.util.List;
import java.util.Set;

public class RedisQueriesDao {
    private final Jedis jedis;

    public RedisQueriesDao(Jedis jedis) {
        this.jedis = jedis;
    }

    public List<String> getHistory(String userId) {
        return jedis.lrange("history:" + userId, 0, -1);
    }

    public Set<String> getIntersection(String setKey1, String setKey2) {
        return jedis.sinter(setKey1, setKey2);
    }

    public Set<String> getDifference(String setKey1, String setKey2) {
        return jedis.sdiff(setKey1, setKey2);
    }

    public List<String> getTopSales(String sortedSetKey) {
        return jedis.zrevrange(sortedSetKey, 0, -1);
    }

    public void publishMessage(String channel, String message) {
        jedis.publish(channel, message);
    }

    public void subscribeChannel(String channel) {
        new Thread(() -> {
            try (Jedis subJedis = new Jedis("localhost", 6379)) {
                subJedis.auth("password123");
                subJedis.subscribe(new JedisPubSub() {
                    @Override
                    public void onMessage(String channel, String message) {
                        System.out.println("Канал: " + channel + ". Получено сообщение: " + message);
                    }
                }, channel);
            }
        }).start();
    }
}