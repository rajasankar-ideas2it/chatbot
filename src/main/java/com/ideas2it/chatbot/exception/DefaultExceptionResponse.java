/*
 *
 */
package com.ideas2it.chatbot.exception;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;

public class DefaultExceptionResponse implements ExceptionResponse {

    public static final String TIMESTAMP = "timestamp";
    public static final String STATUS = "status";
    public static final String ERROR = "error";
    public static final String EXCEPTION = "exception";
    public static final String MESSAGE = "message";
    public static final String PATH = "path";

    @Override
    public Map<String, Object> getExceptionsAttributes(final Exception exception, final HttpServletRequest httprequest,
            final HttpStatus httpStatus) {
        final Map<String, Object> exceptionResponse = new LinkedHashMap<>();
        exceptionResponse.put(TIMESTAMP, new Date());
        addHttpStatus(exceptionResponse, httpStatus);
        addExceptionDetail(exceptionResponse, exception);
        addPath(exceptionResponse, httprequest);
        return exceptionResponse;
    }

    private void addHttpStatus(final Map<String, Object> exceptionAttributes, final HttpStatus httpStatus) {
        exceptionAttributes.put(STATUS, httpStatus.value());
        exceptionAttributes.put(ERROR, httpStatus.getReasonPhrase());
    }

    private void addExceptionDetail(final Map<String, Object> exceptionAttributes, final Exception exeception) {
        exceptionAttributes.put(EXCEPTION, exeception.getClass().getName());
        exceptionAttributes.put(ERROR, exeception.getMessage());
    }

    private void addPath(final Map<String, Object> exceptionAttributes, final HttpServletRequest httpRequest) {
        exceptionAttributes.put(PATH, httpRequest.getServletPath());
    }
}
