package io.codepool.springchallenge.controller;

import io.codepool.springchallenge.common.pojo.product.CreateUpdateProductRequest;
import io.codepool.springchallenge.common.pojo.product.ProductDTO;
import io.codepool.springchallenge.service.product.ProductService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<ProductDTO> create(@RequestBody CreateUpdateProductRequest productDTO) {
        return new ResponseEntity<>(
                productService.createProduct(productDTO),
                HttpStatus.CREATED);
    }

    /**
     * Method to delete Product
     *
     * @param productId the product id
     * @return ProductDTO
     */
    @ApiOperation(value = "Delete Product")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", paramType = "header",
                    required = true)
    })
    @DeleteMapping(value = "/delete/{productId}", produces = "application/json")
    public ResponseEntity<ProductDTO> deleteProduct(@PathVariable("productId") Long productId) {
        return new ResponseEntity<>(
                productService.deleteProduct(productId),
                HttpStatus.OK);
    }

    /**
     * Method to update Product
     *
     * @param productId the product id
     * @return ProductDTO
     */
    @ApiOperation(value = "Update Product")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", paramType = "header",
                    required = true)
    })
    @PutMapping(value = "/update/{productId}", produces = "application/json")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable("productId") Long productId,
                                                    @RequestBody CreateUpdateProductRequest updateProductRequest) {
        return new ResponseEntity<>(
                productService.updateProduct(productId,updateProductRequest),
                HttpStatus.OK);
    }

    /**
     * Method to get Product by Id
     *
     * @param productId the product id
     * @return ProductDTO
     */
    @ApiOperation(value = "Get Product By Id")
    @GetMapping(value = "/get/{productId}", produces = "application/json")
    public ResponseEntity<ProductDTO> getById(@PathVariable("productId") Long productId) {
        return new ResponseEntity<>(
                productService.getById(productId),
                HttpStatus.OK);
    }

    /**
     * Method to get all products
     *
     * @return List of Product DTO
     */
    @ApiOperation(value = "Get Products")
    @PostMapping(value = "/list", produces = "application/json")
    public ResponseEntity<List<ProductDTO>> getProducts() {
        return new ResponseEntity<>(
                productService.getProducts(),
                HttpStatus.OK);
    }
}
