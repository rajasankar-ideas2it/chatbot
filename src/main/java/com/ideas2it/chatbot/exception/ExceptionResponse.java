/*
 * 
 */
package com.ideas2it.chatbot.exception;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;

public interface ExceptionResponse {

    Map<String, Object> getExceptionsAttributes(Exception exeception, HttpServletRequest httprequest,
            HttpStatus httpStatus);
}
