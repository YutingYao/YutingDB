package com.msb.service.process;

/**
 * ClassName DataService
 * Description
 * Create by liudz
 * Date 2020/9/14 12:04 上午
 */

import javax.servlet.http.HttpServletRequest;

/**
 * 数据采集服务器中的业务接口
 */
public interface DataService {

    void process(String dataType, HttpServletRequest request) throws  Exception;
}
