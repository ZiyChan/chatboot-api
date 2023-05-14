package com.gptapi.chatboot.api.domain.zsxq.model.aggregates;

import com.gptapi.chatboot.api.domain.zsxq.model.res.RespData;

/*
* description: 待回答问题汇总（返回JSON数据的ROOT）
*/
public class UnAnsweredQuestionsAggregates {
    private boolean succeeded;

    private RespData resp_data;

    public void setSucceeded(boolean succeeded){
        this.succeeded = succeeded;
    }
    public boolean getSucceeded(){
        return this.succeeded;
    }
    public void setResp_data(RespData resp_data){
        this.resp_data = resp_data;
    }
    public RespData getResp_data(){
        return this.resp_data;
    }
}