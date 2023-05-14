package com.gptapi.chatboot.api.test;

import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.IOException;

/*
* description: 单元测试
*/

public class ApiTest {
    @Test
    public void query_unanswered_questions() throws IOException {
        // 1.建立HTTP客户端（浏览器）对象，对象可以发送HTTP请求
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        // 2.创建一个HttpGet对象，设置请求的URL和参数
        HttpGet get = new HttpGet("https://api.zsxq.com/v2/groups/88885824814422/topics?scope=unanswered_questions&count=20");
        // 3.设置请求头部信息，包括cookie和Content-Type
        // cookie是该API需要的身份验证信息
        // content-type是请求数据的数据类型，这里数据是JSON格式
        get.addHeader("cookie", "zsxq_access_token=7B5F459D-794D-DD71-D699-2F7B394FC223_100957CB2757E387; zsxqsessionid=5755b6c834e2508b7e2c797e215fc8a5; abtest_env=product; sajssdk_2015_cross_new_user=1; sensorsdata2015jssdkcross={\"distinct_id\":\"818851185851252\",\"first_id\":\"18804547143b6d-05302e12ae99878-7b515477-1247616-18804547144abf\",\"props\":{},\"identities\":\"eyIkaWRlbnRpdHlfY29va2llX2lkIjoiMTg4MDQ1NDcxNDNiNmQtMDUzMDJlMTJhZTk5ODc4LTdiNTE1NDc3LTEyNDc2MTYtMTg4MDQ1NDcxNDRhYmYiLCIkaWRlbnRpdHlfbG9naW5faWQiOiI4MTg4NTExODU4NTEyNTIifQ==\",\"history_login_id\":{\"name\":\"$identity_login_id\",\"value\":\"818851185851252\"},\"$device_id\":\"18804547143b6d-05302e12ae99878-7b515477-1247616-18804547144abf\"}; UM_distinctid=188045c560c96-09676a070901e4-7b515477-130980-188045c560d169");
        get.addHeader("content-type", "application/json; charset=UTF-8");
        // 4.发送HTTP请求 GET
        // 如果响应状态码为200，则表示请求成功，将响应数据转换为字符串并输出
        CloseableHttpResponse response = httpClient.execute(get);
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            String res = EntityUtils.toString(response.getEntity());
            System.out.println(res);
        } else {
            System.out.println(response.getStatusLine().getStatusCode());
        }
    }

    @Test
    public void answer() throws IOException {
        // 1.建立HTTP客户端（浏览器）对象，对象可以发送HTTP请求
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        // 2.创建一个HttpPost对象，设置请求的URL topic_id("181481411411852") 标识待回答的问题id
        HttpPost post = new HttpPost("https://api.zsxq.com/v2/topics/181481485841852/answer");
        // 3.设置请求头部信息，包括cookie和Content-Type
        // cookie是该API需要的身份验证信息
        // content-type是请求数据的数据类型，这里数据是JSON格式
        post.addHeader("cookie", "zsxq_access_token=7B5F459D-794D-DD71-D699-2F7B394FC223_100957CB2757E387; zsxqsessionid=5755b6c834e2508b7e2c797e215fc8a5; abtest_env=product; sajssdk_2015_cross_new_user=1; sensorsdata2015jssdkcross={\"distinct_id\":\"818851185851252\",\"first_id\":\"18804547143b6d-05302e12ae99878-7b515477-1247616-18804547144abf\",\"props\":{},\"identities\":\"eyIkaWRlbnRpdHlfY29va2llX2lkIjoiMTg4MDQ1NDcxNDNiNmQtMDUzMDJlMTJhZTk5ODc4LTdiNTE1NDc3LTEyNDc2MTYtMTg4MDQ1NDcxNDRhYmYiLCIkaWRlbnRpdHlfbG9naW5faWQiOiI4MTg4NTExODU4NTEyNTIifQ==\",\"history_login_id\":{\"name\":\"$identity_login_id\",\"value\":\"818851185851252\"},\"$device_id\":\"18804547143b6d-05302e12ae99878-7b515477-1247616-18804547144abf\"}; UM_distinctid=188045c560c96-09676a070901e4-7b515477-130980-188045c560d169");
        post.addHeader("content-type", "application/json; charset=UTF-8");
        // 4.设置POST请求的参数，这里使用一个JSON字符串，并将其封装为StringEntity对象
        // text表示回答的文本内容
        // silenced true表示不公开
        String paraJson = "{\n" +
                "  \"req_data\": {\n" +
                "    \"text\": \"我爱吃水果!!!\\n\",\n" +
                "    \"image_ids\": [],\n" +
                "    \"silenced\": false\n" +
                "  }\n" +
                "}";
        StringEntity stringEntity = new StringEntity(paraJson, ContentType.create("text/json", "UTF-8"));
        post.setEntity(stringEntity);
        // 5.发送HTTP请求 POST
        CloseableHttpResponse response = httpClient.execute(post);
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            String res = EntityUtils.toString(response.getEntity());
            System.out.println(res);
        } else {
            System.out.println(response.getStatusLine().getStatusCode());
        }
    }

    @Test
    public void testChatGpt() throws IOException {

        HttpHost proxy = new HttpHost("127.0.0.1", 7890);
        DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
        CloseableHttpClient httpClient = HttpClientBuilder.create().setRoutePlanner(routePlanner).build();

        // CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        HttpPost post = new HttpPost("https://api.openai.com/v1/chat/completions");
        post.addHeader("Content-Type", "application/json");
        post.addHeader("Authorization", "Bearer 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");

        String paramJson = "{\n" +
                "     \"model\": \"gpt-3.5-turbo\",\n" +
                "     \"messages\": [{\"role\": \"user\", \"content\": \"Say this is a test!\"}],\n" +
                "     \"temperature\": 0.7\n" +
                "   }";
        StringEntity stringEntity = new StringEntity(paramJson, ContentType.create("text/json", "UTF-8"));
        post.setEntity(stringEntity);

        CloseableHttpResponse response = httpClient.execute(post);
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            String res = EntityUtils.toString(response.getEntity());
            System.out.println(res);
        } else {
            System.out.println(response.getStatusLine().getStatusCode());
        }
    }

}
