/*
 *
 */
package com.ideas2it.chatbot.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.ideas2it.chatbot.Global;

@Service
public class GoogleConnectionService implements GoogleConnection {

    private final static Logger logger = LoggerFactory.getLogger(GoogleConnectionService.class);

    private static final List<String> SCOPES = Arrays.asList("https://spreadsheets.google.com/feeds");
    private static final File DATA_STORE_DIR = new File(System.getProperty("user.home"), ".store/chatbot");
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String CLIENT_SECRETS = "/client_secret.json";

    private static FileDataStoreFactory dataStoreFactory;
    private static HttpTransport httpTransport;
    private static GoogleClientSecrets clientSecrets;

    private String authorizationCode;
    private String sourceUrl;
    private Credential credential;

    private InputStream getSecretFile() throws IOException {
        return this.getClass().getResourceAsStream(CLIENT_SECRETS);
    }

    /** Authorizes the installed application to access user's protected data. */
    private static Credential authorize() throws Exception {
        // load client secrets
        clientSecrets = GoogleClientSecrets.load(JSON_FACTORY,
                new InputStreamReader(GoogleConnectionService.class.getResourceAsStream(CLIENT_SECRETS)));
        if (clientSecrets.getDetails().getClientId().startsWith("Enter")
                || clientSecrets.getDetails().getClientSecret().startsWith("Enter ")) {
            logger.info("Invalid Client ID and Secret data in client_secrets.json");
            return null;
        }
        httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        dataStoreFactory = new FileDataStoreFactory(DATA_STORE_DIR);
        final GoogleAuthorizationCodeFlow flow =
                new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(dataStoreFactory).build();
        final AuthorizationCodeInstalledApp app = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver());
        return app.authorize("user");
    }

    public GoogleConnectionService() {
        logger.info("google connection");
    }

    @Override
    public GoogleClientSecrets getClientSecrets() {
        if (clientSecrets == null)
            try {
                // load client secrets
                final InputStreamReader clientSecretsReader = new InputStreamReader(getSecretFile());
                clientSecrets = GoogleClientSecrets.load(Global.JSON_FACTORY, clientSecretsReader);
            } catch (final IOException e) {
                logger.error(e.getMessage(), e);
            }
        return clientSecrets;
    }

    @Override
    public Credential getCredentials() throws Exception {
        credential = authorize();
        return credential;
    }

    /**
     * @param code
     * @return
     * @throws IOException
     */
    @Override
    public boolean exchangeCode(final String code) {
        this.authorizationCode = code;

        // Step 2: Exchange --> exchange code for tokens
        boolean result = false;
        final String callbackUri = clientSecrets.getDetails().getRedirectUris().get(0);
        GoogleTokenResponse response;
        try {
            response = new GoogleAuthorizationCodeTokenRequest(Global.HTTP_TRANSPORT, Global.JSON_FACTORY,
                    clientSecrets.getDetails().getClientId(), clientSecrets.getDetails().getClientSecret(), code,
                    callbackUri).execute();

            // Build a new GoogleCredential instance and return it.
            credential = new GoogleCredential.Builder().setClientSecrets(clientSecrets)
                    .setJsonFactory(Global.JSON_FACTORY).setTransport(Global.HTTP_TRANSPORT).build()
                    .setAccessToken(response.getAccessToken()).setRefreshToken(response.getRefreshToken());
            result = true;
        } catch (final IOException e) {
            logger.error(e.getMessage(), e);
        }
        // End of Step 2 <--
        return result;
    }

    public String getAuthorizationCode() {
        return authorizationCode;
    }

    @Override
    public String getSourceUrl() {
        return sourceUrl;
    }

    @Override
    public void setSourceUrl(final String redirectUrl) {
        this.sourceUrl = redirectUrl;
    }

    @Override
    public String getRedirectUrl() {
        if (clientSecrets != null)
            return clientSecrets.getDetails().getRedirectUris().get(0);
        return null;
    }

}
