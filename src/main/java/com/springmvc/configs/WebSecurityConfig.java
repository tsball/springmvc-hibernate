package com.springmvc.configs;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Resource(name="customDetailService") // Can not use @Autowired, conflict with IdentityServiceUserDetailsService
    private UserDetailsService userDetailsService;
    
    @Autowired CustomFilterInvocationSecurityMetadataSource securityMetadtaSource;
    @Autowired AccessDecisionManager accessDecisionManager;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public AccessDecisionManager defaultAccessDecisionManager() {
        List<AccessDecisionVoter<?>> voters = new ArrayList<AccessDecisionVoter<?>>();
        // **** 重要：不同的AccessDecisionManager(decide) 与  Voter(vote) 组合了不同的匹配规则
        // voters.add(new WebExpressionVoter()); // default expression: [hasRole('ROLE_ADMIN')]
        voters.add(new RoleVoter()); // expression: ROLE_ADMIN
        AccessDecisionManager result = new AffirmativeBased(voters); // ROLE_ANONYMOUS will not allow to access. vs UnanimousBased/AffirmativeBased/ConsensusBased
        return result;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .antMatchers("/css/**", "/js/**", "/auth/registration").permitAll()
                    //.anyRequest().authenticated()
                    // replace the default FilterSecurityInterceptor (problem: page no need to login)
                    .anyRequest().authenticated().withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
					    public <O extends FilterSecurityInterceptor> O postProcess(O fsi) {
					        fsi.setSecurityMetadataSource(securityMetadtaSource);
					        fsi.setAccessDecisionManager(defaultAccessDecisionManager());
					        return fsi;
					    }
					}).anyRequest().denyAll()
                    .and()
                .formLogin()
                	.failureUrl("/login?error")
                	.defaultSuccessUrl("/")
                    .loginPage("/login")
		            .usernameParameter("username")
		            .passwordParameter("password")
                    .permitAll()
                    .and()
                .logout()
                	.logoutRequestMatcher(new AntPathRequestMatcher("/signout"))
		            .logoutSuccessUrl("/login")
		            .deleteCookies("JSESSIONID")
		            .invalidateHttpSession(true)
                    .permitAll();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    	// auth.inMemoryAuthentication().withUser("admin").password("admin").roles(RoleCode.ADMIN.toString()); // default account
    	auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
    }
}
