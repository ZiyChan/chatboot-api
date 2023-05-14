package com.gptapi.chatboot.api.application.job;

import com.alibaba.fastjson.JSON;
import com.gptapi.chatboot.api.domain.ai.IOpenAI;
import com.gptapi.chatboot.api.domain.zsxq.IZsxqApi;
import com.gptapi.chatboot.api.domain.zsxq.model.aggregates.UnAnsweredQuestionsAggregates;
import com.gptapi.chatboot.api.domain.zsxq.model.vo.Topics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;



@EnableScheduling
@Configuration
public class ChatbotSchedule {

    Logger logger = LoggerFactory.getLogger(ChatbotSchedule.class);
    @Value("${chatboot-api.groupId}")
    private String groupId;
    @Value("${chatboot-api.cookie}")
    private String cookie;

    @Resource
    private IZsxqApi zsxqApi;

    @Autowired
    private IOpenAI openAI;


    @Scheduled(cron = "0/30 * * * * ?")
    public void run() {
        try {
            logger.info("hi...");
            // 随机打烊
            if (new Random().nextBoolean()) {
                logger.info("随机打烊中...");
                return;
            }

            // 下班时间
//        GregorianCalendar calendar = new GregorianCalendar();
//        int hour = calendar.get(Calendar.HOUR_OF_DAY);
//        if (hour > 22 || hour < 7) {
//            logger.info("打烊时间不工作，AI 下班了！");
//            return;
//        }

            // 1. 检索问题
            UnAnsweredQuestionsAggregates unAnsweredQuestionsAggregates = zsxqApi.queryUnAnsweredQuestionsTopicId(groupId, cookie);
            logger.info("检索结果：{}", JSON.toJSONString(unAnsweredQuestionsAggregates));
            List<Topics> topics = unAnsweredQuestionsAggregates.getResp_data().getTopics();
            if (topics == null || topics.isEmpty()) {
                logger.info("本次未查询到待回答问题");
                return;
            }

            //2. AI 回复
            Topics topic = topics.get(0);// 每次只处理第一个问题
            String topicId = topic.getTopic_id();
            String questionText = topic.getQuestion().getText().trim();
            String aiAnswer = openAI.doChatGPT(questionText);

            // 3.回答问题
            boolean status = zsxqApi.answer(groupId, cookie, topicId, aiAnswer, false);
            logger.info("编号：{} 问题：{} 回答：{} 状态：{}", topicId, questionText, aiAnswer, status);


        } catch (Exception e) {
            logger.error("自动问答异常", e);
        }

    }

}
