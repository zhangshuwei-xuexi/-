package com.aim.questionnaire.controller;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.Page;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.session.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.aim.questionnaire.beans.HttpResponseEntity;
import com.aim.questionnaire.common.Constans;
import com.aim.questionnaire.dao.UserEntityMapper;
import com.aim.questionnaire.dao.entity.UserEntity;
import com.aim.questionnaire.service.UserService;
import com.github.pagehelper.PageInfo;


/**
 * Created by wln on 2018\8\9 0009.
 */
@RestController
@RequestMapping("/admin")
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(UserController.class);
    
    @Autowired
    private UserService userService;

    @Autowired
    private UserEntityMapper userEntityMapper;
   
    /**
     * 用户登录
     * @param
     * @return
     */
    @RequestMapping(value="/userLogin",method= RequestMethod.POST, headers = "Accept=application/json")
    public HttpResponseEntity userLogin(@RequestBody UserEntity userEntity) {
        HttpResponseEntity httpResponseEntity1 = new HttpResponseEntity();
        try {
            List<UserEntity> hasUser = userEntityMapper.selectUserInfo(userEntity);

            if(CollectionUtils.isEmpty(hasUser) ) {

                httpResponseEntity1.setMessage(Constans.LOGIN_USERNAME_PASSWORD_MESSAGE);
            	httpResponseEntity1.setData(null);
                httpResponseEntity1.setCode(Constans.EXIST_CODE);
            }else {

            	httpResponseEntity1.setData(hasUser);
            	httpResponseEntity1.setMessage(Constans.LOGIN_MESSAGE);
                httpResponseEntity1.setCode(Constans.SUCCESS_CODE);
            }

        } catch (Exception e) {

            httpResponseEntity1.setCode(Constans.EXIST_CODE);
            httpResponseEntity1.setMessage(Constans.EXIST_MESSAGE);


        }
        return httpResponseEntity1;
    }

    /**
     * 查询用户列表（模糊搜索）
     * @param map
     * @return
     */
    @RequestMapping(value = "/queryUserList",method = RequestMethod.POST, headers = "Accept=application/json")
    public HttpResponseEntity queryUserList(@RequestBody Map<String,Object> map) {

        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        Page page =  PageHelper.startPage((int)map.get("pageNum"),10);
        httpResponseEntity.setCode(Constans.SUCCESS_CODE);
        List<Map<String,Object>>  result =userEntityMapper.queryUserList(map);
        int size=(int)page.getTotal();
        httpResponseEntity.setData(result);
        httpResponseEntity.setShuliang(size);
        return httpResponseEntity;
    }
    /**
     * 查询用户列表（模糊搜索）
     * @param map
     * @return
     */
    @RequestMapping(value = "/queryUserListDESC",method = RequestMethod.POST, headers = "Accept=application/json")
    public HttpResponseEntity queryUserList1(@RequestBody Map<String,Object> map) {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        httpResponseEntity.setCode(Constans.SUCCESS_CODE);
        Page page =  PageHelper.startPage((int)map.get("pageNum"),10);

        List<Map<String,Object>>  result =userEntityMapper.queryUserListDESC(map);
        int size=(int)page.getTotal();
        httpResponseEntity.setData(result);
        httpResponseEntity.setShuliang(size);
        return httpResponseEntity;
    }
    /**
     * 创建用户的基本信息
     * @param map
     * @return
     */
    @RequestMapping(value = "/addUserInfo",method = RequestMethod.POST, headers = "Accept=application/json")
    public HttpResponseEntity addUserInfo(@RequestBody Map<String,Object> map) {
        HttpResponseEntity httpResponseEntity1 = new HttpResponseEntity();
        try {
            int result = userService.addUserInfo(map);
            if(result == 3) {
                httpResponseEntity1.setCode(Constans.USER_USERNAME_CODE);
                httpResponseEntity1.setMessage(Constans.USER_USERNAME_MESSAGE);
                logger.info("放查重");
            }else {
                httpResponseEntity1.setCode(Constans.SUCCESS_CODE);
                httpResponseEntity1.setMessage(Constans.ADD_MESSAGE);
                logger.info("放查重");
            }
        } catch (Exception e) {
            logger.info("addUserInfo 创建用户的基本信息>>>>>>>>>>>" + e.getLocalizedMessage());

            httpResponseEntity1.setMessage(Constans.EXIST_MESSAGE);
            httpResponseEntity1.setCode(Constans.EXIST_CODE);
        }
        return httpResponseEntity1;
    }

    /**
     * 编辑用户的基本信息
     * @param map
     * @return
     */
    @RequestMapping(value = "/modifyUserInfo",method = RequestMethod.POST, headers = "Accept=application/json")
    public HttpResponseEntity modifyUserInfo(@RequestBody Map<String,Object> map) {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        
        return httpResponseEntity;
    }


    /**
     *  根据用户id查询用户基本信息
     * @param userEntity
     * @return
     */
    @RequestMapping(value = "/selectUserInfoById",method = RequestMethod.POST, headers = "Accept=application/json")
    public HttpResponseEntity selectUserInfoById(@RequestBody UserEntity userEntity) {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        
        return httpResponseEntity;
    }



    /**
     * 修改用户状态
     * @param map
     * @return
     */
    @RequestMapping(value = "/modifyUserStatus",method = RequestMethod.POST, headers = "Accept=application/json")
    public HttpResponseEntity modifyUserStatus(@RequestBody Map<String,Object> map) {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        
        return httpResponseEntity;
    }
    /**
     *  删除用户信息
     * @param userEntity
     * @return
     */
    @RequestMapping(value = "/deleteUserInfoById",method = RequestMethod.POST, headers = "Accept=application/json")
    public HttpResponseEntity deteleUserInfoById(@RequestBody UserEntity userEntity) {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        
        return httpResponseEntity;
    }


    /**
     * 用户没有权限
     * @return
     */
    @RequestMapping(value = "/error")
    public HttpResponseEntity logout() {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        
        return httpResponseEntity;
    }
}
