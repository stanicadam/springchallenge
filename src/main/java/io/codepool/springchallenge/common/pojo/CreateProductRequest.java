package io.codepool.springchallenge.common.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class CreateProductRequest {

    private String name;
    private Integer amountAvailable;
    private BigDecimal cost;
}
