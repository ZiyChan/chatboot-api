package com.gptapi.chatboot.api.domain.ai.model.req;

import com.gptapi.chatboot.api.domain.ai.model.vo.Message;

import java.util.List;

public class ReqData {
    private String model;

    private List<Message> messages;

    private double temperature;

    public ReqData(String model, List<Message> messages, double temperature) {
        this.model = model;
        this.messages = messages;
        this.temperature = temperature;
    }

    public void setModel(String model){
        this.model = model;
    }
    public String getModel(){
        return this.model;
    }
    public void setMessages(List<Message> messages){
        this.messages = messages;
    }
    public List<Message> getMessages(){
        return this.messages;
    }
    public void setTemperature(double temperature){
        this.temperature = temperature;
    }
    public double getTemperature(){
        return this.temperature;
    }
}