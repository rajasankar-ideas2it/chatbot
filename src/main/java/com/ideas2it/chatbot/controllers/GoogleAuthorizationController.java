/*
 *
 */
package com.ideas2it.chatbot.controllers;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.ideas2it.chatbot.Global;
import com.ideas2it.chatbot.service.GoogleConnectionService;

@RestController
public class GoogleAuthorizationController extends BaseController {
    
    @Autowired
    private GoogleConnectionService connection;
    
    @RequestMapping(value = "/ask", method = RequestMethod.GET)
    public void ask(final HttpServletResponse response) throws IOException {
        // Step 1: Authorize --> ask for auth code
        final String url = new GoogleAuthorizationCodeRequestUrl(connection.getClientSecrets(),
                connection.getRedirectUrl(), Global.SCOPES)
                .setApprovalPrompt("force").build();
        logger.info("Go to the following link in your browser: ");
        logger.info(url);
        response.sendRedirect(url);
    }
}
