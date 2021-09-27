package com.example.demoexcel.Controller;


import com.example.demoexcel.Entity.Center;
import com.example.demoexcel.Service.CenterService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * (Center)表控制层
 *
 * @author makejava
 * @since 2021-09-08 13:28:09
 */
@RestController
@RequestMapping("center")
public class CenterController {
    /**
     * 服务对象
     */
    @Resource
    private CenterService centerService;

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
//    @GetMapping("selectOne")
//    public Center selectOne(Integer id) {
//        return this.centerService.queryById(id);
//    }

}
