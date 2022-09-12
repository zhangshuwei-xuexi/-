package com.aim.questionnaire.controller;

import com.aim.questionnaire.beans.HttpResponseEntity;
import com.aim.questionnaire.common.Constans;
import com.aim.questionnaire.service.UserRoleService;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Created by wln on 2018\8\10 0010.
 */
@RestController
@RequestMapping("/admin")
public class UserRoleController {

    private final Logger logger = LoggerFactory.getLogger(UserRoleController.class);

    @Autowired
    private UserRoleService userRoleService;

    /**
     * 角色查询
     * @return
     */
    @RequestMapping(value = "/queryRoleInfo",method = RequestMethod.POST, headers = "Accept=application/json")
    public HttpResponseEntity queryUserList() {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        try {
            Session session = SecurityUtils.getSubject().getSession();
            Object object = session.getAttribute("user");
            if (object != null) {
                List<Map<String,Object>> result = userRoleService.queryRoleInfo();
                httpResponseEntity.setData(result);
                httpResponseEntity.setCode(Constans.SUCCESS_CODE);
            }else {
                httpResponseEntity.setMessage(Constans.LOGOUT_NO_MESSAGE);
                httpResponseEntity.setCode(Constans.LOGOUT_NO_CODE);
            }
        } catch (Exception e) {
//            logger.info("queryRoleInfo 角色查询>>>>>>>>>>>" + e.getLocalizedMessage());
            httpResponseEntity.setCode(Constans.EXIST_CODE);
            httpResponseEntity.setMessage(Constans.EXIST_MESSAGE);
        }
        return httpResponseEntity;
    }

    /**
     * 添加角色
     * @return
     */
    @RequestMapping(value = "/addRoleInfo",method = RequestMethod.POST, headers = "Accept=application/json")
    public HttpResponseEntity addRoleInfo(@RequestBody Map<String,Object> map) {
        HttpResponseEntity httpResponseEntityppp = new HttpResponseEntity();
        try {
            Session session = SecurityUtils.getSubject().getSession();
            Object object = session.getAttribute("user");
            if (object != null) {
                int result = userRoleService.addRoleInfo(map);
                if(result == -3) {
                    httpResponseEntityppp.setMessage(Constans.ROLE_NAME_EXIT);
                }else {
                    httpResponseEntityppp.setMessage(Constans.ADD_MESSAGE);
                }
                httpResponseEntityppp.setCode(Constans.SUCCESS_CODE);
            }else {
                httpResponseEntityppp.setCode(Constans.LOGOUT_NO_CODE);
                httpResponseEntityppp.setMessage(Constans.LOGOUT_NO_MESSAGE);
            }
        } catch (Exception e) {
//            logger.info("addRoleInfo 添加角色>>>>>>>>>>>" + e.getLocalizedMessage());
            httpResponseEntityppp.setCode(Constans.EXIST_CODE);
            httpResponseEntityppp.setMessage(Constans.EXIST_MESSAGE);
        }
        return httpResponseEntityppp;
    }

    /**
     * 角色状态
     * @return
     */
    @RequestMapping(value = "/modifyRoleStatus",method = RequestMethod.POST, headers = "Accept=application/json")
    public HttpResponseEntity modifyRoleStatus(@RequestBody Map<String,Object> map) {
        HttpResponseEntity httpResponseEntityppp = new HttpResponseEntity();
        try {
            Session session = SecurityUtils.getSubject().getSession();
            Object object = session.getAttribute("user");
            if (object != null) {
                int result = -1;
                if(map.get("roleId").toString().equals("1")) {
                    httpResponseEntityppp.setMessage(Constans.ROLE_NO_UPDATE);
                }else {
                    result = userRoleService.modifyRoleStatus(map);
                    httpResponseEntityppp.setMessage(Constans.UPDATE_MESSAGE);
                }
                httpResponseEntityppp.setCode(Constans.SUCCESS_CODE);
                httpResponseEntityppp.setData(result);
            }else {
                httpResponseEntityppp.setCode(Constans.LOGOUT_NO_CODE);
                httpResponseEntityppp.setMessage(Constans.LOGOUT_NO_MESSAGE);
            }
        } catch (Exception e) {
            logger.info("modifyRoleStatus 角色状态>>>>>>>>>>>" + e.getLocalizedMessage());
            httpResponseEntityppp.setCode(Constans.EXIST_CODE);
            httpResponseEntityppp.setMessage(Constans.EXIST_MESSAGE);
        }
        return httpResponseEntityppp;
    }

    /**
     * 查询全部的角色信息
     * @return
     */
    @RequestMapping(value = "/queryAllRoleInfo",method = RequestMethod.POST, headers = "Accept=application/json")
    public HttpResponseEntity queryAllRoleInfo(@RequestBody Map<String,Object> map) {
        HttpResponseEntity httpResponseEntityppp = new HttpResponseEntity();
        try {
            Session session = SecurityUtils.getSubject().getSession();
            Object object = session.getAttribute("user");
            if (object != null) {
                List<Map<String,Object>> result = userRoleService.queryAllRoleInfo(map);
                httpResponseEntityppp.setCode(Constans.SUCCESS_CODE);
                httpResponseEntityppp.setData(result);
            }else {
                httpResponseEntityppp.setCode(Constans.LOGOUT_NO_CODE);
                httpResponseEntityppp.setMessage(Constans.LOGOUT_NO_MESSAGE);
            }
        } catch (Exception e) {
            logger.info("queryAllRoleInfo 查询全部的角色信息>>>>>>>>>>>" + e.getLocalizedMessage());
            httpResponseEntityppp.setCode(Constans.EXIST_CODE);
            httpResponseEntityppp.setMessage(Constans.EXIST_MESSAGE);
        }
        return httpResponseEntityppp;
    }

    /**
     * 查询角色的权限list
     * @param map
     * @return
     */
    @RequestMapping(value = "/queryRolePermissionList",method = RequestMethod.POST, headers = "Accept=application/json")
    public HttpResponseEntity queryRolePermissionList(@RequestBody(required = false) Map<String,Object> map) {
        HttpResponseEntity httpResponseEntityppp = new HttpResponseEntity();
        try {
            Session session = SecurityUtils.getSubject().getSession();
            Object object = session.getAttribute("user");

            if (object != null) {
                map.put("user", object.toString());
                List<Map<String,Object>> result = userRoleService.queryRolePermissionList(map);
                httpResponseEntityppp.setCode(Constans.SUCCESS_CODE);
                httpResponseEntityppp.setData(result);
            }else {
                httpResponseEntityppp.setCode(Constans.LOGOUT_NO_CODE);
                httpResponseEntityppp.setMessage(Constans.LOGOUT_NO_MESSAGE);
            }
        } catch (Exception e) {
            logger.info("queryRolePermissionList 查询角色的权限list>>>>>>>>>>>" + e.getLocalizedMessage());
            httpResponseEntityppp.setCode(Constans.EXIST_CODE);
            httpResponseEntityppp.setMessage(Constans.EXIST_MESSAGE);
        }
        return httpResponseEntityppp;
    }

    /**
     * 删除角色
     * @return
     */
    @RequestMapping(value = "/deleteRoleInfo",method = RequestMethod.POST, headers = "Accept=application/json")
    public HttpResponseEntity deleteRoleInfo(@RequestBody Map<String,Object> map) {
        HttpResponseEntity httpResponseEntityppp = new HttpResponseEntity();
        try {
            Session session = SecurityUtils.getSubject().getSession();
            Object object = session.getAttribute("user");
            if (object != null) {
                if("1".equals(map.get("id").toString())) {
                    httpResponseEntityppp.setMessage(Constans.USER_ROLE_DELETE_MESSAGE);
                }else {
                    int result = userRoleService.deleteRoleInfo(map);
                    if(result == -1) {
                        httpResponseEntityppp.setMessage(Constans.ROLE_HAVE_USER);
                    }else {
                        httpResponseEntityppp.setMessage(Constans.DELETE_MESSAGE);
                    }

                }
                httpResponseEntityppp.setCode(Constans.SUCCESS_CODE);

            }else {
                httpResponseEntityppp.setCode(Constans.LOGOUT_NO_CODE);
                httpResponseEntityppp.setMessage(Constans.LOGOUT_NO_MESSAGE);
            }
        } catch (Exception e) {
            logger.info("deleteRoleInfo 删除角色>>>>>>>>>>>" + e.getLocalizedMessage());
            httpResponseEntityppp.setCode(Constans.EXIST_CODE);
            httpResponseEntityppp.setMessage(Constans.EXIST_MESSAGE);
        }
        return httpResponseEntityppp;
    }

    /**
     * 修改角色
     * @return
     */
    @RequestMapping(value = "/modifyRoleInfo",method = RequestMethod.POST, headers = "Accept=application/json")
    public HttpResponseEntity modifyRoleInfo(@RequestBody Map<String,Object> map) {
        HttpResponseEntity httpResponseEntityppp = new HttpResponseEntity();
        try {
            Session session = SecurityUtils.getSubject().getSession();
            Object object = session.getAttribute("user");
            if (object != null) {
                if(map.get("id").toString().equals("1")) {
                    httpResponseEntityppp.setMessage(Constans.USER_ROLE_UPDATE_MESSAGE);
                }else {
                    int result = userRoleService.modifyRoleInfo(map);
                    if(result == -3) {
                        httpResponseEntityppp.setMessage(Constans.ROLE_NAME_EXIT);
                    }else {
                        httpResponseEntityppp.setMessage(Constans.UPDATE_MESSAGE);
                    }
                }
                httpResponseEntityppp.setCode(Constans.SUCCESS_CODE);
            }else {
                httpResponseEntityppp.setCode(Constans.LOGOUT_NO_CODE);
                httpResponseEntityppp.setMessage(Constans.LOGOUT_NO_MESSAGE);
            }
        } catch (Exception e) {
            logger.info("modifyRoleInfo 修改角色>>>>>>>>>>>" + e.getLocalizedMessage());
            httpResponseEntityppp.setCode(Constans.EXIST_CODE);
            httpResponseEntityppp.setMessage(Constans.EXIST_MESSAGE);
        }
        return httpResponseEntityppp;
    }
}
