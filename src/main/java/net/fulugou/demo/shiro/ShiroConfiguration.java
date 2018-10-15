package net.fulugou.demo.shiro;

import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.mgt.DefaultWebSubjectFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class ShiroConfiguration {

    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean filterFactoryBean = new ShiroFilterFactoryBean();

        // 添加自定义过滤器
        Map<String, Filter> filterMap = new HashMap<>();
        filterMap.put("cors", new CorsFilter());
        filterMap.put("jwt", jwtAccessControllerFilter());
        filterFactoryBean.setFilters(filterMap);
        filterFactoryBean.setSecurityManager(securityManager);

        // 自定义url规则
        Map<String, String> filterRuleMap = new HashMap<>();
        // 设置以下部分请求不需要走 jwt过滤器
        filterRuleMap.put("/", "anon");
        filterRuleMap.put("/error", "anon");
        filterRuleMap.put("/401", "anon");
        filterRuleMap.put("/403", "anon");
        filterRuleMap.put("/login", "anon");
        filterRuleMap.put("/register", "anon");
        filterRuleMap.put("/swagger-ui.html", "anon");
        filterRuleMap.put("/v2/api-docs", "anon");
        filterRuleMap.put("/swagger-resources", "anon");
        filterRuleMap.put("/configuration/security", "anon");
        filterRuleMap.put("/webjars/**", "anon");

        // 除上面设置的部分，其它所有请求走 jwt过滤器
        filterRuleMap.put("/**", "jwt");
        filterFactoryBean.setFilterChainDefinitionMap(filterRuleMap);
        return filterFactoryBean;
    }

    /**
     * shiro安全管理器:
     * 主要是身份认证的管理，缓存管理，cookie管理，
     * 所以在实际开发中我们主要是和SecurityManager进行打交道的
     *
     * @return
     */
    @Bean
    public DefaultWebSecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 注入 subject 工厂管理器
        securityManager.setSubjectFactory(subjectFactory());
        // 注入 session 管理器
        securityManager.setSessionManager(sessionManager());
        // 注入 realm
        securityManager.setRealm(jwtAuthorizingRealm());

        // 禁用使用Sessions 作为存储策略的实现，但它没有完全地禁用Sessions
        // 所以需要配合context.setSessionCreationEnabled(false);

//        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
//        DefaultSessionStorageEvaluator evaluator = new DefaultSessionStorageEvaluator();
//        evaluator.setSessionStorageEnabled(false);
//        subjectDAO.setSessionStorageEvaluator(evaluator);
//        securityManager.setSubjectDAO(subjectDAO);

        ((DefaultSessionStorageEvaluator) ((DefaultSubjectDAO) securityManager.getSubjectDAO()).getSessionStorageEvaluator())
                .setSessionStorageEnabled(false);

        return securityManager;
    }

    /**
     * subject工厂管理器
     * @return
     */
    public DefaultWebSubjectFactory subjectFactory() {
        return new JWTSubjectFactory();
    }

    /**
     * sessionManager通过sessionValidationSchedulerEnabled禁用掉会话调度器，
     * 因为我们禁用掉了会话，所以没必要再定期过期会话了。
     * @return
     */
    public DefaultSessionManager sessionManager() {
        DefaultSessionManager sessionManager = new DefaultSessionManager();
        sessionManager.setSessionValidationSchedulerEnabled(false);
        return sessionManager;
    }

    /**
     * 自定义过滤器
     * @return
     */
    public JWTAccessControllerFilter jwtAccessControllerFilter() {
        return new JWTAccessControllerFilter();
    }

    /**
     * 自己定义的realm.
     * @return
     */
    @Bean
    public JWTAuthorizingRealm jwtAuthorizingRealm(){
        JWTAuthorizingRealm realm = new JWTAuthorizingRealm();
        // 自定义密码匹配规则
//        realm.setCredentialsMatcher(new BCryptCredentialsMatcher());
        return realm;
    }
}
