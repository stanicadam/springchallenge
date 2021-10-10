package io.codepool.springchallenge.dao.model;

import io.swagger.annotations.ApiModelProperty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * The User Model.
 */
@Entity
@Table(name = "USER")
public class UserEntity extends BaseModel implements UserDetails {

    @Column(name = "USERNAME")
    private String username;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "DEPOSIT")
    private BigDecimal deposit;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    @ApiModelProperty(hidden = true)
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //ideally this is where we would populate our roles and permissions but we dont have them here
        //had to implement this as it is due to overriding default spring user model
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        return authorities;
    }

    //overriding default methods
    //in our case we dont care about is expired or is locked
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    /**
     * Overriding the default equals method.
     * The unique identifiers for our user entities are the username/password.
     *
     * @param object the object
     * @return the boolean
     */
    @Override
    public boolean equals(Object object){
        if(this == object) return true;

        if(!(object instanceof UserEntity)) return false;

        UserEntity userEntity = (UserEntity)object;

        return  Objects.equals(this.getUsername(), userEntity.getUsername());
    }

    public BigDecimal getDeposit() {
        return deposit;
    }

    public void setDeposit(BigDecimal deposit) {
        this.deposit = deposit;
    }
}
