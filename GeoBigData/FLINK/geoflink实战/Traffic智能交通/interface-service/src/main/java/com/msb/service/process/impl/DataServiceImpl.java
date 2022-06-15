package com.msb.service.process.impl;

import com.msb.service.bean.CustomException;
import com.msb.service.bean.ResponseCodePropertyConfig;
import com.msb.service.constant.ResponseConstant;
import com.msb.service.process.DataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedInputStream;
import java.nio.charset.StandardCharsets;

@Service("dataService")
public class DataServiceImpl implements DataService {

    Logger logger = LoggerFactory.getLogger(DataServiceImpl.class);

    @Autowired
    private ResponseCodePropertyConfig config;

    /**
     * 接受数据，并通过Logger保存数据到文件中
     * @param dataType 数据类型
     * @param request 请求对象
     * @throws Exception
     */
    public void process(String dataType, HttpServletRequest request) throws Exception {
        //保护性的判断
        if(StringUtils.isEmpty(dataType)){ //数据类型没有传入，抛出异常
            throw new CustomException(ResponseConstant.CODE_0001,config.getMsg(ResponseConstant.CODE_0001),dataType);
        }

        //判断请求头中是否传入数据
        int contentLength = request.getContentLength();
        if(contentLength<1){
            throw new CustomException(ResponseConstant.CODE_0002,config.getMsg(ResponseConstant.CODE_0002),dataType);
        }

        //从Request中读取数据
        byte[] datas = new byte[contentLength]; //存放数据的字节数组

        BufferedInputStream input = new BufferedInputStream(request.getInputStream());

        //最大尝试读取的次数
        int tryTime=0;
        //最大尝试读取次数内，最终读取的数据长度
        int totalRealLeagth=0;

        int maxTryTime=100; //容错性

        while (totalRealLeagth<contentLength && tryTime< maxTryTime){
            int readLength = input.read(datas, totalRealLeagth, contentLength - totalRealLeagth);
            if(readLength<0){
                throw new CustomException(ResponseConstant.CODE_0007,config.getMsg(ResponseConstant.CODE_0007),dataType);
            }
            totalRealLeagth+=readLength;

            if(totalRealLeagth==contentLength){
                break;
            }
            tryTime++;
            Thread.sleep(200);
        }

        if(totalRealLeagth<contentLength){ //经过多处的读取，数据任然没有读完
            throw new CustomException(ResponseConstant.CODE_0007,config.getMsg(ResponseConstant.CODE_0007),dataType);
        }
        input.close();

        String jsonStr = new String(datas, StandardCharsets.UTF_8);


        logger.info(jsonStr);
    }
}

