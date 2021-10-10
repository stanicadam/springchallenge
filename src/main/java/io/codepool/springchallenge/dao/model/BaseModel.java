package io.codepool.springchallenge.dao.model;

import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

/**
 * Base model that all other models will inherit from.
 * Ideally we could add audit columns like create date, update date, create user and others.
 */
@MappedSuperclass
@EntityListeners({AuditingEntityListener.class})
public abstract class BaseModel {

    @Id
    @ApiModelProperty(hidden = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "ACTIVE")
    private Boolean active = true;

    public long getId() {
        return id;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}

