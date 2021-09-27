package com.example.demoexcel.Service.Impl;


import com.example.demoexcel.Dao.MissionDao;
import com.example.demoexcel.Entity.Mission;
import com.example.demoexcel.Service.MissionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * (Mission)表服务实现类
 *
 * @author makejava
 * @since 2021-09-08 13:28:34
 */
@Service("missionService")
public class MissionServiceImpl implements MissionService {
    @Resource
    private MissionDao missionDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public Mission queryById(Integer id) {
        return this.missionDao.queryById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    @Override
    public List<Mission> queryAllByLimit(int offset, int limit) {
        return this.missionDao.queryAllByLimit(offset, limit);
    }

    @Override
    public List<Mission> queryAllByDate(String date1, String date2,Integer model_name,String excel_name) {
        return this.missionDao.queryAllBydate(date1, date2,model_name,excel_name);
    }

    @Override
    public List<Mission> queryByModel_id(Integer model_id,String excel_name) {
        return this.missionDao.queryByModel_id(model_id,excel_name);
    }

    @Override
    public List<String> queryByfilename(String excel_name) {
        return this.missionDao.queryByfilename(excel_name);
    }

    @Override
    public List<String> queryBydirector(String excel_name, String director,String date1,String date2) {
        return this.missionDao.queryBydirector(director,excel_name,date1,date2);
    }

    @Override
    public List<String> queryFile() {
        return this.missionDao.queryFile();
    }

    /**
     * 查询多条数据
     *
     * @return 对象列表
     */
    @Override
    public List<Mission> queryAllMissions() {
        return this.missionDao.queryAllMissions();
    }

    /**
     * 新增数据
     *
     * @param mission 实例对象
     * @return 实例对象
     */
    @Override
    public Mission insert(Mission mission) {
        this.missionDao.insert(mission);
        return mission;
    }

    /**
     * 修改数据
     *
     * @param mission 实例对象
     * @return 实例对象
     */
    @Override
    public Mission update(Mission mission) {
        this.missionDao.update(mission);
        return this.queryById(mission.getId());
    }
    /**
     * 修改数据
     *
     * @param mission 实例对象
     * @return 实例对象
     */
    @Override
    public Mission updateOrinsert(Mission mission) {
        this.missionDao.updateOrinsert(mission);
        return this.queryById(mission.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.missionDao.deleteById(id) > 0;
    }
}
