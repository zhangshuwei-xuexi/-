package com.aim.questionnaire.controller;

import com.aim.questionnaire.beans.HttpResponseEntity;
import com.aim.questionnaire.common.Constans;
import com.aim.questionnaire.service.ProjectService;
import com.aim.questionnaire.service.QuestionnaireService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.tomcat.util.bcel.Const;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class QuestionnaireController {
    private final Logger logger = LoggerFactory.getLogger(ProjectController.class);
    @Autowired
    private QuestionnaireService questionnaireService;
    @RequestMapping(value = "/deleteQuestionnaireById",method = RequestMethod.POST, headers = "Accept=application/json")
    public HttpResponseEntity deleteQuestionnaireById(@RequestBody Map<String,Object> map){
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        String id= (String) map.get("id");
        int type=questionnaireService.deleteQuestionnaireById(id);
        if(type==-1){
            httpResponseEntity.setCode(Constans.LOGOUT_NO_CODE);
            httpResponseEntity.setMessage("问卷已启动，请等待停止后再删除");
        }else if(type==-2){
            httpResponseEntity.setCode(Constans.LOGOUT_NO_CODE);
            httpResponseEntity.setMessage("问卷已发送，无法删除");
        } else if(type==1){
            httpResponseEntity.setCode(Constans.SUCCESS_CODE);
            httpResponseEntity.setMessage(Constans.DELETE_MESSAGE);
        }else{
            httpResponseEntity.setCode(Constans.LOGOUT_NO_CODE);
            httpResponseEntity.setMessage("question_stop==1");
        }
        return httpResponseEntity;
    }
    @RequestMapping(value = "/queryQuestionnaireByProjectId",
            method = RequestMethod.POST, headers = "Accept=application/json")
    public HttpResponseEntity queryQuestionnaireByProjectId(@RequestBody Map<String,Object> map){
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        httpResponseEntity.setCode(Constans.SUCCESS_CODE);
        String id= (String) map.get("id");
        List<Map<String,Object>> list=questionnaireService.queryQuestionListByProjectId(id);
        httpResponseEntity.setData(list);
        return httpResponseEntity;
    }

    @RequestMapping(value = "/queryAllDataType",method = RequestMethod.POST, headers = "Accept=application/json")
    public HttpResponseEntity queryAllDataType(@RequestBody Map<String,Object> map) {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        httpResponseEntity.setCode(Constans.SUCCESS_CODE);
        Map<String,Object> map1=new HashMap<>();
        map1.put("name","老师调查");
        map1.put("id",1);
        Map<String,Object> map2=new HashMap<>();
        map2.put("name","在校生调查");
        map2.put("id",2);
        Map<String,Object> map3=new HashMap<>();
        map3.put("name","毕业生调查");
        map3.put("id",3);
        Map<String,Object> map4=new HashMap<>();
        map4.put("name","企业调查");
        map4.put("id",4);
        List<Map<String,Object>> list=new ArrayList<>();
        list.add(map1);
        list.add(map2);
        list.add(map3);
        list.add(map4);
        httpResponseEntity.setData(list);
        return httpResponseEntity;
    }
    @RequestMapping(value = "/addQuestionnaire",method = RequestMethod.POST, headers = "Accept=application/json")
    public HttpResponseEntity addQuestionnaire(@RequestBody Map<String,Object> map){

        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        httpResponseEntity.setCode(Constans.SUCCESS_CODE);
        List<Map<String,Object>> list=questionnaireService.queryQuestionListByProjectId((String) map.get("projectId"));
        for(Map<String,Object> maps:list){

            if((map.get("questionName")).equals(maps.get("question_name"))){
                httpResponseEntity.setCode(Constans.LOGOUT_NO_CODE);
                httpResponseEntity.setMessage(Constans.NAME_EXIT_MESSAGE);

                return httpResponseEntity;
            }
        }
        try {

            int  result =questionnaireService.addQuestionnaire(map);
            httpResponseEntity.setMessage(Constans.ADD_MESSAGE);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return httpResponseEntity;
    }
    @RequestMapping(value = "/deleteOverdueQuestionnaires",method = RequestMethod.POST,
            headers = "Accept=application/json")
    public HttpResponseEntity deleteOverdueQuestionnaires(@RequestBody Map<String,Object> map){
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        httpResponseEntity.setCode(Constans.SUCCESS_CODE);
        questionnaireService.deleteOverdueQuestionnaires(map);
        httpResponseEntity.setMessage("已删除过期的历史问卷");
        return httpResponseEntity;
    }
    @RequestMapping(value = "/queryHistoryQuestionnaire",
            method = RequestMethod.POST, headers = "Accept=application/json")
    public HttpResponseEntity queryHistoryQuestionnaire(@RequestBody Map<String,Object> map){
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        httpResponseEntity.setCode(Constans.SUCCESS_CODE);
        List<Map<String,Object>> list=questionnaireService.queryHistoryQuestionnaire(map);
        httpResponseEntity.setData(list);

        return httpResponseEntity;
    }
    @RequestMapping(value = "/queryQuestionnaireAll",method = RequestMethod.POST, headers = "Accept=application/json")
    public HttpResponseEntity queryQuestionnaireAll(@RequestBody Map<String,Object> map){
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        httpResponseEntity.setCode(Constans.SUCCESS_CODE);
        Map<String,String> maps=questionnaireService.queryQuestionnaireAll(map);
        httpResponseEntity.setData(maps);
        return httpResponseEntity;
    }
    @RequestMapping(value = "/queryQuestionnaireMould",method = RequestMethod.POST, headers = "Accept=application/json")
    public HttpResponseEntity queryQuestionnaireMould(@RequestBody Map<String,Object> map){
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        httpResponseEntity.setCode(Constans.SUCCESS_CODE);
        List<Map<String,Object>> list=questionnaireService.queryQuestionnaireMould(map);
        httpResponseEntity.setData(list);
        return httpResponseEntity;
    }
    @RequestMapping(value = "/modifyQuestionnaire",method = RequestMethod.POST, headers = "Accept=application/json")
    public HttpResponseEntity modifyQuestionnaire(@RequestBody Map<String,Object> map){
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        httpResponseEntity.setCode(Constans.SUCCESS_CODE);
        httpResponseEntity.setMessage(Constans.UPDATE_MESSAGE);
        questionnaireService.modifyQuestionnaire(map);
        return httpResponseEntity;
    }
    @RequestMapping(value = "/queryQuestionnaireList",method = RequestMethod.POST, headers = "Accept=application/json")
    public HttpResponseEntity queryQuestionnaireList(@RequestBody Map<String,Object> map){
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        httpResponseEntity.setCode(Constans.SUCCESS_CODE);
        httpResponseEntity.setMessage(Constans.STATUS_MESSAGE);
        List<Map<String,Object>> list=questionnaireService.queryQuestionnaireList(map);
        httpResponseEntity.setData(list);
        return httpResponseEntity;
    }
    @RequestMapping(value = "/modifyHistoryQuestionnaireStatus",
            method = RequestMethod.POST, headers = "Accept=application/json")
    public HttpResponseEntity modifyHistoryQuestionnaireStatus(@RequestBody Map<String,Object> map)
            throws ParseException {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        httpResponseEntity.setCode(Constans.SUCCESS_CODE);
        httpResponseEntity.setMessage(Constans.STATUS_MESSAGE);
        questionnaireService.modifyHistoryQuestionnaireStatus(map);
        return httpResponseEntity;
    }
    @RequestMapping(value = "/queryQuestionnaireById",method = RequestMethod.POST, headers = "Accept=application/json")
    public HttpResponseEntity queryQuestionnaireById(@RequestBody Map<String,Object> map) throws ParseException {

        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        httpResponseEntity.setCode(Constans.SUCCESS_CODE);
        httpResponseEntity.setMessage(Constans.STATUS_MESSAGE);
        Map<String,String> list=questionnaireService.queryQuestionnaireById(map);
        httpResponseEntity.setData(list);
        return httpResponseEntity;
    }
    @RequestMapping(value = "/modifyQuestionnaireInfo",method = RequestMethod.POST, headers = "Accept=application/json")
    public HttpResponseEntity modifyQuestionnaireInfo(@RequestBody Map<String,Object> map) throws ParseException {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        int tem=questionnaireService.modifyQuestionnaireInfo(map);
        if(tem==-1){
            httpResponseEntity.setMessage("问卷在开启中，无法修改");
            httpResponseEntity.setCode(Constans.LOGOUT_NO_CODE);
        }else if(tem==-2){
            httpResponseEntity.setMessage("问卷已发送，无法修改");
            httpResponseEntity.setCode(Constans.LOGOUT_NO_CODE);
        }else{
            httpResponseEntity.setMessage(Constans.STATUS_MESSAGE);
            httpResponseEntity.setCode(Constans.SUCCESS_CODE);
        }
        return httpResponseEntity;
    }
    @RequestMapping(value = "/updateStatus",method = RequestMethod.POST, headers = "Accept=application/json")
    public HttpResponseEntity updateStatus(@RequestBody Map<String,Object> map) throws ParseException {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        httpResponseEntity.setCode(Constans.SUCCESS_CODE);
        httpResponseEntity.setMessage(Constans.UPDATE_MESSAGE);
        questionnaireService.updateStatus();
        return httpResponseEntity;
    }
    @RequestMapping(value = "/selSum",method = RequestMethod.POST, headers = "Accept=application/json")
    public int selSum(@RequestBody Map<String,Object> map) throws ParseException {
//        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
//        httpResponseEntity.setCode(Constans.SUCCESS_CODE);
//        httpResponseEntity.setMessage(Constans.UPDATE_MESSAGE);
//        httpResponseEntity.setData("");
        return 100;
    }
@RequestMapping(value = "/addSendQuestionnaire",method = RequestMethod.POST, headers = "Accept=application/json")
public HttpResponseEntity addSendQuestionnaire(@RequestBody Map<String,Object> map) throws ParseException {
    HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
    httpResponseEntity.setCode(Constans.SUCCESS_CODE);
    httpResponseEntity.setMessage(Constans.UPDATE_MESSAGE);
    questionnaireService.addSendQuestionnaire(map);
    return httpResponseEntity;
}

    @RequestMapping(value = "/queryQuestContextEnd",method = RequestMethod.POST, headers = "Accept=application/json")
    public HttpResponseEntity queryQuestContextEnd(@RequestBody Map<String,Object> map) throws ParseException {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        httpResponseEntity.setCode(Constans.SUCCESS_CODE);
        httpResponseEntity.setMessage(Constans.UPDATE_MESSAGE);

        httpResponseEntity.setData(questionnaireService.queryQuestContextEnd(map));
        return httpResponseEntity;
    }
//    @RequestMapping(value = "/queryQuestContextEnd",method = RequestMethod.POST, headers = "Accept=application/json")
//    public HttpResponseEntity queryQuestContextEnd(@RequestBody Map<String,Object> map) throws ParseException {
//        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
//        httpResponseEntity.setCode(Constans.SUCCESS_CODE);
//        httpResponseEntity.setMessage(Constans.UPDATE_MESSAGE);
//        questionnaireService.addAnswerQuestionnaire(map);
//
//        return httpResponseEntity;
//    }
//@RequestMapping(value = "/selectCheckedCorrelation",method = RequestMethod.POST, headers = "Accept=application/json")
//public HttpResponseEntity selectCheckedCorrelation(@RequestBody Map<String,Object> map) throws ParseException {
//    HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
//    httpResponseEntity.setCode(Constans.SUCCESS_CODE);
//    httpResponseEntity.setMessage(Constans.UPDATE_MESSAGE);
//    questionnaireService.selectCheckedCorrelation(map);
//    httpResponseEntity.setData(questionnaireService.queryQuestContextEnd(map));
//    return httpResponseEntity;
//}
}
