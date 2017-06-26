/*
 *
 */
package com.ideas2it.chatbot.service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;

public interface GoogleConnection extends IHasUrl {

    GoogleClientSecrets getClientSecrets();

    Credential getCredentials() throws Exception;

    boolean exchangeCode(String code);
}
