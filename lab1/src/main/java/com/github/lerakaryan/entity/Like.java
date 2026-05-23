package com.github.lerakaryan.entity;

public class Like {
    private Integer id;
    private Integer userId; // Integer для поддержки NULL
    private int messageId;

    public Like() {}
    public Like(Integer userId, int messageId) { this.userId = userId; this.messageId = messageId; }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    public int getMessageId() { return messageId; }
    public void setMessageId(int messageId) { this.messageId = messageId; }
}
