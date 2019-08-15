package com.example.gpbms.shiro;

import com.example.gpbms.user.entity.User;
import com.example.gpbms.util.Md5Utils;
import com.example.gpbms.util.RespBean;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api")
public class LoginController {
    //登录处理
    @PostMapping(value = "/doLogin")
    public RespBean doLogin(@RequestBody User user) {
        //1.获取Subject
        Subject subject = SecurityUtils.getSubject();
        //2.封装用户数据
        // MD5加密密码
        String secret = Md5Utils.md5(user.getPassword());
        System.out.println(secret);
//        user.setPassword(secret);
        UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(), user.getPassword());
        //3.执行登录
        try {
            //登录成功
            subject.login(token);
            System.out.println("登录成功");
            System.out.println(user.toString());
            return  RespBean.success("登录成功", user.getUsername());
        } catch (UnknownAccountException exception) {
            //返回错误信息
            return  RespBean.failure("账号错误");
        } catch (IncorrectCredentialsException exception) {
            //返回错误信息
            return RespBean.failure("密码错误");
        }
    }

    //注销处理
    @RequestMapping(value = "/doLogout")
    public RespBean doLogout() {
        //1.获取Subject
        Subject subject = SecurityUtils.getSubject();
        //2.执行注销
        try {
            subject.logout();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return RespBean.success("注销成功");
    }
}
