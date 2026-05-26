package com.liciot.usermanagementms.service.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.impl.crypto.DefaultJwtSignatureValidator;
import io.jsonwebtoken.impl.crypto.JwtSignatureValidator;
import java.io.Serializable;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class JwtTokenUtil implements Serializable {

    private static final long serialVersionUID = -2550185165626007488L;

    public static final long JWT_TOKEN_VALIDITY = 24*60*60;

    @Value("${jwt.secret}")
    private String secret;

    public String getSubjectFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getIssuedAtDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getIssuedAt);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }
    private String getUsername(Claims claims)
    {
        if( claims.containsKey("username"))
        {
            return claims.get("username").toString();
        }
        else
        {
            return null;
        }
    }
    private List<String> getAuthorities(Claims claims)
    {
        if( claims.containsKey("authorities"))
        {
            return (List<String>) claims.get("authorities");
        }
        else
        {
            return null;
        }
    }
    private String getSessionToken(Claims claims)
    {
        if( claims.containsKey("token"))
        {
            return (String )claims.get("token");
        }
        else
        {
            return null;
        }
    }
    private String getSessionId(Claims claims)
    {
        if( claims.containsKey("sessionId"))
        {
            return String.valueOf(claims.get("sessionId"));
        }
        else
        {
            return null;
        }
    }
    public String getUsernameFromToken(String token)
    {
        return getClaimFromToken(token,this::getUsername);
    }
    public List<String> getAuthoritiesFromToken(String token)
    {
        return getClaimFromToken(token,this::getAuthorities);
    }
    public String getSessionIdFromToken(String token)
    {
        return getClaimFromToken(token,this::getSessionId);
    }

    public String getSessionTokenFromToken(String token)
    {
        return getClaimFromToken(token,this::getSessionToken);
    }
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {

        Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        return claims;

    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
          expiration.before(new Date());
          return false;
    }

    private Boolean ignoreTokenExpiration(String token) {
        // here you specify tokens, for that the expiration is ignored
        return false;
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", userDetails.getUsername());
        claims.put("authorities", userDetails.getAuthorities().stream().map(grantedAuthority -> grantedAuthority.getAuthority().toString()).collect(Collectors.toList()));

        return doGenerateToken(claims, userDetails.getUsername());
    }
    public String generateToken(UUID sessionToken) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("token", sessionToken.toString());

        return doGenerateToken(claims, "*");
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY*1000))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    public Boolean canTokenBeRefreshed(String token) {
        return (!isTokenExpired(token) || ignoreTokenExpiration(token));
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
       // final String username = getSubjectFromToken(token);
      //  return(username.equals(userDetails.getUsername()) && !isTokenExpired(token));
        return true;
    }

}
