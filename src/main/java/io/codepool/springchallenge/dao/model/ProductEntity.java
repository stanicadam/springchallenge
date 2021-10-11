package io.codepool.springchallenge.dao.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "PRODUCT")
public class ProductEntity extends BaseModel {

    @Column(name = "AMOUNT_AVAILABLE")
    private Integer amountAvailable;

    @Column(name = "COST")
    private BigDecimal cost;

    @Column(name = "NAME")
    private String name;

    @ManyToOne
    @JoinColumn(name = "SELLER_ID", referencedColumnName = "id")
    private UserEntity seller;

}
