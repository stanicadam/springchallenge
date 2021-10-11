package io.codepool.springchallenge.controller;

import io.codepool.springchallenge.common.pojo.interaction.BuyProductsRequest;
import io.codepool.springchallenge.common.pojo.interaction.BuyProductsResponse;
import io.codepool.springchallenge.service.interaction.InteractionService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/interaction")
public class InteractionController {

    @Autowired
    private InteractionService interactionService;

    /**
     * Deposit money into the logged in User's account
     *
     * @param amount the amount
     */
    @ApiOperation(value = "Deposit money")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", paramType = "header",
                    required = true)
    })
    @PostMapping(value = "/deposit/{amount}", produces = "application/json")
    public ResponseEntity<?> deposit(@PathVariable("amount") Integer amount) {
        interactionService.depositAmount(amount);
        return ResponseEntity.ok().build();
    }

    /**
     * Method to reset deposit
     *
     */
    @ApiOperation(value = "Buy products")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", paramType = "header",
                    required = true)
    })
    @PostMapping(value = "/buy")
    public ResponseEntity<BuyProductsResponse> buy(@RequestBody BuyProductsRequest buyProductsRequest) {
        return new ResponseEntity<>(
                interactionService.buyProducts(buyProductsRequest),
                HttpStatus.OK);
    }

    /**
     * Method to reset deposit
     *
     */
    @ApiOperation(value = "Reset user deposit")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", paramType = "header",
                    required = true)
    })
    @PostMapping(value = "/deposit/reset")
    public ResponseEntity<Void> resetDeposit() {
        interactionService.resetDeposit();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
