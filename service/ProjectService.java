package com.aim.questionnaire.service;

import com.aim.questionnaire.common.utils.DateUtil;
import com.aim.questionnaire.common.utils.UUIDUtil;
import com.aim.questionnaire.dao.ProjectEntityMapper;
import com.aim.questionnaire.dao.UserEntityMapper;
import com.aim.questionnaire.dao.entity.ProjectEntity;
import com.aim.questionnaire.dao.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by wln on 2018\8\6 0006.
 */
@Service
public class ProjectService {

    @Autowired
    private ProjectEntityMapper projectEntityMapper;

    @Autowired
    private UserEntityMapper userEntityMapper;

    /**
     * 添加项目
     * @param projectEntity
     * @return
     */
    public int addProjectInfo(ProjectEntity projectEntity,String user) {
        List<Map<String,Object>>  list=projectEntityMapper.queryAllProjectName();
        for(Map<String,Object> map: list){
            if(projectEntity.getProjectName().equals(map.get("project_name"))){
                return 3;
            }
        }

        String id = UUIDUtil.getOneUUID();
        projectEntity.setId(id);
        Date date = DateUtil.getCreateTime();
        projectEntity.setCreationDate(date);
        projectEntity.setLastUpdateDate(date);
        UserEntity userEntity=userEntityMapper.selectAllByName(user);
        String userId=userEntity.getId();
        projectEntity.setUserId(userId);//可以使用自己设计的接口
        //下面的难点是怎么找到userId
        int result = projectEntityMapper.insert(projectEntity);
        return result;
    }

    /**
     * 修改项目
     * @param projectEntity
     * @return
     */
    public int modifyProjectInfo(ProjectEntity projectEntity) {
       projectEntityMapper.updateByPrimaryKey(projectEntity);
        return 0;
    }

    /**
     * 删除项目
     * @param projectEntity
     * @return
     */
    public int deleteProjectById(Map<String, Object> map) {
       String id= (String) map.get("id");
       projectEntityMapper.deleteProjectById(id);
       return 0;
    }

    /**
     * 查询项目列表
     * @param projectEntity
     * @return
     */
    public List<Object> queryProjectList(ProjectEntity projectEntity) {
        List<Object> resultList = new ArrayList<Object>();
        
        return resultList;
    }

    /**
     * 查询全部项目的名字接口
     * @return
     */
    public List<Map<String,Object>> queryAllProjectName() {
        return null;
    }

}
