package com.example.gpbms.util;

import lombok.Data;

@Data
public class RespBean {

    private String code; //200代表操作成功，失败默认为500，还可定义其他的
    private String msg;  //错误信息，成功的话不管
    private Object data;

    public static RespBean success(){
        RespBean respBean = new RespBean();
        respBean.setCode("200");
        respBean.setMsg("操作成功");
        return respBean;
    }

    public static RespBean success(String successMsg){
        RespBean respBean = new RespBean();
        respBean.setCode("200");
        respBean.setMsg(successMsg);
        return respBean;
    }

    public static RespBean success(Object data){
        RespBean respBean = new RespBean();
        respBean.setCode("200");
        respBean.setMsg("操作成功");
        respBean.setData(data);
        return respBean;
    }

    public static RespBean success(String successMsg,Object data){
        RespBean respBean = new RespBean();
        respBean.setCode("200");
        respBean.setMsg(successMsg);
        respBean.setData(data);
        return respBean;
    }

    public static RespBean failure(){
        RespBean respBean = new RespBean();
        respBean.setCode("500");
        return respBean;
    }

    public static RespBean failure(String failureMsg){
        RespBean respBean = new RespBean();
        respBean.setCode("500");
        respBean.setMsg(failureMsg);
        return respBean;
    }

    public static RespBean failure(String failureCode,String failureMsg){
        RespBean respBean = new RespBean();
        respBean.setCode(failureCode);
        respBean.setMsg(failureMsg);
        return respBean;
    }
}
