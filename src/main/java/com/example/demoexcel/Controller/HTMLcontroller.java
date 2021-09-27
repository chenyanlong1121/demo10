package com.example.demoexcel.Controller;

import com.example.demoexcel.Entity.Mission;
import com.example.demoexcel.Service.MissionService;
import com.example.demoexcel.Until.ResultUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/HTMLmission")
public class HTMLcontroller {
    @Resource
    MissionService missionService;
    Map<String,String> map=getWeekDate();
    String date1=map.get("mondayDate");
    String date2=map.get("sundayDate");
    @RequestMapping(value = "/mission",method = RequestMethod.GET)
    public void getMission(@RequestParam("filename") String filename, HttpServletResponse response){
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put(date1,date2);
        List<String> directors= missionService.queryByfilename(filename);
        for(int i=0;i<directors.size();i++){
            resultMap.put(directors.get(i), missionService.queryBydirector(filename,directors.get(i),date1,date2));
        }
        ResultUtil.responseJson(response,resultMap);
    }

    public  Map<String,String> getWeekDate() {
        Map<String,String> map = new HashMap();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

        Calendar cal = Calendar.getInstance();
        // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        // 获得当前日期是一个星期的第几天
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
        if(dayWeek==1){
            dayWeek = 8;
        }

        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - dayWeek);// 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        Date mondayDate = cal.getTime();
        String weekBegin = sdf.format(mondayDate);

        cal.add(Calendar.DATE, 4 +cal.getFirstDayOfWeek());
        Date sundayDate = cal.getTime();
        String weekEnd = sdf.format(sundayDate);

        map.put("mondayDate", weekBegin);
        map.put("sundayDate", weekEnd);
        return map;
    }
}
