package com.gptapi.chatboot.api.domain.ai.model.vo;

public class Message {
    private String role;

    private String content;

    public Message(String role, String content) {
        this.role = role;
        this.content = content;
    }

    public void setRole(String role){
        this.role = role;
    }
    public String getRole(){
        return this.role;
    }
    public void setContent(String content){
        this.content = content;
    }
    public String getContent(){
        return this.content;
    }
}
