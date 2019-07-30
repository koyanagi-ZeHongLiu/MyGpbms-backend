package com.example.gpbms.shiro;

import com.example.gpbms.user.entity.Role;
import com.example.gpbms.user.entity.User;
import com.example.gpbms.user.repository.UserRepository;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

public class UserRealm extends AuthorizingRealm {
    @Autowired
    UserRepository userRepository;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        User user = (User) principalCollection.getPrimaryPrincipal();
        for (Role role : user.getRoles()) {
            // 把当前登录用户所拥有的角色权限写进shiro的权限控制中
            simpleAuthorizationInfo.addRole(role.getRoleName());
        }
        return simpleAuthorizationInfo;
    }

    @Override
    /**
    * @Description:  1、检查提交的进行认证的令牌信息    2、根据令牌信息从数据源(通常为数据库)中获取用户信息
     *               3、对用户信息进行匹配验证。        4、验证通过将返回一个封装了用户信息的AuthenticationInfo实例。
     *               5、验证失败则抛出AuthenticationException异常信息。
    * @Param: [authenticationToken]
    * @return: org.apache.shiro.authc.AuthenticationInfo
    * @Author: HWJ
    * @Date: 2019/7/29
    */
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        // 1.判断账号名是否正确
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        User user = userRepository.findByUsername(token.getUsername()).orElse(null);
        if (user == null) {
            //账号错误，Shiro底层会抛出UnknownAccountException异常
            return null;
        }
        // 2.判断密码
        // TODO 密码加盐进行加密
        return new SimpleAuthenticationInfo("", user.getPassword(), "");
    }
}
