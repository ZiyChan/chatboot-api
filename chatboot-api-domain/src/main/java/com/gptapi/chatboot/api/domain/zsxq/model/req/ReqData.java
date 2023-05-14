package com.gptapi.chatboot.api.domain.zsxq.model.req;

import java.util.ArrayList;
import java.util.List;

public class ReqData {
    private String text;

    private List<String> image_ids;

    private boolean silenced;

    public ReqData(String text, boolean silenced) {
        this.text = text;
        this.silenced = silenced;
    }

    public void setText(String text){
        this.text = text;
    }
    public String getText(){
        return this.text;
    }
    public void setImage_ids(List<String> image_ids){
        this.image_ids = image_ids;
    }
    public List<String> getImage_ids(){
        return this.image_ids;
    }
    public void setSilenced(boolean silenced){
        this.silenced = silenced;
    }
    public boolean getSilenced(){
        return this.silenced;
    }
}
