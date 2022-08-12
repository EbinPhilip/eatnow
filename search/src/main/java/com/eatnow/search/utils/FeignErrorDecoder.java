package com.eatnow.search.utils;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import feign.Response;
import feign.codec.ErrorDecoder;

public class FeignErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, Response response) {

        switch (response.status()){
            case 404:
                return new ResponseStatusException(HttpStatus.NOT_FOUND, "address not found");
            default:
                return new RuntimeException();
        }
    }
}