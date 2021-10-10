package io.codepool.springchallenge.common.security.filter;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.codepool.springchallenge.common.pojo.auth.LoginRequest;
import io.codepool.springchallenge.common.security.Constants;
import io.codepool.springchallenge.common.security.service.LoginResponseService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.stream.Collectors;


/**
 * The type Jwt authentication filter. Web security invokes this filter on log in attempts
 * and returns the resulting JWT token and whatever else we may need as a logged in user as defined in the LoginResponseService
 */
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final Constants constants;
    private final LoginResponseService loginResponseService;

    /**
     * Instantiates a new Jwt authentication filter.
     *
     * @param authenticationManager the authentication manager
     * @param constants             the constants
     * @param loginResponseService  the login response service
     */
    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, Constants constants, LoginResponseService loginResponseService) {
        this.authenticationManager = authenticationManager;
        this.constants = constants;
        this.setFilterProcessesUrl(constants.loginUrl);
        this.loginResponseService = loginResponseService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try {
            LoginRequest credentials = new ObjectMapper().readValue(request.getInputStream(), LoginRequest.class);
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    credentials.getUsernameEmail(), credentials.getPassword()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Successful authentication.
     * Invoke LoginResponseService and return those results.
     *
     * @param request  the request
     * @param response the response
     * @param chain    the chain
     * @param auth     the auth
     * @throws IOException the io exception
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication auth) throws IOException {

        // Generate the token with roles, issuer, date, expiration
        final String authorities = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        String token = Jwts.builder().setIssuedAt(new Date()).setIssuer(constants.issuerInfo)
                .setSubject(((org.springframework.security.core.userdetails.User) auth.getPrincipal()).getUsername())
                .claim(constants.authoritiesKey, authorities)
                .setExpiration(new Date(System.currentTimeMillis() + constants.tokenExpirationTime))
                .signWith(SignatureAlgorithm.HS512, constants.superSecretKey).compact();

        String JSONBody = loginResponseService.generateLoginResponse(constants.tokenBearerPrefix + " " + token,
                ((org.springframework.security.core.userdetails.User) auth.getPrincipal()).getUsername());

        out(response,JSONBody);
    }

    public static void out(HttpServletResponse res, String content) {
        res.setStatus(HttpServletResponse.SC_OK);
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");
        try{
            OutputStream out = res.getOutputStream();
            out.write(content.getBytes(StandardCharsets.UTF_8));
            out.flush();
            out.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
