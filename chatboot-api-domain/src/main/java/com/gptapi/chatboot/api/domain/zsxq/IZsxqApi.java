package com.gptapi.chatboot.api.domain.zsxq;

import com.gptapi.chatboot.api.domain.zsxq.model.aggregates.UnAnsweredQuestionsAggregates;

import java.io.IOException;

public interface IZsxqApi {
    // 获取待回答问题
    UnAnsweredQuestionsAggregates queryUnAnsweredQuestionsTopicId(String groupId, String cookie) throws IOException;
    // 回答问题
    boolean answer(String groupId, String cookie, String topicId, String text, boolean silenced) throws IOException;
}
