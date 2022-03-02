package com.xxx.crm.config;

import com.xxx.crm.interceptor.NoLoginInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * TODO
 *
 * @autor yanshupan
 * @date 2022/2/28 21:25
 */
@Configuration //配置类
public class MvcConfig extends WebMvcConfigurerAdapter {

    @Bean //将方法的返回值交给IOC维护
    public NoLoginInterceptor noLoginInterceptor(){
        return new NoLoginInterceptor();
    }
    
    /**
     * @Author yanshupan
     * @Description //TODO
     *    添加拦截器
     *
     *
     * @Date 21:41 2022/2/28
     * @Param [registry]
     * @return void
     **/

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //需要一个实现了拦截器功能的实例对象，这里使用的是noLoginInterceptor
        registry.addInterceptor(noLoginInterceptor())
                //设置需要被拦截的资源
                .addPathPatterns("/**") //默认拦截所有的资源
                // 设置不需要被拦截的资源
                .excludePathPatterns("/css/**","images/**","/js/**","/lib/**","/index","/user/login");

    }
}
