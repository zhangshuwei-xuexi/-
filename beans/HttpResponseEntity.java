package com.aim.questionnaire.beans;

import java.io.Serializable;

/**
 * Created by myz on 2017/7/8.
 */
//@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
//@Data
public class HttpResponseEntity implements Serializable {

    private String code; //状态码
    private Object data; //内容
    private String message; //状态消息
    private int shuliang;
    private String sortName;
    private String sortOrder;

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getSortName() {
        return sortName;
    }

    public void setSortName(String sortName) {
        this.sortName = sortName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getShuliang() {
        return shuliang;
    }

    public void setShuliang(int shuliang) {
        this.shuliang = shuliang;
    }
}
