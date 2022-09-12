package com.aim.questionnaire.service;

import com.aim.questionnaire.common.utils.DateUtil;
import com.aim.questionnaire.common.utils.UUIDUtil;
import com.aim.questionnaire.dao.ProjectEntityMapper;
import com.aim.questionnaire.dao.QuestionnaireEntityMapper;
import com.aim.questionnaire.dao.entity.QuestionnaireEntity;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class QuestionnaireService {
    @Autowired
    private QuestionnaireEntityMapper questionnaireEntityMapper;
    @Autowired
    private ProjectEntityMapper projectEntityMapper;
    public List<Map<String,Object>> queryQuestionListByProjectId(String projectId){

        return questionnaireEntityMapper.queryQuestionListByProjectId(projectId);
    }
    public int addQuestionnaire(Map<String, Object> map) throws ParseException {

        String id = UUIDUtil.getOneUUID();
        map.put("id",id);
        String project_id= projectEntityMapper.getIdByName((String) map.get("projectName"));
        map.put("projectId",project_id);
        map.put("questionTitle",(String)map.get("questionName"));
        Date date = DateUtil.getCreateTime();
        map.put("releaseTime",date);
        map.put("lastUpdateDate",date);
        map.put("creationDate",date);

        if(map.get("dataId").equals("teacher")){
            map.put("dataId",1);
        }
        QuestionnaireEntity questionnaireEntity=new QuestionnaireEntity();
        questionnaireEntity.setId((String)map.get("id"));
        questionnaireEntity.setCreatedBy((String)map.get("createdBy"));
        questionnaireEntity.setCreationDate((Date)map.get("creationDate"));
        questionnaireEntity.setDataId((String)map.get("dataId"));
        questionnaireEntity.setLastUpdateDate((Date)map.get("lastUpdateDate"));
        questionnaireEntity.setLastUpdatedBy((String)map.get("lastUpdatedBy"));
        questionnaireEntity.setProjectId((String)map.get("projectId"));
        questionnaireEntity.setQuestionContent((String)map.get("questionContent"));
        questionnaireEntity.setQuestionName((String)map.get("questionName"));
        questionnaireEntity.setQuestionStop((String)map.get("questionStop"));
        questionnaireEntity.setQuestionTitle((String) map.get("questionTitle"));
        questionnaireEntity.setQuestion((String) map.get("questionList"));
        questionnaireEntity.setLastUpdatedBy((String) map.get("lastUpdatedBy"));
        System.out.println((String) map.get("lastUpdatedBy"));
        questionnaireEntity.setAnswerTotal((String) map.get("answerTotal"));
        String startTime= (String) map.get("startTime");
        String endTime=(String)map.get("endTime");
        if(!startTime.equals("")&&!endTime.equals("")&&startTime!=null&&endTime!=null){
            startTime = startTime.replace("Z", " UTC");
            startTime=startTime.replace("T"," ");
            endTime=endTime.replace("Z","UTC");
            endTime=endTime.replace("T"," ");
            SimpleDateFormat defaultFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date time1 = defaultFormat.parse(startTime);
            Date time11=new Date(time1.getTime()+8*60*60*1000);
            Date time2=defaultFormat.parse(endTime);
            Date time22=new Date(time2.getTime()+8*60*60*1000);
            System.out.println(time1.getTime());
            System.out.println(time11.getTime());
            questionnaireEntity.setStartTime(time11);
            questionnaireEntity.setEndTime(time22);
        }
        int result=questionnaireEntityMapper.insert(questionnaireEntity);
        return result;
    }

    public int deleteQuestionnaireById(String id) {//删除问卷，先判断是否开启
        List<Map<String,Object>> list=  questionnaireEntityMapper.selectByPrimaryKey1(id);
        for(Map<String,Object> map:list){
            LocalDateTime startTime= (LocalDateTime) map.get("start_time");
            LocalDateTime endTime= (LocalDateTime) map.get("end_time");

            LocalDateTime now=LocalDateTime.now();

            if(now.compareTo(startTime)>0&&now.compareTo(endTime)<0){
               return -1;//-1是有问卷在进行
            }
            if(map.get("release_time")!=null&&now.compareTo(startTime)<0){
                return -2;
            }
        }
        int i=questionnaireEntityMapper.deleteByPrimaryKey(id);
        return i;
    }

    public void deleteOverdueQuestionnaires(Map<String, Object> map) {
        String id= (String) map.get("projectId");
        List<Map<String,Object>> list=questionnaireEntityMapper.queryQuestionListByProjectId(id);
        for(Map<String,Object> maps:list){
            LocalDateTime endTime= (LocalDateTime) maps.get("end_time");
            LocalDateTime now=LocalDateTime.now();
            if(endTime==null){

            }
            if(now==null){

            }
            if(now.compareTo(endTime)>0){
                questionnaireEntityMapper.deleteByPrimaryKey((String) maps.get("id"));
            }
        }
    }

    public List<Map<String, Object>> queryHistoryQuestionnaire(Map<String, Object> map) {
        List<Map<String,Object>> list=questionnaireEntityMapper.queryHistoryQuestionnaire(map);
        return list;
    }

    public Map<String, String> queryQuestionnaireAll(Map<String, Object> map) {
        Map<String,String> maps=questionnaireEntityMapper.queryQuestionnaireById(map);
        return maps;
    }

    public List<Map<String, Object>> queryQuestionnaireMould(Map<String, Object> map) {

        return questionnaireEntityMapper.queryQuestionnaireMould((String) map.get("dataId"));
    }

    public void modifyQuestionnaire(Map<String, Object> map) {
        map.put("lastUpdateDate",LocalDateTime.now());
        ArrayList<Map<String,Object>> result= (ArrayList<Map<String, Object>>) map.get("questionList");
        JSONArray studentJsonArray = new JSONArray();
        studentJsonArray = JSON.parseArray(JSONObject.toJSONString(result));
        map.put("questionList",studentJsonArray.toJSONString(result));
        questionnaireEntityMapper.modifyQuestionnaire(map);
    }

    public List<Map<String,Object>> queryQuestionnaireList(Map<String, Object> map) {
        List<Map<String,Object>> list1=questionnaireEntityMapper.queryQuestionnaireList(map);
        for(Map<String,Object> maps:list1){
            String project_name;
            String id= (String) maps.get("projectId");
            project_name=projectEntityMapper.queryProjectNameById(   id);
            maps.put("projectName",project_name);
        }
        return list1;
    }

    public void modifyHistoryQuestionnaireStatus(Map<String, Object> map) throws ParseException {
        String endTime=(String)map.get("endTime");
        SimpleDateFormat defaultFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        endTime=endTime.replace("Z","UTC");
        endTime=endTime.replace("T"," ");
        Date time2=defaultFormat.parse(endTime);
        map.put("endTime",defaultFormat.format(time2));
        questionnaireEntityMapper.modifyHistoryQuestionnaireStatus(map);
    }

    public Map<String, String> queryQuestionnaireById(Map<String, Object> map) {
        return questionnaireEntityMapper.queryQuestionnaireById(map);
    }

    public int modifyQuestionnaireInfo(Map<String, Object> map) throws ParseException {
        SimpleDateFormat defaultFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<Map<String,Object>> list=  questionnaireEntityMapper.selectByPrimaryKey1((String) map.get("id"));
        if(map.get("dataId").equals("teacher")){
            map.put("dataId",1);
        }
        if(map.get("dataId").equals("student")){
            map.put("dataId",2);
        }
        if(map.get("dataId").equals("graduate")){
            map.put("dataId",3);
        }
        if(map.get("dataId").equals("company")){
            map.put("dataId",4);
        }
        for(Map<String,Object> maps:list){
            LocalDateTime endTime1= (LocalDateTime) maps.get("end_time");
            LocalDateTime startTime1= (LocalDateTime) maps.get("start_time");

//            endTime1=endTime1.replace("Z","UTC");
//            startTime1 = startTime1.replace("Z", " UTC");
//            startTime1=startTime1.replace("T"," ");
//            endTime1=endTime1.replace("T"," ");

            LocalDateTime now=LocalDateTime.now();
            if(now.compareTo(startTime1)>0&&now.compareTo(endTime1)<0){
                return -1;
            }
//            System.out.println(map.get("release_time"));
//            System.out.println(now.compareTo(startTime1));
            if(maps.get("release_time")!=null&&now.compareTo(startTime1)<0){
                return -2;
            }

        }
        Date date=new Date();
        date.getTime();
        map.put("lastUpdateDate",defaultFormat.format(date));
        String startTime= (String) map.get("startTime");
        String endTime=(String)map.get("endTime");
        endTime=endTime.replace("Z","UTC");
        endTime=endTime.replace("T"," ");
        startTime = startTime.replace("Z", " UTC");
        startTime=startTime.replace("T"," ");
        map.put("startTime",defaultFormat.parse(startTime));
        map.put("endTime", defaultFormat.parse(endTime));
        Date end=defaultFormat.parse(endTime);
        if(end.after(date)){
            map.put("questionStop","5");
        }else{
            map.put("questionStop","4");
        }
        questionnaireEntityMapper.modifyQuestionnaireInfo(map);
        return 0;
    }

    public void updateStatus() {
        List<Map<String,Object>> list=questionnaireEntityMapper.queryAllQuestionnaire();
        for(Map<String,Object> map:list){
            LocalDateTime now=LocalDateTime.now();
            LocalDateTime end= (LocalDateTime) map.get("end_time");
            if(end!=null&&now.compareTo(end)>0){
                Map<String,Object> maps=new HashMap<String,Object>();
                maps.put("id",map.get("id"));
                maps.put("questionStop","4");
                questionnaireEntityMapper.modifyQuestionnaireStatus(maps);
            }
        }
    }

    public void addSendQuestionnaire(Map<String, Object> map) {
        Date date = DateUtil.getCreateTime();
        if(map.get("dataId").equals("teacher")){
            map.put("dataId",1);
        }
        map.put("releaseTime",date);
        map.put("lastUpdateDate",date);
        questionnaireEntityMapper.addSendQuestionnaire(map);
    }

    public QuestionnaireEntity queryQuestContextEnd(Map<String, Object> map) {
        QuestionnaireEntity entity=questionnaireEntityMapper.queryQuestContextEnd((String) map.get("id"));

        return entity;
    }

    public void addAnswerQuestionnaire(Map<String, Object> map) {

    }

//    public void selectCheckedCorrelation(Map<String, Object> map) {
//        questionnaireEntityMapper.
//    }

//    public void selSum(Map<String, Object> map) {
//        questionnaireEntityMapper.
//    }
}
