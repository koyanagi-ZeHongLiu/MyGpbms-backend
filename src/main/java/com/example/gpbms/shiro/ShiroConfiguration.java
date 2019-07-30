package com.example.gpbms.shiro;

import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;

@Configuration
public class ShiroConfiguration {

    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(@Qualifier("securityManager") DefaultWebSecurityManager defaultWebSecurityManager) {
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
        // 设置安全管理器
        shiroFilter.setSecurityManager(defaultWebSecurityManager);
        // 前后端分离后，后台不需要再设置设置登录跳转页面，即登录URL
        // shiroFilter.setLoginUrl("/login");
        // TODO 未登录，后端默认将请求转发到login.jsp，正确的是应返回未登录信息，前端vue router控制页面跳转
        // 设置过滤器
        LinkedHashMap<String, String> filterMap = new LinkedHashMap<>();
        /*
        *Shiro内置过滤器：实现权限相关的拦截
        *      常用过滤器：
        *          anon（认证用）：无需认证（登录）即可访问
        *          authc（认证用）：必须认证才可访问
        *          user（少用）：使用rememberMe功能可以访问
        *          perms（授权用）：粒度更小，可控制到url
        *          role（授权用）：粒度更大，按角色控制，必须拥有对应角色权限才可访问
        *          logout ：org.apache.shiro.web.filter.authc.LogoutFilter
        * */
        // 放行登录请求
        filterMap.put("/api/doLogin", "anon");
        // 配置退出过滤器，退出代码Shiro已实现
        filterMap.put("/api/doLogOut", "logout");
        // 过滤链定义，从上至下顺序执行，一般将/*放在最后最下边（LinkedHashMap是有序的）
        filterMap.put("/*", "authc");
        //所有/api/xxx请求都需要登录后才能访问
        filterMap.put("/**", "authc");

        shiroFilter.setFilterChainDefinitionMap(filterMap);

        return shiroFilter;
    }

    @Bean(name = "securityManager")
    public DefaultWebSecurityManager getWebSecurityManager(@Qualifier("userRealm") UserRealm userRealm) {
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        defaultWebSecurityManager.setRealm(userRealm);
        return defaultWebSecurityManager;
    }
    @Bean(name = "userRealm")
    public UserRealm getUserRealm() {
        UserRealm userRealm = new UserRealm();
        return userRealm;
    }
}
