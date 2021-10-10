package io.codepool.springchallenge.common.pojo.product;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class ProductDTO {

    private Long id;
    private String name;
    private Integer amountAvailable;
    private BigDecimal cost;
    private String sellerId;
    private String sellerUsername;
    private Boolean active;
}
