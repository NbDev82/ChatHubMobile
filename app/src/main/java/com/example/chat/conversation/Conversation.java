package com.example.chat.conversation;

import com.example.chat.enums.EType;
import com.example.chat.Utils;

import java.util.HashMap;

import javax.annotation.Nullable;

public class Conversation {

    private Long id;
    private String name;
    private EType type;
    private boolean isDeleted;

    public Conversation() {
        this.id = 0L;
        this.name = "name";
        this.type = EType.CHANNEL;
        this.isDeleted = false;
    }

    public Conversation(Long id, String name, EType type, boolean isDeleted) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.isDeleted = isDeleted;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EType getType() {
        return type;
    }

    public void setType(EType type) {
        this.type = type;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public HashMap<String, Object> convertHashMap() {
        HashMap<String, Object> conversation = new HashMap<>();

        conversation.put(Utils.KEY_NAME, name);
        conversation.put(Utils.KEY_TYPE, type);
        conversation.put(Utils.KEY_IS_DELETED, isDeleted);

        return  conversation;
    }
}
