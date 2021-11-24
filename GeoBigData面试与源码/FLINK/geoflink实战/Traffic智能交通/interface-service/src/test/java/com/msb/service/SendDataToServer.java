package com.msb.service;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.nio.charset.Charset;

/**
 * ClassName SendDataToServer
 * Description
 * Create by liudz
 * Date 2020/9/14 1:38 上午
 */
public class SendDataToServer {
    public static void main(String[] args) {
        String url ="http://localhost:8686/controller/sendData/traffic_data";
        HttpClient client = HttpClients.createDefault();
        String result=null;
        try{
            int i =0;
            while (i<20){
                HttpPost post = new HttpPost(url);
                post.setHeader("Content-Type","application/json");
                String data ="11,22,33,京P12345,57.2,"+i;
                post.setEntity(new StringEntity(data, Charset.forName("UTF-8")));
                HttpResponse response =client.execute(post); //发送数据
                i++;
                Thread.sleep(1000);

                //响应的状态如果是200的话，获取服务器返回的内容
                if(response.getStatusLine().getStatusCode()== HttpStatus.SC_OK){
                    result= EntityUtils.toString(response.getEntity(),"UTF-8");
                }
                System.out.println(result);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
