package com.example.demoexcel.Until;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class ResultUtil {
    /**
     * 私有化构造器
     */
    private ResultUtil(){}
    //response 输出json
    public static void responseJson(ServletResponse response, Map<String, Object> resultMap){
        PrintWriter out = null;
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            out = response.getWriter();
            out.println(JSON.toJSONString(resultMap));
        } catch (Exception e) {
            log.error("【JSON输出异常】"+e);
        }finally{
            if(out!=null){
                out.flush();
                out.close();
            }
        }
    }

    //返回通用实例
    public static Map<String, Object> resultCode(String director, List<String> missionName){
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put(director,missionName);
        return resultMap;
    }
}
