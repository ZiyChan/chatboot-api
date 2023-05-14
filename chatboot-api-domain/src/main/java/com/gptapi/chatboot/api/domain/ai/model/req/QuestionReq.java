package com.gptapi.chatboot.api.domain.ai.model.req;

public class QuestionReq {
    private ReqData reqData;

    public QuestionReq(ReqData reqData) {
        this.reqData = reqData;
    }

    public void setReqData(ReqData reqData) {
        this.reqData = reqData;
    }

    public ReqData getReqData() {
        return reqData;
    }
}
