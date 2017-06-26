/*
 *
 */
package com.ideas2it.chatbot.intersept;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.ideas2it.chatbot.service.GoogleConnection;

public class GoogleSheetsInterceptor implements HandlerInterceptor {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private GoogleConnection connection;

    @Override
    public void afterCompletion(final HttpServletRequest arg0, final HttpServletResponse response, final Object arg2,
            final Exception arg3) throws Exception {
    }

    @Override
    public void postHandle(final HttpServletRequest arg0, final HttpServletResponse response, final Object arg2,
            final ModelAndView arg3) throws Exception {
    }

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object arg2)
            throws Exception {
        logger.info("Pre-handle");
        logger.info(request.getRequestURI());
        // is connected
        if (connection.getCredentials() == null) {
            connection.setSourceUrl(request.getRequestURI());
            response.sendRedirect("/ask");
            return false;
        }
        return true;
    }

}
