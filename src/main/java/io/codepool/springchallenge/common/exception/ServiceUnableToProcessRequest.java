package io.codepool.springchallenge.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.SERVICE_UNAVAILABLE)
public class ServiceUnableToProcessRequest extends RuntimeException{
    public ServiceUnableToProcessRequest(String message){
        super(message);
    }
}