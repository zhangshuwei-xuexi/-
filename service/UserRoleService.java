package com.aim.questionnaire.service;

import com.aim.questionnaire.common.utils.DateUtil;
import com.aim.questionnaire.common.utils.UUIDUtil;
//import com.aim.questionnaire.config.shiro.ShiroService;
import com.aim.questionnaire.dao.ModelEntityMapper;
import com.aim.questionnaire.dao.RootPermissionEntityMapper;
import com.aim.questionnaire.dao.UserEntityMapper;
import com.aim.questionnaire.dao.UserRootEntityMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by wln on 2018\8\10 0010.
 */
@Service
public class UserRoleService {

    @Autowired
    private UserRootEntityMapper userRootEntityMapper;

    @Autowired
    private UserEntityMapper userEntityMapper;

    @Autowired
    private RootPermissionEntityMapper rootPermissionEntityMapper;

    //@Autowired
    //private ShiroService shiroService;

    @Autowired
    private ModelEntityMapper modelEntityMapper;

    /**
     * 角色查询
     * @return
     */
    public List<Map<String,Object>> queryRoleInfo() {
        List<Map<String,Object>> result = userRootEntityMapper.queryRoleInfo();
        return result;
    }

    /**
     * 添加角色
     * @param map
     * @return
     */
    public int addRoleInfo(Map<String, Object> map) {

        //根据角色的code查找角色code是否存在
        int count = userRootEntityMapper.queryRoleNameIsExit(map);
        if(count > 0 ) {
            //角色名已经存在
            return -3;
        }
        String id = UUIDUtil.getOneUUID();
        Date date = DateUtil.getCreateTime();
        List<String> num = userRootEntityMapper.queryRoleNumAll();
        String rate = num.get(num.size()-1);
        map.put("id","500"+id);
        //创建时间

        map.put("creationDate",date);
        map.put("lastUpdateDate",date);
        //查找所有的角色的等级
        int late =2;
        map.put("lastUpdatedBy",late);

        /*int late = Integer.parseInt(rate) + 1;*/

        int result = userRootEntityMapper.addRoleInfo(map);
        //查找默认的权限
        List<Map<String,Object>> list = rootPermissionEntityMapper.findPermissionByAdminIdDefault();
        String permissionCreatedBy = "authc,roles["+map.get("roleCode").toString()+"],kickout";
        //遍歷權限并為角色添加權限
        for(int j = 0; j < list.size(); j++) {
            String permissionId ="500"+ UUIDUtil.getOneUUID();
            map.put("id",permissionId);
            map.put("pathId",list.get(j).get("pathId"));
            map.put("createdBy",permissionCreatedBy);
            map.put("permission",list.get(j).get("permission"));

            map.put("roleId","500"+id);
            int permissionIdResult = rootPermissionEntityMapper.addRootPermission(map);
          
        }
 //       shiroService.updatePermission();
        return result;
    }

    /**
     * 角色状态
     * @param map
     * @return
     */
    public int modifyRoleStatus(Map<String, Object> map) {
        //创建时间
        Date dateppp = DateUtil.getCreateTime();
        map.put("lastUpdateDate",dateppp);
        int result = userRootEntityMapper.modifyRoleStatus(map);
        //        shiroService.updatePermission();
        int resultUser = userEntityMapper.modifyUserInfoStatus(map);

        return result;
    }

    /**
     * 查询全部的角色信息
     * @param map
     * @return
     */
    public List<Map<String,Object>> queryAllRoleInfo(Map<String, Object> map) {

        List<Map<String,Object>> resultListppp = new ArrayList<Map<String,Object>>();
        List<Map<String,Object>> resultppp = userRootEntityMapper.queryAllRoleInfo(map);
        for(Map<String,Object> resultMap : resultppp) {
            //时间转换
            Date creationDate = DateUtil.stringToSqlDate(resultMap.get("creationDate").toString(),
                    DateUtil.DEF_DATE_FORMAT_STR);
            String creationDateStr = DateUtil.dateToString(creationDate,DateUtil.DEF_DATE_FORMAT_STR);
            resultMap.put("creationDate",creationDateStr);
            resultListppp.add(resultMap);
        }
        return resultListppp;
    }

    /**
     * 查询角色的权限list
     * @param map
     * @return
     */
    public List<Map<String,Object>> queryRolePermissionList(Map<String, Object> map) {

        String resultppp = userRootEntityMapper.queryRolePermissionList(map);
        List<Map<String,Object>> listModelppp = new ArrayList<>();
        if(resultppp.length() == 1) {
            return listModelppp;
        }else {
            resultppp = resultppp.substring(2);
        }
        List<String> strResult = Arrays.asList(resultppp.split(","));
        //查询status对应的模块信息
        listModelppp = modelEntityMapper.queryModelListByStatus(strResult);
        return listModelppp;
    }

    /**
     * 删除角色
     * @param map
     * @return
     */
    public int deleteRoleInfo(Map<String, Object> map) {
        //根据角色查询用户
        int resultUserppp = userEntityMapper.queryUserCountByRole(map);
        if(resultUserppp == 0) {
            //删除角色
            int resultRoleppp = userRootEntityMapper.deleteRoleInfo(map);
            if(resultRoleppp == 1) {
                //删除角色对应的权限
                int resultPermission = userRootEntityMapper.deletePermission(map);
                //            shiroService.updatePermission();
            }

            return resultRoleppp;
        }
        return -1;
    }

    /**
     * 修改角色信息
     * @param map
     * @return
     */
    public int modifyRoleInfo(Map<String, Object> map) {
        //根据角色的code查找角色code是否存在
        int countppp = userRootEntityMapper.queryRoleNameIsExit(map);
        if(countppp > 0 ) {
            //角色名已经存在
            return -3;
        }
        int resultppp = userRootEntityMapper.modifyRoleInfo(map);
        if(resultppp > 0) {
            //修改权限对应的角色名
            int resultPerppp = rootPermissionEntityMapper.modifyPermissionByRoleId(map);
            if(resultPerppp > 0) {
 //               shiroService.updatePermission();
            }
        }
        return resultppp;
    }
}
