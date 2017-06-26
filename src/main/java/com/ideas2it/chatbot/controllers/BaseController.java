/*
 *
 */
package com.ideas2it.chatbot.controllers;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.ideas2it.chatbot.exception.DefaultExceptionResponse;
import com.ideas2it.chatbot.exception.ExceptionResponse;

public class BaseController {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleException(final Exception exception,
            final HttpServletRequest request) {
        logger.error("Exception", exception);

        final ExceptionResponse exceptionResponse = new DefaultExceptionResponse();
        final Map<String, Object> responseBody =
                exceptionResponse.getExceptionsAttributes(exception, request, HttpStatus.INTERNAL_SERVER_ERROR);

        return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
