package io.codepool.springchallenge.common.pojo.product;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class CreateUpdateProductRequest {

    private String name;
    private Integer amountAvailable;
    private BigDecimal cost;
    private Boolean active;
}
