package com.aim.questionnaire.controller;

import com.aim.questionnaire.beans.HttpResponseEntity;
import com.aim.questionnaire.common.Constans;
import com.aim.questionnaire.common.utils.DateUtil;
import com.aim.questionnaire.dao.ProjectEntityMapper;
import com.aim.questionnaire.dao.entity.ProjectEntity;
import com.aim.questionnaire.service.ProjectService;

import com.aim.questionnaire.service.QuestionnaireService;
import org.apache.tomcat.util.bcel.Const;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by wln on 2018\8\6 0006.
 */
@RestController
public class ProjectController {

    private final Logger logger = LoggerFactory.getLogger(ProjectController.class);

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ProjectEntityMapper projectEntityMapper;

    @Autowired
    private QuestionnaireService questionnaireService;
    /**
     * 查询全部项目的信息
     * @param projectEntity
     * @return
     * 目前是将所有的项目都查询出来了
     *///
    @RequestMapping(value = "/queryProjectList",method = RequestMethod.POST, headers = "Accept=application/json")
    public HttpResponseEntity queryProjectList(@RequestBody(required = false) ProjectEntity projectEntity) {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        List<Map<String,Object>>  result =projectEntityMapper.queryProjectList(projectEntity);
        httpResponseEntity.setData(result);
        httpResponseEntity.setCode(Constans.SUCCESS_CODE);
        httpResponseEntity.setMessage(Constans.STATUS_MESSAGE);
        return httpResponseEntity;
    }
    @RequestMapping(value = "/queryProjectListById",method = RequestMethod.POST, headers = "Accept=application/json")
    public HttpResponseEntity queryProjectListById(@RequestBody(required = false) ProjectEntity projectEntity) {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        List<Map<String,Object>>  result =projectEntityMapper.queryProjectListById(projectEntity);
        httpResponseEntity.setData(result);
        httpResponseEntity.setCode(Constans.SUCCESS_CODE);
        httpResponseEntity.setMessage(Constans.STATUS_MESSAGE);
        return httpResponseEntity;
    }

    /**
     * 根据id删除项目
     * @param
     * @return
     */
    @RequestMapping(value = "/deleteProjectById",method = RequestMethod.POST, headers = "Accept=application/json")
    public HttpResponseEntity deleteProjectById(@RequestBody Map<String,Object> map1) {

        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        List<Map<String,Object>> list=questionnaireService.queryQuestionListByProjectId((String) map1.get("id"));
        for(Map<String,Object> map:list){
            LocalDateTime now=LocalDateTime.now();
            LocalDateTime endTime=(LocalDateTime)map.get("end_time");
            LocalDateTime startTime= (LocalDateTime) map.get("start_time");
            if(now.compareTo(startTime)>0&&now.compareTo(endTime)<0){
                httpResponseEntity.setCode(Constans.LOGOUT_NO_CODE);
                httpResponseEntity.setMessage("项目有正在进行中的问卷，删除失败");
                return httpResponseEntity;
            }
        }
        projectService.deleteProjectById(map1);
        httpResponseEntity.setCode(Constans.SUCCESS_CODE);
        httpResponseEntity.setMessage(Constans.DELETE_MESSAGE);
        return httpResponseEntity;
    }

    /**
     * 添加项目
     * @param projectEntity
     * @return
     */
    @RequestMapping(value = "/addProjectInfo",method = RequestMethod.POST, headers = "Accept=application/json")
    public HttpResponseEntity addProjectInfo(@RequestBody ProjectEntity projectEntity) {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        String username=projectEntity.getCreatedBy();
        int result=projectService.addProjectInfo(projectEntity,username);
        if(result == 3) {
            httpResponseEntity.setCode(Constans.USER_USERNAME_CODE);
            httpResponseEntity.setMessage("项目名称重复");
        }else{
            httpResponseEntity.setMessage(Constans.ADD_MESSAGE);
            httpResponseEntity.setCode(Constans.SUCCESS_CODE);
        }
        return httpResponseEntity;

    }

    /**
     * 根据项目id修改项目
     * @param projectEntity
     * @return
     */
    @RequestMapping(value = "/modifyProjectInfo",method = RequestMethod.POST, headers = "Accept=application/json")
    public HttpResponseEntity modifyProjectInfo(@RequestBody ProjectEntity projectEntity) {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        Date date= DateUtil.getCurrentDate();
        projectEntity.setLastUpdateDate(date);
        //在这里查询有没有相关联的正在进行中的问卷，调用QuestionnaireService层的东西
        List<Map<String,Object>> list=questionnaireService.queryQuestionListByProjectId(projectEntity.getId());
        for(Map<String,Object> map:list){
            LocalDateTime startTime= (LocalDateTime) map.get("start_time");
            LocalDateTime endTime=(LocalDateTime)map.get("end_time");
            LocalDateTime now=LocalDateTime.now();
            if(now.compareTo(startTime)>0&&now.compareTo(endTime)<0){
                httpResponseEntity.setCode(Constans.LOGOUT_NO_CODE);
                httpResponseEntity.setMessage("项目中有问卷正在进行，不可以编辑");
                return httpResponseEntity;
            }
        }
        projectService.modifyProjectInfo(projectEntity);
        httpResponseEntity.setCode(Constans.SUCCESS_CODE);
        httpResponseEntity.setMessage(Constans.UPDATE_MESSAGE);
        return httpResponseEntity;
    }



    /**
     * 查询全部项目的名字接口
     * @return
     */
    @RequestMapping(value = "/queryAllProjectName",method = RequestMethod.POST, headers = "Accept=application/json")
    public HttpResponseEntity queryAllProjectName() {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();

        return httpResponseEntity;
    }
}
