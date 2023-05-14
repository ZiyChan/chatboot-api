package com.gptapi.chatboot.api.domain.zsxq.service;

import com.gptapi.chatboot.api.domain.zsxq.IZsxqApi;
import com.gptapi.chatboot.api.domain.zsxq.model.aggregates.UnAnsweredQuestionsAggregates;
import com.gptapi.chatboot.api.domain.zsxq.model.req.AnswerReq;
import com.gptapi.chatboot.api.domain.zsxq.model.req.ReqData;
import com.gptapi.chatboot.api.domain.zsxq.model.res.AnswerRes;
import net.sf.json.JSONObject;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ZsxqApi implements IZsxqApi {

    private Logger logger = LoggerFactory.getLogger(ZsxqApi.class);

    @Override
    public UnAnsweredQuestionsAggregates queryUnAnsweredQuestionsTopicId(String groupId, String cookie) throws IOException {
        // 1.建立HTTP客户端（浏览器）对象，对象可以发送HTTP请求
        CloseableHttpClient httpclient = HttpClientBuilder.create().build();
        // 2.创建一个HttpGet对象，设置请求的URL和参数
        HttpGet get = new HttpGet("https://api.zsxq.com/v2/groups/" + groupId + "/topics?scope=unanswered_questions&count=20");
        // 3.设置请求头部信息，包括cookie和Content-Type
        // cookie是该API需要的身份验证信息
        // content-type是请求数据的数据类型，这里数据是JSON格式
        get.addHeader("cookie", cookie);
        get.addHeader("content-type", "application/json; charset=UTF-8");
        // 4.发送HTTP请求 GET
        // 如果响应状态码为200，则表示请求成功，将响应数据转换为字符串并输出
        CloseableHttpResponse response = httpclient.execute(get);
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            String jsonStr = EntityUtils.toString(response.getEntity());
            logger.info("拉取提问数据。groupId：{} jsonStr：{}", groupId, jsonStr);
            // JSON数据转实体对象
            return JSON.parseObject(jsonStr, UnAnsweredQuestionsAggregates.class);

        } else {
            throw new RuntimeException("queryUnAnsweredQuestionsTopicId Err Code is " + response.getStatusLine().getStatusCode());
        }
    }


    @Override
    public boolean answer(String groupId, String cookie, String topicId, String text, boolean silenced) throws IOException {
        // 1.建立HTTP客户端（浏览器）对象，对象可以发送HTTP请求
        CloseableHttpClient httpclient = HttpClientBuilder.create().build();
        // 2.创建一个HttpPost对象，设置请求的URL topic_id("181481411411852") 标识待回答的问题id
        HttpPost post = new HttpPost("https://api.zsxq.com/v2/topics/" + topicId + "/answer");
        // 3.设置请求头部信息，包括cookie和Content-Type
        // cookie是该API需要的身份验证信息
        // content-type是请求数据的数据类型，这里数据是JSON格式
        post.addHeader("cookie", cookie);
        post.addHeader("content-type", "application/json; charset=UTF-8");
        post.addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/113.0.0.0 Safari/537.36 Edg/113.0.1774.35");
        // 4.设置POST请求的参数，这里使用一个JSON字符串，并将其封装为StringEntity对象
        // text表示回答的文本内容
        // silenced true表示不公开
        /* 测试数据
           String paraJson = "{\n" +
                "  \"req_data\": {\n" +
                "    \"text\": \"我爱吃水果\\n\",\n" +
                "    \"image_ids\": [],\n" +
                "    \"silenced\": true\n" +
                "  }\n" +
                "}";
        */

        // 生成回答（post请求对象）
        AnswerReq answerReq = new AnswerReq(new ReqData(text, silenced));
        // 实体对象转JSON数据
        String paramJson = JSON.toJSONString(answerReq);
//        logger.info("JSON.toJSONString: {}", paramJson);
//        String paramJson = JSONObject.fromObject(answerReq).toString();
//        logger.info("JSONObject.fromObject: {}", paramJson);


        StringEntity stringEntity = new StringEntity(paramJson, ContentType.create("text/json", "UTF-8"));
        post.setEntity(stringEntity);
        // 5.发送HTTP请求 POST
        CloseableHttpResponse response = httpclient.execute(post);
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            String jsonStr = EntityUtils.toString(response.getEntity());
            logger.info("回答问题结果。groupId：{} topicId：{} jsonStr：{}", groupId, topicId, jsonStr);
            AnswerRes answerRes = JSON.parseObject(jsonStr, AnswerRes.class);
            return answerRes.isSucceeded();

        } else {
            throw new RuntimeException("answer Err Code is " + response.getStatusLine().getStatusCode());
        }
    }
}
