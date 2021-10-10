package io.codepool.springchallenge.common.security;

import org.springframework.core.env.Environment;

/**
 * The type Constants.
 * To be used in our web security set up to pull all the
 * necessary info from properties files.
 */
public final class Constants {
    /**
     * The Login url.
     */
    public String loginUrl;
    /**
     * The Header authorization key.
     */
    public String headerAuthorizationKey;
    /**
     * The Token bearer prefix.
     */
    public String tokenBearerPrefix;
    /**
     * The Authorities key.
     */
    public String authoritiesKey;
    /**
     * The Issuer info.
     */
    public String issuerInfo;
    /**
     * The Super secret key.
     */
    public String superSecretKey;
    /**
     * The Token expiration time.
     */
    public long tokenExpirationTime;

    public Constants(Environment env) {
        this.loginUrl = env.getRequiredProperty("LOGIN_URL");
        this.headerAuthorizationKey = env.getRequiredProperty("HEADER_AUTHORIZATION_KEY");
        this.tokenBearerPrefix = env.getRequiredProperty("TOKEN_BEARER_PREFIX");
        this.authoritiesKey = env.getRequiredProperty("AUTHORITIES_KEY");
        this.issuerInfo = env.getRequiredProperty("ISSUER_INFO");
        this.superSecretKey = env.getRequiredProperty("SUPER_SECRET_KEY");
        this.tokenExpirationTime = Long.parseLong(env.getRequiredProperty("TOKEN_EXPIRATION_TIME"));
    }
}

