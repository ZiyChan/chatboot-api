package com.gptapi.chatboot.api.test;


import com.gptapi.chatboot.api.domain.ai.IOpenAI;
import com.gptapi.chatboot.api.domain.zsxq.IZsxqApi;
import com.gptapi.chatboot.api.domain.zsxq.model.aggregates.UnAnsweredQuestionsAggregates;
import com.gptapi.chatboot.api.domain.zsxq.model.vo.Topics;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

// @RunWith(SpringRunner.class)使用Spring测试框架来运行测试，使得测试运行在Spring容器环境下
@RunWith(SpringRunner.class)
// @SpringBootTest注解将默认扫描与启动类相同的包以及其子包中的所有Spring注解组件
@SpringBootTest
public class SpringBootRunTest {

    private Logger logger = LoggerFactory.getLogger(SpringBootRunTest.class);

    @Value("${chatboot-api.groupId}")
    private String groupId;
    @Value("${chatboot-api.cookie}")
    private String cookie;

    @Autowired
    private IZsxqApi zsxqApi;

    @Autowired
    private IOpenAI openAI;

    @Test
    public void test_zsxqApi() throws IOException {
        // logger.info("启动");
        // logger.info("groupId: {} cookie: {}", groupId, cookie);
        UnAnsweredQuestionsAggregates unAnsweredQuestionsAggregates = zsxqApi.queryUnAnsweredQuestionsTopicId(groupId, cookie);
        logger.info("测试结果：{}", JSON.toJSONString(unAnsweredQuestionsAggregates));

        List<Topics> topics = unAnsweredQuestionsAggregates.getResp_data().getTopics();
        for (Topics topic : topics) {
            String topicId = topic.getTopic_id();
            String text = topic.getQuestion().getText();
            logger.info("topicId：{} text：{}", topicId, text);

            // 回答问题
            // zsxqApi.answer(groupId, cookie, topicId, text, false);
        }
    }

    @Test
    public void test_openAi() throws IOException {
        String response = openAI.doChatGPT("帮我写一个java冒泡排序");
        logger.info("测试结果：{}", response);
    }

}
