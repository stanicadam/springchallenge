package io.codepool.springchallenge.controller;

import io.codepool.springchallenge.common.pojo.CreateProductRequest;
import io.codepool.springchallenge.common.pojo.ProductDTO;
import io.codepool.springchallenge.common.pojo.UserDTO;
import io.codepool.springchallenge.service.product.ProductService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    /**
     * Method to create new product
     *
     * @param productDTO the product dto
     * @return Product DTO
     */
    @ApiOperation(value = "Create new Product")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", paramType = "header",
                    required = true)
    })
    @PostMapping(value = "/create", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ProductDTO> create(@RequestBody CreateProductRequest productDTO) {
        return new ResponseEntity<>(
                productService.createNewProduct(productDTO),
                HttpStatus.CREATED);
    }

    /**
     * Method to get all products
     *
     * @return List of Product DTO
     */
    @ApiOperation(value = "Get Products")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", paramType = "header",
                    required = true)
    })
    @PostMapping(value = "/list", produces = "application/json")
    public ResponseEntity<List<ProductDTO>> getProducts() {
        return new ResponseEntity<>(
                productService.getProducts(),
                HttpStatus.OK);
    }
}
