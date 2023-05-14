package com.gptapi.chatboot.api.domain.ai.service;

import com.alibaba.fastjson.JSON;
import com.gptapi.chatboot.api.domain.ai.IOpenAI;
import com.gptapi.chatboot.api.domain.ai.model.aggregates.AIAnswer;
import com.gptapi.chatboot.api.domain.ai.model.req.QuestionReq;
import com.gptapi.chatboot.api.domain.ai.model.req.ReqData;
import com.gptapi.chatboot.api.domain.ai.model.vo.Choices;
import com.gptapi.chatboot.api.domain.ai.model.vo.Message;
import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Primary
public class OpenAI implements IOpenAI {

    Logger logger = LoggerFactory.getLogger(OpenAI.class);

    @Value("${chatboot-api.openAiKey}")
    private String openAiKey;
    @Value("${chatboot-api.openAiModel}")
    private String openAiModel;
    @Value("${chatboot-api.openAiTemperature}")
    private double openAiTemperature;

    @Value("${chatboot-api.proxyPort}")
    private int proxyPort;
    @Override
    public String doChatGPT(String question) throws IOException {

        HttpHost proxy = new HttpHost("127.0.0.1", proxyPort);
        DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
        CloseableHttpClient httpClient = HttpClientBuilder.create().setRoutePlanner(routePlanner).build();

        // CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        HttpPost post = new HttpPost("https://api.openai.com/v1/chat/completions");
        post.addHeader("Content-Type", "application/json");
        post.addHeader("Authorization", "Bearer " + openAiKey);

//        String paramJson = "{\n" +
//                "     \"model\": \"gpt-3.5-turbo\",\n" +
//                "     \"messages\": [{\"role\": \"user\", \"content\": \"" + question + "\"}],\n" +
//                "     \"temperature\": 0.7\n" +
//                "   }";
        List<Message> messages = new ArrayList<Message>();
        messages.add(new Message("user", question));
        QuestionReq questionReq = new QuestionReq(new ReqData(openAiModel, messages, openAiTemperature));
        String paramJson = JSON.toJSONString(questionReq); // 实体对象转JSON数据

        StringEntity stringEntity = new StringEntity(paramJson, ContentType.create("text/json", "UTF-8"));
        post.setEntity(stringEntity);

        CloseableHttpResponse response = httpClient.execute(post);
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

            String jsonStr = EntityUtils.toString(response.getEntity());
            logger.info("ai 返回结果:{}", jsonStr);
            AIAnswer aiAnswer = JSON.parseObject(jsonStr, AIAnswer.class);
            StringBuilder answers = new StringBuilder();
            List<Choices> choices = aiAnswer.getChoices();
            for (Choices choice : choices) {
                answers.append(choice.getMessage().getContent());
            }
            return answers.toString();
        } else {
            throw new RuntimeException("api.openai.com Err Code is " + response.getStatusLine().getStatusCode());
        }
    }
}
