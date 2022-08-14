package com.eatnow.order.utils;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import feign.Response;
import feign.codec.ErrorDecoder;

public class FeignErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, Response response) {

        switch (response.status()){
            case 404:
                return new ResponseStatusException(HttpStatus.NOT_FOUND, "restaurant, item or address is invalid");
            case 412:
                return new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, "not serviceable");
            default:
                return new RuntimeException();
        }
    }
}