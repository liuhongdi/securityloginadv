package com.securityloginadv.demo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.RememberMeAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;

import javax.annotation.Resource;
import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final static BCryptPasswordEncoder ENCODER = new BCryptPasswordEncoder();
    private static final String SECRET = "lhd@2020";
    @Resource
    private UserLoginFailureHandler userLoginFailureHandler;//验证失败的处理类
    @Resource
    private UserLoginSuccessHandler userLoginSuccessHandler;//验证成功的处理类
    @Resource
    private UserLogoutSuccessHandler userLogoutSuccessHandler;
    @Resource
    private UserAccessDeniedHandler userAccessDeniedHandler;
    @Resource
    private SecUserDetailService secUserDetailService;

    //rememberme
    @Resource
    private DataSource dataSource;

    //RememberMeAuthenticationProvider.
    @Bean
    public RememberMeAuthenticationProvider rememberMeAuthenticationProvider() {
        return new RememberMeAuthenticationProvider(SECRET);
    }

    //TokenBasedRememberMeServices.
    @Bean("tokenBaseRememberMeServices")
    public TokenBasedRememberMeServices tokenBasedRememberMeServices() {
        System.out.println("==================----begin TokenBasedRememberMeServices");
        TokenBasedRememberMeServices rememberMeServices = new TokenBasedRememberMeServices(SECRET, secUserDetailService);
        rememberMeServices.setAlwaysRemember(false);
        rememberMeServices.setCookieName("remember-me");
        //rememberMeServices.setTokenValiditySeconds(AbstractRememberMeServices.TWO_WEEKS_S);
        rememberMeServices.setTokenValiditySeconds(300);
        return rememberMeServices;
    }

    //rememberme repository
    @Bean
    public PersistentTokenRepository persistentTokenRepository(){
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        // 设置数据源
        tokenRepository.setDataSource(dataSource);
        return tokenRepository;
    }


    //指定加密的方式，避免出现:There is no PasswordEncoder mapped for the id "null"
    @Bean
    public PasswordEncoder passwordEncoder(){//密码加密类
        return  new BCryptPasswordEncoder();
    }

    //配置规则
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        //permitall
        http.authorizeRequests()
                .antMatchers("/home/**","/css/**","/js/**","/img/**","/image/defaultkaptcha**")//静态资源等不需要验证
                .permitAll();

        //login
        http.formLogin()
                .loginPage("/login/login")
                //.failureUrl("/login/login?error")
                .loginProcessingUrl("/login/logined")//发送Ajax请求的路径
                .usernameParameter("username")//请求验证参数
                .passwordParameter("password")//请求验证参数
                .failureHandler(userLoginFailureHandler)//验证失败处理
                .successHandler(userLoginSuccessHandler)//验证成功处理
                .permitAll(); //登录页面用户任意访问

        //logout
        http.logout()
                .logoutUrl("/login/logout")
                .logoutSuccessUrl("/login/logout")
                .logoutSuccessHandler(userLogoutSuccessHandler)//登出处理
                .deleteCookies("JSESSIONID")
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .permitAll();

         //hasrole
         //有角色的用户才能访问
         http.authorizeRequests()
                 .antMatchers("/admin/**").hasRole("ADMIN")
                 //.antMatchers("/merchant/**").hasRole("MERCHANT");
                 .antMatchers("/merchant/**").hasAnyRole("MERCHANT","ADMIN");

        //其他任何请求,登录后可以访问
        http.authorizeRequests().anyRequest().authenticated();

        //accessdenied
        http.exceptionHandling().accessDeniedHandler(userAccessDeniedHandler);//无权限时的处理

        //user detail
        //http.userDetailsService(secUserDetailService);

        //rememberme
        http.rememberMe()
                .tokenRepository(persistentTokenRepository())
                .tokenValiditySeconds(300)   //Token过期时间为1minutes,一个小时
                .userDetailsService(secUserDetailService);
        //图形验证码
        http.addFilterBefore(new KaptchaFilter("/login/logined", "/login?error"), UsernamePasswordAuthenticationFilter.class);

        //http.csrf().disable();
  }

    @Resource
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(secUserDetailService).passwordEncoder(new PasswordEncoder() {
            @Override
            public String encode(CharSequence charSequence) {
                return ENCODER.encode(charSequence);
            }
            //密码匹配，看输入的密码经过加密与数据库中存放的是否一样
            @Override
            public boolean matches(CharSequence charSequence, String s) {
                return ENCODER.matches(charSequence,s);
            }
        });
    }

}