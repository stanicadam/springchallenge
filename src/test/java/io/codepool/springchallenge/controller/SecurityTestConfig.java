package io.codepool.springchallenge.controller;

import io.codepool.springchallenge.common.enums.AuthorityEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.*;

/**
 * The Test Security config.
 * This security config will be used together with the spring boot test kit
 * that allows us to simulate being logged in.
 */
@TestConfiguration
public class SecurityTestConfig {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean("testUserDetailsService")
    @Primary
    public UserDetailsService userDetailsService() {
        Set<SimpleGrantedAuthority> sellerAuthorities = new HashSet<>();
        sellerAuthorities.add(new SimpleGrantedAuthority(
                AuthorityEnum.SELLER.getValue()
        ));

        Set<SimpleGrantedAuthority> buyerAuthorities = new HashSet<>();
        buyerAuthorities.add(new SimpleGrantedAuthority(
                AuthorityEnum.BUYER.getValue()
        ));
        User adminUser = new User("seller",passwordEncoder.encode("strongpass"),true,true,true,true,sellerAuthorities);
        User otherUser = new User("buyer",passwordEncoder.encode("weakpass"),true,true,true,true,buyerAuthorities);
        return new InMemoryUserDetailsManager(Arrays.asList(adminUser, otherUser));
    }
}