package io.codepool.springchallenge.common.security.filter;

import io.codepool.springchallenge.common.security.Constants;
import io.codepool.springchallenge.dao.model.UserEntity;
import io.codepool.springchallenge.dao.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;

/**
 * The type Jwt authorization filter.
 * Checks the JWT token for if it is expired,
 * ideally we would also do permissions checks here if and when the project grows and requires it.
 */
public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    private final Constants constants;

    private UserRepository userRepository;

    public JWTAuthorizationFilter(AuthenticationManager authManager, Constants constants, UserRepository userRepository) {
        super(authManager);
        this.constants = constants;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        String header = req.getHeader(constants.headerAuthorizationKey);
        if (header == null || !header.startsWith(constants.tokenBearerPrefix)) {
            chain.doFilter(req, res);
            return;
        }
        UsernamePasswordAuthenticationToken authentication = getAuthentication(req);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(req, res);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(constants.headerAuthorizationKey);
        if (token != null) {
            final JwtParser jwtParser = Jwts.parser().setSigningKey(constants.superSecretKey);
            final Jws<Claims> claimsJws = jwtParser.parseClaimsJws(token.replace(constants.tokenBearerPrefix, ""));
            final Claims claims = claimsJws.getBody();
            final Date expirationDate = claims.getExpiration();

            String user = claims.getSubject();
            UserEntity userEntity;

            userEntity = userRepository.findByUsername(user);

            if (userEntity.getActive() != null && !userEntity.getActive())
                throw new DisabledException("User disabled");

            final Collection<SimpleGrantedAuthority> authorities = userEntity.getAuthorities();

            if (user != null && expirationDate.after(new Date())) {
                return new UsernamePasswordAuthenticationToken(user, null, authorities);
            }
            return null;
        }
        return null;
    }

}

