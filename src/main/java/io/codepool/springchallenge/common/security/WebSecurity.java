package io.codepool.springchallenge.common.security;

import io.codepool.springchallenge.common.enums.AuthorityEnum;
import io.codepool.springchallenge.common.security.filter.JWTAuthenticationFilter;
import io.codepool.springchallenge.common.security.filter.JWTAuthorizationFilter;
import io.codepool.springchallenge.common.security.service.LoginResponseService;
import io.codepool.springchallenge.dao.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Web security. Here we define the different filters (JWT authentication, JWT authorization and others)
 * and the log in request response actions.
 */
@Component
public class WebSecurity extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private Environment environment;

    @Autowired
    private LoginResponseService loginResponseService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Constants constants;

    @PostConstruct
    void init(){
        constants = new Constants(environment);
    }


    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .headers().frameOptions().disable().and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .csrf().disable()
                .authorizeRequests().antMatchers(HttpMethod.POST, constants.loginUrl).permitAll().and()
                .authorizeRequests().antMatchers(
                "/v2/api-docs/**",
                "/swagger-resources/**",
                "/swagger-ui.html/**",
                "/webjars/**",
                "/favicon.ico",
                "/api/v1/user/register",
                "/api/v1/product/list",
                "/api/v1/product/get/**").permitAll()

                .and()
                //special endpoints for buyers
                .authorizeRequests().antMatchers(
 "/api/v1/interaction/deposit/***",
                "/api/v1/interaction/deposit/reset/***",
                "/api/v1/interaction/buy"
                ).hasAuthority(AuthorityEnum.BUYER.getValue())

                .and()
                //special endpoints for sellers
                .authorizeRequests().antMatchers(
 "/api/v1/product/create",
                "/api/v1/delete/***",
                "/api/v1/update/***"
                ).hasAuthority(AuthorityEnum.SELLER.getValue())

                .anyRequest().authenticated().and()
                .addFilter(new JWTAuthenticationFilter(authenticationManager(), constants, loginResponseService))
                .addFilter(new JWTAuthorizationFilter(authenticationManager(), constants, userRepository));
    }

    /**
     * Configure our auth manager to use the bCryptPasswordEncoder
     *
     * @param auth the auth
     * @throws Exception the exception
     */
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        //Define The class that retrieves the users and the algorithm to process the passwords is defined
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

}
