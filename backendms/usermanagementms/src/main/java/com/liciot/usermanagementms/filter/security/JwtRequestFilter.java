package com.liciot.usermanagementms.filter.security;

import com.liciot.usermanagementms.entity.User;
import com.liciot.usermanagementms.service.security.JWTUserDetailsService;
import com.liciot.usermanagementms.service.security.JwtTokenUtil;
import com.liciot.usermanagementms.service.security.SessionService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.stream.Collectors;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JWTUserDetailsService jwtUserDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    SessionService sessionService;
/*
So the JWT auth schema will be like this:
    The user will authenticate :
        - Will make a specific request to a specific endpoint (/security/authenticate):
            - specify the credentials via:
                - HTTP JSON Body (UserDTO)
                    or
                - HTTP Header: [ Authorization: "Basic base64encoded(username:password)" ]
        - If valid:
            - a new userSession will be generated containing:
                - sessionId
                - the user for which the session has been generated
                - creation and expiration DateTimes
                - if the JWT token should be updated (details in section X)

            - a new JWT will be generated containing:
                - username
                - authorities
                - expiration
                - sessionId
            - the token will be send back to the client in the HTTP JSON Body (this is an access token)

    The user will make any other request :
        - The request will be processed by a series of filters and at some point it will be redirected to the SECURITY FILTER CHAIN
        - at some point it will be processed by our JWTRequestFilter filter , which:\
            - is searching for the Authorization (HTTP) header
            - If finded, will search in it's value for Bearer keyword and then will take the base64encoded JWT token\
            - If token is expired we disconsider it and will try to delete the coresponding session if exists
            - If not, will fetch the coresponding session from de database
            -


     We actually use the information from both the JWT that we get from the client request and from the session that we store in the database.
     So without each other, separately, the JWT and the session are incomplete and can not be used for the user's authorization.
     We combine them in order to achieve the maximum (reliable) level of security while still maintaining the best performance (time efficiency) we can.
     The JWT expiration will be used for the expiration of both JWT and session. Why? Because it is easy to manipulate the expiration time with every request without being necessary to write in the database).
        We want to update the expiration time (if some conditions are true (ex: exp time is lower that 20% of the max exp time (the initial one)


Deci atenti vom folisi precum zice si in doc ument (net) astfel ) :
JWt pentru authentificare ( adica sa prezentam identitatea userului
Session pentru restul inclusiv p3entru autotrizqare adica determinarea accesulu pentru a primi ceea ce a cerut.
Nu tinem rolurile in token ca zice si pe alta paginac ase poate complica tare JWTR si sa devina matre (mai ales cand va deveni accesul la alte servicii externe ec2 AMAZON SI ce mai era acolo)
Si oricum am fi interoigat baza de date la fiecare request .
Dar cum facem pe frontend sa separam utilizatorii
Pur si sumplu desena aceeasi pagina pentru toti si cand se vace requestul. pentru a cere niste date sau a executa ceva opur si simplu nu se intmple si al cerere nu se va afixa pur si simplu acele date iar la executie pur si simplku se va da un mesaj alert ca nu poiate efewctua acele actiuni
totusi eu zic sa predefinesti niste roluluri si in functie de alea in front sa ti putin cont choia rdaca in backend permisiiuynile vor fi mult mai vastej.

daaa facem asa:
Deci pastram ideea cu objec based access (penmtru fiecare user se retin obiectele la care are acces fabrica , si ce altceva....)
Apoii definim roluri
la fiecare rol specificam permisiunile
Si la fiecare persoana specificam rolurile
si apoi in frontewnd putem seta la ce pagina sa se deschida penmtru fiecare rol

Separarea pe fabrici poate fi facut in acelasi sytem prin roluri
Sau poate fi facuta pentru cu totul alt sistem, adica separam sistemele si gata


Sau putem concretizxa de la inceput fromos pe fabri accsul si sa spunem pt fiecare fabrica la fiecare uas3er cr are dreputl sa favc sa sterga sa adauge sa modicice
sa vizualizeze

Nu poti sa megi preq granulqr cu AZsta ca nu ajungi nicaieri\
Dec9i nu pe toate obiectele facem , predefinit! obiectele pre care se pot face filtrari de acces
de exemplu fabrici, linii de incarcare, transporturi, si pentru fiecare setezi view create update delete



@@@@@@@!!!!!!!!!!!!!!!@Atentie prin acest filtru tree orice request prin conf pe care ai facuto poate potis sa schimbi sa nu trreaca toate, dar acum trec si de exemplu si cinevq care nu ar trebui authorizat treec ( adas un request) si apar ceva exceptii aici ala cnu  va mai primi nici un raspun si nu este ok asa ca pune totul in try catch si in finally pui chain.doFiltyer()!@!!!!!!!!!!!!!!!!!
 */

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        try {
            final String requestTokenHeader = request.getHeader("Authorization");
            Enumeration<String> headerNames = request.getHeaderNames();
            System.out.println("Headers:");
       /* while(headerNames.hasMoreElements())
        {
            String headerName  = headerNames.nextElement();
            System.out.println(headerName +" : "+request.getHeader(headerName));
        }

        */
            System.out.println(request.getRequestURI());
            String sessionToken = null;
            String jwtToken = null;
            // JWT Token is in the form "Bearer token". Remove Bearer word and get only the Token
            System.out.println(requestTokenHeader);


            if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ") && requestTokenHeader.length() > 10) {
                jwtToken = requestTokenHeader.substring(String.valueOf("Bearer ").length() - 1).trim();
                // System.out.println("Username: " + jwtTokenUtil.getUsernameFromToken(jwtToken));
                // for(String grantedAuthority : jwtTokenUtil.getAuthoritiesFromToken(jwtToken))

                /// {
                //    System.out.println("* "+grantedAuthority+" *");
                // }

                try {
                    sessionToken = jwtTokenUtil.getSessionTokenFromToken(jwtToken);
                    System.out.println(sessionToken);

                } catch (IllegalArgumentException e) {
                    System.out.println("Unable to get JWT Token");
                } catch (ExpiredJwtException e) {
                    System.out.println("JWT Token has expired");
                } catch (SignatureException signatureException) {
                    System.out.println("JWT token's signature can not be validated");
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Exception during parsing the JWT");
                }
            } else {
                logger.warn("JWT Token does not begin with Bearer String");
            }

            //Once we get the token validate it.
            if (sessionToken != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                User user = sessionService.validateAndGetUserWithAuthoritiesFormSessionToken(sessionToken);
                if (user != null) {
                    UserDetails userDetails = new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), user.getAuthoritySet().stream().map((authority -> {
                        return new SimpleGrantedAuthority(authority.getAuthority());
                    })).collect(Collectors.toList()));


                    // if token is valid configure Spring Security to manually set authentication
                    if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {

                        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                                userDetails, userDetails.getAuthorities(), userDetails.getAuthorities());
                        usernamePasswordAuthenticationToken
                                .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        //usernamePasswordAuthenticationToken.setAuthenticated(true);

                        System.out.println("Auth : " + userDetails.getAuthorities());
                        // After setting the Authentication in the context, we specify
                        // that the current user is authenticated. So it passes the Spring Security Configurations successfully.
                        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);


                    }
                }
            }
            if (false) {
                System.out.println("Will add headers ***********");
                response.addHeader("WWW-Authenticate", "Basic");
                response.addHeader("MyHeader", "Basic");
                chain.doFilter(request, response);// lasa-l numai aici si nu se intampla nimic se bloheaza fluxul aici
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            chain.doFilter(request, response);

        }

    }

}
