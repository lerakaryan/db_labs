package com.github.lerakaryan.entity;
import java.sql.Timestamp;

public class Message {
    private int id;
    private int userId;
    private String content;
    private Timestamp createdAt;

    public Message() {}
    public Message(int userId, String content) { this.userId = userId; this.content = content; }
    public Message(int id, int userId, String content, Timestamp createdAt) {
        this.id = id; this.userId = userId; this.content = content; this.createdAt = createdAt;
    }
    public Message(int userId) { this.userId = userId; this.content = null; }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public Timestamp getCreatedAt() { return createdAt; }

    @Override
    public String toString() { return "Message{id=" + id + ", content='" + content + "'}"; }
}
